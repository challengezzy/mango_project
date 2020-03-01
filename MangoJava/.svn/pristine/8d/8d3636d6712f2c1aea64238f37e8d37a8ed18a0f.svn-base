/**************************************************************************
 * $RCSfile: PubQueryTempletDialog.java,v $  $Revision: 1.2.8.2 $  $Date: 2009/02/02 16:12:54 $
 **************************************************************************/
package smartx.framework.metadata.ui;

/**
 * 获取查询选择界面，在选择需要的行以后，通过方法getQueryResult可以得到一个HashVO数组，即查询选择的结果.
 * by sxf.
 */
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import smartx.framework.common.ui.*;
import smartx.framework.common.vo.*;
import smartx.framework.metadata.ui.componentscard.*;
import smartx.framework.metadata.vo.*;


public class PubQueryTempletDialog extends NovaDialog {

    private static final long serialVersionUID = 1L;

    private Pub_QueryTempletVO querytempletVO = null;

    private Pub_QueryTemplet_ItemVO[] querytempletItemVOs = null;

    private Vector v_compents = new Vector();

    private JTable cardorderTable = null;

    private JTextArea queryTextareacard = null;

    private HashMap maplist = new HashMap();

    private HashMap map = new HashMap();

    private boolean isFirst = true;

    private StringBuffer sbcard = null;

    private Object[][] tabledata = null;

    private DefaultTableModel tableModel = null;

    private TableColumnModel columnModel = null;

    private TableColumn[] allTableColumns = null;

    private HashVO[] vos = null; // 返回的HASHVO

    private StringBuffer sb = null;

    private String[] tableheader = null;

    private boolean isadmin = true;

    /**
     * 根据查询模板编码,创建窗口!!
     *
     * @param _queryTempletCode
     */

    public PubQueryTempletDialog(Container _parent, String _querytempletcode) {
        super(_parent, "查询选择", 700, 630);
        try {
            querytempletVO = UIUtil.getPub_QueryTempletVO(_querytempletcode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        querytempletItemVOs = querytempletVO.getItemVOs();
        if (NovaClientEnvironment.getInstance().isAdmin() == true) {
            isadmin = true;
        } else {
            isadmin = false;
        }
        this.getContentPane().add(getMainPanel());
        this.setVisible(true);
    }

    public PubQueryTempletDialog(Container _parent, Pub_QueryTempletVO templetVO) {
        super(_parent, "查询选择", 700, 630);
        this.querytempletVO = templetVO;
        this.querytempletItemVOs = templetVO.getItemVOs();
        if (NovaClientEnvironment.getInstance().isAdmin() == true) {
            isadmin = true;
        } else {
            isadmin = false;
        }
        this.getContentPane().add(getMainPanel());
        this.setVisible(true);
    }

    private JPanel getMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        JScrollPane tablescrollPanel = new JScrollPane(getCardPanel());
        tablescrollPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "输入查询条件",
            TitledBorder.LEFT, TitledBorder.TOP, new Font("宋体", 0, 12)));
        tablescrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JPanel fixpanel = new JPanel();
        fixpanel.setLayout(new GridLayout(2, 1));
        fixpanel.add(tablescrollPanel);
        fixpanel.add(getTablePanel());
        mainPanel.add(fixpanel, BorderLayout.CENTER);
        if (isadmin == true) {
            mainPanel.add(getPreviewPanel(), BorderLayout.SOUTH);
        }
        mainPanel.setVisible(true);
        return mainPanel;
    }

    /**
     * 预览面板
     *
     * @return
     */
    private JScrollPane getPreviewPanel() {
        queryTextareacard = new JTextArea();
        queryTextareacard.setLineWrap(true);
        queryTextareacard.setEditable(false);
        queryTextareacard.setBackground(Color.LIGHT_GRAY);
        JScrollPane scrollPanel = new JScrollPane(queryTextareacard);
        scrollPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "预览SQL语句",
            TitledBorder.LEFT, TitledBorder.TOP, new Font("宋体", 0, 12)));
        scrollPanel.setPreferredSize(new Dimension(100, 100));
        scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        return scrollPanel;
    }

    /**
     * 表格所在面板
     *
     * @return
     */
    private JPanel getTablePanel() {
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "查询结果",
            TitledBorder.LEFT, TitledBorder.TOP, new Font("宋体", 0, 12)));
        tablePanel.add(new JScrollPane(getTable()), BorderLayout.CENTER);
        tablePanel.add(getTableBtnPanel(), BorderLayout.EAST);
        return tablePanel;
    }

    /**
     * 卡片所在面板
     *
     * @return
     */
    private JPanel getCardPanel() {
        JPanel rpanel = new JPanel();
        rpanel.setLayout(new BorderLayout());
        JPanel cardPanel = new JPanel();
        cardPanel.setPreferredSize(new Dimension(510, getCardPaneHeight()));
        FlowLayout flayout = new FlowLayout(FlowLayout.LEFT);
        flayout.setHgap(10);
        cardPanel.setLayout(flayout);
        for (int i = 0; i < querytempletItemVOs.length; i++) {
            String str_type = querytempletItemVOs[i].getItemType();
            if (str_type.equals("文本框")) {
                TextFieldPanel panel = new TextFieldPanel(querytempletItemVOs[i].getItemKey(),
                    querytempletItemVOs[i].getItemName());
                cardPanel.add(panel);
                v_compents.add(panel);
            } else if (str_type.equals("数字框")) {
                JPanel numberpanel = new JPanel();
                QueryNumberFieldstartPanel startpanel = new QueryNumberFieldstartPanel(querytempletItemVOs[i].getItemKey(), querytempletItemVOs[i].getItemName());
                QueryNumberFieldendPanel endpanel = new QueryNumberFieldendPanel(querytempletItemVOs[i].getItemKey(),querytempletItemVOs[i].getItemName());
                JLabel lablenull = new JLabel();
                lablenull.setPreferredSize(new Dimension(5, 20));
                numberpanel.add(startpanel);
                numberpanel.add(lablenull);
                numberpanel.add(endpanel);
                cardPanel.add(numberpanel);
                v_compents.add(startpanel);
                v_compents.add(endpanel);
            } else if (str_type.equals("下拉框")) {
                ComBoxPanel panel = new ComBoxPanel(querytempletItemVOs[i].getItemKey(),
                    querytempletItemVOs[i].getItemName(), querytempletItemVOs[i].getItemVOs());
                cardPanel.add(panel);
                v_compents.add(panel);
            } else if (str_type.equals("参照")) {
                UIRefPanel panel = new UIRefPanel(querytempletItemVOs[i].getItemKey(),
                                                  querytempletItemVOs[i].getItemName(),
                                                  querytempletItemVOs[i].getRefdesc());
                cardPanel.add(panel);
                v_compents.add(panel);
            } else if (str_type.equals("日历")) {
                JPanel datapanel = new JPanel();
                QueryUIDateTimestartPanel startPanel = new QueryUIDateTimestartPanel(querytempletItemVOs[i].getItemKey(), querytempletItemVOs[i].getItemName());
                QueryUIDateTimeendPanel endPanel = new QueryUIDateTimeendPanel(querytempletItemVOs[i].getItemKey(), querytempletItemVOs[i].getItemName());
                JLabel lablenull = new JLabel();
                lablenull.setPreferredSize(new Dimension(5, 20));
                datapanel.add(startPanel);
                datapanel.add(lablenull);
                datapanel.add(endPanel);
                cardPanel.add(datapanel);
                v_compents.add(startPanel);
                v_compents.add(endPanel);
            } else if (str_type.equals("时间")) {
                JPanel timepanel = new JPanel();
                QueryUITimeSetstartPanel startPanel = new QueryUITimeSetstartPanel(querytempletItemVOs[i].getItemKey(), querytempletItemVOs[i].getItemName());
                QueryUITimeSetendPanel endPanel = new QueryUITimeSetendPanel(querytempletItemVOs[i].getItemKey(), querytempletItemVOs[i].getItemName());
                JLabel lablenull = new JLabel();
                lablenull.setPreferredSize(new Dimension(5, 20));
                timepanel.add(startPanel);
                timepanel.add(lablenull);
                timepanel.add(endPanel);
                cardPanel.add(timepanel);
                v_compents.add(startPanel);
                v_compents.add(endPanel);
            } else if (str_type.equals("勾选框")) {
                UICheckBoxPanel panel = new UICheckBoxPanel(querytempletItemVOs[i].getItemKey(),
                    querytempletItemVOs[i].getItemName(), false);
                cardPanel.add(panel);
                v_compents.add(panel);
            } else if (str_type.equals("密码框")) {
                PasswordFieldPanel panel = new PasswordFieldPanel(querytempletItemVOs[i].getItemKey(),
                    querytempletItemVOs[i].getItemName());
                cardPanel.add(panel);
                v_compents.add(panel);
            } else if (str_type.equals("多行文本框")) {
                TextAreaPanel panel = new TextAreaPanel(querytempletItemVOs[i].getItemKey(),
                    querytempletItemVOs[i].getItemName());
                cardPanel.add(panel);
                v_compents.add(panel);
            } else if (str_type.equals("文件选择框")) {
                UIFilePathPanel panel = new UIFilePathPanel(querytempletItemVOs[i].getItemKey(),querytempletItemVOs[i].getItemName());
                cardPanel.add(panel);
                v_compents.add(panel);
            } else if (str_type.equals("颜色")) {
                UIColorPanel panel = new UIColorPanel(querytempletItemVOs[i].getItemKey(),querytempletItemVOs[i].getItemName());
                cardPanel.add(panel);
                v_compents.add(panel);
            } else if (str_type.equals("大文本框")) {
                UITextAreaPanel panel = new UITextAreaPanel(querytempletItemVOs[i].getItemKey(), querytempletItemVOs[i].getItemName());
                cardPanel.add(panel);
                v_compents.add(panel);
            } else if (str_type.equals("图片选择框")) {
                UIImagePanel panel = new UIImagePanel(querytempletItemVOs[i].getItemKey(),querytempletItemVOs[i].getItemName());
                cardPanel.add(panel);
                v_compents.add(panel);
            }

        }
        rpanel.add(cardPanel, BorderLayout.CENTER);
        rpanel.add(getCardBtnPanel(), BorderLayout.EAST);
        return rpanel;
    }

    private int getCardPaneHeight() {
        int li_height = 0;
        for (int i = 0; i < querytempletItemVOs.length; i++) {
            if (querytempletItemVOs[i].getItemType().equals("时间") || querytempletItemVOs[i].getItemType().equals("日历") ||
                querytempletItemVOs[i].getItemType().equals("数字框")) {
                li_height = li_height + 72;
            } else {
                li_height = li_height + 36;
            }
        }
        return li_height / 2;
    }

    private JPanel getTableBtnPanel() {
        JPanel rpanel = new JPanel();
        rpanel.setLayout(new BorderLayout());

        JPanel order_buttonpanel = new JPanel();
        order_buttonpanel.setPreferredSize(new Dimension(60, 45));
        order_buttonpanel.setLayout(new GridLayout(0, 1, 0, 5));
        JButton btn_order_confirm = new JButton("确定");
        JButton btn_order_cancel = new JButton("取消");
        btn_order_confirm.setPreferredSize(new Dimension(50, 20));
        btn_order_cancel.setPreferredSize(new Dimension(50, 20));
        order_buttonpanel.add(btn_order_cancel);
        order_buttonpanel.add(btn_order_confirm);
        btn_order_confirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCardConfirm();
            }
        });
        btn_order_cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCardExit();
            }
        });
        rpanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder()));
        rpanel.add(order_buttonpanel, BorderLayout.SOUTH);
        return rpanel;
    }

    /**
     * 卡片按钮面板
     *
     * @return
     */
    private JPanel getCardBtnPanel() {
        JPanel rpanel = new JPanel();
        rpanel.setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 1, 0, 5));
        buttonPanel.setPreferredSize(new Dimension(60, 70));
        JButton btn_confirm = new JButton("查询");
        JButton btn_release = new JButton("清空");
        JButton btn_preview = new JButton("预览");
        btn_confirm.setPreferredSize(new Dimension(50, 50));
        btn_release.setPreferredSize(new Dimension(50, 50));
        btn_preview.setPreferredSize(new Dimension(50, 50));
        btn_confirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    onCardQuery();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        btn_release.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearCard();
            }
        });
        btn_preview.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onPreviewcard();
            }
        });
        buttonPanel.add(btn_release);
        if (isadmin == true) {
            buttonPanel.add(btn_preview);
        }
        buttonPanel.add(btn_confirm);
        rpanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder()));
        rpanel.add(buttonPanel, BorderLayout.SOUTH);
        return rpanel;
    }

    /**
     * 表格.
     *
     * @return
     */
    private JTable getTable() {
        if (cardorderTable != null) {
            return cardorderTable;
        }
        cardorderTable = new JTable(getTableModel(), getColumnModel());
        cardorderTable.setAutoscrolls(true);
        cardorderTable.getTableHeader().setReorderingAllowed(false);
        cardorderTable.setRowHeight(18);
        cardorderTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        return cardorderTable;
    }

    public String getDataSourceName() {
        if (querytempletVO.getDataSourceName() == null || querytempletVO.getDataSourceName().trim().equals("null") ||
            querytempletVO.getDataSourceName().trim().equals("")) {
            return NovaClientEnvironment.getInstance().getDefaultDatasourceName(); // 默认数据源
        } else {
            return new FrameWorkTBUtil().convertExpression(NovaClientEnvironment.getInstance(),
                querytempletVO.getDataSourceName()); // 算出数据源!!
        }
    }

    private DefaultTableModel getTableModel() {
        if (tableModel != null) {
            return tableModel;
        }
        try {
            TableDataStruct tablestruct = UIUtil.getTableDataStructByDS(getDataSourceName(),
                this.getInitSQL() + " and 1=2");
            tableheader = tablestruct.getTable_header();
            tableModel = new DefaultTableModel();
            tableModel.setColumnCount(tableheader.length);
        } catch (Exception e) {
            NovaMessage.show(this, "生成表格出错，请检查SQL:" + this.getInitSQL(), NovaConstants.MESSAGE_ERROR);
            e.printStackTrace();
        }
        return tableModel;
    }

    public TableColumnModel getColumnModel() {
        if (columnModel != null) {
            return columnModel;
        }

        columnModel = new DefaultTableColumnModel();
        for (int i = 0; i < tableheader.length; i++) {
            columnModel.addColumn(getTableColumns()[i]);
        }

        return columnModel;
    }

    private TableColumn[] getTableColumns() {
        if (this.allTableColumns != null) {
            return allTableColumns;
        }
        allTableColumns = new TableColumn[tableheader.length];
        for (int i = 0; i < allTableColumns.length; i++) {
            TableCellEditor cellEditor = null;
            TableCellRenderer cellRender = null;

            cellRender = null;
            cellEditor = new TextFieldCellEditor(new JTextField());
            allTableColumns[i] = new TableColumn(i, 143, cellRender, cellEditor);
            allTableColumns[i].setHeaderValue(tableheader[i]);
            allTableColumns[i].setIdentifier(tableheader[i]);
        }
        return allTableColumns;
    }

    /**
     * 得到在数据库中定义的SQL.
     *
     * @return String
     */
    public String getInitSQL() {
        if (sb == null) {
            sb = new StringBuffer();
            sb.append(querytempletVO.getSql());
        }
        return sb.toString();
    }

    /**
     * 得到查询SQL，如果定义的SQL为空，刚返回"",如：select * from menu where id=1;
     *
     * @return String
     */
    public String getQuerySQL() {
        if (querytempletVO.getSql().equals("")) {
            return "";
        }
        StringBuffer tempstr = new StringBuffer();
        tempstr.append(querytempletVO.getSql()); //
        tempstr.append(this.getQueryCondition());
        return tempstr.toString();
    }

    protected void onCardQuery() throws Exception {
        clearTable();
        try {
            if (getQueryCondition() == null || getQueryCondition().equals("")) {
                tabledata = UIUtil.getStringArrayByDS(getDataSourceName(), this.getInitSQL());
            } else {
                if (getQuerySQL().equals("")) {
                    return;
                }
                tabledata = UIUtil.getStringArrayByDS(getDataSourceName(), this.getQuerySQL());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        DefaultTableModel model = (DefaultTableModel) cardorderTable.getModel();
        if (tabledata == null || tabledata.length <= 0) {
            return;
        } else {
            for (int i = 0; i < tabledata.length; i++) {
                model.addRow(tabledata[i]);
            }
            cardorderTable.updateUI();
        }
    }

    private void clearTable() {
        DefaultTableModel model = (DefaultTableModel) cardorderTable.getModel();
        int li_rowcount = cardorderTable.getRowCount();
        for (int i = 0; i < li_rowcount; i++) {
            model.removeRow(0);
        }
        cardorderTable.updateUI();
    }

    /**
     * 预览查询语句
     *
     */
    protected void onPreviewcard() {
        if (getQueryCondition() == null || getQueryCondition().length() <= 0) {
            queryTextareacard.setText(getInitSQL());
        } else {
            queryTextareacard.setText(getInitSQL() + getQueryCondition());
        }
    }

    protected void clearCard() {
        for (int i = 0; i < querytempletItemVOs.length; i++) {
            if (querytempletItemVOs[i].getItemType().equals("数字框") || querytempletItemVOs[i].getItemType().equals("时间") ||
                querytempletItemVOs[i].getItemType().equals("日历")) {
                setCompentObjectValue(querytempletItemVOs[i].getItemKey() + "START", null);
                setCompentObjectValue(querytempletItemVOs[i].getItemKey() + "END", null);
            } else if (querytempletItemVOs[i].getItemType().equals("勾选框")) {
                setCompentObjectValue(querytempletItemVOs[i].getItemKey(), "N");
            } else {
                setCompentObjectValue(querytempletItemVOs[i].getItemKey(), null);
            }
        }
    }

    protected void onCardConfirm() {
        int selectRows[] = cardorderTable.getSelectedRows();
        vos = new HashVO[selectRows.length];
        for (int i = 0; i < selectRows.length; i++) {
            vos[i] = new HashVO();
            for (int j = 0; j < tableheader.length; j++) {
                vos[i].setAttributeValue(tableheader[j], cardorderTable.getModel().getValueAt(selectRows[i], j));
            }
        }
        this.dispose();
    }

    protected void onCardExit() {
        this.dispose();
    }

    public void setCompentObjectValue(String _key, Object _obj) {
        INovaCompent compent = getCompentByKey(_key);
        compent.setObject(_obj);
    }

    public INovaCompent getCompentByKey(String _key) {
        INovaCompent[] compents = (INovaCompent[]) v_compents.toArray(new INovaCompent[0]);
        for (int i = 0; i < compents.length; i++) {
            if (compents[i].getKey().equalsIgnoreCase(_key)) {
                return compents[i];
            }
        }
        return null;
    }

    public HashMap getChirldren(int _chirld) {
        ArrayList chirldlist = new ArrayList();
        chirldlist.add(this.getCompentRealValue(querytempletItemVOs[_chirld].getItemKey() + "START"));
        chirldlist.add(this.getCompentRealValue(querytempletItemVOs[_chirld].getItemKey() + "END"));
        Object chirld = new Integer(_chirld);
        map.put(chirld, chirldlist);
        return map;
    }

    public String getCompentRealValue(String _key) {
        INovaCompent[] compents = (INovaCompent[]) v_compents.toArray(new INovaCompent[0]);
        for (int i = 0; i < compents.length; i++) {
            if (compents[i].getKey().equalsIgnoreCase(_key)) {
                return String.valueOf(compents[i].getObject());
            }
        }
        return "";
    }

    public ArrayList findIndexMap(Object _ob) {
        ArrayList index = new ArrayList();
        if (map.get(_ob) instanceof ArrayList) {
            index.add( ( (ArrayList) (map.get(_ob))).get(0).toString());
            index.add( ( (ArrayList) (map.get(_ob))).get(1).toString());
        }
        return index;
    }

    public void setChildrenQuery(int _chirld) {
        Object chirld = new Integer(_chirld);
        if (isFirst == true) {
            if ( (findIndexMap(chirld).get(0) != null && findIndexMap(chirld).get(0).toString().length() > 0) ||
                (findIndexMap(chirld).get(1) != null && findIndexMap(chirld).get(1).toString().length() > 0)) {
                if ( (findIndexMap(chirld).get(0) != null && findIndexMap(chirld).get(0).toString().length() > 0) &&
                    (findIndexMap(chirld).get(1) == null || findIndexMap(chirld).get(1).toString().length() <= 0)) {
                    if (querytempletItemVOs[_chirld].getItemType().equals("日历") ||
                        querytempletItemVOs[_chirld].getItemType().equals("时间")) {
                        sbcard.append(" " + querytempletItemVOs[_chirld].getItemKey() + " >= TO_DATE('" +
                                      findIndexMap(chirld).get(0).toString() + "','YYYY-MM-DD HH24:MI:SS') ");
                        isFirst = false;
                    } else {
                        sbcard.append(" " + querytempletItemVOs[_chirld].getItemKey() + " >= '" +
                                      findIndexMap(chirld).get(0).toString() + "' ");
                        isFirst = false;
                    }
                } else if ( (findIndexMap(chirld).get(0) == null ||
                             findIndexMap(chirld).get(0).toString().length() <= 0) &&
                           (findIndexMap(chirld).get(1) != null && findIndexMap(chirld).get(1).toString().length() >= 0)) {
                    if (querytempletItemVOs[_chirld].getItemType().equals("日历") ||
                        querytempletItemVOs[_chirld].getItemType().equals("时间")) {
                        sbcard.append(" " + querytempletItemVOs[_chirld].getItemKey() + " <= TO_DATE('" +
                                      findIndexMap(chirld).get(1).toString() + "','YYYY-MM-DD HH24:MI:SS') ");
                        isFirst = false;
                    } else {
                        sbcard.append(" " + querytempletItemVOs[_chirld].getItemKey() + " <= '" +
                                      findIndexMap(chirld).get(1).toString() + "' ");
                        isFirst = false;
                    }
                } else {
                    if (querytempletItemVOs[_chirld].getItemType().equals("日历") ||
                        querytempletItemVOs[_chirld].getItemType().equals("时间")) {
                        sbcard.append(" " + querytempletItemVOs[_chirld].getItemKey() + " BETWEEN TO_DATE('" +
                                      findIndexMap(chirld).get(0).toString() +
                                      "','YYYY-MM-DD HH24:MI:SS') AND TO_DATE('" + findIndexMap(chirld).get(1).toString() +
                                      "','YYYY-MM-DD HH24:MI:SS') ");
                        isFirst = false;
                    } else {
                        sbcard.append(" " + querytempletItemVOs[_chirld].getItemKey() + " BETWEEN '" +
                                      findIndexMap(chirld).get(0).toString() + "' AND '" +
                                      findIndexMap(chirld).get(1).toString() + "' ");
                        isFirst = false;
                    }
                }
            }
        } else {
            if ( (findIndexMap(chirld).get(0) != null && findIndexMap(chirld).get(0).toString().length() > 0) ||
                (findIndexMap(chirld).get(1) != null && findIndexMap(chirld).get(1).toString().length() > 0)) {
                if ( (findIndexMap(chirld).get(0) != null && findIndexMap(chirld).get(0).toString().length() > 0) &&
                    (findIndexMap(chirld).get(1) == null || findIndexMap(chirld).get(1).toString().length() <= 0)) {
                    if (querytempletItemVOs[_chirld].getItemType().equals("日历") ||
                        querytempletItemVOs[_chirld].getItemType().equals("时间")) {
                        sbcard.append(" AND " + querytempletItemVOs[_chirld].getItemKey() + " >= TO_DATE('" +
                                      findIndexMap(chirld).get(0).toString() + "','YYYY-MM-DD HH24:MI:SS') ");
                    } else {
                        sbcard.append(" AND " + querytempletItemVOs[_chirld].getItemKey() + " >= '" +
                                      findIndexMap(chirld).get(0).toString() + "' ");
                    }
                } else if ( (findIndexMap(chirld).get(0) == null ||
                             findIndexMap(chirld).get(0).toString().length() <= 0) &&
                           (findIndexMap(chirld).get(1) != null && findIndexMap(chirld).get(1).toString().length() >= 0)) {
                    if (querytempletItemVOs[_chirld].getItemType().equals("日历") ||
                        querytempletItemVOs[_chirld].getItemType().equals("时间")) {
                        sbcard.append(" AND " + querytempletItemVOs[_chirld].getItemKey() + " <= TO_DATE('" +
                                      findIndexMap(chirld).get(1).toString() + "','YYYY-MM-DD HH24:MI:SS') ");
                        isFirst = false;
                    } else {
                        sbcard.append(" AND " + querytempletItemVOs[_chirld].getItemKey() + " <= '" +
                                      findIndexMap(chirld).get(1).toString() + "' ");
                    }
                } else {
                    if (querytempletItemVOs[_chirld].getItemType().equals("日历") ||
                        querytempletItemVOs[_chirld].getItemType().equals("时间")) {
                        sbcard.append(" AND " + querytempletItemVOs[_chirld].getItemKey() + " BETWEEN TO_DATE('" +
                                      findIndexMap(chirld).get(0).toString() +
                                      "','YYYY-MM-DD HH24:MI:SS') AND TO_DATE('" + findIndexMap(chirld).get(1).toString() +
                                      "','YYYY-MM-DD HH24:MI:SS') ");
                        isFirst = false;
                    } else {
                        sbcard.append(" AND " + querytempletItemVOs[_chirld].getItemKey() + " BETWEEN '" +
                                      findIndexMap(chirld).get(0).toString() + "' AND '" +
                                      findIndexMap(chirld).get(1).toString() + "' ");
                    }
                }
            }
        }
    }

    public void initcard() {
        maplist = new HashMap();
        for (int i = 0; i < querytempletItemVOs.length; i++) {
            if (querytempletItemVOs[i].getItemType().equals("时间")) {
                maplist.putAll(getChirldren(i));
            } else if (querytempletItemVOs[i].getItemType().equals("日历")) {
                maplist.putAll(getChirldren(i));
            } else if (querytempletItemVOs[i].getItemType().equals("数字框")) {
                maplist.putAll(getChirldren(i));
            } else {
                Object inob = new Integer(i);
                maplist.put(inob, this.getCompentRealValue(querytempletItemVOs[i].getItemKey()));
            }
        }
    }

    public String getQueryCondition() {
        initcard();
        sbcard = new StringBuffer(); //
        for (int i = 0; i < querytempletItemVOs.length; i++) {
            if (!querytempletItemVOs[i].getItemType().equals("时间") && !querytempletItemVOs[i].getItemType().equals("日历") &&
                !querytempletItemVOs[i].getItemType().equals("数字框") &&
                !querytempletItemVOs[i].getItemType().equals("下拉框") &&
                !querytempletItemVOs[i].getItemType().equals("参照")
                && !querytempletItemVOs[i].getItemType().equals("勾选框")) {
                if (this.getCompentRealValue(querytempletItemVOs[i].getItemKey()) != null &&
                    this.getCompentRealValue(querytempletItemVOs[i].getItemKey()).length() > 0) {
                    Object inob = new Integer(i);
                    sbcard.append(" AND " + querytempletItemVOs[i].getItemKey() + " LIKE '%" + maplist.get(inob) +
                                  "%' ");
                }
            } else if (querytempletItemVOs[i].getItemType().equals("时间")) {
                setChildrenQuery(i);
            } else if (querytempletItemVOs[i].getItemType().equals("日历")) {
                setChildrenQuery(i);
            } else if (querytempletItemVOs[i].getItemType().equals("数字框")) {
                setChildrenQuery(i);
            } else if (querytempletItemVOs[i].getItemType().equals("下拉框") ||
                       querytempletItemVOs[i].getItemType().equals("参照") ||
                       querytempletItemVOs[i].getItemType().equals("勾选框")) {
                String str_realValue = this.getCompentRealValue(querytempletItemVOs[i].getItemKey());
                if (str_realValue != null && !str_realValue.trim().equals("")) {
                    Object inob = new Integer(i);
                    sbcard.append(" AND " + querytempletItemVOs[i].getItemKey() + " = '" + maplist.get(inob) + "' ");
                }
            }
        }
        return sbcard.toString();
    }

    /**
     * 得到查询结果.点击确定以后调用此函数可得到选中的行.
     *
     * @return HashVO[]
     */
    public HashVO[] getQueryResult() {
        return vos;
    }

    class TextFieldCellEditor extends DefaultCellEditor {

        private static final long serialVersionUID = 1L;

        JTextField textField = null;

        public TextFieldCellEditor(JTextField _textField) {
            super(_textField);
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
            int column) {
            textField = (JTextField)super.getTableCellEditorComponent(table, value, isSelected, row, column);
            return textField;
        }

        public Object getCellEditorValue() {
            return textField.getText();
        }

        public boolean isCellEditable(EventObject anEvent) {
            return false;
        }
    }
}
/**************************************************************************
 * $RCSfile: PubQueryTempletDialog.java,v $  $Revision: 1.2.8.2 $  $Date: 2009/02/02 16:12:54 $
 *
 * $Log: PubQueryTempletDialog.java,v $
 * Revision 1.2.8.2  2009/02/02 16:12:54  wangqi
 * *** empty log message ***
 *
 * Revision 1.2.8.1  2008/11/05 05:21:08  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2007/05/31 07:38:15  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:01:37  qilin
 * no message
 *
 * Revision 1.8  2007/03/07 02:01:55  shxch
 * *** empty log message ***
 *
 * Revision 1.7  2007/03/02 05:28:06  shxch
 * *** empty log message ***
 *
 * Revision 1.6  2007/03/02 05:02:49  shxch
 * *** empty log message ***
 *
 * Revision 1.5  2007/02/27 06:03:01  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/02/10 08:59:51  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/02/10 08:51:58  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:56:14  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
