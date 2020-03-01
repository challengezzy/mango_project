/**************************************************************************
 * $RCSfile: AbstractTempletFrame07.java,v $  $Revision: 1.6.6.10 $  $Date: 2010/01/14 04:48:55 $
 **************************************************************************/
package smartx.publics.styletemplet.ui.templet07;

import java.util.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import smartx.framework.common.ui.*;
import smartx.framework.common.utils.Sys;
import smartx.framework.metadata.ui.*;
import smartx.framework.metadata.ui.componentscard.*;
import smartx.framework.metadata.util.UIComponentUtil;
import smartx.framework.metadata.vo.*;
import smartx.publics.styletemplet.ui.*;


/**
 * 风格模板07：双表列列/卡
 * @author James.W
 *
 */
public abstract class AbstractTempletFrame07 extends AbstractStyleFrame implements NovaEventListener {
	public static final int BTN_INSERT_INDEX = 2;
    public static final int BTN_DELETE_INDEX = 4;
    public static final int BTN_UPDATE_INDEX = 8;
	
    //子表分布方向
	public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    protected String pritablename = null;

    protected String subtablename = null;

    protected String prifield = null;

    protected String subfield = null;

    protected String uiinterceptor = "";

    protected String bsinterceptor = "";

    

    protected BillListPanel pritable = null; // 主表,入口表,左表

    protected BillListPanel subtable = null; // 子表,关联表,处理表,右表

    protected JPanel sublistp = null;

    protected CardLayout cardlayout = null;

    private boolean enablesave = false;

    protected JButton btn_quick = new JButton(Sys.getSysRes("edit.simplequery.msg"), UIUtil.getImage(Sys.getSysRes("edit.simplequery.icon")));
    protected JButton btn_insert = new JButton(Sys.getSysRes("edit.new.msg"), UIUtil.getImage(Sys.getSysRes("edit.new.icon")));
    protected JButton btn_delete = new JButton(Sys.getSysRes("edit.delete.msg"), UIUtil.getImage(Sys.getSysRes("edit.delete.icon")));
    protected JButton btn_edit = new JButton(Sys.getSysRes("edit.edit.msg"), UIUtil.getImage(Sys.getSysRes("edit.edit.icon")));
    protected JButton btn_Search = new JButton(Sys.getSysRes("edit.complexquery.msg"), UIUtil.getImage(Sys.getSysRes("edit.complexquery.icon")));
    protected JButton btn_refresh = new JButton("刷新");
    protected JButton btn_switch = new JButton("切换列表");
    protected JButton btn_save_return = new JButton("保存并返回");
    protected JButton btn_cancel_return = new JButton("放弃并返回");
    protected JButton btn_save = new JButton(Sys.getSysRes("edit.save.msg"), UIUtil.getImage(Sys.getSysRes("edit.save.icon")));

    protected BillCardPanel card = null;

    protected String[] menu = null;

    protected String returntotable = "切换列表";

    protected String returntocard = "切换卡片";

    protected String customerpanel = null;

    protected AbstractCustomerButtonBarPanel panel_customer = null;

    private boolean showsystembutton = true;

    protected QuickQueryActionPanel querypanel = null;

    private IUIIntercept_07 uiIntercept = null; // ui端拦截器

    public AbstractTempletFrame07() {
        super();
        init();
    }

    public AbstractTempletFrame07(String _title) {
        super(_title);
    }

    public abstract String getPriTableTempletcode(); //

    public abstract String getPriTableAssocField();

    public abstract String getSubTableTempletcode(); //

    public abstract String getSubTableAssocField();

    public int getOrientation() {
        return AbstractTempletFrame07.VERTICAL;
    }

    public String[] getSys_Selection_Path() {
        return menu;
    }

    public String getCustomerpanel() {
        return customerpanel;
    }

    public boolean isShowsystembutton() {
        return showsystembutton;
    }

    protected void init() {
        pritablename = getPriTableTempletcode();
        subtablename = getSubTableTempletcode();
        prifield = getPriTableAssocField();
        subfield = getSubTableAssocField();
        menu = getSys_Selection_Path();
        customerpanel = this.getCustomerpanel();
        uiinterceptor = this.getUiinterceptor();
        bsinterceptor = this.getBsinterceptor();
        showsystembutton = isShowsystembutton();
        if (getUiinterceptor() != null && !getUiinterceptor().trim().equals("")) {
            try {
                uiIntercept = (IUIIntercept_07) Class.forName(getUiinterceptor().trim()).newInstance(); //
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.setSize(getTempletSize());
        this.setTitle(getTempletTitle());
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(getBtnPanel(), BorderLayout.NORTH);
        this.getContentPane().add(getBody(), BorderLayout.CENTER);		
        subtable.addNovaEventListener(this); // 注册自己事件监听!!
        card.addNovaEventListener(this); // 注册自己事件监听!!
    }

    public JComponent getBody() {
    	JPanel rpanel = new JPanel();
    	rpanel.setLayout(new BorderLayout());
    	
    	JSplitPane jsp = null;    	
        if (getOrientation() == HORIZONTAL) {
        	jsp=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, getPriList(), getSubList());//getSubList/getSubtable
        } else {
        	jsp=new JSplitPane(JSplitPane.VERTICAL_SPLIT, getPriList(), getSubList());            
        }
        jsp.setDividerSize(10);
        jsp.setDividerLocation(300);
        jsp.setOneTouchExpandable(true);
        jsp.setAutoscrolls(true);
        
        addPriListlistener();
        addSubListlistener();
        
        
        rpanel.add(getQueryPanel(), BorderLayout.NORTH);
        rpanel.add(jsp, BorderLayout.CENTER);
        return rpanel;
    }

    // 快速查询
    protected JPanel getQueryPanel() {
        if (querypanel != null) {
            return querypanel;
        }
        querypanel = new QuickQueryActionPanel(pritable);
        return querypanel;
    }

    protected BillListPanel getPriList() {
        if (pritable == null) {
            pritable = new BillListPanel(pritablename, true, false);
            pritable.setAllItemValue("listiseditable", NovaConstants.BILLCOMPENTEDITABLE_NONE);
            pritable.initialize();
        }
        pritable.QueryData(pritable.getSQL("1=1"));
        return pritable;
    }

    protected void addPriListlistener() {
        if (pritable != null) {
            pritable.getTable().addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        refreshSubView();
                    }
                }
            });
        }
    }

    protected void addSubListlistener() {
        if (subtable != null) {
            subtable.getTable().addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        subTableClicked();
                    }
                }
            });
        }
    }

    private void subTableClicked() {
        if (uiIntercept != null) {
            uiIntercept.actionMouseClickOnSubTable(this);
        }
    }

    /**
     * 获取子表所在的BillListPanel
     *
     * @return BillListPanel
     */
    public BillListPanel getSubtable() {
        if (subtable == null) {
            subtable = new BillListPanel(subtablename, true, false);
            subtable.setAllItemValue("listiseditable", NovaConstants.BILLCOMPENTEDITABLE_NONE);
            subtable.initialize();
        }
        return subtable;
    }

    protected void refreshSubView() {
        if (pritable.getTable().getSelectedRowCount() == 1) {
            if (uiIntercept != null) {
                uiIntercept.actionMouseClickOnPriTable(this);
            }
            subtable.QueryData(subtable.getSQL(subfield + "='" +
                                               pritable.getValueAt(pritable.getSelectedRow(), prifield) + "'"));
        }
    }

    protected JPanel getSubList() {
        JPanel rpanel = new JPanel();
        rpanel.setLayout(new BorderLayout());
        getSubtable();
        sublistp = new JPanel();
        cardlayout = new CardLayout();
        card = new BillCardPanel(subtable.getTempletVO());
        sublistp.setLayout(cardlayout);
        sublistp.add("list", subtable);
        sublistp.add("card", card);
        rpanel.add(sublistp, BorderLayout.CENTER);
        
        return rpanel;
    }

    /**
     * 获取主表所在的BillListPanel
     *
     * @return BillListPanel
     */
    public BillListPanel getPritable() {
        if (pritable == null) {
            pritable = new BillListPanel(pritablename, true, false);
            pritable.setAllItemValue("listiseditable", NovaConstants.BILLCOMPENTEDITABLE_NONE);
            pritable.initialize();
        }
        return pritable;
    }
    

    /**
     * 获得窗口显示标题
     * 首先，获得标题：如果有菜单导航数据则取最末级菜单名，否则取主元模板名。
     * 然后，把获得的标题与导航数据合并起来。
     * @return String
     */
    protected String getTempletTitle() {
        return (menu==null)?(pritablename + "-" + subtablename):(menu[menu.length - 1]+" ["+getNavigation()+"]");        
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
     * 获取窗口大小
     *
     * @return Dimension
     */
    protected Dimension getTempletSize() {
        return new Dimension(800, 600);
    }

    /**
     * 获取所有按钮的面板
     *
     * @return JPanel
     */
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
    
    /**
     * 获取子表
     *
     * @return JTable
     */
    public JTable getSubTable() {
        return subtable.getTable();
    }

    /**
     * 获取主表
     *
     * @return JTable
     */
    public JTable getPriTable() {
        return pritable.getTable();
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
    
    /**
     * 获取系统按钮
     *
     * @return JPanel
     */
    protected void getSysBtnPanel(JToolBar tbar) {
    	
        btn_save.setVisible(false);
        btn_save.setPreferredSize(UIComponentUtil.getButtonDefaultSize());
        btn_save.setFocusPainted(false);
        btn_save_return.setPreferredSize(UIComponentUtil.getButtonDefaultSize());
        btn_save_return.setVisible(false);
        btn_save_return.setFocusPainted(false);
        btn_cancel_return.setPreferredSize(UIComponentUtil.getButtonDefaultSize());
        btn_cancel_return.setVisible(false);
        btn_cancel_return.setFocusPainted(false);
        btn_switch.setPreferredSize(UIComponentUtil.getButtonDefaultSize());
        btn_switch.setVisible(false);
        btn_switch.setFocusPainted(false);
        btn_insert.setPreferredSize(UIComponentUtil.getButtonDefaultSize());
        btn_insert.setFocusPainted(false);
        btn_delete.setPreferredSize(UIComponentUtil.getButtonDefaultSize());
        btn_delete.setFocusPainted(false);
        btn_edit.setPreferredSize(UIComponentUtil.getButtonDefaultSize());
        btn_edit.setFocusPainted(false);
        btn_refresh.setPreferredSize(UIComponentUtil.getButtonDefaultSize());
        btn_refresh.setFocusPainted(false);
        btn_Search.setPreferredSize(UIComponentUtil.getButtonDefaultSize());
        btn_Search.setFocusPainted(false);
        
        btn_insert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onInsert();
            }
        });

        btn_delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onDelete();
            }
        });

        btn_edit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onEdit();
            }
        });
        btn_Search.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onQuery();
            }
        });
        btn_refresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onRefresh();
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
        
        tbar.add(btn_insert);
        tbar.add(btn_edit);
        tbar.add(btn_delete);
        tbar.add(btn_Search);
        tbar.add(btn_refresh);
        tbar.add(btn_save);
        tbar.add(btn_save_return);
        tbar.add(btn_cancel_return);
        tbar.add(btn_switch);

    }

    /**
     * 在卡片与列表间切换
     *
     */
    protected void onSwitch() {
        enablesave = !enablesave;
        btn_quick.setVisible(!enablesave);    	
        btn_insert.setVisible(!enablesave);
        btn_edit.setVisible(!enablesave);
        btn_Search.setVisible(!enablesave);
        btn_delete.setVisible(!enablesave);
        btn_switch.setVisible(enablesave);
        if (panel_customer != null) {
            panel_customer.setVisible(!enablesave);
        }
        if (querypanel != null) {
            querypanel.setVisible(!enablesave);
        }

        if (card.getEditState() == NovaConstants.BILLDATAEDITSTATE_INSERT ||
            card.getEditState() == NovaConstants.BILLDATAEDITSTATE_UPDATE) {
            btn_switch.setText(returntocard);
            if (enablesave) {
                btn_switch.setText(returntotable);
            }
            btn_switch.setVisible(true);
        }
        btn_save.setVisible(enablesave);
        btn_save_return.setVisible(enablesave);
        btn_cancel_return.setVisible(enablesave);
        cardlayout.next(sublistp);
    }

    public void onSaveAndReturn() {
        if (!onSave()) {
            return;
        }
        card.setEditState(NovaConstants.BILLDATAEDITSTATE_INIT);
        onSwitch();
    }

    public void onCancelAndReturn() {
        card.reset();
        card.setEditState(NovaConstants.BILLDATAEDITSTATE_INIT);
        onSwitch();
    }

    protected void onInsert() {    	
        subtable.getTable().editingStopped(new ChangeEvent(subtable.getTable()));
        HashMap map = new HashMap();
        String str_id = pritable.getRealValueAtModel(pritable.getSelectedRow(), prifield);
        map.put(subfield, str_id);
        onSwitch();
        card.createNewRecord();
        INovaCompent cardfield = card.getCompentByKey(subfield);
        if (cardfield instanceof UIRefPanel) {
            cardfield = (UIRefPanel) cardfield;
            RefItemVO itemvo = new RefItemVO(str_id, "", "");
            cardfield.setObject(itemvo);
        } else {
            card.setCompentObjectValue(subfield, str_id);
        }
        // 执行拦截器操作!!
        if (uiIntercept != null) {
            try {
                uiIntercept.actionAfterInsert(card); // 执行删除前的动作!!
            } catch (Exception e) {
                e.printStackTrace(); //
            }
        }
    }

    protected void onEdit() {
        int li_row = subtable.getTable().getSelectedRow(); // 取得选中的行!!
        if (li_row < 0) {
            return;
        }
        try {
            if (subtable.getTable().getSelectedColumnCount() != 1) {
                NovaMessage.show(AbstractTempletFrame07.this, "请选择一条记录", NovaConstants.MESSAGE_ERROR);
            } else {
                onSwitch(); //
                card.setValue(subtable.getValueAtRowWithHashMap(subtable.getSelectedRow()));
                card.setRowNumberItemVO( (RowNumberItemVO) subtable.getValueAt(subtable.getSelectedRow(), 0)); // 设置行号
                card.setEditState(NovaConstants.BILLDATAEDITSTATE_UPDATE);
            }
        } catch (Exception ex) {
            NovaMessage.show(AbstractTempletFrame07.this, NovaConstants.STRING_OPERATION_FAILED + ":" + ex.getMessage(),
                             NovaConstants.MESSAGE_ERROR);
        }
    }

    protected void onDelete() {
        int li_row = subtable.getTable().getSelectedRow(); // 取得选中的行!!
        if (li_row < 0) {
            NovaMessage.show(NovaConstants.STRING_DEL_SELECTION_NEED, NovaConstants.MESSAGE_ERROR);
            return;
        }
        // 执行拦截器删除前操作!!
        if (uiIntercept != null) {
            try {
                uiIntercept.actionBeforeDelete(subtable, li_row); // 执行删除前的动作!!
            } catch (Exception e) {
                if (!e.getMessage().trim().equals("")) {
                    JOptionPane.showMessageDialog(this, e.getMessage()); //
                }
                return; // 不往下走了!!
            }
        }

        // 提交删除数据!!!
        try {
            BillVO vo = subtable.getBillVO(li_row); //
            dealDelete(vo); // 真正删除
            subtable.removeRow(li_row); // 如果成功
            subtable.clearDeleteBillVOs();
        } catch (Exception ex) {
            ex.printStackTrace(); //
            //			JOptionPane.showMessageDialog(this, "删除记录失败,原因:" + ex.getMessage());
            NovaMessage.show(ex.getMessage(), NovaConstants.MESSAGE_ERROR);
        }
    }

    
    protected void dealDelete(BillVO _deleteVO) throws Exception {
        if (this.uiIntercept != null) {
            try {
                uiIntercept.dealCommitBeforeDelete(this, _deleteVO);
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
        }

        getService().style07_dealDelete(subtable.getDataSourceName(), getBsinterceptor(), _deleteVO); //

        if (this.uiIntercept != null) {
            uiIntercept.dealCommitAfterDelete(this, _deleteVO);
        }
    }

    protected boolean onSave() {    	
    	if (!card.checkValidate()) { //校验
            return false;
        }
    	
    	card.updateVersion();
        BillVO billVO = card.getBillVO(); //
        int _row=subtable.getSelectedRow();
        try {
        	if (card.getEditState() == NovaConstants.BILLDATAEDITSTATE_INSERT) { // 如果是新增提交
                if (!dealInsert(billVO)) { // 新增提交
                    return false;
                }
                card.setEditState(NovaConstants.BILLDATAEDITSTATE_UPDATE);
                HashMap map = card.getAllObjectValuesWithHashMap();
                subtable.insertRowWithInitStatus(_row, map);            
        	} else if (card.getEditState() == NovaConstants.BILLDATAEDITSTATE_UPDATE) { // 如果是修改提交
                if (!dealUpdate(billVO)) {
                    return false; // 修改提交
                }
                subtable.setValueAtRow(_row, billVO);
                subtable.setRowStatusAs(_row, "INIT");
                card.setEditState(NovaConstants.BILLDATAEDITSTATE_INIT);            
        	}
        	return true;
        } catch (Exception e1) {
            NovaMessage.show(e1.getMessage(), NovaConstants.MESSAGE_ERROR);
            return false;
        }
    };
    
    protected boolean dealInsert(BillVO _insertVO) throws Exception {
        // 执行新增提交前的拦截器
        if (this.uiIntercept != null) {
            uiIntercept.dealCommitBeforeInsert(this, _insertVO);            
        }
        BillVO returnVO = getService().style07_dealInsert(subtable.getDataSourceName(), getBsinterceptor(), _insertVO); // 直接提交数据库,这里可能抛异常!!
        // 执行新增提交后的拦截器
        if (this.uiIntercept != null) {
            uiIntercept.dealCommitAfterInsert(this, returnVO); //
        }
        return true;
    }

    protected boolean dealUpdate(BillVO _updateVO) throws Exception {
        if (this.uiIntercept != null) {
            uiIntercept.dealCommitBeforeUpdate(this, _updateVO); // 修改提交前拦截器            
        }
        BillVO returnvo = getService().style07_dealUpdate(subtable.getDataSourceName(), getBsinterceptor(), _updateVO); //
        if (this.uiIntercept != null) {
            uiIntercept.dealCommitAfterUpdate(this, returnvo); //
        }
        return true;
    }

    protected void onQuery() {
        QueryDialog queryDialog = new QueryDialog(AbstractTempletFrame07.this, subtable.getTempletVO());
        if (queryDialog.getStr_return_sql() != null && queryDialog.getStr_return_sql().length() > 0) {
            String str_wherecondition = "1=1";
            if (pritable.getTable().getSelectedRowCount() == 1) {
                str_wherecondition = subfield + "='" + pritable.getValueAt(pritable.getSelectedRow(), prifield) + "'";
            }
            subtable.QueryDataByCondition(queryDialog.getStr_return_sql() + " and " + str_wherecondition);
        }
    }

    /**
     * 卡片会调用这里
     */
    public void onValueChanged(NovaEvent _evt) {
        if (_evt.getChangedType() == NovaEvent.CardChanged) {
            if (uiIntercept != null) {
                BillCardPanel card_tmp = (BillCardPanel) _evt.getSource(); //
                String tmp_itemkey = _evt.getItemKey(); //
                try {
                    uiIntercept.actionAfterUpdate(card_tmp, tmp_itemkey);
                } catch (Exception e) {
                    if (!e.getMessage().trim().equals("")) {
                        JOptionPane.showMessageDialog(this, e.getMessage()); //
                    }
                }
            }
        } else if (_evt.getChangedType() == NovaEvent.ListChanged) { // 如果是列表变化

        }
    }

    protected void onRefresh() {
        subtable.refreshCurrData();
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

    /**
     * 获取所有用户自定义面板类名
     *
     * @return String
     */
    public String getCustomerPanelNames() {
        return this.customerpanel;
    }


    public void showInsertButton(boolean isshow) {
        this.btn_insert.setVisible(isshow);
    }

    public void showDeleteButton(boolean isshow) {
        this.btn_delete.setVisible(isshow);
    }

    public void showSearchButton(boolean isshow) {
        this.btn_Search.setVisible(isshow);
    }

    public void showRefreshButton(boolean isshow) {
        this.btn_refresh.setVisible(isshow);
    }

    public void setInsertButtonText(String text) {
        this.btn_insert.setText(text);
    }

    public void setDeleteButtonText(String text) {
        this.btn_delete.setText(text);
    }

    public void setEditButtonText(String text) {
        this.btn_edit.setText(text);
    }

    public void setSearchButtonText(String text) {
        this.btn_Search.setText(text);
    }

    public void setRefreshButtonText(String text) {
        this.btn_refresh.setText(text);
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

    public JButton getEditButton() {
        return this.btn_edit;
    }

    public void showEditButton(boolean isshow) {
        this.btn_edit.setVisible(isshow);
    }

    public JButton getSearchButton() {
        return this.btn_Search;
    }

    public JButton getRefreshButton() {
        return this.btn_refresh;
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

    public String[] getMustInputItemKeys() {
        ArrayList list = new ArrayList();
        for (int i = 0; i < this.subtable.getTempletVO().getItemVos().length; i++) {
            if (this.subtable.getTempletVO().getItemVos()[i].getIsMustInput().booleanValue()) {
                list.add(this.subtable.getTempletVO().getItemVos()[i].getItemkey());
            }
        }
        return (String[]) list.toArray(new String[0]);
    }

    public AbstractCustomerButtonBarPanel getPanel_customer() {
        return panel_customer;
    }

    public void setPanel_customer(AbstractCustomerButtonBarPanel customerpanel) {
        this.panel_customer = customerpanel;
    }

    public void setButtonStatus(int btnIndex, boolean status) {
        if ( (btnIndex & BTN_INSERT_INDEX) != 0) {
            btn_insert.setVisible(status);
        }
        if ( (btnIndex & BTN_DELETE_INDEX) != 0) {
            btn_delete.setVisible(status);
        }
        if ( (btnIndex & BTN_UPDATE_INDEX) != 0) {
            btn_edit.setVisible(status);
        }
    }
}
/*******************************************************************************
 * $RCSfile: AbstractTempletFrame07.java,v $ $Revision: 1.6.6.10 $ $Date: 2007/01/30
 * 04:48:32 $
 *
 * $Log: AbstractTempletFrame07.java,v $
 * Revision 1.6.6.10  2010/01/14 04:48:55  wangqi
 * *** empty log message ***
 *
 * Revision 1.6.6.9  2010/01/13 02:57:34  wangqi
 * *** empty log message ***
 *
 * Revision 1.6.6.8  2010/01/13 02:48:43  wangqi
 * *** empty log message ***
 *
 * Revision 1.6.6.7  2010/01/11 05:17:00  wangqi
 * *** empty log message ***
 *
 * Revision 1.6.6.6  2009/12/30 02:24:32  wangqi
 * *** empty log message ***
 *
 * Revision 1.6.6.5  2009/12/21 02:57:33  wangqi
 * *** empty log message ***
 *
 * Revision 1.6.6.4  2009/12/16 05:34:11  wangqi
 * *** empty log message ***
 *
 * Revision 1.6.6.3  2009/12/16 04:13:50  wangqi
 * *** empty log message ***
 *
 * Revision 1.6.6.2  2009/04/20 06:31:16  wangqi
 * *** empty log message ***
 *
 * Revision 1.6.6.1  2008/09/16 06:13:32  wangqi
 * patch   : 20080916
 * file    : nova_20080128_20080916.jar
 * content : 处理 MR nova20-87,nova20-30；
 * 另外，改写了快速查询面板的处理。
 *
 * Revision 1.6  2007/07/23 10:59:00  sunxf
 * Nova 20-14    平台层面更新数据的地方提供乐观锁控制，解决并发访问问题
 *
 * Revision 1.5  2007/07/04 01:38:53  qilin
 * 去掉系统状态栏
 *
 * Revision 1.4  2007/07/02 02:18:47  qilin
 * 去掉系统状态栏
 *
 * Revision 1.3  2007/05/31 07:39:04  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:14:28  qilin
 * no message
 *
 * Revision 1.7  2007/03/28 09:16:06  sunxf
 * *** empty log message ***
 *
 * Revision 1.6  2007/03/27 08:01:28  shxch
 * *** empty log message ***
 *
 * Revision 1.5  2007/03/27 07:55:19  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/03/13 03:51:38  sunxf
 * *** empty log message ***
 *
 * Revision 1.3  2007/03/08 10:53:18  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/03/06 07:55:17  qilin
 * no message
 *
 * Revision 1.1  2007/03/05 09:59:15  shxch
 * *** empty log message ***
 *
 * Revision 1.8  2007/02/10 08:59:37  shxch
 * *** empty log message ***
 *
 * Revision 1.7  2007/02/07 02:43:08  lujian
 * *** empty log message ***
 *
 * Revision 1.6  2007/02/05 05:08:18  lujian
 * *** empty log message ***
 *
 * Revision 1.5  2007/01/31 05:44:08  sunxf
 * 切换按钮的初始文字＂切换卡片＂改为＂切换列表＂
 *
 * Revision 1.4  2007/01/30 09:22:32  sunxf
 * "保存并返回"在保存失败后不返回
 *
 * Revision 1.3  2007/01/30 05:13:51  sunxf
 * 修改按钮文字和切换时是否显示
 * Revision 1.2 2007/01/30 04:48:32 lujian
 * *** empty log message ***
 *
 *
 ******************************************************************************/
