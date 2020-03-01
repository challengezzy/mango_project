package smartx.framework.common.bs.dbaccess;

import java.util.*;

import java.sql.SQLException;

import smartx.framework.common.bs.NovaDBConnection;
import smartx.framework.common.bs.dbaccess.ifc.DBErrExplainerIFC;

public class DatabaseAdapter implements DatabaseInfo {
    private static DatabaseAdapter self = null;
    private DatabaseInfo dbInfo = null;

    private DatabaseAdapter() {
    }

    public static DatabaseAdapter getInstance(String dsClassName) throws Exception {
        if (self != null) {
            return self;
        }
        self = new DatabaseAdapter();
       if (dsClassName.indexOf("oracle") != -1) {
            self.setDB(new OracleDatabaseInfo());
        } else {
            throw new Exception("the data source " + dsClassName + " is not supported now");
        }
        return self;
    }

    public static DatabaseAdapter getInstance(int dbType) throws Exception {
        if (self != null) {
            return self;
        }
        self = new DatabaseAdapter();
        if (dbType == DBInfoConst.DATABASE_TYPE_ORACLE) {
            self.setDB(new OracleDatabaseInfo());
        } else {
            throw new Exception("the database type " + dbType + " is not supported now");
        }
        return self;
    }

    public static DatabaseAdapter getInstance() {
        return self;
    }

    private void setDB(DatabaseInfo db) {
        this.dbInfo = db;
    }

    public int getDatabaseType() {
        return dbInfo.getDatabaseType();
    }

    public String getJDBCDriverName() {
        return dbInfo.getJDBCDriverName();
    }

    public String getDatabaseURL(String host, String port, String sid) {
        return dbInfo.getDatabaseURL(host, port, sid);
    }

    //Interface functions
    public String to_char(String numFldName) {
        return dbInfo.to_char(numFldName);
    }

    //format 0-普通字符串 1-时间格式(YYYYMMDD HH24:MI:SS) 2-YYYY-MM-DD HH24:MI:SS
    public String to_char(String numFldName, byte format) {
        return dbInfo.to_char(numFldName, format);
    }

    public String to_date(String charVal) {
        return dbInfo.to_date(charVal);
    }

    //format 0-YYYYMMDD HH24:MI:SS,1-YYYY-MM-DD HH24:MI:SS)
    public String to_date(String charVal, byte format) {
        return dbInfo.to_date(charVal, format);
    }

    public String nvl() {
        return dbInfo.nvl();
    }

    public String leftJoin(String rightFld) {
        return dbInfo.leftJoin(rightFld);
    }

    public String rightJoin(String rightFld) {
        return dbInfo.rightJoin(rightFld);
    }

    public String getSysdate() {
        return dbInfo.getSysdate();
    }

    //get Database system datetime
    //flag true-return "SYSDATE" false-return "SELECT SYSDATE....."
    public String getSysdate(boolean flag) {
        return dbInfo.getSysdate(flag);
    }

    public String getSeqNextval(String tabName) {
        return dbInfo.getSeqNextval(tabName);
    }

    public long getIdBySequence(String tabName) {
        return dbInfo.getIdBySequence(tabName);
    }

    public String getBackupCommand() {
        return dbInfo.getBackupCommand();
    }

    public String subString(String chaVal, int start, int len) {
        return dbInfo.subString(chaVal, start, len);
    }

    // date sub
    public String subDate(String toDate, String fromDate) {
        return dbInfo.subDate(toDate, fromDate);
    }

    // date sub
    //format 0-天  1-小时 2-分钟 3-秒 其他-天
    public String subDate(String toDate, String fromDate, byte format) {
        return dbInfo.subDate(toDate, fromDate, format);
    }

    //proccess dual(oracle)
    public String getDual() {
        return dbInfo.getDual();
    }

    //a function to tell whether an error number means an attempt to execute a stored procedure that does not exist
    public boolean isSPNONExistedErr(int errorNum) {
        return dbInfo.isSPNONExistedErr(errorNum);
    }

    public DBErrExplainerIFC getDBErrExplainer() {
        return dbInfo.getDBErrExplainer();
    }

    public Vector getErrDesc(SQLException dbException, NovaDBConnection session) {
        return dbInfo.getErrDesc(dbException,session);
    }
}
