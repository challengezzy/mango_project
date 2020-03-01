/**************************************************************************
 * $RCSfile: IUIIntercept_09.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:05 $
 **************************************************************************/
package smartx.publics.styletemplet.ui.templet09;

import smartx.framework.metadata.ui.*;
import smartx.publics.styletemplet.vo.*;

/**
 * 主子表的前端Intercept!!
 * @author user
 *
 */
public interface IUIIntercept_09 {

    //主表删除前做的动作
    public void actionBeforeDelete_parent(BillListPanel _parentbilllistPanel, int _delerow) throws Exception;

    //子表删除前做的动作,_tabIndex是子表各页签的顺序,从1开始,分别是1,2,3,4,5
    public void actionBeforeDelete_child(int _tabIndex, BillListPanel _childbilllistPanel, int _delerow) throws
        Exception;

    //主表新增后做的动作,新增时主表是卡片
    public void actionAfterInsert_parent(BillCardPanel _billcardPanel) throws Exception;

    //子表新增后做的动作,新增时子表是列表!!,_tabIndex是子表各页签的顺序,从1开始,分别是1,2,3,4,5
    public void actionAfterInsert_child(int _tabIndex, BillListPanel _billListPanel, int _newrow) throws Exception;

    //主表修改后做的动作,修改时主表是卡片!!!
    public void actionAfterUpdate_parent(BillCardPanel _billcardPanel, String _itemkey) throws Exception; //

    //子表修改后做的动作!!,_tabIndex是子表各页签的顺序,从1开始,分别是1,2,3,4,5
    public void actionAfterUpdate_child(int _tabIndex, BillListPanel _billListPanel, String _itemkey, int _updatedrow) throws
        Exception; //

    //新增前处理
    public void dealCommitBeforeInsert(smartx.publics.styletemplet.ui.templet09.AbstractTempletFrame09 _frame,
                                       AggBillVO _insertobjs) throws Exception; //

    //新增后处理
    public void dealCommitAfterInsert(smartx.publics.styletemplet.ui.templet09.AbstractTempletFrame09 _frame,
                                      AggBillVO _insertobjs); //

    //删除前处理
    public void dealCommitBeforeDelete(smartx.publics.styletemplet.ui.templet09.AbstractTempletFrame09 _frame,
                                       AggBillVO _insertobjs) throws Exception; //

    //删除后处理
    public void dealCommitAfterDelete(smartx.publics.styletemplet.ui.templet09.AbstractTempletFrame09 _frame,
                                      AggBillVO _insertobjs); //

    //修改前处理
    public void dealCommitBeforeUpdate(smartx.publics.styletemplet.ui.templet09.AbstractTempletFrame09 _frame,
                                       AggBillVO _insertobjs) throws Exception; //

    //修改后处理
    public void dealCommitAfterUpdate(smartx.publics.styletemplet.ui.templet09.AbstractTempletFrame09 _frame,
                                      AggBillVO _insertobjs); //

}
/**************************************************************************
 * $RCSfile: IUIIntercept_09.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:05 $
 *
 * $Log: IUIIntercept_09.java,v $
 * Revision 1.2  2007/05/31 07:39:05  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:14:29  qilin
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