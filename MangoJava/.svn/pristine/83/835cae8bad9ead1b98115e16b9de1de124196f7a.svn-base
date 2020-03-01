/**
 * 
 */
package smartx.bam.bs.scenariomnager;

import org.apache.log4j.Logger;

import smartx.bam.vo.DatabaseConst;
import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaLogger;
import smartx.publics.cep.bs.service.SmartXCEPService;

/**
 * @author caohenghui
 * 
 */
public class ScenarioManager {

	private Logger logger = NovaLogger.getLogger(this.getClass());

	private CommDMO dmo = null;

	private static final String STMT_HEAD = "stmt_";

	private SmartXCEPService sxCepservice;
	
	private static MessageDispatcher messageDispatcher = null;
	
	public ScenarioManager(SmartXCEPService sxCepservice){
		this.sxCepservice = sxCepservice;
	}

	private CommDMO getCommDMO() {
		if (dmo == null)
			dmo = new CommDMO();
		return dmo;
	}
	
	private MessageDispatcher getMessageDispatcherInstance(){
		if(messageDispatcher == null ){
			messageDispatcher = new MessageDispatcher(DatabaseConst.DATASOURCE_DEFAULT);
		}
		return messageDispatcher;
	}

	public void start(String ruleCode) throws Exception {

		String providerName = null;
		String moduleName = null;

		try {

			String searchSQL = "SELECT PCS.NAME,PCS.PROVIDERNAME FROM PUB_CEP_STREAMMODULE PCS WHERE PCS.NAME=?";

			HashVO[] temp = this.getCommDMO().getHashVoArrayByDS(
					DatabaseConst.DATASOURCE_DEFAULT, searchSQL, "rule_"+ruleCode);

			if (temp != null && temp.length > 0) {
				for (HashVO vo : temp) {
					
					providerName = vo.getStringValue("PROVIDERNAME");
					moduleName = vo.getStringValue("NAME");
					sxCepservice.startStreamModule(providerName, moduleName);
				}
			} else {
				throw new Exception( "没有找到相关流模块" );
			}

		} catch (Exception e) {
			logger.debug("模块启动失败[providerName=" + providerName + ",name="
					+ moduleName, e);
			throw e;
		}
		
		this.addStatementListener(providerName, moduleName, ruleCode);

	}

	public void stop(String ruleCode) throws Exception {
		
		String providerName = null;
		String moduleName = null;

		try {
			
			String searchSQL = "SELECT PCS.NAME,PCS.PROVIDERNAME FROM PUB_CEP_STREAMMODULE PCS WHERE PCS.NAME=?";

			HashVO[] temp = this.getCommDMO().getHashVoArrayByDS(
					DatabaseConst.DATASOURCE_DEFAULT, searchSQL, "rule_"+ruleCode);

			if (temp != null && temp.length > 0) {
				for (HashVO vo : temp) {
					
					providerName = vo.getStringValue("PROVIDERNAME");
					moduleName = vo.getStringValue("NAME");
					
					try{
						this.removeStatementListener(providerName, moduleName);
					}catch(Exception e){
						logger.error("", e);
					}
					
					sxCepservice.stopStreamModule(providerName, moduleName);
				}
			} else {
				throw new Exception( "没有找到相关流模块" );
			}

		} catch (Exception e) {
			logger.debug("停止模块失败![providerName=" + providerName + ",name="
					+ moduleName, e);
			throw e;
		}finally{
			this.getCommDMO().releaseContext(DatabaseConst.DATASOURCE_DEFAULT);
		}

	}
	
	public void addStatementListener(String providerName,String moduleName,String ruleCode) throws Exception {
		try {

			sxCepservice.addStatementListener(providerName, STMT_HEAD
					+ moduleName, new RuleListener(ruleCode,this.getMessageDispatcherInstance()));

		} catch (Exception e) {
			this.stop(ruleCode);
			logger.debug("添加监听器失败!", e);
			throw e;
		}finally{
			this.getCommDMO().releaseContext(DatabaseConst.DATASOURCE_DEFAULT);
		}
	}
	
	public void removeStatementListener(String providerName,String moduleName) throws Exception {
		try {

			sxCepservice.removeStatementListener(providerName, STMT_HEAD+moduleName);

		} catch (Exception e) {
			logger.debug("删除监听器失败!", e);
			throw e;
		}finally{
			this.getCommDMO().releaseContext(DatabaseConst.DATASOURCE_DEFAULT);
		}
	}

}
