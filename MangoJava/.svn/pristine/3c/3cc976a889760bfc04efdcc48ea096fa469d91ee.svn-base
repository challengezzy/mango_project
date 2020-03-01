package smartx.bam.bs.avmanager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import smartx.bam.vo.DatabaseConst;
import smartx.bam.vo.SysConst;
import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.bs.NovaDBConnection;

public class WeekDayAnalyzer extends Analyzer {

	public WeekDayAnalyzer(String code, String metadataXML) throws Exception {
		super(code, metadataXML);
		this.analyzerType = SysConst.AVANALYZERTYPE_WEEKDAYANALYZER;
	}

	@Override
	public String getResultStructSQL() {
		
		return "create table "+code+"(" +
		"columnName varchar2(100)," +
		"sunday int default 0," +
		"monday int default 0," +
		"tuesday int default 0," +
		"wednesday int default 0," +
		"thursday int default 0," +
		"friday int default 0," +
		"saturday int default 0" +
		")";
		
	}

	@Override
	public void analyze() {
		
		logger.debug("周期分析器[code="+code+"]开始分析数据...");
		
		if(isAnalyzing){
			return;
		}
		isAnalyzing = true;
		
		Map<String,Integer> sundayMap = new HashMap<String, Integer>();
		Map<String,Integer> mondayMap = new HashMap<String, Integer>();
		Map<String,Integer> tuesdayMap = new HashMap<String,Integer>();
		Map<String,Integer> wednesdayMap = new HashMap<String,Integer>();
		Map<String,Integer> thursdayMap = new HashMap<String,Integer>();
		Map<String,Integer> fridayMap = new HashMap<String,Integer>();
		Map<String,Integer> saturdayMap = new HashMap<String,Integer>();
		
		//初始化结果Map
		for(String columnName : inputColumn){
			sundayMap.put(columnName, 0);
			mondayMap.put(columnName, 0);
			tuesdayMap.put(columnName, 0);
			wednesdayMap.put(columnName, 0);
			thursdayMap.put(columnName, 0);
			fridayMap.put(columnName, 0);
			saturdayMap.put(columnName, 0);
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
						Date value = rs.getDate(columnName);
						String columnKey = nameSpace+":"+columnName;
						if(value != null){
							
							Calendar cld = Calendar.getInstance();
							cld.setTime(value);
							
							int weekDay = cld.get(Calendar.DAY_OF_WEEK);
							
							switch (weekDay) {
							case Calendar.SUNDAY:
								sundayMap.put(columnKey, sundayMap.get(columnKey)+1);
								break;
							case Calendar.MONDAY:
								mondayMap.put(columnKey, mondayMap.get(columnKey)+1);
								break;
							case Calendar.TUESDAY:
								tuesdayMap.put(columnKey, tuesdayMap.get(columnKey)+1);
								break;
							case Calendar.WEDNESDAY:
								wednesdayMap.put(columnKey, wednesdayMap.get(columnKey)+1);
								break;
							case Calendar.THURSDAY:
								thursdayMap.put(columnKey, thursdayMap.get(columnKey)+1);
								break;
							case Calendar.FRIDAY:
								fridayMap.put(columnKey, fridayMap.get(columnKey)+1);
								break;
							case Calendar.SATURDAY:
								saturdayMap.put(columnKey, saturdayMap.get(columnKey)+1);
								break;
							default:
								break;
							}
						}
					}
				}
			}
			
			NovaDBConnection conn = dmo.getConn(realDs);
			stmt = conn.prepareStatement(fetchSQL);
			rs = stmt.executeQuery();
			
			//清空原表
			String sql = "truncate table "+code;
			dmo.executeUpdateByDS(DatabaseConst.DATASOURCE_AVE, sql);
			for(String columnName : inputColumn){
				sql = "insert into "+code+"(" +
						"columnName," +
						"sunday," +
						"monday," +
						"tuesday," +
						"wednesday," +
						"thursday," +
						"friday," +
						"saturday"+
						")values(?,?,?,?,?,?,?,?)";
				dmo.executeUpdateByDS(DatabaseConst.DATASOURCE_AVE, sql,
						columnName,
						sundayMap.get(columnName),
						mondayMap.get(columnName),
						tuesdayMap.get(columnName),
						wednesdayMap.get(columnName),
						thursdayMap.get(columnName),
						fridayMap.get(columnName),
						saturdayMap.get(columnName));
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
		
		logger.debug("周期分析器[code="+code+"]分析数据完毕");
	}

}
