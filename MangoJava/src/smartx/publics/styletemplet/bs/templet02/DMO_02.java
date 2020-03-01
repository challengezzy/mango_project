/**************************************************************************
 * $RCSfile: DMO_02.java,v $  $Revision: 1.4.6.2 $  $Date: 2009/01/16 08:51:28 $
 **************************************************************************/

package smartx.publics.styletemplet.bs.templet02;

import smartx.framework.common.bs.*;
import smartx.framework.metadata.vo.*;

public class DMO_02 {

    public DMO_02() {
    }

    public BillVO dealInsert(String _dsName, String _bsInterceptName, BillVO _insertobj) throws Exception {
        IBSIntercept_02 bsIntercept = getIntercept(_bsInterceptName); // 拦截器

        // 新增前拦截!!
        if (bsIntercept != null) {
            bsIntercept.dealCommitBeforeInsert(_dsName, _insertobj);
        }

        // 直正去新增!!!!!!!!!!!!!!
        String str_sql = _insertobj.getInsertSQL(); // 实际的SQL
        new CommDMO().executeUpdateByDS(_dsName, str_sql); //真正提交数据库!

        // 新增后拦截!!
        if (bsIntercept != null) {
            bsIntercept.dealCommitAfterInsert(_dsName, _insertobj);
        }

        return _insertobj;
    }

    public void dealDelete(String _dsName, String _bsInterceptName, BillVO _deleteobj) throws Exception {
        IBSIntercept_02 bsIntercept = getIntercept(_bsInterceptName); // 拦截器

        // 删除前拦截!!
        if (bsIntercept != null) {
            bsIntercept.dealCommitBeforeDelete(_dsName, _deleteobj);
        }

        // 真正去删除
        String str_sql = _deleteobj.getDeleteSQL(); // 有?的SQL
        new CommDMO().executeUpdateByDS(_dsName, str_sql); //真正提交数据库!

        // 删除后拦截!!
        if (bsIntercept != null) {
            bsIntercept.dealCommitAfterDelete(_dsName, _deleteobj);
        }
    }

    public BillVO dealUpdate(String _dsName, String _bsInterceptName, BillVO _updateobj) throws Exception {
        IBSIntercept_02 bsIntercept = getIntercept(_bsInterceptName); // 拦截器

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
        new CommDMO().executeUpdateByDS(_dsName, str_sql); //真正提交数据库!

        if (bsIntercept != null) {
            bsIntercept.dealCommitAfterUpdate(_dsName, _updateobj);
        }

        return _updateobj;
    }

    /**
     * 取得当前版本
     * @param _dsName
     * @param _updateobj
     * @return
     */
    private Integer getVersion(String _dsName, BillVO _updateobj) {
        StringBuffer str_sql = new StringBuffer();
        str_sql.append("select nvl(version,0) FROM " + _updateobj.getSaveTableName() + " WHERE" +
                       _updateobj.getUpdateWhereCondition() + "");
        try {
            return new CommDMO().getHashVoArrayByDS(_dsName, str_sql.toString())[0].getIntegerValue(0);            
        } catch (Exception e) {            
            return null;
        }
    }

    /**
     * 取得服务器端拦截器类
     * @param _interceptName
     * @return
     * @throws Exception
     */
    private IBSIntercept_02 getIntercept(String _interceptName) throws Exception {
        if (_interceptName != null && !_interceptName.equals("")) {
            return (IBSIntercept_02) Class.forName(_interceptName).newInstance();
        } else {
            return null;
        }
    }
}
/*******************************************************************************
 * $RCSfile: DMO_02.java,v $ $Revision: 1.4.6.2 $ $Date: 2009/01/16 08:51:28 $
 *
 * $Log: DMO_02.java,v $
 * Revision 1.4.6.2  2009/01/16 08:51:28  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2008/11/28 05:58:53  wangqi
 * *** empty log message ***
 *
 * Revision 1.1  2008/09/01 07:38:59  wangqi
 * *** empty log message ***
 *
 * Revision 1.4  2007/08/01 05:56:17  qilin
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
 * Revision 1.3  2007/03/15 07:00:32  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/03/09 01:14:46  sunxf
 * *** empty log message ***
 *
 * Revision 1.1  2007/03/08 10:42:29  shxch
 * *** empty log message ***
 *
 * Revision 1.7  2007/03/08 10:40:41  shxch
 * *** empty log message ***
 *
 * Revision 1.6  2007/03/08 08:24:34  shxch
 * *** empty log message ***
 *
 * Revision 1.5  2007/03/02 05:02:51  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/02/05 04:40:43  lujian
 * *** empty log message ***
 * Revision 1.3 2007/02/02 08:52:25 lujian *** empty log
 * message ***
 *
 * Revision 1.2 2007/01/30 04:32:03 lujian *** empty log message ***
 *
 *
 ******************************************************************************/
