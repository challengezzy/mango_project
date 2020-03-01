/**
 * 
 */
package smartx.bam.bs.entitymodel;

import org.apache.log4j.Logger;
import org.dom4j.Element;

import smartx.framework.common.vo.NovaLogger;
import smartx.publics.datatask.DataTaskExecThread;
import smartx.publics.datatask.DataTaskExecuteIFC;

/**
 * @author caohenghui
 * Dec 19, 2011
 */
public class EntityDimensionDataTaskImpl implements DataTaskExecuteIFC {
	
	protected Logger logger = NovaLogger.getLogger(this.getClass());
	
	private EntityDimensionService eds = new EntityDimensionService();

	/* (non-Javadoc)
	 * @see smartx.publics.datatask.DataTaskExecuteIFC#dataTaskExec(org.dom4j.Element)
	 */
	@Override
	public void dataTaskExec(Element taskEle,DataTaskExecThread mainThread) throws Exception {
		if(taskEle != null ){
			String modelCode = taskEle.element("modelCode").getText();
			String dimensionCode = taskEle.element("dimensionCode").getText();
			eds.dealDimension(eds.getEntityModelContent(modelCode), modelCode, dimensionCode);
		}
	}
}
