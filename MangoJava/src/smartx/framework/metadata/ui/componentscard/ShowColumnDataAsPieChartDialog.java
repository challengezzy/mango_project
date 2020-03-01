/**************************************************************************
 * $RCSfile: ShowColumnDataAsPieChartDialog.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:18 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentscard;

import java.text.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import smartx.framework.metadata.ui.*;


/**
 * 饼图查看某列数据
 *
 * @author user
 *
 */
public class ShowColumnDataAsPieChartDialog extends NovaDialog {

    private static final long serialVersionUID = 1L;

    private final static Font textFont = new Font("宋体", Font.PLAIN, 12);

    private String title = null;

    private String[] data = null;

    private String[] send_str = null;

    private Vector row_vec = null;

    private Pair pair = null;

    private Vector pair_vec = null;

    private PieChartPanel pieChartPanel = null;

    private double values[] = null;

    private double row_value[] = null;

    private JTable table = null;

    private String[] DefaultColoumnNames = {"内容", "记录数(/次)",
        "所占比例(/%)", "图中颜色"};

    private JButton confirmButton = null;

    private int index_selected = -1;

    /**
     * 该构造方法主要适合于，需要对传入的_data进行统计
     * 然后计算_data中的各个元素的比重，进行饼图显示
     * @param _parent：母板
     * @param _columnname：列明
     * @param _data：传入数据
     */
    public ShowColumnDataAsPieChartDialog(Container _parent,
                                          String _columnname, String[] _data) {
        super(_parent, _columnname + "饼图查看", 550, 500); //
        this.title = _columnname;
        this.data = _data;
        initialize();
    }

    /**
     * 该构造方法主要适合于，要显示的元素的比重已经获得，直需要显示
     * @param _parent
     * @param _title
     * @param _row_name
     * @param _value
     */
    public ShowColumnDataAsPieChartDialog(Container _parent, String _title,
                                          Vector _row_name, double[] _value) {
        super(_parent, _title + "饼图查看", 550, 500); //
        this.title = _title;
        this.row_vec = _row_name;
        this.row_value = _value;
        getColumnNames();
        getPairVec();
        initialize();
    }

    private void initialize() {
        getValues();
        pieChartPanel = new PieChartPanel(this, title, send_str, values);
        pieChartPanel.setPreferredSize(new Dimension(250, 280));

        JScrollPane scroll = getJSPTable();
        scroll.setPreferredSize(new Dimension(300, 100));

        JTabbedPane jtp_table = new JTabbedPane();
        jtp_table.add("表数据展示", scroll);

        confirmButton = new JButton("确定");
        confirmButton.setFont(textFont);
        confirmButton.setPreferredSize(new Dimension(75, 20));
        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onConfirm();
            }
        });

        Box buttonBox = Box.createHorizontalBox();
        buttonBox.add(Box.createGlue());
        buttonBox.add(confirmButton);
        buttonBox.add(Box.createGlue());

        JSplitPane _split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                                           pieChartPanel, jtp_table);
        _split.setDividerLocation(300);
        _split.setDividerSize(5);
        _split.setOneTouchExpandable(true);

        this.getContentPane().add(_split, BorderLayout.CENTER);
        this.getContentPane().add(buttonBox, BorderLayout.SOUTH);
    }

    protected void onConfirm() {
        this.dispose();
    }

    private void getColumnNames() {
        DefaultColoumnNames[1] = this.title;
    }

    private void dealSelectedChanged() {
        int selected_index = table.getSelectedRow();
        if (selected_index == table.getRowCount() - 1) {
            pieChartPanel.setSelectedKey( -1);
            return;
        }
        pieChartPanel.setSelectedKey(selected_index);
        return;
    }

    /**
     * 设置数据表中选中的行，该方法主要是被饼图面板
     * 在处理鼠标单击，而数据表做出相对应的举动的处理
     * @param _index
     */
    public void setSelectedRow(int _index) {
        if (_index != -1 && index_selected != _index) {
            Rectangle rect = table.getCellRect(_index, 0, true);
            table.scrollRectToVisible(rect);
            table.setRowSelectionInterval(_index, _index);

            index_selected = _index;
        } else if (_index == -1) {
            table.clearSelection();
        }
    }

    /**
     * 初始化数据表
     */
    private JScrollPane getJSPTable() {
        ListAndTableFactoty latf_table = new ListAndTableFactoty();

        Object[][] obj_values = new Object[pair_vec.size() + 1][];
        DecimalFormat df = new DecimalFormat("##.00");
        int i;
        for (i = 0; i < pair_vec.size(); i++) {
            pair = (Pair) pair_vec.get(i);
            obj_values[i] = new Object[] { (Object) pair.getKey(),
                new Double(pair.getValue()),
                (Object) (df.format(values[i] * 100))};
        }
        obj_values[i] = new Object[] { (Object) "合计",
            new Double(df.format(getSumValue())), new Integer(100)};
        JScrollPane jsp_temp = latf_table.getJSPTable(obj_values, DefaultColoumnNames, null);
        table = latf_table.getTable();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setColumnSelectionAllowed(false);
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    dealSelectedChanged();
                }
            }
        });
        latf_table.setAllColumnUnEditeable();

        TableCellRenderer myTableCellRenderer = new MyTableHeaderRenderer();
        latf_table.setColumnCellRender(myTableCellRenderer, DefaultColoumnNames.length - 1);

        return jsp_temp;
    }

    /**
     * 查看Vec容器中是否已经包含元素的pair
     * 如果包含，然后该元素pair的键值
     * @param _key
     * @return
     */
    private int containsKey(String _key) {
        for (int i = 0; i < pair_vec.size(); i++) {
            pair = (Pair) pair_vec.get(i);
            if (pair.getKey().equals(_key)) {
                return i;
            }
        }
        return -1;
    }

    private void getPairVec() {
        pair_vec = new Vector();
        for (int i = 0; i < this.row_vec.size(); i++) {
            Pair _pair = new Pair(row_vec.get(i).toString(), row_value[i]);
            pair_vec.add(_pair);
        }
    }

    /**
     * 获得统计值
     * @return
     */
    private double getSumValue() {
        double sum = 0;
        for (int i = 0; i < pair_vec.size(); i++) {
            pair = (Pair) pair_vec.get(i);
            sum = sum + pair.getValue();
        }
        return sum;
    }

    /**
     * 根据传过来的数据来获得各个元素的比重值
     *
     */
    private void getValues() {
        if (pair_vec == null) {
            pair_vec = new Vector();

            for (int i = 0; i < data.length; i++) {

                if (containsKey(data[i]) != -1) {
                    int item_count = (int) pair.getValue();
                    item_count++;
                    pair.setValue(item_count);
                    pair_vec.set(containsKey(data[i]), pair);
                } else {
                    pair = new Pair(data[i], 1);
                    pair_vec.add(pair);
                }
            }
        }

        sortVec();
        values = new double[pair_vec.size()];
        send_str = new String[pair_vec.size()];

        for (int i = 0; i < pair_vec.size(); i++) {
            DecimalFormat df = new DecimalFormat("##.00");
            pair = (Pair) pair_vec.get(i);

            String pair_key = pair.getKey();
            double pair_value = pair.getValue();

            values[i] = pair_value / getSumValue();
            send_str[i] = pair_key + " " + df.format(values[i] * 100) + "%";
        }
    }

    /**
     * 冒泡排序
     */
    private void sortVec() {
        double value_first = 0;
        double value_second = 0;
        Pair pair_first = null;
        Pair pair_second = null;

        for (int i = 0; i < pair_vec.size(); i++) {
            for (int j = pair_vec.size() - 1; j > i; j--) {
                pair_first = (Pair) pair_vec.get(i);
                pair_second = (Pair) pair_vec.get(j);

                value_first = (double) pair_first.getValue();
                value_second = (double) pair_second.getValue();

                if (value_first < value_second) {
                    Pair temp_pair = pair_first;
                    pair_vec.set(i, pair_second);
                    pair_vec.set(j, temp_pair);
                }
            }
        }
    }

    public void closeDialog() {
        this.dispose();
    }

    public static void main(String[] argv) {
        String[] data = new String[] {"aa", "bb", "cc", "dd", "aa", "ee",
            "aa", "cc", "aa", "ee", "dd"};
        ShowColumnDataAsPieChartDialog dialog = new ShowColumnDataAsPieChartDialog(
            null, "菜单", data);
        dialog.setVisible(true);
    }

    class MyTableHeaderRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = -1546156018632933567L;

        public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row,
            int column) {

            JLabel label = (JLabel)super.getTableCellRendererComponent(table,
                value, isSelected, hasFocus, row, column);

            label.setFont(textFont);
            int li_rowcount = table.getModel().getRowCount();
            if (row == li_rowcount - 1) { // 如果是最后一行,则彩色显示
                label.setBackground(Color.WHITE);
            } else {
                int color_length = PieChartPanel.Colors.length;
                label.setBackground(Color.WHITE);
                label.setForeground(Color.BLACK);
                if (column == 3) {
                    if (row > 0 && row % color_length == 0) {
                        label.setBackground(PieChartPanel.Colors[color_length - 3]);
                    } else {
                        label.setBackground(PieChartPanel.Colors[row % color_length]);
                    }
                }
            }
            return label;
        }

        public void validate() {
        }

        public void revalidate() {
        }

        protected void firePropertyChange(String propertyName, Object oldValue,
                                          Object newValue) {
        }

        public void firePropertyChange(String propertyName, boolean oldValue,
                                       boolean newValue) {
        }
    }
}

class Pair {
    private String key = null;

    private double value = 0;

    public Pair() {
        this.key = null;
        this.value = 0;
    }

    public Pair(String _key, double _value) {
        this.key = _key;
        this.value = _value;
    }

    public void setKey(String _key) {
        this.key = _key;
    }

    public String getKey() {
        return this.key;
    }

    public double getValue() {
        return this.value;
    }

    public void setValue(int _value) {
        this.value = _value;
    }
}
/**************************************************************************
 * $RCSfile: ShowColumnDataAsPieChartDialog.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:18 $
 *
 * $Log: ShowColumnDataAsPieChartDialog.java,v $
 * Revision 1.2  2007/05/31 07:38:18  qilin
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