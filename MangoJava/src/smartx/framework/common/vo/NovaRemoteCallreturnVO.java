/**************************************************************************
 * $RCSfile: NovaRemoteCallreturnVO.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:23 $
 **************************************************************************/
package smartx.framework.common.vo;

import java.io.*;

public class NovaRemoteCallreturnVO implements Serializable {

    private static final long serialVersionUID = 7116778489819827914L;

    private String serviceName = null; //

    private String serviceImplName = null; //

    private Object returnObject = null;

    private long dealtime = 0; //

    private int callDBCount = 0;

    public Object getReturnObject() {
        return returnObject;
    }

    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    public String getServiceImplName() {
        return serviceImplName;
    }

    public void setServiceImplName(String serviceImplName) {
        this.serviceImplName = serviceImplName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public long getDealtime() {
        return dealtime;
    }

    public void setDealtime(long dealtime) {
        this.dealtime = dealtime;
    }

    public int getCallDBCount() {
        return callDBCount;
    }

    public void setCallDBCount(int callDBCount) {
        this.callDBCount = callDBCount;
    }

}
/**************************************************************************
 * $RCSfile: NovaRemoteCallreturnVO.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:23 $
 *
 * $Log: NovaRemoteCallreturnVO.java,v $
 * Revision 1.2  2007/05/31 07:38:23  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 05:56:47  qilin
 * no message
 *
 * Revision 1.4  2007/03/22 01:56:03  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/02/10 07:13:05  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/02/10 06:23:44  shxch
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
