package smartx.publics.form.bs.interceptors.datatask;

import org.apache.log4j.Logger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.job.bs.JobServer;
import smartx.framework.common.vo.DMOConst;
import smartx.framework.common.vo.HashVO;
import smartx.publics.datatask.DataTaskCronJob;

/**
 * @author zhangzy Description
 */
public class DataTaskFormService {
	private Logger logger = Logger.getLogger(this.getClass());

	public static String METADATA_PREFIX = "MT_DT_";
	
	/**
	 * 保存任务模板
	 * @param name
	 * @param code
	 * @param cronexpression
	 * @param mtcode
	 * @param content
	 * @return
	 */
	public String saveCronDataTask(String name, String code, String cronexpression, String mtcode, String content)
			throws Exception {
		CommDMO dmo = new CommDMO();
		String sql = "SELECT ID FROM PUB_DATATASK_TEMPLET T where t.code = ?";
		HashVO[] vos = dmo.getHashVoArrayByDS(DMOConst.DS_DEFAULT, sql, code);
		String taskTempleteId = "";
		if (vos.length > 0) {
			taskTempleteId = vos[0].getStringValue("ID");
			String updateSql = "update pub_datatask_templet t set t.cronexpression=? where t.code = ?";
			dmo.executeUpdateByDS(DMOConst.DS_DEFAULT, updateSql, cronexpression, code);
		} else {
			String insertSql = "insert into pub_datatask_templet(TYPE,ID,NAME,CODE,CRONEXPRESSION,MTCODE) values(1,?,?,?,?,?)";
			taskTempleteId = dmo.getSequenceNextValByDS(DMOConst.DS_DEFAULT, "s_pub_datatask_templet");

			dmo.executeUpdateByDS(DMOConst.DS_DEFAULT, insertSql, taskTempleteId, name, code, cronexpression, mtcode);
		}
		dmo.commit(DMOConst.DS_DEFAULT);
		dmo.releaseContext(DMOConst.DS_DEFAULT);
		this.addTaskToJob(code, taskTempleteId, content, cronexpression);

		return taskTempleteId;
	}
	
	/**
	 * 判断数据任务模板是否存在
	 * 
	 * @param code
	 * @throws Exception
	 */
	public boolean isExistDataTask(String code) throws Exception {
		logger.debug("是否存在数据任务模板" + code + "]");
		CommDMO dmo = new CommDMO();
		String sql = "select b.id from PUB_DATATASK_TEMPLET b where b.code =?";
		try {
			HashVO[] vos = dmo.getHashVoArrayByDS(null, sql, code);
			if (vos.length > 0)
				return true;
		} catch (Exception e) {
			throw e;
		} finally {
			dmo.releaseContext(null);
		}
		return false;
	}
	
	/**
	 * 将任务添加到JOB
	 * @param jobName
	 * @param taskTempleteId
	 * @param content
	 * @param exp
	 * @return
	 */
	private boolean addTaskToJob(String jobName, String taskTempleteId,String content, String exp) {
		boolean flag = false;
		try {
			JobDataMap dataMap = new JobDataMap();
			dataMap.put("ID", taskTempleteId);
			dataMap.put("CONTENT", content);
			if (JobServer.isCronJobExist(jobName)) {
				JobServer.removeJob(jobName, "DataTask");
			}
			JobDetail jobDetail = new JobDetail();
			jobDetail.setJobClass(DataTaskCronJob.class);
			jobDetail.setGroup("DataTask");
			jobDetail.setName(jobName);
			jobDetail.setJobDataMap(dataMap);
			JobServer.addCronJob(jobDetail, exp);
			flag = true;
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

}
