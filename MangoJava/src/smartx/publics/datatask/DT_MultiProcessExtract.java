package smartx.publics.datatask;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Element;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaLogger;
import smartx.publics.clustercompute.ClusterComputeManager;
import smartx.publics.clustercompute.ClusterComputeTaskListener;
import smartx.publics.clustercompute.vo.ClusterComputeConst;
import smartx.publics.clustercompute.vo.ClusterComputeTaskResult;
import smartx.publics.datatask.cluster.DataExtractClusterTask;
import smartx.publics.datatask.cluster.DataExtractUtil;

/**
 * @author zzy
 * @date Jul 12, 2012
 * @description 多进行数据抽取任务实现类
 **/
public class DT_MultiProcessExtract implements DataTaskExecuteIFC {

	private Logger logger = NovaLogger.getLogger(this.getClass());
	private CommDMO dmo = new CommDMO();
	private DataExtractUtil extractUtil = new DataExtractUtil();
	
	private DataTaskExecThread mainThread;
	
	boolean isExtractFinished = false;
	boolean isExecOk = false;
	Map<String, ?> extractResultMap;
	
	@Override
	public void dataTaskExec(Element task, DataTaskExecThread mainThread) throws Exception {
		this.mainThread = mainThread;
		Element efrom = task.element("from");
		String fromds = efrom.attributeValue("datasource");
		String fromPkColumn = efrom.attributeValue("pkColumn");

		String fromsql = efrom.getText();

		Element eto = task.element("to");
		String toTable = eto.attributeValue("toTable");
		String tods = eto.attributeValue("datasource");
		String toPkColumn = eto.attributeValue("pkColumn");
		String pkColumntype = eto.attributeValue("pkColumnType");;
		String tosql = eto.getText();
		int batch = Integer.parseInt(eto.attributeValue("batch"));

		String[] cols = efrom.attributeValue("cols").split(",");
		String fromColsStr = task.elementText("fromColsName");
		
		Element option = task.element("option");
		String isIncrement = option.attributeValue("isIncrement");
		String createHashTable = option.attributeValue("isCreateHashTable");
		
		boolean incrementFalg = false;
		boolean isCreateHashTable = false;
		if("true".equalsIgnoreCase(isIncrement))
			incrementFalg = true;
		
		if("true".equalsIgnoreCase(createHashTable))
			isCreateHashTable = true;
		
		try {
			dataExtract(fromds, fromsql, fromColsStr, fromPkColumn, tods, tosql, toTable, toPkColumn, cols, batch,
					pkColumntype,incrementFalg,isCreateHashTable);
		} catch (Exception e) {
			mainThread.logTaskRun("多进程集群数据抽取错误!", e.getMessage());
			throw new Exception("多进程集群数据抽取错误!", e);
		} finally {
			dmo.releaseContext(fromds);
			dmo.releaseContext(tods);
		}
	}

	/**
	 * 
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
	 */
	public void dataExtract(String fromds, String fromsql, String fromColsStr, String fromPkColumn, String tods,
			String tosql, String toTable, String toPkColumn, String colsMap[], int batch, String pkColumntype,
			boolean incrementFalg,boolean isCreateHashTable)
			throws Exception {
		mainThread.logTaskRun("开始多进程抽取数据到目标表【"+toTable+"】","");

		int[] fromcols = new int[colsMap.length];
		for (int i = 0; i < colsMap.length; i++) {
			fromcols[i] = Integer.parseInt(colsMap[i]);
		}
		
		String hashTableName = extractUtil.getHashTableName(toTable);
		String tempHashTable = extractUtil.getTempHashTableName(toTable);
		String[] fromPkCols = fromPkColumn.split(",");
		String[] toPkCols = toPkColumn.split(",");
		String[] toPkColTypes = pkColumntype.split(",");
		
		long startTime = System.currentTimeMillis();
		if(!incrementFalg){
			mainThread.logTaskRun("全量数据抽取进程开始...", "");
			// 全量更新记录
			dmo.executeUpdateByDS(tods, "truncate table " + toTable);
			dmo.executeUpdateByDS(tods, "call drop_ind_table_proc('" + toTable + "')");
			if(isCreateHashTable){
				//生成hashmap表
				extractUtil.dropTable(tods, hashTableName);
				extractUtil.createHashTable(tods, toTable, toPkCols, toPkColTypes, hashTableName);
			}
			mainThread.logTaskRun("目标表数据清空完毕，索引禁用，启动抽取进程...", "");
		}else{
			mainThread.logTaskRun("增量数据抽取进程开始...", "");
			extractUtil.dropTable(tods, tempHashTable);
			extractUtil.createHashTable(tods, toTable, toPkCols, toPkColTypes, tempHashTable);
			mainThread.logTaskRun("创建值的HASHMAP表完毕，抽取Hash值抽取进程...", "");
		}
		
		int module = 3;//模，也就是分的进程数
		ExtractTaskCompleteListener listener = new ExtractTaskCompleteListener();
		DataExtractClusterTask extractTask = new DataExtractClusterTask(module);
		extractTask.setFromds(fromds);
		extractTask.setFromds(fromds);
		extractTask.setFromsql(fromsql);
		extractTask.setFromPkColumn(fromPkColumn);
		extractTask.setTods(tods);
		extractTask.setFromColsStr(fromColsStr);
		extractTask.setTosql(tosql);
		extractTask.setFromcols(fromcols);
		extractTask.setToPkColumn(toPkColumn);
		extractTask.setToPkColumnType(pkColumntype);
		extractTask.setToTable(toTable);
		extractTask.setIncrementFlag(incrementFalg);
		extractTask.setCreateHashTable(isCreateHashTable);
		
		ClusterComputeManager.getInstance().startClusterComputeTask(extractTask,listener);
		
		//等待执行结束
		while (!isExtractFinished) {
			Thread.sleep(500);
		}
		
		Integer totalRows;
		if(isExecOk){		
			totalRows = (Integer) extractResultMap.get(DataExtractClusterTask.KEY_DEALEDROWS);
		}else{
			throw new Exception("多进程数据抽取时执行异常!");
		}

		if(!incrementFalg){
			
			mainThread.logTaskRun("多进程全量数据抽取完毕，耗时："+(System.currentTimeMillis() - startTime)/1000+"s，开始恢复表索引...","");
			dmo.executeUpdateByDS(tods, "call recover_ind_table_proc('" + toTable + "')");
			mainThread.logTaskRun("开始执行表分析...","");
			dmo.executeUpdateByDS(tods, "ANALYZE TABLE "+toTable+" COMPUTE STATISTICS");
			
			if(isCreateHashTable){
				extractUtil.addHashTableIndex(tods, hashTableName, toPkCols);
			}
			
			mainThread.logTaskRun("表【"+toTable+"】抽取结束,记录数 " + totalRows + "条，总耗时: " + (System.currentTimeMillis() - startTime) / 1000 + "s.","");
		}else{
			
			extractUtil.addHashTableIndex(tods, tempHashTable, toPkCols);
			mainThread.logTaskRun("增量多进程Hash数据抽取完毕,耗时："+(System.currentTimeMillis() - startTime)/1000+"s ","");
			
			PreparedStatement insertPs = dmo.getConn(tods).prepareStatement(tosql);//插入数据的stat  
			
			//以下删除分批进行，每次1W条。
			int deleteBatch = 10000;
			int deletedRows = deleteBatch;
			int totalDeletedRows = 0;
			int totalUpdatedRows = 0;
			
			String hashPkCondition = "";
			String toTablePkCondition = "";
			String selPkColumns1 = "";
			String selPkColumns2 = "";
			String deletedCondition = "";
			String updatedCondition = "";
			//约定tempHashTable记为t1,hashTable记为t2, toTable记为t, tempHashTable与hashTable组合记为h
			for(int i=0;i<toPkCols.length;i++){
				if(i==0){
					hashPkCondition += " t1."+toPkCols[i]+"=t2."+toPkCols[i];
					toTablePkCondition += " t."+toPkCols[i]+"=h."+toPkCols[i];
					selPkColumns1 += "t1."+toPkCols[i];
					selPkColumns2 += "t2."+toPkCols[i];
				}else{
					hashPkCondition += " and t1."+toPkCols[i]+"=t2."+toPkCols[i];
					toTablePkCondition += " and t."+toPkCols[i]+"=h."+toPkCols[i];
					selPkColumns1 += ",t1."+toPkCols[i];
					selPkColumns2 += ",t2."+toPkCols[i];
				}
				
				deletedCondition += " and "+toPkCols[i]+" in (select "+toPkCols[i]+" from "+hashTableName+" t2 "
						+" where not exists(select 1 from "+tempHashTable+" t1 where t1."+toPkCols[i]+"=t2."+toPkCols[i]+") )";
				
				updatedCondition += " and " +toPkCols[i]+" in (select t1."+toPkCols[i]+" from "+tempHashTable+" t1,"+hashTableName+" t2 "
						+" where t2."+toPkCols[i]+"=t1."+toPkCols[i]+" and t2.contenthashvalue!=t1.contenthashvalue )";
			}
			//判断删除的条件是：主键在旧hashtable表中有，在新hashtable表中没有
			String deletedPkView = "select "+ selPkColumns2 +" from "+hashTableName+" t2 "
						+" where not exists(select 1 from "+tempHashTable+" t1 where "+hashPkCondition +")";
			
			//判断更新的条件是：在新旧hashtable中主键均有，但是hashvalue值不一致
			String updatedPkView = "select "+selPkColumns1+" from "+tempHashTable+" t1,"+hashTableName+" t2 "
						+" where "+hashPkCondition+" and t2.contenthashvalue!=t1.contenthashvalue ";
			
			//删除被删除的记录
			String tempSql =  "delete "+toTable+" h where exists (select 1 from ("+deletedPkView+") t where " + toTablePkCondition +") and rownum <= "+deleteBatch;
			deletedRows = deleteBatch;
			while(deletedRows == deleteBatch){
				deletedRows = dmo.executeUpdateByDS(tods, tempSql);
				totalDeletedRows += deletedRows;
				dmo.commit(tods);
			}
			
			//删除更新的记录 
			String tempSql2 = "delete "+toTable+" h where exists (select 1 from ("+updatedPkView+") t where " + toTablePkCondition +") and rownum <= "+deleteBatch;
			deletedRows = deleteBatch;
			while(deletedRows == deleteBatch){
				deletedRows = dmo.executeUpdateByDS(tods, tempSql2);
				totalUpdatedRows += deletedRows;
				dmo.commit(tods);
			}
			mainThread.logTaskRun("删除目标表中更新或删除的记录成功,删除"+totalDeletedRows+"条，更新"+totalUpdatedRows+"条，已耗时："+ (System.currentTimeMillis()-startTime)/1000 +"s","");
			
			String queryUpdatedHashSql = "select "+selPkColumns1+" from "+tempHashTable+" t1 where not exists "
					+"(select 1 from " +hashTableName+" t2 where "+hashPkCondition+" and t2.contenthashvalue=t1.contenthashvalue)";
			PreparedStatement updatedPs = dmo.getConn(tods).prepareStatement(queryUpdatedHashSql);
			
			String queryUpdatedDataSql = "select * from ("+fromsql + ")";
			boolean isSinglePk = true; //主键个数是否单一
			if(toPkCols.length > 1)
				isSinglePk = false;
			
			//判断是否为数字，来决定是否加单引号
			boolean[] toPkIsNumber = new boolean[toPkColTypes.length];
			for(int i=0;i<toPkColTypes.length;i++){
				String temp = toPkColTypes[i].toUpperCase();
				if( temp.indexOf("NUMBER") > 0){
					toPkIsNumber[i] = true;
				}else{
					toPkIsNumber[i] = false;
				}
			}
			
			StringBuffer pkValues = new StringBuffer();
			int row2 =0 ;
			ResultSet updatedHashRs = updatedPs.executeQuery();
			updatedHashRs.setFetchSize(200);
			while(updatedHashRs.next()){
				row2 ++;
				
				if(isSinglePk){
					if(toPkIsNumber[0])
						pkValues.append(",").append(updatedHashRs.getString(1));
					else
						pkValues.append(",'").append(updatedHashRs.getString(1)).append("'");
				}else{
					pkValues.append(" or (1=1");
					for(int i=0;i<toPkCols.length;i++){
						if(toPkIsNumber[i])
							pkValues.append(" and ").append( fromPkCols[i]+"=" + updatedHashRs.getString(i+1));
						else
							pkValues.append(" and ").append( fromPkCols[i]+"='" + updatedHashRs.getString(i+1)+"'");
					}
					pkValues.append(")");
				}
				
				//1000条查询一次
				if(row2% 1000 == 0){
					StringBuffer sb = new StringBuffer();
					if(isSinglePk){
						pkValues = pkValues.deleteCharAt(0);
						sb.append(queryUpdatedDataSql).append(" where " + fromPkColumn + " in ("+pkValues+")");
					}else{
						sb.append(queryUpdatedDataSql).append(" where 1=2 " + pkValues);
					}
					logger.debug("查询增量数据语句：" + sb.toString());
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
					try{
						rs.close();
						ps.close();
					}catch (Exception e) {
						logger.error("数据库cursor释放异常");
					}
				}
				if(row2%batch==0) {
             		insertPs.executeBatch();
             		dmo.commit(tods);
             	}
			}
			//不足批次的处理
			if(pkValues.length() > 0){//还存在更新的记录
				StringBuffer sb = new StringBuffer();
				if(isSinglePk){
					pkValues = pkValues.deleteCharAt(0);
					sb.append(queryUpdatedDataSql).append(" where " + fromPkColumn + " in ("+pkValues+")");
				}else{
					sb.append(queryUpdatedDataSql).append(" where 1=2 " + pkValues);
				}
				logger.debug("查询增量数据语句：" + sb.toString());
				PreparedStatement ps = dmo.getConn(fromds).prepareStatement(sb.toString());
				ResultSet rs = ps.executeQuery();
				ResultSetMetaData rsmd= rs.getMetaData();
				while(rs.next()){
					for(int i=0;i<colsMap.length;i++){
						insertPs.setObject(i+1, rs.getObject(fromcols[i]), rsmd.getColumnType(fromcols[i]));            		
					}
					insertPs.addBatch();
				}
				try{
					rs.close();
					ps.close();
				}catch (Exception e) {
					logger.error("数据库cursor释放异常");
				}
				insertPs.executeBatch();
				dmo.commit(tods);
			}
			mainThread.logTaskRun("更新(新增、更新)记录插入,记录数 "+row2+"条，已耗时: " + (System.currentTimeMillis()-startTime)/1000+"s","");
			try{
				updatedPs.close();
				insertPs.close();
			}catch (Exception e) {
				logger.error("数据库cursor释放异常");
			}
     		
     		String statisticSql = "select (select count(1) from "+tempHashTable+" t1 where not exists (select 1 from "+hashTableName+" t2 where "+hashPkCondition+")) inserted,"
     			+ " (select count(1) from "+tempHashTable+" t1,"+hashTableName+" t2 where "+hashPkCondition+" and t2.contenthashvalue!=t1.contenthashvalue) updated,"
     			+ " (select count(1) from "+tempHashTable+" t1,"+hashTableName+" t2 where "+hashPkCondition+" and t2.contenthashvalue=t1.contenthashvalue) nochanged,"
     			+ " (select count(1) from "+hashTableName+" t2 where not exists (select 1 from "+tempHashTable+" t1 where "+hashPkCondition+")) deleted "
     			+ " from dual";
     		HashVO[] tjVos = dmo.getHashVoArrayByDS(tods, statisticSql);
            String logMessage = "";
            int totalCount = 0;
            logMessage += "新增记录 " + tjVos[0].getIntegerValue("inserted").intValue() + " 条, " ;
            logMessage += "更新记录 " + tjVos[0].getIntegerValue("updated").intValue() + " 条, " ;
            logMessage += "删除记录 " + tjVos[0].getIntegerValue("deleted").intValue() + " 条, " ;
            logMessage += "不变的记录 " + tjVos[0].getIntegerValue("nochanged").intValue() + " 条," ;
            
            totalCount = tjVos[0].getIntegerValue("inserted").intValue()+tjVos[0].getIntegerValue("nochanged").intValue()
            			+tjVos[0].getIntegerValue("updated").intValue()+tjVos[0].getIntegerValue("deleted").intValue();
            
            //把新的HashMap表，作为现在的MAP表,索引重命名
            extractUtil.updateHashValueTable(tods, tempHashTable, hashTableName,toPkCols);
            mainThread.logTaskRun("HashValueMap表【"+hashTableName+"】更新成功！","");
            
            logMessage = "****数据抽取，总耗时"+ ((System.currentTimeMillis() - startTime)/1000) +"s,共处理记录 " + totalCount + " 条，其中： " + logMessage;
            mainThread.logTaskRun(logMessage,"");
		}
	}
	
	class ExtractTaskCompleteListener implements ClusterComputeTaskListener{

		@Override
		public void taskComplete(String taskCode, ClusterComputeTaskResult result) {
			logger.info("收到任务回复[taskcode="+taskCode+",returnCode="+result.getReturnCode()+"]");
			int returnCode = result.getReturnCode();
			
			if(returnCode == ClusterComputeConst.TASK_RESPONSECODE_OK){
				extractResultMap = result.getResultMap();
				isExecOk = true;
			}else{
				extractResultMap = result.getResultMap();
				isExecOk = false;
				mainThread.logTaskRun("多进程数据抽取执行异常!",result.getExceptionDetail());
			}
			
			isExtractFinished = true;
		}
		
	}

}
