/**************************************************************************
 * $RCSfile: RecordShowDialog.java,v $  $Revision: 1.3 $  $Date: 2007/07/19 02:24:48 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentscard;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import smartx.framework.common.ui.*;
import smartx.framework.metadata.ui.*;
import smartx.framework.metadata.vo.*;


public class RecordShowDialog extends NovaDialog {
    private static final long serialVersionUID = 1L;

    private String dataSourceName = null;

    private String str_tablename = null;

    private String str_pkvalue = null;

    private String[] itemkeys = null;

    private String[] itemvalue = null;

    private String str_pkname = null;

    private JTable table = null;

    private TableDataStruct tabledata = null;

    private Color color = new Color(240, 240, 240);

    public RecordShowDialog(BillListPanel _parent, String _tablename, String _pkname, String _pkvalue) throws Exception {
        super(_parent);
        this.dataSourceName = _parent.getDataSourceName(); //取得数据源名称
        this.str_tablename = _tablename;
        this.str_pkvalue = _pkvalue;
        this.str_pkname = _pkname;
        this.setTitle("明细");
        this.setSize(665, 360);
        this.setLocation(200, 100);
        this.setModal(true);
        initialize();
    }

    public RecordShowDialog(BillCardPanel _parent, String _tablename, String _pkname, String _pkvalue) throws Exception {
        super(_parent);
        this.dataSourceName = _parent.getDataSourceName(); //取得数据源名称
        this.str_tablename = _tablename;
        this.str_pkvalue = _pkvalue;
        this.str_pkname = _pkname;
        this.setTitle("明细");
        this.setSize(665, 360);
        this.setLocation(200, 100);
        this.setModal(true);
        initialize();
    }

    public RecordShowDialog(BillListPanel _parent, String _tablename, String[] _itemkeys, String[] _itemvalue) throws
        Exception {
        super(_parent);
        this.dataSourceName = _parent.getDataSourceName(); //取得数据源名称
        this.str_tablename = _tablename;
        this.itemkeys = _itemkeys;
        this.itemvalue = _itemvalue;
        this.setTitle("明细");
        this.setSize(665, 360);
        this.setLocation(200, 100);
        this.setModal(true);
        initialize();
    }

    public RecordShowDialog(BillCardPanel _parent, String _tablename, String[] _itemkeys, String[] _itemvalue) throws
        Exception {
        super(_parent);
        this.dataSourceName = _parent.getDataSourceName(); //取得数据源名称
        this.str_tablename = _tablename;
        this.itemkeys = _itemkeys;
        this.itemvalue = _itemvalue;
        this.setTitle("明细");
        this.setSize(665, 360);
        this.setLocation(200, 100);
        this.setModal(true);
        initialize();
    }

    private void initialize() throws Exception {
        this.getContentPane().setLayout(new BorderLayout());
        StringBuffer str_sql = new StringBuffer();
        if (str_pkname == null) {
            str_sql.append("SELECT * FROM  " + str_tablename + " WHERE ");
            for (int i = 0; i < itemkeys.length; i++) {
            	if(itemvalue[i]==null||itemvalue[i].equalsIgnoreCase("null")||itemvalue[i].equals(""))
            		continue;
                if (i == itemkeys.length - 1) {
                    str_sql.append("" + itemkeys[i] + "='" + itemvalue[i] + "'");
                } else {
                    str_sql.append("" + itemkeys[i] + "='" + itemvalue[i] + "' AND ");
                }
            }
        } else {
            str_sql.append("SELECT * FROM " + str_tablename + " WHERE " + str_pkname + " ='" + str_pkvalue + "'");
        }
        try {
            System.out.println("查询数据库真实数据SQL:" + str_sql.toString());
            tabledata = UIUtil.getTableDataStructByDS(this.dataSourceName, str_sql.toString());
            String[] str_row = tabledata.getTable_header();
            String[] str_type = tabledata.getTable_body_type();
            String[][] data = tabledata.getTable_body();
            String[][] str_data = new String[str_row.length][3];
            if (str_row == null || str_row.length <= 0) {
                return;
            }
            if (data == null || data.length <= 0) {
                NovaMessage.show(this, "该表在数据库中不存在！", NovaConstants.MESSAGE_ERROR);
                return;
            }
            for (int i = 0; i < str_row.length; i++) {
                str_data[i][0] = str_row[i];
                str_data[i][1] = str_type[i];
                str_data[i][2] = data[0][i];
            }
            table = new JTable(str_data, new String[] {"数据库列名", "字段类型", "字段值"});
            table.getColumn(table.getColumnName(0)).setCellEditor(new TypeCellEditor(new JTextField()));
            table.getColumn(table.getColumnName(1)).setCellEditor(new TypeCellEditor(new JTextField()));
            table.getColumn(table.getColumnName(2)).setCellEditor(new ValueCellEditor(new JTextField()));
            table.getColumnModel().getColumn(0).setPreferredWidth(150);
            table.getColumnModel().getColumn(1).setPreferredWidth(100);
            table.getColumnModel().getColumn(2).setPreferredWidth(400);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            table.setAutoscrolls(true);
            table.setBackground(color);
            JScrollPane scrollPanel = new JScrollPane(table);
            scrollPanel.setBackground(color);
            if (str_pkname == null) {
                this.getContentPane().add(new JLabel(this.str_tablename + "(该表无主键)", SwingConstants.CENTER),
                                          BorderLayout.NORTH);
            } else {
                this.getContentPane().add(new JLabel(this.str_tablename + "(" + str_pkname + "=" + this.str_pkvalue +
                    ")", SwingConstants.CENTER), BorderLayout.NORTH);
            }
            this.getContentPane().add(scrollPanel, BorderLayout.CENTER);
            JButton bn_exit = new JButton("确定");
            bn_exit.setPreferredSize(new Dimension(80, 20));
            bn_exit.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    onExit();
                }
            });
            JPanel panel_south = new JPanel();
            panel_south.setLayout(new FlowLayout(FlowLayout.CENTER));
            panel_south.add(bn_exit);
            panel_south.setBackground(color);
            this.getContentPane().add(panel_south, BorderLayout.SOUTH);
            this.getContentPane().setBackground(color);
            this.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    private void onExit() {
        this.dispose();
    }

    class TypeCellEditor extends DefaultCellEditor {

        private static final long serialVersionUID = 1L;

        public TypeCellEditor(JTextField textField) {
            super(textField);
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
            int column) {
            JTextField textField = (JTextField)super.getTableCellEditorComponent(table, value, isSelected, row, column);
            return textField;
        }

        public boolean isCellEditable(EventObject evt) {
            return false;
        }
    }

    class ValueCellEditor extends DefaultCellEditor {

        private static final long serialVersionUID = 1L;

        public ValueCellEditor(JTextField textField) {
            super(textField);
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
            int column) {
            JTextField textField = (JTextField)super.getTableCellEditorComponent(table, value, isSelected, row, column);
            return textField;
        }

        public boolean isCellEditable(EventObject evt) {
            if (evt instanceof MouseEvent) {
                return ( (MouseEvent) evt).getClickCount() >= 2;
            }
            return true;
        }
    }
}
/**************************************************************************
 * $RCSfile: RecordShowDialog.java,v $  $Revision: 1.3 $  $Date: 2007/07/19 02:24:48 $
 *
 * $Log: RecordShowDialog.java,v $
 * Revision 1.3  2007/07/19 02:24:48  sunxf
 * *** empty log message ***
 *
 * Revision 1.2  2007/05/31 07:38:18  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:01:07  qilin
 * no message
 *
 * Revision 1.5  2007/03/02 05:02:51  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/02/10 08:59:51  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/02/10 08:51:57  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 05:14:31  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
