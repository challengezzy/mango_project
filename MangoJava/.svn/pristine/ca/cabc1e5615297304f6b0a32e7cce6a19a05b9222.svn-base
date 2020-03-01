/**
 * 
 */
package smartx.bam.bs.alertmessage;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.vo.NovaLogger;

/**
 * @author caohenghui
 *
 */
public class ArchiveAlertMessageJob implements Job {

	private final Logger logger = NovaLogger.getLogger(this.getClass());
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {

		CommDMO dmo = new CommDMO();
		try {
			logger.debug("定时任务：=============开始归档告警消息===================");

			dmo.callProcedureByDS(null,"P_BAM_ARCHIVEALL_ALERTMSG", null);

			logger.debug("定时任务：=============归档告警消息成功===================");
		} catch (Exception e) {
			logger.error("归档告警消息失败", e);
		} finally {
			dmo.releaseContext();
		}
	}

}
