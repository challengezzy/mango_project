/**
 * 
 */
package smartx.bam.bs.entitymodel;

import org.apache.log4j.Logger;
import org.dom4j.Element;

import smartx.framework.common.utils.ExceptionUtil;
import smartx.framework.common.vo.NovaLogger;
import smartx.publics.datatask.DataTaskExecThread;
import smartx.publics.datatask.DataTaskExecuteIFC;

/**
 * @author caohenghui
 * Nov 1, 2011
 */
public class EntityRuleDataTaskImpl implements DataTaskExecuteIFC {
	
	protected Logger logger = NovaLogger.getLogger(this.getClass());
	
	private EntityRuleRealTimeStatisticsService errtss = new EntityRuleRealTimeStatisticsService();

	/* (non-Javadoc)
	 * @see smartx.publics.datatask.DataTaskExecuteIFC#dataTaskExec(org.dom4j.Element)
	 */
	@Override
	public void dataTaskExec(Element taskEle,DataTaskExecThread mainThread) throws Exception {
		try{
			if(taskEle != null ){
				String modelCode = taskEle.element("modelCode").getText();
				String entityCode = taskEle.element("entityCode").getText();
				String ruleCode = taskEle.element("ruleCode").getText();
				String taskName = taskEle.element("taskName").getText();
				
				String batchNo = mainThread.getVersion();
				
				errtss.dealRealTimeStatistics(errtss.getEntityModelContent(modelCode), modelCode, entityCode, ruleCode,batchNo,taskName,mainThread);
				
			}
		}catch(Exception e){
			throw e;
		}
	}
}
