/**
 * 
 */
package smartx.bam.bs.common;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import smartx.bam.vo.DatabaseConst;
import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.utils.StringUtil;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaLogger;
import smartx.framework.common.vo.SimpleHashVO;

/**
 * @author caohenghui
 * Jun 19, 2012
 */
public class ExamineChartService {
	
	private Logger logger = NovaLogger.getLogger(this.getClass());
	
	public Map<String,Object> getGroupUserData(String groupValue){
		CommDMO dmo = new CommDMO();
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			HashVO[] vos = dmo.getHashVoArrayByDS(DatabaseConst.DS_SMARTDQ, "select distinct batchno from dq_examine_result where groupvalue=? order by batchno desc", groupValue);
			if(vos != null && vos.length >0){
				String currentBatchNo = vos[0].getStringValue("batchno");
				String lastBatchNo = vos[0].getStringValue("batchno");
				if(vos.length >=2){
					lastBatchNo = vos[1].getStringValue("batchno");
				}
				
				String countSql = "select count(1) cou from (select t.groupvalue, t.grouplabel, round(sum(t.score) / sum(t.totalscore) * 100,1) score from dq_examine_result t where t.batchno = '"+currentBatchNo+"' group by t.groupvalue, t.grouplabel order by score desc)";
				HashVO[] couVos = dmo.getHashVoArrayByDS(DatabaseConst.DS_SMARTDQ,countSql);
				int count = 0;
				if(couVos != null && couVos.length>0){
					count = couVos[0].getIntegerValue("cou");
				}
				
				String dataSql = "select * from (select groupvalue,grouplabel,score,rownum rn from (select t.groupvalue, t.grouplabel, round(sum(t.score) / sum(t.totalscore) * 100,1) score from dq_examine_result t where t.batchno = '"+currentBatchNo+"' group by t.groupvalue, t.grouplabel order by score desc) ) where groupvalue = '"+groupValue+"'";
				HashVO[] dataVos = dmo.getHashVoArrayByDS(DatabaseConst.DS_SMARTDQ,dataSql);
				if(dataVos != null && dataVos.length >0){
					int temp = (int) (count*0.2);
					double score = new Double(StringUtil.trim(dataVos[0].getStringValue("score")));
					map.put("score", score+"");
					
					int rank = dataVos[0].getIntegerValue("rn");
					map.put("rank", rank+"");
					
					map.put("rankRate", "normal");
					if(rank <= temp){
						map.put("rankRate", "first20");
					}
					if(rank >= (count - temp)){
						map.put("rankRate", "last20");
					}
				}
				
				Map<String,Object> detailMap = new HashMap<String,Object>();
				map.put("detail", detailMap);
				
				String dataSql2 = "select * from (select groupvalue,grouplabel,score,rownum rn from (select t.groupvalue, t.grouplabel, round(sum(t.score) / sum(t.totalscore) * 100,1) score from dq_examine_result t where t.batchno = '"+lastBatchNo+"' group by t.groupvalue, t.grouplabel order by score desc) ) where groupvalue = '"+groupValue+"'";
				HashVO[] dataVos2 = dmo.getHashVoArrayByDS(DatabaseConst.DS_SMARTDQ,dataSql2);
				if(dataVos2 != null && dataVos2.length >0){
					
					double score = new Double(StringUtil.trim(dataVos2[0].getStringValue("score")));
					detailMap.put("lastScore", score+"");
					
					int rank = dataVos2[0].getIntegerValue("rn");
					detailMap.put("lastRank", rank+"");
					
				}
				
				String tempSql = "select (select count(d.id) totalcount from dq_examine_result t, dq_examine_resultdetail d " +
						" where t.groupvalue = "+groupValue+" and t.batchno = '"+currentBatchNo+"' and t.id = d.examineresultid) totalcount,(select count(d.id) recount from dq_examine_result t, dq_examine_resultdetail d " +
						" where t.groupvalue = "+groupValue+" and t.batchno = '"+currentBatchNo+"' and t.id = d.examineresultid and d.score <d.totalscore) recount,(select to_char(t.createtime,'YYYY/MM/DD') from dq_examine_result t " +
					    " where t.batchno = '"+currentBatchNo+"' and rownum<=1) createtime from dual";
				HashVO[] countVos = dmo.getHashVoArrayByDS(DatabaseConst.DS_SMARTDQ,tempSql);
				if(countVos != null && countVos.length>0){
					detailMap.put("totalcount", countVos[0].getIntegerValue("totalcount"));
					detailMap.put("recount", countVos[0].getIntegerValue("recount"));
					detailMap.put("createtime", countVos[0].getStringValue("createtime"));
				}
				
				String highRankSql = "select (d.score-d.totalscore) rescore,d.* from dq_examine_result t, dq_examine_resultdetail d where t.groupvalue = "+groupValue+" and t.batchno = '"+currentBatchNo+"' and t.id = d.examineresultid and d.score <=(d.totalscore*0.1) order by d.indicatorcode";
				HashVO[] highRankVos = dmo.getHashVoArrayByDS(DatabaseConst.DS_SMARTDQ,highRankSql);
				if(highRankVos != null && highRankVos.length>0){
					SimpleHashVO[] simpleVos = new SimpleHashVO[highRankVos.length];
					for (int i = 0; i < highRankVos.length; i++) {
						SimpleHashVO vo = new SimpleHashVO(highRankVos[i]);
						simpleVos[i] = vo;
					}
					detailMap.put("highRank", simpleVos);
				}
				
				String middleRankSql = "select (d.score-d.totalscore) rescore,d.* from dq_examine_result t, dq_examine_resultdetail d where t.groupvalue = "+groupValue+" " +
						" and t.batchno = '"+currentBatchNo+"' and t.id = d.examineresultid and d.score < d.totalscore and " +
								" d.id not in (select d.id from dq_examine_result t, dq_examine_resultdetail d where t.groupvalue = "+groupValue+"  and t.batchno = '"+currentBatchNo+"' and t.id = d.examineresultid and d.score <=(d.totalscore*0.1)) and rownum <=5 order by d.score asc";
				HashVO[] middleRankVos = dmo.getHashVoArrayByDS(DatabaseConst.DS_SMARTDQ,middleRankSql);
				if(middleRankVos != null && middleRankVos.length>0){
					SimpleHashVO[] simpleVos = new SimpleHashVO[middleRankVos.length];
					for (int i = 0; i < middleRankVos.length; i++) {
						SimpleHashVO vo = new SimpleHashVO(middleRankVos[i]);
						simpleVos[i] = vo;
					}
					detailMap.put("middleRank", simpleVos);
				}
				
				String lowRankSql = "select (d.score-d.totalscore) rescore,d.* from dq_examine_result t, dq_examine_resultdetail d " +
						" where t.groupvalue = "+groupValue+" and t.batchno = '"+currentBatchNo+"' and t.id = d.examineresultid and d.score < d.totalscore and d.id not in (select d.id from dq_examine_result t, dq_examine_resultdetail d " +
						" where t.groupvalue = "+groupValue+" and t.batchno = '"+currentBatchNo+"' and t.id = d.examineresultid and d.score <=(d.totalscore*0.1) union select id from (select t2.id from dq_examine_result t, dq_examine_resultdetail t2 " +
						" where t.groupvalue = "+groupValue+" and t.batchno = '"+currentBatchNo+"' and t.id = t2.examineresultid and t2.score < t2.totalscore and t2.id not in (select t3.id from dq_examine_result t, dq_examine_resultdetail t3 " +
						" where t.groupvalue = "+groupValue+" and t.batchno = '"+currentBatchNo+"' and t.id = t3.examineresultid and t3.score <=(t3.totalscore*0.1)) and rownum <=5 order by t2.score asc)) order by d.score asc";
				HashVO[] lowRankVos = dmo.getHashVoArrayByDS(DatabaseConst.DS_SMARTDQ,lowRankSql);
				if(lowRankVos != null && lowRankVos.length>0){
					SimpleHashVO[] simpleVos = new SimpleHashVO[lowRankVos.length];
					for (int i = 0; i < lowRankVos.length; i++) {
						SimpleHashVO vo = new SimpleHashVO(lowRankVos[i]);
						simpleVos[i] = vo;
					}
					detailMap.put("lowRank", simpleVos);
				}
				
			}
		}catch(Exception e){
			logger.debug("",e);
		}
		return map;
	}
	
	public Map<String,Object> getProvinceData(){
		CommDMO dmo = new CommDMO();
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			HashVO[] vos = dmo.getHashVoArrayByDS(DatabaseConst.DS_SMARTDQ, "select distinct batchno from dq_examine_result order by batchno desc");
			if(vos != null && vos.length >0){
				String currentBatchNo = vos[0].getStringValue("batchno");
				String lastBatchNo = vos[0].getStringValue("batchno");
				if(vos.length >=2){
					lastBatchNo = vos[1].getStringValue("batchno");
				}
				
				String countSql = "select round(avg(score),1) avgscore,count(groupvalue) groupCount from (select t.groupvalue,t.grouplabel, round(sum(t.score) / sum(t.totalscore) * 100, 1) score from dq_examine_result t " +
						" where t.batchno = '"+currentBatchNo+"' group by t.groupvalue, t.grouplabel order by score desc)";
				HashVO[] couVos = dmo.getHashVoArrayByDS(DatabaseConst.DS_SMARTDQ,countSql);
				if(couVos != null && couVos.length>0){
					map.put("avgScore", couVos[0].getStringValue("avgscore"));
					map.put("groupCount", couVos[0].getStringValue("groupcount"));
				}
				
				String examineCountSql = "select count(distinct d.indicatorcode) examineCount from dq_examine_result r, dq_examine_resultdetail d where r.id = d.examineresultid " +
						" and r.batchno = '"+currentBatchNo+"'";
				HashVO[] exVos = dmo.getHashVoArrayByDS(DatabaseConst.DS_SMARTDQ,examineCountSql);
				if(exVos != null && exVos.length>0){
					map.put("examineCount", exVos[0].getStringValue("examineCount"));
				}
				
				String firstGroupSql = "select t.groupvalue, t.grouplabel, round(sum(t.score) / sum(t.totalscore) * 100, 1) score from dq_examine_result t where " +
						" t.batchno = '"+currentBatchNo+"' group by t.groupvalue, t.grouplabel order by score desc";
				HashVO[] fgVos = dmo.getHashVoArrayByDS(DatabaseConst.DS_SMARTDQ,firstGroupSql);
				String firstGroup = "";
				double groupScore= 0;
				if(fgVos != null && fgVos.length>0){
					for(HashVO v : fgVos){
						if(StringUtil.isEmpty(firstGroup)){
							firstGroup = v.getStringValue("grouplabel");
							groupScore = v.getDoubleValue("score");
						}else{
							if(groupScore==v.getDoubleValue("score")){
								firstGroup =  firstGroup+","+v.getStringValue("grouplabel");
							}
						}
					}
				}
				map.put("firstGroup", firstGroup);
				
				Map<String,Object> detailMap = new HashMap<String,Object>();
				map.put("detail", detailMap);
				
				//计算上次得分
				String lastCountSql = "select round(avg(score),1) avgscore from (select t.groupvalue,t.grouplabel, round(sum(t.score) / sum(t.totalscore) * 100, 1) score from dq_examine_result t " +
				" where t.batchno = '"+lastBatchNo+"' group by t.groupvalue, t.grouplabel order by score desc)";
				HashVO[] lastCouVos = dmo.getHashVoArrayByDS(DatabaseConst.DS_SMARTDQ,lastCountSql);
				if(lastCouVos != null && lastCouVos.length>0){
					detailMap.put("lastAvgScore", lastCouVos[0].getStringValue("avgscore"));
				}
				
				String infoSql = "select (select count(distinct d.indicatorcode) totalcount from dq_examine_result t, dq_examine_resultdetail d where t.id = d.examineresultid " +
						" and t.batchno = '"+currentBatchNo+"') totalcount, (select count(distinct d.indicatorcode) recount  from dq_examine_result t,dq_examine_resultdetail d where t.id = d.examineresultid and d.score < d.totalscore " +
						" and t.batchno = '"+currentBatchNo+"') recount,(select to_char(t.createtime, 'YYYY/MM/DD') from dq_examine_result t where " +
						" t.batchno = '"+currentBatchNo+"' and rownum <= 1) createtime from dual";
				HashVO[] infoCouVos = dmo.getHashVoArrayByDS(DatabaseConst.DS_SMARTDQ,infoSql);
				if(lastCouVos != null && lastCouVos.length>0){
					detailMap.put("totalcount", infoCouVos[0].getIntegerValue("totalcount"));
					detailMap.put("recount", infoCouVos[0].getIntegerValue("recount"));
					detailMap.put("createtime", infoCouVos[0].getStringValue("createtime"));
				}
				
				//计算排名
				String rankSql = "select v.*,rownum rn from v_examine_rank v where v.batchno=?";
				HashVO[] lastRankVos = dmo.getHashVoArrayByDS(DatabaseConst.DS_SMARTDQ,rankSql,lastBatchNo);
				HashVO[] currentRankVos = dmo.getHashVoArrayByDS(DatabaseConst.DS_SMARTDQ,rankSql,currentBatchNo);
				Map<Double,Integer> lastRankMap = new HashMap<Double,Integer>();
				if( lastRankVos != null ){
					int temp = 1;
					double v = 0;
					for(int i = 0 ;i<lastRankVos.length; i++){
						Double tempValue = lastRankVos[i].getDoubleValue("groupvalue");
						Double tempScore = lastRankVos[i].getDoubleValue("score");
						if(i == 0 ){
							lastRankMap.put(tempValue, temp);
							v = tempScore;
						}else{
							if(tempScore == v){
								lastRankMap.put(tempValue, temp);
							}else{
								temp++;
								v = tempScore;
								lastRankMap.put(tempValue, temp);
							}
						}
						
					}
				}
				if(currentRankVos != null && currentRankVos.length>0){
					SimpleHashVO[] simpleVos = new SimpleHashVO[currentRankVos.length];
					int tempRank = 1;
					double tempScore = 0;
					for (int i = 0; i < currentRankVos.length; i++) {
						
						HashVO temp = currentRankVos[i];
						double groupValue = temp.getDoubleValue("groupvalue");
						double score = temp.getDoubleValue("score");
						if(i == 0 ){
							tempScore = score;
						}else{
							if(tempScore == score){
								tempScore = score;
							}else{
								tempRank = tempRank+1;
								tempScore = score;
							}
						}
						
						Integer lastRank = lastRankMap.get(groupValue);
						if(lastRank != null){
							int scale = lastRank - tempRank;
							if(scale > 0 ){
								temp.setAttributeValue("scale", "↑"+Math.abs(scale));
								temp.setAttributeValue("status", "rise");
							}else if(scale <0){
								temp.setAttributeValue("scale", "↓"+Math.abs(scale));
								temp.setAttributeValue("status", "fall");
							}else{
								temp.setAttributeValue("scale", "--");
								temp.setAttributeValue("status", "normal");
							}
						}else{
							temp.setAttributeValue("scale", "--");
							temp.setAttributeValue("status", "normal");
						}
						temp.setAttributeValue("rn", tempRank);
						SimpleHashVO vo = new SimpleHashVO(temp);
						simpleVos[i] = vo;
					}
					detailMap.put("rankDetail", simpleVos);
				}
				
				//计算特别关注
				Map<String,Set<HashVO>> dataMap = new LinkedHashMap<String, Set<HashVO>>();
				HashVO[] rankInfoVos = dmo.getHashVoArrayByDS(DatabaseConst.DS_SMARTDQ,"select * from (select distinct t.batchno from dq_examine_result t order by t.batchno desc) where rownum <=30");
				if(rankInfoVos != null && rankInfoVos.length>0){
					
					for(HashVO bVo: rankInfoVos){
						String tempBatchNo = bVo.getStringValue("batchno");
						Set<HashVO> set = dataMap.get(tempBatchNo);
						if(set == null){
							set = new LinkedHashSet<HashVO>();
							dataMap.put(tempBatchNo, set);
						}
						String rankSql2 = "select v.* from v_examine_rank v where v.batchno=?";
						HashVO[] tempRankVos = dmo.getHashVoArrayByDS(DatabaseConst.DS_SMARTDQ,rankSql2,tempBatchNo);
						if( tempRankVos != null ){
							int temp = 1;
							double v = 0;
							for(int i = 0 ;i<tempRankVos.length; i++){
								Double tempScore = tempRankVos[i].getDoubleValue("score");
								if(i == 0 ){
									v = tempScore;
								}else{
									if(tempScore == v){
										v = tempScore;
									}else{
										temp++;
										v = tempScore;
									}
								}
								tempRankVos[i].setAttributeValue("rank", temp);
								set.add(tempRankVos[i]);
							}
						}
					}
					
					Set<String> keys = dataMap.keySet();
					
					Set<HashVO> currentDataSet = dataMap.get(currentBatchNo);
					HashVO firstVo = null;
					HashVO lastVo = null;
					int a = 0;
					for(HashVO currVo : currentDataSet){
						a++;
						if(currVo.getIntegerValue("rank") == 1){
							firstVo = currVo;
						}
						if(a == currentDataSet.size()){
							lastVo = currVo;
						}
					}
					
					String firstGroupLabel = firstVo.getStringValue("grouplabel");
					String lastGroupLabel = lastVo.getStringValue("grouplabel");
					
					int firstIndex = 0;
					int lastIndex = 0;
					
					//连续N次上升
					for(String key : keys){
						Set<HashVO> tempSet = dataMap.get(key);
						boolean isSerial = true;
						for(HashVO tempVo : tempSet){
							if(tempVo.getIntegerValue("rank") == 1 && tempVo.getStringValue("grouplabel").equalsIgnoreCase(firstGroupLabel)){
								firstIndex++;
								isSerial = true;
								break;
							}else{
								isSerial = false;
							}
						}
						
						if(!isSerial){
							break;
						}
					}
					
					//连续N次下降
					for(String key : keys){
						Set<HashVO> tempSet = dataMap.get(key);
						boolean isSerial = true;
						int b = 0;
						for(HashVO tempVo : tempSet){
							b++;
							if(b == tempSet.size() &&tempVo.getStringValue("grouplabel").equalsIgnoreCase(lastGroupLabel) ){
								lastIndex++;
								isSerial = true;
								break;
							}else{
								isSerial = false;
							}
						}
						if(!isSerial){
							break;
						}
					}
					
					detailMap.put("firstGroupLabel", firstGroupLabel);
					detailMap.put("firstGroupCount", firstIndex);
					detailMap.put("lastGroupLabel", lastGroupLabel);
					detailMap.put("lastGroupCount", lastIndex);
					
					//计算上升最快和下降最快的
					Set<HashVO> currentRankSet = dataMap.get(currentBatchNo);
					Set<HashVO> lastRankSet = dataMap.get(lastBatchNo);
					Map<String,Integer> lastTempRankMap = new HashMap<String,Integer>();
					for(HashVO lastItem : lastRankSet){
						lastTempRankMap.put(lastItem.getStringValue("grouplabel"), lastItem.getIntegerValue("rank"));
					}
					
					String fastLabel = "";
					int fastScale = 0;
					
					String slowestLabel = "";
					int slowestScale = 0;
					
					for(HashVO currVo : currentRankSet){
						String label = currVo.getStringValue("grouplabel");
						int rank = currVo.getIntegerValue("rank");
						Integer lastRank = lastTempRankMap.get(label);
						int temp = lastRank - rank;
						if(temp > 0){
							if(temp > fastScale){
								fastLabel = label;
								fastScale = temp;
							}else if(temp == fastScale){
								if(StringUtil.isEmpty(fastLabel)){
									fastLabel = label;
								}else{
									fastLabel = fastLabel+","+label;
								}
								fastScale = temp;
							}
						}else if(temp < 0 ){
							if(Math.abs(temp) > Math.abs(slowestScale)){
								slowestLabel = label;
								slowestScale = Math.abs(temp);
							}else if(Math.abs(temp) ==  Math.abs(slowestScale)){
								if(StringUtil.isEmpty(slowestLabel)){
									slowestLabel = label;
								}else{
									slowestLabel = slowestLabel+","+label;
								}
								slowestScale = Math.abs(temp);
							}
						}
						
					}
					
					detailMap.put("fastLabel", fastLabel);
					detailMap.put("fastScale", fastScale);
					detailMap.put("slowestLabel", slowestLabel);
					detailMap.put("slowestScale",  Math.abs(slowestScale));
					
					//计算连续上升或连续下降的数据
					Map<String,Integer> riseAndLowMap = new HashMap<String,Integer>();
					Map<String,Integer> rMap = new HashMap<String,Integer>();
					Map<String,Boolean> rStatrus = new HashMap<String,Boolean>();
					
					Map<String,Integer> lMap = new HashMap<String,Integer>();
					Map<String,Boolean> lStatrus = new HashMap<String,Boolean>();
					
					for(String key : keys){
						Set<HashVO> tempSet = dataMap.get(key);
						if(key.equalsIgnoreCase(currentBatchNo)){
							for(HashVO vo : tempSet){
								riseAndLowMap.put(vo.getStringValue("grouplabel"), vo.getIntegerValue("rank"));
								
								rMap.put(vo.getStringValue("grouplabel"), 0);
								rStatrus.put(vo.getStringValue("grouplabel"), true);
								
								lMap.put(vo.getStringValue("grouplabel"), 0);
								lStatrus.put(vo.getStringValue("grouplabel"), true);
							}
						}else{
							for(HashVO vo : tempSet){
								String groupLabel = vo.getStringValue("grouplabel");
								Integer rank = vo.getIntegerValue("rank");
								Integer tempRank = riseAndLowMap.get(groupLabel);
								if(rank < tempRank){
									if(lStatrus.get(groupLabel)){
										lMap.put(groupLabel, lMap.get(groupLabel)+1);
									}
									rStatrus.put(groupLabel, false);
								}
								if(rank > tempRank){
									if(rStatrus.get(groupLabel)){
										rMap.put(groupLabel, rMap.get(groupLabel)+1);
									}
									lStatrus.put(groupLabel, false);
								}
								if(rank == tempRank){
									rStatrus.put(groupLabel, false);
									lStatrus.put(groupLabel, false);
								}
								riseAndLowMap.put(groupLabel, rank);
							}
						}
					}
					
					Set<String> rKeys = rMap.keySet();
					int tempR = 0;
					String rGroupLabel = "";
					for(String key : rKeys ){
						if(rMap.get(key)>tempR){
							tempR = rMap.get(key);
							rGroupLabel = key;
						}else if(rMap.get(key)==tempR){
							if(StringUtil.isEmpty(rGroupLabel)){
								rGroupLabel = key;
							}else{
								rGroupLabel = rGroupLabel+","+key;
							}
							tempR = rMap.get(key);
						}
					}
					
					Set<String> lKeys = lMap.keySet();
					int tempL = 0;
					String lGroupLabel = "";
					for(String key : lKeys ){
						if(lMap.get(key)>tempL){
							tempL = lMap.get(key);
							lGroupLabel = key;
						}else if(lMap.get(key)==tempL){
							if(StringUtil.isEmpty(lGroupLabel)){
								lGroupLabel = key;
							}else{
								lGroupLabel = lGroupLabel+","+key;
							}
							tempL = lMap.get(key);
						}
					}
					
					detailMap.put("serialRiseCount", tempR);
					detailMap.put("serialRiseLabel", rGroupLabel);
					detailMap.put("serialLowCount", tempL);
					detailMap.put("serialLowLabel", lGroupLabel);
					
					
					//严重问题,有限问题,普通问题
					
					String codeStr = "";
					
					String highQuestionSql = "select indicatorcode,indicatorname,(score-totalscore) rescore from (select d.indicatorcode,d.indicatorname,round(avg(d.score),1) score,round(avg(d.totalscore),1) totalscore from dq_examine_result t, dq_examine_resultdetail d where t.id = d.examineresultid and d.score<d.totalscore  and t.batchno = '"+currentBatchNo+"' group by d.indicatorcode,d.indicatorname) where score <=(totalscore*0.1)";
					HashVO[] highVos = dmo.getHashVoArrayByDS(DatabaseConst.DS_SMARTDQ, highQuestionSql);
					if(highVos != null && highVos.length>0){
						SimpleHashVO[] simpleVos = new SimpleHashVO[highVos.length];
						int index = 0;
						for (HashVO vo : highVos) {
							
							String indicatorcode = vo.getStringValue("indicatorcode");
							
							if(codeStr.trim().equalsIgnoreCase("")){
								codeStr = "'"+indicatorcode+"'";
							}else{
								codeStr = codeStr+",'"+indicatorcode+"'";
							}
							
							String tempSql = "select distinct t.batchno,t.grouplabel,d.indicatorcode,d.indicatorname,(d.score-d.totalscore) rescore from dq_examine_result t, dq_examine_resultdetail d where t.id = d.examineresultid and d.score<d.totalscore and t.batchno = '"+currentBatchNo+"' " +
									" and d.indicatorcode='"+indicatorcode+"' " +
									" order by rescore desc";
							HashVO[] tempVos = dmo.getHashVoArrayByDS(DatabaseConst.DS_SMARTDQ, tempSql);
							if(tempVos != null && tempVos.length>0){
								String mingrouplabel = "";
								double minrescore = 0;
								
								String maxgrouplabel = "";
								double maxrescore = 0;
								
								for(int i=0;i<tempVos.length;i++){
									String tempMingrouplabel = tempVos[i].getStringValue("grouplabel");
									double tempRescore = tempVos[i].getDoubleValue("rescore");
									if(i == 0){
										mingrouplabel = tempMingrouplabel;
										minrescore = tempRescore;
									}else{
										if(tempRescore == minrescore){
											mingrouplabel = mingrouplabel+","+tempMingrouplabel;
										}
									}
								}
								
								for(int i=(tempVos.length-1);i>=0;i--){
									String tempMaxgrouplabel = tempVos[i].getStringValue("grouplabel");
									double tempRescore = tempVos[i].getDoubleValue("rescore");
									if(i == (tempVos.length-1)){
										maxgrouplabel = tempMaxgrouplabel;
										maxrescore = tempRescore;
									}else{
										if(tempRescore == minrescore){
											maxgrouplabel = tempMaxgrouplabel+","+maxgrouplabel;
										}
									}
								}
								
								vo.setAttributeValue("mingrouplabel", mingrouplabel);
								vo.setAttributeValue("minrescore", minrescore);
								vo.setAttributeValue("maxgrouplabel", maxgrouplabel);
								vo.setAttributeValue("maxrescore", maxrescore);
							}
							
							SimpleHashVO shvo = new SimpleHashVO(vo);
							simpleVos[index] = shvo;
							index++;
						}
						detailMap.put("highQuestion", simpleVos);
					}
					
					String middleQuestionSql = "select * from (select indicatorcode,indicatorname,(score-totalscore) rescore from (select d.indicatorcode,d.indicatorname,round(avg(d.score),1) score,round(avg(d.totalscore),1) totalscore from dq_examine_result t, dq_examine_resultdetail d where t.id = d.examineresultid and d.score<d.totalscore  and t.batchno = '"+currentBatchNo+"' group by d.indicatorcode,d.indicatorname) where score <totalscore " 
						    + (codeStr.equals("")?"":" and indicatorcode not in ("+codeStr+")")+
							"order by rescore asc) where rownum<=5";
					HashVO[] middleVos = dmo.getHashVoArrayByDS(DatabaseConst.DS_SMARTDQ, middleQuestionSql);
					if(middleVos != null && middleVos.length>0){
						SimpleHashVO[] simpleVos = new SimpleHashVO[middleVos.length];
						int index = 0;
						for (HashVO vo : middleVos) {
							
							String indicatorcode = vo.getStringValue("indicatorcode");
							
							if(codeStr.trim().equalsIgnoreCase("")){
								codeStr = "'"+indicatorcode+"'";
							}else{
								codeStr = codeStr+",'"+indicatorcode+"'";
							}
							
							String tempSql = "select distinct t.batchno,t.grouplabel,d.indicatorcode,d.indicatorname,(d.score-d.totalscore) rescore from dq_examine_result t, dq_examine_resultdetail d where t.id = d.examineresultid and d.score<d.totalscore and t.batchno = '"+currentBatchNo+"' " +
									"and d.indicatorcode='"+indicatorcode+"' " +
									"order by rescore desc";
							
							HashVO[] tempVos = dmo.getHashVoArrayByDS(DatabaseConst.DS_SMARTDQ, tempSql);
							if(tempVos != null && tempVos.length>0){
								String mingrouplabel = "";
								double minrescore = 0;
								
								String maxgrouplabel = "";
								double maxrescore = 0;
								
								for(int i=0;i<tempVos.length;i++){
									String tempMingrouplabel = tempVos[i].getStringValue("grouplabel");
									double tempRescore = tempVos[i].getDoubleValue("rescore");
									if(i == 0){
										mingrouplabel = tempMingrouplabel;
										minrescore = tempRescore;
									}else{
										if(tempRescore == minrescore){
											mingrouplabel = mingrouplabel+","+tempMingrouplabel;
										}
									}
								}
								
								for(int i=(tempVos.length-1);i>=0;i--){
									String tempMaxgrouplabel = tempVos[i].getStringValue("grouplabel");
									double tempRescore = tempVos[i].getDoubleValue("rescore");
									if(i == (tempVos.length-1)){
										maxgrouplabel = tempMaxgrouplabel;
										maxrescore = tempRescore;
									}else{
										if(tempRescore == minrescore){
											maxgrouplabel = tempMaxgrouplabel+","+maxgrouplabel;
										}
									}
								}
								
								vo.setAttributeValue("mingrouplabel", mingrouplabel);
								vo.setAttributeValue("minrescore", minrescore);
								vo.setAttributeValue("maxgrouplabel", maxgrouplabel);
								vo.setAttributeValue("maxrescore", maxrescore);
							}
							
							SimpleHashVO shvo = new SimpleHashVO(vo);
							simpleVos[index] = shvo;
							index++;
						}
						detailMap.put("middleQuestion", simpleVos);
					}
					
					String lowQuestionSql = "select * from (select indicatorcode,indicatorname,(score-totalscore) rescore from (select d.indicatorcode,d.indicatorname,round(avg(d.score),1) score,round(avg(d.totalscore),1) totalscore from dq_examine_result t, dq_examine_resultdetail d where t.id = d.examineresultid and d.score<d.totalscore  and t.batchno = '"+currentBatchNo+"' group by d.indicatorcode,d.indicatorname) where score <totalscore " 
					    + (codeStr.equals("")?"":" and indicatorcode not in ("+codeStr+")")+
						"order by rescore asc) ";
				HashVO[] lowVos = dmo.getHashVoArrayByDS(DatabaseConst.DS_SMARTDQ, lowQuestionSql);
				if(lowVos != null && lowVos.length>0){
					SimpleHashVO[] simpleVos = new SimpleHashVO[lowVos.length];
					int index = 0;
					for (HashVO vo : lowVos) {
						
						String indicatorcode = vo.getStringValue("indicatorcode");
						
						String tempSql = "select distinct t.batchno,t.grouplabel,d.indicatorcode,d.indicatorname,(d.score-d.totalscore) rescore from dq_examine_result t, dq_examine_resultdetail d where t.id = d.examineresultid and d.score<d.totalscore and t.batchno = '"+currentBatchNo+"' " +
								"and d.indicatorcode='"+indicatorcode+"' " +
								"order by rescore desc";
						
						HashVO[] tempVos = dmo.getHashVoArrayByDS(DatabaseConst.DS_SMARTDQ, tempSql);
						if(tempVos != null && tempVos.length>0){
							String mingrouplabel = "";
							double minrescore = 0;
							
							String maxgrouplabel = "";
							double maxrescore = 0;
							
							for(int i=0;i<tempVos.length;i++){
								String tempMingrouplabel = tempVos[i].getStringValue("grouplabel");
								double tempRescore = tempVos[i].getDoubleValue("rescore");
								if(i == 0){
									mingrouplabel = tempMingrouplabel;
									minrescore = tempRescore;
								}else{
									if(tempRescore == minrescore){
										mingrouplabel = mingrouplabel+","+tempMingrouplabel;
									}
								}
							}
							
							for(int i=(tempVos.length-1);i>=0;i--){
								String tempMaxgrouplabel = tempVos[i].getStringValue("grouplabel");
								double tempRescore = tempVos[i].getDoubleValue("rescore");
								if(i == (tempVos.length-1)){
									maxgrouplabel = tempMaxgrouplabel;
									maxrescore = tempRescore;
								}else{
									if(tempRescore == minrescore){
										maxgrouplabel = tempMaxgrouplabel+","+maxgrouplabel;
									}
								}
							}
							
							vo.setAttributeValue("mingrouplabel", mingrouplabel);
							vo.setAttributeValue("minrescore", minrescore);
							vo.setAttributeValue("maxgrouplabel", maxgrouplabel);
							vo.setAttributeValue("maxrescore", maxrescore);
						}
						
						SimpleHashVO shvo = new SimpleHashVO(vo);
						simpleVos[index] = shvo;
						index++;
					}
					detailMap.put("lowQuestion", simpleVos);
				}
					
				}
			}
		}catch(Exception e){
			logger.debug("",e);
		}
		return map;
	}
}
