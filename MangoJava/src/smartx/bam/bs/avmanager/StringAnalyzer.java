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

public class StringAnalyzer extends Analyzer {
	
	
	public StringAnalyzer(String code, String metadataXML) throws Exception{
		super(code,metadataXML);
		this.analyzerType = SysConst.AVANALYZERTYPE_STRINGANALYZER;
	}
	@Override
	public void analyze() {
		logger.debug("字符串分析器[code="+code+"]开始分析数据...");
		
		if(isAnalyzing){
			return;
		}
		
		isAnalyzing = true;

		int rowCount = 0;
		Map<String,Integer> rowCountMap = new HashMap<String, Integer>();
		Map<String,Integer> nullCountMap = new HashMap<String, Integer>();
		Map<String,Integer> totalCharCountMap = new HashMap<String, Integer>();
		Map<String,Integer> entirelyUppercaseCountMap = new HashMap<String,Integer>();
		Map<String,Integer> entirelyLowercaseCountMap = new HashMap<String,Integer>();
		Map<String,Integer> maxCharsMap = new HashMap<String,Integer>();
		Map<String,Integer> minCharsMap = new HashMap<String,Integer>();
		Map<String,Integer> avgCharsMap = new HashMap<String,Integer>();
		Map<String,Integer> maxWhiteSpacesMap = new HashMap<String,Integer>();
		Map<String,Integer> minWhiteSpacesMap = new HashMap<String,Integer>();
		Map<String,Integer> avgWhiteSpacesMap = new HashMap<String,Integer>();
		Map<String,Integer> uppercaseCharsMap = new HashMap<String,Integer>();
		Map<String,Integer> lowercaseCharsMap = new HashMap<String,Integer>();
		Map<String,Integer> digitCharsMap = new HashMap<String,Integer>();
		Map<String,Integer> diacriticCharsMap = new HashMap<String,Integer>();
		Map<String,Integer> noLetterCharsMap = new HashMap<String,Integer>();
		Map<String,Integer> wordCountMap = new HashMap<String,Integer>();
		Map<String,Integer> maxWordsMap = new HashMap<String,Integer>();
		Map<String,Integer> minWordsMap = new HashMap<String,Integer>();
		
		Map<String,Integer> whiteSpacesCountMap = new HashMap<String,Integer>();
		//初始化结果Map
		for(String columnKey : inputColumn){
			rowCountMap.put(columnKey, 0);
			nullCountMap.put(columnKey, 0);
			totalCharCountMap.put(columnKey, 0);
			entirelyUppercaseCountMap.put(columnKey, 0);
			entirelyLowercaseCountMap.put(columnKey, 0);
			maxCharsMap.put(columnKey, 0);
			minCharsMap.put(columnKey, 0);
			avgCharsMap.put(columnKey, 0);
			maxWhiteSpacesMap.put(columnKey, 0);
			minWhiteSpacesMap.put(columnKey, 0);
			avgWhiteSpacesMap.put(columnKey, 0);
			uppercaseCharsMap.put(columnKey, 0);
			lowercaseCharsMap.put(columnKey, 0);
			digitCharsMap.put(columnKey, 0);
			diacriticCharsMap.put(columnKey, 0);
			noLetterCharsMap.put(columnKey, 0);
			wordCountMap.put(columnKey, 0);
			maxWordsMap.put(columnKey, 0);
			minWordsMap.put(columnKey, 0);
			
			whiteSpacesCountMap.put(columnKey, 0);
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
						rowCountMap.put(columnKey, rowCountMap.get(columnKey)+1);
						if(value == null)
							nullCountMap.put(columnKey, nullCountMap.get(columnKey)+1);
						else{
							
							totalCharCountMap.put(columnKey, totalCharCountMap.get(columnKey)+value.length());
							
							if(isEntirelyUppercase(value)){
								entirelyUppercaseCountMap.put(columnKey,entirelyUppercaseCountMap.get(columnKey)+1);
							}
							
							if(isEntirelyLowercase(value)){
								entirelyLowercaseCountMap.put(columnKey,entirelyLowercaseCountMap.get(columnKey)+1);
							}
							
							if(rowCountMap.get(columnKey)<=1){
								maxCharsMap.put(columnKey, value.length());
								minCharsMap.put(columnKey, value.length());
							}else{
								int length = value.length();
								if(length>maxCharsMap.get(columnKey)){
									maxCharsMap.put(columnKey, length);
								}
								if(length<minCharsMap.get(columnKey)){
									minCharsMap.put(columnKey, length);
								}
							}
							
							avgCharsMap.put(columnKey, totalCharCountMap.get(columnKey)/rowCountMap.get(columnKey));
							
							dealWhiteSpacesHander(value,columnKey,rowCountMap.get(columnKey),maxWhiteSpacesMap, minWhiteSpacesMap,avgWhiteSpacesMap,whiteSpacesCountMap);
							
							dealCharHander(value,columnKey,uppercaseCharsMap,lowercaseCharsMap,digitCharsMap,diacriticCharsMap,noLetterCharsMap);
							
							dealWordHander(value,columnKey,rowCountMap.get(columnKey),wordCountMap,maxWordsMap,minWordsMap);
							
						}
					}
				}
			}

			
			//清空原表
			String sql = "truncate table "+code;
			dmo.executeUpdateByDS(DatabaseConst.DATASOURCE_AVE, sql);
			for(String columnName : inputColumn){
				sql = "insert into "+code+"(ColumnName," +
						"RowCount," +
						"NullCount," +
						"TotalCharCount,EntirelyUppercaseCount,EntirelyLowercaseCount,MaxChars,MinChars,AvgChars,MaxWhiteSpaces,MinWhiteSpaces,AvgWhiteSpaces" +
						",UppercaseChars,LowercaseChars,DigitChars,DiacriticChars,NoLetterChars,WordCount,MaxWords,MinWords" +
						")values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				dmo.executeUpdateByDS(DatabaseConst.DATASOURCE_AVE, sql,columnName,rowCountMap.get(columnName),
						nullCountMap.get(columnName),totalCharCountMap.get(columnName),
						entirelyUppercaseCountMap.get(columnName),
						entirelyLowercaseCountMap.get(columnName),
						maxCharsMap.get(columnName),
						minCharsMap.get(columnName),
						avgCharsMap.get(columnName),
						maxWhiteSpacesMap.get(columnName),
						minWhiteSpacesMap.get(columnName),
						avgWhiteSpacesMap.get(columnName),
						uppercaseCharsMap.get(columnName),
						lowercaseCharsMap.get(columnName),
						digitCharsMap.get(columnName),
						diacriticCharsMap.get(columnName),
						noLetterCharsMap.get(columnName),
						wordCountMap.get(columnName),
						maxWordsMap.get(columnName),
						minWordsMap.get(columnName));
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
		
		logger.debug("字符串分析器[code="+code+"]分析数据完毕");
	}
	
	private void dealWhiteSpacesHander(String value,String columnName,int rowCount,
			Map<String,Integer> maxWhiteSpacesMap,
			Map<String,Integer> minWhiteSpacesMap,
			Map<String,Integer> avgWhiteSpacesMap,
			Map<String,Integer> whiteSpacesCountMap){
		try{
			int maxWhitespaceLength = maxWhiteSpacesMap.get(columnName);
			int minWhitespaceLength = minWhiteSpacesMap.get(columnName);
			
			Set<Integer> set = new HashSet<Integer>();
			int whitespaceLength = 0;
			boolean isSequentialWhitespace = false;
			byte[] bytes = value.getBytes();
			for(int i=0;i<bytes.length;i++){
				byte b = bytes[i];
				if(Character.isWhitespace(b)){
					if(isSequentialWhitespace){
						whitespaceLength = whitespaceLength + 1 ;
					}else{
						whitespaceLength = 1;
					}
					whiteSpacesCountMap.put(columnName, whiteSpacesCountMap.get(columnName)+1);
					isSequentialWhitespace = true;
					continue;
				}else{
					if(whitespaceLength>0){
						set.add(whitespaceLength);
					}
					whitespaceLength = 0;
					isSequentialWhitespace = false;
				}

			}
			
			if(maxWhitespaceLength>0){
				set.add(maxWhitespaceLength);
			}
			if(minWhitespaceLength>0){
				set.add(minWhitespaceLength);
			}
			if(set.size()>0){
				 Object[] obj =	set.toArray();
				 int maxValue = (Integer)obj[0];
				 int minValue = (Integer)obj[0];
				 for(int i=1;i<obj.length;i++){
					 int t = (Integer)obj[i];
					 if(t>maxValue){
						 maxValue = t;
					 }
					 
					 if(t<minValue){
						 minValue = t;
					 }
				 }
				
				 maxWhiteSpacesMap.put(columnName, maxValue);
				 minWhiteSpacesMap.put(columnName, minValue);
				 
			}
			
			avgWhiteSpacesMap.put(columnName, whiteSpacesCountMap.get(columnName)/rowCount);
		}catch(Exception e){
			
		}
	}
	
	private void dealCharHander(String value,String columnName,
			Map<String,Integer> uppercaseCharsMap,
			Map<String,Integer> lowercaseCharsMap,
			Map<String,Integer> digitCharsMap,
			Map<String,Integer> diacriticCharsMap,
			Map<String,Integer> noLetterCharsMap){
		try{
			
			byte[] bytes = value.getBytes();
			
			for(byte b : bytes){
				
				if(Character.isUpperCase(b)){
					uppercaseCharsMap.put(columnName, uppercaseCharsMap.get(columnName)+1);
				}
				if(Character.isLowerCase(b)){
					lowercaseCharsMap.put(columnName, lowercaseCharsMap.get(columnName)+1);
				}
				if(Character.isDigit(b)){
					digitCharsMap.put(columnName, digitCharsMap.get(columnName)+1);
				}
				if(!Character.isLetter(b)){
					noLetterCharsMap.put(columnName, noLetterCharsMap.get(columnName)+1);
				}
			}
		}catch(Exception e){
		}
	}
	
	private void dealWordHander(String value,String columnName,int rowCount,
			Map<String,Integer> wordCountMap,
			Map<String,Integer> maxWordsMap,
			Map<String,Integer> minWordsMap){
		
		try{
			String[] words = value.split(" ");
			for(String word : words){
				if(word != null && !word.trim().equals("")){
					wordCountMap.put(columnName, wordCountMap.get(columnName)+1);
					int wordLength = word.length();
					int maxWords = maxWordsMap.get(columnName);
					int minWords = minWordsMap.get(columnName);
					
					if(maxWords<=0){
						maxWordsMap.put(columnName, wordLength);
					}else{
						if(wordLength>maxWords){
							maxWordsMap.put(columnName, wordLength);
						}
					}
					
					if(minWords<=0){
						minWordsMap.put(columnName, wordLength);
					}else{
						if(wordLength<minWords){
							if(wordLength>0){
								minWordsMap.put(columnName, wordLength);
							}
						}
					}
				}
			}
			
		}catch(Exception e){
			
		}
	}
	
	private boolean isEntirelyUppercase(String value){
		boolean flag = true;
		try{
			
			byte[] bytes = value.replaceAll(" ", "").getBytes();
			for(byte b : bytes){
				if(!Character.isUpperCase(b)){
					flag = false;
					break;
				}
			}
		}catch(Exception e){
			flag = false;
		}
		return flag;
	}
	
	private boolean isEntirelyLowercase(String value){
		boolean flag = true;
		try{
			
			byte[] bytes = value.replaceAll(" ", "").getBytes();
			for(byte b : bytes){
				if(!Character.isLowerCase(b)){
					flag = false;
					break;
				}
			}
		}catch(Exception e){
			flag = false;
		}
		return flag;
	}


	@Override
	public String getResultStructSQL() {
		return "create table "+code+"(" +
				"ColumnName varchar2(100)," +
				"RowCount int default 0," +
				"NullCount int default 0," +
				"EntirelyUppercaseCount int default 0," +
				"EntirelyLowercaseCount int default 0," +
				"TotalCharCount int default 0," +
				"MaxChars int default 0," +
				"MinChars int default 0," +
				"AvgChars DECIMAL default 0," +
				"MaxWhiteSpaces int default 0," +
				"MinWhiteSpaces int default 0," +
				"AvgWhiteSpaces DECIMAL default 0," +
				"UppercaseChars int default 0," +
				"LowercaseChars int default 0," +
				"DigitChars int default 0," +
				"DiacriticChars int default 0," +
				"NoLetterChars int default 0," +
				"WordCount int default 0," +
				"MaxWords int default 0," +
				"MinWords int default 0" +
				")";
	}

}
