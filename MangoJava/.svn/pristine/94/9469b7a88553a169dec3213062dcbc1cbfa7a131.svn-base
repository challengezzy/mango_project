/**
 * 
 */
package smartx.bam.bs.bvmanager;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import smartx.framework.common.vo.NovaLogger;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.EventType;
import com.espertech.esper.client.StatementAwareUpdateListener;

/**
 * @author sky
 *
 */
public class BvUpdateListener implements StatementAwareUpdateListener{
	private Logger logger = NovaLogger.getLogger(this);

	private BvPersistentWriter bvPersistentWriter;
	
	public BvUpdateListener(BvPersistentWriter bvPersistentWriter)
	{
		this.bvPersistentWriter = bvPersistentWriter;
	}
	@Override
	public void update(EventBean[] newEvents, EventBean[] oldEvents, EPStatement statement,
			EPServiceProvider provider) {
		try{
			if(newEvents != null){
				for(EventBean newEvent : newEvents){
					BvPersitentTask task = new BvPersitentTask();
					EventType eventType = newEvent.getEventType();
					String windowName = eventType.getName();
					//如果窗口不属于业务视图的，则不持久化
					if(!BusinessViewManager.bvTableMap.containsKey(windowName))
						continue;
					List<String> values = new ArrayList<String>();
					List<String> properties = new ArrayList<String>();
					for(String propertyName : eventType.getPropertyNames()){
						values.add(newEvent.get(propertyName)+"");
						properties.add(propertyName);
					}
					task.setWindowName(windowName);
					task.setFieldLists(properties);
					task.setValues(values);
					bvPersistentWriter.addBvPersistenTaskQueue(task);
				}
			}
		}catch(Exception e){
			logger.error("",e);
		}
	}


}
