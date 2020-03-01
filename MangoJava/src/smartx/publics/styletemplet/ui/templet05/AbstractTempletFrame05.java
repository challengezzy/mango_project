/**************************************************************************
 * $RCSfile: AbstractTempletFrame05.java,v $  $Revision: 1.8.6.6 $  $Date: 2010/01/13 02:48:43 $
 **************************************************************************/
package smartx.publics.styletemplet.ui.templet05;

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
 * 风格模板05：双表树列/卡
 * @author James.W
 *
 */
public abstract class AbstractTempletFrame05 extends AbstractStyleFrame implements NovaEventListener {
    protected String str_treetempletecode = null;

    protected String str_treetitle = null;

    protected String str_treesql = null;

    protected String str_treepk = null;

    protected String str_treeparentpk = null;

    protected String str_tablecode = null;

    protected String str_treefield = null;

    protected String str_tablefield = null;

    protected CardLayout cardlayout = null;

    protected JPanel right = null;

    protected BillListPanel list = null;

    protected BillCardPanel card = null;

    protected String customerpanel = null;

    protected AbstractCustomerButtonBarPanel panel_customer = null;

    protected String[] menu = null;

    protected String uiinterceptor = "";

    protected String bsinterceptor = "";

    protected String str_wherecondition = "";

    protected DefaultMutableTreeNode curnode = null;

    protected int status = -1;

    protected boolean enablesave = false;

    protected JButton btn_quick = new JButton(Sys.getSysRes("edit.simplequery.msg"), UIUtil.getImage(Sys.getSysRes("edit.simplequery.icon")));
    protected JButton btn_insert = new JButton(Sys.getSysRes("edit.new.msg"), UIUtil.getImage(Sys.getSysRes("edit.new.icon")));
    protected JButton btn_delete = new JButton(Sys.getSysRes("edit.delete.msg"), UIUtil.getImage(Sys.getSysRes("edit.delete.icon")));
    protected JButton btn_edit = new JButton(Sys.getSysRes("edit.edit.msg"), UIUtil.getImage(Sys.getSysRes("edit.edit.icon")));
    protected JButton btn_refresh = new JButton("刷新");
    protected JButton btn_Search = new JButton(Sys.getSysRes("edit.complexquery.msg"), UIUtil.getImage(Sys.getSysRes("edit.complexquery.icon")));
    protected JButton btn_save = new JButton(Sys.getSysRes("edit.save.msg"), UIUtil.getImage(Sys.getSysRes("edit.save.icon")));
    protected JButton btn_save_return = new JButton("保存并返回", UIUtil.getImage(Sys.getSysRes("edit.save.icon")));
    protected JButton btn_cancel_return = new JButton("放弃并返回");
    protected JButton btn_switch = new JButton(Sys.getSysRes("edit.view.msg"), UIUtil.getImage(Sys.getSysRes("edit.view.icon")));
    
    protected JTree tree = null;

    protected JScrollPane scroll = null;

    protected JPanel navigationpanel = new JPanel();

    protected String returntotable = "切换列表";

    protected String returntocard = "切换卡片";

    protected QuickQueryActionPanel querypanel = null;

    protected boolean showsystembutton = true;

    protected IUIIntercept_05 uiIntercept = null; // ui端拦截器
    protected int currentShowPos = -1;
    protected JTextField jtf_search = null;
    protected ArrayList treePath = null;

    protected TreePath path;
    public AbstractTempletFrame05() {
        super();
        init(); //
    }

    public AbstractTempletFrame05(String _title) {
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

    public abstract String getTreeTempeltCode();

    public abstract String getTableTempletCode();

    // 表与树关联的外键
    public abstract String getTableFK();

    // 树中与列表相关联的字段。
    public abstract String getTreeAssocField();

    /**
     * 完成初始化.
     */
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
        showsystembutton = isShowsystembutton();
        if (getUiinterceptor() != null && !getUiinterceptor().trim().equals("")) {
            try {
                uiIntercept = (IUIIntercept_05) Class.forName(getUiinterceptor().trim()).newInstance(); //
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.setTitle(getTempletTitle());
        this.setSize(getTempletSize());
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(getMainPanel(), BorderLayout.CENTER);
        list.addNovaEventListener(this); // 注册自己事件监听!!
        card.addNovaEventListener(this); // 注册自己事件监听!!
    }

    private JPanel getMainPanel() {
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

    private JPanel getRightPanel() {
        JPanel rpanel = new JPanel();
        rpanel.setLayout(new BorderLayout());

        JPanel bodyp = new JPanel();
        bodyp.setLayout(new BorderLayout());
        bodyp.add(getTablePanel(), BorderLayout.CENTER);
        bodyp.add(getQueryPanel(), BorderLayout.NORTH);

        rpanel.add(bodyp, BorderLayout.CENTER);
        rpanel.add(getBtnpanel(), BorderLayout.NORTH);
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

    private JPanel getTablePanel() {
        cardlayout = new CardLayout();
        right = new JPanel();
        right.setLayout(cardlayout);
        if (list == null) {
            list = new BillListPanel(str_tablecode, true, false);
            list.setAllItemValue("listiseditable", NovaConstants.BILLCOMPENTEDITABLE_NONE);
            list.initialize();
        }

        card = new BillCardPanel(list.getTempletVO());
        right.add(list, "list");
        right.add(card, "card");
        return right;
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
     * 获取窗口标题
     *
     * @return String
     */
    protected String getTreeTitle() {
        return this.str_treetitle;
    }

    public boolean isShowsystembutton() {
        return showsystembutton;
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
     * 获取面板，包括系统按钮面板和用户自定义按钮面板
     *
     * @return JPanel
     */
    private JComponent getBtnpanel() {
    	JToolBar tbar = new JToolBar();
    	tbar.setFloatable(false);
        tbar.setBorderPainted(false);
        tbar.add(new JLabel("操作："));

        btn_quick.setPreferredSize(UIComponentUtil.getButtonDefaultSize());
        btn_quick.setFocusPainted(true);        
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
     * 获取表格
     *
     * @return JTable
     */
    public JTable getTable() {
        return list.getTable();
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

    public String getCustomerPanelNames() {
        return this.customerpanel;
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
        btn_save.setVisible(false);
        btn_save.setPreferredSize(UIComponentUtil.getButtonDefaultSize());
        btn_save.setFocusPainted(false);
        btn_switch.setPreferredSize(UIComponentUtil.getButtonDefaultSize());
        btn_switch.setVisible(false);
        btn_switch.setFocusPainted(false);
        btn_save_return.setVisible(false);
        btn_save_return.setPreferredSize(UIComponentUtil.getButtonDefaultSize());
        btn_save_return.setFocusPainted(false);
        btn_cancel_return.setVisible(false);
        btn_cancel_return.setPreferredSize(UIComponentUtil.getButtonDefaultSize());
        btn_cancel_return.setFocusPainted(false);
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

    protected void onInsert() {
        if (curnode == null) {
            NovaMessage.show(this, "请选择所属目录", NovaConstants.MESSAGE_ERROR);
            return;
        }
        list.getTable().editingStopped(new ChangeEvent(list.getTable()));
        HashMap map = new HashMap();
        HashVO vo = (HashVO) curnode.getUserObject();
        String str_id = vo.getStringValue(str_treefield);
        map.put(str_tablefield, str_id);
        onSwitch();
        card.createNewRecord();
        INovaCompent cardfield = card.getCompentByKey(str_tablefield);
        if (cardfield instanceof UIRefPanel) {
            cardfield = (UIRefPanel) cardfield;
            RefItemVO itemvo = new RefItemVO(str_id, vo.getStringValue(1), vo.getStringValue(2));
            cardfield.setObject(itemvo);
        } else {
            card.setCompentObjectValue(str_tablefield, str_id);
        }
        // 执行拦截器操作!!
        if (uiIntercept != null) {
            try {
                uiIntercept.actionAfterInsert(card); // 执行新增后的动作!!
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void onEdit() {
        int li_row = list.getTable().getSelectedRow(); // 取得选中的行!!
        if (li_row < 0) {
            NovaMessage.show(this, "请选择要修改的记录");
            return;
        }
        try {
            if (list.getTable().getSelectedColumnCount() != 1) {
                NovaMessage.show(AbstractTempletFrame05.this, "请选择一条记录", NovaConstants.MESSAGE_ERROR);
            } else {
                onSwitch(); //
                card.setValue(list.getValueAtRowWithHashMap(list.getSelectedRow()));
                card.setRowNumberItemVO( (RowNumberItemVO) list.getValueAt(list.getSelectedRow(), 0)); // 设置行号
                card.setEditState(NovaConstants.BILLDATAEDITSTATE_UPDATE);
            }
        } catch (Exception ex) {
            NovaMessage.show(AbstractTempletFrame05.this, NovaConstants.STRING_OPERATION_FAILED + ":" + ex.getMessage(),
                             NovaConstants.MESSAGE_ERROR);
        }
    }

    protected void onDelete() {
        int li_row = list.getTable().getSelectedRow(); // 取得选中的行!!
        if (li_row < 0) {
            NovaMessage.show(NovaConstants.STRING_DEL_SELECTION_NEED, NovaConstants.MESSAGE_ERROR);
            return;
        }
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
        // 提交删除数据!!!
        try {
            BillVO vo = list.getBillVO(li_row); //
            dealDelete(vo); // 真正删除
            list.removeRow(li_row); // 如果成功
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
            } catch (Exception e) {
                if (!e.getMessage().trim().equals("")) {
                    JOptionPane.showMessageDialog(this, e.getMessage()); //
                }
                return; // 不往下走了!!
            }
        }

        getService().style05_dealUpdate(list.getDataSourceName(), getBsinterceptor(), _deleteVO); //

        if (this.uiIntercept != null) {
            try {
                uiIntercept.dealCommitAfterDelete(this, _deleteVO);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
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

    public boolean onSave() {
        list.getTable().editingStopped(new ChangeEvent(list.getTable()));
        if (!list.checkValidate()) { //校验
            return false;
        }
        if (!card.checkValidate()) { //校验
            return false;
        }
        
        //更新显示的version值
        card.updateVersion();        
        BillVO vo = card.getBillVO();
        int _row=list.getSelectedRow();
        try {
        	if (card.getEditState() == NovaConstants.BILLDATAEDITSTATE_INSERT) { // 如果是新增提交
                dealInsert(vo); // 新增提交
                card.setEditState(NovaConstants.BILLDATAEDITSTATE_UPDATE);
                HashMap map = card.getAllObjectValuesWithHashMap();
                list.insertRowWithInitStatus(_row, map);            
        	} else if (card.getEditState() == NovaConstants.BILLDATAEDITSTATE_UPDATE) { // 如果是修改提交
                dealUpdate(vo); // 修改提交
                list.setValueAtRow(_row, vo);
                list.setRowStatusAs(_row, "INIT");
            } 
        	return true;
        } catch (Exception e1) {
            NovaMessage.show(e1.getMessage(), NovaConstants.MESSAGE_ERROR);
            return false;
        }
    }
    
    protected void dealInsert(BillVO _insertVO) throws Exception {
        // 执行新增提交前的拦截器
        if (this.uiIntercept != null) {
            uiIntercept.dealCommitBeforeInsert(this, _insertVO);            
        }
        BillVO returnVO = getService().style05_dealInsert(list.getDataSourceName(), getBsinterceptor(), _insertVO); // 直接提交数据库,这里可能抛异常!!
        // 执行新增提交前的拦截器
        if (this.uiIntercept != null) {
            uiIntercept.dealCommitAfterInsert(this, returnVO); //            
        }
    }

    protected void dealUpdate(BillVO _updateVO) throws Exception {
        if (this.uiIntercept != null) {
        	uiIntercept.dealCommitBeforeUpdate(this, _updateVO); // 修改提交前拦截器            
        }
        BillVO returnvo = getService().style05_dealUpdate(list.getDataSourceName(), getBsinterceptor(), _updateVO); //
        if (this.uiIntercept != null) {
            uiIntercept.dealCommitAfterUpdate(this, returnvo); //
        }
    }
    

    /**
     * 在列表和卡片间切换
     */
    protected void onSwitch() {
        enablesave = !enablesave;
        btn_quick.setVisible(!enablesave);
        btn_switch.setVisible(enablesave);
        btn_insert.setVisible(!enablesave);
        btn_edit.setVisible(!enablesave);
        btn_delete.setVisible(!enablesave);
        btn_refresh.setVisible(!enablesave);
        if (panel_customer != null) {
            panel_customer.setVisible(!enablesave);
        }
        if (querypanel != null) {
            querypanel.setVisible(!enablesave);
        }
        if (enablesave) {
            btn_switch.setText(returntotable);
        } else {
            if (!card.getEditState().equalsIgnoreCase(NovaConstants.BILLDATAEDITSTATE_INIT)) {
                btn_switch.setVisible(true);
                btn_switch.setText(returntocard);
            }
        }
        btn_save.setVisible(enablesave);
        btn_save_return.setVisible(enablesave);
        btn_cancel_return.setVisible(enablesave);
        cardlayout.next(right);
    }

    private void onQuery() {
        QueryDialog queryDialog = new QueryDialog(AbstractTempletFrame05.this, list.getTempletVO());
        if (queryDialog.getStr_return_sql() != null && queryDialog.getStr_return_sql().length() > 0) {
            if (!str_wherecondition.equals("")) {
                list.QueryDataByCondition(queryDialog.getStr_return_sql() + " and " + str_wherecondition);
            } else {
                list.QueryDataByCondition(queryDialog.getStr_return_sql());
            }
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

    private void onRefresh() {
        list.getTable().editingStopped(new ChangeEvent(list.getTable()));
        String str_sql = list.getSQL(str_wherecondition);
        list.QueryData(str_sql);
    }

    protected JPanel getTreeSearchPanel() {
        JPanel panel_btn = new JPanel();
        panel_btn.setLayout(new FlowLayout());
        JButton btn = new JButton("查询");
        btn.setPreferredSize(new Dimension(60, 20));
        btn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dealSearch(jtf_search.getText());
            }
        });
        btn.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F3) {
                    dealSearchNext();
                }
            }
        });
        jtf_search = new JTextField();
        jtf_search.setPreferredSize(new Dimension(90, 20));
        jtf_search.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                dealKeyPerform(e);
            }
        });
        panel_btn.add(jtf_search);
        panel_btn.add(btn);
        return panel_btn;
    }

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
        tree.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F3) {
                    dealSearchNext();
                }
            }
        });
        scroll = new JScrollPane(tree);
        rpanel.add(getTreeSearchPanel(), BorderLayout.NORTH);
        rpanel.add(scroll, BorderLayout.CENTER);
        return rpanel;
    }

    /**
     * 处理键盘事件
     *
     * @param e
     */
    protected void dealKeyPerform(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            dealSearch(jtf_search.getText());
        } else if (e.getKeyCode() == KeyEvent.VK_F3) {
            dealSearchNext();
        }
    }

    protected void dealSearchNext() {
        if (treePath == null || treePath.size() <= 0) {
            return;
        }
        if (currentShowPos == treePath.size() - 1) {
            currentShowPos = -1;
        }
        showTreeNode(currentShowPos + 1);

    }

    /**
     * 根据_name来处理搜索
     *
     * @param _name
     */
    private void dealSearch(String _name) {
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) tree.getModel().getRoot();
        Enumeration e = rootNode.preorderEnumeration();
        if (treePath == null) {
            treePath = new ArrayList();
        }
        treePath.clear();
        while (e.hasMoreElements()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
            if (node.isRoot()) {
                continue;
            }
            HashVO vo = (HashVO) node.getUserObject();
            String temp_name = vo.toString(); // 显示名称与查询条件比较
            int compareCount = temp_name.indexOf(_name.trim());
            if (compareCount == 0) {
                DefaultTreeModel model = (DefaultTreeModel) tree.getModel();

                TreeNode[] nodes = model.getPathToRoot(node);
                TreePath path = new TreePath(nodes);
                treePath.add(path);
            }
        }
        if (treePath.size() == 0) {
            JOptionPane.showMessageDialog(this, "没有检测到你要找的节点！");
        } else {
            showTreeNode(0);
        }
        return;
    }

    protected void showTreeNode(int pos) {
        if (pos < 0 || pos >= treePath.size()) {
            return;
        }
        tree.makeVisible( (TreePath) treePath.get(pos));
        tree.setSelectionPath( (TreePath) treePath.get(pos));
        tree.scrollPathToVisible( (TreePath) treePath.get(pos));
        currentShowPos = pos;
    }

    protected void onChangeSelectTree(DefaultMutableTreeNode _node) {
        if (!card.getEditState().equalsIgnoreCase(NovaConstants.BILLDATAEDITSTATE_INIT)) {
            if (NovaMessage.confirm(this, "当前正处于新增或编辑状态，确定终止当前任务?")) {
                btn_save.setEnabled(false);
                btn_switch.setVisible(false);
                card.reset();
                enablesave = false;
                cardlayout.show(right, "list");
            } else {
                return;
            }
        }
        curnode = _node;
        path = tree.getSelectionPath();
        if (!_node.isRoot()) {
            list.getTable().editingStopped(new ChangeEvent(list.getTable()));
//			curnode = _node;
            path = tree.getSelectionPath();
            HashVO vo = (HashVO) _node.getUserObject();
            String str_id = vo.getStringValue(str_treefield);
            String str_sql = null;
            if (str_wherecondition == null || str_wherecondition.equals("")) {
                str_sql = list.getSQL(str_tablefield + "='" + str_id + "'");
            } else {
                str_sql = list.getSQL(str_wherecondition + " and " + str_tablefield + "='" + str_id + "'");
            }
            list.QueryData(str_sql);
        }
    }

//	private JPanel getTreeButtonPanel() {
//		JPanel panel_btn = new JPanel();
//		panel_btn.setLayout(new FlowLayout());
//
//		JButton btn_refresh = new JButton("刷新");
//		btn_refresh.setPreferredSize(new Dimension(60, 20));
//		btn_refresh.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				onTreeRefresh();
//			}
//		});
//
//		panel_btn.add(btn_refresh);
//
//		return panel_btn;
//	}

    public void onTreeRefresh() {
        list.getTable().editingStopped(new ChangeEvent(list.getTable()));
        list.clearTable();
        tree = UIUtil.getJTreeByParentPK_HashVO(str_treetempletecode, str_treetitle, str_treesql, str_treepk,
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

    public AbstractCustomerButtonBarPanel getPanel_customer() {
        return panel_customer;
    }

    public void setPanel_customer(AbstractCustomerButtonBarPanel customerpanel) {
        this.panel_customer = customerpanel;
    }
}
/**************************************************************************
 * $RCSfile: AbstractTempletFrame05.java,v $  $Revision: 1.8.6.6 $  $Date: 2010/01/13 02:48:43 $
 *
 * $Log: AbstractTempletFrame05.java,v $
 * Revision 1.8.6.6  2010/01/13 02:48:43  wangqi
 * *** empty log message ***
 *
 * Revision 1.8.6.5  2010/01/11 05:17:00  wangqi
 * *** empty log message ***
 *
 * Revision 1.8.6.4  2009/12/30 02:24:32  wangqi
 * *** empty log message ***
 *
 * Revision 1.8.6.3  2009/12/21 02:57:33  wangqi
 * *** empty log message ***
 *
 * Revision 1.8.6.2  2009/12/16 05:34:11  wangqi
 * *** empty log message ***
 *
 * Revision 1.8.6.1  2009/12/16 04:13:50  wangqi
 * *** empty log message ***
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
 * Revision 1.5  2007/05/31 07:39:03  qilin
 * code format
 *
 * Revision 1.3  2007/05/23 03:29:04  qilin
 * no message
 *
 * Revision 1.2  2007/05/21 03:45:45  lst
 * MR#:NM30-0000
 *
 * Revision 1.1  2007/05/17 06:14:28  qilin
 * no message
 *
 * Revision 1.5  2007/04/03 03:37:24  sunxf
 * *** empty log message ***
 *
 * Revision 1.4  2007/03/27 08:01:28  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/03/27 07:55:20  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/03/08 10:53:19  shxch
 * *** empty log message ***
 *
 * Revision 1.1  2007/03/05 09:59:14  shxch
 * *** empty log message ***
 *
 * Revision 1.8  2007/02/27 06:03:00  shxch
 * *** empty log message ***
 *
 * Revision 1.7  2007/02/10 08:59:36  shxch
 * *** empty log message ***
 *
 * Revision 1.6  2007/02/07 02:43:09  lujian
 * *** empty log message ***
 *
 * Revision 1.5  2007/02/05 05:08:19  lujian
 * *** empty log message ***
 *
 * Revision 1.4  2007/02/02 04:37:14  lujian
 * *** empty log message ***
 *
 * Revision 1.3  2007/01/30 09:22:32  sunxf
 * "保存并返回"在保存失败后不返回
 *
 * Revision 1.2  2007/01/30 04:48:31  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
