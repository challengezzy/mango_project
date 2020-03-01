/**
 * 
 */
package smartx.publics.form.vo;

import smartx.framework.common.bs.SysConst;
import smartx.framework.common.utils.Sys;

/**
 * @author caohenghui
 *
 */
public class InitMetaDataConst {
	
	public static String RootPath = (String) Sys.getInfo("NOVA2_SYS_ROOTPATH");
	
	public static String InitDatas_DIR = "initmetadata";
	
	public static String MT_INITDATAS = "mt_initmetadata";
	
	public static String SMARTXCONFIG = (String) Sys.getInfo("NOVA2_SYS_ROOTPATH")+"WEB-INF/"+SysConst.SmartXConfig;

}
