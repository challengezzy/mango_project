/**
 * 
 */
package smartx.bam.bs.scenariomnager;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import smartx.bam.vo.DatabaseConst;
import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.utils.StringUtil;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaLogger;
import smartx.framework.metadata.vo.Pub_Templet_1VO;
import smartx.framework.metadata.vo.RefItemVO;
import smartx.publics.cep.bs.service.SmartXCEPService;
import smartx.publics.form.bs.service.FormInterceptor;

/**
 * @author caohenghui
 * 
 */
public class RuleInsertAfFormInterceptor implements FormInterceptor {
	
	private Logger logger = NovaLogger.getLogger(this.getClass());
	
	private SmartXCEPService sxCepservice = new SmartXCEPService();
	
	private CommDMO dmo = null;
	
	public CommDMO getCommDMO() {
		if (dmo == null)
			dmo = new CommDMO();
		return dmo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * smartx.publics.form.bs.service.FormInterceptor#doSomething(smartx.framework
	 * .metadata.vo.Pub_Templet_1VO, java.util.Map)
	 */
	@Override
	public void doSomething(Pub_Templet_1VO templetVO,
			Map<String, Object> dataValue){
		
		if(templetVO == null || dataValue == null){
			return;
		}
		
		try{
			
			String ruleCode = (String)dataValue.get("CODE");
			
			String condition = (String)dataValue.get("CONDITION");
			
			String businessScenarioId = ((RefItemVO)dataValue.get("BUSINESSSCENARIOID")).getId();
			
			String operator = (String)dataValue.get("OPERATOR");
			
			String moduleName = "";
			
			String searchSQL = "SELECT T.STREAMMODULENAME AS MODULENAME FROM BAM_BUSINESSVIEW T,BAM_BUSINESSSCENARIO BB WHERE T.CODE = BB.DATASOURCECODE AND T.STREAMMODULENAME IS NOT NULL AND BB.ID=?";
			
			HashVO[] module = this.getCommDMO().getHashVoArrayByDS(DatabaseConst.DATASOURCE_DEFAULT, searchSQL,businessScenarioId);
			if(module != null && module.length>0){
				
				moduleName = module[0].getStringValue("MODULENAME");
				
				String update = "UPDATE BAM_RULE T SET T.STREAMMODULENAME=? WHERE T.CODE=?";
				this.getCommDMO().executeUpdateByDS(DatabaseConst.DATASOURCE_DEFAULT, update,moduleName,ruleCode);
				this.getCommDMO().commit(DatabaseConst.DATASOURCE_DEFAULT);
				
			}
			
			searchSQL = " select * from pub_cep_streammodule where name=?";
			
			HashVO[] temp = this.getCommDMO().getHashVoArrayByDS(DatabaseConst.DATASOURCE_DEFAULT, searchSQL,moduleName);
			
			if(temp == null || temp.length == 0){
				logger.debug("没有找到相关的模块,无法创建监听语句!moduleName=["+moduleName+"]");
				return;
			}
			
			String providerName = this.isEmpty(temp[0].getStringValue("PROVIDERNAME"))?"DEFAULTPROVIDERNAME":temp[0].getStringValue("PROVIDERNAME");
			
			
			searchSQL = "SELECT DISTINCT to_char(BV.STREAMNAME) STREAMNAME FROM BAM_BUSINESSVIEW BV, BAM_BUSINESSSCENARIO BS, BAM_RULE BR WHERE BR.BUSINESSSCENARIOID = BS.ID AND BS.DATASOURCECODE = BV.CODE AND BS.ID=?";
			
			HashVO[] vos = this.getCommDMO().getHashVoArrayByDS(DatabaseConst.DATASOURCE_DEFAULT, searchSQL,businessScenarioId);
			if(vos == null || vos.length == 0){
				logger.debug("没有找到相关的事件,无法更新监听语句!moduleName=["+moduleName+"]");
				return;
			}
			
			String eventName = this.isEmpty(vos[0].getStringValue("STREAMNAME"))?"":vos[0].getStringValue("STREAMNAME");
			if(eventName == null || eventName.trim().equals("")){
				logger.debug("事件名称为空,无法更新监听语句!moduleName=["+moduleName+"]");
				return;
			}
			
			String newModuleName ="rule_"+ruleCode.trim();
			String stmtName = "stmt_"+newModuleName.trim();
			
			StringBuffer epl = new StringBuffer();
			
			epl.append("module "+newModuleName+";\n");
			epl.append("uses "+moduleName+";\n");
			epl.append("@Name(\""+stmtName+"\") ");
			epl.append("select * from "+eventName);
			
			if(condition != null && ! condition.trim().equals("")){
				if(StringUtil.trim(condition).toLowerCase().startsWith("where ")){
					epl.append(" "+condition);
				}else if(StringUtil.trim(condition).toLowerCase().startsWith("as ")){
					epl.append(" "+condition);
				}else{
					epl.append(" where "+condition);
				}
			}
			
			epl.append(";");
			
			sxCepservice.deployStreamModule(providerName, newModuleName, epl.toString(), operator);
			
			
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
