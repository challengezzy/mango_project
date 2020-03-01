package smartx.bam.bs.datatask.custom;

import org.apache.log4j.Logger;
import org.dom4j.Element;

import smartx.framework.common.vo.NovaLogger;
import smartx.publics.dataprofile.ColumnProfileService;
import smartx.publics.datatask.DataTaskExecThread;
import smartx.publics.datatask.DataTaskExecuteIFC;

/**
 * 分析集剖析任务
 *@author zzy
 *@date Dec 30, 2011
 **/
public class AnalyzerProfileTask implements DataTaskExecuteIFC {

	private Logger logger = NovaLogger.getLogger(this.getClass());
	
	@Override
	public void dataTaskExec(Element task, DataTaskExecThread mainThread) throws Exception {
		String analyzerSetId = task.elementText("analyzerSetId");
		String ds = task.elementText("datasource");
		
		String profileDate = task.elementText("profileDate");
		String msg = "开始执行分析集剖析任务，分析集ID="+ analyzerSetId +",数据源名称=" + ds;
		mainThread.logTaskRun(msg,"");
		logger.info(msg);
		
		ColumnProfileService service = ColumnProfileService.getInstance();
		service.profileAnalyzerSet(analyzerSetId, ds, profileDate);
		
		mainThread.logTaskRun("剖析任务执行成功！","");
	}

}
