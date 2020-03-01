/**************************************************************************
 * $RCSfile: AbstractListCustomerButtonBarPanel.java,v $  $Revision: 1.2.8.1 $  $Date: 2009/12/21 02:57:27 $
 **************************************************************************/
package smartx.framework.metadata.ui;

//
import java.awt.*;

import javax.swing.*;

/**
 * 客户自定义按钮栏类,即有一个地方可以让用户放自己的一些按钮或其他控件,一般都是按钮为主!!所以叫按钮栏类
 *
 * 所有的风格中都有一项可以注册该类的一个子类,然后在所有风格模板中如果发现注册了该类,那么就创建该类实例,
 * 然后调用setParentFrame()方法将风格模板类本身句柄传进来,然后调用抽象方法initialize();初始化该类!!
 *
 * 在initialize()方法中客户可以自由发挥随便加入自己的什么控件,比如按钮,输入框,然后随便进行自己的事件处理,
 * 因为只要他能够通过getParentFrame()方法得到父界面,并通过父界面从而得到页面上任何一个控件句柄,他就能够干一切他想所干的!!!
 * @author user
 *
 */
public abstract class AbstractListCustomerButtonBarPanel extends JPanel implements CustomerCtrlIFC {

    private BillListPanel billListPanel = null;

    public AbstractListCustomerButtonBarPanel() {
    	FlowLayout flayout = new FlowLayout(FlowLayout.LEFT);
        flayout.setHgap(0);
        flayout.setVgap(0);
        this.setLayout(flayout);
    }

    /**
     * 初始化页面
     *
     */
    public abstract void initialize();

    public BillListPanel getBillListPanel() {
        return billListPanel;
    }

    public void setBillListPanel(BillListPanel billListPanel) {
        this.billListPanel = billListPanel;
    }
    
    
    
    /**
	 * 获得自定义控件数组，目的是为了创建一个工具条等类型展现。
	 * 返回的数组中如果元素为空，则表示在JToolBar中建立一个分隔。
	 * @return
	 */
	public Action[] getActionCtrls() {
		return null;
	}

	/**
	 * 获得自定义Action数组，目的是为了创建一个工具条等类型展现。
	 * 返回的数组中如果元素为空，则表示在JToolBar中建立一个分隔。
	 * @return
	 */
	public JComponent[] getJComponentCtrls() {
		return null;
	}

	/**
	 * 获得所在组件
	 * @return
	 */
	public JComponent getParentCtrl() {
		return parentComp;
	}

	/**
	 * 设置所在组件
	 * @param jcomp
	 */
	public void setParentCtrl(JComponent jcomp) {
		parentComp=jcomp;
	}
	protected JComponent parentComp=null;
    
    
    

}
/**************************************************************************
 * $RCSfile: AbstractListCustomerButtonBarPanel.java,v $  $Revision: 1.2.8.1 $  $Date: 2009/12/21 02:57:27 $
 *
 * $Log: AbstractListCustomerButtonBarPanel.java,v $
 * Revision 1.2.8.1  2009/12/21 02:57:27  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2007/05/31 07:38:14  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:01:37  qilin
 * no message
 *
 * Revision 1.2  2007/01/30 04:56:14  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/