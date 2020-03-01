/**
 * 
 */
package smartx.publics.clustercompute;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;

import smartx.framework.common.ui.NovaRemoteServiceFactory;
import smartx.framework.common.vo.NovaLogger;
import smartx.framework.common.vo.NovaRemoteException;
import smartx.publics.clustercompute.service.ClusterComputeServerService;
import smartx.publics.clustercompute.vo.ClusterComputeConst;
import smartx.publics.clustercompute.vo.ClusterComputeSubTask;
import smartx.publics.clustercompute.vo.ClusterComputeTaskResult;

/**
 * 集群计算子节点控制器
 * 
 * @author teddyxu
 * 
 */
public class ClusterComputeNodeManager {
	private Logger logger = NovaLogger.getLogger(this.getClass());
	private static ClusterComputeNodeManager instance = new ClusterComputeNodeManager();
	private static final int MAXNUMOFSUBTASK = 100;
	ConcurrentLinkedQueue<String> subTaskQueue = new ConcurrentLinkedQueue<String>();
	Map<String, ClusterComputeSubTask> subTaskMap = new HashMap<String, ClusterComputeSubTask>();
	Map<String, String> retrurnURLMap = new HashMap<String, String>();
	Map<String, ClusterComputeTaskResult> pendingResultMap = new ConcurrentHashMap<String, ClusterComputeTaskResult>();// 未发送成功的回复

	private SubTaskExecuteThread subTaskExecuteThread = new SubTaskExecuteThread();

	private ClusterComputeNodeManager() {
		subTaskExecuteThread.start();
	}

	public static ClusterComputeNodeManager getInstance() {
		return instance;
	}

	public boolean executeSubTask(String subTaskCode, ClusterComputeSubTask subTask, String returnURL) {
		logger.debug("接受到集群计算子任务[" + subTaskCode + "]");
		if (subTaskQueue.size() > MAXNUMOFSUBTASK) {
			logger.warn("节点超过子任务执行数量上线，拒绝新任务[" + subTaskCode + "]");
			return false;
		}
		subTaskMap.put(subTaskCode, subTask);
		retrurnURLMap.put(subTaskCode, returnURL);
		subTaskQueue.add(subTaskCode);
		return true;
	}

	protected void removeSubTaskInfo(String subTaskCode) {
		subTaskMap.remove(subTaskCode);
		retrurnURLMap.remove(subTaskCode);
	}

}

class SubTaskExecuteThread extends Thread {
	private Logger logger = NovaLogger.getLogger(this.getClass());

	public void run() {
		while (true) {
			String subTaskCode = ClusterComputeNodeManager.getInstance().subTaskQueue.poll();
			if (subTaskCode != null) {
				logger.debug("开始执行子任务[" + subTaskCode + "]");
				long starttime = System.currentTimeMillis();
				ClusterComputeSubTask subTask = ClusterComputeNodeManager.getInstance().subTaskMap.get(subTaskCode);
				String returnURL = ClusterComputeNodeManager.getInstance().retrurnURLMap.get(subTaskCode);
				ClusterComputeTaskResult result = new ClusterComputeTaskResult();
				try {
					Map<String, ?> resultMap = subTask.execute();
					result.setReturnCode(ClusterComputeConst.SUBTASK_RESPONSECODE_OK);
					result.setResultMap(resultMap);
					long endtime = System.currentTimeMillis();
					logger.debug("子任务[" + subTaskCode + "]执行成功，耗时" + (endtime - starttime) + "ms");
				} catch (Exception e) {
					logger.error("子任务[" + subTaskCode + "]执行失败", e);
					result.setReturnCode(ClusterComputeConst.SUBTASK_RESPONSECODE_ERROR);
					result.setExceptionDetail(e.getMessage());
				}
				try {
					logger.debug("向服务端回复子任务执行结果[" + subTaskCode + "]");
					makeSubTaskResponse(subTaskCode, result, returnURL);
					// 执行成功，删除任务信息
					ClusterComputeNodeManager.getInstance().removeSubTaskInfo(subTaskCode);
				} catch (Exception e) {
					logger.error("调用回应失败", e);
					// 调用失败的Result放置在pending队列中，处理方式还未实现
					ClusterComputeNodeManager.getInstance().pendingResultMap.put(subTaskCode, result);
				}
			}
			try {
				sleep(100);
			} catch (InterruptedException e) {
			}

		}
	}

	private void makeSubTaskResponse(String subTaskCode, ClusterComputeTaskResult result, String returnURL)
			throws NovaRemoteException, Exception {
		ClusterComputeServerService service = (ClusterComputeServerService) NovaRemoteServiceFactory.getInstance()
				.lookUpService(ClusterComputeServerService.class, returnURL);
		service.makeSubTaskResponse(subTaskCode, result);
	}
}
