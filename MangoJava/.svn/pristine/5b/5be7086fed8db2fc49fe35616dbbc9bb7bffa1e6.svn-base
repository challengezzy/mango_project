/**
 * 
 */
package smartx.publics.form.bs.service;

import java.io.File;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import smartx.framework.common.utils.FileUtil;
import smartx.framework.common.vo.NovaLogger;
import smartx.publics.form.bs.utils.ImportInitMetaData;
import smartx.publics.form.vo.InitMetaDataConst;

/**
 * @author caohenghui
 *
 */
public class InitMetaDataService {
	
	private static boolean isInit = false;
	
	private ImportInitMetaData iimd = new ImportInitMetaData();
	
	public InitMetaDataService(){
		if (isInit)
			return;
		isInit = true;
		
		try {
			NovaLogger.getLogger(InitMetaDataService.class).debug("初始化平台元数据开始!");
			initMetadata();
			
		} catch (Exception e) {
			NovaLogger.getLogger(InitMetaDataService.class).debug("初始化平台元数据异常!",e);
		}
		NovaLogger.getLogger(InitMetaDataService.class).debug("初始化平台元数据结束!");
	}
	

	@SuppressWarnings("unchecked")
	private void initMetadata(){
		
		try {
			
			String content = FileUtil.readFileContent(InitMetaDataConst.SMARTXCONFIG, "UTF-8");
//			File file = new File(InitMetaDataConst.SMARTXCONFIG);
//			if(file.exists()){
//				
//				FileInputStream fis = new FileInputStream(file);
//				InputStreamReader isr = new InputStreamReader(fis,"UTF-8"); 
//				BufferedReader br = new BufferedReader(isr);
//				
//				String line = null;
//				
//				while( (line = br.readLine()) != null ){
//					content.append(line);
//				}
//				fis.close();
//				isr.close();
//				br.close();
//				
//			}
			
			Document doc = DocumentHelper.parseText(content);
			
			Element root = doc.getRootElement();
			Element initMetadataFiles = root.element("init-metadata-files");
			
			String flag = "false";
			List<Element> files = null;
			
			if(initMetadataFiles != null){
				flag = initMetadataFiles.attributeValue("initialize", "false");
				files = initMetadataFiles.elements();
			}
			
			
			
			if(flag != null && flag.trim().equalsIgnoreCase("true")){
				if(files != null){
					for(Element ele : files){
						String datasource = (ele.attributeValue("datasource")==null||ele.attributeValue("datasource").trim().equals(""))?null:ele.attributeValue("datasource");
						String UpLoadFileName = ele.attributeValue("name");
						String unZipPatchDir = iimd.unZipPatchFile(UpLoadFileName);
						if(unZipPatchDir != null && !unZipPatchDir.equals("")){
							File fileDir = new File(unZipPatchDir);
							File[] zipFiles = fileDir.listFiles();
							for(File tempFile : zipFiles){
								if(tempFile.isFile()){
									String fileName = tempFile.getName();
									fileName = fileName.substring(0,fileName.lastIndexOf("."));
									String fullFileName = tempFile.getAbsolutePath();
									iimd.unZipFile(fullFileName, unZipPatchDir+"/"+fileName);
									iimd.importXMLFromMDFile(unZipPatchDir+"/"+fileName, datasource);
								}
							}
						}
					}
				}
				iimd.writeFlagToXML();
			}
			
		} catch (Exception e) {
			NovaLogger.getLogger(InitMetaDataService.class).debug("",e);
		}
		
	}

}
