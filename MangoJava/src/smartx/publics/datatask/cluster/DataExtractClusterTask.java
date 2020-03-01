package smartx.publics.datatask.cluster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import smartx.publics.clustercompute.vo.ClusterComputeSubTask;
import smartx.publics.clustercompute.vo.ClusterComputeTask;

/**
 *@author zzy
 *@date Jul 17, 2012
 **/
public class DataExtractClusterTask implements ClusterComputeTask {
	
	public static String KEY_DEALEDROWS = "KEY_DEALEDROWS";//处理记录数
	public static String KEY_DEALEDTIME = "KEY_DEALEDTIME";//处理时间
	public static String KEY_EXCEPTIONMSG = "KEY_EXCEPTIONMSG";
	
	public static String PARAM_MODULE = "PARAM_MODULE";
	public static String PARAM_MODVALUE = "PARAM_MODVALUE";
	
	public static String PARAM_FROMDS = "PARAM_FROMDS";
	public static String PARAM_FROMPKCOLUMN = "PARAM_FROMPKCOLUMN";
	public static String PARAM_FROMSQL = "PARAM_FROMSQL";
	public static String PARAM_FROMCOLSSTR = "PARAM_FROMCOLSSTR";
	public static String PARAM_FROMCOLS = "PARAM_FROMCOLS";
	public static String PARAM_TODS = "PARAM_TODS";
	public static String PARAM_TOPKCOLUMN = "PARAM_TOPKCOLUMN";
	public static String PARAM_TOTABLE = "PARAM_TOTABLE";
	public static String PARAM_TOSQL = "PARAM_TOSQL";
	public static String PARAM_TOPKCOLUMNTYPE = "PARAM_TOPKCOLUMNTYPE";
	public static String PARAM_INCREMENTFLAG = "PARAM_INCREMENTFLAG";//增量标志
	public static String PARAM_ISCREATEHASHTABLE = "PARAM_ISCREATEHASHTABLE";//增量标志
	public static String PARAM_MODFLAG = "PARAM_MODFLAG";
	
	String fromds, fromPkColumn, fromsql,fromColsStr;
	String tods,toPkColumn, tosql,toTable,toPkColumnType;
	int[] fromcols;
	boolean incrementFlag = false;//增量标志
	private boolean isCreateHashTable = false;//是否生成主键值的MAP表
	boolean modFalg = false;//查询数据时，是否取模
	
	public static String SUBTASKEXECUTOR = "smartx.publics.datatask.cluster.DataExtractClusterSubTaskExecutor";
	
	protected int module;
	
	public DataExtractClusterTask(int module){
		this.module = module;
	}

	@Override
	public int getTimeout() {
		return 72000000;
	}

	@Override
	public List<ClusterComputeSubTask> generateSubTasks(int numOfNode) {
		ArrayList<ClusterComputeSubTask> subTaskList = new ArrayList<ClusterComputeSubTask>();
		if(numOfNode > 1){
			modFalg = true;
		}
		for(int i=0; i< numOfNode; i++){
			ClusterComputeSubTask task = new ClusterComputeSubTask();
			task.setExecutorClassName(SUBTASKEXECUTOR);
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put(PARAM_MODULE, numOfNode);
			paramMap.put(PARAM_MODVALUE, i);
			paramMap.put(PARAM_FROMDS , fromds);
			paramMap.put(PARAM_FROMPKCOLUMN , fromPkColumn);
			paramMap.put(PARAM_FROMSQL , fromsql);
			paramMap.put(PARAM_FROMCOLSSTR , fromColsStr);
			paramMap.put(PARAM_FROMCOLS, fromcols);
			paramMap.put(PARAM_TODS , tods);
			paramMap.put(PARAM_TOPKCOLUMN , toPkColumn);
			paramMap.put(PARAM_TOTABLE , toTable);
			paramMap.put(PARAM_TOPKCOLUMNTYPE,toPkColumnType);
			paramMap.put(PARAM_TOSQL , tosql);
			paramMap.put(PARAM_INCREMENTFLAG, incrementFlag);
			paramMap.put(PARAM_ISCREATEHASHTABLE, isCreateHashTable);
			paramMap.put(PARAM_MODFLAG, modFalg);
			
			task.setParamMap(paramMap);
			subTaskList.add(task);
		}
		return subTaskList;
	}

	@Override
	public Map<String, ?> generateTaskResult(List<Map<String, ?>> subTaskResults) {
		Integer rows = 0;
		
		for(Map<String, ?> subMap:subTaskResults){
			Integer dealedRows = (Integer) subMap.get(DataExtractClusterTask.KEY_DEALEDROWS);
			rows+= dealedRows;
		}
		Map<String, Integer> result = new HashMap<String, Integer>();
		result.put(DataExtractClusterTask.KEY_DEALEDROWS, rows);
		
		return result;
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

	public void setToPkColumn(String toPkColumn) {
		this.toPkColumn = toPkColumn;
	}

	public void setFromColsStr(String fromColsStr) {
		this.fromColsStr = fromColsStr;
	}

	public void setTosql(String tosql) {
		this.tosql = tosql;
	}

	public void setFromcols(int[] fromcols) {
		this.fromcols = fromcols;
	}

	public void setIncrementFlag(boolean incrementFlag) {
		this.incrementFlag = incrementFlag;
	}

	public void setToTable(String toTable) {
		this.toTable = toTable;
	}

	public void setToPkColumnType(String toPkColumnType) {
		this.toPkColumnType = toPkColumnType;
	}

	public void setCreateHashTable(boolean isCreateHashTable) {
		this.isCreateHashTable = isCreateHashTable;
	}

	public void setModFalg(boolean modFalg) {
		this.modFalg = modFalg;
	}


}
