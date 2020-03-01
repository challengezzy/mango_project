package smartx.framework.common.bs.dbaccess;

import smartx.framework.common.bs.dbaccess.ifc.DBErrExplainerIFC;
import smartx.framework.metadata.bs.DBExplainerCache;

import java.util.*;
import java.sql.SQLException;

public class DBErrExplainer implements DBErrExplainerIFC {
    /**
     * the first element of errInfo is error code
     * the section element of errInfo is table name
     * the third or later element of errInfo is column name
     * @throws WWFException
     */
    public SQLException resolve(Vector errInfo) throws Exception {
        if (errInfo == null || errInfo.size() < 3)return null;
        int errCode = ( (Integer) errInfo.elementAt(0)).intValue();
        errInfo.remove(0);
        Vector errInd = errInfo;
//         if (errInd == null || errInd.size() == 0) return null;
        SQLException se ;
        String info = null;

//table name
        String table_en = (String) errInd.firstElement();
        String table_cn = (String) DBExplainerCache.getInstance().getTableLocalName(table_en);
        if (table_cn == null) table_cn = table_en; ;

//column name
        if (errCode == DBInfoConst.DATABASE_ERROR_CODE_UNIQUE_CONSTRAINT) {
            String[] columns = new String[errInd.size() - 1];
            for (int i = 0; i < errInd.size() - 1; i++) {
                String c_en = (String) errInd.elementAt(i + 1);
                String c_cn = (String) DBExplainerCache.getInstance().getColumnLocalName(table_en,c_en);
                if (c_cn == null) c_cn = c_en;
                columns[i] = c_cn;
            }
            info = createDetailInfo(table_cn, columns);
            se= new SQLException(info);
        } else if (errCode == DBInfoConst.DATABASE_ERROR_CODE_FOREIGNKEY_CONSTRAINT) {
            String column_en = (String) errInd.elementAt(1);
            String column_cn = (String) DBExplainerCache.getInstance().getColumnLocalName(table_en,column_en);
            if (column_cn == null) column_cn = column_en;
            info = createDetailInfo(table_cn, column_cn);
            se= new SQLException(info);
        } else throw new Error("error in DBErrExplainer: error Code is not " +
                               "unique constraint or foreign key constraint");
        return se;

    }

    private String createDetailInfo(String table, String[] columns) {
        if (columns.length == 1) {
            return "原因：" + table + "的" + columns[0] + "必须唯一。";
        }
        //return "数据库唯一性约束冲突：" + table + "的" + columns[0] + "必须唯一。";
        else {
            //String info = "数据库唯一性约束冲突：以下组合，" + table + "的" + columns[0];
            String info = "原因：以下组合，" + table + "的" + columns[0];
            for (int i = 1; i < columns.length - 1; i++) {
                info += "、" + columns[i];
            }
            info += "和" + columns[columns.length - 1] + "，必须唯一。";
            return info;
        }
    }

    private String createDetailInfo(String table, String column) {
        //return "数据库完整性约束冲突：欲删除的" + column + "正被某个" + table + "所引用。";
        return "原因：欲删除的" + column + "正被某个" + table + "所引用。";
    }

}
