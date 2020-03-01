package smartx.framework.metadata.lookandfeel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;

import smartx.framework.common.utils.EmbedDbTool;
import smartx.framework.common.utils.Sys;
import smartx.framework.common.vo.NovaLogger;


/**
 * 风格设置本地保存类
 * @author James.W
 *
 */
public class LookAndFeelLocalSetting extends EmbedDbTool {
	private Logger logger = NovaLogger.getLogger(LookAndFeelLocalSetting.class);
	public LookAndFeelLocalSetting(){
		init();
	}
	
	/**
	 * 获得系统定义的所有风格模板列表
	 * @return String[n][0]-名称；String[n][1]-类名
	 */
	public String[][] getSysLookAndFeels(){
		Document doc = (Document)Sys.getInfo("SYS_DESKTOP_DEFINE");    	
		
		List list = (doc==null)?(new ArrayList()):(doc.getRootElement().getChild("lookandfeels").getChildren("lookandfeel"));
		int rows = list.size();
		String[][] rts=new String[rows][2];
		int lie=-1;
		for (int i = 0; i < rows; i++) {
			Element e = (Element)list.get(i);
			rts[i][0]=e.getChild("name").getTextTrim();
			rts[i][1]= e.getChild("class").getTextTrim();			
		}
		return rts;
	}
	
	/**
	 * 增加元原模板的可见列
	 * @param tmpcode 元原模板编码
	 * @param cols 列名列表
	 */
	public void setLookAndFeel(String cls){
		if(cls==null) return;
		
		String delsql="DELETE FROM LOOKANDFEEL ";
		String inssql="INSERT INTO LOOKANDFEEL (SETTING) VALUES('"+cls+"')";
		try{
			execUpdate(new String[]{delsql, inssql});	
			((LookAndFeelIFC)Class.forName(cls).newInstance()).updateUI();
		}catch(Exception e){
			logger.error("设置当前风格",e);
		}
	}
	
	/**
	 * 获得元原模板的可见列
	 * @param tmpcode
	 * @return
	 */
	public String getLookAndFeel(){
		String selsql="SELECT SETTING FROM LOOKANDFEEL ";
		
		Connection conn=null;
		Statement st=null;
		ResultSet rs=null;
		try{
			conn=getConnection();	
			st=conn.createStatement();		
			rs=st.executeQuery(selsql);
			if(rs.next()){
				return rs.getString("SETTING");				
			}	
		}catch(Exception e){	
			logger.error("获得当前风格设置",e);				
		}finally{
			if(rs!=null)try{rs.close();}catch(Exception ex){}
			if(st!=null)try{st.close();}catch(Exception ex){}
			if(conn!=null)try{conn.close();}catch(Exception ex){}
		}
		return null;
	}
	
	/**
	 * 初始化(必须执行)
	 * @throws Exception
	 */
	private void init(){
		String createsql="CREATE TABLE IF NOT EXISTS LOOKANDFEEL(SETTING VARCHAR(1000) NOT NULL)";
		try{
			execUpdate(createsql);	
		}catch(Exception e){
			logger.error("初始化LookAndFeel显示设置表发生错误",e);
		}			
	}
	
}
