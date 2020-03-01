/**************************************************************************
 * $RCSfile: TestCustBtnPanel.java,v $  $Revision: 1.1 $  $Date: 2007/09/11 07:54:18 $
 **************************************************************************/
package smartx.publics.print;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

import javax.swing.*;

import smartx.framework.metadata.ui.*;
import smartx.publics.print.ui.PrintProvider;
import smartx.publics.styletemplet.ui.templet02.AbstractTempletFrame02;


/**
 * 在模板中注册该类,该类就是用户自定义的按钮栏!!
 * @author user
 *
 */
public class TestCustBtnPanel extends AbstractCustomerButtonBarPanel {

    private static final long serialVersionUID = 468433666101921333L;

    public void initialize() {
        this.setLayout(new FlowLayout());
        JButton btn_1 = new JButton("打印测试");
        btn_1.setPreferredSize(new Dimension(120, 20));
        btn_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onClicked_1();
            }
        });
        this.add(btn_1);
    }

    private void onClicked_1() {
    	BillListPanel list = ((AbstractTempletFrame02)super.getParentFrame()).getBillListPanel();
    	PrintProvider provider = new PrintProvider();
    	int[] rows = list.getTable().getSelectedRows();
    	int[] data = new int[rows.length];
    	for (int i = 0; i < rows.length; i++) {
			data[i]=Integer.parseInt(list.getValueAt(rows[i],list.getTempletVO().getPkname()).toString());
		}
        provider.print(list.getTempletVO().getTempletcode(),data, list.getTempletVO().getPkname());

    }
}
/**************************************************************************
 * $RCSfile: TestCustBtnPanel.java,v $  $Revision: 1.1 $  $Date: 2007/09/11 07:54:18 $
 *
 * $Log: TestCustBtnPanel.java,v $
 * Revision 1.1  2007/09/11 07:54:18  john_liu
 * no message
 *
 * Revision 1.1  2007/07/02 03:08:09  sunxf
 * *** empty log message ***
 *
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
