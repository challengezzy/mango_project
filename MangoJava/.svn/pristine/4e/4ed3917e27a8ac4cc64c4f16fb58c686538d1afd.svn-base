/**
 * 
 */
package smartx.bam.bs.bvmanager;

import static smartx.framework.common.utils.StringUtil.getTimestamp;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.vo.NovaLogger;

/**
 * @author sky
 * 持久化业务视图历史数据线程
 */
public class BvPersistentWriter extends Thread {

	private Logger logger = NovaLogger.getLogger(this);
	
	private ConcurrentLinkedQueue<BvPersitentTask> queue = new ConcurrentLinkedQueue<BvPersitentTask>();
	
	public void addBvPersistenTaskQueue(BvPersitentTask bvPersistentTask)
	{
		queue.add(bvPersistentTask);
	}
	
	@Override
	public void run() {
		CommDMO dmo = new CommDMO();
		while(true){
			try{
				while(!queue.isEmpty()){
					BvPersitentTask task = queue.poll();
					handlerTask(task);
				}
				sleep(1000);
			}catch(Exception e){
				try {
					dmo.rollback(null);
				} catch (Exception e1) {
				}
				logger.error("",e);
			}
		}
	}
	
	private void handlerTask(BvPersitentTask task){
		CommDMO dmo = new CommDMO();
		String tableName = BusinessViewManager.BVTABLE_PREFIX + task.getWindowName();
		String sequenceName = "S_" + tableName;
		StringBuilder sql = new StringBuilder("insert into ");
		sql.append(tableName);
		sql.append(" values(");
		sql.append(sequenceName);
		sql.append(".nextval,");
		for(String value : task.getValues()){
			sql.append("'").append(value).append("'").append(",");
		}
		try{
			sql.append("sysdate)");
			logger.debug(sql.toString());
			dmo.execAtOnceByDS(null, sql.toString());
		}catch(Exception e){
			if(e.getMessage().indexOf("ORA-00942") >= 0){
				queue.add(task);
				createTableStructByBvEpl(tableName,task.getFieldLists(),false);
			}else if(e.getMessage().indexOf("ORA-00913") >= 0 || e.getMessage().indexOf("ORA-00947") >= 0){
				queue.add(task);
				createTableStructByBvEpl(tableName,task.getFieldLists(),true);
			}else
				logger.error("",e);
		}
		
	}
	
	/**
	 * 通过业务视图中的EPL创建表
	 * @param bvcode
	 */
	private void createTableStructByBvEpl(String tablename,List<String> fields,boolean isExist)
	{
		logger.debug("创建业务视图历史数据表["+tablename+"]");
		CommDMO dmo = new CommDMO();
		StringBuilder sql = null;
		String modifyTableName = "";
		if(isExist){
			modifyTableName = getModifyTableName(tablename);
			sql = new StringBuilder();
			sql.append("alter table ");
			sql.append(tablename).append(" rename to ").append(modifyTableName);
			logger.debug("修改表名sql..."+sql.toString());
			try {
				dmo.execAtOnceByDS(null, sql.toString());
			} catch (Exception e) {
				logger.error("修改表名错误!", e);
			}
		}
		sql = new StringBuilder("create table ");
		sql.append(tablename).append(" (BV_ID NUMBER(18) not null,");
		for(String field : fields){
			sql.append("\"").append(field).append("\"").append(" VARCHAR2(500),");
		}
		sql.append("BV_CREATEDATE DATE not null)");
		logger.debug("创建业务视图历史数据表sql..."+sql.toString());
		try{
			dmo.execAtOnceByDS(null, sql.toString());
			if(!isExist){
				sql = new StringBuilder("create sequence S_");
				sql.append(tablename);
				sql.append(" minvalue 1");
				sql.append(" maxvalue 999999999999999999");
				sql.append(" start with 2");
				sql.append(" increment by 1");
				sql.append(" nocache");
				sql.append(" cycle");
				dmo.execAtOnceByDS(null, sql.toString());
			}
		}catch(Exception e){
			logger.error("创建业务视图历史数据表错误!",e);
			if(isExist){
				sql = new StringBuilder("alter table ");
				sql.append(modifyTableName);
				sql.append(" rename to ");
				sql.append(tablename);
				logger.debug("修改表名sql..."+sql.toString());
				try{
					dmo.execAtOnceByDS(null, sql.toString());
				}catch(Exception e1){
					logger.error("修改表名错误!",e1);
				}
			}
		}finally{
			dmo.releaseContext(null);
		}
	}
	
	private String getModifyTableName(String tablename){
		if(tablename.length() > 16)
			return tablename.substring(0, 16).concat(getTimestamp());
		else
			return tablename.concat(getTimestamp());
	}
	
}
