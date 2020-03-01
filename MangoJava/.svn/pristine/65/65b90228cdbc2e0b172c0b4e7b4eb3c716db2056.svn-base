/**************************************************************************
 * $RCSfile: TestUIIntercept.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:04 $
 **************************************************************************/
package smartx.publics.styletemplet.ui.templet07;

import smartx.framework.metadata.ui.*;
import smartx.framework.metadata.vo.*;

public class TestUIIntercept implements IUIIntercept_07 {

    public void actionBeforeDelete(BillListPanel _billlistPanel, int _delerow) throws Exception {
        //System.out.println("执行了删除前的拦截!!"); //
    }

    public void actionAfterInsert(BillCardPanel _billCardPanel) throws Exception {
        //_billCardPanel.setCompentObjectValue("NAME", "欢迎光临!!"); //
    }

    public void actionAfterUpdate(BillCardPanel _billCardPanel, String _itemkey) throws Exception {
        //System.out.println("[" + _itemkey + "]发生变化!!"); //
        if (_itemkey.equals("CODE")) {
            System.out.println("[" + _itemkey + "]发生变化!!"); //
            //_billCardPanel.setCompentObjectValue("CERT_CODE", "789"); //
        }
    }

    public void dealCommitAfterDelete(AbstractTempletFrame07 _frame, BillVO _deleteobjs) {

    }

    public void dealCommitAfterInsert(AbstractTempletFrame07 _frame, BillVO _insertobjs) {

    }

    public void dealCommitAfterUpdate(AbstractTempletFrame07 _frame, BillVO _updateobjs) {
        System.out.println("执行了保存提交扣的校验!!"); ////
    }

    public void dealCommitBeforeDelete(AbstractTempletFrame07 _frame, BillVO _deleteobjs) throws Exception {

    }

    public void dealCommitBeforeInsert(AbstractTempletFrame07 _frame, BillVO _insertobjs) throws Exception {
        //throw new Exception("执行了新增提交前的校验!!");
    }

    public void dealCommitBeforeUpdate(AbstractTempletFrame07 _frame, BillVO _updateobjs) throws Exception {
        //throw new Exception("执行了保存提交前的校验!!");
    }

    public void actionMouseClickOnPriTable(AbstractTempletFrame07 _frame) {
    }

    public void actionMouseClickOnSubTable(AbstractTempletFrame07 _frame) {
    }

}
/**************************************************************************
 * $RCSfile: TestUIIntercept.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:04 $
 *
 * $Log: TestUIIntercept.java,v $
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
