package smartx.bam.bs.avmanager;

import org.apache.log4j.Logger;

import smartx.bam.vo.SysConst;
import smartx.framework.common.job.bs.JobServer;
import smartx.framework.common.vo.NovaLogger;

public class AnalyzerJobService {
	
	private final Logger logger = NovaLogger.getLogger(this.getClass());
	
	public void stopAnalyzerJobByName(String jobName){
		try{
			JobServer.removeJob(jobName, SysConst.ANALYZER_GROUP);
		}catch(Exception e){
			logger.debug("",e);
		}
	}
	
	public void startAnalyzerJobByName(String jobName,String jobExp){
		try{
			JobServer.addCronJob(jobName,SysConst.ANALYZER_GROUP,AnalyzerJob.class.getName(),jobExp);
		}catch(Exception e){
			logger.debug("",e);
		}
	}
}
