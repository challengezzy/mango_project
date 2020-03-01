package smartx.framework.common.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.sql.RowSet;
import javax.sql.rowset.CachedRowSet;

import com.sun.rowset.CachedRowSetImpl;

/**
 * 嵌入式数据库（H2DB）处理工具类
 * @author James.W
 *
 */
public class EmbedDbTool {
	
	/**
	 * 检索数据
	 * @param sql
	 * @return
	 * @throws Exception
	 */	
	protected RowSet execQuery(String sql)throws Exception{
		Connection conn=null;
		//Statement st=null;
		try{
			conn=getConnection();	
			CachedRowSet rs;
			//st=conn.createStatement();			
			//rs.populate(st.executeQuery(sql));		
			try{
				rs=new CachedRowSetImpl();
				rs.setCommand(sql);
				rs.execute(conn);
			}catch(Exception e){
				rs=new CachedRowSetImpl();
				rs.setCommand(sql);
				rs.execute(conn);
			}			
		    return rs;
		}catch(Exception e){			
			throw e;
		}finally{
			//if(st!=null)st.close();
			if(conn!=null)conn.close();
		}
	}
	
	/**
	 * 执行sql
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	protected int execUpdate(String sql)throws Exception{
		Connection conn=null;
		Statement st=null;
		try{
			conn=getConnection();
			conn.setAutoCommit(false);
			st=conn.createStatement();
		    int rt=st.executeUpdate(sql);
		    conn.commit();
		    return rt;
		}catch(Exception e){
			conn.rollback();
			throw e;
		}finally{
			conn.setAutoCommit(true);
			if(st!=null)st.close();
			if(conn!=null)conn.close();
		}
	}
	
	/**
	 * 执行sql列表
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	protected int[] execUpdate(String[] sqls)throws Exception{
		Connection conn=null;
		Statement st=null;
		try{
			conn=getConnection();
			conn.setAutoCommit(false);
			st=conn.createStatement();
			int[] rts=new int[sqls.length];
			for(int i=0;i<sqls.length;i++){
		        rts[i]=st.executeUpdate(sqls[i]);
			}
		    conn.commit();
		    return rts;
		}catch(Exception e){
			conn.rollback();
			throw e;
		}finally{
			conn.setAutoCommit(true);
			if(st!=null)st.close();
			if(conn!=null)conn.close();
		}
	}
	
	
	//TODO 如果需要使用连接池，并注意事务问题
	protected Connection getConnection()throws Exception{
		Class.forName(str_dbdriver);
	    return DriverManager.getConnection(str_dburl);		
	}
	protected String str_dbdriver = "org.h2.Driver";
	protected String str_dburl = "jdbc:h2:"+Sys.getInfo("CLIENT_DATACACHE")+"/nova";
	
}
