package smartx.publics.cep.bs;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.bs.dbaccess.dsmgr.DataSourceManager;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaLogger;
import smartx.publics.cep.vo.CEPConst;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.UpdateListener;
import com.espertech.esper.client.deploy.EPDeploymentAdmin;
import com.espertech.esper.client.deploy.Module;

/**
 * 流模块管理
 * @author gxlx
 *
 */
public class StreamModuleDeploymentManager {
	private Logger logger = NovaLogger.getLogger(this);
	private static Map<String, String> moduleDeploymentIdMap = new HashMap<String, String>();//key为模块名，值为deploymentId
	public static final String JDBC_DATASOURCE_PREFIX = "datasource_cep_";
	//private static final String JDBC_DATASOURCE_DRIVER = "com.espertech.esper.jdbc.remote.EPLRemoteJdbcDriver";
	private static final String JDBC_DATASOURCE_DRIVER = "com.espertech.esper.jdbc.local.EPLLocalJdbcDriver";
	/**
	 * 发布流模块
	 * @param name
	 * @param source
	 * @return 模块是否已存在
	 */
	public boolean deployStreamModule(String providerName, String name, String source, String operator) throws Exception{
		logger.info("发布流模块[providerName="+providerName+",name="+name+",operator="+operator+"]");
		boolean isExists = registorStreamModule(providerName,name, source, operator);
		if(!isExists){
			//新部署的模块，部署到esper引擎
			addStreamModuleToEngine(providerName, name);
		}
		logger.info("发布流模块[providerName="+providerName+",name="+name+",operator="+operator+"]完毕");
		return isExists;
	}
	
	/**
	 * 重新发布流模块
	 * @param name
	 * @throws Exception
	 */
	public void redeployStreamModule(String providerName, String name) throws Exception{
		if(name == null)
			throw new IllegalArgumentException("name不能为null");
		logger.info("重新发布流模块[name="+name+"]");
		String sql = "select providername from pub_cep_streammodule where name=? and providername=?";
		CommDMO dmo = new CommDMO();
		try{
			HashVO[] vos = dmo.getHashVoArrayByDS(null, sql, name,providerName);
			if(vos.length == 0 )
				throw new Exception("流模块不存在");
			String key = providerName+"_"+name;
			String deploymentId = moduleDeploymentIdMap.get(key);
			if(deploymentId != null){
				EPServiceProvider epService = EPServiceProviderManager.getProvider(providerName);
				EPDeploymentAdmin deployAdmin = epService.getEPAdministrator().getDeploymentAdmin();
				deployAdmin.undeployRemove(deploymentId);
				moduleDeploymentIdMap.remove(key);
			}
			addStreamModuleToEngine(providerName,name);
		}
		finally{
			dmo.releaseContext(null);
		}
		logger.info("重新发布流模块[providerName="+providerName+",name="+name+"]完毕");
	}
	
	/**
	 * 注册流模块（仅仅是保存到数据库中，不直接发布到cep引擎）
	 * @param name
	 * @param source
	 * @return 模块是否已存在
	 * @throws Exception
	 */
	public boolean registorStreamModule(String providerName, String name, String source, String operator) throws Exception{
		if(name == null)
			throw new IllegalArgumentException("name不能为null");
		if(source == null)
			throw new IllegalArgumentException("source不能为null");
		
		logger.info("注册流模块[providerName="+providerName+",name="+name+"]");
		logger.debug(source);
		CommDMO dmo = new CommDMO();
		boolean isExists = false;
		try {
			String sql = "select 1 from pub_cep_streammodule where name=? and providername=?";
			HashVO[] vos = dmo.getHashVoArrayByDS(null, sql, name,providerName);
			if(vos.length == 0){
				logger.debug("新建流模块[providerName="+providerName+",name="+name+"]");
				sql = "insert into pub_cep_streammodule(id,providername,name,source,creator,modifytime,status)" +
						"values(s_pub_cep_streammodule.nextval,?,?,empty_clob(),?,sysdate,?)";
				dmo.executeUpdateByDS(null, sql, providerName,name,operator,CEPConst.STATUS_STREAMMODULE_STOP);
				dmo.executeUpdateClobByDS(null, "source","pub_cep_streammodule","name='"+name+"' and providername='"+providerName+"'",source);
			}
			else{
				logger.debug("修改流模块[name="+name+"]");
				sql = "update pub_cep_streammodule set creator=?,modifytime=sysdate where name=? and providername=?";
				dmo.executeUpdateByDS(null, sql,operator,name,providerName);
				dmo.executeUpdateClobByDS(null, "source","pub_cep_streammodule","name='"+name+"' and providername='"+providerName+"'",source);
				isExists = true;
			}
			dmo.commit(null);
		} catch (Exception e) {
			dmo.rollback(null);
			throw e;
		} finally {
			dmo.releaseContext(null);
		}
		logger.info("注册流模块[name="+name+"]完毕");
		return isExists;
	}
	
	/**
	 * 添加流模块到esper引擎
	 * @param name
	 * @throws Exception
	 */
	public void addStreamModuleToEngine(String providerName,String name) throws Exception{
		if(name == null)
			throw new IllegalArgumentException("name不能为null");
		logger.info("添加流模块[providerName="+providerName+",name="+name+"]至事件流引擎");
		CommDMO dmo = new CommDMO();
		try{
			String sql = "select source from pub_cep_streammodule where name='"+name+"' and providername='"+providerName+"'";
			String source = dmo.readClobDataByDS(null, sql);
			if(source == null)
				throw new Exception("要添加的流模块不存在");
			EPServiceProvider epService = EPServiceProviderManager.getProvider(providerName);
			EPDeploymentAdmin deployAdmin = epService.getEPAdministrator().getDeploymentAdmin();
			Module module = deployAdmin.parse(source);
			String deploymentId = deployAdmin.add(module);
			String key = providerName+"_"+name;
			moduleDeploymentIdMap.put(key, deploymentId);
			updateStreamModuleStatus(name, providerName,CEPConst.STATUS_STREAMMODULE_STOP);
			
			//判断是否已存在该provider的jdbc数据源，没有则创建
			String datasource = JDBC_DATASOURCE_PREFIX + providerName;
			if(!DataSourceManager.hasDatasource(datasource)){
				String url = "jdbc:esper:local/"+providerName;
				logger.debug("初始化流模块JDBC数据源["+datasource+"]");
				HashMap<String, String> dssetup = new HashMap<String, String>();
				dssetup.put("name", datasource);
		        dssetup.put("driver", JDBC_DATASOURCE_DRIVER);
		        dssetup.put("url", url);
		        dssetup.put("initsize", "2");
		        dssetup.put("poolsize", "10");
		        DataSourceManager.initDS(new HashMap[]{dssetup});
			}
		} finally {
			dmo.releaseContext(null);
		}
		logger.info("添加流模块[providerName="+providerName+",name="+name+"]至事件流引擎完毕");
		
	}
	
	/**
	 * 启动流模块
	 * @param name
	 * @throws Exception
	 */
	public void startStreamModule(String providerName,String name) throws Exception{
		if(name == null)
			throw new IllegalArgumentException("name不能为null");
		logger.info("启动流模块[providerName="+providerName+",name="+name+"]");
		
		CommDMO dmo = new CommDMO();
		try{
			String sql = "select providername from pub_cep_streammodule where name=? and providername=?";
			HashVO[] vos = dmo.getHashVoArrayByDS(null, sql, name,providerName);
			if(vos.length == 0 )
				throw new Exception("流模块不存在");
			String key = providerName+"_"+name;
			String deploymentId = moduleDeploymentIdMap.get(key);
			if(deploymentId == null)
				throw new Exception("流模块还未被发布，无法启动");
			EPServiceProvider epService = EPServiceProviderManager.getProvider(providerName);
			EPDeploymentAdmin deployAdmin = epService.getEPAdministrator().getDeploymentAdmin();
			deployAdmin.deploy(deploymentId, null);
			
			
			updateStreamModuleStatus(name, providerName,CEPConst.STATUS_STREAMMODULE_START);
			logger.info("启动流模块[providerName="+providerName+",name="+name+"]完毕");
		}
		finally{
			dmo.releaseContext(null);
		}
		
	}
	
	/**
	 * 更新模块状态
	 * @param name
	 * @param status
	 * @throws Exception
	 */
	public void updateStreamModuleStatus(String name, String providerName, int status) throws Exception{
		//更新数据库状态
		String sql = "update pub_cep_streammodule set status=?,providername=? where name=?";
		CommDMO dmo = new CommDMO();
		try {
			dmo.executeUpdateByDS(null, sql, status, providerName, name);
			dmo.commit(null);
		} catch (Exception e) {
			dmo.rollback(null);
			throw e;
		}
		finally{
			dmo.releaseContext(null);
		}
	}
	
	/**
	 * 停止流模块
	 * @param name
	 * @throws Exception
	 */
	public void stopStreamModule(String providerName,String name) throws Exception {
		if (name == null)
			throw new IllegalArgumentException("name不能为null");
		logger.info("停止流模块[providerName="+providerName+",name=" + name + "]");
		CommDMO dmo = new CommDMO();
		try {
			String sql = "select providername from pub_cep_streammodule where name=? and providername=?";

			HashVO[] vos = dmo.getHashVoArrayByDS(null, sql, name, providerName);
			if (vos.length == 0)
				throw new Exception("流模块不存在");
			String key = providerName + "_" + name;
			String deploymentId = moduleDeploymentIdMap.get(key);
			if (deploymentId == null)
				throw new Exception("流模块还未被发布，无法停止");
			EPServiceProvider epService = EPServiceProviderManager
					.getProvider(providerName);
			EPDeploymentAdmin deployAdmin = epService.getEPAdministrator()
					.getDeploymentAdmin();
			deployAdmin.undeploy(deploymentId);
			updateStreamModuleStatus(name, providerName,
					CEPConst.STATUS_STREAMMODULE_STOP);

			logger.info("停止流模块[providerName=" + providerName + ",name=" + name
					+ "]完毕");
		} finally {
			dmo.releaseContext(null);
		}
	}
	
	/**
	 * 卸载流模块
	 * @param name
	 * @throws Exception
	 */
	public void undeployStreamModule(String providerName,String name) throws Exception{
		if(name == null)
			throw new IllegalArgumentException("name不能为null");
		logger.info("卸载流模块[providerName="+providerName+",name="+name+"]");
		CommDMO dmo = new CommDMO();
		try {
			String sql = "select providername from pub_cep_streammodule where name=? and providername=?";
			HashVO[] vos = dmo.getHashVoArrayByDS(null, sql, name,providerName);
			if(vos.length == 0 )
				throw new Exception("流模块不存在");
			String key = providerName+"_"+name;
			String deploymentId = moduleDeploymentIdMap.get(key);
			if(deploymentId != null){
				EPServiceProvider epService = EPServiceProviderManager.getProvider(providerName);
				EPDeploymentAdmin deployAdmin = epService.getEPAdministrator().getDeploymentAdmin();
				deployAdmin.undeployRemove(deploymentId);
			}
			else
				logger.warn("流模块未被发布，强行删除");
			//从数据库中删除
			sql = "delete from pub_cep_streammodule where name=? and providername=?";
		
			dmo.executeUpdateByDS(null, sql, name,providerName);
			dmo.commit(null);
		} catch (Exception e) {
			dmo.rollback(null);
			throw e;
		}
		finally{
			dmo.releaseContext(null);
		}
		logger.info("卸载流模块[name="+name+"]完毕");
	}
	
	private static String concatParts(String parts[], int indexPart)
    {
        if(parts.length == indexPart + 1)
            return parts[indexPart];
        StringBuilder builder = new StringBuilder(parts[indexPart]);
        for(int i = indexPart + 1; i < parts.length; i++)
        {
            builder.append(":");
            builder.append(parts[i]);
        }

        return builder.toString();
    }
	
	public void addStatementListener(String providerName,String statementName,UpdateListener listener) throws Exception {
		
		logger.info("添加监听器[providerName"+providerName+",statementName="+statementName+"]");
		
		EPServiceProvider epService = EPServiceProviderManager.getProvider(providerName);
		EPStatement stmt = epService.getEPAdministrator().getStatement(statementName);
		
		if(stmt != null){
			stmt.addListener(listener);
		}else{
			throw new Exception("未找到要添加监听器的语句!");
		}
		logger.info("添加监听器[providerName"+providerName+",statementName="+statementName+"]完毕!");
	}
	
	public void removeStatementListener(String providerName,String statementName) throws Exception{
		
		logger.info("删除监听器[providerName"+providerName+",statementName="+statementName+"]");
		
		EPServiceProvider epService = EPServiceProviderManager.getProvider(providerName);
		EPStatement stmt = epService.getEPAdministrator().getStatement(statementName);
		
		if(stmt != null){
			
			Iterator<UpdateListener> listeners =  stmt.getUpdateListeners();
			
			while(listeners.hasNext()){
				stmt.removeListener(listeners.next());
			}
			
		}else{
			throw new Exception("未找到要删除监听器的EPStatement!");
		}
		logger.info("删除监听器[providerName"+providerName+",statementName="+statementName+"]完毕!");
		
	}

	
	public static void main(String[] args) {
		String url = "jdbc:esper:local/test";
		String[] parts = url.split(":"); 
		System.out.println(concatParts(parts, 2));
	}
}
