package smartx.publics.datatask.custom;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dom4j.Element;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.vo.DMOConst;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaLogger;
import smartx.publics.datatask.DataTaskExecThread;
import smartx.publics.datatask.DataTaskExecuteIFC;
import smartx.publics.report.jasper.JasperExportService;

/**
 * 根据jaser文件生成报表的数据任务
 *@author zzy
 *@date Dec 30, 2011
 **/
public class ReportGenerateTask implements DataTaskExecuteIFC{

	private Logger logger = NovaLogger.getLogger(this.getClass());
	
	@Override
	public void dataTaskExec(Element task, DataTaskExecThread mainThread) throws Exception {
		//jarper文件生成报表
		JasperExportService expService = JasperExportService.getInstance();

		//ireport路径
		String ireportPath = System.getProperty("NOVA2_SYS_ROOTPATH") + "ireport/" ;
		
		String templateCode = task.elementText("templatecode");//模板编码
		String reportType = task.elementText("reporttype");//生成的报表类型，可以多个，逗号分割,pdf,html,xls
		
		if(templateCode ==null || "".equals(templateCode) || reportType==null || "".equals(reportType)){
			logger.error("japer模板编码或报表类型不能为空！");
			throw new Exception("japer模板编码或报表类型不能为空！");
		}
		String[] types = reportType.split(",");
		
		//根据模板模板获取模板的相关内容
		HashVO templateVo = getReportTemplate(templateCode);
		if(templateVo == null){
			mainThread.logTaskRun("任务执行失败根据报表模板编码【" + "】未找到对应的报表模板","");
			throw new Exception("任务执行失败根据报表模板编码【" + "】未找到对应的报表模板");
		}
		//PARAM1 一定是报表生成的批次号
		String jasperName = templateVo.getStringValue("JASPERFILEPATH");
		String dsName = templateVo.getStringValue("DATASOURCE");	
		
		//获取报表参数
		Map<String, Object> jasperParamMap = new HashMap<String, Object>();
		//由于jasperParamMap在报表生成过程中，内容会发生变化，需要保留原始的这份
		Map<String, Object> jasperParamMapCopy = new HashMap<String, Object>();
		//加入报表路径
		jasperParamMap.put("SUBREPORT_DIR", ireportPath + "jasper/");
		jasperParamMap.put("REPORT_DIR", ireportPath + "jasper/");
		
		Element paramE = task.element("reportparams");
		@SuppressWarnings("unchecked")
		List<Element> paramsE = paramE.elements();
		for(int i=0;i<paramsE.size();i++){
			Element e = paramsE.get(i);
			String key = e.attributeValue("key");
			String value = e.attributeValue("value");
			jasperParamMap.put(key, value);
			jasperParamMapCopy.put(key, value);
			
		}
		Map<String,String> paramInsMap = getParamInstanceMap(templateVo, jasperParamMapCopy);
		String reportName = templateCode + paramInsMap.get("PARAM1") + paramInsMap.get("PARAM2");//报表模板中第一个参数一定是批次号
		
		mainThread.logTaskRun("开始根据【"+jasperName+".jasper】生成报表文件,报表文件类型有【"+reportType+"】","");
		for(int i=0;i<types.length;i++){
			String reprotExt = types[i];
			if("xls".equalsIgnoreCase(reprotExt)){
				expService.exportExcel(ireportPath, jasperName,reportName, dsName, jasperParamMap);
			}else if("pdf".equalsIgnoreCase(reprotExt)){
				expService.exportPdf(ireportPath, jasperName,reportName, dsName, jasperParamMap);
			}else if("html".equalsIgnoreCase(reprotExt)){
				expService.exportHtml(ireportPath, jasperName,reportName, dsName, jasperParamMap);
			}else{
				logger.error("未支持的报表导出类型，类型定义为：" + reprotExt);
				mainThread.logTaskRun("不支持导出类型为【"+reprotExt+"】报表文件！","");
				continue;
			}
			
			insertReportInsRecord(templateVo, reprotExt, "", reportName, paramInsMap);
			mainThread.logTaskRun("报表文件【"+ reportName +"."+ reprotExt + "】生成文件成功！","");
		}
		
		mainThread.logTaskRun("结束报表文件【"+ jasperName +".jasper"+ "】导出！","");
		new CommDMO().releaseContext();
	}
	
	/**
	 * 根据报表模板中参数说明 和任务参数值，获取各参数的实际定义
	 * @param templateVo
	 * @param jasperParamMap
	 * @return
	 */
	private Map<String,String> getParamInstanceMap(HashVO templateVo,Map<String,Object> jasperParamMap){
		Map<String, String> paramTMap = new HashMap<String, String>();//模板中参数定义
	    paramTMap.put(templateVo.getStringValue("PARAM1"), "PARAM1");
		paramTMap.put(templateVo.getStringValue("PARAM2"), "PARAM2");
		paramTMap.put(templateVo.getStringValue("PARAM3"), "PARAM3");
		paramTMap.put(templateVo.getStringValue("PARAM4"), "PARAM4");
		paramTMap.put(templateVo.getStringValue("PARAM5"), "PARAM5");
		paramTMap.put(templateVo.getStringValue("PARAM6"), "PARAM6");
		paramTMap.put(templateVo.getStringValue("PARAM7"), "PARAM7");
		paramTMap.put(templateVo.getStringValue("PARAM8"), "PARAM7");
		paramTMap.put(templateVo.getStringValue("PARAM9"), "PARAM7");
		
		Map<String, String> paramInsMap = new HashMap<String, String>();//报表实例参数定义
		
		Set<String> keySet = jasperParamMap.keySet();
		Iterator<String> it = keySet.iterator();
		for(;it.hasNext();){
			String key = it.next();
			String value = (String)jasperParamMap.get(key);			
			paramInsMap.put(paramTMap.get(key), value);
		}
		
		return paramInsMap;
	}
	
	private void insertReportInsRecord(HashVO templateVo,String type,String reportPath,String reportName,Map<String,String> paramInsMap) throws Exception{
		CommDMO dmo = new CommDMO();
		//如果已经存在同名的报表记录，则先删除
		String delSql = "delete pub_report_instance where code =? and type=? and createdate_str=? and reportname=?";
		dmo.executeUpdateByDS(DMOConst.DS_DEFAULT, delSql, templateVo.getStringValue("CODE"),type,paramInsMap.get("PARAM1"),reportName);
		
		String insertSql = "INSERT INTO PUB_REPORT_INSTANCE(ID,CODE,NAME,TEMPLATCONTENT,CREATEDATE_STR,TYPE,REPORTPATH,REPORTNAME,CREATETIME"
			+", PARAM1,PARAM2,PARAM3,PARAM4,PARAM5,PARAM6,PARAM7,PARAM8,PARAM9) "
			+" VALUES(S_PUB_REPORT_INSTANCE.NEXTVAL,?,?,?,?,?,?,?,SYSDATE,?,?,?,?,?,?,?,?,?) ";
		
		dmo.executeUpdateByDS(DMOConst.DS_DEFAULT, insertSql, templateVo.getStringValue("CODE"),templateVo.getStringValue("NAME"),templateVo.getStringValue("JASPERXML")
				,paramInsMap.get("PARAM1"),type,reportPath,reportName,paramInsMap.get("PARAM1"),paramInsMap.get("PARAM2"),paramInsMap.get("PARAM3"),paramInsMap.get("PARAM4")
				,paramInsMap.get("PARAM5"),paramInsMap.get("PARAM6"),paramInsMap.get("PARAM7"),paramInsMap.get("PARAM8"),paramInsMap.get("PARAM9"));
		dmo.commit(DMOConst.DS_DEFAULT);
	}
	
	public HashVO getReportTemplate(String templateCode) throws Exception{
		HashVO tempVo = null;
		CommDMO dmo = new CommDMO();
		String sql = "SELECT ID,CODE,NAME,MTCODE,DATASOURCE,JASPERFILEPATH,JASPERXML,PARAM1,PARAM2,PARAM3 "
				+",PARAM4,PARAM5,PARAM6,PARAM7,PARAM8,PARAM9 "
				+" FROM PUB_REPORT_TEMPLATE WHERE CODE = ?";
		HashVO[] vos = dmo.getHashVoArrayByDS(DMOConst.DS_DEFAULT, sql, templateCode);
		if(vos.length > 0)
			tempVo = vos[0];
		
		return tempVo;
	}

}
