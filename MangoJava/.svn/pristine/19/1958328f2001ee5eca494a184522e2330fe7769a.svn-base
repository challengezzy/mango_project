/**
 * 
 */
package smartx.bam.bs.scenariomnager;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.vo.NovaLogger;
import smartx.framework.metadata.vo.Pub_Templet_1VO;
import smartx.publics.cep.vo.CEPConst;
import smartx.publics.form.bs.service.FormInterceptor;
import smartx.publics.form.bs.service.SmartXFormService;

/**
 * @author caohenghui
 *
 */
public class AlertDeleteBfFromInterceptor implements FormInterceptor {
	
	private Logger logger = NovaLogger.getLogger(this.getClass());

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

		// TODO Auto-generated method stub
		CommDMO dmo = new CommDMO();
		try{
			
			for (Map<String, Object> dataValue : dataValueList) {
				String flag = (String) dataValue.get(SmartXFormService.KEYNAME_MODIFYFLAG);
				String alertId = (String)dataValue.get("ID");
				if ("delete".equalsIgnoreCase(flag.trim())){
					String deleteSQL ="delete from bam_subsciber where alertid=?";
					dmo.executeUpdateByDS(null, deleteSQL, alertId);
					dmo.commit(null);
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
	public void doSomething(Map<String, Object> arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see smartx.publics.form.bs.service.FormInterceptor#doSomething(java.util.List)
	 */
	@Override
	public void doSomething(List<Map<String, Object>> arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
