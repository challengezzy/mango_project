package smartx.system.service;


import java.io.*;
import java.util.ArrayList;

import smartx.framework.common.bs.DownLoadTool;
import smartx.framework.common.utils.Sys;


public class SysJarLibsServiceImpl implements SysJarLibsServiceIFC {
   
	/**
	 * 读取本地应用下的WEB-INF/lib目录下的所有jar包
	 * 
	 * @return ArrayList jar信息序列 [jarname,size]
	 * @throws Exception
	 */
	public ArrayList getSysLibs()throws Exception{
		ArrayList rt=new ArrayList();
		//判断是否需要jar缓冲
		String cachejar=(String)Sys.getInfo("CACHE_JAR");
		if(cachejar==null||!"true".equals(cachejar.toLowerCase())){
			return rt;
		}
		System.out.println("开始准备第三方包列表：");
		
		String rootpath=(String)Sys.getInfo("NOVA2_SYS_ROOTPATH");
		ArrayList jars=DownLoadTool.getDefaultTool(rootpath).getJars();
		
		if(jars==null){
			System.out.println("没有需要服务端第三方包！");
			return rt;
		}
		
		for(int i=0;i<jars.size();i++){
			String[] info=(String[])jars.get(i);	
			//System.out.println("服务端第三方包["+info[0]+"] 路径["+info[1]+"] 长度["+info[2]+"字节]");
			//判断是否被缓冲
			if(!DownLoadTool.getDefaultTool(rootpath).isCachedJar(info[1])){
			  rt.add(info);
			}
		}
		return rt;		
		
//		// TODO 默认WEB-INF/lib/，以后可以使用配置文件指定目录
//		String libpath=System.getProperty("NOVA2_SYS_ROOTPATH")+"WEB-INF/lib/";
//		File file=new File(libpath);
//		File list[]=file.listFiles();
//		ArrayList rt=new ArrayList();
//		for(int i=0;i<list.length;i++){
//			System.out.println("检索到服务器端本地文件["+list[i].getName()+"] 路径["+list[i].getPath()+"] 长度["+list[i].length()+"]");
//			
//			if (list[i].isDirectory()){//是目录
//				;
//			}else{//是文件
//				String fname=list[i].getName();
//				if(fname.length()>4&&fname.substring(fname.length()-4).equals(".jar")){
//					//name,path,size
//					String[] info=new String[]{
//							list[i].getName(),
//							list[i].getPath().replace('\\', '/'),
//							String.valueOf(list[i].length())						
//					};
//					rt.add(info);
//				}				
//			}			
//		}		
//		return rt;
	}
	
	/**
	 * 读取本地应用下的WEB-INF/lib目录下的指定jar包
	 * @param f String[]=[jarname,path,size]
	 * @return ArrayList jar信息序列 [jarname,size,block1,block2,...]每100k作为一段 
	 * @throws Exception
	 */
	public ArrayList getSysLib(String[] f)throws Exception{
		if(f==null||f.length==0){
			return null;
		}
		File file=null;
		FileInputStream fis=null;
		try{
			file=new File(f[1]);
			fis=new FileInputStream(file);
			byte[] bs=new byte[1024*100];
			ArrayList rt=new ArrayList();
			rt.add(f[0]);
			rt.add(f[2]);
			while(fis.read(bs)>0){
				rt.add(bs);
				bs=new byte[1024*100];						
			}
			
			return rt;
		}catch(Exception e){
			System.out.println("读写文件["+f[0]+"]错误！"+e.getMessage());
			throw new Exception("读写文件["+f[0]+"]错误！"+e.getMessage());			
		}finally{
			if(fis!=null){
			  try{fis.close();}catch(Exception e){;}
			}			
		}
	}
	
}
