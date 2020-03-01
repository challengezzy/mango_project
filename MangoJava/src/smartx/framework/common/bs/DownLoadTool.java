package smartx.framework.common.bs;


import java.io.*;
import java.util.*;
import java.util.jar.*;

import org.apache.log4j.Logger;

import smartx.applet.vo.ClassFileVO;
import smartx.framework.common.utils.Sys;
import smartx.framework.common.vo.NovaLogger;

public class DownLoadTool {

	private String str_path = null;
    //自身缓冲对象 唯一
	private static DownLoadTool m_defaultTool = null;
    //路径对照jar包关系
	public static Hashtable ht_package_jar = new Hashtable();
    //缓冲的classes和images
	public static Hashtable ht_pluto = new Hashtable();
	//被缓冲过的jar 防止错误不能下载
	public static Hashtable ht_cache_jar=new Hashtable();
	//系统WEB-INF/lib下的所有jar包
	public static ArrayList sys_jars=new ArrayList();
	
	private static Logger logger=NovaLogger.getLogger(DownLoadTool.class);
	

	/**
	 * 
	 */
	public DownLoadTool(String _path) {
		super();
		this.str_path = _path;
	}

	/**
	 * 
	 * @param className
	 * @return
	 */
	public byte[] getClassBytes(String className, String _clientIP1, String _clientIP2) {

		String classFilePath = str_path + "classes/" + className.replace('.', '/') + ".class";
		ByteArrayOutputStream baos = null;
		FileInputStream fin = null;
		byte[] byteCodes = null;
		try {
			baos = new ByteArrayOutputStream();
			fin = new FileInputStream(classFilePath);
			byte[] buf = new byte[2048];
			int len = -1;
			while ((len = fin.read(buf)) != -1) {
				baos.write(buf, 0, len);
			}
			byteCodes = baos.toByteArray();
		} catch (java.io.FileNotFoundException e) {
			// from \lib\*.jar find...
			// System.out.println(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace(System.out);
		} finally {
			try {
				if (fin != null)
					fin.close();
				if (baos != null)
					baos.close();
			} catch (Exception e) {
			}
		}

		return byteCodes;
	}

	/**
	 * 
	 * @param classFile
	 * @return
	 */
	private ClassFileVO getClassFileVO(java.io.File classFile) {
		String className = classFile.getName();
		ByteArrayOutputStream baos = null;
		FileInputStream fin = null;
		byte[] byteCodes = null;
		try {
			baos = new ByteArrayOutputStream();
			fin = new FileInputStream(classFile);
			byte[] buf = new byte[2048];
			int len = -1;
			while ((len = fin.read(buf)) != -1) {
				baos.write(buf, 0, len);
			}
			byteCodes = baos.toByteArray();
		} catch (java.io.FileNotFoundException e) {
			// System.out.println(e.getMessage());
		} catch (java.io.IOException e) {
			e.printStackTrace(System.out);
		} finally {
			try {
				if (fin != null)
					fin.close();
				if (baos != null)
					baos.close();
			} catch (Exception e) {
			}
		}

		ClassFileVO cfVO = new ClassFileVO();
		cfVO.setClassFileName(className);
		cfVO.setByteCodes(byteCodes);
		return cfVO;
	}

	
	/**
	 * 
	 * @param cls
	 * @return
	 */
	private Vector getClassFromClasses(String cls){
		Vector vec_temp = new Vector();// 用来暂存所有所需文件的class处理文件
		int li_classesfiles = 0;
		String packageFilePath = str_path + "classes/" + cls.replace('.', '/');// 下载文件和绝对路径
		File dir = new File(packageFilePath); // 从/WEB-INF/classes 目录下找
		if (dir.exists() && dir.isDirectory()) { // 如果文件已存在，且是目录
			File[] files = dir.listFiles(new FileFilter() {
				public boolean accept(File fi) {
					return !fi.isDirectory() && !fi.getPath().toLowerCase().endsWith(".java");
				}
			});
			li_classesfiles = files == null ? 0 : files.length;
			
			if (li_classesfiles > 0) { // 把classes目录下的所需文件进行处理，然后压入temp_Vec中
				for (int i = 0; i < li_classesfiles; i++) {
					vec_temp.add(getClassFileVO(files[i]));
				}
			}

		}
		return vec_temp;		
	}
	
	/**
	 * 
	 * @param cls
	 * @return
	 */
	private Vector getClassFromJars(Vector vec_temp, String cls){
		// 从 /lib/*.jar 中寻找..
		String str_libpath = str_path + "lib/";
		String temp_dir = cls.replace('.', '/');
		int li_filecount = 0; //取文件的个数

		String str_type = ""; //打印时所用的取文件的出处类型
		String str_msg = ""; //打印的msg的头部

		if (ht_pluto.get(cls.replace('.', '/')) != null) {
			Vector vec_vos = (Vector) ht_pluto.get(cls.replace('.', '/'));
			if (vec_vos.size() >= 0) {
				int li_tmp = dealVector(vec_temp, vec_vos); // 拼两个Vector
				if (li_tmp > 0) {
					str_msg = str_msg + "系统初始缓存";
					li_filecount += li_tmp;
				}
			}
			str_type = "(初始内存)";
		} else if (ht_package_jar.get(cls) != null) { // 从ht中获取以前处理时，所存储的路径和jar,在缓存中发现包packageName
			Vector vec_jars = (Vector) ht_package_jar.get(cls);
			for (int i = 0; i < vec_jars.size(); i++) {
				String file_name = (String) vec_jars.get(i);
				Vector vector = dealJar(file_name, temp_dir, false); //
				if (vector.size() > 0) {
					int li_tmp = dealVector(vec_temp, vector); // 拼两个Vector
					if (li_tmp >= 0) {
						String tmp_filename = (String) vec_jars.get(i); //
						tmp_filename.replace('\\', '/');
						tmp_filename = tmp_filename.substring(tmp_filename.lastIndexOf("/") + 1, tmp_filename.length());
						str_msg = str_msg + "从/lib/" + tmp_filename + "(" + li_tmp + ");";
						li_filecount = li_filecount + li_tmp;
					}
				}
			}
			str_type = "(缓存)";
		} else { // 如果在ht中没有packageName的纪录，则重新遍历所有指定目录下的jar文件进行查找
			File lib_dir = new File(str_libpath);
			File[] f_jars = lib_dir.listFiles(new FileFilter() {
				public boolean accept(File fi) {
					return fi.getPath().toLowerCase().endsWith(".jar");
				}
			});
			for (int i = 0; i < f_jars.length; i++) { //循环所有的jar文件进行搜索查找
				Vector vector = dealJar(str_libpath + f_jars[i].getName(), temp_dir, true);
				if (vector.size() > 0) {
					int li_tmp = dealVector(vec_temp, vector);
					if (li_tmp >= 0) {
						String tmp_filename = (String) f_jars[i].getName(); // ..
						tmp_filename.replace('\\', '/'); //
						tmp_filename = tmp_filename.substring(tmp_filename.lastIndexOf("/") + 1, tmp_filename.length()); // ..
						str_msg = str_msg + "从/lib/" + tmp_filename + "(" + li_tmp + ");";
						li_filecount = li_filecount + li_tmp;
					}
				}
			}
			str_type = "(循环)";
		}
		
		return vec_temp;
	}
	
	
	/**
	 * 打包类路径下的所有类
	 * @param packageName 类完整路径
	 * @return
	 * @throws IOException
	 */
	public synchronized ClassFileVO[] getClassFileVOS(String className, String packageName, String _clientIP1, String _clientIP2) {
		
		//默认从classes目录下载，只要是在这个目录下都可以直接下载。
		long ll_time1 = System.currentTimeMillis();//寻找类 开始时间
		Vector vec_temp=getClassFromClasses(packageName);
		long ll_time2 = System.currentTimeMillis();//寻找类 结束时间
		
		//如果是gxlu开头的，在class没有找到可以到lib中再找找，如果不是gxlu开头的，即使没有找到也不找了。
		//如果已经进行过客户端jar包缓冲，则直接返回	
		if((packageName.startsWith("gxlu")&&vec_temp.size()==0)||
			!"true".equalsIgnoreCase((String)Sys.getInfo("CACHE_JAR"))){			
			vec_temp=getClassFromJars(vec_temp,packageName);
			long ll_time3 = System.currentTimeMillis();
			
			StringBuffer sb_out =new StringBuffer(1024);
			sb_out.append("[").append(_clientIP2).append("][").append(_clientIP1).append("]下载包[").append(packageName).append("](").append(className).append(")")
			      .append(",文件总数[").append(vec_temp.size()).append("],耗时[").append((ll_time3 - ll_time1)).append("]");
			logger.info(sb_out.toString());
			
		}else{
			StringBuffer sb_out =new StringBuffer(1024);
			sb_out.append("[").append(_clientIP2).append("][").append(_clientIP1).append("]下载包[").append(packageName).append("](").append(className).append(")")
			      .append(",文件总数[").append(vec_temp.size()).append("],耗时[").append((ll_time2 - ll_time1)).append("]");
			logger.info(sb_out.toString());
		}
		return (ClassFileVO[])vec_temp.toArray(new ClassFileVO[0]);		
	}

	/**
	 * 处理从classes或者jar文件系统中获取的文件的交集
	 * 
	 * @param _first
	 * @param _second
	 * @return
	 */
	public int dealVector(Vector _first, Vector _second) {
		int li_filecount = 0;
		boolean findElement = false;
		for (int i = 0; i < _second.size(); i++) {
			ClassFileVO classVO2 = ((ClassFileVO) _second.get(i));
			findElement = false;
			for (int j = 0; j < _first.size(); j++) {
				String classname2 = classVO2.getClassFileName();
				String classname1 = ((ClassFileVO) _first.get(j)).getClassFileName();
				if (classname2.equals(classname1)) {
					findElement = true;
					break;
				}
			}
			if (!findElement) {
				_first.add(classVO2);
				li_filecount++;
			}
		}

		return li_filecount;
	}

	/**
	 * 从jar文件中获取指定的目录下的所有文件
	 * 
	 * @param _filename
	 * @param _path
	 * @return
	 */
	public Vector dealJar(String _filename, String _dir, boolean _cycle) {
		byte[] byteCodes = null;

		Vector vec_classVO = new Vector();
		JarEntry myJarEntry = null;
		ByteArrayOutputStream baos = null;

		try {
			JarInputStream myJarInputStream = new JarInputStream(new FileInputStream(_filename));
			while (true) { // 循环释放jar下的文件，并赋予一个jarEntry对象(jarEntry对象我们可将它理解成解压jar后的每一个元素)
				myJarEntry = myJarInputStream.getNextJarEntry();
				if (myJarEntry == null) { // 如果jarEntry为空则中断循环
					break;
				} else if (myJarEntry.getName().toLowerCase().endsWith(".java")) {// 如果是java文件则不处理
					continue; //
				}

				if (_cycle) { //
					String str_key = myJarEntry.getName();

					if (str_key == null) {
					} else if (myJarEntry.isDirectory()) { // 如果直接判断出是目录
						str_key = str_key.replace('/', '.'); //
						str_key = str_key.substring(0, str_key.length() - 1);
					} else {
						int li_pos = str_key.lastIndexOf("/");
						if (li_pos >= 0) {
							str_key = str_key.substring(0, li_pos);
							str_key = str_key.replace('/', '.'); //
							str_key = str_key.substring(0, str_key.length());
						}
					}

					if (str_key != null) {
						Object obj = ht_package_jar.get(str_key); // 是否已有
						if (obj == null) { // 如果没有,则新建一个来存储.jar文件中所有的文件
							Vector v_jars = new Vector();
							v_jars.add(_filename);
							ht_package_jar.put(str_key, v_jars);
						} else {
							Vector v_jars = (Vector) obj; // 如果有,则在原.jar文件的Vec中加一个
							if (!v_jars.contains(_filename)) {
								v_jars.add(_filename);
								ht_package_jar.put(str_key, v_jars); //
							}
						}
					}
				}

				if (_dir.equals("")) { //如果包名为空,则该文件不是目录则统统下载下来!!!
					if (!myJarEntry.getName().trim().equals("") && myJarEntry.getName().trim().indexOf("/") < 0 && myJarEntry.getName().trim().indexOf("\\") < 0) {
						baos = new ByteArrayOutputStream(); // 创建输入流
						byte[] buf = new byte[1024];
						int len;
						while ((len = myJarInputStream.read(buf, 0, 1024)) > 0) {
							baos.write(buf, 0, len);
						}
						byteCodes = baos.toByteArray();
						baos.close();
						baos = null;
						ClassFileVO cfVO = new ClassFileVO();
						String str_realFileName = myJarEntry.getName().substring(_dir.length()); //
						cfVO.setClassFileName(str_realFileName);
						cfVO.setByteCodes(byteCodes);
						vec_classVO.add(cfVO);
					}
				} else if (myJarEntry.getName().startsWith(_dir + "/") && !myJarEntry.isDirectory()) { // 如果找到目录
					StringTokenizer token_file = new StringTokenizer(myJarEntry.getName(), "/");
					StringTokenizer token_dir = new StringTokenizer(_dir, "/");

					int token_file_count = token_file.countTokens();
					int token_dir_count = token_dir.countTokens();

					if (token_file_count > token_dir_count + 1) {
						continue;
					}
					baos = new ByteArrayOutputStream(); // 创建输入流
					byte[] buf = new byte[1024];
					int len;
					while ((len = myJarInputStream.read(buf, 0, 1024)) > 0) {
						baos.write(buf, 0, len);
					}
					byteCodes = baos.toByteArray();
					baos.close();
					baos = null;
					ClassFileVO cfVO = new ClassFileVO();
					String str_realFileName = myJarEntry.getName().substring(_dir.length() + 1); //
					cfVO.setClassFileName(str_realFileName);
					cfVO.setByteCodes(byteCodes);
					vec_classVO.add(cfVO);
				}

			}
		} catch (Throwable ex) {
			System.out.println(" \n\n\n\n  异常处理！！" + "\n\n\n");
			ex.printStackTrace();
		} finally {
			try {
				if (baos != null)
					baos.close();
			} catch (Exception e) {
			}
		}
		return vec_classVO;
	}
	
	/**
	 * 注册被缓冲的jar文件
	 * @param jarpathname
	 */
	public synchronized void addCachedJar(String jarpathname ){
		ht_cache_jar.put(jarpathname, jarpathname);
	}
	/**
	 * 判断jar文件是否被缓冲
	 * @param jarpathname
	 * @return
	 */
	public synchronized boolean isCachedJar(String jarpathname){
		return ht_cache_jar.get(jarpathname)!=null;
	}
	
	/**
	 * 注册Jar包
	 * @param jarpathname
	 */
	public synchronized void addJar(String[] jarinfo ){
		sys_jars.add(jarinfo);
	}
	/**
	 * 获得注册Jar包序列
	 * @param jarpathname
	 * @return 
	 */
	public synchronized ArrayList getJars(){
		return sys_jars;
	}
	
	
	
	

	/**
	 * 
	 * @param _path
	 * @return
	 */
	public static DownLoadTool getDefaultTool(String _path) {
		if (m_defaultTool == null)
			m_defaultTool = new DownLoadTool(_path);
		return m_defaultTool;
	}

}