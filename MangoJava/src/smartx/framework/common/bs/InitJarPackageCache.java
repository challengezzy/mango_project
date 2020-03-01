/**************************************************************************
 * $RCSfile: InitJarPackageCache.java,v $  $Revision: 1.16.2.4 $  $Date: 2008/11/25 10:25:42 $
 **************************************************************************/
package smartx.framework.common.bs;

import java.io.*;
import java.util.*;
import java.util.jar.*;

import smartx.applet.vo.ClassFileVO;
import smartx.framework.common.vo.NovaLogger;

/**
 * 处理nova2config.xml中指定的缓冲jar包
 * 
 * @deprecated 不建议使用，意义不大。当某个类不在这些jar包内的时候，还是需要从整个lib内的所有jar包查找，此时效率极低。
 *             而且缓冲的时候写死了部分路径，这也容易导致部分路径不能缓冲，这样做的意义实在有限。
 *             
 * @author Administrator
 *
 */
public class InitJarPackageCache {
    //特殊的起始路径，images开头表示图形图像目录，gxlu目录标识gxlu的本地类
	private final static String str_image_path = "images";
	private final static String str_gxlu_path = "gxlu";
    private final static String str_local_path = "local";
  
	private Vector vec_images = new Vector();

	private String[] str_jars;

	public InitJarPackageCache(String _realpath, String[] _initjars) {
		this.str_jars = _initjars;

		long ll_begin = System.currentTimeMillis();
		NovaLogger.getLogger(this).debug("开始缓存所有JAR包路径...");
        
		String rootpath=_realpath;
		// 将WEB-INF/lib下的所有jar文件的包注册一下
		_realpath = _realpath.replace('\\', '/');
		_realpath = _realpath + "WEB-INF/lib/";

		File lib_dir = new File(_realpath);
		File[] jar_files = lib_dir.listFiles(new FileFilter() {
			public boolean accept(File fi) {
				return fi.getPath().toLowerCase().endsWith(".jar");
			}
		});

		long ll_1 = 0;
		long ll_2 = 0;
		for (int i = 0; i < jar_files.length; i++) {
			try {
				String str_filename = _realpath + jar_files[i].getName();
				String str_filename2=str_filename.replaceAll("\\\\","/");
				System.out.println("扫描到服务端第三方包["+jar_files[i].getName()+"] 路径["+str_filename2+"] 长度["+jar_files[i].length()+"字节]");
				//注册系统jar包 James.W 2007.10.09
				DownLoadTool.getDefaultTool(rootpath).addJar(
					new String[]{
						jar_files[i].getName(),str_filename2,String.valueOf(jar_files[i].length())
						}	
					);
				
				JarInputStream myJarInputStream = new JarInputStream(new FileInputStream(str_filename));

				int li_count = 0;
				boolean li_flag = false;//判断是否指定缓冲jar包
				boolean li_flag2= false;//根据路径判断是否需要缓冲 gxlu和images开头的路径

				if (isInitJar(jar_files[i].getName())) {
					li_flag = true;
					ll_1 = System.currentTimeMillis();
				}
				while (true) { // 循环释放jar下的文件，并赋予一个jarEntry对象(jarEntry对象我们可将它理解成解压jar后的每一个元素)
					JarEntry myJarEntry = myJarInputStream.getNextJarEntry();
					if (myJarEntry == null) { // 如果jarEntry为空则中断循环
						break;
					} else if (myJarEntry.getName().toLowerCase().endsWith(".java")) { // 如果是java文件则不处理
						continue;
					}

					String str_entry = myJarEntry.getName();
					String str_key = null;
					

					if (!str_entry.trim().equals("") && str_entry.trim().indexOf("/") < 0 && str_entry.trim().indexOf("\\") < 0) {
						str_key = "";
					} else {
						if (myJarEntry.isDirectory()) { // 如果直接判断出是目录
							str_key = str_entry;
							str_key = str_key.replace('/', '.'); //
							str_key = str_key.substring(0, str_key.length() - 1);
						} else {
							str_key = str_entry;
							int li_pos = str_key.lastIndexOf("/");

							if (li_pos >= 0) {
								str_key = str_key.substring(0, li_pos);
								str_key = str_key.replace('/', '.'); //
								str_key = str_key.substring(0, str_key.length());
							}
						}
					}
					
					if (str_key != null) {
						Object obj = DownLoadTool.ht_package_jar.get(str_key); // 是否已有
						if (obj == null) { // 如果没有,则新建一个
							Vector v_jars = new Vector();
							v_jars.add(str_filename);
							DownLoadTool.ht_package_jar.put(str_key, v_jars);
							li_count++;
						} else {
							Vector v_jars = (Vector) obj; // 如果有,则在原来的基础上加一个
							if (!v_jars.contains(str_filename)) {
								v_jars.add(str_filename);
								DownLoadTool.ht_package_jar.put(str_key, v_jars);
								li_count++;
							}
						}
						
					}
					if (myJarEntry.isDirectory() || myJarEntry.getName().toLowerCase().endsWith(".java")) {
						continue;
					}
					
					/* TODO 这一段有点浪费效率，因此暂时屏蔽掉观察观察。
					//如果路径是gxlu开头的，则自动缓冲 James.W 2007.10.08
					if(str_key.startsWith(str_gxlu_path)||str_key.startsWith(str_image_path)||str_key.startsWith(str_local_path)){
						li_flag2=true;	
						//该jar包已经缓冲过了 James.W 2007.10.09
						DownLoadTool.getDefaultTool(rootpath).addCachedJar(str_filename2);
					}else{
						li_flag2=false;
					}
					*/
					li_flag2=false;//如果要把上一段注释去掉，本行语句应该删除。

					ByteArrayOutputStream baos = null;
					byte[] byteCodes = null;
					if (li_flag||li_flag2) {
						//if(str_entry.startsWith(str_image_path)){
						//    System.out.println("缓冲："+str_entry+" Jar包："+str_filename);
						//}
						baos = new ByteArrayOutputStream(); // 创建输入流
						byte[] buf = new byte[1024];
						int len;
						while ((len = myJarInputStream.read(buf, 0, 1024)) > 0) {
							baos.write(buf, 0, len);
						}
						byteCodes = baos.toByteArray();
						baos.close();
						baos = null;

						String str_f = str_entry; 
						String str_package = str_entry;
						int li_pos = str_entry.lastIndexOf("/");
						if (li_pos <= 0 || str_entry.length() <= li_pos + 1) {
						} else {
							str_package = str_entry.substring(0, li_pos);
							str_f = str_entry.substring(li_pos + 1);
						}
						Vector vec_files = (Vector) DownLoadTool.ht_pluto.get(str_package);
						if (vec_files == null) {
							vec_files = new Vector();
						}
						if (str_package.startsWith(str_image_path)) {
							String str_temp = str_f.toLowerCase();
							if (str_temp.endsWith(".jpg") || str_temp.endsWith(".gif") || str_temp.endsWith(".jpeg") || str_temp.endsWith(".ico")) {
								vec_images.add(str_entry);
							}
						}
						ClassFileVO cfVO = new ClassFileVO();
						cfVO.setClassFileName(str_f);
						cfVO.setByteCodes(byteCodes);
						vec_files.add(cfVO);

						DownLoadTool.ht_pluto.put(str_package, vec_files);
					}
				}
				if (li_flag) {
					li_flag = false;
					ll_2 = System.currentTimeMillis(); //
					NovaLogger.getLogger(this).debug("加载[" + jar_files[i].getName() + "]至缓存结束,耗时[" + (ll_2 - ll_1) + "]!");
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		long ll_end = System.currentTimeMillis();
		NovaLogger.getLogger(this).debug("缓存所有JAR包路径结束,共处理[" + jar_files.length + "]个JAR文件,耗时[" + (ll_end - ll_begin) + "]");
	}

	private boolean isInitJar(String _jar) {
		if (str_jars == null || str_jars.length == 0) {
			return false;
		}
		for (int i = 0; i < str_jars.length; i++) {
			if (str_jars[i] != null && str_jars[i].trim().equals(_jar)) {
				return true;
			}
		}
		return false;
	}

	public Vector getImagesVec() {
		return vec_images;
	}
}

/*******************************************************************************
 * $RCSfile: InitJarPackageCache.java,v $ $Revision: 1.16.2.4 $ $Date: 2007/01/30
 * 03:35:24 $
 *
 * $Log: InitJarPackageCache.java,v $
 * Revision 1.16.2.4  2008/11/25 10:25:42  wangqi
 * *** empty log message ***
 *
 * Revision 1.16.2.3  2008/10/13 07:30:56  wangqi
 * *** empty log message ***
 *
 * Revision 1.16.2.2  2008/08/25 09:17:00  wangqi
 * *** empty log message ***
 *
 * Revision 1.16.2.1  2008/04/22 08:13:19  wangqi
 * *** empty log message ***
 *
 * Revision 1.16  2007/10/09 03:39:32  wangqi
 * *** empty log message ***
 *
 * Revision 1.15  2007/10/09 03:24:38  wangqi
 * *** empty log message ***
 *
 * Revision 1.14  2007/10/09 03:15:35  wangqi
 * *** empty log message ***
 *
 * Revision 1.13  2007/10/09 02:49:48  wangqi
 * *** empty log message ***
 *
 * Revision 1.12  2007/10/08 09:41:53  wangqi
 * *** empty log message ***
 *
 * Revision 1.11  2007/10/08 09:32:02  wangqi
 * *** empty log message ***
 *
 * Revision 1.10  2007/10/08 09:26:27  wangqi
 * *** empty log message ***
 *
 * Revision 1.9  2007/10/08 09:24:59  wangqi
 * *** empty log message ***
 *
 * Revision 1.8  2007/10/08 06:57:31  wangqi
 * *** empty log message ***
 *
 * Revision 1.7  2007/07/27 01:05:51  shxch
 * *** empty log message ***
 *
 * Revision 1.6  2007/07/16 09:01:55  sunxf
 * *** empty log message ***
 *
 * Revision 1.5  2007/07/16 08:00:30  sunxf
 * *** empty log message ***
 *
 * Revision 1.4  2007/07/16 05:59:28  sunxf
 * 增加并行环节跳转支持
 *
 * Revision 1.3  2007/05/31 07:38:16  qilin
 * code format
 *
 * Revision 1.2  2007/05/22 07:58:47  qilin
 * no message
 *
 * Revision 1.1  2007/05/17 05:56:15  qilin
 * no message
 *
 * Revision 1.9  2007/02/27 03:22:06  shxch
 * *** empty log message ***
 *
 * Revision 1.8  2007/02/27 03:18:13  shxch
 * *** empty log message ***
 *
 * Revision 1.7  2007/02/27 01:30:31  shxch
 * *** empty log message ***
 *
 * Revision 1.6  2007/02/27 01:23:43  shxch
 * *** empty log message ***
 *
 * Revision 1.5  2007/02/01 05:57:28  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/02/01 05:55:12  shxch
 * 完善初始Cache中的图片
 *
 * Revision 1.3  2007/01/30 05:43:42  shxch
 * *** empty log message ***
 * Revision 1.2 2007/01/30 03:35:24 lujian
 * *** empty log message ***
 *
 *
 ******************************************************************************/
