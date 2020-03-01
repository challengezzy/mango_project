/**
 * 
 */
package smartx.bam.bs.userlibmanage;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.log4j.Logger;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.utils.Sys;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaLogger;

/**
 * @author caohenghui
 *
 */
public class JarFileManageService {
	
	protected Logger logger = NovaLogger.getLogger(this.getClass());
	
	private static String UserLibPath = (String) Sys.getInfo("NOVA2_SYS_ROOTPATH")+"WEB-INF/userlib";
	
	public void uploadJarFile(byte[] content,Map<String,String> fileInfo) throws Exception{
		
		String fileName = fileInfo.get("filename");
		String fileCode = fileInfo.get("filecode");
		String description = fileInfo.get("description");
		
		int index = fileName.lastIndexOf(".");
		String tempName = fileName;
		if(index > 0){
			tempName = fileName.substring(0, index);
		}
		
		File dir = new File(UserLibPath);
		if(!dir.exists()){
			dir.mkdir();
		}
		
		String filePath = UserLibPath+"/"+tempName+".jar";
		
		if(!isValidFileCode(fileCode)){
			throw new Exception("文件编码重复,不能上传!");
		}
		
		if(!createNewJarFile(content,filePath)){
			throw new Exception("该文件已存在,不能上传!");
		}
		
		CommDMO dmo = new CommDMO();
		try{
			
			String insertSQL = "insert into bam_jarfile (id,name,code,path,description) values(s_bam_jarfile.nextval,?,?,?,?)";
			dmo.executeUpdateByDS(null, insertSQL, fileName,fileCode,filePath,description);
			dmo.commit(null);
			
			ClassLoaderUtil.addSystemClassPath(new File(filePath));
			
		}catch(Exception e){
			logger.debug("",e);
			dmo.rollback(null);
			throw e;
		}finally{
			dmo.releaseContext(null);
		}
	}
	
	private boolean createNewJarFile(byte[] content,String filePath){
		
		boolean flag = true;
		
		try{
			
			File file = new File(filePath);
			if(file.exists()){
				flag = false;
			}else{
				
				file.createNewFile();
				
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(content);
				fos.close();
				
				flag = true;
			}
			
		}catch(Exception e){
			logger.debug("",e);
		}
		return flag;
	}
	
	private boolean isValidFileCode(String fileCode){
		CommDMO dmo = new CommDMO();
		boolean isValid = true;
		try{
			String searchSQL="select count(code) cou from BAM_JARFILE where code=?";
			HashVO[] temp = dmo.getHashVoArrayByDS(null, searchSQL,fileCode);
			if(temp != null && temp.length>0){
				if(temp[0].getIntegerValue("cou")>0){
					isValid = false;
				}
			}
		}catch(Exception e){
			logger.debug("",e);
			isValid = false;
		}finally{
			dmo.releaseContext(null);
		}
		return isValid;
	}
	
	public void deleteJarFile(String Id,String filePath) throws Exception{
		
		CommDMO dmo = new CommDMO();
		
		try{
			
			String deleteSQL="delete from BAM_JARFILE where id=?";
			dmo.executeUpdateByDS(null, deleteSQL, Id);
			dmo.commit(null);
			
			File file = new File(filePath);
			file.delete();
			
		}catch(Exception e){
			logger.debug("",e);
			dmo.rollback(null);
			throw e;
		}finally{
			dmo.releaseContext(null);
		}
		
	}

}
