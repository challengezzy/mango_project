package smartx.publics.clustercompute.service;

import smartx.framework.common.ui.NovaRemoteCallServiceIfc;
import smartx.publics.clustercompute.vo.ClusterComputeSubTask;

/**
 * 集群计算节点端服务
 * @author teddyxu
 *
 */
public interface ClusterComputeNodeService extends NovaRemoteCallServiceIfc {


	/**
	 * 执行节点任务
	 * @param subTaskCode 执行编码，全局唯一
	 * @param subTask
	 * @return 是否接受任务
	 */
	public boolean executeSubTask(String subTaskCode, ClusterComputeSubTask subTask, String returnURL);
	
	
	/**
	 * 取消一个节点任务（在任务执行前，如果已开始执行，则一律返回无法取消）
	 * @param subTaskCode
	 * @return 是否取消成功
	 */
	public boolean cancelSubTask(String subTaskCode, String returnURL);
}
