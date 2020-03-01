package smartx.bam.initdsfactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import smartx.bam.vo.SysConst;
import smartx.framework.common.bs.dbaccess.dsmgr.DataSourceManager;
import smartx.framework.common.vo.NovaLogger;
import smartx.framework.metadata.vo.ComBoxItemVO;
import smartx.framework.metadata.vo.Pub_Templet_1VO;
import smartx.publics.form.bs.service.FormInterceptor;

public class DataSourceInsertAfInterceptor implements FormInterceptor {

	protected Logger logger = NovaLogger.getLogger(this.getClass());
	
	@Override
	public void doSomething(Pub_Templet_1VO templetVO,
			Map<String, Object> dataValue) throws Exception {
		try{
			
			String modifyType = (String)dataValue.get("FORMSERVICE_MODIFYFLAG");
			
			if(modifyType.equalsIgnoreCase("insert")){
			
				String type = ((ComBoxItemVO)dataValue.get("TYPE")).getId();
				int status = Integer.parseInt((String)((ComBoxItemVO)dataValue.get("STATUS")).getId());
				String name = (String)dataValue.get("NAME");
				String propXml = (String)dataValue.get("CONFIGURATION");
				
				if(status == SysConst.RELATION_DATASOURCE_STATUS_VALID){
				
					Properties conn = new Properties();
					
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
		
					if (type.equalsIgnoreCase(SysConst.BAM_RELATIONALDATASOURCE_TYPE_DATASOURCEFACTORY+"")) {
						
						String driverName = conn.getProperty("driverClassName");
						String url = conn.getProperty("url");
						
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
		
					} else if (type.equalsIgnoreCase(SysConst.BAM_RELATIONALDATASOURCE_TYPE_DRIVERMANAGER+"")) {
		
						String driverName = element.attributeValue("driver-name");
		
						String url = conn.getProperty("url");
						
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
						
					} else if(type.equalsIgnoreCase(SysConst.BAM_RELATIONALDATASOURCE_TYPE_DATASOURCE+"")){
						
						String lookupName = element.attributeValue("context-look-name");
						String datasource = SysConst.RELATION_DS_PREFIX + name;
						logger.debug("初始化关系型数据源[" + datasource + "]");
						HashMap<String, String> dssetup = new HashMap<String, String>();
						dssetup.put("name", datasource);
						dssetup.put("type", "JNDI");
						dssetup.put("lookupName", lookupName);
						dssetup.put("url", lookupName);
						DataSourceManager.initDS(new HashMap[] { dssetup });
					}
				}
			}
		}catch(Exception e){
			logger.debug("",e);
		}
	}

	@Override
	public void doSomething(Pub_Templet_1VO templetVO,
			List<Map<String, Object>> dataValueList) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSomething(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSomething(List<Map<String, Object>> dataValueList)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
