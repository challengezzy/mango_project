/**
 * 
 */
package smartx.bam.bs.scenariomnager;

import com.espertech.esper.client.EventBean;

/**
 * @author caohenghui
 *
 */
public class MessageTask {
	
	private EventBean newEvent;
	
	private EventBean oldEvent;
	
	private String ruleCode;

	public MessageTask(EventBean newEvent,EventBean oldEvent,String ruleCode){
		this.newEvent = newEvent;
		this.ruleCode = ruleCode;
	}

	public EventBean getNewEvent() {
		return newEvent;
	}

	public void setNewEvent(EventBean newEvent) {
		this.newEvent = newEvent;
	}

	public EventBean getOldEvent() {
		return oldEvent;
	}

	public void setOldEvent(EventBean oldEvent) {
		this.oldEvent = oldEvent;
	}

	public String getRuleCode() {
		return ruleCode;
	}

	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}

}
