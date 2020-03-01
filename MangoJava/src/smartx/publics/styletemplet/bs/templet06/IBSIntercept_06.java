/**************************************************************************
 * $RCSfile: IBSIntercept_06.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:06 $
 **************************************************************************/
package smartx.publics.styletemplet.bs.templet06;

import smartx.framework.metadata.vo.*;

public interface IBSIntercept_06 {

    // 提交前做的校验!!!!参数分别是新增的,删除的,修改的数据!!!
    public void dealBeforeCommit(String _dsName, BillVO[] _insertobjs, BillVO[] _deleteobjs, BillVO[] _updateobjs) throws
        Exception; //

    // 提交后做的处理!!
    public void dealAfterCommit(String _dsName, BillVO[] _insertobjs, BillVO[] _deleteobjs, BillVO[] _updateobjs) throws
        Exception; //

}
/**************************************************************************
 * $RCSfile: IBSIntercept_06.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:06 $
 *
 * $Log: IBSIntercept_06.java,v $
 * Revision 1.2  2007/05/31 07:39:06  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:13:21  qilin
 * no message
 *
 * Revision 1.3  2007/03/08 10:40:40  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:32:02  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
