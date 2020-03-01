/**
 * 
 */
package smartx.bam.bs.report;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.utils.StringUtil;
import smartx.framework.common.vo.DMOConst;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaLogger;
import smartx.publics.datatask.CommTaskManager;

/**
 * @author caohenghui
 * Feb 1, 2012
 */
public class ReportService {
	
	private final Logger logger = NovaLogger.getLogger(this.getClass());
	
	public Map[] getReportComponentHashVO(String sql){
		
		Map<String,String>[] maps = null;
		
		CommDMO dmo = new CommDMO();
		
		try{
			if(!StringUtil.isEmpty(sql)){
				
				String[] sqlInfo = sql.split(";");
				if(sqlInfo != null ){
					String fetchSQL = sqlInfo[0];
					String dsName = "";
		            if (sqlInfo.length == 1) {
		            	dsName = null;
		            } else if (sqlInfo.length == 2) {
		            	dsName = getDataSourceName(sqlInfo[1]);
		            }
		            
		            HashVO[] vos = dmo.getHashVoArrayByDS(dsName, fetchSQL);
		            if(vos != null && vos.length >0 ){
		            	maps = new HashMap[vos.length];
		            	int i = 0 ;
		            	for(HashVO vo : vos){
		            		Map<String,String> map = new HashMap<String,String>();
		            		map.put("ID", vo.getStringValue(0));
		            		map.put("CODE", vo.getStringValue(1));
		            		map.put("NAME", vo.getStringValue(2));
		            		maps[i] = map;
		            		i++;
		            	}
		            }
				}
			}
		}catch(Exception e){
			logger.debug("", e);
		}
		
		return maps;
	}
	
	public String onReportQuery(String taskCode,Map<String,String> map) throws Exception{
		String version = null;
		CommDMO dmo = new CommDMO();
		try{
			version = this.getVersion();
			map.put("version", version);
			if(!StringUtil.isEmpty(taskCode)){
				String instanceId = createTaskAndStart(taskCode,map); //createTaskInstance(taskCode,map);
				long startTime = System.currentTimeMillis();
				while(true){
					HashVO[] vos = dmo.getHashVoArrayByDS(null, "SELECT STATUS,LASTMASSAGE FROM PUB_DATATASK WHERE ID=?",instanceId);
					if(vos != null && vos.length >0 ){
						int status = vos[0].getIntegerValue("STATUS");
						if(status == 9){//任务正常结束
							break;
						}else if( status == 10){//任务异常结束
							throw new Exception("查询任务执行失败，原因：" + vos[0].getIntegerValue("LASTMASSAGE"));
						}
					}else{
						break;
					}
					
					//5分钟还没有结果就直接退出 
					long endTime = System.currentTimeMillis();
					if((endTime-startTime)>(5*60*1000)){
						break;
					}
					
					Thread.sleep(100);
				}
			}

		}catch(Exception e){
			logger.error("执行后台查询任务异常！", e);
			throw e;
		}finally{
			dmo.releaseContext(null);
		}
		
		return version;
	}
	
    private String getDataSourceName(String _des) {
        String str_new = _des;
        int li_pos = str_new.indexOf("="); //
        str_new = str_new.substring(li_pos + 1, str_new.length()).trim();

        if (str_new.startsWith("\"") || str_new.startsWith("'")) {
            str_new = str_new.substring(1, str_new.length());
        }

        if (str_new.endsWith("\"") || str_new.endsWith("'")) {
            str_new = str_new.substring(0, str_new.length() - 1);
        }

        return str_new;
    }
    
    public Map<String,String> getReportMetaDateContent(String mtCode) throws Exception {
		Map<String,String> map = new HashMap<String,String>();
		CommDMO dmo = new CommDMO();
		try {
			String sql = "select *  from pub_metadata_templet t where code=?";
			HashVO[] vos = dmo.getHashVoArrayByDS(null, sql, mtCode);
			if (vos.length == 0)
				throw new Exception("未找到元数据模板");
			HashVO vo = vos[0];
			map.put("CONTENT", vo.getStringValue("content"));
		} catch (Exception e) {
			logger.error("", e);
			throw e;
		} finally {
			dmo.releaseContext(null);
		}
		return map;
    }
    
    /**
     * 报建并启动数据任务
     * @param taskCode 任务模板编码
     * @param pmap 任务参数
     * @return
     * @throws Exception
     */
	private String createTaskAndStart(String taskCode, Map<String, String> pmap) throws Exception {
		String taskInsId = null;//数据任务实例ID
		CommDMO dmo = new CommDMO();
		
		String queryTaskSql = "SELECT P.ID,P.NAME,T.CONTENT FROM PUB_DATATASK_TEMPLET P ,PUB_METADATA_TEMPLET T " +
						" WHERE P.MTCODE = T.CODE AND T.TYPE=20 AND P.TYPE=0 AND P.CODE=?";
		HashVO[] vos = dmo.getHashVoArrayByDS(DMOConst.DS_DEFAULT,queryTaskSql,taskCode);
		if (vos != null && vos.length > 0) {
			String templateId = vos[0].getStringValue("ID");
			String taskName = vos[0].getStringValue("NAME");
			String mtContent = vos[0].getStringValue("CONTENT");
			
			//添加并启动数据任务
			taskInsId = CommTaskManager.getInstance().addAndStartTask(templateId, taskName, mtContent, pmap);
		}else{
			throw new Exception("根据数据任务编码【"+taskCode+"】未找到元模板数据任务定义！");
		}
		return taskInsId;
	}
    
	private String getVersion(){
		String version ="";
		try{
			Calendar cd = Calendar.getInstance();
			
			String year = cd.get(Calendar.YEAR)+"";
			
			int monthInt = cd.get(Calendar.MONTH)+1;
			String month = monthInt+"";
			if(monthInt<10){
				month = "0"+monthInt;
			}
			
			int dayInt = cd.get(Calendar.DAY_OF_MONTH);
			String day = dayInt+"";
			if(dayInt<10){
				day = "0"+dayInt;
			}
			
			int hourInt = cd.get(Calendar.HOUR_OF_DAY);
			String hour = hourInt+"";
			if(hourInt<10){
				hour = "0"+hourInt;
			}
			
			int minuteInt = cd.get(Calendar.MINUTE);
			String minute = minuteInt+"";
			if(minuteInt<10){
				minute = "0"+minuteInt;
			}
			
			int secondInt = cd.get(Calendar.SECOND);
			String second = secondInt+"";
			if(secondInt<10){
				second = "0"+secondInt;
			}
			
			int miSecondInt = cd.get(Calendar.MILLISECOND);
			
			version = year+month+day+hour+minute+second+miSecondInt;
			
		}catch(Exception e){
			logger.debug("",e);
		}
		return version;
	}
	
	private void setParams(Element customTaskEle,Map<String,String> map){
		try{
			
			Set<String> keys = map.keySet();
			if(keys != null){
				for(String key : keys){
					String value = map.get(key);
					Element tempEle = customTaskEle.addElement(key);
					tempEle.setText(value);
				}
			}
			
		}catch(Exception e){
			logger.debug("",e);
		}
	}
}
