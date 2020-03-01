

package smartx.framework.common.job.bs;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import smartx.framework.common.vo.NovaLogger;


import java.util.*;
import java.text.*;
public class PrintDateTimeJob implements Job {
	private static Logger log =NovaLogger.getLogger(PrintDateTimeJob.class);
	private static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
	private static long runtimes=0;
	private static long firsttime=0;
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		long id=(new Date()).getTime();//本次任务开始时间
		Thread thread=Thread.currentThread();//本次任务所在线程
		if(runtimes==0){
			firsttime=id;			
		}
		runtimes++;
		long delay=id-firsttime;//首次执行到现在的延迟
		long pertime=1000*60*60*24;//理论间隔时间24小时      //根据具体设置请自行修改
		long times=delay/pertime+1 + (delay%pertime>0?1:0);		
		log.info("当前线程："+ (thread.getName()==null?"noname":thread.getName())+"-"+id+" 第【"+runtimes+"】次执行，延迟【"+delay+"】毫秒，理论启动【"+times+"】次  JOB启动时间："+ sdf.format(new Date())); 
		//1.5 log.info("当前线程："+ thread.getId() +"-"+id+" 第【"+runtimes+"】次执行，延迟【"+delay+"】毫秒，理论启动【"+times+"】次  JOB启动时间："+ sdf.format(new Date()));
		try {
			log.info("模拟业务执行，延迟两秒。");
			Thread.sleep(1000*2);//2s
		} catch (Exception e) {
			; 
		}
		log.info("当前线程："+ (thread.getName()==null?"noname":thread.getName())+"-"+id+"  JOB结束时间："+ sdf.format(new Date()));
		//1.5 log.info("当前线程："+ thread.getId() +"-"+id+"  JOB结束时间："+ sdf.format(new Date()));
	}

}
