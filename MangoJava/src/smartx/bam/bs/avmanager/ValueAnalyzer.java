package smartx.bam.bs.avmanager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import smartx.bam.vo.DatabaseConst;
import smartx.bam.vo.SysConst;
import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.bs.NovaDBConnection;

public class ValueAnalyzer extends Analyzer {

	public ValueAnalyzer(String code, String metadataXML) throws Exception {
		super(code, metadataXML);
		this.analyzerType = SysConst.AVANALYZERTYPE_VALUEANALYZER;
	}

	@Override
	public String getResultStructSQL() {
		
		return "create table "+code+"(" +
		"ColumnName varchar2(100)," +
		"ColumnValue varchar2," +
		"ColumnCount int default 0"+
		")";
		
	}

	@Override
	public void analyze() {

		logger.debug("值字分析器[code="+code+"]开始分析数据...");
		
		if(isAnalyzing){
			return;
		}
		isAnalyzing = true;
		
		Map<String,HashMap<String,Integer>> colunmMap = new HashMap<String,HashMap<String,Integer>>();
		
		for(String columnName : inputColumn){
			
			colunmMap.put(columnName, new HashMap<String,Integer>());
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
						String value = rs.getString(columnName);
						String columnKey = nameSpace+":"+columnName;
						HashMap<String,Integer> tempMap = colunmMap.get(columnKey);
						int tempCount = tempMap.get(value)==null?0:tempMap.get(value);
						tempMap.put(value, tempCount+1);
						colunmMap.put(columnKey, tempMap);
						
					}
				}
			}

			
			for(String columnName : inputColumn){
				HashMap<String,Integer> tempMap = colunmMap.get(columnName);
				String nameSpace = columnName.split(":")[0];
				Set<String> keys = tempMap.keySet();
				HashMap<String,Integer> finalMap = new HashMap<String,Integer>();
				for(String value : keys){
					int count = tempMap.get(value);
					if(count<=1){
						int tempCount = finalMap.get(nameSpace+":<unique>")==null?0:finalMap.get(nameSpace+":<unique>");
						finalMap.put(nameSpace+":<unique>", tempCount+1);
					}else{
						finalMap.put(value, count);
					}
				}
				colunmMap.put(columnName, finalMap);
			}
			
			//清空原表
			String sql = "truncate table "+code;
			dmo.executeUpdateByDS(DatabaseConst.DATASOURCE_AVE, sql);
			for(String columnName : inputColumn){
				
				HashMap<String,Integer> tempMap = colunmMap.get(columnName);
				Set<String> keys = tempMap.keySet();
				for(String value : keys){
					
					sql = "insert into "+code+"(" +
							"ColumnName," +
							"ColumnValue," +
							"ColumnCount" +
							")values(?,?,?)";
					
					dmo.executeUpdateByDS(DatabaseConst.DATASOURCE_AVE, sql,
					columnName,
					value,
					tempMap.get(value)
					);
				}				
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
		
		logger.debug("值分析器[code="+code+"]分析数据完毕");
		
	
	}

}
