/**************************************************************************
 * $RCSfile: AbstractTempletFrame06.java,v $  $Revision: 1.9.6.4 $  $Date: 2010/01/13 02:48:43 $
 **************************************************************************/
package smartx.publics.styletemplet.ui.templet06;

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
 * 风格模板06：双表列列
 * @author Administrator
 *
 */
public abstract class AbstractTempletFrame06 extends AbstractStyleFrame implements NovaEventListener {
    public static final int HORIZONTAL = 0;

    public static final int VERTICAL = 1;

    protected String pritablename = null;

    protected String subtablename = null;

    protected String uiinterceptor = "";

    protected String bsinterceptor = "";

    protected String prifield = null;

    protected String subfield = null;

    protected int order = 1;

    protected BillListPanel pritable = null; // 主表,入口表,左表

    protected BillListPanel subtable = null; // 子表,关联表,处理表,右表

    protected Pub_Templet_1VO templetVO = null;

    protected Pub_Templet_1_ItemVO[] templetItemVOs = null;

    protected String customerpanel = null;

    protected AbstractCustomerButtonBarPanel panel_customer = null;

    protected String[] menu = null;

    protected JButton btn_quick = new JButton(Sys.getSysRes("edit.simplequery.msg"), UIUtil.getImage(Sys.getSysRes("edit.simplequery.icon")));
    protected JButton btn_insert = new JButton(Sys.getSysRes("edit.new.msg"), UIUtil.getImage(Sys.getSysRes("edit.new.icon")));
    protected JButton btn_delete = new JButton(Sys.getSysRes("edit.delete.msg"), UIUtil.getImage(Sys.getSysRes("edit.delete.icon")));
    protected JButton btn_save = new JButton(Sys.getSysRes("edit.save.msg"), UIUtil.getImage(Sys.getSysRes("edit.save.icon")));
    protected JButton btn_Search = new JButton(Sys.getSysRes("edit.complexquery.msg"), UIUtil.getImage(Sys.getSysRes("edit.complexquery.icon")));
    protected JButton btn_refresh_currpage = new JButton("刷新本页");

    protected QuickQueryActionPanel querypanel = null;

    private boolean showsystembutton = true;

    private IUIIntercept_06 uiIntercept = null; // ui端拦截器

    public AbstractTempletFrame06() {
        super();
        init();
    }

    public AbstractTempletFrame06(String _title) {
        super(_title);
    }

    public abstract String getPriTableTempletcode(); //

    public abstract String getPriTableAssocField();

    public abstract String getSubTableTempletcode(); //

    public abstract String getSubTableAssocField();

    public int getOrientation() {
        return AbstractTempletFrame06.VERTICAL;
    }

    public String[] getSys_Selection_Path() {
        return menu;
    }

    public boolean isShowsystembutton() {
        return showsystembutton;
    }

    public String getCustomerpanel() {
        return customerpanel;
    }

    public void init() {
        pritablename = getPriTableTempletcode();
        subtablename = getSubTableTempletcode();
        prifield = getPriTableAssocField();
        subfield = getSubTableAssocField();
        order = getOrientation();
        menu = getSys_Selection_Path();
        customerpanel = this.getCustomerpanel();
        uiinterceptor = this.getUiinterceptor();
        bsinterceptor = this.getBsinterceptor();
        showsystembutton = isShowsystembutton();
        if (getUiinterceptor() != null && !getUiinterceptor().trim().equals("")) {
            try {
                uiIntercept = (IUIIntercept_06) Class.forName(getUiinterceptor().trim()).newInstance(); //
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        this.setTitle(getTempletTitle());
        this.setSize(getTempletSize());
        this.getContentPane().setLayout(new BorderLayout());

        this.getContentPane().add(getBody(), BorderLayout.CENTER);
        this.getContentPane().add(getBtnpanel(), BorderLayout.NORTH);
        
        subtable.addNovaEventListener(this); // 注册自己事件监听!!
    }

    public JPanel getBody() {
        JPanel rpanel = new JPanel();
        rpanel.setLayout(new BorderLayout());
        JSplitPane jsp = new JSplitPane();
//        jsp.setDividerLocation(300);
        jsp.setOneTouchExpandable(true);
        if (order == HORIZONTAL) {
            jsp.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
            JScrollPane js1 = new JScrollPane(getPriList());
            JScrollPane js2 = new JScrollPane(getSubList());
            jsp.setTopComponent(js1);
            jsp.setBottomComponent(js2);
        } else {
            jsp.setOrientation(JSplitPane.VERTICAL_SPLIT);
            jsp.setTopComponent(getPriList());
            jsp.setBottomComponent(getSubList());
        }
        jsp.setDividerLocation((int)(this.getHeight()-80+querypanel.getPreferredSize().getHeight())/2);//余出标题的高度
        addPriListlistener();
        rpanel.add(jsp, BorderLayout.CENTER);
        return rpanel;
    }

    protected JPanel getPriList() {
        JPanel rpanel = new JPanel();
        rpanel.setLayout(new BorderLayout());
        getPritable();
        pritable.QueryData(pritable.getSQL("1=1"));
        rpanel.add(pritable, BorderLayout.CENTER);
        rpanel.add(getQueryPanel(), BorderLayout.NORTH);
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

    /**
     * 刷新子表
     *
     */
    protected void refreshSubView() {
        if (pritable.getTable().getSelectedRowCount() == 1) {
            subtable.QueryData(subtable.getSQL(subfield + "='" +
                                               pritable.getValueAt(pritable.getSelectedRow(), prifield) + "'"));
        }
    }

    protected JPanel getSubList() {
        JPanel rpanel = new JPanel();
        rpanel.setLayout(new BorderLayout());
        getSubtable();

        rpanel.add(subtable, BorderLayout.CENTER);        
        return rpanel;
    }

    /**
     * 获取子表的BillListPanel
     *
     * @return BillListPanel
     */
    public BillListPanel getSubtable() {
        if (subtable == null) {
            subtable = new BillListPanel(subtablename, true, false);
            subtable.initialize();
        }
        return subtable;
    }

    /**
     * 获取主表的BillListPanel
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
        return (menu==null)?pritablename:(menu[menu.length - 1]+" ["+getNavigation()+"]");        
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
     * 获取子表表格
     *
     * @return JTable
     */
    public JTable getTable() {
        return subtable.getTable();
    }

    /**
     * 获取面板，包括系统按钮面板和用户自定义按钮面板
     *
     * @return JPanel
     */
    protected JComponent getBtnpanel() {
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
     * 获取系统按钮面板
     *
     * @return Jpanel
     */
    protected void getSysBtnPanel(JToolBar tbar) {
    	
        btn_insert.setPreferredSize(UIComponentUtil.getButtonDefaultSize());
        btn_insert.setFocusPainted(false);
        btn_delete.setPreferredSize(UIComponentUtil.getButtonDefaultSize());
        btn_delete.setFocusPainted(false);
        btn_save.setPreferredSize(UIComponentUtil.getButtonDefaultSize());
        btn_save.setFocusPainted(false);
        btn_Search.setPreferredSize(UIComponentUtil.getButtonDefaultSize());
        btn_save.setFocusPainted(false);
        btn_refresh_currpage.setPreferredSize(UIComponentUtil.getButtonDefaultSize());
        btn_refresh_currpage.setFocusPainted(false);
        
        btn_insert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                subtable.getTable().editingStopped(new ChangeEvent(subtable.getTable()));
                onInsert();
            }
        });

        btn_delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                subtable.getTable().editingStopped(new ChangeEvent(subtable.getTable()));
                onDelete();
            }
        });

        btn_save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                subtable.getTable().editingStopped(new ChangeEvent(subtable.getTable()));
                onSave();
            }
        });

        btn_Search.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onRefresh();
            }
        });

        btn_refresh_currpage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                subtable.getTable().editingStopped(new ChangeEvent(subtable.getTable()));
                onRefreshCurrPage();
            }
        });

        
        tbar.add(btn_insert);
        tbar.add(btn_delete);
        tbar.add(btn_save);
        tbar.add(btn_Search);
        tbar.add(btn_refresh_currpage);
    }

    protected void onInsert() {
        subtable.getTable().editingStopped(new ChangeEvent(subtable.getTable()));
        int rownum = subtable.newRow();
        subtable.setValueAt(pritable.getValueAt(pritable.getTable().getSelectedRow(), this.prifield), rownum,
                            this.subfield);
        subtable.setValueAt(new Integer(1).toString(), subtable.getSelectedRow(), "VERSION");
        // 执行拦截器操作!!
        if (uiIntercept != null) {
            try {
                uiIntercept.actionAfterInsert(subtable, subtable.getSelectedRow()); // 执行删除前的动作!!
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, e.getMessage()); //
                return;
            }
        }
    };

    protected void onDelete() {
        subtable.getTable().editingStopped(new ChangeEvent(subtable.getTable()));
        int li_row = subtable.getTable().getSelectedRow(); // 取得选中的行!!
        if (li_row < 0) {
            NovaMessage.show(NovaConstants.STRING_DEL_SELECTION_NEED, NovaConstants.MESSAGE_ERROR);
            return;
        }
        if (NovaMessage.confirm(NovaConstants.STRING_DEL_CONFIRM)) {
            // 执行拦截器操作!!
            if (uiIntercept != null) {
                try {
                    uiIntercept.actionBeforeDelete(subtable, subtable.getSelectedRow()); // 执行删除前的动作!!
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e.getMessage()); //
                    return;
                }
            }
            subtable.removeRow();
        }
    };

    protected void onSave() {
        subtable.getTable().editingStopped(new ChangeEvent(subtable.getTable()));
        if (!subtable.checkValidate()) { //校验
            return;
        }

        try {
            BillVO[] insertvo = subtable.getInsertBillVOs();
            BillVO[] updatevo = subtable.getUpdateBillVOs();
            BillVO[] deletevo = subtable.getDeleteBillVOs();
            // 执行提交前拦截.
            if (this.uiIntercept != null) {
                try {
                    uiIntercept.dealBeforeCommit(subtable, insertvo, deletevo, updatevo);
                } catch (Exception e) {
                    e.printStackTrace();
                    NovaMessage.show(this, e.getMessage(), NovaConstants.MESSAGE_WARN);
                    return;
                }

            }
            for (int i = 0; i < updatevo.length; i++) {
				updatevo[i].updateVersion();
			}
            HashMap returnMap = getService().style06_dealCommit(subtable.getDataSourceName(), getBsinterceptor(),
                insertvo, deletevo, updatevo);
            subtable.updateVersion();
            subtable.clearDeleteBillVOs();
            for (int i = 0; i < subtable.getTable().getRowCount(); i++) {
                RowNumberItemVO itemvo = (RowNumberItemVO) subtable.getValueAt(i, "_RECORD_ROW_NUMBER");
                if (itemvo.getState().equals("UPDATE")) {
                    try {
                        int version = new Integer(subtable.getValueAt(i, "VERSION").toString()).intValue() + 1;
                        subtable.setValueAt(new Integer(version).toString(), i, "VERSION");
                    } catch (Exception ex) {
                    }
                }
            }

            // 执行提交后拦截.
            if (this.uiIntercept != null) {
                uiIntercept.dealAfterCommit(subtable, (BillVO[]) returnMap.get("INSERT"),
                                            (BillVO[]) returnMap.get("DELETE"), (BillVO[]) returnMap.get("UPDATE"));

            }
            subtable.clearDeleteBillVOs();
            NovaMessage.show(AbstractTempletFrame06.this, NovaConstants.STRING_OPERATION_SUCCESS);
            subtable.setAllRowStatusAs("INIT");
        } catch (Exception e) {
            NovaMessage.show(e.getMessage(), NovaConstants.MESSAGE_ERROR);
            e.printStackTrace();
        }

        } ;

        /**
         * 列表变化会调用这里
         */
        public void onValueChanged(NovaEvent _evt) {
            if (_evt.getChangedType() == NovaEvent.ListChanged) {
                if (uiIntercept != null) {
                    BillListPanel list_temp = (BillListPanel) _evt.getSource(); //
                    String tmp_itemkey = _evt.getItemKey(); //
                    try {
                        uiIntercept.actionAfterUpdate(list_temp, list_temp.getSelectedRow(), tmp_itemkey);
                    } catch (Exception e) {
                        if (!e.getMessage().trim().equals("")) {
                            JOptionPane.showMessageDialog(this, e.getMessage()); //
                        }
                    }
                }
            }
        }

        protected void onRefreshCurrPage() {
            if (pritable.getTable().getSelectedRowCount() == 1) {
                subtable.QueryData(subtable.getSQL(subfield + "='" +
                    pritable.getValueAt(pritable.getSelectedRow(), prifield) + "'"));
            }
        }

        protected void onRefresh() {
            if (pritable.getSelectedRow() != -1) {
                subtable.getTable().editingStopped(new ChangeEvent(subtable.getTable()));
                String condition = "1=1";
                if (pritable.getTable().getSelectedRowCount() == 1) {
                    condition = subfield + "='" + pritable.getValueAt(pritable.getSelectedRow(), prifield) + "'";
                }
                QueryDialog queryDialog = new QueryDialog(AbstractTempletFrame06.this, subtable.getTempletVO());
                if (queryDialog.getStr_return_sql() != null && queryDialog.getStr_return_sql().length() > 0) {
                    subtable.QueryDataByCondition(queryDialog.getStr_return_sql() + " and " + condition);
                }
            }
        }

        /**
         * 获取定义的BS拦截器类名
         *
         * @return String
         */
        public String getBsinterceptor() {
            return bsinterceptor;
        }

        public void setBsinterceptor(String bsinterceptor) {
            this.bsinterceptor = bsinterceptor;
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
            this.btn_Search.setVisible(isshow);
        }

        public void setInsertButtonText(String text) {
            this.btn_insert.setText(text);
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

        public JButton getSaveButton() {
            return this.btn_save;
        }
    }
    /**************************************************************************
     * $RCSfile: AbstractTempletFrame06.java,v $  $Revision: 1.9.6.4 $  $Date: 2010/01/13 02:48:43 $
     *
     * $Log: AbstractTempletFrame06.java,v $
     * Revision 1.9.6.4  2010/01/13 02:48:43  wangqi
     * *** empty log message ***
     *
     * Revision 1.9.6.3  2009/12/21 02:57:33  wangqi
     * *** empty log message ***
     *
     * Revision 1.9.6.2  2009/12/16 05:34:11  wangqi
     * *** empty log message ***
     *
     * Revision 1.9.6.1  2009/12/16 04:13:50  wangqi
     * *** empty log message ***
     *
     * Revision 1.9  2007/07/31 05:08:31  sunxf
     * MR#:Nova 20-16
     *
     * Revision 1.8  2007/07/23 10:58:59  sunxf
     * Nova 20-14    平台层面更新数据的地方提供乐观锁控制，解决并发访问问题
     *
     * Revision 1.7  2007/07/04 01:38:52  qilin
     * 去掉系统状态栏
     *
     * Revision 1.6  2007/07/02 02:18:46  qilin
     * 去掉系统状态栏
     *
     * Revision 1.5  2007/06/15 06:18:04  qilin
     * MR#:BZM10-121
     *
     * Revision 1.4  2007/05/31 07:39:04  qilin
     * code format
     *
     * Revision 1.2  2007/05/23 03:29:05  qilin
     * no message
     *
     * Revision 1.1  2007/05/17 06:14:28  qilin
     * no message
     *
     * Revision 1.5  2007/04/24 05:56:59  qilin
     * no message
     *
     * Revision 1.4  2007/03/27 08:01:28  shxch
     * *** empty log message ***
     *
     * Revision 1.3  2007/03/08 10:53:18  shxch
     * *** empty log message ***
     *
     * Revision 1.2  2007/03/08 10:40:41  shxch
     * *** empty log message ***
     *
     * Revision 1.1  2007/03/05 09:59:12  shxch
     * *** empty log message ***
     *
     * Revision 1.5  2007/02/10 08:59:37  shxch
     * *** empty log message ***
     *
     * Revision 1.4  2007/02/05 05:08:18  lujian
     * *** empty log message ***
     *
     * Revision 1.3  2007/02/02 04:37:13  lujian
     * *** empty log message ***
     *
     * Revision 1.2  2007/01/30 04:48:32  lujian
     * *** empty log message ***
     *
     *
     **************************************************************************/
