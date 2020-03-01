package smartx.publics.datatask;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.vo.DMOConst;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaLogger;

/**
 *@author zzy
 *@date Feb 8, 2012
 *@description 数据库、表、视图等元数据结构分析,剖析结果记录到数据库表pub_dp_xx中
 **/
public class DataExtractService {
	
	private Logger logger = NovaLogger.getLogger(DataExtractService.class);
	
	public static DataExtractService extractService;
	public CommDMO dmo = new CommDMO();
	
	public static String RECODE_DEALFLAG_NOTDEAL = "0";
	public static String RECODE_DEALFLAG_ADD = "1";
	public static String RECODE_DEALFLAG_UPDATE = "2";
	public static String RECODE_DEALFLAG_NOCHANGE = "3";
	
	public static String COLUMN_HASHVALUE = "COLHASHVALUE_XXX";
	
	public DataExtractService(){
		//构造方法
	}
	
	//获取服务实例
	public static DataExtractService getInstance(){
		if(extractService == null)
			extractService = new DataExtractService();
		
		return extractService;
	}
	
	public void dataExtract(String mtCode) throws Exception{
		Document doc = DocumentHelper.parseText(getMetadataContent(mtCode));
		Element root = doc.getRootElement();
		Element datataskE = root.element("datatask").element("data_extract");
		
		Element efrom = datataskE.element("from");
		String fromds = efrom.attributeValue("datasource");
		String fromPkColumn = efrom.attributeValue("pkColumn");
		
		String fromsql = efrom.getText();;

		Element eto = datataskE.element("to");
		String toTable = eto.attributeValue("toTable");
		String tods = eto.attributeValue("datasource");
		String toPkColumn = eto.attributeValue("pkColumn");
		String tosql = eto.getText();

		String[] cols = efrom.attributeValue("cols").split(",");
		String fromColsStr = datataskE.elementText("fromColsName");
		
		int batch = Integer.parseInt(eto.attributeValue("batch"));
		String pkColumntype = "NUMBER(18)";
		
		boolean incrementFlag = false; //true, false 是否增量更新
		try{
			dataIncrementExtract(incrementFlag,fromds,fromsql,fromColsStr,fromPkColumn,tods,tosql,toTable,toPkColumn,cols,batch,pkColumntype);
		}catch (Exception e) {
			logger.error("数据增量抽取错误!", e);
			throw new Exception("数据增量抽取错误!", e);
		}finally{
			dmo.releaseContext(fromds);
			dmo.releaseContext(tods);
		}
		
	}
	
	
	/**
	 * 
	 * @param incrementFlag
	 * @param fromds
	 * @param fromsql
	 * @param fromColsStr
	 * @param fromPkColumn
	 * @param tods
	 * @param tosql
	 * @param toTable
	 * @param toPkColumn
	 * @param colsMap
	 * @param batch
	 * @param pkColumntype
	 * @throws Exception
	 */
	public void dataIncrementExtract(boolean incrementFlag,String fromds,String fromsql,String fromColsStr,String fromPkColumn,
			String tods,String tosql,String toTable,String toPkColumn,String[] colsMap,int batch,String pkColumntype) throws Exception{
		
		String[] fromColsName = fromColsStr.split(",");
		int[] fromcols = new int[colsMap.length];
		
		long oldTime = System.currentTimeMillis();		
		String hashValueColumn = "";
		for (int i = 0; i < colsMap.length; i++) {
			fromcols[i] = Integer.parseInt(colsMap[i]);
			if(hashValueColumn.equals(""))
				hashValueColumn = " dbms_utility.get_hash_value("+fromColsName[i]+",1,1000000000)";
			else{
				hashValueColumn += " + dbms_utility.get_hash_value("+fromColsName[i]+",1,1000000000)";;
			}
		}
		String fullFromsql = "select "+fromColsStr + "," + hashValueColumn +" "+COLUMN_HASHVALUE+" from ("+fromsql+")";
        
        int row=0;
        PreparedStatement insertPs = dmo.getConn(tods).prepareStatement(tosql);//插入数据的stat        
		if (incrementFlag) {
			//应该是先创建表
			String hashTableName = getHashTableName(tods,toTable,toPkColumn,pkColumntype,false);
			String fromHashTable = getFromHashTableName(tods, toTable, toPkColumn,pkColumntype);
			
			int modLength = 5;
			Hashtable<String,Object> threadTable =new Hashtable<String,Object>();
			for(int i=0;i<modLength;i++){
				ExtractHashValueThread thread = new ExtractHashValueThread();
				thread.setFromds(fromds);
				thread.setFromds(fromds);
				thread.setFromsql(fromsql);
				thread.setFromPkColumn(fromPkColumn);
				thread.setTods(tods);
				thread.setFromHashTable(fromHashTable);
				thread.setHashValueColumn(hashValueColumn);
				thread.setToPkColumn(toPkColumn);
				thread.setModValue(i+"");
				thread.setIncrementFlag(true);
				
				thread.start();
				
				threadTable.put(i+"", thread);
			}
			
			boolean isFinished = false;
			while( !isFinished ){
				isFinished = true;
				for(Entry e :  threadTable.entrySet()){
					ExtractHashValueThread thread = (ExtractHashValueThread)e.getValue();
					if( !thread.isFinished){
						isFinished = false;
						break;
					}
				}
				Thread.sleep(150);
			}
			
			HashVO[] vos = dmo.getHashVoArrayByDS(tods, "select count(1) c from " + fromHashTable );
			
			addHashTableIndex(tods, fromHashTable, toPkColumn);
			logger.info("******插入来源表Hash耗时："+ (System.currentTimeMillis()-oldTime)/1000 + "s,共有记录" + vos[0].getStringValue(0));
			
			//以下删除可以分批进行，每次1W条。
			//删除更新的记录 
			int deleteBatch = 10000;
			int deletedRows = deleteBatch;
			String tempSql = "delete "+toTable+" where "+toPkColumn+" in (select t1."+toPkColumn+" from "+fromHashTable+" t1,"+hashTableName+" t2 "
						+" where t2."+toPkColumn+"=t1."+toPkColumn+" and t2.contenthashvalue!=t1.contenthashvalue ) and rownum <= "+deleteBatch;
			
			while(deletedRows >= deleteBatch){
				deletedRows = dmo.executeUpdateByDS(tods, tempSql);
				dmo.commit(tods);
			}
			//删除被删除的记录
			tempSql =  "delete "+toTable+" where "+toPkColumn+" in (select "+toPkColumn+" from "+hashTableName+" t2 "
						+" where not exists(select 1 from "+fromHashTable+" t1 where t1."+toPkColumn+"=t2."+toPkColumn+") ) and rownum <= "+deleteBatch;
			deletedRows = deleteBatch;
			while(deletedRows >= deleteBatch){
				deletedRows = dmo.executeUpdateByDS(tods, tempSql);
				dmo.commit(tods);
			}
			
			logger.info("删除目标表【"+toTable+"】中更新或删除的记录耗时："+ (System.currentTimeMillis()-oldTime)/1000 );
			
			String queryUpdatedHashSql = "select "+toPkColumn+" from "+fromHashTable+" t1 where not exists (select 1 from " + hashTableName
				+" t2 where t2."+toPkColumn+"=t1."+toPkColumn+" and t2.contenthashvalue=t1.contenthashvalue)";
			PreparedStatement updatedPs = dmo.getConn(tods).prepareStatement(queryUpdatedHashSql);
			
			StringBuffer queryUpdatedDataSql = new StringBuffer(fromsql).append(" where ").append(toPkColumn).append(" in ");
			
			StringBuffer pkValues = new StringBuffer();
			int row2 =0 ;
			ResultSet updatedHashRs = updatedPs.executeQuery();
			updatedHashRs.setFetchSize(200);
			while(updatedHashRs.next()){
				row2 ++;
				if(pkValues.length() ==0)
					pkValues = pkValues.append("(").append(updatedHashRs.getString(1));
				else
					pkValues = pkValues.append(",").append(updatedHashRs.getString(1));
				
				//1000条查询一次
				if(row2% 1000 == 0){
					StringBuffer sb = new StringBuffer(queryUpdatedDataSql.toString()).append(pkValues).append(")");
					PreparedStatement ps = dmo.getConn(fromds).prepareStatement(sb.toString());
					ResultSet rs = ps.executeQuery();
					ResultSetMetaData rsmd= rs.getMetaData();
					while(rs.next()){
		     			for(int i=0;i<colsMap.length;i++){
		         			insertPs.setObject(i+1, rs.getObject(fromcols[i]), rsmd.getColumnType(fromcols[i]));            		
		             	}
		         		insertPs.addBatch();
		         		
					}
					pkValues = new StringBuffer();
				}
				if(row2%batch==0) {
             		insertPs.executeBatch();
             		dmo.commit(tods);
             	}
			}
			//不足批次的处理
			if(pkValues.length() > 0){//还存在更新的记录
				StringBuffer sb = new StringBuffer(queryUpdatedDataSql.toString()).append(pkValues).append(")");
				PreparedStatement ps = dmo.getConn(fromds).prepareStatement(sb.toString());
				ResultSet rs = ps.executeQuery();
				ResultSetMetaData rsmd= rs.getMetaData();
				while(rs.next()){
					for(int i=0;i<colsMap.length;i++){
						insertPs.setObject(i+1, rs.getObject(fromcols[i]), rsmd.getColumnType(fromcols[i]));            		
					}
					insertPs.addBatch();
				}
				insertPs.executeBatch();
				dmo.commit(tods);
			}
     		logger.info("更新(新增、更新)记录插入,记录数 "+row2+"条，耗时 " + (System.currentTimeMillis()-oldTime)/1000);
     		
     		String statisticSql = "select (select count(1) from "+fromHashTable+" t1 where not exists (select 1 from "+hashTableName+" t2 where t2."+toPkColumn+"=t1."+toPkColumn+")) inserted,"
     			+ " (select count(1) from "+fromHashTable+" t1,"+hashTableName+" t2 where t2."+toPkColumn+"=t1."+toPkColumn+" and t2.contenthashvalue!=t1.contenthashvalue) updated,"
     			+ " (select count(1) from "+fromHashTable+" t1,"+hashTableName+" t2 where t2."+toPkColumn+"=t1."+toPkColumn+" and t2.contenthashvalue=t1.contenthashvalue) nochanged,"
     			+ " (select count(1) from "+hashTableName+" t2 where not exists (select 1 from "+fromHashTable+" t1 where t2."+toPkColumn+"=t1."+toPkColumn+")) deleted "
     			+ " from dual";
     		HashVO[] tjVos = dmo.getHashVoArrayByDS(tods, statisticSql);
            String logMessage = "";
            int totalCount = 0;
            logMessage += "新增记录 " + tjVos[0].getIntegerValue("inserted").intValue() + " 条。 " ;
            logMessage += "更新记录 " + tjVos[0].getIntegerValue("updated").intValue() + " 条。 " ;
            logMessage += "删除记录 " + tjVos[0].getIntegerValue("deleted").intValue() + " 条。 " ;
            logMessage += "不变的记录 " + tjVos[0].getIntegerValue("nochanged").intValue() + " 条。 " ;
            
            totalCount = tjVos[0].getIntegerValue("inserted").intValue()+tjVos[0].getIntegerValue("nochanged").intValue()
            			+tjVos[0].getIntegerValue("updated").intValue()+tjVos[0].getIntegerValue("deleted").intValue();
            
            //把新的HashMap表，作为现在的MAP表,索引重命名
            this.updateHashValueTable(tods, fromHashTable, hashTableName);
            logger.info("HashValueMap表【"+hashTableName+"】变更成功！");
            
            logMessage = " 数据抽取，总耗时"+ ((System.currentTimeMillis() - oldTime)/1000) +"s,共处理记录 " + totalCount + " 条，其中： " + logMessage;
            logger.info(logMessage);
		}else{
			//全量更新记录
			//清空目标表数据
			String hashTableName = getHashTableName(tods,toTable,toPkColumn,pkColumntype,true);
			dmo.executeUpdateByDS(tods, "truncate table "+toTable);
			dmo.executeUpdateByDS(tods, "call drop_ind_table_proc('"+ toTable +"')");
			
			int modLength = 5;
			Hashtable<String,Object> threadTable =new Hashtable<String,Object>();
			for(int i=0;i<modLength;i++){
				ExtractHashValueThread thread = new ExtractHashValueThread();
				thread.setFromds(fromds);
				thread.setFromds(fromds);
				thread.setFromsql(fromsql);
				thread.setFromPkColumn(fromPkColumn);
				thread.setTods(tods);
				thread.setFromColsStr(fromColsStr);
				thread.setTosql(tosql);
				thread.setHashTableName(hashTableName);
				thread.setFromcols(fromcols);
				thread.setHashValueColumn(hashValueColumn);
				thread.setToPkColumn(toPkColumn);
				thread.setModValue(i+"");
				thread.setIncrementFlag(false);
				
				thread.start();
				
				threadTable.put(i+"", thread);
			}
			
			boolean isFinished = false;
			while( !isFinished ){
				isFinished = true;
				for(Entry e :  threadTable.entrySet()){
					ExtractHashValueThread thread = (ExtractHashValueThread)e.getValue();
					if( !thread.isFinished){
						isFinished = false;
						break;
					}
				}
				Thread.sleep(150);
			}
             
             dmo.executeUpdateByDS(tods, "call recover_ind_table_proc('"+ toTable +"')");
             addHashTableIndex(tods, hashTableName, toPkColumn);
             
             logger.info("数据全量插入,记录数 "+row+"条，耗时 " + (System.currentTimeMillis()-oldTime)/1000);
        }
        
        long ll_dealtime = System.currentTimeMillis() - oldTime;
        logger.info(" 数据抽取，总耗时"+ (ll_dealtime/1000) +"s 。");
	}
	
	public void extractFullValue(String fromds, String fromsql, String fromPkColumn,String fromColsStr,
			int[] fromcols,
			String tods,String tosql,String toPkColumn,
			String hashValueColumn,String hashTableName, String modValue) {
		try {
			String fullFromsql = "select "+fromColsStr + "," + hashValueColumn +" "+COLUMN_HASHVALUE+" from ("+fromsql+") "
			+ " where MOD(dbms_utility.get_hash_value("+fromPkColumn+",1,10000),"+5+") = "+ modValue ;
	        
	        int row=0;
	        PreparedStatement insertPs = dmo.getConn(tods).prepareStatement(tosql);//插入数据的stat   
	        
			// 插入表数据的同时记录Hash值
			String insertHashSql = "insert into " + hashTableName + " (contenthashvalue," + toPkColumn
					+ ") values(?,?)";
			PreparedStatement insertHashPs = dmo.getConn(tods).prepareStatement(insertHashSql);

			PreparedStatement fromps = dmo.getConn(fromds).prepareStatement(fullFromsql);
			ResultSet fromRS = fromps.executeQuery();
			ResultSetMetaData rsmd = fromRS.getMetaData();
			fromRS.setFetchSize(100);
			while (fromRS.next()) {
				row++;
				for (int i = 0; i < fromcols.length; i++) {
					insertPs.setObject(i + 1, fromRS.getObject(fromcols[i]), rsmd.getColumnType(fromcols[i]));
				}
				insertPs.addBatch();

				insertHashPs.setObject(1, fromRS.getString(COLUMN_HASHVALUE));
				insertHashPs.setObject(2, fromRS.getString(fromPkColumn));
				insertHashPs.addBatch();

				if (row % 10000 == 0) {
					insertPs.executeBatch();
					insertHashPs.executeBatch();
					dmo.commit(tods);
				}
			}
			insertPs.executeBatch();
			insertHashPs.executeBatch();
			dmo.commit(tods);
			
			logger.info("全量线程 【"+modValue+"】 ,处理记录数 " + row + "条");
		} catch (Exception e) {
			logger.error("全量抽取数据错误!!!!!!", e);
		}

	}

	public void extractHashValue(String fromds,String fromsql,String fromPkColumn,String tods,String fromHashTable,
			String hashValueColumn,String toPkColumn,String modValue) {
		try {
			int row = 0;

			String selectHashSql = "select " + fromPkColumn + "," + hashValueColumn + " " + COLUMN_HASHVALUE
					+ " from (" + fromsql + ") where MOD(dbms_utility.get_hash_value("+fromPkColumn+",1,10000),"+5+") = "+ modValue ;
			PreparedStatement fromHashPs = dmo.getConn(fromds).prepareStatement(selectHashSql);
			ResultSet fromHashRs = fromHashPs.executeQuery();

			String insertFromHashSql = "insert into " + fromHashTable + "(contenthashvalue," + toPkColumn
					+ ") values(?,?)";
			PreparedStatement insrtFromHashSql = dmo.getConn(tods).prepareStatement(insertFromHashSql);

			// 取出所有的Hash值插入到临时FROMHASH_TEMP1表中
			fromHashRs.setFetchSize(500);
			while (fromHashRs.next()) {
				row++;
				insrtFromHashSql.setObject(1, fromHashRs.getString(COLUMN_HASHVALUE));
				insrtFromHashSql.setObject(2, fromHashRs.getString(fromPkColumn));
				insrtFromHashSql.addBatch();

				if (row % (10000 * 2) == 0) {
					insrtFromHashSql.executeBatch();
					dmo.commit(tods);
				}
			}
			insrtFromHashSql.executeBatch();
			dmo.commit(tods);
			logger.info("增量线程 【"+modValue+"】 ,处理记录数 " + row + "条");
		} catch (Exception e) {
			logger.error("抽取来源表Hash到临时表错误！！！！", e);
		}

	}
	
	class ExtractHashValueThread extends Thread{
		
		String fromds, fromsql, fromPkColumn, tods, fromHashTable, hashValueColumn, toPkColumn;
		String modValue;
		String fromColsStr,tosql,hashTableName;
		int[] fromcols;
		
		boolean isFinished = false;
		boolean incrementFlag = false;
		
		public void run(){
			if(incrementFlag)//全量
				extractHashValue(fromds, fromsql, fromPkColumn, tods, fromHashTable, hashValueColumn, toPkColumn,modValue);
			else
				extractFullValue(fromds, fromsql, fromPkColumn, fromColsStr, fromcols, tods, tosql, toPkColumn, hashValueColumn, hashTableName, modValue);
				
			isFinished = true;
		}
		public void setFromds(String fromds) {
			this.fromds = fromds;
		}
		public void setFromsql(String fromsql) {
			this.fromsql = fromsql;
		}
		public void setFromPkColumn(String fromPkColumn) {
			this.fromPkColumn = fromPkColumn;
		}
		public void setTods(String tods) {
			this.tods = tods;
		}
		public void setFromHashTable(String fromHashTable) {
			this.fromHashTable = fromHashTable;
		}
		public void setHashValueColumn(String hashValueColumn) {
			this.hashValueColumn = hashValueColumn;
		}
		public void setToPkColumn(String toPkColumn) {
			this.toPkColumn = toPkColumn;
		}
		
		public boolean isFinished() {
			return isFinished;
		}
		public void setModValue(String modValue) {
			this.modValue = modValue;
		}
		public void setFromColsStr(String fromColsStr) {
			this.fromColsStr = fromColsStr;
		}
		public void setTosql(String tosql) {
			this.tosql = tosql;
		}
		public void setHashTableName(String hashTableName) {
			this.hashTableName = hashTableName;
		}
		public void setFromcols(int[] fromcols) {
			this.fromcols = fromcols;
		}
		public void setIncrementFlag(boolean incrementFlag) {
			this.incrementFlag = incrementFlag;
		}
	}
	
	public void updateHashValueTable(String tods,String fromHashTable,String hashTableName) throws Exception{
		List<String> sqls = new ArrayList<String>();
		sqls.add("DROP TABLE "+ hashTableName);
        sqls.add( "ALTER TABLE "+fromHashTable+" RENAME TO "+hashTableName);
        sqls.add( "ALTER INDEX I1"+fromHashTable+" RENAME TO I1"+hashTableName+"");
        sqls.add( "ALTER INDEX I2"+fromHashTable+" RENAME TO I2"+hashTableName+"");
        
        dmo.executeBatchByDS(tods, sqls);
	}
	
	public String getFromHashTableName(String tods,String toTable,String toPkColumn,String pkColumntype) throws Exception{
		String mapTableName = "HT_" + toTable.toUpperCase();
		if(mapTableName.length() > 25)
			mapTableName = mapTableName.substring(0, 25);
		
		dropTable(tods,mapTableName);//先删除
		String buildTableSql = "create table " + mapTableName + "( contenthashvalue varchar2(127)," + toPkColumn + " " + pkColumntype + ")";
		
		dmo.executeUpdateByDS(tods, buildTableSql);
		logger.info("创建源数据的HashValueMap临时表【"+mapTableName+"】成功");
		
		return mapTableName;
	}
	
	public String getHashTableName(String tods,String toTable,String toPkColumn,String pkColumntype,boolean rebuild) throws Exception{
		//步骤 1，判断表是否存在hashmap
		//2, 创建出临时表 hashmap_temp(不加索引)
		
		String mapTableName = "H_" + toTable.toUpperCase();
		if(mapTableName.length() > 25)
			mapTableName = mapTableName.substring(0, 25);
		
		if(rebuild == false)
			return mapTableName;
		
		dropTable(tods, mapTableName);
		
		String buildTableSql = "create table " + mapTableName + "( contenthashvalue varchar2(127)," + toPkColumn + " " + pkColumntype + ")";
		dmo.executeUpdateByDS(tods, buildTableSql);
		logger.info("创建源数据的HashValueMap表【"+mapTableName+"】成功");
		
		return mapTableName;
	}
	
	private void dropTable(String ds,String tableName) throws Exception{
		String sql = "select 1 from user_tables t where table_name = ?";
		HashVO[] vos = dmo.getHashVoArrayByDS(ds, sql, tableName);
		if(vos.length > 0){//表存在
			sql = "drop table " + tableName;
			dmo.executeUpdateByDS(ds, sql);
		}
	}
	
	private void addHashTableIndex(String tods,String hashTableName,String pkColumn) throws Exception{
		List<String> sqls = new ArrayList<String>();
		sqls.add("create index " + "I1" + hashTableName + " on " + hashTableName + " (CONTENTHASHVALUE)");
		sqls.add("create index " + "I2" + hashTableName + " on " + hashTableName + " ("+pkColumn+")");
		
		dmo.executeBatchByDS(tods, sqls);
		logger.info("对HashValueMap表【"+hashTableName+"】添加索引成功！");
	}
	
	public String getMetadataContent(String mtCode) throws Exception{
		String content = null;
		CommDMO dmo = new CommDMO();
		String sql = "select content from pub_metadata_templet where code=?";
		HashVO[] vos = dmo.getHashVoArrayByDSUnlimitRows(DMOConst.DS_DEFAULT, sql,mtCode);
		if(vos.length>0){
			content = vos[0].getStringValue("content");
		}else{
			throw new Exception("未找到比对实体["+mtCode+"]的元数据定义！");
		}
		
		return content;
	}
	
	/**
	 * 数据增量更新方法，效果较差，需要频繁的query,update MapHash表
	 * @param mtCode
	 * @throws Exception
	 */
	public void dataExtract2NoUse(String mtCode) throws Exception{
		//1, 数据库信息
		
		Document doc = DocumentHelper.parseText(getMetadataContent(mtCode));
		Element root = doc.getRootElement();
		Element datataskE = root.element("datatask").element("data_extract");
		
		Element efrom = datataskE.element("from");
		String fromds = efrom.attributeValue("datasource");
		String fromPkColumn = efrom.attributeValue("pkColumn");
		
		String fromsql = efrom.getText();;

		Element eto = datataskE.element("to");
		String toTable = eto.attributeValue("toTable");
		String tods = eto.attributeValue("datasource");
		String toPkColumn = eto.attributeValue("pkColumn");
		String tosql = eto.getText();

		String[] cols = efrom.attributeValue("cols").split(",");
		String fromColsStr = datataskE.elementText("fromColsName");
		String[] fromColsName = fromColsStr.split(",");
		int[] fromcols = new int[cols.length];
		
		String hashValueColumn = "";
		for (int i = 0; i < cols.length; i++) {
			fromcols[i] = Integer.parseInt(cols[i]);
			if(hashValueColumn.equals(""))
				hashValueColumn = " dbms_utility.get_hash_value("+fromColsName[i]+",1,1000000000)";
			else{
				hashValueColumn += " + dbms_utility.get_hash_value("+fromColsName[i]+",1,1000000000)";;
			}
		}
		long ll_1 = System.currentTimeMillis();
		fromsql = "select "+fromColsStr + "," + hashValueColumn +" colhashvalue_xxx from ("+fromsql+")";
		
		//String selectHashSql = "select "+fromColsStr + "," + hashValueColumn +" colhashvalue_xxx from ("+fromsql+")";
		
		String hashTableName = getHashTableName(tods,toTable,toPkColumn,"NUMBER(18)",false);
		
		PreparedStatement fromps = dmo.getConn(fromds).prepareStatement(fromsql); 
        ResultSet fromRS=fromps.executeQuery();
        ResultSetMetaData rsmd= fromRS.getMetaData();
        //这里只一个主键，可能考虑有多个主键的情况
        String queryHashSql = "select hashmap_idxxx,contenthashvalue,dealflag, "+toPkColumn+" from "+hashTableName+" where " +toPkColumn + "=?";
        PreparedStatement queryHashPs = dmo.getConn(tods).prepareStatement(queryHashSql);
       
        PreparedStatement insertPs = dmo.getConn(tods).prepareStatement(tosql);
        
        String insertHashSql = "insert into " + hashTableName +" (updatetime,hashmap_idxxx,dealflag,contenthashvalue,"+toPkColumn
        					+") values(sysdate,s_hashmap_idxxx.nextval,1,?,?)";
        PreparedStatement insertHashPs = dmo.getConn(tods).prepareStatement(insertHashSql);
        
        String updateHashSql = "update " + hashTableName +" set updatetime=sysdate,dealflag=?,contenthashvalue=? where hashmap_idxxx=?";
        PreparedStatement updateHashPs = dmo.getConn(tods).prepareStatement(updateHashSql);
        
        String delOldSql = "delete " + toTable + " where " + toPkColumn + "=?";
        PreparedStatement delOldPs = dmo.getConn(tods).prepareStatement(delOldSql);
        
        int batch = 10000;
        long row=0;
        while(fromRS.next()){
        	row++;
        	//System.out.println(row);
        	String fromPkValue = fromRS.getString(fromPkColumn);
        	String hashvalue = fromRS.getString("colhashvalue_xxx");
        	
        	queryHashPs.setObject(1, fromPkValue);
        	ResultSet mapRS = queryHashPs.executeQuery();
        	if(mapRS.next()){//存在该记录
        		String oldHashValue = mapRS.getString("contenthashvalue");
        		String hashmap_idxxx = mapRS.getString("hashmap_idxxx");
        		if(hashvalue.equals(oldHashValue)){//记录数据不变
        			updateHashPs.setObject(1, RECODE_DEALFLAG_NOCHANGE);
        			updateHashPs.setObject(2, hashvalue);
        			updateHashPs.setObject(3, hashmap_idxxx);
        			updateHashPs.addBatch();
        		}else{
        			//记录有更新,先删除原有记录再插入
        			delOldPs.setObject(1,fromPkValue);
        			delOldPs.addBatch();
        			
        			updateHashPs.setObject(1, RECODE_DEALFLAG_UPDATE);
        			updateHashPs.setObject(2, hashvalue);
        			updateHashPs.setObject(3, hashmap_idxxx);
        			updateHashPs.addBatch();
        			
        			for(int i=0;i<cols.length;i++){
            			insertPs.setObject(i+1, fromRS.getObject(fromcols[i]), rsmd.getColumnType(fromcols[i]));            		
                	}
            		insertPs.addBatch();
        		}
        	}else{//新增的记录
        		for(int i=0;i<cols.length;i++){
        			insertPs.setObject(i+1, fromRS.getObject(fromcols[i]), rsmd.getColumnType(fromcols[i]));            		
            	}
        		insertPs.addBatch();
        		
        		insertHashPs.setObject(1, hashvalue);
        		insertHashPs.setObject(2, fromPkValue);
        		insertHashPs.addBatch();
        	}
        	
        	if(row%batch==0) {
        		delOldPs.executeBatch();
        		updateHashPs.executeBatch();
        		insertPs.executeBatch();
        		insertHashPs.executeBatch();
        		dmo.commit(tods);
        	}
        }
        System.out.println(row);
        delOldPs.executeBatch();
        updateHashPs.executeBatch();
        insertPs.executeBatch();
        insertHashPs.executeBatch();
        dmo.commit(tods);
        
        logger.info("扫描一遍，耗时 " + (System.currentTimeMillis()-ll_1)/1000);
        
        HashVO[] tjVos = dmo.getHashVoArrayByDS(tods, "select dealflag, count(1) c from DQC_NM_REGION_HASH t group by t.dealflag");
        String logMessage = "";
        int totalCount = 0;
        for(int i=0;i<tjVos.length;i++){
        	String dealFlag = tjVos[i].getStringValue("dealflag");
        	int flagCount = tjVos[i].getIntegerValue("c").intValue();
        	if(RECODE_DEALFLAG_ADD.equals(dealFlag) ){
        		logMessage += "新增记录 " + flagCount + " 条。 " ;
        	}else if(RECODE_DEALFLAG_UPDATE.equals(dealFlag) ){
        		logMessage += "更新记录 " + flagCount + " 条。 " ;
        	}if(RECODE_DEALFLAG_NOTDEAL.equals(dealFlag) ){
        		logMessage += "删除记录 " + flagCount + " 条。 " ;
        	}if(RECODE_DEALFLAG_NOCHANGE.equals(dealFlag) ){
        		logMessage += "不变的记录 " + flagCount + " 条。 " ;
        	}
        	totalCount = totalCount + flagCount;
        }
        
        //处理删除的记录,状态标记为未处理，即为删除的数据
        String delDeletedSql = "delete "+toTable+" t1 where exists (select 1 from "+hashTableName+" t2 where t2.dealflag=0 and t2."+toPkColumn+"=t1."+toPkColumn+")";
        String delHashSql = "delete dqc_nm_region_hash where dealflag = 0";
        dmo.executeBatchByDS(tods, new String[]{delDeletedSql,delHashSql});
        dmo.commit(tods);
        
        long ll_2 = System.currentTimeMillis();
        long ll_dealtime = ll_2 - ll_1;
        logMessage = " 耗时"+ (ll_dealtime/1000) +"s,共处理记录 " + totalCount + " 条，其中： " + logMessage;
        logger.info(logMessage);
        
        //更新hashmap表的状态为未处理状态
        dmo.executeUpdateByDS(tods, "update DQC_NM_REGION_HASH set dealflag = 0");
        dmo.commit(tods);
	}
	
}
