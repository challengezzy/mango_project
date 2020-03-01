package smartx.bam.bs.avmanager;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import smartx.bam.vo.DatabaseConst;
import smartx.bam.vo.SysConst;
import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.bs.dbaccess.dsmgr.DataSourceManager;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaLogger;

public class AnalyzeViewManager {
	protected Logger logger = NovaLogger.getLogger(this.getClass());
	protected static Map<String, Analyzer> analyzerMap = new HashMap<String, Analyzer>();
	private static boolean isInit = false;
	
	
	public AnalyzeViewManager(){
		if(isInit)
			return;
		logger.debug("初始化分析视图引擎");
		logger.debug("初始化分析视图引擎数据源...");
		HashMap<String, String> dssetup = new HashMap<String, String>();
		dssetup.put("name", DatabaseConst.DATASOURCE_AVE);
        dssetup.put("driver", "org.h2.Driver");
        dssetup.put("url", "jdbc:h2:mem:avdb");
        dssetup.put("initsize", "1");
        dssetup.put("poolsize", "10");
        DataSourceManager.initDS(new HashMap[]{dssetup});
		CommDMO dmo = new CommDMO();
		try{
			String sql = "select * from bam_analyzeview";
			HashVO[] vos = dmo.getHashVoArrayByDS(null, sql);
			for(HashVO vo : vos){
				String code = vo.getStringValue("CODE");
				String metadataXML = vo.getStringValue("METADATA");
				try{
					Analyzer analyzer = getAnalyzerByMetadata(code, metadataXML);
					analyzerMap.put(analyzer.code, analyzer);
				}
				catch(Exception e){
					NovaLogger.getLogger(AnalyzeViewManager.class).error("初始化分析视图[code="+code+"]失败", e);
					continue;
				}
			}
		} 
		catch(Exception e){
			NovaLogger.getLogger(AnalyzeViewManager.class).error("",e);
		}
		finally{
			dmo.releaseContext(null);
		}
		isInit = true;
		logger.debug("初始化分析视图引擎完毕");
	}
	
	public Analyzer getAnalyzerByMetadata(String code, String metadataXML) throws Exception{
		Document doc = DocumentHelper.parseText(metadataXML);
		Element rootElement = doc.getRootElement();
		Element analyzerNode = rootElement.element("analyzer");
		if(analyzerNode == null)
			throw new Exception("未找到analyzer节点");
		Attribute typeAttr = analyzerNode.attribute("type");
		String type = null;
		if(typeAttr != null)
			type = typeAttr.getText();
		if(SysConst.AVANALYZERTYPE_STRINGANALYZER.equalsIgnoreCase(type)){
			return new StringAnalyzer(code, metadataXML);
		}else if(SysConst.AVANALYZERTYPE_NUMBERANALYZER.equalsIgnoreCase(type)){
			return new NumberAnalyzer(code, metadataXML);
		}else if(SysConst.AVANALYZERTYPE_BOOLEANANALYZER.equalsIgnoreCase(type)){
			return new BooleanAnalyzer(code, metadataXML);
		}else if(SysConst.AVANALYZERTYPE_DATEANALYZER.equalsIgnoreCase(type)){
			return new DateAnalyzer(code, metadataXML);
		}else if(SysConst.AVANALYZERTYPE_VALUEANALYZER.equalsIgnoreCase(type)){
			return new ValueAnalyzer(code, metadataXML);
		}else if(SysConst.AVANALYZERTYPE_WEEKDAYANALYZER.equalsIgnoreCase(type)){
			return new WeekDayAnalyzer(code, metadataXML);
		}else if(SysConst.AVANALYZERTYPE_MATCHANALYZER.equalsIgnoreCase(type)){
			return new MatchAnalyzer(code, metadataXML);
		}
		else
			throw new Exception("未支持的分析器["+type+"]");
	}
}
