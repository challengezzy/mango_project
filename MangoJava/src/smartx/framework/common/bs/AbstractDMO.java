/**************************************************************************
 * $RCSfile: AbstractDMO.java,v $  $Revision: 1.5.2.4 $  $Date: 2009/01/09 02:59:08 $
 **************************************************************************/

package smartx.framework.common.bs;

import java.sql.*;
import java.text.SimpleDateFormat;

import smartx.framework.common.ui.*;
import smartx.framework.common.vo.NovaLogger;
import smartx.framework.metadata.bs.NovaServerEnvironment;


public abstract class AbstractDMO {
    
	//Fixed by James.W 2007.11.19  
    protected NovaInitContext initContext = null; //
    protected int DS_RCONN_TIMES=10;
    protected long DS_RCONN_DELAY=-1l;

    protected final NovaClientEnvironment getClientEnv() {
        if (initContext == null) {
            initContext = new NovaInitContext();
        }

        return initContext.getClientEnv(); //
    }

    public final NovaDBConnection getConn(String _dsname) throws SQLException {
    	try{
        	return getConnection(_dsname);
    	}catch(SQLException e){
    		e.printStackTrace();
    		NovaLogger.getLogger(this).error("获取数据库链接错误：[DS="+_dsname+";ErrorCode="+e.getErrorCode()+"]"+e.getMessage());
    		throw new SQLException("不能连接到数据库！获取数据库链接错误：[DS="+_dsname+";ErrorCode="+e.getErrorCode()+"]"+e.getMessage());
    	}catch(Exception err){
    		NovaLogger.getLogger(this).error("发生不可控制异常！",err);
			throw new SQLException("发生不可控制异常！请查看日志。发生时间为【"+(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(new Date(System.currentTimeMillis()))+"】");
		}        
    }
    
    private final NovaDBConnection getConnection(String _dsname) throws SQLException {

        if (initContext == null) {
            initContext = new NovaInitContext();
        }
        return initContext.getConn(_dsname);
    }

    protected final NovaDBConnection[] getAllConns() throws SQLException {
        if (initContext == null) {
            initContext = new NovaInitContext();
        }
        return initContext.GetAllConns();
    }
}
