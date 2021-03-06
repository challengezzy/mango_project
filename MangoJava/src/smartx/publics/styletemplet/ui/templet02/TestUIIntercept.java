/**************************************************************************
 * $RCSfile: TestUIIntercept.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:02 $
 **************************************************************************/
package smartx.publics.styletemplet.ui.templet02;

import smartx.framework.metadata.ui.*;
import smartx.framework.metadata.vo.*;

public class TestUIIntercept implements IUIIntercept_02 {

    public void actionBeforeDelete(BillListPanel _billlistPanel, int _delerow) throws Exception {
        NovaMessage.show("删除前动作拦截");
    }

    public void actionAfterInsert(BillCardPanel _billCardPanel) throws Exception {
        NovaMessage.show("新增后动作拦截");
    }

    public void actionAfterUpdate(BillCardPanel _billCardPanel, String _itemkey) throws Exception {
        NovaMessage.show("修改后动作拦截");
    }

    public void dealCommitAfterDelete(AbstractTempletFrame02 _frame, BillVO _deleteobjs) {
        NovaMessage.show("删除后拦截");
    }

    public void dealCommitAfterInsert(AbstractTempletFrame02 _frame, BillVO _insertobjs) {
        NovaMessage.show("新增后拦截");
    }

    public void dealCommitAfterUpdate(AbstractTempletFrame02 _frame, BillVO _updateobjs) {
        NovaMessage.show("修改后拦截");
    }

    public void dealCommitBeforeDelete(AbstractTempletFrame02 _frame, BillVO _deleteobjs) throws Exception {
        NovaMessage.show("删除前拦截");
    }

    public void dealCommitBeforeInsert(AbstractTempletFrame02 _frame, BillVO _insertobjs) throws Exception {
        NovaMessage.show("新增前拦截");
    }

    public void dealCommitBeforeUpdate(AbstractTempletFrame02 _frame, BillVO _updateobjs) throws Exception {
        NovaMessage.show("修改前拦截");
    }

}
/**************************************************************************
 * $RCSfile: TestUIIntercept.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:02 $
 *
 * $Log: TestUIIntercept.java,v $
 * Revision 1.2  2007/05/31 07:39:02  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:14:27  qilin
 * no message
 *
 * Revision 1.4  2007/03/05 09:59:14  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/02/10 08:59:36  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:48:30  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/