package smartx.framework.common.utils;

public class ExceptionUtil {

	public static String getStackTraceMessage(Exception e){
		StringBuffer sbf = new StringBuffer(); 
		sbf.append(e.getMessage()).append("\r\n");
        StackTraceElement[] stackItems = e.getStackTrace();
        for (int i = 0; i < stackItems.length; i++) {
        	sbf.append(stackItems[i]).append("\r\n");        	
        }
        return sbf.toString();        
	}
	
}
