/**
 * 
 */
package smartx.bam.bs.entitymodel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.text.DecimalFormat;
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

import smartx.bam.bs.entitymodel.util.EntitySqlUtil;
import smartx.bam.vo.DatabaseConst;
import smartx.bam.vo.SysConst;
import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.bs.NovaDBConnection;
import smartx.framework.common.utils.StringUtil;
import smartx.framework.common.vo.DMOConst;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaLogger;
import smartx.framework.common.vo.SimpleHashVO;
import smartx.framework.metadata.bs.NovaServerEnvironment;
import smartx.framework.metadata.vo.TableDataStruct;
import smartx.publics.datatask.DataTaskExecThread;
import smartx.publics.metadata.bs.service.SmartXMetadataTempletService;
import smartx.publics.metadata.vo.MetadataTemplet;

/**
 * @author caohenghui Nov 7, 2011
 */
public class EntityRuleRealTimeStatisticsService {

	protected Logger logger = NovaLogger.getLogger(this.getClass());

	private final String DQ_ENTITY_STATISTICS = "DQ_ENTITY_STATISTICS";
	private final String DQ_ENTITY_STATISTICS_GROUP = "DQ_ENTITY_STATISTICS_GROUP";
	private final String DQ_ENTITY_STATISTICS_MAP = "DQ_ENTITY_STATISTICS_MAP";

	private final String S_DQ_ENTITY_STATISTICS = "S_DQ_ENTITY_STATISTICS";
	private final String S_DQ_ENTITY_STATISTICS_GROUP = "S_DQ_ENTITY_STATISTICS_GROUP";
	private final String S_DQ_ENTITY_STATISTICS_MAP = "S_DQ_ENTITY_STATISTICS_MAP";
	
	private final String HIS_TYPE_DAY = "DAY";
	private final String HIS_TYPE_MONTH = "MONTH";
	private final String HIS_TYPE_YEAR = "YEAR";

	private Map<String, String> modelMap = new HashMap<String, String>();
	private Map<String, Element> entitiesMap = new HashMap<String, Element>();
	private Map<String, Element> ruleMap = new HashMap<String, Element>();
	private Map<String, String> columnParaMap = new HashMap<String, String>();
	private Map<String, Element> paramsMap = new HashMap<String, Element>();
	
	private Map<String,Element> AttributesMap = new HashMap<String, Element>();

	private DataTaskExecThread mainThread = null;

	private EntityModelManager emm = new EntityModelManager();

	public void dealRealTimeStatistics(String modelContent, String modelCode,
			String entityCode, String ruleCode, String batchNo,
			String taskName, DataTaskExecThread mainThread) throws Exception {
		this.mainThread = mainThread;
		if (!StringUtil.isEmpty(modelContent)) {
			if (this.initEntitiesInfo(modelContent, modelCode, ruleCode)) {
				String datasource = getDatasource(modelContent);
				String entityTable = this.getEntityTable(entityCode);
				String ruleName = this.getRuleName(entityCode, ruleCode);
				String curHeade = this.getHeaderContent(entityCode);

				if (!this.checkTables(DatabaseConst.DS_SMARTDQ)) {
					return;
				}
				
				AttributesMap = EntitySqlUtil.getAllAttributesMap(modelCode, entityCode);

				String statisticsId = this.insertIntoEntityStatistics(
						modelCode, entityCode, ruleCode, entityTable, ruleName,
						batchNo, taskName, DatabaseConst.DS_SMARTDQ);
				if (!StringUtil.isEmpty(statisticsId)) {
					String detailTabelName = this.dealDetailTableInfo(
							modelCode, entityCode, statisticsId, curHeade,
							DatabaseConst.DS_SMARTDQ);
					if (!StringUtil.isEmpty(detailTabelName)) {
						this.insertRecordsToDetailTable(detailTabelName,
								statisticsId, modelCode, entityCode, ruleCode,
								DatabaseConst.DS_SMARTDQ, datasource);
					}
					this.insertRecordToGroupTable(statisticsId, modelCode,
							entityCode, ruleCode, DatabaseConst.DS_SMARTDQ,
							datasource);
				}
			} else {
				logger.debug("无法初始化模型元数据!");
			}

		}
	}

	private String getDatasource(String modelContent) {
		String datasource = "";
		try {
			Document doc = DocumentHelper.parseText(modelContent);
			Element root = doc.getRootElement();
			datasource = root.elementText("datasource");
		} catch (DocumentException e) {
			logger.debug("", e);
		}
		return datasource;
	}

	private boolean checkTables(String datasource) {
		boolean flag = true;
		if (!isExistTable(DQ_ENTITY_STATISTICS, datasource)) {
			if (!this.createStatisticsTable(datasource)) {
				return flag;
			}
		}

		if (!isExistTable(this.DQ_ENTITY_STATISTICS_MAP, datasource)) {
			if (!this.createStatisticsMapTable(datasource)) {
				return false;
			}
		}

		if (!isExistTable(this.DQ_ENTITY_STATISTICS_GROUP, datasource)) {
			if (!this.createStatisticsGroupTable(datasource)) {
				return false;
			}
		}
		return flag;
	}

	private String insertIntoEntityStatistics(String modelCode,
			String entityCode, String ruleCode, String entityTable,
			String ruleName, String batchNo, String taskName, String datasource)
			throws Exception {
		String statisticsId = "";
		CommDMO dmo = new CommDMO();
		try {
			statisticsId = dmo.getSequenceNextValByDS(datasource,
					S_DQ_ENTITY_STATISTICS);
			String insertSQL = "insert into "
					+ DQ_ENTITY_STATISTICS
					+ " (ID,ENTITYMODELCODE,ENTITYCODE,RULECODE,ENTITYTABLE,RULENAME,BATCHNO,TASKNAME,CREATETIME) values(?,?,?,?,?,?,?,?,systimestamp)";
			dmo.executeUpdateByDS(datasource, insertSQL, statisticsId,
					modelCode, entityCode, ruleCode, entityTable, ruleName,
					batchNo, taskName);
			dmo.commit(datasource);
		} catch (Exception e) {
			try {
				dmo.rollback(datasource);
				statisticsId = "";
			} catch (Exception e1) {
				logger.debug("", e1);
			}
			throw e;
		} finally {
			dmo.releaseContext(datasource);
		}

		return statisticsId;
	}

	// 返回详情表的表名
	private String dealDetailTableInfo(String modelCode, String entityCode,
			String statisticsId, String curHeade, String datasource)
			throws Exception {
		CommDMO dmo = new CommDMO();
		String detailTableName = "";
		try {
			log("正在获取详情表表名....", "");
			
			Map<String, String> columnsMap = new HashMap<String, String>();
			columnsMap.put("DQ_ID", "NUMBER");
			columnsMap.put("DQ_STATISTICSID", "NUMBER");
			
			String curDisplayAttributes = this.getDistPlayAttributes(modelCode,entityCode,columnsMap);
			
			String searchMapSql = "select * from "
					+ DQ_ENTITY_STATISTICS_MAP
					+ " where entitymodelcode=? and entitycode=? order by createtime desc";
			HashVO[] mapVOS = dmo.getHashVoArrayByDS(datasource, searchMapSql,
					modelCode, entityCode);
			if (mapVOS != null && mapVOS.length > 0) {
				HashVO vo = mapVOS[0];
				String oldHeade = vo.getStringValue("MTCONTENT");
				if (oldHeade.equals(curDisplayAttributes)) {
					String mappingId = vo.getStringValue("ID");
					detailTableName = vo.getStringValue("DETAILTABLENAME");
					updateEntityStatistics(statisticsId, mappingId, datasource);
					log("已获取详情表:" + detailTableName, "");
					return detailTableName;
				}
			}

			log("未获取到对应的详情表....", "");
			
			getAllGroupColumns(modelCode,entityCode,columnsMap);
			getPkColumn(modelCode,entityCode,columnsMap);
			
			detailTableName = createNewDetailTable(columnsMap, datasource);
			if (!StringUtil.isEmpty(detailTableName)) {
				String mappingId = insertMappingInfo(modelCode, entityCode,
						detailTableName, curDisplayAttributes, datasource);
				updateEntityStatistics(statisticsId, mappingId, datasource);
			}

		} catch (Exception e) {
			logger.debug("", e);
			log("处理详情表时发生异常!", "");
			throw e;
		}finally{
			dmo.releaseContext(datasource);
		}
		return detailTableName;
	}
	
	private String getDistPlayAttributes(String modelCode,String entityCode,Map<String, String> columnsMap) throws Exception{
		Document doc = DocumentHelper.parseText("<attributes><displayAttributes></displayAttributes></attributes>");
		try{
			List<Element> list = EntitySqlUtil.getAllDisplayAttributes(modelCode,entityCode);
			if(list != null){
				Element root = doc.getRootElement();
				Element displayEle = root.element("displayAttributes");
				for(Element ele : list){
					String refDisplayAtt = ele.attributeValue("refDisplayAtt");
					columnsMap.put(ele.attributeValue("name").toUpperCase(),ele.attributeValue("type").toUpperCase());
					if(StringUtil.isEmpty(refDisplayAtt)){
						displayEle.add(ele.createCopy());
					}else{
						String name = ele.attributeValue("name");
						Element tempEle = ele.createCopy();
						tempEle.addAttribute("name", refDisplayAtt);
						tempEle.addAttribute("refDisplayAtt", name);
						displayEle.add(tempEle);
						columnsMap.put(refDisplayAtt.toUpperCase(),"varchar2");
					}
				}
			}
		}catch(Exception e){
			this.log("获取显示属性时出错!", "");
			throw e;
		}
		return doc.asXML();
	}
	
	private void getAllGroupColumns(String modelCode,String entityCode,Map<String, String> columnsMap){
		try{
			if (entitiesMap != null) {
				Element entity = entitiesMap.get(entityCode);
				if (entity != null) {
					Element rulesNode = entity.element("rules");
					List rulesList = rulesNode.elements();
					if (rulesList != null) {
						for (Object rule : rulesList) {
							Element ele = (Element) rule;
							String columnName = ele.attributeValue("groupcolumn");
							if(!StringUtil.isEmpty(columnName)){
								Element columnEle = AttributesMap.get(columnName);
								String name = columnEle.attributeValue("name");
								String type = columnEle.attributeValue("type");
								if(!StringUtil.isEmpty(name)){
									columnsMap.put(name.toUpperCase(), type);
								}
							}
						}
					}
				}
			}
		}catch(Exception e){
			logger.debug("",e);
		}
	}
	
	private void getPkColumn(String modelCode,String entityCode,Map<String, String> columnsMap){
		try{
			if (entitiesMap != null) {
				Element entity = entitiesMap.get(entityCode);
				if (entity != null) {
					String pkName = entity.attributeValue("idAttributeName");
					if(!StringUtil.isEmpty(pkName)){
						Element columnEle = AttributesMap.get(pkName);
						String name = columnEle.attributeValue("name");
						String type = columnEle.attributeValue("type");
						if(!StringUtil.isEmpty(name)){
							columnsMap.put(name.toUpperCase(), type);
						}
					}
				}
			}
		}catch(Exception e){
			logger.debug("",e);
		}
	}

	private String createNewDetailTable(Map<String, String> columnsMap, String datasource)
			throws Exception {
		String tableName = "";
		CommDMO dmo = new CommDMO();
		log("正在创建新的详情表....", "");
		try {

			List<String> sqls = new ArrayList<String>();
			String seq = dmo.getSequenceNextValByDS(DatabaseConst.DS_SMARTDQ,
					"S_DQ_ENTITYMODEL_DETAILNAME");
			tableName = "DQ_ENTITY_DETAIL_" + seq;

			String buildTableSql = "create table " + tableName + " ";

			String columns = "";
			Set<String> keys = columnsMap.keySet();
			for (String key : keys) {
				if (StringUtil.isEmpty(columns)) {
					columns = "(" + key + " "
							+ getColumnLength(columnsMap.get(key));
				} else {
					columns = columns + "," + key + " "
							+ getColumnLength(columnsMap.get(key));
				}
			}

			buildTableSql = buildTableSql + columns + ")";

			sqls.add(buildTableSql);

			String addPrimaryKeySQL = "alter table " + tableName
					+ " add primary key (DQ_ID) using index";
			sqls.add(addPrimaryKeySQL);

			String addIndexSQL = "create index " + "IDX2" + tableName + " on "
					+ tableName + " (DQ_STATISTICSID)";
			sqls.add(addIndexSQL);

			String sequenceSQL = "create sequence S_"
					+ tableName
					+ " minvalue 1 start with 1 increment by 1 nomaxvalue nocycle cache 20";
			sqls.add(sequenceSQL);

			dmo.executeBatchByDS(datasource, sqls);
			dmo.commit(datasource);

		} catch (Exception e) {
			logger.debug("", e);
			try {
				dmo.rollback(datasource);
				tableName = "";
			} catch (Exception e1) {
				logger.debug("", e1);
			}
			throw e;
		} finally {
			dmo.releaseContext(datasource);
		}
		log("详情表创建成功:" + tableName, "");
		return tableName;
	}

	private String getColumnLength(String type) {
		String temp = "VARCHAR2(1000)";
		if (type.toUpperCase().trim().equals("VARCHAR2")) {
			temp = "VARCHAR2(2000)";
		} else if (type.toUpperCase().trim().equals("NUMBER")) {
			temp = "NUMBER(30)";
		} else if (type.toUpperCase().trim().equals("DATE")) {
			temp = "DATE";
		} else if (type.toUpperCase().trim().equals("CLOB")) {
			temp = "CLOB";
		}
		return temp;
	}

	private boolean isExistTable(String tableName, String datasource) {
		boolean flag = false;
		CommDMO dmo = new CommDMO();
		try {
			String sql = "select count(table_name) cou from user_tables where table_name=?";
			HashVO[] vos = dmo.getHashVoArrayByDS(datasource, sql, tableName);
			if (vos != null && vos.length > 0) {
				int count = vos[0].getIntegerValue("cou");
				if (count > 0) {
					flag = true;
				} else {
					flag = false;
				}
			}
		} catch (Exception e) {
			flag = false;
		} finally {
			dmo.releaseContext(datasource);
		}
		return flag;
	}

	private boolean createStatisticsTable(String datasource) {
		boolean flag = false;
		CommDMO dmo = new CommDMO();
		try {

			List<String> sqls = new ArrayList<String>();

			String createSQL = "create table "
					+ DQ_ENTITY_STATISTICS
					+ " (ID NUMBER(11) not null,ENTITYMODELCODE VARCHAR2(255),ENTITYCODE VARCHAR2(255),ENTITYTABLE VARCHAR2(50),RULENAME VARCHAR2(255),RULECODE VARCHAR2(255),MAPID NUMBER(11),CREATETIME DATE,REMARK VARCHAR2(1000),ROWCOUNT NUMBER(18))";
			sqls.add(createSQL);

			String createIndex1 = "alter table "
					+ DQ_ENTITY_STATISTICS
					+ " add constraint PK_DQ_ENTITY_STATISTICS primary key (ID) using index";
			sqls.add(createIndex1);

			String createIndex2 = "create index IDX_ENTITY_STATISTICS_1 on "
					+ DQ_ENTITY_STATISTICS + " (ENTITYMODELCODE, ENTITYCODE)";
			sqls.add(createIndex2);

			String createIndex3 = "create index IDX_ENTITY_STATISTICS_MAPID on "
					+ DQ_ENTITY_STATISTICS + " (MAPID)";
			sqls.add(createIndex3);

			String sequenceSQL = "create sequence "
					+ this.S_DQ_ENTITY_STATISTICS
					+ " minvalue 1 start with 1 increment by 1 nomaxvalue nocycle cache 20";
			sqls.add(sequenceSQL);

			dmo.executeBatchByDS(datasource, sqls);
			dmo.commit(datasource);

			flag = true;

		} catch (Exception e) {
			try {
				dmo.rollback(datasource);
				flag = false;
			} catch (Exception e1) {
				logger.debug("", e1);
			}
		} finally {
			dmo.releaseContext(datasource);
		}
		return flag;
	}

	private boolean createStatisticsMapTable(String datasource) {
		boolean flag = false;
		CommDMO dmo = new CommDMO();
		try {

			List<String> sqls = new ArrayList<String>();

			String createSQL = "create table "
					+ DQ_ENTITY_STATISTICS_MAP
					+ " (ID NUMBER(11) not null,ENTITYMODELCODE VARCHAR2(255),ENTITYCODE VARCHAR2(255),DETAILTABLENAME VARCHAR2(255),MTCONTENT CLOB,CREATETIME DATE)";
			sqls.add(createSQL);

			String createIndex1 = "alter table "
					+ DQ_ENTITY_STATISTICS_MAP
					+ " add constraint PK_DQ_ENTITY_STATISTICS_MAP primary key (ID) using index";
			sqls.add(createIndex1);

			String createIndex2 = "create index IDX_ENTITY_STATISTICS_MAP_1 on "
					+ DQ_ENTITY_STATISTICS_MAP
					+ " (ENTITYMODELCODE, ENTITYCODE)";
			sqls.add(createIndex2);

			String createIndex3 = "create index IDX_ENTITY_STATISTICS_MAP_2 on "
					+ DQ_ENTITY_STATISTICS_MAP + " (DETAILTABLENAME)";
			sqls.add(createIndex3);

			String sequenceSQL = "create sequence "
					+ this.S_DQ_ENTITY_STATISTICS_MAP
					+ " minvalue 1 start with 1 increment by 1 nomaxvalue nocycle cache 20";
			sqls.add(sequenceSQL);

			dmo.executeBatchByDS(datasource, sqls);
			dmo.commit(datasource);

			flag = true;

		} catch (Exception e) {
			try {
				dmo.rollback(datasource);
				flag = false;
			} catch (Exception e1) {
				logger.debug("", e1);
			}
		} finally {
			dmo.releaseContext(datasource);
		}
		return flag;
	}

	private boolean createStatisticsGroupTable(String datasource) {
		boolean flag = false;
		CommDMO dmo = new CommDMO();
		try {

			List<String> sqls = new ArrayList<String>();

			String createSQL = "create table "
					+ DQ_ENTITY_STATISTICS_GROUP
					+ " (ID NUMBER(11) not null,STATISTICSID NUMBER(11),GROUPCOLUMN VARCHAR2(50),COLUMNVALUE VARCHAR2(255),ROWCOUNT NUMBER(11),TOTLECOUNT NUMBER(11),PERCENT NUMBER(11,8))";
			sqls.add(createSQL);

			String createIndex1 = "alter table "
					+ DQ_ENTITY_STATISTICS_GROUP
					+ " add constraint PK_DQ_ENTITY_STATISTICS_GROUP primary key (ID) using index";
			sqls.add(createIndex1);

			String createIndex2 = "alter table "
					+ DQ_ENTITY_STATISTICS_GROUP
					+ " add constraint FK_DQ_ENTI_REFERENCE_BAM_ENTI foreign key (STATISTICSID) references BAM_ENTITY_STATISTICS (ID)";
			sqls.add(createIndex2);

			String createIndex3 = "create index IDX_ENTITY_STATISTICS_GROUP_1 on "
					+ DQ_ENTITY_STATISTICS_GROUP + " (STATISTICSID)";
			sqls.add(createIndex3);

			String sequenceSQL = "create sequence "
					+ this.S_DQ_ENTITY_STATISTICS_GROUP
					+ " minvalue 1 start with 1 increment by 1 nomaxvalue nocycle cache 20";
			sqls.add(sequenceSQL);

			dmo.executeBatchByDS(datasource, sqls);
			dmo.commit(datasource);

			flag = true;

		} catch (Exception e) {
			try {
				dmo.rollback(datasource);
				flag = false;
			} catch (Exception e1) {
				logger.debug("", e1);
			}
		} finally {
			dmo.releaseContext(datasource);
		}
		return flag;
	}

	private String insertMappingInfo(String modelCode, String entityCode,
			String detailTableName, String mtContent, String datasource) {
		CommDMO dmo = new CommDMO();
		String id = "";
		try {
			id = dmo.getSequenceNextValByDS(datasource,
					S_DQ_ENTITY_STATISTICS_MAP);
			String sql = "insert into "
					+ DQ_ENTITY_STATISTICS_MAP
					+ " (ID,ENTITYMODELCODE,ENTITYCODE,DETAILTABLENAME,CREATETIME) values(?,?,?,?,systimestamp)";
			dmo.executeUpdateByDS(datasource, sql, id, modelCode, entityCode,
					detailTableName);
			dmo.commit(datasource);

			dmo.executeUpdateClobByDS(datasource, "MTCONTENT",
					DQ_ENTITY_STATISTICS_MAP, " id = " + id, mtContent);
			dmo.commit(datasource);

		} catch (Exception e) {
			try {
				dmo.rollback(datasource);
			} catch (Exception e1) {
				logger.debug("", e1);
			}
		} finally {
			dmo.releaseContext(datasource);
		}
		return id;
	}

	private void updateEntityStatistics(String entityStatisticsId,
			String mappingId, String datasource) {
		CommDMO dmo = new CommDMO();
		try {
			String sql = "update " + DQ_ENTITY_STATISTICS
					+ " set mapid=? where id = ?";
			dmo.executeUpdateByDS(datasource, sql, mappingId,
					entityStatisticsId);
			dmo.commit(datasource);
		} catch (Exception e) {
			try {
				dmo.rollback(datasource);
			} catch (Exception e1) {
				logger.debug("", e1);
			}
		} finally {
			dmo.releaseContext(datasource);
		}

	}

	private void insertRecordsToDetailTable(String detailTableName,
			String entityStatisticsId, String modelCode, String entityCode,
			String ruleCode, String dq_datasource, String datasource)
			throws Exception {
		CommDMO dmo = new CommDMO();
		try {

			String countSQL = this.getOriginalSQL(modelCode, entityCode,ruleCode);
			
			String querySQL = this.getSQL(modelCode, entityCode,ruleCode);

			int rowCount = 0;
			
			log("正在计算总数量....", "");

//			HashVO[] totalCountVO = dmo.getHashVoArrayByDS(datasource,"select count(1) cou from (" + countSQL + ")");
//			if (totalCountVO != null && totalCountVO.length > 0) {
//				rowCount = totalCountVO[0].getIntegerValue("cou");
//			}

			int totalCount = emm.generateOriginalEntitySqlCountByMtCode(SysConst.EN_METADATA_PREFIX+modelCode+"_"+entityCode, SysConst.EM_METADATA_PREFIX+modelCode, "", "", datasource);
//			String totalSQL = this.getOriginalSQL(modelCode, entityCode, null);
//			HashVO[] totalCountVO = dmo.getHashVoArrayByDS(datasource,"select count(1) cou from (" + totalSQL + ")");
//			if (totalCountVO != null && totalCountVO.length > 0) {
//				totalCount = totalCountVO[0].getIntegerValue("cou");
//			}
			
			log("总数量计算结束,数量为:"+totalCount, "");
			
			log("正在处理详情....", "");

//			if (rowCount > 0) {

				NovaDBConnection conn = null;
				PreparedStatement stat = null;
				if (dq_datasource == null) {
					dq_datasource = NovaServerEnvironment.getInstance().getDefaultDataSourceName();
				}

				String[] keys = this.getKeysFromTable(detailTableName,dq_datasource);

				String columns = "";
				String values = "";
				int index = 1;
				int[] cols = new int[keys.length - 2];
				for (String column : keys) {
					if (!column.equalsIgnoreCase("DQ_ID")
							&& !column.equalsIgnoreCase("DQ_STATISTICSID")) {
						if (StringUtil.isEmpty(columns)) {
							columns = column;
							values = "?";
						} else {
							columns = columns + "," + column;
							values = values + ",?";
						}
						cols[index - 1] = index;
						index++;
					}

				}

				String sql = "insert into " + detailTableName
						+ " (DQ_ID,DQ_STATISTICSID"
						+ (columns == "" ? "" : ("," + columns)) + ") "
						+ "values(s_" + detailTableName + ".nextval,"
						+ entityStatisticsId
						+ (values == "" ? "" : ("," + values)) + ")";

				conn = dmo.getConn(datasource);
				stat = conn.prepareStatement("select "
						+ (columns == "" ? "*" : columns) + " from ("
						+ querySQL + ")");
				ResultSet resultSet = stat.executeQuery();

				long row = dmo.executeImportByDS(dq_datasource, sql, resultSet, cols, 1000);
				
				log("记录详情处理结束....", "");
				
				updateRowCountToStatistics(entityStatisticsId, (int)row,
						totalCount, dq_datasource);
				
				log("总记录数:" + totalCount + ";符合规则总记录数:" + row, "");
//			}
		} catch (Exception e) {
			logger.debug("", e);
			log("发生异常!", "");
			throw e;
		} finally {
			dmo.releaseContext(dq_datasource);
			dmo.releaseContext(datasource);
		}
	}

	private String[] getKeysFromTable(String tableName, String datasource)
			throws Exception {
		CommDMO dmo = new CommDMO();
		String[] keys = null;
		try {
			TableDataStruct tds = dmo.getTableDataStructByDS(datasource,
					"select * from " + tableName + " where 1=2");
			if (tds != null) {
				keys = tds.getTable_header();
			}
		} catch (Exception e) {
			logger.debug("", e);
			throw e;
		}
		return keys;
	}

	private void updateRowCountToStatistics(String statisticsId, int rowcount,
			int totalCount, String datasource) throws Exception {
		CommDMO dmo = new CommDMO();
		try {
			String sql = "update " + DQ_ENTITY_STATISTICS
					+ " set ROWCOUNT=?,TOTALCOUNT=? where id = ?";
			dmo.executeUpdateByDS(datasource, sql, rowcount, totalCount,
					statisticsId);
			dmo.commit(datasource);
		} catch (Exception e) {
			try {
				dmo.rollback(datasource);
			} catch (Exception e1) {
				logger.debug("", e1);
			}
			throw e;
		} finally {
			dmo.releaseContext(datasource);
		}
	}

	private Object getColumnValue(String key, ResultSet resultSet) {

		Object value = null;
		try {
			int type = resultSet.getType();
			if (type == Types.VARCHAR || type == Types.CLOB) {
				value = resultSet.getString(key);
			} else if (type == Types.NUMERIC || type == Types.SMALLINT) {
				value = resultSet.getLong(key);
			} else if (type == Types.DATE || type == Types.TIMESTAMP) {
				value = resultSet.getDate(key);
			} else if (type == Types.DECIMAL || type == Types.DOUBLE
					|| type == Types.FLOAT) {
				value = resultSet.getDouble(key);
			} else {
				value = resultSet.getObject(key);
			}
		} catch (Exception e) {
			logger.debug("", e);
		}
		return value;
	}

	private void insertRecordToGroupTable(String entityStatisticsId,
			String modelCode, String entityCode, String ruleCode,
			String dq_datasource, String datasource) throws Exception {
		CommDMO dmo = new CommDMO();
		try {
			log("正在处理分组记录.....", "");
			String groupField = getGroupColumn(entityCode, ruleCode);
			if (StringUtil.isEmpty(groupField)) {
				log("无分组字段,分组处理结束!", "");
				return;
			}
			
			Element columnEle = AttributesMap.get(groupField);
			String tempTotalGroupSQL = null;
			HashVO[] allGroupItems = null;
			if(columnEle != null && columnEle.attributeValue("category").equalsIgnoreCase("entity")){
				
				String refEntityCode = columnEle.attributeValue("refEntity");
				String refEntityAtt = columnEle.attributeValue("refEntityAtt");	
				tempTotalGroupSQL = "select " + refEntityAtt + ",0 goupcount from ("+ getOriginalSQL(modelCode, refEntityCode, null)+ ")";
				
				allGroupItems = dmo.getHashVoArrayByDS(datasource,tempTotalGroupSQL);
			}
			
			String totalGroupSQL = "select " + groupField + ",count(*) as goupcount from (" + getOriginalSQL(modelCode, entityCode, null) + ") group by " + groupField;
			
			HashVO[] groupVOS = dmo.getHashVoArrayByDS(datasource,totalGroupSQL);
			if(allGroupItems != null && allGroupItems.length >0){
				Map<String,HashVO> tempMap = new HashMap<String,HashVO>();
				for(HashVO vo : groupVOS){
					tempMap.put(vo.getStringValue(0), vo);
				}
				groupVOS = new HashVO[allGroupItems.length];
				int index = 0;
				for(HashVO item : allGroupItems){
					HashVO groupVo = tempMap.get(item.getStringValue(0));
					if(groupVo == null){
						groupVo = item;
					}
					groupVOS[index] = groupVo;
					index++;
				}
			}
			
			String rowGroupSQL = "select " + groupField+ ",count(*) as rowcount from ("+ getOriginalSQL(modelCode, entityCode, ruleCode)+ ") group by " + groupField;
			
			HashVO[] rowVOS = dmo.getHashVoArrayByDS(datasource, rowGroupSQL);
			if (rowVOS == null || rowVOS.length == 0) {
				rowVOS = new HashVO[groupVOS.length];
				for (int i = 0; i < groupVOS.length; i++) {
					HashVO rowVo = new HashVO();
					rowVo.setAttributeValue(groupField,
							groupVOS[i].getStringValue(groupField));
					rowVo.setAttributeValue("rowcount", "0");
					rowVOS[i] = rowVo;
				}
			} else if (rowVOS.length < groupVOS.length) {
				
				Map<String,HashVO> rowVosMap = new HashMap<String,HashVO>();
				for(HashVO tempVo : rowVOS){
					String key = tempVo.getStringValue(groupField);
					rowVosMap.put(key, tempVo);
				}
				for (int i = 0; i < groupVOS.length; i++) {
					String tempVaule = groupVOS[i].getStringValue(groupField);
					if(!rowVosMap.containsKey(tempVaule)){
						HashVO rowVo = new HashVO();
						rowVo.setAttributeValue(groupField,tempVaule);
						rowVo.setAttributeValue("rowcount", "0");
						rowVosMap.put(tempVaule,rowVo);
					}
				}
				HashVO[] tempRowVOS = new HashVO[rowVosMap.size()];
				Set<String> keys = rowVosMap.keySet();
				int index = 0;
				for(String key : keys){
					tempRowVOS[index] = rowVosMap.get(key);
					index++;
				}
				rowVOS = tempRowVOS;
			}

			String insertSQL = "insert into "
					+ DQ_ENTITY_STATISTICS_GROUP
					+ "(ID,STATISTICSID,GROUPCOLUMN,COLUMNVALUE,ROWCOUNT,TOTALCOUNT,PERCENT,COLUMNLABEL) values("
					+ S_DQ_ENTITY_STATISTICS_GROUP + ".nextval,?,?,?,?,?,?,?)";
			log("正在写入分组数据,分组字段:" + groupField, "");
			if (groupVOS != null) {
				for (HashVO groupItem : groupVOS) {
					String groupValue = groupItem.getStringValue(groupField);
					String groupLabel = this.getGroupLable(groupField,
							groupValue, entityCode);
					int groupCount = groupItem.getIntegerValue("goupcount");
					for (HashVO rowItem : rowVOS) {
						String rowValue = rowItem.getStringValue(groupField);
						int rowCount = rowItem.getIntegerValue("rowcount");
						if ((StringUtil.isEmpty(groupValue) && StringUtil
								.isEmpty(rowValue))
								|| (!StringUtil.isEmpty(groupValue)
										&& !StringUtil.isEmpty(rowValue) && groupValue
										.trim().equals(rowValue.trim()))) {
							float percentTemp = 0;
							if(groupCount != 0 )
								percentTemp = (float) rowCount / groupCount;
							
							DecimalFormat df = new DecimalFormat("0.00");
							Float percent = new Float(df.format(percentTemp));
							Object[] parameters = new Object[7];
							parameters[0] = entityStatisticsId;
							parameters[1] = groupField;
							parameters[2] = rowValue;
							parameters[3] = rowCount;
							parameters[4] = groupCount;
							parameters[5] = percent;
							parameters[6] = groupLabel;
							dmo.executeUpdateByDS(dq_datasource, insertSQL,
									parameters);
							dmo.commit(dq_datasource);
							break;
						}
					}
				}
			}

		} catch (Exception e) {
			logger.debug("", e);
			log("分组处理异常!", "");
			throw e;
		} finally {
			dmo.releaseContext(datasource);
			dmo.releaseContext(dq_datasource);
		}
		log("分组处理结束!", "");
	}

	private String getHeaderContent(String entityCode) {

		String headerContent = "";
		if (entitiesMap != null) {
			Element entity = entitiesMap.get(entityCode);
			if (entity != null) {
				Element attr = entity.element("attributes");
				headerContent = attr.asXML();
			}
		}
		return headerContent;

	}

	public String getSQL(String modelCode, String entityCode, String ruleCode)
			throws Exception {
		return emm.generateEntitySql(this.getEntityToString(entityCode),
				getEntityModelToString(modelCode),
				this.getRuleToString(entityCode, ruleCode));
	}

	public String getOriginalSQL(String modelCode, String entityCode,
			String ruleCode) throws Exception {
		return emm.generateEntitySqlOriginalData(
				this.getEntityToString(entityCode),
				getEntityModelToString(modelCode),
				this.getRuleToString(entityCode, ruleCode));
	}

	private String getEntityModelToString(String modelCode) {
		String content = "";
		if (modelMap != null && !StringUtil.isEmpty(modelCode)) {
			content = modelMap.get(modelCode);
		}
		return content;
	}

	private String getEntityToString(String entityCode) {
		String content = "";
		if (entitiesMap != null && !StringUtil.isEmpty(entityCode)) {
			content = entitiesMap.get(entityCode).asXML();
		}
		return content;
	}

	private String getRuleToString(String entityCode, String ruleCode) {
		String content = "";
		if (ruleMap != null && !StringUtil.isEmpty(entityCode)
				&& !StringUtil.isEmpty(ruleCode)) {
			content = ruleMap.get(entityCode + "_" + ruleCode).asXML();
		}
		return content;
	}

	private String getGroupColumn(String entityCode, String ruleCode) {
		String columnName = "";
		if (entitiesMap != null) {
			Element entity = entitiesMap.get(entityCode);
			if (entity != null) {
				Element rulesNode = entity.element("rules");
				List rulesList = rulesNode.elements();
				if (rulesList != null) {
					for (Object rule : rulesList) {
						Element ele = (Element) rule;
						String tempRuleCode = ele.attributeValue("code");
						if (!StringUtil.isEmpty(tempRuleCode)
								&& tempRuleCode.equals(ruleCode)) {
							columnName = ele.attributeValue("groupcolumn");
							break;
						}
					}
				}

				// if(StringUtil.isEmpty(columnName)){
				// columnName = entity.attributeValue("groupAttributes");
				// }
			}
		}
		return columnName;
	}

	private String getRuleName(String entityCode, String ruleCode) {
		String ruleName = "";
		if (entitiesMap != null) {
			Element entity = entitiesMap.get(entityCode);
			if (entity != null) {
				Element rulesNode = entity.element("rules");
				List rulesList = rulesNode.elements();
				if (rulesList == null) {
					return "";
				}
				for (Object rule : rulesList) {
					Element ele = (Element) rule;
					String tempRuleCode = ele.attributeValue("code");
					if (!StringUtil.isEmpty(tempRuleCode)
							&& tempRuleCode.equals(ruleCode)) {
						ruleName = ele.attributeValue("name");
						break;
					}
				}
			}
		}
		return ruleName;
	}

	private String getEntityTable(String entityCode) {
		String table = "";
		if (entitiesMap != null) {
			Element entity = entitiesMap.get(entityCode);
			if (entity != null) {
				Element mappingInfo = entity.element("mappingInfo");
				String type = mappingInfo.attributeValue("type");
				if (!StringUtil.isEmpty(type) && type.trim().equals("table")) {
					table = mappingInfo.elementText("tableName");
				} else {
					String[] info = mappingInfo.elementText("queryView").split(
							"@@");
					if (info != null && info.length == 2) {
						table = info[1];
					}
				}
			}
		}
		return table;
	}

	public String getEntityModelContent(String modelCode) throws Exception {
		CommDMO dmo = new CommDMO();
		String content = "";
		try {
			String sql = "select mt.content from bam_entitymodel em,pub_metadata_templet mt where em.mtcode=mt.code and mt.type=? and em.code=?";
			HashVO[] vos = dmo.getHashVoArrayByDS(
					DatabaseConst.DATASOURCE_DEFAULT, sql, 103, modelCode);
			if (vos != null && vos.length > 0) {
				HashVO vo = vos[0];
				content = vo.getStringValue("content");
			}
		} catch (Exception e) {
			logger.debug("", e);
			throw e;
		} finally {
			dmo.releaseContext(DatabaseConst.DATASOURCE_DEFAULT);
		}
		return content;
	}

	public boolean initEntitiesInfo(String modelContent, String entityModelCode)
			throws Exception {
		boolean flag = false;
		try {
			if (!StringUtil.isEmpty(modelContent)) {
				modelMap.put(entityModelCode, modelContent);
				Document doc = DocumentHelper.parseText(modelContent);
				Element root = doc.getRootElement();
				Element entities = root.element("entities");
				List entitiesList = entities.elements();
				if (entitiesList != null) {
					for (Object obj : entitiesList) {
						Element tempEntity = (Element) obj;
						String entityCode = tempEntity.attributeValue("code");
						entitiesMap.put(entityCode, tempEntity);
						Element rulesNode = tempEntity.element("rules");
						if (rulesNode != null) {
							List ruleList = rulesNode.elements("rule");
							if (ruleList != null) {
								for (Object item : ruleList) {
									Element rule = (Element) item;
									String ruleCode = rule
											.attributeValue("code");
									ruleMap.put(entityCode + "_" + ruleCode,
											rule);
								}
							}
						}
					}
				}
				flag = true;
			}
		} catch (Exception e) {
			logger.debug("", e);
			flag = false;
			throw new Exception("实体无法初始化:" + e.getMessage());
		}

		return flag;
	}

	public boolean initEntitiesInfo(String modelContent,
			String entityModelCode, String ruleCode) throws Exception {
		boolean flag = false;
		log("正在初始化元数据....", "");
		try {
			modelMap.clear();
			entitiesMap.clear();
			ruleMap.clear();
			paramsMap.clear();
			if (!StringUtil.isEmpty(modelContent)) {
				// modelMap.put(entityModelCode, modelContent);
				Document doc = DocumentHelper.parseText(modelContent);
				Element root = doc.getRootElement();
				Element entities = root.element("entities");
				List entitiesList = entities.elements();
				if (entitiesList != null) {
					for (Object obj : entitiesList) {
						Element tempEntity = (Element) obj;
						String entityCode = tempEntity.attributeValue("code");
						String mtCode = tempEntity.attributeValue("mtcode");
						String mtContent = this.getMtContentByCode(mtCode);

						Document entityDoc = DocumentHelper
								.parseText(mtContent);
						Element entityEle = entityDoc.getRootElement();

						entities.remove(tempEntity);
						entities.add(entityEle);

						entitiesMap.put(entityCode, entityEle);

						Element rulesEle = entityEle.addElement("rules");
						Element ruleEle = getRuleElement(entityModelCode,
								entityCode, ruleCode);
						if (ruleEle != null) {
							rulesEle.add(ruleEle);
							ruleMap.put(entityCode + "_" + ruleCode, ruleEle);
						}
						// Element rulesNode = tempEntity.element("rules");
						// if(rulesNode != null ){
						// List ruleList = rulesNode.elements("rule");
						// if(ruleList != null){
						// for(Object item : ruleList){
						// Element rule = (Element) item;
						// ruleCode = rule.attributeValue("code");
						// ruleMap.put(entityCode+"_"+ruleCode, rule);
						// }
						// }
						// }
					}
				}

				modelMap.put(entityModelCode, doc.asXML());

				Element paramsEle = root.element("parameters");
				List paramList = paramsEle.elements();
				if (paramList != null) {
					for (Object param : paramList) {
						Element paramEle = (Element) param;
						String tempName = paramEle.attributeValue("name");
						paramsMap.put(tempName, paramEle);
					}
				}

				flag = true;
			}
		} catch (Exception e) {
			logger.debug("", e);
			flag = false;
			throw new Exception("实体无法初始化:" + e.getMessage());
		}
		log("初始化元数据结束....", "");
		return flag;
	}

	private String getMtContentByCode(String mtCode) {
		CommDMO dmo = new CommDMO();
		String mtContent = "";
		try {
			String sql = "select mt.content from pub_metadata_templet mt where mt.code=?";
			HashVO[] vos = dmo.getHashVoArrayByDS(
					DatabaseConst.DATASOURCE_DEFAULT, sql, mtCode);
			if (vos != null && vos.length > 0) {
				HashVO vo = vos[0];
				mtContent = vo.getStringValue("content");
			}
		} catch (Exception e) {
			logger.debug("", e);
		} finally {
			dmo.releaseContext(DatabaseConst.DATASOURCE_DEFAULT);
		}
		return mtContent;
	}

	private Element getRuleElement(String modelCode, String entityCode,
			String ruleCode) {
		CommDMO dmo = new CommDMO();
		Element ele = null;
		try {
			String sql = "select t.mtcode,t.name,t.code from dq_entity_rule t where t.entitymodelcode=? and t.entitycode=? and t.code=?";
			HashVO[] vos = dmo.getHashVoArrayByDS(DatabaseConst.DS_SMARTDQ,
					sql, modelCode, entityCode, ruleCode);
			if (vos != null && vos.length > 0) {
				HashVO vo = vos[0];
				String mtCode = vo.getStringValue("mtcode");

				String name = vo.getStringValue("name");
				String code = vo.getStringValue("code");

				sql = "select mt.content from pub_metadata_templet mt where mt.code=?";
				vos = dmo.getHashVoArrayByDS(DatabaseConst.DATASOURCE_DEFAULT,
						sql, mtCode);
				if (vos != null && vos.length > 0) {
					String content = vos[0].getStringValue("content");
					if (!StringUtil.isEmpty(content)) {
						Document doc = DocumentHelper.parseText(content);
						ele = doc.getRootElement();
						ele.addAttribute("name", name);
						ele.addAttribute("code", code);
					}
				}
			}
		} catch (Exception e) {
			logger.debug("", e);
		} finally {
			dmo.releaseContext(DatabaseConst.DS_SMARTDQ);
			dmo.releaseContext(DatabaseConst.DATASOURCE_DEFAULT);
		}
		return ele;
	}

	public Map<String, Object> analyseEntityRule(Map[] rules) throws Exception {
		CommDMO dmo = new CommDMO();
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			HashVO[] vos = null;
			SimpleHashVO[] simpleVos = new SimpleHashVO[rules.length];
			int index = 0;
			for (Map rule : rules) {

				String ruleCode = (String) rule.get("ruleCode");
				String ruleName = (String) rule.get("ruleName");
				String entityCode = (String) rule.get("entityCode");
				String modelCode = (String) rule.get("modelCode");
				String groupColumnName = (String) rule.get("groupColumnName");
				String groupColumnValue = (String) rule.get("groupColumnValue");
				String datasource = (String) rule.get("datasource");

				String info = modelCode + ";" + entityCode + ";" + ruleCode
						+ ";" + groupColumnName + ";" + groupColumnValue + ";"
						+ datasource;

				String groupFilter = " where 1=1 ";

				if (!StringUtil.isEmpty(groupColumnName)) {
					if (!StringUtil.isEmpty(groupColumnValue)
							&& !groupColumnValue.equalsIgnoreCase("-1")
							&& !groupColumnValue.equalsIgnoreCase("all")
							&& !groupColumnValue.equalsIgnoreCase("全部")
							&& !groupColumnValue.equalsIgnoreCase("IGNORE")) {
						groupFilter = groupFilter + " AND " + groupColumnName
								+ "='" + groupColumnValue + "'";
					}
				}

				try {

					int totalCount = 0;
					String totalCountSQL = emm
							.generateEntitySqlOriginalDataByMtCode(
									SysConst.EN_METADATA_PREFIX + modelCode
											+ "_" + entityCode,
									SysConst.EM_METADATA_PREFIX + modelCode,
									null, DatabaseConst.DATASOURCE_DEFAULT);
					vos = dmo.getHashVoArrayByDSUnlimitRows(datasource,
							"select count(*) totalcount from ( "
									+ totalCountSQL + ") " + groupFilter);
					if (vos != null && vos.length > 0) {
						totalCount = vos[0].getIntegerValue("totalcount");
					}

					String tempSQL = emm.generateEntitySqlOriginalDataByMtCode(
							SysConst.EN_METADATA_PREFIX + modelCode + "_"
									+ entityCode, SysConst.EM_METADATA_PREFIX
									+ modelCode,
							SysConst.EN_RULE_METADATA_PREFIX + ruleCode,
							DatabaseConst.DATASOURCE_DEFAULT);
					vos = dmo.getHashVoArrayByDS(datasource,
							"select count(*) rowcount from ( " + tempSQL + ") "
									+ groupFilter);
					int rowCount = 0;
					if (vos != null && vos.length > 0) {
						rowCount = vos[0].getIntegerValue("rowcount");
					}

					HashVO tempVo = new HashVO();
					tempVo.setAttributeValue("rowcount", rowCount);
					tempVo.setAttributeValue("totalcount", totalCount);
					tempVo.setAttributeValue("rowname", ruleName);
					tempVo.setAttributeValue("info", info);

					SimpleHashVO vo = new SimpleHashVO(tempVo);
					simpleVos[index] = vo;
					index++;

				} catch (Exception ex) {
					logger.debug("", ex);
				} finally {
					dmo.releaseContext(datasource);
				}

			}

			result.put(DMOConst.SIMPLEHASHVOARRAY, simpleVos);

		} catch (Exception e) {
			logger.error("getSimpleHashVOArrayByPage 错误！", e);
			throw e;
		} finally {

		}
		return result;
	}

	public Map<String, Object> analyseHisEntityRule(Map[] rules)
			throws Exception {
		CommDMO dmo = new CommDMO();
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			HashVO[] vos = null;
			SimpleHashVO[] simpleVos = null;
			StringBuffer querySql = new StringBuffer("");

			for (Map rule : rules) {

				String ruleCode = (String) rule.get("ruleCode");
				String entityCode = (String) rule.get("entityCode");
				String modelCode = (String) rule.get("modelCode");
				String groupColumnName = (String) rule.get("groupColumnName");
				String groupColumnValue = (String) rule.get("groupColumnValue");
				String startTime = (String) rule.get("startTime");
				String endTime = (String) rule.get("endTime");
				String limitCount = ((Integer) rule.get("limitCount"))
						.toString();
				
				String type = (String)rule.get("type");

				String info = groupColumnName + ";" + groupColumnValue;

				String groupColumnStr = "";
				String groupColumnValueStr = "";

				if (!StringUtil.isEmpty(groupColumnName)) {
					groupColumnStr = " and groupcolumn = '" + groupColumnName
							+ "' ";
				}

				if (!StringUtil.isEmpty(groupColumnValue)
						&& !groupColumnValue.equalsIgnoreCase("-1")
						&& !groupColumnValue.equalsIgnoreCase("all")
						&& !groupColumnValue.equalsIgnoreCase("全部")
						&& !groupColumnValue.equalsIgnoreCase("IGNORE")) {
					groupColumnValueStr = " and columnvalue = '"
							+ groupColumnValue + "' ";
				}

				String sql = "";
				if(type != null && type.equalsIgnoreCase(this.HIS_TYPE_DAY)){
					if (!StringUtil.isEmpty(groupColumnValueStr)) {
						sql = "select t.rulename,substr(t.batchno,1,8) batchno,batchno version,sum(g.rowcount) rowcount,sum(g.totalcount) totalcount,t.id||';"
								+ info
								+ ";'||m.detailtablename info from dq_entity_statistics t, dq_entity_statistics_group g,dq_entity_statistics_map m where t.id = g.statisticsid and t.mapid = m.id "
								+ " and t.entitymodelcode = '"
								+ modelCode
								+ "' "
								+ " and t.entitycode = '"
								+ entityCode
								+ "' "
								+ " and t.rulecode = '"
								+ ruleCode
								+ "' "
								+ " "
								+ groupColumnStr
								+ " "
								+ " "
								+ groupColumnValueStr
								+ " "
								+ " and t.createtime>to_date('"
								+ startTime
								+ "','yyyy-MM-DD') "
								+ " and t.createtime<=to_date('"
								+ endTime
								+ "','yyyy-MM-DD') "
								+ " group by rulename,substr(batchno, 1, 8),batchno,t.id,m.detailtablename ";
					} else {
	
						sql = "select t.rulename,substr(t.batchno,1,8) batchno,batchno version,t.rowcount,t.totalcount,t.id||';"
								+ info
								+ ";'||m.detailtablename info from dq_entity_statistics t,dq_entity_statistics_map m where t.mapid = m.id "
								+ " and t.entitymodelcode = '"
								+ modelCode
								+ "' "
								+ " and t.entitycode = '"
								+ entityCode
								+ "' "
								+ " and t.rulecode = '"
								+ ruleCode
								+ "' "
								+ " and t.createtime>to_date('"
								+ startTime
								+ "','yyyy-MM-DD') "
								+ " and t.createtime<=to_date('"
								+ endTime
								+ "','yyyy-MM-DD') ";
						// " group by rulename,substr(batchno, 1, 8),batchno,t.id,m.detailtablename ";
	
					}
				}else if(type != null && type.equalsIgnoreCase(this.HIS_TYPE_MONTH)){
					String[] startDates = startTime.split("-");
					String[] endDates = endTime.split("-");
					if(!StringUtil.isEmpty(groupColumnValueStr)){
						
						sql = "select t.rulename,substr(t.batchno, 1, 6) batchno,substr(t.batchno, 1, 6) version, round(avg(g.rowcount)) rowcount, round(avg(g.totalcount)) totalcount, '' info " +
						"from dq_entity_statistics t,dq_entity_statistics_group g where 1=1 and t.id=g.statisticsid " +
						"and t.entitymodelcode = '"+modelCode+"' " +
						"and t.entitycode = '"+entityCode+"' " +
						"and t.rulecode = '"+ruleCode+"' " +
						groupColumnStr+" "+groupColumnValueStr+" "+
						"and t.createtime>to_date('"+startDates[0]+"-"+startDates[1]+"','YYYY-MM') " +
						"and t.createtime<= to_date('"+endDates[0]+"-"+endDates[1]+"','YYYY-MM') " +
						"group by t.rulename, substr(batchno, 1, 6)";
						
					}else{
						sql = "select t.rulename,substr(t.batchno, 1, 6) batchno,substr(t.batchno, 1, 6) version, round(avg(t.rowcount)) rowcount, round(avg(t.totalcount)) totalcount, '' info " +
								"from dq_entity_statistics t where 1=1 " +
								"and t.entitymodelcode = '"+modelCode+"' " +
								"and t.entitycode = '"+entityCode+"' " +
								"and t.rulecode = '"+ruleCode+"' " +
								"and t.createtime>to_date('"+startDates[0]+"-"+startDates[1]+"','YYYY-MM') " +
								"and t.createtime<= to_date('"+endDates[0]+"-"+endDates[1]+"','YYYY-MM') " +
								"group by t.rulename, substr(batchno, 1, 6)";
					}
					
				}else if(type != null && type.equalsIgnoreCase(this.HIS_TYPE_YEAR)){
					String[] startDates = startTime.split("-");
					String[] endDates = endTime.split("-");
					if(!StringUtil.isEmpty(groupColumnValueStr)){
						sql = "select t.rulename,substr(t.batchno, 1, 4) batchno,substr(t.batchno, 1, 4) version, round(avg(g.rowcount)) rowcount, round(avg(g.totalcount)) totalcount, '' info " +
						"from dq_entity_statistics t,dq_entity_statistics_group g where 1=1 and t.id=g.statisticsid " +
						"and t.entitymodelcode = '"+modelCode+"' " +
						"and t.entitycode = '"+entityCode+"' " +
						"and t.rulecode = '"+ruleCode+"' " +
						groupColumnStr+" "+groupColumnValueStr+" "+
						"and t.createtime>to_date('"+startDates[0]+"-01-01"+"','YYYY-MM-DD') " +
						"and t.createtime<= to_date('"+endDates[0]+"-12-31"+"','YYYY-MM-DD') " +
						"group by t.rulename, substr(batchno, 1, 4)";
					}else{
						sql = "select t.rulename,substr(t.batchno, 1, 4) batchno,substr(t.batchno, 1, 4) version, round(avg(t.rowcount)) rowcount, round(avg(t.totalcount)) totalcount, '' info " +
						"from dq_entity_statistics t where 1=1 " +
						"and t.entitymodelcode = '"+modelCode+"' " +
						"and t.entitycode = '"+entityCode+"' " +
						"and t.rulecode = '"+ruleCode+"' " +
						"and t.createtime>to_date('"+startDates[0]+"-01-01"+"','YYYY-MM-DD') " +
						"and t.createtime<= to_date('"+endDates[0]+"-12-31"+"','YYYY-MM-DD') " +
						"group by t.rulename, substr(batchno, 1, 4)";
					}
					
				}

				sql = "select rulename,batchno,version,rowcount,totalcount,info from ("
						+ sql + ") where rownum<=" + limitCount;

				if (querySql.toString().equalsIgnoreCase("")) {
					querySql.append(sql);
				} else {
					querySql.append(" union ");
					querySql.append(sql);
				}
			}

			// querySql.append(" order by batchno asc");

			String tempSql = "select rulename,batchno,version,rowcount,totalcount,info from ("
					+ querySql.toString() + ") order by batchno asc";

			vos = dmo.getHashVoArrayByDS(DatabaseConst.DS_SMARTDQ, tempSql);
			if (vos != null && vos.length > 0) {
				simpleVos = new SimpleHashVO[vos.length];
				for (int i = 0; i < vos.length; i++) {
					HashVO tempVo = vos[i];
					SimpleHashVO vo = new SimpleHashVO(tempVo);
					simpleVos[i] = vo;
				}
			}

			result.put(DMOConst.SIMPLEHASHVOARRAY, simpleVos);

		} catch (Exception e) {
			logger.error("getSimpleHashVOArrayByPage 错误！", e);
			throw e;
		} finally {
			dmo.releaseContext(DatabaseConst.DS_SMARTDQ);
		}
		return result;
	}

	public Map<String, Object> analyseHisEntityByBatchNo(Map[] rules)
			throws Exception {
		CommDMO dmo = new CommDMO();
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			HashVO[] vos = null;
			SimpleHashVO[] simpleVos = null;
			StringBuffer querySql = new StringBuffer("");

			for (Map rule : rules) {

				String ruleCode = (String) rule.get("ruleCode");
				String entityCode = (String) rule.get("entityCode");
				String modelCode = (String) rule.get("modelCode");
				String groupColumnName = (String) rule.get("groupColumnName");
				String groupColumnValue = (String) rule.get("groupColumnValue");
				String batchNo = (String) rule.get("batchNo");
				String taskName = (String)rule.get("taskName");

				String info = groupColumnName + ";" + groupColumnValue;

				String groupColumnStr = "";
				String groupColumnValueStr = "";

				if (!StringUtil.isEmpty(groupColumnName)) {
					groupColumnStr = " and groupcolumn = '" + groupColumnName
							+ "' ";
				}

				if (!StringUtil.isEmpty(groupColumnValue)
						&& !groupColumnValue.equalsIgnoreCase("-1")
						&& !groupColumnValue.equalsIgnoreCase("all")
						&& !groupColumnValue.equalsIgnoreCase("全部")
						&& !groupColumnValue.equalsIgnoreCase("IGNORE")) {
					groupColumnValueStr = " and columnvalue = '"
							+ groupColumnValue + "' ";
				}

				String sql = "";
				if (!StringUtil.isEmpty(groupColumnName)) {
					sql = "select t.rulename,substr(t.batchno,1,8) batchno,batchno version,sum(g.rowcount) rowcount,sum(g.totalcount) totalcount,t.id||';"
							+ info
							+ ";'||m.detailtablename info from dq_entity_statistics t, dq_entity_statistics_group g,dq_entity_statistics_map m where t.id = g.statisticsid and t.mapid = m.id "
							+ " and t.entitymodelcode = '"
							+ modelCode
							+ "' "
							+ " and t.entitycode = '"
							+ entityCode
							+ "' "
							+ " and t.rulecode = '"
							+ ruleCode
							+ "' "
							+ " and t.batchno = '"
							+ batchNo
							+ "'"
							+ " and t.taskname='"+taskName+"' "
							+ groupColumnStr
							+ " "
							+ " "
							+ groupColumnValueStr
							+ " "
							+ " group by rulename,substr(batchno, 1, 8),batchno,t.id,m.detailtablename ";

				} else {

					sql = "select t.rulename,substr(t.batchno,1,8) batchno,batchno version,t.rowcount,t.totalcount,t.id||';"
							+ info
							+ ";'||m.detailtablename info from dq_entity_statistics t,dq_entity_statistics_map m where t.mapid = m.id "
							+ " and t.entitymodelcode = '"
							+ modelCode
							+ "' "
							+ " and t.entitycode = '"
							+ entityCode
							+ "' "
							+ " and t.rulecode = '"
							+ ruleCode
							+ "' "
							+ " and t.batchno = '" + batchNo + "' and t.taskname='"+taskName+"'";
					// " group by rulename,substr(batchno, 1, 8),batchno,t.id,m.detailtablename ";

				}

				sql = "select rulename,batchno,version,rowcount,totalcount,info from ("
						+ sql + ")";

				if (querySql.toString().equalsIgnoreCase("")) {
					querySql.append(sql);
				} else {
					querySql.append(" union ");
					querySql.append(sql);
				}
			}

			// querySql.append(" order by batchno asc");

			String tempSql = "select rulename,batchno,version,rowcount,totalcount,info from ("
					+ querySql.toString() + ") order by batchno asc";

			vos = dmo.getHashVoArrayByDS(DatabaseConst.DS_SMARTDQ, tempSql);
			if (vos != null && vos.length > 0) {
				simpleVos = new SimpleHashVO[vos.length];
				for (int i = 0; i < vos.length; i++) {
					HashVO tempVo = vos[i];
					SimpleHashVO vo = new SimpleHashVO(tempVo);
					simpleVos[i] = vo;
				}
			}

			result.put(DMOConst.SIMPLEHASHVOARRAY, simpleVos);

		} catch (Exception e) {
			logger.error("getSimpleHashVOArrayByPage 错误！", e);
			throw e;
		} finally {
			dmo.releaseContext(DatabaseConst.DS_SMARTDQ);
		}
		return result;
	}

	public Map<String, Object> analyseEntity(Map[] rules) throws Exception {
		CommDMO dmo = new CommDMO();
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			HashVO[] vos = null;
			SimpleHashVO[] simpleVos = null;
			StringBuffer querySql = new StringBuffer("");
			for (Map rule : rules) {

				String ruleCode = (String) rule.get("ruleCode");
				String entityCode = (String) rule.get("entityCode");
				String modelCode = (String) rule.get("modelCode");
				String staticsTime = (String) rule.get("staticsTime");

				String sql = "select t.batchno,t.rulename, g.columnlabel, g.rowcount, g.totalcount,t.id||';'||g.groupcolumn||';'||g.columnvalue||';'||m.detailtablename info from dq_entity_statistics t, dq_entity_statistics_group g,dq_entity_statistics_map m where t.id = g.statisticsid and t.mapid = m.id "
						+ " and t.entitymodelcode = '"
						+ modelCode
						+ "' "
						+ " and t.entitycode = '"
						+ entityCode
						+ "' "
						+ " and t.rulecode = '"
						+ ruleCode
						+ "' "
						+ " and t.createtime=to_date('"
						+ staticsTime
						+ "','yyyy-MM-DD hh24:mi:ss') ";

				if (querySql.toString().equalsIgnoreCase("")) {
					querySql.append(sql);
				} else {
					querySql.append(" union ");
					querySql.append(sql);
				}
			}

			querySql.append("order by columnlabel asc");

			vos = dmo.getHashVoArrayByDS(DatabaseConst.DS_SMARTDQ,
					querySql.toString());
			if (vos != null && vos.length > 0) {
				simpleVos = new SimpleHashVO[vos.length];
				for (int i = 0; i < vos.length; i++) {
					SimpleHashVO vo = new SimpleHashVO(vos[i]);
					simpleVos[i] = vo;
				}
			}

			result.put(DMOConst.SIMPLEHASHVOARRAY, simpleVos);

		} catch (Exception e) {
			logger.error("getSimpleHashVOArrayByPage 错误！", e);
			throw e;
		} finally {
			dmo.releaseContext(DatabaseConst.DS_SMARTDQ);
		}
		return result;
	}

	public Map<String, Object> analyseEntityByBatchNo(Map[] rules)
			throws Exception {
		CommDMO dmo = new CommDMO();
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			HashVO[] vos = null;
			SimpleHashVO[] simpleVos = null;
			StringBuffer querySql = new StringBuffer("");
			for (Map rule : rules) {

				String ruleCode = (String) rule.get("ruleCode");
				String entityCode = (String) rule.get("entityCode");
				String modelCode = (String) rule.get("modelCode");
				String batchNo = (String) rule.get("batchNo");
				String taskName = (String)rule.get("taskName");

				String sql = "select t.batchno,t.rulename, g.columnlabel, g.rowcount, g.totalcount,t.id||';'||g.groupcolumn||';'||g.columnvalue||';'||m.detailtablename info from dq_entity_statistics t, dq_entity_statistics_group g,dq_entity_statistics_map m where t.id = g.statisticsid and t.mapid = m.id "
						+ " and t.entitymodelcode = '"
						+ modelCode
						+ "' "
						+ " and t.entitycode = '"
						+ entityCode
						+ "' "
						+ " and t.rulecode = '"
						+ ruleCode
						+ "' "
						+ " and t.batchno='" + batchNo + "' and t.taskname='"+taskName+"'";

				if (querySql.toString().equalsIgnoreCase("")) {
					querySql.append(sql);
				} else {
					querySql.append(" union ");
					querySql.append(sql);
				}
			}

			querySql.append("order by columnlabel asc");

			vos = dmo.getHashVoArrayByDS(DatabaseConst.DS_SMARTDQ,
					querySql.toString());
			if (vos != null && vos.length > 0) {
				simpleVos = new SimpleHashVO[vos.length];
				for (int i = 0; i < vos.length; i++) {
					SimpleHashVO vo = new SimpleHashVO(vos[i]);
					simpleVos[i] = vo;
				}
			}

			result.put(DMOConst.SIMPLEHASHVOARRAY, simpleVos);

		} catch (Exception e) {
			logger.error("getSimpleHashVOArrayByPage 错误！", e);
			throw e;
		} finally {
			dmo.releaseContext(DatabaseConst.DS_SMARTDQ);
		}
		return result;
	}

	private String getGroupLable(String columnName, String columnValue,
			String entityCode) {
		String label = columnValue;
		try {
			Document doc = DocumentHelper.parseText(this
					.getEntityToString(entityCode));
			String paramName = "";
			Element entityRoot = doc.getRootElement();
			Element attEle = entityRoot.element("groupAttributes");
			List list = attEle.elements();
			if (list != null) {
				for (Object attItem : list) {
					Element e = (Element) attItem;
					String tempColumnname = e.attributeValue("name");
					if (!StringUtil.isEmpty(tempColumnname)
							&& tempColumnname.equalsIgnoreCase(columnName)) {
						paramName = e.attributeValue("parameter");
						break;
					}
				}
			}
			if (!StringUtil.isEmpty(paramName)) {
				Element paraEle = paramsMap.get(paramName);
				String controlType = paraEle.attributeValue("controlType");
				if (controlType.equalsIgnoreCase("comboBox")) {
					String isUseSynonyms = paraEle
							.attributeValue("isUseSynonyms");
					String defineCbo = paraEle.attributeValue("defineCbo");
					if (isUseSynonyms.equalsIgnoreCase("Y")) {
						String synCode = defineCbo.split("@@@")[1];
						MetadataTemplet metadata = SmartXMetadataTempletService
								.getInstance().findMetadataTemplet(
										"MT_SYNONYMS");
						Document synDoc = DocumentHelper.parseText(metadata
								.getContent());
						Element synRoot = synDoc.getRootElement();
						List synList = synRoot.elements();
						if (synList != null) {
							Element synEle = null;
							for (Object synObj : synList) {
								synEle = (Element) synObj;
								String tempCode = synEle.attributeValue("code");
								if (!StringUtil.isEmpty(tempCode)
										&& tempCode.equalsIgnoreCase(synCode)) {
									break;
								}
							}
							if (synEle != null) {
								List varList = synEle.elements();
								if (varList != null) {
									for (Object varItem : varList) {
										Element varEle = (Element) varItem;
										String value = varEle
												.attributeValue("value");
										String name = varEle
												.attributeValue("name");
										if (!StringUtil.isEmpty(value)
												&& value.equalsIgnoreCase(columnValue)) {
											label = name;
											break;
										}
									}
								}
							}
						}
					} else {
						if (!StringUtil.isEmpty(defineCbo)) {
							HashVO[] vos = this.getHashVOS(defineCbo);
							if (vos != null) {
								for (HashVO vo : vos) {
									if (vo.getStringValue(0).equalsIgnoreCase(
											columnValue)) {
										label = vo.getStringValue(2);
										break;
									}
								}
							}
						}

					}
				} else if (controlType.equalsIgnoreCase("refPanel")) {
					String refDefinition = paraEle
							.attributeValue("refDefinition");
					if (!StringUtil.isEmpty(refDefinition)) {
						HashVO[] vos = this.getHashVOS(refDefinition);
						if (vos != null) {
							for (HashVO vo : vos) {
								if (vo.getStringValue(0).equalsIgnoreCase(
										columnValue)) {
									label = vo.getStringValue(2);
									break;
								}
							}
						}
					}
				} else {
					label = columnValue;
				}
			}
		} catch (Exception e) {
			logger.debug("", e);
		}
		return label;
	}

	private HashVO[] getHashVOS(String sql) {
		CommDMO dmo = new CommDMO();
		HashVO[] vos = null;
		try {
			String ds = null;
			String querySql = "";
			String[] info = sql.split(";");
			querySql = info[0];
			if (info.length == 2) {
				ds = StringUtil.trim(info[1].split("=")[1]);
			}
			vos = dmo.getHashVoArrayByDS(ds, querySql);

		} catch (Exception e) {
			logger.debug("", e);
		}
		return vos;
	}

	private void log(String msg, String detail) {
		if (mainThread != null) {
			mainThread.logTaskRun(msg, detail);
		}
	}

}
