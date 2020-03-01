package smartx.publics.form.bs.utils;

import org.apache.commons.lang.StringUtils;

public class StringUtil {
	/**
	 * sql转义，'转换成''，""或null转换成"null"
	 * @param value
	 * @return
	 */
	public static String strToSQLValue(String value){
		if(value == null || "".equals(value)||"null".equalsIgnoreCase(value.trim()))
			return "null";
		return "'"+StringUtils.replace(value, "'", "''")+"'";
	}
}
