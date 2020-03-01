package smartx.publics.datatask.cluster;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.vo.NovaLogger;
import smartx.publics.clustercompute.vo.ClusterComputeSubTaskExecutor;

/**
 *@author zzy
 *@date Jul 17, 2012
 **/
public class DataExtractClusterSubTaskExecutor implements ClusterComputeSubTaskExecutor{

	private static final long serialVersionUID = 7452605778005302181L;
	private Logger logger = NovaLogger.getLogger(this.getClass());
	private CommDMO dmo = new CommDMO();
	private DataExtractUtil extractUtil = new DataExtractUtil();
	
	private Map<String,Object> execResult = new HashMap<String, Object>();//节点任务执行结果
	
	public static String COLUMN_HASHVALUE = "COLHASHVALUE_XXX";//hash值字段的rename
	
	private String fromds, fromPkColumn, fromsql,fromColsStr;
	private String tods,toPkColumn, tosql,toTable,toPkColumnType;
	private int[] fromcols;
	private boolean isCreateHashTable = false;//是否生成主键值的MAP表
	private boolean incrementFlag;//是否是增量抽取
	private boolean modFlag = false;//是否把数据分批处理
	
	private String[] fromPkCols,toPkCols,toPkColTypes; //必须考虑到多个列构成联合主键的情况
	private String hashValueColumn = "";
	private String hashPkColumn;
	
	private int module;//取模的大小
	private int modValue; //当前模值
	
	@Override
	public Map<String, ?> execute(Map<String, ?> params) throws Exception {
		module = (Integer)params.get(DataExtractClusterTask.PARAM_MODULE);
		modValue = (Integer)params.get(DataExtractClusterTask.PARAM_MODVALUE);
		fromds = (String) params.get(DataExtractClusterTask.PARAM_FROMDS);
		fromPkColumn = (String) params.get(DataExtractClusterTask.PARAM_FROMPKCOLUMN);
		fromsql = (String) params.get(DataExtractClusterTask.PARAM_FROMSQL);
		fromColsStr = (String) params.get(DataExtractClusterTask.PARAM_FROMCOLSSTR);
		fromcols = (int[]) params.get(DataExtractClusterTask.PARAM_FROMCOLS);
		tods = (String) params.get(DataExtractClusterTask.PARAM_TODS);
		toPkColumn = (String) params.get(DataExtractClusterTask.PARAM_TOPKCOLUMN);
		toPkColumnType = (String)params.get(DataExtractClusterTask.PARAM_TOPKCOLUMNTYPE);
		toTable = (String) params.get(DataExtractClusterTask.PARAM_TOTABLE);
		tosql = (String) params.get(DataExtractClusterTask.PARAM_TOSQL);
		incrementFlag = (Boolean)params.get(DataExtractClusterTask.PARAM_INCREMENTFLAG);
		isCreateHashTable = (Boolean)params.get(DataExtractClusterTask.PARAM_ISCREATEHASHTABLE);
		modFlag = (Boolean)params.get(DataExtractClusterTask.PARAM_MODFLAG);
		
		toPkCols = toPkColumn.split(",");
		toPkColTypes = toPkColumnType.split(",");
		fromPkCols = fromPkColumn.split(",");
		hashPkColumn = fromPkCols[0]; //如果有多個主鍵字段,則只取第一個
		
		String[] fromColsName = fromColsStr.split(",");
		
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < fromcols.length; i++) {
			if(sb.length()<1)
				sb.append("dbms_utility.get_hash_value("+fromColsName[i]+",1,1000000000)");
			else{
				sb.append(" + dbms_utility.get_hash_value("+fromColsName[i]+",1,1000000000)");
			}
		}
		hashValueColumn = sb.toString();
		//hashValueColumn = "dbms_utility.get_hash_value("+sb.toString()+",1,1000000000)";
		
		//数据抽取
		if(incrementFlag){
			extractFullHashValue();//增量抽取，这里只抽取hash值
		}else{
			extractFullValue();
		}
		
		return execResult;
	}
	
	public void extractFullValue() throws Exception{
		int row = 0;
		PreparedStatement insertHashPs = null;
		try {
			String fullFromsql = null;
			
			logger.info("全量抽取进程 【" + modValue + "】到表【"+toTable+"】 执行开始...");			
			if(isCreateHashTable){
				String hashTableName = extractUtil.getHashTableName(toTable);
				
				fullFromsql = "select "+fromColsStr + "," + hashValueColumn +" "+COLUMN_HASHVALUE+" from ("+fromsql+") ";
				if(modFlag)
					fullFromsql = fullFromsql + " where MOD(dbms_utility.get_hash_value("+hashPkColumn+",1,1000),"+module+") = " + modValue;
				
				// 插入表数据的同时记录Hash值
				String insertHashSql = "insert into " + hashTableName + " (contenthashvalue," + toPkColumn + ") values(?";
				for(int i=0;i<toPkCols.length;i++){
					insertHashSql += ",?";
				}
				insertHashSql += ")";
				
				insertHashPs = dmo.getConn(tods).prepareStatement(insertHashSql);
			}else{
				fullFromsql = "select " + fromColsStr + " from (" + fromsql + ") " ;
				if(modFlag)
					fullFromsql = fullFromsql + " where MOD(dbms_utility.get_hash_value("+hashPkColumn+",1,1000)," +module+") = " + modValue;
			}
			
			PreparedStatement insertPs = dmo.getConn(tods).prepareStatement(tosql);// 插入数据的stat
			PreparedStatement fromps = dmo.getConn(fromds).prepareStatement(fullFromsql);
			
			logger.debug("查询源表SQL语句为：" + fullFromsql);
			ResultSet fromRS = fromps.executeQuery();
			ResultSetMetaData rsmd = fromRS.getMetaData();
			fromRS.setFetchSize(500);
			while (fromRS.next()) {
				row++;
				for (int i = 0; i < fromcols.length; i++) {
					insertPs.setObject(i + 1, fromRS.getObject(fromcols[i]), rsmd.getColumnType(fromcols[i]));
				}
				insertPs.addBatch();
				if(isCreateHashTable){
					insertHashPs.setObject(1, fromRS.getString(COLUMN_HASHVALUE));
					for(int n=0;n<toPkCols.length;n++){
						insertHashPs.setObject(n+2, fromRS.getString(fromPkCols[n]));
					}
					insertHashPs.addBatch();
				}
				//每5000条提交一次
				if (row % 5000 == 0) {
					insertPs.executeBatch();
					if(isCreateHashTable){
						insertHashPs.executeBatch();
					}
					dmo.commit(tods);
				}
			}
			insertPs.executeBatch();
			if(isCreateHashTable){
				insertHashPs.executeBatch();
			}
			dmo.commit(tods);			
			logger.info("全量抽取进程,【" + modValue + "】 执行结束,处理记录数【 " + row + "】条");
			try{
				fromRS.close();
				insertHashPs.close();
				insertPs.close();
				fromps.close();
			}catch (Exception e) {
				logger.error("数据库cursor释放异常");
			}
			
			execResult.put(DataExtractClusterTask.KEY_DEALEDROWS, row);
			
		} catch (Exception e) {
			String msg = "全量抽取进程【" + modValue + "】执行异常!!!!!! \n" + e.getMessage();
			logger.info(msg);
			execResult.put(DataExtractClusterTask.KEY_EXCEPTIONMSG, msg);
			row = -1;
			throw e;
		}finally{
			dmo.releaseContext(tods);
			dmo.releaseContext(fromds);
		}
	}
	
	public void extractFullHashValue() throws Exception{
		try{
			logger.info("增量抽取进程 【" + modValue + "】到表【"+toTable+"】 执行开始...");
			int row = 0;
			String tempHashTable = extractUtil.getTempHashTableName(toTable);
			String selectHashSql = "select " + fromPkColumn + "," + hashValueColumn + " " + COLUMN_HASHVALUE
					+ " from (" + fromsql + ") ";
			
			if(modFlag)
				selectHashSql = selectHashSql + " where MOD(dbms_utility.get_hash_value("+hashPkColumn+",1,1000),"+module+") = "+ modValue ;
			
			logger.debug("查询Hash值的SQL语句为：" + selectHashSql);
			PreparedStatement fromHashPs = dmo.getConn(fromds).prepareStatement(selectHashSql);

			String insertTempHashSql = "insert into " + tempHashTable + "(contenthashvalue," + toPkColumn + ") values(?";
			for(int i=0;i<toPkCols.length;i++){
				insertTempHashSql += ",?";
			}
			insertTempHashSql += ")";
			PreparedStatement insrtTempHashSql = dmo.getConn(tods).prepareStatement(insertTempHashSql);
			
			// 取出所有的Hash值插入到临时FROMHASH_TEMP1表中
			ResultSet fromHashRs = fromHashPs.executeQuery();
			fromHashRs.setFetchSize(500);
			while (fromHashRs.next()) {
				row++;
				insrtTempHashSql.setObject(1, fromHashRs.getString(COLUMN_HASHVALUE));
				for(int n=0;n<toPkCols.length;n++){
					insrtTempHashSql.setObject(n+2, fromHashRs.getString(fromPkCols[n]));
				}
				insrtTempHashSql.addBatch();

				if (row % (15000) == 0) {
					insrtTempHashSql.executeBatch();
					dmo.commit(tods);
				}
			}
			insrtTempHashSql.executeBatch();
			dmo.commit(tods);
			logger.info("增量抽取进程 【"+modValue+"】 执行结束,处理记录数 " + row + "条");
			
			try{
				fromHashRs.close();
				fromHashPs.close();
				insrtTempHashSql.close();
			}catch (Exception e) {
				logger.error("数据库cursor释放异常");
			}
			
			execResult.put(DataExtractClusterTask.KEY_DEALEDROWS, row);
			
		}catch (Exception e) {
			String msg = "增量抽取进程【" + modValue + "】执行异常!!!!!! \n" + e.getMessage();
			logger.info(msg);
			execResult.put(DataExtractClusterTask.KEY_EXCEPTIONMSG, msg);
			
			throw e;
		}finally{
			dmo.releaseContext(tods);
			dmo.releaseContext(fromds);
		}
	}
	
}
