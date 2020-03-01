package smartx.publics.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import smartx.framework.common.utils.Sys;
import smartx.framework.common.vo.NovaLogger;

/**
 *@author zzy
 *@date Sep 24, 2012
 **/
public class TempFileDelService {
	
	private Logger logger = NovaLogger.getLogger(this.getClass());
	
	/**
	 * 保留的文件间隔时间，24小时之内
	 */
	private long fileDuration = 24*60*60L;
	
	public static final String UPLOAD_DIR = "upload";
	
	public TempFileDelService(){
	}
	
	public void startThread(){
		//启动线程s
		(new FileDelThread()).start();
	}
	
	/**
	 * 文件删除
	 * @param dir 文件相对目录
	 * @param duratoin 保留X时间内创建的新文件
	 * @throws Exception
	 */
	public void fileDelete(String dir,Long duratoin) throws Exception{
		logger.info("删除临时目录["+dir+"]下，"+duratoin+"s 以前的文件，文件:");
		String rootPath = (String) Sys.getInfo("NOVA2_SYS_ROOTPATH");
		File dirFile = new File(rootPath + dir);
		
		if( !dirFile.isDirectory()){
			logger.info("["+dir+"],不是目录！！！");
			return;
		}
		List<File> files = (List<File>)FileUtils.listFiles(dirFile, null, false);
		
		long now = System.currentTimeMillis();
		for(File f : files){
			long fileTime = f.lastModified();
			if(now - fileTime > duratoin*1000)
			{
				f.delete();
				logger.info("临时文件【" + f.getPath() + "/" +f.getName() +"被删除");
			}
		}
		
	}
	
	/**
	 * 临时文件删除线程,1小时执行一次
	 * @author zzy
	 *
	 */
	class FileDelThread extends Thread{
		public void run(){
			logger.info("临时文件清理进程启动！");
			while(true){
				try{
					fileDelete(FileConstant.DOWNLOAD_DIR,fileDuration);
					fileDelete(FileConstant.OLAP_TEMPDATA_DIR,fileDuration);
					
					
				}catch(Exception e){
					e.printStackTrace();
					logger.error("文件清理异常" + e.toString());
				}
				try{
					//1小时执行一次
					sleep(3600*1000);
				}catch (Exception e) {				}
			}
		}
	}

}
