/**************************************************************************

 * $RCSfile: NovaServerEnvironment.java,v $  $Revision: 1.5.2.2 $  $Date: 2009/09/15 02:50:10 $
 **************************************************************************/
package smartx.framework.metadata.bs;

import java.io.*;
import java.util.*;

import smartx.framework.common.vo.*;


/**
 * 客户端缓存
 * @author user
 *
 */
public class NovaServerEnvironment implements Serializable {

    public static String[] APPMODULENAME;

    private static final long serialVersionUID = -3231827644600597388L;

    private static NovaServerEnvironment serverEnv = null; //

    private VectorMap map = null;

    private HashMap descmap = null;

    private String str_defaultdatasource = null;

    private String[] str_alldatasources = null;

    private NovaServerEnvironment() {
        map = new VectorMap();
        descmap = new HashMap();
    }

    public static NovaServerEnvironment getInstance() {
        if (serverEnv != null) {
            return serverEnv;
        }

        serverEnv = new NovaServerEnvironment(); ////.......
        return serverEnv;
    }

    public void put(Object _key, Object _value) {
        map.put(_key, _value);
    }

    public void put(Object _key, String _desc, Object _value) {
        map.put(_key, _value);
        descmap.put(_key, _desc);
    }

    public Object get(Object _key) {
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
        str_return[2] = "" + get(_key);
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
            } else { //
                str_data[i][1] = "";
                str_data[i][2] = "";
            } //
        }
        return str_data;
    }

    /**
     * 默认数据源名称
     * @return
     */
    public String getDefaultDataSourceName() {
        if (str_defaultdatasource != null) {
            return str_defaultdatasource;
        }

        str_defaultdatasource = (String) get("defaultdatasource"); //
        return str_defaultdatasource;
    }

    /**
     * 默认数据源名称
     * @return
     */
    public String[] getAllDataSourceNames() {
        if (str_alldatasources != null) {
            return str_alldatasources;
        }

        str_alldatasources = (String[]) get("ALLDATASOURCENAMES"); //
        return str_alldatasources;
    }

    public String[] getAppModuleName() {
        return APPMODULENAME;
    }
    
    public void setAppModuleName(String[] names) {
        APPMODULENAME=names;
    }

    /**
     * 设置name实现类（不建议使用）
     * @deprecated 不建议使用，请直接是使用
     * @param className
     * @throws Exception
     */
    public void setAppModuleClass(String className) throws Exception {
    	try{
    		APPMODULENAME = new String[]{((NovaAppModuleConfigIFC)Class.forName(className.trim()).newInstance()).getAppModuleName()};
    	}catch(Exception e){
    		APPMODULENAME = className.trim().split(",");
    	}
    }
}
/**************************************************************************
 * $RCSfile: NovaServerEnvironment.java,v $  $Revision: 1.5.2.2 $  $Date: 2009/09/15 02:50:10 $
 *
 * $Log: NovaServerEnvironment.java,v $
 * Revision 1.5.2.2  2009/09/15 02:50:10  wangqi
 * *** empty log message ***
 *
 * Revision 1.5.2.1  2008/11/25 10:26:32  wangqi
 * *** empty log message ***
 *
 * Revision 1.5  2007/10/16 06:26:17  wangqi
 * *** empty log message ***
 *
 * Revision 1.4  2007/06/05 10:25:10  qilin
 * no message
 *
 * Revision 1.3  2007/06/02 01:56:23  qilin
 * no message
 *
 * Revision 1.2  2007/05/31 07:38:21  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:00:41  qilin
 * no message
 *
 * Revision 1.4  2007/03/01 08:42:15  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/02/27 06:57:18  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 03:44:56  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
