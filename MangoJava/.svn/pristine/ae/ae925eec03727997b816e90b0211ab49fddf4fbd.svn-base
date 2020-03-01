/**************************************************************************
 * $RCSfile: RemoteCallServlet.java,v $  $Revision: 1.13.2.4 $  $Date: 2009/10/21 06:24:43 $
 **************************************************************************/
package smartx.framework.common.bs;

import java.io.*;
import java.lang.reflect.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.commons.pool.impl.*;
import org.apache.log4j.Logger;

import smartx.framework.common.ui.*;
import smartx.framework.common.vo.*;


public class RemoteCallServlet extends HttpServlet {

	private static final long serialVersionUID = 7103285415882911348L;

	private boolean ifGetInstanceFromPool = true; //

	private Logger logger = NovaLogger.getLogger(RemoteCallServlet.class);

	public void init() throws ServletException {
		super.init();
	}

	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		super.doGet(arg0, arg1);
	}

	protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		super.doPost(arg0, arg1);
	}

	protected void service(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		super.service(arg0, arg1);
	}

	public void service(ServletRequest _request, ServletResponse _response) throws ServletException, IOException {
		dealCall(_request, _response); //新的调用方法,不需要WrapperUtil了
	}

	/**
	 * 根据参数中的class类名与实际值,反射调用实际值
	 * @param _request
	 * @param _response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void dealCall(ServletRequest _request, ServletResponse _response) throws ServletException, IOException {
		long ll_1 = System.currentTimeMillis(); //
		try {

			//读取入参..
			InputStream request_in = _request.getInputStream();
			ObjectInputStream request_in_objStream = new ObjectInputStream(request_in);

			NovaRemoteCallParVO par_vo = (NovaRemoteCallParVO) request_in_objStream.readObject();
			request_in_objStream.close();

			String clientIP1 = _request.getRemoteAddr(); //par_vo.getClientIP(); //取得Request中最后一个访问的来源IP地址
			String clientIP2 = par_vo.getClientIP(); //取得客户端IP地址

			NovaClientEnvironment clientEnv = par_vo.getClientEnv(); //得到整个客户端环境变量!!!
			String serviceName = par_vo.getServiceName(); //服务名!
			String methodName = par_vo.getMethodName(); //方法名!
			String str_implClassName = NovaServicePoolFactory.getInstance().getImplClassName(serviceName); //实现类名!!
			NovaRemoteCallreturnVO returnVO = null;

			if (str_implClassName == null) {
				throw new Exception("没有在启动配置文件[Nova2Config.xml]中注册名为[" + serviceName + "]的远程访问服务!");
			} else {
				GenericObjectPool pool = null;
				Object instanceObj = null; //实例
				if (ifGetInstanceFromPool) {//是否用池管理 //调试变量
					try {
						pool = NovaServicePoolFactory.getInstance().getPool(serviceName); //取得池
						instanceObj = pool.borrowObject(); //从池中得到实例!!!!
						//System.out.println("成功从池中抓取服务实例[" + str_implClassName + "],池中当前活动[" + pool.getNumActive() + "]"); //
					} catch (java.lang.ClassNotFoundException ex) {
						ex.printStackTrace();
						instanceObj = new Exception("从池中抽取服务实例[" + str_implClassName + "]失败,原因:找不到该类!"); //
					} catch (Exception ex) {
						ex.printStackTrace();
						instanceObj = new Exception("从池中抽取服务实例[" + str_implClassName + "]失败,原因:" + ex.getMessage()); //
					}
				} else {
					try {
						instanceObj = Class.forName(str_implClassName).newInstance(); //这里都是直接创建实例的,效率较低,以后可以考虑用Spring加载..
						//System.out.println("成功直接创建服务实例[" + str_implClassName + "]"); //
					} catch (Exception ex) {
						ex.printStackTrace();
						instanceObj = new Exception("直接创建服务实例[" + str_implClassName + "]失败,原因:" + ex.getMessage()); //
					}
				}

				if (instanceObj instanceof NovaRemoteException) { //如果创建失败,即直接返回的是一个NovaRemoteException
					returnVO = new NovaRemoteCallreturnVO(); //
					returnVO.setServiceName(serviceName); //直接的实现名
					returnVO.setServiceImplName(str_implClassName); //直接的实现名
					returnVO.setReturnObject(instanceObj); //
					returnVO.setDealtime(System.currentTimeMillis() - ll_1); //
					returnVO.setCallDBCount(0); //
				} else { //如果创建实例成功!!
					returnVO = realInvoke(clientIP1, clientIP2, ll_1, serviceName, str_implClassName, instanceObj, methodName, par_vo.getParClasses(), par_vo.getParObjs(), clientEnv); //真正调用!!,核心地带!!!!!!!!!!!!!!!
					if (ifGetInstanceFromPool) { //如果是从池中取的则还要释放!!
						pool.returnObject(instanceObj); //释放!!
					}
				}
			}

			OutputStream out = _response.getOutputStream(); //输出
			ObjectOutputStream objStream = new ObjectOutputStream(out); //输出对象流!!
			objStream.writeObject(returnVO); //输出对象!!
			objStream.flush(); //
			objStream.close(); //
		} catch (Throwable ex) {
			//ex.printStackTrace(); //
			logger.error("调用服务异常！",ex);
			NovaRemoteCallreturnVO returnVO = new NovaRemoteCallreturnVO(); //
			NovaRemoteException remoteEX = new NovaRemoteException(ex.getMessage());
			remoteEX.setServerTargetEx(ex);
			returnVO.setReturnObject(remoteEX); //
			returnVO.setDealtime(System.currentTimeMillis() - ll_1);
			returnVO.setCallDBCount(-1);
			OutputStream out = _response.getOutputStream(); //输出
			ObjectOutputStream objStream = new ObjectOutputStream(out); //输出对象流!!
			objStream.writeObject(returnVO); //输出对象!!
			objStream.flush();
			objStream.close(); //
		}
	}

	/**
	 * 真正调用!!
	 * @param _instanceObj
	 * @param _methodName
	 * @param _classes
	 * @param _parObjs
	 * @param _clientEnv
	 * @return
	 */
	private NovaRemoteCallreturnVO realInvoke(String _clientIP1, String _clientIP2, long _beginTime, String _serviceName, String _implClassName, Object _instanceObj, String _methodName, Class[] _classes, Object[] _parObjs, NovaClientEnvironment _clientEnv) {
		NovaInitContext initContext = new NovaInitContext(); //
		initContext.regisClientEnv(_clientEnv); //注册整个客户端环境
		try {
			Method method = _instanceObj.getClass().getMethod(_methodName, _classes);
			Object obj = method.invoke(_instanceObj, _parObjs); //调用对应方法返回值!!!
			int li_allStmtCount = commitTrans(initContext); //提交该次会话的所有事务
			long ll_2 = System.currentTimeMillis(); //

			NovaRemoteCallreturnVO returnVO = new NovaRemoteCallreturnVO();
			returnVO.setServiceName(_serviceName); //服务接口名!
			returnVO.setServiceImplName(_implClassName); //真正的实现类名!
			returnVO.setReturnObject(obj); //
			returnVO.setDealtime(ll_2 - _beginTime); //
			returnVO.setCallDBCount(li_allStmtCount); //访问DB次数!

			logger.debug("[" + _clientIP1 + "][" + _clientIP2 + "][" + (String)_clientEnv.get("SYS_LOGINUSER_CODE") + "]调用服务[" + _implClassName + "][" + _methodName + "],耗时[" + (ll_2 - _beginTime) + "],访问DB[" + li_allStmtCount + "]次,(" + Thread.currentThread().getName() + ")");
			return returnVO; //
		} catch (InvocationTargetException ex) { //如果是反射调用异常
			int li_allStmtCount = rollbackTrans(initContext); //回滚该次会话的所有事务!必须保证
			//ex.getTargetException().printStackTrace(); //这里要包装远程异常
			logger.error("调用服务异常！",ex);
			Throwable targetex = ex.getTargetException();
			String str_message = targetex.getMessage(); //
            
			//处理调用堆栈
			String str_stackdetail = "";
			StackTraceElement[] stackItems = targetex.getStackTrace();
			for (int i = 0; i < stackItems.length; i++) {
				str_stackdetail = str_stackdetail + stackItems[i] + "\r\n"; //把实际堆栈搞进来!
			}

			NovaRemoteException novaEx = new NovaRemoteException(str_message); //
			novaEx.setServerTargetEx(targetex); //

			long ll_2 = System.currentTimeMillis(); //

			NovaRemoteCallreturnVO returnVO = new NovaRemoteCallreturnVO();
			returnVO.setServiceName(_serviceName); //服务接口名!
			returnVO.setServiceImplName(_implClassName); //真正的实现类名!
			returnVO.setReturnObject(novaEx); //
			returnVO.setDealtime(ll_2 - _beginTime); //耗时
			returnVO.setCallDBCount(li_allStmtCount); //访问DB次数!
			return returnVO;
		} catch (Throwable ex) {
			int li_allStmtCount = rollbackTrans(initContext); //回滚该次会话的所有事务
			//ex.printStackTrace();
			logger.error("调用服务异常！",ex);
			NovaRemoteException novaEx = new NovaRemoteException(ex.getMessage()); //
			novaEx.setServerTargetEx(ex); //

			long ll_2 = System.currentTimeMillis(); //
			NovaRemoteCallreturnVO returnVO = new NovaRemoteCallreturnVO();
			returnVO.setServiceName(_serviceName); //服务接口名!
			returnVO.setServiceImplName(_implClassName); //真正的实现类名!
			returnVO.setReturnObject(novaEx); //
			returnVO.setDealtime(ll_2 - _beginTime); //耗时
			returnVO.setCallDBCount(li_allStmtCount); //访问DB次数!
			return returnVO;
		} finally { //关闭连接释放资源!!
			closeConn(initContext); //关闭该次会话的所有连接!!!非常重要一定要执行!! 必须保证!!
			releaseContext(initContext); //
		}
	}

	/**
	 * 提交所有事务
	 * @param _initContext
	 */
	private int commitTrans(NovaInitContext _initContext) {
		//System.out.println("提交该次远程访问所有事务!"); //
		int li_allStmtCount = 0;
		if (_initContext.isGetConn()) {
			NovaDBConnection[] conns = _initContext.GetAllConns();
			for (int i = 0; i < conns.length; i++) {
				try {
					li_allStmtCount = li_allStmtCount + conns[i].getOpenStmtCount(); //
					conns[i].transCommit();
				} catch (Throwable e) {
					//e.printStackTrace();
					NovaLogger.getLogger(this).error(e.getMessage());
				}
			}
		}
		return li_allStmtCount;
	}

	private int rollbackTrans(NovaInitContext _initContext) {
		//System.out.println("回滚该次远程访问所有事务!"); //
		int li_allStmtCount = 0;
		if (_initContext.isGetConn()) {
			NovaDBConnection[] conns = _initContext.GetAllConns();
			for (int i = 0; i < conns.length; i++) {
				try {
					li_allStmtCount = li_allStmtCount + conns[i].getOpenStmtCount(); //
					conns[i].transRollback();
				} catch (Throwable e) {
					//e.printStackTrace();
					NovaLogger.getLogger(this).error(e.getMessage());
				}
			}
		}
		return li_allStmtCount;
	}

	/**
	 * 关闭数据库连接
	 * @param _initContext
	 */
	private void closeConn(NovaInitContext _initContext) {
		//System.out.println("关闭该次远程访问所有事务!"); //
		if (_initContext.isGetConn()) {
			NovaDBConnection[] conns = _initContext.GetAllConns();
			for (int i = 0; i < conns.length; i++) {
				try {
					conns[i].close(); //关闭指定数据源连接
					//System.out.println("关闭当前远程访问用到的数据库连接[" + conns[i].getDsName() + "]");
				} catch (Throwable e) {
					//e.printStackTrace();
					NovaLogger.getLogger(this).error(e.getMessage());
				}
			}
		}
	}

	private void releaseContext(NovaInitContext _initContext) {
		//System.out.println("释放该次远程访问所有资源!"); //
		try {
			_initContext.release(); //释放所有资源!!
		} catch (Throwable ex) {
			//ex.printStackTrace();
			NovaLogger.getLogger(this).error(ex.getMessage());
		}

	}

}
/**************************************************************************
 * $RCSfile: RemoteCallServlet.java,v $  $Revision: 1.13.2.4 $  $Date: 2009/10/21 06:24:43 $
 *
 * $Log: RemoteCallServlet.java,v $
 * Revision 1.13.2.4  2009/10/21 06:24:43  wangqi
 * *** empty log message ***
 *
 * Revision 1.13.2.3  2008/12/16 06:29:10  wangqi
 * *** empty log message ***
 *
 * Revision 1.13.2.2  2008/08/11 04:39:52  wangqi
 * *** empty log message ***
 *
 * Revision 1.13.2.1  2008/02/27 02:19:39  wangqi
 * *** empty log message ***
 *
 * Revision 1.13  2007/11/19 11:19:47  wangqi
 * *** empty log message ***
 *
 * Revision 1.12  2007/11/16 03:18:21  wangqi
 * *** empty log message ***
 *
 * Revision 1.11  2007/11/14 06:06:19  wangqi
 * *** empty log message ***
 *
 * Revision 1.10  2007/11/14 05:24:41  wangqi
 * 取消同步限制
 *
 * Revision 1.9  2007/07/30 03:05:13  shxch
 * *** empty log message ***
 *
 * Revision 1.8  2007/07/18 06:33:54  shxch
 * 将logger使用类变量来实现,这样效率会高一点
 *
 * Revision 1.7  2007/07/18 06:32:27  shxch
 * *** empty log message ***
 *
 * Revision 1.6  2007/07/16 05:58:59  sunxf
 * 增加服务器日志功能
 *
 * Revision 1.5  2007/07/16 01:22:32  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/07/12 03:52:19  shxch
 * 增加转调Afx JNDI服务的逻辑
 *
 * Revision 1.3  2007/06/16 02:42:48  qilin
 * no message
 *
 * Revision 1.2  2007/05/31 07:38:17  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 05:56:14  qilin
 * no message
 *
 * Revision 1.33  2007/03/22 01:56:02  shxch
 * *** empty log message ***
 *
 * Revision 1.32  2007/03/21 02:38:49  shxch
 * *** empty log message ***
 *
 * Revision 1.31  2007/03/16 03:37:33  shxch
 * *** empty log message ***
 *
 * Revision 1.30  2007/03/15 02:35:49  shxch
 * *** empty log message ***
 *
 * Revision 1.29  2007/03/15 01:37:36  shxch
 * *** empty log message ***
 *
 * Revision 1.28  2007/03/13 03:41:07  shxch
 * *** empty log message ***
 *
 * Revision 1.27  2007/03/13 03:34:06  shxch
 * *** empty log message ***
 *
 * Revision 1.26  2007/03/13 03:29:27  shxch
 * *** empty log message ***
 *
 * Revision 1.25  2007/03/07 08:32:20  shxch
 * *** empty log message ***
 *
 * Revision 1.24  2007/03/07 02:01:57  shxch
 * *** empty log message ***
 *
 * Revision 1.23  2007/03/02 01:44:30  shxch
 * *** empty log message ***
 *
 * Revision 1.22  2007/03/01 07:08:37  shxch
 * *** empty log message ***
 *
 * Revision 1.21  2007/03/01 06:44:42  shxch
 * *** empty log message ***
 *
 * Revision 1.20  2007/03/01 06:10:25  shxch
 * *** empty log message ***
 *
 * Revision 1.19  2007/02/28 09:17:59  shxch
 * *** empty log message ***
 *
 **************************************************************************/
