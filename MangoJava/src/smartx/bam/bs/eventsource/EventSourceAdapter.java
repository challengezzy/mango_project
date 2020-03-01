/**
 * 
 */
package smartx.bam.bs.eventsource;

import org.dom4j.Document;

/**
 * @author sky
 * Description 事件源适配器
 */
public interface EventSourceAdapter
{
	/**
	 * 启动事件源
	 */
	public void start(Document config);
	
	/**
	 * 停止事件源
	 */
	public void stop();
}
