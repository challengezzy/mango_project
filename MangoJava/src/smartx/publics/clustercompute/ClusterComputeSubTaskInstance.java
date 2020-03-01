package smartx.publics.clustercompute;

import java.util.Date;
import java.util.Map;

import smartx.publics.clustercompute.vo.ClusterComputeSubTask;

/**
 * 集群计算节点子任务实例
 * 
 * @author teddyxu
 * 
 */
public class ClusterComputeSubTaskInstance {
	private String code;
	private ClusterComputeNode node;
	private Date startTime;
	private int returnCode;
	private Map<String, ?> resultMap;
	private ClusterComputeTaskInstance taskInstance;
	private ClusterComputeSubTask subTask;
	private Date responseTime;

	public Date getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(Date responseTime) {
		this.responseTime = responseTime;
	}

	public ClusterComputeTaskInstance getTaskInstance() {
		return taskInstance;
	}

	public void setTaskInstance(ClusterComputeTaskInstance taskInstance) {
		this.taskInstance = taskInstance;
	}

	public ClusterComputeSubTask getSubTask() {
		return subTask;
	}

	public void setSubTask(ClusterComputeSubTask subTask) {
		this.subTask = subTask;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public ClusterComputeNode getNode() {
		return node;
	}

	public void setNode(ClusterComputeNode node) {
		this.node = node;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public int getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}

	public Map<String, ?> getResultMap() {
		return resultMap;
	}

	public void setResultMap(Map<String, ?> resultMap) {
		this.resultMap = resultMap;
	}

}
