package smartx.system.log.bs;

import java.util.*;

import java.net.URL;

import java.io.File;

import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.utils.StringUtil;
import smartx.framework.common.utils.Sys;
import smartx.framework.common.vo.*;
import smartx.framework.metadata.bs.NovaServerEnvironment;
import smartx.framework.metadata.vo.BillVO;
import smartx.system.common.constant.LogConst;
import smartx.system.common.sysexception.*;

public class LogUtil {
    private static boolean isObjectTypeMapInited=false;
    //the table of object types
    private static Hashtable tObjectType = new Hashtable();

    // 日志状态缓存对象
    private static LogStatusCache mLogStatus = new LogStatusCache();

    private static CommDMO dmo;
    private static CommDMO getDMO() {
        if(dmo==null) {
            dmo=new CommDMO();
        }
        return dmo;
    }
    /**
     * 启用日志
     * @param context
     * @param appModule 模块名
     */
    public static void enableLog() throws NovaException {
    	String appModule=StringUtil.joinArray2String(NovaServerEnvironment.getInstance().getAppModuleName());
        try {
            getDMO().executeUpdateByDS(null, "update MODULEATTRIBUTE set VALUE = " +LogConst.LOGSTATUS_ON+
                                       " where ATTRIBUTE = 'log' and MODULE = '" + appModule + "'");
        } catch (Exception ex) {
            throw NovaExceptionHandler.createNovaEx("启动日志服务失败："+ex.getMessage());
        }
        mLogStatus.setLogStatus(appModule, true);
        new LogWriter().writeLog(LogConst.OPERATION_ENABLE_LOG,null);
    }

    /**
     * 禁用日志
     * @param context
     * @param appModule 模块名
     */
    public static void disableLog() throws NovaException {
    	String appModule=StringUtil.joinArray2String(NovaServerEnvironment.getInstance().getAppModuleName());
        new LogWriter().writeLog(LogConst.OPERATION_DISABLE_LOG,null);
        mLogStatus.setLogStatus(appModule, false);
        try {
            getDMO().executeUpdateByDS(null, "update MODULEATTRIBUTE set VALUE = " +LogConst.LOGSTATUS_OFF+
                                       " where ATTRIBUTE = 'log' and MODULE = '" + appModule + "'");
        } catch (Exception ex) {
            throw NovaExceptionHandler.createNovaEx("关闭日志服务失败："+ex.getMessage());
        }
    }

    public static boolean isLogEnabled() throws NovaException {
    	String appModule=StringUtil.joinArray2String(NovaServerEnvironment.getInstance().getAppModuleName());
        // 先读取本地缓存
        Boolean bCache = mLogStatus.getLogStatus(appModule);
        if (bCache != null)
            return bCache.booleanValue();
        String sql="SELECT * FROM ModuleAttribute WHERE ATTRIBUTE='log' AND MODULE='"+appModule+"'";
        HashVO[] vos = null;
        try {
            vos = getDMO().getHashVoArrayByDS(null, sql);
        } catch (Exception ex) {
        }
        if (vos == null || vos.length==0) {
            return true;
        }

        boolean isEnabled = (vos[0].getIntegerValue("VALUE").intValue() == LogConst.LOGSTATUS_ON);
        mLogStatus.setLogStatus(appModule, isEnabled);

        return isEnabled;
    }

    /**
     * 返回所操作对象的类名
     */
    public static String getObjectType(String tableName) {
        if(tableName==null) return null;
        initObjectTypeMap();

        Object obj = tObjectType.get(tableName);
        if (obj == null)
            return null;
        return obj.toString();
    }

    public static void initObjectTypeMap() {
        if(isObjectTypeMapInited) return;
        isObjectTypeMapInited=true;

        org.jdom.Document logDoc=null;
        
        String str_logFileName = (String)NovaServerEnvironment.getInstance().get("LogServiceConfig");
        str_logFileName=(str_logFileName==null)?"conf/log.xml":str_logFileName;

        try {
            org.jdom.input.SAXBuilder builder = new org.jdom.input.SAXBuilder(); //
            logDoc = builder.build(LogUtil.class.getClassLoader().getResource(str_logFileName)); // 加载XML            
        } catch (Throwable ex) {
            System.out.println("加载日志配置文件[" + str_logFileName + "]失败:"+ex.getMessage()); //
            //ex.printStackTrace();
        }
        if(logDoc==null){
	        try {
	        	String rootpath=(String)NovaServerEnvironment.getInstance().get("NOVA2_SYS_ROOTPATH");
	        	str_logFileName=(rootpath+"WEB-INF/"+str_logFileName).replaceAll("//", "/");
	            logDoc = Sys.loadXML(str_logFileName); // 加载XML            
	        } catch (Throwable ex) {
	            System.out.println("加载日志配置文件[" + str_logFileName + "]失败:"+ex.getMessage()); //
	            //ex.printStackTrace();
	        }
        }

        if (logDoc == null) {
            return;
        }

        System.out.println("\n开始初始化日志文件...");
        try {
            org.jdom.Element objectEntity = logDoc.getRootElement().getChild("objectentity");
            if (objectEntity != null) {
                java.util.List objects = objectEntity.getChildren("object");
                if (objects != null) {
                    for (int i = 0; i < objects.size(); i++) {
                        if (objects.get(i) instanceof org.jdom.Element) {
                            org.jdom.Element objNode = (org.jdom.Element) objects.get(i);
                            if (objNode != null) {
                                String tablename = objNode.getAttributeValue("tablename");
                                String desc = objNode.getAttributeValue("desc");
                                String isenable = objNode.getAttributeValue("isenable");
                                if(isenable.equalsIgnoreCase("Y")) {
                                    putObjectType(tablename, desc);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Throwable tr) {
            tr.printStackTrace();
        }
    }

    private static void putObjectType(String tableName,String desc) {
        tObjectType.put(tableName,desc);
    }

    public static String getTableNameBySql(String sql) {
        String tableName=null;
        if(sql!=null) {
            try {
                StringTokenizer token = new StringTokenizer(sql, " ");
                String type=token.nextToken().trim();
                if(type.equalsIgnoreCase("INSERT")) {
                    token.nextToken();
                    tableName = token.nextToken();
                    int index=tableName.indexOf("(");
                    if(index>0) {
                        tableName=tableName.substring(0,index);
                    }
                    tableName.trim().toUpperCase();
                } else if(type.equalsIgnoreCase("UPDATE")) {
                    tableName = token.nextToken().trim().toUpperCase();
                } else if(type.equalsIgnoreCase("DELETE")) {
                    tableName = token.nextToken().trim().toUpperCase();
                    if(tableName.equals("FROM")) {
                        tableName = token.nextToken().trim().toUpperCase();
                    }
                }
            } catch (Exception ex) {
            }
        }
        return tableName;
    }

    public static String getSqlType(String sql) {
        if(sql!=null) {
            StringTokenizer token=new StringTokenizer(sql," ");
            if(token.hasMoreTokens()) {
                String type=token.nextToken().trim();
                if(type.equalsIgnoreCase("INSERT")) {
                    return LogConst.OPERATION_ADD;
                } else if(type.equalsIgnoreCase("UPDATE")) {
                    return LogConst.OPERATION_UPDATE;
                } else if(type.equalsIgnoreCase("DELETE")) {
                    return LogConst.OPERATION_DELETE;
                }
            }
        }
        return "";
    }

}

/**
 * 日志状态缓存类
 * @author Brook
 *
 * title: LogUtil.java
 * date:  2005-7-6
 */
class LogStatusCache {
    /*
     * 日志状态有效时间
     *
     * 经常出现的情况是在很短的时间内出现大量查询日志状态的操作，而日志
     * 状态很少变化，所以在一定时间间隔内可以认为状态不变，而不用反复查库
     */
    private final long TIME_INTERVAL = 30 * 1000; // 30秒

    private HashMap statusMap = new HashMap(); // 日志状态	key: appModule, value: Boolean
    private HashMap timeMap = new HashMap(); // 日志状态时间信息 key: appModule, value: last visit time

    /**
     * 返回日志状态
     * @param appModule	子系统名称
     * @return True、False or null
     */
    synchronized Boolean getLogStatus(String appModule) {
        Long nt0 = (Long) timeMap.get(appModule);
        if (nt0 == null)
            return null;

        // 时间过期
        if (System.currentTimeMillis() - nt0.longValue() > TIME_INTERVAL)
            return null;

        return (Boolean) statusMap.get(appModule);
    }

    /**
     * 设置日志状态
     * @param appModule 子系统名称
     * @param isEnabled 日志是否可用
     */
    synchronized void setLogStatus(String appModule, boolean isEnabled) {
        statusMap.put(appModule, Boolean.valueOf(isEnabled));
        timeMap.put(appModule, new Long(System.currentTimeMillis()));
    }
}
