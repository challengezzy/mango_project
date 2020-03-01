package smartx.publics.clustercompute.sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import smartx.publics.clustercompute.vo.ClusterComputeSubTask;
import smartx.publics.clustercompute.vo.ClusterComputeTask;

public class SampleClusterComputeTask implements ClusterComputeTask {

	@Override
	public int getTimeout() {
		return -1;
	}

	@Override
	public List<ClusterComputeSubTask> generateSubTasks(int numOfNode) {
		ArrayList<ClusterComputeSubTask> result = new ArrayList<ClusterComputeSubTask>();
		for(int i=0;i<numOfNode;i++){
			ClusterComputeSubTask task = new ClusterComputeSubTask();
			task.setExecutorClassName("smartx.publics.clustercompute.sample.SampleClusterComputeSubTaskExecutor");
			HashMap<String, Integer> paramMap = new HashMap<String, Integer>();
			paramMap.put("nodeCount", 1);
			task.setParamMap(paramMap);
			result.add(task);
		}
		return result;
	}

	@Override
	public Map<String, ?> generateTaskResult(List<Map<String, ?>> subTaskResults) {
		Integer r = 0;
		for(Map<String, ?> subMap:subTaskResults){
			Integer x = (Integer) subMap.get("nodeCount");
			r+=x;
		}
		Map<String, Integer> result = new HashMap<String, Integer>();
		result.put("nodeCount", r);
		return result;
	}

}
