package smartx.publics.clustercompute.service;

import smartx.publics.clustercompute.ClusterComputeNodeManager;
import smartx.publics.clustercompute.vo.ClusterComputeSubTask;

public class ClusterComputeNodeServiceImpl implements ClusterComputeNodeService {

	@Override
	public boolean executeSubTask(String subTaskCode,
			ClusterComputeSubTask subTask, String returnURL) {
		return ClusterComputeNodeManager.getInstance().executeSubTask(
				subTaskCode, subTask, returnURL);
	}

	@Override
	public boolean cancelSubTask(String subTaskCode, String returnURL) {

		return false;
	}

}
