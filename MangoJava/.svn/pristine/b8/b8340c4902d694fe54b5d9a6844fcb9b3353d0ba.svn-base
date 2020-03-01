/**
 * 
 */
package smartx.bam.initdsfactory;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import smartx.bam.vo.SysConst;
import smartx.framework.common.bs.dbaccess.dsmgr.DataSourceManager;
import smartx.framework.common.vo.NovaLogger;
import smartx.framework.metadata.vo.Pub_Templet_1VO;
import smartx.publics.form.bs.service.FormInterceptor;

/**
 * @author caohenghui
 * Mar 19, 2012
 */
public class DataSourceDeleteAfInterceptor implements FormInterceptor {
	
	protected Logger logger = NovaLogger.getLogger(this.getClass());

	/* (non-Javadoc)
	 * @see smartx.publics.form.bs.service.FormInterceptor#doSomething(smartx.framework.metadata.vo.Pub_Templet_1VO, java.util.Map)
	 */
	@Override
	public void doSomething(Pub_Templet_1VO templetVO,
			Map<String, Object> dataValue) throws Exception {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see smartx.publics.form.bs.service.FormInterceptor#doSomething(smartx.framework.metadata.vo.Pub_Templet_1VO, java.util.List)
	 */
	@Override
	public void doSomething(Pub_Templet_1VO templetVO,
			List<Map<String, Object>> dataValueList) throws Exception {
		
		try{
			
			if( dataValueList != null){
				for(Map<String,Object> map : dataValueList){
					String modifyType = (String)map.get("FORMSERVICE_MODIFYFLAG");
					if(modifyType.equalsIgnoreCase("delete")){
						String dsName = (String)map.get("NAME");
						DataSourceManager.destroy(SysConst.RELATION_DS_PREFIX+dsName);
					}
				}
			}
			
		}catch(Exception e){
			logger.debug("",e);
		}

	}

	/* (non-Javadoc)
	 * @see smartx.publics.form.bs.service.FormInterceptor#doSomething(java.util.Map)
	 */
	@Override
	public void doSomething(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see smartx.publics.form.bs.service.FormInterceptor#doSomething(java.util.List)
	 */
	@Override
	public void doSomething(List<Map<String, Object>> dataValueList)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
