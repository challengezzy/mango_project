/**************************************************************************
 * $RCSfile: TestUIIntercept.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:02 $
 **************************************************************************/
package smartx.publics.styletemplet.ui.templet01;

import smartx.framework.metadata.ui.*;
import smartx.framework.metadata.vo.*;

public class TestUIIntercept implements IUIIntercept_01 {

    public void actionBeforeInsert(BillListPanel _billlistPanel, int _newrow) throws Exception {
        System.out.println("测试模板一UI拦截器，新增之前");
//		throw new Exception("测试模板一UI拦截器报错，新增之前");
    }

    public void actionBeforeUpdate(BillListPanel _billlistPanel, int _updatedrow, String _itemkey) throws Exception {
        System.out.println("测试模板一UI拦截器，更新之前");
//		throw new Exception("测试模板一UI拦截器报错，更新之前");
    }

    public void actionBeforeDelete(BillListPanel _billlistPanel, int _delerow) throws Exception {
        System.out.println("测试模板一UI拦截器，删除之前");
//		throw new Exception("测试模板一UI拦截器报错，删除之前");
    }

    public void dealAfterCommit(BillListPanel _billlistPanel, BillVO[] _insertobjs, BillVO[] _deleteobjs,
                                BillVO[] _updateobjs) throws Exception {
        System.out.println("测试模板一UI拦截器，保存之后");
//		throw new Exception("测试模板一UI拦截器报错，保存之后");
    }

    public void dealBeforeCommit(BillListPanel _billlistPanel, BillVO[] _insertobjs, BillVO[] _deleteobjs,
                                 BillVO[] _updateobjs) throws Exception {
        System.out.println("测试模板一UI拦截器，保存之前");
//		throw new Exception("测试模板一UI拦截器报错，保存之前");
    }

}
/**************************************************************************
 * $RCSfile: TestUIIntercept.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:02 $
 *
 * $Log: TestUIIntercept.java,v $
 * Revision 1.2  2007/05/31 07:39:02  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:14:26  qilin
 * no message
 *
 * Revision 1.2  2007/01/30 04:48:33  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
