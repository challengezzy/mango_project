/**************************************************************************
 * $RCSfile: ResponseVO.java,v $  $Revision: 1.1.2.1 $  $Date: 2008/06/16 01:49:17 $
 *
 * $Log: ResponseVO.java,v $
 * Revision 1.1.2.1  2008/06/16 01:49:17  wangqi
 * 分支新增
 *
 * Revision 1.1  2008/06/14 06:33:59  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2008/02/27 08:02:05  wangqi
 * *** empty log message ***
 *
 * Revision 1.1  2007/07/30 02:05:07  shxch
 * *** empty log message ***
 *
 * Revision 1.1  2007/07/26 10:19:46  shxch
 * *** empty log message ***
 *
 * Revision 1.1  2007/02/25 09:55:46  shxch
 * *** empty log message ***
 *
 **************************************************************************/

package smartx.applet.vo;

/**
 * @author user
 *
 */
public class ResponseVO implements java.io.Serializable {

	private static final long serialVersionUID = 8711497356631146198L;

	private ClassFileVO[] fileVOs = null;

	private long readFileTime = 0;

	/**
	 * 获得文件VO
	 * @return
	 */
	public ClassFileVO[] getFileVOs() {
		return this.fileVOs;
	}

	/**
	 * 设置文件VO
	 * @param fileVOs
	 */
	public void setFileVOs(ClassFileVO[] fileVOs) {
		this.fileVOs = fileVOs;
	}

	/**
	 * 获得读文件耗时
	 * @return
	 */
	public long getReadFileTime() {
		return this.readFileTime;
	}

	/**
	 * 设置读文件耗时
	 * @param readFileTime
	 */
	public void setReadFileTime(long readFileTime) {
		this.readFileTime = readFileTime;
	}
	
	/**
	 * 获得文件总长度
	 * @return long
	 */
	public long getFilesLength(){
		if(this.fileVOs==null){
			return 0;
		}else{
			long size=0L;
			for(int i=0;i<this.fileVOs.length;i++){
				size+=this.fileVOs[i].getFileLength();
			}
			return size;
		}
	}
}
