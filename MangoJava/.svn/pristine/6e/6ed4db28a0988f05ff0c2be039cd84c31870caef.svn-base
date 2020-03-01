package smartx.publics.datatask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.utils.StringUtil;
import smartx.framework.common.vo.DMOConst;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaLogger;
import smartx.framework.metadata.bs.NovaServerEnvironment;
import smartx.framework.metadata.vo.jepfunctions.JepFormulaParse;

/**
 *@author zzy
 *@date Sep 7, 2011
 **/
public class DataTaskCronJob implements Job{

	private final Logger logger = NovaLogger.getLogger(this.getClass());
	
	public void execute(JobExecutionContext ctx) throws JobExecutionException
	{
		CommDMO dmo = new CommDMO();
		Map<String, String> paramMap = new HashMap<String,String>();
		JepFormulaParse jepParse = new JepFormulaParse(JepFormulaParse.li_bs);
		try
		{
			JobDetail detail = ctx.getJobDetail();
			JobDataMap dataMap = detail.getJobDataMap();
			String taskTempletId = dataMap.get("ID").toString();
			HashVO taskVo = getDataTaskById(taskTempletId);
			if(taskVo == null)
				throw new Exception("根据taskTempletId=" + taskTempletId + " 没有找到对应的数据模板定义！");
			
			String taskContent = taskVo.getStringValue("CONTENT");
			
			//这里对参数进行解析，取值可以通过两种方式：1，系统函数    2，服务端环境变量
			String[] keys = StringUtil.getFormulaMacPars(taskContent);
			for(int j=0;j<keys.length;j++){
				String paramKey = keys[j];
				if( paramKey.indexOf(")") > 0 )
				{//函数
					String paramValue = (String)jepParse.execFormula(paramKey);
					paramMap.put(paramKey, paramValue);
				}
				else{
					if(NovaServerEnvironment.getInstance().get(paramKey) != null){
						paramMap.put(paramKey, (String)NovaServerEnvironment.getInstance().get(paramKey));
					}
				}
			}
			
			//初始化参数处理
			dealParams(paramMap,taskContent);
			
			taskContent = StringUtil.buildExpression(paramMap, taskContent);
			
			CommTaskManager.getInstance().addTask(taskTempletId, detail.getName(), taskContent, paramMap);
			//logger.debug("===========数据预处理任务【"+ detail.getFullName() +"】开始触发===================");
			dmo.commit(DMOConst.DS_DEFAULT);
			
		}
		catch (Exception e)
		{
			logger.error("数据预处理任务触发启动失败！",e);
		}
		finally{
			dmo.releaseContext();
		}
	}
	
	//获取元数据编码获取子任务内容
	private HashVO getDataTaskById(String taskId) throws Exception{
		CommDMO dmo=new CommDMO();		
		try{
			String sql = "SELECT MT.CONTENT,D.MTCODE,D.NAME FROM PUB_METADATA_TEMPLET MT,PUB_DATATASK_TEMPLET D WHERE D.MTCODE=MT.CODE AND D.ID = ?";
			HashVO[] vos= dmo.getHashVoArrayByDS(DMOConst.DS_DEFAULT, sql,taskId);
			
			if(vos.length > 0)
				return vos[0];
			else
				return null;
		}finally{
			try{dmo.releaseContext(DMOConst.DS_DEFAULT);}catch(Exception e){};
		}
	}
	
	//初始化参数处理
	private void dealParams(Map<String,String> paramsMap,String content){
		try{
			if(!StringUtil.isEmpty(content)){
				Document doc = DocumentHelper.parseText(content);
				Element root = doc.getRootElement();
				Element paramsEle = root.element("datatask").element("initparam");
				if(paramsEle != null){
					List paramsList = paramsEle.elements();
					if(paramsList != null){
						for(Object obj : paramsList){
							Element paramItem = (Element)obj;
							String key = paramItem.attributeValue("name");
							String value = paramItem.getText();
							if(!paramsMap.containsKey(key)){
								paramsMap.put(key, value);
							}
						}
					}
				}
			}
			
		}catch(Exception e){
			logger.debug("",e);
		}
	}
}
