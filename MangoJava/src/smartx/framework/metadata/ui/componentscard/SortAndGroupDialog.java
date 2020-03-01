/**************************************************************************
 * $RCSfile: SortAndGroupDialog.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:18 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentscard;

import java.text.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import smartx.framework.common.vo.*;
import smartx.framework.metadata.ui.*;
import smartx.framework.metadata.vo.*;


public class SortAndGroupDialog extends NovaDialog {

    private static final long serialVersionUID = -2049061173694412022L;

    private Vector vec_sort_item = new Vector();

    private Vector vec_group_item = new Vector();

    private Vector vec_group_pos = new Vector();

    private Vector group_rows = new Vector();

    private VectorMap vm_result = new VectorMap();

    private VectorMap vm_sort_item = new VectorMap();

    private Pub_Templet_1VO templetVO = null;

    private Pub_Templet_1_ItemVO[] templetItemVOs = null;

    private JTable jt_table = null;

    private Object[][] obj_values = null;

    private boolean lastcolumn_falg = false;

    private DecimalFormat df = new DecimalFormat("##.00");

    public SortAndGroupDialog(Container _parent, String _title, int _width, int li_height) {
        super(_parent, _title, _width, li_height);
    }

    public SortAndGroupDialog(Container _parent, String _title, int _width, int li_height, Object[][] _data,
                              Pub_Templet_1VO _templetVO, Vector _sortcolumn, Vector _groupcolumn) {
        super(_parent, _title, _width, li_height);
        this.vec_sort_item = _sortcolumn;
        this.vec_group_item = _groupcolumn;
        this.obj_values = _data;
        templetVO = _templetVO;
        this.templetItemVOs = templetVO.getItemVos();
        intialize();
    }

    public SortAndGroupDialog(Container _parent, String _title, int _width, int li_height, Object[][] _data,
                              Pub_Templet_1VO _templetVO, VectorMap _sortbyseqcolumn, Vector _groupcolumn) {
        super(_parent, _title, _width, li_height);
        this.vm_sort_item = _sortbyseqcolumn;
        this.vec_group_item = _groupcolumn;
        this.obj_values = _data;
        this.templetVO = _templetVO;
        this.templetItemVOs = templetVO.getItemVos();
        intialize();
        sortByColumn();
    }

    private void intialize() {
        JScrollPane scrollPane = getJSPTable();
        JButton btn_viewChart = new JButton("查看图表");
        btn_viewChart.setPreferredSize(new Dimension(90, 20));
        btn_viewChart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onViewChartBtn();
            }
        });
        JButton btn_cancel = new JButton("取消");
        btn_cancel.setPreferredSize(new Dimension(80, 20));
        btn_cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        Box buttonBox = Box.createHorizontalBox();
        buttonBox.add(Box.createGlue());
        buttonBox.add(btn_viewChart);
        buttonBox.add(Box.createHorizontalStrut(40));
        buttonBox.add(btn_cancel);
        buttonBox.add(Box.createGlue());

        this.getContentPane().add(scrollPane, BorderLayout.CENTER);
        this.getContentPane().add(buttonBox, BorderLayout.SOUTH);
    }

    protected void onViewChartBtn() {
//        ShowSortAndGroupChartDialog showSortAndGroupChartDialog = new ShowSortAndGroupChartDialog(this, "分组统计", "分组列",
//            "统计值", vm_result, vec_group_item);
//        showSortAndGroupChartDialog.setVisible(true); //
    }

    /**
     * 获取要处理的内容
     *
     * @param _obj
     * @param _begin
     * @param _end
     * @return
     */
    private Object[] getObjectArray(Object[] _obj, int _begin, int _end) {
        Object[] obj = new Object[_end - _begin];
        int li_index = _begin;
        for (int i = 0; i < obj.length; i++) {
            obj[i] = _obj[li_index];
            li_index++;
        }
        return obj;
    }

    /**
     * 获得带有滚动条的JTable
     *
     * @return
     */
    private JScrollPane getJSPTable() {
        Object[][] obj_temp = new Object[obj_values.length][];
        for (int i = 0; i < obj_values.length; i++) {
            obj_temp[i] = getObjectArray(obj_values[i], 1, obj_values[i].length);
        }
        obj_values = obj_temp;
        ListAndTableFactoty latf_table = new ListAndTableFactoty();

        JScrollPane jsp_temp = latf_table.getJSPTable(obj_temp, templetVO.getItemNames(), null);
        jt_table = latf_table.getTable();

        MyTableHeaderRenderer mthr_temp = new MyTableHeaderRenderer();
        latf_table.setAllColumnCellRender(mthr_temp);
        return jsp_temp;
    }

    /**
     * 查看当前列是否在临界列
     *
     * @param _obj
     * @return
     */
    private boolean findObj(Object[] _obj) {
        Iterator group_it = vec_group_pos.iterator();
        while (group_it.hasNext()) {
            Object[] temp_Item = (Object[]) group_it.next();
            if (temp_Item.equals(_obj)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 找出分组统计临界行
     */
    private void getGroupRow() {
        Object[] obj_first = obj_values[0];
        vec_group_pos.add(obj_first);
        for (int i = 1; i < obj_values.length; i++) {
            Object[] obj_second = obj_values[i];
            for (int j = 0; j < vec_sort_item.size(); j++) {
                int _pos = ( (Integer) vec_sort_item.get(j)).intValue();

                int compareresult = -1;
                String str_type = templetItemVOs[_pos - 1].getItemtype(); // 类型
                if (str_type.equals("数字框")) {
                    compareresult = Integer.parseInt( (String) (obj_first[_pos])) -
                        Integer.parseInt( (String) (obj_second[_pos]));
                } else {
                    compareresult = compareObject(obj_first[_pos], obj_second[_pos]);
                }

                if (compareresult != 0) {
                    vec_group_pos.add(obj_second);
                    obj_first = obj_second;
                    break;
                }
            }
        }
    }

    /**
     * 将所有排序的列拼成AA/BB/CC的模式，以供在图标查看的时候使用
     *
     * @param _obj
     * @return
     */
    private String getVMKey(Object[] _obj) {
        String result = "";

        for (int i = 0; i < vec_sort_item.size(); i++) {
            int _pos = ( (Integer) vec_sort_item.get(i)).intValue();
            String get_str = "";
            if (_obj[_pos] == null) {
                get_str = "null";
            } else {
                get_str = _obj[_pos].toString();
                if (get_str.equals("")) {
                    get_str = "null";
                }
            }
            if (i != vec_sort_item.size() - 1) {
                get_str = get_str + "/";
            }
            result = result + get_str;
        }
        return result;
    }

    /**
     * 向整个数据表中插入分组统计行
     *
     * @param _objvalue
     * @param _desc
     */
    private void addGroupRow(Object[][] _objvalue, boolean _desc) {
        if (vec_group_item.size() == 0) {
            return;
        }
        getGroupRow();
        Vector group_index = new Vector();
        Iterator group_it = vec_group_item.iterator();
        while (group_it.hasNext()) { // 找出分组统计列的编号
            JListItemVO temp_Item = (JListItemVO) group_it.next();
            String str_key = (String) temp_Item.getId();
            int li_modelondex = findModelIndex(str_key) - 1; //
            group_index.add(new Integer(li_modelondex));
        }
        int findCount = 0;
        Hashtable temp_ht = new Hashtable();

        Vector temp_vec = new Vector();
        for (int i = 0; i < _objvalue.length; i++) {
            if (findObj(_objvalue[i])) {
                findCount++;
            }
            if (findCount % 2 == 1) { // 统计两个临界行之间的所有要求进行统计的列
                temp_vec.add(_objvalue[i]);

                for (int j = 0; j < group_index.size(); j++) {
                    int groupIndex = ( (Integer) group_index.get(j)).intValue();
                    Object obj_temp = _objvalue[i][groupIndex];
                    String str_temp = null;
                    if (obj_temp == null) {
                        str_temp = "0";
                    } else {
                        str_temp = obj_temp.toString();
                    }
                    double temp_value = Double.parseDouble( (String) str_temp);

                    if (temp_ht.get(group_index.get(j)) == null) {
                        temp_ht.put(group_index.get(j), new Double(temp_value));
                    } else {
                        double old_value = ( (Double) temp_ht.get(group_index.get(j))).doubleValue();
                        temp_ht.put(group_index.get(j), new Double(df.format(temp_value + old_value)));
                    }
                }
                if (i == _objvalue.length - 1) { // 如果是最后一组，把统计结果加入到最后一行
                    Double sum_value = null;
                    Object[] temp_obj = new Object[_objvalue[i].length];
                    Object[] result_obj = new Object[group_index.size()];
                    int result_index = 0;
                    Enumeration enum_ht = temp_ht.keys();
                    while (enum_ht.hasMoreElements()) {
                        Integer column_index = (Integer) enum_ht.nextElement();
                        sum_value = (Double) temp_ht.get(column_index);
                        temp_obj[column_index.intValue()] = sum_value;
                        result_obj[result_index] = sum_value;
                        result_index++;
                    }
                    vm_result.put(getVMKey(_objvalue[i]), result_obj);
                    group_rows.add(new Integer(vm_result.size() - 1));
                    temp_vec.add(temp_obj);
                    temp_ht.clear();
                }
            } else { // 相同的元素统计完，然后加入Vec_temp中
                Double sum_value = null;
                Object[] temp_obj = new Object[_objvalue[i].length];
                Object[] result_obj = new Object[group_index.size()];
                int result_index = 0;

                Enumeration enum_ht = temp_ht.keys();
                while (enum_ht.hasMoreElements()) {
                    Integer column_index = (Integer) enum_ht.nextElement();
                    sum_value = (Double) temp_ht.get(column_index);
                    temp_obj[column_index.intValue()] = sum_value;
                    result_obj[result_index] = sum_value;
                    result_index++;
                }
                temp_vec.add(temp_obj);
                vm_result.put(getVMKey(_objvalue[i - 1]), result_obj);
                group_rows.add(new Integer(vm_result.size() - 1));
                temp_ht.clear();
                i--;
            }
        }
        putValue(temp_vec);
    }

    /**
     * 排序处理
     */
    private void sortByColumn() {
        if (vm_sort_item.size() == 0) {
            return;
        }
        Object[] keys = vm_sort_item.getKeys();
        for (int i = 0; i < keys.length; i++) {
            JListItemVO temp_Item = (JListItemVO) keys[i];
            String str_key = (String) temp_Item.getId();
            boolean bl_flag = ( (Boolean) vm_sort_item.get(temp_Item)).booleanValue();

            int li_modelondex = findModelIndex(str_key); //

            System.out.println("The get pos is:" + li_modelondex);
            if (i == keys.length - 1) {
                lastcolumn_falg = true;
            }
            vec_sort_item.insertElementAt(new Integer(li_modelondex), 0);

            if (!bl_flag) {
                Object[][] newObjects = sortObjsByDesc(obj_values, li_modelondex, true); // 升序
                obj_values = newObjects;

                // 排序完毕后，进行分组统计(lastcolumn_falg的作用)
                if (vec_group_item.size() > 0 && lastcolumn_falg) {
                    addGroupRow(newObjects, true);
                } else {
                    putValue(newObjects);
                }
            } else {
                Object[][] newObjects = sortObjsByAsc(obj_values, li_modelondex, false); // 降序
                obj_values = newObjects;
                if (vec_group_item.size() > 0 && lastcolumn_falg) {
                    addGroupRow(newObjects, true);
                } else {
                    putValue(newObjects);
                }
            }
        }
    }

    /**
     * 升序排序
     *
     * @param _objs
     * @param _pos
     * @param _isdesc
     * @return
     */
    public Object[][] sortObjsByDesc(Object[][] _objs, int _pos, boolean _isdesc) {
        Vector v_tmp = new Vector();

        for (int i = 0; i < _objs.length; i++) {
            int li_pos = findPos(v_tmp, _objs[i], _pos); // 找出对应的位置
            v_tmp.insertElementAt(_objs[i], li_pos); // 插入数据
        }

        Object[][] objs_return = new Object[v_tmp.size()][_objs[0].length];

        if (_isdesc) { // 升序
            for (int i = 0; i < v_tmp.size(); i++) {
                objs_return[i] = (Object[]) v_tmp.get(i);
            }
        }

        return objs_return;
    }

    /**
     * 降序排序
     *
     * @param _objs
     * @param _pos
     * @param _isdesc
     * @return
     */
    private Object[][] sortObjsByAsc(Object[][] _objs, int _pos, boolean _isdesc) {
        Vector v_tmp = new Vector();

        for (int i = 0; i < _objs.length; i++) {
            int li_pos = findPosAsc(v_tmp, _objs[i], _pos); // 找出对应的位置
            v_tmp.insertElementAt(_objs[i], li_pos); // 插入数据
        }

        Object[][] objs_return = new Object[v_tmp.size()][_objs[0].length];

        for (int i = 0; i < v_tmp.size(); i++) {
            objs_return[i] = (Object[]) v_tmp.get(i);
        }
        return objs_return;
    }

    /**
     * 找出升序要插入元素的位置
     *
     * @param _v
     * @param _objs
     * @param _pos
     * @return
     */
    private int findPos(Vector _v, Object[] _objs, int _pos) {
        int li_pos = 0;
        int li_compareresult = -1;
        for (int i = 0; i < _v.size(); i++) {
            Object[] objs = (Object[]) _v.get(i); // 队列中的对象

            String str_type = templetItemVOs[_pos - 1].getItemtype(); // 类型
            if (str_type.equals("数字框")) {
                li_compareresult = Integer.parseInt( (String) _objs[_pos]) - Integer.parseInt( (String) objs[_pos]);
            } else {
                li_compareresult = compareObject(objs[_pos], _objs[_pos]);
            }

            if (li_compareresult > 0) { // 如果发现我比队列中的数据大,则继续往下走
            } else if (li_compareresult < 0) { // 如果发现我比队列中的数据小，则返回
                return i;
            } else {
            }
            li_pos++;
        }
        return li_pos;
    }

    /**
     * 找出降序要插入元素的位置
     *
     * @param _v
     * @param _objs
     * @param _pos
     * @return
     */
    private int findPosAsc(Vector _v, Object[] _objs, int _pos) {
        int li_pos = 0;
        int li_compareresult = -1;
        for (int i = 0; i < _v.size(); i++) {
            Object[] objs = (Object[]) _v.get(i); // 队列中的对象

            String str_type = templetItemVOs[_pos - 1].getItemtype(); // 类型
            if (str_type.equals("数字框")) {
                li_compareresult = Integer.parseInt( (String) _objs[_pos]) - Integer.parseInt( (String) objs[_pos]);
            } else {
                li_compareresult = compareObject(objs[_pos], _objs[_pos]);
            }

            if (li_compareresult < 0) { // 如果发现我比队列中的数据小,则继续往下走
            } else if (li_compareresult > 0) { // 如果发现我比队列中的数据大，则返回
                return i;
            } else {
            }
            li_pos++;
        }
        return li_pos;
    }

    /**
     * 比较两个Object
     *
     * @param _obj1
     * @param _obj2
     * @return
     */
    private int compareObject(Object _obj1, Object _obj2) {
        //
        String str_1 = null;
        String str_2 = null;
        if (_obj1 != null) {
            str_1 = _obj1.toString();
        }
        if (_obj2 != null) {
            str_2 = _obj2.toString();
        }

        if (str_1 == null) {
            if (str_2 == null) {
                return 0;
            } else {
                return 1;
            }
        } else {
            if (str_2 == null) {
                return -1;
            } else {
                Collator myCollator = Collator.getInstance();
                return myCollator.compare(str_2, str_1);
            }
        }
    }

    private void putValue(Vector _vec) {
        clearTable();
        for (int i = 0; i < _vec.size(); i++) {
            addRow( (Object[]) _vec.get(i));
        }
    }

    private void putValue(Object[][] _objs) {
        clearTable();
        for (int i = 0; i < _objs.length; i++) {
            addRow(_objs[i]);
        }
        this.obj_values = _objs;
    }

    private void clearTable() {
        DefaultTableModel model = (DefaultTableModel) jt_table.getModel();
        int li_rowcount = jt_table.getRowCount();
        for (int i = 0; i < li_rowcount; i++) {
            model.removeRow(0);
        }
        jt_table.updateUI();
    }

    private void addRow(Object[] _objs) {
        Object[] allobjs = new Object[templetItemVOs.length];
        for (int i = 0; i < allobjs.length; i++) {
            allobjs[i] = null;
        }
        for (int i = 0; i < allobjs.length; i++) {
            if (i < _objs.length) {
                allobjs[i] = _objs[i];
            }
        }
        ( (DefaultTableModel) jt_table.getModel()).addRow(allobjs);
    }

    private int findModelIndex(String _key) {
        for (int i = 0; i < templetItemVOs.length; i++) {
            if (templetItemVOs[i].getItemkey().equalsIgnoreCase(_key)) {
                return i + 1;
            }
        }
        return -1;
    }

    protected void onCancel() {
        this.dispose();
    }

    class MyTableHeaderRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 6906068457979976168L;

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
            Object obj = table.getModel().getValueAt(row, 0);
            String str_type = null;

            if (obj instanceof String) {
                str_type = (String) obj;
            } else {
                obj = table.getModel().getValueAt(row, 1);
                str_type = (String) obj;
            }
            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                if (str_type == null) {
                    setBackground(Color.ORANGE);
                } else {
                    setBackground(Color.WHITE);
                }
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }
}
/*******************************************************************************
 * $RCSfile: SortAndGroupDialog.java,v $ $Revision: 1.2 $ $Date: 2007/02/27
 * 06:57:19 $
 *
 * $Log: SortAndGroupDialog.java,v $
 * Revision 1.2  2007/05/31 07:38:18  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:01:06  qilin
 * no message
 *
 * Revision 1.5  2007/03/13 15:03:43  shxch
 * *** empty log message ***
 * Revision 1.4 2007/02/27 06:57:19 shxch ***
 * empty log message ***
 *
 * Revision 1.3 2007/02/10 08:51:57 shxch *** empty log message ***
 *
 * Revision 1.2 2007/01/30 05:14:31 lujian *** empty log message ***
 *
 *
 ******************************************************************************/
