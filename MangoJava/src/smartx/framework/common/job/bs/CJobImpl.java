

/**************************************************************************
 * $RCSfile: CJobImpl.java,v $   $Date: 2010/03/08 06:27:30 $
 ***************************************************************************/
package smartx.framework.common.job.bs;



import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import smartx.framework.common.vo.NovaLogger;

/**
 * <p>Title: Job实现类的基类</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Gxlu inc., rm2 </p>
 * @author wuyc
 * @version 2.5
 */
public abstract class CJobImpl implements org.quartz.Job{
    
	/**
     * 实现org.quartz.Job的execute方法
     */
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException{
        try{
            executeBusiness(jobExecutionContext);
            System.out.println("job 已经启动");
        }catch (Exception e){
            throw new JobExecutionException(e);
        }
        
        try {
			postBusiness(jobExecutionContext);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    /**
     * 处理业务
     * @throws WWFException
     */
    protected abstract void executeBusiness(JobExecutionContext jobExecutionContext) throws Exception;

    
    
    /**
     * 在运行任务后调用
     * @param jobExecutionContext
     */
    protected void postBusiness(JobExecutionContext jobExecutionContext) throws Exception{
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        JobDataMap metaData = jobDetail.getJobDataMap();
        String jobId =  metaData.getString(JobConst.job_parameter_key);
        
        /** 修改单次任务的状态 */
       if(jobId==null)
    	   return;
        try{
        	JobServer.oneShotJobFinish(Long.valueOf(jobId).longValue());
        }catch (NumberFormatException e){
            NovaLogger.getLogger(this).error(e);
        }
    }
}
/************************************************************************
 * $RCSfile: CJobImpl.java,v $  $Date: 2010/03/08 06:27:30 $
 *
 * $Log: CJobImpl.java,v $
 * Revision 1.1.4.2  2010/03/08 06:27:30  wangqi
 * *** empty log message ***
 *
 * Revision 1.1.4.1  2010/03/05 13:27:42  wangqi
 * *** empty log message ***
 *
 * Revision 1.1  2007/08/30 12:05:36  wangqi
 * *** empty log message ***
 *  2007/08/16 05:05:11  wuyc
 * 
 *
 *************************************************************************/
