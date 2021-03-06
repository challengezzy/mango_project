/**************************************************************************
 * $RCSfile: RequestVO.java,v $  $Revision: 1.1.2.1 $  $Date: 2008/06/16 01:49:17 $
 *
 * $Log: RequestVO.java,v $
 * Revision 1.1.2.1  2008/06/16 01:49:17  wangqi
 * 分支新增
 *
 * Revision 1.1  2008/06/14 06:33:59  wangqi
 * *** empty log message ***
 *
 * Revision 1.1  2007/07/30 02:05:07  shxch
 * *** empty log message ***
 *
 * Revision 1.1  2007/07/26 10:19:46  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/02/27 06:48:53  shxch
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
public class RequestVO implements java.io.Serializable {

	private static final long serialVersionUID = 8711497356631146198L;

	private String classname = null;  //类名
	
	private String packagename = null;  //包名

	private String clientIP = null;

	public String getClientIP() {
		return clientIP;
	}

	public void setClientIP(String clientIP) {
		this.clientIP = clientIP;
	}

	public String getPackagename() {
		return packagename;
	}

	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

}
