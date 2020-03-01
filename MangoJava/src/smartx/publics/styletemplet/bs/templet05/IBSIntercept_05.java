/**************************************************************************
 * $RCSfile: IBSIntercept_05.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:06 $
 **************************************************************************/
package smartx.publics.styletemplet.bs.templet05;

import smartx.framework.metadata.vo.*;

public interface IBSIntercept_05 {

    //新增前处理
    public void dealCommitBeforeInsert(String _dsName, BillVO _insertobjs) throws Exception; //

    //新增后处理
    public void dealCommitAfterInsert(String _dsName, BillVO _insertobjs) throws Exception; //

    //删除前处理
    public void dealCommitBeforeDelete(String _dsName, BillVO _deleteobjs) throws Exception; //

    //删除后处理
    public void dealCommitAfterDelete(String _dsName, BillVO _deleteobjs) throws Exception; //

    //修改前处理
    public void dealCommitBeforeUpdate(String _dsName, BillVO _updateobjs) throws Exception; //

    //修改后处理
    public void dealCommitAfterUpdate(String _dsName, BillVO _updateobjs) throws Exception; //

}
/**************************************************************************
 * $RCSfile: IBSIntercept_05.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:06 $
 *
 * $Log: IBSIntercept_05.java,v $
 * Revision 1.2  2007/05/31 07:39:06  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:13:21  qilin
 * no message
 *
 * Revision 1.3  2007/03/08 10:53:18  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:32:02  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/