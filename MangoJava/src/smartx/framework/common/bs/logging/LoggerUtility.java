/**************************************************************************
 *
 * $RCSfile: LoggerUtility.java,v $  $Revision: 1.1 $  $Date: 2007/06/18 01:55:42 $
 *
 * $Log: LoggerUtility.java,v $
 * Revision 1.1  2007/06/18 01:55:42  qilin
 * no message
 *
 * Revision 1.2  2007/05/31 07:41:35  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:22:10  qilin
 * no message
 *
 * Revision 1.1  2007/02/27 07:20:41  yuhong
 * MR#:NMBF30-9999 2007/02/27
 * moved from afx
 *
 * Revision 1.1  2007/01/11 12:21:44  john_liu
 * 2006.01.11 by john_liu
 * MR#: BZM10-13
 *
 * Revision
 *
 * created by john_liu, 2007.01.05    for MR#: BZM10-13
 *
 ***************************************************************************/

package smartx.framework.common.bs.logging;

import java.io.*;
import java.util.*;

import org.apache.log4j.*;
import org.apache.log4j.xml.*;

/**
 * <p>Title: Logger工具类</p>
 * <p>Description:  </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Gxlu inc., nm2 </p>
 * @author Jedi H. Zheng
 * @version 1.0
 */
public class LoggerUtility {
    /**
     * 根据Property初始化Log4J
     * @param propFile
     * @throws IOException
     */
    public static void initLog4JProp(String propFile) {
        if (propFile != null) {
            Properties prop = new Properties();

            /** 从当前的classpath读取配置文件信息 */
            ClassLoader cl = LoggerUtility.class.getClassLoader();
            InputStream input = cl.getResourceAsStream(propFile);
            try {
                prop.load(input);
            } catch (IOException e) {
                e.printStackTrace();
                BasicConfigurator.configure();
                return;
            }

            PropertyConfigurator.configure(prop);
        } else {
            BasicConfigurator.configure();
        }
    }

    /**
     * 根据XML初始化Log4J
     * @param xmlFile
     */
    public static void initLog4JXML(String xmlFile) {
        if (xmlFile != null) {
            DOMConfigurator.configure(xmlFile);
        } else {
            BasicConfigurator.configure();
        }
    }
}
