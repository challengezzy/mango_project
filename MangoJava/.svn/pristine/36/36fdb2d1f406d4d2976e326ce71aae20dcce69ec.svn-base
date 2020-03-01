/**************************************************************************
 * $RCSfile: VectorMap.java,v $  $Revision: 1.3.2.1 $  $Date: 2009/10/16 03:48:24 $
 **************************************************************************/
package smartx.framework.common.vo;

import java.io.*;
import java.util.*;


//TODO 本类用途整理
/**
 * 
 * @author James.W
 * 
 */
public class VectorMap implements Serializable {

    private static final long serialVersionUID = -2057520512242590475L;

    private HashMap hashMap = null;

    private Vector vector = null;

    public VectorMap() {
        hashMap = new HashMap();
        vector = new Vector();
    }

    public synchronized void put(Object _key, Object _value) {
        hashMap.put(_key, _value);
        if (!vector.contains(_key)) {
            vector.add(_key);
        }
    }

    public Object get(Object _key) {
        return hashMap.get(_key);
    }

    public Object get(int _index) {
        Object str_key = getKeys()[_index]; //取得Key名!!
        return hashMap.get(str_key); //
    }

    public void set(int _index, Object obj) {
        Object str_key = getKeys()[_index]; //取得Key名!!
        hashMap.put(str_key, obj); //
    }

    public Set keySet() {
        return hashMap.keySet();
    }

    public boolean isEmpty() {
        return hashMap.isEmpty();
    }

    public synchronized void remove(Object _key) {
        hashMap.remove(_key);
        vector.remove(_key);
    }

    public int size() {
        return hashMap.size();
    }

    public Object[] getKeys() {
        return vector.toArray();
    }

    public String[] getKeysAsString() {
        return (String[]) vector.toArray(new String[0]);
    }

    public Object[] getValues() {
        Object[] keys = getKeys();
        Object[] objs = new Object[keys.length];
        for (int i = 0; i < keys.length; i++) {
            objs[i] = get(keys[i]);
        }
        return objs;
    }

    public String[] getValuesAsString() {
        Object[] keys = getKeys();
        String[] objs = new String[keys.length];
        for (int i = 0; i < keys.length; i++) {
            objs[i] = (String) get(keys[i]);
        }
        return objs;
    }

    public Object[][] getAllData() {
        Object[] keys = getKeys();
        Object[][] objs = new Object[keys.length][2];
        for (int i = 0; i < keys.length; i++) {
            objs[i][0] = keys[i];
            objs[i][1] = get(keys[i]);
        }
        return objs;
    }

    public String[][] getAllDataAsString() {
        Object[] keys = getKeys();
        String[][] objs = new String[keys.length][2];
        for (int i = 0; i < keys.length; i++) {
            objs[i][0] = (String) keys[i];
            objs[i][1] = (String) get(keys[i]);
        }
        return objs;
    }

    public HashMap getMap(){
    	return hashMap;
    }
    
    public void clear() {
        vector.clear();
        hashMap.clear();
    }

    public static void main(String[] args) {
        VectorMap vm = new VectorMap();
        vm.put("aa", "111");
        vm.put("bb", "222");
        vm.put("cc", "333");

        String[][] str_keys = vm.getAllDataAsString();
        System.out.println(str_keys[0][0] + "," + str_keys[0][1]);

        vm.set(2, null);
        for (int i = 0; i < 3; i++) {
            System.out.println(vm.get(i));
        }
    }

	/**
	 * 测试是否存在键值
	 * @param _itemkey
	 * @return 是否存在
	 * @since 1.3 add by James.W for PrintTools
	 */
	public boolean containsKey(String _itemkey) {
		return hashMap.containsKey(_itemkey);
	}

}
/**************************************************************************
 * $RCSfile: VectorMap.java,v $  $Revision: 1.3.2.1 $  $Date: 2009/10/16 03:48:24 $
 *
 * $Log: VectorMap.java,v $
 * Revision 1.3.2.1  2009/10/16 03:48:24  wangqi
 * *** empty log message ***
 *
 * Revision 1.3  2007/12/26 08:35:44  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2007/05/31 07:38:23  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 05:56:47  qilin
 * no message
 *
 * Revision 1.2  2007/03/30 07:10:03  qilin
 * no message
 *
 * Revision 1.1  2007/02/27 06:57:19  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:16:43  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
