package smartx.publics.dataprofile;

import java.sql.DatabaseMetaData;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.bs.NovaDBConnection;
import smartx.framework.common.vo.DMOConst;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaLogger;

/**
 *@author zzy
 *@date Feb 8, 2012
 *@description 数据库、表、视图等元数据结构分析,剖析结果记录到数据库表pub_dp_xx中
 **/
public class StructureProfileService {
	
	private static Logger logger = NovaLogger.getLogger(StructureProfileService.class);
	
	public static StructureProfileService profileService;
	public static CommDMO dmo = new CommDMO();
	
	public StructureProfileService(){
		//构造方法
	}
	
	//获取服务实例
	public static StructureProfileService getInstance(){
		if(profileService == null)
			profileService = new StructureProfileService();
		
		return profileService;
	}
	
	
	/**
	 * 数据库模型剖析
	 * @param dsName 数据源名称
	 * @param profileDate 剖析日期 YYYYMMDD
	 */
	public void dbSchemaProfile(String dsName,String profileDate) throws Exception{
		//1, 数据库信息
		NovaDBConnection novaConn = dmo.getConn(dsName);
		DatabaseMetaData dbMeta = novaConn.getMetaData();
		
		//删除同期剖析结果
		deleteSametermResult(dsName,profileDate);
		
		String schemaId = dmo.getSequenceNextValByDS(DMOConst.DS_DEFAULT, "S_PUB_DP_SCHEMA");
		String addSchemmaSql = "insert into pub_dp_schema(id,analysedate,datasourcename,url,dbproductname,dbproductversion,analyseresult,analysetime)"
			+ " values(?,?,?,?,?,?,?,sysdate)";
		
		String dbVersion =  dbMeta.getDatabaseProductVersion().substring(0, dbMeta.getDatabaseProductVersion().indexOf("- "));
		dmo.executeUpdateByDS(DMOConst.DS_DEFAULT, addSchemmaSql, schemaId,profileDate,dsName,dbMeta.getURL(),dbMeta.getDatabaseProductName()
				,dbVersion,"分析中...");
		dmo.commit(DMOConst.DS_DEFAULT);
		
		//2, 表\列分析
		tableProfile(schemaId,dsName, profileDate);	
		columnProfile(schemaId, dsName, profileDate);
		//3,视图分析
		viewProfile(schemaId, dsName, profileDate);
		//4,索引分析
		indexProfile(schemaId, dsName, profileDate);
		//5,序列号分析
		sequenceProfile(schemaId, dsName, profileDate);
		
		//更新模型和表剖析的统计字段
		String updateSql1 = "update pub_dp_schema s set s.analyseresult='剖析成功',"
			+" s.records_count=(select sum(t.records_count) from pub_dp_table t where t.schemaid=s.id),"
			+" s.tables_count=(select count(1) from pub_dp_table t where t.schemaid=s.id),"
			+" s.views_count=(select count(1) from pub_dp_view t where t.schemaid=s.id),"
			+" s.indexs_count=(select count(1) from pub_dp_index t where t.schemaid=s.id)"     
			+" where s.id = ?";
		String updateSql2 = "update pub_dp_schema s set s.recordspertable_count = round(records_count/tables_count,2) where s.id=?";
		
		String updateTabSql = "update pub_dp_table t set t.indexs_count=(select count(1) from pub_dp_index d where d.schemaid=t.schemaid and d.tablename=t.name),"
			+" t.columns_count=(select count(1) from pub_dp_column c where c.schemaid=t.schemaid and c.tablename=t.name)"
			+" where t.schemaid=?";
		
		dmo.executeUpdateByDS(DMOConst.DS_DEFAULT,updateSql1,schemaId);
		dmo.executeUpdateByDS(DMOConst.DS_DEFAULT,updateSql2,schemaId);
		dmo.executeUpdateByDS(DMOConst.DS_DEFAULT,updateTabSql,schemaId);
		dmo.commit(DMOConst.DS_DEFAULT);
		logger.info("在数据源【"+dsName+"】上进行数据剖析成功！剖析批次【"+profileDate+"】");
		dmo.releaseContext(DMOConst.DS_DEFAULT);
		dmo.releaseContext(dsName);
	}
	
	//删除同期剖析结果
	public void deleteSametermResult(String dsName,String profileDate) throws Exception {
		String querySchema = "select id from pub_dp_schema where analysedate = ? and datasourcename=?";
		HashVO[] results = dmo.getHashVoArrayByDS(DMOConst.DS_DEFAULT, querySchema, profileDate,dsName);
		
		if(results.length > 0){
			String oldSchemaId = results[0].getStringValue("ID");//上次分析记录ID
			ArrayList<String> sqlList = new ArrayList<String>();
			sqlList.add("delete pub_dp_index where schemaid = " + oldSchemaId); 
			sqlList.add("delete pub_dp_sequence where schemaid = " + oldSchemaId);
			sqlList.add("delete pub_dp_column where schemaid = " + oldSchemaId);
			sqlList.add("delete pub_dp_table where schemaid = " + oldSchemaId);
			sqlList.add("delete pub_dp_view where schemaid = " + oldSchemaId);
			sqlList.add("delete pub_dp_schema where id = " + oldSchemaId);
			
			dmo.executeBatchByDS(DMOConst.DS_DEFAULT, sqlList);
			dmo.commit(DMOConst.DS_DEFAULT);
			logger.info("同批次【"+profileDate+"】的原有剖析结果数据删除成功。");
		}
	}
	
	/**
	 * 判断是否存在同期剖析结果
	 */
	public boolean isExistsSametermResult(String dsName,String profileDate) throws Exception {
		String querySchema = "select id from pub_dp_schema where analysedate = ? and datasourcename=?";
		HashVO[] results = dmo.getHashVoArrayByDS(DMOConst.DS_DEFAULT, querySchema, profileDate,dsName);
		
		if(results.length > 0){
			logger.info("在数据源【"+dsName+"】上存在同批次【"+profileDate+"】的数据模型剖析结果。");
			return true;
		}else{
			return false;
		}
	}
	
	public void tableProfile(String schemaId,String dsName,String profileDate) throws Exception{
		String queryTabSql = "SELECT T.TABLE_NAME,TC.TABLE_TYPE,TC.COMMENTS,T.NUM_ROWS,T.TABLESPACE_NAME FROM USER_TABLES T,USER_TAB_COMMENTS TC "
				+ "  WHERE TC.TABLE_NAME=T.TABLE_NAME";
		String insertTabSql = "insert into pub_dp_table(id,schemaid,name,records_count,comments,analysetime)"
				+ " values(s_pub_dp_table.nextval,?,?,?,?,sysdate)";
		HashVO[] tables = dmo.getHashVoArrayByDS(dsName, queryTabSql);
		for(int i=0;i<tables.length;i++){
			dmo.executeUpdateByDS(DMOConst.DS_DEFAULT, insertTabSql, schemaId,tables[i].getStringValue("TABLE_NAME")
					,tables[i].getStringValue("NUM_ROWS"),tables[i].getStringValue("COMMENTS"));
		}
		
		dmo.commit(DMOConst.DS_DEFAULT);
		logger.info("所有表剖析完成！");
	}
	
	//列分析
	public void columnProfile(String schemaId,String dsName,String profileDate) throws Exception{
		String queryColSql = "SELECT C.TABLE_NAME,C.COLUMN_NAME,CC.COMMENTS,C.DATA_TYPE,C.DATA_LENGTH,C.DATA_DEFAULT,C.NULLABLE "
			+" FROM USER_TAB_COLS C,USER_COL_COMMENTS CC,USER_TABLES T "
			+" WHERE CC.TABLE_NAME=C.TABLE_NAME AND CC.COLUMN_NAME=C.COLUMN_NAME AND C.TABLE_NAME=T.TABLE_NAME";
		String insertColSql = "INSERT INTO PUB_DP_COLUMN(ID,SCHEMAID,TABLEID,TABLENAME,NAME,COMMENTS,TYPE,NULLABLE,DEFAULTVALUE,LENGTH,ANALYSETIME)"
			+ " VALUES(S_PUB_DP_COLUMN.NEXTVAL,?,(SELECT ID FROM PUB_DP_TABLE WHERE SCHEMAID=? AND NAME=? AND ROWNUM=1),?,?,?,?,?,?,?,SYSDATE)";
		HashVO[] columns = dmo.getHashVoArrayByDSUnlimitRows(dsName, queryColSql);
		
		for(int i=0;i<columns.length;i++){
			HashVO col = columns[i];
			dmo.executeUpdateByDS(DMOConst.DS_DEFAULT, insertColSql,schemaId,schemaId,col.getStringValue("TABLE_NAME"),col.getStringValue("TABLE_NAME")
					,col.getStringValue("COLUMN_NAME"),col.getStringValue("COMMENTS"),col.getStringValue("DATA_TYPE"),col.getStringValue("NULLABLE")
					,col.getStringValue("DATA_DEFAULT"),col.getStringValue("DATA_LENGTH"));
		}
		dmo.commit(DMOConst.DS_DEFAULT);
		logger.info("所有列分析完成！");
	}
	
	//视图分析
	public void viewProfile(String schemaId,String dsName,String profileDate) throws Exception{
		String queryViewSql = "select v.view_name,v.text_length,v.text from user_views v";
		String insertViewSql = "insert into pub_dp_view(id,schemaid,name,textlength,viewdefinition,analysetime) "
			+" values(s_pub_dp_view.nextval,?,?,?,?,sysdate)";
		HashVO[] views = dmo.getHashVoArrayByDSUnlimitRows(dsName, queryViewSql);
		
		for(int i=0;i<views.length;i++){
			HashVO v = views[i];
			//TODO 计算视图中能查到的记录数是一件很危险的事情，转为视图中存在数据量很大且不走索引的情况
			//String countSql = "SELECT COUNT(1) RECORDSCOUNT FROM " + v.getStringValue("VIEW_NAME");
			//HashVO[] tempVos = dmo.getHashVoArrayByDS(dsName, countSql);
			//String recordCount = tempVos[0].getStringValue("RECORDSCOUNT");
			
			
			dmo.executeUpdateByDS(DMOConst.DS_DEFAULT, insertViewSql,schemaId,v.getStringValue("VIEW_NAME")
					,v.getStringValue("TEXT_LENGTH"),v.getStringValue("TEXT"));
		}
		dmo.commit(DMOConst.DS_DEFAULT);
		logger.info("所有列分析完成！");
	}
	
	//索引分析
	public void indexProfile(String schemaId,String dsName,String profileDate) throws Exception{
		String queryIdxSql = "SELECT INDEX_NAME,INDEX_TYPE,TABLE_NAME,UNIQUENESS,JOIN_INDEX FROM USER_INDEXES T";
		String insertIdxSql = "insert into pub_dp_index(id,schemaid,name,type,tablename,columns,uniqueness,isjoinindex,tableid,analysetime)"
			+ " values(s_pub_dp_index.nextval,?,?,?,?,?,?,?,(SELECT ID FROM PUB_DP_TABLE WHERE SCHEMAID=? AND NAME=? AND ROWNUM=1),sysdate)";
		
		String qryIdxColsSql = "select COLUMN_NAME from user_ind_columns where index_name = ?";
		HashVO[] indexes = dmo.getHashVoArrayByDSUnlimitRows(dsName, queryIdxSql);
		
		for(int i=0;i<indexes.length;i++){
			HashVO idx = indexes[i];
			//查询索引上对应的列
			String idxColsStr = "";
			HashVO[] cols = dmo.getHashVoArrayByDS(dsName, qryIdxColsSql, idx.getStringValue("INDEX_NAME"));
			for(int j=0;j<cols.length;j++){
				idxColsStr = idxColsStr + cols[j].getStringValue("COLUMN_NAME")+",";
			}
			if(idxColsStr.length() > 0)
				idxColsStr = idxColsStr.substring(0, idxColsStr.length()-1);
			
			dmo.executeUpdateByDS(DMOConst.DS_DEFAULT, insertIdxSql,schemaId,idx.getStringValue("INDEX_NAME")
					,idx.getStringValue("INDEX_TYPE"),idx.getStringValue("TABLE_NAME"),idxColsStr
					,idx.getStringValue("UNIQUENESS"),idx.getStringValue("JOIN_INDEX"),schemaId,idx.getStringValue("TABLE_NAME"));
		}
		dmo.commit(DMOConst.DS_DEFAULT);
		logger.info("所有索引分析完成！");
	}
	
	//序列号分析
	public void sequenceProfile(String schemaId,String dsName,String profileDate) throws Exception{
		String queryColSql = "SELECT SEQUENCE_NAME,MIN_VALUE,MAX_VALUE,INCREMENT_BY,LAST_NUMBER FROM USER_SEQUENCES T ";
		String insertColSql = "insert into pub_dp_sequence(id,schemaid,name,lastnumber,minvalue,maxvalue,incrementby,analysetime)"
			+ "  values(s_pub_dp_sequence.nextval,?,?,?,?,?,?,sysdate)";
		HashVO[] seqs = dmo.getHashVoArrayByDSUnlimitRows(dsName, queryColSql);
		
		for(int i=0;i<seqs.length;i++){
			HashVO s = seqs[i];
			dmo.executeUpdateByDS(DMOConst.DS_DEFAULT, insertColSql,schemaId,s.getStringValue("SEQUENCE_NAME"),s.getStringValue("LAST_NUMBER")
					,s.getStringValue("MIN_VALUE"),s.getStringValue("MAX_VALUE"),s.getStringValue("INCREMENT_BY"));
		}
		dmo.commit(DMOConst.DS_DEFAULT);
		logger.info("所有序列号分析完成！");
	}

}
