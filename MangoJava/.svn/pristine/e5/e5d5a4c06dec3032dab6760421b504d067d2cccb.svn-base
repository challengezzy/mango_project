package smartx.bam.bs.avmanager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.quartz.JobDetail;

import smartx.bam.vo.DatabaseConst;
import smartx.bam.vo.SysConst;
import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.job.bs.JobServer;
import smartx.framework.common.utils.StringUtil;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaLogger;

public abstract class Analyzer{
	
	protected Logger logger = NovaLogger.getLogger(this.getClass());
	
	protected final static String DATASOURCE_TYPE = "type";
	protected final static String DATASOURCE_CODE = "code";
	
	protected final static String DS_NAMESPACE = "namespace";
	protected final static String DS_NAME = "dsname";
	protected final static String DS_SQL = "dssql";
	
	protected String[] inputColumn;
	protected String datasourceType = SysConst.AVDATASOURCETYPE_QUERYVIEW;
	protected String datasourceName;
	protected String analyzerType;
	protected String code;
	protected String realDs;//真正的数据源名称
	protected String fetchSQL;//真正读取源数据的SQL
	
	protected HashSet<HashMap<String,String>> dsSet = new HashSet<HashMap<String,String>>();
	
	protected HashSet<HashMap<String,String>> dsInfoSet = new HashSet<HashMap<String,String>>();
	
	protected HashMap<String,HashSet<String>> dsFieldMap = new HashMap<String,HashSet<String>>();
	
	protected static boolean isAnalyzing = false;
	
	public Analyzer(String code, String metadataXML) throws Exception{
		if(code == null)
			throw new IllegalArgumentException();
		logger.debug("初始化分析器[code="+code+"]");
		this.code = code.toUpperCase();
		Document doc = DocumentHelper.parseText(metadataXML);
		Element rootElement = doc.getRootElement();
		Element datasources = rootElement.element("datasources");
		List elements = datasources.elements();
		if(elements == null)
			throw new Exception("未找到datasource节点");
		
		for(int i=0;i<elements.size();i++){
			Element datasourceNode = (Element)elements.get(i);
			String dsType = datasourceNode.attributeValue("type");
			String dsCode = datasourceNode.attributeValue("code");
			HashMap<String,String> pop = new HashMap<String,String>();
			pop.put(DATASOURCE_TYPE,dsType);
			pop.put(DATASOURCE_CODE,dsCode);
			
			dsSet.add(pop);
		}
		
		Element analyzerNode = rootElement.element("analyzer");
		if(analyzerNode == null)
			throw new Exception("未找到analyzer节点");
		
		Attribute inputColumnAttr = analyzerNode.attribute("inputColumns");
		if(inputColumnAttr != null){
			String inputColumnStr = inputColumnAttr.getText();
			if(inputColumnStr != null){
				inputColumn = inputColumnStr.trim().split(",");
				if(inputColumn != null ){
					for(String tempColumn : inputColumn ){
						String[] temp = tempColumn.split(":");
						HashSet<String> fieldSet = dsFieldMap.get(temp[0]);
						if(fieldSet == null ){
							fieldSet = new HashSet<String>();
						}
						fieldSet.add(temp[1]);
						dsFieldMap.put(temp[0], fieldSet);
					}
				}
			}
		}
		
		CommDMO dmo = new CommDMO();
		String datasource = DatabaseConst.DATASOURCE_AVE;
		HashVO[] vos;
		try{
			String sql = null;
			
			for(HashMap<String,String> dsObj : dsSet){
				datasourceType = dsObj.get(DATASOURCE_TYPE);
				datasourceName = dsObj.get(DATASOURCE_CODE);
				//解析数据源信息
				if(SysConst.AVDATASOURCETYPE_QUERYVIEW.equalsIgnoreCase(datasourceType)){
					sql = "select datasourcename,sql from bam_queryview where code=?";
					vos = dmo.getHashVoArrayByDS(null, sql,datasourceName);
					if(vos.length != 1)
						throw new Exception("指定的数据源["+datasourceName+"]不存在");
					realDs = vos[0].getStringValue("datasourcename");
					fetchSQL = vos[0].getStringValue("sql");
					
					HashMap<String,String> dsinfo = new HashMap<String,String>();
					dsinfo.put(DS_NAME, realDs);
					dsinfo.put(DS_SQL, fetchSQL);
					dsinfo.put(DS_NAMESPACE, datasourceType+"."+datasourceName);
					
					dsInfoSet.add(dsinfo);
				}
				else if(SysConst.AVDATASOURCETYPE_BUSINESSVIEW.equalsIgnoreCase(datasourceType)){
					sql = "select streamwindowname from bam_businessview where code=?";
					vos = dmo.getHashVoArrayByDS(null, sql,datasourceName);
					if(vos.length != 1)
						throw new Exception("指定的数据源["+datasourceName+"]不存在");
					realDs = DatabaseConst.DATASOURCE_CEP;
					fetchSQL = "select * from "+vos[0].getStringValue("streamwindowname");
					
					HashMap<String,String> dsinfo = new HashMap<String,String>();
					dsinfo.put(DS_NAME, realDs);
					dsinfo.put(DS_SQL, fetchSQL);
					dsinfo.put(DS_NAMESPACE, datasourceType+"."+datasourceName);
					
					dsInfoSet.add(dsinfo);
				}
				
			}
			
			//构建内存临时表
			sql = getResultStructSQL();
			dmo.executeUpdateByDS(datasource, sql);
			
			String analyzeInJob = analyzerNode.attributeValue("analyzeInJob");
			String jobExp = analyzerNode.attributeValue("analyzeJobExp");
			if(!StringUtil.isEmpty(analyzeInJob) && analyzeInJob.equalsIgnoreCase("true")){
				JobServer.addCronJob(code,SysConst.ANALYZER_GROUP,AnalyzerJob.class.getName(),jobExp);
			}
			
			String analyzeOnQuery = analyzerNode.attributeValue("analyzeOnQuery");
			if(!StringUtil.isEmpty(analyzeOnQuery) && analyzeOnQuery.equalsIgnoreCase("true")){
				sql = "create trigger TR_"+code+" before select " +
				"on "+code+" call \"smartx.bam.bs.avmanager.AnalyzerTrigger\"";
				dmo.executeUpdateByDS(datasource, sql);
			}
			
		}
		catch(Exception e){
			throw e;
		}
		finally{
			dmo.releaseContext(datasource);

		}
		logger.debug("初始化分析器[code="+code+"]完毕");
	}
	
	
	/**
	 * 创建结果集结构的SQL语句
	 * @return
	 */
	public abstract String getResultStructSQL();
	
	/**
	 * 调用分析逻辑
	 */
	public  abstract  void analyze();
	
}
