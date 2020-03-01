package smartx.system.sysdata.bs;

import java.sql.*;

import smartx.framework.common.bs.*;
import smartx.framework.common.vo.*;
import smartx.framework.metadata.vo.*;
import smartx.publics.styletemplet.bs.templet07.*;
import smartx.system.common.constant.*;


/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class IBSInterceptSysdata implements IBSIntercept_07 {
    public IBSInterceptSysdata() {
    }

    public void dealCommitBeforeInsert(String _dsName, BillVO _insertobjs) throws Exception {
        Statement p_parent_stmt = null;
        try {
            if (_insertobjs != null) {
                //查找当前最大的value值
                int maxValue = 0;
                long categoryID = new Long( (String) _insertobjs.getObject("SYSDICTIONARYCATEGORYID")).longValue();
                String sql = "SELECT MAX(VALUE) FROM BFBIZ_SYSDICTIONARY WHERE SYSDICTIONARYCATEGORYID=" + categoryID;
                HashVO[] result = getDMO().getHashVoArrayByDS(_dsName, sql);
                if (result != null && result.length > 0) {
                    if(result[0].getIntegerValue("MAX(VALUE)")!=null) {
                        maxValue = result[0].getIntegerValue("MAX(VALUE)").intValue() + 1;
                    }
                }
                sql = "SELECT CLASSID,ATTRIBUTEID,APPMODULE FROM BFBIZ_SYSDICTIONARYCATEGORY WHERE ID=" + categoryID;
                result = getDMO().getHashVoArrayByDS(_dsName, sql);
                if (result != null && result.length > 0) {
                    String classID = result[0].getStringValue("CLASSID");
                    String attrubuteID = result[0].getStringValue("ATTRIBUTEID");
                    String appModule = result[0].getStringValue("APPMODULE");

                    _insertobjs.setObject("VALUE", String.valueOf(maxValue++));
                    _insertobjs.setObject("TYPE", String.valueOf(CommonSysConst.SYSDICTIONARY_TYPE_USER));
                    _insertobjs.setObject("CLASSID", classID);
                    _insertobjs.setObject("ATTRIBUTEID", attrubuteID);
                    _insertobjs.setObject("APPMODULE", appModule);
                    _insertobjs.setObject("STATE", String.valueOf(CommonSysConst.SYSDICTIONARY_STATE_USED));
                }
            }

        } catch (Exception ex) {
            throw ex;
        } finally {
            if (p_parent_stmt != null) {
                p_parent_stmt.close();
            }
        }

    }

    public void dealCommitAfterInsert(String _dsName, BillVO _insertobjs) throws Exception {
    }

    public void dealCommitBeforeDelete(String _dsName, BillVO _deleteobjs) throws Exception {
    }

    public void dealCommitAfterDelete(String _dsName, BillVO _deleteobjs) throws Exception {
    }

    public void dealCommitBeforeUpdate(String _dsName, BillVO _updateobjs) throws Exception {
    }

    public void dealCommitAfterUpdate(String _dsName, BillVO _updateobjs) throws Exception {
    }

    CommDMO dmo = null;
    private CommDMO getDMO() {
        if (dmo == null) {
            dmo = new CommDMO();
        }
        return dmo;
    }

}
/***********************************************************************
 * $RCSfile: IBSInterceptSysdata.java,v $  $Revision: 1.3 $  $Date: 2007/08/24 02:00:56 $
 * $Log: IBSInterceptSysdata.java,v $
 * Revision 1.3  2007/08/24 02:00:56  qilin
 * MR#:BZM10-338
 *
 * Revision 1.2  2007/05/31 07:41:33  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:22:09  qilin
 * no message
 *
 * Revision 1.3  2007/04/24 05:59:36  qilin
 * no message
 *
 * Revision 1.2  2007/03/09 01:36:09  qilin
 * no message
 *
 * Revision 1.1  2007/03/07 02:51:10  qilin
 * no message
 *
 *************************************************************************/
