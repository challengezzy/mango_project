/**************************************************************************
 * $RCSfile: AbstractTempletFrame09.java,v $  $Revision: 1.9.6.10 $  $Date: 2010/02/03 08:14:54 $
 **************************************************************************/
package smartx.publics.styletemplet.ui.templet09;

import java.util.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import smartx.framework.common.ui.*;
import smartx.framework.common.utils.Sys;
import smartx.framework.common.vo.*;
import smartx.framework.metadata.ui.*;
import smartx.framework.metadata.ui.componentscard.*;
import smartx.framework.metadata.util.UIComponentUtil;
import smartx.framework.metadata.vo.*;
import smartx.publics.styletemplet.ui.*;
import smartx.publics.styletemplet.vo.*;


/**
 * 风格模板09：多子表
 * @author James.W
 *
 */
public abstract class AbstractTempletFrame09 extends AbstractStyleFrame implements NovaEventListener {
    protected String srt_parenttemplete_code = null; // 主表模板编码

    protected ArrayList arr_childtemplete_code = null; // 子模板编码

    protected String str_parent_pkname = null; // 主表主键字段名

    protected ArrayList arr_child_pkname = null; // 子表主键字段名

    protected IUIIntercept_09 uiinterceptor = null;

    protected String bsinterceptor = null;

    protected ArrayList arr_child_forpkname = null; // 子表外键字段名

    protected String[] menu = null;

    protected String customerpanel = null;

    protected AbstractCustomerButtonBarPanel panel_customer = null;

    protected BillListPanel parent_BillListPanel = null;

    protected BillCardPanel parent_BillCardPanel = null; //

    private ArrayList child_BillListPanel = null;

    protected JButton btn_quick = new JButton(Sys.getSysRes("edit.simplequery.msg"), UIUtil.getImage(Sys.getSysRes("edit.simplequery.icon")));
    protected JButton btn_save = new JButton(Sys.getSysRes("edit.save.msg"), UIUtil.getImage(Sys.getSysRes("edit.save.icon")));
    protected JButton btn_switch = new JButton("切换卡片");
    protected JButton btn_insert = new JButton(Sys.getSysRes("edit.new.msg"), UIUtil.getImage(Sys.getSysRes("edit.new.icon")));
    protected JButton btn_delete = new JButton(Sys.getSysRes("edit.delete.msg"), UIUtil.getImage(Sys.getSysRes("edit.delete.icon")));
    protected JButton btn_edit = new JButton(Sys.getSysRes("edit.edit.msg"), UIUtil.getImage(Sys.getSysRes("edit.edit.icon")));
    protected JButton btn_save_return = new JButton("保存并返回"); //
    protected JButton btn_cancel_return = new JButton("放弃并返回"); //
    protected JButton btn_Search = new JButton(Sys.getSysRes("edit.complexquery.msg"), UIUtil.getImage(Sys.getSysRes("edit.complexquery.icon")));
    protected JButton btn_view = new JButton(Sys.getSysRes("edit.view.msg"), UIUtil.getImage(Sys.getSysRes("edit.view.icon")));
    protected JButton btn_back = new JButton(Sys.getSysRes("sys.button.return.msg"), UIUtil.getImage(Sys.getSysRes("sys.button.return.icon")));

    private boolean oncreate = false;

    protected CardLayout cardlayout = null;

    private JPanel topanel = null;

    private int UPDATE_ROW_NUM = -1; // 主表中进行编辑的行号

    private JTabbedPane tabs = null;

    private BillListPanel currentchildpanel = null; // 当前选中的子表

    private int index = -1;

    private boolean initing = true; // 标示是否是初始化时，为子表TAB切换提供事件监听

    protected String returntotable = "切换列表";

    protected String returntocard = "切换卡片";

    private JPanel btnpanel = null;

    private JPanel[] childcustomerpanel = null; // 子表增删改面板.


    protected QuickQueryActionPanel querypanel = null;

    private boolean showsystembutton = true;

    public AbstractTempletFrame09() {
        super();
        init();
    }

    public AbstractTempletFrame09(String _title) {
        super(_title);
    }

    public abstract String getParentTableTempletcode(); //

    public abstract ArrayList getChildTableTempletcode();

    public abstract String getParentTablePK(); //

    public abstract ArrayList getChildTablePK();

    public abstract ArrayList getChildTableFK();

    public String[] getSys_Selection_Path() {
        return menu;
    }

    public String getCustomerpanel() {
        return customerpanel;
    }

    protected void init() {
        srt_parenttemplete_code = getParentTableTempletcode();
        arr_childtemplete_code = getChildTableTempletcode();
        str_parent_pkname = getParentTablePK();
        arr_child_pkname = getChildTablePK();
        arr_child_forpkname = getChildTableFK();
        menu = getSys_Selection_Path();
        customerpanel = getCustomerpanel();
        /**
         * 创建UI端拦截器
         */
        if (getUiinterceptor() != null && !getUiinterceptor().trim().equals("")) {
            try {
                uiinterceptor = (IUIIntercept_09) Class.forName(getUiinterceptor().trim()).newInstance(); //
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        showsystembutton = isShowsystembutton();
        this.setTitle(getTempletTitle()); //
        this.setSize(getTempletSize()); //
        this.getContentPane().setLayout(new BorderLayout()); //

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, getParentPanelWithBtn(), getChildPanel());
        splitPane.setDividerSize(10);
        splitPane.setDividerLocation(250);
        splitPane.setOneTouchExpandable(true);
        setAllChildCustomerJPanelVisible(false);
        this.getContentPane().add(splitPane, BorderLayout.CENTER); //
        initing = false;
        // 注册主表事件监听!!，子表在初始化时已注册.
        parent_BillCardPanel.addNovaEventListener(this);

    }

    protected JPanel getParentPanelWithBtn() {
        JPanel rpanel = new JPanel();
        rpanel.setLayout(new BorderLayout());
        rpanel.add(getBtnPanel(), BorderLayout.NORTH);
        rpanel.add(getParentPanel(), BorderLayout.CENTER);
        return rpanel;
    }

    /**
     * 系统按钮
     *
     * @return JPanel
     */
    protected JComponent getBtnPanel() {
    	JToolBar tbar = new JToolBar();
    	tbar.setFloatable(false);
        tbar.setBorderPainted(false);
        tbar.add(new JLabel("操作："));
        
        btn_quick.setPreferredSize(new Dimension(80, 20));
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
        				panel_customer=(AbstractCustomerButtonBarPanel)ctrl;
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

    protected Dimension getTempletSize() {
        return new Dimension(1000, 700);
    }

    public boolean isShowsystembutton() {
        return showsystembutton;
    }

    /**
     * 获得窗口显示标题
     * 首先，获得标题：如果有菜单导航数据则取最末级菜单名，否则取主元模板名。
     * 然后，把获得的标题与导航数据合并起来。
     * @return String
     */
    protected String getTempletTitle() {
        return (menu==null)?srt_parenttemplete_code:(menu[menu.length - 1]+" ["+getNavigation()+"]");        
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
    

    

    /**
     * 获取定义的UI拦截器类名
     *
     * @return String
     */
    public String getUiinterceptor() {
        return null;
    }

    /**
     * 获取BS拦截器类名.需要实现类覆盖
     *
     * @return String
     */
    public String getBsinterceptor() {
        return bsinterceptor;
    }

    public Object[] getNagivationPath() {
        return menu;
    }

    /**
     * 主表所在Panel
     *
     * @return JPanel
     */
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

    /**
     * 在卡片和列表中切换
     */
    protected void onSwitch() {
        oncreate = !oncreate;
        btn_quick.setVisible(!oncreate);
        btn_switch.setVisible(oncreate);
        btn_insert.setVisible(!oncreate);
        btn_edit.setVisible(!oncreate);
        btn_delete.setVisible(!oncreate);
        btn_view.setVisible(!oncreate);
        btn_Search.setVisible(!oncreate);
        if (panel_customer != null) {
            panel_customer.setVisible(!oncreate);
        }
        if (querypanel != null) {
            querypanel.setVisible(!oncreate);
        }
        if (oncreate) {
            btn_switch.setText(returntotable);
        } else {
            if (parent_BillCardPanel.getEditState().equals(NovaConstants.BILLDATAEDITSTATE_INSERT) ||
                parent_BillCardPanel.getEditState().equals(NovaConstants.BILLDATAEDITSTATE_UPDATE)) {
                btn_switch.setVisible(true);
                btn_switch.setText(returntocard);
            }
        }
        btn_save.setVisible(oncreate);
        btn_save_return.setVisible(oncreate);
        btn_cancel_return.setVisible(oncreate);
        cardlayout.next(topanel);
    }

    /*
     * 子表所在的Panel
     */
    protected JPanel getChildPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout()); //
        tabs = new JTabbedPane();
        tabs.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                if (!initing) {
                    setSelectStatus();
                }
            }

        });
        child_BillListPanel = new ArrayList();
        childcustomerpanel = new JPanel[arr_childtemplete_code.size()];
        for (int i = 0; i < this.arr_childtemplete_code.size(); i++) {
            tabs.addTab(getChild_BillListPanel(i).getTempletVO().getTempletname(), getChild_BillListPanel(i));
        }
        panel.add(tabs, BorderLayout.CENTER);
        currentchildpanel = (BillListPanel) tabs.getSelectedComponent();
        index = tabs.indexOfComponent(currentchildpanel);
        return panel;
    }

    public BillListPanel getParent_BillListPanel() {
        if (parent_BillListPanel != null) {
            return parent_BillListPanel;
        }
        parent_BillListPanel = new BillListPanel(srt_parenttemplete_code, true, false); //
        parent_BillListPanel.setLoadedFrame(this);
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
        parent_BillCardPanel = new BillCardPanel(parent_BillListPanel.getTempletVO());
        parent_BillCardPanel.setLoadedFrame(this);
        return parent_BillCardPanel;
    }

    public BillListPanel getChild_BillListPanel(int i) {
        if (child_BillListPanel != null && i < child_BillListPanel.size()) {
            return (BillListPanel) child_BillListPanel.get(i);
        }

        BillListPanel child = new BillListPanel( (String) arr_childtemplete_code.get(i), true, false);
        child.setLoadedFrame(this);
        child.setAllItemValue("listiseditable", NovaConstants.BILLCOMPENTEDITABLE_NONE);
        child.setCustomerNavigationJPanel(getChildCustomerJPanel(i));
        child.initialize();
        child.addNovaEventListener(this); // 注册监听器
        child_BillListPanel.add(child);
        return child;
    }

    /**
     * 得到所有子表页签!!
     *
     * @return
     */
    public BillListPanel[] getChild_BillListPanels() {
        return (BillListPanel[]) child_BillListPanel.toArray(new BillListPanel[0]);
    }

    protected void getSysBtnPanel(JToolBar tbar) {
        btn_save_return.setVisible(oncreate);
        btn_save_return.setPreferredSize(new Dimension(80, 20));
        btn_save_return.setFocusPainted(false);
        btn_cancel_return.setVisible(oncreate);
        btn_cancel_return.setPreferredSize(new Dimension(80, 20));
        btn_cancel_return.setFocusPainted(false);
        btn_switch.setVisible(oncreate);
        btn_switch.setPreferredSize(new Dimension(80, 20));
        btn_switch.setFocusPainted(false);
        btn_insert.setPreferredSize(new Dimension(80, 20));
        btn_insert.setFocusPainted(false);
        btn_delete.setPreferredSize(new Dimension(80, 20));
        btn_delete.setFocusPainted(false);
        btn_edit.setPreferredSize(new Dimension(80, 20));
        btn_edit.setFocusPainted(false);
        btn_Search.setPreferredSize(new Dimension(80, 20));
        btn_Search.setFocusPainted(false);
        btn_save.setVisible(oncreate);
        btn_save.setVisible(false);
        btn_save.setPreferredSize(new Dimension(80, 20));
        btn_save.setFocusPainted(false);
        btn_view.setPreferredSize(new Dimension(80, 20));
        btn_view.setFocusPainted(false);
        btn_back.setVisible(false);
        
        btn_insert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onParentInsert(); //
            }
        });
        btn_edit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onParentEdit();
            }
        });
        btn_delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onParentDelete();
            }
        });

        btn_Search.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onParentQuery();
            }
        });
        btn_view.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onParentView();
            }
        });

        btn_switch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onSwitch();
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
        
        
        
        btn_back.setPreferredSize(UIComponentUtil.getButtonDefaultSize());
        btn_back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancelAndReturn();
                btn_back.setVisible(false);
            }
        });
        
        
        tbar.add(btn_insert);
        tbar.add(btn_edit);
        tbar.add(btn_delete);
        tbar.add(btn_view);
        tbar.add(btn_Search);
        tbar.add(btn_save);
        tbar.add(btn_save_return);
        tbar.add(btn_cancel_return);
        tbar.add(btn_switch);
        tbar.add(btn_back);

        
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

    public void setAllChildTableEditable(boolean show) {
        for (int i = 0; i < childcustomerpanel.length; i++) {
            ( (BillListPanel) (child_BillListPanel.get(i))).stopEditing();
            ( (BillListPanel) (child_BillListPanel.get(i))).setAllItemValue("listiseditable",
                show ? NovaConstants.BILLCOMPENTEDITABLE_ALL : NovaConstants.BILLCOMPENTEDITABLE_NONE);
        }
        tabs.updateUI();
    }

    public void setAllChildTableStatusAS(String _status) {
        for (int i = 0; i < childcustomerpanel.length; i++) {
            ( (BillListPanel) (child_BillListPanel.get(i))).stopEditing();
            ( (BillListPanel) (child_BillListPanel.get(i))).setAllRowStatusAs(_status);
        }
        // tabs.updateUI();
    }

    public void clearAllChildTable() {
        for (int i = 0; i < childcustomerpanel.length; i++) {
            ( (BillListPanel) (child_BillListPanel.get(i))).stopEditing();
            ( (BillListPanel) (child_BillListPanel.get(i))).clearTable();
        }
        tabs.updateUI();
    }

    public void setAllChildCustomerJPanelVisible(boolean show) {
        for (int i = 0; i < childcustomerpanel.length; i++) {
            childcustomerpanel[i].setVisible(show);
        }
        tabs.updateUI();
    }

    protected JPanel getChildCustomerJPanel(int i) {
        childcustomerpanel[i] = new JPanel();
        FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
        layout.setHgap(10);
        childcustomerpanel[i].setLayout(layout);
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

        childcustomerpanel[i].add(btn_insert);
        childcustomerpanel[i].add(btn_delete);
        childcustomerpanel[i].add(btn_refresh);

        return childcustomerpanel[i];
    }

    private void setSelectStatus() {
        this.currentchildpanel = getCurrentChild();
        this.index = getCurrentIndex();
    }

    /**
     * 主表新增 事件
     *
     * @return void
     */
    protected void onParentInsert() {
        setAllChildCustomerJPanelVisible(true);
        for (int i = 0; i < child_BillListPanel.size(); i++) {
            ( (BillListPanel) (child_BillListPanel.get(i))).clearTable();
        }
        onSwitch();
        setAllChildTableEditable(true);
        parent_BillCardPanel.createNewRecord();
        // 执行主表新增 后拦截;
        if (uiinterceptor != null) {
            try {
                uiinterceptor.actionAfterInsert_parent(parent_BillCardPanel);
            } catch (Exception e1) {
                NovaMessage.show(this, e1.getMessage(), NovaConstants.MESSAGE_WARN);
                e1.printStackTrace();
                return;
            }
        }
    }

    /**
     * 主表编辑事件
     */
    protected void onParentEdit() {
        try {
            if (parent_BillListPanel.getTable().getSelectedRowCount() != 1) {
                NovaMessage.show(this, "请选择一条纪录", NovaConstants.MESSAGE_ERROR);
            } else {
                setAllChildCustomerJPanelVisible(true);
                setAllChildTableEditable(true);
                UPDATE_ROW_NUM = parent_BillListPanel.getSelectedRow();
                onSwitch();
                parent_BillCardPanel.setValue(parent_BillListPanel.getValueAtRowWithHashMap(parent_BillListPanel.getSelectedRow()));
                parent_BillCardPanel.setEditState(NovaConstants.BILLDATAEDITSTATE_UPDATE);
                parent_BillListPanel.setRowStatusAs(parent_BillListPanel.getSelectedRow(), NovaConstants.BILLDATAEDITSTATE_UPDATE);
            }
        } catch (Exception ex) {
            NovaMessage.show(NovaConstants.STRING_OPERATION_FAILED + ":" + ex.getMessage(), NovaConstants.MESSAGE_ERROR);
        }
    }

    protected void onParentQuery() {
        QueryDialog queryDialog = new QueryDialog(AbstractTempletFrame09.this, parent_BillListPanel.getTempletVO());
        if (queryDialog.getStr_return_sql() != null && queryDialog.getStr_return_sql().length() > 0) {
            parent_BillListPanel.QueryDataByCondition(queryDialog.getStr_return_sql());
        }
    }

    protected BillListPanel getCurrentChild() {
        return (BillListPanel) child_BillListPanel.get(tabs.getSelectedIndex());
    }

    protected int getCurrentIndex() {
        return tabs.getSelectedIndex();
    }

    protected void onChildInsert() {

        if (!oncreate && parent_BillListPanel.getTable().getSelectedRowCount() != 1) {
            NovaMessage.show(this, "请选择一条父纪录", NovaConstants.MESSAGE_WARN);
            return;
        }
        int li_row = currentchildpanel.newRow();
        currentchildpanel.setValueAt(parent_BillCardPanel.getValueAt(str_parent_pkname), li_row,
                                     (String) arr_child_forpkname.get(index));
        currentchildpanel.setValueAt(new Integer(1).toString(), currentchildpanel.getSelectedRow(), "VERSION");
        // 执行子表新增 后拦截;
        if (uiinterceptor != null) {
            try {
                uiinterceptor.actionAfterInsert_child(tabs.getSelectedIndex(), currentchildpanel, li_row);
            } catch (Exception e1) {
                NovaMessage.show(this, e1.getMessage(), NovaConstants.MESSAGE_WARN);
                e1.printStackTrace();
                return;
            }
        }

    }

    
    /**
     * 查看主表信息
     *
     */
    protected void onParentView() {
        try {

            if (parent_BillListPanel.getTable().getSelectedColumnCount() != 1) {
                NovaMessage.show(AbstractTempletFrame09.this, "请选择一条纪录", NovaConstants.MESSAGE_ERROR);
            } else {
                onSwitch();
                btn_save.setVisible(false);
                btn_save_return.setVisible(false);
                btn_cancel_return.setVisible(false);
                btn_switch.setVisible(false);
                btn_back.setVisible(true);
                parent_BillCardPanel.setValue(parent_BillListPanel.getValueAtRowWithHashMap(parent_BillListPanel.getSelectedRow()));
                parent_BillCardPanel.setRowNumberItemVO( (RowNumberItemVO) parent_BillListPanel.getValueAt(
                    parent_BillListPanel.getSelectedRow(), 0)); // 设置行号
                parent_BillCardPanel.setAllUnable();
                parent_BillCardPanel.setEditState(NovaConstants.BILLDATAEDITSTATE_INIT);
            }
        } catch (Exception ex) {
            NovaMessage.show(AbstractTempletFrame09.this, NovaConstants.STRING_OPERATION_FAILED + ":" + ex.getMessage(),
                             NovaConstants.MESSAGE_ERROR);
        }
    }

    protected boolean onSave() {
        for (int i = 0; i < child_BillListPanel.size(); i++) {
            ( (BillListPanel) (child_BillListPanel.get(i))).stopEditing();
        }

        if (!parent_BillCardPanel.checkValidate()) { //校验主表
            return false;
        }

        for (int i = 0; i < child_BillListPanel.size(); i++) {
            if (! ( (BillListPanel) (child_BillListPanel.get(i))).checkValidate()) { //校验子表
                return false;
            }
        }

        try {
            HashMap map = parent_BillCardPanel.getAllObjectValuesWithHashMap();
            AggBillVO aggvo = new AggBillVO();
            BillVO _parentvo=parent_BillCardPanel.getBillVO();
            aggvo.setParentVO(_parentvo);
            VectorMap child_billvo = new VectorMap();
            for (int i = 0; i < child_BillListPanel.size(); i++) {
                BillListPanel temppanel = (BillListPanel) (child_BillListPanel.get(i));
                child_billvo.put("" + (i + 1), temppanel.getBillVOs());
            }
            aggvo.setChildVOMaps(child_billvo);
            int _row=parent_BillListPanel.getSelectedRow();
            try {
                if (parent_BillCardPanel.getEditState().equals(NovaConstants.BILLDATAEDITSTATE_INSERT)) { // 如果是新增提交
                    if (!dealInsert(aggvo)) {
                        return false;
                    }
                    parent_BillListPanel.insertRowWithInitStatus(_row, map);
                    parent_BillListPanel.setAllRowStatusAs("INIT");
                } else if (parent_BillCardPanel.getEditState().equals(NovaConstants.BILLDATAEDITSTATE_UPDATE)) { // 如果是修改提交
                    if (!dealUpdate(aggvo)) {
                        return false;
                    }
                    BillVO billVO=parent_BillCardPanel.getBillVO(); //新的billvo，version已经修改
                    parent_BillListPanel.setValueAtRow(_row, billVO);
                    parent_BillListPanel.setRowStatusAs(_row, "INIT");
                    UPDATE_ROW_NUM = -1;
                }

                btn_save.setVisible(false);
                btn_save_return.setVisible(false);
                btn_cancel_return.setVisible(false);
                parent_BillCardPanel.setEditState(NovaConstants.BILLDATAEDITSTATE_INIT);
                this.setAllChildTableStatusAS(NovaConstants.BILLDATAEDITSTATE_INIT);
                setAllChildCustomerJPanelVisible(false);
                setAllChildTableEditable(false);
                NovaMessage.show(this,"保存数据成功！", NovaConstants.MESSAGE_INFO);
            } catch (Exception e) {
                NovaMessage.show(e.getMessage(), NovaConstants.MESSAGE_ERROR);
                return false;
            }

        } catch (Exception e) {
            NovaMessage.show(NovaConstants.STRING_OPERATION_FAILED + ":" + e.getMessage(), NovaConstants.MESSAGE_ERROR);
            return false;
        }
        return true;
    }
    
    /**
     * 新增 根据主表的BILLCARD来确定.
     *
     */
    protected boolean dealInsert(AggBillVO _insertVO) throws Exception {
        // 执行新增提交前的拦截器
        if (this.uiinterceptor != null) {
            uiinterceptor.dealCommitBeforeInsert(this, _insertVO);
        }
        AggBillVO returnVO = getService().style09_dealInsert(parent_BillListPanel.getDataSourceName(), getBsinterceptor(), _insertVO); // 直接提交数据库,这里可能抛异常!!
        // 执行新增提交前的拦截器
        if (this.uiinterceptor != null) {
            uiinterceptor.dealCommitAfterInsert(this, returnVO);
        }
        return true;
    }

    /**
     * 修改提交
     *
     */
    protected boolean dealUpdate(AggBillVO _updateVO) throws Exception {
        if (this.uiinterceptor != null) {
            uiinterceptor.dealCommitBeforeUpdate(this, _updateVO); // 修改提交前拦截器
        }
        //版本加1
    	_updateVO.getParentVO().updateVersion();
        AggBillVO returnvo = getService().style09_dealUpdate(parent_BillListPanel.getDataSourceName(), getBsinterceptor(),
            _updateVO); //
        //更新显示版本值
        parent_BillCardPanel.updateVersion();
        parent_BillListPanel.updateVersion();        
        for (int j = 0; j < child_BillListPanel.size(); j++) {
        	((BillListPanel)child_BillListPanel.get(j)).updateVersion();
		}
        if (this.uiinterceptor != null) {
            uiinterceptor.dealCommitAfterUpdate(this, returnvo); //
        }
        return true;
    }

    protected void onChildDelete() {
        // 执行子表删除前拦截;
        if (currentchildpanel.getTable().getSelectedRowCount() != 1) {
            NovaMessage.show(NovaConstants.STRING_DEL_SELECTION_NEED, NovaConstants.MESSAGE_ERROR);
            return;
        }
        if (uiinterceptor != null) {
            try {
                uiinterceptor.actionBeforeDelete_child(tabs.getSelectedIndex(), currentchildpanel,
                    currentchildpanel.getSelectedRow());
            } catch (Exception e1) {
                NovaMessage.show(this, e1.getMessage(), NovaConstants.MESSAGE_WARN);
                e1.printStackTrace();
                return;
            }
        }
        currentchildpanel.removeRow();
    }

    protected void onParentDelete() {
        // 删除时级联.....
        if (parent_BillListPanel.getTable().getSelectedRowCount() != 1) {
            NovaMessage.show(this, "请选择一条要删除的记录!");
            return;
        }

        int li_selectedRow = parent_BillListPanel.getTable().getSelectedRow();
        if (li_selectedRow < 0) {
            NovaMessage.show(NovaConstants.STRING_DEL_SELECTION_NEED, NovaConstants.MESSAGE_ERROR);
            return;
        }

        if (NovaMessage.confirm(this, "确定删除选中纪录，这将删除子表中的关联纪录?")) {
            // 执行拦截器删除前操作!!
            if (uiinterceptor != null) {
                try {
                    uiinterceptor.actionBeforeDelete_parent(parent_BillListPanel,
                        parent_BillListPanel.getTable().getSelectedRowCount()); // 执行删除前的动作!!
                } catch (Exception e) {
                    if (!e.getMessage().trim().equals("")) {
                        JOptionPane.showMessageDialog(this, e.getMessage()); //
                    }
                    return;
                }
            }

            // 创建删除的数据!!
            AggBillVO aggvo = new AggBillVO();
            aggvo.setParentVO(parent_BillListPanel.getBillVO(li_selectedRow)); // 只能删除一条纪录，所以billvo也只有一个.

            VectorMap child_billvo = new VectorMap();
            // 删除所有子表
            for (int i = 0; i < child_BillListPanel.size(); i++) {
                BillListPanel tmp_billListPanel = (BillListPanel) (child_BillListPanel.get(i));
                BillVO[] tmp_childVOs = new BillVO[tmp_billListPanel.getTable().getRowCount()];
                for (int j = 0; j < tmp_childVOs.length; j++) {
                    tmp_childVOs[j] = tmp_billListPanel.getBillVO(j);
                    tmp_childVOs[j].setEditType(NovaConstants.BILLDATAEDITSTATE_DELETE); //
                }
                child_billvo.put("" + (i + 1), tmp_childVOs); //
            }
            aggvo.setChildVOMaps(child_billvo);

            try {
                // 执行删除;
                dealDelete(aggvo); // 直接去删除!!

                // 从页面上移去记录!!
                for (int i = 0; i < child_BillListPanel.size(); i++) {
                    BillListPanel temppanel = (BillListPanel) (child_BillListPanel.get(i));
                    temppanel.removeAllRows();
                    temppanel.clearDeleteBillVOs();
                }

                // 删除主表
                parent_BillListPanel.removeRow();
                parent_BillListPanel.clearDeleteBillVOs(); // 清除删除的纪录
            } catch (Exception e) {
                e.printStackTrace();
                NovaMessage.show(this, "删除记录失败,原因:" + e.getMessage(), NovaConstants.MESSAGE_WARN); //
            }
        }
    }

    /**
     * 删除提交 主表删除时调用
     *
     */
    protected void dealDelete(AggBillVO _deleteVO) throws Exception {
        if (this.uiinterceptor != null) {
            try {
                uiinterceptor.dealCommitBeforeDelete(this, _deleteVO);
            } catch (Exception ex) {
                throw ex;
            }
        }

        getService().style09_dealDelete(parent_BillListPanel.getDataSourceName(), getBsinterceptor(), _deleteVO); //

        if (this.uiinterceptor != null) {
            uiinterceptor.dealCommitAfterDelete(this, _deleteVO);
        }

    }

    public void onSaveAndReturn() {
        if (!onSave()) {
            return;
        }
        //parent_BillCardPanel.setEditState(NovaConstants.BILLDATAEDITSTATE_INIT);
        onChildRefresh();
        onSwitch();
    }

    public void onCancelAndReturn() {
        parent_BillCardPanel.reset();
        setAllChildCustomerJPanelVisible(false);
        setAllChildTableEditable(false);
        clearAllChildTable();
        parent_BillCardPanel.setEditState(NovaConstants.BILLDATAEDITSTATE_INIT);
        parent_BillListPanel.getTable().clearSelection();
        onSwitch();
    }

    /**
     * 卡片会调用这里
     */
    public void onValueChanged(NovaEvent _evt) {
        // 如果主表以卡片形式被修改
        if (_evt.getChangedType() == NovaEvent.CardChanged) {
            if (uiinterceptor != null) {
                BillCardPanel card_tmp = (BillCardPanel) _evt.getSource(); //
                String tmp_itemkey = _evt.getItemKey(); //
                try {
                    uiinterceptor.actionAfterUpdate_parent(card_tmp, tmp_itemkey);
                } catch (Exception e) {
                    if (!e.getMessage().trim().equals("")) {
                        JOptionPane.showMessageDialog(this, e.getMessage()); //
                    }
                }
            }
        }
        // 如果子表被直接修改
        else if (_evt.getChangedType() == NovaEvent.ListChanged) {
            if (uiinterceptor != null) {
                BillListPanel list_tmp = (BillListPanel) _evt.getSource(); //
                String tmp_itemkey = _evt.getItemKey(); //
                try {
                    uiinterceptor.actionAfterUpdate_child(tabs.getSelectedIndex(), list_tmp, tmp_itemkey,
                        list_tmp.getSelectedRow());
                } catch (Exception e) {
                    if (!e.getMessage().trim().equals("")) {
                        JOptionPane.showMessageDialog(this, e.getMessage()); //
                    }
                }
            }
        }
    }

    public String[] getArr_childtemplete_code() {
        return (String[]) arr_childtemplete_code.toArray(new String[0]);
    }

    public JTable getTable() {
        return parent_BillListPanel.getTable();
    }

    public String getSrt_parenttemplete_code() {
        return srt_parenttemplete_code;
    }

    public String[] getArr_child_forpkname() {
        return (String[]) arr_child_forpkname.toArray(new String[0]);
    }

    public String[] getArr_child_pkname() {
        return (String[]) arr_child_pkname.toArray(new String[0]);
    }

    public String getStr_parent_pkname() {
        return str_parent_pkname;
    }

    public void onChildRefresh() {
        if (parent_BillListPanel.getTable().getSelectedRowCount() == 0) {
            for (int i = 0; i < child_BillListPanel.size(); i++) {
                BillListPanel temppanel = (BillListPanel) (child_BillListPanel.get(i));
                temppanel.refreshCurrData();
            }
        } else {
            refreshChildTable();
        }
    }

    protected void refreshChildTable() {
        for (int i = 0; i < child_BillListPanel.size(); i++) {
            BillListPanel temppanel = (BillListPanel) (child_BillListPanel.get(i));
            if (parent_BillListPanel.getTable().getSelectedRowCount() == 0) {
                temppanel.refreshCurrData();
                return;
            }
            temppanel.QueryDataByCondition(arr_child_forpkname.get(i) + "='" +
                                           parent_BillListPanel.getValueAt(parent_BillListPanel.getSelectedRow(),
                str_parent_pkname) + "'");
        }
    }

    protected BillListPanel getChildAt(int index) {
        return (BillListPanel) child_BillListPanel.get(index);
    }


    public void showInsertButton(boolean isshow) {
        this.btn_insert.setVisible(isshow);
    }

    public void showDeleteButton(boolean isshow) {
        this.btn_delete.setVisible(isshow);
    }

    public void showEditButton(boolean isshow) {
        this.btn_edit.setVisible(isshow);
    }

    public void showSearchButton(boolean isshow) {
        this.btn_Search.setVisible(isshow);
    }

    public void showRefreshButton(boolean isshow) {
        this.btn_Search.setVisible(isshow);
    }

    public void setInsertButtonText(String text) {
        this.btn_insert.setText(text);
    }

    public void setEditButtonText(String text) {
        this.btn_edit.setText(text);
    }

    public void setDeleteButtonText(String text) {
        this.btn_delete.setText(text);
    }

    public void setSearchButtonText(String text) {
        this.btn_Search.setText(text);
    }

    public void setRefreshButtonText(String text) {
        this.btn_Search.setText(text);
    }

    public void setSaveButtonText(String text) {
        this.btn_save.setText(text);
    }

    public JButton getInsertButton() {
        return this.btn_insert;
    }

    public JButton getDeleteButton() {
        return this.btn_delete;
    }

    public JButton getSearchButton() {
        return this.btn_Search;
    }

    public JButton getRefreshButton() {
        return this.btn_Search;
    }

    public JButton getEditButton() {
        return this.btn_edit;
    }

    public JButton getSaveButton() {
        return this.btn_save;
    }

    public void setSwitchButtonTexttoCard(String text) {
        this.returntocard = text;
    }

    public void setSwitchButtonTexttoList(String text) {
        this.returntotable = text;
    }

    public AbstractCustomerButtonBarPanel getPanel_customer() {
        return panel_customer;
    }

    public void setPanel_customer(AbstractCustomerButtonBarPanel customerpanel) {
        this.panel_customer = customerpanel;
    }
}
/*******************************************************************************
 * $RCSfile: AbstractTempletFrame09.java,v $ $Revision: 1.9.6.10 $ $Date: 2007/02/27
 * 06:57:20 $
 *
 * $Log: AbstractTempletFrame09.java,v $
 * Revision 1.9.6.10  2010/02/03 08:14:54  wangqi
 * *** empty log message ***
 *
 * Revision 1.9.6.9  2010/01/13 02:48:43  wangqi
 * *** empty log message ***
 *
 * Revision 1.9.6.8  2010/01/11 05:17:00  wangqi
 * *** empty log message ***
 *
 * Revision 1.9.6.7  2009/12/30 02:24:33  wangqi
 * *** empty log message ***
 *
 * Revision 1.9.6.6  2009/12/21 02:57:33  wangqi
 * *** empty log message ***
 *
 * Revision 1.9.6.5  2009/12/16 05:34:12  wangqi
 * *** empty log message ***
 *
 * Revision 1.9.6.4  2009/12/16 04:13:50  wangqi
 * *** empty log message ***
 *
 * Revision 1.9.6.3  2009/01/16 08:51:29  wangqi
 * *** empty log message ***
 *
 * Revision 1.9.6.2  2008/11/25 10:27:05  wangqi
 * *** empty log message ***
 *
 * Revision 1.9.6.1  2008/06/11 11:23:44  wangqi
 * *** empty log message ***
 *
 * Revision 1.9  2007/07/23 10:58:58  sunxf
 * Nova 20-14    平台层面更新数据的地方提供乐观锁控制，解决并发访问问题
 *
 * Revision 1.8  2007/07/04 01:38:53  qilin
 * 去掉系统状态栏
 *
 * Revision 1.7  2007/07/02 02:18:47  qilin
 * 去掉系统状态栏
 *
 * Revision 1.6  2007/06/25 03:22:38  qilin
 * no message
 *
 * Revision 1.5  2007/05/31 07:39:05  qilin
 * code format
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
 * Revision 1.8  2007/03/28 11:00:45  shxch
 * *** empty log message ***
 *
 * Revision 1.7  2007/03/28 09:16:06  sunxf
 * *** empty log message ***
 *
 * Revision 1.6  2007/03/27 08:01:28  shxch
 * *** empty log message ***
 *
 * Revision 1.5  2007/03/27 07:55:20  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/03/13 07:58:09  sunxf
 * *** empty log message ***
 *
 * Revision 1.3  2007/03/08 10:53:19  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/03/08 10:17:57  shxch
 * *** empty log message ***
 *
 * Revision 1.1  2007/03/05 09:59:13  shxch
 * *** empty log message ***
 * Revision 1.8 2007/02/27 06:57:20 shxch
 * *** empty log message ***
 *
 * Revision 1.7 2007/02/10 08:59:36 shxch *** empty log message ***
 *
 * Revision 1.6 2007/02/07 02:43:08 lujian *** empty log message ***
 *
 * Revision 1.5 2007/02/05 05:08:19 lujian *** empty log message ***
 *
 * Revision 1.4 2007/02/02 04:37:13 lujian *** empty log message ***
 *
 * Revision 1.3 2007/01/30 09:22:33 sunxf "保存并返回"在保存失败后不返回
 *
 * Revision 1.2 2007/01/30 04:48:31 lujian *** empty log message ***
 *
 *
 ******************************************************************************/
