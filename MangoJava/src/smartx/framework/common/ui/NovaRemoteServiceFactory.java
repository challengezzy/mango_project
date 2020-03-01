/**************************************************************************
 * $RCSfile: NovaRemoteServiceFactory.java,v $  $Revision: 1.3 $  $Date: 2007/07/12 03:55:01 $
 **************************************************************************/
package smartx.framework.common.ui;

import java.lang.reflect.*;

import smartx.framework.common.vo.*;


public class NovaRemoteServiceFactory {

    private static NovaRemoteServiceFactory factory = null;

    private NovaRemoteServiceFactory() {

    }

    public static NovaRemoteServiceFactory getInstance() {
        if (factory == null) {
            factory = new NovaRemoteServiceFactory();

        }
        return factory;
    }
    
    /**
     * 返回一个实例!!
     * @param _class
     * @param _implname
     * @return
     * @throws Exception
     */
    public synchronized Object lookUpService(Class _class) throws NovaRemoteException, Exception {
    	return lookUpService(_class,null);
    }

    /**
     * 返回一个实例!!
     * @param _class
     * @param _implname
     * @return
     * @throws Exception
     */
    public synchronized Object lookUpService(Class _class,String remoteUrl) throws NovaRemoteException, Exception {
        String str_serviceName = _class.getName();
        Object serviceobj = Proxy.newProxyInstance(_class.getClassLoader(), new Class[] {_class},
            new NovaRemoteCallHandler(str_serviceName,remoteUrl));
        return serviceobj;
        

		//        if (serviceobj instanceof NovaRemoteCallServiceIfc) { //是否Nova服务
		//            return serviceobj;
		//        } else {
		//            throw new Exception(str_serviceName + "不是INova2RemoteCallService的子类!!");
		//        }
    }
    
    

}

/**************************************************************************
 * $RCSfile: NovaRemoteServiceFactory.java,v $  $Revision: 1.3 $  $Date: 2007/07/12 03:55:01 $
 *
 * $Log: NovaRemoteServiceFactory.java,v $
 * Revision 1.3  2007/07/12 03:55:01  shxch
 * 因为增加了调用Afx的服务,所以去掉了以前所有服务都必须继承于NovaRemoteIfc的限制
 *
 * Revision 1.2  2007/05/31 07:38:23  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 05:56:36  qilin
 * no message
 *
 * Revision 1.2  2007/03/21 05:52:19  shxch
 * *** empty log message ***
 *
 * Revision 1.1  2007/01/31 10:03:25  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/01/31 09:37:50  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 03:41:28  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
