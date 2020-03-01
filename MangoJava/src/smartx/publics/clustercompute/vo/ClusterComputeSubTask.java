/**
 * 
 */
package smartx.publics.clustercompute.vo;

import java.io.Serializable;
import java.util.Map;

/**
 * 节点计算任务
 * @author teddyxu
 *
 */
public class ClusterComputeSubTask implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1333691372171385331L;
	/**
	 * 执行器类名
	 */
	private String executorClassName;
	/**
	 * 执行参数
	 */
	private Map<String, ?> paramMap;
	
	public String getExecutorClassName() {
		return executorClassName;
	}
	public void setExecutorClassName(String executorClassName) {
		this.executorClassName = executorClassName;
	}
	public Map<String, ?> getParamMap() {
		return paramMap;
	}
	public void setParamMap(Map<String, ?> paramMap) {
		this.paramMap = paramMap;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,?> execute() throws Exception{
		Class<ClusterComputeSubTaskExecutor> taskClass = (Class<ClusterComputeSubTaskExecutor>) Class.forName(executorClassName);
		ClusterComputeSubTaskExecutor executor = taskClass.newInstance();
		return executor.execute(paramMap);
	}
}
