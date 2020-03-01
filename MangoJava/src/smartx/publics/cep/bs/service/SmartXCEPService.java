package smartx.publics.cep.bs.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaLogger;
import smartx.framework.metadata.bs.NovaServerEnvironment;
import smartx.publics.cep.bs.PersistentManager;
import smartx.publics.cep.bs.StreamAppManager;
import smartx.publics.cep.bs.StreamEventInputManager;
import smartx.publics.cep.bs.StreamEventQueryManager;
import smartx.publics.cep.bs.StreamModuleDeploymentManager;
import smartx.publics.cep.bs.configuration.CEPEngineConfigurationFactory;
import smartx.publics.cep.bs.configuration.DefaultCEPEngineConfigurationFactory;
import smartx.publics.cep.bs.listener.SmartXCepListener;
import smartx.publics.cep.vo.CEPConst;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.UpdateListener;
import com.espertech.esper.client.deploy.DeploymentOrder;
import com.espertech.esper.client.deploy.DeploymentOrderOptions;
import com.espertech.esper.client.deploy.EPDeploymentAdmin;
import com.espertech.esper.client.deploy.Module;

public class SmartXCEPService {
	private Logger logger = NovaLogger.getLogger(SmartXCEPService.class);
	private StreamAppManager streamAppManager = new StreamAppManager();
	private StreamModuleDeploymentManager streamModuleDeployManager = new StreamModuleDeploymentManager();
	private StreamEventInputManager streamEventInputManager = new StreamEventInputManager();
	private StreamEventQueryManager streamEventQueryManager = new StreamEventQueryManager();
	private PersistentManager persistentManager;
	public static final String JDBC_DEFAULT_LISTEN_PORT = "8450";
	private static List<SmartXCepListener> smartXCepListeners = new ArrayList<SmartXCepListener>();
	
	private static boolean isInited = false;
	
	public SmartXCEPService(){
		if(isInited)
			return;
		isInited = true;
		initStreamModule();
	}
	
	public void reInited(){
		initStreamModule();
	}
	
	private void initStreamModule()
	{
		logger.info("初始化CEP服务");
		CommDMO dmo = new CommDMO();
		try{
			String engineConfigFactoryClass = (String)NovaServerEnvironment.getInstance().get(CEPConst.PARA_CEP_ENGINECONFIGURATION_FACTORY);
			CEPEngineConfigurationFactory configFactory;
			if(engineConfigFactoryClass != null){
				Class<CEPEngineConfigurationFactory> cls = (Class<CEPEngineConfigurationFactory>) Class.forName(engineConfigFactoryClass);
				configFactory = (CEPEngineConfigurationFactory) cls.newInstance();
			}
			else
				configFactory = new DefaultCEPEngineConfigurationFactory();
			Map<String, Configuration> configMap = new HashMap<String, Configuration>();
			for(String provider : configFactory.getProviderNamesNeedToInit()){
				Configuration config = configFactory.getConfigurationByProviderName(provider);
				if(config == null){
					config = configFactory.getDefaultConfiguration();
					if(config == null)
						config = new Configuration();
				}
				configMap.put(provider, config);
			}
			//持久化的容器需要设置关闭视图共享
			String persistentProvidersParam = (String)NovaServerEnvironment.getInstance().get(CEPConst.PARA_CEP_PERSISTENT_PROVIDERS);
			String[] persistentProviders = new String[0];
			if(persistentProvidersParam != null && !"".equals(persistentProvidersParam)){
				persistentProviders = persistentProvidersParam.split(",");
				for(String persistProvider : persistentProviders){
					Configuration config = configMap.get(persistProvider);
					if(config != null){
						config.getEngineDefaults().getViewResources().setShareViews(false);
					}
				}
			}
			//正式初始化
			for(String providerName : configMap.keySet()){
				EPServiceProvider epService = 
					EPServiceProviderManager.getProvider(providerName,configMap.get(providerName));
				epService.initialize();
			}
			//初始化持久化服务
			persistentManager = new PersistentManager(persistentProviders,true,null);
			
			for(SmartXCepListener smartXCepListener : smartXCepListeners)
			{
				smartXCepListener.process();
			}
			
			String sql = "select name,providername,source,status from PUB_CEP_STREAMMODULE";
			HashVO[] vos = dmo.getHashVoArrayByDS(null, sql);
			//首先确定module的加载顺序
			Map<String,List<Module>> moduleMap = new HashMap<String, List<Module>>();
			Map<String,HashVO> voMap = new HashMap<String, HashVO>();
			for(HashVO vo : vos){
				String providername = vo.getStringValue("providername");
				String source = vo.getStringValue("source");
				//String name = vo.getStringValue("name");
				EPServiceProvider epService = EPServiceProviderManager.getProvider(providername);
				EPDeploymentAdmin deployAdmin = epService.getEPAdministrator().getDeploymentAdmin();
				Module module = deployAdmin.parse(source);
				List<Module> list = moduleMap.get(providername);
				if(list == null){
					list = new ArrayList<Module>();
					moduleMap.put(providername, list);
				}
				list.add(module);
				voMap.put(providername+"_"+module.getName(), vo);
			}
			//对每一个provider，确定加载顺序
			for(String providername : moduleMap.keySet()){
				List<Module> list = moduleMap.get(providername);
				EPServiceProvider epService = EPServiceProviderManager.getProvider(providername);
				EPDeploymentAdmin deployAdmin = epService.getEPAdministrator().getDeploymentAdmin();
				DeploymentOrder order = deployAdmin.getDeploymentOrder(list, new DeploymentOrderOptions());
				for(Module module : order.getOrdered()){
					String moduleName = module.getName();
					HashVO vo = voMap.get(providername+"_"+moduleName);
					String name = vo.getStringValue("name");
					int status = vo.getIntegerValue("status");
					try {
						streamModuleDeployManager.addStreamModuleToEngine(providername,name);
						if(status == CEPConst.STATUS_STREAMMODULE_START){
							//需要启动
							streamModuleDeployManager.startStreamModule(providername,name);
						}
					} catch (Exception e) {
						logger.error("初始化流模块[providername="+providername+",name="+name+"]失败",e);
						streamModuleDeployManager.updateStreamModuleStatus(providername,name, CEPConst.STATUS_STREAMMODULE_ERROR);
						continue;
					}
				}
			}
			//恢复数据
			persistentManager.replayHistoryEvents();
		} catch (Exception e) {
			logger.error("初始化CEP服务失败",e);
		}
		finally{
			dmo.releaseContext(null);
		}	
		
		logger.info("初始化CEP服务完毕");
	}
	
	/**
	 * 保存流应用
	 * @param streamAppXML
	 * @param operator
	 * @throws Exception
	 */
	public void saveStreamApp(String streamAppXML, String operator) throws Exception{
		try {
			streamAppManager.saveStreamApp(streamAppXML, operator);
		} catch (Exception e) {
			logger.error("保存streamApp失败",e);
			throw e;
		}
	}
	
	/**
	 * 读取流应用XML 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public String loadStreamAppXML(String code) throws Exception{
		try {
			return streamAppManager.loadStreamAppXML(code);
		} catch (Exception e) {
			logger.error("读取streamAppXML失败",e);
			throw e;
		}
	}
	
	/**
	 * 发布流模块
	 * @param name
	 * @param source
	 * @return 模块是否已存在
	 */
	public boolean deployStreamModule(String providerName,String name, String source, String operator) throws Exception{
		try {
			return streamModuleDeployManager.deployStreamModule(providerName, name, source, operator);
		} catch (Exception e) {
			logger.error("发布流模块失败",e);
			throw e;
		}
	}
	
	/**
	 * 批量发布流模块
	 * @param moduleList
	 */
	public void batchDeployStreamModule(List<Map<String,String>> moduleList) throws Exception {
		try {
			for(Map<String, String> moduleInfo : moduleList){
				String providerName = moduleInfo.get("providerName");
				String name = moduleInfo.get("name");
				String source = moduleInfo.get("source");
				String operator = moduleInfo.get("operator");
				streamModuleDeployManager.deployStreamModule(providerName, name, source, operator);
			}
		} catch (Exception e) {
			logger.error("批量发布流模块失败",e);
			throw e;
		}
	}
	
	/**
	 * 重新发布流模块
	 * @param name
	 * @throws Exception
	 */
	public void redeployStreamModule(String providerName,String name) throws Exception{
		try {
			streamModuleDeployManager.redeployStreamModule(providerName,name);
		} catch (Exception e) {
			logger.error("重新发布流模块失败",e);
			throw e;
		}
	}
	
	/**
	 * 发布并启动流模块
	 * @param name
	 * @throws Exception
	 */
	public void deployAndStartStreamModule(String providerName,String name, String source, String operator) throws Exception{
		try {
			if(streamModuleDeployManager.deployStreamModule(providerName,name,source,operator)){
				streamModuleDeployManager.redeployStreamModule(providerName,name);
			}
			streamModuleDeployManager.startStreamModule(providerName,name);
		} catch (Exception e) {
			logger.error("发布并启动流模块失败",e);
			throw e;
		}
	}
	
	/**
	 * 启动流模块
	 * @param name
	 * @throws Exception
	 */
	public void startStreamModule(String providerName, String name) throws Exception{
		try {
			streamModuleDeployManager.startStreamModule(providerName,name);
		} catch (Exception e) {
			logger.error("启动流模块失败",e);
			throw e;
		}
	}
	
	/**
	 * 停止流模块
	 * @param name
	 * @throws Exception
	 */
	public void stopStreamModule(String providerName,String name ) throws Exception{
		try {
			streamModuleDeployManager.stopStreamModule(providerName,name);
		} catch (Exception e) {
			logger.error("停止流模块失败",e);
			throw e;
		}
	}
	
	/**
	 * 卸载流模块
	 * @param name
	 * @throws Exception
	 */
	public void undeployStreamModule(String providerName,String name) throws Exception{
		try {
			streamModuleDeployManager.undeployStreamModule(providerName,name);
		} catch (Exception e) {
			logger.error("卸载流模块失败",e);
			throw e;
		}
	}
	
	/**
	 * 输入新事件
	 * @param moduleName
	 * @param event
	 * @param eventTypeName
	 */
	public void sendEvent(String providerName, Map<String, Object> event, String eventTypeName) throws Exception{
		try {
			streamEventInputManager.sendEvent(providerName, event, eventTypeName);
		} catch (Exception e) {
			logger.error("事件发送失败",e);
			throw e;
		}
	}
	
	public void addStatementListener(String providerName,String statementName,UpdateListener listener) throws Exception{
		try{
			streamModuleDeployManager.addStatementListener(providerName, statementName, listener);
		}catch(Exception e){
			logger.error("添加监听器失败!");
			throw e;
		}
	}
	
	public void removeStatementListener(String providerName,String statementName) throws Exception{
		try{
			streamModuleDeployManager.removeStatementListener(providerName, statementName);
		}catch(Exception e){
			logger.error("删除监听器失败!");
			throw e;
		}
	}
	
	/**
	 * 查询事件
	 * @param moduleName
	 * @param epl
	 * @return
	 * @throws Exception 
	 */
	public Map<String, Object>[] queryEvent(String providerName, String epl) throws Exception{
		try {
			return streamEventQueryManager.queryEvent(providerName, epl);
		} catch (Exception e) {
			logger.error("查询事件失败",e);
			throw e;
		}
	}
	
	public static void addSmartXCepListener(SmartXCepListener listener)
	{
		smartXCepListeners.add(listener);
	}
}
