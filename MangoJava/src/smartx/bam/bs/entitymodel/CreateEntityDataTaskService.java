/**
 * 
 */
package smartx.bam.bs.entitymodel;

import java.util.Map;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;

import smartx.bam.vo.DatabaseConst;
import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.job.bs.JobServer;
import smartx.framework.common.utils.StringUtil;
import smartx.publics.datatask.DataTaskCronJob;

/**
 * @author sky 生成实体任务模板SERVICE
 */
public class CreateEntityDataTaskService {
	
	public static final String METADATA_HEAD = "MT_DT_";
	
	public static final String TASK_HEAD_DEFAULT = "ENTITY_DEFAULT";
	public static final String TASK_HEAD_DATAEXTRACT = "ENTITY_DATAEXTRACT";
	public static final String TASK_HEAD_DATAPROFILE= "ENTITY_DATAPROFILE";
	public static final String TASK_HEAD_RULE = "ENTITY_RULE";
	public static final String TASK_HEAD_DATACOMPARE = "ENTITY_DATACOMPARE";
	public static final String TASK_HEAD_QUALITYEXAMINATION = "ENTITY_QUALITYEXAMINATION";
	public static final String TASK_HEAD_DBDATACALCULATE = "ENTITY_DBDATACALCULATE";
	public static final String TASK_HEAD_GISCONVERT = "ENTITY_GISCONVERT";
	
	public static final String TASK_COMMENT_DEFAULT = "自定义任务";
	public static final String TASK_COMMENT_DATAEXTRACT = "数据采集";
	public static final String TASK_COMMENT_DATAPROFILE = "数据剖析";
	public static final String TASK_COMMENT_RULE = "数据规则处理任务";
	public static final String TASK_COMMENT_DATACOMPARE = "数据比对";
	public static final String TASK_COMMENT_QUALITYEXAMINATION = "质量考核";
	public static final String TASK_COMMENT_DBDATACALCULATE = "仪表盘数据计算";
	public static final String TASK_COMMENT_GISCONVERT = "GIS数据转换";
	
	public static final String TASK_CATEGORY_DEFAULT = "0";//缺省
	public static final String TASK_CATEGORY_DATAEXTRACT = "1";//数据采集
	public static final String TASK_CATEGORY_DATAPROFILE = "2";//数据剖析
	public static final String TASK_CATEGORY_RULECHECK = "3";//规则检查
	public static final String TASK_CATEGORY_DATACOMPARE = "4";//数据比对
	public static final String TASK_CATEGORY_QUALITYEXAMINATION = "5";//质量考核
	public static final String TASK_CATEGORY_DBDATACALCULATE= "6";//仪表盘数据计算
	public static final String TASK_CATEGORY_GISCONVERT = "9";//GIS数据转换
	

	public void saveEntityTaskTemplet(Map<String, String> taskInfo,String taskCategory)	throws Exception {
		
			String taskName = taskInfo.get("taskName");
			String taskCode = taskInfo.get("taskCode");
			String taskExp = taskInfo.get("taskExp");
			String loginName = taskInfo.get("loginName");
			String foregroundtask = taskInfo.get("foregroundtask");
			String type = StringUtil.isEmpty(taskInfo.get("type"))?"1":taskInfo.get("type");
			
			if(type.equalsIgnoreCase("1")&&StringUtil.isEmpty(taskExp)){
				taskExp = "0 30 23 * * ?";
			}else{
				taskExp = "";
			}
			
			String task_head = "";
			String task_desc = "";
			
			if(TASK_CATEGORY_DEFAULT.equals(taskCategory)){
				task_head = TASK_HEAD_DEFAULT;
				task_desc = TASK_COMMENT_DEFAULT;
			}else if(TASK_CATEGORY_DATAEXTRACT.equals(taskCategory)){
				task_head = TASK_HEAD_DATAEXTRACT;
				task_desc = TASK_COMMENT_DATAEXTRACT;
			}else if(TASK_CATEGORY_DATAPROFILE.equals(taskCategory)){
				task_head = TASK_HEAD_DATAPROFILE;
				task_desc = TASK_COMMENT_DATAPROFILE;
			}else if(TASK_CATEGORY_RULECHECK.equals(taskCategory)){
				task_head = TASK_HEAD_RULE;
				task_desc = TASK_COMMENT_RULE;
			}else if(TASK_CATEGORY_DATACOMPARE.equals(taskCategory)){
				task_head = TASK_HEAD_DATACOMPARE;
				task_desc = TASK_COMMENT_DATACOMPARE;
			}else if(TASK_CATEGORY_QUALITYEXAMINATION.equals(taskCategory)){
				task_head = TASK_HEAD_QUALITYEXAMINATION;
				task_desc = TASK_COMMENT_QUALITYEXAMINATION;
			}else if(TASK_CATEGORY_DBDATACALCULATE.equals(taskCategory)){
				task_head = TASK_HEAD_DBDATACALCULATE;
				task_desc = TASK_COMMENT_DBDATACALCULATE;
			}else if(TASK_CATEGORY_GISCONVERT.equals(taskCategory)){
				task_head = TASK_HEAD_GISCONVERT;
				task_desc = TASK_COMMENT_GISCONVERT;
			}
			
			taskCode = task_head + taskCode;

			String mtId = getSequenceValue("s_pub_metadata_templet",DatabaseConst.DATASOURCE_DEFAULT);
			String mtCode = METADATA_HEAD + taskCode + "_" + mtId;
			String mtContent = buildMetaDataContent(task_desc);

			if (!StringUtil.isEmpty(mtId)) {
				if (saveMetaData(mtId, taskName, mtCode, loginName, mtContent)) {
					if (!saveDataTaskTemplete(taskName, taskCode, taskExp,foregroundtask, mtCode, mtContent,taskCategory,type)) 
						throw new Exception("保存任务模板失败!");
				} else 
					throw new Exception("保存任务元数据失败!");
			} else 
				throw new Exception("保存任务数据失败!");
	}

	
	

	/**
	 * 更新任务元数据
	 * 
	 * @param mtCode
	 * @param content
	 * @param oldMtCode
	 * @param oldContent
	 */
	public void updateTaskMetaDatabyCode(String mtCode, String content,
			String oldMtCode, String oldContent) throws Exception {
		CommDMO dmo = new CommDMO();
		try {
			if (!StringUtil.isEmpty(oldMtCode)) {
				dmo.executeUpdateClobByDS(DatabaseConst.DATASOURCE_DEFAULT,
						"content", "PUB_METADATA_TEMPLET", " CODE='"
								+ oldMtCode + "'", oldContent);
				dmo.commit(DatabaseConst.DATASOURCE_DEFAULT);
			}
			dmo.executeUpdateClobByDS(DatabaseConst.DATASOURCE_DEFAULT,
					"content", "PUB_METADATA_TEMPLET",
					" CODE='" + mtCode + "'", content);
			dmo.commit(DatabaseConst.DATASOURCE_DEFAULT);
			
		} catch (Exception e) {
			try {
				dmo.rollback(DatabaseConst.DATASOURCE_DEFAULT);
			} catch (Exception e1) {
			}
		} finally {
			dmo.releaseContext(DatabaseConst.DATASOURCE_DEFAULT);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void updateMetaData(Map[] maps){
		CommDMO dmo = new CommDMO();
		try {
			
			if(maps != null ){
				for(Map map : maps){
					String mtCode = (String)map.get("mtcode");
					String content = (String)map.get("content");
					dmo.executeUpdateClobByDS(DatabaseConst.DATASOURCE_DEFAULT,"content", "PUB_METADATA_TEMPLET"," CODE='" + mtCode + "'", content);
					dmo.commit(DatabaseConst.DATASOURCE_DEFAULT);
				}
			}
			
		} catch (Exception e) {
			try {
				dmo.rollback(DatabaseConst.DATASOURCE_DEFAULT);
			} catch (Exception e1) {
			}
		} finally {
			dmo.releaseContext(DatabaseConst.DATASOURCE_DEFAULT);
		}
	}

	/**
	 * 更新任务模板
	 * 
	 * @param id
	 * @param name
	 * @param code
	 * @param exp
	 */
	public void editTaskTemplet(String id, String name, String code, String exp,String roundTask,String type) throws Exception {
		CommDMO dmo = new CommDMO();
		try {
			String updateSQL = "update PUB_DATATASK_TEMPLET set NAME=?,CRONEXPRESSION=?,Foregroundtask=?,type=? where id=?";
			dmo.executeUpdateByDS(DatabaseConst.DATASOURCE_DEFAULT, updateSQL,
					name, exp,roundTask,type,id);
			dmo.commit(DatabaseConst.DATASOURCE_DEFAULT);
			this.removeJob(code);
			if(!StringUtil.isEmpty(type)&& type.equalsIgnoreCase("1")){
				this.addTaskToJob(code, id, "", exp);
			}
			
		} catch (Exception e) {
			try {
				dmo.rollback(DatabaseConst.DATASOURCE_DEFAULT);
			} catch (Exception e1) {
				throw e1;
			}
			throw e;
			
		} finally {
			dmo.releaseContext(DatabaseConst.DATASOURCE_DEFAULT);
		}
	}
	
	public void deleteTaskTemplete(String code,String mtCode){
		CommDMO dmo = new CommDMO();
		try {
			
			String delDataTaskSQL = "delete from PUB_DATATASK where datatasktempletid in (select id from PUB_DATATASK_TEMPLET where code=?)";
			dmo.executeUpdateByDS(DatabaseConst.DATASOURCE_DEFAULT, delDataTaskSQL,code);
			dmo.commit(DatabaseConst.DATASOURCE_DEFAULT);
			
			String deleteSQL = "delete from PUB_DATATASK_TEMPLET where code=?";
			dmo.executeUpdateByDS(DatabaseConst.DATASOURCE_DEFAULT, deleteSQL,code);
			dmo.commit(DatabaseConst.DATASOURCE_DEFAULT);
			
			String deleteMTSQL = "delete from pub_metadata_templet where code=?";
			dmo.executeUpdateByDS(DatabaseConst.DATASOURCE_DEFAULT, deleteMTSQL,mtCode);
			dmo.commit(DatabaseConst.DATASOURCE_DEFAULT);
			
			this.removeJob(code);
			
		} catch (Exception e) {
			e.printStackTrace();
			try {
				dmo.rollback(DatabaseConst.DATASOURCE_DEFAULT);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
		} finally {
			dmo.releaseContext(DatabaseConst.DATASOURCE_DEFAULT);
		}
	}
	
	/**
	 * 构建任务模板元数据内容
	 * @param desc
	 * @return
	 */
	protected String buildMetaDataContent(String desc) {
		StringBuffer buff = new StringBuffer();
		buff.append("<root>");
		buff.append("<datatask desc='");
		buff.append(desc);
		buff.append("'>");
		buff.append("</datatask>");
		buff.append("</root>");
		return buff.toString();
	}

	/**
	 * 获取指定SEQUENCE值
	 * @param sequenceName
	 * @param dsName
	 * @return
	 */
	protected String getSequenceValue(String sequenceName,String dsName){
		CommDMO dmo = new CommDMO();
		String value = "";
		try{
			value = dmo.getSequenceNextValByDS(dsName,sequenceName);
		}catch(Exception e){
			try {
				dmo.rollback(DatabaseConst.DATASOURCE_DEFAULT);
			} catch (Exception e1) {
			}
		}finally{
			dmo.releaseContext(DatabaseConst.DATASOURCE_DEFAULT);
		}
		return value;
	}
	
	/**
	 * 保存元数据
	 * @param Id
	 * @param name
	 * @param code
	 * @param owner
	 * @param mtContent
	 * @return
	 */
	protected boolean saveMetaData(String Id,String name,String code,String owner,String mtContent){
		boolean flag = false;
		CommDMO dmo = new CommDMO();
		String sql = "insert into pub_metadata_templet(TYPE,ID,NAME,CODE,OWNER) values(20,?,?,?,?)";
		try{
			dmo.executeUpdateByDS(DatabaseConst.DATASOURCE_DEFAULT, sql,Id,name,code,owner);
			dmo.commit(DatabaseConst.DATASOURCE_DEFAULT);
			
			dmo.executeUpdateClobByDS(DatabaseConst.DATASOURCE_DEFAULT, "CONTENT", "pub_metadata_templet", "code='"+code+"'", mtContent);
			dmo.commit(DatabaseConst.DATASOURCE_DEFAULT);
			flag = true;
		}catch(Exception e){
			try {
				dmo.rollback(DatabaseConst.DATASOURCE_DEFAULT);
			} catch (Exception e1) {
			}
			flag = false;
		}finally{
			dmo.releaseContext(DatabaseConst.DATASOURCE_DEFAULT);
		}
		return flag;
	}
	
	/**
	 * 保存任务模板
	 * @param name
	 * @param code
	 * @param cronexpression
	 * @param mtcode
	 * @param content
	 * @return
	 */
	protected boolean saveDataTaskTemplete(String name,String code,String cronexpression,String mtcode,String content){
		boolean flag = false;
		CommDMO dmo = new CommDMO();
		String sql = "insert into pub_datatask_templet(TYPE,ID,NAME,CODE,CRONEXPRESSION,MTCODE) values(1,?,?,?,?,?)";
		try{
			String taskTempleteId = dmo.getSequenceNextValByDS(DatabaseConst.DATASOURCE_DEFAULT, "s_pub_datatask_templet");
			code = code+"_"+taskTempleteId;
			dmo.executeUpdateByDS(DatabaseConst.DATASOURCE_DEFAULT, sql,taskTempleteId,name,code,cronexpression,mtcode);
			dmo.commit(DatabaseConst.DATASOURCE_DEFAULT);
			this.removeJob(code);
			this.addTaskToJob(code, taskTempleteId, content, cronexpression);
			flag = true;
		}catch(Exception e){
			try {
				dmo.rollback(DatabaseConst.DATASOURCE_DEFAULT);
			} catch (Exception e1) {
			}
			flag = false;
		}finally{
			dmo.releaseContext(DatabaseConst.DATASOURCE_DEFAULT);
		}
		return flag;
	}
	
	protected boolean saveDataTaskTemplete(String name,String code,String cronexpression,String foregroundtask ,String mtcode,String content,String taskCategory,String type){
		boolean flag = false;
		CommDMO dmo = new CommDMO();
		String sql = "insert into pub_datatask_templet(TYPE,ID,NAME,CODE,CRONEXPRESSION,FOREGROUNDTASK,MTCODE,TASKCATEGORY,APPMODULE) values(?,?,?,?,?,?,?,?,?)";
		try{
			String taskTempleteId = dmo.getSequenceNextValByDS(DatabaseConst.DATASOURCE_DEFAULT, "s_pub_datatask_templet");
			code = code+"_"+taskTempleteId;
			dmo.executeUpdateByDS(DatabaseConst.DATASOURCE_DEFAULT, sql,type,taskTempleteId,name,code,cronexpression,foregroundtask,mtcode,taskCategory,"DQ");
			dmo.commit(DatabaseConst.DATASOURCE_DEFAULT);
			this.removeJob(code);
			if(!StringUtil.isEmpty(type)&&type.equalsIgnoreCase("1")){
				this.addTaskToJob(code, taskTempleteId, content, cronexpression);
			}
			flag = true;
		}catch(Exception e){
			try {
				dmo.rollback(DatabaseConst.DATASOURCE_DEFAULT);
			} catch (Exception e1) {
			}
			flag = false;
		}finally{
			dmo.releaseContext(DatabaseConst.DATASOURCE_DEFAULT);
		}
		return flag;
	}

	/**
	 * 将任务添加到JOB
	 * 
	 * @param jobName
	 * @param taskTempleteId
	 * @param content
	 * @param exp
	 * @return
	 */
	private boolean addTaskToJob(String jobName, String taskTempleteId,
			String content, String exp) {
		boolean flag = false;
		try {
			JobDataMap dataMap = new JobDataMap();
			dataMap.put("ID", taskTempleteId);
			dataMap.put("CONTENT", content);
			if (JobServer.isCronJobExist(jobName)) {
				JobServer.removeJob(jobName, "DataTask");
			}
			JobDetail jobDetail = new JobDetail();
			jobDetail.setJobClass(DataTaskCronJob.class);
			jobDetail.setGroup("DataTask");
			jobDetail.setName(jobName);
			jobDetail.setJobDataMap(dataMap);
			JobServer.addCronJob(jobDetail, exp);
			flag = true;
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}
	
	/**
	 * 从Job高度引擎中删除
	 * @param jobName
	 */
	private void removeJob(String jobName){
		try{
			if (JobServer.isCronJobExist(jobName)) {
				JobServer.removeJob(jobName, "DataTask");
			}
		}catch(Exception e){
			
		}
	}
	
	public void stopTaskByTempletId(String templeteId) throws Exception{
		CommDMO dmo = new CommDMO();
		String sql="UPDATE PUB_DATATASK SET STATUS=-1 WHERE datatasktempletid=? AND STATUS IN (0,1)";
		try{
			dmo.executeUpdateByDS(DatabaseConst.DATASOURCE_DEFAULT, sql,templeteId);
			dmo.commit(DatabaseConst.DATASOURCE_DEFAULT);
		}catch(Exception e){
			throw e;
		}finally{
			dmo.releaseContext(DatabaseConst.DATASOURCE_DEFAULT);
		}
	}
}
