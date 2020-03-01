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
 * Dec 27, 2011
 */
public class EntityCubeDataTaskImpl implements DataTaskExecuteIFC {
	
	protected Logger logger = NovaLogger.getLogger(this.getClass());
	
	private EntityCubeService esc = new EntityCubeService();

	/* (non-Javadoc)
	 * @see smartx.publics.datatask.DataTaskExecuteIFC#dataTaskExec(org.dom4j.Element, smartx.publics.datatask.DataTaskExecThread)
	 */
	@Override
	public void dataTaskExec(Element task, DataTaskExecThread mainThread)
			throws Exception {
		if(task != null ){
			String modelCode = task.element("modelCode").getText();
			String entityCode = task.element("entityCode").getText();
			String cubeCode = task.element("cubeCode").getText();
			esc.dealCube(modelCode, entityCode, cubeCode);
		};

	}

}
