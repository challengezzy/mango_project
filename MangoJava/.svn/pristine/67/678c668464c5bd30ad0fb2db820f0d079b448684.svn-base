/**************************************************************************
 * $RCSfile: IUIIntercept_03.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:02 $
 **************************************************************************/
package smartx.publics.styletemplet.ui.templet03;

import smartx.framework.metadata.ui.*;
import smartx.framework.metadata.vo.*;

public interface IUIIntercept_03 {

    // 删除前做的动作
    public void actionBeforeDelete(BillCardPanel _billCardPanel) throws Exception;

    // 新增后做的动作
    public void actionAfterInsert(BillCardPanel _billCardPanel) throws Exception;

    // 修改后做的动作,即在触发编辑公式后做的!
    public void actionAfterUpdate(BillCardPanel _billCardPanel, String _itemkey) throws Exception;

    // 新增提交前处理,一般是校验
    public void dealCommitBeforeInsert(
        smartx.publics.styletemplet.ui.templet03.AbstractTempletFrame03 _frame,
        BillVO _insertobjs) throws Exception; //

    // 新增提交后处理,一般是重置页面控件!!比如有一项必须在后台经过复杂的运算后才能知道其值!!!
    public void dealCommitAfterInsert(
        smartx.publics.styletemplet.ui.templet03.AbstractTempletFrame03 _frame,
        BillVO _insertobjs); //

    // 删除提交前处理,一般是校验
    public void dealCommitBeforeDelete(
        smartx.publics.styletemplet.ui.templet03.AbstractTempletFrame03 _frame,
        BillVO _deleteobjs) throws Exception; //

    // 删除提交后处理,一般是重置页面控件!!
    public void dealCommitAfterDelete(
        smartx.publics.styletemplet.ui.templet03.AbstractTempletFrame03 _frame,
        BillVO _deleteobjs); //

    // 修改提交前处理,一般是校验
    public void dealCommitBeforeUpdate(
        smartx.publics.styletemplet.ui.templet03.AbstractTempletFrame03 _frame,
        BillVO _updateobjs) throws Exception; //

    // 修改提交后处理,一般是重置页面控件!!
    public void dealCommitAfterUpdate(
        smartx.publics.styletemplet.ui.templet03.AbstractTempletFrame03 _frame,
        BillVO _updateobjs); //

}
/**************************************************************************
 * $RCSfile: IUIIntercept_03.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:02 $
 *
 * $Log: IUIIntercept_03.java,v $
 * Revision 1.2  2007/05/31 07:39:02  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:14:27  qilin
 * no message
 *
 * Revision 1.3  2007/03/05 09:59:13  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:48:32  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/