/**************************************************************************
 * $RCSfile: TestCustBtnPanel.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:02 $
 **************************************************************************/
package smartx.publics.styletemplet.ui.templet02;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import smartx.framework.metadata.ui.*;


/**
 * 在模板中注册该类,该类就是用户自定义的按钮栏!!
 * @author user
 *
 */
public class TestCustBtnPanel extends AbstractCustomerButtonBarPanel {

    private static final long serialVersionUID = 468433666101921333L;

    public void initialize() {
        this.setLayout(new FlowLayout());
        JButton btn_1 = new JButton("查询测试");
        btn_1.setPreferredSize(new Dimension(120, 20));
        btn_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onClicked_1();
            }
        });
        this.add(btn_1);
    }

    private void onClicked_1() {
        NovaMessage.show("hello");

    }
}
/**************************************************************************
 * $RCSfile: TestCustBtnPanel.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:02 $
 *
 * $Log: TestCustBtnPanel.java,v $
 * Revision 1.2  2007/05/31 07:39:02  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:14:27  qilin
 * no message
 *
 * Revision 1.3  2007/02/10 08:59:35  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:48:30  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
