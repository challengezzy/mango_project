/**************************************************************************
 * $RCSfile: LoginSQLDefineConstant.java,v $  $Revision: 1.3.10.5 $  $Date: 2010/03/13 04:29:22 $
 **************************************************************************/
package smartx.system.login.tb;

import java.io.*;

public class LoginSQLDefineConstant implements Serializable {

    private static final long serialVersionUID = -1200947584885273690L;

    //取得菜单的树 代码中的{uid}表示实际的用户id
    public static String SQL_GETMENU = "SELECT * FROM PUB_MENU WHERE (APPMODULE = 'NOVA' OR ',{appmodule},' LIKE '%,' || APPMODULE || ',%') ORDER BY SEQ"; 
    public static String SQL_GETMENU_USER = "SELECT * FROM ( "
    	+"SELECT * FROM PUB_MENU WHERE (APPMODULE = 'NOVA' OR ',{appmodule},' LIKE '%,' || APPMODULE || ',%') AND ID IN (SELECT MENUID FROM PUB_USER_MENU WHERE USERID={uid}) "
    	+"UNION "
    	+"SELECT * FROM PUB_MENU WHERE (APPMODULE = 'NOVA' OR ',{appmodule},' LIKE '%,' || APPMODULE || ',%') AND ID IN (SELECT MENUID FROM PUB_ROLE_MENU WHERE ROLEID IN ( SELECT ROLEID FROM PUB_USER_ROLE WHERE USERID={uid})) "
    	+"UNION "
    	+"SELECT * FROM PUB_MENU WHERE (APPMODULE = 'NOVA' OR ',{appmodule},' LIKE '%,' || APPMODULE || ',%') AND ID IN (SELECT MENUID FROM PUB_WORKPOSITION_MENU WHERE WORKPOSITIONID IN ( SELECT WORKPOSITIONID FROM PUB_USER_WORKPOSITION WHERE USERID={uid})) "
    	+") T ORDER BY SEQ"; 

    //取得菜单快捷按钮，必须末节点 代码中的{uid}表示实际的用户id
    public static String SQL_GETBTNPANEL = "SELECT * FROM PUB_MENU WHERE (APPMODULE = 'NOVA' OR ',{appmodule},' LIKE '%,' || APPMODULE || ',%') AND ID NOT IN (SELECT PARENTMENUID FROM PUB_MENU WHERE PARENTMENUID IS NOT NULL) AND NVL(SHOWINTOOLBAR,'N') ='Y' ORDER BY TOOLBARSEQ";
    public static String SQL_GETBTNPANEL_USER = "SELECT * FROM ( "
    	+"SELECT * FROM PUB_MENU WHERE (APPMODULE = 'NOVA' OR ',{appmodule},' LIKE '%,' || APPMODULE || ',%') AND ID IN (SELECT MENUID FROM PUB_USER_MENU WHERE USERID={uid}) "
    	+"UNION "
    	+"SELECT * FROM PUB_MENU WHERE (APPMODULE = 'NOVA' OR ',{appmodule},' LIKE '%,' || APPMODULE || ',%') AND ID IN (SELECT MENUID FROM PUB_ROLE_MENU WHERE ROLEID IN ( SELECT ROLEID FROM PUB_USER_ROLE WHERE USERID={uid})) "
    	+"UNION "
    	+"SELECT * FROM PUB_MENU WHERE (APPMODULE = 'NOVA' OR ',{appmodule},' LIKE '%,' || APPMODULE || ',%') AND ID IN (SELECT MENUID FROM PUB_WORKPOSITION_MENU WHERE WORKPOSITIONID IN ( SELECT WORKPOSITIONID FROM PUB_USER_WORKPOSITION WHERE USERID={uid})) "
    	+") T WHERE  ID NOT IN (SELECT PARENTMENUID FROM PUB_MENU WHERE PARENTMENUID IS NOT NULL) AND NVL(SHOWINTOOLBAR,'N') ='Y' ORDER BY TOOLBARSEQ";
    	
    

    public static String SQL_GETNEWS = "SELECT * FROM (SELECT * FROM PUB_NEWS ORDER BY CREATETIME DESC) WHERE ROWNUM<6"; //取得新闻数据

    public static String SQL_GETTASKCOMMIT = "SELECT * FROM PUB_TASK_COMMIT"; //取得已提交任务数据

    public static String SQL_GETTASKDEAL = "SELECT * FROM PUB_TASK_DEAL"; //取得已提交任务数据

    //获得用户信息
    public static String SQL_GET_USERINFO_BY_NAME="SELECT * FROM PUB_USER WHERE LOGINNAME='{loginname}'";
    public static String SQL_GET_USERINFO_BY_ID="SELECT * FROM PUB_USER WHERE ID='{uid}'";
}
/**************************************************************************
 * $RCSfile: LoginSQLDefineConstant.java,v $  $Revision: 1.3.10.5 $  $Date: 2010/03/13 04:29:22 $
 *
 * $Log: LoginSQLDefineConstant.java,v $
 * Revision 1.3.10.5  2010/03/13 04:29:22  wangqi
 * *** empty log message ***
 *
 * Revision 1.3.10.4  2009/08/14 09:23:53  wangqi
 * *** empty log message ***
 *
 * Revision 1.3.10.3  2008/11/26 03:03:36  wangqi
 * *** empty log message ***
 *
 * Revision 1.3.10.2  2008/11/25 10:27:29  wangqi
 * *** empty log message ***
 *
 * Revision 1.3.10.1  2008/09/17 07:36:46  wangqi
 * patch   : 20080917
 * file    : nova_20080128_20080917.jar
 * content : 处理 MR nova20-83
 *
 * Revision 1.3  2007/05/31 07:41:35  qilin
 * code format
 *
 * Revision 1.2  2007/05/24 08:27:51  qilin
 * no message
 *
 * Revision 1.1  2007/05/17 06:22:10  qilin
 * no message
 *
 * Revision 1.4  2007/03/22 01:44:07  qilin
 * no message
 *
 * Revision 1.3  2007/03/15 01:50:09  sunxf
 * 系統公告按時間排序，取最近五條
 *
 * Revision 1.2  2007/01/30 04:20:39  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
