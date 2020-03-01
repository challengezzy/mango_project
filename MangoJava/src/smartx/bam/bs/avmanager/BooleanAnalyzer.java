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

public class BooleanAnalyzer extends Analyzer {

	public BooleanAnalyzer(String code, String metadataXML) throws Exception {
		super(code, metadataXML);
		this.analyzerType = SysConst.AVANALYZERTYPE_BOOLEANANALYZER;
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

		logger.debug("布尔分析器[code="+code+"]开始分析数据...");
		
		if(isAnalyzing){
			return;
		}
		isAnalyzing = true;
		
		int rowCount = 0;
		
		Map<String,Integer> rowCountMap = new HashMap<String, Integer>();
		Map<String,Integer> nullCountMap = new HashMap<String, Integer>();
		Map<String,Integer> trueCountMap = new HashMap<String, Integer>();
		Map<String,Integer> falseCountMap = new HashMap<String,Integer>();
		
		for(String columnKey : inputColumn){
			rowCountMap.put(columnKey, 0);
			nullCountMap.put(columnKey, 0);
			trueCountMap.put(columnKey, 0);
			falseCountMap.put(columnKey, 0);
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
						Boolean value = rs.getBoolean(columnName);
						String columnKey = nameSpace+":"+columnName;
						rowCountMap.put(columnKey, rowCountMap.get(columnKey)+1);
						if(value == null)
							nullCountMap.put(columnKey, nullCountMap.get(columnKey)+1);
						else{ 
							if(value.booleanValue()==true){
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
		
		logger.debug("布尔分析器[code="+code+"]分析数据完毕");
		

	}

}
