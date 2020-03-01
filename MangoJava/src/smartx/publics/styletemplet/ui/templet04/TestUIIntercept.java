/**************************************************************************
 * $RCSfile: TestUIIntercept.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:03 $
 **************************************************************************/
package smartx.publics.styletemplet.ui.templet04;

import smartx.framework.metadata.ui.*;
import smartx.framework.metadata.vo.*;

public class TestUIIntercept implements IUIIntercept_04 {

    public void actionAfterInsert(BillListPanel _billlistPanel, int _newrow) throws Exception {

    }

    public void actionAfterUpdate(BillListPanel _billlistPanel, int _updatedrow, String _itemkey) throws Exception {

    }

    public void actionBeforeDelete(BillListPanel _billlistPanel, int _delerow) throws Exception {

    }

    public void dealAfterCommit(BillListPanel _billlistPanel, BillVO[] _insertobjs, BillVO[] _deleteobjs,
                                BillVO[] _updateobjs) throws Exception {

    }

    public void dealBeforeCommit(BillListPanel _billlistPanel, BillVO[] _insertobjs, BillVO[] _deleteobjs,
                                 BillVO[] _updateobjs) throws Exception {

    }

}
/**************************************************************************
 * $RCSfile: TestUIIntercept.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:03 $
 *
 * $Log: TestUIIntercept.java,v $
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
