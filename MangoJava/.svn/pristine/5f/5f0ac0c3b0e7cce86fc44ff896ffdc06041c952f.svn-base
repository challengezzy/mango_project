package smartx.framework.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.ResourceBundle;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.DOMBuilder;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;


/**
 * 本类没有什么其他用途，就是管理一下版本号
 * @author James.W
 *
 */
public class Sys {
	
	public static boolean isDebug=true;

	private static final String product_name="SmartX 应用平台";
	private static final String procuct_version="1";
	private static final String procuct_subversion="0";
	private static final String procuct_littleversion="0";
	private static final String procuct_branch="main";
	private static final String procuct_build="1111";
	
	private Sys(){		
	}

	/**
	 * 产品名称
	 * @return
	 */
	public static String getProduct_name() {
		return product_name;
	}

	/**
	 * 主版本号
	 * @return
	 */
	public static String getProcuct_version() {
		return procuct_version;
	}

	/**
	 * 次版本号
	 * @return
	 */
	public static String getProcuct_subversion() {
		return procuct_subversion;
	}
	
	/**
	 * 小版本号
	 * @return
	 */
	public static String getProcuct_littleversion() {
		return procuct_littleversion;
	}
	

	/**
	 * 分支名称
	 * @return
	 */
	public static String getProcuct_branch() {
		return procuct_branch;
	}

	/**
	 * 编译版本
	 * @return
	 */
	public static String getProcuct_build() {
		return procuct_build;
	}
	
	
	
	
	/**
	 * 获得版本描述信息
	 * @return
	 */
	public static String getVersion(){
		return procuct_version+"."+procuct_subversion+"."+procuct_littleversion
		  + " branch:"+procuct_branch+" build-"+procuct_build;
	}
	
	/**
	 * 获得产品版本描述
	 * @return
	 */
	public static String getProductInfo(){
		return product_name
		  + " V" + getVersion();
	}
	
	/**
	 * 直接输出版本信息
	 */
	public static void systemOutInfo(){
		String pInfo=getProductInfo();
		System.err.println();
        System.err.println("******************************************************************");
        System.err.println("           "+pInfo);
        System.err.println("                    2010 版权所有 ");
        System.err.println("******************************************************************");
        System.err.println();
        System.err.println();
        //System.err.println("☆version ：$$");
	}
	
	/***** 多语言支持 begin *************************/
	private static ResourceBundle _res=null;
	private static Hashtable _resmap=new Hashtable();
	/**
	 * 获得系统定义资源
	 * @param key 资源key
	 * @return
	 */
	public static String getSysRes(String key){
		if(_res==null){
			try{
				_res=ResourceBundle.getBundle("conf/res");
			}catch(Exception e){
				return null;
			}
		}
		return _res.getString(key);
	}
	/**
	 * 获得指定的资源
	 * @param resname 资源文件名
	 * @param key 资源key
	 * @return
	 */
	public static String getRes(String resname, String key){
		if(!_resmap.contains(resname)){
			ResourceBundle tmp=ResourceBundle.getBundle(resname);
			if(tmp==null){return null;}
			_resmap.put(resname, tmp);
		}
		return ((ResourceBundle)_resmap.get(resname)).getString(key);				
	}	
	/***** 多语言支持 end   *************************/
	
	
	
	/*****下面是创建了系统级的缓冲，注意规模*************************/
	private static HashMap _info=new HashMap();
	private static long CACHE_DELAY=60*60*1000L;//缓冲时间(毫秒数)
	//TODO 需要一个线程，处理一下过期的对象
	/**
	 * 设置对象默认缓冲时间（秒数）
	 * @param delay 秒数
	 */
	public static void setCacheDelay(int delay){
		CACHE_DELAY=delay*1000L;
	}
	/**
	 * 缓冲对象
	 * @param key
	 * @param obj
	 */
	public static void putInfo(String key,Object obj){
		CacheObject co=new CacheObject(0L,obj);
		_info.put(key, co);
	}
	/**
	 * 缓冲对象
	 * @param key
	 * @param obj
	 * @param delay 缓冲秒数
	 */
	public static void putInfo(String key,Object obj,int delay){
		CacheObject co=new CacheObject(delay*60L,obj);
		_info.put(key, co);
	}
	/**
	 * 缓冲对象，默认缓冲时间
	 * @param key
	 * @param obj
	 * @param delay 缓冲秒数
	 */
	public static void putInfoCache(String key,Object obj){
		CacheObject co=new CacheObject(CACHE_DELAY,obj);//默认缓冲时间
		_info.put(key, co);
	}
	/**
	 * 
	 * @param key
	 * @return
	 */
	public static Object getInfo(String key){
		//如果永久缓冲对象，直接返回；
		//如果已经超时，则返回空，且清除缓冲。
		CacheObject co=(CacheObject)_info.get(key);
		if(co==null){return null;}
		if(co.getDelay()==0){
			return co.getObj();
		}
		if(co.getDelayed()>co.getDelay()){
			removeInfo(key);
			return null;
		}
		return co.getObj();		
	}
	/**
	 * 
	 * @param key
	 * @return
	 */
	public static Object removeInfo(String key){
		CacheObject co=(CacheObject)_info.remove(key);
		if(co==null){return null;}
		return co.getObj();
	}
	
	/**
	 * 缓冲对象定义
	 * @author James.W
	 */
	private static class CacheObject{
		private long delay=0L;//-1 永久缓冲
		private long create=0L;
		private Object obj=null;
		public CacheObject(long delay,Object obj){
			this.delay=delay;
			this.obj=obj;
			this.create=(new java.util.Date()).getTime();
		}
		/**
		 * 缓冲生命控制
		 * @return
		 */
		public long getDelay() {
			return this.delay;
		}
		/**
		 * 缓冲对象
		 * @return
		 */
		public Object getObj() {
			return this.obj;
		}
		/**
		 * 被缓冲时间
		 * @return
		 */
		public long getDelayed(){
			return (new java.util.Date()).getTime()-this.create;
		}
		
		
	}
	
	/*****下面是创建了系统级的缓冲 end*************************/
	
	/*****下面是一些共用的方法，简化系统调用*******************************************/
	/**
     * 加载XML文件
     * @param sysRootPath xml文件路径
     * @return Document
     */
    public static Document loadXML(String xmlpath) {
        //long ll_1 = System.currentTimeMillis();        
        try {
            SAXBuilder builder = new SAXBuilder(); //
            
            Document doc = builder.build(new File(xmlpath)); // 加载XML
//            Document doc = builder.build(reader);
            return doc;
        } catch (Throwable ex) {
        	//System.out.println("☆读取XML文件["+xmlpath+"]失败！\n"); //
            //ex.printStackTrace();
            return null;
        }finally{
        	//long ll_2 = System.currentTimeMillis();
            //System.out.println("☆读取XML文件["+xmlpath+"]操作结束,耗时["+(ll_2 - ll_1)+"]。\n"); //
        }
    }
    /**
     * 加载XML文件
     * @param sysRootPath xml文件路径
     * @return Document
     */
    public static Document loadXML(InputStream in) {
        //long ll_1 = System.currentTimeMillis();        
        try {
            SAXBuilder builder = new SAXBuilder(); //
            Document doc = builder.build(in); // 加载XML
            return doc;
        } catch (Throwable ex) {
        	//System.out.println("☆从输入流读取XML文件失败！\n"); //
            //ex.printStackTrace();
            return null;
        }finally{
        	//long ll_2 = System.currentTimeMillis();
            //System.out.println("☆从输入流读取XML文件操作结束,耗时["+(ll_2 - ll_1)+"]。\n"); //
        }
    }
    
    /**
     * 加载XML文件
     * @param in xml文件Reader
     * @return Document
     */
    public static Document loadXML(Reader in) {
        //long ll_1 = System.currentTimeMillis();        
        try {
            SAXBuilder builder = new SAXBuilder(); //
            Document doc = builder.build(in); // 加载XML
            return doc;
        } catch (Throwable ex) {
        	//System.out.println("☆从输入流读取XML文件失败！\n"); //
            //ex.printStackTrace();
            return null;
        }finally{
        	//long ll_2 = System.currentTimeMillis();
            //System.out.println("☆从输入流读取XML文件操作结束,耗时["+(ll_2 - ll_1)+"]。\n"); //
        }
    }
    
    /**
     * 加载XML文件
     * @param doc org.w3c.dom.Document类型的dom
     * @return Document
     */
    public static Document loadXML(org.w3c.dom.Document doc) {
        //long ll_1 = System.currentTimeMillis();        
        try {
        	DOMBuilder builder = new DOMBuilder(); //
            Document rt = builder.build(doc); // 加载XML
            return rt;
        } catch (Throwable ex) {
        	//System.out.println("☆从输入流读取XML文件失败！\n"); //
            //ex.printStackTrace();
            return null;
        }finally{
        	//long ll_2 = System.currentTimeMillis();
            //System.out.println("☆从输入流读取XML文件操作结束,耗时["+(ll_2 - ll_1)+"]。\n"); //
        }
    }
    /**
     * 使用缓冲功能的加载XML文件，使用的默认生命周期缓冲。
     * 由于过期后就重新加载，因此具有一定的动态加载能力。
     * @param sysRootPath xml文件路径
     * @return Document
     */
    public static Document loadXMLByCache(String xmlpath) {
    	//System.out.println("☆尝试从缓冲中读取XML文件["+xmlpath+"]...\n");
    	Document doc=(Document)getInfo(xmlpath);
    	if(doc==null){//对象没有缓冲或者已经过期了
    		//System.out.println("☆缓冲已经过期或者缓冲不存在，直接读取XML文件["+xmlpath+"]...\n");
        	doc=loadXML(xmlpath);
        	if(doc!=null){
        		putInfoCache(xmlpath,doc);
        	}
        }
    	return doc;
    }
    
    /**
     * 使用缓冲功能的加载XML文件，使用的默认生命周期缓冲。
     * 由于过期后就重新加载，因此具有一定的动态加载能力。
     * @param sysRootPath xml文件路径
     * @param keep 保持时间
     * @return Document
     */
    public static Document loadXMLByCache(String xmlpath, int keep) {
    	//System.out.println("☆尝试从缓冲中读取XML文件["+xmlpath+"]...\n");
    	Document doc=(Document)getInfo(xmlpath);
    	if(doc==null){//对象没有缓冲或者已经过期了
    		//System.out.println("☆缓冲已经过期或者缓冲不存在，直接读取XML文件["+xmlpath+"]...\n");
        	doc=loadXML(xmlpath);
        	if(doc!=null){
        		putInfo(xmlpath, doc, keep);
        	}
        }
    	return doc;
    }
    
    
    
    
    /**
     * 打开文件
     * @param fpath
     * @return
     */
    public static File newFile(String fpath)throws Exception{
    	File f=new File(fpath);
    	if(f.exists()){
    		f.delete();
    	}
    	f.createNewFile();
    	return f;
    }
    /**
     * 打开文件
     * @param fpath
     * @return
     */
    public static File openFile(String fpath)throws Exception{
    	File f=new File(fpath);
    	if(f.exists()){
    		if(f.isFile()){
    			return f;
    		}else{
    			throw new Exception("已经存在同名目录！");
    		}    		
    	}
    	f.createNewFile();
    	return f;
    }
    
    /**
     * 生成xml文件
     * @return
     */
    public static void buildXml(Document doc,String fpath)throws Exception{
    	File xmlfile=Sys.newFile(fpath);
    	FileOutputStream out=new FileOutputStream(xmlfile);
    	
    	XMLOutputter outputter = new XMLOutputter();
    	Format f = Format.getPrettyFormat();
    	f.setEncoding("UTF-8");//这句也可以不要，default=UTF-8 
    	outputter.setFormat(f); 
    	
    	outputter.output(doc, out);
		out.close();
		
    }
    /**
     * 生成xml片段
     * @return
     */
    public static String buildXmlString(Element e)throws Exception{
    	StringWriter out = new StringWriter();  
		XMLOutputter outputter = new XMLOutputter();
		outputter.output(e, out);
		return out.toString();    	    	
    }
    
}
