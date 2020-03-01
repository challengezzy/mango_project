package smartx.publics.clustercompute.sample;

import smartx.publics.clustercompute.ClusterComputeManager;
import smartx.publics.clustercompute.ClusterComputeTaskListener;
import smartx.publics.clustercompute.vo.ClusterComputeTaskResult;

public class SampleClusterCompute {
	public SampleClusterCompute(){
		Thread h = new Thread(){
			public void run(){
				try {
					sleep(5000);
					ClusterComputeManager.getInstance().startClusterComputeTask(new SampleClusterComputeTask(), new ClusterComputeTaskListener() {
						
						@Override
						public void taskComplete(String taskCode, ClusterComputeTaskResult result) {
							System.out.println("收到任务回复[taskcode="+taskCode+",returnCode="+result.getReturnCode()+"]");
							System.out.println(result.getResultMap());
						}
					});
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		h.start();
	}
}
