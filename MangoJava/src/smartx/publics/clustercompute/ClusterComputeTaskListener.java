package smartx.publics.clustercompute;

import java.util.Map;

import smartx.publics.clustercompute.vo.ClusterComputeTaskResult;

/**
 * 集群计算任务监听器
 * 
 * @author teddyxu
 * 
 */
public interface ClusterComputeTaskListener {
	/**
	 * 任务完成
	 * @param taskCode
	 *            任务执行码
	 * @param returnCode
	 *            返回状态码
	 * @param taskResult
	 *            返回结果
	 */
	public void taskComplete(String taskCode, ClusterComputeTaskResult result);
}
