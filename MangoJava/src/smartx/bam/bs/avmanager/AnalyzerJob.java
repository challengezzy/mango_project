package smartx.bam.bs.avmanager;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import smartx.framework.common.vo.NovaLogger;

public class AnalyzerJob implements Job {
	
	private final Logger logger = NovaLogger.getLogger(this.getClass());

	@Override
	public void execute(JobExecutionContext arg) throws JobExecutionException {
		try {
			
			JobDetail jd = arg.getJobDetail();
			String analyzerCode = jd.getName();
			logger.debug("定时任务：=============分析视图JOB启动:"+analyzerCode+"===================");
			if(analyzerCode != null ){
				Analyzer analyzer = AnalyzeViewManager.analyzerMap.get(analyzerCode.toUpperCase());
				if(analyzer != null){
					analyzer.analyze();
				}
			}
			
			logger.debug("定时任务：=============分析视图JOB结束:"+analyzerCode+"===================");
		} catch (Exception e) {
			logger.error(" ", e);
		} finally {
		}
	}

}
