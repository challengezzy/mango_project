/**************************************************************************
 * $RCSfile: NovaClientCache.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:23 $
 **************************************************************************/
package smartx.framework.common.ui;

import java.io.*;
import java.util.*;

import smartx.framework.common.vo.*;


/**
 * 客户端缓存
 * @author user
 *
 */
public class NovaClientCache implements Serializable {

    private static final long serialVersionUID = -3231827644600597388L;

    private static NovaClientCache clientCache = null; //

    private VectorMap map = null; //

    private HashMap descmap = null;

    private NovaClientCache() {
        map = new VectorMap();
        descmap = new HashMap();
    }

    public final static NovaClientCache getInstance() {
        if (clientCache != null) {
            return clientCache;
        }
        clientCache = new NovaClientCache();
        return clientCache;
    }

    public final void put(Object _key, Object _value) {
        map.put(_key, _value);
    }

    public final void put(Object _key, Object _desc, Object _value) {
        map.put(_key, _value);
        descmap.put(_key, _desc);
    }

    public final Object get(Object _key) {
        return map.get(_key); //
    }

    public Object[] getKeys() {
        return map.getKeys(); //
    }

    public Object[] getRowValue(Object _key) {
        Object[] str_return = new Object[3];
        if (get(_key) == null) {
            return null;
        }

        str_return[0] = _key;
        str_return[1] = descmap.get(_key) == null ? "" : descmap.get(_key);
        str_return[2] = get(_key);
        return str_return;
    }

    public Object[][] getAllData() {
        Object[] str_keys = getKeys();
        Object[][] str_data = new Object[str_keys.length][3];
        for (int i = 0; i < str_keys.length; i++) {
            str_data[i][0] = str_keys[i];

            Object[] rowValue = getRowValue(str_keys[i]);
            if (rowValue != null) {
                str_data[i][1] = getRowValue(str_keys[i])[1];
                str_data[i][2] = getRowValue(str_keys[i])[2];
            }

        }
        return str_data;
    }
}
/**************************************************************************
 * $RCSfile: NovaClientCache.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:23 $
 *
 * $Log: NovaClientCache.java,v $
 * Revision 1.2  2007/05/31 07:38:23  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 05:56:36  qilin
 * no message
 *
 * Revision 1.3  2007/02/27 06:57:19  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 03:41:28  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/