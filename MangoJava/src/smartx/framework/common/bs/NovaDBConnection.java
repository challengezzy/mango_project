/**************************************************************************
 * $RCSfile: NovaDBConnection.java,v $  $Revision: 1.6.2.6 $  $Date: 2010/04/12 07:55:45 $
 *
 * $Log: NovaDBConnection.java,v $
 * Revision 1.6.2.6  2010/04/12 07:55:45  wangqi
 * *** empty log message ***
 *
 * Revision 1.6.2.5  2009/08/13 02:07:03  wangqi
 * *** empty log message ***
 *
 * Revision 1.6.2.4  2009/01/09 02:59:09  wangqi
 * *** empty log message ***
 *
 * Revision 1.6.2.3  2008/11/25 10:26:09  wangqi
 * *** empty log message ***
 *
 * Revision 1.6.2.2  2008/08/13 02:59:56  wangqi
 * *** empty log message ***
 *
 * Revision 1.6.2.1  2008/02/27 02:12:32  wangqi
 * *** empty log message ***
 *
 * Revision 1.6  2007/11/29 05:38:32  wangqi
 * *** empty log message ***
 *
 * Revision 1.5  2007/11/29 03:34:17  wangqi
 * *** empty log message ***
 *
 * Revision 1.4  2007/11/16 03:40:22  wangqi
 * *** empty log message ***
 *
 * Revision 1.3  2007/09/15 10:21:30  wangqi
 * 处理连接池相关的监控
 *
 * Revision 1.2  2007/05/31 07:38:16  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 05:56:15  qilin
 * no message
 *
 * Revision 1.10  2007/03/22 02:45:25  shxch
 * *** empty log message ***
 *
 * Revision 1.9  2007/03/22 01:56:02  shxch
 * *** empty log message ***
 *
 * Revision 1.8  2007/03/02 10:24:02  shxch
 * *** empty log message ***
 *
 * Revision 1.7  2007/03/02 10:23:43  shxch
 * *** empty log message ***
 *
 * Revision 1.6  2007/03/01 06:53:14  shxch
 * *** empty log message ***
 *
 * Revision 1.5  2007/03/01 06:33:37  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/03/01 06:33:21  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/03/01 06:32:39  shxch
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

import org.apache.commons.dbcp.PoolingDriver;
import org.apache.commons.pool.impl.GenericObjectPool;

import smartx.framework.common.bs.dbaccess.dsmgr.DataSourceManager;
import smartx.framework.common.utils.Sys;
import smartx.framework.common.vo.NovaLogger;
import smartx.framework.metadata.bs.*;


/**
 * @author user
 *
 */
public class NovaDBConnection {

    private String dsName = null; //数据源名称!!

    private java.sql.Connection conn = null;    
    private int openStmtCount = 0;

    /**
     * 默认数据源
     * @throws SQLException
     */
    public NovaDBConnection() throws SQLException {
        this.dsName = NovaServerEnvironment.getInstance().getDefaultDataSourceName();
        createConn();
    }

    /**
     * 指定数据源
     * @param _dsname
     * @throws SQLException
     */
    public NovaDBConnection(String _dsname) throws SQLException {
        this.dsName = _dsname;
        createConn();
    }

    private void createConn() throws SQLException { 
    	conn = DataSourceManager.getConnection(this.dsName);
    	//conn.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_COMMITTED); 
        conn.setAutoCommit(false);
    }
    

    protected void transCommit() throws SQLException {
        conn.commit();
        this.openStmtCount=0;//提交后归零
    }

    protected void transRollback() throws SQLException {
    	conn.rollback();  
    	this.openStmtCount=0;//提交后归零
    }

    protected void close() throws SQLException {
    	conn.setAutoCommit(true);
    	conn.close();    	
    }
    /**
     * 是否关闭连接
     * @throws SQLException
     */
    protected boolean isClosed() throws SQLException{
    	return conn.isClosed();
    }

    public java.sql.Connection getConn() {
        return conn;
    }

    public DatabaseMetaData getMetaData() throws SQLException {
        return conn.getMetaData();
    }

    /**
     * 创建标准Statement
     * @return
     * @throws SQLException
     */
    public java.sql.Statement createStatement() throws SQLException {
        openStmtCount = openStmtCount + 1;
        return conn.createStatement();
    }
    
    /**
     * 创建标准Statement，指定ResultSet类型和并发属性
     * @param resultSetType
     * @param resultSetConcurrency
     * @return
     * @throws SQLException
     */
    public java.sql.Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        openStmtCount = openStmtCount + 1;
        return conn.createStatement(resultSetType, resultSetConcurrency);
    }

    /**
     * 创建PrepareStatement
     * @param sql
     * @return
     * @throws SQLException
     */
    public java.sql.PreparedStatement prepareStatement(String sql) throws SQLException {
        openStmtCount = openStmtCount + 1;
        return conn.prepareStatement(sql);
    }
    
    /**
     * 创建PrepareStatement，指定ResultSet类型和并发属性
     * @param sql
     * @param resultSetType
     * @param resultSetConcurrency
     * @return
     * @throws SQLException
     */
    public java.sql.PreparedStatement prepareStatement(String sql,int resultSetType, int resultSetConcurrency) throws SQLException{
    	openStmtCount = openStmtCount + 1;
        return conn.prepareStatement(sql,resultSetType,resultSetConcurrency);
    }

    /**
     * 创建CallableStatement,为存储过程与存储函数用!!!
     * @param sql
     * @return
     * @throws SQLException
     */
    public java.sql.CallableStatement prepareCall(String sql) throws SQLException {
        openStmtCount = openStmtCount + 1;
        return conn.prepareCall(sql); //
    }

    public String getDsName() {
        return dsName;
    }

    public void setDsName(String dsName) {
        this.dsName = dsName;
    }

    //打开游标的数量!!
    public int getOpenStmtCount() {
        return openStmtCount;
    }
}
