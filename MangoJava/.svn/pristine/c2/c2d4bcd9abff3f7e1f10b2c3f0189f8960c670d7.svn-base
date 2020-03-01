/**
 * 
 */
package smartx.bam.bs.entitymodel;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
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
import smartx.publics.metadata.bs.service.SmartXMetadataTempletService;
import smartx.publics.metadata.vo.MetadataTemplet;

/**
 * @author caohenghui
 * Dec 15, 2011
 */
public class EntityDimensionService {

	
	protected Logger logger = NovaLogger.getLogger(this.getClass());
	
	private final String ENTITY_DIMENSION = "EntityDimension";//实体维度
	private final String TIME_DIMENSION = "TimeDimension";//时间维度
	private final String INNER_DIMENSION = "InnerDimension";//内部维度
	private final String SYNONYMS_DIMENSION = "SynonymsDimension";//同义词维度
	
	private final String DW_DIMENSION_YEAR = "DW_DIMENSION_YEAR"; // 年
	private final String DW_DIMENSION_YEARMONTH = "DW_DIMENSION_YEARMONTH"; // 年月
	private final String DW_DIMENSION_YEARMONTHDAY = "DW_DIMENSION_YEARMONTHDAY"; // 年月日
	private final String DW_DIMENSION_YEARMONTHDAYHOUR = "DW_DIMENSION_YEARMONTHDAYHOUR";// 年月日时
	private final String DW_DIMENSION_SYNONYMS = "DW_DIMENSION_SYNONYMS";// 同义词

	private final String S_DW_DIMENSION_YEAR = "S_DW_DIMENSION_YEAR";
	private final String S_DW_DIMENSION_YEARMONTH = "S_DW_DIMENSION_YEARMONTH";
	private final String S_DW_DIMENSION_YEARMONTHDAY = "S_DW_DIMENSION_YEARMONTHDAY";
	private final String S_DW_DIMENSION_YEARMONTHDAYHOUR = "S_DW_DIMENSION_YMDH";
	
	private final String S_DW_DIMENSION_SYNONYMS = "S_DW_DIMENSION_SYNONYMS";
	
	private final String CREATE_TIMEDIMENSION_TABLE_SQL_YEAR ="create table DW_DIMENSION_YEAR( ID NUMBER(18) not null, YEARNAME VARCHAR2(200))";
	private final String CREATE_TIMEDIMENSION_PRIMARY_SQL_YEAR ="alter table DW_DIMENSION_YEAR add constraint PK_DW_DIMENSION_YEAR primary key (ID) using index";
	private final String CREATE_TIMEDIMENSION_SEQUENCE_SQL_YEAR = "create sequence S_DW_DIMENSION_YEAR minvalue 1 start with 1 increment by 1 nomaxvalue nocycle cache 20";
	
	private final String CREATE_TIMEDIMENSION_TABLE_SQL_YEARMONTH ="create table DW_DIMENSION_YEARMONTH (ID NUMBER(18) not null,YEARNAME  VARCHAR2(200),MONTHNAME VARCHAR2(200))";
	private final String CREATE_TIMEDIMENSION_PRIMARY_SQL_YEARMONTH = "alter table DW_DIMENSION_YEARMONTH add constraint PK_DW_DIMENSION_YM primary key (ID) using index";
	private final String CREATE_TIMEDIMENSION_SEQUENCE_SQL_YEARMONTH = "create sequence S_DW_DIMENSION_YEARMONTH minvalue 1 start with 1 increment by 1 nomaxvalue nocycle cache 20";
	
	private final String CREATE_TIMEDIMENSION_TABLE_SQL_YEARMONTHDAY ="create table DW_DIMENSION_YEARMONTHDAY ( ID NUMBER(18) not null, YEARNAME  VARCHAR2(200), MONTHNAME VARCHAR2(200), DAYNAME   VARCHAR2(200))";
	private final String CREATE_TIMEDIMENSION_PRIMARY_SQL_YEARMONTHDAY = "alter table DW_DIMENSION_YEARMONTHDAY add constraint PK_DW_DIMENSION_YMD primary key (ID) using index";
	private final String CREATE_TIMEDIMENSION_SEQUENCE_SQL_YEARMONTHDAY = "create sequence S_DW_DIMENSION_YEARMONTHDAY minvalue 1 start with 1 increment by 1 nomaxvalue nocycle cache 20";
	
	private final String CREATE_TIMEDIMENSION_TABLE_SQL_YEARMONTHDAYHOUR ="create table DW_DIMENSION_YEARMONTHDAYHOUR (ID NUMBER(18) not null, YEARNAME  VARCHAR2(200), MONTHNAME VARCHAR2(200), DAYNAME   VARCHAR2(200), HOURNAME  VARCHAR2(200) )";
	private final String CREATE_TIMEDIMENSION_PRIMARY_SQL_YEARMONTHDAYHOUR = "alter table DW_DIMENSION_YEARMONTHDAYHOUR add constraint PK_DW_DIMENSION_YMDH primary key (ID) using index";
	private final String CREATE_TIMEDIMENSION_SEQUENCE_SQL_YEARMONTHDAYHOUR = "create sequence S_DW_DIMENSION_YMDH minvalue 1 start with 1 increment by 1 nomaxvalue nocycle cache 20";
	
	private final String CREATE_DIMENSION_TABLE_NAME_SEQUENCE_SQL = "create sequence S_DW_DIMENSION_TABLE minvalue 1 start with 1 increment by 1 nomaxvalue nocycle cache 20";
	
	private final String MT_SYNONYMS = "MT_SYNONYMS";

	private Map<String,Element> modelMap =  new HashMap<String,Element>();
	private Map<String,Element> entitiesMap = new HashMap<String,Element>();
	private Map<String,Element> dimensionMap = new HashMap<String,Element>();
	
	private EntityModelManager emm = new EntityModelManager();
	private SmartXMetadataTempletService smts = new SmartXMetadataTempletService();
	
	public String dealDimension(String modelContent,String modelCode,String dimensionCode) throws Exception {
		String tableName ="";
		if(!StringUtil.isEmpty(modelContent)){
			if(this.initEntitiesInfo(modelContent,modelCode)){
				String dqc_datasource = getDatasource(modelContent);
				String dw_datasource = getDWDatasource(modelContent);
//				Set<String> keySet = dimensionMap.keySet();
//				for(String key : keySet ){
					Element dimensionEle = dimensionMap.get(dimensionCode);
					String dimensionType = dimensionEle.attributeValue("type");
					
					//如果是实体类型的维度
					if(dimensionType.equalsIgnoreCase(this.ENTITY_DIMENSION)){
						tableName = this.dealEntityDimension(modelCode,dimensionEle,dqc_datasource,dw_datasource);
					}else if(dimensionType.equalsIgnoreCase(this.TIME_DIMENSION)){
						this.dealTimeDimension(dimensionEle,dw_datasource);
					}else if(dimensionType.equalsIgnoreCase(this.SYNONYMS_DIMENSION)){
						tableName = this.dealSynonymsDimension(modelCode,dimensionEle,dqc_datasource,dw_datasource);
					}
//				}
			}else{
				logger.debug("无法初始化模型元数据!");
			}
			
		}
		return tableName;
	}
	
	private String dealEntityDimension(String modelCode,Element dimensionEle,String dqc_datasource,String dw_datasource){
		
		Element tableEle = dimensionEle.element("Hierarchy").element("Table");
		String oldTable = tableEle.attributeValue("name");
		if(!StringUtil.isEmpty(oldTable)){
			this.dropSequence(dw_datasource, "S_"+oldTable);
		}
		
		String dimensiionTableName = createEntityDimensionTable(dimensionEle.asXML(), dw_datasource);
//		if(!isExistTable(dimensiionTableName, dw_datasource)){
//			dimensiionTableName = createEntityDimensionTable(tableEle.asXML(), dw_datasource);
//			if(StringUtil.isEmpty(dimensiionTableName)){
//				this.logger.debug("无法获取实体维度表,无抽进行实体维度抽取!");
//				return;
//			}
//			dimensionEle.element("Hierarchy").element("Table").addAttribute("name", dimensiionTableName);
//			
//			updateMtContent("MT_EM_"+modelCode,this.modelMap.get(modelCode).asXML());
//		}
		if(StringUtil.isEmpty(dimensiionTableName)){
			this.logger.debug("无法获取实体维度表,无抽进行实体维度抽取!");
			return null;
		}
		dimensionEle.element("Hierarchy").element("Table").addAttribute("name", dimensiionTableName.toUpperCase());
		
		updateMtContent("MT_EM_"+modelCode,this.modelMap.get(modelCode).asXML());
		
		String dimensionEntityCode = dimensionEle.attributeValue("entityCode");
		Element entityEle = entitiesMap.get(dimensionEntityCode);
		if(entityEle != null){
			insertIntoEntityDimensionTable(modelCode,dimensionEntityCode,dimensionEle,dimensiionTableName,dqc_datasource,dw_datasource);
		}
		return dimensiionTableName;
	}
	
	private void dealTimeDimension(Element dimensionEle,String dw_datasource){
		try{
			
			String yearStr = Calendar.getInstance().get(Calendar.YEAR)+"";
			
			int month = Calendar.getInstance().get(Calendar.MONTH)+1;
			int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
			
			String monthStr = (month<10)?("0"+month):(month+"");
			String dayStr = (day<10)?("0"+day):(day+"");
			
			if(!isExistTable(this.DW_DIMENSION_YEAR, dw_datasource)){
				List<String> sqls = new ArrayList<String>();
				sqls.add(this.CREATE_TIMEDIMENSION_TABLE_SQL_YEAR);
				sqls.add(this.CREATE_TIMEDIMENSION_PRIMARY_SQL_YEAR);
				sqls.add(this.CREATE_TIMEDIMENSION_SEQUENCE_SQL_YEAR);
				this.dropSequence(dw_datasource, this.S_DW_DIMENSION_YEAR);
				this.executeDDL(dw_datasource, sqls);
			}
			
			if(!isExistTable(this.DW_DIMENSION_YEARMONTH, dw_datasource)){
				List<String> sqls = new ArrayList<String>();
				sqls.add(this.CREATE_TIMEDIMENSION_TABLE_SQL_YEARMONTH);
				sqls.add(this.CREATE_TIMEDIMENSION_PRIMARY_SQL_YEARMONTH);
				sqls.add(this.CREATE_TIMEDIMENSION_SEQUENCE_SQL_YEARMONTH);
				this.dropSequence(dw_datasource, this.S_DW_DIMENSION_YEARMONTH);
				this.executeDDL(dw_datasource, sqls);
			}
			
			if(!isExistTable(this.DW_DIMENSION_YEARMONTHDAY, dw_datasource)){
				List<String> sqls = new ArrayList<String>();
				sqls.add(this.CREATE_TIMEDIMENSION_TABLE_SQL_YEARMONTHDAY);
				sqls.add(this.CREATE_TIMEDIMENSION_PRIMARY_SQL_YEARMONTHDAY);
				sqls.add(this.CREATE_TIMEDIMENSION_SEQUENCE_SQL_YEARMONTHDAY);
				this.dropSequence(dw_datasource, this.S_DW_DIMENSION_YEARMONTHDAY);
				this.executeDDL(dw_datasource, sqls);
			}
			
			
			String yearCheck = "select count(1) cou from "+this.DW_DIMENSION_YEAR +" where YEARNAME='"+yearStr+"'";
			String yearMonthCheck = "select count(1) cou from "+this.DW_DIMENSION_YEARMONTH +" where YEARNAME='"+yearStr+"' and MONTHNAME='"+monthStr+"'";
			String yearMonthDayCheck = "select count(1) cou from "+this.DW_DIMENSION_YEARMONTHDAY +" where YEARNAME='"+yearStr+"' and MONTHNAME='"+monthStr+"' and DAYNAME='"+dayStr+"'";
			
			if(!this.isExistRecords(dw_datasource, yearCheck)){
				String insertYear = "insert into DW_DIMENSION_YEAR (ID,YEARNAME) values(S_DW_DIMENSION_YEAR.NEXTVAL,?)";
				this.insertTimeDimension(dw_datasource, insertYear, new Object[]{yearStr});
			}
			
			if(!this.isExistRecords(dw_datasource, yearMonthCheck)){
				String insertSql = "insert into DW_DIMENSION_YEARMONTH (ID,YEARNAME,MONTHNAME) values(S_DW_DIMENSION_YEARMONTH.NEXTVAL,?,?)";
				this.insertTimeDimension(dw_datasource, insertSql, new Object[]{yearStr,monthStr});
			}
			
			if(!this.isExistRecords(dw_datasource, yearMonthDayCheck)){
				String insertSql = "insert into DW_DIMENSION_YEARMONTHDAY (ID,YEARNAME,MONTHNAME,DAYNAME) values(S_DW_DIMENSION_YEARMONTHDAY.NEXTVAL,?,?,?)";
				this.insertTimeDimension(dw_datasource, insertSql, new Object[]{yearStr,monthStr,dayStr});
				
				//小时数据,暂时不用
//				for(int i=0; i<24; i++){
//					String hourStr = (i<10)?("0"+i):(i+"");
//					String insertHour = "insert into DW_DIMENSION_YEARMONTHDAYHOUR (ID,YEARNAME,MONTHNAME,DAYNAME,HOURNAME) values(S_DW_DIMENSION_YMDH.NEXTVAL,?,?,?,?)";
//					this.insertTimeDimension(dw_datasource, insertHour, new Object[]{yearStr,monthStr,dayStr,hourStr});
//				}
			}
			
			String startYear = dimensionEle.attributeValue("startyear");
			if(!StringUtil.isEmpty(startYear)&& StringUtil.isNumber(startYear)){
				int startYearInt = Integer.parseInt(startYear);
				insertHistroyTimeDimension(startYearInt,dw_datasource);
			}
			
			
		}catch(Exception e){
			this.logger.debug("",e);
		}
	}
	
	private void insertHistroyTimeDimension(int startYear,String dw_datasource){
		
		try{
			
			int endYear = Calendar.getInstance().get(Calendar.YEAR);
			
			if(startYear>1900 && startYear <= endYear){
			
				Calendar cd = Calendar.getInstance();
				
				cd.set(startYear,0,1);
				
				while(true){
					
					int year = cd.get(Calendar.YEAR);
					int month = cd.get(Calendar.MONTH)+1;
					int day = cd.get(Calendar.DAY_OF_MONTH);
					
					if(year == (endYear+1)){
						break;
					}
					
					String yearStr = year+"";
					
					String monthStr = (month<10)?("0"+month):(month+"");
					String dayStr = (day<10)?("0"+day):(day+"");
					
					String yearCheck = "select count(1) cou from "+this.DW_DIMENSION_YEAR +" where YEARNAME='"+yearStr+"'";
					String yearMonthCheck = "select count(1) cou from "+this.DW_DIMENSION_YEARMONTH +" where YEARNAME='"+yearStr+"' and MONTHNAME='"+monthStr+"'";
					String yearMonthDayCheck = "select count(1) cou from "+this.DW_DIMENSION_YEARMONTHDAY +" where YEARNAME='"+yearStr+"' and MONTHNAME='"+monthStr+"' and DAYNAME='"+dayStr+"'";
					
					if(!this.isExistRecords(dw_datasource, yearCheck)){
						String insertYear = "insert into DW_DIMENSION_YEAR (ID,YEARNAME) values(S_DW_DIMENSION_YEAR.NEXTVAL,?)";
						this.insertTimeDimension(dw_datasource, insertYear, new Object[]{yearStr});
					}
					
					if(!this.isExistRecords(dw_datasource, yearMonthCheck)){
						String insertSql = "insert into DW_DIMENSION_YEARMONTH (ID,YEARNAME,MONTHNAME) values(S_DW_DIMENSION_YEARMONTH.NEXTVAL,?,?)";
						this.insertTimeDimension(dw_datasource, insertSql, new Object[]{yearStr,monthStr});
					}
					
					if(!this.isExistRecords(dw_datasource, yearMonthDayCheck)){
						String insertSql = "insert into DW_DIMENSION_YEARMONTHDAY (ID,YEARNAME,MONTHNAME,DAYNAME) values(S_DW_DIMENSION_YEARMONTHDAY.NEXTVAL,?,?,?)";
						this.insertTimeDimension(dw_datasource, insertSql, new Object[]{yearStr,monthStr,dayStr});
					}
					
					cd.add(Calendar.DATE, 1);
				}
			}
			
		}catch(Exception e){
			
		}
	}
	
	private String dealSynonymsDimension(String modelCode,Element dimensionEle,String dqc_datasource,String dw_datasource){
		String dimensiionTableName = null;
		try{
			MetadataTemplet metadataTemplet = smts.findMetadataTemplet(this.MT_SYNONYMS);
			if(metadataTemplet != null){
				if(!StringUtil.isEmpty(metadataTemplet.getContent())){
					
					Document doc = DocumentHelper.parseText(metadataTemplet.getContent());
					
					Element tableEle = dimensionEle.element("Hierarchy").element("Table");
					String oldTable = tableEle.attributeValue("name");
					if(!StringUtil.isEmpty(oldTable)){
						this.dropSequence(dw_datasource, "S_"+oldTable);
					}
					dimensiionTableName = createSynonymsDimensionTable(dw_datasource);
//					if(!isExistTable(dimensiionTableName, dw_datasource)){
//						dimensiionTableName = createSynonymsDimensionTable(dw_datasource);
//						if(StringUtil.isEmpty(dimensiionTableName)){
//							this.logger.debug("无法创建同义词维度表,无法进行同义词维度抽取!");
//							return;
//						}
//						dimensionEle.element("Hierarchy").element("Table").addAttribute("name", dimensiionTableName);
//						updateMtContent("MT_EM_"+modelCode,this.modelMap.get(modelCode).asXML());
//					}
					
					if(StringUtil.isEmpty(dimensiionTableName)){
						this.logger.debug("无法获取实体维度表,无抽进行实体维度抽取!");
						return null;
					}
					
					dimensionEle.element("Hierarchy").element("Table").addAttribute("name", dimensiionTableName.toUpperCase());
					updateMtContent("MT_EM_"+modelCode,this.modelMap.get(modelCode).asXML());
					
					String synonymsCode = dimensionEle.attributeValue("synonymsCode");
					
					Element root = doc.getRootElement();
					
					Element synonymsEle = null;
					
					List list = root.elements();
					if(list != null ){
						for(Object obj : list){
							Element tempSynonymsEle = (Element)obj;
							String tempCode = tempSynonymsEle.attributeValue("code");
							if(tempCode.equalsIgnoreCase(synonymsCode)){
								synonymsEle = tempSynonymsEle;
								break;
							}
						}
					}
					
					if(synonymsEle != null ){
						this.insertIntoSynonymsDimensionTable(synonymsEle,dimensiionTableName, dw_datasource);
					}
				}
			}
			
		}catch(Exception e){
			this.logger.debug("",e);
		}
		return dimensiionTableName;
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
	
	public String createSynonymsDimensionTable(String dwdatasource){
		String tableName = "";
		CommDMO dmo = new CommDMO();
		try{
			
			List<String> sqls = new ArrayList<String>();
			
			if(!this.isExistSequence("S_DW_DIMENSION_TABLE", dwdatasource)){
				List<String> sequenceList = new ArrayList<String>();
				sequenceList.add(this.CREATE_DIMENSION_TABLE_NAME_SEQUENCE_SQL);
				this.executeDDL(dwdatasource, sequenceList);
			}
			
			String seq = dmo.getSequenceNextValByDS(dwdatasource, "S_DW_DIMENSION_TABLE");
			tableName = "DW_DIMENSION_SYNONYMS_"+seq;
			
			String buildTableSql = "create table "+tableName+" ";
			
			//为防止在创建表的时候不出现相同的列
			Map<String,String> columnsMap = new HashMap<String,String>();
			columnsMap.put("ID", "NUMBER");
			columnsMap.put("NAME", "VARCHAR2");
			columnsMap.put("VALUE", "VARCHAR2");
			
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
			logger.debug("创建同义词维度表失败!",e);
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
	
//	private void reBuildSequence(String datasource,String sequenceName){
//		CommDMO dmo = new CommDMO();
//		try{
//			List<String> sqls = new ArrayList<String>();
//			dropSequence(datasource,sequenceName);
//			
//			String sequenceSQL ="create sequence "+sequenceName+" minvalue 1 start with 1 increment by 1 nomaxvalue nocycle cache 20";
//			sqls.add(sequenceSQL);
//			
//			dmo.executeBatchByDS(datasource,sqls);
//			dmo.commit(datasource);
//			
//		}catch(Exception e){
//			try {
//				dmo.rollback(datasource);
//			} catch (Exception e1) {
//				logger.debug("", e1);
//			}finally{
//				dmo.releaseContext(datasource);
//			}
//		}
//	}
	
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
	
	private void updateMtContent(String mtCode,String mtContent){
		CommDMO dmo = new CommDMO();
		try{
			dmo.executeUpdateClobByDS(DatabaseConst.DATASOURCE_DEFAULT, "content", "pub_metadata_templet", " type=103 and code='"+mtCode+"'", mtContent);
			dmo.commit(DatabaseConst.DATASOURCE_DEFAULT);
		}catch(Exception e){
			logger.debug("",e);
		}finally{
			dmo.releaseContext(DatabaseConst.DATASOURCE_DEFAULT);
		}
	}
	
	private void dropSequence(String datasource,String sequenceName){
		CommDMO dmo = new CommDMO();
		try{
			
			HashVO[] vos = dmo.getHashVoArrayByDS(datasource, "select * from user_sequences where sequence_name='"+sequenceName.toUpperCase()+"'");
			if(vos != null && vos.length>0){
				dmo.executeUpdateByDS(datasource, "drop sequence "+sequenceName);
				dmo.commit(datasource);
			}
			
		}catch(Exception e){
			logger.debug("",e);
		}finally{
			dmo.releaseContext(datasource);
		}
	}
	
	public String createEntityDimensionTable(String dimensionXml,String dwdatasource){
		String tableName = "";
		CommDMO dmo = new CommDMO();
		try{
			
			List<String> sqls = new ArrayList<String>();
			
			if(!this.isExistSequence("S_DW_DIMENSION_TABLE", dwdatasource)){
				List<String> sequenceList = new ArrayList<String>();
				sequenceList.add(this.CREATE_DIMENSION_TABLE_NAME_SEQUENCE_SQL);
				this.executeDDL(dwdatasource, sequenceList);
			}
			
			String seq = dmo.getSequenceNextValByDS(dwdatasource, "S_DW_DIMENSION_TABLE");
			tableName = "DW_DIMENSION_TALBE_"+seq;
			
			String buildTableSql = "create table "+tableName+" ";
			
			//为防止在创建表的时候不出现相同的列
			Map<String,String> columnsMap = new HashMap<String,String>();
			columnsMap.put("ID", "NUMBER");
			
			Document doc = DocumentHelper.parseText(dimensionXml);
			Element root = doc.getRootElement();
			Element eleHierarchy = root.element("Hierarchy");
			List list = eleHierarchy.elements();
			if(list != null ){
				for(int i = 0; i<list.size(); i++){
					Element element = (Element)list.get(i);
					if(element.getName().equalsIgnoreCase("Level")){
						String columnName = element.attributeValue("column");
						String columnType = "VARCHAR2";
						columnsMap.put(columnName.toUpperCase(), columnType.toUpperCase());
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
			logger.debug("创建实体维度表失败!",e);
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
	
	private void insertIntoEntityDimensionTable(String modelCode,String dimensionEntityCode,Element dimensionEle,String dimensionTable,String dqc_datasource,String dw_datasource){
		
		CommDMO dmo = new CommDMO();
		try{
			
			String querySQL = this.getSQL(modelCode,dimensionEntityCode);
			String[] keys = this.getKeysFromTable(dimensionTable, dw_datasource);
			
			String columnStr = "";
			for(String key: keys){
				if(key.toUpperCase().equals("ID")){
					continue;
				}
				if(columnStr.equals("")){
					columnStr = key;
				}else{
					columnStr = columnStr+","+key;
				}
			}
			
			querySQL = "select distinct "+columnStr+" from ("+querySQL+")";
			
			int totalCount = 0;
			int pageSize = 1000;
			
			HashVO[] totalCountVO = dmo.getHashVoArrayByDS(dqc_datasource, "select count(1) cou from ("+querySQL+")");
			if(totalCountVO != null && totalCountVO.length>0){
				totalCount = totalCountVO[0].getIntegerValue("cou");
			}
			
			int totalPage = (totalCount+pageSize-1)/pageSize;
			
			if(totalCount >0){
//				truncateTable(dimensionTable,dw_datasource);
//				reBuildSequence(dw_datasource,"S_"+dimensionTable);
				for(int pageNum = 1; pageNum <= totalPage;pageNum++){
					
					Map<String, Object> hashvoMap= dmo.getHashVoArrayByPage(dqc_datasource, querySQL, pageNum, pageSize);
					HashVO[] vos = (HashVO[])hashvoMap.get(DMOConst.HASHVOARRAY);
					if(vos != null && vos.length >0){
						for(HashVO vo : vos){
							try{
								String id = dmo.getSequenceNextValByDS(dw_datasource, "S_"+dimensionTable);
								
								//为防止出现相同的字段
								Map<String,Object> columnValue= new HashMap<String,Object>();
								columnValue.put("ID", id);
								
								if(keys == null ){
									break;
								}
								for(String key : keys){
									if(key.toUpperCase().equals("ID")){
										continue;
									}
									columnValue.put(key.toUpperCase(), getColumnValue(key,vo));
								}
								
								Object[] parameters = new Object[columnValue.size()];
								
								String columns = "";
								String values = "";
								
								Set<String> tempKeys = columnValue.keySet();
								int index = 0;
								for(String column : tempKeys){
									if(StringUtil.isEmpty(columns)){
										columns = "("+column;
										values = " values (?";
									}else{
										columns = columns + ","+column;
										values = values +",?";
									}
									parameters[index] = columnValue.get(column);
									index ++;
								}
								
								String sql = "insert into "+dimensionTable+" "+columns+") "+values+")";
								dmo.executeUpdateByDS(dw_datasource, sql, parameters);
								dmo.commit(dw_datasource);
							}catch(Exception e){
								logger.debug("", e);
							}finally{
								dmo.releaseContext(dw_datasource);
							}
						}
					}
				}
			}
			
		}catch(Exception e){
			logger.debug("", e);
		}finally{
			dmo.releaseContext(dw_datasource);
		}
	}
	
 private void insertIntoSynonymsDimensionTable(Element synonymsEle,String dimensionTable,String dw_datasource){

		CommDMO dmo = new CommDMO();
		try{
			truncateTable(dimensionTable,dw_datasource);
			List variableList = synonymsEle.elements();
			if(variableList != null ){
//				truncateTable(dimensionTable,dw_datasource);
//				reBuildSequence(dw_datasource,"s_"+dimensionTable);
				for(Object item : variableList ){
					Element variableEle = (Element)item;
					String name = variableEle.attributeValue("name");
					String value = variableEle.attributeValue("value");
					String sql = "insert into "+dimensionTable+"(ID,NAME,VALUE) values(S_"+dimensionTable+".NEXTVAL,?,?)";
					try{
						dmo.executeUpdateByDS(dw_datasource, sql,name,value);
						dmo.commit(dw_datasource);
					}catch(Exception e){
						this.logger.debug("",e);
					}finally{
						dmo.releaseContext(dw_datasource);
					}
				}
			}
			
		}catch(Exception e){
			logger.debug("", e);
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
	
	private void truncateTable(String tableName,String datasource){
		CommDMO dmo = new CommDMO();
		try{
			String sql = "truncate table "+tableName;
			dmo.executeUpdateByDS(datasource,sql);
			dmo.commit(datasource);
		}catch(Exception e){
			this.logger.debug("",e);
		}finally{
			dmo.releaseContext(datasource);
		}
	}
	
	private boolean isExistRecords(String datasource,String sql){
		boolean isExist = false;
		CommDMO dmo = new CommDMO();
		try{
			HashVO[] vos = dmo.getHashVoArrayByDS(datasource, sql);
			if(vos != null && vos.length>0){
				if(vos[0].getIntegerValue("cou")>0){
					isExist = true;
				}
			}
		}catch(Exception e){
			this.logger.debug("",e);
		}finally{
			dmo.releaseContext(datasource);
		}
		return isExist;
	}
	
	private boolean isExistTable(String tableName,String datasource){
		boolean flag = false;
		CommDMO dmo = new CommDMO();
		try{
			if(StringUtil.isEmpty(tableName)){
				return false;
			}
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
		return emm.generateEntitySql(this.getEntityToString(entityCode), getEntityModelToString(modelCode),null);
	}
	
	public String getOriginalSQL(String modelCode, String entityCode) throws Exception{
		return emm.generateEntitySqlOriginalData(this.getEntityToString(entityCode), getEntityModelToString(modelCode),null);
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
	
	private void insertTimeDimension(String datasource,String sql,Object[] parameters){
		CommDMO dmo = new CommDMO();
		try{
			dmo.executeUpdateByDS(datasource, sql, parameters);
		}catch(Exception e){
			logger.debug("",e);
		}finally{
			dmo.releaseContext(datasource);
		}
	}
	
	public boolean initEntitiesInfo(String modelContent,String modelCode) throws Exception{
		boolean flag = false;
		try{
			if(!StringUtil.isEmpty(modelContent)){
				Document doc = DocumentHelper.parseText(modelContent);
				Element root = doc.getRootElement();
				modelMap.put(modelCode, root);
				Element entities = root.element("entities");
				List entitiesList = entities.elements();
				if(entitiesList != null ){
					for(Object obj : entitiesList){
						Element tempEntity = (Element)obj;
						String entityCode = tempEntity.attributeValue("code");
						entitiesMap.put(entityCode, tempEntity);
					}
				}
				
				Element dimensionsNode = root.element("dimensions");
				if(dimensionsNode != null ){
					List dimensionList = dimensionsNode.elements("Dimension");
					if(dimensionList != null){
						for(Object item : dimensionList){
							Element dimension = (Element) item;
							String  dimensionCode = dimension.attributeValue("code");
							dimensionMap.put(dimensionCode, dimension);
						}
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
