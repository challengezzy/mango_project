/**************************************************************************
 * $RCSfile: AbstractTempletFrame.java,v $  $Revision: 1.10.8.6 $  $Date: 2010/01/13 02:48:43 $
 **************************************************************************/
package smartx.publics.styletemplet.ui.templet10;


import java.util.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import smartx.framework.common.ui.*;
import smartx.framework.common.ui.component.*;
import smartx.framework.common.utils.Sys;
import smartx.framework.metadata.ui.*;
import smartx.framework.metadata.ui.componentscard.*;
import smartx.framework.metadata.util.UIComponentUtil;


/**
 * 主子孙表
 * @author James.W
 */
public abstract class AbstractTempletFrame extends NovaInternalFrame {
    protected String srt_parenttemplete_code = null; // 主表模板编码

    protected String srt_childtemplete_code = null; // 子模板编码

    protected String str_parent_pkname = null; // 主表主键字段名

    protected String str_child_pkname = null; // 子表主键字段名

    protected String str_child_forpkname = null; // 子表外键字段名

    protected String str_grandchildtemplet_code = null;

    protected String str_grandchild_pkname = null;

    protected String str_grandchild_forpkname = null;

    protected String[] menu = null;

    CardLayout cardlayout = null;

    private JPanel topanel = null;

    private BillListPanel parent_BillListPanel = null;

    private BillCardPanel parent_BillCardPanel = null; //

    private BillListPanel child_BillListPanel = null;

    private BillListPanel grandchild_BillListPanel = null;

    protected JButton btn_quick = new JButton(Sys.getSysRes("edit.simplequery.msg"), UIUtil.getImage(Sys.getSysRes("edit.simplequery.icon")));
    protected JButton btn_save = new JButton(Sys.getSysRes("edit.save.msg"), UIUtil.getImage(Sys.getSysRes("edit.save.icon")));
    protected JButton btn_return = new JButton(Sys.getSysRes("edit.view.msg"), UIUtil.getImage(Sys.getSysRes("edit.view.icon")));
    protected JButton btn_save_return = new JButton("保存并返回"); //
    protected JButton btn_cancel_return = new JButton("放弃并返回"); //
    protected JButton btn_insert = new JButton(Sys.getSysRes("edit.new.msg"), UIUtil.getImage(Sys.getSysRes("edit.new.icon")));
    protected JButton btn_delete = new JButton(Sys.getSysRes("edit.delete.msg"), UIUtil.getImage(Sys.getSysRes("edit.delete.icon")));
    protected JButton btn_update = new JButton(Sys.getSysRes("edit.update.msg"), UIUtil.getImage(Sys.getSysRes("edit.update.icon")));
    protected JButton btn_query = new JButton(Sys.getSysRes("edit.complexquery.msg"), UIUtil.getImage(Sys.getSysRes("edit.complexquery.icon")));
    protected JButton btn_quicksearch = new JButton("显示数据"); //
    

    private boolean oncreate = false;

    private ArrayList deleteData = null;

    private static final int INSERT = 0;

    private static final int UPDATE = 1;

    private int status = -1;

    private String childlastselectkey = "-1";

    private JPanel childcustomerpanel = null;

    private JPanel grandchildcustomerpanel = null;

    HashMap granddata = new HashMap();

    HashMap insertrowmap = new HashMap();

    HashMap updaterowmap = new HashMap();

    protected String uiinterceptor = "";

    protected String bsinterceptor = "";

    protected String customerpanel = null;

    protected QuickQueryActionPanel querypanel = null;

    private boolean showsystembutton = true;

    public AbstractTempletFrame() {
        super();
        init();
    }

    public AbstractTempletFrame(String _title) {
        super(_title);
    }

    public abstract String getParentTableTempletcode(); //

    public abstract String getParentTablePK(); //

    public abstract String getChildTableTempletcode();

    public abstract String getChildTablePK();

    public abstract String getChildTableFK();

    public abstract String getGrandChildTableTempletcode();

    public abstract String getGrandChildTablePK();

    public abstract String getGrandChildTableFK();

    public boolean isShowsystembutton() {
        return showsystembutton;
    }

    protected void init() {
        srt_parenttemplete_code = getParentTableTempletcode();
        srt_childtemplete_code = getChildTableTempletcode();
        str_parent_pkname = getParentTablePK();
        str_child_pkname = getChildTablePK();
        str_child_forpkname = getChildTableFK();
        str_grandchildtemplet_code = getGrandChildTableTempletcode();
        str_grandchild_pkname = getGrandChildTablePK();
        str_grandchild_forpkname = getGrandChildTableFK();
        customerpanel = getCustomerpanel();
        uiinterceptor = getUiinterceptor(); //
        bsinterceptor = getBsinterceptor();
        showsystembutton = isShowsystembutton();
        menu = getSys_Selection_Path();
        this.setTitle(getTempletTitle());
        this.setSize(getTempletSize());
        this.getContentPane().setLayout(new BorderLayout()); //

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, getParentPanelWithBtn(), getChildPanel());
        splitPane.setDividerSize(10);
        splitPane.setDividerLocation(200);
        splitPane.setOneTouchExpandable(true);
        setAllChildCustomerJPanelVisible(false);
        this.getContentPane().add(splitPane, BorderLayout.CENTER); //
    }

    public String[] getSys_Selection_Path() {
        return menu;
    }

    public String getCustomerpanel() {
        return customerpanel;
    }

    protected Dimension getTempletSize() {
        return new Dimension(1000, 750);
    }

    protected String getChildTabTitle() {
        return this.srt_childtemplete_code;
    }

    protected String getGrandChildTabTitle() {
        return this.str_grandchildtemplet_code;
    }

    protected JPanel getParentPanelWithBtn() {
        JPanel rpanel = new JPanel();
        rpanel.setLayout(new BorderLayout());
        rpanel.add(getBtnPanel(), BorderLayout.NORTH);
        rpanel.add(getParentPanel(), BorderLayout.CENTER);
        return rpanel;
    }

    /**
     * 获得窗口显示标题
     * 首先，获得标题：如果有菜单导航数据则取最末级菜单名，否则取主元模板名。
     * 然后，把获得的标题与导航数据合并起来。
     * @return String
     */
    protected String getTempletTitle() {
        return (menu==null)?(srt_parenttemplete_code + "-" + srt_childtemplete_code + "-" + str_grandchildtemplet_code):(menu[menu.length - 1]+" ["+getNavigation()+"]");        
    }
    
    /**
     * 系统导航
     * @return String
     */
    protected String getNavigation() {
    	if (menu == null) {
            return "";
        }
        StringBuffer sbf=new StringBuffer();
        sbf.append(NovaConstants.STRING_CURRENT_POSITION);
        sbf.append(menu[0]);
        for (int i = 1; i < menu.length; i++) {
        	sbf.append("/");
        	sbf.append(menu[i]);        	
        }
        return sbf.toString();
    }
    
    

    protected JComponent getBtnPanel() {
    	JToolBar tbar = new JToolBar();
    	tbar.setFloatable(false);
        tbar.setBorderPainted(false);
        tbar.add(new JLabel("操作："));
        btn_quick.setPreferredSize(UIComponentUtil.getButtonDefaultSize());
        btn_quick.setFocusPainted(false);
        btn_quick.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent a) {
            	onQuickQuery();
            }
        });
        tbar.add(btn_quick);
        
        if (showsystembutton) {
            getSysBtnPanel(tbar);
        }
        
        //处理自定义面板
        if (customerpanel != null) {
        	CustomerCtrlIFC ctrl=getCustomerCtrl(customerpanel);
        	ctrl.setParentCtrl(this);//设置所在控件
        	Action[] acts=ctrl.getActionCtrls();
        	if(acts!=null){
        		UIComponentUtil.buildToolBar(acts ,tbar);
        	}else{
        		JComponent[] comps=ctrl.getJComponentCtrls();
        		if(comps!=null){
        			UIComponentUtil.buildToolBar(comps ,tbar);
        		}else{
        			if(ctrl instanceof AbstractCustomerButtonBarPanel){
        				AbstractCustomerButtonBarPanel panel_customer=(AbstractCustomerButtonBarPanel)ctrl;
        				panel_customer.setParentFrame(this); //设置所在控件
        				panel_customer.initialize(); //
        				tbar.add(panel_customer);
        			}
        		}
        	}        	
        }
        
        return tbar;
    }
    
    /**
     * 用户自定义控制
     *
     * @return JPanel
     */
    protected CustomerCtrlIFC getCustomerCtrl(String cls) {
        try {
            return (CustomerCtrlIFC)Class.forName(cls).newInstance();            
        } catch (Exception e) {
        	NovaMessage.show(this, "初始化[" + customerpanel + "]失败，请检查", NovaConstants.MESSAGE_ERROR);
            return null;
        }        
    }

    /**
     * 获取定义的UI拦截器类名
     *
     * @return String
     */
    public String getUiinterceptor() {
        return uiinterceptor;
    }

    public void setUiinterceptor(String uiinterceptor) {
        this.uiinterceptor = uiinterceptor;
    }

    public String getBsinterceptor() {
        return bsinterceptor;
    }


    public void setBsinterceptor(String bsinterceptor) {
        this.bsinterceptor = bsinterceptor;
    }

    public Object[] getNagivationPath() {
        return menu;
    }

    protected JPanel getParentPanel() {
        JPanel rpanel = new JPanel();
        rpanel.setLayout(new BorderLayout());
        cardlayout = new CardLayout();
        topanel = new JPanel(cardlayout);

        topanel.add("list", getParent_BillListPanel());
        topanel.add("card", getParent_BillCardPanel());
        rpanel.add(topanel, BorderLayout.CENTER);
        rpanel.add(getQueryPanel(), BorderLayout.NORTH);
        return rpanel;
    }

    // 快速查询
    protected JPanel getQueryPanel() {
        if (querypanel != null) {
            return querypanel;
        }
        querypanel = new QuickQueryActionPanel(this.parent_BillListPanel);
        return querypanel;
    }

    public BillListPanel getParent_BillListPanel() {
        if (parent_BillListPanel != null) {
            return parent_BillListPanel;
        }
        parent_BillListPanel = new BillListPanel(srt_parenttemplete_code, false, false); //
        parent_BillListPanel.setAllItemValue("listiseditable", NovaConstants.BILLCOMPENTEDITABLE_NONE);
        parent_BillListPanel.initialize();
        parent_BillListPanel.getTable().addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent arg0) {
                if (arg0.getButton() == MouseEvent.BUTTON1) {
                    refreshChildTable();
                }
            }
        });

        return parent_BillListPanel;
    }

    public BillCardPanel getParent_BillCardPanel() {
        if (parent_BillCardPanel != null) {
            return parent_BillCardPanel;
        }
        parent_BillCardPanel = new BillCardPanel(srt_parenttemplete_code);
        return parent_BillCardPanel;
    }

    private void refreshChildTable() {
        child_BillListPanel.QueryDataByCondition(str_child_forpkname + "='" +
                                                 parent_BillListPanel.getValueAt(parent_BillListPanel.getSelectedRow(),
            str_parent_pkname) + "'");
        grandchild_BillListPanel.clearTable();
    }

    protected JPanel getChildPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout()); //
        JTabbedPane tabp = new JTabbedPane();
        tabp.addTab(getChildTabTitle(), getChild_BillListPanel());
        tabp.addTab(getGrandChildTabTitle(), getGrandChildPanel());
        panel.add(tabp, BorderLayout.CENTER);
        return panel;
    }

    protected JPanel getGrandChildPanel() {
        JPanel rpanel = new JPanel();
        rpanel.setLayout(new BorderLayout());
        rpanel.add(getGrandChild_BillListPanel(), BorderLayout.CENTER);
        return rpanel;
    }

    public BillListPanel getChild_BillListPanel() {
        if (child_BillListPanel != null) {
            return child_BillListPanel;
        }
        child_BillListPanel = new BillListPanel(srt_childtemplete_code, false, false);
        child_BillListPanel.getTable().addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent arg0) {
                if (arg0.getButton() == MouseEvent.BUTTON1) {
                    grandchild_BillListPanel.stopEditing();
                    String currentselectkey = child_BillListPanel.getRealValueAtModel(child_BillListPanel.
                        getSelectedRow(), str_child_pkname);
                    if (!childlastselectkey.equals("-1")) {
                        // 对原选中子表纪录的备份操作
                        if (checkIsGrandChildChanged()) { // 如果孙表被修改过.保存孙表，然后再刷新.
                            // System.out.println("孙表"+childlastselectkey+"变化...");
                            saveGrandTableData(childlastselectkey, grandchild_BillListPanel.getValueAtAll());
                            // System.out.println("保存孙表"+childlastselectkey+"完成...");
                        }
                        // 对新选中子表记录的操作
                        // 如果先前保存过孙表的数据则恢复
                        if (checkExistGrandTableData(currentselectkey)) {
                            recoverGrandChildTable(currentselectkey); // 恢复
                        }
                        // 如果先前未保存过，刚直接刷新
                        else {
                            refreshGrandChildTable();
                        }
                    } else {
                        refreshGrandChildTable();
                    }
                    childlastselectkey = currentselectkey;

                }

            }

        });
        child_BillListPanel.setCustomerNavigationJPanel(getChildCustomerJPanel());
        child_BillListPanel.initialize();

        return child_BillListPanel;
    }

    public BillListPanel getGrandChild_BillListPanel() {
        if (grandchild_BillListPanel != null) {
            return grandchild_BillListPanel;
        }
        grandchild_BillListPanel = new BillListPanel(str_grandchildtemplet_code, false, false);
        grandchild_BillListPanel.setCustomerNavigationJPanel(getGrandChildCustomerJPanel());
        grandchild_BillListPanel.initialize();
        return grandchild_BillListPanel;
    }

    /**
     * 执行快速检索
     */
    protected void onQuickQuery() {
    	try{
    		querypanel.onQuery();
    	}catch(Exception ee){
    		NovaMessage.show(ee.getMessage(),NovaConstants.MESSAGE_ERROR);
    	}
    }
    
    protected void getSysBtnPanel(JToolBar tbar) {
    	btn_save_return.setVisible(oncreate);
        btn_save_return.setPreferredSize(UIComponentUtil.getButtonDefaultSize());
        btn_save_return.setFocusPainted(false);
        btn_cancel_return.setVisible(oncreate);
        btn_cancel_return.setPreferredSize(UIComponentUtil.getButtonDefaultSize());
        btn_cancel_return.setFocusPainted(false);
        btn_return.setVisible(oncreate);
        btn_return.setPreferredSize(UIComponentUtil.getButtonDefaultSize());
        btn_return.setFocusPainted(false);
        btn_insert.setPreferredSize(UIComponentUtil.getButtonDefaultSize());
        btn_insert.setFocusPainted(false);
        btn_delete.setPreferredSize(UIComponentUtil.getButtonDefaultSize());
        btn_delete.setFocusPainted(false);
        btn_update.setPreferredSize(UIComponentUtil.getButtonDefaultSize());
        btn_update.setFocusPainted(false);
        btn_query.setPreferredSize(UIComponentUtil.getButtonDefaultSize());
        btn_query.setFocusPainted(false);
        btn_save.setVisible(oncreate);
        btn_save.setPreferredSize(UIComponentUtil.getButtonDefaultSize());
        btn_save.setFocusPainted(false);
        btn_quicksearch.setPreferredSize(UIComponentUtil.getButtonDefaultSize());
        btn_quicksearch.setFocusPainted(false);

        btn_insert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onParentInsert(); //
            }
        });
        btn_update.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (parent_BillListPanel.getTable().getSelectedRowCount() != 1) {
                        NovaMessage.show(NovaConstants.STRING_DEL_SELECTION_NEED, NovaConstants.MESSAGE_ERROR);
                    } else {
                        setStatus(AbstractTempletFrame.UPDATE);
                        onReturn();
                        parent_BillCardPanel.setValue(parent_BillListPanel.getValueAtRowWithHashMap(
                            parent_BillListPanel.getSelectedRow()));
                    }
                } catch (Exception ex) {
                    NovaMessage.show(NovaConstants.STRING_OPERATION_FAILED + ":" + ex.getMessage(),
                                     NovaConstants.MESSAGE_ERROR);
                }
            }
        });
        btn_delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (parent_BillListPanel.getTable().getSelectedRowCount() <= 0) {
                        NovaMessage.show(NovaConstants.STRING_DEL_SELECTION_NEED, NovaConstants.MESSAGE_ERROR);
                    } else {
                        onParentDel();
                    }
                } catch (Exception ex) {
                    NovaMessage.show(NovaConstants.STRING_OPERATION_FAILED + ":" + ex.getMessage(),
                                     NovaConstants.MESSAGE_ERROR);
                }
            }
        });

        btn_query.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onParentQuery();
            }
        });
        btn_quicksearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onParentRefresh();
            }
        });

        btn_return.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onReturn();
            }
        });
        btn_save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onSave();
            }
        });
        btn_save_return.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onSaveAndReturn();
            }
        });
        btn_cancel_return.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancelAndReturn();
            }
        });
        tbar.add(btn_insert);
        tbar.add(btn_update);
        tbar.add(btn_delete);
        tbar.add(btn_query);
        tbar.add(btn_quicksearch);
        tbar.add(btn_save);
        tbar.add(btn_save_return);
        tbar.add(btn_cancel_return);
        tbar.add(btn_return);

    }

    public void onSaveAndReturn() {
        onSave();
        setAllChildCustomerJPanelVisible(false);
        parent_BillCardPanel.initCurrRow();
        onReturn();
    }

    public void onCancelAndReturn() {
        parent_BillCardPanel.reset();
        setAllChildCustomerJPanelVisible(false);
        parent_BillCardPanel.initCurrRow();
        this.setStatus( -1);
        onReturn();
    }

    private void setAllChildCustomerJPanelVisible(boolean show) {
        grandchildcustomerpanel.setVisible(show);
        childcustomerpanel.setVisible(show);
    }

    private JPanel getChildCustomerJPanel() {
        childcustomerpanel = new JPanel();
        FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
        layout.setHgap(10);
        childcustomerpanel.setLayout(layout);

        JButton btn_insert = new JButton(UIUtil.getImage("images/platform/insert.gif")); //
        JButton btn_delete = new JButton(UIUtil.getImage("images/platform/delete.gif")); //
        JButton btn_refresh = new JButton(UIUtil.getImage("images/platform/refresh.gif")); //

        btn_insert.setToolTipText("新增记录");
        btn_delete.setToolTipText("删除记录");
        btn_refresh.setToolTipText("刷新");

        btn_insert.setPreferredSize(new Dimension(18, 18));
        btn_delete.setPreferredSize(new Dimension(18, 18));
        btn_refresh.setPreferredSize(new Dimension(18, 18));

        btn_insert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onChildInsert();
            }
        });

        btn_delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onChildDelete();
            }
        });

        btn_refresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onChildRefresh();
            }
        });

        childcustomerpanel.add(btn_insert);
        childcustomerpanel.add(btn_delete);
        childcustomerpanel.add(btn_refresh);

        return childcustomerpanel;
    }

    private JPanel getGrandChildCustomerJPanel() {
        grandchildcustomerpanel = new JPanel();
        FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
        layout.setHgap(10);
        grandchildcustomerpanel.setLayout(layout);

        JButton btn_insert = new JButton(UIUtil.getImage("images/platform/insert.gif")); //
        JButton btn_delete = new JButton(UIUtil.getImage("images/platform/delete.gif")); //
        JButton btn_refresh = new JButton(UIUtil.getImage("images/platform/refresh.gif")); //

        btn_insert.setToolTipText("新增记录");
        btn_delete.setToolTipText("删除记录");
        btn_refresh.setToolTipText("刷新");

        btn_insert.setPreferredSize(new Dimension(18, 18));
        btn_delete.setPreferredSize(new Dimension(18, 18));
        btn_refresh.setPreferredSize(new Dimension(18, 18));

        btn_insert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onGrandChildInsert();
            }
        });

        btn_delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onGrandChildDel();
            }
        });

        btn_refresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onGrandChildRefresh();
            }
        });

        grandchildcustomerpanel.add(btn_insert);
        grandchildcustomerpanel.add(btn_delete);
        grandchildcustomerpanel.add(btn_refresh);

        return grandchildcustomerpanel;
    }

    public void onGrandChildRefresh() {
        if (child_BillListPanel.getTable().getSelectedRowCount() == 0) {
            grandchild_BillListPanel.refreshCurrData();
        } else {
            refreshGrandChildTable();
        }
    }

    private void refreshGrandChildTable() {
        grandchild_BillListPanel.QueryDataByCondition(str_grandchild_forpkname + "='" +
            child_BillListPanel.getValueAt(child_BillListPanel.getSelectedRow(), str_child_pkname) + "'");
    }

    public void onChildRefresh() {
        if (parent_BillListPanel.getTable().getSelectedRowCount() == 0) {
            child_BillListPanel.refreshCurrData();
            grandchild_BillListPanel.clearTable();
        } else {
            refreshChildTable();
        }
    }

    private void onReturn() {
        oncreate = !oncreate;
        btn_return.setVisible(oncreate);
        btn_insert.setVisible(!oncreate);
        btn_update.setVisible(!oncreate);
        btn_delete.setVisible(!oncreate);
        btn_quicksearch.setVisible(!oncreate);
        btn_query.setVisible(!oncreate);
        if (querypanel != null) {
            querypanel.setVisible(!oncreate);
        }
        if (oncreate) {
            btn_return.setText("返回列表");
        } else {
            if (getStatus() == AbstractTempletFrame.INSERT || getStatus() == AbstractTempletFrame.UPDATE) {
                btn_return.setVisible(true);
                btn_return.setText("返回卡片");
            }
        }
        btn_save.setVisible(oncreate);
        btn_save_return.setVisible(oncreate);
        btn_cancel_return.setVisible(oncreate);
        cardlayout.next(topanel);
    }

    private void onParentInsert() {
        setAllChildCustomerJPanelVisible(true);
        child_BillListPanel.clearTable();
        grandchild_BillListPanel.clearTable();
        onReturn();
        setStatus(AbstractTempletFrame.INSERT);
        parent_BillCardPanel.createNewRecord();
    }

    private void onParentQuery() {
        QueryDialog queryDialog = new QueryDialog(AbstractTempletFrame.this, parent_BillListPanel.getTempletVO());
        if (queryDialog.getStr_return_sql() != null && queryDialog.getStr_return_sql().length() > 0) {
            parent_BillListPanel.QueryDataByCondition(queryDialog.getStr_return_sql());
        }
    }

    private void onChildInsert() {

        if (!oncreate && parent_BillListPanel.getTable().getSelectedRowCount() != 1) {
            NovaMessage.show("请选择一条父记录", NovaConstants.MESSAGE_WARN);
            return;
        }

        int li_row = child_BillListPanel.newRow();
        if (!btn_save.isVisible()) {
            btn_save.setVisible(true);
        }
        if (getStatus() != -1) {
            child_BillListPanel.setValueAt(parent_BillCardPanel.getValueAt(str_parent_pkname), li_row,
                                           str_child_forpkname);
        } else {
            child_BillListPanel.setValueAt(parent_BillListPanel.getRealValueAtModel(parent_BillListPanel.getSelectedRow(),
                str_parent_pkname), li_row, str_child_forpkname);
        }

    }

    private void onGrandChildInsert() {

        if (!oncreate && child_BillListPanel.getTable().getSelectedRowCount() != 1) {
            NovaMessage.show("请选择一条父记录", NovaConstants.MESSAGE_WARN);
            return;
        }

        int li_row = grandchild_BillListPanel.newRow();
        if (!btn_save.isVisible()) {
            btn_save.setVisible(true);
        }
        grandchild_BillListPanel.setValueAt(child_BillListPanel.getRealValueAtModel(child_BillListPanel.getSelectedRow(),
            str_child_pkname), li_row, str_grandchild_forpkname);

    }

    private void onParentRefresh() {
        parent_BillListPanel.QueryData(parent_BillListPanel.getSQL("1=1"));
    }

    private void onSave() {
        child_BillListPanel.stopEditing();
        if (!parent_BillCardPanel.checkValidate()) { //校验
            return;
        }
        if (!child_BillListPanel.checkValidate()) { //校验
            return;
        }

        try {
        	parent_BillCardPanel.updateVersion();
            HashMap map = parent_BillCardPanel.getAllObjectValuesWithHashMap();
            int _row=parent_BillListPanel.getSelectedRow();
            ArrayList allsqls = new ArrayList();
            if (getStatus() == AbstractTempletFrame.INSERT) { // 新增时子表只会有新增的记录.不会有修改的记录.
            	allsqls.add(parent_BillCardPanel.getInsertSQL());
                parent_BillListPanel.insertRowWithInitStatus(_row, map);                
            } else if (getStatus() == AbstractTempletFrame.UPDATE) {
            	allsqls.add(parent_BillCardPanel.getUpdateSQL());
            	parent_BillListPanel.setValueAtRow(_row, map);
            }
            String[] childsqls = child_BillListPanel.getOperatorSQLs();
            String[] grandchildsqls = grandchild_BillListPanel.getOperatorSQLs();
            for (int i = 0; i < childsqls.length; i++) {
                allsqls.add(childsqls[i]);
            }
            for (int i = 0; i < grandchildsqls.length; i++) {
                allsqls.add(grandchildsqls[i]);
            }
            if (allsqls.size() <= 0) {
                return;
            }
            UIUtil.executeBatchByDS(null, allsqls);
            childlastselectkey = "-1";
            granddata.clear();
            insertrowmap.clear();
            updaterowmap.clear();
            grandchild_BillListPanel.setAllRowStatusAs("INIT");
            child_BillListPanel.setAllRowStatusAs("INIT");
            parent_BillListPanel.setAllRowStatusAs("INIT");
            NovaMessage.show(NovaConstants.STRING_OPERATION_SUCCESS);
            setAllChildCustomerJPanelVisible(false);
        } catch (Exception e) {
            NovaMessage.show(NovaConstants.STRING_OPERATION_FAILED + ":" + e.getMessage(), NovaConstants.MESSAGE_ERROR);
        }
        setStatus( -1);
    }

    private void onChildDelete() {
        if (NovaMessage.confirm(this, "确定删除选中记录，这将删除子表中的关联记录?")) {
            ArrayList sql_delete = new ArrayList();
            deleteData = new ArrayList();

            int[] li_selectRow = child_BillListPanel.getTable().getSelectedRows();
            for (int i = 0; i < li_selectRow.length; i++) {
                String id = child_BillListPanel.getRealValueAtModel(li_selectRow[i], this.str_child_pkname);
                String delsql = "delete from " + grandchild_BillListPanel.getTempletVO().getTablename() + " where " +
                    this.str_grandchild_forpkname + "='" + id + "'";
                sql_delete.add(delsql);
                deleteData.add(child_BillListPanel.getRealValueAtModel(li_selectRow[i], str_child_pkname));
            }
            child_BillListPanel.removeRows(li_selectRow);
            String[] str_dels = child_BillListPanel.getDeleteSQLs();
            if (str_dels != null && str_dels.length > 0) {
                for (int i = 0; i < str_dels.length; i++) {
                    sql_delete.add(str_dels[i]);
                }
            }
            try {
                if (sql_delete.size() > 0) {
                    UIUtil.executeBatchByDS(null, sql_delete);
                    sql_delete.clear();
                    grandchild_BillListPanel.clearTable();
                }
            } catch (Exception e) {
                NovaMessage.show(NovaConstants.STRING_OPERATION_FAILED + ":" + e.getMessage(),
                                 NovaConstants.MESSAGE_ERROR);
            }
        }
    }

    private void onGrandChildDel() {
        grandchild_BillListPanel.removeRow();
        String[] delsql = grandchild_BillListPanel.getDeleteSQLs();
        try {
            UIUtil.executeBatchByDS(null, delsql);
            NovaMessage.show(NovaConstants.STRING_OPERATION_SUCCESS);
        } catch (Exception e) {
            NovaMessage.show(NovaConstants.STRING_OPERATION_FAILED + ":" + e.getMessage(), NovaConstants.MESSAGE_ERROR);
        }
    }

    private void onParentDel() {
        // 删除时级联.....
        if (NovaMessage.confirm(this, "确定删除选中记录，这将删除子表中的关联记录?")) {
            ArrayList sql_delete = new ArrayList();
            deleteData = new ArrayList();

            // 删除孙表

            for (int p = 0; p < child_BillListPanel.getRowCount(); p++) {
                String id = child_BillListPanel.getRealValueAtModel(p, this.str_child_pkname);
                String delsql = "delete from " + grandchild_BillListPanel.getTempletVO().getTablename() + " where " +
                    this.str_grandchild_forpkname + "='" + id + "'";
                sql_delete.add(delsql);
            }
            grandchild_BillListPanel.clearTable();
            // 删除子表
            String id = parent_BillListPanel.getRealValueAtModel(parent_BillListPanel.getSelectedRow(),
                this.str_parent_pkname);
            String delsql = "delete from " + child_BillListPanel.getTempletVO().getTablename() + " where " +
                this.str_child_forpkname + "='" + id + "'";
            sql_delete.add(delsql);
            child_BillListPanel.clearTable();
            // 删除主表
            int[] li_selectRow = parent_BillListPanel.getTable().getSelectedRows();
            for (int i = 0; i < li_selectRow.length; i++) {
                deleteData.add(parent_BillListPanel.getRealValueAtModel(li_selectRow[i], str_parent_pkname));
            }
            parent_BillListPanel.removeRows(li_selectRow);
            String[] str_delsqls = parent_BillListPanel.getDeleteSQLs();
            if (str_delsqls != null && str_delsqls.length > 0) {
                for (int i = 0; i < str_delsqls.length; i++) {
                    sql_delete.add(str_delsqls[i]);
                }
            }
            try {
                if (sql_delete.size() > 0) {
                    UIUtil.executeBatchByDS(null, sql_delete);
                    sql_delete.clear();
                    child_BillListPanel.clearTable();
                }
            } catch (Exception e) {
                NovaMessage.show(NovaConstants.STRING_OPERATION_FAILED + ":" + e.getMessage(),
                                 NovaConstants.MESSAGE_ERROR);
            }
        }
    }

    private void recoverGrandChildTable(String _key) {
        Object[][] data = getGrandTableData(_key);
        grandchild_BillListPanel.clearTable();
        grandchild_BillListPanel.getTableModel().setValueAtAll(data);
        grandchild_BillListPanel.setAllRowStatusAs("INIT");
        if (updaterowmap.containsKey(_key)) { // 恢复更改行标志
            ArrayList updaterowlist = (ArrayList) updaterowmap.get(_key);
            for (int i = 0; i < updaterowlist.size(); i++) {
                grandchild_BillListPanel.setRowStatusAs( ( (Integer) updaterowlist.get(i)).intValue(), "UPDATE");
            }
            updaterowmap.remove(_key);
        }
        if (insertrowmap.containsKey(_key)) { // 恢复插入行标志
            ArrayList insertrowlist = (ArrayList) insertrowmap.get(_key);
            for (int i = 0; i < insertrowlist.size(); i++) {
                grandchild_BillListPanel.setRowStatusAs( ( (Integer) insertrowlist.get(i)).intValue(), "INSERT");
            }
            insertrowmap.remove(_key);
        }
        // System.out.println("恢愎"+_key+"孙表完成...");
        removeGrandChildFromBack(_key);
        // System.out.println("删除 "+_key+" 备份完成...");

    }

    private void removeGrandChildFromBack(String _key) {
        this.granddata.remove(_key);
    }

    private Object[][] getGrandTableData(String key) {
        return (Object[][])this.granddata.get(key);
    }

    private void saveGrandTableData(String key, Object[][] _data) {
        this.granddata.put(key, _data);
        insertrowmap.put(key, grandchild_BillListPanel.getRowNumsWithStatus("INSERT"));
        updaterowmap.put(key, grandchild_BillListPanel.getRowNumsWithStatus("UPDATE"));
    }

    private int getStatus() {
        return status;
    }

    private void setStatus(int i) {
        this.status = i;
    }

    private boolean checkExistGrandTableData(String key) {
        return this.granddata.containsKey(key);
    }

    private boolean checkIsGrandChildChanged() {
        for (int i = 0; i < grandchild_BillListPanel.getRowCount(); i++) {
            String temp = grandchild_BillListPanel.getValueAt(i, "_RECORD_ROW_NUMBER").toString();
            if (!temp.equals("INIT")) {
                return true;
            }
        }
        return false;
    }
}
/**************************************************************************
 * $RCSfile: AbstractTempletFrame.java,v $  $Revision: 1.10.8.6 $  $Date: 2010/01/13 02:48:43 $
 *
 * $Log: AbstractTempletFrame.java,v $
 * Revision 1.10.8.6  2010/01/13 02:48:43  wangqi
 * *** empty log message ***
 *
 * Revision 1.10.8.5  2010/01/11 05:17:00  wangqi
 * *** empty log message ***
 *
 * Revision 1.10.8.4  2009/12/30 02:24:33  wangqi
 * *** empty log message ***
 *
 * Revision 1.10.8.3  2009/12/21 02:57:33  wangqi
 * *** empty log message ***
 *
 * Revision 1.10.8.2  2009/12/16 05:34:12  wangqi
 * *** empty log message ***
 *
 * Revision 1.10.8.1  2009/12/16 04:13:50  wangqi
 * *** empty log message ***
 *
 * Revision 1.10  2007/07/02 02:18:46  qilin
 * 去掉系统状态栏
 *
 * Revision 1.9  2007/06/29 02:05:33  sunxf
 * *** empty log message ***
 *
 * Revision 1.8  2007/06/07 12:50:08  qilin
 * no message
 *
 * Revision 1.7  2007/05/31 10:43:07  qilin
 * no message
 *
 * Revision 1.6  2007/05/31 07:39:01  qilin
 * code format
 *
 * Revision 1.4  2007/05/23 03:29:04  qilin
 * no message
 *
 * Revision 1.3  2007/05/22 07:58:45  qilin
 * no message
 *
 * Revision 1.2  2007/05/22 02:04:11  sunxb
 * *** empty log message ***
 *
 * Revision 1.1  2007/05/17 06:14:29  qilin
 * no message
 *
 * Revision 1.6  2007/03/13 08:55:13  shxch
 * *** empty log message ***
 *
 * Revision 1.5  2007/03/02 05:16:43  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/03/01 09:05:34  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/02/10 08:59:37  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:48:32  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
