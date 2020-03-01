/**************************************************************************
 * $RCSfile: DictManagerFrame.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:24 $
 **************************************************************************/
package smartx.framework.metadata.ui.dictmanager;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import smartx.framework.common.ui.*;
import smartx.framework.common.vo.*;
import smartx.framework.metadata.ui.*;
import smartx.framework.metadata.ui.componentscard.*;


/**
 * 数据字典的管理
 *
 * @author Administrator
 *
 */
public class DictManagerFrame extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 1344666737329565505L;

    private static final Font textFont = new Font("宋体", Font.PLAIN, 12);

    private static final int li_blp_db_counts = 7;

    private static final int li_blp_table_details_counts = 6;

    private static final int li_mtp_array_counts = 4;

    private JTabbedPane jtp_con = null;

    private MyTextPane[] mtp_array = null;

    private String str_parentcode = null;

    private BillListPanel[] blp_db = null;

    private BillListPanel[] blp_table_details = null;

    private MouseAdapter listener = null;

    private String str_selected_table = null;

    private JTextArea jta_text = null;

    private HashMap hm_data = null;

    private JButton jbt_check;

    private boolean[] bl_state = null;

    private int li_selected_pane = 0;

    public DictManagerFrame() {
        this.setTitle("数据字典管理");
        this.setSize(999, 600);
        this.setLocation(0, 0);
        initFrame();
    }

    private void initFrame() {
        mtp_array = new MyTextPane[li_mtp_array_counts];
        blp_db = new BillListPanel[li_blp_db_counts];
        blp_table_details = new BillListPanel[li_blp_table_details_counts];

        listener = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() >= 1) {
                    dealTableClick(e);
                }
            }
        };
        jtp_con = new JTabbedPane();
        jtp_con.addTab("表管理", getTablePanel());
        jtp_con.addTab("视图管理", getViewPanel());
        jtp_con.addTab("存储过程", getDBDetailPanel("PROCEDURE", 4));
        jtp_con.addTab("存储函数", getDBDetailPanel("FUNCTION", 5));
        jtp_con.addTab("触发器管理", getDBDetailPanel("TRIGGER", 6));
        jtp_con.addTab("CMM校验", getCheckPanel());
        jtp_con.addMouseListener(listener);

        this.getContentPane().add(jtp_con, BorderLayout.CENTER);
        this.getContentPane().add(getSouthPanel(), BorderLayout.SOUTH);
    }

    /**
     * 获得表维护面板
     *
     * @return
     */
    private Component getTablePanel() {
        blp_db[0] = new BillListPanel(new Table_VO(), false, false);
        blp_db[0].QueryDataByCondition("1=1"); //
        blp_db[0].setPageScrollable(false);
        (blp_db[0].getTable()).setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        (blp_db[0].getTable()).addMouseListener(listener);

        blp_db[1] = new BillListPanel(new TableField_VO(), false, false);
        // blp_db[1].setPageScrollable(false);
        (blp_db[1].getTable()).setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        (blp_db[1].getTable()).addMouseListener(listener);

        JSplitPane _split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, blp_db[1], getTableDetailInfoPane());
        _split.setDividerLocation(300);
        _split.setDividerSize(5);
        _split.setOneTouchExpandable(true);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, blp_db[0], _split);
        splitPane.setDividerLocation(280);
        splitPane.setDividerSize(10);
        splitPane.setOneTouchExpandable(true);

        return splitPane;
    }

    /**
     * 获得视图维护面板
     *
     * @return
     */
    private Component getViewPanel() {
        blp_db[2] = new BillListPanel(new View_VO(), false, false);
        blp_db[2].QueryDataByCondition("1=1"); //
        blp_db[2].setPageScrollable(false);
        (blp_db[2].getTable()).setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        (blp_db[2].getTable()).addMouseListener(listener);

        blp_db[3] = new BillListPanel(new TableField_VO("FIELD"), false, false);
        // blp_db[3].setPageScrollable(false);
        (blp_db[3].getTable()).setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        (blp_db[3].getTable()).addMouseListener(listener);

        JTabbedPane jtp_view = new JTabbedPane();
        jtp_view.add("视图构建查看", getMyTextPaneScroll(0));

        JSplitPane _split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, blp_db[3], jtp_view);
        _split.setDividerLocation(300);
        _split.setDividerSize(5);
        _split.setOneTouchExpandable(true);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, blp_db[2], _split);
        splitPane.setDividerLocation(280);
        splitPane.setDividerSize(10);
        splitPane.setOneTouchExpandable(true);

        return splitPane;
    }

    private Component getTableDetailInfoPane() {
        bl_state = new boolean[li_blp_table_details_counts];
        blp_table_details[0] = new BillListPanel(new TableDetailInfo_VO(), false, false);
        blp_table_details[1] = new BillListPanel(new TableDetailInfo_VO(), false, false);
        blp_table_details[2] = new BillListPanel(new TableDetailInfo_VO(), false, false);
        blp_table_details[3] = new BillListPanel(new TableDetailInfo_VO(), false, false);
        blp_table_details[4] = new BillListPanel(new TriggerInfo_VO(), false, false);
        blp_table_details[5] = new BillListPanel(new TableIndexes_VO(), false, false);

        JTabbedPane jtp_info = new JTabbedPane();
        jtp_info.add("主键", blp_table_details[0]);
        jtp_info.add("外键", blp_table_details[1]);
        jtp_info.add("被引用外键", blp_table_details[2]);
        jtp_info.add("约束", blp_table_details[3]);
        jtp_info.add("触发器", blp_table_details[4]);
        jtp_info.add("索引", blp_table_details[5]);

        jtp_info.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                Object obj = e.getSource();
                if (obj instanceof JTabbedPane) {
                    li_selected_pane = ( (JTabbedPane) obj).getSelectedIndex();
                    onJtpChange(li_selected_pane);
                }
            }
        });

        return jtp_info;
    }

    protected void onJtpChange(int selectedIndex) {
        if (bl_state[selectedIndex]) {
            return;
        }
        bl_state[selectedIndex] = true;
        String str_sql = "";

        if (selectedIndex == 0) {
            str_sql = "Select * From PUB_TABLE_INFO Where 1=1  and TABLE_NAME = '" + str_selected_table +
                "' And CONSTRAINT_TYPE = 'P'";
        } else if (selectedIndex == 1) {
            str_sql = "Select * From PUB_TABLE_INFO Where 1=1  and TABLE_NAME = '" + str_selected_table +
                "' And CONSTRAINT_TYPE = 'R'";
        } else if (selectedIndex == 2) {
            str_sql = "Select * From PUB_TABLE_INFO Where 1=1  and R_TABLE = '" + str_selected_table + "'";
        } else if (selectedIndex == 3) {
            str_sql = "Select * From PUB_TABLE_INFO Where 1=1  and TABLE_NAME = '" + str_selected_table +
                "' And CONSTRAINT_TYPE = 'C'";
        } else if (selectedIndex == 4) {
            str_sql = "Select TRIGGER_NAME,TRIGGER_TYPE,TRIGGERING_EVENT,TABLE_NAME,DESCRIPTION,TRIGGER_BODY From USER_TRIGGERS Where 1=1  and TABLE_NAME = '" +
                str_selected_table + "'";
        } else if (selectedIndex == 5) {
            str_sql = "Select * From PUB_TABLE_INDEXES Where 1=1  and TABLE_NAME = '" + str_selected_table + "'";
        } else {
            return;
        }
        blp_table_details[selectedIndex].QueryData(str_sql);
    }

    /**
     * 根据type不同来确定产生不同的面板 type有：FUNCTION,PROCEDURE,TRIGGER
     *
     * @param _type
     * @param _index
     * @return
     */
    private Component getDBDetailPanel(String _type, int _index) {
        blp_db[_index] = new BillListPanel(new DBDetail_VO(_type), false, false);

        String str_sql = "select distinct NAME from user_source where type = '" + _type + "'";
        if (_type.equals("TRIGGER")) {
            str_sql = "Select TRIGGER_NAME, TABLE_NAME, STATUS From USER_TRIGGERS";
        }
        blp_db[_index].QueryData(str_sql);
        blp_db[_index].setPageScrollable(false);
        (blp_db[_index].getTable()).setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        (blp_db[_index].getTable()).addMouseListener(listener);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, blp_db[_index],
                                              getMyTextPaneScroll(_index - 3));
        splitPane.setDividerLocation(280);
        splitPane.setDividerSize(10);
        splitPane.setOneTouchExpandable(true);

        return splitPane;
    }

    /**
     * 根据编号产生相对应的JScrollPane mtp_array[0]: mtp_view_detail mtp_array[1]:
     * mtp_procedure mtp_array[2]: mtp_function mtp_array[3]: mtp_trigger
     *
     * @param _index
     * @return
     */
    private JScrollPane getMyTextPaneScroll(int _index) {
        mtp_array[_index] = new MyTextPane();
        mtp_array[_index].requestFocus();
        mtp_array[_index].setCaretPosition(0);
        mtp_array[_index].setBackground(Color.WHITE);
        mtp_array[_index].setCursor(new Cursor(Cursor.TEXT_CURSOR));

        JScrollPane scrollPane = new JScrollPane(mtp_array[_index]);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        return scrollPane;
    }

    /**
     * 获得查看校验面板
     * @return
     */
    private Component getCheckPanel() {
        JPanel jpn_check = new JPanel();

        jta_text = new JTextArea();
        jta_text.setFont(textFont);
        jta_text.setLineWrap(true);
        jta_text.setWrapStyleWord(true);

        JScrollPane jsp_text = new JScrollPane(jta_text);
        jsp_text.setPreferredSize(new Dimension(270, 400));

        jpn_check.setLayout(new BorderLayout());
        jpn_check.add(jsp_text, BorderLayout.CENTER);

        return jpn_check;
    }

    /**
     * 处理查看校验按钮
     */
    protected void onCheckData() {
        jta_text.setText("");
        getCheckData();
        jta_text.append("----------不符合CMM标准的数据库对象如下：------------\n");
        jta_text.append("----------主键------------\n");
        jta_text.append("\n");

        String[] str_tit_1 = new String[] {"数据表", "原始主键", "CMM标准主键"};
        String[][] str_1 = getDealString("DICT_CHECK_P", new String[] {"TABLE_NAME", "CONSTRAINT_NAME", "COLUMN_NAME"},
                                         str_tit_1);
        jta_text.append(str_tit_1[0] + "\t" + str_tit_1[1] + "\t" + str_tit_1[2] + "\n");
        for (int i = 0; i < str_1.length; i++) {
            if (!str_1[i][1].startsWith("PK_" + str_1[i][0].trim())) {
                jta_text.append(str_1[i][0] + "\t" + str_1[i][1] + "\tPK_" + str_1[i][0].trim() + str_1[i][2] + "\n");
            }
        }
        jta_text.append("\n----------外键------------\n");
        jta_text.append("\n");
        String[] str_tit_2 = new String[] {"数据表", "原始外键", "CMM标准外键"};
        String[][] str_2 = getDealString("DICT_CHECK_R", new String[] {"TABLE_NAME", "CONSTRAINT_NAME", "R_TABLE",
                                         "R_COLUMN"}, str_tit_2);
        jta_text.append(str_tit_2[0] + "\t" + str_tit_2[1] + "\t" + str_tit_2[2] + "\n");
        for (int i = 0; i < str_2.length; i++) {
            if (!str_2[i][1].startsWith("FK_" + str_2[i][2].trim())) {
                jta_text.append(str_2[i][0] + "\t" + str_2[i][1] + "\tFK_" + str_2[i][2].trim() + str_2[i][3].trim() +
                                "\n");
            }
        }
        jta_text.append("\n----------约束------------\n");
        jta_text.append("\n");
        String[] str_tit_3 = new String[] {"数据表", "原始约束", "CMM标准约束"};
        String[][] str_3 = getDealString("DICT_CHECK_C", new String[] {"TABLE_NAME", "CONSTRAINT_NAME", "COLUMN_NAME"},
                                         str_tit_3);
        jta_text.append(str_tit_3[0] + "\t" + str_tit_3[1] + "\t" + str_tit_3[2] + "\n");
        for (int i = 0; i < str_3.length; i++) {
            if (!str_3[i][1].startsWith("UK_" + str_3[i][0].trim())) {
                jta_text.append(str_3[i][0] + "\t" + str_3[i][1] + "\tUK_" + str_3[i][0].trim() + str_3[i][2].trim() +
                                "\n");
            }
        }
        jta_text.append("\n----------索引------------\n");
        jta_text.append("\n");
        String[] str_tit_4 = new String[] {"数据表", "原始索引", "CMM标准索引"};
        String[][] str_4 = getDealString("DICT_CHECK_INX", new String[] {"TABLE_NAME", "INDEX_NAME", "INDEX_TYPE"},
                                         str_tit_4);
        jta_text.append(str_tit_4[0] + "\t" + str_tit_4[1] + "\t" + str_tit_4[2] + "\n");
        for (int i = 0; i < str_4.length; i++) {
            String str_temp = "";
            if (str_4[i][2].trim().toUpperCase().equals("NORMAL")) {
                str_temp = "INX" + str_4[i][0];
            } else {
                str_temp = "XFK" + str_4[i][0];
            }
            if (!str_4[i][1].startsWith(str_temp)) {
                jta_text.append(str_4[i][0] + "\t" + str_4[i][1] + str_temp.trim() + "\n");
            }
        }
        jta_text.append("\n----------视图------------\n");
        jta_text.append("\n");
        String[] str_tit_5 = new String[] {"视图", "CMM标准视图"};
        String[][] str_5 = getDealString("DICT_CHECK_VIEW", new String[] {"VIEW_NAME"}, str_tit_5);
        jta_text.append(str_tit_5[0] + "\t" + str_tit_5[1] + "\n");
        for (int i = 0; i < str_5.length; i++) {
            if (!str_5[i][0].startsWith("V_")) {
                jta_text.append(str_5[i][0] + "\tV_" + str_5[i][0] + "\n");
            }
        }
        jta_text.append("\n----------序列号------------\n");
        jta_text.append("\n");
        String[] str_tit_6 = new String[] {"序列号", "CMM标准序列号"};
        String[][] str_6 = getDealString("DICT_CHECK_SEQ", new String[] {"SEQUENCE_NAME"}, str_tit_6);
        jta_text.append(str_tit_6[0] + "\t" + str_tit_6[1] + "\n");
        for (int i = 0; i < str_6.length; i++) {
            if (!str_6[i][0].startsWith("S_")) {
                jta_text.append(str_6[i][0] + "\tS_" + str_6[i][0] + "\n");
            }
        }
        jta_text.append("\n----------触发器------------\n");
        jta_text.append("\n");
        String[] str_tit_7 = new String[] {"数据表", "触发器", "CMM标准序触发器"};
        String[][] str_7 = getDealString("DICT_CHECK_TRG", new String[] {"TABLE_NAME", "TRIGGER_NAME",
                                         "TRIGGERING_EVENT"}, str_tit_7);
        jta_text.append(str_tit_7[0] + "\t" + str_tit_7[1] + "\t" + str_tit_7[2] + "\n");
        for (int i = 0; i < str_7.length; i++) {
            String str_temp = "";
            if (str_7[i][2].toUpperCase().indexOf("INSERT") >= 0) {
                str_temp = str_temp + "I";
            }
            if (str_7[i][2].toUpperCase().indexOf("UPDATE") >= 0) {
                str_temp = str_temp + "U";
            }
            if (str_7[i][2].toUpperCase().indexOf("DELETE") >= 0) {
                str_temp = str_temp + "D";
            }
            if (!str_7[i][1].startsWith("TRG_" + str_temp)) {
                jta_text.append(str_7[i][0] + "\t" + str_7[i][1] + "\tTRG_" + str_temp + "_" + str_7[i][0] + "\n");
            }
        }
        jta_text.append("\n----------储存过程------------\n");
        jta_text.append("\n");
        String[] str_tit_8 = new String[] {"储存过程", "CMM标准序储存过程"};
        String[][] str_8 = getDealString("DICT_CHECK_PRO", new String[] {"NAME"}, str_tit_8);
        jta_text.append(str_tit_8[0] + "\t" + str_tit_8[1] + "\n");
        for (int i = 0; i < str_8.length; i++) {
            if (!str_8[i][0].startsWith("P_")) {
                jta_text.append(str_8[i][0] + "\tP_" + str_8[i][0] + "\n");
            }
        }
        jta_text.append("\n----------储存函数------------\n");
        jta_text.append("\n");
        String[] str_tit_9 = new String[] {"储存函数", "CMM标准序储存函数"};
        String[][] str_9 = getDealString("DICT_CHECK_FN", new String[] {"NAME"}, str_tit_9);
        jta_text.append(str_tit_9[0] + "\t" + str_tit_9[1] + "\n");
        for (int i = 0; i < str_9.length; i++) {
            if (!str_9[i][0].startsWith("F_")) {
                jta_text.append(str_9[i][0] + "\tF_" + str_9[i][0] + "\n");
            }
        }
        jta_text.setCaretPosition(0);
    }

    /**
     * 根据_key来获得相对应的HashVO[]，进行对其进行处理
     *
     * @param _key
     * @param _cols
     * @param _title
     * @return
     */
    private String[][] getDealString(String _key, String[] _cols, String[] _title) {
        int[] li_width = new int[_cols.length];

        HashVO[] hv_p = (HashVO[]) hm_data.get(_key);
        String[][] str_return = new String[hv_p.length][_cols.length];
        for (int i = 0; i < hv_p.length; i++) { //计算每列的最大长度
            int[] li_temp = new int[_cols.length];
            for (int j = 0; j < _cols.length; j++) {
                li_temp[j] = SwingUtilities.computeStringWidth(jta_text.getFontMetrics(textFont),
                    hv_p[i].getStringValue(_cols[j]));
                if (li_width[j] < li_temp[j]) {
                    li_width[j] = li_temp[j];
                }
            }
        }
        for (int i = 0; i < _title.length - 1; i++) { //处理标题
            int[] li_temp = new int[_cols.length];
            li_temp[i] = SwingUtilities.computeStringWidth(jta_text.getFontMetrics(textFont), _title[i]);
            for (int k = 0; k < (li_width[i] - li_temp[i]) / 6 + 1; k++) {
                _title[i] = _title[i] + " ";
            }
        }

        for (int i = 0; i < hv_p.length; i++) { //处理显示的数据
            int[] li_temp = new int[_cols.length];
            for (int j = 0; j < li_width.length; j++) {
                String str_temp = hv_p[i].getStringValue(_cols[j]);
                li_temp[j] = SwingUtilities.computeStringWidth(jta_text.getFontMetrics(textFont), str_temp);
                for (int k = 0; k < (li_width[j] - li_temp[j]) / 6 + 1; k++) {
                    str_temp = str_temp + " ";
                }
                str_return[i][j] = str_temp;
            }
        }

        return str_return;
    }

    /**
     * 获得所有校验数据
     *
     * @return
     */
    private HashMap getCheckData() {
        if (hm_data != null) {
            return hm_data;
        }
        String[] str_attrs = new String[] {"DICT_CHECK_P", "DICT_CHECK_R", "DICT_CHECK_C", "DICT_CHECK_INX",
            "DICT_CHECK_VIEW", "DICT_CHECK_SEQ", "DICT_CHECK_TRG", "DICT_CHECK_PRO", "DICT_CHECK_FN"};
        String[] str_sqls = new String[] {
            "Select TABLE_NAME, CONSTRAINT_NAME, COLUMN_NAME From pub_table_info Where CONSTRAINT_TYPE = 'P'", //
            "Select TABLE_NAME, CONSTRAINT_NAME, R_TABLE, R_COLUMN From pub_table_info Where CONSTRAINT_TYPE = 'R'", //
            "Select TABLE_NAME, CONSTRAINT_NAME, COLUMN_NAME From pub_table_info Where CONSTRAINT_TYPE = 'C'", //
            "Select TABLE_NAME, INDEX_NAME, INDEX_TYPE From PUB_TABLE_INDEXES", //
            "Select VIEW_NAME From User_Views", //
            "Select SEQUENCE_NAME From user_sequences", //
            "Select TABLE_NAME, TRIGGER_NAME, TRIGGERING_EVENT From USER_TRIGGERS", //
            "select distinct NAME from user_source where type = 'PROCEDURE'", //
            "select distinct NAME from user_source where type = 'FUNCTION'"};
        try {
            hm_data = UIUtil.getHashVoArrayReturnMapByMark(null, str_sqls, str_attrs);
        } catch (NovaRemoteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hm_data;
    }

    /**
     * 获取按钮面板
     *
     * @return
     */
    private Component getSouthPanel() {
        JPanel jpn_south = new JPanel();
        jpn_south.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 15));

        jbt_check = new JButton("查看校验结果");
        jbt_check.setPreferredSize(new Dimension(120, 20));
        jbt_check.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCheckData();
            }
        });
        jbt_check.setVisible(false);

        JButton jbt_refresh = new JButton("刷新表数据");
        jbt_refresh.setPreferredSize(new Dimension(100, 20));
        jbt_refresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onRefresh();
            }
        });

        JButton jbt_confirm = new JButton("确定");
        jbt_confirm.setPreferredSize(new Dimension(75, 20));
        jbt_confirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onConfirm();
            }
        });

        JButton jbt_cancel = new JButton("取消");
        jbt_cancel.setPreferredSize(new Dimension(75, 20));
        jbt_cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });
        jpn_south.add(jbt_check);
        jpn_south.add(jbt_refresh);
        jpn_south.add(jbt_confirm);
        jpn_south.add(jbt_cancel);
        return jpn_south;
    }

    private void refreshBlState() {
        for (int i = 0; i < 6; i++) {
            bl_state[i] = false;
        }
        onJtpChange(li_selected_pane);
        bl_state[li_selected_pane] = true;
    }

    /**
     * 处理表单击事件
     *
     * @param e
     */
    protected void dealTableClick(MouseEvent e) {
        if (e.getSource().equals(blp_db[0].getTable())) {
            int li_selected = blp_db[0].getTable().getSelectedRow();
            String str_selected_value = (String) blp_db[0].getTable().getModel().getValueAt(li_selected, 1);
            if (str_selected_table != null && str_selected_table.equals(str_selected_value)) {
                return;
            }
            str_selected_table = str_selected_value;
            refreshBlState();
            String field_sql = "Select * From DB_TABLE_FIELD where 1=1  and TABLE_NAME = '" + str_selected_value + "'";
            blp_db[1].QueryData(field_sql);

        } else if (e.getSource().equals(blp_db[2].getTable())) {
            int li_selected = blp_db[2].getTable().getSelectedRow();
            String str_selected_value = (String) blp_db[2].getTable().getModel().getValueAt(li_selected, 1);
            String field_sql = "Select * From DB_TABLE_FIELD where 1=1  and TABLE_NAME = '" + str_selected_value + "'";
            String str_view_sql = "Select TEXT From user_views Where VIEW_NAME = '" + str_selected_value + "'";

            String[][] str_view_detail = null;
            try {
                str_view_detail = UIUtil.getStringArrayByDS(null, str_view_sql);
            } catch (NovaRemoteException e1) {
                e1.printStackTrace();
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            String str_text = "";
            for (int i = 0; i < str_view_detail.length; i++) {
                str_text = str_text + str_view_detail[i][0];
            }
            mtp_array[0].setText(str_text);
            mtp_array[0].syntaxParse();
            mtp_array[0].setCaretPosition(0);

            blp_db[3].QueryData(field_sql);
        } else if (e.getSource().equals(blp_db[4].getTable())) {
            int li_selected = blp_db[4].getTable().getSelectedRow();
            String str_value = (String) blp_db[4].getTable().getModel().getValueAt(li_selected, 1);
            dealTextRefresh(mtp_array[1], "PROCEDURE", str_value);
        } else if (e.getSource().equals(blp_db[5].getTable())) {
            int li_selected = blp_db[5].getTable().getSelectedRow();
            String str_value = (String) blp_db[5].getTable().getModel().getValueAt(li_selected, 1);
            dealTextRefresh(mtp_array[2], "FUNCTION", str_value);
        } else if (e.getSource().equals(blp_db[6].getTable())) {
            int li_selected = blp_db[6].getTable().getSelectedRow();
            String str_value = (String) blp_db[6].getTable().getModel().getValueAt(li_selected, 1);
            dealTextRefresh(mtp_array[3], "TRIGGER", str_value);
        } else if (e.getSource().equals(jtp_con)) {
            int li_temp = jtp_con.getSelectedIndex();
            if (li_temp == 5) { //只有在点击CMM校验的时候,查看校验结果按钮才显示
                jbt_check.setVisible(true);
            } else {
                jbt_check.setVisible(false);
            }
        }
    }

    private void dealTextRefresh(MyTextPane _mtp, String _type, String _value) {
        String field_sql = "Select TEXT From user_source Where TYPE = '" + _type + "'" + " AND NAME = '" + _value +
            "' order by LINE";
        String[][] field_values = null;
        try {
            field_values = UIUtil.getStringArrayByDS(null, field_sql);
        } catch (NovaRemoteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String str_text = "";
        for (int i = 0; i < field_values.length; i++) {
            str_text = str_text + field_values[i][0];
        }
        _mtp.setText(str_text);
        _mtp.syntaxParse();
        _mtp.setCaretPosition(0);
    }

    /**
     * 从新刷新远程数据库中的自定义的两个数据字典表PUB_TABLE_INDEXES和PUB_TABLE_INFO
     */
    protected void onRefresh() {
        String[] str_execute_sql = new String[] {"Truncate Table PUB_TABLE_INDEXES", // 删除表PUB_TABLE_INDEXES

            "Insert Into PUB_TABLE_INDEXES Select user_indexes.INDEX_NAME," //
            + " INDEX_TYPE, COLUMN_NAME,user_indexes.TABLE_NAME, COMPRESSION," //
            + "PCT_FREE,INI_TRANS,MAX_TRANS,LAST_ANALYZED From user_indexes " //
            + "Left Join user_ind_columns On user_indexes.INDEX_NAME = " //
            + "user_ind_columns.INDEX_NAME", // 重新构建表PUB_TABLE_INDEXES

            "Truncate table PUB_TABLE_INFO", // 删除PUB_TABLE_INFO所有的信息

            "Insert Into PUB_TABLE_INFO Select USER_CONSTRAINTS.CONSTRAINT_NAME," //
            + " CONSTRAINT_TYPE, AA.COLUMN_NAME,BB.COLUMN_NAME R_COLUMN, " //
            + "USER_CONSTRAINTS.TABLE_NAME TABLE_NAME, BB.TABLE_NAME R_TABLE, " //
            + "R_CONSTRAINT_NAME, LAST_CHANGE From " //
            + "(USER_CONSTRAINTS Left Join USER_CONS_COLUMNS AA on " //
            + "USER_CONSTRAINTS.CONSTRAINT_NAME = AA.CONSTRAINT_NAME) " //
            + "Left Join USER_CONS_COLUMNS BB On USER_CONSTRAINTS.R_CONSTRAINT_NAME" //
            + " = BB.CONSTRAINT_NAME" // 重新构建表PUB_TABLE_INFO
        };
        try {
            UIUtil.executeBatchByDS(null, str_execute_sql); //
            JOptionPane.showMessageDialog(this, "刷新字典表数据成功，\n请重新打开本页查看新数据！");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "刷新字典表数据出错！");
        }
    }

    protected void onConfirm() {
        this.dispose();
    }

    protected void onCancel() {
        this.dispose();
    }
}
/**************************************************************************
 * $RCSfile: DictManagerFrame.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:24 $
 *
 * $Log: DictManagerFrame.java,v $
 * Revision 1.2  2007/05/31 07:38:24  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:02:12  qilin
 * no message
 *
 * Revision 1.11  2007/03/07 02:01:56  shxch
 * *** empty log message ***
 *
 * Revision 1.10  2007/03/02 05:16:41  shxch
 * *** empty log message ***
 *
 * Revision 1.9  2007/03/02 05:02:51  shxch
 * *** empty log message ***
 *
 * Revision 1.8  2007/03/01 09:06:55  shxch
 * *** empty log message ***
 *
 * Revision 1.7  2007/02/27 09:38:48  shxch
 * *** empty log message ***
 *
 * Revision 1.6  2007/02/27 06:03:00  shxch
 * *** empty log message ***
 *
 * Revision 1.5  2007/02/07 04:48:14  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/02/01 08:14:38  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/02/01 01:55:56  shxch
 * 添加等待框
 *
 * Revision 1.2  2007/01/30 05:01:56  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
