/**
 * 
 */
package smartx.bam.utils.epl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;

/**
 * @author sky
 * Description 
 */
public class BamEplUtil
{
	public static Date parseDate(String dateStr) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.parse( dateStr );
	}
	
	public static long dateConvertToLong(Date date){
		return date.getTime();
	}
	
	public static Date longConvertToDate(long date){
		return new Date(date);
	}
	
	public static long bigDecimalConvertToLong(BigDecimal date){
		return date.longValue();
	}
	
	public static Double bigDecimalDivide(BigDecimal b1,BigDecimal b2){
		return b1.divide(b2,BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	public static String getStatementText(String statementName,String providerName){
		EPServiceProvider epService = EPServiceProviderManager.getProvider(providerName);
		EPStatement statement = epService.getEPAdministrator().getStatement(statementName);
		if(statement != null){
			return statement.getText();
		}
		return "";
	}
}
