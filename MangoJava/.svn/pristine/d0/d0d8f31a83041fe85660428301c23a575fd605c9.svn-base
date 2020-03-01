package smartx.framework.metadata.ui;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;

/**
 * 测试自定义面板，用于测试自定义面板使用
 * @author James.W
 *
 */
public class CustomerCtrlDemo extends CustomerCtrlDefaultImpl {
	private static final long serialVersionUID = -8149242182739601868L;

	/**
	 * 获得自定义Action数组，目的是为了创建一个工具条等类型展现。
	 * 返回的数组中如果元素为空，则表示在JToolBar中建立一个分隔。
	 * @return
	 */
	public JComponent[] getJComponentCtrls() {
		JButton btn_1 = new JButton("测试按钮");
        btn_1.setPreferredSize(new Dimension(80, 20));
        btn_1.setMargin(new Insets(0,0,0,0));
        btn_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onClicked_1();
            }
        });
        
        return new JComponent[]{btn_1};
	}
	
    private void onClicked_1() {
        NovaMessage.show(parentComp, "测试自定义界面");
    }
    
    

}


/**************************************************************************
 * $RCSfile: CustomerCtrlDemo.java,v $  $Revision: 1.1.2.1 $  $Date: 2009/12/21 02:57:27 $
 * $Log: CustomerCtrlDemo.java,v $
 * Revision 1.1.2.1  2009/12/21 02:57:27  wangqi
 * *** empty log message ***
 *
 * Revision 1.1.2.2  2009/12/17 02:47:54  wangqi
 * *** empty log message ***
 *
 **************************************************************************/