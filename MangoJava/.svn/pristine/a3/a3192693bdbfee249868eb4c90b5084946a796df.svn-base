package smartx.bam.bs.avmanager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import smartx.bam.vo.DatabaseConst;
import smartx.bam.vo.SysConst;
import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.bs.NovaDBConnection;

public class DateAnalyzer extends Analyzer {

	public DateAnalyzer(String code, String metadataXML) throws Exception {
		super(code, metadataXML);
		this.analyzerType = SysConst.AVANALYZERTYPE_DATEANALYZER;
	}

	@Override
	public String getResultStructSQL() {
		
		return "create table "+code+"(" +
		"ColumnName varchar2(100)," +
		"RowCount int default 0," +
		"NullCount int default 0," +
		"highestDate varchar2(200)," +
		"lowestDate varchar2(200)," +
		"highestTime varchar2(200)," +
		"lowestTime varchar2(200)" +
		")";
	}

	@Override
	public void analyze() {

		logger.debug("分析器[code="+code+"]开始分析数据...");
		
		if(isAnalyzing){
			return;
		}
		isAnalyzing = true;
		
		int rowCount = 0;
		
		Map<String,Integer> rowCountMap = new HashMap<String, Integer>();
		Map<String,Integer> nullCountMap = new HashMap<String, Integer>();
		Map<String,String> highestDateMap = new HashMap<String, String>();
		Map<String,String> lowestDateMap = new HashMap<String,String>();
		Map<String,String> highestTimeMap = new HashMap<String,String>();
		Map<String,String> lowestTimeMap = new HashMap<String,String>();
		
		Map<String,HashSet<Long>> dateMap = new HashMap<String,HashSet<Long>>();
		Map<String,HashSet<Long>> timeMap = new HashMap<String,HashSet<Long>>();
		
		for(String columnKey : inputColumn){
			rowCountMap.put(columnKey, 0);
			nullCountMap.put(columnKey, 0);
			highestDateMap.put(columnKey, "");
			lowestDateMap.put(columnKey, "");
			highestTimeMap.put(columnKey, "");
			lowestTimeMap.put(columnKey, "");
			
			dateMap.put(columnKey, new HashSet<Long>());
			timeMap.put(columnKey, new HashSet<Long>());
		}
		
		CommDMO dmo = new CommDMO();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		DateFormat dateDf = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat timeDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
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
						Timestamp value = rs.getTimestamp(columnName);
						String columnKey = nameSpace+":"+columnName;
						rowCountMap.put(columnKey, rowCountMap.get(columnKey)+1);
						if(value == null)
							nullCountMap.put(columnKey, nullCountMap.get(columnKey)+1);
						else{
							
							dateMap.get(columnKey).add(dateDf.parse(dateDf.format(value)).getTime());
							
							timeMap.get(columnKey).add(getTimeNumber(timeDf.parse(timeDf.format(value))));
						}
					}
				}
			}


			
			for(String columnName : inputColumn){
				HashSet<Long> dateSet = dateMap.get(columnName);
				HashSet<Long> timeSet = timeMap.get(columnName);
				
				dealDateHandler(columnName,highestDateMap,lowestDateMap,dateSet);
				dealTimeHandler(columnName,highestTimeMap,lowestTimeMap,timeSet);
			}
			
			//清空原表
			String sql = "truncate table "+code;
			dmo.executeUpdateByDS(DatabaseConst.DATASOURCE_AVE, sql);
			for(String columnName : inputColumn){
				sql = "insert into "+code+"(" +
						"ColumnName," +
						"RowCount," +
						"NullCount," +
						"highestDate," +
						"lowestDate," +
						"highestTime," +
						"lowestTime" +
						")values(?,?,?,?,?,?,?)";
				dmo.executeUpdateByDS(DatabaseConst.DATASOURCE_AVE, sql,columnName,
						rowCountMap.get(columnName),
						nullCountMap.get(columnName),
						highestDateMap.get(columnName),
						lowestDateMap.get(columnName),
						highestTimeMap.get(columnName),
						lowestTimeMap.get(columnName));
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
		
		logger.debug("日期分析器[code="+code+"]分析数据完毕");

	}
	
	private long getTimeNumber(Date date){
		
		long timeNO = 0;
		try{
			
			Calendar cld = Calendar.getInstance();
			cld.setTime(date);
			
			String hour = cld.get(Calendar.HOUR_OF_DAY)+"";
			String minute = cld.get(Calendar.MINUTE)+"";
			String second = cld.get(Calendar.SECOND)+"";
			
			if(hour.length()==1){
				hour = "0"+hour;
			}
			
			if(minute.length()==1){
				minute = "0"+minute;
			}
			if(second.length()==1){
				second = "0"+second;
			}
			
			DateFormat timeDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String tempDateTime = "2000-01-01 "+hour+":"+minute+":"+second;
			
			timeNO = timeDf.parse(tempDateTime).getTime();
			
		}catch(Exception e){
			
		}
		
		return timeNO;
	}
	
	private void dealDateHandler(String columnName,Map<String,String> highestDateMap,Map<String,String> lowestDateMap,HashSet<Long> set){
		try{
			if(set.size()>0){
				 Object[] obj =	set.toArray();
				 Long maxValue = (Long)obj[0];
				 Long minValue = (Long)obj[0];
				 for(int i=1;i<obj.length;i++){
					 Long t = (Long)obj[i];
					 if(t>maxValue){
						 maxValue = t;
					 }
					 
					 if(t<minValue){
						 minValue = t;
					 }
				 }
				 
				 DateFormat dateDf = new SimpleDateFormat("yyyy-MM-dd");
				 String maxDate = dateDf.format(new Date(maxValue));
				 String minDate = dateDf.format(new Date(minValue));
				
				 highestDateMap.put(columnName, maxDate);
				 lowestDateMap.put(columnName, minDate);
				 
			}
		}catch(Exception e){
			
		}
	}
	
	private void dealTimeHandler(String columnName,Map<String,String> highestTimeMap,Map<String,String> lowestTimeMap,HashSet<Long> set){
		try{
			if(set.size()>0){
				 Object[] obj =	set.toArray();
				 Long maxValue = (Long)obj[0];
				 Long minValue = (Long)obj[0];
				 for(int i=1;i<obj.length;i++){
					 Long t = (Long)obj[i];
					 if(t>maxValue){
						 maxValue = t;
					 }
					 
					 if(t<minValue){
						 minValue = t;
					 }
				 }
				 
				 DateFormat dateDf = new SimpleDateFormat("HH:mm:ss");
				 String maxDate = dateDf.format(new Date(maxValue));
				 String minDate =dateDf.format(new Date(minValue));
				 
				 highestTimeMap.put(columnName, maxDate);
				 lowestTimeMap.put(columnName, minDate);
				 
			}
		}catch(Exception e){
			
		}
	}

}
