/**************************************************************************
 *
 * $RCSfile: Log.java,v $  $Revision: 1.1 $  $Date: 2007/06/18 01:55:42 $
 *
 * $Log: Log.java,v $
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

/**
 * <p>Title: log类</p>
 * <p>Description:  </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Gxlu inc., nm2 </p>
 * @author Jedi H. Zheng
 * @version 1.0
 */
public class Log implements Serializable {
//    public final static String defaultLogConfigFile      = "Log4j.properties";

    /** Log Category */
    public final static String LogCategory = "Work";
    public final static String FileAppender = "R_Work";
    public final static String ConsoleAppender = "stdout";

    /**
     * appender 列表
     */
    private static List logAppenders = new ArrayList();

    /**
     * 是否继承上级logger
     */
    private static boolean additivity = false;

    /**
     * 是否已经初始化
     */
    private static boolean isInitial = false;

    private Logger logger = null;

    /**
     * 初始化
     */
//    public static void init()
//    {
//        init(defaultLogConfigFile);
//    }

    /**
     * 初始化
     * @param propertyFilePath
     */
    public static void init(String propertyFilePath) {
        if (isInitial == true) {
            return;
        }

        System.out.println("日志初始化...");

        LoggerUtility.initLog4JProp(propertyFilePath);

        org.apache.log4j.Logger _logger = org.apache.log4j.Logger.getLogger(LogCategory);
        if (_logger == null) {
            return;
        }

        additivity = _logger.getAdditivity();

        Appender ap = _logger.getAppender(FileAppender);
        if (ap != null) {
            logAppenders.add(ap);
        }

        ap = _logger.getAppender(ConsoleAppender);
        if (ap != null) {
            logAppenders.add(ap);
        }

        isInitial = true;
    }

    public static Log getLogger(Class clazz) {
        return new Log(clazz);
    }

    public static Log getDefaultLogger() {
        return new Log(Log.class);
    }

    public Logger getLogger() {
        return logger;
    }

    private Log(Class clazz) {
        logger = org.apache.log4j.Logger.getLogger(clazz);

        logger.setAdditivity(additivity);

        Iterator iter = logAppenders.iterator();
        for (; iter.hasNext(); ) {
            logger.addAppender( (Appender) iter.next());
        }
    }

    public void info(String msg) {
        logger.info(msg);
    }

    public void info(Exception ex) {
        logger.info(ex.getMessage(), ex);
    }

    public void info(Exception ex, String msg) {
        logger.info(msg, ex);
    }

    public void info(Throwable ex) {
        logger.info(ex.getMessage(), ex);
    }

    public void warn(String msg) {
        logger.warn(msg);
    }

    public void warn(Exception ex) {
        logger.warn(ex.getMessage(), ex);
    }

    public void warn(Exception ex, String msg) {
        logger.warn(msg, ex);
    }

    public void warn(Throwable ex) {
        logger.warn(ex.getMessage(), ex);
    }

    public void debug(String msg) {
        if (logger.isDebugEnabled()) {
            logger.debug(msg);
        }
    }

    public void debug(Exception ex) {
        if (logger.isDebugEnabled()) {
            logger.debug(ex.getMessage(), ex);
        }
    }

    public void debug(Exception ex, String msg) {
        if (logger.isDebugEnabled()) {
            logger.debug(msg, ex);
        }
    }

    public void debug(Throwable ex) {
        logger.debug(ex.getMessage(), ex);
    }

    public void error(String msg) {
        logger.error(msg, new Throwable(msg));
    }

    public void error(Exception ex) {
        logger.error(ex.getMessage(), ex);
    }

    public void error(Throwable ex) {
        logger.error(ex.getMessage(), ex);
    }

    public void error(Exception ex, String msg) {
        logger.error(msg, ex);
    }

    public void fatal(String msg) {
        logger.fatal(msg, new Throwable(msg));
    }

    public void fatal(Exception ex) {
        logger.fatal(ex.getMessage(), ex);
    }

    public void fatal(Throwable ex) {
        logger.fatal(ex.getMessage(), ex);
    }

    public void fatal(Exception ex, String msg) {
        logger.fatal(msg, ex);
    }

    public boolean isDebug() {
        return logger.isDebugEnabled();
    }

    public boolean isEnabledFor(Priority pri) {
        return logger.isEnabledFor(pri);
    }

}

/**
 # Log4j Properties

 ##
 ## CategoryFactory for COTS application
 ##
 log4j.rootLogger=DEBUG,stdout

 log4j.appender.stdout=org.apache.log4j.ConsoleAppender
 log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
 log4j.appender.stdout.layout.ConversionPattern=%d{HH:mm:ss.SSS} %5p [%t] %c{1} - %m%n

 ### The server.log file appender
 #log4j.appender.Default=org.apache.log4j.DailyRollingFileAppender
 #log4j.appender.Default.DatePattern='.'yyyy-MM-dd
 log4j.appender.Default.File=default.log
 log4j.appender.Default.layout=org.apache.log4j.PatternLayout
 log4j.appender.Default.layout.ConversionPattern=%d{HH:mm:ss.SSS} %5p %c{1} - %m%n

 log4j.appender.Default=org.apache.log4j.RollingFileAppender
 log4j.appender.Default.MaxFileSize=10000KB
 log4j.appender.Default.MaxBackupIndex=5
 log4j.appender.Default.Append=false

 ###
 ## work log
 ###
 log4j.logger.Work=DEBUG,stdout,R_Work
 log4j.additivity.Work=false

 #### Second appender writes to a file
 #log4j.appender.R_Work=org.apache.log4j.RollingFileAppender
 log4j.appender.R_Work = org.apache.log4j.DailyRollingFileAppender
 log4j.appender.R_Work.File = server
 log4j.appender.R_Work.DatePattern = '.'yyyy-MM-dd'.log'
 log4j.appender.R_Work.layout=org.apache.log4j.PatternLayout
 log4j.appender.R_Work.layout.ConversionPattern=%d{HH:mm:ss.SSS} %5p %c{1} - %m%n
 #log4j.appender.R_Work.MaxFileSize=2048KB
 #log4j.appender.R_Work.MaxBackupIndex=10
 log4j.appender.R_Work.Append=true
 */
