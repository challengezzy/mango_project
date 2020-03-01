package smartx.publics.dataprofile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.utils.StringUtil;
import smartx.framework.common.vo.DMOConst;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaLogger;

/**
 *@author zzy
 *@date Feb 10, 2012
 *@description 数据列分析服务，多少数据列在一个数据分析集中，一个列可能指定多个分析指标
 **/
public class ColumnProfileService {
	
	private static Logger logger = NovaLogger.getLogger(ColumnProfileService.class);
	
	/**
	 * 指标模板缓存
	 */
	private static Map<String, Indicator> indicatorCache = new HashMap<String, Indicator>();
	
	public static ColumnProfileService profileService;
	public static CommDMO dmo = new CommDMO();
	
	public ColumnProfileService() throws Exception{
		//构造方法
		if(profileService != null)
			throw new Exception("ColumnProfileService 不能直接使用构造方法，请使用 getInstance进行实例化!");
		
		init();
	}
	
	//获取服务实例
	public static ColumnProfileService getInstance() throws Exception{
		if(profileService == null)
			profileService = new ColumnProfileService();
		
		return profileService;
	}
	
	public void init() throws Exception{
		CommDMO dmo = new CommDMO();
		String sql = "select id,code,name,definitiontemplate,type,creator,extattribute1 from pub_dp_indicatortemplate";
			
		HashVO[] vos = dmo.getHashVoArrayByDS(DMOConst.DS_DEFAULT, sql);
		int count =0;
		for(int i=0;i<vos.length;i++){
			HashVO vo = vos[i];
			Indicator indicaor = new Indicator();
			
			indicaor.setId( vo.getStringValue("id"));
			indicaor.setName(vo.getStringValue("name"));
			indicaor.setCode(vo.getStringValue("code"));
			indicaor.setDefinitionTemplate(vo.getStringValue("definitiontemplate"));
			indicaor.setCreator(vo.getStringValue("creator"));
			indicaor.setType(vo.getStringValue("type"));
			indicaor.setExtattribute1(vo.getStringValue("extattribute1"));
			
			count++;
			indicatorCache.put(indicaor.getId(), indicaor);
		}
		logger.info("成功初始化【"+count+"】个分析指标模板！");
		dmo.releaseContext(DMOConst.DS_DEFAULT);
	}
	
	public void profileAnalyzerSet(String analyzerSetId,String dsName,String analyseDate) throws Exception{
		logger.info("开始分析集【"+ analyzerSetId +"】的数据剖析!");
		//1，查询所有分析集中的所有分析列及其指标
		String querySql = "SELECT A.ID,A.CODE,A.DATASOURCENAME,MT.CONTENT FROM PUB_DP_ANALYZERSET A,PUB_METADATA_TEMPLET MT "
						  + " WHERE A.MTCODE=MT.CODE AND A.ID = ?";
		
		String insertSetSql = "insert into pub_dp_analyzerins(id,analyzersetid,analysedate,result,analysistime)"
			+ " values(?,?,?,?,sysdate)";
		
		//删除同期剖析结果
		deleteSametermResult(analyzerSetId,analyseDate);
		
		String setInsId = dmo.getSequenceNextValByDS(DMOConst.DS_DEFAULT, "S_PUB_DP_ANALYZERINS");
		dmo.executeUpdateByDS(DMOConst.DS_DEFAULT, insertSetSql, setInsId,analyzerSetId,analyseDate,"剖析中...");
		dmo.commit(DMOConst.DS_DEFAULT);
		
		HashVO[] setVos = dmo.getHashVoArrayByDS(DMOConst.DS_DEFAULT, querySql, analyzerSetId);
		if(setVos.length < 1)
			throw new Exception("分析集【"+analyzerSetId+"】定义未找到!");
		
		String setContent = setVos[0].getStringValue("CONTENT");
		Document doc = DocumentHelper.parseText(setContent);
		Element root = doc.getRootElement();
		Element columnsE = root.element("columns");
		
		@SuppressWarnings("unchecked")
		List<Element> colList = columnsE.elements();
		for (int i = 0; i < colList.size(); i++) {
			Element colE = colList.get(i);
			String tablename = colE.attributeValue("table");
			String columnname = colE.attributeValue("name");
			String datafilter = colE.attributeValue("datafilter");
			
			@SuppressWarnings("unchecked")
			List<Element> inList = colE.elements();
			for(int m=0; m <inList.size(); m++){
				//对每个分析指标进行分析
				Element inE = inList.get(m);
				String param = inE.attributeValue("param");
				String indicatorId = inE.attributeValue("indicatorid");
				Indicator indicator = indicatorCache.get(indicatorId);
				indicatorAnalyse(setInsId,dsName, analyseDate,indicator.getId(),tablename, columnname
						,datafilter, param,indicator.getType(),indicator.getDefinitionTemplate(),indicator.getName());
			}
		}
		String updateSql = "update pub_dp_analyzerins i set i.result = ? where id =? ";
		dmo.executeUpdateByDS(DMOConst.DS_DEFAULT, updateSql, "剖析成功",setInsId);
		
		dmo.commit(DMOConst.DS_DEFAULT);
		dmo.commit(dsName);
		dmo.releaseContext(DMOConst.DS_DEFAULT);
		dmo.releaseContext(dsName);
		logger.info("成功结束分析集【"+ analyzerSetId +"】的数据剖析!");
		
	}
	
	//删除同期剖析结果
	public void deleteSametermResult(String analyzerSetId,String analyseDate) throws Exception {
		String queryAnaIns = "select id from pub_dp_analyzerins i where i.analyzersetid=? and i.analysedate=?";
		HashVO[] results = dmo.getHashVoArrayByDS(DMOConst.DS_DEFAULT, queryAnaIns, analyzerSetId,analyseDate);
		
		if(results.length > 0){
			String analyzerInsId = results[0].getStringValue("ID");//上次分析记录ID
			ArrayList<String> sqlList = new ArrayList<String>();
			sqlList.add("delete pub_dp_result_frequency t where t.indicatorinsid in (select i.id from pub_dp_indicatorins i where i.analyzerinsid=" + analyzerInsId+")");
			sqlList.add("delete pub_dp_result_pattern t where t.indicatorinsid in (select i.id from pub_dp_indicatorins i where i.analyzerinsid=" + analyzerInsId+")"); 
			sqlList.add("delete pub_dp_result_simple t where t.indicatorinsid in (select i.id from pub_dp_indicatorins i where i.analyzerinsid=" + analyzerInsId+")"); 
			sqlList.add("delete pub_dp_indicatorins i where i.analyzerinsid=" + analyzerInsId); 
			sqlList.add("delete pub_dp_analyzerins where id=" + analyzerInsId); 
			
			dmo.executeBatchByDS(DMOConst.DS_DEFAULT, sqlList);
			dmo.commit(DMOConst.DS_DEFAULT);
			logger.info("分析集【"+analyzerSetId+"】，同批次【"+analyseDate+"】的原有剖析结果数据删除成功。");
		}
		logger.info("没有该分析集的同批次分析记录，不需要清理！");
	}
	
	
	/**
	 * 指标实例剖析
	 * @param dsName
	 * @param analyseDate 剖析日期yyyymmdd
	 * @param tableName
	 * @param columnName
	 * @param dataFilter
	 * @param param
	 * @param type
	 * @param definitionTemplate
	 */
	public void indicatorAnalyse(String analyseSetInsId,String dsName,String analyseDate,String col_indicatorId,String tableName
			,String columnName,String dataFilter,String param,String type,String definitionTemplate,String indicatorName) throws Exception{
		
		logger.info("分析指标【"+indicatorName+"】,在数据源【"+dsName+"】上的【"+tableName+"."+ columnName +"】属性。");
		//1,根据指标类型， 确定查询SQL，作参数替换
		if(dataFilter == null)
			dataFilter = "";
		if(param == null || "".equals(param))
			param = "'%'";
		
		String analyseSql = sqlParse(tableName, columnName, dataFilter, param, definitionTemplate);
		
		//2,执行指标SQL
		HashVO[] resultVos = dmo.getHashVoArrayByDS(dsName, analyseSql);
		//3,根据指标类型，把查询结果保存到分析结果库中pub_dp_result_simple,pub_dp_result_pattern,pub_dp_result_frequency
		String indicatorInsId = dmo.getSequenceNextValByDS(DMOConst.DS_DEFAULT, "S_PUB_DP_INDICATORINS");
		String sqlI = "insert into pub_dp_indicatorins(id,analyzerinsid,analysedate,indicatorid,indicatorname,analysesql,tablename,columnname,type,patternexpr)"
			+ " values(?,?,?,?,?,?,?,?,?,?)";
		String sqlS = "insert into pub_dp_result_simple(indicatorinsid,analyvalue) values(?,?)";
		String sqlP = "insert into pub_dp_result_pattern(indicatorinsid,totalrecord,matchrecord,notmatchrecord) values(?,?,?,?)";
		String sqlF = "insert into pub_dp_result_frequency(indicatorinsid,columnvalue,columncount) values(?,?,?)";
		
		dmo.executeUpdateByDS(DMOConst.DS_DEFAULT, sqlI, indicatorInsId,analyseSetInsId,analyseDate,col_indicatorId,indicatorName
				,analyseSql,tableName,columnName,type,param);
		
		if(ProfileConst.INDICATOR_TYPE_SIMPLE.equals(type)){
			//简单统计
			String analyseValue = resultVos[0].getStringValue(0);
			dmo.executeUpdateByDS(DMOConst.DS_DEFAULT, sqlS, indicatorInsId,analyseValue);
			
			logger.info("在" + tableName +"."+ columnName + "上" + "插入简单统计指标:" + indicatorName);
			
		}else if(ProfileConst.INDICATOR_TYPE_PATTERN.equals(type)){
			//模式匹配
			int matchNum = resultVos[0].getIntegerValue(0).intValue();
			int totalNum = resultVos[0].getIntegerValue(1).intValue();
			int notMatchNum = totalNum - matchNum;
			
			dmo.executeUpdateByDS(DMOConst.DS_DEFAULT, sqlP, indicatorInsId,totalNum,matchNum,notMatchNum);
			
			logger.info("在" + tableName +"."+ columnName + "上" + "进行匹配计算:" + param);
			
		}else if(ProfileConst.INDICATOR_TYPE_FREQUENCY.equals(type)){
			//频率分析
			for(int i=0;i<resultVos.length;i++){
				HashVO vo = resultVos[i];
				String columnValue = vo.getStringValue(0);
				String columnCount = vo.getStringValue(1);
				dmo.executeUpdateByDS(DMOConst.DS_DEFAULT, sqlF, indicatorInsId,columnValue,columnCount);
			}
			//TODO 针对年、月、日、周频率，需要对时期字段进行数据处理后，再分组统计 ,在SQL中已进行过处理
			
			logger.info("在" + tableName +"."+ columnName + "上" + "进行频率统计:" + param);
		}else{
			logger.info("未识别的指标统计类型 type=" + type);
		}
		
		dmo.commit(DMOConst.DS_DEFAULT);
	}
	
	//SQL参数解析
	public String sqlParse(String tableName,String columnName,String dataFilter,String param,String definitionTemplate)
		throws Exception{
		String sql = definitionTemplate;
		HashMap<String, Object>  paramMap = new HashMap<String, Object>();
		paramMap.put(ProfileConst.SQL_PARAM_TABLENAME, tableName);
		paramMap.put(ProfileConst.SQL_PARAM_COLUMNNAMES, columnName);
		paramMap.put(ProfileConst.SQL_PARAM_WHERECLAUSE, "".equals(dataFilter)?"":" where " + dataFilter);
		paramMap.put(ProfileConst.SQL_PARAM_ANDWHERECLAUSE, "".equals(dataFilter)?"":" and " + dataFilter);
		paramMap.put(ProfileConst.SQL_PARAM_GROUPBYALIAS, columnName);
		paramMap.put(ProfileConst.SQL_PARAM_PATTERNEXPR, param);
		
		sql = StringUtil.buildExpression(paramMap, sql, "<%=__", "__%>");
		
		return sql;
	}
	
	
}