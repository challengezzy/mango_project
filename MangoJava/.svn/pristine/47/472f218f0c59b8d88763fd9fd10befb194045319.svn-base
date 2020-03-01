package smartx.bam.bs.task;

import org.apache.log4j.Logger;

import smartx.bam.bs.systemsetting.SysSettingManager;
import smartx.bam.vo.DatabaseConst;
import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.utils.StringUtil;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaLogger;
import smartx.publics.email.EmailService;

/**
 * 任务管理服务类
 * @author zzy
 *
 */
public class TaskManager {
	
	protected Logger logger = NovaLogger.getLogger(this.getClass());
	
	private EmailService mailService;//邮件发送服务类
	
	/**
	 * 更新任务备注信息
	 * @param datasourceName
	 * @param comment
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	public int updateTaskComment(String datasourceName, String comment,String taskId) throws Exception{
		CommDMO dmo = new CommDMO();
		try{
			String sql1 = "update bam_task t set lastupdated=sysdate where t.id = ?";// + taskId;
			//更新CLOB字段
			dmo.executeUpdateClobByDS(datasourceName, "MEMO", "BAM_TASK", "ID="+taskId, comment);
			//更新日期信息
			
			int i = dmo.executeUpdateByDS(datasourceName, sql1,taskId);
			
			dmo.commit(datasourceName);
			return i;
			
		}catch (Exception e) {
			logger.error("更新任务表备注信息错误!", e);
			dmo.rollback(datasourceName);
			throw e;
		}finally{
			dmo.releaseContext(datasourceName);
		}
	}
	
	/**
	 * 保存任务观察者信息
	 * @param datasourceName
	 * @param taskId
	 * @param users
	 * @return
	 * @throws Exception
	 */
	public int[] saveTaskWatcher(String datasourceName,String taskId, String[] users) throws Exception{
		CommDMO dmo = new CommDMO();
		try{
			String[] sqls = new String[users.length];
			for(int i=0; i<users.length; i++){
				String sql = " insert into bam_taskwatcher values(s_bam_taskwatcher.nextval,'" + taskId + "','" + users[i] + "')";
				sqls[i] = sql;
			}
			
			int[] nums = dmo.executeBatchByDS(datasourceName, sqls);
			dmo.commit(datasourceName);
			
			return nums;
		}catch (Exception e) {
			logger.error("保存任务观察者信息错误!", e);
			dmo.rollback(datasourceName);
			throw e;
		}finally{
			dmo.releaseContext(datasourceName);
		}
	}
	
	/**
	 * 为此任务发送邮件给相关用户
	 * @param taskId
	 * @param subject
	 * @param body
	 */
	public void sendTaskInfoEmail(String taskId,String subject,String body) {
		CommDMO dmo = new CommDMO();
		try {
			//初始化邮件服务相关信息
			this.initEmailInfo();
			
			String searchSQL = "select taskid,username,email from v_bam_taskemail where taskid = ?";

			HashVO[] tempVos = dmo.getHashVoArrayByDS(DatabaseConst.DATASOURCE_DEFAULT, searchSQL,taskId);

			for (HashVO vo : tempVos) {
				String username = vo.getStringValue("USERNAME");
				String toEmail = vo.getStringValue("EMAIL");
				String mailBody = username + ",你好: \n" + body;
				
				if (toEmail == null || toEmail.trim().equals("")) {
					logger.info("任务观察者【" + username + "】的EMAIL地址为空，不能向其发送邮件提醒！");
					return;
				} else {
					if ( mailService != null ) {
						mailService.sendMail(toEmail, subject, mailBody);
					}
				}
			}

		} catch (Exception e) {
			logger.error("项任务相关用户发送邮件出错!", e);
		} finally {
			dmo.releaseContext(DatabaseConst.DATASOURCE_DEFAULT);
		}
	}
	
	private void initEmailInfo(){
		try{
			//邮件发送服务信息设置
			String smtp_host = SysSettingManager.getInstance().findSysSettingByKey("SMTP_HOST");
			String smtp_user = SysSettingManager.getInstance().findSysSettingByKey("SMTP_USER");
			String smtp_pwd = SysSettingManager.getInstance().findSysSettingByKey("SMTP_PWD");
			String sender_email = SysSettingManager.getInstance().findSysSettingByKey("SENDER_EMAIL");
		    
			if (!StringUtil.isEmpty(smtp_host) && !StringUtil.isEmpty(smtp_user)
					&& !StringUtil.isEmpty(smtp_pwd)
					&& !StringUtil.isEmpty(sender_email)) {
				mailService = new EmailService(smtp_host, smtp_user, smtp_pwd, sender_email,false);
			}
			
		}catch(Exception e){
			logger.info("获取邮件配置时出错!",e);
		}
	}

}
