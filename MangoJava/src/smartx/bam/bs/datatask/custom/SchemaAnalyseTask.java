package smartx.bam.bs.datatask.custom;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.dom4j.Element;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.vo.NovaLogger;
import smartx.publics.dataprofile.StructureProfileService;
import smartx.publics.datatask.DataTaskExecThread;
import smartx.publics.datatask.DataTaskExecuteIFC;

/**
 *@author zzy
 *@date Mar 2, 2012
 *@description 数据模型剖析
 **/
public class SchemaAnalyseTask implements DataTaskExecuteIFC{
	private Logger logger = NovaLogger.getLogger(this.getClass());
	
	@Override
	public void dataTaskExec(Element task,DataTaskExecThread mainThread) throws Exception {
		
		String dsName = task.elementText("datasource");
		String needprofileStr = task.elementText("needprofile");
		if(needprofileStr == null || "false".equalsIgnoreCase(needprofileStr)){
			logger.info("在数据源【"+dsName+"】上无需进行剖析，直接返回。");
			return;
		}
		
		String overrideStr = task.elementText("override");
		boolean isOverride = false;//是否覆盖
		if(overrideStr != null && "true".equalsIgnoreCase(overrideStr))
			isOverride = true;
		
		//剖析日期
		Date d_curr = new Date();
        SimpleDateFormat sdf_curr = new SimpleDateFormat("yyyyMMdd", Locale.SIMPLIFIED_CHINESE);
        String profileDate = sdf_curr.format(d_curr);
        //判断是否存在同期分析结果，并决定是否要覆盖分析
        boolean isExists = StructureProfileService.getInstance().isExistsSametermResult(dsName, profileDate);
        if(isExists && !isOverride){
        	logger.info("在数据源【"+dsName+"】上存在同批次【"+profileDate+"】的数据模型剖析结果,无需再次分析。");
        	return ;
        }
        
		mainThread.logTaskRun("执行【"+dsName+"】的数据模型剖析任务，执行参数：" + profileDate, "");
		
		StructureProfileService.getInstance().dbSchemaProfile(dsName, profileDate);
		
		mainThread.logTaskRun("执行【"+dsName+"】的数据模型剖析任务成功。", "");
		
		new CommDMO().releaseContext();
	}

}
