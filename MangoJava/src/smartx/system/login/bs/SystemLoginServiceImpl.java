/**************************************************************************
 * $RCSfile: SystemLoginServiceImpl.java,v $  $Revision: 1.15.4.10 $  $Date: 2009/12/02 05:48:51 $
 **************************************************************************/

package smartx.system.login.bs;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.jdom.Document;

import flex.messaging.FlexContext;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.bs.BootServlet;
import smartx.framework.common.bs.NovaSessionFactory;
import smartx.framework.common.ui.NovaClientEnvironment;
import smartx.framework.common.ui.NovaConstants;
import smartx.framework.common.utils.StringUtil;
import smartx.framework.common.utils.Sys;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaLogger;
import smartx.framework.common.vo.NovaRemoteException;
import smartx.system.common.constant.CommonSysConst;
import smartx.system.common.constant.LogConst;
import smartx.system.log.bs.LogWriter;
import smartx.system.login.tb.LoginSQLDefineConstant;
import smartx.system.login.ui.Md5Digest;
import smartx.system.login.ui.SystemLoginServiceIFC;
import smartx.system.login.vo.DeskTopVO;
import smartx.system.login.vo.LoginInfoVO;

public class SystemLoginServiceImpl implements SystemLoginServiceIFC {
    //管理员的默认id，由于管理员不是pub_user表中的用户，所以需要指定一个默认的id
    protected static String SYSTEM_DEFAULT_USER_ID = "-1";
    private Logger logger=NovaLogger.getLogger(SystemLoginServiceImpl.class);

    /**
     * 登录并返回登录信息
     * 
     */
    public LoginInfoVO login(HashMap params) throws Exception {
        if(params==null) throw new Exception("非法登录");
    	String user=(String)params.get("user");
    	if(user==null) throw new Exception("非法登录");
        String pwd=(String)params.get("pwd");
        String adpwd=(String)params.get("adpwd");
        if(adpwd==null){
        	return login(user, pwd, null, false);
        }else{
        	return login(user, pwd, adpwd, true);
        }
    }
    /**
     * 登录并返回登录信息
     * 
     */
    public LoginInfoVO login(String _usercode, String _pwd) throws Exception {
        return login(_usercode, _pwd, null, false);
    }

    /**
     * 登录并返回登录信息
     */
    public LoginInfoVO login(String _usercode, String _pwd, String _adminpwd) throws Exception {
        return login(_usercode, _pwd, _adminpwd, true);
    }
    
    /**
     * 登录实现
     * @param _usercode
     * @param _pwd
     * @param _adminpwd
     * @param isAdmin
     * @return
     * @throws Exception
     */
    protected LoginInfoVO login(String _usercode, String _pwd, String _adminpwd, boolean isAdmin) throws Exception {
        LoginInfoVO loginInfo = new LoginInfoVO();
        loginInfo.setAdmin(isAdmin);
        
        //Flex客户端的怎样信息
        HttpServletRequest request = FlexContext.getHttpRequest();
        String clientIp = request.getRemoteAddr();
        
        NovaClientEnvironment clientEnv=NovaSessionFactory.getInstance().getClientEnv(Thread.currentThread());
        if(clientEnv.get("SYS_LOGINUSER_LOGINNAME")==null){
        	clientEnv.put("SYS_LOGINUSER_LOGINNAME",_usercode);
        }
        clientEnv.put("CLIENTIP", clientIp);
        
        if (isSystemDefaultUser(isAdmin, _usercode, _pwd, _adminpwd, loginInfo)) {
            new LogWriter().writeLog(LogConst.OPERATION_SUPERLOGIN,null);
            return loginInfo;
        }
        String str_newpasswd = modifyPasswd(_usercode, _pwd); //加密密码
        String str_newadminpasswd = modifyPasswd(_usercode, _adminpwd); //加密密码
        String _sql =
            "SELECT * FROM PUB_USER WHERE LOGINNAME = '" +
            _usercode + "' AND PWD='" + str_newpasswd.trim() + "'";
        if (isAdmin) {
        	
        	_sql = _sql + " AND ADMINPWD='" + str_newadminpasswd.trim() + "'";
        }
        
        try {
            HashVO[] queryResult = execQuery(_sql);
            if (queryResult.length <= 0) {
                if (isAdmin) {
                    loginInfo.setLoginStatus(SystemLoginServiceIFC.ADMINUSER_ERROR_TYPE);
                } else {
                    loginInfo.setLoginStatus(SystemLoginServiceIFC.USER_ERROR_TYPE);
                }
                new LogWriter().writeLog(LogConst.OPERATION_LOGIN_FAILED,null);
            } else {
            	
            	String ipAddress = queryResult[0].getStringValue("EXTENDATTRIBUTE20");
                if(ipAddress != null && !ipAddress.trim().equals("")){
                	if(!this.isAuthorizedIP(clientIp,ipAddress)){
                		 loginInfo.setLoginStatus(SystemLoginServiceIFC.USER_IP_UNAUTHORIZED);
                		 return loginInfo;
                	}
                }
            	
                if (isAdmin) {
                    loginInfo.setLoginStatus(SystemLoginServiceIFC.ADMINUSER_LOGINOK_TYPE);
                    new LogWriter().writeLog(LogConst.OPERATION_ADMINLOGIN,null);
                } else {
                    loginInfo.setLoginStatus(SystemLoginServiceIFC.USER_LOGINOK_TYPE);
                    new LogWriter().writeLog(LogConst.OPERATION_LOGIN,null);
                }
                setLoginInfo(loginInfo, queryResult);
            }
        } catch (NovaRemoteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loginInfo;
    }
    
    private boolean isAuthorizedIP(String clientIP,String ipaddress){
    	boolean flag = false;
    	try{
    		String[] ipArray = ipaddress.trim().split(",");
    		clientIP = clientIP.replaceAll("\\.", ";");
    		if(ipArray != null ){
    			for(String ip : ipArray){
    				ip = ip.replaceAll("\\.", ";");
    				ip = ip.replaceAll("\\*", "[0-9]{1,3}");
    				if(clientIP.matches(ip)){
    					flag = true;
    					break;
    				}
    			}
    		}
    		
    	}catch(Exception e){
    		logger.debug("",e);
    		flag = false;
    	}
    	return flag;
    }
    
    /**
     * 给后台调用的获取登录信息
     * @param loginname
     * @return
     */
    public LoginInfoVO getUserInfoByLogin(String loginname)throws Exception{
    	//判断是否第三方登录
    	if(!NovaConstants.SYS_LOGIN_3TH.equalsIgnoreCase((String)Sys.getInfo("LOGIN_TYPE"))){
    		return null;
    	}
    	
    	LoginInfoVO loginInfo = new LoginInfoVO();
    	HashMap param=new HashMap();
    	param.put("loginname", new String[]{loginname});
    	String sql=StringUtil.buildExpressionArray(param, LoginSQLDefineConstant.SQL_GET_USERINFO_BY_NAME,0);
    	try{
    		HashVO[] rts=execQuery(sql);
    		if (rts.length <= 0) {
                loginInfo.setLoginStatus(SystemLoginServiceIFC.USER_ERROR_TYPE);                
            } else {
                loginInfo.setLoginStatus(SystemLoginServiceIFC.USER_LOGINOK_TYPE);
                setLoginInfo(loginInfo, rts);
            }
    	}catch(Exception e){
    		loginInfo.setLoginStatus(SystemLoginServiceIFC.USER_ERROR_TYPE);
    	}  	
    	return loginInfo;
    }
    
    

    private boolean isSystemDefaultUser(boolean isAdmin, String _usercode, String _pwd, String _adminpwd,
                                        LoginInfoVO loginInfo) {
        boolean result = false;
        if (isAdmin) {
            if (_usercode.equals(BootServlet.sysDefaultUserName) && _pwd.equals(BootServlet.sysDefaultUserPwd) &&
                _adminpwd.equals(BootServlet.sysDefaultUserAdminPwd)) {
                loginInfo.setLoginStatus(SystemLoginServiceIFC.ADMINUSER_LOGINOK_TYPE);
                result = true;
            }
        } else {
            if (_usercode.equals(BootServlet.sysDefaultUserName) && _pwd.equals(BootServlet.sysDefaultUserPwd)) {
                loginInfo.setLoginStatus(SystemLoginServiceIFC.USER_LOGINOK_TYPE);
                result = true;
            }
        }
        if (result) {
            loginInfo.setId(SYSTEM_DEFAULT_USER_ID); //
            loginInfo.setName(_usercode); //
            loginInfo.setLoginName(_usercode); //
            loginInfo.setLoginTime(new Date());
        }
        return result;
    }

    private void setLoginInfo(LoginInfoVO loginInfo, HashVO[] queryResult) {
        loginInfo.setId(queryResult[0].getStringValue("ID")); //
        loginInfo.setCode(queryResult[0].getStringValue("LOGINNAME"));
        loginInfo.setName(queryResult[0].getStringValue("NAME")); //
        loginInfo.setLoginName(queryResult[0].getStringValue("LOGINNAME")); //
        loginInfo.setRegionCode(queryResult[0].getStringValue("REGIONCODE")); //
        loginInfo.setLoginTime(new Date(System.currentTimeMillis()));
        loginInfo.setDbReadWriteAuth(queryResult[0].getStringValue("EXTENDATTRIBUTE1"));
        loginInfo.setDispatchtype(queryResult[0].getStringValue("DISPATCHTYPE"));
        System.out.println("----------------"+queryResult[0].getStringValue("EXTENDATTRIBUTE5")+"--------------------");
        //add by zhangzz 20121016 start 添加黑名单用户项
        loginInfo.setIsBlackUser(queryResult[0].getBooleanValue("EXTENDATTRIBUTE4"));
        //add by zhangzz 20121016 end
        if (queryResult[0].getLognValue("LOGINCOUNT") != null) {
            loginInfo.setUserLoginCount(queryResult[0].getLognValue("LOGINCOUNT").longValue());
        }
        if (queryResult[0].getLognValue("LOGINTIMECOUNT") != null) {
            loginInfo.setTotalLoginCount(queryResult[0].getLognValue("LOGINTIMECOUNT").longValue());
        }
        if (queryResult[0].getIntegerValue("ACCOUNTSTATUS") != null &&
            queryResult[0].getIntegerValue("ACCOUNTSTATUS").intValue() ==
            CommonSysConst.PUB_USER_ACCOUNTSTATUS_DISABLED) {
            loginInfo.setLoginStatus(SystemLoginServiceIFC.USER_ACCOUTSTATUS_DISABLED);
            return;
        }
        Date nowDate = new Date(System.currentTimeMillis());
        if (queryResult[0].getDateValue("EXPDATE") != null && nowDate.after(queryResult[0].getDateValue("EXPDATE"))) {
            loginInfo.setLoginStatus(SystemLoginServiceIFC.USER_ACCOUT_EXPDATE);
            return;
        }
        if (queryResult[0].getDateValue("PWDEXPDATE") != null && nowDate.after(queryResult[0].getDateValue("PWDEXPDATE"))) {
            loginInfo.setLoginStatus(SystemLoginServiceIFC.USER_PWD_EXPDATE);
            return;
        }
        String _sqlWorkPosition = "SELECT * FROM PUB_USER_WORKPOSITION WHERE USERID = " +
            queryResult[0].getStringValue("ID");
        try {
            HashVO[] voWorkPosition = execQuery(_sqlWorkPosition);
            if (voWorkPosition.length > 0) {
                Vector workPositionID = new Vector();
                for (int i = 0; i < voWorkPosition.length; i++) {
                    workPositionID.add(voWorkPosition[i].getStringValue("WORKPOSITIONID"));
                }
                loginInfo.setWorkPositionID(workPositionID);
            }
        } catch (Exception ex) {
        }

        updateUserInfo(queryResult);
    }

    private void updateUserInfo(HashVO[] queryResult) {
        long id = queryResult[0].getLognValue("ID").longValue();
        long loginCount = 0;
        if (queryResult[0].getLognValue("LOGINCOUNT") != null) {
            loginCount = queryResult[0].getLognValue("LOGINCOUNT").longValue();
        }
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String _sql = "UPDATE PUB_USER SET ISONLINE=" + CommonSysConst.GLOBAL_BOOLEAN_TRUE +
                ",LOGINCOUNT=" + (loginCount + 1) +
                ",LOGINTIME=TO_DATE('" + format.format(new Date(System.currentTimeMillis())) +
                "','YYYY-MM-DD HH24:MI:SS')" +
                " WHERE ID=" + id;
            new CommDMO(false).executeUpdateByDS(CommonSysConst.DATASOURCE_USERMGMT,_sql);
        } catch (Exception e) {
            logger.error("发生异常：",e);
        }
    }

    /**
     * 用户退出
     * @param _userID String
     */
    public void logout(String _userID) {
        try {
        	//Flex客户端的怎样信息
            HttpServletRequest request = FlexContext.getHttpRequest();
            String clientIp = request.getRemoteAddr();
            
            NovaClientEnvironment clientEnv=NovaSessionFactory.getInstance().getClientEnv(Thread.currentThread());
            
            if(clientEnv == null)
            	clientEnv = NovaClientEnvironment.getInstance();
            
            if(clientEnv.get("SYS_LOGINUSER_LOGINNAME")==null){
            	clientEnv.put("SYS_LOGINUSER_LOGINNAME",_userID);
            }
            clientEnv.put("CLIENTIP", clientIp);
            
            String _sql = "SELECT ID,LOGINTIME,LOGINTIMECOUNT FROM PUB_USER WHERE ID = " + _userID;
            HashVO[] queryResult = execQuery(_sql);
            if (queryResult.length > 0) {
                Date loginTime = queryResult[0].getDateValue("LOGINTIME");
                Date nowTime = new Date(System.currentTimeMillis());
                long timeStanding = nowTime.getTime() - loginTime.getTime();
                long onlineTime = timeStanding / 1000;
                if (queryResult[0].getLognValue("LOGINTIMECOUNT") != null) {
                    onlineTime = onlineTime + queryResult[0].getLognValue("LOGINTIMECOUNT").longValue();
                }
                _sql = "UPDATE PUB_USER SET ISONLINE=" + CommonSysConst.GLOBAL_BOOLEAN_FALSE +
                    ",LOGINTIMECOUNT=" + onlineTime + " WHERE ID=" + _userID;
                new CommDMO(false).executeUpdateByDS(CommonSysConst.DATASOURCE_USERMGMT, _sql);
            }
            new LogWriter().writeLog(LogConst.OPERATION_EXIT,null);
        } catch (Exception e) {
        	logger.error("发生异常：",e);
        }
    }

    // 清理用户在线状态
    public void initUserOnlineStatus() {
        try {
            String _sql = "update pub_user set ISONLINE=" + 0;
            new CommDMO(false).executeUpdateByDS(null,_sql);
        } catch (Exception e) {
        	logger.error("清理用户在线状态出错！"+e.getMessage());        	
        }
    }

    /**
     * 加密用户密码,即用加密算法进行处理...
     */
    public String modifyPasswd(String _userid, String _pwd) {
        Md5Digest digest = new Md5Digest();
        String newpassword = digest.generatePasswd(_userid, _pwd);
        return newpassword;
//        return _pwd; //不加密
    }

    

    /**
     * 重置密码（MD5）
     * 
     */
    public boolean resetPwd(String _userLoginName, String _pwd) throws Exception {
        Md5Digest digest = new Md5Digest();
        String newPwd = digest.generatePasswd(_userLoginName, _pwd);
        String _sql = "UPDATE PUB_USER SET PWD='" + newPwd +
            "' WHERE LOGINNAME='" + _userLoginName + "'";
        (new CommDMO()).executeUpdateByDS(CommonSysConst.DATASOURCE_USERMGMT, _sql);
        return true;
    }


    /**
     * 检索用户相关信息
     * @param sql
     * @return
     * @throws Exception
     */
    protected HashVO[] execQuery(String sql) throws Exception {
        return (new CommDMO()).getHashVoArrayByDS(CommonSysConst.DATASOURCE_USERMGMT, sql);
    }

    /**
     * 更新用户相关信息
     * @param sql
     * @throws Exception
     */
    protected void execUpdate(String sql) throws Exception {
        (new CommDMO()).executeUpdateByDS(CommonSysConst.DATASOURCE_USERMGMT, sql);
    }

    
    
    
    /**
     * 获得桌面定义授权信息（菜单、按钮等等）
     */
    public DeskTopVO getDeskTopVO(String[] workposition, String _loginuserID) throws Exception {
        HashVO[] vos_1 = execQuery(getMenuSQL(_loginuserID)); // 菜单
        HashVO[] vos_2 = execQuery(getButtonSQL(_loginuserID)); // 按钮栏
        HashVO[] vos_3 = execQuery(getNewsSQL()); // 新闻
        HashVO[] vos_4 = execQuery(getTaskcommitSQL(workposition)); // 已提交任务
        HashVO[] vos_5 = execQuery(getTaskdealSQL(workposition)); // 待处理任务

        DeskTopVO deskTopVO = new DeskTopVO();
        deskTopVO.setMenuVOs(vos_1);
        deskTopVO.setBtnVOs(vos_2);
        deskTopVO.setNewsVOs(vos_3);
        deskTopVO.setTaskCommitVOs(vos_4);
        deskTopVO.setTaskDealVOs(vos_5);

        return deskTopVO; //
    }

    /**
     * 获得菜单
     * @param uid
     * @return
     */
    protected String getMenuSQL(String uid) {
    	String rt=null;
        if (uid == null || uid.equals(SYSTEM_DEFAULT_USER_ID)) {//超级管理员
            rt= LoginSQLDefineConstant.SQL_GETMENU
            		.replaceAll("\\{appmodule\\}",(String)Sys.getInfo("APPMODULE"));
        } else {
            //return "SELECT * FROM PUB_MENU WHERE ID IN(SELECT MENUID FROM PUB_USER_MENU WHERE USERID=" + uid + ") ORDER BY SEQ";            
            rt= LoginSQLDefineConstant.SQL_GETMENU_USER
                    .replaceAll("\\{uid\\}", uid)
                    .replaceAll("\\{appmodule\\}",(String)Sys.getInfo("APPMODULE"));            
        }
        return rt;
    }
    
    /**
     * 获得快捷工具按钮
     * @param uid
     * @return
     */
    protected String getButtonSQL(String uid) {
    	String rt=null;
    	if (uid == null || uid.equals(SYSTEM_DEFAULT_USER_ID)) {
        	rt= LoginSQLDefineConstant.SQL_GETBTNPANEL
        			.replaceAll("\\{appmodule\\}",(String)Sys.getInfo("APPMODULE"));
        } else {
            rt= LoginSQLDefineConstant.SQL_GETBTNPANEL_USER
                    .replaceAll("\\{uid\\}", uid)
                    .replaceAll("\\{appmodule\\}",(String)Sys.getInfo("APPMODULE"));            
        } 	
    	return rt;
    	
    }

    /**
     * 获得待办事项
     * @param workposition
     * @return
     */
    protected String getTaskcommitSQL(String[] workposition) {
        // 根据工位获取待处理任务列表
        StringBuffer sql = new StringBuffer();
        sql.append(LoginSQLDefineConstant.SQL_GETTASKCOMMIT);
        if (workposition != null && workposition.length >= 0) {
            for (int i = 0; i < workposition.length; i++) {
                if (i == 0) {
                    sql.append(" WHERE WORKPOSITION LIKE '" + workposition[i] + "'");
                } else {
                    sql.append(" OR WORKPOSITION LIKE '" + workposition[i] + "'");
                }
            }
        }
        return sql.toString();
    }

    /**
     * 获取待办事项
     * @param workposition
     * @return
     */
    protected String getTaskdealSQL(String[] workposition) {
        // 根据工位获取已处理任务列表,只取最近十条记录
        StringBuffer sql = new StringBuffer();
        if (workposition == null) {
            sql.append(LoginSQLDefineConstant.SQL_GETTASKDEAL);
        } else {
            sql.append("SELECT * FROM ( ");
            sql.append(LoginSQLDefineConstant.SQL_GETTASKDEAL);
            if (workposition.length >= 0) {
                for (int i = 0; i < workposition.length; i++) {
                    if (i == 0) {
                        sql.append(" WHERE WORKPOSITION LIKE '" + workposition[i] + "'");
                    } else {
                        sql.append(" OR WORKPOSITION LIKE '" + workposition[i] + "'");
                    }
                }
            }
            sql.append(" ORDER BY CREATETIME DESC ) WHERE ROWNUM<10");
        }
        return sql.toString();
    }

    
    protected String getNewsSQL() {
        return LoginSQLDefineConstant.SQL_GETNEWS;
    }

    public DeskTopVO getDeskTopVO(String[] workposition) throws Exception {
        return getDeskTopVO(workposition, null);
    }

    
    /**
     * 获得消息
     */
	public HashVO[] getMessages(String id, String type,String sql) throws Exception {
		String qsql=sql;
		if(sql==null||sql.trim().equals("")){
			qsql=(type==null||type.trim().equals(""))
				?"select id,title,to_char(starttime,'yyyy-MM-dd HH24:MI:ss') starttime,to_char(endtime,'yyyy-MM-dd HH24:MI:ss')endtime,degree,content," +
				 "to_char(createtime,'yyyy-MM-dd') createtime,type from PUB_MESSAGES where starttime<=sysdate and endtime>=sysdate order by id desc"
				:"select id,title,to_char(starttime,'yyyy-MM-dd HH24:MI:ss') starttime,to_char(endtime,'yyyy-MM-dd HH24:MI:ss')endtime,degree,content," +
				 "to_char(createtime,'yyyy-MM-dd') createtime,type from PUB_MESSAGES where starttime<=sysdate and endtime>=sysdate and type='abc' order by id desc";
		}
		return (new CommDMO()).getHashVoArrayByDS(null,qsql);		
	}

	
	/**
	 * 获得桌面定义信息
	 * @return Document 桌面定义的XML文档
	 */
	public Document getDeskTopDesign() throws Exception {
		//String sysRootPath=System.getProperty("NOVA2_SYS_ROOTPATH");
		String sysRootPath=(String)Sys.getInfo("NOVA2_SYS_ROOTPATH");
        
        String str_filePath = sysRootPath + "WEB-INF/Nova2Desktop.xml"; //
		Document doc=Sys.loadXML(str_filePath);        
        return doc;
	}
    
	/**
     * 获得用户所属地域
     * @return
     */
    public HashVO[] getRegions(String uId) throws Exception{
    	String sql="SELECT * FROM REGION WHERE ID IN (SELECT REGIONID FROM PUB_USER_REGION WHERE USERID="+uId+") AND TYPE=1";
    	return (new CommDMO()).getHashVoArrayByDS(null,sql);	
    	
    }
    
    
    

}
/*******************************************************************************
 * $RCSfile: SystemLoginServiceImpl.java,v $ $Revision: 1.15.4.10 $ $Date: 2007/02/27
 * 09:49:43 $
 *
 * $Log: SystemLoginServiceImpl.java,v $
 * Revision 1.15.4.10  2009/12/02 05:48:51  wangqi
 * *** empty log message ***
 *
 * Revision 1.15.4.9  2009/10/21 06:25:32  wangqi
 * *** empty log message ***
 *
 * Revision 1.15.4.8  2009/09/03 06:55:13  wangqi
 * *** empty log message ***
 *
 * Revision 1.15.4.7  2009/08/14 09:23:53  wangqi
 * *** empty log message ***
 *
 * Revision 1.15.4.6  2009/01/13 06:43:26  wangqi
 * *** empty log message ***
 *
 * Revision 1.15.4.5  2008/12/16 06:29:16  wangqi
 * *** empty log message ***
 *
 * Revision 1.15.4.4  2008/11/25 10:27:24  wangqi
 * *** empty log message ***
 *
 * Revision 1.15.4.3  2008/09/17 07:36:41  wangqi
 * patch   : 20080917
 * file    : nova_20080128_20080917.jar
 * content : 处理 MR nova20-83
 *
 * Revision 1.15.4.2  2008/07/11 03:12:56  wangqi
 * *** empty log message ***
 *
 * Revision 1.15.4.1  2008/06/18 01:38:13  wangqi
 * *** empty log message ***
 *
 *
 ******************************************************************************/
