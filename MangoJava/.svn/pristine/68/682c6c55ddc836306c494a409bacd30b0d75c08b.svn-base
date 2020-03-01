/**************************************************************************
 * $RCSfile: DMO_09.java,v $  $Revision: 1.4.6.1 $  $Date: 2009/01/16 08:51:29 $
 **************************************************************************/

package smartx.publics.styletemplet.bs.templet09;

import java.util.*;

import smartx.framework.common.bs.*;
import smartx.framework.metadata.vo.*;
import smartx.publics.styletemplet.vo.*;


public class DMO_09 {

    public DMO_09() {
    }

    /**
     * 新增!!
     * @param _dsName
     * @param _bsInterceptName
     * @param _aggVO
     * @return
     * @throws Exception
     */
    public AggBillVO dealInsert(String _dsName, String _bsInterceptName, AggBillVO _aggVO) throws Exception {
        IBSIntercept_09 bsIntercept = getIntercept(_bsInterceptName);

        // 新增前拦截!!
        if (bsIntercept != null) {
            bsIntercept.dealCommitBeforeInsert(_dsName, _aggVO);
        }

        Vector v_sqls = new Vector(); //
        v_sqls.add(_aggVO.getParentVO().getInsertSQL()); //加入主表新增的SQL

        int li_childCount = _aggVO.getChildCount(); // 所有页签数量!!!
        for (int i = 0; i < li_childCount; i++) {
            BillVO childInsertVOs[] = _aggVO.getChildInsertVOs(i + 1);
            if (childInsertVOs != null && childInsertVOs.length > 0) { // 如果有新增的!!
                for (int j = 0; j < childInsertVOs.length; j++) {
                    String str_sqlchild_insert = childInsertVOs[j].getInsertSQL();
                    v_sqls.add(str_sqlchild_insert);
                }
            }
        }

        new CommDMO().executeBatchByDS(_dsName, v_sqls); //真正插入数据库!!!!!核心地带!!!

        // 新增后拦截!!
        if (bsIntercept != null) {
            bsIntercept.dealCommitAfterInsert(_dsName, _aggVO);
        }
        return _aggVO;
    }

    /**
     * 删除
     * @param _dsName
     * @param _bsInterceptName
     * @param _aggVO
     * @throws Exception
     */
    public void dealDelete(String _dsName, String _bsInterceptName, AggBillVO _aggVO) throws Exception {
        IBSIntercept_09 bsIntercept = getIntercept(_bsInterceptName);

        // 删除前拦截!!
        if (bsIntercept != null) {
            bsIntercept.dealCommitBeforeDelete(_dsName, _aggVO); //删除前拦截
        }

        Vector v_sqls = new Vector(); //
        int li_childCount = _aggVO.getChildCount(); //
        for (int i = 0; i < li_childCount; i++) {
            BillVO[] childDeleteVOs = _aggVO.getChildDeleteVOs(i + 1);
            if (childDeleteVOs != null && childDeleteVOs.length > 0) {
                for (int j = 0; j < childDeleteVOs.length; j++) {
                    String str_sqlchild_delete = childDeleteVOs[j].getDeleteSQL();
                    v_sqls.add(str_sqlchild_delete); //
                }
            }
        }
        v_sqls.add(_aggVO.getParentVO().getDeleteSQL()); //加入主表新增的SQL

        new CommDMO().executeBatchByDS(_dsName, v_sqls); //真正操作数据库!!!核心地带!!!

        // 删除后拦截!!
        if (bsIntercept != null) {
            bsIntercept.dealCommitAfterDelete(_dsName, _aggVO); //删除后拦截
        }
    }

    public AggBillVO dealUpdate(String _dsName, String _bsInterceptName, AggBillVO _aggVO) throws Exception {
        IBSIntercept_09 bsIntercept = getIntercept(_bsInterceptName);

        // 修改前拦截!!
        if (bsIntercept != null) {
            bsIntercept.dealCommitBeforeUpdate(_dsName, _aggVO);
        }

        // 乐观锁检查,,如果需要有乐观锁处理!!!
        if (_aggVO.getParentVO().isDealVersion()) {
        	Integer oldver = _aggVO.getParentVO().getVersion(); // 取得当前版本号!!
            Integer newver = this.getVersion(_dsName, _aggVO); //
            if (oldver != null && newver != null &&  oldver.compareTo(newver)<=0) {
                throw new Exception("主表所操作数据已经被其他用户修改，请刷新数据后再进行操作！");
            }
        }
        // 子表乐观锁检查,,如果需要有乐观锁处理!!!
        //TODO 这里检查的不是子表的version，而是主表的version。为什么呢？
        if(_aggVO.getChildUpdateVOs()!=null&&_aggVO.getChildUpdateVOs().length>=0)
        {
        	for (int i = 0; i < _aggVO.getChildUpdateVOs().length; i++) {
		        if (_aggVO.getChildUpdateVOs()[i].isDealVersion()) {
		        	Integer oldver = _aggVO.getParentVO().getVersion(); // 取得当前版本号!!
		            Integer newver = this.getVersion(_dsName, _aggVO); //
		            if (oldver != null && newver != null &&  oldver.compareTo(newver)<=0) {
		                throw new Exception("子表所操作数据已经被其他用户修改，请刷新数据后再进行操作！");
		            }
		        }
        	  }
        }

        //真正去修改!!!!
        Vector v_sqls = new Vector(); //
        String str_updateparent_sql = _aggVO.getParentVO().getUpdateSQL(); // 取得主表update的SQL!!!
        v_sqls.add(str_updateparent_sql); //

        //处理所有子表....!!!
        int li_childCount = _aggVO.getChildCount(); //
        for (int i = 0; i < li_childCount; i++) {
            BillVO[] childInsertVOs = _aggVO.getChildInsertVOs(i + 1);
            BillVO[] childUpdateVOs = _aggVO.getChildUpdateVOs(i + 1);
            BillVO[] childDeleteVOs = _aggVO.getChildDeleteVOs(i + 1);

            if (childDeleteVOs != null) { // 先做删除
                for (int j = 0; j < childDeleteVOs.length; j++) {
                    String str_sqlchild_delete = childDeleteVOs[j].getDeleteSQL();
                    v_sqls.add(str_sqlchild_delete);
                }
            }

            if (childInsertVOs != null) { // 再做新增
                for (int j = 0; j < childInsertVOs.length; j++) {
                    String str_sqlchild_insert = childInsertVOs[j].getInsertSQL();
                    v_sqls.add(str_sqlchild_insert);
                }
            }

            if (childUpdateVOs != null) { // 最后做修改
                for (int j = 0; j < childUpdateVOs.length; j++) {
                    String str_sqlchild_update = childUpdateVOs[j].getUpdateSQL();
                    v_sqls.add(str_sqlchild_update);
                }
            }
        }

        new CommDMO().executeBatchByDS(_dsName, v_sqls); //真正去操作数据库!核心地带!!

        // 修改后拦截!!
        if (bsIntercept != null) {
            bsIntercept.dealCommitAfterUpdate(_dsName, _aggVO);
        }

        return _aggVO;
    }

    /**
     * 获得当前的记录版本号
     * @param _aggvo
     * @return
     */
    private Integer getVersion(String _dsName, AggBillVO _aggvo) {
        String str_version_sql = "select nvl(version,0) from " + _aggvo.getParentVO().getSaveTableName() + " where " +
            _aggvo.getParentVO().getUpdateWhereCondition(); //
        try {
            return new CommDMO().getHashVoArrayByDS(_dsName, str_version_sql.toString())[0].getIntegerValue(0);            
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
    private IBSIntercept_09 getIntercept(String _interceptName) throws Exception {
        if (_interceptName != null && !_interceptName.equals("")) {
            return (IBSIntercept_09) Class.forName(_interceptName).newInstance();
        } else {
            return null;
        }
    }
}
/*******************************************************************************
 * $RCSfile: DMO_09.java,v $ $Revision: 1.4.6.1 $ $Date: 2009/01/16 08:51:29 $
 *
 * $Log: DMO_09.java,v $
 * Revision 1.4.6.1  2009/01/16 08:51:29  wangqi
 * *** empty log message ***
 *
 * Revision 1.1  2008/09/01 07:39:01  wangqi
 * *** empty log message ***
 *
 * Revision 1.4  2007/08/01 05:56:18  qilin
 * VERSION为null时用0进行比较
 *
 * Revision 1.3  2007/07/23 10:59:01  sunxf
 * Nova 20-14    平台层面更新数据的地方提供乐观锁控制，解决并发访问问题
 *
 * Revision 1.2  2007/05/31 07:39:07  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:13:21  qilin
 * no message
 *
 * Revision 1.10  2007/03/21 07:56:23  sunxf
 * *** empty log message ***
 *
 * Revision 1.9  2007/03/15 07:00:31  shxch
 * *** empty log message ***
 *
 * Revision 1.8  2007/03/09 01:17:20  sunxf
 * *** empty log message ***
 *
 * Revision 1.7  2007/03/08 10:59:49  shxch
 * *** empty log message ***
 *
 * Revision 1.6  2007/03/08 10:14:05  shxch
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
