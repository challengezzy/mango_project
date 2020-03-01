/**************************************************************************
 * $RCSfile: TestUIIntercept.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:05 $
 **************************************************************************/
package smartx.publics.styletemplet.ui.templet09;

import smartx.framework.metadata.ui.*;
import smartx.publics.styletemplet.vo.*;

public class TestUIIntercept implements IUIIntercept_09 {

    public void actionAfterInsert_child(int _tabIndex, BillListPanel _billListPanel, int _newrow) throws Exception {
        // TODO Auto-generated method stub

    }

    public void actionAfterInsert_parent(BillCardPanel _billcardPanel) throws Exception {
        // TODO Auto-generated method stub

    }

    public void actionAfterUpdate_child(int _tabIndex, BillListPanel _billListPanel, String _itemkey, int _updatedrow) throws
        Exception {
        // TODO Auto-generated method stub

    }

    public void actionAfterUpdate_parent(BillCardPanel _billcardPanel, String _itemkey) throws Exception {
        // TODO Auto-generated method stub

    }

    public void actionBeforeDelete_child(int _tabIndex, BillListPanel _parentbilllistPanel, int _delerow) throws
        Exception {
        // TODO Auto-generated method stub

    }

    public void actionBeforeDelete_parent(BillListPanel _parentbilllistPanel, int _delerow) throws Exception {
        // TODO Auto-generated method stub

    }

    public void dealCommitAfterDelete(AbstractTempletFrame09 _frame, AggBillVO _insertobjs) {
        // TODO Auto-generated method stub

    }

    public void dealCommitAfterInsert(AbstractTempletFrame09 _frame, AggBillVO _insertobjs) {
        // TODO Auto-generated method stub

    }

    public void dealCommitAfterUpdate(AbstractTempletFrame09 _frame, AggBillVO _insertobjs) {
        // TODO Auto-generated method stub

    }

    public void dealCommitBeforeDelete(AbstractTempletFrame09 _frame, AggBillVO _insertobjs) throws Exception {
        System.out.println("dealCommitBeforeDelete");
        //throw new Exception("不让你删除");
    }

    public void dealCommitBeforeInsert(AbstractTempletFrame09 _frame, AggBillVO _insertobjs) throws Exception {
        // TODO Auto-generated method stub

    }

    public void dealCommitBeforeUpdate(AbstractTempletFrame09 _frame, AggBillVO _insertobjs) throws Exception {
        // TODO Auto-generated method stub

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
 * Revision 1.3  2007/03/05 09:59:13  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:48:30  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
