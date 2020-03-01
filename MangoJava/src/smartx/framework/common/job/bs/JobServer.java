package smartx.framework.common.job.bs;

import java.util.Date;
import java.util.Hashtable;

import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.utils.StringUtil;
import smartx.framework.common.utils.Sys;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaLogger;

public class JobServer {
	
	// 任务调度器 静态变量
	private static Scheduler sched;
	private static Logger log = NovaLogger.getLogger(JobServer.class);
	/**
	 * 当前可处理的任务标签列表
	 */
	protected static String[] JOB_TAGS = null;
	protected static String JOB_TAGS_STR = null;

	private static Hashtable<String, Object> cronJobTable = new Hashtable<String, Object>();

	static {
		String tmp = (String) Sys.getInfo("JOB_TAGS");
		JOB_TAGS = (tmp == null || tmp.trim().equals("")) ? (new String[] {}) : tmp.trim().split(",");
		JOB_TAGS_STR = StringUtil.joinArray2String(JOB_TAGS, ",", "'");
	}

	/**
	 * 构造函数，同时启动任务管理
	 */
	private JobServer() {
	}

	public static void start() {
		try {
			SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
			sched = schedFact.getScheduler();
			sched.start();
			// JobDetail jobDetail = new JobDetail("dsafd", "xxx",
			// Class.forName("smartx.framework.common.job.bs.PrintDateTimeJob"));
			// SimpleTrigger trigger = new SimpleTrigger("dsafd_tg", "xxx", new
			// Date(System.currentTimeMillis()-10000));
			// CronTrigger trigger = new CronTrigger("dsafd_tg", "xxx","0/15 * * * * ?");
			// sched.scheduleJob(jobDetail, trigger);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("初始化quartz调度器失败！", e);
		}

		try {
			// 下面的启动任务部分和IJobServiceImpl中的启动任务部分应该合并处理。
			String sql = "SELECT * FROM PUB_JOB " + "WHERE STATUS=" + JobConst.JOB_STATUS_INUSE + ((JOB_TAGS.length == 0) ? "" : (" AND JOBTAG IN (" + JOB_TAGS_STR + ")"));
			HashVO[] vos = (new CommDMO()).getHashVoArrayByDS(null, sql);

			for (int i = 0; i < vos.length; i++) {

				String iscore = vos[i].getStringValue("INDICATOR");

				String name = vos[i].getStringValue("name");
				String group = vos[i].getStringValue("jobgroup");
				String cls = vos[i].getStringValue("implClassName");

				if (iscore != null && iscore.equals("CRONJOB")) {
					String exp = vos[i].getStringValue("cronExpression");
					addCronJob(name, group, cls, exp);
				} else {
					//Date dt = vos[i].getDateValue("oneshotRundate");// 这个日期不需要
					addOneShotJob(name, group, cls, new Date(System.currentTimeMillis()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}

	}

	/**
	 * 删除JOB
	 * 
	 * @param jobName
	 * @param groupName
	 * @throws Exception
	 */
	public static void removeJob(String jobName, String groupName) throws Exception {
		if (jobName == null) {
			throw new Exception("JOB不能为空");
		}
		sched.deleteJob(jobName, groupName);
		cronJobTable.remove(jobName);

		log.debug("Job[" + jobName + "],Group[" + groupName + "]已从调度器中删除");
	}

	/**
	 * 启动quartz线程
	 */
	public static void stop() {
		if (sched == null) {// 没有初始化
			return;
		}
		try {
			sched.shutdown(true);
		} catch (SchedulerException e) {
			e.printStackTrace();
			log.error("调度器关闭异常！", e);
		}
	}

	/**
	 * 停止quartz线程
	 * 
	 * @param force
	 *            是否强制终止 true-强制 false-不强制，等待任务自行结束
	 */
	public static void stop(boolean force) {
		if (sched == null) {// 没有初始化
			return;
		}

		try {
			sched.shutdown(!force);// 方法的参数是：是否等待任务结束，强制就是不等待
		} catch (SchedulerException e) {
			log.error("调度器关闭异常！", e);
		}
	}

	/**
	 * 增加单次任务
	 * 
	 * @param name
	 * @param group
	 * @param cls
	 * @param dt
	 *            启动时间（忽略）
	 * @throws Exception
	 */
	public static void addOneShotJob(String name, String group, String cls, Date dt) throws Exception {
		JobDetail jobDetail = new JobDetail(name, group, Class.forName(cls));
		SimpleTrigger trigger = new SimpleTrigger(name + "_tg", group, dt);
		sched.scheduleJob(jobDetail, trigger);

		log.debug("Job[" + name + "],Group[" + group + "]加入到调度器中!");
		// JobDetail jobDetail = new JobDetail("dsafd", "xxx",
		// Class.forName("smartx.framework.common.job.bs.PrintDateTimeJob"));
		// SimpleTrigger trigger = new SimpleTrigger("dsafd_tg", "xxx", new
		// Date(System.currentTimeMillis()-10000));
		// CronTrigger trigger = new CronTrigger("dsafd_tg", "xxx", "0/15 * * * * ?");
		// sched.scheduleJob(jobDetail, trigger);
	}

	/**
	 * 判断当前的循环任务调度Server中是否存在指定的任务
	 * 
	 * @param jobName
	 * @return
	 * @throws Exception
	 */
	public static boolean isCronJobExist(String jobName) throws Exception {
		if (cronJobTable.containsKey(jobName))
			return true;
		else
			return false;
	}

	/**
	 * 增加循环任务
	 * 
	 * @param name
	 * @param group
	 * @param cls
	 * @param exp
	 * @throws Exception
	 */
	public static void addCronJob(String name, String group, String cls, String exp) throws Exception {
		JobDetail jobDetail = new JobDetail(name, group, Class.forName(cls));
		CronTrigger trigger = new CronTrigger(name + "_tg", group, exp);
		sched.scheduleJob(jobDetail, trigger);

		cronJobTable.put(jobDetail.getName(), jobDetail.getName());

		log.debug("Job[" + name + "],Group[" + group + "]加入到调度器中! 循环表达式[" + exp + "]");

		// 以下部分测试用
		// JobDetail jobDetail = new JobDetail("dsafd", "xxx",
		// Class.forName("smartx.framework.common.job.bs.PrintDateTimeJob"));
		// CronTrigger trigger = new CronTrigger("dsafd_tg", "xxx",
		// "0/15 * * * * ?");
		// sched.scheduleJob(jobDetail, trigger);
	}

	/**
	 * 增加循环任务
	 * 
	 * @param jobDetail
	 *            必填属性name,group,class；jobDataMap属性项可选
	 * @param exp
	 * @throws Exception
	 */
	public static void addCronJob(JobDetail jobDetail, String exp) throws Exception {
		CronTrigger trigger = new CronTrigger(jobDetail.getName() + "_tg", jobDetail.getGroup(), exp);
		sched.scheduleJob(jobDetail, trigger);
		cronJobTable.put(jobDetail.getName(), jobDetail.getName());

		log.debug("Job[" + jobDetail.getName() + "],Group[" + jobDetail.getName() + "]加入到调度器中! 循环表达式[" + exp + "]");

		// 一下测试用
		// JobDetail jobDetail = new JobDetail("dsafd", "xxx",
		// Class.forName("smartx.framework.common.job.bs.PrintDateTimeJob"));
		// CronTrigger trigger = new CronTrigger("dsafd_tg", "xxx",
		// "0/15 * * * * ?");
		// sched.scheduleJob(jobDetail, trigger);
	}

	public static JobDetail getJobDetail(String jobName, String group) {
		if (sched != null || jobName == null) {
			try {
				return sched.getJobDetail(jobName, group);
			} catch (SchedulerException e) {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * 单次任务结束，把状态修改为WWFConst.JOB_STATUS_FINISHED
	 * 
	 * @param jobId
	 * @throws WWFException
	 */
	public static void oneShotJobFinish(long jobId) throws Exception {
		try {
			String sql = "update pub_job set status=" + JobConst.JOB_STATUS_FINISHED + " where INDICATOR='ONESHOTJOB' AND id=" + jobId;
			(new CommDMO()).executeUpdateByDS(null, sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.debug("更新单次任务jobid[" + jobId + "]状态为执行结束");
	}

}
