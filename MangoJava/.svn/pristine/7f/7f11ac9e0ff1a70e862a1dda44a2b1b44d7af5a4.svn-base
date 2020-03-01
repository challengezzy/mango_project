package smartx.publics.clustercompute.vo;

import java.util.List;
import java.util.Map;

/**
 * 集群计算任务
 * @author teddyxu
 *
 */
public interface ClusterComputeTask {
	
	/**
	 * 获取任务超时
	 * @return 任务超时的毫秒数，若小于0视为无限
	 */
	public int getTimeout();

	/**
	 * 生成子节点任务
	 * @param numOfNode 节点数
	 * @return
	 */
	public List<ClusterComputeSubTask> generateSubTasks(int numOfNode);
	
	/**
	 * 合并生成任务执行结果
	 * @param subTaskResults 子任务的执行结果
	 * @return
	 */
	public Map<String,?> generateTaskResult(List<Map<String, ?>> subTaskResults);
	
}
