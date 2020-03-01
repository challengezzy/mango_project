/**************************************************************************
 * $RCSfile: FrameWorkCommService.java,v $  $Revision: 1.6.2.5 $  $Date: 2009/01/31 12:40:40 $
 **************************************************************************/
package smartx.framework.common.ui;

import java.util.*;

import smartx.framework.common.vo.*;
import smartx.framework.metadata.vo.*;


/**
 * 平台通用服务,基本上都是查询数据库存,保存数据库,执行SQL,存储过程等!!
 * @author user
 */
public interface FrameWorkCommService extends NovaRemoteCallServiceIfc {

    /**
     * 通用方法!!!通过该方法,只要直接在服务器端写一个类就行了,也就是连在接口中增加方法都不要干了!!!!!
     * @param _className
     * @param _functionName
     * @param _parMap
     * @return
     * @throws NovaRemoteException
     * @throws Exception
     */
    public HashMap commMethod(String _className, String _functionName, HashMap _parMap) throws Exception; //会抛远程调用异常

    /**
     * 取得项目名称!!
     * @return
     * @throws NovaRemoteException
     * @throws Exception
     */
    public String getProjectName() throws Exception;

    /**
     * 获得用户关联子系统名称
     * @param uid
     * @return
     */
    public String getUserAppModule(String uid)throws Exception;
    
    /**
     * 获得平台启动子系统名称列表
     * @return String[]
     */
    public String[] getAppModule()throws Exception ;
    
    /**
     * 取得server端环境参数，使参数的获得封装为一次远程访问
     * @return HashMap
     * @throws Exception
     */
    public HashMap getEnvironmentParam() throws Exception;
    /**
     * 系统配置参数原始值获取接口
     * @param param
     * @return
     * @throws Exception
     */
    public String getEnvironmentParamStr(String param) throws Exception;

    /**
     * 系统配置参数原始值获取接口
     * @param param 参数名
     * @return Object 返回服务器端对象
     * @throws Exception
     */
    public Object getEnvironmentParamObject(String param) throws Exception;
    
    /**
     * 取得自定义登录逻辑实现类
     * @return
     * @throws NovaRemoteException
     * @throws Exception
     */
    public String getLoginServiceName() throws Exception; //取得登录服务的实现类,如果为null则用默认的登录逻辑

    /**
     * 获得所有图标文件名
     * TODO 需要看看实现
     * @return
     * @throws Exception
     */
    public String[] getImageFileNames() throws Exception;

    /**
     * 取得默认数据源
     * @return
     * @throws NovaRemoteException
     * @throws Exception
     */
    public String getDeaultDataSource() throws Exception; //取得默认数据源

    /**
     * 返回某个序列下一个值
     * @param _datasourcename 
     * @param _sequenceName
     * @return
     * @throws Exception
     */
    public String getSequenceNextValByDS(String _datasourcename, String _sequenceName) throws Exception;

    /**
     * 检索sql返回二维数组
     * @param _datasourcename
     * @param _sql
     * @return
     * @throws Exception
     */
    public String[][] getStringArrayByDS(String _datasourcename, String _sql) throws Exception;

    /**
     * 检索sql返回表结构
     * @param _datasourcename
     * @param _sql
     * @return
     * @throws Exception
     */
    public TableDataStruct getTableDataStructByDS(String _datasourcename, String _sql) throws Exception;

    /**
     * 检索sql返回HashVO[]
     * @param _datasourcename
     * @param _sql
     * @return
     * @throws Exception
     */
    public HashVO[] getHashVoArrayByDS(String _datasourcename, String _sql) throws Exception;

    /**
     * 送一组SQL,返回Vector
     * @param _datasourcename
     * @param _sqls
     * @return
     * @throws Exception
     */
    public Vector getHashVoArrayReturnVectorByDS(String _datasourcename, String[] _sqls) throws Exception; //送入一组SQL,Vector中每一项都是一个HashVO[]

    /**
     * 送一组SQL,返回HashMap
     * @param _datasourcename
     * @param _sqls
     * @param _keys
     * @return
     * @throws Exception
     */
    public HashMap getHashVoArrayReturnMapByDS(String _datasourcename, String[] _sqls, String[] _keys) throws Exception; //送一组SQL返回HashMap,Keys对应SQLs位置，是返回HashMap中的key

    /**
     * 执行一条SQL
     * @param _datasourcename
     * @param _sql
     * @return int 
     * @throws Exception
     */
    public int executeUpdateByDS(String _datasourcename, String _sql) throws Exception; //在指定数据源上,执行一条数据库修改语句,比如insert,delete,update

    /**
     * 执行一批SQL
     * @param _datasourcename
     * @param _sqls
     * @return int[]
     * @throws Exception
     */
    public int[] executeBatchByDS(String _datasourcename, String[] _sqls) throws Exception; //在指定数据源上,执行一批数据库修改语句,比如insert,delete,update

    /**
     * 执行一批SQL
     * @param _datasourcename
     * @param _sqllist
     * @return int[]
     * @throws Exception
     */
    public int[] executeBatchByDS(String _datasourcename, java.util.List _sqllist) throws Exception; //在指定数据源上,执行一批数据库修改语句,比如insert,delete,update

    /**
     * 存储过程,没有返回值
     * @param _datasourcename
     * @param procedureName
     * @param parmeters
     * @throws Exception
     */
    public void callProcedureByDS(String _datasourcename, String procedureName, String[] parmeters) throws Exception;

    /**
     * 存储过程,返回String!
     * @param _datasourcename
     * @param procedureName
     * @param parmeters
     * @return
     * @throws Exception
     */
    public String callProcedureReturnStrByDS(String _datasourcename, String procedureName, String[] parmeters) throws
        Exception;

    /**
     * 存储函数,返回String
     * @param _datasourcename
     * @param functionName
     * @param parmeters
     * @return
     * @throws Exception
     */
    public String callFunctionReturnStrByDS(String _datasourcename, String functionName, String[] parmeters) throws
        Exception;

    /**
     * 存储函数,返回二维结构!
     * @param _datasourcename
     * @param functionName
     * @param parmeters
     * @return
     * @throws Exception
     */
    public String[][] callFunctionReturnTableByDS(String _datasourcename, String functionName, String[] parmeters) throws
        Exception;

    /**
     * 上传文件!!
     * @param _code
     * @param tablename
     * @param fieldname
     * @param _vo
     * @return
     * @throws Exception
     */
    public HashMap uploadFileFromClient(String _code, String tablename, String fieldname, ClassFileVO _vo) throws
        Exception; //

    /**
     * 下载文件!!
     * @param _filename
     * @return
     * @throws Exception
     */
    public ClassFileVO downloadToClient(String _filename) throws Exception;
    
	/**
	 * 根据Afx的JNDI名字取得对应的接口名字.
	 * TODO 什么意思？什么目的？
	 * @param _afxJNDIContextName
	 * @return
	 * @throws Exception
	 */
	public String getAfxInterfaceName(String _afxJNDIContextName) throws Exception;
}

/**************************************************************************
 * $RCSfile: FrameWorkCommService.java,v $  $Revision: 1.6.2.5 $  $Date: 2009/01/31 12:40:40 $
 *
 * $Log: FrameWorkCommService.java,v $
 * Revision 1.6.2.5  2009/01/31 12:40:40  wangqi
 * *** empty log message ***
 *
 * Revision 1.6.2.4  2008/11/25 10:26:17  wangqi
 * *** empty log message ***
 *
 * Revision 1.6.2.3  2008/10/14 18:16:03  wangqi
 * *** empty log message ***
 *
 * Revision 1.6.2.2  2008/03/27 05:52:04  wangqi
 * *** empty log message ***
 *
 * Revision 1.7  2008/01/29 09:38:02  wangqi
 * *** empty log message ***
 *
 * Revision 1.6  2008/01/08 00:46:39  wangqi
 * *** empty log message ***
 *
 * Revision 1.5  2008/01/04 08:44:48  wangqi
 * *** empty log message ***
 *
 * Revision 1.4  2008/01/04 08:34:10  wangqi
 * *** empty log message ***
 *
 * Revision 1.3  2007/07/12 03:48:43  shxch
 * 增加调用Afx JNDI服务名的方法
 *
 * Revision 1.2  2007/05/31 07:38:23  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 05:56:36  qilin
 * no message
 *
 * Revision 1.21  2007/03/16 06:02:30  qilin
 * no message
 *
 * Revision 1.20  2007/03/14 06:15:54  shxch
 * *** empty log message ***
 *
 * Revision 1.19  2007/03/02 05:16:43  shxch
 * *** empty log message ***
 *
 * Revision 1.18  2007/03/02 05:02:50  shxch
 * *** empty log message ***
 *
 * Revision 1.17  2007/03/01 09:06:56  shxch
 * *** empty log message ***
 *
 * Revision 1.16  2007/03/01 07:20:47  shxch
 * *** empty log message ***
 *
 * Revision 1.15  2007/03/01 02:57:08  shxch
 * 添加获取图标文件名数组的方法
 *
 * Revision 1.14  2007/02/28 09:18:15  shxch
 * *** empty log message ***
 *
 * Revision 1.13  2007/02/27 09:38:47  shxch
 * *** empty log message ***
 *
 * Revision 1.12  2007/02/27 08:11:39  shxch
 * *** empty log message ***
 *
 * Revision 1.11  2007/02/27 07:51:05  shxch
 * *** empty log message ***
 *
 * Revision 1.10  2007/02/27 06:03:03  shxch
 * *** empty log message ***
 *
 * Revision 1.9  2007/02/25 09:01:29  shxch
 * *** empty log message ***
 *
 * Revision 1.8  2007/02/25 02:05:33  shxch
 * *** empty log message ***
 *
 * Revision 1.7  2007/02/25 02:04:11  shxch
 * *** empty log message ***
 *
 * Revision 1.6  2007/02/02 09:02:44  shxch
 * *** empty log message ***
 *
 * Revision 1.5  2007/02/01 07:17:21  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/02/01 07:10:40  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/01/31 09:37:50  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 03:41:28  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
