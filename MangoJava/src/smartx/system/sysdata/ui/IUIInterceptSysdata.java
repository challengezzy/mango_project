package smartx.system.sysdata.ui;

import smartx.framework.metadata.ui.*;
import smartx.framework.metadata.vo.*;
import smartx.publics.styletemplet.ui.templet07.*;
import smartx.system.common.constant.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class IUIInterceptSysdata implements IUIIntercept_07 {
    public IUIInterceptSysdata() {
    }

    public void actionBeforeDelete(BillListPanel _billlistPanel, int _delerow) throws Exception {
    }

    public void actionAfterInsert(BillCardPanel _billCardPanel) throws Exception {
    }

    public void actionAfterUpdate(BillCardPanel _billCardPanel, String _itemkey) throws Exception {
    }

    public void dealCommitBeforeInsert(AbstractTempletFrame07 _frame, BillVO _insertobjs) throws Exception {
    }

    public void dealCommitAfterInsert(AbstractTempletFrame07 _frame, BillVO _insertobjs) {
    }

    public void dealCommitBeforeDelete(AbstractTempletFrame07 _frame, BillVO _deleteobjs) throws Exception {
    }

    public void dealCommitAfterDelete(AbstractTempletFrame07 _frame, BillVO _deleteobjs) {
    }

    public void dealCommitBeforeUpdate(AbstractTempletFrame07 _frame, BillVO _updateobjs) throws Exception {
    }

    public void dealCommitAfterUpdate(AbstractTempletFrame07 _frame, BillVO _updateobjs) {
    }

    public void actionMouseClickOnPriTable(AbstractTempletFrame07 _frame) {
        if (_frame.getPriTable().getSelectedRowCount() == 1) {
            Object modifyType = _frame.getPritable().getBillVO(_frame.getPriTable().getSelectedRow()).getRealValue(
                "MODIFYTYPE");
            //系统字典不可维护
            if (modifyType != null) {
                if (new Integer( (String) modifyType).intValue() ==
                    CommonSysConst.SYSDICTIONARYCATEGORY_MODIFYTYPE_UNMAINTAIN) {
                    _frame.setButtonStatus(AbstractTempletFrame07.BTN_INSERT_INDEX |
                                           AbstractTempletFrame07.BTN_UPDATE_INDEX |
                                           AbstractTempletFrame07.BTN_DELETE_INDEX, false);
                } else {
                    _frame.setButtonStatus(AbstractTempletFrame07.BTN_INSERT_INDEX |
                                           AbstractTempletFrame07.BTN_UPDATE_INDEX, true);
                }
            }
        }

    }

    public void actionMouseClickOnSubTable(AbstractTempletFrame07 _frame) {
        if (_frame.getSubTable().getSelectedRowCount() == 1) {
            Object modifyType = _frame.getSubtable().getBillVO(_frame.getSubTable().getSelectedRow()).getRealValue(
                "TYPE");
            //系统字典不可维护
            if (modifyType != null) {
                if (new Integer( (String) modifyType).intValue() ==
                    CommonSysConst.SYSDICTIONARY_TYPE_SYSTEM) {
                    _frame.setButtonStatus(AbstractTempletFrame07.BTN_UPDATE_INDEX |
                                           AbstractTempletFrame07.BTN_DELETE_INDEX, false);
                } else {
                    _frame.setButtonStatus(AbstractTempletFrame07.BTN_UPDATE_INDEX, true);
                }
            }
        }
    }
}
/***********************************************************************
 * $RCSfile: IUIInterceptSysdata.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:41:34 $
 * $Log: IUIInterceptSysdata.java,v $
 * Revision 1.2  2007/05/31 07:41:34  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:22:10  qilin
 * no message
 *
 * Revision 1.2  2007/03/22 01:46:37  qilin
 * no message
 *
 * Revision 1.1  2007/03/07 02:51:10  qilin
 * no message
 *
 *************************************************************************/
