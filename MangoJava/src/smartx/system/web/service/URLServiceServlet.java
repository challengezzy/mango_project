package smartx.system.web.service;


import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.jdom.*;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import smartx.framework.common.bs.NovaInitContext;
import smartx.framework.common.bs.NovaServicePoolFactory;
import smartx.framework.common.utils.Sys;
import smartx.framework.common.vo.HashVO;

/**
 * 统一资源访问接口<br/>
 * 目的：通过统一的方式URL访问定义的资源。<br/>
 *      如,http://host:port/service/WebResource/func?a=100&b=200&c=300
 *      这样的url表示调用服务器端的名称为WebResource的资源的方法func，调用参数为a=100&b=200&c=300<br/>
 * 资源：通过下面格式定义实现<br/> 
 * 		&lt;root&gt;<br/>
 * 			&lt;directcallservice&gt;true/false 
 *                       是否直接调用系统服务，true-直接调用系统服务。
 *                       如果是true，表示把nova平台的service配置直接暴露在web访问界面，换句话说就是可以直接调用后台的服务接口（满足参数的情况下）
 *                       如果是false，表示不能直接调用系统后台服务，只能调用本配置文件定义的服务类。
 *                       目前本参数还没有实际意义，主要原因是由于系统后台服务的参数比较庞杂，必须用一定的转换方式才能调用，当前还不能解析。
 *                       &lt;/directcallservice&gt;<br/>           
 *      	&lt;resource&gt;<br/>
 *      		&lt;name>资源名&lt;/name&gt;<br/>
 *          	&lt;cls>资源实现类名&lt;/cls&gt;<br/>
 *          	&lt;isservice&gt;
 *                   true/false 是否系统服务，如是则表示cls是服务名，可以通过其调用服务。
 *                   true，表示cls就是系统后台服务的名称（对应name属性），通过这个参数可以把指定的服务暴露出来。
 *              &lt;/isservice&gt;<br/>
 *      	&lt;/resource&gt;<br/>
 *      &lt;/root&gt;<br/>  
 * 返回：结果格式定义<br/>
 *      &lt;root&gt;<br/> 
 *          &lt;success&gt;执行成功返回提示，如果执行仅仅返回单值则自动填到此处&lt;/success&gt;<br/>
 *          &lt;failure&gt;执行失败提示&lt;/failure&gt;<br/>
 *          &lt;root&gt;记录行数&lt;/root&gt;<br/>
 *          &lt;row&gt;记录内容，字段名作为标签名，字段值在标签内&lt;/row&gt;<br/>
 *      &lt;/root&gt;<br/>
 * @author James.W
 *
 */
public class URLServiceServlet extends HttpServlet {

	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doPost(request, response);
	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//doDebug(request,response);
		
		//处理事务 //TODO 如果本类在jdk5的环境下发布，则context调用方式有所不同
		NovaInitContext context=new NovaInitContext();
		
		//获得服务名：/service/ServiceName/add?x=000&y=100		
		String PATH_INFO=request.getPathInfo();//PATH_INFO:/ServiceName/add
		//QUERY_STRING的获得只在get方法时候成功，如果使用post则不能成功，因此不建议使用这种方式获得参数
		//String QUERY_STRING=request.getQueryString();//QUERY_STRING:x=000&y=100	
		
		
		//0、根据需要加入访问许可判断，如果非法访问则直接返回非法提示信息
		//1、根据名称PATH_INFO检索服务配置，获得服务的实现类，
		//   两段式：a-服务名，b-方法
		//2、实例化实现类（也可以通过池化处理），得到服务的实现类
		//3、解析参数，得到参数的HashMap(key,value)
		//4、调用方法并附加参数，获得返回值HashMap
		//   包含两个参数：ReturnType，ReturnValue
		//   全部包装为xml
		//5、通过上下文判断事务处理，提交事务
		
	    String reqrs=PATH_INFO;
		if(reqrs==null||reqrs.equals("")){//没有资源请求			
			exportResult(PATH_INFO,new Exception("请求没有指定资源！"),request,response,0L);
			return;
		}
		//PATH_INFO:/ServiceName/add
		String[] ress=reqrs.split("/");
		if(ress.length!=3){
			exportResult(PATH_INFO,new Exception("请求资源的格式错误！"),request,response,0L);
			return;
		}
		
		
		
		try{
			long l_start=System.currentTimeMillis();
			Object obj=getServiceObj(ress[1]);
			Object rt=execMethod(obj, ress[2], request);
			long l_end=System.currentTimeMillis();
			
			exportResult(PATH_INFO,rt,request,response,l_end-l_start);
			
			//如果返回值类型是异常，则回滚事务
			if(rt instanceof Exception){
				context.rollbackAll();
			}else{
				context.commitAll();
			}
			
		}catch(Exception e){
			//e.printStackTrace();
			try{context.rollbackAll();}catch(Exception ex){}
		}finally{
			context.release();
		}
	}
	
	
	private void exportResult(String q,Object obj,HttpServletRequest req, HttpServletResponse resp,long cost){
		//判断返回值类型
		String responsetype=req.getParameter("_response_type");
		responsetype=(responsetype==null)?"xml":responsetype.trim().toLowerCase();
		if(responsetype.equals("json")){
			exportResultJson(q,obj, resp,cost);
		}else{
			exportResultXml(q,obj, resp,cost);
		}
	}
	
	//输出结果对象，cost业务执行时间
	private void exportResultXml(String q,Object obj,HttpServletResponse resp,long cost){
		try{
			if(obj instanceof Exception){
				exportDocumentXml(exportExceptionXml(q,(Exception)obj,cost),resp);
			}else if(obj instanceof ArrayList){
				exportDocumentXml(exportArrayListXml(q,(ArrayList)obj,cost),resp);
			}else if(obj instanceof HashVO[]){
				exportDocumentXml(exportHashVOsXml(q,(HashVO[])obj,cost),resp);
			}else if(obj instanceof PagingVO){
				exportDocumentXml(exportPagingVOXml(q,(PagingVO)obj,cost),resp);
			}else if(obj instanceof String){
				exportDocumentXml(exportStringXml(q,(String)obj,cost),resp);
			}else if(obj instanceof Document){//直接返回xml
				exportDocumentXml((Document)obj,resp);
			}else{
				exportDocumentXml(exportExceptionXml(q,new Exception("不能处理的返回值类型。"),cost),resp);
			}	
		}catch(Exception e){
			//e.printStackTrace();			
		}
	}
	//输出结果对象，cost业务执行时间
	private void exportResultJson(String q,Object obj,HttpServletResponse resp,long cost){
		try{
			if(obj instanceof Exception){
				exportDocumentJson(exportExceptionJson(q,(Exception)obj,cost),resp);
			}else if(obj instanceof ArrayList){
				exportDocumentJson(exportArrayListJson(q,(ArrayList)obj,cost),resp);
			}else if(obj instanceof HashVO[]){
				exportDocumentJson(exportHashVOsJson(q,(HashVO[])obj,cost),resp);
			}else if(obj instanceof PagingVO){
				exportDocumentJson(exportPagingVOJson(q,(PagingVO)obj,cost),resp);
			}else if(obj instanceof TreeVO){
				exportDocumentJson(exportTreeVOJson(q,(TreeVO)obj,cost),resp);
			}else if(obj instanceof String){
				exportDocumentJson(exportStringJson(q,(String)obj,cost),resp);
			}else{
				exportDocumentJson(exportExceptionJson(q,new Exception("不能处理的返回值类型。"),cost),resp);
			}	
		}catch(Exception e){
			//e.printStackTrace();			
		}
	}
	private static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	private static DecimalFormat idf=new DecimalFormat("#,##0");
	private static DecimalFormat fdf=new DecimalFormat("#,##0.00");
	private Document exportHashVOsXml(String q,HashVO[] vos,long cost)throws Exception{
		vos=(vos==null)?(new HashVO[0]):vos;
		Element root=new Element("root");
		Element eq=new Element("queryfunc");
		eq.addContent(new CDATA(q));
		root.addContent(eq);
		Element ecost=new Element("execcost");
		ecost.addContent(String.valueOf(cost));
		root.addContent(ecost);
		Element esuccess=new Element("success");
		esuccess.addContent("true");
		root.addContent(esuccess);
		Element esuccessMsg=new Element("successMsg");
		esuccessMsg.addContent(new CDATA("执行成功！"));
		root.addContent(esuccessMsg);
		Element erowcount=new Element("rowcount");
		erowcount.addContent(String.valueOf(vos.length));
		root.addContent(erowcount);
		
		for(int i=0;i<vos.length;i++){
			Element erow=new Element("row");
			String[] keys=vos[i].getKeys();
			for(int j=0;j<keys.length;j++){
				Element ecol=new Element(keys[j]);
				Object obj=vos[i].getObjectValue(keys[j]);
				if(obj instanceof Date){					
					ecol.addContent(sdf.format((Date)obj));
				}else if(obj instanceof Long||obj instanceof Integer) {
					ecol.addContent(idf.format(Long.valueOf(obj.toString())));
				}
				/*else if(obj instanceof Double) {
					ecol.addContent(fdf.format((Double)obj));
				}else if(obj instanceof Float) {
					ecol.addContent(fdf.format((Float)obj));
				}else if(obj instanceof BigDecimal) {
					ecol.addContent(fdf.format((BigDecimal)obj));
				}*/
				else{
					ecol.addContent(new CDATA(obj==null?"":obj.toString()));					
				}				
				erow.addContent(ecol);				
			}
			root.addContent(erow);				
		}
		return new Document(root);
	}
	private String exportHashVOsJson(String q,HashVO[] vos,long cost)throws Exception{
		vos=(vos==null)?(new HashVO[0]):vos;
		StringBuffer sbf=new StringBuffer();
		sbf.append("{")
		   .append("queryfunc:'"+q+"',")
		   .append("execcost:"+cost+",")
		   .append("success:true,")
		   .append("successMsg:'执行成功！',")		
		   .append("rowcount:"+vos.length+",");
		sbf.append("rows:[");
		for(int i=0;i<vos.length;i++){
			if(i>0)sbf.append(",");
			sbf.append("{");
			String[] keys=vos[i].getKeys();
			for(int j=0;j<keys.length;j++){
				if(j>0)sbf.append(",");
				Object obj=vos[i].getObjectValue(keys[j]);
				String tmp=null;
				if(obj instanceof Date){					
					tmp=sdf.format((Date)obj);
				}else if(obj instanceof Long||obj instanceof Integer) {
					tmp=idf.format(Long.valueOf(obj.toString()));
				}else if(obj instanceof Double) {
					tmp=fdf.format((Double)obj);
				}else if(obj instanceof Float) {
					tmp=fdf.format((Float)obj);
				}else{
					tmp=obj.toString();
				}			
				sbf.append(keys[j]+":'"+tmp+"'");				
			}			
			sbf.append("}");
		}
		sbf.append("]");
		sbf.append("}");
		
		return sbf.toString();
	}
	private String exportTreeVOJson(String q,TreeVO tree,long cost)throws Exception{
		HashVO[] vos=tree.getVos();
		vos=(vos==null)?(new HashVO[0]):vos;
		StringBuffer sbf=new StringBuffer();
		sbf.append("[");
		for(int i=0;i<vos.length;i++){
			if(i>0)sbf.append(",");
			sbf.append("{");
			String[] keys=vos[i].getKeys();
			for(int j=0;j<keys.length;j++){
				if(j>0)sbf.append(",");
				Object obj=vos[i].getObjectValue(keys[j]);
				String tmp=null;
				if(obj instanceof Date){					
					tmp=sdf.format((Date)obj);
				}else if(obj instanceof Long||obj instanceof Integer) {
					tmp=idf.format(Long.valueOf(obj.toString()));
				}else if(obj instanceof Double) {
					tmp=fdf.format((Double)obj);
				}else if(obj instanceof Float) {
					tmp=fdf.format((Float)obj);
				}else{
					tmp=obj.toString();
				}			
				
				if(keys[j].equals("leaf")){					
					sbf.append(keys[j]+":"+String.valueOf((tmp.equals("1")))+"");
				}else{
					sbf.append(keys[j]+":'"+tmp+"'");
				}
				
				
			}			
			sbf.append("}");
		}
		sbf.append("]");
		
		return sbf.toString();
	}
	private Document exportPagingVOXml(String q,PagingVO pvo,long cost)throws Exception{
		pvo=(pvo==null)?(new PagingVO()):pvo;
		Element root=new Element("root");
		Element eq=new Element("queryfunc");
		eq.addContent(new CDATA(q));
		root.addContent(eq);
		Element ecost=new Element("execcost");
		ecost.addContent(String.valueOf(cost));
		root.addContent(ecost);
		Element esuccess=new Element("success");
		esuccess.addContent("true");
		root.addContent(esuccess);
		Element esuccessMsg=new Element("successMsg");
		esuccessMsg.addContent(new CDATA("执行成功！"));
		root.addContent(esuccessMsg);
		Element erowcount=new Element("rowcount");
		erowcount.addContent(String.valueOf(pvo.getRowcount()));
		root.addContent(erowcount);
		
		HashVO[] vos=pvo.getVos();
		vos=(vos==null)?(new HashVO[0]):vos;
		for(int i=0;i<vos.length;i++){
			Element erow=new Element("row");
			String[] keys=vos[i].getKeys();
			for(int j=0;j<keys.length;j++){
				Element ecol=new Element(keys[j]);
				Object obj=vos[i].getObjectValue(keys[j]);
				if(obj instanceof Date){					
					ecol.addContent(sdf.format((Date)obj));
				}else if(obj instanceof Long||obj instanceof Integer) {
					ecol.addContent(idf.format(Long.valueOf(obj.toString())));
				}
				/*else if(obj instanceof Double) {
					ecol.addContent(fdf.format((Double)obj));
				}else if(obj instanceof Float) {
					ecol.addContent(fdf.format((Float)obj));
				}else if(obj instanceof BigDecimal) {
					ecol.addContent(fdf.format((BigDecimal)obj));
				}*/
				else{
					ecol.addContent(new CDATA(obj==null?"":obj.toString()));					
				}				
				erow.addContent(ecol);				
			}
			root.addContent(erow);				
		}
		return new Document(root);
	}
	private String exportPagingVOJson(String q,PagingVO pvo,long cost)throws Exception{
		pvo=(pvo==null)?(new PagingVO()):pvo;
		StringBuffer sbf=new StringBuffer();
		sbf.append("{")
		   .append("queryfunc:'"+q+"',")
		   .append("execcost:"+cost+",")
		   .append("success:true,")
		   .append("successMsg:'执行成功！',")		
		   .append("rowcount:"+pvo.getRowcount()+",");
		sbf.append("rows:[");
		HashVO[] vos=pvo.getVos();
		vos=(vos==null)?(new HashVO[0]):vos;
		for(int i=0;i<vos.length;i++){
			if(i>0)sbf.append(",");
			sbf.append("{");
			String[] keys=vos[i].getKeys();
			for(int j=0;j<keys.length;j++){
				if(j>0)sbf.append(",");
				Object obj=vos[i].getObjectValue(keys[j]);
				String tmp=null;
				if(obj instanceof Date){					
					tmp=sdf.format((Date)obj);
				}else if(obj instanceof Long||obj instanceof Integer) {
					tmp=idf.format(Long.valueOf(obj.toString()));
				}else if(obj instanceof Double) {
					tmp=fdf.format((Double)obj);
				}else if(obj instanceof Float) {
					tmp=fdf.format((Float)obj);
				}else{
					tmp=obj.toString();
				}			
				sbf.append(keys[j]+":'"+tmp+"'");				
			}			
			sbf.append("}");
		}
		sbf.append("]");
		sbf.append("}");
		
		return sbf.toString();
	}
	private Document exportArrayListXml(String q,ArrayList ary,long cost)throws Exception{
		ary=(ary==null)?(new ArrayList()):ary;
		Element root=new Element("root");
		Element eq=new Element("queryfunc");
		eq.addContent(new CDATA(q));
		root.addContent(eq);
		Element ecost=new Element("execcost");
		ecost.addContent(String.valueOf(cost));
		root.addContent(ecost);
		Element esuccess=new Element("success");
		esuccess.addContent("true");
		root.addContent(esuccess);
		Element esuccessMsg=new Element("successMsg");
		esuccessMsg.addContent(new CDATA("执行成功！"));
		root.addContent(esuccessMsg);
		Element erowcount=new Element("rowcount");
		erowcount.addContent(String.valueOf(ary.size()));
		root.addContent(erowcount);
		for(int i=0;i<ary.size();i++){
			Element erow=new Element("row");
			HashMap map=(HashMap)ary.get(i);
			String[] keys=(String[])map.keySet().toArray(new String[0]);
			for(int j=0;j<keys.length;j++){
				Element ecol=new Element(keys[j]);
				Object obj=map.get(keys[j]);
				ecol.addContent(obj.toString());
				erow.addContent(ecol);				
			}
			root.addContent(erow);	
		}
		return new Document(root);
	}
	private String exportArrayListJson(String q,ArrayList ary,long cost)throws Exception{
		ary=(ary==null)?(new ArrayList()):ary;
		StringBuffer sbf=new StringBuffer();
		sbf.append("{")
		   .append("queryfunc:'"+q+"',")
		   .append("execcost:"+cost+",")
		   .append("success:true,")
		   .append("successMsg:'执行成功！',")		
		   .append("rowcount:"+ary.size()+",");
		sbf.append("rows:[");
		for(int i=0;i<ary.size();i++){
			if(i>0)sbf.append(",");
			sbf.append("{");
			HashMap map=(HashMap)ary.get(i);
			String[] keys=(String[])map.keySet().toArray(new String[0]);
			for(int j=0;j<keys.length;j++){
				if(j>0)sbf.append(",");
				sbf.append(keys[j]+":'"+String.valueOf(map.get(keys[j]))+"'");							
			}			
			sbf.append("}");
		}
		sbf.append("],");
		sbf.append("}");
		
		return sbf.toString();
	}
	
	private Document exportStringXml(String q,String str,long cost)throws Exception{
		Element root=new Element("root");
		Element eq=new Element("queryfunc");
		eq.addContent(new CDATA(q));
		root.addContent(eq);
		Element ecost=new Element("execcost");
		ecost.addContent(String.valueOf(cost));
		root.addContent(ecost);
		Element esuccess=new Element("success");
		esuccess.addContent("true");
		root.addContent(esuccess);
		Element esuccessMsg=new Element("successMsg");
		esuccessMsg.addContent(new CDATA(str));
		root.addContent(esuccessMsg);
		return new Document(root);
	}	
	private String exportStringJson(String q,String str,long cost)throws Exception{
		StringBuffer sbf=new StringBuffer();
		sbf.append("{")
		   .append("queryfunc:'"+q+"',")
		   .append("execcost:"+cost+",")
		   .append("success:true,")
		   .append("successMsg:'"+str+"'")		
		   .append("}");
		
		return sbf.toString();
	}	
	private Document exportExceptionXml(String q,Exception obj,long cost)throws Exception{
		Element root=new Element("root");
		Element eq=new Element("queryfunc");
		eq.addContent(new CDATA(q));
		root.addContent(eq);
		Element ecost=new Element("execcost");
		ecost.addContent(String.valueOf(cost));
		root.addContent(ecost);
		Element efailure=new Element("failure");
		efailure.addContent("true");
		root.addContent(efailure);
		Element efailureMsg=new Element("failureMsg");
		efailureMsg.addContent(new CDATA(obj.getMessage()));
		root.addContent(efailureMsg);
		return new Document(root);
	}
	private String exportExceptionJson(String q,Exception obj,long cost)throws Exception{
		StringBuffer sbf=new StringBuffer();
		String msg=obj.getMessage();
		msg=StringCoder.escape(msg);
		sbf.append("{")
		   .append("queryfunc:'"+q+"',")
		   .append("execcost:"+cost+",")
		   .append("failure:true,")
		   .append("failureMsg:'"+msg+"'")		
		   .append("}");
		
		return sbf.toString();
	}
	private void exportDocumentXml(Document doc,HttpServletResponse resp)throws Exception{
		
		resp.setContentType("text/xml; charset=UTF-8");
    	PrintWriter out=resp.getWriter();
    	Format f = Format.getPrettyFormat();
        f.setEncoding("UTF-8");//default=UTF-8 
        XMLOutputter outputter = new XMLOutputter();
        outputter.setFormat(f);
        outputter.output(doc, out);
    	out.flush();
    	out.close();
		
	}
	
	private void exportDocumentJson(String str,HttpServletResponse resp)throws Exception{
		resp.setContentType("text/html; charset=UTF-8");
    	PrintWriter out=resp.getWriter();
    	out.print(str);
    	out.flush();
    	out.close();
		
	}
	
	
	
	//返回值应该是Method，但遇到异常的时候可以直接返回异常
	private Object execMethod(Object obj,String func,HttpServletRequest req){
		if(obj instanceof Exception){
			return obj;
		}
		try{
			Method method = obj.getClass().getMethod(func, new Class[]{HashMap.class});
			HashMap map=parseQuery(req);
			Object rt=method.invoke(obj, new Object[]{map});
			return rt;
		}catch(Exception e){
			
			//e.printStackTrace();
			return new Exception("非法的资源调用，原因是资源方法【"+func+"】不存在。");
		}	
	}
	//获得上传参数列表，参数值都是String数组类型。
	private HashMap parseQuery(HttpServletRequest req){
		HashMap rt=new HashMap();
		String[] qs=(String[])(req.getParameterMap().keySet().toArray(new String[0]));
		for(int i=0;i<qs.length;i++){
			String[] qsub=qs[i].split("\\=");
			String[] vs=req.getParameterValues(qsub[0]);
			for(int x=0;x<vs.length;x++){
				vs[x]=StringCoder.unescape(vs[x]);
			}			
			rt.put(qsub[0], vs);//要求检索参数必须都用escape处理过			
		}
		//最后把request也作为参数放入参数列表，参数名为"@@request"
		rt.put("@@request", req);
		return rt;
	}
	
	
	//获得url内指定资源的服务实现
	private Object getServiceObj(String svr){
		//TODO 需要在系统全局规划中给出在Sys类中的系统缓冲规划    SYS_WEB_RESOURCE_MAP：系统WEB资源服务注册信息：HashMap
		//1、首先获得系统WEB资源服务注册信息，如果为空，则初始化服务信息	
		HashMap wsmap=(HashMap)Sys.getInfo("SYS_SERVICE_FOR_WEB_MAP");
		if(wsmap==null){
			wsmap=initService();
			Sys.putInfo("SYS_WEB_RESOURCE_MAP", wsmap);
		}
		if(wsmap.containsKey(svr)){
			WebResourceRegInfo reg=(WebResourceRegInfo)wsmap.get(svr);
			String cls=reg.cls;
			
			GenericObjectPool pool = null;
			Object instanceObj = null; //实例
			if (reg.isService) {//是否用池管理
				try {
					pool = NovaServicePoolFactory.getInstance().getPool(cls); //取得池
					instanceObj = pool.borrowObject(); //从池中得到实例!!!!					
				} catch (java.lang.ClassNotFoundException ex) {
					//ex.printStackTrace();
					instanceObj = new Exception("从池中抽取服务实例【" + cls + "】失败,原因:找不到该类!"); //
				} catch (Exception ex) {
					//ex.printStackTrace();
					instanceObj = new Exception("从池中抽取服务实例【" + cls + "】失败,原因:" + ex.getMessage()); //
				}
			} else {
				try {
					instanceObj = Class.forName(cls).newInstance(); //这里都是直接创建实例的,效率较低,以后可以考虑用Spring加载..
					//System.out.println("成功直接创建服务实例[" + str_implClassName + "]"); //
				} catch (Exception ex) {
					//ex.printStackTrace();
					instanceObj = new Exception("直接创建资源实例【"+svr+":" + cls + "】失败,原因:" + ex.getMessage()); //
				}
			}
			return instanceObj;
		}
		return new Exception("指定资源【" + svr + "】没有定义！");
	}
	//获得WEB资源服务配置信息
	private HashMap initService(){
		String webrs=(String)Sys.getInfo("SYS_WEB_RESOURCE");
		if(webrs==null){
			return new HashMap();
		}
		if(webrs.startsWith("/")||webrs.substring(1, 2).equals(":/")||webrs.substring(1, 2).equals(":\\")){
			//配置文件已经带了绝对路径，所以不需要再组合文件路径了			
		}else{
			String rootpath=(String)Sys.getInfo("NOVA2_SYS_ROOTPATH")+"WEB-INF/";
			webrs=rootpath+webrs;
		}
		
		HashMap rt=new HashMap();
		Document doc=Sys.loadXML(webrs);
		if(doc==null){
			return rt;
		}
		//解析资源服务配置参数
		Element ecallservice=doc.getRootElement().getChild("directcallservice");
		if(ecallservice==null){
			rt.put("directcallservice", "false");
		}else{
			String scallservice=ecallservice.getTextTrim().toLowerCase();
			rt.put("directcallservice", String.valueOf(scallservice.equals("true")));
		}		
		
		//解析资源服务配置
		List rss=doc.getRootElement().getChildren("resource");
		for(int i=0;i<rss.size();i++){
			Element e=(Element)rss.get(i);
			String rsname=e.getChildTextTrim("name");
			String clsname=e.getChildTextTrim("cls");
			Element sube=e.getChild("isservice");
			String isservice=(sube==null)?"false":sube.getTextTrim().toLowerCase();//是否服务，对应为nova服务配置
			rt.put(rsname, new WebResourceRegInfo(rsname,clsname,isservice.equals("true")));
		}
		return rt;
	}
	class WebResourceRegInfo{
		public String name=null;
		public String cls=null;
		public boolean isService=false;
		public WebResourceRegInfo(String name,String cls,boolean isService){
			this.name=name;
			this.cls=cls;
			this.isService=isService;
		}
	}
	
	private void doDebug(HttpServletRequest request, HttpServletResponse response)throws IOException{
		
		String[][] variables =
	      { { "AUTH_TYPE", request.getAuthType() },
	        { "CONTENT_LENGTH", String.valueOf(request.getContentLength()) },
	        { "CONTENT_TYPE", request.getContentType() },
	        { "DOCUMENT_ROOT", (String)Sys.getInfo("NOVA2_SYS_ROOTPATH") },
	        { "PATH_INFO", request.getPathInfo() },
	        { "PATH_TRANSLATED", request.getPathTranslated() },
	        { "QUERY_STRING", request.getQueryString() },
	        { "REMOTE_ADDR", request.getRemoteAddr() },
	        { "REMOTE_HOST", request.getRemoteHost() },
	        { "REMOTE_USER", request.getRemoteUser() },
	        { "REQUEST_METHOD", request.getMethod() },
	        { "SERVLET_PATH", request.getServletPath() },
	        { "SERVER_NAME", request.getServerName() },
	        { "SERVER_PORT", String.valueOf(request.getServerPort()) },
	        { "SERVER_PROTOCOL", request.getProtocol() },
	        { "SERVER_SOFTWARE", getServletContext().getServerInfo() }
	      };

		for(int i=0,count=variables.length;i<count;i++){
		  System.out.println(variables[i][0]+"="+variables[i][1]);
		}
		
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println("<?xml version='1.0' encoding='utf-8' ?>");		
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
		out.println("<HTML xmlns='http://www.w3.org/1999/xhtml'>");
		out.println("  <HEAD>");
		out.println("      <meta http-equiv='Content-Type' content='text/html; charset=utf-8'/>");
		out.println("      <TITLE>A Servlet</TITLE>");
		out.println("  </HEAD>");
		
		out.println("  <BODY>");
		out.println("     获得服务信息：<br/>");
		for(int i=0;i<variables.length;i++){
			out.println("     "+variables[i][0]+"："+variables[i][1]+"<br/>");
		}
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
		
	}

}
