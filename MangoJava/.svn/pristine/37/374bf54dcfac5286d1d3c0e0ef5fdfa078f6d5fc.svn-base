package smartx.system.util.hashvo;

import java.text.*;
import java.util.*;

import smartx.framework.common.bs.*;
import smartx.framework.common.vo.*;

public class HashVOUtil {
    public HashVOUtil() {
    }

    CommDMO dmo = null;
    private CommDMO getDMO() {
        if (dmo == null) {
            dmo = new CommDMO();
        }
        return dmo;
    }

    private HashMap copyMap(HashMap value) {
        HashMap result = new HashMap();
        if (value != null) {
            Iterator it = value.keySet().iterator();
            while (it.hasNext()) {
                Object key = it.next();
                result.put(key, value.get(key));
            }
        }
        return result;
    }

    public InsertSQLVO getInsertSQLByHashVO(HashVO vo, String tableName, HashMap _replaceValues, String dataSourceName){
        InsertSQLVO result = new InsertSQLVO();
        StringBuffer strAttributes = new StringBuffer();
        StringBuffer strValus = new StringBuffer();
        HashMap replaceValues = copyMap(_replaceValues);

        String key[] = vo.getKeys();
        for (int i = 0; i < key.length; i++) {
            strAttributes.append(key[i]);
            strAttributes.append(",");
            Object valueObj = vo.getObjectValue(key[i]);
			if (replaceValues != null && replaceValues.get(key[i].toUpperCase()) != null) {
				valueObj = replaceValues.get(key[i].toUpperCase());
				replaceValues.remove(key[i].toUpperCase());
			}
			appendValue(valueObj, strValus);
        }

        if (replaceValues!= null&&replaceValues.size()!=0) {
            Iterator it = replaceValues.keySet().iterator();
            while (it.hasNext()) {
                String replaceKey = (String) it.next();
                strAttributes.append(replaceKey);
                strAttributes.append(",");
                appendValue(replaceValues.get(replaceKey), strValus);
            }
        }
        strAttributes.deleteCharAt(strAttributes.length() - 1);
        strValus.deleteCharAt(strValus.length() - 1);

        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO ");
        sql.append(tableName);
        sql.append(" (");
        sql.append(strAttributes);
        sql.append(")");
        sql.append(" VALUES ");
        sql.append("(");
        sql.append(strValus);
        sql.append(")");

//        result.setID(nextID);
        result.setSQL(sql.toString());
        return result;
    }

    public InsertSQLVO getInsertSQLByHashVO(HashVO vo, String tableName, String idName, String sequenceName,
                                            HashMap _replaceValues, String dataSourceName) throws Exception {
        InsertSQLVO result = new InsertSQLVO();
        StringBuffer strAttributes = new StringBuffer();
        StringBuffer strValus = new StringBuffer();
        HashMap replaceValues = copyMap(_replaceValues);

        String nextID = vo.getStringValue(idName);
        if (sequenceName != null) {
            nextID = getDMO().getSequenceNextValByDS(dataSourceName, sequenceName);
        }
        String key[] = vo.getKeys();
        for (int i = 0; i < key.length; i++) {
            strAttributes.append(key[i]);
            strAttributes.append(",");
            if (key[i].equalsIgnoreCase(idName)) {
                strValus.append(nextID);
                strValus.append(",");
            } else {
                Object valueObj = vo.getObjectValue(key[i]);
                if (replaceValues != null && replaceValues.get(key[i].toUpperCase()) != null) {
                    valueObj = replaceValues.get(key[i].toUpperCase());
                    replaceValues.remove(key[i].toUpperCase());
                }
                appendValue(valueObj, strValus);
            }
        }
        if (replaceValues != null) {
            Iterator it = replaceValues.keySet().iterator();
            while (it.hasNext()) {
                String replaceKey = (String) it.next();
                strAttributes.append(replaceKey);
                strAttributes.append(",");
                appendValue(replaceValues.get(replaceKey), strValus);
            }
        }
        strAttributes.deleteCharAt(strAttributes.length() - 1);
        strValus.deleteCharAt(strValus.length() - 1);

        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO ");
        sql.append(tableName);
        sql.append(" (");
        sql.append(strAttributes);
        sql.append(")");
        sql.append(" VALUES ");
        sql.append("(");
        sql.append(strValus);
        sql.append(")");

        result.setID(nextID);
        result.setSQL(sql.toString());
        return result;
    }

    public String getUpdateSQLByHashVO(HashVO vo, String tableName, String idName) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append( "update " + tableName +" set ");
        String key[] = vo.getKeys();
        for (int i = 0; i < key.length; i++) {
            StringBuffer strValues = new StringBuffer();
            Object valueObj = vo.getObjectValue(key[i]);
            appendValue( valueObj, strValues );
            if (i == key.length - 1)
                sql.append(key[i] + " = "+strValues.toString().replace(',',' '));//最后一个逗号去掉
            else
                sql.append(key[i] + " = "+strValues.toString());
        }
        sql.append(" where id = "+vo.getObjectValue(idName));
        return sql.toString();
    }

    private void appendValue(Object value, StringBuffer s) {
        if (value instanceof String) {
            s.append("'");
            String tmpStr = ( (String) value).replaceAll("'", "''");
            s.append(tmpStr);
            s.append("'");
            s.append(",");
        } else if (value instanceof java.sql.Timestamp) {
            s.append("TO_DATE('");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String str_date = sdf.format(new java.util.Date( ( (java.sql.Timestamp) value).getTime()));
            s.append(str_date);
            s.append("','YYYY-MM-DD HH24:MI:SS')");
            s.append(",");
        } else if (value instanceof java.util.Date) {
            s.append("TO_DATE('");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String str_date = sdf.format((Date)value);
            s.append(str_date);
            s.append("','YYYY-MM-DD HH24:MI:SS')");
            s.append(",");
        }else {
            s.append(value);
            s.append(",");
        }
    }
}
