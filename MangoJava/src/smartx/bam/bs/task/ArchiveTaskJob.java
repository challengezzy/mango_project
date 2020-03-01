package smartx.bam.bs.task;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import smartx.bam.vo.DatabaseConst;
import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.vo.NovaLogger;

/**
 * <li>Title: ArchiveTaskJob.java</li>
 * <li>Description: 对BAM任务及任务相关信息进行归档</li>
 * <li>Project: bam</li>
 * <li>Copyright: Copyright (c) 2011</li>
 * @Company: lianzhi
 * @author zhangzy
 * @version 1.0
 */
public class ArchiveTaskJob implements Job
{
	private final Logger logger = NovaLogger.getLogger(this.getClass());
	
	public void execute(JobExecutionContext ctx) throws JobExecutionException
	{
		CommDMO dmo = new CommDMO();
		try
		{
			logger.debug("定时任务：=============开始归档任务信息===================");
			
			dmo.callProcedureByDS(DatabaseConst.DATASOURCE_DEFAULT, "p_archiveall_task", null);
			
			logger.debug("定时任务：=============归档任务信息成功===================");
		}
		catch (Exception e)
		{
			logger.error("归档任务信息失败",e);
		}
		finally{
			dmo.releaseContext();
		}
	}

}
