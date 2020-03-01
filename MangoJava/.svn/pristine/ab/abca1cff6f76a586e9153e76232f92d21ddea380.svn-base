/**
 * 
 */
package smartx.bam.bs.entitymodel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import smartx.bam.vo.DatabaseConst;
import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.bs.dbaccess.dsmgr.DataSourceManager;
import smartx.framework.common.utils.StringUtil;
import smartx.framework.common.utils.Sys;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaLogger;

/**
 * @author caohenghui
 * Jan 3, 2012
 */
public class MondrianSchemaService {
	
	protected Logger logger = NovaLogger.getLogger(this.getClass());
	
	private String DATASOURCE_FILE_FULLNAME = (String) Sys.getInfo("NOVA2_SYS_ROOTPATH")+"WEB-INF/datasources.xml";
	private String SCHEMA_DIR_FULLNAME = (String) Sys.getInfo("NOVA2_SYS_ROOTPATH")+"WEB-INF/schemas";
	
	private final String ENTITY_DIMENSION = "EntityDimension";//实体维度
	private final String TIME_DIMENSION = "TimeDimension";//时间维度
	private final String INNER_DIMENSION = "InnerDimension";//内部维度
	private final String SYNONYMS_DIMENSION = "SynonymsDimension";//同义词维度
	
	private Map<String,Element> modelMap =  new HashMap<String,Element>();
	private Map<String,Element> entitiesMap = new HashMap<String,Element>();
	private Map<String,Element> cubeMap = new HashMap<String,Element>();
	private Map<String,Element> dimensionMap = new HashMap<String,Element>();
	
	private Map<String,Element> catalogMap = new HashMap<String,Element>();
	private Map<String,Element> schemaCubeMap = new HashMap<String,Element>();
	
	public void dealMondrianConfig(String modelCode,String entityCode,String cubeCode) throws Exception {
		String modelContent = getEntityModelContent(modelCode);
		if(!StringUtil.isEmpty(modelContent)){
			if(this.initEntitiesInfo(modelContent,modelCode)){
				
				String dw_datasource = getDWDatasource(modelContent);
				
				String dsUrl = DataSourceManager.getDataSourceUrl(dw_datasource);
				
				Element cubeEle = cubeMap.get(entityCode+"_"+cubeCode);
				
				Element schemaCubeEle = this.getCubeContent(cubeEle,cubeCode);
				if(schemaCubeEle != null){
					if(this.dealSchemaFile(modelCode, cubeCode, schemaCubeEle)){
						this.dealDatasources(modelCode, dsUrl);
					}
				}
				
			}else{
				logger.debug("无法初始化模型元数据!");
			}
			
		}
	}
	
	private String getDWDatasource(String modelContent){
		String datasource = "";
		try {
			Document doc = DocumentHelper.parseText(modelContent);
			Element root = doc.getRootElement();
			datasource = root.elementText("dwdatasource");
		} catch (DocumentException e) {
			logger.debug("",e);
		}
		return datasource;
	}
	
	private void writeXMLToFile(Document doc,String fullFileName) throws Exception {
		
		try{
			
			File file = new File(fullFileName);
			
			StringWriter writer = new StringWriter();
	        OutputFormat format = OutputFormat.createPrettyPrint();
	        XMLWriter xmlwriter = new XMLWriter(writer,format);
	        xmlwriter.write(doc);
			
	        OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
	        BufferedWriter bw = new BufferedWriter(write);
	        bw.write(writer.toString());
	        bw.close();
			
		}catch(Exception e){
			logger.debug("", e);
			throw e;
		}
		
	}
	
	private Element getCubeContent(Element cubeEle,String cubeCode){
		Element tempCubeEle = null;
		try{
			
			if(cubeEle != null ){
				
				tempCubeEle = cubeEle.createCopy();
				
				if(tempCubeEle.attribute("code") != null)
				tempCubeEle.remove(tempCubeEle.attribute("code"));
				
				if(tempCubeEle.attribute("filter") != null)
				tempCubeEle.remove(tempCubeEle.attribute("filter"));
				
				if(tempCubeEle.attribute("taskCode") != null)
				tempCubeEle.remove(tempCubeEle.attribute("taskCode"));
				
				tempCubeEle.addAttribute("name", cubeCode);
				tempCubeEle.clearContent();
				
				List itemList = cubeEle.elements();
				
				Set<Element> tableSet = new HashSet<Element>();
				Set<Element> dimensionSet = new HashSet<Element>();
				Set<Element> measureSet = new HashSet<Element>();
				
				if(itemList != null){
					for(Object item : itemList){
						Element tempEle = (Element)item;
						if(tempEle.getName().equalsIgnoreCase("Dimension")){
							dimensionSet.add(tempEle);
						}else if(tempEle.getName().equalsIgnoreCase("Measure")){
							measureSet.add(tempEle);
						}else if(tempEle.getName().equalsIgnoreCase("Table")){
							tableSet.add(tempEle);
						}
					}
				}
				
				//先加事实表
				for(Element tempEle : tableSet){
					tempCubeEle.add(tempEle.createCopy());
					break;
				}
				
				//再加维度
				for(Element tempEle : dimensionSet){
						String dimensionCode = tempEle.attributeValue("code");
						String dimensionType = tempEle.attributeValue("type");
						String foreignKey = tempEle.attributeValue("foreignKey");
						if(!StringUtil.isEmpty(dimensionType) && dimensionType.equalsIgnoreCase(INNER_DIMENSION)){
							
							String name = tempEle.attributeValue("name");
							
							Element ed = tempCubeEle.addElement("Dimension");
							ed.addAttribute("name", name);
							
							Element eh = ed.addElement("Hierarchy");
							eh.addAttribute("hasAll", "true");
							eh.addAttribute("allMemberName", "内部维度");
							
							Element el = eh.addElement("Level");
							el.addAttribute("name", "级别");
							el.addAttribute("type", "String");
							el.addAttribute("uniqueMembers", "false");
							el.addAttribute("column", foreignKey);
							el.addAttribute("nameColumn", foreignKey);
							
						}else{
							foreignKey = foreignKey+"_ID";
							Element dimensionEle = dimensionMap.get(dimensionCode);
							Element tempDimensionEle = dimensionEle.createCopy();
							
							if(!StringUtil.isEmpty(dimensionType) && dimensionType.equalsIgnoreCase(ENTITY_DIMENSION)){
								tempDimensionEle.remove(tempDimensionEle.attribute("entityCode"));
								tempDimensionEle.remove(tempDimensionEle.attribute("type"));
							}else if(!StringUtil.isEmpty(dimensionType) && dimensionType.equalsIgnoreCase(SYNONYMS_DIMENSION)){
								tempDimensionEle.remove(tempDimensionEle.attribute("synonymsCode"));
								tempDimensionEle.remove(tempDimensionEle.attribute("type"));
							}else if(!StringUtil.isEmpty(dimensionType) && dimensionType.equalsIgnoreCase(TIME_DIMENSION)){
								Element temp = tempDimensionEle.element("Hierarchy");
								temp.remove(temp.attribute("type"));
								tempDimensionEle.remove(tempDimensionEle.attribute("startyear"));
							}
							
							tempDimensionEle.remove(tempDimensionEle.attribute("code"));
							
							Attribute ab = tempDimensionEle.attribute("taskCode");
							if(ab != null ){
								tempDimensionEle.remove(ab);
							}
							
							tempDimensionEle.addAttribute("foreignKey", foreignKey);
							tempCubeEle.add(tempDimensionEle);
							
						}
					}
				
				//再加指标
				for(Element item : measureSet){
					Element tempEle = item.createCopy();
					String aggregator = tempEle.attributeValue("aggregator");
					String column = tempEle.attributeValue("column");
					if(!StringUtil.isEmpty(aggregator) && aggregator.equalsIgnoreCase("count")){
						tempEle.addAttribute("aggregator", "sum");
					}
					
					column = aggregator.toUpperCase()+"_"+column.toUpperCase();
					tempEle.addAttribute("column", column);
					tempCubeEle.add(tempEle);
				}
				
			}
			
		}catch(Exception e){
			logger.debug("",e);
		}
		
		return tempCubeEle;
	}
	
	private boolean dealSchemaFile(String modelCode,String cubeCode,Element schemaCubeEle){
		boolean flag = true;
		try{
			
			StringBuffer content = new StringBuffer();
				
			File file = new File(SCHEMA_DIR_FULLNAME+"/"+modelCode+".xml");
			
			if(!file.exists()){
				file.createNewFile();
				content.append("<Schema name=\""+modelCode+"\" />");
			}else{
				
//				FileReader fr = new FileReader(file);
				
				InputStreamReader read = new InputStreamReader(new FileInputStream(file),"UTF-8");
				
				BufferedReader br = new BufferedReader(read);
				
				String line = null;
				
				while( (line = br.readLine()) != null ){
//					String tempLine = new String(line.getBytes("UTF-8"));
					content.append(line);
				}
				read.close();
				br.close();
			}
			

			
			Document doc = DocumentHelper.parseText(content.toString());
			
			Element root = doc.getRootElement();
			root.addAttribute("name", modelCode);
			
			List cubeList = root.elements();
			if(cubeList != null ){
				this.schemaCubeMap.clear();
				for(Object obj : cubeList){
					Element tempCubeEle = (Element)obj;
					String name = tempCubeEle.attributeValue("name");
					schemaCubeMap.put(name, tempCubeEle);
				}
			}
			
			Element cubeEle = schemaCubeMap.get(cubeCode);
			if(cubeEle != null){
				root.remove(cubeEle);
				
			}
			root.add(schemaCubeEle);

			writeXMLToFile(doc,SCHEMA_DIR_FULLNAME+"/"+modelCode+".xml");
			
		}catch(Exception e){
			logger.debug("",e);
			flag = false;
		}
		return flag;
	}
	
	private void dealDatasources(String modelCode,String datasourceUrl){
		
		String Provider = "Provider=mondrian";
		String JdbcDrivers = "oracle.jdbc.OracleDriver";
		String SchemaPath = "/WEB-INF/schemas/"+modelCode+".xml";
		String version = getVersion();//取一个随即版本号,这样schema.xml就可以动态加载
		try{
			
			StringBuffer content = new StringBuffer();
				
			File file = new File(DATASOURCE_FILE_FULLNAME);
			
			if(!file.exists()){
				throw new Exception("没有找到文件:"+DATASOURCE_FILE_FULLNAME);
			}
			
//			FileReader fr = new FileReader(file);
			
			InputStreamReader read = new InputStreamReader(new FileInputStream(file),"UTF-8");
			
			BufferedReader br = new BufferedReader(read);
			
			String line = null;
			
			while( (line = br.readLine()) != null ){
				content.append(line);
			}
			read.close();
			br.close();
			
			Document doc = DocumentHelper.parseText(content.toString());
			
			Element root = doc.getRootElement();
			
			Element catalogs = root.element("DataSource").element("Catalogs");
			if(catalogs != null ){
				List catalogList = catalogs.elements();
				if(catalogList != null ){
					this.catalogMap.clear();
					for(Object obj : catalogList){
						Element tempCatalogEle = (Element)obj;
						String name = tempCatalogEle.attributeValue("name");
						catalogMap.put(name, tempCatalogEle);
					}
				}
			}
			
			Element catalogEle = catalogMap.get(modelCode);
			if(catalogEle == null){
				
				catalogEle = catalogs.addElement("Catalog");
				catalogEle.addAttribute("name", modelCode);
				
				Element dsEle = catalogEle.addElement("DataSourceInfo");
				dsEle.setText(Provider+";Jdbc="+datasourceUrl+";JdbcDrivers="+JdbcDrivers+";version="+version);
				
				Element dfEle = catalogEle.addElement("Definition");
				dfEle.setText(SchemaPath);
				
				catalogMap.put(modelCode, catalogEle);
				
			}else{
				
				Element dsEle = catalogEle.element("DataSourceInfo");
				dsEle.setText(Provider+";Jdbc="+datasourceUrl+";JdbcDrivers="+JdbcDrivers+";version="+version);
				
				Element dfEle = catalogEle.element("Definition");
				dfEle.setText(SchemaPath);
			}
			
			writeXMLToFile(doc,DATASOURCE_FILE_FULLNAME);
			
		}catch(Exception e){
			logger.debug("",e);
		}
	}
	
	private String getVersion(){
		String version ="";
		try{
			Calendar cd = Calendar.getInstance();
			
			String year = cd.get(Calendar.YEAR)+"";
			
			int monthInt = cd.get(Calendar.MONTH)+1;
			String month = monthInt+"";
			if(monthInt<10){
				month = "0"+monthInt;
			}
			
			int dayInt = cd.get(Calendar.DAY_OF_MONTH);
			String day = dayInt+"";
			if(dayInt<10){
				day = "0"+dayInt;
			}
			
			int hourInt = cd.get(Calendar.HOUR_OF_DAY);
			String hour = hourInt+"";
			if(hourInt<10){
				hour = "0"+hourInt;
			}
			
			int minuteInt = cd.get(Calendar.MINUTE);
			String minute = minuteInt+"";
			if(minuteInt<10){
				minute = "0"+minuteInt;
			}
			
			int secondInt = cd.get(Calendar.SECOND);
			String second = secondInt+"";
			if(secondInt<10){
				second = "0"+secondInt;
			}
			
			version = year+month+day+hour+minute+second;
			
		}catch(Exception e){
			logger.debug("",e);
		}
		return version;
	}
	
	public String getEntityModelContent(String modelCode){
		CommDMO dmo = new CommDMO();
		String content = "";
		try{
			String sql  = "select mt.content from bam_entitymodel em,pub_metadata_templet mt where em.mtcode=mt.code and mt.type=? and em.code=?";
			HashVO[] vos = dmo.getHashVoArrayByDS(DatabaseConst.DATASOURCE_DEFAULT, sql, 103,modelCode);
			if(vos != null && vos.length >0){
				HashVO vo = vos[0];
				content = vo.getStringValue("content");
			}
		}catch(Exception e){
			logger.debug("",e);
		}finally{
			dmo.releaseContext(DatabaseConst.DATASOURCE_DEFAULT);
		}
		return content;
	}
	
	public boolean initEntitiesInfo(String modelContent,String entityModelCode) throws Exception{
		boolean flag = false;
		try{
			if(!StringUtil.isEmpty(modelContent)){
				modelMap.clear();
				entitiesMap.clear();
				cubeMap.clear();
				dimensionMap.clear();
				
				
				Document doc = DocumentHelper.parseText(modelContent);
				Element root = doc.getRootElement();
				modelMap.put(entityModelCode, root);
				Element entities = root.element("entities");
				List entitiesList = entities.elements();
				if(entitiesList != null ){
					for(Object obj : entitiesList){
						Element tempEntity = (Element)obj;
						String entityCode = tempEntity.attributeValue("code");
						entitiesMap.put(entityCode, tempEntity);
						Element cubesNode = tempEntity.element("cubes");
						if(cubesNode != null ){
							List cubeList = cubesNode.elements("Cube");
							if(cubeList != null){
								for(Object item : cubeList){
									Element cube = (Element) item;
									String  cubeCode = cube.attributeValue("code");
									cubeMap.put(entityCode+"_"+cubeCode, cube);
								}
							}
						}
					}
				}
				Element dimensions = root.element("dimensions");
				List dimensionsList = dimensions.elements();
				if(dimensionsList != null){
					for(Object obj : dimensionsList){
						Element tempDimension = (Element)obj;
						String dimensionCode = tempDimension.attributeValue("code");
						dimensionMap.put(dimensionCode, tempDimension);
					}
				}
				flag = true;
			}
		}catch(Exception e){
			logger.debug("",e);
			flag = false;
			throw new Exception("实体无法初始化:"+e.getMessage());
		}
		
		return flag;
	}

}
