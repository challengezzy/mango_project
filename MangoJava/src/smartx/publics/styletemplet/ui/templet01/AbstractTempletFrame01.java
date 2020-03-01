/**************************************************************************
 * $RCSfile: AbstractTempletFrame01.java,v $  $Revision: 1.8.6.6 $  $Date: 2010/01/13 02:48:43 $
 **************************************************************************/
package smartx.publics.styletemplet.ui.templet01;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JToolBar;

import org.apache.log4j.Logger;

import smartx.framework.common.ui.NovaConstants;
import smartx.framework.common.ui.UIUtil;
import smartx.framework.common.utils.Sys;
import smartx.framework.common.vo.NovaLogger;
import smartx.framework.metadata.ui.AbstractCustomerButtonBarPanel;
import smartx.framework.metadata.ui.BillListPanel;
import smartx.framework.metadata.ui.CustomerCtrlIFC;
import smartx.framework.metadata.ui.NovaEvent;
import smartx.framework.metadata.ui.NovaEventListener;
import smartx.framework.metadata.ui.NovaMessage;
import smartx.framework.metadata.ui.QuickQueryActionPanel;
import smartx.framework.metadata.ui.componentscard.QueryDialog;
import smartx.framework.metadata.util.UIComponentUtil;
import smartx.framework.metadata.vo.BillVO;
import smartx.publics.styletemplet.ui.AbstractStyleFrame;

/**
 * 风格模板01：单表列
 * @author Administrator
 *
 */
public abstract class AbstractTempletFrame01 extends AbstractStyleFrame implements NovaEventListener {
    private Logger logger=NovaLogger.getLogger(AbstractTempletFrame01.class);
	
	protected String str_templetecode = null;                        //元模板
    protected String customerpanel = null;                          //自定义面板
    protected AbstractCustomerButtonBarPanel panel_customer = null; //自定义面板 //TODO 应该不需要此变量，作为局部变量即可
    protected String uiinterceptor = "";                            //UI拦截器
    protected String bsinterceptor = "";                            //BS拦截器
    protected IUIIntercept_01 uiIntercept = null;                   //ui端拦截器
    
    protected String[] menu = null;                                 //通过菜单得到的导航列表

    protected QuickQueryActionPanel querypanel = null;              //快速查询面板
    protected BillListPanel mainBillListPanel = null;               //数据列表框

    protected boolean showsystembutton = true;                      //是否显示系统按钮

    protected JButton btn_quick = UIComponentUtil.getButton(
        Sys.getSysRes("edit.simplequery.msg"), 
        UIUtil.getImage(Sys.getSysRes("edit.simplequery.icon")),
        UIComponentUtil.getButtonDefaultSize(),
        true,false,
        new ActionListener() {
            public void actionPerformed(ActionEvent a) {
            	onQuickQuery();
            }
        }
    );
    protected JButton btn_insert = UIComponentUtil.getButton(
        Sys.getSysRes("edit.new.msg"), 
        UIUtil.getImage(Sys.getSysRes("edit.new.icon")),
        UIComponentUtil.getButtonDefaultSize(),
        true,false,
        new ActionListener() {
            public void actionPerformed(ActionEvent a) {
            	onInsert();
            }
        }
    );
    protected JButton btn_delete = UIComponentUtil.getButton(
		Sys.getSysRes("edit.delete.msg"), 
		UIUtil.getImage(Sys.getSysRes("edit.delete.icon")),
        UIComponentUtil.getButtonDefaultSize(),
        true,false,
        new ActionListener() {
            public void actionPerformed(ActionEvent a) {
            	onDelete();
            }
        }
    );
    protected JButton btn_save = UIComponentUtil.getButton(
		Sys.getSysRes("edit.save.msg"), 
		UIUtil.getImage(Sys.getSysRes("edit.save.icon")),
        UIComponentUtil.getButtonDefaultSize(),
        true,false,
        new ActionListener() {
            public void actionPerformed(ActionEvent a) {
            	onSave();
            }
        }
	);
    protected JButton btn_Search = UIComponentUtil.getButton(
		Sys.getSysRes("edit.complexquery.msg"), 
		UIUtil.getImage(Sys.getSysRes("edit.complexquery.icon")),
        UIComponentUtil.getButtonDefaultSize(),
        true,false,
        new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	            QueryDialog queryDialog = new QueryDialog(AbstractTempletFrame01.this, mainBillListPanel.getTempletVO());
	            if (queryDialog.getStr_return_sql() != null && queryDialog.getStr_return_sql().length() > 0) {
	                onSearch(queryDialog.getStr_return_sql());
	            }
	        }
        }
	);
    
    
    /**
     * 构造方法
     */
    public AbstractTempletFrame01() {
        super();
        init();
    }

    /**
     * 构造方法
     */
    public AbstractTempletFrame01(String _title) {
        super(_title);
    }

    /**
     * 初始化方法
     */
    protected void init() {
        str_templetecode = getTempletcode();
        customerpanel = getCustomerpanel();
        uiinterceptor = getUiinterceptor(); //
        bsinterceptor = getBsinterceptor();
        menu = getSys_Selection_Path();
        showsystembutton = isShowsystembutton();
        
        
        /**
         * 创建UI端拦截器
         */
        if (getUiinterceptor() != null && !getUiinterceptor().trim().equals("")) {
            try {
                uiIntercept = (IUIIntercept_01) Class.forName(getUiinterceptor().trim()).newInstance(); //
            } catch (Exception e) {
                logger.error("实例化UI拦截器错误："+getUiinterceptor(),e);
            }
        }

        this.setTitle(getTempletTitle()); //设置标题
        this.setSize(getTempletSize());   //设置界面大小
        this.getContentPane().add(getNorthpanel(), BorderLayout.NORTH);//按钮导航区 
        this.getContentPane().add(getBody(), BorderLayout.CENTER); //设置主体
//		this.setVisible(true);
//		this.toFront();
    }

    /**
     * 获得模板数据
     * @return
     */
    public abstract String getTempletcode(); //

    /**
     * 获得导航列表
     * @return
     */
    public String[] getSys_Selection_Path(){
    	return menu;
    }

    /**
     * 获得自定义面板类定义
     * @return
     */
    public String getCustomerpanel() {
        return customerpanel;
    }

    /**
     * 是否显示系统按钮
     * @return
     */
    public boolean isShowsystembutton() {
        return showsystembutton;
    }

    /**
     * 获得UI拦截器定义
     * @return
     */
    public String getUiinterceptor() {
        return uiinterceptor;
    }
    
    /**
     * 获得BS拦截器定义
     * @return
     */
    public String getBsinterceptor() {
        return bsinterceptor;
    }

    
    
    
    /**
     * 系统窗口大小
     *
     * @return Dimension
     */
    protected Dimension getTempletSize() {
    	return UIComponentUtil.getInternalFrameDefaultSize();
    }
    
    /**
     * 获得窗口显示标题
     * 首先，获得标题：如果有菜单导航数据则取最末级菜单名，否则取主元模板名。
     * 然后，把获得的标题与导航数据合并起来。
     * @return String
     */
    protected String getTempletTitle() {
        return (menu==null)?str_templetecode:(menu[menu.length - 1]+" ["+getNavigation()+"]");        
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
     * 系统表格主体，包括按钮与表格
     *
     * @return JPanel
     */
    protected JPanel getBody() {
        JPanel rpanel = new JPanel();
        rpanel.setLayout(new BorderLayout());
        JPanel bodyp = new JPanel();
        bodyp.setLayout(new BorderLayout());
        bodyp.add(getTablePanel(), BorderLayout.CENTER);    //数据列表面板
        bodyp.add(getQueryPanel(), BorderLayout.NORTH);     //快速查询面板
        rpanel.add(bodyp, BorderLayout.CENTER);             //主显示区域
        
        return rpanel;
    }

    /**
     * 获取表格所在的BillListPanel
     *
     * @return BillListPanel
     */
    public BillListPanel getTablePanel() {
        if (mainBillListPanel == null) {
            mainBillListPanel = new BillListPanel(str_templetecode, true, false);
            mainBillListPanel.initialize();
        }
        return mainBillListPanel;
    }
    

    

    /**
     * 获取表格
     *
     * @return JTable
     */
    public JTable getTable() {
        return mainBillListPanel.getTable();
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
     * 获取按钮所在的面板，包括系统按钮面板与用户自定义面板
     *
     * @return JPanel
     */
    protected JComponent getNorthpanel() {
        JToolBar tbar = UIComponentUtil.buildToolBar(new JComponent[]{new JLabel("操作："),btn_quick,btn_insert,btn_delete,btn_save,btn_Search}, null);
        tbar.setFloatable(false);
        tbar.setBorderPainted(false);
        
        if (!showsystembutton) {
            btn_insert.setVisible(false);
            btn_delete.setVisible(false);
            btn_save.setVisible(false);
            btn_Search.setVisible(false);
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
     * 设置定义的BS拦截器类名
     *
     * @return String
     */

    public void setBsinterceptor(String bsinterceptor) {
        this.bsinterceptor = bsinterceptor;
    }

    /**
     * 设置定义的UI拦截器类名
     *
     * @return String
     */
    public void setUiinterceptor(String uiinterceptor) {
        this.uiinterceptor = uiinterceptor;
    }
    
    
    

    protected void onInsert() {
        // 执行拦截器操作!!
        if (uiIntercept != null) {
            try {
                uiIntercept.actionBeforeInsert(mainBillListPanel, mainBillListPanel.getSelectedRow());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage()); //
                return;
            }
        }
        mainBillListPanel.newRow();
        if (mainBillListPanel.containsItemKey("VERSION")) {
            mainBillListPanel.setValueAt(new Integer(1).toString(), mainBillListPanel.getSelectedRow(), "VERSION");
        }
    }

    protected void onDelete() {
        if (mainBillListPanel.getTable().getSelectedRowCount() != 1) {
            NovaMessage.show(this, "请选择一条要删除的记录!");
            return;
        }
        if (!NovaMessage.confirm(this, "你确定要删除吗?")) {
            return;
        }

        if (uiIntercept != null) {
            try {
                uiIntercept.actionBeforeDelete(mainBillListPanel, mainBillListPanel.getSelectedRow());
            } catch (Exception e) {
                //e.printStackTrace();
                NovaMessage.show(this, "错误：" + e.getMessage(), NovaConstants.MESSAGE_ERROR);
                return;
            }
        }

        mainBillListPanel.removeRow();
    }

    protected void onSave() {
        mainBillListPanel.stopEditing();

        if (!mainBillListPanel.checkValidate()) { //校验
            return;
        }

        //HashMap BillVOMap = new HashMap();
        try {
            BillVO[] insertvo = mainBillListPanel.getInsertBillVOs();
            BillVO[] updatevo = mainBillListPanel.getUpdateBillVOs();
            BillVO[] deletevo = mainBillListPanel.getDeleteBillVOs();
            // 执行提交前拦截.
            if (this.uiIntercept != null) {
                try {
                    uiIntercept.dealBeforeCommit(mainBillListPanel, insertvo, deletevo, updatevo);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    NovaMessage.show(this, ex.getMessage(), NovaConstants.MESSAGE_WARN);
                    return;
                }
            }

            //真正远程提交数据库!!!!
            HashMap returnMap = null;
            try
            {
            	for (int i = 0; i < updatevo.length; i++) {
					updatevo[i].updateVersion();
				}
            	returnMap = getService().style01_dealCommit(mainBillListPanel.getDataSourceName(), getBsinterceptor(),
                insertvo, deletevo, updatevo); //真正远程访问!!
            	
            }catch(Exception e)
            {
            	NovaMessage.showException(this, e);
            	return;
            }
            //version增加1
            mainBillListPanel.updateVersion();
            mainBillListPanel.clearDeleteBillVOs();
            if(returnMap==null)
            	return;
            BillVO[] returnInsertVOs = (BillVO[]) returnMap.get("INSERT"); //...
            BillVO[] returnDeleteVOs = (BillVO[]) returnMap.get("DELETE"); //...
            BillVO[] returnUpdateVOs = (BillVO[]) returnMap.get("UPDATE"); //...
            // 执行提交后拦截.
            if (this.uiIntercept != null) {
                uiIntercept.dealAfterCommit(mainBillListPanel, returnInsertVOs, returnDeleteVOs, returnUpdateVOs);
            }
            mainBillListPanel.stopEditing();
            mainBillListPanel.setAllRowStatusAs("INIT");
            NovaMessage.show(this, NovaConstants.STRING_OPERATION_SUCCESS); //
        } catch (Exception e) {
            NovaMessage.show(e.getMessage(), NovaConstants.MESSAGE_ERROR);
            e.printStackTrace();
        }
    }

    /**
     * 列表变化会调用这里
     */
    public void onValueChanged(NovaEvent _evt) {
        if (_evt.getChangedType() == NovaEvent.ListChanged) {
            if (uiIntercept != null) {
                BillListPanel list_temp = (BillListPanel) _evt.getSource(); //
                String tmp_itemkey = _evt.getItemKey(); //
                try {
                    uiIntercept.actionBeforeUpdate(list_temp, list_temp.getSelectedRow(), tmp_itemkey);
                } catch (Exception e) {
                    if (!e.getMessage().trim().equals("")) {
                        JOptionPane.showMessageDialog(this, e.getMessage()); //
                    }
                }
            }
        }
    }

    // 快速查询
    protected JPanel getQueryPanel() {
        if (querypanel != null) {
            return querypanel;
        }
        querypanel = new QuickQueryActionPanel(mainBillListPanel);
        return querypanel;
    }

    protected void onQuickSearch() {
        mainBillListPanel.stopEditing(); //
        mainBillListPanel.QueryDataByCondition(" 1=1 ");
    }
    

    protected void onSearch(String _sql) {
        mainBillListPanel.stopEditing(); //
        mainBillListPanel.QueryDataByCondition(_sql);
    }
    

    protected void onRefreshCurrPage() {
        mainBillListPanel.refreshCurrData();
    }

    
    
    
    
    
    public Object[] getNagivationPath() {
        return menu;
    }

    public String getTempletCode() {
        return this.str_templetecode;
    }

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

    //	public void showQuickSearchButton(boolean isshow) {
    //		this.btn_quicksearch.setVisible(isshow);
    //	}
    //
    //	public void showRefreshButton(boolean isshow) {
    //		this.btn_refresh_currpage.setVisible(isshow);
    //	}

    public void setInsertButtonText(String text) {
        this.btn_insert.setText(text);
    }

    public void setDeleteButtonText(String text) {
        this.btn_delete.setText(text);
    }

    public void setSearchButtonText(String text) {
        this.btn_Search.setText(text);
    }

    //	public void setQuickSearchButtonText(String text) {
    //		this.btn_quicksearch.setText(text);
    //	}
    //
    //	public void setRefreshButtonText(String text) {
    //		this.btn_refresh_currpage.setText(text);
    //	}

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

    //	public JButton getQuickSearchButton() {
    //		return this.btn_quicksearch;
    //	}
    //
    //	public JButton getRefreshButton() {
    //		return this.btn_refresh_currpage;
    //	}

    public JButton getSaveButton() {
        return this.btn_save;
    }
    
        
        
        
        
}
/**************************************************************************
 * $RCSfile: AbstractTempletFrame01.java,v $  $Revision: 1.8.6.6 $  $Date: 2010/01/13 02:48:43 $
 *
 * $Log: AbstractTempletFrame01.java,v $
 * Revision 1.8.6.6  2010/01/13 02:48:43  wangqi
 * *** empty log message ***
 *
 * Revision 1.8.6.5  2009/12/29 00:53:41  wangqi
 * *** empty log message ***
 *
 * Revision 1.8.6.4  2009/12/21 02:57:33  wangqi
 * *** empty log message ***
 *
 * Revision 1.8.6.3  2009/12/16 06:51:39  wangqi
 * *** empty log message ***
 *
 * Revision 1.8.6.2  2009/12/16 04:13:50  wangqi
 * *** empty log message ***
 *
 * Revision 1.8.6.1  2009/12/03 06:26:35  wangqi
 * *** empty log message ***
 *
 * Revision 1.8  2007/07/23 10:59:00  sunxf
 * Nova 20-14    平台层面更新数据的地方提供乐观锁控制，解决并发访问问题
 *
 * Revision 1.7  2007/07/02 02:37:34  qilin
 * 去掉系统状态栏
 *
 *
 **************************************************************************/
