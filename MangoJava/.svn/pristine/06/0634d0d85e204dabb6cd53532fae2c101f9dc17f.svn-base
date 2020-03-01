/**************************************************************************
 * $RCSfile: IUIIntercept_04.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:03 $
 **************************************************************************/
package smartx.publics.styletemplet.ui.templet04;

import smartx.framework.metadata.ui.*;
import smartx.framework.metadata.vo.*;

public interface IUIIntercept_04 {

    //删除前做的动作
    public void actionBeforeDelete(BillListPanel _billlistPanel, int _delerow) throws Exception;

    //新增后做的动作
    public void actionAfterInsert(BillListPanel _billlistPanel, int _newrow) throws Exception;

    //修改后做的动作
    public void actionAfterUpdate(BillListPanel _billlistPanel, int _updatedrow, String _itemkey) throws Exception; //

    //提交前做的校验处理!!!!参数分别是新增的,删除的,修改的数据!!!
    public void dealBeforeCommit(BillListPanel _billlistPanel, BillVO[] _insertobjs, BillVO[] _deleteobjs,
                                 BillVO[] _updateobjs) throws Exception; //

    //提交后的后续处理..
    public void dealAfterCommit(BillListPanel _billlistPanel, BillVO[] _insertobjs, BillVO[] _deleteobjs,
                                BillVO[] _updateobjs) throws Exception; //

}
/**************************************************************************
 * $RCSfile: IUIIntercept_04.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:03 $
 *
 * $Log: IUIIntercept_04.java,v $
 * Revision 1.2  2007/05/31 07:39:03  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:14:27  qilin
 * no message
 *
 * Revision 1.2  2007/01/30 04:48:31  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/