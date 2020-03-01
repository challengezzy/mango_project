package smartx.framework.common.utils;

import com.ice.jni.registry.Registry;
import com.ice.jni.registry.RegistryKey;


/**
 * 与Windows相关的组件处理
 * @author James.W
 *
 */
public class WindowsCOM {
	private WindowsCOM(){}
	
	
	/**
     * 获得Excel的安装路径
     * 依赖Windows实现
     * @return Excel的安装路径
     * @throws Exception
     * 
     */
    public static String getExcelExePath() throws Exception {
        String str_1 = null;
        try {
            RegistryKey note_1 = Registry.HKEY_LOCAL_MACHINE.openSubKey("SOFTWARE\\Classes\\Excel.Application\\CLSID");
            str_1 = note_1.getDefaultValue();
        } catch (Exception e) {
            //e.printStackTrace();
            throw new Exception("可能没装Excel");
        }

        try {
            RegistryKey node_2 = Registry.HKEY_LOCAL_MACHINE.openSubKey("SOFTWARE\\Classes\\CLSID\\"+str_1+"\\LocalServer32"); //
            String str_2 = node_2.getDefaultValue(); //
            return str_2;
        } catch (Exception e) {
            //e.printStackTrace();
            throw new Exception("可能该操作系统不是Windows2000/XP/2003,对应位置找不到配置!");
        }
    }
    
    /**
     * 创建并打开Excel编辑
     * 依赖FrameWorkTBUtil.getExcelExePath()
     * @throws Exception
     */
    public static void invokeExcel(String xlspath)throws Exception {
        try {
            String str_path = getExcelExePath();
            if (str_path != null) {
                String str_command = str_path + " " + "\"" + xlspath + "\""; //
                if(Sys.isDebug) System.out.println(str_command); //
                Runtime.getRuntime().exec(str_command); // 调用Excel
            }
        } catch (Exception e) {
        	throw new Exception("调用Excel来查看导出数据出现异常！",e);            
        } 
    }
    
    
    
    
    
    
    public static void main(String[] args)throws Exception{
    	//Excel的安装路径
    	System.out.println("Excel的安装路径 ："+WindowsCOM.getExcelExePath());
    	
    	
    	
    }
    
}
