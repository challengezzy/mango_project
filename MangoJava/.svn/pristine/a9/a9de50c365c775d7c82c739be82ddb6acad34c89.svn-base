/**************************************************************************
 * $RCSfile: AbstractTempletFrame04.java,v $  $Revision: 1.9.6.5 $  $Date: 2010/01/13 02:48:43 $
 **************************************************************************/
package smartx.publics.styletemplet.ui.templet04;

import java.util.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import smartx.framework.common.ui.*;
import smartx.framework.common.utils.Sys;
import smartx.framework.common.vo.*;
import smartx.framework.metadata.ui.*;
import smartx.framework.metadata.ui.componentscard.*;
import smartx.framework.metadata.util.UIComponentUtil;
import smartx.framework.metadata.vo.*;
import smartx.publics.styletemplet.ui.*;


/**
 * 风格模板04：双表树列
 * @author James.W
 *
 */
public abstract class AbstractTempletFrame04 extends AbstractStyleFrame implements NovaEventListener {
    protected String str_treetempletecode = null;

    protected String str_treetitle = null;

    protected String str_treesql = null;

    protected String str_treepk = null;

    protected String str_treeparentpk = null;

    protected String str_tablecode = null;

    protected String str_treefield = null;

    protected String str_tablefield = null;

    protected String customerpanel = null;

    protected AbstractCustomerButtonBarPanel panel_customer = null;

    protected String[] menu = null;

    private BillListPanel list = null;

    protected String uiinterceptor = null;

    protected String bsinterceptor = null;

    protected DefaultMutableTreeNode curnode = null;

    private String str_wherecondition = null;

    protected JTree tree = null;

    private JScrollPane scroll = null;

    protected JButton btn_insert = new JButton("新增");
    protected JButton btn_delete = new JButton("删除");
    protected JButton btn_save = new JButton("保存");
    protected JButton btn_Search = new JButton("查询");
    protected JButton btn_refresh_currpage = new JButton("刷新本页");

    protected JPanel navigationpanel = new JPanel();

    protected QuickQueryActionPanel querypanel = null;

    private boolean showsystembutton = true;

    private IUIIntercept_04 uiIntercept = null; // ui端拦截器

    public AbstractTempletFrame04() {
        super();
        init(); //
    }

    public AbstractTempletFrame04(String _title) {
        super(_title);
    }

    public abstract String getTreeSQL();

    public abstract String getTreePrimarykey();

    public abstract String getTreeParentkey();

    public String[] getSys_Selection_Path() {
        return menu;
    }

    public String getCustomerpanel() {
        return customerpanel;
    }

    public String getTreetitle() {
        return "根结点";
    }

    public boolean isShowsystembutton() {
        return showsystembutton;
    }

    public abstract String getTreeTempeltCode();

    public abstract String getTableTempletCode();

    // 表与树关联的外键
    public abstract String getTableFK();

    // 树中与列表相关联的字段。
    public abstract String getTreeAssocField();

    protected void init() {
        str_treetempletecode = getTreeTempeltCode();
        str_treetitle = this.getTreetitle();
        str_treesql = getTreeSQL();
        str_treepk = getTreePrimarykey();
        str_treefield = getTreeAssocField();
        str_treeparentpk = getTreeParentkey();
        str_tablecode = getTableTempletCode();
        str_tablefield = getTableFK();
        menu = getSys_Selection_Path();
        customerpanel = getCustomerpanel();
        uiinterceptor = getUiinterceptor();
        bsinterceptor = getBsinterceptor();
        initVars();
        if (getUiinterceptor() != null && !getUiinterceptor().trim().equals("")) {
            try {
                uiIntercept = (IUIIntercept_04) Class.forName(getUiinterceptor().trim()).newInstance(); //
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.setTitle(getTempletTitle());
        this.setSize(getTempletSize());
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(getMainPanel(), BorderLayout.CENTER);
        list.addNovaEventListener(this); // 注册自己事件监听!!
    }

    protected JPanel getMainPanel() {
        JPanel rpanel = new JPanel();
        JSplitPane jsp = new JSplitPane();
        jsp.setOneTouchExpandable(true);
        jsp.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        jsp.setDividerLocation(200);
        jsp.setRightComponent(getRightPanel());
        jsp.setLeftComponent(getTreePanel());
        rpanel.setLayout(new BorderLayout());
        rpanel.add(jsp, BorderLayout.CENTER);
        return rpanel;
    }

    protected JPanel getRightPanel() {
        JPanel rpanel = new JPanel();
        rpanel.setLayout(new BorderLayout());
        JPanel bodyp = new JPanel();
        bodyp.setLayout(new BorderLayout());
        bodyp.add(getTablePanel(), BorderLayout.CENTER);
        bodyp.add(getQueryPanel(), BorderLayout.NORTH);
        rpanel.add(bodyp, BorderLayout.CENTER);
        rpanel.add(getSysBtnPanel(), BorderLayout.NORTH);
        return rpanel;
    }

    // 快速查询
    protected JPanel getQueryPanel() {
        if (querypanel != null) {
            return querypanel;
        }
        querypanel = new QuickQueryActionPanel(list);
        return querypanel;
    }

    /**
     * 获得窗口显示标题
     * 首先，获得标题：如果有菜单导航数据则取最末级菜单名，否则取主元模板名。
     * 然后，把获得的标题与导航数据合并起来。
     * @return String
     */
    protected String getTempletTitle() {
        return (menu==null)?str_tablecode:(menu[menu.length - 1]+" ["+getNavigation()+"]");        
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
     * 获取树标题
     *
     * @return String
     */
    public String getTreeTitle() {
        return this.str_treetitle;
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
     * 系统窗口大小
     *
     * @return Dimension
     */
    protected Dimension getTempletSize() {
    	return UIComponentUtil.getInternalFrameDefaultSize();
    }

    /**
     * 获取系统按钮面板
     *
     * @return Jpanel
     */
    protected JComponent getSysBtnPanel() {
    	JToolBar tbar = new JToolBar();
        tbar.setFloatable(false);
        tbar.setBorderPainted(false);
        tbar.add(new JLabel("操作："));
    	
        if (showsystembutton) {
            getBtnPanel(tbar);
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
     * 获取面板，包括系统按钮面板和用户自定义按钮面板
     *
     * @return JPanel
     */
    protected void getBtnPanel(JToolBar tbar) {
        
        btn_insert.setPreferredSize(UIComponentUtil.getButtonDefaultSize());
        btn_insert.setFocusPainted(false);
        btn_delete.setPreferredSize(UIComponentUtil.getButtonDefaultSize());
        btn_delete.setFocusPainted(false);
        btn_save.setPreferredSize(UIComponentUtil.getButtonDefaultSize());
        btn_save.setFocusPainted(false);
        btn_Search.setPreferredSize(UIComponentUtil.getButtonDefaultSize());
        btn_Search.setFocusPainted(false);
        btn_refresh_currpage.setPreferredSize(UIComponentUtil.getButtonDefaultSize());
        btn_refresh_currpage.setFocusPainted(false);
        
        
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

        btn_save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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
//		list.getTable().editingStopped(new ChangeEvent(list.getTable()));
        int rownum = list.newRow();
        if (curnode != null) {
            HashVO vo = (HashVO) curnode.getUserObject();
            String str_id = vo.getStringValue(str_treefield);
            list.setValueAt(str_id, rownum, str_tablefield);
        }
        list.setValueAt(new Integer(1).toString(), list.getSelectedRow(), "VERSION");
        // 执行拦截器操作!!
        if (uiIntercept != null) {
            try {
                uiIntercept.actionAfterInsert(list, rownum); // 执行新增后的动作!!
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void onSave() {
        list.stopEditing();
        if (!list.checkValidate()) { //校验
            return;
        }
        try {
            BillVO[] insertvo = list.getInsertBillVOs();
            BillVO[] updatevo = list.getUpdateBillVOs();
            BillVO[] deletevo = list.getDeleteBillVOs();
            if (this.uiIntercept != null) {
                try {
                    uiIntercept.dealBeforeCommit(list, insertvo, deletevo, updatevo);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return;
                }
            }
            for (int i = 0; i < updatevo.length; i++) {
				updatevo[i].updateVersion();
			}
            HashMap returnMap = getService().style04_dealCommit(list.getDataSourceName(), getBsinterceptor(), insertvo,
                deletevo, updatevo);
            list.updateVersion();
            list.clearDeleteBillVOs();
            for (int i = 0; i < list.getTable().getRowCount(); i++) {
                RowNumberItemVO itemvo = (RowNumberItemVO) list.getValueAt(i, "_RECORD_ROW_NUMBER");
                if (itemvo.getState().equals("UPDATE")) {
                    //int version = new Integer(list.getValueAt(i, "VERSION").toString()).intValue() + 1;
                    int version = list.getValueAt(i, "VERSION")==null?1:(new Integer(list.getValueAt(i, "VERSION").toString()).intValue() + 1);
                    list.setValueAt(new Integer(version).toString(), i, "VERSION");
                }
            }
            if (this.uiIntercept != null) {
                try {
                    uiIntercept.dealAfterCommit(list, (BillVO[]) returnMap.get("INSERT"),
                                                (BillVO[]) returnMap.get("DELETE"), (BillVO[]) returnMap.get("UPDATE"));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            NovaMessage.show(AbstractTempletFrame04.this, NovaConstants.STRING_OPERATION_SUCCESS);
            list.setAllRowStatusAs("INIT");
        } catch (Exception e) {
            NovaMessage.show(e.getMessage(), NovaConstants.MESSAGE_ERROR);
            e.printStackTrace();
            return;
        }
    }

    protected void onDelete() {
        int li_row = list.getTable().getSelectedRow(); // 取得选中的行!!
        if (list.getTable().getSelectedRowCount() != 1) {
            NovaMessage.show(NovaConstants.STRING_DEL_SELECTION_NEED, NovaConstants.MESSAGE_ERROR);
            return;
        }
        list.getTable().editingStopped(new ChangeEvent(list.getTable()));
        if (NovaMessage.confirm(NovaConstants.STRING_DEL_CONFIRM)) {
            // 执行拦截器删除前操作!!
            if (uiIntercept != null) {
                try {
                    uiIntercept.actionBeforeDelete(list, li_row); // 执行删除前的动作!!
                } catch (Exception e) {
                    if (!e.getMessage().trim().equals("")) {
                        JOptionPane.showMessageDialog(this, e.getMessage()); //
                    }
                    return; // 不往下走了!!
                }
            }
			try {
				this.getService().style04_dealDelete(list.getDataSourceName(), null, list.getBillVO(li_row));
			} catch (Exception e) {
				e.printStackTrace();
			}
            list.removeRow();
        }
    };

    /**
     * 卡片会调用这里
     */
    public void onValueChanged(NovaEvent _evt) {
        if (_evt.getChangedType() == NovaEvent.ListChanged) { //如果是列表变化!!
            if (uiIntercept != null) {
                BillListPanel card_tmp = (BillListPanel) _evt.getSource(); //
                String tmp_itemkey = _evt.getItemKey(); //
                try {
                    uiIntercept.actionAfterUpdate(card_tmp, list.getSelectedRow(), tmp_itemkey);
                } catch (Exception e) {
                    if (!e.getMessage().trim().equals("")) {
                        JOptionPane.showMessageDialog(this, e.getMessage()); //
                    }
                }
            }
        } else if (_evt.getChangedType() == NovaEvent.CardChanged) { // 如果是卡片变化

        }
    }

    protected void onRefresh() {
        if (tree.getSelectionCount() == 1) {
            list.getTable().editingStopped(new ChangeEvent(list.getTable()));
            QueryDialog queryDialog = new QueryDialog(AbstractTempletFrame04.this, list.getTempletVO());
            if (queryDialog.getStr_return_sql() != null && queryDialog.getStr_return_sql().length() > 0) {
                if (!str_wherecondition.equals("")) {
                    list.QueryDataByCondition(queryDialog.getStr_return_sql() + " and " + str_wherecondition);
                } else {
                    list.QueryDataByCondition(queryDialog.getStr_return_sql());
                }
            }
        }
    }

    protected void onRefreshCurrPage() {
        list.getTable().editingStopped(new ChangeEvent(list.getTable()));
        list.refreshCurrData();
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

    /**
     * 获取树控件的面板
     *
     * @return JPanel
     */
    protected JPanel getTreePanel() {
        JPanel rpanel = new JPanel();
        rpanel.setLayout(new BorderLayout());
        tree = UIUtil.getJTreeByParentPK_HashVO(list.getDataSourceName(), str_treetitle, str_treesql, str_treepk,
                                                str_treeparentpk);
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent evt) {
                TreePath[] paths = evt.getPaths();
                for (int i = 0; i < paths.length; i++) {
                    if (evt.isAddedPath(i)) {
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) paths[i].getLastPathComponent();
                        onChangeSelectTree(node);
                    } else {
                    }
                }
            }
        });
        scroll = new JScrollPane(tree);
        rpanel.add(getTreeButtonPanel(), BorderLayout.NORTH);
        rpanel.add(scroll, BorderLayout.CENTER);
        return rpanel;
    }

    /**
     * 选择树结点事件
     *
     * @param _node
     */
    protected void onChangeSelectTree(DefaultMutableTreeNode _node) {
        if (!_node.isRoot()) {

            curnode = _node;
            HashVO vo = (HashVO) _node.getUserObject();
            String str_id = vo.getStringValue(str_treefield);
            str_wherecondition = str_tablefield + "='" + str_id + "'";
            String str_sql = list.getSQL(str_wherecondition);
            list.QueryData(str_sql);
        }
    }

    /**
     * 树上方按钮面板
     *
     * @return JPanel
     */
    protected JPanel getTreeButtonPanel() {
        JPanel panel_btn = new JPanel();
        panel_btn.setLayout(new FlowLayout());

        JButton btn_refresh = new JButton("刷新");
        btn_refresh.setPreferredSize(new Dimension(60, 20));
        btn_refresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onTreeRefresh();
            }
        });

        panel_btn.add(btn_refresh);

        return panel_btn;
    }

    /**
     * 刷新树
     *
     */
    public void onTreeRefresh() {
        list.clearTable();
        tree = UIUtil.getJTreeByParentPK_HashVO(list.getDataSourceName(), str_treetitle, str_treesql, str_treepk,
                                                str_treeparentpk);
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent evt) {
                TreePath[] paths = evt.getPaths();
                for (int i = 0; i < paths.length; i++) {
                    if (evt.isAddedPath(i)) {
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) paths[i].getLastPathComponent();
                        onChangeSelectTree(node);
                    } else {
                    }
                }
            }
        });
        scroll.getViewport().removeAll();
        scroll.getViewport().add(tree);
        scroll.updateUI();
    }

    /**
     * 初始化本地变量
     */
    protected void initVars() {
    }

    /**
     * 获取树
     *
     * @return JTree
     */
    public JTree getTree() {
        return this.tree;
    }

    /**
     * 获取表格所在的面板
     *
     * @return
     */
    protected JPanel getTablePanel() {
        JPanel rpanel = new JPanel();
        rpanel.setLayout(new BorderLayout());
        if (list == null) {
            list = new BillListPanel(str_tablecode, true, false);
            list.addNovaEventListener(this);
            list.initialize();
        }
        rpanel.add(list, BorderLayout.CENTER);
        return rpanel;
    }

    /**
     * 获取BillListPanel
     *
     * @return BillListPanel
     */
    public BillListPanel getBillListPanel() {
        return this.list;
    }

    /**
     * 获取BillListPanel中的列表
     *
     * @return
     */
    public JTable getTable() {
        return list.getTable();
    }

    public void setNavigationVisible(boolean isshow) {
        this.navigationpanel.setVisible(isshow);
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
        this.btn_refresh_currpage.setVisible(isshow);
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
        this.btn_refresh_currpage.setText(text);
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
        return this.btn_refresh_currpage;
    }

    public JButton getSaveButton() {
        return this.btn_save;
    }

}
/**************************************************************************
 * $RCSfile: AbstractTempletFrame04.java,v $  $Revision: 1.9.6.5 $  $Date: 2010/01/13 02:48:43 $
 *
 * $Log: AbstractTempletFrame04.java,v $
 * Revision 1.9.6.5  2010/01/13 02:48:43  wangqi
 * *** empty log message ***
 *
 * Revision 1.9.6.4  2009/12/21 02:57:33  wangqi
 * *** empty log message ***
 *
 * Revision 1.9.6.3  2009/12/16 05:34:11  wangqi
 * *** empty log message ***
 *
 * Revision 1.9.6.2  2009/12/16 04:13:50  wangqi
 * *** empty log message ***
 *
 * Revision 1.9.6.1  2008/09/26 06:57:24  wangqi
 * *** empty log message ***
 *
 * Revision 1.9  2007/07/23 10:58:59  sunxf
 * Nova 20-14    平台层面更新数据的地方提供乐观锁控制，解决并发访问问题
 *
 * Revision 1.8  2007/07/04 01:38:52  qilin
 * 去掉系统状态栏
 *
 * Revision 1.7  2007/07/02 02:18:46  qilin
 * 去掉系统状态栏
 *
 * Revision 1.6  2007/05/31 11:32:22  sunxb
 * *** empty log message ***
 *
 * Revision 1.5  2007/05/31 07:39:03  qilin
 * code format
 *
 * Revision 1.3  2007/05/23 03:29:04  qilin
 * no message
 *
 * Revision 1.2  2007/05/21 11:59:08  sunxb
 * *** empty log message ***
 *
 * Revision 1.1  2007/05/17 06:14:27  qilin
 * no message
 *
 * Revision 1.9  2007/04/02 03:45:41  shxch
 * *** empty log message ***
 *
 * Revision 1.8  2007/03/30 10:09:53  sunxb
 * *** empty log message ***
 *
 * Revision 1.7  2007/03/29 08:36:07  sunxb
 * *** empty log message ***
 *
 * Revision 1.6  2007/03/29 08:32:44  sunxb
 * *** empty log message ***
 *
 * Revision 1.5  2007/03/27 08:01:28  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/03/09 01:16:03  sunxf
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
 * Revision 1.6  2007/02/27 06:03:02  shxch
 * *** empty log message ***
 *
 * Revision 1.5  2007/02/10 08:59:36  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/02/05 05:08:19  lujian
 * *** empty log message ***
 *
 * Revision 1.3  2007/02/02 04:37:13  lujian
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:48:31  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
