/**
 * 
 */
package smartx.bam.bs.scenariomnager;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import smartx.bam.vo.DatabaseConst;
import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaLogger;
import smartx.framework.metadata.vo.Pub_Templet_1VO;
import smartx.framework.metadata.vo.RefItemVO;
import smartx.publics.cep.bs.service.SmartXCEPService;
import smartx.publics.cep.vo.CEPConst;
import smartx.publics.form.bs.service.FormInterceptor;

/**
 * @author caohenghui
 *
 */
public class RuleUpdateBfFormInterceptor implements FormInterceptor {
	
	private Logger logger = NovaLogger.getLogger(this.getClass());
	
	private SmartXCEPService sxCepservice = new SmartXCEPService();
	
	private final String STMT_HEAD = "stmt_";
	
	private CommDMO dmo = null;
	
	public CommDMO getCommDMO() {
		if (dmo == null)
			dmo = new CommDMO();
		return dmo;
	}

	/* (non-Javadoc)
	 * @see smartx.publics.form.bs.service.FormInterceptor#doSomething(smartx.framework.metadata.vo.Pub_Templet_1VO, java.util.Map)
	 */
	@Override
	public void doSomething(Pub_Templet_1VO templetVO,
			Map<String, Object> dataValue) {
		
		if(templetVO == null || dataValue == null){
			return;
		}
		
		try{
			
			String ruleId = (String)dataValue.get("ID");
			
			String moduleName = ((RefItemVO)dataValue.get("STREAMMODULENAME")).getName();
			
			String searchRule = "select * from bam_rule where id=?";
			
			HashVO[] ruleVOS = this.getCommDMO().getHashVoArrayByDS(DatabaseConst.DATASOURCE_DEFAULT, searchRule,ruleId);
			
			if(ruleVOS == null || ruleVOS.length==0){
				logger.debug("没有找到相关的规则,无法更新监听语句!moduleName=["+moduleName+"]");
				return;
			}
			
			String oldRuleCode = this.isEmpty(ruleVOS[0].getStringValue("CODE"))?"":ruleVOS[0].getStringValue("CODE");
			
			String searchSQL = " select * from pub_cep_streammodule where name=?";
			
			HashVO[] temp = this.getCommDMO().getHashVoArrayByDS(DatabaseConst.DATASOURCE_DEFAULT, searchSQL,"rule_"+oldRuleCode.trim());
			
			if(temp == null || temp.length == 0){
				logger.debug("没有找到相关的模块,无法更新监听语句!moduleName=["+moduleName+"]");
				return;
			}
			
			String providerName = this.isEmpty(temp[0].getStringValue("PROVIDERNAME"))?"DEFAULTPROVIDERNAME":temp[0].getStringValue("PROVIDERNAME");
			
			try{
				sxCepservice.removeStatementListener(CEPConst.DEFAULTPROVIDERNAME_CEP, STMT_HEAD+"rule_"+oldRuleCode.trim());
			}catch(Exception e){
				logger.error("", e);
			}
			
			sxCepservice.undeployStreamModule(providerName, "rule_"+oldRuleCode.trim());
			
			
		}catch(Exception e){
			logger.debug("无法写入数据库!",e);
			try {
				this.getCommDMO().rollback(DatabaseConst.DATASOURCE_DEFAULT);
			} catch (Exception e1) {
				logger.debug("数据回滚出错!",e);
				e1.printStackTrace();
			}
		}finally{
			this.getCommDMO().releaseContext(DatabaseConst.DATASOURCE_DEFAULT);
		}

	}
	
	private boolean isEmpty(String str){
		boolean flag = false;
		if(str == null || str.trim().equals("") || str.trim().equals("null")){
			flag = true;
		}
		return flag;
	}

	/* (non-Javadoc)
	 * @see smartx.publics.form.bs.service.FormInterceptor#doSomething(smartx.framework.metadata.vo.Pub_Templet_1VO, java.util.List)
	 */
	@Override
	public void doSomething(Pub_Templet_1VO templetVO,
			List<Map<String, Object>> dataValueList) throws Exception {
		// TODO Auto-generated method stub
		
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
