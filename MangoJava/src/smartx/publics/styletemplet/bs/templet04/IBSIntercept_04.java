/**************************************************************************
 * $RCSfile: IBSIntercept_04.java,v $  $Revision: 1.3 $  $Date: 2007/05/31 11:23:11 $
 **************************************************************************/
package smartx.publics.styletemplet.bs.templet04;

import smartx.framework.metadata.vo.*;

public interface IBSIntercept_04 {

    // 提交前做的校验!!!!参数分别是新增的,删除的,修改的数据!!!
    public void dealBeforeCommit(String _dsName, BillVO[] _insertobjs, BillVO[] _deleteobjs, BillVO[] _updateobjs) throws
        Exception; //

    // 提交后做的处理!!
    public void dealAfterCommit(String _dsName, BillVO[] _insertobjs, BillVO[] _deleteobjs, BillVO[] _updateobjs) throws
        Exception; //


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
 * $RCSfile: IBSIntercept_04.java,v $  $Revision: 1.3 $  $Date: 2007/05/31 11:23:11 $
 *
 * $Log: IBSIntercept_04.java,v $
 * Revision 1.3  2007/05/31 11:23:11  sunxb
 * *** empty log message ***
 *
 * Revision 1.2  2007/05/31 07:39:06  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:13:21  qilin
 * no message
 *
 * Revision 1.3  2007/03/08 10:40:41  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:32:03  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
