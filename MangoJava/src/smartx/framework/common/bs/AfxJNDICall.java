/**************************************************************************
 * $RCSfile: AfxJNDICall.java,v $  $Revision: 1.9 $  $Date: 2007/07/31 08:40:34 $
 **************************************************************************/

package smartx.framework.common.bs;


import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

import smartx.framework.common.vo.NovaRemoteCallreturnVO;
import smartx.framework.common.vo.NovaRemoteException;

/**
 * 调用Afx JNDI服务的类
 * @author xch
 *
 */
public class AfxJNDICall {

	private static Properties prop = null; //配置文件,读取/jndi.properties文件中的内容!!

	static {
		if (prop == null) { //如果配置文件为空
			InputStream in = null;
			prop = new Properties(); //创建配置文件
			try {
				in = AfxJNDICall.class.getResourceAsStream("/jndi.properties"); //
				prop.load(in);
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				try {
					in.close();
				} catch (IOException e) {
				} //
			}
		}
	}

	public NovaRemoteCallreturnVO remoteCall(String str_serviceName, String _methodName, Class[] par_classes, Object[] par_objects) {
		long ll_1 = System.currentTimeMillis();
		try {

			System.out.println("JNDI远程URL:" + prop.getProperty("java.naming.provider.url")); ////
			//prop.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory"); //这些是从配置文件取
			//prop.put("java.naming.factory.url.pkgs", "gxlu.afx.framework.naming:org.jnp.interfaces"); //这些从配置文件取
			//prop.put("java.naming.provider.url", "jnp://172.16.8.5:58298"); //

			String str_service1 = AfxJNDIServiceUtil.getInstance().getServiceName1(str_serviceName);
			String str_service2 = AfxJNDIServiceUtil.getInstance().getServiceName2(str_serviceName);

			Context init = new InitialContext(prop); //
			Context context = (Context) init.lookup(str_service1); //"system/security"
			Object obj = context.lookup(str_service2); //"security0"
			Method mtehod = obj.getClass().getMethod(_methodName, par_classes); //找到对应方法
			Object returnobj = mtehod.invoke(obj, par_objects); //调用对应方法返回值!!!
			//System.out.println("输出结果:\r\n" + returnobj); //

			NovaRemoteCallreturnVO returnVO = new NovaRemoteCallreturnVO(); //
			returnVO.setServiceName(str_serviceName); //直接的实现名
			returnVO.setServiceImplName(str_serviceName); //直接的实现名
			returnVO.setReturnObject(returnobj); //
			returnVO.setDealtime(System.currentTimeMillis() - ll_1); //
			returnVO.setCallDBCount(0); //

			return returnVO;
		} catch (InvocationTargetException ex) {
			ex.getTargetException().printStackTrace();
			NovaRemoteException novaEx = new NovaRemoteException(ex.getTargetException().getMessage()); //
			novaEx.setServerTargetEx(ex.getTargetException()); //
			NovaRemoteCallreturnVO returnVO = new NovaRemoteCallreturnVO(); //
			returnVO.setServiceName(str_serviceName); //直接的实现名
			returnVO.setServiceImplName(str_serviceName); //直接的实现名
			returnVO.setReturnObject(novaEx); //
			returnVO.setDealtime(System.currentTimeMillis() - ll_1); //
			returnVO.setCallDBCount(0); //

			return returnVO;
		} catch (Exception ex) {
			ex.printStackTrace();
			NovaRemoteException novaEx = new NovaRemoteException(ex.getMessage()); //
			novaEx.setServerTargetEx(ex); //
			NovaRemoteCallreturnVO returnVO = new NovaRemoteCallreturnVO(); //
			returnVO.setServiceName(str_serviceName); //直接的实现名
			returnVO.setServiceImplName(str_serviceName); //直接的实现名
			returnVO.setReturnObject(novaEx); //
			returnVO.setDealtime(System.currentTimeMillis() - ll_1); //
			returnVO.setCallDBCount(0); //

			return returnVO;
		}
	}

}

/*******************************************************************************
 * $RCSfile: AfxJNDICall.java,v $ $Revision: 1.9 $ $Date: 2007/07/31 08:40:34 $
 *
 * $Log: AfxJNDICall.java,v $
 * Revision 1.9  2007/07/31 08:40:34  lst
 * no message
 *
 * Revision 1.7  2007/07/30 03:05:13  shxch
 * *** empty log message ***
 *
 * Revision 1.6  2007/07/16 01:19:03  shxch
 * 增加了配置文件从/jndi.properties中读取的逻辑!!
 *
 * Revision 1.5  2007/07/12 03:42:49  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/07/12 03:41:55  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/07/12 03:40:48  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/07/12 03:40:15  shxch
 * 调用Afx JNDI的类
 *
 *
 ******************************************************************************/
