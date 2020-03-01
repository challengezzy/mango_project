/**
 * 
 */
package smartx.bam.initdsfactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import smartx.bam.vo.DatabaseConst;
import smartx.bam.vo.SysConst;
import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.bs.dbaccess.dsmgr.DataSourceManager;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaLogger;
import smartx.publics.cep.bs.configuration.CEPEngineConfigurationFactory;
import smartx.publics.cep.vo.CEPConst;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.ConfigurationDBRef;
import com.espertech.esper.client.ConfigurationDBRef.MetadataOriginEnum;

/**
 * @author caohenghui
 * 
 */
public class DataSourceInitFactory implements CEPEngineConfigurationFactory {

	protected Logger logger = NovaLogger.getLogger(this.getClass());

	/*
	 * (non-Javadoc)
	 * 
	 * @see smartx.publics.cep.bs.configuration.CEPEngineConfigurationFactory#
	 * getConfigurationByProviderName(java.lang.String)
	 */
	@Override
	public Configuration getConfigurationByProviderName(String providerName) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see smartx.publics.cep.bs.configuration.CEPEngineConfigurationFactory#
	 * getDefaultConfiguration()
	 */
	@Override
	public Configuration getDefaultConfiguration() {

		Configuration config = null;

		try {

			config = new Configuration();
			config.addImport("smartx.bam.utils.epl.BamEplUtil");
			logger.debug("初始化ESPER数据源开始!");
			this.addDataSource(config);
			logger.debug("初始化ESPER数据源结束!");

		} catch (Exception e) {
			logger.debug("获取ESPER容器配置时出错!", e);
		}

		return config;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see smartx.publics.cep.bs.configuration.CEPEngineConfigurationFactory#
	 * getProviderNamesNeedToInit()
	 */
	@Override
	public List<String> getProviderNamesNeedToInit() {
		ArrayList<String> result = new ArrayList<String>();
		result.add(CEPConst.DEFAULTPROVIDERNAME_CEP);
		return result;
	}

	private void addDataSource(Configuration config) {

		CommDMO dmo = new CommDMO();

		try {
			String searchSQL = "select t.id,t.name,t.type,t.configuration from bam_relationaldatasource t where t.status=?";

			HashVO[] vos = dmo.getHashVoArrayByDS(
					DatabaseConst.DATASOURCE_DEFAULT, searchSQL,SysConst.STARTED);

			if (vos != null) {

				for (HashVO vo : vos) {

					int type = vo.getIntegerValue("TYPE");
					String dataSourceName = vo.getStringValue("NAME");
					String propXml = vo.getStringValue("CONFIGURATION");

					ConfigurationDBRef confDBRef = this.getConfigurationDBRef(
							dataSourceName, type, propXml);

					if (confDBRef != null) {
						config.addDatabaseReference(dataSourceName, confDBRef);
						logger.debug("ESPER数据源:[" + dataSourceName + "]初始化成功!");
					} else {
						logger.debug("ESPER数据源:[" + dataSourceName + "]无法初始化!");
					}
				}
			}

		} catch (Exception e) {

			logger.debug("执行SQL语句出错!", e);

		} finally {
			dmo.releaseContext(DatabaseConst.DATASOURCE_DEFAULT);
		}
	}

	@SuppressWarnings("unchecked")
	private ConfigurationDBRef getConfigurationDBRef(String name, int type,
			String propXml) {

		Properties conn = new Properties();

		ConfigurationDBRef DBRef = null;
		try {

			Document doc = DocumentHelper.parseText(propXml);
			Element root = doc.getRootElement();

			Element element = root.element("database-connection");

			List connNodes = element.elements();

			for (int i = 0; i < connNodes.size(); i++) {
				Element ele = (Element) connNodes.get(i);
				if (ele != null) {
					conn.put(ele.attributeValue("name"),
							ele.attributeValue("value"));
				}
			}

			Element eleProp = root.element("database-properties");
			List propsNodes = eleProp.elements();

			if (type == SysConst.BAM_RELATIONALDATASOURCE_TYPE_DATASOURCE) {

				String lookupName = element
						.attributeValue("context-look-name");

				if (!isEmpty(lookupName)) {
					DBRef = new ConfigurationDBRef();
					DBRef.setDataSourceConnection(lookupName, conn);
					this.addProperties(propsNodes, DBRef);
					
					String datasource = SysConst.RELATION_DS_PREFIX + name;
					logger.debug("初始化关系型数据源[" + datasource + "]");
					HashMap<String, String> dssetup = new HashMap<String, String>();
					dssetup.put("name", datasource);
					dssetup.put("type", "JNDI");
					dssetup.put("lookupName", lookupName);
					dssetup.put("url", lookupName);
					DataSourceManager.initDS(new HashMap[] { dssetup });
				}

			} else if (type == SysConst.BAM_RELATIONALDATASOURCE_TYPE_DATASOURCEFACTORY) {

				String className = element.attributeValue("class-name");
				String driverName = conn.getProperty("driverClassName");
				String url = conn.getProperty("url");

				if (!isEmpty(className)) {
					conn.put("removeAbandoned", true);
					conn.put("minIdle", 3);
					conn.put("testWhileIdle", true);
					conn.put("timeBetweenEvictionRunsMillis", 30000);
					conn.put("validateQuery", "select 1 from dual");
					DBRef = new ConfigurationDBRef();
					DBRef.setDataSourceFactory(conn, className);
					DBRef.setMetadataOrigin(MetadataOriginEnum.SAMPLE);
					this.addProperties(propsNodes, DBRef);
					
//					addDataSource(name,driverName,url);
					String datasource = SysConst.RELATION_DS_PREFIX + name;
					logger.debug("初始化关系型数据源[" + datasource + "]");
					HashMap<String, String> dssetup = new HashMap<String, String>();
					dssetup.put("name", datasource);
					dssetup.put("type", "DBCP");
					dssetup.put("driver", driverName);
					dssetup.put("url", url);
					dssetup.put("initsize", "2");
					dssetup.put("poolsize", "10");
					DataSourceManager.initDS(new HashMap[] { dssetup });
				}

			} else if (type == SysConst.BAM_RELATIONALDATASOURCE_TYPE_DRIVERMANAGER) {

				String driverName = element.attributeValue("driver-name");

				String url = conn.getProperty("url");
				String username = conn.getProperty("username");
				String password = conn.getProperty("password");

				if (!isEmpty(driverName) && !isEmpty(url) && !isEmpty(username)
						&& !isEmpty(password)) {

					DBRef = new ConfigurationDBRef();
					DBRef.setDriverManagerConnection(driverName, url, username,
							password);
					this.addProperties(propsNodes, DBRef);
					
//					addDataSource(name,driverName,url);
					
					String datasource = SysConst.RELATION_DS_PREFIX + name;
					logger.debug("初始化关系型数据源[" + datasource + "]");
					HashMap<String, String> dssetup = new HashMap<String, String>();
					dssetup.put("name", datasource);
					dssetup.put("type", "DBCP");
					dssetup.put("driver", driverName);
					dssetup.put("url", url);
					dssetup.put("initsize", "2");
					dssetup.put("poolsize", "10");
					DataSourceManager.initDS(new HashMap[] { dssetup });
				}

			}

		} catch (Exception e) {
			logger.debug("获取ESPER数据源连接出错!", e);
		}

		return DBRef;
	}
	
	/**
	 * 添加数据源
	 * @param name 
	 * @param driverName
	 * @param url
	 */
	private void addDataSource(String name,String driverName,String url){
		// 将数据源添加到连接池中
		String datasource = SysConst.RELATION_DS_PREFIX + name;
		logger.debug("初始化关系型数据源[" + datasource + "]");
		HashMap<String, String> dssetup = new HashMap<String, String>();
		dssetup.put("name", datasource);
		dssetup.put("driver", driverName);
		dssetup.put("url", url);
		dssetup.put("initsize", "2");
		dssetup.put("poolsize", "10");
		DataSourceManager.initDS(new HashMap[] { dssetup });
	}

	@SuppressWarnings("unchecked")
	private void addProperties(List propsNodes, ConfigurationDBRef DBRef) {

		try {

			if (propsNodes != null && propsNodes.size() > 0) {

				for (int i = 0; i < propsNodes.size(); i++) {
					Element ele = (Element) propsNodes.get(i);
					if (ele != null) {
						String name = ele.attributeValue("name");
						String value = ele.attributeValue("value");
						if (!isEmpty(name)
								&& name.trim().equalsIgnoreCase("lifecycle")) {
							if (!isEmpty(value)
									&& value.trim().equalsIgnoreCase("pooled")) {
								DBRef.setConnectionLifecycleEnum(ConfigurationDBRef.ConnectionLifecycleEnum.POOLED);
							} else {
								DBRef.setConnectionLifecycleEnum(ConfigurationDBRef.ConnectionLifecycleEnum.RETAIN);
							}
						} else if (!isEmpty(name)
								&& name.trim().equalsIgnoreCase("readonly")) {
							if ((!isEmpty(value) && value.trim()
									.equalsIgnoreCase("true"))
									|| (!isEmpty(value) && value.trim()
											.equalsIgnoreCase("false"))) {
								DBRef.setConnectionReadOnly(new Boolean(value));
							}
						} else if (!isEmpty(name)
								&& name.trim().equalsIgnoreCase("autocommit")) {
							if ((!isEmpty(value) && value.trim()
									.equalsIgnoreCase("true"))
									|| (!isEmpty(value) && value.trim()
											.equalsIgnoreCase("false"))) {
								DBRef.setConnectionAutoCommit(new Boolean(value));
							}
						} else if (!isEmpty(name)
								&& name.trim().equalsIgnoreCase(
										"transaction-isolation")) {
							if (isNumber(value)) {
								DBRef.setConnectionTransactionIsolation(new Integer(
										value));
							}
						} else if (!isEmpty(name)
								&& name.trim().equalsIgnoreCase("catalog")) {
							if (!isEmpty(value)) {
								DBRef.setConnectionCatalog(value);
							}
						} else if (!isEmpty(name)
								&& name.trim().equalsIgnoreCase("lru-cache")) {
							if (this.isNumber(value)) {
								DBRef.setLRUCache(new Integer(value));
							}
						} else if (!isEmpty(name)
								&& name.trim().equalsIgnoreCase(
										"expiry-time-cache")) {
							String maxAgeSec = ele
									.attributeValue("max-age-seconds");
							String purgeIntervalSec = ele
									.attributeValue("purge-interval-seconds");
							if (isNumber(maxAgeSec)
									&& isNumber(purgeIntervalSec)) {
								DBRef.setExpiryTimeCache(
										Double.parseDouble(maxAgeSec),
										Double.parseDouble(purgeIntervalSec));
							}
						}
					}
				}
			}

		} catch (Exception e) {
			logger.debug("添加ESPER数据源属性出错!", e);
		}

	}

	private boolean isEmpty(String str) {

		boolean flag = false;
		try {
			if (str == null || str.trim().equals("")) {
				flag = true;
			}
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	private boolean isNumber(String str) {

		boolean flag = false;
		try {
			if (str != null && !str.trim().equals("")) {
				new Integer(str);
				flag = true;
			}
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

}
