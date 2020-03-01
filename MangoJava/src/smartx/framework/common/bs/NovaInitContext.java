/**************************************************************************
 * $RCSfile: NovaInitContext.java,v $  $Revision: 1.2.8.4 $  $Date: 2009/01/16 12:25:12 $
 *
 * $Log: NovaInitContext.java,v $
 * Revision 1.2.8.4  2009/01/16 12:25:12  wangqi
 * *** empty log message ***
 *
 * Revision 1.2.8.3  2008/08/25 09:17:00  wangqi
 * *** empty log message ***
 *
 * Revision 1.2.8.2  2008/08/13 03:00:01  wangqi
 * *** empty log message ***
 *
 * Revision 1.2.8.1  2008/08/11 04:39:59  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2007/05/31 07:38:16  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 05:56:15  qilin
 * no message
 *
 * Revision 1.5  2007/03/02 01:44:30  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/03/01 06:44:42  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/03/01 06:22:49  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/03/01 06:10:26  shxch
 * *** empty log message ***
 *
 * Revision 1.1  2007/02/28 09:17:54  shxch
 * *** empty log message ***
 *
 **************************************************************************/

package smartx.framework.common.bs;

import java.sql.*;

import smartx.framework.common.ui.*;
import smartx.framework.common.vo.NovaLogger;
import smartx.framework.metadata.bs.*;


/**
 * Nova环境上下文可以处理事务等...
 * @author user
 *
 */
public class NovaInitContext {

    public NovaInitContext() {
    }

    /**
     * 取得指定数据源,并参与同一事务!
     * @param _dsname
     * @return
     * @throws SQLException
     */
    public NovaDBConnection getConn(String _dsname) throws SQLException {
        if (_dsname == null) {
            _dsname = NovaServerEnvironment.getInstance().getDefaultDataSourceName();
        }

        return NovaSessionFactory.getInstance().getConnection(Thread.currentThread(), _dsname); //取得当前线程的连接!!
    }
    
    /**
     * 注册整个客户端环境!!
     * @param _clientEnv
     */
    public synchronized void regisClientEnv(NovaClientEnvironment _clientEnv) {
        NovaSessionFactory.getInstance().regisClientEnv(Thread.currentThread(), _clientEnv);
    }

    /**
     * 得到客户端环境变量
     * @return
     */
    public NovaClientEnvironment getClientEnv() {
        return NovaSessionFactory.getInstance().getClientEnv(Thread.currentThread()); //
    }

    /**
     * 是否获得数据库链接
     * @return
     * @deprecated 直接调用commit()、rollback()和release()
     */
    public boolean isGetConn() {
        return NovaSessionFactory.getInstance().isGetConnection(Thread.currentThread());
    }

    /**
     * 得到线程所有用到的数据库链接
     * @return
     * @deprecated 直接调用commit()、rollback()和release()
     */
    public synchronized NovaDBConnection[] GetAllConns() {
        return NovaSessionFactory.getInstance().GetAllConnections(Thread.currentThread());
    }

    /**
     * 提交线程事务
     * @throws Exception
     */
    public synchronized int commit() throws Exception{
    	int rt=0;
    	NovaSessionFactory nf=NovaSessionFactory.getInstance();
    	Thread t=Thread.currentThread();
    	if(nf.isGetConnection(t)){
    		NovaDBConnection[] conns=nf.GetAllConnections(t);
    		for(int i=0;i<conns.length;i++){
    			rt+=conns[i].getOpenStmtCount();    			
    			conns[i].transCommit();    			
    		}
    	}
    	return rt;
    }
    /**
     * 提交线程事务
     * @throws Exception
     */
    public synchronized int commit(String ds) throws Exception{
    	int rt=0;
    	
    	NovaSessionFactory nf=NovaSessionFactory.getInstance();
    	Thread t=Thread.currentThread();
    	if(nf.isGetConnection(t)){
    		NovaDBConnection conns=nf.getConnection(t, ds);
    		rt+=conns.getOpenStmtCount();    			
    		conns.transCommit();
    	}
    	return rt;
    }
    /**
     * 回滚线程事务
     * @throws Exception
     */
    public synchronized int rollback() throws Exception{
    	int rt=0;
    	NovaSessionFactory nf=NovaSessionFactory.getInstance();
    	Thread t=Thread.currentThread();
    	if(nf.isGetConnection(t)){
    		NovaDBConnection[] conns=nf.GetAllConnections(t);
    		for(int i=0;i<conns.length;i++){
    			rt+=conns[i].getOpenStmtCount();    			
    			conns[i].transRollback();    			
    		}
    	}
    	return rt;
    }
    /**
     * 回滚线程事务
     * @throws Exception
     */
    public synchronized int rollback(String ds) throws Exception{
    	int rt=0;
    	NovaSessionFactory nf=NovaSessionFactory.getInstance();
    	Thread t=Thread.currentThread();
    	if(nf.isGetConnection(t)){
    		NovaDBConnection conns=nf.getConnection(t, ds);
    		rt+=conns.getOpenStmtCount();    			
    		conns.transRollback(); 
    	}
    	return rt;
    }

    /**
     * 清空线程缓冲信息
     */
    public synchronized void release() {
        try {
            NovaSessionFactory.getInstance().releaseCustConnection(Thread.currentThread()); //
        } catch (Throwable th) {
        }

        try {
            NovaSessionFactory.getInstance().releaseClientEnv(Thread.currentThread()); //
        } catch (Throwable th) {
        }
    }
    
    /**
     * 清空线程缓冲信息
     */
    public synchronized void release(String ds)throws Exception {
    	/**
    	 * 判断是否存在数据库链接，如存在则释放链接，然后释放线程所有缓冲信息。
    	 */
    	NovaSessionFactory nf=NovaSessionFactory.getInstance();
    	Thread t=Thread.currentThread();
    	if(nf.isGetConnection(t)){
    		NovaDBConnection conns=nf.getConnection(t, ds);
    		conns.close();
    	}
    	NovaSessionFactory.getInstance().releaseThreadInfo(Thread.currentThread(),ds);    	
    }
    
    public void commitAll() throws Exception {
        NovaDBConnection[] conns = GetAllConns();
        if(conns==null)return;
        for (int i = 0; i < conns.length; i++) {
            conns[i].transCommit();
        }
    }

    public void rollbackAll() throws Exception {
        NovaDBConnection[] conns = GetAllConns();
        if(conns==null)return;
        for (int i = 0; i < conns.length; i++) {
            conns[i].transRollback();
        }
    }

}
