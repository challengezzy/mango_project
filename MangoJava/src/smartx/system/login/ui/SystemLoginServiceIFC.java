/***********************************************************************
 * $RCSfile: SystemLoginServiceIFC.java,v $  $Revision: 1.4.10.4 $  $Date: 2009/10/21 06:25:46 $
 * $Log: SystemLoginServiceIFC.java,v $
 * Revision 1.4.10.4  2009/10/21 06:25:46  wangqi
 * *** empty log message ***
 *
 * Revision 1.4.10.3  2009/09/03 06:58:21  wangqi
 * *** empty log message ***
 *
 * Revision 1.4.10.2  2009/08/14 09:23:53  wangqi
 * *** empty log message ***
 *
 * Revision 1.4.10.1  2008/07/11 03:13:04  wangqi
 * *** empty log message ***
 *
 * Revision 1.4  2007/05/31 07:41:32  qilin
 * code format
 *
 * Revision 1.3  2007/05/18 08:02:35  qilin
 * no message
 *
 * Revision 1.2  2007/05/17 08:32:41  qilin
 * no message
 *
 * Revision 1.1  2007/05/17 06:22:08  qilin
 * no message
 *
 * Revision 1.5  2007/04/06 01:53:44  qilin
 * no message
 *
 * Revision 1.4  2007/03/19 05:46:31  qilin
 * no message
 *
 * Revision 1.3  2007/03/16 09:26:41  qilin
 * no message
 *
 * Revision 1.2  2007/03/16 05:34:28  qilin
 * no message
 *
 * Revision 1.1  2007/03/14 10:24:25  qilin
 * 登陆方式的修改，合并登陆service为SystemLoginServiceIFC
 * 将登陆信息封装为LoginInfoVO对象
 *
 *************************************************************************/
package smartx.system.login.ui;

import java.util.HashMap;

import org.jdom.Document;

import smartx.framework.common.ui.*;
import smartx.framework.common.vo.HashVO;
import smartx.system.login.vo.*;


public interface SystemLoginServiceIFC extends NovaRemoteCallServiceIfc {
    /**
     * 普通用户登录失败
     */
    public final static int USER_ERROR_TYPE = 1;

    /**
     * 普通用户登录成功
     */
    public final static int USER_LOGINOK_TYPE = 2;

    /**
     * 管理员登录失败
     */
    public final static int ADMINUSER_ERROR_TYPE = 3;

    /**
     * 管理员登录成功
     */
    public final static int ADMINUSER_LOGINOK_TYPE = 4;

    /**
     * 用户帐户停用
     */
    public final static int USER_ACCOUTSTATUS_DISABLED = 5;

    /**
     * 用户帐户过期
     */
    public final static int USER_ACCOUT_EXPDATE = 6;

    /**
     * 用户密码过期
     */
    public final static int USER_PWD_EXPDATE = 7;
    
    /**
     * 未授权的IP地址
     */
    public final static int USER_IP_UNAUTHORIZED = 8;

    /**
     * 获取登录结果
     *
     * @param _username
     * @param _password
     * @return
     */
    public LoginInfoVO login(HashMap params) throws Exception; //
    
    /**
     * 获取登录结果
     *
     * @param _username
     * @param _password
     * @return
     */
    public LoginInfoVO login(String _username, String _password) throws Exception; //

    /**
     * 获取管理员登录结果
     *
     * @param _username
     * @param _password
     * @param _adminpwd
     * @return
     */
    public LoginInfoVO login(String _username, String _password, String _adminpwd) throws Exception; //

    /**
     * 给后台调用的获取登录信息
     * @param loginname
     * @return
     */
    public LoginInfoVO getUserInfoByLogin(String loginname)throws Exception;
    
    /**
     * 用户退出
     * @param _userID String
     */
    public void logout(String _userID); //

    /**
     * 加密用户密码,
     *
     * @param _userid:用户名
     * @param _pwd:密码
     * @return
     */
    public String modifyPasswd(String _userid, String _pwd);

    /**
     * 处理修改密码
     *
     * @param _loginuser
     * @param _pwd
     * @return
     */
    public boolean resetPwd(String _loginuser, String _pwd) throws Exception;

    /**
     * 获得登录信息
     * @param workposition
     * @param _loginuserID
     * @return
     * @throws Exception
     */
    public DeskTopVO getDeskTopVO(String[] workposition, String _loginuserID) throws Exception;

    /**
     * 检索消息。
     * 根据消息类型和当前用户检索系统消息，消息按照时间倒排序。
     * @param uId 登录用户
     * @param type 消息类型
     * @param sql 检索sql
     * @return
     * @throws Exception
     */
    public HashVO[] getMessages(String uId,String type,String sql)throws Exception;
    
    /**
     * 获得桌面定义
     * @return
     * @throws Exception
     */
    public Document getDeskTopDesign()throws Exception;
    
    
    /**
     * 清理用户在线状态
     */
    public void initUserOnlineStatus();
    
    /**
     * 获得用户所属地域
     * @return
     */
    public HashVO[] getRegions(String uId) throws Exception;

}
