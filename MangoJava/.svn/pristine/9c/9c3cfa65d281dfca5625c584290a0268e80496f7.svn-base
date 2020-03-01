/**
 * 
 */
package smartx.bam.bs.scenariomnager;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaLogger;
import smartx.framework.metadata.vo.Pub_Templet_1VO;
import smartx.publics.cep.bs.service.SmartXCEPService;
import smartx.publics.cep.vo.CEPConst;
import smartx.publics.form.bs.service.FormInterceptor;
import smartx.publics.form.bs.service.SmartXFormService;

/**
 * @author caohenghui
 *
 */
public class BusinessruleDelBfFromInterceptor implements FormInterceptor {
	
	private Logger logger = NovaLogger.getLogger(this.getClass());
	
	private SmartXCEPService sxCepservice = new SmartXCEPService();
	
	private final String STMT_HEAD = "stmt_";

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
		CommDMO dmo = new CommDMO();
		try{
			
			for (Map<String, Object> dataValue : dataValueList) {
				String flag = (String) dataValue.get(SmartXFormService.KEYNAME_MODIFYFLAG);
				String id = (String)dataValue.get("ID");
				if ("delete".equalsIgnoreCase(flag.trim())){
					String searchSQL = "select * from bam_rule where businessruleid=?";
					HashVO[] temp = dmo.getHashVoArrayByDS(null, searchSQL, id);
					if(temp != null){
						for(HashVO vo : temp){
							
							String ruleCode = vo.getStringValue("code");
							String deleteSQL = "delete bam_rule where code=?";
							dmo.executeUpdateByDS(null, deleteSQL, ruleCode);
							dmo.commit(null);
							
							try{
								sxCepservice.removeStatementListener(CEPConst.DEFAULTPROVIDERNAME_CEP, STMT_HEAD+"rule_"+ruleCode.trim());
								sxCepservice.undeployStreamModule(CEPConst.DEFAULTPROVIDERNAME_CEP, "rule_"+ruleCode.trim());
							}catch(Exception e){
								logger.debug(e.getMessage());
							}
							
						}
					}

				}

			}
			
		}catch(Exception e){
			logger.debug(e.getMessage(),e);
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
