/**
 * 
 */
package smartx.bam.bs.scenariomnager;

import org.apache.log4j.Logger;

import smartx.framework.common.vo.NovaLogger;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

/**
 * @author caohenghui
 * 
 */
public class RuleListener implements UpdateListener {

	private Logger logger = NovaLogger.getLogger(this.getClass());

	private String ruleCode = null;
	
	private MessageDispatcher msgDispatcher;

	public RuleListener(String ruleCode,MessageDispatcher msgDispatcher) {
		this.ruleCode = ruleCode;
		this.msgDispatcher = msgDispatcher;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.espertech.esper.client.UpdateListener#update(com.espertech.esper.
	 * client.EventBean[], com.espertech.esper.client.EventBean[])
	 */
	@Override
	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		
		for (EventBean newEvent : newEvents) {
			try {
				MessageTask task = new MessageTask(newEvent, null,this.ruleCode);
				msgDispatcher.addTask(task);
			} catch (Exception e) {
				logger.debug("", e);
			}
		}

		if (oldEvents != null && oldEvents.length > 0) {

		}
	}
}
