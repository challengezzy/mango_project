package smartx.bam.bs.dataprofile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.vo.DMOConst;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaLogger;
import smartx.publics.datatask.CommTaskManager;
import smartx.publics.form.bs.interceptors.datatask.DataTaskFormService;
import smartx.publics.metadata.MetadataTempletUtil;

/**
 *@author zzy
 *@date Feb 20, 2012
 **/
public class AnalyzerSetTaskService {
	
	private Logger logger = NovaLogger.getLogger(this.getClass());	
	
	public static String METADATA_PREFIX = "MT_DT_";//元数据编码前缀
	public static String DATATASK_PREFIX = "AnalyzerSet_";//元数据编码前缀
	
	public static AnalyzerSetTaskService AnalysetService;
	
	private DataTaskFormService taskService  = new DataTaskFormService();
	
	public static AnalyzerSetTaskService getInstance(){
		if(AnalysetService == null)
			AnalysetService = new AnalyzerSetTaskService();
		
		return AnalysetService;
	}
	
	public void addDataTask(String name,String code,String cronexpression,String content,boolean isExecNow) throws Exception{
		try{
			String mtcode = DataTaskFormService.METADATA_PREFIX + code;
			MetadataTempletUtil.updateMetadataTemplet(mtcode, content, name, "admin", "20");//数据任务
			String taskTempletId =  taskService.saveCronDataTask(name, code, cronexpression, mtcode, content);
			logger.info("循环数据任务添加成功 taskcode = " + code);
			if(isExecNow){
				Date d_curr = new Date();
		        SimpleDateFormat sdf_curr = new SimpleDateFormat("yyyyMMdd", Locale.SIMPLIFIED_CHINESE);
		        String str_date = sdf_curr.format(d_curr);
		        Map<String, String> param = new HashMap<String, String>();
		        param.put("getCurrDate()", str_date);
				CommTaskManager.getInstance().addAndStartTask(taskTempletId, name, content, param);
				logger.info("执行数据剖析任务 :" + name);
			}
			
		}catch (Exception e) {
			new CommDMO().releaseContext(DMOConst.DS_DEFAULT);
			throw e;
		}
	}
	
	/**
	 * 判断数据任务模板是否存在
	 * 
	 * @param code
	 * @throws Exception
	 */
	public boolean isExistAnalyzerSet(String code) throws Exception {
		logger.debug("判断是否存在分析集模板" + code + "]");
		CommDMO dmo = new CommDMO();
		String sql = "select b.id from pub_dp_analyzerset b where b.code =?";
		try {
			HashVO[] vos = dmo.getHashVoArrayByDS(null, sql, code);
			if (vos.length > 0)
				return true;
		} catch (Exception e) {
			throw e;
		} finally {
			dmo.releaseContext(null);
		}
		return false;
	}

	
}
