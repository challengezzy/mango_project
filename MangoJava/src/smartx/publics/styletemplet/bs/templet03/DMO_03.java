/**************************************************************************
 * $RCSfile: DMO_03.java,v $  $Revision: 1.4.6.1 $  $Date: 2009/01/16 08:51:30 $
 **************************************************************************/

package smartx.publics.styletemplet.bs.templet03;

import java.util.*;

import smartx.framework.common.bs.*;
import smartx.framework.common.vo.*;
import smartx.framework.metadata.vo.*;


public class DMO_03 extends AbstractDMO {

    public DMO_03() {

    }

    /**
     * 平台新增
     *
     * @param _dsName
     * @param _bsInterceptName
     * @param _insertobj
     * @return
     * @throws Exception
     */
    public BillVO dealInsert(String _dsName, String _bsInterceptName, BillVO _insertobj) throws Exception {
        IBSIntercept_03 bsIntercept = getIntercept(_bsInterceptName); // 拦截器

        // 新增前拦截!!
        if (bsIntercept != null) {
            bsIntercept.dealCommitBeforeInsert(_dsName, _insertobj); // 可能会抛异常!!!
        }

        // 直正去新增!!!!!!!!!!!!!!
        String str_sql = _insertobj.getInsertSQL(); // 实际的SQL
        new CommDMO().executeUpdateByDS(_dsName, str_sql); // 平台去提交数据库,可能会抛异常!!!

        // 新增后拦截!!
        if (bsIntercept != null) {
            bsIntercept.dealCommitAfterInsert(_dsName, _insertobj); // 可能会抛异常!!!
        }

        return _insertobj;
    }

    /**
     * 平台删除
     *
     * @param _dsName
     * @param _bsInterceptName
     * @param _deleteobj
     * @throws Exception
     */
    public void dealDelete(String _dsName, String _bsInterceptName, BillVO _deleteobj) throws Exception {
        IBSIntercept_03 bsIntercept = getIntercept(_bsInterceptName); // 拦截器

        // 删除前拦截!!
        if (bsIntercept != null) {
            bsIntercept.dealCommitBeforeDelete(_dsName, _deleteobj);
        }

        // 真正去删除
        String str_sql = _deleteobj.getDeleteSQL(); // 取得SQL
        new CommDMO().executeUpdateByDS(_dsName, str_sql); // 平台去提交数据库,可能会抛异常!!!

        // 删除后拦截!!
        if (bsIntercept != null) {
            bsIntercept.dealCommitAfterDelete(_dsName, _deleteobj);
        }
    }

    /**
     * 平台级联删除
     *
     * @param _dsName
     * @param _bsInterceptName
     * @param _deleteobj
     * @param tablename
     *            要删除的记录所在的表名
     * @param field
     *            记录的唯一标识字段,如:ID.
     * @throws Exception
     */
    public void dealDelete(String _dsName, String _bsInterceptName, HashVO[] _deleteobj, String tablename, String field) throws
        Exception {
        IBSIntercept_03 bsIntercept = getIntercept(_bsInterceptName); // 拦截器

        // 删除前拦截!!
        if (bsIntercept != null) {
            bsIntercept.dealCommitBeforeCascadeDelete(_dsName, _deleteobj);
        }
        if (tablename == null || tablename.equals("")) {
            throw new Exception("没有设置删除记录的表名");
        }
        // 真正去删除
        ArrayList list = new ArrayList();
        for (int i = 0; i < _deleteobj.length; i++) {
            if (_deleteobj[i].getStringValue(field) != null && !_deleteobj[i].getStringValue(field).equals("")) {
                list.add("delete from " + tablename + " where " + field + "='" + _deleteobj[i].getStringValue(field)
                         + "'");
            } else {
                throw new Exception("要删除的记录条件不完整");
            }
        }

        new CommDMO().executeBatchByDS(_dsName, list); // 平台去提交数据库,可能会抛异常!!!

        // 删除后拦截!!
        if (bsIntercept != null) {
            bsIntercept.dealCommitAfterCascadeDelete(_dsName, _deleteobj);
        }
    }

    /**
     * 平台修改
     *
     * @param _dsName
     * @param _bsInterceptName
     * @param _updateobj
     * @return
     * @throws Exception
     */
    public BillVO dealUpdate(String _dsName, String _bsInterceptName, BillVO _updateobj) throws Exception {
        IBSIntercept_03 bsIntercept = getIntercept(_bsInterceptName); // 拦截器

        // 修改前拦截!!
        if (bsIntercept != null) {
            bsIntercept.dealCommitBeforeUpdate(_dsName, _updateobj);
        }
        // 乐观锁检查,,如果需要有乐观锁处理!!!
        if (_updateobj.isDealVersion()) {
        	Integer oldver = _updateobj.getVersion(); // 取得当前版本号!!
            Integer newver = this.getVersion(_dsName, _updateobj); //
            if (oldver != null && newver != null &&  oldver.compareTo(newver)<=0) {
                throw new Exception("所操作数据已经被其他用户修改，请刷新数据后再进行操作！");
            }
        }

        String str_sql = _updateobj.getUpdateSQL();
        new CommDMO().executeUpdateByDS(_dsName, str_sql); // 真正去执行!!

        // 修改后拦截!!
        if (bsIntercept != null) {
            bsIntercept.dealCommitAfterUpdate(_dsName, _updateobj);
        }

        return _updateobj;
    }

    /**
     * 得到版本号
     *
     * @param _updateobj
     * @return
     */
    private Integer getVersion(String _dsName, BillVO _updateobj) {
        StringBuffer str_sql = new StringBuffer();
        str_sql.append("select nvl(version,0) from " + _updateobj.getSaveTableName() + " where "
                       + _updateobj.getUpdateWhereCondition());
        
        try {
            return new CommDMO().getHashVoArrayByDS(_dsName, str_sql.toString())[0].getIntegerValue(0);            
        } catch (Exception e) {            
            return null;
        }
    }

    /**
     * 取得服务器端拦截器类
     *
     * @param _interceptName
     * @return
     * @throws Exception
     */
    private IBSIntercept_03 getIntercept(String _interceptName) throws Exception {
        if (_interceptName != null && !_interceptName.equals("")) {
            return (IBSIntercept_03) Class.forName(_interceptName).newInstance();
        } else {
            return null;
        }
    }

}

/*******************************************************************************
 * $RCSfile: DMO_03.java,v $ $Revision: 1.4.6.1 $ $Date: 2009/01/16 08:51:30 $
 *
 * $Log: DMO_03.java,v $
 * Revision 1.4.6.1  2009/01/16 08:51:30  wangqi
 * *** empty log message ***
 *
 * Revision 1.1  2008/09/01 07:38:59  wangqi
 * *** empty log message ***
 *
 * Revision 1.4  2007/08/01 05:56:18  qilin
 * VERSION为null时用0进行比较
 *
 * Revision 1.3  2007/07/23 10:58:59  sunxf
 * Nova 20-14    平台层面更新数据的地方提供乐观锁控制，解决并发访问问题
 *
 * Revision 1.2  2007/05/31 07:39:06  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:13:21  qilin
 * no message
 *
 * Revision 1.5  2007/03/15 07:00:32  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/03/12 04:46:21  sunxf
 * *** empty log message ***
 * Revision 1.3 2007/03/09 01:11:58 sunxf *** empty log
 * message ***
 *
 * Revision 1.2 2007/03/08 11:03:01 shxch *** empty log message ***
 *
 * Revision 1.1 2007/03/08 10:42:29 shxch *** empty log message ***
 *
 * Revision 1.6 2007/03/08 07:45:09 shxch *** empty log message ***
 *
 * Revision 1.5 2007/03/02 05:02:50 shxch *** empty log message ***
 *
 * Revision 1.4 2007/02/05 04:40:43 lujian *** empty log message ***
 *
 * Revision 1.3 2007/02/02 08:52:25 lujian *** empty log message ***
 *
 * Revision 1.2 2007/01/30 04:32:02 lujian *** empty log message ***
 *
 *
 ******************************************************************************/
