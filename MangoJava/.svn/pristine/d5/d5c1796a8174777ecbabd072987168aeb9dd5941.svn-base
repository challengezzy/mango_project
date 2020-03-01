/**
 * 
 */
package smartx.framework.metadata.ui;

import javax.swing.Action;
import javax.swing.JComponent;

/**
 * Card面板自定义控制接口
 * 如果需要在合适的地方自定义设计一些控制（一般是按钮），可以实现本接口。
 * 1、根据需要给出getActions()和getComponents()的返回值，其中前者比后者的优先级高。
 * 2、调用的时候需要首先设置setParentCtrl()方法设置调用本自定义控制的调用控件。
 * 3、根据需要在本接口实现类内部可以调用getParentCtrl()获得所在调用控件，并根据需要转型。
 * @author James.W
 * @see CustomerCtrlDefaultImpl
 */
public interface CustomerCtrlIFC {
	
	/**
	 * 获得自定义控件数组，目的是为了创建一个工具条等类型展现。
	 * 返回的数组中如果元素为空，则表示在JToolBar中建立一个分隔。
	 * @return
	 */
	public JComponent[] getJComponentCtrls();
	
	/**
	 * 获得自定义Action数组，目的是为了创建一个工具条等类型展现。
	 * 返回的数组中如果元素为空，则表示在JToolBar中建立一个分隔。
	 * @return
	 */
	public Action[] getActionCtrls();
	
	/**
	 * 设置所在组件
	 * @param jcomp
	 */
	public void setParentCtrl(JComponent jcomp);
	
	/**
	 * 获得所在组件
	 * @return
	 */
	public JComponent getParentCtrl();
}

/**************************************************************************
 * $RCSfile: CustomerCtrlIFC.java,v $  $Revision: 1.1.2.1 $  $Date: 2009/12/21 02:57:27 $
 * $Log: CustomerCtrlIFC.java,v $
 * Revision 1.1.2.1  2009/12/21 02:57:27  wangqi
 * *** empty log message ***
 *
 **************************************************************************/
