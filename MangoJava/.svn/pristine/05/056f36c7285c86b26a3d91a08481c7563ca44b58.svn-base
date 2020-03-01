/***********************************************************************
     * $RCSfile: LoginInfoVO.java,v $  $Revision: 1.3.10.2 $  $Date: 2009/09/03 07:00:32 $
 * $Log: LoginInfoVO.java,v $
 * Revision 1.3.10.2  2009/09/03 07:00:32  wangqi
 * *** empty log message ***
 *
 * Revision 1.3.10.1  2009/08/14 09:23:53  wangqi
 * *** empty log message ***
 *
 * Revision 1.3  2007/05/31 07:41:33  qilin
 * code format
 *
 * Revision 1.2  2007/05/23 09:37:49  qilin
 * no message
 *
 * Revision 1.1  2007/05/17 06:22:09  qilin
 * no message
 *
 * Revision 1.2  2007/03/16 05:34:27  qilin
 * no message
 *
 * Revision 1.1  2007/03/14 10:24:26  qilin
 * 登陆方式的修改，合并登陆service为SystemLoginServiceIFC
 * 将登陆信息封装为LoginInfoVO对象
 *
*************************************************************************/
package smartx.system.login.vo;

import java.io.*;
import java.util.*;

/**
 * 登录情况反馈
 * @author Administrator
 *
 */
public class LoginInfoVO implements Serializable {
	private static final long serialVersionUID = -2747584812716114979L;
	private String name;
    private String loginName;
    private String id;
    private String code;
    private String regionCode;
    private long totalLoginCount;
    private long userLoginCount;
    private Date loginTime;
    private boolean isAdmin=false;
    private Vector workPositionID;
    public String dispatchtype;
    private Boolean isBlackUser;//黑名单用户
   

	public Boolean getIsBlackUser() {
		return isBlackUser;
	}

	public void setIsBlackUser(Boolean isBlackUser) {
		this.isBlackUser = isBlackUser;
	}

	public String getDispatchtype() {
		return dispatchtype;
	}

	public void setDispatchtype(String dispatchtype) {
		this.dispatchtype = dispatchtype;
	}

	private String dbReadWriteAuth;//读写权限
    

    private int loginStatus;

    private HashMap userParam = new HashMap();

    public LoginInfoVO() {
    }

    public boolean isAdmin(){
    	return isAdmin;
    }
    
    public void setAdmin(boolean is){
    	isAdmin=is;
    }
    
    public String getCode() {
        return code;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public long getTotalLoginCount() {
        return totalLoginCount;
    }

    public long getUserLoginCount() {
        return userLoginCount;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public int getLoginStatus() {
        return loginStatus;
    }

    public String getLoginName() {
        return loginName;
    }

    public HashMap getUserParam() {
        return userParam;
    }

    public Vector getWorkPositionID() {
        return workPositionID;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public void setTotalLoginCount(long totalLoginCount) {
        this.totalLoginCount = totalLoginCount;
    }

    public void setUserLoginCount(long userLoginCount) {
        this.userLoginCount = userLoginCount;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public void setLoginStatus(int loginStatus) {
        this.loginStatus = loginStatus;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public void setUserParam(HashMap userParam) {
        this.userParam = userParam;
    }

    public void setWorkPositionID(Vector workPositionID) {
        this.workPositionID = workPositionID;
    }

    public void putUserParamValue(Object key, Object value) {
        userParam.put(key, value);
    }

    public Object getUserParamValue(Object _key) {
        Iterator it = userParam.keySet().iterator();
        if (it != null) {
            while (it.hasNext()) {
                Object key = it.next();
                if (key.equals(_key)) {
                    return userParam.get(key);
                }
            }
        }
        return null;
    }

	public String getDbReadWriteAuth() {
		return dbReadWriteAuth;
	}

	public void setDbReadWriteAuth(String dbReadWriteAuth) {
		this.dbReadWriteAuth = dbReadWriteAuth;
	}

}
