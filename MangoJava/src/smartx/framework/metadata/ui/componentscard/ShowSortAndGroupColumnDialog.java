/**************************************************************************
 * $RCSfile: ShowSortAndGroupColumnDialog.java,v $  $Revision: 1.4 $  $Date: 2007/05/31 07:38:18 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentscard;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import smartx.framework.common.ui.*;
import smartx.framework.common.vo.*;
import smartx.framework.metadata.ui.*;
import smartx.framework.metadata.vo.*;


public class ShowSortAndGroupColumnDialog extends NovaDialog {

    Pub_Templet_1_ItemVO[] templetItemVOs = null;

    private static final Font smallFont = new Font("宋体", Font.PLAIN, 12);

    private static final long serialVersionUID = 1L;

    private JList[] jl_all = null;

    private JTable sortTable = null;

    private JButton[] jbt_operator = null;

    private DefaultListModel[] dlm_model = null;

    private Vector sortItem_vec = new Vector();

    private Vector unsortItem_vec = new Vector();

    private Vector groupItem_vec = new Vector();

    private Vector ungroupItem_vec = new Vector();

    private int return_type = 0;

    private String[] str_filterkeys = null;

    private DefaultTableModel tableModel = null;

    private VectorMap vm_sort_item = new VectorMap();

    private ActionListener jbt_listener = null;

    private KeyAdapter jbt_adapter = null;

    /**
     *
     * @param _parent
     * @param _title
     * @param _width
     *            指定宽度
     * @param li_height
     *            指定高度
     * @param str_filterkeys
     */
    public ShowSortAndGroupColumnDialog(Container _parent, String _title,
                                        int _width, int li_height, Pub_Templet_1_ItemVO[] _templetItemVOs,
                                        String[] str_filterkeys) {
        super(_parent, _title, _width, li_height);
        this.templetItemVOs = _templetItemVOs;
        this.str_filterkeys = str_filterkeys;
        initialize();

    }

    /**
     * 初始化页面
     *
     */
    private void initialize() {
        Container con = getContentPane();
        con.setLayout(new BorderLayout());
        this.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                return_type = 2;
            }

            public void windowClosing(WindowEvent e) {
                return_type = 2;
            }
        });
        jbt_listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dealAcitonPerform(e);
            }
        };
        jbt_adapter = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                dealKeyPerform(e);
            }
        };

        jl_all = new JList[3];
        dlm_model = new DefaultListModel[3];
        jbt_operator = new JButton[8];

        Box conBox = Box.createVerticalBox();
        conBox.add(getSortBox());
        conBox.add(getGroupBox());

        jbt_operator[6] = getBtn("确定", new Dimension(75, 20));
        jbt_operator[7] = getBtn("取消", new Dimension(75, 20));

        JPanel panel_south = new JPanel();
        FlowLayout layout = new FlowLayout(FlowLayout.CENTER, 50, 20);
        panel_south.setLayout(layout);
        panel_south.setPreferredSize(new Dimension(600, 40));

        panel_south.add(jbt_operator[6]);
        panel_south.add(jbt_operator[7]);

        con.add(conBox, BorderLayout.CENTER);
        con.add(panel_south, BorderLayout.SOUTH);

        initList();
    }

    /**
     * 定制所有按钮
     *
     * @param _obj:String ||
     *            Icon
     * @param _demension:初始大小
     * @return
     */
    private JButton getBtn(Object _obj, Dimension _demension) {
        JButton jbt_temp = null;
        if (_obj instanceof String) {
            jbt_temp = new JButton(_obj.toString());
        } else if (_obj instanceof Icon) {
            jbt_temp = new JButton( (Icon) _obj);
        }
        jbt_temp.setFont(smallFont);
        jbt_temp.setPreferredSize(_demension);
        jbt_temp.addActionListener(jbt_listener);
        jbt_temp.addKeyListener(jbt_adapter);
        return jbt_temp;
    }

    /**
     * 处理所有按钮事件
     * @param e
     */
    protected void dealAcitonPerform(ActionEvent e) {
        Object obj = e.getSource();
        if (obj.equals(jbt_operator[0])) {
            onSortRight();
        } else if (obj.equals(jbt_operator[1])) {
            onSortLeft();
        } else if (obj.equals(jbt_operator[2])) {
            onSortUp();
        } else if (obj.equals(jbt_operator[3])) {
            onSortDown();
        } else if (obj.equals(jbt_operator[4])) {
            onGroupRight();
        } else if (obj.equals(jbt_operator[5])) {
            onGroupLeft();
        } else if (obj.equals(jbt_operator[6])) {
            onConfirm();
        } else if (obj.equals(jbt_operator[7])) {
            onCancel();
        }
    }

    /**
     * 处理所有键盘事件
     * @param e
     */
    protected void dealKeyPerform(KeyEvent e) {
        Object obj = e.getSource();
        if (obj.equals(jbt_operator[0])) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                onSortRight();
            }
        } else if (obj.equals(jbt_operator[1])) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                onSortLeft();
            }
        } else if (obj.equals(jbt_operator[2])) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                onSortUp();
            }
        } else if (obj.equals(jbt_operator[3])) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                onSortDown();
            }
        } else if (obj.equals(jbt_operator[4])) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                onGroupRight();
            }
        } else if (obj.equals(jbt_operator[5])) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                onGroupLeft();
            }
        } else if (obj.equals(jbt_operator[6])) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                onConfirm();
            }
        } else if (obj.equals(jbt_operator[7])) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                onCancel();
            }
        } else if (obj.equals(jl_all[0])) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                onSortLeft();
            }
        } else if (obj.equals(jl_all[1])) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                onGroupLeft();
            }
        } else if (obj.equals(jl_all[2])) {
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                onGroupRight();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            onCancel();
        }
    }

    /**
     * 获得排序处理Box
     * @return
     */
    private Box getSortBox() {
        ListAndTableFactoty latf_1 = new ListAndTableFactoty();
        JPanel jpn_1 = latf_1.getJSPList("非排序列");
        jl_all[0] = latf_1.getList();
        jl_all[0].addKeyListener(jbt_adapter);
        dlm_model[0] = latf_1.getListModel();

        String[] coloumnNames = {"排序列列名", "是否倒序"};
        ListAndTableFactoty latf_2 = new ListAndTableFactoty();
        JScrollPane jsp_1 = latf_2.getJSPTable(new String[0][2], coloumnNames, new int[] {80, 80});
        jsp_1.setPreferredSize(new Dimension(240, 180));
        latf_2.setColumnUnEditeable(new int[] {0});

        MyCheckBoxCellRender render = new MyCheckBoxCellRender();
        CheckBoxCellEditor editor = new CheckBoxCellEditor(new JCheckBox());
        latf_2.setColumnCellRender(render, 1);
        latf_2.setColumnCellEditor(editor, 1);

        sortTable = latf_2.getTable();
        tableModel = latf_2.getTableModel();

        jbt_operator[2] = getBtn(UIUtil.getImage("images/platform/up.gif"), new Dimension(20, 20));
        jbt_operator[3] = getBtn(UIUtil.getImage("images/platform/down.gif"), new Dimension(20, 20));

        Box box_updown = Box.createVerticalBox();
        box_updown.add(Box.createGlue());
        box_updown.add(jbt_operator[2]);
        box_updown.add(Box.createGlue());
        box_updown.add(jbt_operator[3]);
        box_updown.add(Box.createGlue());

        JPanel jpn_table = new JPanel();
        jpn_table.setLayout(new BorderLayout());
        jpn_table.add(jsp_1, BorderLayout.CENTER);
        jpn_table.add(box_updown, BorderLayout.EAST);
        jpn_table.add(new JLabel("排序列"), BorderLayout.NORTH);

        jbt_operator[0] = getBtn(">>", new Dimension(60, 18));
        jbt_operator[1] = getBtn("<<", new Dimension(60, 18));

        Box box_rl = Box.createVerticalBox();

        box_rl.add(Box.createGlue());
        box_rl.add(jbt_operator[0]);
        box_rl.add(Box.createVerticalStrut(40));
        box_rl.add(jbt_operator[1]);
        box_rl.add(Box.createGlue());

        Box box_sort = Box.createHorizontalBox();
        box_sort.add(Box.createHorizontalStrut(20));
        box_sort.add(jpn_1);
        box_sort.add(Box.createHorizontalStrut(10));
        box_sort.add(box_rl);
        box_sort.add(Box.createHorizontalStrut(10));
        box_sort.add(jpn_table);
        box_sort.add(Box.createHorizontalStrut(20));

        return box_sort;
    }

    /**
     * 获得处理分组统计列的Box
     * @return
     */
    private Box getGroupBox() {
        ListAndTableFactoty latf_2 = new ListAndTableFactoty();
        JPanel jpn_1 = latf_2.getJSPList("非分组统计列");
        jl_all[1] = latf_2.getList();
        jl_all[1].addKeyListener(jbt_adapter);
        dlm_model[1] = latf_2.getListModel();

        ListAndTableFactoty latf_3 = new ListAndTableFactoty();
        JPanel jpn_2 = latf_3.getJSPList("分组统计列");
        jl_all[2] = latf_3.getList();
        jl_all[2].addKeyListener(jbt_adapter);
        dlm_model[2] = latf_3.getListModel();

        jbt_operator[4] = getBtn(">>", new Dimension(60, 18));
        jbt_operator[5] = getBtn("<<", new Dimension(60, 18));

        Box box_rl = Box.createVerticalBox();
        box_rl.add(Box.createGlue());
        box_rl.add(jbt_operator[4]);
        box_rl.add(Box.createVerticalStrut(40));
        box_rl.add(jbt_operator[5]);
        box_rl.add(Box.createGlue());

        Box box_group = Box.createHorizontalBox();
        box_group.add(Box.createHorizontalStrut(20));
        box_group.add(jpn_1);
        box_group.add(Box.createHorizontalStrut(10));
        box_group.add(box_rl);
        box_group.add(Box.createHorizontalStrut(10));
        box_group.add(jpn_2);
        box_group.add(Box.createHorizontalStrut(40));

        return box_group;
    }

    /**
     * 初始化JList
     */
    private void initList() {
        String id = null;
        String name = null;
        String code = null;
        String templete_type = null;

        int showColumnCount = templetItemVOs.length;

        for (int i = 0; i < showColumnCount; i++) {
            id = templetItemVOs[i].getItemkey();
            name = templetItemVOs[i].getItemname();
            code = templetItemVOs[i].getItemname();
            templete_type = templetItemVOs[i].getItemtype();

            if (isLocked(id)) {
                continue;
            }
            JListItemVO temp_Item = new JListItemVO(id, name, code);
            if (templetItemVOs[i].getListisshowable().booleanValue()) {
                unsortItem_vec.add(temp_Item);
                dlm_model[0].addElement(name);
                if (templete_type.equals("数字框")) {
                    groupItem_vec.add(temp_Item);
                    dlm_model[2].addElement(name);
                }
            }
        }
    }

    /**
     * 分组统计列的左移
     */
    protected void onGroupLeft() {
        int selected_row[] = jl_all[2].getSelectedIndices();
        int ungroupItem_vec_size = ungroupItem_vec.size();

        if (selected_row.length == 0) {
            return;
        } else {
            if (getMinIndex(false, false) != -1) {
                ungroupItem_vec_size = getMinIndex(false, false);
            }
            for (int i = selected_row.length - 1; i >= 0; i--) {
                JListItemVO temp_Item = (JListItemVO) groupItem_vec
                    .get(selected_row[i]);
                ungroupItem_vec.add(ungroupItem_vec_size, temp_Item);
                groupItem_vec.remove(selected_row[i]);
            }
            refreshList(false);
            jl_all[1].setSelectionInterval(ungroupItem_vec_size,
                                           ungroupItem_vec_size + selected_row.length - 1);
            jl_all[1].requestFocus();
        }
    }

    /**
     * 分组统计列的右移
     */
    protected void onGroupRight() {
        int selected_row[] = jl_all[1].getSelectedIndices();
        int groupItem_vec_size = groupItem_vec.size();

        if (selected_row.length == 0) {
            return;
        } else {
            if (getMinIndex(false, true) != -1) {
                groupItem_vec_size = getMinIndex(false, true);
            }
            for (int i = selected_row.length - 1; i >= 0; i--) {
                JListItemVO temp_Item = (JListItemVO) ungroupItem_vec
                    .get(selected_row[i]);
                groupItem_vec.add(groupItem_vec_size, temp_Item);
                ungroupItem_vec.remove(selected_row[i]);
            }
            refreshList(false);
            jl_all[2].setSelectionInterval(groupItem_vec_size,
                                           groupItem_vec_size + selected_row.length - 1);
            jl_all[2].requestFocus();
        }
    }

    private boolean isLocked(String _itemid) {
        int filterKeys_length = str_filterkeys.length;

        for (int i = 0; i < filterKeys_length; i++) {
            if (str_filterkeys[i].equals(_itemid)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 刷新List，包括sort和Group
     * @param _sortorgroup
     */
    private void refreshList(boolean _sortorgroup) {
        if (_sortorgroup) {
            dlm_model[0].clear();
            for (int j = 0; j < unsortItem_vec.size(); j++) {
                JListItemVO temp_Item = (JListItemVO) unsortItem_vec.get(j);
                dlm_model[0].addElement(temp_Item.getName());
            }
        } else {
            dlm_model[2].clear();
            dlm_model[1].clear();

            for (int i = 0; i < groupItem_vec.size(); i++) {
                JListItemVO temp_Item = (JListItemVO) groupItem_vec.get(i);
                dlm_model[2].addElement(temp_Item.getName());
            }
            for (int j = 0; j < ungroupItem_vec.size(); j++) {
                JListItemVO temp_Item = (JListItemVO) ungroupItem_vec.get(j);
                dlm_model[1].addElement(temp_Item.getName());
            }
        }
    }

    /**
     * 刷新数据表
     */
    private void refreshTable() {
        int count = tableModel.getRowCount();
        if (count > 0) {
            for (int i = count - 1; i >= 0; i--) {
                tableModel.removeRow(i);
            }
        }
        //	sortTable.updateUI();
        for (int i = 0; i < sortItem_vec.size(); i++) {
            Object[] temp_object = { (Object) sortItem_vec.get(i),
                new Boolean(false)};
            tableModel.addRow(temp_object);
        }
        sortTable.updateUI();
    }

    /**
     * 处理向上的按钮
     */
    private void onSortUp() {
        int selected_row = sortTable.getSelectedRow();

        if (selected_row <= 0) {
            return;
        } else {
            JListItemVO temp_Item = (JListItemVO) sortItem_vec
                .get(selected_row - 1);
            sortItem_vec.setElementAt(sortItem_vec.get(selected_row),
                                      selected_row - 1);
            sortItem_vec.setElementAt(temp_Item, selected_row);
            refreshTable();
            sortTable.setRowSelectionInterval(selected_row - 1,
                                              selected_row - 1);
        }
    }

    /**
     * 处理向下的按钮
     */
    private void onSortDown() {
        int selected_row = sortTable.getSelectedRow();

        if (selected_row >= tableModel.getRowCount() - 1) {
            return;
        } else {
            JListItemVO temp_Item = (JListItemVO) sortItem_vec
                .get(selected_row + 1);
            sortItem_vec.setElementAt(sortItem_vec.get(selected_row),
                                      selected_row + 1);
            sortItem_vec.setElementAt(temp_Item, selected_row);
            refreshTable();
            sortTable.setRowSelectionInterval(selected_row + 1,
                                              selected_row + 1);
        }
    }

    /**
     * 获得sort和Group选择行的最小值
     * @param _sortORgroup
     * @param _listflag
     * @return
     */
    private int getMinIndex(boolean _sortORgroup, boolean _listflag) {
        if (_sortORgroup) {
            if (_listflag) {
                int selected_row[] = sortTable.getSelectedRows();
                if (selected_row.length > 0) {
                    return selected_row[0];
                }
            } else {
                int selected_row[] = jl_all[0].getSelectedIndices();
                if (selected_row.length > 0) {
                    return jl_all[0].getMinSelectionIndex();
                }
            }
        } else {
            if (_listflag) {
                int selected_row[] = jl_all[2].getSelectedIndices();
                if (selected_row.length > 0) {
                    return jl_all[2].getMinSelectionIndex();
                }
            } else {
                int selected_row[] = jl_all[1].getSelectedIndices();
                if (selected_row.length > 0) {
                    return jl_all[1].getMinSelectionIndex();
                }
            }
        }
        return -1;
    }

    /**
     * 处理sort左移
     */
    private void onSortLeft() {
        int selected_row = sortTable.getSelectedRow();
        int hiddenItem_vec_size = unsortItem_vec.size();

        if (selected_row < 0) {
            return;
        } else {
            if (getMinIndex(true, false) != -1) {
                hiddenItem_vec_size = getMinIndex(true, false);
            }

            JListItemVO temp_Item = (JListItemVO) sortItem_vec
                .get(selected_row);

            unsortItem_vec.add(hiddenItem_vec_size, temp_Item);
            sortItem_vec.remove(selected_row);

            refreshTable();
            refreshList(true);
            jl_all[0].setSelectionInterval(hiddenItem_vec_size,
                                           hiddenItem_vec_size);
            jl_all[0].requestFocus();
        }
    }

    /**
     * 处理sort右移
     */
    private void onSortRight() {
        int selected_row[] = jl_all[0].getSelectedIndices();
        int sortItem_vec_size = sortItem_vec.size();

        if (selected_row.length == 0) {
            return;
        } else {
            if (getMinIndex(true, true) != -1) {
                sortItem_vec_size = getMinIndex(true, true);
            }
            for (int i = selected_row.length - 1; i >= 0; i--) {
                JListItemVO temp_Item = (JListItemVO) unsortItem_vec
                    .get(selected_row[i]);
                sortItem_vec.add(sortItem_vec_size, temp_Item);
                unsortItem_vec.remove(selected_row[i]);
            }
            refreshTable();
            refreshList(true);
            sortTable.setRowSelectionInterval(sortItem_vec_size,
                                              sortItem_vec_size + selected_row.length - 1);
        }
    }

    /**
     * 获得排序的列
     */
    private void getSortItem() {
        if (sortItem_vec.size() <= 0) {
            return;
        }
        for (int i = 0; i < sortItem_vec.size(); i++) {
            JListItemVO temp_Item = (JListItemVO) sortItem_vec.get(sortItem_vec
                .size()
                - i - 1);
            Boolean temp_boolean = (Boolean) sortTable.getModel().getValueAt(
                sortItem_vec.size() - i - 1, 1);

            vm_sort_item.put(temp_Item, temp_boolean);
        }
    }

    private void onConfirm() {
        getSortItem();
        this.return_type = 0;
        this.dispose();
    }

    private void onCancel() {
        this.return_type = 1;
        this.dispose();
    }

    public VectorMap getSortBySqenceResult() {
        return vm_sort_item;
    }

    public Vector getSortResult() {
        return sortItem_vec;
    }

    public Vector getGroupResult() {
        return groupItem_vec;
    }

    public int getReturn_type() {
        return return_type;
    }

    class MyCheckBoxCellRender extends JCheckBox implements TableCellRenderer {

        /**
         *
         */
        private static final long serialVersionUID = -2287511407673957076L;

        public MyCheckBoxCellRender() {
            super();
            this.setHorizontalAlignment(JCheckBox.CENTER);
        }

        public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row,
            int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                super.setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }

            if (value == null) {
                return this;
            }
            if (value instanceof Boolean) {
                this.setSelected( ( (Boolean) value).booleanValue());
            } else {
                this.setSelected(false);
            }
            return this;
        }

    }

    class CheckBoxCellEditor extends DefaultCellEditor {

        private static final long serialVersionUID = -4302686187683419327L;
        JCheckBox checkBox = null;

        public CheckBoxCellEditor(JCheckBox checkBox) {
            super(checkBox);
        }

        public Component getTableCellEditorComponent(JTable table,
            Object value, boolean isSelected, int row, int column) {
            checkBox = (JCheckBox)super.getTableCellEditorComponent(table,
                value, isSelected, row, column);
            checkBox.setBackground(table.getSelectionBackground()); // 背景颜色是表格选择的背景颜色!!
            checkBox.setHorizontalAlignment(SwingConstants.CENTER);

            if (value == null) {
                return checkBox;
            }
            if (value instanceof Boolean) {
                checkBox.setSelected( ( (Boolean) value).booleanValue());
            } else {
                checkBox.setSelected(false);
            }
            return checkBox;
        }

        public Object getCellEditorValue() {
            return new Boolean(checkBox.isSelected());
        }
    }
}
/**************************************************************************
 * $RCSfile: ShowSortAndGroupColumnDialog.java,v $  $Revision: 1.4 $  $Date: 2007/05/31 07:38:18 $
 *
 * $Log: ShowSortAndGroupColumnDialog.java,v $
 * Revision 1.4  2007/05/31 07:38:18  qilin
 * code format
 *
 * Revision 1.3  2007/05/22 07:58:46  qilin
 * no message
 *
 * Revision 1.2  2007/05/22 02:03:34  sunxb
 * *** empty log message ***
 *
 * Revision 1.1  2007/05/17 06:01:06  qilin
 * no message
 *
 * Revision 1.5  2007/03/13 15:03:43  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/02/27 06:57:19  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/02/10 08:51:57  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 05:14:29  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
