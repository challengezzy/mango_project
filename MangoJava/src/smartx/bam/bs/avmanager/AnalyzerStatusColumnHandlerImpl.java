package smartx.bam.bs.avmanager;

import java.util.Map;

import org.quartz.JobDetail;

import smartx.bam.vo.SysConst;
import smartx.framework.common.job.bs.JobServer;
import smartx.publics.form.bs.service.VirtualColumnHandlerIFC;

public class AnalyzerStatusColumnHandlerImpl implements VirtualColumnHandlerIFC {

	@Override
	public Object handler(Map<?, ?> rowMap) {
		String value = "停止";
		try{
			String analyzerCode = (String)rowMap.get("CODE");
			if(analyzerCode != null){
				JobDetail jd = JobServer.getJobDetail(analyzerCode, SysConst.ANALYZER_GROUP);
				//如果job已存在的话就说明已经被启动了
				if(jd != null ){
					value = "启动";
				}
			}
		}catch(Exception e){
			
		}
		return value;
	}

}
