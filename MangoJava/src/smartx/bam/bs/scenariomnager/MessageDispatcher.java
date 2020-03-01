/**
 * 
 */
package smartx.bam.bs.scenariomnager;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import smartx.bam.bs.systemsetting.SysSettingManager;
import smartx.bam.vo.DatabaseConst;
import smartx.bam.vo.SysConst;
import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.utils.StringUtil;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaLogger;
import smartx.publics.email.EmailService;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.EventType;

/**
 * @author caohenghui
 * 
 */
public class MessageDispatcher extends Thread {

	private Logger logger = NovaLogger.getLogger(this);

	private String SMTP_HOST = null;
    private String SMTP_USER = null;
    private String SMTP_PWD = null;
    private String SENDER_EMAIL = null;
    private EmailService mailService = null;

	private String dataSource = null;

	private Queue<MessageTask> queue = new LinkedBlockingQueue<MessageTask>();

	public MessageDispatcher(String dataSource) {
		this.dataSource = dataSource;
		this.start();
	}

	@Override
	public void run() {
		logger.info("消息发送线程启动!");
		CommDMO dmo = new CommDMO();
		while (true) {

			try {
				
				if(!this.queue.isEmpty()){
					this.initEmailInfo();
				}
				
				while (!this.queue.isEmpty()) {

					MessageTask task = this.queue.poll();
					HashVO[] ruleVo = this.getRuleVO(task.getRuleCode());
					if(ruleVo != null && ruleVo.length >=0){
						dealMessageTask(ruleVo, task.getNewEvent());
					}
					
				}
				sleep(800);
			} catch (Exception e) {
				try {
					dmo.rollback(dataSource);
				} catch (Exception e1) {
					logger.error("", e1);
				}
				logger.error("", e);
			} finally {
				dmo.releaseContext(dataSource);
			}
		}

	}

	public void addTask(MessageTask task) {
		this.queue.add(task);
	}
	
	private HashVO[] getRuleVO(String ruleCode){
		CommDMO dmo = new CommDMO();
		HashVO[] temp = null;
		try{
			String sql = "SELECT BA.ID ALERTID,BA.SUBJECT,BA.BODY,BA.SEVERITY,BR.ACTIONTYPE,BA.STATUS FROM BAM_RULE BR,BAM_ALERT BA WHERE BR.ACTIONALERTID = BA.ID AND BR.CODE=?";
			temp = dmo.getHashVoArrayByDS(dataSource, sql,ruleCode);
		}catch(Exception e){
			logger.error("", e);
		}finally{
			dmo.releaseContext(dataSource);
		}
		return temp;
	}
	
	private void dealMessageTask(HashVO[] temp, EventBean newEvent) {

		CommDMO dmo = new CommDMO();

		for (HashVO vo : temp) {

			try {

				String alertId = vo.getStringValue("ALERTID");
				String subject = vo.getStringValue("SUBJECT");
				String body = vo.getStringValue("BODY");
				int severity = vo.getIntegerValue("SEVERITY");

				if (subject != null) {
					subject = this.convertExpression(newEvent, subject);
				}

				if (body != null) {
					body = this.convertExpression(newEvent, body);
				}

				int actionType = vo.getIntegerValue("ACTIONTYPE");
				
				int currStatus = getCurrentStatus(alertId);
				
				int nextStatus = 0;
				
				if( currStatus == SysConst.BAM_ALERT_LOWERED){
					nextStatus = SysConst.BAM_ALERT_RAISED;
				}else if( currStatus == SysConst.BAM_ALERT_RAISED){
					nextStatus = SysConst.BAM_ALERT_RAISED;
				}else if( currStatus == SysConst.BAM_ALERT_ACKNOWLEDGED){ 
					nextStatus = SysConst.BAM_ALERT_ACKNOWLEDGED;
				}
				if(actionType == SysConst.BAM_RULE_SENDONCE ){
					if(currStatus == SysConst.BAM_ALERT_LOWERED && nextStatus == SysConst.BAM_ALERT_RAISED){
						logger.debug("告警类型:只发送一次;ALERTID:"+alertId);
						this.sendMessage(alertId, subject, body, severity);
						this.updateAlertStatus(SysConst.BAM_ALERT_RAISED, alertId);
					}
				}else if(actionType == SysConst.BAM_RULE_SENDEVERYTIME){
					if((currStatus == SysConst.BAM_ALERT_LOWERED && nextStatus == SysConst.BAM_ALERT_RAISED)||(currStatus == SysConst.BAM_ALERT_RAISED && nextStatus == SysConst.BAM_ALERT_RAISED)){
						logger.debug("告警类型:每次均发送;ALERTID:"+alertId);
						this.sendMessage(alertId, subject, body, severity);
						this.updateAlertStatus(SysConst.BAM_ALERT_RAISED, alertId);
					}
				}else if(actionType == SysConst.BAM_RULE_RESET){
					logger.debug("告警类型:重置告警!;ALERTID:"+alertId);
					if(currStatus == SysConst.BAM_ALERT_ACKNOWLEDGED || currStatus == SysConst.BAM_ALERT_RAISED){
						String tempMessage =   "告警【"+getCurrentName(alertId)+"】已重置,状态已恢复正常!";
						this.sendMessage(alertId,tempMessage,tempMessage,1);
						this.updateAlertStatus(SysConst.BAM_ALERT_LOWERED, alertId);
					}
				}
				

			} catch (Exception e) {
				logger.debug("执行SQL语句出错!", e);
				try {
					dmo.rollback(this.dataSource);
				} catch (Exception e1) {
					logger.debug("数据库回滚出错!", e1);
				}
			} finally {
				dmo.releaseContext(this.dataSource);
			}
		}
	}

	private void updateAlertStatus(int status, String alertId) {
		CommDMO dmo = new CommDMO();
		try {
			
			String updateSQL = "UPDATE BAM_ALERT T SET T.STATUS =? WHERE T.ID = ?";
			dmo.executeUpdateByDS(this.dataSource, updateSQL,status, alertId);
			dmo.commit(this.dataSource);

		} catch (Exception e) {
			logger.debug("执行SQL语句出错!", e);
			try {
				dmo.rollback(this.dataSource);
			} catch (Exception e1) {
				logger.debug("数据库回滚出错!", e1);
			}

		} finally {
			dmo.releaseContext(this.dataSource);
		}
	}

	private void sendMessage(String alertId, String subject, String body,
			int severity) {

		CommDMO dmo = new CommDMO();

		try {

			logger.debug("开始发送消息:alertId=[" + alertId + "],subject=[" + subject
					+ "]");

			String messageId = dmo.getSequenceNextValByDS(
					DatabaseConst.DATASOURCE_DEFAULT, "S_BAM_ALERTMESSAGE");

			String insertSQL = "INSERT INTO BAM_ALERTMESSAGE(ID,ALERTID,SUBJECT,SEVERITY,BODY,ACTIVATETIME,STATUS) VALUES(?,?,?,?,?,SYSDATE,0)";

			dmo.executeUpdateByDS(this.dataSource, insertSQL, messageId,alertId, subject, severity, body);

			dmo.commit(this.dataSource);

			this.dispatchMessage(messageId, alertId, subject, body);

			logger.debug("发送消息结束:alertId=[" + alertId + "],subject=[" + subject
					+ "]");

		} catch (Exception e) {

			logger.debug("执行SQL语句出错!", e);
			try {
				dmo.rollback(this.dataSource);
			} catch (Exception e1) {
				logger.debug("数据库回滚出错!", e1);
			}
		} finally {
			dmo.releaseContext(this.dataSource);
		}
	}

	private void dispatchMessage(String messageId, String alertId,
			String subject, String body) {

		CommDMO dmo = new CommDMO();

		try {

			
			String searchSQL = "SELECT T.OBJECTNAME,T.DELIVERYTYPE,T.EMAIL FROM V_BAM_SUBSCIBER T WHERE T.ALERTID=?";

			HashVO[] temp = dmo.getHashVoArrayByDS(this.dataSource, searchSQL,alertId);

			for (HashVO vo : temp) {

				String orderName = vo.getStringValue("OBJECTNAME");
				String TO_Email = vo.getStringValue("EMAIL");
				int DELIVERYTYPE = vo.getIntegerValue("DELIVERYTYPE");
				if (orderName == null || orderName.trim().equals("")) {
					logger.debug("定阅者名称为空!");
					return;
				} else {
					String insertSQL = "insert into bam_messagesubscriber (ID,ALERTMESSAGEID,USERNAME) values (s_bam_messagesubscriber.nextval,?,?)";
					if (DELIVERYTYPE == SysConst.BAM_SUBSCIBER_DELIVERYTYPE_NORMAL) {

						dmo.executeUpdateByDS(this.dataSource,insertSQL, messageId, orderName);
						dmo.commit(DatabaseConst.DATASOURCE_DEFAULT);

					} else if (DELIVERYTYPE == SysConst.BAM_SUBSCIBER_DELIVERYTYPE_EMAIL) {
						if (mailService != null && !StringUtil.isEmpty(TO_Email)) {
							mailService.sendMail(TO_Email, subject, body);
						}
					} else if (DELIVERYTYPE == SysConst.BAM_SUBSCIBER_DELIVERYTYPE_ALL) {

						if (mailService != null && !StringUtil.isEmpty(TO_Email)) {
							mailService.sendMail(TO_Email, subject, body);
						}

						dmo.executeUpdateByDS(this.dataSource, insertSQL,messageId, orderName);
						dmo.commit(this.dataSource);

					}
				}
			}

		} catch (Exception e) {
			logger.debug("执行SQL语句出错!", e);
			try {
				dmo.rollback(this.dataSource);
			} catch (Exception e1) {
				logger.debug("数据库回滚出错!", e1);
			}
		} finally {
			dmo.releaseContext(this.dataSource);
		}
	}
	
	private int getCurrentStatus(String alertId){
		int status = 0;
		CommDMO dmo = new CommDMO();
		try{
			String searchSQL = "select STATUS from bam_alert where id=?";
			HashVO[] temp = dmo.getHashVoArrayByDS(this.dataSource, searchSQL, alertId);
			if(temp != null && temp.length>0){
				status = temp[0].getIntegerValue("STATUS");
			}
		}catch(Exception e){
			logger.debug("执行SQL语句出错!", e);
		}finally{
			dmo.releaseContext(this.dataSource);
		}
		return status;
	}
	
	private String getCurrentName(String alertId){
		String name = "";
		CommDMO dmo = new CommDMO();
		try{
			String searchSQL = "select NAME from bam_alert where id=?";
			HashVO[] temp = dmo.getHashVoArrayByDS(this.dataSource, searchSQL, alertId);
			if(temp != null && temp.length>0){
				name = temp[0].getStringValue("NAME");
			}
		}catch(Exception e){
			logger.debug("执行SQL语句出错!", e);
		}finally{
			dmo.releaseContext(this.dataSource);
		}
		return name;
	}

	private String convertExpression(EventBean newEvent, String exp) {
		String str_newdsn = exp;
		try {
			String[] keys = StringUtil.getFormulaMacPars(exp);
			Map<String, Object> eventValues = this.getEventValueMap(newEvent);
			for (int i = 0; i < keys.length; i++) {
				Object obj = eventValues.get(keys[i]);
				if (obj != null) {
					str_newdsn = StringUtil.replaceAll(str_newdsn, "{"
							+ keys[i] + "}", obj.toString()); // 替换
				}
			}

		} catch (Exception e) {
			logger.debug("解析表达式出错!", e);
		}
		return str_newdsn;
	}

	private Map<String, Object> getEventValueMap(EventBean newEvent) {
		Map<String, Object> values = new HashMap<String, Object>();
		EventType eventType = newEvent.getEventType();
		for (String name : eventType.getPropertyNames()) {
			values.put(name, newEvent.get(name));
		}
		return values;
	}
	
	private void initEmailInfo(){
		try{
			
			SMTP_HOST = SysSettingManager.getInstance().findSysSettingByKey("SMTP_HOST");
			SMTP_USER = SysSettingManager.getInstance().findSysSettingByKey("SMTP_USER");
		    SMTP_PWD = SysSettingManager.getInstance().findSysSettingByKey("SMTP_PWD");
		    SENDER_EMAIL = SysSettingManager.getInstance().findSysSettingByKey("SENDER_EMAIL");
		    
			if (!StringUtil.isEmpty(SMTP_HOST) && !StringUtil.isEmpty(SMTP_USER)
					&& !StringUtil.isEmpty(SMTP_PWD)
					&& !StringUtil.isEmpty(SENDER_EMAIL)) {
				mailService = new EmailService(SMTP_HOST, SMTP_USER, SMTP_PWD, SENDER_EMAIL,false);
			}
			
		}catch(Exception e){
			logger.info("获取邮件配置时出错!",e);
		}
	}

}
