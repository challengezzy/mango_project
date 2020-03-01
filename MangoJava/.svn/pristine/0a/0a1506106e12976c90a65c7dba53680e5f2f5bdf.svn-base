/**************************************************************************
 * $RCSfile: TempletModify2.java,v $  $Revision: 1.4.8.2 $  $Date: 2009/06/12 05:25:15 $
 **************************************************************************/
package smartx.framework.metadata.ui;

import java.io.*;
import java.util.*;
import java.util.List;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import smartx.framework.common.ui.*;
import smartx.framework.common.vo.*;
import smartx.framework.metadata.vo.*;
import smartx.framework.metadata.vo.templetvo.*;



/**
 * 元原模板第二种编辑模式
 * @author user
 *
 */
public class TempletModify2 extends NovaDialog {
    private static final long serialVersionUID = 1L;

    String str_templetcode = null;

    BillCardPanel cardPanel = null;

    BillListPanel listPanel = null;

    private JButton[] jbt_operator = null;

    public TempletModify2(Container _parent, String _templetcode) {
        super(_parent, "模板配置2", 1024, 740);
        str_templetcode = _templetcode;
        initialize();
        this.setVisible(true);
    }

    private void initialize() {
        this.getContentPane().setLayout(new BorderLayout()); //

        jbt_operator = new JButton[13];

        cardPanel = new BillCardPanel(new PUB_TEMPLET_1_VO());
        listPanel = new BillListPanel(new PUB_TEMPLET_1_ITEM_VO(), true, false);

        listPanel.setCustomerNavigationJPanel(getChildCustomerJPanel());
        listPanel.initialize();

        cardPanel.updateCurrRow(); //
        cardPanel.setEditable("PK_PUB_TEMPLET_1", false); //
        cardPanel.setEditable("TEMPLETCODE", false); //

        JPanel jpn_card = new JPanel(new BorderLayout());
        jpn_card.add(cardPanel, BorderLayout.CENTER);
        jpn_card.add(getOutPanel(), BorderLayout.SOUTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Pub_Templet_1_Item", listPanel); //

        JSplitPane splitPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, jpn_card, tabbedPane);
        splitPanel.setDividerSize(10);
        splitPanel.setDividerLocation(250);
        splitPanel.setOneTouchExpandable(true);

        this.getContentPane().add(splitPanel, BorderLayout.CENTER);
        this.getContentPane().add(getSouthPanel(), BorderLayout.SOUTH);

        onRefresh(); //刷新数据
    }

    /**
     * 获取到处数据的按钮面板
     *
     * @return
     */
    private JPanel getOutPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));

        jbt_operator[0] = getOperatorBtn("导出数据", new Dimension(100, 20), "");
        jbt_operator[1] = getOperatorBtn("导出数据表", new Dimension(100, 20), "");
        jbt_operator[2] = getOperatorBtn("导出存储表", new Dimension(100, 20), "");
        panel.add(jbt_operator[0]);
        panel.add(jbt_operator[1]);
        panel.add(jbt_operator[2]);

        return panel;
    }

    /**
     * 获得中间的BillList操作按钮面板
     *
     * @return
     */
    private JPanel getChildCustomerJPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        jbt_operator[3] = getOperatorBtn(UIUtil.getImage("images/platform/insert.gif"), new Dimension(18, 18), "新增记录");
        jbt_operator[4] = getOperatorBtn(UIUtil.getImage("images/platform/delete.gif"), new Dimension(18, 18), "删除记录");
        jbt_operator[5] = getOperatorBtn(UIUtil.getImage("images/platform/refresh.gif"), new Dimension(18, 18), "刷新");
        panel.add(jbt_operator[3]);
        panel.add(jbt_operator[4]);
        panel.add(jbt_operator[5]);

        return panel;
    }

    private void onChildInsert() {
        int li_row = listPanel.newRow();
        listPanel.setValueAt(cardPanel.getRealValueAt("PK_PUB_TEMPLET_1"), li_row, "PK_PUB_TEMPLET_1");

    }

    private void onChildDelete() {
        listPanel.removeRow();
    }

    public void onChildRefresh() {
        listPanel.QueryDataByCondition("PK_PUB_TEMPLET_1='" + cardPanel.getRealValueAt("PK_PUB_TEMPLET_1") + "'");
    }

    private JPanel getSouthPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); //

        jbt_operator[6] = getOperatorBtn("新增", new Dimension(75, 20), "");
        jbt_operator[7] = getOperatorBtn("删除", new Dimension(75, 20), "");
        jbt_operator[8] = getOperatorBtn("上移", new Dimension(75, 20), "");
        jbt_operator[9] = getOperatorBtn("下移", new Dimension(75, 20), "");
        jbt_operator[10] = getOperatorBtn("刷新", new Dimension(75, 20), "");
        jbt_operator[11] = getOperatorBtn("保存", new Dimension(75, 20), "");
        jbt_operator[12] = getOperatorBtn("取消", new Dimension(75, 20), "");
        panel.add(jbt_operator[6]);
        panel.add(jbt_operator[7]);
        panel.add(jbt_operator[8]);
        panel.add(jbt_operator[9]); // 上移
        panel.add(jbt_operator[10]); // 下移
        panel.add(jbt_operator[11]);
        panel.add(jbt_operator[12]); //

        return panel;
    }

    /**
     * 定制JButton
     *
     * @param _obj
     * @param _demension
     * @return
     */
    private JButton getOperatorBtn(Object _obj, Dimension _demension, String _tooltip) {
        JButton jbt_temp = null;
        if (_obj instanceof String) {
            jbt_temp = new JButton(_obj.toString());
        } else if (_obj instanceof Icon) {
            jbt_temp = new JButton( (Icon) _obj);
        }
        jbt_temp.setToolTipText(_tooltip);
        jbt_temp.setPreferredSize(_demension);
        jbt_temp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dealAcitonPerform(e);
            }
        });

        return jbt_temp;
    }

    /**
     * 集中处理所有的按钮事件
     *
     * @param e
     */
    private void dealAcitonPerform(ActionEvent e) {
        Object obj = e.getSource();
        if (obj.equals(jbt_operator[0])) {
            onOutSQL();
        } else if (obj.equals(jbt_operator[1])) {
            onOutSearchTable();
        } else if (obj.equals(jbt_operator[2])) {
            onOutSaveTable();
        } else if (obj.equals(jbt_operator[3])) { // 处理中间按钮，新增记录
            onChildInsert();
        } else if (obj.equals(jbt_operator[4])) { // 处理中间按钮，删除记录
            onChildDelete();
        } else if (obj.equals(jbt_operator[5])) { // 处理中间按钮，刷新表数据
            onChildRefresh();
        } else if (obj.equals(jbt_operator[6])) { // 处理下面按钮，新增记录
            onInsert();
        } else if (obj.equals(jbt_operator[7])) { // 处理下面按钮，删除记录
            onDelete();
        } else if (obj.equals(jbt_operator[8])) { // 处理下面按钮，上移
            onMoveup();
        } else if (obj.equals(jbt_operator[9])) { // 处理下面按钮，下移
            onMovedown();
        } else if (obj.equals(jbt_operator[10])) { // 处理下面按钮，刷新记录
            onRefresh();
        } else if (obj.equals(jbt_operator[11])) { // 处理下面按钮，保存记录
            onSave();
        } else if (obj.equals(jbt_operator[12])) { // 处理下面按钮，退出
            onExit();
        }
    }

    private void onOutSearchTable() {
        String str_value = cardPanel.getCompentRealValue("TABLENAME");
        if (str_value == null || str_value.equals("")) {
            JOptionPane.showMessageDialog(this, "查询表为空，无法导出查询表结构！");
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = chooser.showSaveDialog(this);

        if (result != 0) {
            return;
        }
        String str_path = chooser.getSelectedFile().getPath();
        str_path = str_path + "\\" + str_value + ".sql";

        String str_temp = null;
        String str_sql = null;
        String str_type = "Select Object_type From user_objects Where OBJECT_NAME = '" + str_value + "'";
        String[][] str_result = null;
        try {
            str_result = UIUtil.getStringArrayByDS(null, str_type);
        } catch (NovaRemoteException e1) {
            e1.printStackTrace();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        if (str_result == null || str_result.length == 0) {
            JOptionPane.showMessageDialog(this, "数据库中没有关于查询表" + str_value + "的信息，请核对表名的正确性！");
            return;
        } else if (str_result[0][0].equalsIgnoreCase("TABLE")) {
            str_temp = "表";
            str_sql =
                "SELECT t.table_name,dbms_metadata.get_ddl('TABLE',t.table_name,USER) text FROM user_tables t where t.table_name = '" +
                str_value + "'";
        } else if (str_result[0][0].equalsIgnoreCase("VIEW")) {
            str_temp = "视图";
            str_sql = "Select text From user_views Where VIEW_NAME = '" + str_value + "'";
        } else {
            JOptionPane.showMessageDialog(this, "数据库中没有检索出以" + str_value + "命名的表或视图，请核对表名的正确性！");
            return;
        }
        HashVO[] hv_result = null;
        try {
            hv_result = UIUtil.getHashVoArrayByDS(null, str_sql);
        } catch (NovaRemoteException e1) {
            e1.printStackTrace();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        if (hv_result == null || hv_result.length == 0) {
            JOptionPane.showMessageDialog(this, "表或视图" + str_value + "的构建脚本为空！");
            return;
        }

        File out_file = new File(str_path);
        if (!out_file.exists()) {
            try {
                out_file.createNewFile();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "创建" + str_path + "出错！");
                e.printStackTrace();
                return;
            }
        }
        try {
            FileWriter resultFile = new FileWriter(out_file);
            PrintWriter myFile = new PrintWriter(resultFile);

            if (str_temp.equals("表")) {
                myFile.println("--表" + str_value + "的构建脚本如下");
                myFile.println("drop   sequence S_" + str_value + ";");
                myFile.println("create sequence S_" + str_value + ";");
                myFile.println("drop   table " + str_value + " cascade constraints; \r\n");
            } else {
                myFile.println("--视图" + str_value + "的构建脚本如下");
                myFile.println("Create Or Replace VIEW " + str_value + " AS ");
            }
            String str_text = hv_result[0].getStringValue("TEXT");
            myFile.println(str_text);
            resultFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(this, "导出查询表" + str_value + "的构建脚本成功！");
    }

    private void onOutSaveTable() {
        String str_value = cardPanel.getCompentRealValue("SAVEDTABLENAME");
        if (str_value == null || str_value.equals("")) {
            JOptionPane.showMessageDialog(this, "保存表为空，无法导出保存表结构！");
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = chooser.showSaveDialog(this);

        if (result != 0) {
            return;
        }
        String str_path = chooser.getSelectedFile().getPath();
        str_path = str_path + "\\" + str_value + ".sql";
        String str_sql =
            "SELECT t.table_name,dbms_metadata.get_ddl('TABLE',t.table_name,USER) text FROM user_tables t where t.table_name = '" +
            str_value + "'";
        HashVO[] hv_result = null;
        try {
            hv_result = UIUtil.getHashVoArrayByDS(null, str_sql);
        } catch (NovaRemoteException e1) {
            e1.printStackTrace();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        if (hv_result == null || hv_result.length == 0) {
            JOptionPane.showMessageDialog(this, "数据库中没有关于查询表" + str_value + "的信息，请核对表名的正确性！");
            return;
        }
        File out_file = new File(str_path);
        if (!out_file.exists()) {
            try {
                out_file.createNewFile();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "创建" + str_path + "出错！");
                e.printStackTrace();
                return;
            }
        }
        try {
            FileWriter resultFile = new FileWriter(out_file);
            PrintWriter myFile = new PrintWriter(resultFile);

            myFile.println("--表" + str_value + "的构建脚本如下");
            myFile.println("drop   sequence S_" + str_value + ";");
            myFile.println("create sequence S_" + str_value + ";");
            myFile.println("drop   table " + str_value + " cascade constraints; \r\n");

            String str_text = hv_result[0].getStringValue("TEXT");
            myFile.println(str_text);
            resultFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(this, "导出存储表" + str_value + "的构建脚本成功！");
    }

    /**
     * 导出该模板的构建SQL
     */
    private void onOutSQL() {
        String str_value = cardPanel.getCompentRealValue("TEMPLETCODE");
        if (str_value == null || str_value.equals("")) {
            JOptionPane.showMessageDialog(this, "模板编码为空，无法导出模板初始数据！");
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = chooser.showSaveDialog(this);

        if (result != 0) {
            return;
        }
        String str_path = chooser.getSelectedFile().getPath();

        str_path = str_path + "\\" + str_value + ".sql";
        TempletExport te=new TempletExport();
        if(!te.exportTemplet(this, str_path, str_value)){
        	JOptionPane.showMessageDialog(this, "导出脚本【"+str_path+"】发生错误！");
        	return;
        }
        JOptionPane.showMessageDialog(this, "导出模板" + str_value + "的构建SQL成功！");
    }

    

    private void onInsert() {
        int li_row = listPanel.newRow(); //新增一行
        String str_parentid = cardPanel.getRealValueAt("PK_PUB_TEMPLET_1");
        listPanel.setValueAt(str_parentid, li_row, "PK_PUB_TEMPLET_1"); //

        //		listPanel.setValueAt(str_parentid, li_row, "PK_PUB_TEMPLET_1"); //

        listPanel.setValueAt(new ComBoxItemVO("文本框", "文本框", "文本框"), li_row, "ITEMTYPE"); //

        listPanel.setValueAt("Y", li_row, "CARDISSHOWABLE"); //
        listPanel.setValueAt(new ComBoxItemVO("1", "001", "全部可编辑"), li_row, "CARDISEDITABLE"); //

        listPanel.setValueAt("Y", li_row, "LISTISSHOWABLE"); //
        listPanel.setValueAt(new ComBoxItemVO("1", "001", "全部可编辑"), li_row, "LISTISEDITABLE"); //

        listPanel.setValueAt("150", li_row, "CARDWIDTH"); //
        listPanel.setValueAt("125", li_row, "LISTWIDTH"); //

        listPanel.setValueAt("" + (li_row + 1), li_row, "SHOWORDER"); //

        listPanel.setValueAt("N", li_row, "ISSAVE"); //
        listPanel.setValueAt("N", li_row, "ISMUSTINPUT"); //
        listPanel.setValueAt(new ComBoxItemVO("2", "002", "通用查询"), li_row, "ISDEFAULTQUERY"); //

        listPanel.getTable().getCellEditor(li_row, 0).cancelCellEditing();
        listPanel.getTable().editCellAt(li_row, 0); //
        JTextField textField = ( (JTextField) ( (DefaultCellEditor) listPanel.getTable().getCellEditor(li_row, 0)).
                                getComponent());
        textField.requestFocus();
    }

    private void onDelete() {
        listPanel.removeRow(); //
    }

    private void onSave() {
        stopEditing();
        String str_sql_1 = cardPanel.getUpdateSQL();
        String[] str_sqls_2 = listPanel.getOperatorSQLs();

        ArrayList list = new ArrayList();
        list.add(str_sql_1);
        list.addAll(Arrays.asList(str_sqls_2)); //

        //printArray(list);
        try {
            UIUtil.executeBatchByDS(null, list); //
            listPanel.setAllRowStatusAs("INIT");
            JOptionPane.showMessageDialog(this, "保存数据成功!!!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "对不起,保存数据失败!!" + e.getMessage());
        }

    }

    private void onMoveup() {
        if (listPanel.moveUpRow()) {
            resetShowOrder();
        }
    }

    private void onMovedown() {
        if (listPanel.moveDownRow()) {
            resetShowOrder(); //
        }
    }

    private void resetShowOrder() {
        int li_rowcount = listPanel.getRowCount();
        for (int i = 0; i < li_rowcount; i++) {
            if (listPanel.getValueAt(i, "SHOWORDER") != null &&
                Integer.parseInt("" + listPanel.getValueAt(i, "SHOWORDER")) != (i + 1)) {
                listPanel.setValueAt("" + (i + 1), i, "SHOWORDER"); //
            }
        }
    }

    private void stopEditing() {
        try {
            if (listPanel.getTable().getCellEditor() != null) {
                listPanel.getTable().getCellEditor().stopCellEditing(); //
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void onRefresh() {
        stopEditing(); //
        cardPanel.refreshData("TEMPLETCODE='" + str_templetcode + "'");
        listPanel.QueryDataByCondition(
            "PK_PUB_TEMPLET_1 = (select PK_PUB_TEMPLET_1 from PUB_TEMPLET_1 where TEMPLETCODE='" + str_templetcode +
            "') order by showorder asc");
    }

    private void onExit() {
        this.dispose(); //
    }

    private void printArray(Object[] _array) {
        for (int i = 0; i < _array.length; i++) {
            System.out.println(_array[i]);
        }
    }

    private void printArray(List _list) {
        Iterator iterator = _list.iterator();
        while (iterator.hasNext()) {
            Object obj = iterator.next();
            System.out.println(obj);
        }
    }

}
/**************************************************************************
 * $RCSfile: TempletModify2.java,v $  $Revision: 1.4.8.2 $  $Date: 2009/06/12 05:25:15 $
 *
 * $Log: TempletModify2.java,v $
 * Revision 1.4.8.2  2009/06/12 05:25:15  wangqi
 * *** empty log message ***
 *
 * Revision 1.4.8.1  2009/02/10 17:07:56  wangqi
 * 统一了模板编辑器和BillListPanel的模板编辑器的sql导出功能
 *
 * Revision 1.4  2007/05/31 07:38:16  qilin
 * code format
 *
 * Revision 1.3  2007/05/22 07:58:45  qilin
 * no message
 *
 * Revision 1.2  2007/05/22 02:03:20  sunxb
 * *** empty log message ***
 *
 * Revision 1.1  2007/05/17 06:01:36  qilin
 * no message
 *
 * Revision 1.12  2007/03/27 10:14:28  shxch
 * *** empty log message ***
 *
 * Revision 1.11  2007/03/15 07:02:00  shxch
 * *** empty log message ***
 *
 * Revision 1.10  2007/03/07 02:01:55  shxch
 * *** empty log message ***
 *
 * Revision 1.9  2007/03/02 05:28:05  shxch
 * *** empty log message ***
 *
 * Revision 1.8  2007/03/02 05:16:42  shxch
 * *** empty log message ***
 *
 * Revision 1.7  2007/03/02 05:02:49  shxch
 * *** empty log message ***
 *
 * Revision 1.6  2007/03/01 09:06:55  shxch
 * *** empty log message ***
 *
 * Revision 1.5  2007/02/27 06:03:00  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/02/10 08:51:58  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/01/31 08:02:36  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:56:14  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
