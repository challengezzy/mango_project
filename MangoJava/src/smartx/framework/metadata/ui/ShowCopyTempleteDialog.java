/**************************************************************************
 * $RCSfile: ShowCopyTempleteDialog.java,v $  $Revision: 1.3 $  $Date: 2007/07/02 00:30:27 $
 **************************************************************************/
package smartx.framework.metadata.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import smartx.framework.common.ui.*;
import smartx.framework.common.vo.*;


public class ShowCopyTempleteDialog extends NovaDialog {

    private static final long serialVersionUID = 1L;

    private JLabel table_name_Label = null;

    private JLabel templet_code_Label = null;

    private JLabel templet_name_Label = null;

    private JTextField table_name_text = null;

    private JTextField templet_code_text = null;

    private JTextField templet_name_text = null;

    private String str_oldcode = null; // 原来的模板编码!!

    protected int li_closeType;

    private JButton btn_confirm;

    private JButton btn_cancel;

    private String str_newtemplete_code;

    private String str_newtemplete_name;

    public ShowCopyTempleteDialog(Container _parent, String _oldcode) {
        super(_parent, "复制模板", 550, 300); //
        this.str_oldcode = _oldcode; //

        this.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                li_closeType = 2;
            }

            public void windowClosing(WindowEvent e) {
                li_closeType = 2;
            }
        });
        initialize(); //
    }

    private void initialize() {
        table_name_Label = new JLabel("表名：");
        templet_code_Label = new JLabel("模板编码：");
        templet_name_Label = new JLabel("模板名称：");

        table_name_Label.setHorizontalAlignment(JLabel.RIGHT);
        templet_code_Label.setHorizontalAlignment(JLabel.RIGHT);
        templet_name_Label.setHorizontalAlignment(JLabel.RIGHT);

        table_name_Label.setPreferredSize(new Dimension(80, 20));
        templet_code_Label.setPreferredSize(new Dimension(80, 20));
        templet_name_Label.setPreferredSize(new Dimension(80, 20));

        table_name_text = new JTextField();
        templet_code_text = new JTextField();
        templet_name_text = new JTextField();

        table_name_text.setPreferredSize(new Dimension(320, 20));
        templet_code_text.setPreferredSize(new Dimension(320, 20));
        templet_name_text.setPreferredSize(new Dimension(320, 20));

        table_name_text.setEditable(false);
        table_name_text.setText(str_oldcode);
        templet_code_text.setText(str_oldcode + "_1"); //
        templet_name_text.setText(str_oldcode + "_1"); //

        btn_confirm = new JButton("确定");
        btn_confirm.setPreferredSize(new Dimension(90, 20));
        btn_confirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onConfirm();
            }

        });

        btn_cancel = new JButton("取消");
        btn_cancel.setPreferredSize(new Dimension(90, 20));
        btn_cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }

        });
        Box table_name_box = Box.createHorizontalBox();
        table_name_box.add(Box.createHorizontalStrut(20));
        table_name_box.add(table_name_Label);
        table_name_box.add(table_name_text);
        table_name_box.add(Box.createGlue());

        Box templet_code_box = Box.createHorizontalBox();
        templet_code_box.add(Box.createHorizontalStrut(20));
        templet_code_box.add(templet_code_Label);
        templet_code_box.add(templet_code_text);
        templet_code_box.add(Box.createGlue());

        Box templet_name_box = Box.createHorizontalBox();
        templet_name_box.add(Box.createHorizontalStrut(20));
        templet_name_box.add(templet_name_Label);
        templet_name_box.add(templet_name_text);
        templet_name_box.add(Box.createGlue());

        JPanel center_panel = new JPanel();

        center_panel.add(table_name_box);
        center_panel.add(templet_code_box);
        center_panel.add(templet_name_box);

        Box btn_box = Box.createHorizontalBox();
        btn_box.add(Box.createHorizontalGlue());
        btn_box.add(btn_confirm);
        btn_box.add(Box.createHorizontalStrut(40));
        btn_box.add(btn_cancel);
        btn_box.add(Box.createHorizontalGlue());

        Box center_box = Box.createVerticalBox();
        center_box.add(Box.createVerticalStrut(30));
        center_box.add(table_name_box);
        center_box.add(Box.createVerticalStrut(20));
        center_box.add(templet_code_box);
        center_box.add(Box.createVerticalStrut(20));
        center_box.add(templet_name_box);
        center_box.add(Box.createVerticalStrut(40));
        center_box.add(btn_box);
        center_box.add(Box.createVerticalStrut(20));
        center_panel.add(center_box, BorderLayout.CENTER);

        this.getContentPane().add(center_panel, BorderLayout.CENTER);
    }

    protected void onCancel() {
        this.li_closeType = 1;
        this.dispose();
    }

    /**
     * 关闭
     */
    protected void onClose() {
        this.li_closeType = 2;
        this.dispose();
    }

    protected void onConfirm() {
        this.str_newtemplete_code = this.templet_code_text.getText();
        this.str_newtemplete_name = this.templet_name_text.getText();
        this.li_closeType = 0;

        if (!ensureNotEmpty()) {
            return;
        }
        String[][] str_getArray = null;
        try {
            str_getArray = UIUtil.getStringArrayByDS(null,
                "select * from PUB_TEMPLET_1 where TEMPLETCODE = '" + str_newtemplete_code.toUpperCase() + "'");
        } catch (NovaRemoteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (str_getArray.length > 0) {
            JOptionPane.showMessageDialog(this, "你要插入的模板已经存在，请重新输入模板编码!", "操作提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        this.dispose();
    }

    public String getTempleteName() {
        return this.str_newtemplete_name;
    }

    public String getTempleteCode() {
        return this.str_newtemplete_code;
    }

    public int getCloseType() {
        return this.li_closeType;
    }

    private boolean ensureNotEmpty() {
        if (str_newtemplete_code.equals("")) {
            JOptionPane.showMessageDialog(this, "模板编码名不能为空！");
            return false;
        }
        if (str_newtemplete_name.equals("")) {
            JOptionPane.showMessageDialog(this, "模板名不能为空！");
            return false;
        }
        return true;
    }
}
/**************************************************************************
 * $RCSfile: ShowCopyTempleteDialog.java,v $  $Revision: 1.3 $  $Date: 2007/07/02 00:30:27 $
 *
 * $Log: ShowCopyTempleteDialog.java,v $
 * Revision 1.3  2007/07/02 00:30:27  sunxb
 * *** empty log message ***
 *
 * Revision 1.2  2007/05/31 07:38:15  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:01:36  qilin
 * no message
 *
 * Revision 1.5  2007/03/07 02:01:54  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/03/02 05:02:48  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/02/10 08:51:58  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:56:13  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
