/**************************************************************************
 * $RCSfile: DBExplainerCache.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:20 $
 **************************************************************************/

package smartx.framework.metadata.bs;

import java.util.*;

public class DBExplainerCache {

    private static DBExplainerCache dbExplainerCache = null;

    private HashMap tableMap = null;

    private HashMap columnMap = null;

    private DBExplainerCache() {
        tableMap = new HashMap();
        columnMap = new HashMap();
    }

    public static DBExplainerCache getInstance() {
        if (dbExplainerCache != null) {
            return dbExplainerCache;
        }
        dbExplainerCache = new DBExplainerCache();
        return dbExplainerCache;
    }

    public void putTableName(String _tableName, String _tableLocalName) {
        tableMap.put(_tableName.toUpperCase(), _tableLocalName);
    }

    public void putColumnName(String _tableName, String _columnName, String _columnLocalName) {
        columnMap.put(_columnName.toUpperCase() + "@" + _tableName.toUpperCase(), _columnLocalName);
    }

    public String getTableLocalName(String _tableName) {
        return (String) tableMap.get(_tableName.toUpperCase());
    }

    public String getColumnLocalName(String _tableName, String _columnName) {
        return (String) columnMap.get(_columnName.toUpperCase() + "@" + _tableName.toUpperCase());
    }

}

/**************************************************************************
 * $RCSfile: DBExplainerCache.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:20 $
 *
 * $Log: DBExplainerCache.java,v $
 * Revision 1.2  2007/05/31 07:38:20  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:00:41  qilin
 * no message
 *
 * Revision 1.1  2007/02/05 04:39:52  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/