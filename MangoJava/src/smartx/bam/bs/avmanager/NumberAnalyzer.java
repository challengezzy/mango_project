package smartx.bam.bs.avmanager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import smartx.bam.vo.DatabaseConst;
import smartx.bam.vo.SysConst;
import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.bs.NovaDBConnection;

public class NumberAnalyzer extends Analyzer {

	public NumberAnalyzer(String code, String metadataXML) throws Exception {
		super(code,metadataXML);
		this.analyzerType = SysConst.AVANALYZERTYPE_NUMBERANALYZER;
	}

	@Override
	public String getResultStructSQL() {
		
		return "create table "+code+"(" +
		"ColumnName varchar2(100)," +
		"RowCount int default 0," +
		"NullCount int default 0," +
		"highestValue DECIMAL default 0," +
		"lowestValue DECIMAL default 0," +
		"sumValue DECIMAL default 0," +
		"meanValue DECIMAL default 0," +
		"geometricMean DECIMAL default 0," +
		"standardDeviation DECIMAL default 0," +
		"variance DECIMAL default 0" +
		")";
	}

	@Override
	public void analyze() {
		logger.debug("数字分析器[code="+code+"]开始分析数据...");
		
		if(isAnalyzing){
			return;
		}
		isAnalyzing = true;
		
		int rowCount = 0;
		
		Map<String,Integer> rowCountMap = new HashMap<String, Integer>();
		Map<String,Integer> nullCountMap = new HashMap<String, Integer>();
		Map<String,Double> highestValueMap = new HashMap<String, Double>();
		Map<String,Double> lowestValueMap = new HashMap<String,Double>();
		Map<String,Double> sumValueMap = new HashMap<String,Double>();
		Map<String,Double> meanValueMap = new HashMap<String,Double>();
		Map<String,Double> geometricMeanMap = new HashMap<String,Double>();
		Map<String,Double> standardDeviationMap = new HashMap<String,Double>();
		Map<String,Double> varianceMap = new HashMap<String,Double>();
		
		Map<String,HashSet<Double>> colunmMap = new HashMap<String,HashSet<Double>>();
		
		for(String columnKey : inputColumn){
			rowCountMap.put(columnKey, 0);
			nullCountMap.put(columnKey, 0);
			highestValueMap.put(columnKey, 0.0);
			lowestValueMap.put(columnKey, 0.0);
			sumValueMap.put(columnKey, 0.0);
			meanValueMap.put(columnKey, 0.0);
			geometricMeanMap.put(columnKey, 0.0);
			standardDeviationMap.put(columnKey, 0.0);
			varianceMap.put(columnKey, 0.0);
			
			colunmMap.put(columnKey, new HashSet<Double>());
		}
		
		CommDMO dmo = new CommDMO();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			
			for(HashMap<String,String> dsInfo : dsInfoSet){
				
				String dsName = dsInfo.get(DS_NAME);
				String sql = dsInfo.get(DS_SQL);
				String nameSpace = dsInfo.get(DS_NAMESPACE);
				
				NovaDBConnection conn = dmo.getConn(dsName);
				stmt = conn.prepareStatement(sql);
				rs = stmt.executeQuery();
				
				HashSet<String> columnSet = dsFieldMap.get(nameSpace);
				
				while(rs.next()){
					for(String columnName : columnSet){
						Double value = rs.getDouble(columnName);
						String columnKey = nameSpace+":"+columnName;
						rowCountMap.put(columnKey, rowCountMap.get(columnKey)+1);
						if(value == null)
							nullCountMap.put(columnKey, nullCountMap.get(columnKey)+1);
						else{ 
							
							colunmMap.get(columnKey).add(value);
							sumValueMap.put(columnKey, sumValueMap.get(columnKey)+value);
							meanValueMap.put(columnKey, sumValueMap.get(columnKey)/Double.parseDouble(rowCountMap.get(columnKey)+""));
							
						}
					}
				}
				
			}
			
			for(String columnName : inputColumn){
				
				HashSet<Double> set = colunmMap.get(columnName);
				dealMaxAndMinValueHander(columnName,highestValueMap,lowestValueMap,set);
				dealGeometricMeanHander(columnName,geometricMeanMap,set);
				dealStddevHander(columnName,meanValueMap.get(columnName),standardDeviationMap,set);
				
				varianceMap.put(columnName, Math.pow(standardDeviationMap.get(columnName), 2.0));
			}
			
			//清空原表
			String sql = "truncate table "+code;
			dmo.executeUpdateByDS(DatabaseConst.DATASOURCE_AVE, sql);
			for(String columnName : inputColumn){
				sql = "insert into "+code+"(ColumnName," +
						"RowCount," +
						"NullCount," +
						"highestValue,lowestValue,sumValue,meanValue,geometricMean,standardDeviation,variance" +
						")values(?,?,?,?,?,?,?,?,?,?)";
				dmo.executeUpdateByDS(DatabaseConst.DATASOURCE_AVE, sql,columnName,rowCountMap.get(columnName),
						nullCountMap.get(columnName),highestValueMap.get(columnName),
						lowestValueMap.get(columnName),
						sumValueMap.get(columnName),
						meanValueMap.get(columnName),
						geometricMeanMap.get(columnName),
						standardDeviationMap.get(columnName),
						varianceMap.get(columnName));
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
		
		logger.debug("数字分析器[code="+code+"]分析数据完毕");
		
	}
	
	private void dealMaxAndMinValueHander(String columnName,Map<String,Double> highestValueMap,Map<String,Double> lowestValueMap,HashSet<Double> set){
		try{
			if(set.size()>0){
				 Object[] obj =	set.toArray();
				 Double maxValue = (Double)obj[0];
				 Double minValue = (Double)obj[0];
				 for(int i=1;i<obj.length;i++){
					 Double t = (Double)obj[i];
					 if(t>maxValue){
						 maxValue = t;
					 }
					 
					 if(t<minValue){
						 minValue = t;
					 }
				 }
				
				 highestValueMap.put(columnName, maxValue);
				 lowestValueMap.put(columnName, minValue);
				 
			}
		}catch(Exception e){
			
		}
	}
	
	private void dealGeometricMeanHander(String columnName,Map<String,Double> geometricMeanMap,HashSet<Double> set){
		try{
			Double valueCount = 1.0;
			for(Double value : set){
				valueCount = valueCount*value;
			}
			Double geoMeanValue = Math.pow(valueCount,1.0/set.size());
			geometricMeanMap.put(columnName, geoMeanValue);
		}catch(Exception e){
			
		}
	}
	
	private void dealStddevHander(String columnName,Double meanValue,Map<String,Double> standardDeviationMap,HashSet<Double> set){
		try{
			Double s2Value = 0.0;
			
			for(Double value : set){
				s2Value = s2Value+Math.pow((value - meanValue),2.0);
			}
			
			s2Value = s2Value/set.size()-1;
			
			if(s2Value<0){
				standardDeviationMap.put(columnName,0.0);
			}else{
				standardDeviationMap.put(columnName, Math.sqrt(s2Value));
			}
			
		}catch(Exception e){
			
		}
	}

}
