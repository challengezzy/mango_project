package smartx.framework.metadata.ui.localcache;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;

import smartx.framework.common.utils.EmbedDbTool;
import smartx.framework.common.utils.StringUtil;
import smartx.framework.common.vo.NovaLogger;

/**
 * 维护BillList显示元原模板的列可见情况
 * @author James.W
 *
 */
public class BillListColumnLocalSetting extends EmbedDbTool {
	private Logger logger = NovaLogger.getLogger(BillListColumnLocalSetting.class);

	public BillListColumnLocalSetting(){
		init();		
	}
	
	/**
	 * 增加元原模板的可见列
	 * @param tmpcode 元原模板编码
	 * @param cols 列名列表
	 */
	public void setShowCols(String tmpcode, String[] cols){
		if(cols==null||cols.length==0) return;
		String collist=StringUtil.joinArray2String(cols);
		String inssql="INSERT INTO BILLLIST_TEMPLETE_COLS (TMP_CODE, COL_LIST) " +
				"VALUES('"+tmpcode+"','"+collist+"')";
		try{
			execUpdate(inssql);
		}catch(Exception e){
			logger.error("增加元原模板的可见列",e);
		}
	}
	 
	/**
	 * 获得元原模板的可见列
	 * @param tmpcode
	 * @return
	 */
	public String[] getShowCols(String tmpcode){
		String selsql="SELECT COL_LIST FROM BILLLIST_TEMPLETE_COLS WHERE TMP_CODE='"+tmpcode+"'";
		
		Connection conn=null;
		Statement st=null;
		ResultSet rs=null;
		try{
			conn=getConnection();	
			st=conn.createStatement();		
			rs=st.executeQuery(selsql);
			if(rs.next()){
				String collist=rs.getString("col_list");
				return StringUtil.splitString2Array(collist);				
			}	
		}catch(Exception e){	
			logger.error("获得元原模板的可见列",e);				
		}finally{
			if(rs!=null)try{rs.close();}catch(Exception ex){}
			if(st!=null)try{st.close();}catch(Exception ex){}
			if(conn!=null)try{conn.close();}catch(Exception ex){}
		}
		return new String[0];
	}
	
	
	/**
	 * 初始化(必须执行)
	 * @throws Exception
	 */
	private void init(){
		String createsql="CREATE TABLE IF NOT EXISTS BILLLIST_TEMPLETE_COLS(" +
				"TMP_CODE VARCHAR(100) NOT NULL, COL_LIST VARCHAR(8000) NULL)";
		try{
			execUpdate(createsql);	
		}catch(Exception e){
			logger.error("初始化BillList显示设置表发生错误",e);
		}			
	}
	
	
}
