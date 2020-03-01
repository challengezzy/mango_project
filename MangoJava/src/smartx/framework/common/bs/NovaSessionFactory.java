/**************************************************************************
 * $RCSfile: NovaSessionFactory.java,v $  $Revision: 1.2.8.2 $  $Date: 2009/01/16 12:25:13 $
 *
 * $Log: NovaSessionFactory.java,v $
 * Revision 1.2.8.2  2009/01/16 12:25:13  wangqi
 * *** empty log message ***
 *
 * Revision 1.2.8.1  2008/08/13 03:00:07  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2007/05/31 07:38:17  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 05:56:14  qilin
 * no message
 *
 * Revision 1.1  2007/03/02 01:44:30  shxch
 * *** empty log message ***
 *
 * Revision 1.5  2007/03/01 09:06:56  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/03/01 06:44:42  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/03/01 06:22:49  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/03/01 06:10:25  shxch
 * *** empty log message ***
 *
 * Revision 1.1  2007/02/28 09:17:54  shxch
 * *** empty log message ***
 *
 **************************************************************************/

package smartx.framework.common.bs;

import java.sql.*;
import java.util.*;

import smartx.framework.common.ui.*;


/**
 * 会话工厂,非常重要,是一个单例模式,在这里存储了各个线程的数据库连接与客户端变量的信息!!
 * @author user
 *
 */
public class NovaSessionFactory {

    private static NovaSessionFactory factory = new NovaSessionFactory();

    private static HashMap dbConnectionMap = new HashMap(); //指定数据源存储对象

    private static HashMap clientEnvMap = new HashMap(); //存储客户端环境变量的HashMap

    private NovaSessionFactory() {
    }

    public static NovaSessionFactory getInstance() {
        return factory;
    }

    /**
     * 取得指定数据源，并参与同一事务
     * @param thread
     * @param _dsname
     * @return
     * @throws SQLException
     */
    public synchronized NovaDBConnection getConnection(Thread _thread, String _dsname) throws SQLException {
        Object obj = dbConnectionMap.get(_thread); //指定数据源存储对象!!
        if (obj != null) { //如果找到了该线程的对象!!
            HashMap map = (HashMap) obj;
            Object objConn = map.get(_dsname);
            if (objConn != null) {
                return (NovaDBConnection) objConn; //
            } else {
                NovaDBConnection conn = new NovaDBConnection(_dsname); //创建指定数据源
                map.put(_dsname, conn); //
                return conn; //
            }
        } else {
            HashMap map = new HashMap();
            NovaDBConnection conn = new NovaDBConnection(_dsname); //创建指定数据源
            map.put(_dsname, conn); //
            dbConnectionMap.put(_thread, map); //
            return conn; //
        }
    }

    /**
     * @param _thread
     * @param _dsName
     * @return
     */
    protected synchronized boolean isGetConnection(Thread _thread) {
        return dbConnectionMap.containsKey(_thread); //
    }

    protected synchronized NovaDBConnection[] GetAllConnections(Thread _thread) {
        Object obj = dbConnectionMap.get(_thread); //指定数据源存储对象!!
        if (obj == null) {
            return null;
        } else {
            Vector vector = new Vector();
            HashMap map = (HashMap) obj; //
            return (NovaDBConnection[]) (new ArrayList(map.values()).toArray(new NovaDBConnection[0])); //
        }
    }

    /**
     * 取得当前线程的客户端环境变量!!
     * @param _thread
     * @return
     */
    public synchronized NovaClientEnvironment getClientEnv(Thread _thread) {
        Object obj = clientEnvMap.get(_thread); //指定数据源存储对象!!
        return (NovaClientEnvironment) obj; //
    }

    /**
     * 注册客户端环境变量
     * @param _thread
     */
    public synchronized void regisClientEnv(Thread _thread, NovaClientEnvironment _clientEnv) {
        clientEnvMap.put(_thread, _clientEnv); //注册当前线程的客户端环境变量!
    }

    public synchronized void releaseCustConnection(Thread _thread) {
    	Object obj = dbConnectionMap.remove(_thread); //指定数据源存储对象!!
        if (obj == null) {
            ;
        } else {
        	try{
            Vector vector = new Vector();
            HashMap map = (HashMap) obj; //
            NovaDBConnection[] conns=(NovaDBConnection[]) (new ArrayList(map.values()).toArray(new NovaDBConnection[0])); //
            for(int i=0;i<conns.length;i++){
            	if(!conns[i].isClosed()){
            		conns[i].close();
            	}
            }
        	}catch(Exception e){
        		;
        	}            
        }
    }

    public synchronized void releaseClientEnv(Thread _thread) {
        clientEnvMap.remove(_thread); //
    }
    
    /**
     * 清除线程对应的链接缓冲和客户端环境变量
     * @param _thread
     */
    public synchronized void releaseThreadInfo(Thread _thread,String ds){
    	HashMap map = (HashMap)dbConnectionMap.get(_thread); //指定数据源存储对象!!
        if (map != null) { //如果找到了该线程的对象!!
        	map.remove(ds);
	        if(map.size()==0){
	        	dbConnectionMap.remove(_thread); //
	        	clientEnvMap.remove(_thread); //
	        }
        }
    }

}
