/**************************************************************************
 * $RCSfile: TestUIIntercept.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:05 $
 **************************************************************************/
package smartx.publics.styletemplet.ui.templet08;

import smartx.framework.metadata.ui.*;
import smartx.publics.styletemplet.vo.*;

public class TestUIIntercept implements IUIIntercept_08 {

    public void actionAfterInsert_child(BillListPanel _billListPanel, int _newrow) throws Exception {
        System.out.println("actionAfterInsert_child");

    }

    public void actionAfterInsert_parent(BillCardPanel _billcardPanel) throws Exception {
        System.out.println("actionAfterInsert_parent");

    }

    public void actionAfterUpdate_child(BillListPanel _billListPanel, String _itemkey, int _updatedrow) throws
        Exception {
        System.out.println("actionAfterUpdate_child");

    }

    public void actionAfterUpdate_parent(BillCardPanel _billcardPanel, String _itemkey) throws Exception {
        System.out.println("actionAfterUpdate_parent");
    }

    public void actionBeforeDelete_child(BillListPanel _parentbilllistPanel, int _delerow) throws Exception {
        System.out.println("actionBeforeDelete_child");

    }

    public void actionBeforeDelete_parent(BillListPanel _parentbilllistPanel, int _delerow) throws Exception {
        System.out.println("actionBeforeDelete_parent");

    }

    public void dealCommitAfterDelete(AbstractTempletFrame08 _frame, AggBillVO _insertobjs) {
        System.out.println("dealCommitAfterDelete");

    }

    public void dealCommitAfterInsert(AbstractTempletFrame08 _frame, AggBillVO _insertobjs) {
        System.out.println("dealCommitAfterInsert");

    }

    public void dealCommitAfterUpdate(AbstractTempletFrame08 _frame, AggBillVO _insertobjs) {
        System.out.println("dealCommitAfterUpdate");

    }

    public void dealCommitBeforeDelete(AbstractTempletFrame08 _frame, AggBillVO _insertobjs) throws Exception {
        System.out.println("dealCommitBeforeDelete");
        throw new Exception("不让你删除");

    }

    public void dealCommitBeforeInsert(AbstractTempletFrame08 _frame, AggBillVO _insertobjs) throws Exception {
        System.out.println("dealCommitBeforeInsert");
    }

    public void dealCommitBeforeUpdate(AbstractTempletFrame08 _frame, AggBillVO _insertobjs) throws Exception {
        System.out.println("dealCommitBeforeUpdate");
    }

}
/**************************************************************************
 * $RCSfile: TestUIIntercept.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:05 $
 *
 * $Log: TestUIIntercept.java,v $
 * Revision 1.2  2007/05/31 07:39:05  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:14:28  qilin
 * no message
 *
 * Revision 1.3  2007/03/05 09:59:14  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:48:33  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/