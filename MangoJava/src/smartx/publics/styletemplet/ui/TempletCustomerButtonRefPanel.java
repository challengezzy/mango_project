/**************************************************************************
 * $RCSfile: TempletCustomerButtonRefPanel.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:01 $
 **************************************************************************/
package smartx.publics.styletemplet.ui;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class TempletCustomerButtonRefPanel extends JPanel {
    JTable table = null;
    ArrayList customerpanel = new ArrayList();
    ChildModel model = new ChildModel();
    public TempletCustomerButtonRefPanel() {
        this.setLayout(new BorderLayout());
        this.add(getNorthPanel(), BorderLayout.NORTH);
        this.add(getCenterPanel(), BorderLayout.CENTER);
    }

    private JPanel getNorthPanel() {
        JPanel rpanel = new JPanel();
        rpanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JButton add = new JButton("新增");
        add.setPreferredSize(new Dimension(60, 20));
        JButton del = new JButton("删除");
        del.setPreferredSize(new Dimension(60, 20));
        add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onAdd();
            }
        });
        del.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onDel();
            }
        });
        rpanel.add(add);
        rpanel.add(del);
        return rpanel;
    }

    private JPanel getCenterPanel() {
        JPanel rpanel = new JPanel();
        rpanel.setLayout(new BorderLayout());
        table = new JTable();
        table.setModel(model);
        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(1).setPreferredWidth(20);
        JScrollPane jsp = new JScrollPane(table);
        rpanel.add(jsp, BorderLayout.CENTER);
        return rpanel;
    }

    private void onAdd() {
        StopEditing();
        model.addRow();
    }

    private void onDel() {
        StopEditing();
        if (table.getSelectedRowCount() == 1) {
            model.removeRow(table.getSelectedRow());
        }
    }

    public void StopEditing() {
        table.editingStopped(new ChangeEvent(table));
    }

    public String getCustomerbuttonString() {
        String result = "";
        for (int i = 0; i < customerpanel.size(); i++) {
            result = result + customerpanel.get(i) + ",";
        }
        if (!result.equals("")) {
            result = result.substring(0, result.lastIndexOf(","));
        }
        return result;
    }

    public void initValue(String _par) {
        String[] btn_action = _par.split(",");
        for (int i = 0; i < btn_action.length; i++) {
            customerpanel.add(btn_action[i]);
        }
    }

    private class ChildModel extends AbstractTableModel {

        public int getColumnCount() {
            return 2;
        }

        public int getRowCount() {
            return customerpanel.size();
        }

        public Object getValueAt(int r, int c) {
            String result = "";
            if (c == 0) {
                result = (String) customerpanel.get(r);
            } else if (c == 1) {
                return new Boolean(false);
            }
            return result;
        }

        public boolean isCellEditable(int r, int c) {
            if (c == 1) {
                return false;
            }
            return true;
        }

        public void addRow() {
            customerpanel.add("");
            table.updateUI();
        }

        public void removeRow(int r) {
            customerpanel.remove(r);
            table.updateUI();
        }

        public String getColumnName(int e) {
            String re = "";
            if (e == 0) {
                re = "类名";
            } else if (e == 1) {
                re = "是否必须";
            }
            return re;
        }

        public Class getColumnClass(int c) {
            if (c == 1) {
                return Boolean.class;
            }
            return String.class;
        }

        public void setValueAt(Object arg0, int r, int c) {
            if (c == 0) {
                if (arg0 == null) {
                    customerpanel.set(r, "");
                } else {
                    customerpanel.set(r, arg0.toString());
                }
            }
        }

    }

}
/**************************************************************************
 * $RCSfile: TempletCustomerButtonRefPanel.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:01 $
 *
 * $Log: TempletCustomerButtonRefPanel.java,v $
 * Revision 1.2  2007/05/31 07:39:01  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:14:29  qilin
 * no message
 *
 * Revision 1.2  2007/01/30 04:23:45  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/