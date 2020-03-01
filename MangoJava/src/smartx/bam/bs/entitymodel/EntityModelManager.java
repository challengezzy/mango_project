/**
 * 
 */
package smartx.bam.bs.entitymodel;

import static smartx.bam.bs.entitymodel.util.EntitySqlUtil.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.vo.HashVO;

/**
 * @author sky
 * 领域实体管理类
 */
public class EntityModelManager {
	private Logger logger = Logger.getLogger(this.getClass());

	private List<String> aliasEntities = new ArrayList<String>();//所有实体的别名集合
	
	private Element contentXmlRoot;
	
	private Element entityXmlRoot;
	
	private boolean isVirtualEntity = false;//是否为虚拟实体
	
	public boolean isExistEntityModel(String code) throws Exception {
		logger.debug("是否存在领域实体模型[" + code + "]");
		CommDMO dmo = new CommDMO();
		String sql = "select b.id from bam_entitymodel b where b.code =?";
		try {
			HashVO[] vos = dmo.getHashVoArrayByDS(null, sql, code);
			if (vos.length > 0)
				return true;
		} catch (Exception e) {
			throw e;
		} finally {
			dmo.releaseContext(null);
		}
		return false;
	}
	
	/**
	 * 生成显示属性的SQL并包含查询条件
	 * @param entity
	 * @param content
	 * @param condition
	 * @param rule
	 * @return
	 * @throws Exception
	 */
	public String generateDisplayEntitySqlContainCondition(String entity, String content, String rule ,String condition) 
			throws Exception{
		String sql = null;
		try{
			prepareGenerateEntitySql(entity,content);
			
			sql = getDisplaySql(rule,condition,true);
			
			logger.debug("实体SQL(包含显示属性查询条件)..." + sql);
		}catch(Exception e){
			logger.error(e);
			throw e;
		}
		
		
		return sql;
	}

	/**
	 * 生成实体SQL(包含显示属性)
	 * 
	 * @param entity 实体
	 * @param content 元数据XML
	 * @return
	 * @throws Exception
	 */
	public String generateEntitySql(String entity, String content,String rule)
			throws Exception {
		String sql = "";
		try{
			prepareGenerateEntitySql(entity,content);
			
			if(TYPE_VIRTUAL.equals(entityXmlRoot.attributeValue("type"))){
				isVirtualEntity = true;
				sql = generateVirtualSql(rule,true);
			}else{
				isVirtualEntity = false;
				sql = getDisplaySql(rule,"",true);
				logger.debug("实体SQL..." + sql);
			}
		}catch(Exception e){
			logger.error(e);
			throw e;
		}
		return sql;
	}
	
	/**
	 * 生成实体SQL(包含显示属性且不需要维度字段)
	 * 
	 * @param entity 实体
	 * @param content 元数据XML
	 * @return
	 * @throws Exception
	 */
	public String generateEntitySqlNotNeedDimension(String entity, String content,String rule)
			throws Exception {
		String sql = "";
		try{
			prepareGenerateEntitySql(entity,content);
			
			if(TYPE_VIRTUAL.equals(entityXmlRoot.attributeValue("type"))){
				isVirtualEntity = true;
				sql = generateVirtualSql(rule,false);
			}else{
				isVirtualEntity = false;
				sql = getDisplaySql(rule,"",false);
				logger.debug("实体SQL..." + sql);
			}
		}catch(Exception e){
			logger.error(e);
			throw e;
		}
		return sql;
	}
	
	/**
	 * 生成实体SQL(原始数据)
	 * 
	 * @param entity 实体
	 * @param content 元数据XML
	 * @return
	 * @throws Exception
	 */
	public String generateEntitySqlOriginalData(String entity, String content,String rule)
			throws Exception {
		String sql = null;
		try{
			prepareGenerateEntitySql(entity,content);
			
			sql = getSqlContainRule(rule,false,entityXmlRoot);
			
			logger.debug("实体SQL(原始数据)..." + sql);
		}catch(Exception e){
			logger.error(e);
			throw e;
		}
		return sql;
	}
	
	/**通过MTCODE解析SQL(包含显示属性)
	 * @param entityMtCode 实体MTCODE
	 * @param modelMtCode 模型MTCODE
	 * @param ruleMtCode 规则MTCODE
	 * @param datasource 数据源
	 * @return 返回SQL语句
	 * @throws Exception
	 */
	public String generateEntitySqlByMtCode(String entityMtCode,String modelMtCode,String ruleMtCode,String datasource) throws Exception{
		String sql = "";
		try{
			Map<String,String> contentMap = initMtContent(entityMtCode,modelMtCode,ruleMtCode, datasource);
			if(contentMap != null){
				sql = this.generateEntitySql(contentMap.get(CONTENT_ENTITY), contentMap.get(CONTENT_MODEL), contentMap.get(CONTENT_RULE));
			}
		}catch(Exception e){
			logger.debug("",e);
			throw e;
		}
		
		return sql;
	}
	
	/**
	 * 通过MTCODE解析SQL(包含显示属性并不包含维度字段)
	 * @param entityMtCode
	 * @param modelMtCode
	 * @param ruleMtCode
	 * @param datasource
	 * @return
	 * @throws Exception
	 */
	public String generateEntitySqlNotNeedDimensionByMtCode(String entityMtCode,String modelMtCode
																,String ruleMtCode,String datasource) throws Exception{
		String sql = "";
		try{
			Map<String,String> contentMap = initMtContent(entityMtCode,modelMtCode,ruleMtCode, datasource);
			if(contentMap != null){
				sql = this.generateEntitySqlNotNeedDimension(contentMap.get(CONTENT_ENTITY), contentMap.get(CONTENT_MODEL), contentMap.get(CONTENT_RULE));
			}
		}catch(Exception e){
			logger.debug("",e);
			throw e;
		}
		
		return sql;
	}
	
	/**通过MTCODE解析SQL（原始数据）
	 * @param entityMtCode 实体MTCODE
	 * @param modelMtCode 模型MTCODE
	 * @param ruleMtCode 规则MTCODE
	 * @param datasource 数据源
	 * @return 返回SQL语句
	 * @throws Exception
	 */
	public String generateEntitySqlOriginalDataByMtCode(String entityMtCode
			,String modelMtCode,String ruleMtCode,String datasource) throws Exception{
		String sql = "";
		try{
			Map<String,String> contentMap = initMtContent(entityMtCode,modelMtCode,ruleMtCode, datasource);
			if(contentMap != null)
				sql = this.generateEntitySqlOriginalData(contentMap.get(CONTENT_ENTITY), contentMap.get(CONTENT_MODEL), contentMap.get(CONTENT_RULE));
			
		}catch(Exception e){
			logger.debug("",e);
		}
		
		return sql;
	}
	
	/**
	 * 通过MTCODE解析SQL（带有查询条件的显示SQL）
	 * @param entityMtCode
	 * @param modelMtCode
	 * @param ruleMtCode
	 * @param condition
	 * @param datasource
	 * @return
	 * @throws Exception
	 */
	public String generateDisplayEntitySqlContainConditionByMtCode(String entityMtCode
			,String modelMtCode,String ruleMtCode,String condition,String datasource) throws Exception{
		String sql = "";
		try{
			Map<String,String> contentMap = initMtContent(entityMtCode,modelMtCode,ruleMtCode, datasource);
			if(contentMap != null){
				sql = this.generateDisplayEntitySqlContainCondition(contentMap.get(CONTENT_ENTITY)
									, contentMap.get(CONTENT_MODEL), contentMap.get(CONTENT_RULE),condition);
			}
		}catch(Exception e){
			logger.debug("",e);
		}
		
		return sql;
	}
	
	/**
	 * 通过MTCODE生成实体SQL查询总数
	 * @param entityMtCode
	 * @param modelMtCode
	 * @param ruleMtCode
	 * @param condition
	 * @param datasource
	 * @return
	 * @throws Exception
	 */
	public int generateOriginalEntitySqlCountByMtCode(String entityMtCode
			,String modelMtCode,String ruleMtCode,String condition,String datasource) throws Exception{
		int rowsCount = 0;//查询总数
		CommDMO dmo = new CommDMO();
		StringBuilder sql = new StringBuilder();
		HashVO[] vos = null;
		try{
			Map<String,String> contentMap = initMtContent(entityMtCode,modelMtCode,ruleMtCode, datasource);
			String entityStr = contentMap.get(CONTENT_ENTITY);
			Element entity = DocumentHelper.parseText(entityStr).getRootElement();
			Element mappingInfo =  entity.element("mappingInfo");
			String filter = mappingInfo.elementText("filter");
			if(contentMap != null){
				if(MAPPING_TYPE_TABLE.equals(mappingInfo.attributeValue("type"))){
					sql.append("select count(1) from ").append(mappingInfo.elementText("tableName"));
					if(filter != null && !"".equals(filter.trim()))
						sql.append(" where ").append(filter);
				}else if(MAPPING_TYPE_QUERYVIEW.equals(mappingInfo.attributeValue("type"))){
					String viewCode = mappingInfo.attributeValue("queryView").split("@@")[0];
					String queryViewSql = "select sql from bam_queryview where code = '"
							+ viewCode + "'";
					try{
						vos = dmo.getHashVoArrayByDS(null, queryViewSql);
						if (vos.length > 0){
							sql.append("select count(1) from (").append(vos[0].getStringValue("sql")).append(")");
							if(filter != null && !"".equals(filter.trim()))
								sql.append(" where ").append(filter);
						}else 
							throw new Exception("未找到查询视图[" + viewCode + "]对应的sql语句");
					}catch(Exception e){
						throw e;
					}finally{
						dmo.releaseContext(null);
					}
				}
				vos = dmo.getHashVoArrayByDS(datasource, sql.toString());
				if (vos != null && vos.length > 0) 
					rowsCount = vos[0].getIntegerValue(0);
			}
		}catch(Exception e){
			logger.debug("",e);
			throw e;
		}finally{
			dmo.releaseContext(datasource);
		}
		
		return rowsCount;
	}
	
	/**
	 * 生成实体SQL准备
	 * @param entity
	 * @param content
	 */
	private void prepareGenerateEntitySql(String entity,String content) throws Exception{
//		logger.debug("生成实体SQL[" + entity + "]["+content+"]");
		aliasEntities.clear();
		Document contentXml = DocumentHelper.parseText(content);
		Document entityXml = DocumentHelper.parseText(entity);
		contentXmlRoot = contentXml.getRootElement();
		entityXmlRoot = entityXml.getRootElement();
	}
	
	/**
	 * 生成虚拟实体SQL
	 * @param rule 过滤规则
	 * @param isNeedDimension 是否需要维度字段
	 * @return
	 */
	private String generateVirtualSql(String rule,boolean isNeedDimension) throws Exception{
		StringBuilder sqlBuf = new StringBuilder();
		StringBuilder groupBySqlBuf = new StringBuilder();
		String entityCode = entityXmlRoot.attributeValue("code");
		sqlBuf.append("select ");
		for(Object attributeObj : entityXmlRoot.element("attributes").elements("attribute")){
			Element att = (Element)attributeObj;
			if("true".equals(att.attributeValue("isGroupby")))
				groupBySqlBuf.append(att.attributeValue("expression")).append(",");
			if(ATTRIBUTE_ENTITY.equals(att.attributeValue("category"))){//实体引用
				String refEntityCode = att.attributeValue("refEntity");
				Element refEntity = null;
				for(Object entityObj : contentXmlRoot.element("entities").elements("entity")){
					if(refEntityCode.equals(((Element)entityObj).attributeValue("code"))){
						refEntity = (Element)entityObj;
						break;
					}
				}
				List<Element> refEntitiesList = getAllInheritEntity(refEntity,contentXmlRoot);
				sqlBuf.append("(select ").append(att.attributeValue("refEntityShowAtt")).append(" from (").append(getFetchSql(refEntitiesList))
				.append(") where ").append(att.attributeValue("refEntityAtt")).append("=").append(entityCode).append(".").append(att.attributeValue("name"))
				.append(") ").append(att.attributeValue("name")).append(",");
			}else{
				sqlBuf.append(att.attributeValue("expression")).append(" ").append(att.attributeValue("name")).append(",");
				if(isNeedDimension && ATTRIBUTE_TYPE_DATE.equals(att.attributeValue("type"))){//如果是日期类型,添加年月日三个维度字段
					sqlBuf.append("decode(").append(att.attributeValue("expression"))
					.append(",'','',").append("to_char(").append(att.attributeValue("expression"))
					.append(",'YYYY')||'年')").append(" ").append(att.attributeValue("name")).append("_YEAR,")
					.append("decode(").append(att.attributeValue("expression"))
					.append(",'','',").append("to_char(").append(att.attributeValue("expression"))
					.append(",'MM')||'月')").append(" ").append(att.attributeValue("name")).append("_MONTH,")
					.append("decode(").append(att.attributeValue("expression"))
					.append(",'','',").append("to_char(").append(att.attributeValue("expression"))
					.append(",'DD')||'日')").append(" ").append(att.attributeValue("name")).append("_DAY,");
				}
			}
		}
		sqlBuf = new StringBuilder(sqlBuf.substring(0, sqlBuf.length()-1)).append(" from ");
		for(Object joinEntityObj : entityXmlRoot.element("joinEntities").elements("joinEntity")){
			Element joinEntity = (Element)joinEntityObj;
			String joinEntityCode = joinEntity.attributeValue("code");
			String joinEntityAlias = joinEntity.attributeValue("alias");
			String entitySql = "";
			for(Object entity : contentXmlRoot.element("entities").elements("entity")){
				Element entityEle = (Element)entity;
				if(joinEntityCode.equals(entityEle.attributeValue("code"))){
					List<Element> joinEntitiesList = getAllInheritEntity(entityEle,contentXmlRoot);
					entitySql = getFetchSql(joinEntitiesList);
					break;
				}
			}
			sqlBuf.append("(").append(entitySql).append(") ").append(joinEntityAlias).append(",");
		}
		sqlBuf = new StringBuilder(sqlBuf.substring(0, sqlBuf.length()-1)).append(" where 1=1 ");
		String joinExpression = entityXmlRoot.elementText("joinExpression");
		if(joinExpression != null && !"".equals(joinExpression))
			sqlBuf.append("and ").append(joinExpression).append(" ");
		if(rule != null && !"".equals(rule))
			sqlBuf.append(getCondition(rule, "", false,entityXmlRoot));
		if(!"".equals(groupBySqlBuf.toString()))
			sqlBuf.append(" group by ").append(groupBySqlBuf.substring(0, groupBySqlBuf.length()-1));
		String orderby = entityXmlRoot.elementText("orderby");
		if(orderby != null && !"".equals(orderby))
			sqlBuf.append(" order by ").append(orderby);
		logger.debug("虚拟实体SQL..." + sqlBuf.toString());
		return sqlBuf.toString();
	}
	
	/**
	 * 获得显示属性SQL
	 * @param rule
	 * @param displayCondition
	 * @param isNeedDimension 是否需要维度字段
	 * @return
	 * @throws Exception
	 */
	private String getDisplaySql(String rule,String displayCondition,boolean isNeedDimension) throws Exception{
		List<Element> entitiesList = getAllInheritEntity(entityXmlRoot,contentXmlRoot);
		String entityCode = entityXmlRoot.attributeValue("code");
		StringBuilder sqlBuf = new StringBuilder();
		sqlBuf.append("select ");
		
		List<Element> allAttributes = getAllAttributes(entitiesList);
		Element mappingInfo = entityXmlRoot.element("mappingInfo");
		Map<String,String> tempMap = new HashMap<String, String>();
		for (Object mapObj :mappingInfo.element("attributeMapping").elements("map")) {
			Element map = (Element) mapObj;
			tempMap.put(map.attributeValue("attributeName"), map.attributeValue("columnName"));
		}
		for(Element att : allAttributes){
			if(ATTRIBUTE_ENTITY.equals(att.attributeValue("category"))){//实体引用
				String refEntityCode = att.attributeValue("refEntity");
				Element refEntity = null;
				for(Object entityObj : contentXmlRoot.element("entities").elements("entity")){
					if(refEntityCode.equals(((Element)entityObj).attributeValue("code"))){
						refEntity = (Element)entityObj;
						break;
					}
				}
				List<Element> refEntitiesList = getAllInheritEntity(refEntity,contentXmlRoot);
				/**
				 * 如果实体引用中显示属性不为空，则添加显示名称属性
				 */
				if(att.attributeValue(ATTRIBUTE_ENTITY_REF_DISPLAYATT) != null
						&& !"".equals(att.attributeValue(ATTRIBUTE_ENTITY_REF_DISPLAYATT))){
					sqlBuf.append(entityCode).append(".").append(att.attributeValue("name"))
					.append(" ").append(att.attributeValue("name")).append(",").append("(select ")
					.append(att.attributeValue("refEntityShowAtt")).append(" from (").append(getFetchSql(refEntitiesList))
					.append(") where ").append(att.attributeValue("refEntityAtt")).append("=").append(entityCode).append(".").append(att.attributeValue("name"))
					.append(") ").append(att.attributeValue(ATTRIBUTE_ENTITY_REF_DISPLAYATT)).append(",");
				}else{
					sqlBuf.append("(select ").append(att.attributeValue("refEntityShowAtt")).append(" from (").append(getFetchSql(refEntitiesList))
					.append(") where ").append(att.attributeValue("refEntityAtt")).append("=").append(entityCode).append(".").append(att.attributeValue("name"))
					.append(") ").append(att.attributeValue("name")).append(",");
				}
			}else{
				String attName = att.attributeValue("name");
				sqlBuf.append(entityCode).append(".").append(attName)
				.append(" ").append(att.attributeValue("name")).append(",");
				if(isNeedDimension && ATTRIBUTE_TYPE_DATE.equals(att.attributeValue("type"))){//如果是日期类型,添加年月日三个维度字段
					sqlBuf.append("decode(").append(entityCode).append(".").append(attName)
					.append(",'','',").append("to_char(").append(entityCode).append(".").append(attName)
					.append(",'YYYY')||'年')").append(" ").append(att.attributeValue("name")).append("_YEAR,")
					.append("decode(").append(entityCode).append(".").append(attName)
					.append(",'','',").append("to_char(").append(entityCode).append(".").append(attName)
					.append(",'MM')||'月')").append(" ").append(att.attributeValue("name")).append("_MONTH,")
					.append("decode(").append(entityCode).append(".").append(attName)
					.append(",'','',").append("to_char(").append(entityCode).append(".").append(attName)
					.append(",'DD')||'日')").append(" ").append(att.attributeValue("name")).append("_DAY,");
				}
			}
		}
		sqlBuf = new StringBuilder(sqlBuf.substring(0, sqlBuf.length()-1)).append(" from (select ");
		sqlBuf.append(DISPLAY_ENTITY_SQL_ALIAS).append(".* from (").append(getSqlContainRule(rule,false,entityXmlRoot))
				.append(") ").append(DISPLAY_ENTITY_SQL_ALIAS).append(" ").append(displayCondition).append(") ").append(entityCode);
		return sqlBuf.toString();
	}
	
	/**
	 * 包含过滤规则的查询语句
	 * @param rule
	 * @param isRelationRule
	 * @return sql
	 * @throws Exception
	 */
	private String getSqlContainRule(String rule,boolean isRelationRule,Element entityXmlRoot) throws Exception{
		try {
			List<Element> entitiesList = getAllInheritEntity(entityXmlRoot,contentXmlRoot);
			String entityCode = entityXmlRoot.attributeValue("code");
			entityCode = getNewEntityAlias(entityCode,aliasEntities);
			aliasEntities.add(entityCode);
			
			StringBuilder sqlBuf = new StringBuilder();
			
			sqlBuf.append("select ").append(entityCode).append(".* from (")
				.append(getFetchSql(entitiesList)).append(") ").append(entityCode).append(" where 1=1 ");
			if(rule == null || "".equals(rule.trim()))//没有过滤规则，则直接返回查询SQL
				return sqlBuf.toString();
			else{
				sqlBuf.append(getCondition(rule,entityCode,isRelationRule,entityXmlRoot));
				return sqlBuf.toString();
			}
		} catch (DocumentException e) {
			logger.error("解析实体XML错误", e);
			throw e;
		}
	}
	
	/**
	 * 解析实体生成读取语句
	 * @param entitiesList 所有有继承关系的实体
	 * @return
	 * @throws Exception
	 */
	private String getFetchSql(List<Element> entitiesList) throws Exception {
		String sqlStr = "";
		String pkAttribute = "";// 主键属性
		String entityCode = "";//实体编码
		List<String> aliases = new ArrayList<String>();//当前实体别名集合
		Map<String,String> atts = new HashMap<String,String>();
		for (Element entity : entitiesList) {
			StringBuilder sql = new StringBuilder("select ");
			String pkColumn = "";//主键字段
			entityCode = entity.attributeValue("code");
			entityCode = getNewEntityAlias(entityCode, aliases);
			aliases.add(entityCode);
			if(!"".equals(pkAttribute)){//如果有主键属性，说明有父实体
				for(Entry<String,String> attName : atts.entrySet()){
					sql.append(entity.attributeValue("parentEntityCode")).append(".").append(attName.getValue())
					.append(" ").append(attName.getKey()).append(",");
				}
			}
			Element mappingInfo =  entity.element("mappingInfo");
			String filter = mappingInfo.elementText("filter");
			Map<String,String> tempMap = new HashMap<String, String>();
			for (Object mapObj :mappingInfo.element("attributeMapping").elements("map")) {
				Element map = (Element) mapObj;
				if(entity.attributeValue("idAttributeName").equals(map.attributeValue("attributeName")))
					pkColumn = map.attributeValue("columnName");
				tempMap.put(map.attributeValue("attributeName"), map.attributeValue("columnName"));
			}
			Element attributeInfo = entity.element("attributes");
			for(Object attObj : attributeInfo.elements("attribute")){
				Element att = (Element) attObj;
				if("true".equals(att.attributeValue("isCompute"))){//计算属性
					sql.append("(").append(att.attributeValue("computeExpr")).append(")")
					.append(" ").append(att.attributeValue("name")).append(",");
				}else{
					sql.append(entityCode).append(".").append(tempMap.get(att.attributeValue("name")))
					.append(" ").append(att.attributeValue("name")).append(",");
				}
				atts.put(att.attributeValue("name"), att.attributeValue("name"));
			}

			if("".equals(pkColumn))
				throw new Exception("实体["+entityCode+"]缺少主键字段，无法生成SQL！");
			sql = new StringBuilder(sql.substring(0, sql.length()-1)).append(" from ");
			if(MAPPING_TYPE_TABLE.equals(mappingInfo.attributeValue("type"))){
				sql.append("(select * from ").append(mappingInfo.elementText("tableName"));
				if(filter != null && !"".equals(filter.trim()))
					sql.append(" where ").append(filter);
				sql.append(") ").append(entityCode);
			}else if(MAPPING_TYPE_QUERYVIEW.equals(mappingInfo.attributeValue("type"))){
				String viewCode = mappingInfo.attributeValue("queryView").split("@@")[0];
				CommDMO dmo = new CommDMO();
				String queryViewSql = "select sql from bam_queryview where code = '"
						+ viewCode + "'";
				try{
					HashVO[] vos = dmo.getHashVoArrayByDS(null, queryViewSql);
					if (vos.length > 0){
						sql.append("(select * from (").append(vos[0].getStringValue("sql")).append(")");
						if(filter != null && !"".equals(filter.trim()))
							sql.append(" where ").append(filter);
						sql.append(") ").append(entityCode);
					}else 
						throw new Exception("未找到查询视图[" + viewCode + "]对应的sql语句");
				}catch(Exception e){
					throw e;
				}finally{
					dmo.releaseContext(null);
				}
			}
			if(!"".equals(pkAttribute)){
				sql.append(",(").append(sqlStr).append(") ").append(entity.attributeValue("parentEntityCode"))
				.append(" where ").append(entityCode).append(".").append(pkColumn).append("=").append(entity.attributeValue("parentEntityCode"))
				.append(".").append(pkAttribute);
			}else
				sql.append(" where 1=1 ");
			
			pkAttribute = entity.attributeValue("idAttributeName");
			sqlStr = sql.toString();
		}
		return sqlStr;
	}
	
	/**
	 * 获取查询条件
	 * @param rule 规则
	 * @param entity 实体
	 * @param alias 实体别名
	 * @param isRelationRule 是否是关系过滤中的规则
	 * @return
	 * @throws Exception
	 */
	private String getCondition(String rule,String alias,boolean isRelationRule,Element entityXmlRoot) throws Exception{
		StringBuilder resultSql = new StringBuilder();
		StringBuilder andSql = new StringBuilder();
		StringBuilder orSql = new StringBuilder();
		Document ruleDoc = null;
		try{
			ruleDoc = DocumentHelper.parseText(rule);
		}catch (DocumentException e) {
			logger.error("解析实体XML错误", e);
			throw e;
		}
		Element root = ruleDoc.getRootElement();
		@SuppressWarnings("rawtypes")
		List items = null;
		if(isRelationRule)
			items = root.elements("subItem");
		else
			items = root.elements("item");
		
		if( rule != null){
			for(Object obj : items){
				Element item = (Element)obj;
				String enabled = item.attributeValue("enabled");
				if(enabled == null || "false".equals(enabled))
					continue;
				String type = item.attributeValue("type");
				String column = item.attributeValue("column");
				String logicalOperator = item.attributeValue("logicalOperator");
				String relationalOperator = escapesRelationOperators(item.attributeValue("relationalOperator"));
				String value = item.attributeValue("value");
				
				String valueStr = dealValue(value,relationalOperator);
				
				if("AND".equals(logicalOperator)){
					StringBuilder andTemp = new StringBuilder();
					
					if("stringLength".equalsIgnoreCase(type)){
						andTemp.append("length(");
						if(!isVirtualEntity)
							andTemp.append(alias).append(".");
						andTemp.append(column);
						andTemp.append(") ").append(relationalOperator).append(" ").append(valueStr);
					}else if("rowCount".equalsIgnoreCase(type)){//如果是返回行数上限
						andTemp.append(column).append(" ").append(relationalOperator).append(" ").append(valueStr);
					}else if("relation".equalsIgnoreCase(type)){//如果是关系过滤
						andTemp.append(getRelationRuleCondition(relationalOperator,column,alias,item.asXML(),entityXmlRoot));
					}else if("sql".equalsIgnoreCase(type)){
						andTemp.append(" "+value);
					}
					else{
						if(!isVirtualEntity)
							andTemp.append(alias).append(".");
						andTemp.append(column);
						andTemp.append(" ").append(relationalOperator).append(" ").append(valueStr);
					}
					
					if("".equals(andSql.toString()))
						andSql.append(andTemp);
					else{
						if(!"".equals(andTemp.toString()))
							andSql.append(" AND ").append(andTemp);
					}
				}else if("OR".equals(logicalOperator)){
					StringBuilder orTemp = new StringBuilder();
					
					if("stringLength".equalsIgnoreCase(type)){
						orTemp.append("length(");
						if(!isVirtualEntity)
							orTemp.append(alias).append(".");
						orTemp.append(column);
						orTemp.append(") ").append(relationalOperator).append(" ").append(valueStr);
					}else if("rowCount".equalsIgnoreCase(type)){//如果是返回行数上限
						orTemp.append(column).append(" ").append(relationalOperator).append(" ").append(valueStr);
					}else if("relation".equalsIgnoreCase(type)){//如果是关系过滤
						orTemp.append(getRelationRuleCondition(relationalOperator,column,alias,item.asXML(),entityXmlRoot));
					}else if("sql".equalsIgnoreCase(type)){
						orTemp.append(" "+value);
					}else{
						if(!isVirtualEntity)
							orTemp.append(alias).append(".");
						orTemp.append(column);
						orTemp.append(" ").append(relationalOperator).append(" ").append(valueStr);
					}
					
					if("".equals(orSql.toString()))
						orSql.append(orTemp);
					else{
						if(!"".equals(orTemp.toString()))
							orSql.append(" OR ").append(orTemp);
					}
				}
			}
		}
		if(!"".equals(andSql.toString()) && !"".equals(orSql.toString()))
			resultSql.append("AND ").append(andSql).append(" OR ").append(orSql);
		else if("".equals(andSql.toString()) && !"".equals(orSql.toString()))
			resultSql.append("and 1=2 OR ").append(orSql);
		else if(!"".equals(andSql.toString()) && "".equals(orSql.toString()))
			resultSql.append("AND ").append(andSql);
		
		logger.debug("条件..."+resultSql.toString());
		return resultSql.toString();
	}
	
	/**
	 * 拼装关系过滤条件
	 * @param entity
	 * @return
	 */
	private String getRelationRuleCondition(String relationalOperator,String relationCode
			,String alias,String relationRule,Element entityXmlRoot) throws Exception{
		StringBuilder result = new StringBuilder();
		if("HAVE".equals(relationalOperator))
			result.append(" exists(");
		else if("NOT HAVE".equals(relationalOperator))
			result.append("not exists(");
		result.append("select 1 from (");
		List<Element> allEntity = getAllInheritEntity(entityXmlRoot,contentXmlRoot);
		entity://标签 当查询到relation后，结束这层循环
		for(Element entityXml : allEntity){
			for(Object relationObj : entityXml.element("relations").elements("relation")){
				Element relation = (Element)relationObj;
				if(relationCode.equals(relation.attributeValue("code"))){
					String toEntityCode = relation.attributeValue("toEntityCode");
					for(Object entityObj : contentXmlRoot.element("entities").elements("entity")){
						Element entity = (Element)entityObj;
						if(entity.attributeValue("code").equals(toEntityCode)){
							entityXmlRoot = entity;//查询出目标实体，然后再次解析目标实体的SQL
							break;
						}
					}
					String fetchSql = relation.elementText("fetchSQL");
					String entityAlias = getNewEntityAlias(toEntityCode,aliasEntities);
					aliasEntities.add(entityAlias);
					result.append(fetchSql).append(") where fromId=");
					String idAttributeName = entityXmlRoot.attributeValue("idAttributeName");
					if(isVirtualEntity){//虚拟实体
						for(Object attObj : entityXmlRoot.element("attributes").elements("attribute")){
							Element att = (Element)attObj;
							if(idAttributeName.equals(att.attributeValue("name"))){
								result.append(att.attributeValue("expression"));
								break;
							}
						}
					}else
						result.append(alias).append(".").append(idAttributeName);
					result.append(" and toId in(select ").append(entityAlias).append(".").append(relation.attributeValue("toEntityIdAttributeName"))
					.append(" from (").append(getSqlContainRule(relationRule,true,entityXmlRoot)).append(") ").append(entityAlias).append("))");
					break entity;
				}
			}
		}
		
		return result.toString();
	}
	
	/**
	 * 删除实体数据（可能是多个实体关联，先删子实体数据，再删父实体数据）
	 * @param entity
	 * @param content
	 * @param dataValue
	 * @throws Exception
	 */
	public void deleteBatchEntityData(String entity,String content,Map<String,Object> dataValue) throws Exception{
		logger.debug("删除实体数据");
		Document contentXml = null;
		Document entityXml = null;
		CommDMO dmo = new CommDMO();
		
		try{
			contentXml = DocumentHelper.parseText(content);
			entityXml = DocumentHelper.parseText(entity);
		}catch(DocumentException e){
			logger.error("解析XML["+entity+"]错误", e);
		}
		contentXmlRoot = contentXml.getRootElement();
		entityXmlRoot = entityXml.getRootElement();
		String ds = contentXmlRoot.elementText("datasource");//获取数据源
		//得到所有父实体
		List<Element> allEntity = getAllInheritEntity(entityXmlRoot,contentXmlRoot);
		Collections.reverse(allEntity);//倒序（子-父-祖父）
		try{
			for(Element entityEle : allEntity){
				StringBuilder sql = new StringBuilder();
				String saveTable = null;
				for (Object editorObj : entityEle.element("editors").elements("editor")){//查找默认编辑方案
					Element editor = (Element)editorObj;
					if("true".equals(editor.attributeValue("isDefault"))){
						saveTable = editor.attributeValue("saveTable");
						break;
					}
				}
				if(saveTable == null || "".equals(saveTable))
					throw new Exception("实体["+entityEle.attributeValue("code")+"]没有");
				String pkAttribute = entityEle.attributeValue("idAttributeName");//主键属性
				if(pkAttribute == null || "".equals(pkAttribute))
					throw new Exception("实体["+entityEle.attributeValue("code")+"]主键属性为空，不能删除该实体数据！");
				String pkColumn = null;
				//查找主键属性所对应的数据字段
				for(Object attMapObj:entityEle.element("mappingInfo").element("attributeMapping").elements("map")){
					Element attMap = (Element)attMapObj;
					if(pkAttribute.equals(attMap.attributeValue("attributeName"))){
						pkColumn = attMap.attributeValue("columnName");
						break;
					}
				}
				if(pkColumn == null || "".equals(pkColumn))
					throw new Exception("实体["+entityEle.attributeValue("code")+"]主键属性对应数据字段为空，不能删除该实体数据！");
				Object idValue = dataValue.get(pkColumn.toLowerCase());
				sql.append("delete from ").append(saveTable).append(" where ").append(pkColumn).append("=?");
				logger.debug("删除实体["+entityEle.attributeValue("code")+"]SQL语句["+sql.toString()+"],参数值："+idValue);
				dmo.executeUpdateByDS(ds, sql.toString(),idValue);
			}
			dmo.commit(ds);
		}catch(Exception e){
			logger.error("删除实体数据错误！",e);
		}finally{
			dmo.releaseContext(ds);
		}
	}
	
	public static void main(String[] args) {
	}
}