/**************************************************************************
 * $RCSfile: DBException.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:16 $
 **************************************************************************/

package smartx.framework.common.bs;

import smartx.framework.metadata.bs.*;

public class DBException {

    private static DBException dbException = null;

    public static DBException getInstance() {
        if (dbException != null) {
            return dbException;
        }
        dbException = new DBException();
        return dbException;
    }

    public String getExceptionMsg(String _tablename, String _pkname, String _exception) {
        String str_msg = _exception;
        if (str_msg.indexOf("ORA-00001") >= 0) {
            String tablename = _tablename;
            String pkname = _pkname;
            if (DBExplainerCache.getInstance().getTableLocalName(_tablename) != null) {
                tablename = DBExplainerCache.getInstance().getTableLocalName(_tablename);
                if (DBExplainerCache.getInstance().getColumnLocalName(_tablename, _pkname) != null) {
                    pkname = DBExplainerCache.getInstance().getColumnLocalName(_tablename, _pkname);
                }
            }
            return "违反数据库表[" + tablename + "]的主键[" + pkname + "]的唯一性约束";
        } else if (str_msg.indexOf("ORA-00942") >= 0) {
            return "数据表或视图不存在";
        } else if (str_msg.indexOf("ORA-01688") >= 0) {
            String[] str_errors = _exception.split(" ");
            String str_table = "";
            if (str_errors.length == 0) {
                return "数据库操作出现未知错误" + _exception;
            } else {
                str_table = _tablename;
                if (DBExplainerCache.getInstance().getTableLocalName(_tablename) != null) {
                    str_table = DBExplainerCache.getInstance().getTableLocalName(_tablename);
                }
                return "指定的表空间[" + str_table + "]已经被占满，无法进行再操作";
            }
        } else if (str_msg.indexOf("ORA-03113") >= 0) {
            return "通讯不正常结束，通讯通道终止";
        } else if (str_msg.indexOf("ORA-01598") >= 0) {
            return "当前使用的回滚段为[not online]";
        } else if (str_msg.indexOf("ORA-01400") >= 0) {
            int li_begin = str_msg.indexOf("(");
            int li_end = str_msg.indexOf(")");
            String str_table = "";
            String str_col = "";
            if (li_begin >= 0 && li_end > li_begin) {
                str_table = _tablename;
                String[] str_errors = str_msg.substring(li_begin + 1, li_end).split("\"");
                if (str_errors != null && str_errors.length > 0) {
                    str_col = str_errors[str_errors.length - 1];
                }
                if (DBExplainerCache.getInstance().getTableLocalName(_tablename) != null) {
                    str_table = DBExplainerCache.getInstance().getTableLocalName(_tablename);
                }
                if (DBExplainerCache.getInstance().getColumnLocalName(str_table, str_col) != null) {
                    str_col = DBExplainerCache.getInstance().getColumnLocalName(_tablename, str_col);
                }
                return "数据表[" + str_table + "]" + "的列[" + str_col + "]不能为空值";
            }
        } else if ( (str_msg.indexOf("ORA-00904") >= 0)) {
            String[] str_errors = str_msg.split(":");
            String str_col = "";
            if (str_errors != null && str_errors.length > 0) {
                for (int i = 0; i < str_errors.length; i++) {
                    if (str_errors[i].indexOf("\"") >= 0) {
                        str_errors[i] = str_errors[i].trim();
                        str_col = str_errors[i].substring(1, str_errors[i].length() - 1);
                        return "数据表中没有字段[" + str_col + "]";
                    }
                }
            }
        }
        return "出现未知异常:" + _exception;
    }
}
/*******************************************************************************
 * $RCSfile: DBException.java,v $ $Revision: 1.2 $ $Date: 2007/05/31 07:38:16 $
 *
 * $Log: DBException.java,v $
 * Revision 1.2  2007/05/31 07:38:16  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 05:56:15  qilin
 * no message
 *
 * Revision 1.2  2007/02/06 01:41:54  lujian
 * *** empty log message ***
 * Revision 1.1 2007/02/05 04:39:51 lujian *** empty
 * log message ***
 *
 *
 ******************************************************************************/
