
package smartx.publics.cep.bs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.EventType;
import com.espertech.esper.client.UpdateListener;

import flex.messaging.MessageBroker;
import flex.messaging.messages.AsyncMessage;
import flex.messaging.util.UUIDUtils;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaLogger;
import smartx.framework.common.vo.SimpleHashVO;


public class StreamEventQueryManager {
	private Logger logger = NovaLogger.getLogger(this);
	
	public static final String DESTINATION_CEP_STREAMING = "smartXCEPPushService";
	public static final String PROPERTYNAME_ISNEWEVENT = "isNewEvent";
	
	private String clientID = UUIDUtils.createUUID();
	private MessageBroker msgBroker = MessageBroker.getMessageBroker(null);
	
	private Map<String, EPStatement> listeningStatements = new HashMap<String, EPStatement>();
	
	/**
	 * 查询事件
	 * @param moduleName
	 * @param epl
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object>[] queryEvent(String providerName, String epl) throws Exception{
		Map<String, Object>[] result;
		logger.info("在容器["+providerName+"]上查询事件[EPL="+epl+"]");
		CommDMO dmo = new CommDMO();
		try{
			HashVO[] vos = dmo.getHashVoArrayByDS(StreamModuleDeploymentManager.JDBC_DATASOURCE_PREFIX+providerName, epl);
			result = new Map[vos.length];
			for(int i=0;i<vos.length;i++){
				SimpleHashVO svo = new SimpleHashVO(vos[i]);
				result[i] = svo.getDataMap();
			}
		}
		finally{
			dmo.releaseContext();
		}
		logger.info("在容器["+providerName+"]上查询事件[EPL="+epl+"]完毕");
		return result;
	}
	
	/**
	 * 监听事件（动态epl）
	 * @param moduleName
	 * @param epl
	 * @throws Exception
	 */
	public void addEventQueryListener(final String providerName, String epl) throws Exception{
		logger.info("在容器["+providerName+"]上监听事件[EPL="+epl+"]");
		if(listeningStatements.containsKey(epl)){
			logger.info("该语句已被监听");
			return;
		}
		EPServiceProvider epService = EPServiceProviderManager.getProvider(providerName);
		EPAdministrator admin = epService.getEPAdministrator();
		EPStatement stmt = admin.createEPL(epl);
		stmt.addListener(new UpdateListener(){

			@Override
			public void update(EventBean[] newEvents, EventBean[] oldEvents) {
				List<Map<String,Object>> events = new ArrayList<Map<String,Object>>();
				if(newEvents != null){
					for(EventBean eventBean : newEvents){
						Map<String, Object> event = new HashMap<String, Object>();
						event.put(PROPERTYNAME_ISNEWEVENT, true);
						EventType eventType = eventBean.getEventType();
						for(String name : eventType.getPropertyNames()){
							Object value = eventBean.get(name);
							event.put(name, value);
						}
						events.add(event);
					}
				}
				if(oldEvents != null){
					for(EventBean eventBean : oldEvents){
						Map<String, Object> event = new HashMap<String, Object>();
						event.put(PROPERTYNAME_ISNEWEVENT, false);
						EventType eventType = eventBean.getEventType();
						for(String name : eventType.getPropertyNames()){
							Object value = eventBean.get(name);
							event.put(name, value);
						}
						events.add(event);
					}
				}
				AsyncMessage msg = new AsyncMessage();
                msg.setDestination(DESTINATION_CEP_STREAMING);
                msg.setHeader("DSSubtopic", providerName);
                msg.setClientId(clientID);
                msg.setMessageId(UUIDUtils.createUUID());
                msg.setTimestamp(System.currentTimeMillis());
                msg.setBody(events);
                msgBroker.routeMessageToService(msg, null);
			}
			
		});
		stmt.start();
		listeningStatements.put(epl,stmt);
		logger.info("在容器["+providerName+"]上监听事件[EPL="+epl+"]完毕");
	}
}
