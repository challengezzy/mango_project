/**************************************************************************
 * $RCSfile: TempletModify.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:16 $
 **************************************************************************/
package smartx.framework.metadata.ui;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import smartx.framework.common.ui.*;
import smartx.framework.metadata.vo.*;


public class TempletModify extends JFrame {

    private static final long serialVersionUID = 7724529650589535010L;

    private String templetname;

    private TempletModifyBillCard billcard = null;

    private BillListPanel billlist = null;

    private Color color = new Color(240, 240, 240);

    JTable templet_attr_table = null; // 模板基本属性编辑的表

    DefaultTableColumnModel colmodel = null;

    String itemvo = null; // Card里被点击的那一个VO，由BillCardForTempletModify传入

    private Pub_Templet_1_ItemVO[] templetBasicitems = null; // 模板基本信息...表PUB_ITEM_1的结构.

    String[][] BackInfo_back = null;

    String[][] ItemInfo_back = null;

    JButton apply = new JButton("应用");

    JButton ok = new JButton("确定");

    JButton reset = new JButton("重置");

    Map basicinfo_itemname_itemkey = null; // 存放表的列名与显示中文名称的对应.

    JLabel status = null;

    TempletModifyVerticalBillCard verticalcard = null;

    JSplitPane jsp = null;

    HashMap updatesqls = new HashMap();

    HashMap clickeditemvo = new HashMap();

    public TempletModify(String templetname) {
        this.templetname = templetname;
        this.setSize(800, 600);
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(getMainPane(), BorderLayout.CENTER);
        this.getContentPane().add(getSouthPanel(), BorderLayout.SOUTH);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setTitle("[" + templetname + "]模板编辑");
        this.setVisible(true);
    }

    private JPanel getMainPane() {
        JPanel rpanel = new JPanel();
        rpanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        jsp = new JSplitPane();
        jsp.setDividerSize(8);
        jsp.setDividerLocation(725);
        jsp.setOneTouchExpandable(true);
        jsp.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        jsp.setLeftComponent(getLeftScrollPane());
        jsp.setRightComponent(getRigthPanel());
        jsp.revalidate();
        rpanel.setLayout(new BorderLayout());
        rpanel.add(jsp);
        return rpanel;
    }

    private JPanel getRigthPanel() {
        JPanel rpanel = new JPanel();
        rpanel.setLayout(new BorderLayout());

        verticalcard = new TempletModifyVerticalBillCard(this, "PUB_TEMPLET_1_ITEM");
        final JTabbedPane tabp = new JTabbedPane();
        tabp.add("显示属性", verticalcard); // getItemPanel());//getItemAttributeModifyP());
        tabp.add("基本属性", getBasicAttributeP());
        tabp.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                if (tabp.getSelectedIndex() == 1) {
                    reset.setEnabled(true);
                    apply.setEnabled(true);
                    setStatus("编辑模板基本属性");
                } else {
                    apply.setEnabled(false);
                    reset.setEnabled(false);
                    setStatus("编辑模板单项属性");
                }
            }

        });
        JPanel btnpanel = new JPanel();
        btnpanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        apply.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reset.setEnabled(false);
                if (tabp.getSelectedIndex() == 1) {
                    applyBasicInfo(true);
                } else {
                    applyItemInfo();
                }
            }
        });
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reset.setEnabled(false);
                if (tabp.getSelectedIndex() == 1) {
                    templet_attr_table.editingStopped(new ChangeEvent(templet_attr_table));
                }
                updatesqls.put("basicinfo", applyBasicInfo(false));
                updateAll();
            }
        });
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (tabp.getSelectedIndex() == 1) {
                    resetBasicInfo();
                } else {
                    resetItemInfo();
                }
            }
        });
        btnpanel.add(ok);
        btnpanel.add(apply);
        btnpanel.add(reset);
        rpanel.add(tabp, BorderLayout.CENTER);
        rpanel.add(btnpanel, BorderLayout.SOUTH);
        return rpanel;
    }

    private String applyBasicInfo(boolean updatenow) { // 更改基本属性
        templet_attr_table.editingStopped(new ChangeEvent(templet_attr_table));
        if (basicinfo_itemname_itemkey != null) {
            String sql = "update pub_templet_1 set ";
            for (int i = 0; i < templet_attr_table.getModel().getRowCount(); i++) {
                sql += basicinfo_itemname_itemkey.get(templet_attr_table.getValueAt(i, 0)) + "='" +
                    templet_attr_table.getValueAt(i, 1) + "',";
            }
            sql = sql.substring(0, sql.length() - 1);
            sql += " where PK_PUB_TEMPLET_1 =" + BackInfo_back[0][1];

            // 更新备份信息
            for (int i = 1; i < BackInfo_back.length; i++) {
                for (int j = 0; j < 2; j++) {
                    BackInfo_back[i][j] = ( (String) templet_attr_table.getValueAt(i - 1, j)).trim();
                }
            }
            ( (TempletBasicInfo_TableModel) templet_attr_table.getModel()).setData(backUp(BackInfo_back));

            if (!updatenow) {
                return sql;
            }
            try {
                UIUtil.executeUpdateByDS(null, sql);
            } catch (Exception e) {
                setStatus("更新失败");
                e.printStackTrace();
            }
            setStatus("更新成功");
        }
        return "";
    }

    private JSplitPane getLeftScrollPane() {
        JSplitPane leftjsp = new JSplitPane();
        leftjsp.setDividerSize(8);
        leftjsp.setOneTouchExpandable(true);
        leftjsp.setDividerLocation(400);
        leftjsp.setOrientation(JSplitPane.VERTICAL_SPLIT);
        billcard = getBillCard();
        JScrollPane jsp = new JScrollPane(billcard);
        billlist = new BillListPanel(templetname);
        System.gc();
        billlist.hidePopMenu();
        billlist.addEmptyRow();
        billlist.addEmptyRow();
        leftjsp.setTopComponent(jsp);
        leftjsp.setBottomComponent(billlist);
        leftjsp.revalidate();
        return leftjsp;
    }

    private void applyItemInfo() {
        if (updatesqls.containsKey(itemvo)) {
            updatesqls.remove(itemvo);
        }
        if (updatesqls.size() == 0) {
            ok.setEnabled(false);
        }
        verticalcard.saveData();
        if (clickeditemvo.size() != 0 && clickeditemvo.containsKey(itemvo)) {
            clickeditemvo.remove(itemvo);
        }
        JOptionPane.showMessageDialog(this, "更改成功");
        setStatus("更新成功");
        refreshView();
        apply.setEnabled(false);
        System.gc();
    }

    private void refreshView() {
        if (jsp.getLeftComponent() != null) {
            jsp.remove(jsp.getLeftComponent());
        }
        jsp.setLeftComponent(getLeftScrollPane());
        jsp.setDividerLocation(725);
    }

    private void updateAll() {
        if (!updatesqls.containsKey(itemvo)) {
            saveChange();
        }
        try {
            UIUtil.executeBatchByDS(null, new ArrayList(updatesqls.values())); //
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "更新失败[" + e.getMessage() + "]");
        }
        if (clickeditemvo.size() != 0) { //删除所有保存的ITEMVO备份信息
            clickeditemvo.clear();
        }
        JOptionPane.showMessageDialog(this, "更新成功");
        updatesqls.clear();
        ok.setEnabled(false);
        apply.setEnabled(false);
        refreshView();
    }

    public void resetItemInfo() {
        //			
    }

    public void refreshItemTable(String item) { // 刷新，显示选中组件的属性
        apply.setEnabled(true);
        setStatus("编辑 " + item);

        if (clickeditemvo.containsKey(item)) { //如果已经保存过编辑的ITEM则恢复之

            verticalcard.setValue( (HashMap) clickeditemvo.get(item));
            clickeditemvo.remove(item);
        } else { //如果没有保存过点击的ITEM，刚保存
            verticalcard.refreshData("PK_PUB_TEMPLET_1=(select PK_PUB_TEMPLET_1 from PUB_TEMPLET_1 where TEMPLETCODE='" +
                                     billcard.getTempletVO().getTempletcode() + "') and ITEMKEY='" + item + "'");
        }
        //		上次点击的控件类型会变为新点击的控件的类型.要强制把上次点击的控件赋值为原来的类型.
        if (itemvo != null) {
            billcard.getItemVO(itemvo).setItemtype(pretype);
            billcard.refresh(item);
        }
        itemvo = item;
        pretype = billcard.getItemVO(item).getItemtype();
        reset.setEnabled(false);
    }

    public TempletModifyBillCard getBillCard() {
        if (billcard == null) {
            billcard = new TempletModifyBillCard(this, templetname);
        }
        return billcard;
    }

    private JPanel getBasicAttributeP() { // 模板基本属性
        JPanel rpanel = new JPanel();
        rpanel.setLayout(new BorderLayout());
        String sql = "select * from pub_templet_1 where TEMPLETCODE='" + templetname + "'";
        TableDataStruct attrdata;
        try {
            attrdata = UIUtil.getTableDataStructByDS(null, sql);
            templet_attr_table = getTempletBasicInfoTable(attrdata);
            JScrollPane jsp = new JScrollPane(templet_attr_table);
            rpanel.add(jsp, BorderLayout.CENTER);
            return rpanel;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public void resetBasicInfo() {
        if (BackInfo_back != null) {
            reset.setEnabled(false);
            ( (TempletBasicInfo_TableModel) templet_attr_table.getModel()).setData(backUp(BackInfo_back));
            setStatus("重置成功");
            templet_attr_table.updateUI();
        }
    }

    private JTable getTempletBasicInfoTable(TableDataStruct data) { // 模板基本信息的编辑列表
        try {
            templetBasicitems = UIUtil.getPub_Templet_1VO("PUB_TEMPLET_1").getItemVos();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 表的数据MAP,以备以后修改用......
        basicinfo_itemname_itemkey = new HashMap();
        String[][] tdata = new String[data.getTable_header().length][2];
        for (int i = 0; i < data.getTable_header().length; i++) {
            for (int j = 0; j < templetBasicitems.length; j++) {
                if (templetBasicitems[j].getItemkey().equals(data.getTable_header()[i])) {
                    basicinfo_itemname_itemkey.put(templetBasicitems[j].getItemname(), templetBasicitems[j].getItemkey());
                    tdata[i][0] = templetBasicitems[j].getItemname();
                    tdata[i][1] = data.getTable_body()[0][i];
                }

            }
        }
        // 备份，以后重置时使用.
        BackInfo_back = backUp(tdata);
        TempletBasicInfo_TableModel basicinfomodel = new TempletBasicInfo_TableModel(tdata);
        JTable table = new JTable(basicinfomodel, getColumnModel()); // ,getColumnModel());//);model
        table.setRowHeight(20);
        table.setRowMargin(5);
        return table;
    }

    private String[][] backUp(String[][] source) {

        if (source.length == 0) {
            return null;
        }
        String[][] target = new String[source.length][2];
        for (int i = 0; i < source.length; i++) {
            for (int j = 0; j < 2; j++) {
                if (source[i][j] != null) {
                    target[i][j] = source[i][j].trim();
                }
            }
        }
        return target;
    }

    private DefaultTableColumnModel getColumnModel() {
        if (colmodel != null) {
            return colmodel;
        }
        colmodel = new DefaultTableColumnModel();
        TableColumn attr = new TableColumn(0);
        attr.setHeaderValue("属性");
        attr.setIdentifier("属性");
        attr.setPreferredWidth(70);
        TableColumn value = new TableColumn(1);
        value.setHeaderValue("值");
        value.setIdentifier("值");
        value.setPreferredWidth(70);
        colmodel.addColumn(attr);
        colmodel.addColumn(value);
        return colmodel;
    }

    // //模板基本属性的 TableModel
    private class TempletBasicInfo_TableModel extends DefaultTableModel {
        private static final long serialVersionUID = 6963305258951977865L;

        private String[][] data = null;

        public TempletBasicInfo_TableModel(String[][] _data) {
            this.data = _data;

        }

        public void setData(String[][] _data) {
            this.data = _data;
        }

        public int getColumnCount() {
            return 2;
        }

        public boolean isCellEditable(int r, int c) {
            if (c == 0) {
                return false;
            }
            return true;
        }

        public int getRowCount() {
            if (this.data == null) {
                return 0;
            }
            return this.data.length - 1; // 去掉ID 显示行
        }

        public Object getValueAt(int r, int c) {
            if (this.data == null) {
                return "";
            }
            return this.data[r + 1][c];

        }

        public void setValueAt(Object value, int r, int c) {
            this.data[r + 1][c] = (String) value;
            reset.setEnabled(true);
            fireTableDataChanged();
        }

    }

    private JPanel getSouthPanel() { // 状态栏
        JPanel rpanel = new JPanel();
        rpanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JPanel userp = new JPanel();
        userp.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JLabel info = new JLabel("状态：", JLabel.LEFT);
        status = new JLabel();
        JLabel user = new JLabel("当前用户：" + NovaClientEnvironment.getInstance().get("SYS_LOGINUSER_NAME"), JLabel.RIGHT);

        rpanel.add(info);
        userp.add(user);
        JPanel repanel = new JPanel();
        repanel.setLayout(new BorderLayout());
        repanel.setBackground(color);
        repanel.add(rpanel, BorderLayout.WEST);
        repanel.add(status, BorderLayout.CENTER);
        repanel.add(userp, BorderLayout.EAST);
        return repanel;
    }

    public String getSelectedVO() {
        return this.itemvo;
    }

    public void setStatus(String _info) {
        status.setText(_info);
    }

    public void setName(String name) {
        this.billcard.getCompentByKey(itemvo).getLabel().setText(name);
    }

    public void refreshCard(Pub_Templet_1VO templetVO) {
        billcard.refresh(templetVO, itemvo);
    }

    public void saveChange() {
        if (itemvo == null || itemvo.equals("")) {
            return;
        }
        String sql = verticalcard.getUpdateSQL();

        //		先保存点击前的那个ITEMVO的编辑后的信息
        if (!itemvo.equals("")) {
            clickeditemvo.put(itemvo, verticalcard.getAllObjectValuesWithHashMap());
        }
        addUpdateSql(itemvo, sql);
    }

    public void addUpdateSql(String itemkey, String sql) {
        if (updatesqls.containsKey(itemkey)) {
            updatesqls.remove(itemkey);
        }
        updatesqls.put(itemkey, sql);
    }

    public void setOKButtonEnable(boolean _enable) {
        this.ok.setEnabled(_enable);
    }

    public void setAPPLYButtonEnable(boolean _enable) {
        this.apply.setEnabled(_enable);
    }

    public HashMap getPubItemVOValueFromVertical() {
        return verticalcard.getValueAsItemVO();
    }

    private String pretype = "";
}
/**************************************************************************
 * $RCSfile: TempletModify.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:16 $
 *
 * $Log: TempletModify.java,v $
 * Revision 1.2  2007/05/31 07:38:16  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:01:36  qilin
 * no message
 *
 * Revision 1.6  2007/03/02 05:28:06  shxch
 * *** empty log message ***
 *
 * Revision 1.5  2007/03/02 05:16:42  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/03/02 05:02:48  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/03/01 09:06:56  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:56:13  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
