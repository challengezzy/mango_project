/**************************************************************************
 * $RCSfile: IUIIntercept_08.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:05 $
 **************************************************************************/
package smartx.publics.styletemplet.ui.templet08;

import smartx.framework.metadata.ui.*;
import smartx.publics.styletemplet.vo.*;

/**
 * 主子表的前端Intercept!!
 * @author user
 *
 */
public interface IUIIntercept_08 {

    //主表删除前做的动作
    public void actionBeforeDelete_parent(BillListPanel _parentbilllistPanel, int _delerow) throws Exception;

    //子表删除前做的动作
    public void actionBeforeDelete_child(BillListPanel _parentbilllistPanel, int _delerow) throws Exception;

    //主表新增后做的动作,新增时主表是卡片
    public void actionAfterInsert_parent(BillCardPanel _billcardPanel) throws Exception;

    //子表新增后做的动作,新增时子表是列表!!
    public void actionAfterInsert_child(BillListPanel _billListPanel, int _newrow) throws Exception;

    //主表修改后做的动作,修改时主表是卡片!!!
    public void actionAfterUpdate_parent(BillCardPanel _billcardPanel, String _itemkey) throws Exception; //

    //子表修改后做的动作!!
    public void actionAfterUpdate_child(BillListPanel _billListPanel, String _itemkey, int _updatedrow) throws
        Exception; //

    //新增前处理
    public void dealCommitBeforeInsert(smartx.publics.styletemplet.ui.templet08.AbstractTempletFrame08 _frame,
                                       AggBillVO _insertobjs) throws Exception; //

    //新增后处理
    public void dealCommitAfterInsert(smartx.publics.styletemplet.ui.templet08.AbstractTempletFrame08 _frame,
                                      AggBillVO _insertobjs); //

    //删除前处理
    public void dealCommitBeforeDelete(smartx.publics.styletemplet.ui.templet08.AbstractTempletFrame08 _frame,
                                       AggBillVO _insertobjs) throws Exception; //

    //删除后处理
    public void dealCommitAfterDelete(smartx.publics.styletemplet.ui.templet08.AbstractTempletFrame08 _frame,
                                      AggBillVO _insertobjs); //

    //修改前处理
    public void dealCommitBeforeUpdate(smartx.publics.styletemplet.ui.templet08.AbstractTempletFrame08 _frame,
                                       AggBillVO _insertobjs) throws Exception; //

    //修改后处理
    public void dealCommitAfterUpdate(smartx.publics.styletemplet.ui.templet08.AbstractTempletFrame08 _frame,
                                      AggBillVO _insertobjs); //

}
/**************************************************************************
 * $RCSfile: IUIIntercept_08.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:05 $
 *
 * $Log: IUIIntercept_08.java,v $
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