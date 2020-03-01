/**************************************************************************
 * $RCSfile: ShowServerEnvDialog.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:16 $
 **************************************************************************/
package smartx.framework.metadata.ui;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import smartx.framework.common.ui.*;


public class ShowServerEnvDialog extends NovaDialog {

    private static final long serialVersionUID = 8573901930687178737L;

    public ShowServerEnvDialog(Container _parent) {
        super(_parent, "查看服务器环境变量", 800, 600);
        initialize(); //
    }

    private void initialize() {
        JTabbedPane tabPane = new JTabbedPane(); //
        tabPane.addTab("SystemProperties", getSystemProteriesPanel());
        tabPane.addTab("ServerEnvironment", getClientEnvironmentPanel());

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(tabPane, BorderLayout.CENTER); //
        this.getContentPane().add(getSouthPane(), BorderLayout.SOUTH); //

        this.setVisible(true);
    }

    private JPanel getSouthPane() {
        JPanel panel = new JPanel(); //
        panel.setLayout(new FlowLayout());
        JButton btn_confirm = new JButton("确定"); //
        btn_confirm.setPreferredSize(new Dimension(100, 20)); //
        btn_confirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ShowServerEnvDialog.this.dispose(); //
            }
        });
        panel.add(btn_confirm);
        return panel;
    }

    private JPanel getSystemProteriesPanel() {
        JPanel panel = new JPanel(); //
        panel.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        Properties pro = null;
        try {
            HashMap mapReturn = UIUtil.commMethod("smartx.framework.metadata.bs.NovaBSCommUtil",
                                                  "getSystemProperties", null); //
            pro = (Properties) mapReturn.get("return_1");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (pro != null) {
            String[] str_keys = (String[]) pro.keySet().toArray(new String[0]);
            Arrays.sort(str_keys); //
            for (int i = 0; i < str_keys.length; i++) {
                if (pro.getProperty(str_keys[i]).endsWith("\r") || pro.getProperty(str_keys[i]).endsWith("\n")) {
                    textArea.append(str_keys[i] + " = " + pro.getProperty(str_keys[i])); //
                } else {
                    textArea.append(str_keys[i] + " = " + pro.getProperty(str_keys[i]) + "\r\n"); //
                }
            }
        }

        panel.add(new JScrollPane(textArea));

        return panel;
    }

    private JPanel getClientEnvironmentPanel() {
        JPanel panel = new JPanel(); //
        panel.setLayout(new BorderLayout());

        String[] str_title = new String[] {"key", "说明", "value"};
        String[][] str_data = null;
        try {
            HashMap mapReturn = UIUtil.commMethod("smartx.framework.metadata.bs.NovaBSCommUtil", "getServerEnvData", null); //
            str_data = (String[][]) mapReturn.get("return_1"); //
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (str_data != null) {
            JTable table = new JTable(str_data, str_title);
            panel.add(new JScrollPane(table));
        }

        return panel;
    }

}
/**************************************************************************
 * $RCSfile: ShowServerEnvDialog.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:16 $
 *
 * $Log: ShowServerEnvDialog.java,v $
 * Revision 1.2  2007/05/31 07:38:16  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:01:36  qilin
 * no message
 *
 * Revision 1.4  2007/02/10 08:51:58  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/01/30 08:48:10  sunxf
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:56:14  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/