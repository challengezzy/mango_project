/**************************************************************************
 * $RCSfile: Templet9RefChildPanel.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:05 $
 **************************************************************************/
package smartx.publics.styletemplet.ui.templet09;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import smartx.framework.common.ui.*;
import smartx.framework.metadata.ui.*;


public class Templet9RefChildPanel extends JPanel {
    JTable table = null;

    ArrayList templetcode = new ArrayList();

    ArrayList pk = new ArrayList();

    ArrayList forpk = new ArrayList();

    ChildModel model = new ChildModel();

    public Templet9RefChildPanel() {
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
        Templet9RefChildCellEditor editor = new Templet9RefChildCellEditor();
        table.getColumnModel().getColumn(0).setCellEditor(editor);
        table.getColumnModel().getColumn(1).setCellEditor(editor);
        table.getColumnModel().getColumn(2).setCellEditor(editor);
        JScrollPane jsp = new JScrollPane(table);
        rpanel.add(jsp, BorderLayout.CENTER);
        return rpanel;
    }

    private void onAdd() {
        table.editingStopped(new ChangeEvent(table));
        model.addRow();
    }

    public void stopEditing() {
        table.editingStopped(new ChangeEvent(table));
    }

    private void onDel() {
        table.editingStopped(new ChangeEvent(table));
        if (table.getSelectedRowCount() == 1) {
            model.removeRow(table.getSelectedRow());
        }
    }

    public String getTempletcode() {
        String result = "";
        for (int i = 0; i < templetcode.size(); i++) {
            if (pk.get(i).equals(""))

            {
                NovaMessage.show("未完成全部子表设置", NovaConstants.MESSAGE_ERROR);
                return "";
            }
            result += (String) templetcode.get(i) + ",";
        }
        result = result.substring(0, result.length() - 1);
        return result;

    }

    public String getPk() {
        String result = "";
        for (int i = 0; i < pk.size(); i++) {
            if (pk.get(i).equals(""))

            {
                NovaMessage.show("未完成全部子表设置", NovaConstants.MESSAGE_ERROR);
                return "";
            }
            result += (String) pk.get(i) + ",";
        }
        result = result.substring(0, result.length() - 1);
        return result;

    }

    public String getForpk() {
        String result = "";
        for (int i = 0; i < forpk.size(); i++) {
            if (pk.get(i).equals(""))

            {
                NovaMessage.show("未完成全部子表设置", NovaConstants.MESSAGE_ERROR);
                return "";
            }
            result += (String) forpk.get(i) + ",";
        }
        result = result.substring(0, result.length() - 1);
        return result;

    }

    public void addTempletcode(String code) {
        this.templetcode.add(code);
    }

    public void addPk(String code) {
        this.pk.add(code);
    }

    public void addForpk(String code) {
        this.forpk.add(code);
    }

    private class ChildModel extends AbstractTableModel {

        public int getColumnCount() {
            return 3;
        }

        public int getRowCount() {
            return pk.size();
        }

        public Object getValueAt(int r, int c) {
            String result = "";
            if (c == 0) {
                result = (String) templetcode.get(r);
            } else if (c == 1) {
                result = (String) pk.get(r);
            } else if (c == 2) {
                result = (String) forpk.get(r);
            }
            return result;
        }

        public boolean isCellEditable(int r, int c) {
            return true;
        }

        public void addRow() {
            templetcode.add("");
            pk.add("");
            forpk.add("");
            table.updateUI();
        }

        public void removeRow(int r) {
            templetcode.remove(r);
            pk.remove(r);
            forpk.remove(r);
            table.updateUI();
        }

        public String getColumnName(int e) {
            String re = "";
            if (e == 0) {
                re = "模板名";
            } else if (e == 1) {
                re = "子表主键";
            } else if (e == 2) {
                re = "子表外键";
            }
            return re;
        }

        public void setValueAt(Object arg0, int r, int c) {
            if (c == 0) {
                if (arg0 == null) {
                    templetcode.set(r, "");
                } else {
                    templetcode.set(r, arg0.toString());
                }
            } else if (c == 1) {
                if (arg0 == null) {
                    pk.set(r, "");
                } else {
                    pk.set(r, arg0.toString());
                }
            } else if (c == 2) {
                if (arg0 == null) {
                    forpk.set(r, "");
                } else {
                    forpk.set(r, arg0.toString());
                }
            }
        }

    }

}
/**************************************************************************
 * $RCSfile: Templet9RefChildPanel.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:05 $
 *
 * $Log: Templet9RefChildPanel.java,v $
 * Revision 1.2  2007/05/31 07:39:05  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:14:28  qilin
 * no message
 *
 * Revision 1.3  2007/02/10 08:59:36  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:48:31  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/