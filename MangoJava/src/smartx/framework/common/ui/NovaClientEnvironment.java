/**************************************************************************
 * $RCSfile: NovaClientEnvironment.java,v $  $Revision: 1.5.2.5 $  $Date: 2009/10/24 03:06:14 $
 **************************************************************************/
package smartx.framework.common.ui;

import java.io.*;
import java.util.*;

import smartx.framework.common.vo.VectorMap;


/**
 * 客户端缓存
 * 
 * @author user
 */
public final class NovaClientEnvironment implements Serializable {

	private static final long serialVersionUID = -3231827644600597388L;

	private  NovaClientEnvironment clientEnv = null; //

	private VectorMap map = null; //

	private HashMap descmap = null;

	//TODO 把str_defaultdatasource放到客户端变量，并取消此处的str_defaultdatasource改用get("defaultdatasource")
	private String str_defaultdatasource = null; //默认数据源的名称,这里先写死,实际上应该从服务器取!!
	private Boolean bo_isDevelopeMode = null; //是否是Debug模式!!!
	public static String APPMODULENAME;//模块名称
	//TODO 把ClientCodeCache放到客户端变量，并取消此处的clientCodeCacheDir改用get("CLIENT_CODECACHE")
	private String clientCodeCacheDir = null;  //客户端代码缓存路径
	
	public NovaClientEnvironment() {
		map = new VectorMap();
		descmap = new HashMap();
	}

	public final static NovaClientEnvironment newInstance() {
//		clientEnv = new NovaClientEnvironment();
		return new NovaClientEnvironment();
	}
//	
	public final static NovaClientEnvironment getInstance() {
//		if (clientEnv != null) {
//			return clientEnv;
//		}
//		clientEnv = new NovaClientEnvironment();
		return new NovaClientEnvironment();
	}

	public final void put(Object _key, Object _value) {
		map.put(_key, _value);
	}

	public final void put(Object _key, String _desc, Object _value) {
		map.put(_key, _value);
		descmap.put(_key, _desc);
	}

	public final Object get(Object _key) {
		return map.get(_key); //
	}

	public String[] getKeys() {
		return map.getKeysAsString(); //
	}

	public String[] getRowValue(Object _key) {
		String[] str_return = new String[3];
		if (get(_key) == null) {
			return null;
		}

		str_return[0] = (String) _key;
		str_return[1] = descmap.get(_key) == null ? "" : descmap.get(_key).toString();
		Object obj = get(_key);
		String str_value_tmp = "";
		if (obj.getClass().isArray()) {
			Object[] arrays = (Object[]) obj;
			for (int i = 0; i < arrays.length; i++) {
				str_value_tmp = str_value_tmp + arrays[i];
				if (i != arrays.length - 1) {
					str_value_tmp = str_value_tmp + ",";
				}
			}
		} else {
			str_value_tmp = "" + obj;
		}
		str_return[2] = str_value_tmp;
		return str_return;
	}

	public String[][] getAllData() {
		String[] str_keys = getKeys();
		String[][] str_data = new String[str_keys.length][3];
		for (int i = 0; i < str_keys.length; i++) {
			str_data[i][0] = str_keys[i];

			String[] rowValue = getRowValue(str_keys[i]);
			if (rowValue != null) {
				str_data[i][1] = getRowValue(str_keys[i])[1];
				str_data[i][2] = getRowValue(str_keys[i])[2];
			}

		}
		return str_data;
	}

	/**
	 * 默认数据源!!!
	 * @return
	 */
	public String getDefaultDatasourceName() {
		if (str_defaultdatasource != null) {
			return str_defaultdatasource; //
		}

		str_defaultdatasource = (String) get("defaultdatasource"); //
		return str_defaultdatasource;
	}

	/**
	 * 判断是否是管理员方式登录
	 * @return
	 */
	public boolean isAdmin() {
		String sIsAdmin = (String) get("SYS_LOGINUSER_TYPE");
		return ("y".equalsIgnoreCase(sIsAdmin)||"true".equalsIgnoreCase(sIsAdmin));		
	}

	/**
	 * 判断是否是开发模式!!!
	 * @return
	 */
	public boolean idDevelopeMode() {
		if (bo_isDevelopeMode != null) {
			return bo_isDevelopeMode.booleanValue();
		}

		String str_workmode = System.getProperty("WORKMODE"); //取得运行模式
		if (str_workmode != null && str_workmode.equalsIgnoreCase("DEVELOPE")) {
			bo_isDevelopeMode = new Boolean(true); //开发模式!!
		} else {
			bo_isDevelopeMode = new Boolean(false); //
		}

		return bo_isDevelopeMode.booleanValue();
	}

	public String getAppModuleName() {
		return APPMODULENAME;
	}

	public void setAppModuleName(String name) throws Exception {
		this.APPMODULENAME = name;
	}

	/**
	 * 定义客户端代码缓存路径
	 * @return
	 */
	public String getClientCodeCacheDir() {
		return clientCodeCacheDir;
	}

	public void setClientCodeCacheDir(String clientCodeCacheDir) {
		this.clientCodeCacheDir = clientCodeCacheDir;
	}
	
	/**
	 * 获得登录用户编码
	 * @return
	 * @deprecated 本方法用(String)get("SYS_LOGINUSER_CODE")替换
	 */
	public String getLoginUserCode() {
		return (String)get("SYS_LOGINUSER_CODE"); //
	}

}

/**************************************************************************
 * $RCSfile: NovaClientEnvironment.java,v $  $Revision: 1.5.2.5 $  $Date: 2009/10/24 03:06:14 $
 *
 * $Log: NovaClientEnvironment.java,v $
 * Revision 1.5.2.5  2009/10/24 03:06:14  wangqi
 * *** empty log message ***
 *
 * Revision 1.5.2.4  2009/10/21 06:24:53  wangqi
 * *** empty log message ***
 *
 * Revision 1.5.2.3  2009/09/03 06:33:22  wangqi
 * *** empty log message ***
 *
 * Revision 1.5.2.2  2009/06/01 09:28:55  wangqi
 * *** empty log message ***
 *
 * Revision 1.5.2.1  2008/12/16 06:29:10  wangqi
 * *** empty log message ***
 *
 * Revision 1.5  2008/01/08 00:46:57  wangqi
 * *** empty log message ***
 *
 * Revision 1.4  2007/07/16 09:50:40  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/06/05 10:25:10  qilin
 * no message
 *
 * Revision 1.2  2007/05/31 07:38:23  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 05:56:36  qilin
 * no message
 *
 * Revision 1.6  2007/03/16 01:38:07  qilin
 * no message
 *
 * Revision 1.5  2007/03/02 01:44:02  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/02/27 06:57:19  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/02/01 07:10:31  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 03:41:28  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
