package smartx.framework.common.bs.dbaccess;

import java.util.*;

import java.sql.SQLException;

import smartx.framework.common.bs.NovaDBConnection;
import smartx.framework.common.bs.dbaccess.ifc.DBErrExplainerIFC;

public interface DatabaseInfo {
	// what database using
	public int getDatabaseType();

	// the class full name of jdbc driver
	public String getJDBCDriverName();

	// database URL
	public String getDatabaseURL(String host, String port, String sid);

	// numeric field: numFldNmae change to Character String
	public String to_char(String numFldName);

	// numeric field: numFldNmae change to Character String
	// format 1-时间格式(YYYYMMDD HH24:MI:SS) 2-YYYY-MM-DD HH24:MI:SS else-普通字符串
	public String to_char(String numFldName, byte format);

	// character changet to Date (default YYYYMMDD HH24:MI:SS)
	public String to_date(String charVal);

	// character changet to Date
	// format 0-YYYYMMDD HH24:MI:SS,1-YYYY-MM-DD HH24:MI:SS else-YYYYMMDD
	// HH24:MI:SS
	public String to_date(String charVal, byte format);

	// nvl(expr1, expr2): If expr1 is null, returns expr2; if expr1 is not null,
	// returns expr1
	public String nvl();

	// Left Join: (1)Oralce: a = b(+); (2)Sybase: a *= b
	public String leftJoin(String rightFld);

	// Right Join: (1)Oralce: a(+) = b; (2)Sybase: a =* b
	public String rightJoin(String rightFld);

	// get Database system datetime
	public String getSysdate();

	// get Database system datetime
	// flag true-return "SYSDATE" false-return "SELECT SYSDATE....."
	public String getSysdate(boolean flag);

	// get a String of How to get sequence nextval, used in
	// INSERT(S_SEQ.NEXTVAL)
	public String getSeqNextval(String tabName);

	// get sequence NEXTVAL(numeric)
	public long getIdBySequence(String tabName);

	// get backup command
	public String getBackupCommand();

	// substring
	public String subString(String chaVal, int start, int len);

	// date sub
	public String subDate(String toDate, String fromDate);

	// date sub
	// format 0-天 1-小时 2-分钟 3-秒 其他-天
	public String subDate(String toDate, String fromDate, byte format);

	// proccess dual(oracle)
	public String getDual();

	// a function to tell whether an error number means an attempt to execute a
	// stored procedure that does not exist
	public boolean isSPNONExistedErr(int errorNum);

	public DBErrExplainerIFC getDBErrExplainer();

	public Vector getErrDesc(SQLException dbException, NovaDBConnection session);

}
