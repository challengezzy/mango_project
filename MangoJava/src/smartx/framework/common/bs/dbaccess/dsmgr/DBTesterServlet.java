package smartx.framework.common.bs.dbaccess.dsmgr;

import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * 压力测试小工具，
 * <servlet>
    <servlet-name>DBTesterServlet</servlet-name>
    <servlet-class>smartx.framework.common.bs.dbaccess.dsmgr.DBTesterServlet</servlet-class>
    <init-param>
        <!-- 临时表的表前缀 -->
        <param-name>testtablename</param-name>
        <param-value>T_</param-value>
    </init-param>
    <init-param>
        <!-- 测试并发线程数 -->
        <param-name>threadcount</param-name>
        <param-value>80</param-value>
    </init-param>    
    <init-param>
        <!-- 每个线程测试次数 -->
        <param-name>timecount</param-name>
        <param-value>50</param-value>
    </init-param>   
    <init-param>
        <!-- 测试数据源名称 -->
        <param-name>datasourcename</param-name>
        <param-value>datasource_default</param-value>
    </init-param> 
    <!-- 在WEB应用内的启动顺序 -->
    <load-on-startup>9999</load-on-startup>
  </servlet>
 * @author Administrator
 *
 */
public class DBTesterServlet extends HttpServlet {

	private static final long serialVersionUID = -75437291998181560L;
	private DbTestThread[] ths=null;
	private DecimalFormat df=new DecimalFormat("0000");
	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); 
		if(ths==null)return;
		for(int i=0;i<ths.length;i++){
			ths[i].close();
		}
		
		
		
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		String threadcount = this.getServletConfig().getInitParameter("threadcount"); //
		int ithreadcount = (threadcount==null||threadcount.equals(""))?100:Integer.parseInt(threadcount);
		
		String timecount = this.getServletConfig().getInitParameter("timecount"); //
		int itimecount = (timecount==null||timecount.equals(""))?10000:Integer.parseInt(timecount);
		
		String ds=this.getServletConfig().getInitParameter("datasourcename");
		
		String tbname=this.getServletConfig().getInitParameter("testtablename");
		tbname=(tbname==null||tbname.equals(""))?"T_":tbname;
		
		ths=new DbTestThread[ithreadcount];
		for(int i=0;i<ithreadcount;i++){
			ths[i]=new DbTestThread(tbname+df.format(i),itimecount,ds);
			ths[i].start();
		}
	}

}
