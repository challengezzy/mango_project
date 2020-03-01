/**************************************************************************
 * $RCSfile: IJobServiceImpl.java,v $   $Date: 2010/03/08 06:27:30 $
 ***************************************************************************/
package smartx.framework.common.job.bs;


import java.util.Date;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.job.ui.IJobServiceIFC;
import smartx.framework.common.utils.StringUtil;
import smartx.framework.common.utils.Sys;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaLogger;

/**
 * <p>
 * Title: 缺省的JOB service模块
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: Gxlu inc., rm2
 * </p>
 * 
 * @author wyc
 * @version 2.5
 */
public class IJobServiceImpl implements IJobServiceIFC {

	/**
	 * 当前可处理的任务标签列表
	 */
	protected static String[] JOB_TAGS=null; 
	protected static String JOB_TAGS_STR=null;
//	static{
//		String tmp=(String)Sys.getInfo("JOB_TAGS");
//		JOB_TAGS=(tmp==null||tmp.trim().equals(""))?(new String[]{}):tmp.trim().split(",");
//		JOB_TAGS_STR=StringUtil.joinArray2String(JOB_TAGS, ",", "'");
//	}
	
	private static JobServer jobserver = null;

	public IJobServiceImpl() {
		
	}

	public void jobStart(long jobId) throws Exception {
		//TODO 下面的启动任务部分和JobServer中的启动任务部分应该合并处理。
		NovaLogger.getLogger(this).debug("启动循环任务[id=" +jobId + "]");
		CommDMO dmo=new CommDMO();
		try {
			String sql = "SELECT * FROM PUB_JOB WHERE ID=?";
				//((JOB_TAGS.length==0)?"":(" AND JOBTAG IN ("+JOB_TAGS_STR+")"));
	
			HashVO[] vos=dmo.getHashVoArrayByDS(null, sql, jobId);
			
			if (vos.length==0) {
				throw new Exception("指定的任务不存在");
			}
			
			//查找job的状态，如果为已启动状态
			byte status = vos[0].getByteValue("status").byteValue();
			if(status==JobConst.JOB_STATUS_INUSE){
			}else {
				String iscore = vos[0].getStringValue("INDICATOR");
				
				long id = vos[0].getLongValue("id").longValue();
				String name = vos[0].getStringValue("name");
				String group = vos[0].getStringValue("jobgroup");
				String cls = vos[0].getStringValue("implClassName");
				
				if (iscore != null && iscore.equals("CRONJOB")) {
					String exp = vos[0].getStringValue("cronExpression");
					JobServer.addCronJob(name, group, cls, exp);
					setJobStatus(jobId, JobConst.JOB_STATUS_INUSE);
				}else{
					Date dt = vos[0].getDateValue("oneshotRundate");//这个日期不需要
					JobServer.addOneShotJob(name, group, cls, new Date(System.currentTimeMillis()));
					setJobStatus(jobId, JobConst.JOB_STATUS_FINISHED);
				}
				
				
			}
			dmo.commit(null);
		}catch (Exception ex) {
			NovaLogger.getLogger(this).error("",ex);
			dmo.rollback(null);
			throw ex;
		}
		finally{
			dmo.releaseContext(null);
		}
	}

	public void jobStop(long jobId) throws Exception {
		NovaLogger.getLogger(this).debug("停止循环任务[id=" +jobId + "]");
		CommDMO dmo = new CommDMO();
		try {
			String sql = "select * from pub_job where id=?";
			HashVO[] vos = dmo.getHashVoArrayByDS(null, sql, jobId);
			if (vos.length > 0) {
				// 查找job的状态，如果为已关闭状态
				byte status = vos[0].getByteValue("status").byteValue();
				if (status == JobConst.JOB_STATUS_FINISHED) {
				} else {
					String tmp2 = vos[0].getStringValue("name");
					tmp2 = (tmp2 == null) ? "" : tmp2;
					String tmp10 = vos[0].getStringValue("jobgroup");
					tmp10 = (tmp10 == null) ? "" : tmp10;
					jobserver.removeJob(tmp2, tmp10);
					setJobStatus(jobId, JobConst.JOB_STATUS_FINISHED);
				}
			}
			dmo.commit(null);
		} catch (Exception ex) {
			NovaLogger.getLogger(this).error("", ex);
			dmo.rollback(null);
			throw ex;
		} finally {
			dmo.releaseContext(null);
		}
	}

	private void setJobStatus(long jobId, byte status) throws Exception {
		CommDMO dmo=new CommDMO();
		String sql = "update pub_job set status=? where id=?";
		
		dmo.executeUpdateByDS(null, sql,status,jobId);
	}

}

/*******************************************************************************
 * $RCSfile: IJobServiceImpl.java,v $ $Date: 2010/03/08 06:27:30 $
 * 
 * $Log: IJobServiceImpl.java,v $
 * Revision 1.2.4.6  2010/03/08 06:27:30  wangqi
 * *** empty log message ***
 *
 * Revision 1.2.4.5  2010/03/05 13:27:42  wangqi
 * *** empty log message ***
 *
 * Revision 1.2.4.4  2009/12/25 05:59:40  wangqi
 * *** empty log message ***
 *
 * Revision 1.2.4.3  2009/08/14 07:25:07  yangjm
 * *** empty log message ***
 *
 * Revision 1.2.4.2  2009/05/15 07:10:02  wangqi
 * *** empty log message ***
 *
 * Revision 1.2.4.1  2009/05/15 05:57:59  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2009/01/12 05:33:48  wangqi
 * *** empty log message ***
 *
 * Revision 1.1  2008/09/01 07:38:14  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2007/08/30 13:08:30  wangqi
 * *** empty log message ***
 *
 * Revision 1.1  2007/08/30 12:05:36  wangqi
 * *** empty log message ***
 * 2007/8/16 07:01:48 wuyc create
 * 
 ******************************************************************************/
