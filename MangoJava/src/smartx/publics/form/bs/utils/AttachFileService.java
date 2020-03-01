package smartx.publics.form.bs.utils;


import java.io.File;

import org.apache.log4j.Logger;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.utils.FileUtil;
import smartx.framework.common.vo.DMOConst;
import smartx.framework.common.vo.NovaLogger;
import smartx.publics.file.FileConstant;
import smartx.publics.form.vo.InitMetaDataConst;

/**
 *@author zzy
 *@date Jan 12, 2012
 **/
public class AttachFileService {
	
	protected Logger logger = NovaLogger.getLogger(this.getClass());
	
	private static AttachFileService attachService = null;
	
	public static AttachFileService getInstance(){
		if(attachService != null)
			return attachService;
		
		attachService = new AttachFileService();
		
		return attachService;
	}
	
	/**
	 * 附件文件存储到数据库中
	 * @param bytes
	 * @param fileName
	 * @param tableName
	 * @param keyColumn
	 * @param keyValue
	 */
	public void attachFileInsert(byte[] bytes,String fileName,String tableName,String keyColumn,String keyValue) throws Exception{
		logger.debug("文件【" + fileName + "】存储到表PUB_FILECONTENT中!");
		
		CommDMO dmo = new CommDMO();
		String insertSql = "INSERT INTO PUB_FILECONTENT(ID,TABLENAME,KEYCOLUMN,KEYVALUE,FILENAME,CONTENT,DOWNLOADTIMES) VALUES(?,?,?,?,?,EMPTY_BLOB(),0)";
		
		String id = dmo.getSequenceNextValByDS(DMOConst.DS_DEFAULT, "S_PUB_FILECONTENT");
		dmo.executeUpdateByDS(DMOConst.DS_DEFAULT, insertSql, id,tableName,keyColumn,keyValue,fileName);
		
		//dmo.executeUpdateBlobByDS(DMOConst.DS_DEFAULT, "PUB_FILECONTENT", "KEYVALUE", keyValue, "CONTENT", bytes);
		String updateSql = "select content from PUB_FILECONTENT where id=" + id + " for update ";
		dmo.executeUpdateBlobByDS(DMOConst.DS_DEFAULT, updateSql, bytes);
		dmo.releaseContext(DMOConst.DS_DEFAULT);
		
		logger.info("**************文件"+ fileName + "存储成功！！pub_filecontent.id= 【"+id+"】***************");
	}
	
	/**
	 * 从数据库pub_filecontent中读取文件，返回文件名
	 * @param fileContentId 附件ID
	 * @param fileName 文件名
	 * @return
	 */
	public String attachFileDownload(String fileContentId,String fileName) throws Exception{
		String filePath = InitMetaDataConst.RootPath + FileConstant.DOWNLOAD_DIR + "/" + fileName ;
		//创建下载目录
		FileUtil.createDirIfNotExists(InitMetaDataConst.RootPath + FileConstant.DOWNLOAD_DIR);
		
		CommDMO dmo = new CommDMO();
		String querySql = "select content from PUB_FILECONTENT where id=" + fileContentId;
		byte[] bytes = dmo.readBlobDataByDS(DMOConst.DS_DEFAULT, querySql);		
		File f = FileUtil.getFileFromBytes(bytes, filePath);
		
		//更新下载次数
		String updateSql = "UPDATE PUB_FILECONTENT SET DOWNLOADTIMES=DOWNLOADTIMES+1 WHERE ID=?";
		dmo.executeUpdateByDS(DMOConst.DS_DEFAULT, updateSql,fileContentId);
		
		dmo.commit(DMOConst.DS_DEFAULT);
		dmo.releaseContext(DMOConst.DS_DEFAULT);
		logger.info("文件【"+ f.getName() +"】生成成功");
		
		return fileName;
	}

}
