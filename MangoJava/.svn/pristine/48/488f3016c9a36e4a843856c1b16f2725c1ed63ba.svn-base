package smartx.framework.common.bs.dbaccess;

import java.io.*;
import java.util.*;

import java.sql.SQLException;
import java.sql.ResultSet;

import smartx.framework.common.bs.NovaDBConnection;
import smartx.framework.common.bs.dbaccess.ifc.DBErrExplainerIFC;

public class OracleDatabaseInfo implements DatabaseInfo {

	public int getDatabaseType() {
		return DBInfoConst.DATABASE_TYPE_ORACLE;
	}

	public String getJDBCDriverName() {
		return "oracle.jdbc.driver.OracleDriver";
	}

	public String getDatabaseURL(String host, String port, String sid) {
		return "jdbc:oracle:thin:@" + host + ":" + port + ":" + sid;
	}

	public String to_char(String numFldName) {
		return "to_char(" + numFldName + ")";
	}

	// format 0-时间格式(YYYYMMDD HH24:MI:SS) 2-YYYY-MM-DD HH24:MI:SS 其他-普通字符串
	public String to_char(String numFldName, byte format) {
		if (format == 0) {
			return "to_char(" + numFldName + ",'YYYYMMDD HH24:MI:SS')";
		} else if (format == 1) {
			return "to_char(" + numFldName + ",'YYYY-MM-DD HH24:MI:SS')";
		} else {
			return "to_char(" + numFldName + ")";
		}
	}

	public String to_date(String charVal) {
		return "to_date('" + charVal + " ', 'YYYYMMDD HH24:MI:SS')";
	}

	// format 0-YYYYMMDD HH24:MI:SS,1- YYYY-MM-DD HH24:MI:SS)
	public String to_date(String charVal, byte format) {
		if (format == 0) {
			return "to_date('" + charVal + " ', 'YYYYMMDD HH24:MI:SS')";
		} else if (format == 1) {
			return "to_date('" + charVal + " ', 'YYYY-MM-DD HH24:MI:SS')";
		} else {
			return "to_date('" + charVal + " ', 'YYYYMMDD HH24:MI:SS')";
		}

	}

	public String nvl() {
		return "nvl";
	}

	public String leftJoin(String rightFld) {
		return " = " + rightFld + "(+)";
	}

	public String rightJoin(String rightFld) {
		return "(+) =" + rightFld;
	}

	public String getSysdate() {
		return "select SYSDATE from dual";
	}

	public String getSysdate(boolean flag) {
		if (flag) {
			return "SYSDATE";
		} else {
			return "select SYSDATE from dual";
		}
	}

	public String getSeqNextval(String tabName) {
		return "S_" + tabName + ".nextval ";
	}

	public long getIdBySequence(String tabName) {
		long lIdVal = -1;

//		CommonContext context = null;
//		try {
//			context = new CommonContext();
//			Session session = context.getSession();
//			Vector values = session.executeSQL("select s_" + tabName
//					+ ".nextval IDVAL from dual");
//			if (values == null || values.size() == 0) {
//				return 0;
//			}
//			DatabaseRow row = (DatabaseRow) values.elementAt(0); // Only one row
//			Object obj = row.get("IDVAL");
//			lIdVal = new Long(obj.toString()).longValue();
//		} catch (Exception e) {
//			e.printStackTrace();
//			return -1;
//		} finally {
//			if (context != null) {
//				context.release();
//			}
//		}

		return lIdVal;
	}

	public Vector getErrDesc(SQLException dbException, NovaDBConnection session) {
		String exceptionMessage = dbException.getMessage();
		int errCode = dbException.getErrorCode();
		if ((errCode != 1) && (errCode != 2292) && (errCode != 2091)) {
			if (exceptionMessage.indexOf("ORA-00001") > 0) {
				errCode = 1;
			} else if (exceptionMessage.indexOf("ORA-2292") > 0) {
				errCode = 2292;
			} else if (exceptionMessage.indexOf("ORA-2091") > 0) {
				errCode = 2091;
			} else {
				System.out
						.println("such exception is not processed here! dberrcode is : "
								+ dbException.getErrorCode());
				return null;
			}
		}
		String constraintName;
		int beginIndex;
		int dotIndex;
		int endIndex;
		beginIndex = exceptionMessage.indexOf("constraint");
		beginIndex = exceptionMessage.indexOf('(', beginIndex);
		endIndex = exceptionMessage.indexOf(')', beginIndex);

		String bracketStr = exceptionMessage
				.substring(beginIndex + 1, endIndex);
		dotIndex = bracketStr.lastIndexOf('.');
		// dotIndex = exceptionMessage.indexOf('.', beginIndex);
		constraintName = bracketStr
				.substring(dotIndex + 1, bracketStr.length());
		// System.out.println("owner is :" + owner + " and constraintname is :"
		// + constraintName );
		ResultSet rs = null;
		try {

			if (errCode == 1) {
				// unique constraint violated

				rs = session.createStatement().executeQuery(
						"select table_name,column_name "
								+ "from user_ind_columns where index_name ='"
								+ constraintName + "'");
			} else if (errCode == 2292 || errCode == 2091) {
				// foreign key constraint violated - 2292
				// unique constraint(deferrable deferred) violated - 2091
				rs = session
						.createStatement()
						.executeQuery(
								"select table_name,column_name "
										+ "from user_cons_columns where constraint_name ='"
										+ constraintName + "'");
			}
			Vector result = new Vector();

			if (rs.next()) {
				if (errCode == 1 || errCode == 2091) {
					result.add(new Integer(
							DBInfoConst.DATABASE_ERROR_CODE_UNIQUE_CONSTRAINT));
				} else {
					result.add(new Integer(
							DBInfoConst.DATABASE_ERROR_CODE_FOREIGNKEY_CONSTRAINT));
				}

				String elementName = rs.getString("TABLE_NAME");
				result.addElement(elementName);
				elementName = rs.getString("COLUMN_NAME");
				result.addElement(elementName);
				while (rs.next()) {
					elementName = rs.getString("COLUMN_NAME");
					result.addElement(elementName);
				}
			}
			return result;
		} catch (Exception e) {
			return null;
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
			}
		}
	}

	public String getBackupCommand() {
		String command;
		if (File.separator.compareTo("/") == 0) {
			command = "sh ./exp_schema.sh ";
		} else {
			command = "cmd /c exp_schema.bat ";
		}
		return command;
	}

	//
	public String subString(String chaVal, int start, int len) {
		return "substr(" + chaVal + "," + Integer.toString(start) + ","
				+ Integer.toString(len) + ")";
	}

	// date sub
	public String subDate(String toDate, String fromDate) {
		return toDate + "-" + fromDate;
	}

	// date sub
	// format 0-天 1-小时 2-分钟 3-秒 其他-天
	public String subDate(String toDate, String fromDate, byte format) {
		if (format == 0) {
			return toDate + "-" + fromDate;
		} else if (format == 1) {
			return "(" + toDate + "-" + fromDate + ")*24";
		} else if (format == 2) {
			return "(" + toDate + "-" + fromDate + ")*24*60";
		} else if (format == 3) {
			return "(" + toDate + "-" + fromDate + ")*24*60*60";
		} else {
			return toDate + "-" + fromDate;
		}
	}

	// proccess dual(oracle)
	public String getDual() {
		return "from dual";
	}

	public boolean isSPNONExistedErr(int errorNum) {
		if (errorNum == 6550) {
			return true;
		} else {
			return false;
		}

	}

	private DBErrExplainer dbErrExplainer;

	public DBErrExplainerIFC getDBErrExplainer() {
		if (dbErrExplainer == null) {
			dbErrExplainer = new DBErrExplainer();
		}
		return dbErrExplainer;
	}
}
