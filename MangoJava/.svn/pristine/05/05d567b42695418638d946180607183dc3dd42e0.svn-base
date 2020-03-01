package smartx.publics.clustercompute;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;

import smartx.framework.common.bs.SysConst;
import smartx.framework.common.vo.NovaLogger;
import smartx.framework.metadata.bs.NovaServerEnvironment;
import smartx.publics.clustercompute.vo.ClusterComputeConst;
import smartx.publics.clustercompute.vo.ClusterComputeSubTask;
import smartx.publics.clustercompute.vo.ClusterComputeTask;
import smartx.publics.clustercompute.vo.ClusterComputeTaskResult;

/**
 * 集群计算管理器
 * 
 * @author teddyxu
 * 
 */
public class ClusterComputeManager {
	private static ClusterComputeManager instance = new ClusterComputeManager();
	private Logger logger = NovaLogger.getLogger(this.getClass());

	private ConcurrentHashMap<String, ClusterComputeTaskInstance> taskInstanceMap = new ConcurrentHashMap<String, ClusterComputeTaskInstance>();

	private ConcurrentHashMap<String, ClusterComputeSubTaskInstance> subTaskInstanceMap = new ConcurrentHashMap<String, ClusterComputeSubTaskInstance>();

	private ArrayList<ClusterComputeNode> nodeList;// 配置文件加载

	private String returnURL;// 配置文件加载

	private boolean isInited = false;

	private ClusterComputeManager() {
		// todo此处加载配置文件
		Document configDoc = (Document) NovaServerEnvironment.getInstance().get(SysConst.KEY_SYSTEMCONFIGFILE);
		if (configDoc == null) {
			logger.error("无法读取配置文件");
			return;
		}
		Element serviceElement = configDoc.getRootElement().getChild("clusterComputeService");
		if (serviceElement == null) {
			logger.error("无法读取集群计算服务配置信息");
			return;
		}
		returnURL = serviceElement.getAttributeValue("returnURL");
		if (returnURL == null) {
			logger.error("无法读取集群计算服务端URL");
			return;
		} else
			logger.debug("读取集群计算服务端URL：" + returnURL);
		returnURL += "/RemoteCallServlet";
		nodeList = new ArrayList<ClusterComputeNode>();
		for (Object nodeObj : serviceElement.getChildren()) {
			Element nodeElement = (Element) nodeObj;
			ClusterComputeNode node = new ClusterComputeNode();
			String nodeId = nodeElement.getAttributeValue("id");
			node.setNodeId(nodeId);
			String nodeUrl = nodeElement.getAttributeValue("url");
			if (nodeUrl == null) {
				logger.error("无法读取集群节点[" + nodeId + "]的URL");
				continue;
			}
			node.setRemoteCallUrl(nodeUrl + "/RemoteCallServlet");
			logger.debug("加载集群节点[id=" + nodeId + ",url=" + nodeUrl + "]");
			nodeList.add(node);
		}
		isInited = true;
	}

	public static ClusterComputeManager getInstance() {
		return instance;
	}

	/**
	 * 启动集群计算任务
	 * 
	 * @param task
	 * @param listener
	 * @return 任务执行码
	 * @throws Exception
	 */
	public String startClusterComputeTask(ClusterComputeTask task, ClusterComputeTaskListener listener)
			throws Exception {
		if (!isInited)
			throw new Exception("集群计算未完成初始化，无法启动任务");
		logger.debug("启动集群计算任务[" + task + "]");
		ClusterComputeTaskInstance ins = new ClusterComputeTaskInstance();
		String taskcode = "TASK_" + UUID.randomUUID().toString();
		logger.debug("生成任务执行码[" + taskcode + "]");
		ins.setTask(task);
		ins.setCode(taskcode);
		ins.setStartTime(new Date());
		ins.setTaskListener(listener);
		int nodeCount = nodeList.size();
		logger.debug("拆分任务为" + nodeCount + "个子任务");
		List<ClusterComputeSubTask> taskList = task.generateSubTasks(nodeCount);
		int i = 0;
		for (ClusterComputeSubTask subTask : taskList) {
			// 开始分配子任务
			// 寻找一个最合适的节点执行任务，目前简单地使用平均分配的办法
			boolean b = false;
			int init_i = i;// 记录一下搜索前的节点序号，如果重复了，说明全部全部遍历了仍然无法找到可运行节点
			String subtaskcode = "";
			ClusterComputeNode node = null;
			while (!b) {// 执行失败，则循环寻找一下节点，直到全部遍历完
				node = nodeList.get(i);
				subtaskcode = "SUBTASK_" + UUID.randomUUID().toString();
				logger.debug("生成节点任务执行码[" + subtaskcode + "]，执行节点[" + node.getNodeId() + "]");
				b = node.executeSubTask(subtaskcode, subTask, returnURL);
				if (!b)
					logger.debug("命令节点[" + node.getNodeId() + "]执行任务失败");
				// 移动到下一个节点
				i++;
				if (i >= nodeList.size())
					i = 0;
				if (!b && init_i == i) {// 回到原点了
					throw new Exception("任务执行失败，无可用的节点");
				}
			}
			// 分配成功，则建立子任务的执行实例
			ClusterComputeSubTaskInstance subIns = new ClusterComputeSubTaskInstance();
			subIns.setCode(subtaskcode);
			subIns.setNode(node);
			subIns.setStartTime(new Date());
			subIns.setSubTask(subTask);
			subIns.setTaskInstance(ins);
			ins.getSubTaskInstanceList().add(subIns);
			subTaskInstanceMap.put(subtaskcode, subIns);
		}
		taskInstanceMap.put(taskcode, ins);
		logger.debug("启动集群计算任务[" + task + "]成功");
		return taskcode;
	}

	public void makeSubTaskResponse(String subTaskCode, ClusterComputeTaskResult result) {
		logger.debug("收到子节点任务[" + subTaskCode + "]的执行回复[returnCode=" + result.getReturnCode() + "]");
		ClusterComputeSubTaskInstance subIns = subTaskInstanceMap.get(subTaskCode);
		if (subIns == null) {
			logger.warn("不存在执行中的子节点任务[" + subTaskCode + "]，直接返回");
			return;
		}
		int returnCode = result.getReturnCode();
		subIns.setResponseTime(new Date());
		subIns.setReturnCode(returnCode);
		subIns.setResultMap(result.getResultMap());
		logger.debug("子节点任务[" + subTaskCode + "]执行耗时"
				+ (subIns.getResponseTime().getTime() - subIns.getStartTime().getTime()) + "ms");
		// 清理子任务实例
		subTaskInstanceMap.remove(subTaskCode);
		if (returnCode == ClusterComputeConst.SUBTASK_RESPONSECODE_OK) {
			// 成功返回，判断是否所有子任务都已成功返回
			ClusterComputeTaskInstance taskInstance = subIns.getTaskInstance();
			boolean isAllFinished = true;
			ArrayList<Map<String, ?>> resultMapList = new ArrayList<Map<String, ?>>();
			for (ClusterComputeSubTaskInstance subTaskInstance : taskInstance.getSubTaskInstanceList()) {
				if (subTaskInstance.getReturnCode() != ClusterComputeConst.SUBTASK_RESPONSECODE_OK) {
					isAllFinished = false;
					break;
				}
				resultMapList.add(subTaskInstance.getResultMap());
			}
			if (isAllFinished) {
				logger.debug("集群计算任务[" + taskInstance.getCode() + "]执行完成，耗时"
						+ (taskInstance.getStartTime().getTime() - System.currentTimeMillis()) + "ms");
				// 拼装执行结果
				Map<String, ?> taskResultMap = taskInstance.getTask().generateTaskResult(resultMapList);
				ClusterComputeTaskResult taskResult = new ClusterComputeTaskResult();
				taskResult.setReturnCode(ClusterComputeConst.TASK_RESPONSECODE_OK);
				taskResult.setResultMap(taskResultMap);
				taskInstance.getTaskListener().taskComplete(taskInstance.getCode(), taskResult);
				// 清理任务实例
				taskInstanceMap.remove(taskInstance.getCode());
			}
		} else if (returnCode == ClusterComputeConst.SUBTASK_RESPONSECODE_ERROR) {
			// 返回失败，则整个任务都标识为失败
			ClusterComputeTaskInstance taskInstance = subIns.getTaskInstance();
			logger.debug("集群计算任务[" + taskInstance.getCode() + "]执行失败");
			ClusterComputeTaskResult taskResult = new ClusterComputeTaskResult();
			taskResult.setReturnCode(ClusterComputeConst.TASK_RESPONSECODE_ERROR);
			taskResult.setExceptionDetail(result.getExceptionDetail());
			taskInstance.getTaskListener().taskComplete(taskInstance.getCode(), taskResult);
			// 清理任务实例
			taskInstanceMap.remove(taskInstance.getCode());
			// 同时清理所有子任务实例
			for (ClusterComputeSubTaskInstance subTaskInstance : taskInstance.getSubTaskInstanceList()) {
				subTaskInstanceMap.remove(subTaskInstance.getCode());
			}
		}

	}
}
