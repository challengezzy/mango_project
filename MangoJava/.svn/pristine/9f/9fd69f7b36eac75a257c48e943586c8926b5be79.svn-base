package smartx.publics.cep.bs;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.bs.dbaccess.dsmgr.DataSourceManager;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaLogger;
import smartx.publics.cep.bs.persistent.PersistentTask;
import smartx.publics.cep.bs.persistent.PersistentWriter;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderIsolated;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EPStatementStateListener;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.StatementAwareUpdateListener;
import com.espertech.esper.client.time.CurrentTimeEvent;
import com.espertech.esper.core.EPServiceProviderImpl;
import com.espertech.esper.core.EPStatementImpl;
import com.espertech.esper.core.StatementType;

/**
 * 负责窗口数据的持久化，以便断电后的数据恢复
 * @author teddyxu
 *
 */
public class PersistentManager {
	private Logger logger = NovaLogger.getLogger(this);
	
	private static boolean isInited = false;
	
	private boolean useBuildinDatabase = true;

	private String datasourceName =  "datasource_cep_persiststore";
	
	private static final String DRIVER_DERBY = "org.apache.derby.jdbc.EmbeddedDriver";
	private static final String URL_DERBY = "jdbc:derby:smartxcep;create=true";
	public static final String TABLENAME_EVENTLOG = "CEP_EVENTLOG";
	
	private String[] persistentProviders;
	
	private PersistentWriter writer;
	
	public PersistentManager(String[] persistentProviders, boolean useBuildinDatabase, String externalDatasourceName) throws Exception{
		if(isInited)
			return;
		isInited = true;
		this.useBuildinDatabase = useBuildinDatabase;
		if(!useBuildinDatabase){
			this.datasourceName = externalDatasourceName;
		}
		else{
			//初始化derby数据库
			if(!DataSourceManager.hasDatasource(datasourceName)){
				String url = URL_DERBY;
				logger.debug("初始化内建CEP持久化数据源["+datasourceName+"]");
				HashMap<String, String> dssetup = new HashMap<String, String>();
				dssetup.put("name", datasourceName);
		        dssetup.put("driver", DRIVER_DERBY);
		        dssetup.put("url", url);
		        dssetup.put("initsize", "1");
		        dssetup.put("poolsize", "5");
		        DataSourceManager.initDS(new HashMap[]{dssetup});
			}
			initDatabaseSchema();
		}
		
		//启动writer
		writer = new PersistentWriter(datasourceName);
		writer.start();
		
		this.persistentProviders = persistentProviders;
		for(String persistentProvider : persistentProviders){
			try {
				setupCEPProvider(persistentProvider);
			} catch (Exception e) {
				logger.error("持久化监听容器["+persistentProvider+"]失败，继续下一步",e);
				continue;
			}
		}
	}
	
	public void replayHistoryEvents() throws Exception{
		//首先恢复数据
		CommDMO dmo = new CommDMO();
		try {
			logger.debug("开始恢复CEP引擎的历史数据");
			for(String providerName : persistentProviders){
				logger.debug("恢复容器["+providerName+"的历史数据");
				final EPServiceProviderImpl epService = (EPServiceProviderImpl) EPServiceProviderManager.getProvider(providerName);
				
				String sql = "select * from "+TABLENAME_EVENTLOG+" where provider=? order by ACTIVATETIME";
				HashVO[] vos = dmo.getHashVoArrayByDS(datasourceName, sql, providerName);
				EPServiceProviderIsolated epServiceIsolated = epService.getEPServiceIsolated("replayOldEventsService");
				for(String stmtName : epService.getStatementLifecycleSvc().getStatementNames()){
					EPStatementImpl stmt = (EPStatementImpl) epService.getStatementLifecycleSvc().getStatementByName(stmtName);
					if(stmt.getStatementMetadata().getStatementType() == StatementType.CREATE_WINDOW)
						epServiceIsolated.getEPAdministrator().addStatement(stmt);
				}
				int i=0;
				for(HashVO vo : vos){
					try{
						long activatetime = vo.getLongValue("ACTIVATETIME");
						byte[] contentBytes = vo.getBytesValue("content");
						long id = vo.getLongValue("id");
						String eventType = vo.getStringValue("EVENTTYPE");
						Map<?, ?> eventContent = (Map<?, ?>) byteToObject(contentBytes);
	
						//更新新的hashcode
						sql = "delete from "+TABLENAME_EVENTLOG+" where id=?";
						dmo.executeUpdateByDS(datasourceName, sql,id);
						//发送事件
						epServiceIsolated.getEPRuntime().sendEvent(new CurrentTimeEvent(activatetime));
						epServiceIsolated.getEPRuntime().sendEvent(eventContent,eventType);
						i++;
						//一个事件一提交
						dmo.commit(datasourceName);
					}
					catch(Exception e){
						dmo.rollback(datasourceName);
						logger.error("",e);
						continue;
					}
				}
				epServiceIsolated.getEPRuntime().sendEvent(new CurrentTimeEvent(epService.getSchedulingService().getTime()));
				epServiceIsolated.destroy();
				logger.debug("恢复容器["+providerName+"的历史数据完毕，共恢复"+i+"条");
			}
			//dmo.commit(datasourceName);
			logger.debug("恢复CEP引擎的历史数据完毕");
		} 
		catch(Exception e){
			//dmo.rollback(datasourceName);
			throw e;
		}
		finally{
			dmo.releaseContext(datasourceName);
		}
	}

	private void initDatabaseSchema() throws Exception {
		CommDMO dmo = new CommDMO();
		try{
			String sql = "SELECT * FROM SYS.SYSTABLES WHERE tableName = '"+TABLENAME_EVENTLOG+"'";
			HashVO[] vos = dmo.getHashVoArrayByDS(datasourceName, sql);
			if(vos.length == 0){
				logger.debug("建立内建CEP持久化数据源表结构");
				sql = "create table "+TABLENAME_EVENTLOG+"(" +
						"ID BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
						"PROVIDER VARCHAR(255)," +
						"EVENTCODE VARCHAR(100)," +
						"EVENTTYPE VARCHAR(255)," +
						"ACTIVATETIME BIGINT," +
						"CONTENT LONG VARCHAR FOR BIT DATA" +
						")";
				dmo.executeUpdateByDS(datasourceName, sql);
				sql = "create index idx_eventlog1 on "+TABLENAME_EVENTLOG+"(provider,eventcode)";
				dmo.executeUpdateByDS(datasourceName, sql);
				sql = "create index idx_eventlog2 on "+TABLENAME_EVENTLOG+"(ACTIVATETIME)";
				dmo.executeUpdateByDS(datasourceName, sql);
			}
		}
		catch(Exception e){
			logger.error("初始化CEP数据结构失败",e);
			throw e;
		}
		finally{
			dmo.releaseContext(datasourceName);
		}
	}
	
	private void setupCEPProvider(final String providerName) throws Exception {
		final EPServiceProviderImpl epService = (EPServiceProviderImpl) EPServiceProviderManager.getProvider(providerName);
		
		logger.debug("为容器["+providerName+"]添加持久化所需的监听器");
		epService.addStatementStateListener(new EPStatementStateListener(){

			@Override
			public void onStatementCreate(EPServiceProvider serviceProvider,
					EPStatement statement) {
				EPStatementImpl epstatement = (EPStatementImpl)statement;
				if(epstatement.getStatementMetadata().getStatementType() == StatementType.CREATE_WINDOW){
					epstatement.addListener(new StatementAwareUpdateListener(){

						@Override
						public void update(EventBean[] newEvents,
								EventBean[] oldEvents, EPStatement statement, EPServiceProvider epServiceProvider) {
							try{
								if(newEvents != null){
									for(EventBean eventBean : newEvents){
										String eventCode = String.valueOf(eventBean.hashCode());
										Long activatetime;
										EPStatementImpl statementImpl = (EPStatementImpl) statement;
										activatetime = statementImpl.getStatementContext().getSchedulingService().getTime();
										String eventType = eventBean.getEventType().getName();
										Object realObject = eventBean.getUnderlying();
										PersistentTask task = new PersistentTask();
										task.setType(PersistentTask.TYPE_PersistentTask_ADD);
										task.setEventCode(eventCode);
										task.setActivateTime(activatetime);
										task.setProviderName(providerName);
										task.setEventType(eventType);
										task.setRealObject(realObject);
										writer.addPersistentTask(task);
									}
									
								}
								if(oldEvents != null){
									for(EventBean eventBean : oldEvents){
										String eventCode = String.valueOf(eventBean.hashCode());
										PersistentTask task = new PersistentTask();
										task.setType(PersistentTask.TYPE_PersistentTask_DEL);
										task.setEventCode(eventCode);
										task.setProviderName(providerName);
										writer.addPersistentTask(task);
									}
								}						
							}
							catch(Exception e){
								logger.error("为窗口创建语句添加持久化监听失败",e);
							}
						}
						
					});
				}
				
			}

			@Override
			public void onStatementStateChange(
					EPServiceProvider serviceProvider, EPStatement statement) {
				
			}
			
		});
		
	}

	public boolean isUseBuildinDatabase() {
		return useBuildinDatabase;
	}
	
	
	protected Object byteToObject(byte[] data) throws Exception{
		ByteArrayInputStream byteInStream = new ByteArrayInputStream(data);
		ObjectInputStream objInStream = new ObjectInputStream(byteInStream);
		Object obj = objInStream.readObject();
		objInStream.close();
		byteInStream.close();
		return obj;
	}
}
