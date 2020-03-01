/**
 * 
 */
package smartx.bam.bs.entitymodel;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import smartx.bam.vo.DatabaseConst;
import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.utils.StringUtil;
import smartx.framework.common.vo.DMOConst;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaLogger;
import smartx.framework.metadata.vo.TableDataStruct;

/**
 * @author caohenghui
 * Dec 21, 2011
 */
public class EntityCubeService {
	
	protected Logger logger = NovaLogger.getLogger(this.getClass());
	
	private final String ENTITY_DIMENSION = "EntityDimension";//实体维度
	private final String TIME_DIMENSION = "TimeDimension";//时间维度
	private final String INNER_DIMENSION = "InnerDimension";//内部维度
	private final String SYNONYMS_DIMENSION = "SynonymsDimension";//同义词维度
	
	private final String DATE_DIMENSION_TYPE_YEAR = "year";
	private final String DATE_DIMENSION_TYPE_MONTH = "yearmonth";
	private final String DATE_DIMENSION_TYPE_DAY = "yearmonthday";
	private final String DATE_DIMENSION_TYPE_HOUR = "yearmonthdayhour";
	
	private final String CREATE_TABLE_NAME_SEQUENCE_SQL = "create sequence S_DW_CUBE_FACT_TABLE minvalue 1 start with 1 increment by 1 nomaxvalue nocycle cache 20";
	
	private Map<String,Element> modelMap =  new HashMap<String,Element>();
	private Map<String,Element> entitiesMap = new HashMap<String,Element>();
	private Map<String,Element> cubeMap = new HashMap<String,Element>();
	private Map<String,Element> dimensionMap = new HashMap<String,Element>();
	
	private Map<String,String> entityDimensionTableMap = new HashMap<String,String>();
	
	private EntityModelManager emm = new EntityModelManager();
	
	private MondrianSchemaService mss = new MondrianSchemaService();
	
	public void dealCube(String modelCode,String entityCode,String cubeCode) throws Exception {
		String modelContent = getEntityModelContent(modelCode);
		if(!StringUtil.isEmpty(modelContent)){
			if(this.initEntitiesInfo(modelContent,modelCode)){
				
				String dqc_datasource = getDatasource(modelContent);
				String dw_datasource = getDWDatasource(modelContent);
				
				Element cubeEle = cubeMap.get(entityCode+"_"+cubeCode);
				
				String querySQL = this.getCubeSQL(cubeEle, modelCode);
				
				Element tableEle = cubeEle.element("Table");
				if(tableEle == null ){
					cubeEle.addElement("Table");
				}
				
				String factTable = this.createEntityCubeFactTable(cubeEle.asXML(), dw_datasource);
				if(!StringUtil.isEmpty(factTable)){
					cubeEle.element("Table").addAttribute("name", factTable.toUpperCase());
					updateMtContent("MT_EM_"+modelCode,this.modelMap.get(modelCode).asXML());
					this.insertRecordToFactTable(querySQL, factTable, cubeEle, dqc_datasource, dw_datasource);
					
					//处理mondrian配置信息
					mss.dealMondrianConfig(modelCode, entityCode, cubeCode);
					
				}else{
					logger.debug("无法创建事实表,抽取结束!");
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
	
	private String getDatasource(String modelContent){
		String datasource = "";
		try {
			Document doc = DocumentHelper.parseText(modelContent);
			Element root = doc.getRootElement();
			datasource = root.elementText("datasource");
		} catch (DocumentException e) {
			logger.debug("",e);
		}
		return datasource;
	}
	
	private String getCubeSQL(Element cubeEle,String modelCode){
		String sql ="";
		try{
			if(cubeEle != null ){
				
				entityDimensionTableMap.clear();
				
				String tables ="";
				String conditions = "";
				
				String normalColumns = "";
				String dateColumns ="";
				String aggregateColums ="";
				String entityColumns = "";
				
				String entityGroupColumns = "";
				
				String dateGroupColumns = "";
				
				String ownerEntityCode = cubeEle.getParent().getParent().attributeValue("code");
				
//				String cubeCode = cubeEle.attributeValue("code");
				
				String filter = cubeEle.attributeValue("filter");
				
				String aliasEntityTable = "t1";
				
				String entityTable = "("+this.getOriginalSQL(modelCode, ownerEntityCode)+") "+aliasEntityTable;
				
				tables = entityTable;
				conditions = "1=1";
				
				List list = cubeEle.elements(); //指标集里所有的维度,这里的维度只是一个引用,俱体的维度内容要从hashmap里拿
				if(list != null){
					int aliasNameIndex = 2;
					for(Object obj : list){
						Element ele = (Element)obj;
						String name = ele.getName();
						if(name.equalsIgnoreCase("Dimension")){
							String dimensionType = ele.attributeValue("type");
							String dimensionCode = ele.attributeValue("code");
							String foreignKey = ele.attributeValue("foreignKey");
							if(!StringUtil.isEmpty(dimensionType)&&dimensionType.equalsIgnoreCase(this.TIME_DIMENSION)){
								Element dateDimensionEle = this.dimensionMap.get(dimensionCode);
								if(dateDimensionEle != null ){
									
									String yearChar = "to_char("+aliasEntityTable+"."+foreignKey+",'YYYY') "+foreignKey+"_yearname ";
									String monthChar = "to_char("+aliasEntityTable+"."+foreignKey+",'MM') "+foreignKey+"_monthname ";
									String dayChar = "to_char("+aliasEntityTable+"."+foreignKey+",'DD') "+foreignKey+"_dayname ";
									String hourChar = "to_char("+aliasEntityTable+"."+foreignKey+",'hh24') "+foreignKey+"_hourname ";
									
									String yearGroupChar = "to_char("+aliasEntityTable+"."+foreignKey+",'YYYY') ";
									String monthGroupChar = "to_char("+aliasEntityTable+"."+foreignKey+",'MM') ";
									String dayGroupChar = "to_char("+aliasEntityTable+"."+foreignKey+",'DD') ";
									String hourGroupChar = "to_char("+aliasEntityTable+"."+foreignKey+",'hh24') ";
									
									String dateDimensionType = dateDimensionEle.element("Hierarchy").attributeValue("type");
									if(!StringUtil.isEmpty(dateDimensionType)&&dateDimensionType.equalsIgnoreCase(this.DATE_DIMENSION_TYPE_YEAR)){
										if(StringUtil.isEmpty(dateColumns)){
											dateColumns = yearChar;
											dateGroupColumns = yearGroupChar;
										}else{
											dateColumns = dateColumns+","+yearChar;
											dateGroupColumns = dateGroupColumns+","+yearGroupChar;
										}
									}else if(!StringUtil.isEmpty(dateDimensionType)&&dateDimensionType.equalsIgnoreCase(this.DATE_DIMENSION_TYPE_MONTH)){
										if(StringUtil.isEmpty(dateColumns)){
											dateColumns = yearChar+","+monthChar;
											dateGroupColumns = yearGroupChar+","+monthGroupChar;
										}else{
											dateColumns = dateColumns+","+yearChar+","+monthChar;
											dateGroupColumns = dateGroupColumns+","+yearGroupChar+","+monthGroupChar;
										}
									}else if(!StringUtil.isEmpty(dateDimensionType)&&dateDimensionType.equalsIgnoreCase(this.DATE_DIMENSION_TYPE_DAY)){
										if(StringUtil.isEmpty(dateColumns)){
											dateColumns = yearChar+","+monthChar+","+dayChar;
											dateGroupColumns = yearGroupChar+","+monthGroupChar+","+dayGroupChar;
										}else{
											dateColumns = dateColumns+","+yearChar+","+monthChar+","+dayChar;
											dateGroupColumns = dateGroupColumns+","+yearGroupChar+","+monthGroupChar+","+dayGroupChar;
										}
									}else if(!StringUtil.isEmpty(dateDimensionType)&&dateDimensionType.equalsIgnoreCase(this.DATE_DIMENSION_TYPE_HOUR)){
										if(StringUtil.isEmpty(dateColumns)){
											dateColumns = yearChar+","+monthChar+","+dayChar+","+hourChar;
											dateGroupColumns = yearGroupChar+","+monthGroupChar+","+dayGroupChar+","+hourGroupChar;
										}else{
											dateGroupColumns = dateGroupColumns+","+yearGroupChar+","+monthGroupChar+","+dayGroupChar+","+hourGroupChar;
											dateColumns = dateColumns+","+yearChar+","+monthChar+","+dayChar+","+hourChar;
										}
									}
								}
							}else if(!StringUtil.isEmpty(dimensionType)&&dimensionType.equalsIgnoreCase(this.ENTITY_DIMENSION)){
								
								Element entityDimensionEle = this.dimensionMap.get(dimensionCode);
								
								String entityCode = entityDimensionEle.attributeValue("entityCode");
								Element entityDimension = this.entitiesMap.get(entityCode);
								String primaryKey = entityDimension.attributeValue("idAttributeName");
								
								String entityAliasTableName = "t"+aliasNameIndex;
								
								String entityTableName = "("+this.getOriginalSQL(modelCode, entityCode)+") "+entityAliasTableName;
								
								entityDimensionTableMap.put(dimensionCode, entityAliasTableName);
								
								tables = tables+","+entityTableName;
								conditions = conditions+" AND "+entityAliasTableName+"."+primaryKey+"="+aliasEntityTable+"."+foreignKey;
								
								List levelList = entityDimensionEle.element("Hierarchy").elements();
								if(levelList != null ){
									for(Object item : levelList){
										Element levelEle = (Element)item;
										String tempColumName = levelEle.attributeValue("column");
										if(levelEle.getName().equalsIgnoreCase("Level")){
											if(StringUtil.isEmpty(entityColumns)){
												entityColumns = entityAliasTableName+"."+tempColumName+" "+entityAliasTableName+"_"+tempColumName;
												entityGroupColumns = entityAliasTableName+"."+tempColumName;
											}else{
												entityColumns = entityColumns+","+entityAliasTableName+"."+tempColumName+" "+entityAliasTableName+"_"+tempColumName;
												entityGroupColumns = entityGroupColumns+","+entityAliasTableName+"."+tempColumName;
											}
										}
									}
								}
								aliasNameIndex++;
								
							}else{
								if(StringUtil.isEmpty(normalColumns)){
									normalColumns = aliasEntityTable+"."+foreignKey;
								}else{
									normalColumns = normalColumns+","+aliasEntityTable+"."+foreignKey;
								}
							}
						}else if(name.equalsIgnoreCase("Measure")){
							String columnName = ele.attributeValue("column");
							String aggregator = ele.attributeValue("aggregator");
							String tempColumn = aggregator+"("+aliasEntityTable+"."+columnName+") as "+aggregator+"_"+columnName;
							if(StringUtil.isEmpty(aggregateColums)){
								aggregateColums = tempColumn;
							}else{
								aggregateColums = aggregateColums+","+tempColumn;
							}
							
						}
					}
					
					if(!StringUtil.isEmpty(filter)){
						conditions = "("+conditions+") AND ("+filter+")";
					}
					
					String columns ="";
					String groupColumns = "";
					
					columns = normalColumns;
					groupColumns = normalColumns;
					
					if(StringUtil.isEmpty(columns)){
						columns = entityColumns;
						groupColumns = entityGroupColumns;
					}else{
						if(!StringUtil.isEmpty(entityColumns)){
							columns = columns+","+entityColumns;
							groupColumns = groupColumns+","+entityGroupColumns;
						}
					}
					
					if(StringUtil.isEmpty(columns)){
						columns = dateColumns;
						if(StringUtil.isEmpty(groupColumns)){
							groupColumns = dateGroupColumns;
						}else{
							groupColumns = groupColumns+","+dateGroupColumns;
						}
					}else{
						if(!StringUtil.isEmpty(dateColumns)){
							columns = columns+","+dateColumns;
							groupColumns = groupColumns+","+dateGroupColumns;
						}
					}
					
					if(StringUtil.isEmpty(columns)){
						columns = aggregateColums;
					}else{
						if(!StringUtil.isEmpty(aggregateColums)){
							columns = columns+","+aggregateColums;
						}
					}
					
					sql = "select "+columns+" from "+tables+ " where "+conditions+" group by "+groupColumns;
					
				}
			}
		}catch(Exception e){
			logger.debug("",e);
		}
		return sql;
	}
	
	private void insertRecordToFactTable(String querySQL,String tableName,Element cubeEle,String dqcDatasource,String dwDatasource){
		CommDMO dmo = new CommDMO();
		try{
			
			int totalCount = 0;
			int pageSize = 1000;
			
			HashVO[] totalCountVO = dmo.getHashVoArrayByDS(dqcDatasource, "select count(1) cou from ("+querySQL+")");
			if(totalCountVO != null && totalCountVO.length>0){
				totalCount = totalCountVO[0].getIntegerValue("cou");
			}
			
			int totalPage = (totalCount+pageSize-1)/pageSize;
			
			if(totalCount >0){
				
				for(int pageNum = 1; pageNum <= totalPage;pageNum++){
					
					Map<String, Object> hashvoMap= dmo.getHashVoArrayByPage(dqcDatasource, querySQL, pageNum, pageSize);
					HashVO[] vos = (HashVO[])hashvoMap.get(DMOConst.HASHVOARRAY);
					if(vos != null && vos.length >0){
						for(HashVO vo : vos){
							try{
							
								String id = dmo.getSequenceNextValByDS(dwDatasource, "s_"+tableName);
								
								//为防止出现相同的字段
								Map<String,Object> columnValue= new HashMap<String,Object>();
								columnValue.put("ID", id);
								
								List list = cubeEle.elements();
								if(list != null ){
									for(Object item : list){
										Element ele = (Element)item;
										if(ele.getName().equalsIgnoreCase("Dimension")){
											String code = ele.attributeValue("code");
											String dimensionType = ele.attributeValue("type");
											String foreignKey = ele.attributeValue("foreignKey");
											
											Element dimensionEle = this.dimensionMap.get(code);
											
											if(dimensionType.equalsIgnoreCase(this.INNER_DIMENSION)){
												String value = vo.getStringValue(foreignKey);
												columnValue.put(foreignKey, value);
											}else{
												
												String idValue = getIdFromDimensionTable(dimensionEle, vo,foreignKey,dimensionType,code,dwDatasource);
												columnValue.put(foreignKey+"_ID", idValue);
											}
										}else if(ele.getName().equalsIgnoreCase("Measure")){
											String column = ele.attributeValue("column");
											String aggregator = ele.attributeValue("aggregator");
											String columnName = aggregator+"_"+column;
											String value = vo.getStringValue(columnName);
											columnValue.put(columnName, value);
										}
									}
								}
								
								Set<String> keys = columnValue.keySet();
								String columns = "";
								String values = "";
								Object[] paras = new Object[keys.size()];
								int index = 0;
								for(String key : keys ){
									if(StringUtil.isEmpty(columns)){
										columns = key;
										values = "?";
									}else{
										columns = columns+","+key;
										values = values+",?";
									}
									paras[index] = columnValue.get(key);
									index++;
								}
								
								String insertSQL = "insert into "+tableName+" ("+columns+") values("+values+")";
								dmo.executeUpdateByDS(dwDatasource, insertSQL, paras);
								dmo.commit(dwDatasource);
								
							}catch(Exception e){
								logger.debug("", e);
							}finally{
								dmo.releaseContext(dwDatasource);
							}
						}
					}
				}
			}
			
		}catch(Exception e){
			logger.debug("", e);
		}finally{
			dmo.releaseContext(dwDatasource);
		}
	}
	
	private String getColumnLength(String type){
		String temp = "VARCHAR2(255)";
		if(type.toUpperCase().trim().equals("VARCHAR2")){
			temp = "VARCHAR2(255)";
		}else if(type.toUpperCase().trim().equals("NUMBER")){
			temp = "NUMBER(18)";
		}else if(type.toUpperCase().trim().equals("DATE")){
			temp = "DATE";
		}else if(type.toUpperCase().trim().equals("CLOB")){
			temp = "CLOB";
		}
		return temp;
	}
	
	private String getIdFromDimensionTable(Element dimensionEle,HashVO vo,String foreignKey,String dimensionType,String dimensionCode,String dwDatasource){
		
		CommDMO dmo = new CommDMO();
		String id = null;
		try{

			String tableName = dimensionEle.element("Hierarchy").element("Table").attributeValue("name");
			
			if(StringUtil.isEmpty(tableName)){
				return null;
			}
			
			String sql = "";
			Object[] paras = null;
			
			if(!StringUtil.isEmpty(dimensionType)&&dimensionType.equalsIgnoreCase(this.TIME_DIMENSION)){
				
				if(dimensionEle != null ){
					
					String dateDimensionType = dimensionEle.element("Hierarchy").attributeValue("type");
					if(!StringUtil.isEmpty(dateDimensionType)&&dateDimensionType.equalsIgnoreCase(this.DATE_DIMENSION_TYPE_YEAR)){
						String yearName = vo.getStringValue(foreignKey+"_yearname");
						paras = new Object[]{yearName};
						sql = "select id from "+tableName+" where yearname=?";
					}else if(!StringUtil.isEmpty(dateDimensionType)&&dateDimensionType.equalsIgnoreCase(this.DATE_DIMENSION_TYPE_MONTH)){
						String yearName = vo.getStringValue(foreignKey+"_yearname");
						String monthName = vo.getStringValue(foreignKey+"_monthname");
						paras = new Object[]{yearName,monthName};
						sql = "select id from "+tableName+" where yearname=? and monthname=?";
					}else if(!StringUtil.isEmpty(dateDimensionType)&&dateDimensionType.equalsIgnoreCase(this.DATE_DIMENSION_TYPE_DAY)){
						String yearName = vo.getStringValue(foreignKey+"_yearname");
						String monthName = vo.getStringValue(foreignKey+"_monthname");
						String dayName = vo.getStringValue(foreignKey+"_dayname");
						paras = new Object[]{yearName,monthName,dayName};
						sql = "select id from "+tableName+" where yearname=? and monthname=? and dayname=?";
					}else if(!StringUtil.isEmpty(dateDimensionType)&&dateDimensionType.equalsIgnoreCase(this.DATE_DIMENSION_TYPE_HOUR)){
						String yearName = vo.getStringValue(foreignKey+"_yearname");
						String monthName = vo.getStringValue(foreignKey+"_monthname");
						String dayName = vo.getStringValue(foreignKey+"_dayname");
						String hourName = vo.getStringValue(foreignKey+"_hourname");
						
						paras = new Object[]{yearName,monthName,dayName,hourName};
						sql = "select id from "+tableName+" where yearname=? and monthname=? and dayname=?,hourName=?";
					}
				}
			}else if(!StringUtil.isEmpty(dimensionType)&&dimensionType.equalsIgnoreCase(this.ENTITY_DIMENSION)){
				
				String aliasTableName = entityDimensionTableMap.get(dimensionCode);
				
				List levelList = dimensionEle.element("Hierarchy").elements();
				
				String conditions = "";
				paras = new Object[(levelList.size()-1)];
				
				if(levelList != null ){
					int index = 0;
					for(Object item : levelList){
						Element levelEle = (Element)item;
						String tempColumName = levelEle.attributeValue("column");
						if(levelEle.getName().equalsIgnoreCase("Level")){
							if(StringUtil.isEmpty(conditions)){
								conditions = tempColumName+"=?";
								paras[index] = vo.getStringValue(aliasTableName+"_"+tempColumName);
							}else{
								conditions = conditions+" AND "+tempColumName+"=?";
								paras[index] = vo.getStringValue(aliasTableName+"_"+tempColumName);
							}
							index++;
						}
					}
				}
				
				if(!StringUtil.isEmpty(conditions)){
					sql = "select id from "+tableName+" where "+conditions;
				}
				
			}else if(!StringUtil.isEmpty(dimensionType)&&dimensionType.equalsIgnoreCase(this.SYNONYMS_DIMENSION)){
				String value = vo.getStringValue(foreignKey);
				paras = new Object[]{value};
				sql = "select id from "+tableName+" where value=?";
				
			}
			
			if(!StringUtil.isEmpty(sql)){
				HashVO[] idVos = dmo.getHashVoArrayByDS(dwDatasource, sql, paras);
				if(idVos != null && idVos.length>0){
					id = idVos[0].getStringValue("id");
				}
			}
			
		}catch(Exception e){
			logger.debug("",e);
		}finally{
			dmo.releaseContext(dwDatasource);
		}
		return id;
	}
	
	private boolean isExistTable(String tableName,String datasource){
		boolean flag = false;
		CommDMO dmo = new CommDMO();
		try{
			String sql = "select count(table_name) cou from user_tables where table_name=?";
			HashVO[] vos = dmo.getHashVoArrayByDS(datasource,sql,tableName);
			if(vos != null && vos.length>0){
				int count = vos[0].getIntegerValue("cou");
				if(count >0 ){
					flag = true;
				}else{
					flag = false;
				}
			}
		}catch(Exception e){
			flag = false;
		}finally{
			dmo.releaseContext(datasource);
		}
		return flag;
	}
	
	private String[] getKeysFromTable(String tableName,String datasource){
		CommDMO dmo = new CommDMO();
		String[] keys = null;
		try{
			TableDataStruct tds = dmo.getTableDataStructByDS(datasource, "select * from "+tableName +" where 1=2");
			if(tds != null){
				keys = tds.getTable_header();
			}
		}catch(Exception e){
			logger.debug("",e);
		}
		return keys;
	}
	
	private Object getColumnValue(String key,HashVO vo){
		int type = vo.getColumnType(key);
        Object value = null;
        if (type == Types.VARCHAR || type == Types.CLOB) {
        	value = vo.getStringValue(key);
        } else if (type == Types.NUMERIC || type == Types.SMALLINT) {
        	value = vo.getIntegerValue(key);
        } else if (type == Types.DATE || type == Types.TIMESTAMP) {
        	value = vo.getTimeStampValue(key);
        } else if (type == Types.DECIMAL || type == Types.DOUBLE || type == Types.FLOAT) {
        	value = vo.getDoubleValue(key);
        } else {
            value = vo.getObjectValue(key);
        }
		return value;
	}
	
	public String getSQL(String modelCode, String entityCode) throws Exception{
		return emm.generateEntitySql(this.getEntityToString(entityCode), getEntityModelToString(modelCode), null);
	}
	
	public String getOriginalSQL(String modelCode, String entityCode) throws Exception{
		return emm.generateEntitySqlOriginalData(this.getEntityToString(entityCode), getEntityModelToString(modelCode), null);
	}
	
	private String getEntityModelToString(String modelCode){
		String content = "";
		if(modelMap != null && !StringUtil.isEmpty(modelCode)){
			content = modelMap.get(modelCode).asXML();
		}
		return content;
	}
	
	private String getEntityToString(String entityCode){
		String content = "";
		if(entitiesMap != null && !StringUtil.isEmpty(entityCode)){
			content = entitiesMap.get(entityCode).asXML();
		}
		return content;
	}
	
	public String createEntityCubeFactTable(String cubeXml,String dwdatasource){
		String tableName = "";
		CommDMO dmo = new CommDMO();
		try{
			
			List<String> sqls = new ArrayList<String>();
			
			if(!this.isExistSequence("S_DW_CUBE_FACT_TABLE", dwdatasource)){
				List<String> sequenceList = new ArrayList<String>();
				sequenceList.add(this.CREATE_TABLE_NAME_SEQUENCE_SQL);
				this.executeDDL(dwdatasource, sequenceList);
			}
			
			String seq = dmo.getSequenceNextValByDS(dwdatasource, "S_DW_CUBE_FACT_TABLE");
			tableName = "DW_CUBE_FACT_TALBE_"+seq;
			
			String buildTableSql = "create table "+tableName+" ";
			
			//为防止在创建表的时候不出现相同的列
			Map<String,String> columnsMap = new HashMap<String,String>();
			columnsMap.put("ID", "NUMBER");
			
			Document doc = DocumentHelper.parseText(cubeXml);
			Element root = doc.getRootElement();
			List list = root.elements();
			if(list != null ){
				for(int i = 0; i<list.size(); i++){
					Element element = (Element)list.get(i);
					if(element.getName().equalsIgnoreCase("Dimension")){
						String columnName = element.attributeValue("foreignKey");
						String columnType = "VARCHAR2";
						String dimensionType = element.attributeValue("type");
						if(!dimensionType.equalsIgnoreCase(this.INNER_DIMENSION)){
							columnName = columnName+"_ID";
						}
						columnsMap.put(columnName.toUpperCase(), columnType.toUpperCase());
					}else if(element.getName().equalsIgnoreCase("Measure")){
						String columnName = element.attributeValue("column");
						String aggregator = element.attributeValue("aggregator");
						String columnType = "VARCHAR2";
						String column = aggregator+"_"+columnName;
						columnsMap.put(column.toUpperCase(), columnType.toUpperCase());
					}
				}
			}
			
			String columns = "";
			Set<String> keys = columnsMap.keySet();
			for(String key : keys){
				if(StringUtil.isEmpty(columns)){
					columns = "("+key+" "+getColumnLength(columnsMap.get(key));
				}else{
					columns = columns+","+key+" "+getColumnLength(columnsMap.get(key));
				}
			}
			
			buildTableSql =  buildTableSql+columns+")";
			
			sqls.add(buildTableSql);
			
			String addPrimaryKeySQL = "alter table "+tableName+" add primary key (ID) using index";
			sqls.add(addPrimaryKeySQL);
			
			String sequenceSQL ="create sequence S_"+tableName+" minvalue 1 start with 1 increment by 1 nomaxvalue nocycle cache 20";
			sqls.add(sequenceSQL);
			
			dmo.executeBatchByDS(dwdatasource,sqls);
			dmo.commit(dwdatasource);
			
		}catch(Exception e){
			logger.debug("创建事实表失败!",e);
			try {
				dmo.rollback(dwdatasource);
				tableName = "";
			} catch (Exception e1) {
				logger.debug("", e1);
			}
		}finally{
			dmo.releaseContext(dwdatasource);
		}
		return tableName;
	
	}
	
	private boolean isExistSequence(String sequenceName,String datasource){
		boolean flag = false;
		CommDMO dmo = new CommDMO();
		try{
			if(StringUtil.isEmpty(sequenceName)){
				return false;
			}
			String sql = "select count(sequence_name) cou from user_sequences where sequence_name=?";
			HashVO[] vos = dmo.getHashVoArrayByDS(datasource,sql,sequenceName.toUpperCase());
			if(vos != null && vos.length>0){
				int count = vos[0].getIntegerValue("cou");
				if(count >0 ){
					flag = true;
				}else{
					flag = false;
				}
			}
		}catch(Exception e){
			flag = false;
		}finally{
			dmo.releaseContext(datasource);
		}
		return flag;
	}
	
	private boolean executeDDL(String datasource,List<String> sqls){
		CommDMO dmo = new CommDMO();
		boolean flag = true;
		try{
			dmo.executeBatchByDS(datasource, sqls);
			dmo.commit(datasource);
		}catch(Exception e){
			logger.debug("",e);
			try {
				dmo.rollback(datasource);
			} catch (Exception e1) {
				logger.debug("",e1);
			}
			flag = false;
		}finally{
			dmo.releaseContext(datasource);
		}
		return flag;
	}
	
	private String getEntityTable(String entityCode){
		String table = "";
		if(entitiesMap != null){
			Element entity = entitiesMap.get(entityCode);
			if(entity != null){
				Element mappingInfo = entity.element("mappingInfo");
				String type = mappingInfo.attributeValue("type");
				if(!StringUtil.isEmpty(type)&&type.trim().equals("table")){
					table = mappingInfo.elementText("tableName");
				}else{
					String[] info = mappingInfo.elementText("queryView").split("@@");
					if(info != null && info.length ==2 ){
						table = info[1];
					}
				}
			}
		}
		return table;
	}
	
	private void updateMtContent(String mtCode,String mtContent){
		CommDMO dmo = new CommDMO();
		try{
			dmo.executeUpdateClobByDS(DatabaseConst.DATASOURCE_DEFAULT, "CONTENT", "pub_metadata_templet", " type=103 and code='"+mtCode+"'", mtContent);
			dmo.commit(DatabaseConst.DATASOURCE_DEFAULT);
		}catch(Exception e){
			logger.debug("",e);
		}finally{
			dmo.releaseContext(DatabaseConst.DATASOURCE_DEFAULT);
		}
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
