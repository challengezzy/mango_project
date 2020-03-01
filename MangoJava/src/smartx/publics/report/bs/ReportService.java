package smartx.publics.report.bs;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.utils.FileUtil;
import smartx.framework.common.vo.DMOConst;
import smartx.framework.common.vo.NovaLogger;
import smartx.publics.email.EmailService;
import smartx.publics.report.jasper.JasperExportService;

/**
 * @author zzy
 * @date Dec 22, 2011
 **/
public class ReportService {
	protected Logger logger = NovaLogger.getLogger(this.getClass());

	private static boolean isInit = false;

	public ReportService() {
		init();
		if (isInit)
			return;
	}

	private void init() {
		try {
			//报表生成
			//test();
			//发送
			//mailSendTest2();
			
			//fileContentInsert();
			//fileContentRead();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		isInit = true;
	}
	
	//
	public void fileContentInsert() throws Exception{
		
		String fileName = "E:\\20111205山东现场工作日报.doc";
		File f = new File(fileName);
		byte[] bytes = FileUtil.getBytesFromFile(f);
		
		CommDMO dmo = new CommDMO();
		dmo.executeUpdateBlobByDS(DMOConst.DS_DEFAULT, "PUB_FILECONTENT", "KEYVALUE", "3", "CONTENT", bytes);
		
		logger.info("文件"+ fileName + "存储成功！！");
	}
	
	public void fileContentRead() throws Exception{
		
		String fileName = "E:\\20111205山东现场工作日报.doc";
		//byte[] bytes = FileUtil.getBytesFromFile(fileName);
		
		CommDMO dmo = new CommDMO();
		//dmo.executeUpdateBlobByDS(DMOConst.DS_DEFAULT, "PUB_FILECONTENT", "KEYVALUE", "3", "CONTENT", bytes);
		byte[] bytes = dmo.readBlobDataByDS(DMOConst.DS_DEFAULT, "select content from PUB_FILECONTENT where keyvalue='3'");
		
		File f = FileUtil.getFileFromBytes(bytes, fileName);
		
		logger.info("文件"+ f.getName() +"生成成功");
	}
	
	public void mailSendTest2() throws Exception{
		//String msg = FileUtil.readFileContent("D:\\ireport_src\\chart_test.html", "UTF-8");
		EmailService mailSrv = new EmailService("smtp.idccenter.net", "zhangzhongyou@lianzhisoft.com", "zhangzhongyou123", 
				"dqc@lianzhisoft.com",true);
		//mailSrv.setSessionDebug(true);
		String sysRootPath = System.getProperty("NOVA2_SYS_ROOTPATH");
		String ireportPath = sysRootPath + "ireport/export/" ;
		
		//String msg = FileUtil.readFileContent(ireportPath + "report_cmp_pon.html", "UTF-8");
		String msg = "中文测试";
		mailSrv.sendMail("zhangzhongyou@lianzhisoft.com", "POM比对结果报表", msg);
	}
	
	
	public void test(){
		JasperExportService expService = JasperExportService.getInstance();

		String sysRootPath = System.getProperty("NOVA2_SYS_ROOTPATH");
		String ireportPath = sysRootPath + "ireport/" ;
		String jasperName = "report_cmp_pon";

		String dsName = "datasource_dqc";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("P_PROVINCE_NAME", "山东电信");
		parameters.put("P_COMPAREDAY", "20111129");
		parameters.put("SUBREPORT_DIR", ireportPath + "jasper/");
		parameters.put("REPORT_DIR", ireportPath + "jasper/");

		String reportName = "PON网管资源设备数据比对结果报表";
		try {
			expService.exportHtml(ireportPath, jasperName,reportName, dsName, parameters);
			expService.exportExcel(ireportPath, jasperName,reportName, dsName, parameters);
			expService.exportPdf(ireportPath, jasperName,reportName, dsName, parameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
