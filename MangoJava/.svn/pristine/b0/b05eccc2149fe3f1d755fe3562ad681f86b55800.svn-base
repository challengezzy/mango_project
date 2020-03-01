/**************************************************************************
 * $RCSfile: DeveloperTempleteConfig.java,v $  $Revision: 1.2.8.4 $  $Date: 2010/01/21 05:16:01 $
 **************************************************************************/
package smartx.framework.metadata.ui;

import java.io.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import org.apache.log4j.Logger;

import smartx.framework.common.ui.*;
import smartx.framework.common.vo.*;
import smartx.framework.metadata.vo.*;
import smartx.framework.metadata.vo.templetvo.*;


public class DeveloperTempleteConfig extends JFrame {
    private static final long serialVersionUID = -4655480510003085745L;
    private static Logger logger=NovaLogger.getLogger(DeveloperTempleteConfig.class);
    
    JTextField search_text = null;

    BillListPanel blp_main;

    BillListPanel blp_child;

    private String str_temp_code = "";

    private String str_tem_name = "";

    private String[][] str_tem_cols = null; //模板表的所有列

    private String[][] str_item_cols = null; //模板元素的所有列

    private JButton[] jbt_operator = null; //所有按钮

    private ActionListener listener = null; //所有按钮的Listener

    public DeveloperTempleteConfig() {
        this.setTitle("元原模板配置");
        this.setLocation(0, 0);
        this.setSize(1010, 700);
        //this.setExtendedState(MAXIMIZED_BOTH);
        initialize();
        //this.pack();        
    }

    private void initialize() {
        this.getContentPane().setLayout(new BorderLayout());

        listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dealAcitonPerform(e);
            }
        };
        jbt_operator = new JButton[14];

        JTabbedPane tabedPane = new JTabbedPane();
        tabedPane.addTab("Pub_Templet_1_Item", getChildPanel()); //
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, getMainPanel(), tabedPane);
        splitPane.setDividerLocation(300);
        splitPane.setDividerSize(10);
        splitPane.setOneTouchExpandable(true);

        this.getContentPane().add(getNorthPanel(), BorderLayout.NORTH);
        this.getContentPane().add(splitPane, BorderLayout.CENTER);
        this.getContentPane().add(getSouthPanel(), BorderLayout.SOUTH);
    }

    private BillListPanel getMainPanel() {
        if (blp_main == null) {
            //blp_main = new BillListPanel("PUB_TEMPLET_1"); //
            blp_main = new BillListPanel(new PUB_TEMPLET_1_VO()); //
            //	String str_sql = blp_main.getSQL("1=1 order by TEMPLETCODE asc");
            //blp_main.QueryData(str_sql);
            blp_main.getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting()) {
                        onRowSelectChanged();
                    }
                }
            });
        }
        return blp_main;
    }

    private BillListPanel getChildPanel() {
        if (blp_child == null) {
            //blp_child = new BillListPanel("PUB_TEMPLET_1_ITEM", true, false); //
            blp_child = new BillListPanel(new PUB_TEMPLET_1_ITEM_VO(), false, false); //
            //blp_child.setCustomerNavigationJPanel(getChildCustomerJPanel());
            blp_child.initialize();
        }
        return blp_child;
    }

    private JPanel getNorthPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JLabel label = new JLabel("模板编码/名称:", SwingConstants.RIGHT);
        search_text = new JTextField();
        search_text.setPreferredSize(new Dimension(150, 20));
        search_text.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    onSearch();
                }
            }
        });
        panel.add(label); //
        panel.add(search_text); //

        jbt_operator[0] = getBtn("查询", new Dimension(85, 20));
        jbt_operator[1] = getBtn("导入", new Dimension(85, 20));
        jbt_operator[2] = getBtn("导出SQL", new Dimension(85, 20));
        jbt_operator[3] = getBtn("导出VO", new Dimension(85, 20));
        jbt_operator[4] = getBtn("复制", new Dimension(85, 20));
        jbt_operator[5] = getBtn("删除", new Dimension(85, 20));

        panel.add(jbt_operator[0]);
        panel.add(jbt_operator[1]);
        panel.add(jbt_operator[2]);
        panel.add(jbt_operator[3]);
        panel.add(jbt_operator[4]);
        panel.add(jbt_operator[5]);

        return panel;
    }

    /**
     * 定制所有按钮
     *
     * @param _obj:StringORIcon
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
        jbt_temp.setPreferredSize(_demension);
        jbt_temp.addActionListener(listener);
        return jbt_temp;
    }

    /**
     * 处理所有按钮事件
     * @param e
     */
    protected void dealAcitonPerform(ActionEvent e) {
        Object obj = e.getSource();
        if (obj.equals(jbt_operator[0])) { //处理查询
            onSearch();
        } else if (obj.equals(jbt_operator[1])) { //处理导入
            onReference();
        } else if (obj.equals(jbt_operator[2])) { //处理导出SQL
            onBtnOut();
        } else if (obj.equals(jbt_operator[3])) { //处理导出VO
            onBtnOutVO();
        } else if (obj.equals(jbt_operator[4])) { //处理复制
            onBtnCopy();
        } else if (obj.equals(jbt_operator[5])) { //处理删除
            onDelete(); //
        } else if (obj.equals(jbt_operator[6])) { //子表新增
            onChildInsert();
        } else if (obj.equals(jbt_operator[7])) { //子表删除
            onChildDelete();
        } else if (obj.equals(jbt_operator[8])) { //子表刷新
            onChildRefresh();
        } else if (obj.equals(jbt_operator[9])) { //子表上移
            onChildUpRow();
        } else if (obj.equals(jbt_operator[10])) { //子表下移
            onChildDownRow();
        } else if (obj.equals(jbt_operator[11])) { //处理面板保存按钮
            onSave();
        }
    }

    private void onChildInsert() {

        if (blp_main.getTable().getSelectedRowCount() != 1) {
            NovaMessage.show("请选择一条父记录", NovaConstants.MESSAGE_WARN);
            return;
        }

        int li_row = blp_child.newRow();
        blp_child.setValueAt(blp_main.getRealValueAtModel(blp_main.getSelectedRow(), blp_main.getTempletVO().getPkname()),
                             li_row, blp_main.getTempletVO().getPkname());
    }

    private void onChildDelete() {
        blp_child.removeRow();
    }

    private void onChildUpRow() {
        blp_child.moveUpRow();
        int li_rowcount = blp_child.getRowCount();
        for (int i = 0; i < li_rowcount; i++) {
            if (blp_child.getValueAt(i, "SHOWORDER") != null &&
                Integer.parseInt("" + blp_child.getValueAt(i, "SHOWORDER")) != (i + 1)) {
                blp_child.setValueAt("" + (i + 1), i, "SHOWORDER"); //
            }
        }
    }

    private void onChildDownRow() {
        blp_child.moveDownRow();
        int li_rowcount = blp_child.getRowCount();
        for (int i = 0; i < li_rowcount; i++) {
            if (blp_child.getValueAt(i, "SHOWORDER") != null &&
                Integer.parseInt("" + blp_child.getValueAt(i, "SHOWORDER")) != (i + 1)) {
                blp_child.setValueAt("" + (i + 1), i, "SHOWORDER"); //
            }
        }
    }

    public void onChildRefresh() {
        if (blp_main.getTable().getSelectedRowCount() == 0) {
            blp_child.refreshCurrData();
        } else {
            blp_child.QueryDataByCondition(blp_main.getTempletVO().getPkname() + "='" +
                                           blp_main.getValueAt(blp_main.getSelectedRow(),
                blp_main.getTempletVO().getPkname()) + "'");
        }
    }

    private void getColumnsArray() {
        if (str_tem_cols == null) {
            String _sql_1 = "Select * From cols where TABLE_NAME='PUB_TEMPLET_1' Order by COLUMN_ID";
            try {
                str_tem_cols = UIUtil.getStringArrayByDS(null, _sql_1);
            } catch (NovaRemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (str_item_cols == null) {
            String _sql_item = "Select * From cols where TABLE_NAME='PUB_TEMPLET_1_ITEM' Order by COLUMN_ID";
            try {
                str_item_cols = UIUtil.getStringArrayByDS(null, _sql_item);
            } catch (NovaRemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void onBtnCopy() {
        int li_count = getMainPanel().getTable().getSelectedRowCount();
        if (li_count <= 0) {
            JOptionPane.showMessageDialog(this, "请先选择一条记录!");
            return;
        }
        getColumnsArray();
        int selected_rows = getMainPanel().getTable().getSelectedRow();
        String str_pk = (String) getMainPanel().getValueAt(selected_rows, "PK_PUB_TEMPLET_1"); // 原来记录的主键!!
        String str_code = (String) getMainPanel().getValueAt(selected_rows, "TEMPLETCODE"); // 原来模板名!!!

        ShowCopyTempleteDialog showCopyTempleteDialog = new ShowCopyTempleteDialog(this, str_code);
        showCopyTempleteDialog.setVisible(true);

        if (showCopyTempleteDialog.getCloseType() == 0) {
            this.str_temp_code = showCopyTempleteDialog.getTempleteCode(); // 新编码
            this.str_tem_name = showCopyTempleteDialog.getTempleteName(); // 新名称

            StringBuffer sb_1 = new StringBuffer();
            sb_1.append("insert into pub_templet_1 ");
            sb_1.append("( ");
            sb_1.append("pk_pub_templet_1, ");
            sb_1.append("templetcode, ");
            sb_1.append("templetname, ");
            sb_1.append("tablename, ");
            //sb_1.append("datasourcename, ");
            sb_1.append("pkname, ");
            sb_1.append("pksequencename, ");
            sb_1.append("savedtablename, ");
            sb_1.append("cardcustpanel, ");
            sb_1.append("listcustpanel ");
            sb_1.append(") ");
            sb_1.append("select  ");
            sb_1.append("s_pub_templet_1.nextval, ");
            sb_1.append("'" + str_temp_code + "', ");
            sb_1.append("'" + str_tem_name + "', ");
            sb_1.append("tablename, ");
            //sb_1.append("datasourcename, ");
            sb_1.append("pkname, ");
            sb_1.append("pksequencename, ");
            sb_1.append("savedtablename, ");
            sb_1.append("cardcustpanel, ");
            sb_1.append("listcustpanel ");
            sb_1.append("from pub_templet_1 where pk_pub_templet_1='" + str_pk + "' ");

            StringBuffer sb_2 = new StringBuffer();
            sb_2.append("insert into pub_templet_1_item ");
            sb_2.append("( ");
            sb_2.append("pk_pub_templet_1_item, ");
            sb_2.append("pk_pub_templet_1, ");
            sb_2.append("itemkey, ");
            sb_2.append("itemname, ");
            sb_2.append("itemtype, ");
            sb_2.append("comboxdesc, ");
            sb_2.append("refdesc, ");
            sb_2.append("issave, ");
            sb_2.append("isdefaultquery, ");
            sb_2.append("ismustinput, ");
            sb_2.append("loadformula, ");
            sb_2.append("editformula, ");
            sb_2.append("showorder, ");
            sb_2.append("listwidth, ");
            sb_2.append("cardwidth, ");
            sb_2.append("listisshowable, ");
            sb_2.append("listiseditable, ");
            sb_2.append("cardisshowable, ");
            sb_2.append("cardiseditable, ");
            sb_2.append("defaultvalueformula, ");
            sb_2.append("colorformula ");
            sb_2.append(") ");
            sb_2.append("select  ");
            sb_2.append("s_pub_templet_1_item.nextval, ");
            sb_2.append("(select pk_pub_templet_1 from pub_templet_1 where templetcode='" + str_temp_code + "'), ");
            sb_2.append("itemkey, ");
            sb_2.append("itemname, ");
            sb_2.append("itemtype, ");
            sb_2.append("comboxdesc, ");
            sb_2.append("refdesc, ");
            sb_2.append("issave, ");
            sb_2.append("isdefaultquery, ");
            sb_2.append("ismustinput, ");
            sb_2.append("loadformula, ");
            sb_2.append("editformula, ");
            sb_2.append("showorder, ");
            sb_2.append("listwidth, ");
            sb_2.append("cardwidth, ");
            sb_2.append("listisshowable, ");
            sb_2.append("listiseditable, ");
            sb_2.append("cardisshowable, ");
            sb_2.append("cardiseditable, ");
            sb_2.append("defaultvalueformula, ");
            sb_2.append("colorformula ");
            sb_2.append("from pub_templet_1_item where pk_pub_templet_1='" + str_pk + "' ");

            try {
                UIUtil.executeBatchByDS(null, new String[] {sb_1.toString(), sb_2.toString()}); //
                refreshMailPanel(); // 刷新页面
                JOptionPane.showMessageDialog(this, "复制模板已完成!");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "创建复制模板出错！");
            }
        }
    }

    private void onDelete() {
        int li_count = getMainPanel().getTable().getSelectedRowCount();
        if (li_count <= 0) {
            JOptionPane.showMessageDialog(this, "请先选择一条记录!");
            return;
        }

        if (JOptionPane.showConfirmDialog(this, "你真的想删除该模板数据吗?", "提示", JOptionPane.YES_NO_OPTION) !=
            JOptionPane.YES_OPTION) {
            return;
        }

        int selected_rows = getMainPanel().getTable().getSelectedRow();
        String str_pk = (String) getMainPanel().getValueAt(selected_rows, "PK_PUB_TEMPLET_1"); // 原来记录的主键!!

        String str_sql_1 = "delete from pub_templet_1_item where pk_pub_templet_1='" + str_pk + "'";
        String str_sql_2 = "delete from pub_templet_1      where pk_pub_templet_1='" + str_pk + "'";

        try {
            UIUtil.executeBatchByDS(null, new String[] {str_sql_1, str_sql_2}); //
            getChildPanel().clearTable(); //
            getMainPanel().removeRow(); //
            //JOptionPane.showMessageDialog(this, "删除模板成功!"); //			
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "删除模板失败！");
        }
    }

    /**
     * 导出全部
     */
    private void onBtnOutVO() {
        int li_count = getMainPanel().getTable().getSelectedRowCount();
        if (li_count <= 0) {
            JOptionPane.showMessageDialog(this, "请先至少选择一条记录!");
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = chooser.showSaveDialog(this);

        if (result != 0) {
            return;
        }
        String file_path = chooser.getSelectedFile().getPath() + "\\VOs";
        File directory = new File(file_path);
        if (directory.exists() && directory.isDirectory()) {
        } else if (!directory.mkdir()) {
            JOptionPane.showMessageDialog(this, "创建VO导出目录出错！", "错误提示", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int[] selected_rows = getMainPanel().getTable().getSelectedRows();
        for (int i = 0; i < selected_rows.length; i++) {
            String str_id = getMainPanel().getValueAt(selected_rows[i], "PK_PUB_TEMPLET_1").toString();
            String str_code = getMainPanel().getValueAt(selected_rows[i], "TEMPLETCODE").toString();
            new WriteIntoFile(this).writeToCodeVO(str_code, str_id, file_path);
        }
        JOptionPane.showMessageDialog(this, "导出VO成功！", "操作提示", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * 导出部分
     */
    private void onBtnOut() {
        int li_count = getMainPanel().getTable().getSelectedRowCount();
        if (li_count <= 0) {
            JOptionPane.showMessageDialog(this, "请先至少选择一条记录!");
            return;
        }
        int[] selected_rows = getMainPanel().getTable().getSelectedRows();
        dealOut(selected_rows);
    }

    /**
     * 根据选中的行来导出数据
     * @param _selected_rows
     */
    private void dealOut(int[] _selected_rows) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = chooser.showSaveDialog(this);

        if (result != 0) {
            return;
        }
        String file_path = chooser.getSelectedFile().getPath();

        getColumnsArray(); //
        TempletExport te=new TempletExport();
        for (int i = 0; i < _selected_rows.length; i++) {
            String str_templete_code = getMainPanel().getValueAt(_selected_rows[i], "TEMPLETCODE").toString(); //
            if (result == 0) {
                String file_total_path = file_path + "\\" + str_templete_code + ".sql";
                if(!te.exportTemplets(this, file_total_path, str_templete_code)){
                	JOptionPane.showMessageDialog(this, "导出脚本发生错误！");
                	return;
                }
            }
        }
        JOptionPane.showMessageDialog(this, "数据表导出成功！", "操作提示", JOptionPane.INFORMATION_MESSAGE);
    }

    private void onReference() {
        String str_sql = "Select tname 表名,tabtype 类型,tname 说明 From tab order by 类型,表名";

        SelectTableDialog refTableDialog = new SelectTableDialog(this, "选择表", str_sql);

        refTableDialog.setVisible(true);

        if (refTableDialog.getCloseType() == 0) {
            str_temp_code = refTableDialog.getTempleteCode();
            refreshMailPanel();
        }
    }


    private void refreshMailPanel() {
        String str_sql = " Select rowid,t.* From PUB_TEMPLET_1 t Where TEMPLETCODE='" + str_temp_code + "'";
        String[][] str_values = null;
        try {
            str_values = UIUtil.getStringArrayByDS(null, str_sql);
        } catch (NovaRemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (str_values == null || str_values.length == 0) {
            return;
        }
        int li_selected = getMainPanel().getTable().getSelectedRow();
        if (li_selected < 0) {
            li_selected = getMainPanel().getTable().getRowCount(); //在最后一行!!!
        } else {
            li_selected = li_selected + 1;
        }

        Object[][] objs = null;
        try {
            objs = UIUtil.getBillListDataByDS(null, str_sql, getMainPanel().getTempletVO(),
                                              NovaClientEnvironment.getInstance());
        } catch (NovaRemoteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } // 去取数,包括行号!!
        if (objs != null && objs.length > 0) {
            getMainPanel().insertRow(li_selected, objs[0]);
            getMainPanel().getTable().setRowSelectionInterval(li_selected, li_selected); //
        }
    }

    private JPanel getSouthPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        jbt_operator[11] = getBtn("保存", new Dimension(85, 20));

        JButton btn_insertRow = new JButton("新增");
        JButton btn_deleteRow = new JButton("删除");
        JButton btn_moveupRow = new JButton("上移");
        JButton btn_movedownRow = new JButton("下移");
        JButton btn_refresh = new JButton("刷新");

        btn_insertRow.setPreferredSize(new Dimension(85, 20));
        btn_deleteRow.setPreferredSize(new Dimension(85, 20));
        btn_moveupRow.setPreferredSize(new Dimension(85, 20));
        btn_movedownRow.setPreferredSize(new Dimension(85, 20));
        btn_refresh.setPreferredSize(new Dimension(85, 20));

        btn_insertRow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onInsertRow();
            }
        });

        btn_deleteRow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onDeleteRow();
            }
        });

        btn_moveupRow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onChildUpRow();
            }
        });

        btn_movedownRow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onChildDownRow();
            }
        });

        btn_refresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onRefresh();
            }
        });

        panel.add(btn_insertRow);
        panel.add(btn_deleteRow);
        panel.add(btn_moveupRow);
        panel.add(btn_movedownRow);

        panel.add(btn_refresh);
        panel.add(jbt_operator[11]);

        return panel;
    }

    private void onInsertRow() {
        int li_parentRow = getMainPanel().getTable().getSelectedRow();
        if (li_parentRow < 0) {
            return;
        }

        int li_row = getChildPanel().newRow(); //新增一行
        String str_parentid = (String) getMainPanel().getValueAt(li_parentRow, "PK_PUB_TEMPLET_1");
        getChildPanel().setValueAt(str_parentid, li_row, "PK_PUB_TEMPLET_1"); //

        getChildPanel().setValueAt(new ComBoxItemVO("文本框", "文本框", "文本框"), li_row, "ITEMTYPE"); //

        getChildPanel().setValueAt("Y", li_row, "CARDISSHOWABLE"); //
        getChildPanel().setValueAt(new ComBoxItemVO("1", "001", "全部可编辑"), li_row, "CARDISEDITABLE"); //

        getChildPanel().setValueAt("Y", li_row, "LISTISSHOWABLE"); //
        getChildPanel().setValueAt(new ComBoxItemVO("1", "001", "全部可编辑"), li_row, "LISTISEDITABLE"); //

        getChildPanel().setValueAt("150", li_row, "CARDWIDTH"); //
        getChildPanel().setValueAt("125", li_row, "LISTWIDTH"); //

        getChildPanel().setValueAt("" + (li_row + 1), li_row, "SHOWORDER"); //

        getChildPanel().setValueAt("N", li_row, "ISSAVE"); //
        getChildPanel().setValueAt("N", li_row, "ISMUSTINPUT"); //
        getChildPanel().setValueAt(new ComBoxItemVO("2", "002", "通用查询"), li_row, "ISDEFAULTQUERY"); //

        getChildPanel().getTable().getCellEditor(li_row, 0).cancelCellEditing();
        getChildPanel().getTable().editCellAt(li_row, 0); //
        JTextField textField = ( (JTextField) ( (DefaultCellEditor) getChildPanel().getTable().getCellEditor(li_row, 0)).
                                getComponent());
        textField.requestFocus();
    }

    private void onDeleteRow() {
        getChildPanel().removeRow(); //
    }

    private void onRefresh() {
        blp_child.stopEditing();
        getChildPanel().refreshData();
    }

    private void onRowSelectChanged() {
        int li_row = getMainPanel().getSelectedRow(); // 取得选中的行
        if (li_row < 0) {
            return;
        }
        String str_pk = (String) getMainPanel().getValueAt(li_row, "PK_PUB_TEMPLET_1"); // 取得主键值
        String str_sql = getChildPanel().getSQL(" 1=1 and PK_PUB_TEMPLET_1='" + str_pk + "' order by showorder asc ");
        getChildPanel().stopEditing(); //
        getChildPanel().QueryData(str_sql);
    }

    private void onSearch() {
        getMainPanel().getTable().clearSelection(); //清除选择..
        getMainPanel().stopEditing();
        getChildPanel().stopEditing();

        getMainPanel().getTable().clearSelection(); //清空选择
        getChildPanel().clearTable(); //
        getMainPanel().clearTable();

        String str_text = search_text.getText();
        if (str_text == null || str_text.trim().equals("")) {
            getMainPanel().QueryData("select * from PUB_TEMPLET_1 where 1=1 order by TEMPLETCODE asc");
        } else {
            str_text = str_text.trim().toLowerCase();
            String str_sql_condition = " 1=1 and (lower(templetcode) like '%" + str_text +
                "%' or lower(templetname) like '%" + str_text + "%') order by TEMPLETCODE asc ";
            getMainPanel().QueryData("select * from PUB_TEMPLET_1 where " + str_sql_condition);
        }
    }

    private void onSave() {
        blp_child.stopEditing();
        blp_main.stopEditing();

        int li_row = blp_main.getTable().getSelectedRow(); //
        try {
            String message = "";
            BillVO parent_vo = blp_main.getBillVO(li_row);
            HashMap par_parentmap = new HashMap();
            par_parentmap.put("par_1", null); //
            par_parentmap.put("par_2", null); //
            par_parentmap.put("par_3", new BillVO[] {parent_vo}); //
            try {
                FrameWorkMetaDataService service = (FrameWorkMetaDataService) NovaRemoteServiceFactory.getInstance().
                    lookUpService(FrameWorkMetaDataService.class); //
                service.commitBillVOByDS(null, null, null, new BillVO[] {parent_vo});
                blp_main.setAllRowStatusAs("INIT"); //
                message = message + "保存主表成功,"; //
            } catch (Exception ex) {
                ex.printStackTrace();
                message = message + "保存主表失败,"; //
            }

            BillVO[] deleteVOs = blp_child.getDeleteBillVOs(); //
            BillVO[] insertVOs = blp_child.getInsertBillVOs(); //
            BillVO[] updateVOs = blp_child.getUpdateBillVOs(); //
            try {
                FrameWorkMetaDataService service = (FrameWorkMetaDataService) NovaRemoteServiceFactory.getInstance().
                    lookUpService(FrameWorkMetaDataService.class); //
                service.commitBillVOByDS(null, deleteVOs, insertVOs, updateVOs);
                getChildPanel().clearDeleteBillVOs();
                getChildPanel().setAllRowStatusAs("INIT"); //
                message = message + "保存子表成功!!"; //
            } catch (Exception ex) {
                ex.printStackTrace();
                message = message + "保存子表失败!"; //
            }
            JOptionPane.showMessageDialog(this, message);
        } catch (Exception e) {
            e.printStackTrace(); //
        }
    }

    class Pair {
        private Object _key;

        private Object _value;

        private boolean _need = true;

        public Pair() {
            this._key = "";
            this._value = "";
        }

        public Pair(Pair _pair) {
            this._key = _pair._key;
            this._value = _pair._value;
        }

        public Pair(Object _key, Object _value) {
            this._key = _key;
            this._value = _value;
        }

        public Pair(Object _key, Object _value, boolean _need) {
            this._key = _key;
            this._value = _value;
            this._need = _need;
        }

        public Object getKey() {
            return this._key;
        }

        public Object getValue() {
            return this._value;
        }

        public boolean getNeed() {
            return this._need;
        }
    }
}
/**************************************************************************
 * $RCSfile: DeveloperTempleteConfig.java,v $  $Revision: 1.2.8.4 $  $Date: 2010/01/21 05:16:01 $
 *
 * $Log: DeveloperTempleteConfig.java,v $
 * Revision 1.2.8.4  2010/01/21 05:16:01  wangqi
 * *** empty log message ***
 *
 * Revision 1.2.8.3  2009/02/10 17:07:56  wangqi
 * 统一了模板编辑器和BillListPanel的模板编辑器的sql导出功能
 *
 * Revision 1.2.8.2  2009/02/06 07:52:29  wangqi
 * *** empty log message ***
 *
 * Revision 1.2.8.1  2008/12/18 06:46:42  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2007/05/31 07:38:15  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:01:37  qilin
 * no message
 *
 * Revision 1.11  2007/04/03 01:43:44  shxch
 * *** empty log message ***
 *
 * Revision 1.10  2007/03/27 10:14:34  shxch
 * *** empty log message ***
 *
 * Revision 1.9  2007/03/07 02:01:54  shxch
 * *** empty log message ***
 *
 * Revision 1.8  2007/03/02 05:16:42  shxch
 * *** empty log message ***
 *
 * Revision 1.7  2007/03/02 05:02:49  shxch
 * *** empty log message ***
 *
 * Revision 1.6  2007/03/01 09:06:56  shxch
 * *** empty log message ***
 *
 * Revision 1.5  2007/02/28 09:18:34  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/02/27 06:03:01  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/02/10 08:59:52  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:56:14  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
