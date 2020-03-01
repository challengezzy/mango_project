package smartx.publics.clustercompute;

import java.util.ArrayList;
import java.util.Date;

import smartx.publics.clustercompute.vo.ClusterComputeTask;

/**
 * 集群计算任务实例
 * 
 * @author teddyxu
 * 
 */
public class ClusterComputeTaskInstance {
	private String code;
	private ClusterComputeTask task;
	private Date startTime;
	private ArrayList<ClusterComputeSubTaskInstance> subTaskInstanceList = new ArrayList<ClusterComputeSubTaskInstance>();
	private ClusterComputeTaskListener taskListener;

	public ArrayList<ClusterComputeSubTaskInstance> getSubTaskInstanceList() {
		return subTaskInstanceList;
	}

	public void setSubTaskInstanceList(ArrayList<ClusterComputeSubTaskInstance> subTaskInstanceList) {
		this.subTaskInstanceList = subTaskInstanceList;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public ClusterComputeTask getTask() {
		return task;
	}

	public void setTask(ClusterComputeTask task) {
		this.task = task;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public ClusterComputeTaskListener getTaskListener() {
		return taskListener;
	}

	public void setTaskListener(ClusterComputeTaskListener taskListener) {
		this.taskListener = taskListener;
	}
}
