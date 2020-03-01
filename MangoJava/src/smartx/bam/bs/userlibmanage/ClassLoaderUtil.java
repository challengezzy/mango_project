/**
 * 
 */
package smartx.bam.bs.userlibmanage;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author caohenghui
 * 
 */
public class ClassLoaderUtil {

	private static Field classes;

	private static Method addURL;

	static {
		try {
			classes = ClassLoader.class.getDeclaredField("classes");
			addURL = URLClassLoader.class.getDeclaredMethod("addURL",new Class[] { URL.class });
		} catch (Exception e) {
			e.printStackTrace();
		}
		classes.setAccessible(true);
		addURL.setAccessible(true);
	}

	private static URLClassLoader systemLoader = (URLClassLoader) getSystemClassLoader();
	
	private static URLClassLoader userLoader = null;
	
	public static ClassLoader getSystemClassLoader() {
		return ClassLoader.getSystemClassLoader();
	}
	
	private static void createUserClassLoader(URL[] urls){
		if(userLoader == null){
			userLoader = URLClassLoader.newInstance(urls);
		}
	}
	
	private static void destroyUserLoader(){
		userLoader = null;
		System.gc();
	}
	
	public static ClassLoader getUserClassLoader(){
		return userLoader;
	}

	public static URL[] getSystemClassPathURLs() {
		return systemLoader.getURLs();
	}
	
	public static URL[] getUserClassPathURLs() {
		URL[] urls = null;
		if(userLoader != null){
			urls = userLoader.getURLs();
		}
		return urls;
	}

	public static void addURL2SystemClassLoader(URL url) {
		try {
			addURL.invoke(systemLoader, new Object[] { url });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void addURL2UserClassLoader(URL url){
		try {
			if(userLoader == null){
				createUserClassLoader(new URL[]{url});
			}
			addURL.invoke(userLoader, new Object[] { url });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param file 可以是文件夹或jar文件
	 *
	 */
	public static void addSystemClassPath(File file) {
		try {
			if (file.isDirectory()) {
				List<File> fileList = new ArrayList<File>();
				fileList = getJarFileFromDirectory(file, fileList);
				for (File tempFile : fileList) {
					addURL2SystemClassLoader(tempFile.toURI().toURL());
				}
			} else {
				String fileName = file.getName().toLowerCase();
				if (fileName.endsWith(".jar")) {
					addURL2SystemClassLoader(file.toURI().toURL());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void addUserClassPath(File file) {
		try {
			if (file.isDirectory()) {
				List<File> fileList = new ArrayList<File>();
				fileList = getJarFileFromDirectory(file, fileList);
				for (File tempFile : fileList) {
					addURL2UserClassLoader(tempFile.toURI().toURL());
				}
			} else {
				String fileName = file.getName().toLowerCase();
				if (fileName.endsWith(".jar")) {
					addURL2UserClassLoader(file.toURI().toURL());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static List<File> getJarFileFromDirectory(File file,List<File> fileList) {
		File[] files = file.listFiles();
		for (File tempFile : files) {
			if (tempFile.isDirectory()) {
				getJarFileFromDirectory(tempFile,fileList);
			} else {
				String fileName = tempFile.getName().toLowerCase();
				if (fileName.endsWith(".jar")) {
					fileList.add(tempFile);
				}
			}
		}
		return fileList;
	}
	
	public static void unLoadJarFromUserClassPath(File file){
		
		try{
			
			if(file.isFile()){
				String fileName = file.getName().toLowerCase();
				if (fileName.endsWith(".jar")) {
					URL[] urls = getUserClassPathURLs();
					destroyUserLoader();
					if(urls != null){
						for(URL url:urls){
							if(!file.toURI().toURL().getPath().equalsIgnoreCase(url.getPath())){
								addURL2UserClassLoader(url);
							}
						}
					}
				}
			}
		}catch(Exception e){
			
		}
		
	}

}
