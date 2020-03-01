package smartx.publics.cep.bs;

import java.util.Map;

import org.apache.log4j.Logger;

import smartx.framework.common.vo.NovaLogger;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;

/**
 * 事件输入管理
 * @author gxlx
 *
 */
public class StreamEventInputManager {
	private Logger logger = NovaLogger.getLogger(this);
	/**
	 * 输入新事件
	 * @param moduleName
	 * @param event
	 * @param eventTypeName
	 */
	public void sendEvent(String providerName, Map<String, Object> event, String eventTypeName){
		logger.info("容器["+providerName+"]发送新事件[type="+eventTypeName+"]");
		logger.debug(event);
		EPServiceProvider epService = EPServiceProviderManager.getProvider(providerName);
		epService.getEPRuntime().sendEvent(event, eventTypeName);
		logger.info("容器["+providerName+"]发送新事件[type="+eventTypeName+"]完毕");
	}
}
