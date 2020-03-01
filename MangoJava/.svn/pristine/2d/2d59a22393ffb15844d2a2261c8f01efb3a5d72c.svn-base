/**************************************************************************
 * $RCSfile: UIPhotoRefDialog.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:19 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentscard;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import smartx.framework.metadata.ui.*;


public class UIPhotoRefDialog extends NovaDialog {

    /**
     *
     */
    private static final long serialVersionUID = 7240993480143688006L;

    private FileListPanel flp_file = null;

    private JTextField jtf_name = null;

    private String str_init_image = null;

    private int li_close_type = -1;

    private String str_selectedicon = null;

    public UIPhotoRefDialog(Container _parent, String _name, String _refname_str) {
        super(_parent, _name, 500, 400); //
        str_init_image = _refname_str;
        initialize(); //
    }

    private void initialize() {
        this.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                if (li_close_type == -1) {
                    onBtnCancel();
                }
            }
        });
        flp_file = new FileListPanel(str_init_image);
        this.getContentPane().setBackground(new Color(240, 240, 240));
        this.getContentPane().setLayout(new BorderLayout());

        this.getContentPane().add(new JLabel(""), BorderLayout.NORTH);
        this.getContentPane().add(flp_file, BorderLayout.CENTER);
        this.getContentPane().add(getBtnPanel(), BorderLayout.SOUTH);
    }

    private JPanel getBtnPanel() {
        JButton btn_ok = new JButton("确定");
        JButton btn_cancel = new JButton("取消");

        btn_ok.setPreferredSize(new Dimension(75, 20));
        btn_cancel.setPreferredSize(new Dimension(75, 20));

        btn_ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onBtnOk();
            }
        });
        btn_cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onBtnCancel();
            }
        });

        JPanel p_btn = new JPanel();
        p_btn.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));

        p_btn.add(btn_ok);
        p_btn.add(btn_cancel);

        return p_btn;
    }

    protected void onBtnOk() {
        li_close_type = 0;
        str_selectedicon = flp_file.getSelectedIcon();
        this.dispose();
    }

    protected void onBtnCancel() {
        li_close_type = 1;
        this.dispose();
    }

    public void setFileText(String _filename) {
        this.jtf_name.setText(_filename);
    }

    public int getCloseType() {
        return li_close_type;
    }

    public String getSelectedIcon() {
        return str_selectedicon;
    }
}
/**************************************************************************
 * $RCSfile: UIPhotoRefDialog.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:19 $
 *
 * $Log: UIPhotoRefDialog.java,v $
 * Revision 1.2  2007/05/31 07:38:19  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:01:06  qilin
 * no message
 *
 * Revision 1.3  2007/02/10 08:51:57  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 05:14:30  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/