package smartx.publics.datatask;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Hashtable;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.dom4j.Element;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.vo.NovaLogger;

/**
 * @author zzy
 * @date Jul 12, 2012
 * @description 数据抽取子任务实现类
 **/
public class DT_MultiThreadExtract implements DataTaskExecuteIFC {

	private Logger logger = NovaLogger.getLogger(this.getClass());
	private CommDMO dmo = new CommDMO();

	private DataTaskExecThread mainThread;
	
	@Override
	public void dataTaskExec(Element task, DataTaskExecThread mainThread) throws Exception {
		this.mainThread = mainThread;
		Element efrom = task.element("from");
		String fromds = efrom.attributeValue("datasource");
		String fromPkColumn = efrom.attributeValue("pkColumn");
		//如果有多个主键，这个只用到一个就可以了
		String[] fromPkCols = fromPkColumn.split(",");
		String hashPkColumn = fromPkCols[0]; //如果有多個主鍵字段,則只取第一個
		
		String fromsql = efrom.getText();

		Element eto = task.element("to");
		String toTable = eto.attributeValue("toTable");
		String tods = eto.attributeValue("datasource");
		String toPkColumn = eto.attributeValue("pkColumn");
		String tosql = eto.getText();

		String[] cols = efrom.attributeValue("cols").split(",");
		String fromColsStr = task.elementText("fromColsName");

		int batch = Integer.parseInt(eto.attributeValue("batch"));
		String pkColumntype = "NUMBER(18)";

		try {
			dataFullExtract(fromds, fromsql, fromColsStr, hashPkColumn, tods, tosql, toTable, toPkColumn, cols, batch,
					pkColumntype);
		} catch (Exception e) {
			mainThread.logTaskRun("数据增量抽取错误!", e.toString());
			throw new Exception("数据增量抽取错误!", e);
		} finally {
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
	public void dataFullExtract(String fromds, String fromsql, String fromColsStr, String fromPkColumn, String tods,
			String tosql, String toTable, String toPkColumn, String[] colsMap, int batch, String pkColumntype)
			throws Exception {
		mainThread.logTaskRun("开始多线程抽取数据到目标表【"+toTable+"】","");

		//String[] fromColsName = fromColsStr.split(",");
		int[] fromcols = new int[colsMap.length];
		for (int i = 0; i < colsMap.length; i++) {
			fromcols[i] = Integer.parseInt(colsMap[i]);
		}

		long oldTime = System.currentTimeMillis();
		// 全量更新记录
		dmo.executeUpdateByDS(tods, "truncate table " + toTable);
		dmo.executeUpdateByDS(tods, "call drop_ind_table_proc('" + toTable + "')");
		
		mainThread.logTaskRun("目标表数据清空完毕，索引禁用，启动抽取线程...", "");
		
		int modLength = 3;
		Hashtable<String, Object> threadTable = new Hashtable<String, Object>();
		for (int i = 0; i < modLength; i++) {
			ExtractHashValueThread thread = new ExtractHashValueThread();
			thread.setFromds(fromds);
			thread.setFromds(fromds);
			thread.setFromsql(fromsql);
			thread.setFromPkColumn(fromPkColumn);
			thread.setTods(tods);
			thread.setFromColsStr(fromColsStr);
			thread.setTosql(tosql);
			thread.setFromcols(fromcols);
			thread.setToPkColumn(toPkColumn);
			thread.setModValue(i + "");
			thread.setIncrementFlag(false);

			thread.start();
			threadTable.put(i + "", thread);
		}

		boolean isFinished = false;
		while (!isFinished) {
			isFinished = true;
			for (Entry e : threadTable.entrySet()) {
				ExtractHashValueThread thread = (ExtractHashValueThread) e.getValue();
				if (!thread.isFinished && thread.getDealedRows() == -2) {
					isFinished = false;
					break;
				}
			}
			Thread.sleep(500);
		}
		
		int totalRows = 0;
		for (Entry e : threadTable.entrySet()) {
			ExtractHashValueThread thread = (ExtractHashValueThread) e.getValue();
			int dealed = thread.getDealedRows();
			if(dealed < 0){
				mainThread.logTaskRun("抽取线程【"+e.getKey()+"】执行异常！！！！！！！！", "");
				throw new Exception("抽取线程【"+e.getKey()+"】执行异常！！");
			}else{
				totalRows += dealed;
			}
		}
		
		mainThread.logTaskRun("开始恢复表索引...","");
		dmo.executeUpdateByDS(tods, "call recover_ind_table_proc('" + toTable + "')");
		mainThread.logTaskRun("开始执行表分析...","");
		dmo.executeUpdateByDS(tods, "ANALYZE TABLE "+toTable+" COMPUTE STATISTICS");
		
		mainThread.logTaskRun("数据全量抽取结束,记录数 " + totalRows + "条，耗时: " + (System.currentTimeMillis() - oldTime) / 1000 + "s.","");
	}

	public int extractFullValue(String fromds, String fromsql, String fromPkColumn, String fromColsStr,
			int[] fromcols, String tods, String tosql, String toPkColumn, String hashValueColumn, String hashTableName,
			String modValue) {
		
		int row = 0;
		try {
			mainThread.logTaskRun("全量抽取线程 【" + modValue + "】 执行开始...","");
			
			String fullFromsql = "select " + fromColsStr + " from ("
					+ fromsql + ") " + " where MOD(dbms_utility.get_hash_value(" + fromPkColumn + ",1,10000)," + 3
					+ ") = " + modValue;

			PreparedStatement insertPs = dmo.getConn(tods).prepareStatement(tosql);// 插入数据的stat

			PreparedStatement fromps = dmo.getConn(fromds).prepareStatement(fullFromsql);
			ResultSet fromRS = fromps.executeQuery();
			ResultSetMetaData rsmd = fromRS.getMetaData();
			fromRS.setFetchSize(500);
			while (fromRS.next()) {
				row++;
				for (int i = 0; i < fromcols.length; i++) {
					insertPs.setObject(i + 1, fromRS.getObject(fromcols[i]), rsmd.getColumnType(fromcols[i]));
				}
				insertPs.addBatch();

				if (row % 5000 == 0) {
					insertPs.executeBatch();
					dmo.commit(tods);
				}
			}
			insertPs.executeBatch();
			dmo.commit(tods);			
			mainThread.logTaskRun("全量抽取线程 【" + modValue + "】 执行结束,处理记录数【 " + row + "】条", "");
			
		} catch (Exception e) {
			mainThread.logTaskRun("全量抽取线程 【" + modValue + "】执行异常!!!!!!", e.getMessage());
			row = -1;
		}finally{
			dmo.releaseContext(tods);
			dmo.releaseContext(fromds);
		}
		return row;
	}

	class ExtractHashValueThread extends Thread {

		String fromds, fromsql, fromPkColumn, tods, fromHashTable, hashValueColumn, toPkColumn;
		String modValue;
		String fromColsStr, tosql, hashTableName;
		int[] fromcols;

		boolean isFinished = false;
		boolean incrementFlag = false;
		
		int dealedRows = -2;//-2为初始状态， -1，表示线程执行异常

		public void run() {
			dealedRows = extractFullValue(fromds, fromsql, fromPkColumn, fromColsStr, fromcols, tods, tosql, toPkColumn,
					hashValueColumn, hashTableName, modValue);

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

		public int getDealedRows() {
			return dealedRows;
		}
	}
}
