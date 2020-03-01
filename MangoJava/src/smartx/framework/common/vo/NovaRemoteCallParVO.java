/**************************************************************************
 * $RCSfile: NovaRemoteCallParVO.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:23 $
 **************************************************************************/
package smartx.framework.common.vo;

import java.io.*;

import smartx.framework.common.ui.*;


public class NovaRemoteCallParVO implements Serializable {

    private static final long serialVersionUID = 8108429561075237846L;

    private String clientIP = null;

    private NovaClientEnvironment clientEnv = null;

    private String serviceName = null; //

    private String methodName = null; //

    private Class[] parClasses = null;

    private Object[] parObjs = null;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class[] getParClasses() {
        return parClasses;
    }

    public void setParClasses(Class[] parClasses) {
        this.parClasses = parClasses;
    }

    public Object[] getParObjs() {
        return parObjs;
    }

    public void setParObjs(Object[] parObjs) {
        this.parObjs = parObjs;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getClientIP() {
        return clientIP;
    }

    public void setClientIP(String clientIP) {
        this.clientIP = clientIP;
    }

    public NovaClientEnvironment getClientEnv() {
        return clientEnv;
    }

    public void setClientEnv(NovaClientEnvironment clientEnv) {
        this.clientEnv = clientEnv;
    }

}

/**************************************************************************
 * $RCSfile: NovaRemoteCallParVO.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:23 $
 *
 * $Log: NovaRemoteCallParVO.java,v $
 * Revision 1.2  2007/05/31 07:38:23  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 05:56:47  qilin
 * no message
 *
 * Revision 1.3  2007/03/02 01:44:02  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/02/10 07:20:05  shxch
 * *** empty log message ***
 *
 * Revision 1.1  2007/01/31 10:23:09  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 03:42:06  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
