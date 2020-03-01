/**
 * 
 */
package smartx.bam.bs.entitymodel.util;

import static smartx.framework.common.utils.StringUtil.getTimestampMill;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import smartx.bam.bs.common.MetadataTempletManager;
import smartx.bam.vo.SysConst;
import smartx.framework.common.utils.StringUtil;
import smartx.publics.metadata.vo.MetadataTemplet;

/**
 * @author sky
 * @May 7, 2012 实体SQL工具类
 */
public class EntitySqlUtil {

	private static Logger logger = Logger.getLogger(EntitySqlUtil.class);
	
	public static final String CONTENT_MODEL = "CONTENT_MODEL";
	
	public static final String CONTENT_ENTITY = "CONTENT_ENTITY";
	
	public static final String CONTENT_RULE = "CONTENT_RULE";
	//关系表映射类型
	public static final String MAPPING_TYPE_QUERYVIEW = "queryView";//查询视图

	public static final String MAPPING_TYPE_TABLE = "table";//关系表
	//实体数据类型
	public static final String ATTRIBUTE_TYPE_DATE = "DATE";//日期类型
	
	public static final String ATTRIBUTE_ENTITY = "entity";//实体引用
	
	public static final String TYPE_VIRTUAL = "virtual";//虚拟实体
	
	public static final String DISPLAY_ENTITY_SQL_ALIAS = "displayEntity";//包含显示属性的SQL别名
	
	public static final String ATTRIBUTE_ENTITY_REF_DISPLAYATT = "refDisplayAtt";//实体引用显示属性名称

	/**
	 * 获取新的实体别名
	 * 
	 * @param alias
	 * @return
	 */
	public static String getNewEntityAlias(String alias,
			List<String> aliasEntities) {
		for (String aliasStr : aliasEntities) {
			if (aliasStr.equals(alias)) {
				if (alias.length() > 13)
					alias = alias.substring(0, 13).concat(getTimestampMill());
				else
					alias = alias.concat(getTimestampMill());
				getNewEntityAlias(alias, aliasEntities);
			}
		}
		return alias;
	}

	/**
	 * 转义操作符
	 * 
	 * @param relationOperators
	 * @return
	 */
	public static String escapesRelationOperators(String relationOperators) {
		if ("gt".equals(relationOperators))
			return ">";
		else if ("lt".equals(relationOperators))
			return "<";
		else if ("gte".equals(relationOperators))
			return ">=";
		else if ("lte".equals(relationOperators))
			return "<=";
		else if ("ltgt".equals(relationOperators))
			return "<>";
		else
			return relationOperators;
	}

	/**
	 * 获取所有的属性
	 * 
	 * @param entitiesList
	 *            所有实体包括父实体
	 * @return
	 */
	public static List<Element> getAllAttributes(List<Element> entitiesList) {
		List<Element> allAttributes = new ArrayList<Element>();
		for (Element entity : entitiesList) {
			Element attributeInfo = entity.element("attributes");
			for (Object attObj : attributeInfo.elements("attribute")) {
				allAttributes.add((Element) attObj);
			}
		}
		return allAttributes;
	}
	
	/**
	 * 获取所有的属性
	 * @param entityModelCode
	 * @param entityCode
	 * @return Map<String,Element> key:属性名 value:属性
	 */
	public static Map<String,Element> getAllAttributesMap(String entityModelCode,String entityCode) throws Exception {
		Map<String,Element> allAttributes = new HashMap<String,Element>();		
		try{
			Map<String,String> content = initMtContent(SysConst.EN_METADATA_PREFIX.concat(entityModelCode)
					.concat("_").concat(entityCode), SysConst.EM_METADATA_PREFIX.concat(entityModelCode), null, null);
			String entityContent = content.get(CONTENT_ENTITY);
			String entityModelContent = content.get(CONTENT_MODEL);
			Document contentXml = DocumentHelper.parseText(entityModelContent);
			Document entityXml = DocumentHelper.parseText(entityContent);
			
			List<Element> allInheritEntity = getAllInheritEntity(entityXml.getRootElement(), contentXml.getRootElement());
			
			for (Element entity : allInheritEntity) {
				Element attributeInfo = entity.element("attributes");
				for (Object attObj : attributeInfo.elements("attribute")) {
					Element att = (Element) attObj;
					allAttributes.put(att.attributeValue("name"),att);
				}
			}
		}catch(Exception e){
			logger.error(e);
			throw e;
		}
		return allAttributes;
	}
	
	/**
	 * 获取当前实体的显示属性
	 * @param entityCode
	 * @return
	 */
	public static List<Element> getAllDisplayAttributes(String entityModelCode,String entityCode) throws Exception{
		List<Element> allDisplayAttributes = new ArrayList<Element>();
		//获取所有属性，包括继承属性
		Map<String,Element> allAttributeMap = getAllAttributesMap(entityModelCode,entityCode);
		
		MetadataTempletManager mtm = new MetadataTempletManager();
		MetadataTemplet entityMt = null;
		try{
			entityMt = mtm.getMetadataTempletContent(SysConst.EN_METADATA_PREFIX.concat(entityModelCode).concat("_").concat(entityCode));
			if(entityMt != null){
				Document doc = DocumentHelper.parseText(entityMt.getContent());
				Element root = doc.getRootElement();
				for(Object disAttObj : root.element("attributes").element("displayAttributes").elements("attribute")){
					Element disAtt = (Element)disAttObj;
					//封装属性的类型(varchar2、number、date)
					Element att = allAttributeMap.get(disAtt.attributeValue("name"));
					disAtt.addAttribute("type", att.attributeValue("type"));
					disAtt.addAttribute("category", att.attributeValue("category"));
					allDisplayAttributes.add(disAtt);
				}
			}
		}catch(Exception e){
			logger.error(e);
			throw e;
		}
		return allDisplayAttributes;
	}
	
	/**
	 * 拼装查询条件
	 * @param value 值
	 * @param relationalOperator 操作符
	 * @return
	 */
	public static String dealValue(String value,String relationalOperator){
		String realValue ="";
		if(value != null && value != ""){
			String[] valueArr = value.split(",");
			for(String vl : valueArr){
				if(realValue == "")
					realValue = "'"+vl+"'";
				else
					realValue = realValue+","+"'"+vl+"'";
			}
		}
		
		if("IN".equals(relationalOperator) || "NOT IN".equals(relationalOperator))
			realValue = " ("+realValue+")";
		else if("LIKE".equals(relationalOperator))
			realValue = "'%"+value+"%'";
		else if("IS".equals(relationalOperator) || "IS NOT".equals(relationalOperator))
			realValue = value;
		
		return realValue;
	}


	/**
	 * 初始化元数据（包括 实体元数据、领域实体模型中所有的实体元数据、规则元数据）
	 * @param entityMtCode
	 * @param modelMtCode
	 * @param ruleMtCode
	 * @param datasource
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> initMtContent(String entityMtCode,
			String modelMtCode, String ruleMtCode, String datasource)
			throws Exception {

		Map<String, String> contentMap = new HashMap<String, String>();
		
		MetadataTempletManager mtm = new MetadataTempletManager();
		try {
			MetadataTemplet modelContentMt = mtm.getMetadataTempletContent(modelMtCode);
			String modelContent = modelContentMt==null?"":modelContentMt.getContent();

			//可能在生成SQL的时候不包含规则
			String ruleContent = "";
			if(!StringUtil.isEmpty(ruleMtCode)){
				MetadataTemplet ruleContentMt = mtm.getMetadataTempletContent(ruleMtCode);
				ruleContent = ruleContentMt==null?"":ruleContentMt.getContent();
			}
			
			Element ruleEle = null;
			String toEntityCode = "";
			if (!StringUtil.isEmpty(ruleContent)) {
				Document ruleDoc = DocumentHelper.parseText(ruleContent);
				ruleEle = ruleDoc.getRootElement();
				toEntityCode = ruleEle.attributeValue("toEntityCode");
			}

			if (!StringUtil.isEmpty(modelContent)) {

				contentMap.put(CONTENT_RULE, ruleContent);

				Document modelDoc = DocumentHelper.parseText(modelContent);
				Element modelRoot = modelDoc.getRootElement();
				Element entities = modelRoot.element("entities");
				@SuppressWarnings("rawtypes")
				List entitiesList = entities.elements();
				if (entitiesList != null) {
					for (Object obj : entitiesList) {
						Element tempEntity = (Element) obj;
						String entityCode = tempEntity.attributeValue("code");
						String tempEntityMtCode = tempEntity.attributeValue("mtcode");
						
						MetadataTemplet entityContentMt = mtm.getMetadataTempletContent(tempEntityMtCode);
						String entityContent = entityContentMt==null?"":entityContentMt.getContent();
						
						if (!StringUtil.isEmpty(entityContent)) {
							entities.remove(tempEntity);
							Document entityDoc = DocumentHelper
									.parseText(entityContent);
							Element entityEle = entityDoc.getRootElement();

							entities.add(entityEle);

							if (!StringUtil.isEmpty(entityMtCode)
									&& entityMtCode.equals(tempEntityMtCode)) {
								contentMap.put(CONTENT_ENTITY,
										entityEle.asXML());
							}

							Element rulesEle = entityEle.addElement("rules");
							if (!StringUtil.isEmpty(toEntityCode)
									&& toEntityCode.equals(entityCode)) {
								rulesEle.add(ruleEle);
							}
						}
					}
				}
				contentMap.put(CONTENT_MODEL, modelRoot.asXML());
			}

		} catch (Exception e) {
			logger.debug("", e);
		}

		return contentMap;
	}
	
	/**
	 *  初始化元数据（包括 实体元数据、领域实体模型中所有的实体元数据、规则元数据）通过实体编码和领域编码
	 * @param entityCode
	 * @param modelCode
	 * @param ruleMtCode
	 * @param datasource
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> initMtContentByCode(String entityCode,
			String modelCode, String ruleMtCode, String datasource)
			throws Exception {
		String entityModelMtCode = SysConst.EM_METADATA_PREFIX.concat(modelCode);
		String entityMtCode = SysConst.EN_METADATA_PREFIX.concat(modelCode).concat("_").concat(entityCode);
		return initMtContent(entityMtCode, entityModelMtCode, ruleMtCode, datasource);
	}
	
	/**
	 * 得到所有有父子关系的实体(不包括抽象实体和虚拟实体)
	 * @param entity
	 * @return
	 */
	public static List<Element> getAllInheritEntity(Element entity,Element contentXmlRoot){
		List<Element> entitiesList = new ArrayList<Element>();
		entitiesList.add(entity);
		String parentEntityCode = entity.attributeValue("parentEntityCode");
		if (parentEntityCode != null && !"".equals(parentEntityCode)){
			entitiesList = recursiveAllInheritEntity(parentEntityCode,entitiesList,contentXmlRoot);
			Collections.reverse(entitiesList);//对所有实体排序 （祖父-父-子-孙）
		}
		return entitiesList;
	}

	/**
	 * 递归查询当前实体的所有的父级实体
	 * 
	 * @param parentEntityCode
	 * @param entities
	 * @param attributes
	 */
	private static List<Element> recursiveAllInheritEntity(String parentEntityCode,List<Element> entitiesList,Element contentXmlRoot) {
		for (Object obj : contentXmlRoot.element("entities").elements("entity")) {
			Element entity = (Element) obj;
			if ("false".equals(entity.attributeValue("isAbstract"))
					&& parentEntityCode.equals(entity.attributeValue("code"))) {
				entitiesList.add(entity);
				recursiveAllInheritEntity(entity.attributeValue("parentEntityCode"),entitiesList,contentXmlRoot);
			}
		}
		return entitiesList;
	}
}
