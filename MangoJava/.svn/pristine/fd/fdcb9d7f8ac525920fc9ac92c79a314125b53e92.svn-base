package smartx.publics.cep.bs.persistent;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.vo.NovaLogger;
import smartx.publics.cep.bs.PersistentManager;

import com.espertech.esper.core.EPStatementImpl;

/**
 * 持久化写库线程
 * @author teddyxu
 *
 */
public class PersistentWriter extends Thread{
	private Logger logger = NovaLogger.getLogger(this);
	
	private String datasourceName;
	
	private ConcurrentLinkedQueue<PersistentTask> queue = new ConcurrentLinkedQueue<PersistentTask>();
	
	public PersistentWriter(String datasourceName){
		this.datasourceName = datasourceName;
	}
	
	public void addPersistentTask(PersistentTask task){
		queue.add(task);
	}
	
	public void run(){
		CommDMO dmo = new CommDMO();
		while(true){ 
			try {
				while(!queue.isEmpty()){
					PersistentTask task = queue.poll();
					handleTask(task);
				}
				sleep(100);
			} catch (Exception e) {
				try {
					dmo.rollback(datasourceName);
				} catch (Exception e1) {
				}
				logger.error("",e);
			}
			finally{
				dmo.releaseContext(datasourceName);
			}
		}
	}

	private void handleTask(PersistentTask task) throws Exception {
		CommDMO dmo = new CommDMO();
		String sql;
		if (PersistentTask.TYPE_PersistentTask_ADD.equals(task.getType())) {
			sql = "insert into " + PersistentManager.TABLENAME_EVENTLOG
					+ " (PROVIDER," + "EVENTCODE," + "EVENTTYPE,"
					+ "ACTIVATETIME," + "CONTENT)" + "values(?,?,?,?,?)";
			String eventCode = task.getEventCode();
			Long activatetime = task.getActivateTime();
			String eventType = task.getEventType();
			Object realObject = task.getRealObject();
			String providerName = task.getProviderName();
			if (realObject instanceof Map) {
				// 非map类型事件暂不支持
				byte[] content = objectToByte(realObject);
				dmo.executeUpdateByDS(datasourceName, sql, providerName,
						eventCode, eventType, activatetime, content);
			}
		} else if (PersistentTask.TYPE_PersistentTask_DEL
				.equals(task.getType())) {
			sql = "delete from " + PersistentManager.TABLENAME_EVENTLOG
					+ " where PROVIDER=? and EVENTCODE=?";
			String eventCode = task.getEventCode();
			String providerName = task.getProviderName();
			dmo.executeUpdateByDS(datasourceName, sql, providerName, eventCode);
		}
	}
	
	private byte[] objectToByte(Object obj) throws Exception{
		ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
		ObjectOutputStream objOutStream = new ObjectOutputStream(byteOutStream);
		objOutStream.writeObject(obj);
		objOutStream.flush();
		objOutStream.close();
		byteOutStream.close();
		return byteOutStream.toByteArray();
	}
}
