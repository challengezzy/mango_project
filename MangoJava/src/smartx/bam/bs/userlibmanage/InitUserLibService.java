/**
 * 
 */
package smartx.bam.bs.userlibmanage;

import java.io.File;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.utils.Sys;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaLogger;

/**
 * @author caohenghui
 *
 */
public class InitUserLibService {
	
	protected Logger logger = NovaLogger.getLogger(this.getClass());
	
	private static String UserLibPath = (String) Sys.getInfo("NOVA2_SYS_ROOTPATH")+"WEB-INF/userlib";

	private static boolean isInit = false;
	
	public InitUserLibService() {
		if (isInit)
			return;
		isInit = true;
		logger.debug("初始化用户JAR文件");
		try {
			init();
		} catch (Exception e) {
			logger.error("初始化用户JAR文件失败", e);
		}
		logger.debug("初始化用户JAR文件完毕！");
	}
	
	private void init(){
		CommDMO dmo = new CommDMO();
		try{
			
			String searchSQL = "select * from bam_jarfile";
			HashVO[] temp = dmo.getHashVoArrayByDS(null, searchSQL);

			File file = new File(UserLibPath);
			
			if(temp != null && temp.length>0){
				
				for(int i = 0; i<temp.length; i++){
					HashVO vo = temp[i];
					
					String filePath = vo.getStringValue("path");
					File[] files = file.listFiles();
					if(files != null){
						for(File tempFile : files){
							File f = new File(filePath);
							if(f.getAbsolutePath().trim().equalsIgnoreCase(tempFile.getAbsolutePath().trim())){
								ClassLoaderUtil.addSystemClassPath(tempFile);
								logger.debug("加载JAR文件:"+tempFile.getAbsolutePath());
							}else{
								deleteFile(tempFile);
							}
						}	
					}
				}
				
			}else{
				deleteFile(file);
			}
			
		}catch(Exception e){
			logger.debug("",e);
		}
	}
	
	private void deleteFile(File file){
		if(file.isDirectory()){
			File[] files = file.listFiles();
			if (files != null) {
				for (File tempFile : files) {
					if (tempFile.isFile()) {
						tempFile.delete();
					} else {
						deleteFile(tempFile);
					}
				}
			}
		}else{
			file.delete();
		}
	}

}
