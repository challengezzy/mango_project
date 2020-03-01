package smartx.publics.datatask;

import org.dom4j.Element;

/**
 *@author zzy
 *@date Sep 13, 2011
 *@description 数据任务执行接口，所有自定义数据预处理任务必须实现此接口
 **/
public interface DataTaskExecuteIFC {

	/**
	 * 执行数据预处理任务，具体执行过程在子类中实现
	 * @param task 自定义节点的XML内容
	 */
	public void dataTaskExec(Element task,DataTaskExecThread mainThread) throws Exception;
}
