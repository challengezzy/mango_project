/**************************************************************************
 * $RCSfile: NovaRemoteCallHandler.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:23 $
 **************************************************************************/
package smartx.framework.common.ui;

import java.lang.reflect.*;

import smartx.framework.common.vo.*;


public class NovaRemoteCallHandler implements InvocationHandler {

    private String serviceName = null;
    private String remoteUrl = null;

    public NovaRemoteCallHandler(String _serviceName) {
        this.serviceName = _serviceName;
    }
    
    public NovaRemoteCallHandler(String _serviceName,String _remoteUrl) {
        this.serviceName = _serviceName;
        this.remoteUrl = _remoteUrl;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws NovaRemoteException, Throwable {
        Class[] pars_class = method.getParameterTypes(); //
        Object return_obj = new RemoteCallClient().callServlet(serviceName, method.getName(), pars_class, args,remoteUrl); //得到对象!!
        return return_obj; //
    }

}

/**************************************************************************
 * $RCSfile: NovaRemoteCallHandler.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:23 $
 *
 * $Log: NovaRemoteCallHandler.java,v $
 * Revision 1.2  2007/05/31 07:38:23  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 05:56:36  qilin
 * no message
 *
 * Revision 1.3  2007/03/21 05:52:19  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/02/10 06:20:48  shxch
 * *** empty log message ***
 *
 * Revision 1.1  2007/01/31 10:03:25  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 03:41:28  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
