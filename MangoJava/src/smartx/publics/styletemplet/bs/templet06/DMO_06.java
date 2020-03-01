/**************************************************************************
 * $RCSfile: DMO_06.java,v $  $Revision: 1.4.6.1 $  $Date: 2009/01/16 08:51:28 $
 **************************************************************************/

package smartx.publics.styletemplet.bs.templet06;

import java.util.*;

import smartx.framework.common.bs.*;
import smartx.framework.metadata.vo.*;


public class DMO_06 {

    public DMO_06() {

    }

    public HashMap dealCommit(String _dsName, String _bsInterceptName, BillVO[] _insertobjs, BillVO[] _deleteobjs,
                              BillVO[] _updateobjs) throws Exception {
        IBSIntercept_06 bsIntercept = getIntercept(_bsInterceptName); // 创建BS端拦截器

        // 新增前拦截!!
        if (bsIntercept != null) {
            bsIntercept.dealBeforeCommit(_dsName, _insertobjs, _deleteobjs, _updateobjs);
        }

        Vector v_sqls = new Vector(); //
        if (_deleteobjs != null && _deleteobjs.length > 0) {
            for (int i = 0; i < _deleteobjs.length; i++) {
                String str_delSQL = _deleteobjs[i].getDeleteSQL();
                v_sqls.add(str_delSQL);
            }
        }

        if (_insertobjs != null && _insertobjs.length > 0) {
            for (int i = 0; i < _insertobjs.length; i++) {
                String str_insertSQL = _insertobjs[i].getInsertSQL();
                v_sqls.add(str_insertSQL);
            }
        }

        if (_updateobjs != null && _updateobjs.length > 0) {
            for (int i = 0; i < _updateobjs.length; i++) {
				// 乐观锁检查,,如果需要有乐观锁处理!!!
				if (_updateobjs[i].isDealVersion()) {
					Integer oldver = _updateobjs[i].getVersion(); // 取得当前版本号!!
		            Integer newver = this.getVersion(_dsName, _updateobjs[i]); //
		            if (oldver != null && newver != null &&  oldver.compareTo(newver)<=0) {
						throw new Exception("所操作数据已经被其他用户修改，请刷新数据后再进行操作！");
					}
				}
                String str_updateSQL = _updateobjs[i].getUpdateSQL();
                v_sqls.add(str_updateSQL);
            }
        }

        new CommDMO().executeBatchByDS(_dsName, v_sqls); //真正提交数据库,核心地带!!

        // 新增后拦截!!
        if (bsIntercept != null) {
            bsIntercept.dealAfterCommit(_dsName, _insertobjs, _deleteobjs, _updateobjs);
        }

        HashMap BillVOMap = new HashMap(); //
        BillVOMap.put("INSERT", _insertobjs); //
        BillVOMap.put("DELETE", _deleteobjs); //
        BillVOMap.put("UPDATE", _updateobjs); //

        return BillVOMap;
    }

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
    private IBSIntercept_06 getIntercept(String _interceptName) throws Exception {
        if (_interceptName != null && !_interceptName.equals("")) {
            return (IBSIntercept_06) Class.forName(_interceptName).newInstance();
        } else {
            return null;
        }
    }

}
/*******************************************************************************
 * $RCSfile: DMO_06.java,v $ $Revision: 1.4.6.1 $ $Date: 2009/01/16 08:51:28 $
 *
 * $Log: DMO_06.java,v $
 * Revision 1.4.6.1  2009/01/16 08:51:28  wangqi
 * *** empty log message ***
 *
 * Revision 1.1  2008/09/01 07:39:02  wangqi
 * *** empty log message ***
 *
 * Revision 1.4  2007/08/01 05:56:18  qilin
 * VERSION为null时用0进行比较
 *
 * Revision 1.3  2007/07/23 10:59:00  sunxf
 * Nova 20-14    平台层面更新数据的地方提供乐观锁控制，解决并发访问问题
 *
 * Revision 1.2  2007/05/31 07:39:06  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:13:21  qilin
 * no message
 *
 * Revision 1.4  2007/03/15 07:00:31  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/03/09 01:17:19  sunxf
 * *** empty log message ***
 *
 * Revision 1.2  2007/03/08 11:03:01  shxch
 * *** empty log message ***
 *
 * Revision 1.1  2007/03/08 10:42:29  shxch
 * *** empty log message ***
 *
 * Revision 1.6  2007/03/08 10:40:41  shxch
 * *** empty log message ***
 *
 * Revision 1.5  2007/03/02 05:02:51  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/02/05 04:40:44  lujian
 * *** empty log message ***
 * Revision 1.3 2007/02/02 08:52:25 lujian *** empty log
 * message ***
 *
 * Revision 1.2 2007/01/30 04:32:02 lujian *** empty log message ***
 *
 *
 ******************************************************************************/
