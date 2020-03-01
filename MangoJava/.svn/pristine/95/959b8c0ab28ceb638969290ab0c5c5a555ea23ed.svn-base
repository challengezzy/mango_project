package smartx.bam.bs.scenariomnager;

import java.util.Map;

import org.apache.log4j.Logger;

import smartx.bam.vo.DatabaseConst;
import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.utils.StringUtil;
import smartx.framework.common.vo.HashVO;
import smartx.framework.metadata.vo.ComBoxItemVO;
import smartx.framework.metadata.vo.RefItemVO;
import smartx.publics.cep.bs.service.SmartXCEPService;

public class CopyService {
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	private SmartXCEPService sxCepservice;
	
	public CopyService(SmartXCEPService cepService){
		this.sxCepservice = cepService;
	}
	
	public void copyAlertType(Map<String,Object> dataValue) throws Exception{
		
		CommDMO dmo = new CommDMO();
		try{
			
			RefItemVO buVo = (RefItemVO)dataValue.get("BUSINESSSCENARIOID");
			String businessscenarioid = buVo.getId();
			
			String name = (String)dataValue.get("NAME");
			String code = (String)dataValue.get("CODE");
			String description = (String)dataValue.get("DESCRIPTION");
			
			ComBoxItemVO seVo = (ComBoxItemVO)dataValue.get("SEVERITY");
			String severity = seVo.getId();
			
			String subject = (String)dataValue.get("SUBJECT");
			String body = (String)dataValue.get("BODY");
			
			String insertSQL = "insert into bam_alert(id,status,businessscenarioid,name,code,description,severity,subject,body) " +
					" values(s_bam_alert.nextval,0,?,?,?,?,?,?,?)";
			
			dmo.executeUpdateByDS(null, insertSQL, businessscenarioid,name,code,description,severity,subject,body);
			dmo.commit(null);
			
			
		}catch(Exception e){
			dmo.rollback(null);
			logger.debug("",e);
			throw e;
		}finally{
			dmo.releaseContext(null);
		}
		
	}
	
	public void copyAlertRule(Map<String,Object> dataValue) throws Exception{
		CommDMO dmo = new CommDMO();
		try{
			
			RefItemVO buVo = (RefItemVO)dataValue.get("BUSINESSSCENARIOID");
			String businessscenarioid = buVo.getId();
			
			String name = (String)dataValue.get("NAME");
			String code = (String)dataValue.get("CODE");
			String description = (String)dataValue.get("DESCRIPTION");
			String condition = (String)dataValue.get("CONDITION");
			String holdsfor = (String)dataValue.get("HOLDSFOR");
			
			RefItemVO acVo = (RefItemVO)dataValue.get("ACTIONALERTID");
			String actionalertid = acVo.getId();
			
			ComBoxItemVO aVo = (ComBoxItemVO)dataValue.get("ACTIONTYPE");
			String actiontype = aVo.getId();
			
			RefItemVO sdVo = (RefItemVO)dataValue.get("STREAMMODULENAME");
			String streammodulename = sdVo.getId();
			
//			String businessruleid = (String)dataValue.get("BUSINESSRULEID");
			
			String insertSQL = "insert into bam_rule(id,businessscenarioid,name,code,description,condition,holdsfor,actionalertid,actiontype,streammodulename) " +
					" values(s_bam_rule.nextval,?,?,?,?,?,?,?,?,?)";
			
			dmo.executeUpdateByDS(null, insertSQL, businessscenarioid,name,code,description,condition,holdsfor,actionalertid,actiontype,streammodulename);
			dmo.commit(null);
			
			insertCEP(dataValue);
			
		}catch(Exception e){
			dmo.rollback(null);
			logger.debug("",e);
			throw e;
		}finally{
			dmo.releaseContext(null);
		}
	}
	
	private void insertCEP(Map<String, Object> dataValue){
		
		CommDMO dmo = new CommDMO();
		
		if(dataValue == null){
			return;
		}
		
		try{
			
			String ruleCode = (String)dataValue.get("CODE");
			
			String condition = (String)dataValue.get("CONDITION");
			
			String businessScenarioId = ((RefItemVO)dataValue.get("BUSINESSSCENARIOID")).getId();
			
			String operator = (String)dataValue.get("OPERATOR");
			
			String moduleName = "";
			
			String searchSQL = "SELECT T.STREAMMODULENAME AS MODULENAME FROM BAM_BUSINESSVIEW T,BAM_BUSINESSSCENARIO BB WHERE T.CODE = BB.DATASOURCECODE AND T.STREAMMODULENAME IS NOT NULL AND BB.ID=?";
			
			HashVO[] module = dmo.getHashVoArrayByDS(DatabaseConst.DATASOURCE_DEFAULT, searchSQL,businessScenarioId);
			if(module != null && module.length>0){
				
				moduleName = module[0].getStringValue("MODULENAME");
				
				String update = "UPDATE BAM_RULE T SET T.STREAMMODULENAME=? WHERE T.CODE=?";
				dmo.executeUpdateByDS(DatabaseConst.DATASOURCE_DEFAULT, update,moduleName,ruleCode);
				dmo.commit(DatabaseConst.DATASOURCE_DEFAULT);
				
			}
			
			searchSQL = " select * from pub_cep_streammodule where name=?";
			
			HashVO[] temp = dmo.getHashVoArrayByDS(DatabaseConst.DATASOURCE_DEFAULT, searchSQL,moduleName);
			
			if(temp == null || temp.length == 0){
				logger.debug("没有找到相关的模块,无法创建监听语句!moduleName=["+moduleName+"]");
				return;
			}
			
			String providerName = StringUtil.isEmpty(temp[0].getStringValue("PROVIDERNAME"))?"DEFAULTPROVIDERNAME":temp[0].getStringValue("PROVIDERNAME");
			
			
			searchSQL = "SELECT DISTINCT BV.STREAMNAME FROM BAM_BUSINESSVIEW BV, BAM_BUSINESSSCENARIO BS, BAM_RULE BR WHERE BR.BUSINESSSCENARIOID = BS.ID AND BS.DATASOURCECODE = BV.CODE AND BS.ID=?";
			
			HashVO[] vos = dmo.getHashVoArrayByDS(DatabaseConst.DATASOURCE_DEFAULT, searchSQL,businessScenarioId);
			if(vos == null || vos.length == 0){
				logger.debug("没有找到相关的事件,无法更新监听语句!moduleName=["+moduleName+"]");
				return;
			}
			
			String eventName = StringUtil.isEmpty(vos[0].getStringValue("STREAMNAME"))?"":vos[0].getStringValue("STREAMNAME");
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
				if(condition.trim().toLowerCase().startsWith("where")){
					epl.append(" "+condition);
				}else if(condition.trim().toLowerCase().startsWith("as")){
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
				dmo.rollback(DatabaseConst.DATASOURCE_DEFAULT);
			} catch (Exception e1) {
				logger.debug("数据回滚出错!",e);
				e1.printStackTrace();
			}
		}finally{
			dmo.releaseContext(DatabaseConst.DATASOURCE_DEFAULT);
		}
		
	
	}

}
