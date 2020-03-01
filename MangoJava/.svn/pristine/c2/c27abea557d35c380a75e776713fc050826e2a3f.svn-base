/**
 * 
 */
package smartx.bam.bs.scenariomnager;

import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.utils.StringUtil;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.SimpleHashVO;
import smartx.framework.metadata.vo.TableDataStruct;
import smartx.publics.cep.bs.StreamModuleDeploymentManager;
import smartx.publics.cep.bs.service.SmartXCEPService;
import smartx.publics.cep.vo.CEPConst;
import smartx.publics.form.bs.service.SmartXFormService;

/**
 * @author caohenghui
 *
 */
public class RuleTempleteManager {
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	private SmartXFormService formService = new SmartXFormService();
	
	private ScenarioManager smService = null;
	
	private SmartXCEPService sxCepservice ;
	
	public RuleTempleteManager(SmartXCEPService sxCepservice)
	{
		this.sxCepservice = sxCepservice;
		smService = new ScenarioManager(sxCepservice);
	}
	
	public String[] getRuleTempleteFields(String scenarioId){
		
		CommDMO dmo = new CommDMO();
		String[] keys = null;
		try{
			String windowName = null;
			String searchSQL ="select bv.streamwindowname from bam_businessscenario bs,bam_businessview bv where bs.datasourcecode = bv.code and bs.id=?";
			HashVO[] temp = dmo.getHashVoArrayByDS(null,searchSQL,scenarioId);
			if(temp != null && temp.length>0){
				windowName = temp[0].getStringValue("streamwindowname");
			}
			
			if(!StringUtil.isEmpty(windowName)){
				
				String epl = "select * from "+windowName+" where 1=2";
				TableDataStruct tds = dmo.getTableDataStructByDS(StreamModuleDeploymentManager.JDBC_DATASOURCE_PREFIX+CEPConst.DEFAULTPROVIDERNAME_CEP,epl);
				if(tds !=null){
					keys = tds.getTable_header();
				}
			}
			
		}catch(Exception e){
			logger.debug("",e);
		}
		
		return keys;
	}
	
	public String[] getRuleTemplateParaNames(String templeteId){
		CommDMO dmo = new CommDMO();
		String[] paras = null;
		try{
			String searchSQL ="select t.name from bam_ruletemplateparameter t where t.ruletemplateid=?";
			HashVO[] temp = dmo.getHashVoArrayByDS(null,searchSQL,templeteId);
			if(temp != null && temp.length>0){
				paras = new String[temp.length];
				for(int i =0 ; i<temp.length; i++){
					HashVO vo = temp[i];
					paras[i] = vo.getStringValue("name");
				}
			}
			
		}catch(Exception e){
			logger.debug("",e);
		}
		
		return paras;
	}
	
	public SimpleHashVO[] getRuleEventData(String scenarioId){
		CommDMO dmo = new CommDMO();
		SimpleHashVO[] shvos = null;
		try{
			String windowName = null;
			String searchSQL ="select bv.streamwindowname from bam_businessscenario bs,bam_businessview bv where bs.datasourcecode = bv.code and bs.id=?";
			HashVO[] temp = dmo.getHashVoArrayByDS(null,searchSQL,scenarioId);
			if(temp != null && temp.length>0){
				windowName = temp[0].getStringValue("streamwindowname");
			}
			
			if(!StringUtil.isEmpty(windowName)){
				
				String epl = "select * from "+windowName +" limit 5";
				shvos = formService.getSimpleHashVoArrayByDS(StreamModuleDeploymentManager.JDBC_DATASOURCE_PREFIX+CEPConst.DEFAULTPROVIDERNAME_CEP,epl);
			}
			
		}catch(Exception e){
			logger.debug("",e);
		}
		
		return shvos;
	
	}
	
	public SimpleHashVO[] getRuleTemplateData(String templeteId){
		
		SimpleHashVO[] shvos = null;
		try{
			String searchSQL ="select * from bam_ruletemplate t where t.id="+templeteId;
			shvos = formService.getSimpleHashVoArrayByDS(null,searchSQL);
			
		}catch(Exception e){
			logger.debug("",e);
		}
		
		return shvos;
	}
	
	public SimpleHashVO[] getRuleTemplateParas(String templeteId){
		SimpleHashVO[] shvos = null;
		try{
			String searchSQL ="select * from bam_ruletemplateparameter t where t.ruletemplateid="+templeteId;
			shvos = formService.getSimpleHashVoArrayByDS(null,searchSQL);
			
		}catch(Exception e){
			logger.debug("",e);
		}
		return shvos;
	}
	
	public void saveOrupdateRuleTemplate(Map<String,String> map){
		CommDMO dmo = new CommDMO();
		try{
			
			String id = map.get("id");
			String businessscenarioid = map.get("businessscenarioid");
			String name = StringUtil.isEmpty(map.get("name"))?null:map.get("name");
			String code = StringUtil.isEmpty(map.get("code"))?null:map.get("code");
			String condition =  StringUtil.isEmpty(map.get("condition"))?null:map.get("condition");
			String holdsfor =  StringUtil.isEmpty(map.get("holdsfor"))?null:map.get("holdsfor");
			String resetcondition = StringUtil.isEmpty(map.get("resetcondition"))?null:map.get("resetcondition");
			String resetholdsfor = StringUtil.isEmpty(map.get("resetholdsfor"))?null:map.get("resetholdsfor");
			String showcondition = StringUtil.isEmpty(map.get("showcondition"))?"1":map.get("showcondition");
			String subject = StringUtil.isEmpty(map.get("subject"))?null:map.get("subject");
			String body = StringUtil.isEmpty(map.get("body"))?null:map.get("body");
			String severity = StringUtil.isEmpty(map.get("severity"))?"1":map.get("severity");
			String description = null;
			
			String searchSQL = "select count(id) cou from bam_ruletemplate where id=?";
			
			HashVO[] temp = dmo.getHashVoArrayByDS(null, searchSQL,id);
			int count = 0;
			if(temp !=null && temp.length>0){
				count = temp[0].getIntegerValue("cou");
			}
			
			if(count > 0 ){
				
				String updateSQL = "update bam_ruletemplate set businessscenarioid=?,name=?,code=?,description=?,condition=?,holdsfor=?,resetcondition=?,resetholdsfor=?,severity=?, subject=?,body=?, showcondition=? where id=?";

				dmo.executeUpdateByDS(null, updateSQL, businessscenarioid,
						name, code, description, condition, holdsfor,
						resetcondition, resetholdsfor, severity, subject, body,
						showcondition, id);
				
				
			}else{
				
				String updateSQL = "insert into bam_ruletemplate (id,businessscenarioid,name,code,description,condition,holdsfor,resetcondition,resetholdsfor,severity, subject,body, showcondition) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";

				dmo.executeUpdateByDS(null, updateSQL, id,businessscenarioid,
						name, code, description, condition, holdsfor,
						resetcondition, resetholdsfor, severity, subject, body,
						showcondition);
				
			}
			dmo.commit(null);
			
			
		}catch(Exception e){
			logger.debug("",e);
		}finally{
			dmo.releaseContext(null);
		}
	}
	
	public boolean isExist(String templeteId) {
		CommDMO dmo = new CommDMO();
		boolean flag = false;
		try{
			String searchSQL = "select count(id) cou from bam_ruletemplate where id=?";
			HashVO[] temp = dmo.getHashVoArrayByDS(null, searchSQL,templeteId);
			int count = 0;
			if(temp !=null && temp.length>0){
				count = temp[0].getIntegerValue("cou");
			}
			if(count>0){
				flag = true;
			}else{
				flag = false;
			}
		}catch(Exception e){
			logger.debug("",e);
		}
		return flag;
	}
	
	public void deleteRuleTemplete(String templeteId){
		CommDMO dmo = new CommDMO();
		try{
			String deleteTempletepara = "delete from bam_ruletemplateparameter rtp where rtp.ruletemplateid="+templeteId;
			String deleteTempleteIns = "delete from bam_businessrule b where b.ruletempateid="+templeteId;
			String deleteTemplete ="delete from bam_ruletemplate rt where rt.id="+templeteId;
			
			dmo.executeBatchByDS(null, new String[]{deleteTempletepara,deleteTempleteIns,deleteTemplete});
			dmo.commit(null);
		}catch(Exception e){
			logger.debug("",e);
		}
	}
	
	public void createRuleByTemplete(String templeteId,String operator,String businessruleId,Map<String, String> dataValue,Map<String,String> dataType,Map<String,String> dataInfo,boolean isEdit) throws Exception{
		CommDMO dmo = new CommDMO();
		try{
			
			//根据模板ID创建告警类型
			String searchSQL = "select distinct brt.businessscenarioid,brt.condition,brt.holdsfor,brt.resetcondition,brt.resetholdsfor,brt.severity,brt.subject,to_char(brt.body) as body,bv.streammodulename,bv.streamname " +
					" from bam_ruletemplate brt,bam_businessscenario bs,bam_businessview bv where brt.businessscenarioid=bs.id and bs.datasourcecode = bv.code and brt.id=?";
			
			HashVO[] temp = dmo.getHashVoArrayByDS(null, searchSQL,templeteId);
			if(temp == null || temp.length ==0){
				throw new Exception("没有找到规则模板的相关信息!不能创建规则!");
			}
			
			String businessscenarioid = temp[0].getStringValue("businessscenarioid");
			
			String condition = this.convertExpression(dataValue, dataType, temp[0].getStringValue("condition"));
			
			int holdsfor = StringUtil.isEmpty(temp[0].getStringValue("holdsfor"))?0:temp[0].getIntegerValue("holdsfor");
			
			String resetcondition = this.convertExpression(dataValue, dataType,temp[0].getStringValue("resetcondition"));
			int resetholdsfor = StringUtil.isEmpty(temp[0].getStringValue("resetholdsfor"))?0:temp[0].getIntegerValue("resetholdsfor");
			
			int severity = temp[0].getIntegerValue("severity");
			String subject = temp[0].getStringValue("subject");
			String body = temp[0].getStringValue("body");
			
			String moduleName = temp[0].getStringValue("streammodulename");
			String eventName = temp[0].getStringValue("streamname");
			
			String providerName = "DEFAULTPROVIDERNAME";
			
			String newAlertId = null;
			if(isEdit){
				newAlertId = dataInfo.get("alertid");
			}else{
				newAlertId = dmo.getSequenceNextValByDS(null, "s_bam_alert");;
			}
			
			if(StringUtil.isEmpty(newAlertId)){
				throw new Exception("无法获取告警类型ID!不能创建规则!");
			}
			String newRuleId = dmo.getSequenceNextValByDS(null, "s_bam_rule");
			if(StringUtil.isEmpty(newRuleId)){
				throw new Exception("无法获取规则ID!不能创建规则!");
			}
			
			String resetRuleId = dmo.getSequenceNextValByDS(null, "s_bam_rule");
			if(StringUtil.isEmpty(resetRuleId)){
				throw new Exception("无法获取重置规则ID!不能创建规则!");
			}
			
			String besName = dataInfo.get("besname");
			String besCode = dataInfo.get("bescode");
			String ruleName = dataInfo.get("rulename");
			String ruleCode = dataInfo.get("rulecode");
			String actiontype = dataInfo.get("actiontype");
			String description = dataInfo.get("description");
			String parameterinfo = getParameterinfo(dataValue);
			String besdesc = dataInfo.get("besdesc");
			
			String alertCode = "alertCode"+newAlertId;
			String resetRuleCode =ruleCode+"_reset";
			
			String alertName = "告警"+newAlertId;
			String resetRuleName = "重置规则"+resetRuleId;
			
			if(isEdit){
				String updateBusinessruleSQL="update bam_businessrule set lastupdatetime=sysdate,name=?,code=?,description=?,parameterinfo=? where id=?";
				dmo.executeUpdateByDS(null, updateBusinessruleSQL, besName,besCode,besdesc,parameterinfo,businessruleId);
				dmo.commit(null);
			}else{
				//新建时不能填已存在的编号
				if(checkRuleCode(ruleCode)){
					throw new Exception("告警规则编号已存在,不能新建!");
				}
				
				String insertBusinessruleSQL="insert into bam_businessrule ( id,name, code, description, parameterinfo, ruletempateid,createtime,lastupdatetime) " +
				"values(?,?,?,?,?,?,sysdate,sysdate)";
				dmo.executeUpdateByDS(null, insertBusinessruleSQL, businessruleId,besName,besCode,besdesc,parameterinfo,templeteId);
				dmo.commit(null);
				
			}
			if(!isEdit){
				String insertAlertSQL = "insert into bam_alert (id,businessscenarioid,name,code,description,severity,subject,body,status) " +
				"values(?,?,?,?,?,?,?,?,?)";
				dmo.executeUpdateByDS(null, insertAlertSQL, newAlertId,businessscenarioid,alertName,alertCode,"",severity,subject,body,0);
				
				String insertSubsciber = "insert into bam_subsciber(id,objectname,type,subscription,deliverytype,alertid) values(s_bam_subsciber.nextval,?,?,?,?,?)";
				dmo.executeUpdateByDS(null, insertSubsciber, operator,0,0,0,newAlertId);
			}

			
			String deleteRuleSQL ="delete from bam_rule where code=?";
			dmo.executeUpdateByDS(null, deleteRuleSQL,ruleCode);
			dmo.executeUpdateByDS(null, deleteRuleSQL,resetRuleCode);
			dmo.commit(null);
			
			String insertRuleSQL = "insert into bam_rule(id,businessscenarioid,name,code,description,condition,holdsfor,actionalertid,actiontype,streammodulename,businessruleid) " +
					" values(?,?,?,?,?,?,?,?,?,?,?)";
			dmo.executeUpdateByDS(null,insertRuleSQL, newRuleId, businessscenarioid,ruleName,ruleCode,description,condition,holdsfor,newAlertId,actiontype,moduleName,businessruleId);
			dmo.executeUpdateByDS(null,insertRuleSQL, resetRuleId, businessscenarioid,resetRuleName,resetRuleCode,description,resetcondition,resetholdsfor,newAlertId,2,moduleName,businessruleId);
			
			dmo.commit(null);
			
			//根据新创建的规则创建流模块	
			
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
			
			String newResetModuleName ="rule_"+resetRuleCode.trim();
			String stmtResetName = "stmt_"+newResetModuleName.trim();
			
			StringBuffer resetEpl = new StringBuffer();
			
			resetEpl.append("module "+newResetModuleName+";\n");
			resetEpl.append("uses "+moduleName+";\n");
			resetEpl.append("@Name(\""+stmtResetName+"\") ");
			resetEpl.append("select * from "+eventName);
			
			if(resetcondition != null && ! resetcondition.trim().equals("")){
				if(resetcondition.trim().toLowerCase().startsWith("where")){
					resetEpl.append(" "+resetcondition);
				}else if(resetcondition.trim().toLowerCase().startsWith("as")){
					resetEpl.append(" "+resetcondition);
				}else{
					resetEpl.append(" where "+resetcondition);
				}
			}
			
			resetEpl.append(";");
			
			try{
				//先停掉相关的监听规则
				smService.stop(ruleCode);
				smService.stop(resetRuleCode);
			}catch(Exception e){
			}
			
			try{
				//卸载原有的规则
				sxCepservice.undeployStreamModule(providerName, newModuleName);
				sxCepservice.undeployStreamModule(providerName, newResetModuleName);
			}catch(Exception e){
			}
			
			//发部新的规则
			sxCepservice.deployStreamModule(providerName, newModuleName, epl.toString(), operator);
			sxCepservice.deployStreamModule(providerName, newResetModuleName, resetEpl.toString(), operator);
			
			try{
				//启动新的规则
				smService.start(ruleCode);
				smService.start(resetRuleCode);
			}catch(Exception e){
				logger.debug("无法启动模块",e);
			}
			
		}catch(Exception e){
			logger.debug("无法写入数据库!",e);
			try {
				dmo.rollback(null);
			} catch (Exception e1) {
				logger.debug("数据回滚出错!",e1);
			}
			throw e;
		}finally{
			dmo.releaseContext(null);
		}
	}
	
	private String convertExpression(Map<String, String> dataValue,Map<String,String> dataType, String exp) {
		String str_newdsn = exp;
		if(StringUtil.isEmpty(str_newdsn)){
			return "";
		}
		try {
			String[] keys = StringUtil.getFormulaMacPars(exp);
			for (int i = 0; i < keys.length; i++) {
				String value = dataValue.get(keys[i]);
				if (value != null) {
					String type = dataType.get(keys[i]);
					if(type.trim().equalsIgnoreCase("string")){
						value = "'"+value+"'";
					}
					str_newdsn = StringUtil.replaceAll(str_newdsn, "{"+ keys[i] + "}", value); // 替换
				}
			}

		} catch (Exception e) {
			logger.debug("解析表达式出错!", e);
		}
		return str_newdsn;
	}
	
	public SimpleHashVO[] getParaInfo(String templeteId,String businessruleId,boolean isEdit) throws Exception{
		SimpleHashVO[] shvos = null;
		try{
			String searchSQL = "";
			if(isEdit){
				searchSQL = "select distinct brp.name pname,brp.type ptype,brp.description pdesc,brp.caption pcaption,bbr.name rname,bbr.code rcode,to_char(bbr.parameterinfo) parameterinfo " +
						"from bam_ruletemplateparameter brp, bam_businessrule bbr, bam_rule br " +
						"where brp.ruletemplateid(+)= bbr.ruletempateid and br.businessruleid = bbr.id(+) and bbr.id="+businessruleId;
			}else{
				searchSQL ="select distinct brp.name pname,brp.type ptype, brp.description pdesc, brp.caption pcaption, '' rname, '' rcode, '' parameterinfo from bam_ruletemplateparameter brp where  brp.ruletemplateid ="+templeteId;
			}
			
			shvos = formService.getSimpleHashVoArrayByDS(null,searchSQL);
			
		}catch(Exception e){
			logger.debug("",e);
			throw e;
		}
		return shvos;
	}
	
	public SimpleHashVO[] getCondition(String templeteId) throws Exception{
		SimpleHashVO[] shvos = null;
		try{
			
			String searchSQL = "select t.condition,t.resetcondition,t.showcondition from bam_ruletemplate t where t.id="+templeteId;
			
			shvos = formService.getSimpleHashVoArrayByDS(null,searchSQL);
			
		}catch(Exception e){
			logger.debug("",e);
			throw e;
		}
		return shvos;
	}
	
	private String getParameterinfo(Map<String,String> dataValue){
		String content = "";
		try{
			
			Document doc = DocumentHelper.createDocument();
			Element root = doc.addElement("root");
			
			Set<String> keys = dataValue.keySet();
			for(String key : keys){
				String value = dataValue.get(key);
				Element data = root.addElement("data");
				data.addAttribute("name", key);
				data.addAttribute("value", value);
			}
			
			content = doc.asXML();
			
		}catch(Exception e){
			logger.debug("",e);
		}
		return content;
	}
	
	private boolean checkRuleCode(String ruleCode){
		CommDMO dmo = new CommDMO();
		boolean flag = false;
		try{
			String sql = "select count(id) cou from bam_rule where code='"+ruleCode+"'";
			HashVO[] temp = dmo.getHashVoArrayByDS(null, sql);
			if(temp !=null && temp.length>0){
				int count = temp[0].getIntegerValue("cou");
				if(count>0){
					flag = true;
				}
			}
		}catch(Exception e){
			logger.debug("",e);
		}
		return flag;
	}
	
	public SimpleHashVO[] getRuleInfoByBusinessruleId(String businessruleId) throws Exception {
		
		SimpleHashVO[] shvos = null;
		try{
			
			String searchSQL = "select t.name,t.code,t.description,actionalertid from bam_rule t where (t.actiontype=0 or t.actiontype=1) and t.businessruleid="+businessruleId;
			
			shvos = formService.getSimpleHashVoArrayByDS(null,searchSQL);
			
		}catch(Exception e){
			logger.debug("",e);
			throw e;
		}
		return shvos;
	}

}
