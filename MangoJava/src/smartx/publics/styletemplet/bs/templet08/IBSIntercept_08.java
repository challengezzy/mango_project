/**************************************************************************
 * $RCSfile: IBSIntercept_08.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:07 $
 **************************************************************************/
package smartx.publics.styletemplet.bs.templet08;

import smartx.publics.styletemplet.vo.*;

public interface IBSIntercept_08 {

    //新增前处理
    public void dealCommitBeforeInsert(String _dsName, AggBillVO _insertobjs) throws Exception; //

    //新增后处理
    public void dealCommitAfterInsert(String _dsName, AggBillVO _insertobjs) throws Exception; //

    //删除前处理
    public void dealCommitBeforeDelete(String _dsName, AggBillVO _insertobjs) throws Exception; //

    //删除后处理
    public void dealCommitAfterDelete(String _dsName, AggBillVO _insertobjs) throws Exception; //

    //修改前处理
    public void dealCommitBeforeUpdate(String _dsName, AggBillVO _insertobjs) throws Exception; //

    //修改后处理
    public void dealCommitAfterUpdate(String _dsName, AggBillVO _insertobjs) throws Exception; //

}
/**************************************************************************
 * $RCSfile: IBSIntercept_08.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:07 $
 *
 * $Log: IBSIntercept_08.java,v $
 * Revision 1.2  2007/05/31 07:39:07  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:13:21  qilin
 * no message
 *
 * Revision 1.3  2007/03/08 10:14:04  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:32:03  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
