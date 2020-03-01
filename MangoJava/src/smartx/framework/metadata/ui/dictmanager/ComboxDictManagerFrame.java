/**************************************************************************
 * $RCSfile: ComboxDictManagerFrame.java,v $  $Revision: 1.5 $  $Date: 2007/05/31 07:38:24 $
 **************************************************************************/
package smartx.framework.metadata.ui.dictmanager;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import smartx.framework.common.ui.*;
import smartx.framework.common.ui.component.*;
import smartx.framework.common.vo.*;
import smartx.framework.metadata.ui.componentscard.*;


/**
 * 下拉框数据字典维护
 *
 * @author Administrator
 *
 */
public class ComboxDictManagerFrame extends NovaInternalFrame {

    /**
     *
     */
    private static final long serialVersionUID = -3176295666980705895L;

    private static final int INSERT_STATUS = 0;

    private static final int UPDATE_STATUS = 1;

    private static final int li_table_count = 2;

    private static final int li_table_column_count = 5;

    private static int[][] column_width = null;

    private JTable[] jtb_combox = null;

    private JButton[] jbt_operation = null;

    private JTabbedPane jtp_con = null;

    private String str_selected_name = null;

    private Vector vec_rows = null;

    private int[] selectedIndex = null;

    private Vector vec_sql = null;

    private Hashtable ht_status = null;

    private String[] str_columnnames = null;

    private int li_closetype = 0;

    public ComboxDictManagerFrame() {
        this.setTitle("下拉框数据字典维护");
        this.setSize(900, 600);
        this.setLocation(50, 0);
        initFrame();
    }

    private void initFrame() {
        getColumnWidth();
        vec_rows = new Vector();
        vec_sql = new Vector();
        ht_status = new Hashtable();
        str_columnnames = new String[] {"TYPE", "ID", "CODE", "NAME", "DESCR"};

        jbt_operation = new JButton[7]; // 初始化控制按钮
        jtb_combox = new JTable[li_table_count];
        jtp_con = new JTabbedPane();
        jtp_con.addTab("下拉框字典管理", getTablePanel());

        this.getContentPane().add(jtp_con, BorderLayout.CENTER);
        this.getContentPane().add(getSouthPanel(), BorderLayout.SOUTH);
    }

    /**
     * 获得整个的内容面板，左边的表和右边的详细信息面板
     *
     * @return
     */
    private Component getTablePanel() {
        String[] str_columnnames_1 = new String[] {"类型"};

        String str_sql = "Select distinct type from pub_comboboxdict";

        ListAndTableFactoty latf_type = new ListAndTableFactoty();
        String[][] str_datas = null;
        try {
            str_datas = UIUtil.getStringArrayByDS(null, str_sql);
        } catch (NovaRemoteException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        JScrollPane jsp_combox = latf_type.getJSPTable(str_datas, str_columnnames_1, column_width[0]);
        latf_type.setColumnUnEditeable(new int[] {0});
        jtb_combox[0] = latf_type.getTable();
        jtb_combox[0].addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() >= 1) {
                    dealTableClick(e);
                }
            }
        });

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jsp_combox, getDetailPanel());
        splitPane.setDividerLocation(250);
        splitPane.setDividerSize(10);
        splitPane.setOneTouchExpandable(true);

        return splitPane;
    }

    /**
     * 获得右边的详细信息的表，以及控制按钮
     *
     * @return
     */
    private Component getDetailPanel() {
        jbt_operation[0] = getOperatorBtn(UIUtil.getImage("images/platform/add.jpg"), new Dimension(20, 20));
        jbt_operation[1] = getOperatorBtn(UIUtil.getImage("images/platform/minuse.jpg"), new Dimension(20, 20));
        jbt_operation[2] = getOperatorBtn(UIUtil.getImage("images/platform/up1.jpg"), new Dimension(20, 20));
        jbt_operation[3] = getOperatorBtn(UIUtil.getImage("images/platform/down1.jpg"), new Dimension(20, 20));

        jbt_operation[0].setToolTipText("添加新数据");
        jbt_operation[1].setToolTipText("删除选中行");
        jbt_operation[2].setToolTipText("上移数据行");
        jbt_operation[3].setToolTipText("下移数据行");

        Box box_btn = Box.createVerticalBox();
        box_btn.add(Box.createVerticalGlue());
        box_btn.add(jbt_operation[0]);
        box_btn.add(Box.createVerticalStrut(10));
        box_btn.add(jbt_operation[1]);
        box_btn.add(Box.createVerticalStrut(10));
        box_btn.add(jbt_operation[2]);
        box_btn.add(Box.createVerticalStrut(10));
        box_btn.add(jbt_operation[3]);
        box_btn.add(Box.createVerticalGlue());

        JPanel jpn_detail = new JPanel();
        jpn_detail.setLayout(new BorderLayout());

        String[] str_detail_columnnames = new String[] {"类型", "ID", "编码", "名称", "描述"};
        ListAndTableFactoty latf_detail = new ListAndTableFactoty();
        JScrollPane jsp_combox_detail = latf_detail.getJSPTable(new String[0][4], str_detail_columnnames,
            column_width[1]);
        latf_detail.setColumnUnEditeable(new int[] {0});
        jtb_combox[1] = latf_detail.getTable();
        jtb_combox[1].addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() >= 1) {
                    dealTableClick(e);
                }
            }
        });
        jpn_detail.add(jsp_combox_detail, BorderLayout.CENTER);
        jpn_detail.add(box_btn, BorderLayout.EAST);

        return jpn_detail;
    }

    private JButton getOperatorBtn(Object _obj, Dimension _demension) {
        JButton jbt_temp = null;
        if (_obj instanceof String) {
            jbt_temp = new JButton(_obj.toString());
        } else if (_obj instanceof Icon) {
            jbt_temp = new JButton( (Icon) _obj);
        }
        jbt_temp.setPreferredSize(_demension);
        jbt_temp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dealAcitonPerform(e);
            }
        });

        return jbt_temp;
    }

    /**
     * 处理按钮事件
     *
     * @param e
     */
    protected void dealAcitonPerform(ActionEvent e) {
        if (str_selected_name == null || str_selected_name.equals("")) {
            return;
        }
        Object obj = e.getSource();
        if (obj.equals(jbt_operation[0])) { // 处理添加行
            Object[] obj_temp = new Object[] {str_selected_name, "", "", "", ""};
            ( (DefaultTableModel) jtb_combox[1].getModel()).addRow(obj_temp);
            vec_rows.add(obj_temp);

            ht_status.put(obj_temp, new Integer(INSERT_STATUS));
        } else if (obj.equals(jbt_operation[1])) { // 处理删除行
            int li_count = jtb_combox[1].getSelectedRow();
            if (li_count < 0) {
                JOptionPane.showMessageDialog(this, "请选择一行要删除的数据！");
                return;
            }
            String str_type = (String) jtb_combox[1].getValueAt(li_count, 0);
            String str_id = (String) jtb_combox[1].getValueAt(li_count, 1);
            vec_rows.remove(li_count);
            DefaultTableModel model = (DefaultTableModel) jtb_combox[1].getModel();
            clearRows(model, li_count);
            refreshRows(model, vec_rows, li_count);

            String str_sql = "Delete From pub_comboboxdict Where TYPE = '" + str_type + "' And ID = '" + str_id + "'";
            vec_sql.add(str_sql);
        } else if (obj.equals(jbt_operation[2])) { // 处理上移
            int selected_rows[] = jtb_combox[1].getSelectedRows();
            selectedIndex = selected_rows;
            if (selected_rows.length == 0) {
                return;
            }

            for (int i = 0; i < selected_rows.length; i++) {
                if (selected_rows[i] > i) {
                    Object[] obj_temp = (Object[]) vec_rows.get(selected_rows[i] - 1);
                    vec_rows.setElementAt(vec_rows.get(selected_rows[i]), selected_rows[i] - 1);
                    vec_rows.setElementAt(obj_temp, selected_rows[i]);
                    selectedIndex[i]--;
                }
            }
            DefaultTableModel model = (DefaultTableModel) jtb_combox[1].getModel();
            clearRows(model, selectedIndex[0]);
            refreshRows(model, vec_rows, selectedIndex[0]);
            setSelectedRows(jtb_combox[1], selectedIndex);
            selectedIndex = null;

        } else if (obj.equals(jbt_operation[3])) { // 处理下移
            int selected_rows[] = jtb_combox[1].getSelectedRows();
            selectedIndex = selected_rows;
            if (selected_rows.length == 0) {
                return;
            }
            int li_first = selected_rows[0];
            for (int i = selected_rows.length - 1; i >= 0; i--) {
                if (selected_rows[i] < vec_rows.size() - selected_rows.length + i) {
                    Object[] obj_temp = (Object[]) vec_rows.get(selected_rows[i] + 1);
                    vec_rows.setElementAt(vec_rows.get(selected_rows[i]), selected_rows[i] + 1);
                    vec_rows.setElementAt(obj_temp, selected_rows[i]);
                    selectedIndex[i]++;
                }
            }
            DefaultTableModel model = (DefaultTableModel) jtb_combox[1].getModel();
            clearRows(model, li_first);
            refreshRows(model, vec_rows, li_first);
            setSelectedRows(jtb_combox[1], selectedIndex);
            selectedIndex = null;
        } else if (obj.equals(jbt_operation[4])) {
            onSave();
        } else if (obj.equals(jbt_operation[5])) {
            onConfirm();
        } else if (obj.equals(jbt_operation[6])) {
            onCancel();
        } else {
        }
    }

    /**
     * 判断两个Object数组是否相同
     *
     * @param _obj1
     * @param _obj2
     * @return
     */
    private boolean isEquals(Object[] _obj1, Object[] _obj2) {
        if (_obj1.length != _obj2.length) {
            return false;
        }
        for (int i = 0; i < _obj1.length; i++) {
            if (!_obj1[i].equals(_obj2[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取Table指定行的数据
     *
     * @param _index
     * @return
     */
    private Object[] getTableRow(int _index) {
        Object[] _obj = new Object[jtb_combox[1].getColumnCount()];
        for (int i = 0; i < _obj.length; i++) {
            _obj[i] = jtb_combox[1].getValueAt(_index, i);
        }
        return _obj;
    }

    /**
     * 获取更新的SQL语句
     *
     * @param _obj_row
     * @param _obj_vec
     * @return
     */
    private String getUpdateSql(Object[] _obj_row, Object[] _obj_vec) {
        String str_sql = "";
        str_sql = "Update pub_comboboxdict Set ";
        String str_mid = "";
        for (int j = 0; j < _obj_row.length; j++) {
            if (_obj_row[j].equals(_obj_vec[j])) {
                continue;
            } else {
                str_mid = str_mid + str_columnnames[j] + " = '" + _obj_row[j] + "',";
            }
        }
        str_sql = str_sql + str_mid;
        return str_sql;
    }

    /**
     * 获取所有的SQL，保存到vec_sql中
     *
     * @return
     */
    private String getTotalSQL() {
        String str_sql = "";
        for (int i = 0; i < vec_rows.size(); i++) {
            Object[] objs = (Object[]) vec_rows.get(i);
            Object[] obj_row = getTableRow(i);

            Object obj = ht_status.get(objs);
            if (obj == null && isEquals(objs, obj_row)) {
                continue;
            }
            String str_end = " Where TYPE = '" + obj_row[0] + "' And ID = '" + obj_row[1] + "'";
            if (obj == null) { // 处理更新，该更新不包括SEQ
                str_sql = getUpdateSql(obj_row, objs); //
                str_sql = str_sql.substring(0, str_sql.length() - 1);
                str_sql = str_sql + str_end;
            } else if ( ( (Integer) obj).intValue() == INSERT_STATUS) { // 该行数据处于新增状态
                str_sql = "Insert Into pub_comboboxdict values (S_PUB_COMBOBOXDICT.NEXTVAL, ";
                String str_mid = "";
                for (int j = 0; j < obj_row.length; j++) {
                    str_mid = str_mid + "'" + obj_row[j] + "',";
                }
                str_sql = str_sql + str_mid + "'" + (i + 1) + "')";
            } else if ( ( (Integer) obj).intValue() == UPDATE_STATUS) { // 该行数据处于更新状态，该更新包括SEQ
                str_sql = getUpdateSql(obj_row, objs) + "SEQ = '" + (i + 1) + "'";
                str_sql = str_sql + str_end;
            }
            vec_sql.add(str_sql);
            str_sql = "";
        }
        return null;
    }

    /**
     * 数据表选中多行
     *
     * @param _table
     * @param _selectedrows
     */
    private void setSelectedRows(JTable _table, int[] _selectedrows) {
        if (_table == null) {
            return;
        }
        _table.clearSelection();
        for (int i = 0; i < _selectedrows.length; i++) {
            _table.addRowSelectionInterval(_selectedrows[i], _selectedrows[i]);
        }
    }

    protected void dealTableClick(MouseEvent e) {
        Object obj = e.getSource();
        if (obj.equals(jtb_combox[0])) {
            int li_selected_index = jtb_combox[0].getSelectedRow();
            String str_selected_value = (String) jtb_combox[0].getModel().getValueAt(li_selected_index, 0);
            if (str_selected_name != null && str_selected_name.equals(str_selected_value)) {
                return;
            }
            str_selected_name = str_selected_value;
            vec_sql.clear();
            ht_status.clear();

            if (str_selected_value != null && !str_selected_value.equals("")) {
                String str_sql = "Select TYPE, ID, CODE, NAME, DESCR From pub_comboboxdict Where TYPE = '" +
                    str_selected_value + "' Order by SEQ";
                String[][] str_values = null;
                try {
                    str_values = UIUtil.getStringArrayByDS(null, str_sql);
                } catch (NovaRemoteException e1) {
                    e1.printStackTrace();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                clearRows( (DefaultTableModel) jtb_combox[1].getModel());
                this.addRows( (DefaultTableModel) jtb_combox[1].getModel(), str_values);
            }
        }
    }

    /**
     * 获取两个表的列宽
     */
    private void getColumnWidth() {
        column_width = new int[li_table_count][li_table_column_count];
        column_width[0][0] = 180; // 主表列宽
        column_width[0][1] = 50;
        column_width[0][2] = 100;
        column_width[0][3] = 120;
        column_width[0][4] = 150;

        column_width[1][0] = 150; // 详细内容表列宽
        column_width[1][1] = 100;
        column_width[1][2] = 100;
        column_width[1][3] = 120;
        column_width[1][4] = 150;
    }

    /**
     * 将Vec容器中_start_row以后的元素，加入_model中
     *
     * @param _model
     * @param _vec
     * @param _start_row
     */
    private void refreshRows(DefaultTableModel _model, Vector _vec, int _start_row) {
        if (_start_row < 0) {
            _start_row = 0;
        } else if (_start_row >= _vec.size()) {
            return;
        }
        for (int i = _start_row; i < _vec.size(); i++) {
            Object _obj = ht_status.get(_vec.get(i));
            if (_obj == null) {
                ht_status.put(_vec.get(i), new Integer(UPDATE_STATUS));
            }
            _model.addRow( (Object[]) _vec.get(i));
        }
    }

    /**
     * 向指定的TableModel中添加行
     *
     * @param _model
     * @param _obj
     */
    private void addRows(DefaultTableModel _model, Object[][] _obj) {
        vec_rows.clear();
        for (int i = 0; i < _obj.length; i++) {
            _model.addRow(_obj[i]);
            vec_rows.add(_obj[i]);
        }
    }

    /**
     * 删除指定Model中指定行后的所有行
     *
     * @param _model
     */
    private void clearRows(DefaultTableModel _model, int _row_index) {
        for (int i = _model.getRowCount() - 1; i >= _row_index; i--) {
            _model.removeRow(i);
        }
    }

    /**
     * 删除指定Model中的所有行
     *
     * @param _model
     */
    private void clearRows(DefaultTableModel _model) {
        for (int i = _model.getRowCount() - 1; i >= 0; i--) {
            _model.removeRow(i);
        }
    }

    /**
     * 获取按钮面板
     *
     * @return
     */
    private Component getSouthPanel() {
        JPanel jpn_south = new JPanel();
        jpn_south.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 15));

        jbt_operation[4] = getOperatorBtn("保存", new Dimension(75, 20));
        jbt_operation[5] = getOperatorBtn("确定", new Dimension(75, 20));
        jbt_operation[6] = getOperatorBtn("取消", new Dimension(75, 20));

        jpn_south.add(jbt_operation[4]);
        jpn_south.add(jbt_operation[5]);
        jpn_south.add(jbt_operation[6]);
        return jpn_south;
    }

    protected void onSave() {
        getTotalSQL();
        if (vec_sql.size() <= 0) {
            return;
        }
        String[] str_sqls = new String[vec_sql.size()];
        for (int i = 0; i < str_sqls.length; i++) {
            str_sqls[i] = (String) vec_sql.get(i);
            System.out.println("The sql contains:" + str_sqls[i]);
        }
        try {
            UIUtil.executeBatchByDS(null, str_sqls);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "提交数据库出现异常！");
        }
    }

    /**
     * 获取窗口关闭的类型
     *
     * @return
     */
    public int getCloseType() {
        return this.li_closetype;
    }

    protected void onConfirm() {
        this.li_closetype = 0;
        this.dispose();
    }

    protected void onCancel() {
        this.li_closetype = 1;
        this.dispose();
    }
}
/**************************************************************************
 * $RCSfile: ComboxDictManagerFrame.java,v $  $Revision: 1.5 $  $Date: 2007/05/31 07:38:24 $
 *
 * $Log: ComboxDictManagerFrame.java,v $
 * Revision 1.5  2007/05/31 07:38:24  qilin
 * code format
 *
 * Revision 1.3  2007/05/22 07:58:45  qilin
 * no message
 *
 * Revision 1.2  2007/05/22 02:03:59  sunxb
 * *** empty log message ***
 *
 * Revision 1.1  2007/05/17 06:02:12  qilin
 * no message
 *
 * Revision 1.6  2007/03/07 02:01:56  shxch
 * *** empty log message ***
 *
 * Revision 1.5  2007/03/02 05:16:41  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/03/02 05:02:51  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/03/01 09:06:55  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 05:01:56  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
