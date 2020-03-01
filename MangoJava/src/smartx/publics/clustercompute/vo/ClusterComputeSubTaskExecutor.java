package smartx.publics.clustercompute.vo;

import java.util.Map;

/**
 * 集群计算子任务执行器接口
 * @author teddyxu
 *
 */
public interface ClusterComputeSubTaskExecutor {
	/**
	 * 执行任务逻辑
	 * @return 
	 * @throws Exception
	 */
	public Map<String,?> execute(Map<String,?> params) throws Exception;
}
