package smartx.bam.bs.avmanager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import smartx.bam.vo.DatabaseConst;
import smartx.bam.vo.SysConst;
import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.bs.NovaDBConnection;
import smartx.framework.common.utils.StringUtil;
import smartx.framework.common.vo.HashVO;

public class MatchAnalyzer extends Analyzer {
	
	private String metadata = null;
	
	public MatchAnalyzer(String code, String metadataXML) throws Exception {
		super(code, metadataXML);
		this.analyzerType = SysConst.AVANALYZERTYPE_MATCHANALYZER;
		this.metadata = metadataXML;
	}

	@Override
	public String getResultStructSQL() {
		
		return "create table "+code+"(" +
		"ColumnName varchar2(100)," +
		"RowCount int default 0," +
		"NullCount int default 0," +
		"trueCount int default 0," +
		"falseCount int default 0" +
		")";
		
	}

	@Override
	public void analyze() {

		logger.debug("匹配分析器[code="+code+"]开始分析数据...");
		int rowCount = 0;
		
		if(isAnalyzing){
			return;
		}
		isAnalyzing = true;
		
		Map<String,Integer> rowCountMap = new HashMap<String, Integer>();
		Map<String,Integer> nullCountMap = new HashMap<String, Integer>();
		Map<String,Integer> trueCountMap = new HashMap<String, Integer>();
		Map<String,Integer> falseCountMap = new HashMap<String,Integer>();
	
		//初始化结果Map
		for(String columnName : inputColumn){
			rowCountMap.put(columnName, 0);
			nullCountMap.put(columnName, 0);
			trueCountMap.put(columnName, 0);
			falseCountMap.put(columnName, 0);
		}
		
		CommDMO dmo = new CommDMO();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			
			HashSet<String> matchValues = getMatchValues(metadata);
			
			for(HashMap<String,String> dsInfo : dsInfoSet){
				
				String dsName = dsInfo.get(DS_NAME);
				String sql = dsInfo.get(DS_SQL);
				String nameSpace = dsInfo.get(DS_NAMESPACE);
				
				NovaDBConnection conn = dmo.getConn(dsName);
				stmt = conn.prepareStatement(sql);
				rs = stmt.executeQuery();
				
				HashSet<String> columnSet = dsFieldMap.get(nameSpace);
				
				while(rs.next()){
					rowCount++;
					for(String columnName : columnSet){
						String value = rs.getString(columnName);
						String columnKey = nameSpace+":"+columnName;
						rowCountMap.put(columnKey, rowCountMap.get(columnKey)+1);
						if(value == null)
							nullCountMap.put(columnKey, nullCountMap.get(columnKey)+1);
						else{
							if(isInMachValues(value,matchValues)){
								trueCountMap.put(columnKey, trueCountMap.get(columnKey)+1);
							}else{
								falseCountMap.put(columnKey, falseCountMap.get(columnKey)+1);
							}
						}
					}
				}
			}
			
			//清空原表
			String sql = "truncate table "+code;
			dmo.executeUpdateByDS(DatabaseConst.DATASOURCE_AVE, sql);
			for(String columnName : inputColumn){
				sql = "insert into "+code+"(" +
						"ColumnName," +
						"RowCount," +
						"NullCount," +
						"trueCount," +
						"falseCount" +
						")values(?,?,?,?,?)";
				dmo.executeUpdateByDS(DatabaseConst.DATASOURCE_AVE, sql,
						columnName,
						rowCountMap.get(columnName),
						nullCountMap.get(columnName),
						trueCountMap.get(columnName),
						falseCountMap.get(columnName));
			}
			dmo.commit(DatabaseConst.DATASOURCE_AVE);
		} catch (Exception e) {
			try {
				dmo.rollback(DatabaseConst.DATASOURCE_AVE);
			} catch (Exception e1) {
				logger.error("",e1);
			}
			logger.error("数据分析失败",e);
			
		}
		finally{
			try {
				if(stmt != null)
					stmt.close();
				if(rs != null)
					rs.close();
			} catch (SQLException e) {
				logger.error("",e);
			}
			dmo.releaseContext(realDs);
			isAnalyzing = false;
		}
		
		logger.debug("匹配分析器[code="+code+"]分析数据完毕");
	
	}
	
	private HashSet<String> getMatchValues(String xml){
		HashSet<String> matchValues = new HashSet<String>();
		CommDMO dmo = new CommDMO();
		try{
			Document doc = DocumentHelper.parseText(xml);
			Element rootElement = doc.getRootElement();
			Element analyzerNode = rootElement.element("analyzer");
			String synCode = analyzerNode.attribute("synonymyEN").getText();
			HashVO[] vos = dmo.getHashVoArrayByDS(null, "SELECT CONTENT FROM PUB_METADATA_TEMPLET WHERE CODE='MT_SYNONYMS'");
			if(vos != null && vos.length>0){
				String content = vos[0].getStringValue("content");
				if(content != null && !content.trim().equals("")){
					
					Document synDoc = DocumentHelper.parseText(content);
					Element synRoot = synDoc.getRootElement();
					
					List synList = null;
					synList = synRoot.elements();
					
					if(synList != null ){
						
						for(int i=0;i<synList.size();i++){
							Element synElement = (Element)synList.get(i);
							String code = synElement.attributeValue("code");
							if(!StringUtil.isEmpty(code)&& !StringUtil.isEmpty(synCode) && code.equals(synCode)){
								List sysnValuesList = synElement.elements();
								if(sysnValuesList != null ){
									for(int a=0;a<sysnValuesList.size();a++){
										Element valueEle = (Element)sysnValuesList.get(a);
										String value = valueEle.attributeValue("value");
										matchValues.add(value);
									}
								}
							}
						}
					}
				}
			}
		}catch(Exception e){
			
		}finally{
			dmo.releaseContext(null);
		}
		return matchValues;
	}
	
	private boolean isInMachValues(String value,HashSet<String> matchValues){
		boolean flag = false;
		try{
			for(String valueTemp : matchValues){
				if(!StringUtil.isEmpty(value) && !StringUtil.isEmpty(valueTemp) && valueTemp.equals(value)){
					flag = true;
					break;
				}
			}
		}catch(Exception e){
			flag = false;
		}
		return flag;
	}

}
