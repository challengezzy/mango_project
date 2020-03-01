/**************************************************************************
 * $RCSfile: ShowExistTempleteCodeDialog.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:16 $
 **************************************************************************/
package smartx.framework.metadata.ui;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class ShowExistTempleteCodeDialog extends NovaDialog {

    /**
     *
     */
    private static final long serialVersionUID = 6700062011846933715L;

    private BillListPanel listPanel_main;

    private BillListPanel listPanel_child;

    private String str_condition = "";

    private String str_columnname = "";

    private String str_sql = null;

    private String str_selected_code = null;

    private String str_selected_id = null;

    public ShowExistTempleteCodeDialog(Container _parent, String _name) {
        super(_parent, _name, 550, 300); //
        str_sql = "select * from pub_templet_1";
        initialize();
    }

    public ShowExistTempleteCodeDialog(Container _parent, String _name,
                                       String _column_name, String _condition) {
        super(_parent, _name, 750, 500); //
        this.str_columnname = _column_name;
        this.str_condition = _condition;
        initialize();
    }

    public ShowExistTempleteCodeDialog(Container _parent, String _name,
                                       Hashtable _ht_condition) {
        super(_parent, _name, 750, 500); //
        String str_sql_end = "";
        String str_table = (String) _ht_condition.get("TABLENAME");
        String str_code = (String) _ht_condition.get("TEMPLETCODE");

        if (str_table != null && !str_table.equals("")) {
            str_sql_end = str_sql_end + " And TABLENAME='" + str_table + "'";
        }
        if (str_code != null && !str_code.equals("")) {
            str_sql_end = str_sql_end + " And TEMPLETCODE='" + str_code + "'";
        }
        str_sql = "select * from pub_templet_1 where 1=1" + str_sql_end;
        initialize();
    }

    private void initialize() {
        this.getContentPane().setLayout(new BorderLayout());

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                                              getMainPanel(), getChildPanel());
        splitPane.setDividerLocation(200);
        splitPane.setDividerSize(10);
        splitPane.setOneTouchExpandable(true);

        this.getContentPane().add(splitPane, BorderLayout.CENTER);
        this.getContentPane().add(getSouthPanel(), BorderLayout.SOUTH);
    }

    private Component getSouthPanel() {
        // TODO Auto-generated method stub
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JButton btn_confirm = new JButton("确定");
        btn_confirm.setPreferredSize(new Dimension(100, 20));
        btn_confirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onConfirm();
            }
        });

        panel.add(btn_confirm);
        return panel;
    }

    protected void onConfirm() {
        // TODO Auto-generated method stub
        int li_count = listPanel_main.getSelectedRow();
        if (li_count >= 0) {
            str_selected_id = (String) listPanel_main.getTable().getModel().getValueAt(li_count, 1);
            str_selected_code = (String) listPanel_main.getTable().getModel().getValueAt(li_count, 2);

        }
        this.dispose();
    }

    private BillListPanel getChildPanel() {
        // TODO Auto-generated method stub
        if (listPanel_child == null) {
            listPanel_child = new BillListPanel("PUB_TEMPLET_1_ITEM", false,
                                                false); //
            listPanel_child.initialize();
        }
        return listPanel_child;
    }

    private BillListPanel getMainPanel() {
        // TODO Auto-generated method stub
        if (listPanel_main == null) {
            listPanel_main = new BillListPanel("PUB_TEMPLET_1", false, false); //
            listPanel_main.initialize();
            if (str_sql == null) {
                str_sql = listPanel_main.getSQL(" " + str_columnname
                                                + " = '" + str_condition.trim().toUpperCase()
                                                + "' order by TEMPLETCODE asc");
            }
            listPanel_main.QueryData(str_sql);
            listPanel_main.getTable().getSelectionModel()
                .addListSelectionListener(new ListSelectionListener() {

                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting()) {
                        onRowSelectChanged();
                    }
                }
            });
        }
        return listPanel_main;
    }

    protected void onRowSelectChanged() {
        // TODO Auto-generated method stub

        int li_row = getMainPanel().getSelectedRow(); // 取得选中的行
        if (li_row < 0) {
            return;
        }
        String str_pk = (String) getMainPanel().getRealValueAtModel(li_row,
            "PK_PUB_TEMPLET_1"); // 取得主键值
        // System.out.println(str_pk);
        String str_sql = getChildPanel().getSQL(
            " 1=1 and PK_PUB_TEMPLET_1='" + str_pk
            + "' order by showorder asc ");
        getChildPanel().QueryData(str_sql);
    }

    public String getSelectedRowCode() {
        return str_selected_code;
    }

    public String getSelectedRowCodeID() {
        return str_selected_id;
    }
}
/**************************************************************************
 * $RCSfile: ShowExistTempleteCodeDialog.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:16 $
 *
 * $Log: ShowExistTempleteCodeDialog.java,v $
 * Revision 1.2  2007/05/31 07:38:16  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:01:36  qilin
 * no message
 *
 * Revision 1.3  2007/02/10 08:51:58  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:56:14  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/