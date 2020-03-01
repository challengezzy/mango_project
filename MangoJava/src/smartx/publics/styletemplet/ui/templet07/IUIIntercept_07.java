/**************************************************************************
 * $RCSfile: IUIIntercept_07.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:04 $
 **************************************************************************/
package smartx.publics.styletemplet.ui.templet07;

import smartx.framework.metadata.ui.*;
import smartx.framework.metadata.vo.*;

public interface IUIIntercept_07 {

    //删除前做的动作
    public void actionBeforeDelete(BillListPanel _billlistPanel, int _delerow) throws Exception;

    //新增后做的动作
    public void actionAfterInsert(BillCardPanel _billCardPanel) throws Exception;

    //修改后做的动作,即在触发编辑公式后做的!
    public void actionAfterUpdate(BillCardPanel _billCardPanel, String _itemkey) throws Exception;

    //新增提交前处理,一般是校验
    public void dealCommitBeforeInsert(smartx.publics.styletemplet.ui.templet07.AbstractTempletFrame07 _frame,
                                       BillVO _insertobjs) throws Exception; //

    //新增提交后处理,一般是重置页面控件!!比如有一项必须在后台经过复杂的运算后才能知道其值!!!
    public void dealCommitAfterInsert(smartx.publics.styletemplet.ui.templet07.AbstractTempletFrame07 _frame,
                                      BillVO _insertobjs); //

    //删除提交前处理,一般是校验
    public void dealCommitBeforeDelete(smartx.publics.styletemplet.ui.templet07.AbstractTempletFrame07 _frame,
                                       BillVO _deleteobjs) throws Exception; //

    //删除提交后处理,一般是重置页面控件!!
    public void dealCommitAfterDelete(smartx.publics.styletemplet.ui.templet07.AbstractTempletFrame07 _frame,
                                      BillVO _deleteobjs); //

    //修改提交前处理,一般是校验
    public void dealCommitBeforeUpdate(smartx.publics.styletemplet.ui.templet07.AbstractTempletFrame07 _frame,
                                       BillVO _updateobjs) throws Exception; //

    //修改提交后处理,一般是重置页面控件!!
    public void dealCommitAfterUpdate(smartx.publics.styletemplet.ui.templet07.AbstractTempletFrame07 _frame,
                                      BillVO _updateobjs); //

    //主表table鼠标动作
    public void actionMouseClickOnPriTable(AbstractTempletFrame07 _frame);

    //子表table鼠标动作
    public void actionMouseClickOnSubTable(AbstractTempletFrame07 _frame);

}
/**************************************************************************
 * $RCSfile: IUIIntercept_07.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:04 $
 *
 * $Log: IUIIntercept_07.java,v $
 * Revision 1.2  2007/05/31 07:39:04  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:14:28  qilin
 * no message
 *
 * Revision 1.4  2007/03/06 07:55:17  qilin
 * no message
 *
 * Revision 1.3  2007/03/05 09:59:15  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:48:31  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
