package smartx.publics.clustercompute;

import org.apache.log4j.Logger;

import smartx.framework.common.ui.NovaRemoteServiceFactory;
import smartx.framework.common.vo.NovaLogger;
import smartx.framework.common.vo.NovaRemoteException;
import smartx.publics.clustercompute.service.ClusterComputeNodeService;
import smartx.publics.clustercompute.vo.ClusterComputeSubTask;

public class ClusterComputeNode {
	private Logger logger = NovaLogger.getLogger(this.getClass());
	private String nodeId;
	private String remoteCallUrl;

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getRemoteCallUrl() {
		return remoteCallUrl;
	}

	public void setRemoteCallUrl(String remoteCallUrl) {
		this.remoteCallUrl = remoteCallUrl;
	}

	public boolean executeSubTask(String subTaskCode, ClusterComputeSubTask subTask, String returnURL) {
		try {
			ClusterComputeNodeService service = (ClusterComputeNodeService) NovaRemoteServiceFactory.getInstance()
					.lookUpService(ClusterComputeNodeService.class, remoteCallUrl);
			return service.executeSubTask(subTaskCode, subTask, returnURL);
		} catch (Exception e) {
			logger.error("", e);
			return false;
		}
	}
}
