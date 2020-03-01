package smartx.publics.message;

import org.apache.log4j.Logger;

import smartx.framework.common.vo.NovaLogger;
import flex.messaging.MessageBroker;
import flex.messaging.messages.AsyncMessage;
import flex.messaging.util.UUIDUtils;

/**
 *@author zzy
 *@date Sep 9, 2011
 *@description 消息推送服务，由服务端发送给flex客户端
 **/
public class MessagePushService {
	
	private static Logger logger = NovaLogger.getLogger(MessagePushService.class);
	
	private static MessageBroker msgBroker = MessageBroker.getMessageBroker(null);
	
	public static String MESSAGEDESTINATION = "smartx_message_push";
	
	public static MessagePushService pushService;
	
	public static MessagePushService getInstance(){
		if(pushService == null)
			pushService = new MessagePushService();
		
		return pushService;
	}
	
	/**
	 * 推送一条消息到FLEX客户端
	 * @param subtopic
	 * @param messageBody
	 * @param clientId
	 */
	public static void pushMessage(String subtopic,Object messageBody,String clientId){
		AsyncMessage msg = new AsyncMessage();
		
		try{
			msg.setDestination(MessagePushService.MESSAGEDESTINATION);
			msg.setClientId(clientId);
			msg.setTimestamp(System.currentTimeMillis());
			msg.setMessageId(UUIDUtils.createUUID());
		
			msg.setHeader("DSSubtopic", subtopic);
			msg.setBody(messageBody);
		
		//You can call this method in order to send a message from your code into the message routing system. 
		//The message is routed to a service that is defined to handle messages of this type. 
		//Once the service is identified, the destination property of the message is used to find a destination configured for that service. 
		//The adapter defined for that destination is used to handle the message.
			msgBroker.routeMessageToService(msg, null);
		}catch (Exception e) {
			logger.error("消息推送时发生异常！", e);
		}
		logger.debug("消息推送成功:subtopic=" + subtopic + " ;messageBody=" + messageBody.toString());
	}
}
