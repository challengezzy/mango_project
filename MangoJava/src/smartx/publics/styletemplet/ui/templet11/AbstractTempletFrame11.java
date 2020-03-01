package smartx.publics.styletemplet.ui.templet11;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import smartx.framework.common.ui.*;
import smartx.framework.common.ui.component.*;
import smartx.framework.common.vo.*;
import smartx.framework.metadata.ui.*;
import smartx.framework.metadata.ui.componentscard.*;
import smartx.framework.metadata.util.UIComponentUtil;
import smartx.framework.metadata.vo.*;
import smartx.publics.styletemplet.ui.*;



public abstract class AbstractTempletFrame11 extends NovaInternalFrame implements NovaEventListener {

    protected String str_2treetempletecode = null;

    protected String str_1treetempletecode = null;

    protected String str_2treetitle = null;

    protected String str_2treesql = null;

    protected String str_2treepk = null;

    protected String str_2treeparentpk = null;

    protected String str_1treetitle = null;

    protected String str_1treesql = null;

    protected String str_1treepk = null;

    protected String str_1treeparentpk = null;

    protected String str_2treefield_1tree = null;

    // protected JPanel customerpanel = null;

    protected AbstractCustomerButtonBarPanel panel_customer = null;

    protected String[] menu = null;

    private BillListPanel list = null;

    protected String uiinterceptor = null;

    protected String bsinterceptor = null;

    protected DefaultMutableTreeNode curnode = null;

    private String str_wherecondition = null;

    protected String treerefreshcondition = null;

    protected JTree tree = null;

    protected JTree Firsttree = null;

    private JScrollPane scroll = null;

    protected TreePath path = null;

    protected JPanel systembtnpanel = null;

    protected JPanel navigationpanel = new JPanel();

    protected QuickQueryPanel querypanel = null;

    private boolean showsystembutton = true;

    private JPanel secondtreepanel = null;

    protected BillCardPanel cardpanel = null;
    protected HashVO vo_firsttree = null;

    protected JPanel panel_btn = null;

    private JTextField jtf_search = null;

    protected int currentShowPos = -1;

    protected ArrayList treePath = null;
    protected JButton btn_insert = null;

    protected JButton btn_delete = null;
    protected JButton btn_edit = null;

//    private boolean isrefreshfromtable = false;//如果是从列表中选择一行导致树２要刷新，则不执行树2的刷新事件.

    public AbstractTempletFrame11() {
        super();
        init(); //
    }

    public AbstractTempletFrame11(String _title) {
        super(_title);
    }

    public abstract String get2TreeSQL();

    public abstract String get2TreePrimarykey();

    public abstract String get2TreeParentkey();

    public abstract String get1TreeSQL();

    public abstract String get1TreePrimarykey();

    public abstract String get1TreeParentkey();

    public String[] getSys_Selection_Path() {
        return menu;
    }

    //
    // public JPanel getCustomerpanel() {
    // return new JPanel();
    // }

    public String get2Treetitle() {
        return "根结点";
    }

    public String get1Treetitle() {
        return "根结点";
    }

    public boolean isShowsystembutton() {
        return showsystembutton;
    }

    public abstract String get2TreeTempeltCode();

    public abstract String get1TreeTempeltCode();

    // 第２棵树中与第一棵树相关联的字段,外键。
    public abstract String get2Tree_1TreeField();

    protected void init() {
        str_1treetempletecode = get1TreeTempeltCode();
        str_1treetitle = this.get1Treetitle();
        str_1treesql = get1TreeSQL();
        str_1treepk = get1TreePrimarykey();
        str_1treeparentpk = get1TreeParentkey();

        str_2treetempletecode = get2TreeTempeltCode();
        str_2treetitle = this.get2Treetitle();
        str_2treesql = get2TreeSQL();
        str_2treepk = get2TreePrimarykey();
        str_2treefield_1tree = get2Tree_1TreeField();
        str_2treeparentpk = get2TreeParentkey();
        menu = getSys_Selection_Path();
        // customerpanel = getCustomerpanel();
        uiinterceptor = getUiinterceptor();
        bsinterceptor = getBsinterceptor();
        // if (getUiinterceptor() != null &&
        // !getUiinterceptor().trim().equals("")) {
        // try {
        // uiIntercept = (IUIIntercept_04)
        // Class.forName(getUiinterceptor().trim()).newInstance(); //
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // }
        this.setTitle(getTempletTitle());
        this.setSize(getTempletSize());
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(getMainPanel(), BorderLayout.CENTER);
        this.getContentPane().add(getNavigationPanel(), BorderLayout.NORTH);
        this.setVisible(true); //
        this.toFront();
        list.addNovaEventListener(this); // 注册自己事件监听!!
    }

    protected JPanel getMainPanel() {
        JPanel rpanel = new JPanel();
        JSplitPane jsp = new JSplitPane();
        jsp.setOneTouchExpandable(true);
        jsp.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        jsp.setDividerLocation(200);
        jsp.setLeftComponent(getLeftPanel());
        jsp.setRightComponent(getRightPanel());
        rpanel.setLayout(new BorderLayout());
        rpanel.add(jsp, BorderLayout.CENTER);

        return rpanel;
    }

    protected JPanel getLeftPanel() {
        JPanel rpanel = new JPanel();
        rpanel.setLayout(new GridLayout(2, 1, 10, 0));
        rpanel.add(getFirstTreePanel());
        rpanel.add(getSecondTreePanel());
        return rpanel;
    }

    protected JPanel getRightPanel() {
        JPanel rpanel = new JPanel();
        rpanel.setLayout(new BorderLayout());
        JPanel bodyp = new JPanel();
        bodyp.setLayout(new GridLayout(2, 1, 10, 0));
        bodyp.add(getTablePanel());
        bodyp.add(getCardPanel());
        rpanel.add(bodyp, BorderLayout.CENTER);
        return rpanel;
    }

    protected JPanel getCardPanel() {
        JPanel cardbtnpanel = new JPanel();
        cardbtnpanel.setLayout(new BorderLayout());
        cardpanel = new BillCardPanel(this.str_2treetempletecode);
        cardbtnpanel.add(getSysBtnPanel(), BorderLayout.SOUTH);
        cardbtnpanel.add(cardpanel, BorderLayout.CENTER);
        return cardbtnpanel;
    }

    // 快速查询
    // protected JPanel getQueryPanel( )
    // {
    // if ( querypanel != null )
    // return querypanel;
    // querypanel = new QuickQueryPanel( list );
    // return querypanel;
    // }

    protected String getTempletTitle() {
        if (menu == null) {
            return str_2treetempletecode;
        }
        return "[" + menu[menu.length - 1] + "]";
    }

    /**
     * 获取树标题
     *
     * @return String
     */
    public String get2TreeTitle() {
        return this.str_2treetitle;
    }

    /**
     * 获取窗口大小
     *
     * @return Dimension
     */
    protected Dimension getTempletSize() {
        return new Dimension(1000, 700);
    }

    /**
     * 获取系统按钮面板
     *
     * @return Jpanel
     */
    protected JPanel getSysBtnPanel() {
        if (systembtnpanel != null) {
            return systembtnpanel;
        }
        systembtnpanel = new JPanel();
        systembtnpanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        if (showsystembutton) {
            systembtnpanel.add(getBtnPanel());
        }
        systembtnpanel.setVisible(false);
        return systembtnpanel;
    }

    /**
     * 获取系统导航栏
     *
     * @return JPanel
     */
    protected JPanel getNavigationPanel() {
        if (menu == null) {
            return new JPanel();
        }
        navigationpanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel temp = new JLabel();
        temp.setHorizontalAlignment(JLabel.LEFT);
        String menulist = NovaConstants.STRING_CURRENT_POSITION;
        for (int i = 1; i < menu.length; i++) {
            menulist = menulist + menu[i].toString() + NovaConstants.STRING_CURRENT_POSITION_DIVIDER;
        }
        menulist = menulist.substring(0, menulist.lastIndexOf(NovaConstants.STRING_CURRENT_POSITION_DIVIDER));
        temp.setText(menulist);
        navigationpanel.add(temp);
        return navigationpanel;
    }

    /**
    /**
     * 获取按钮面板，包括在新增 ，修改时的确实和取消按钮
     *
     * @return JPanel
     */
    protected JPanel getBtnPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JButton okbtn = new JButton("确定");
        okbtn.setPreferredSize(UIComponentUtil.getButtonDefaultSize());
        okbtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                onSave();
            }
        });
        JButton cancelbtn = new JButton("取消");
        cancelbtn.setPreferredSize(UIComponentUtil.getButtonDefaultSize());
        cancelbtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e1) {
                onCancel();
            }
        });
        panel.add(okbtn);
        panel.add(cancelbtn);
        return panel;
    }

    /**
     * 新增 事件处理
     *
     */
    protected void onInsert() {
        if (cardpanel.getEditState().equalsIgnoreCase(NovaConstants.BILLDATAEDITSTATE_INSERT)) {
            NovaMessage.show(this, "当前正处于新增状态", NovaConstants.MESSAGE_WARN);
            return;
        }
        HashVO vo2 = getCurrSelectedSecondTreeVO();
        if (vo2 == null) {
            return;
        }
        cardpanel.createNewRecord();
        if (!curnode.isRoot()) {
            if (cardpanel.getCompentByKey(str_2treeparentpk) instanceof UIRefPanel) {
                cardpanel.getCompentByKey(str_2treeparentpk).setObject(new RefItemVO(vo2));
            } else {
                cardpanel.getCompentByKey(str_2treeparentpk).setObject(
                    vo2.getIntegerValue(str_2treepk).intValue() == -1
                    ? ""
                    : (vo2.getIntegerValue(str_2treepk) + ""));
            }

        }
        HashVO vo1 = getCurrSelectedFirstTreeVO();
        if (vo1 == null) {
            return;
        }
        if (! ( (DefaultMutableTreeNode) Firsttree.getLastSelectedPathComponent()).isRoot()) {
            if (cardpanel.getCompentByKey(str_2treefield_1tree) instanceof UIRefPanel) {
                cardpanel.getCompentByKey(str_2treefield_1tree).setObject(new RefItemVO(vo1));
            } else {
                cardpanel.getCompentByKey(str_2treefield_1tree).setObject(
                    vo2.getIntegerValue(str_1treepk).intValue() == -1
                    ? ""
                    : (vo2.getIntegerValue(str_1treepk) + ""));
            }
        }
        cardpanel.getCompentByKey(str_2treeparentpk).setEditable(false);
        cardpanel.getCompentByKey(str_2treefield_1tree).setEditable(false);
        systembtnpanel.setVisible(true);
    }

    /**
     * 修改 事件处理
     *
     */
    public void onEdit() {
        path = tree.getSelectionPath();
        if (path == null) {
            NovaMessage.show(this, "请先选择一个结点!", NovaConstants.MESSAGE_WARN);
            return;
        }
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
        if (node.isRoot()) {
            NovaMessage.show(this, "不能修改根结点!!!", NovaConstants.MESSAGE_ERROR);
            return;
        }
        executeEdit();
    }

    private void executeEdit() {
        cardpanel.setEditable(true);
        cardpanel.setEditState(NovaConstants.BILLDATAEDITSTATE_UPDATE);
        cardpanel.setEditable(this.str_2treefield_1tree,false);
        systembtnpanel.setVisible(true);
        setUpdateConditionForTree1Relation();
    }

    protected void setUpdateConditionForTree1Relation() {
        if (cardpanel.getCompentByKey(str_2treeparentpk) instanceof UIRefPanel) {
            HashVO vo = (HashVO) ( (DefaultMutableTreeNode)this.Firsttree.getLastSelectedPathComponent()).getUserObject();
            String str_id = vo.getStringValue(str_1treepk);
            String extraCondition=" AND "+this.str_2treefield_1tree + "='" + str_id + "'";

            UIRefPanel refPanel = (UIRefPanel)cardpanel.getCompentByKey(str_2treeparentpk);
            String preCondition=refPanel.getRefDesc();
            String condition=preCondition.substring(0,preCondition.indexOf(";"))+
                extraCondition+
                preCondition.substring(preCondition.indexOf(";"),preCondition.length());
            refPanel.setRefDesc(condition);
        }

    }

    /**
     * 保存 事件处理 根据新增 或 修改分发到相应的事件处理过程
     *
     */
    protected void onSave() {
        try {
            if(!cardpanel.checkValidate()) {
                return;
            }

            if(!saveBeforeCheck()){
            	return;
            }

            if (cardpanel.getEditState().equalsIgnoreCase(NovaConstants.BILLDATAEDITSTATE_INSERT)) { // 如果是新增状态
                insertSave();
            } else if (cardpanel.getEditState().equalsIgnoreCase(NovaConstants.BILLDATAEDITSTATE_UPDATE)) { // 如果是编辑状态
                updateSave();
            } else {
                NovaMessage.show("当前不处于新增或修改状态!!", NovaConstants.MESSAGE_ERROR);
                return;
            }
            cardpanel.setEditState(NovaConstants.BILLDATAEDITSTATE_INIT);
            systembtnpanel.setVisible(false);
        } catch (Exception e) {
            NovaMessage.showException(this,e);
            e.printStackTrace();
        }
    }

    /**
     * 新增 记录的保存调用
     *
     * @throws Exception
     */
    protected void insertSave() throws Exception {
        BillVO billVO = cardpanel.getBillVO(); //
        // BillVO returnVO = null;
        try {
            StyleTempletServiceIfc service = (StyleTempletServiceIfc) NovaRemoteServiceFactory.getInstance()
                .lookUpService(StyleTempletServiceIfc.class);
            service.style03_dealInsert(cardpanel.getDataSourceName(), getBsinterceptor(), billVO);
        } catch (Exception e1) {
            e1.printStackTrace();
            throw e1;
        } // 直接提交数据库,这里可能抛异常!!

        VectorMap map = cardpanel.getAllObjectValuesWithVectorMap();
        HashVO vo = new HashVO();
        String[] keys = map.getKeysAsString();
        for (int i = 0; i < keys.length; i++) {
            vo.setAttributeValue(keys[i], map.get(keys[i]));
        }
        DefaultMutableTreeNode newnode = new DefaultMutableTreeNode(vo);
        curnode.add(newnode);
        path = path.pathByAddingChild(newnode);
        curnode = newnode;
        cardpanel.setEditable(false);
        onRefresh();
    }

    /**
     * 修改 记录的保存调用
     *
     * @throws Exception
     */
    protected void updateSave() throws Exception {
        BillVO billVO = cardpanel.getBillVO(); //
        //版本加1
        billVO.updateVersion();
        try {
            StyleTempletServiceIfc service = (StyleTempletServiceIfc) NovaRemoteServiceFactory.getInstance()
                .lookUpService(StyleTempletServiceIfc.class);
            service.style03_dealUpdate(cardpanel.getDataSourceName(), getBsinterceptor(), billVO);
            cardpanel.updateVersion();
        } catch (Exception e1) {
            e1.printStackTrace();
            throw e1;
        } // 直接提交数据库,这里可能抛异常!!
        // 设置显示信息
        if (!curnode.isLeaf()) {
            onRefresh();
        }
        VectorMap map = cardpanel.getAllObjectValuesWithVectorMap();
        HashVO nvo = new HashVO();
        String[] keys = map.getKeysAsString();
        for (int i = 0; i < keys.length; i++) {
            nvo.setAttributeValue(keys[i], map.get(keys[i]));
        }
        HashVO ovo = (HashVO) curnode.getUserObject();
        curnode.setUserObject(nvo);
        cardpanel.setEditable(false);

        // 得到修改结点的原先父结点和新的父结点，比较两个是否相同.如果相同，刚不用从数据库刷新，如果不同，则要再从数据库刷新一遍
        Object oldparent = ovo.getAttributeValue(str_2treeparentpk.toLowerCase());
        Object newparent = nvo.getAttributeValue(str_2treeparentpk.toLowerCase());
        if (newparent != null) {
            if (newparent instanceof RefItemVO) {
                newparent = ( (RefItemVO) newparent).getId();
            } else {
                newparent = newparent.toString();
            }
        }

        if (oldparent != null) {
            if (oldparent instanceof RefItemVO) {
                oldparent = ( (RefItemVO) oldparent).getId();
            } else {
                oldparent = oldparent.toString();
            }
        }
        // 比较两个String 是否相同
        if (oldparent != null && newparent != null && !oldparent.equals(newparent)) {
        	  HashVO vo = (HashVO) ((DefaultMutableTreeNode)this.Firsttree.getLastSelectedPathComponent()).getUserObject();
              String str_id = vo.getStringValue(str_1treepk);
            this.generateSecondTree( this.str_2treefield_1tree+"='"+str_id+"'");
            list.clearTable();
            cardpanel.reset();
        } else {
            onRefresh();
        }
        if (list.getSelectedRow() != -1) {
            list.setValueAtRow(list.getSelectedRow(), cardpanel.getBillVO());
        }
        if (cardpanel.getCompentByKey("VERSION") != null&&cardpanel.getValueAt("VERSION")!=null&&!cardpanel.getValueAt("VERSION").equals("")) {
            cardpanel.setValueAt("VERSION", "" + (Double.parseDouble(String.valueOf(cardpanel.getValueAt("VERSION"))) + 1));
        }
    }

    // 取消 新增或修改操作
    protected void onCancel() {
        if (!cardpanel.getEditState().equalsIgnoreCase(NovaConstants.BILLDATAEDITSTATE_INIT)) {
            cardpanel.reset();
        }
        cardpanel.setEditable(false);
        systembtnpanel.setVisible(false);
    }

    protected void onDelete() {
		if(path==null||getCurrSelectedSecondTreeVO()==null){
            NovaMessage.show(this, NovaConstants.STRING_DEL_SELECTION_NEED);
            return;
		}
		path = path.getParentPath();
		String id = getCurrSelectedSecondTreeVO().getStringValue("ID");
		if (executeDelete(id)) {
			for (int i = 0; i < list.getRowCount(); i++) {
				if (list.getBillVO(i).getRealValue("ID").equalsIgnoreCase(id)) {
					list.removeRow(i);
				}
			}
		}
	}

    protected void onBtnDelete() {
        if (list.getTable().getSelectedRowCount() != 1) {
            NovaMessage.show(this, NovaConstants.STRING_DEL_SELECTION_NEED);
            return;
        }
        if (executeDelete(list.getRealValueAtModel(list.getSelectedRow(), "ID"))) {
            list.removeRow(list.getSelectedRow());
        }
    }
    
    protected boolean executeDelete(String id) {
        // 执行业务逻辑判断
        if (!execJudgement(id)) {
            return false;
        }
        if (!NovaMessage.confirm("确实要删除吗?")) {
            return false;
        }
        //BillVO billVO = cardpanel.getBillVO(); //yh20070823替换为如下行,解决点击取消再点击删除无法删除的bug
        BillVO billVO = list.getBillVO(list.getSelectedRow());
      
        try {
            StyleTempletServiceIfc service = (StyleTempletServiceIfc) NovaRemoteServiceFactory.getInstance()
                .lookUpService(StyleTempletServiceIfc.class);
            service.style03_dealDelete(cardpanel.getDataSourceName(), getBsinterceptor(), billVO);
            cardpanel.reset();
            generateSecondTree(treerefreshcondition);
        } catch (Exception e1) {
            NovaMessage.showException(this,e1);
            e1.printStackTrace();
            return false;
        } // 直接提交数据库,这里可能抛异常!!
        return true;

    }

    protected void onBtnEdit() {
        if (list.getTable().getSelectedRowCount() != 1) {
            NovaMessage.show(this, "请选择一条要修改的记录!");
            return;
        }
        executeEdit();
    }

    // 执行业务逻辑判断.返回false,则不再执行删除操作.
    protected boolean execJudgement(String id) {
        if (!curnode.isLeaf()) {
            NovaMessage.show(this, "目录非空,不能删除!");
            return false;
        }

        return true;
    }

    /**
     * 递归所选结点的所有子结点.
     *
     * @return
     */
    protected HashVO[] getCurrSelectedTreeVOWithChildren() {
        path = tree.getSelectionPath();
        if (path == null) {
            NovaMessage.show(this, "请先选择一个结点!", NovaConstants.MESSAGE_WARN);
            return null;
        }
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
        ArrayList children = getChildrenNode(node);

        if (node.isRoot()) {
            NovaMessage.show(this, "不能操作根结点!!!", NovaConstants.MESSAGE_ERROR);
            return null;
        }
        HashVO[] vos = new HashVO[children.size()];
        for (int i = 0; i < children.size(); i++) {
            DefaultMutableTreeNode tnode = (DefaultMutableTreeNode) children.get(i);
            vos[i] = (HashVO) tnode.getUserObject();
        }
        return vos; //
    }

    protected ArrayList getChildrenNode(DefaultMutableTreeNode node) { // 得到顺排的所选结点的所选结点本身和所有子结点.删除时需要倒序遍历
        ArrayList temp = new ArrayList();
        temp.add(node);
        if (node.getChildCount() > 0) {
            for (int i = 0; i < node.getChildCount(); i++) {
                temp.addAll(getChildrenNode( (DefaultMutableTreeNode) node.getChildAt(i)));
            }

        }
        return temp;

    }

    protected HashVO getCurrSelectedSecondTreeVO() {
        path = tree.getSelectionPath();
        if (path == null) {
            NovaMessage.show(this, "请先选择一个结点!", NovaConstants.MESSAGE_WARN);
            return null;
        }

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
        if (node.isRoot()) {
            HashVO vo = new HashVO();
            vo.setAttributeValue(str_2treepk, new Integer( -1));
            return vo;
        }
        HashVO vo = (HashVO) node.getUserObject();
        return vo; //
    }

    protected HashVO getCurrSelectedFirstTreeVO() {
        TreePath path = Firsttree.getSelectionPath();
        if (path == null) {
            NovaMessage.show(this, "请先选择一个结点!", NovaConstants.MESSAGE_WARN);
            return null;
        }

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
        if (node.isRoot()) {
            HashVO vo = new HashVO();
            vo.setAttributeValue(str_1treepk, new Integer( -1));
            return vo;
        }
        HashVO vo = (HashVO) node.getUserObject();
        return vo; //
    }

    /**
     * 卡片会调用这里
     */
    public void onValueChanged(NovaEvent _evt) {
        if (_evt.getChangedType() == NovaEvent.CardChanged) {
            // if (uiIntercept != null) {
            // BillListPanel card_tmp = (BillListPanel) _evt.getSource(); //
            // String tmp_itemkey = _evt.getItemKey(); //
            // try {
            // uiIntercept.actionAfterUpdate(card_tmp, list.getSelectedRow(),
            // tmp_itemkey);
            // } catch (Exception e) {
            // if (!e.getMessage().trim().equals("")) {
            // JOptionPane.showMessageDialog(this, e.getMessage()); //
            // }
            // }
            // }
        } else if (_evt.getChangedType() == NovaEvent.ListChanged) { // 如果是列表变化

        }
    }

    public void onRefresh() {
        if (path != null) {
            tree.expandPath(path);
            tree.setSelectionPath(path);
            tree.scrollPathToVisible(path);
        }
        tree.updateUI();
        scroll.updateUI();
    }

    protected void onQuery() {
        if (tree.getSelectionCount() == 1) {
            list.getTable().editingStopped(new ChangeEvent(list.getTable()));
            QueryDialog queryDialog = new QueryDialog(AbstractTempletFrame11.this, list.getTempletVO());
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
     * 获取第二棵树控件的面板
     *
     * @return JPanel
     */
    protected JPanel getSecondTreePanel() {
        secondtreepanel = new JPanel();
        secondtreepanel.setLayout(new BorderLayout());
        return secondtreepanel;
    }

    /**
     * 获取第一棵树控件的面板
     *
     * @return JPanel
     */
    protected JPanel getFirstTreePanel() {
        JPanel rpanel = new JPanel();
        rpanel.setLayout(new BorderLayout());
        Firsttree = UIUtil.getJTreeByParentPK_HashVO(null, str_1treetitle, str_1treesql, str_1treepk,
            str_1treeparentpk);
        Firsttree.addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent evt) {
                TreePath[] paths = evt.getPaths();
                for (int i = 0; i < paths.length; i++) {
                    if (evt.isAddedPath(i)) {
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) paths[i].getLastPathComponent();
                        onChangeSelectFirstTree(node);
                    } else {
                    }
                }
            }
        });
        Firsttree.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F3) {
                    dealSearchNext();
                }
            }
        });
        JScrollPane scroll = new JScrollPane(Firsttree);
        rpanel.add(getTreeButtonPanel(), BorderLayout.NORTH);
        rpanel.add(scroll, BorderLayout.CENTER);
        return rpanel;
    }

    /**
     * 选择第一棵树结点事件
     *
     * @param _node
     */
    protected void onChangeSelectFirstTree(DefaultMutableTreeNode _node) {
        systembtnpanel.setVisible(false);
        if (!_node.isRoot()) {
            // curnode = _node;
            HashVO vo = (HashVO) _node.getUserObject();
            String str_id = vo.getStringValue(str_1treepk);
            generateSecondTree(str_2treefield_1tree + "='" + str_id + "'");
            list.clearTable();
            cardpanel.reset();
            vo_firsttree = vo;

            if (hasPermissionOnTree1(vo)) {
                setOperatBtnEnable(true);
            } else {
                setOperatBtnEnable(false);
            }
        }
    }

    //检查是否对第一棵树下的内容有更改权限,如果返回true 则会出现增,删,改等操作按钮
    protected boolean hasPermissionOnTree1(HashVO tree_vo) {
        return true;
    }

    /*
     * 根据第一棵树生成第二棵树
     *
     * @param condition：由第一棵树生成的过滤条件 如：regionid=12
     */
    protected void generateSecondTree(String condition) {
        this.treerefreshcondition = condition;
        tree = UIUtil.getJTreeByParentPK_HashVO(null, str_2treetitle, str_2treesql + " and " + condition, str_2treepk,
                                                str_2treeparentpk);
        tree.addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent evt) {
                TreePath[] paths = evt.getPaths();
                for (int i = 0; i < paths.length; i++) {
                    if (evt.isAddedPath(i)) {
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) paths[i].getLastPathComponent();
                        onChangeSelectSecondTree(node);
                    } else {
                    }
                }
            }
        });
        MouseAdapter listener = new MyMouseListener();
        tree.addMouseListener(listener);
        tree.updateUI();
        scroll = new JScrollPane(tree);
        secondtreepanel.removeAll();
        secondtreepanel.add(scroll, BorderLayout.CENTER);
        secondtreepanel.updateUI();
    }

    /**
     * 选择树结点事件
     *
     * @param _node
     */
    protected void onChangeSelectSecondTree(DefaultMutableTreeNode _node) {
        systembtnpanel.setVisible(false);
        curnode = _node;
        if (!_node.isRoot()) {
            path = tree.getSelectionPath();
            HashVO vo = (HashVO) _node.getUserObject();
            String str_id = vo.getStringValue(str_2treepk);
            cardpanel.reset();
            if (_node.isLeaf()) {
                str_wherecondition = list.getTempletVO().getPkname() + "='" + str_id + "'";
                cardpanel.queryData(list.getSQL(str_wherecondition));
            } else {
                str_wherecondition = str_2treeparentpk + "='" + str_id + "'";
                cardpanel.refreshData(this.str_2treepk + "='" + vo.getStringValue(this.str_2treepk) + "'");
            }
            String str_sql = list.getSQL(str_wherecondition);
            list.QueryData(str_sql);
            if (_node.isLeaf()) {
                list.getTable().setRowSelectionInterval(0, 0);
            }
        }else{
        	if(_node.getUserObject() instanceof String){
                str_wherecondition = "("+str_2treeparentpk + "='' or "+str_2treeparentpk +" is null) and regionid="+vo_firsttree.getStringValue("ID");
                String str_sql = list.getSQL(str_wherecondition);
                list.QueryData(str_sql);
        	}
        }
    }

    /**
     * 树上方按钮面板
     *
     * @return JPanel
     */
    protected JPanel getTreeButtonPanel() {
        if (panel_btn != null) {
            return panel_btn;
        }
        panel_btn = new JPanel();
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

    /**
     * 刷新树
     *
     */
    public void onTreeRefresh() {
        list.clearTable();
        tree = UIUtil.getJTreeByParentPK_HashVO(null, str_2treetitle, str_2treesql, str_2treepk, str_2treeparentpk);
        tree.addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent evt) {
                TreePath[] paths = evt.getPaths();
                for (int i = 0; i < paths.length; i++) {
                    if (evt.isAddedPath(i)) {
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) paths[i].getLastPathComponent();
                        onChangeSelectSecondTree(node);
                    } else {
                    }
                }
            }
        });
        scroll.getViewport().removeAll();
        scroll.getViewport().add(tree);
        scroll.updateUI();
    }

    // 根据列表选择的列刷新卡片的显示值
    public void refreshCard(int rownum) {
        if (rownum < 0) {
            return;
        }
        if (!cardpanel.getEditState().equalsIgnoreCase(NovaConstants.BILLDATAEDITSTATE_INIT)) {
            onCancel();
        }
        cardpanel.setValue(list.getValueAtRowWithHashMap(list.getSelectedRow()));
        cardpanel.setRowNumberItemVO( (RowNumberItemVO) list.getValueAt(list.getSelectedRow(), 0)); // 设置行号

        cardpanel.setEditState(NovaConstants.BILLDATAEDITSTATE_INIT);
    }

    /**
     * 处理键盘事件,查询后按F3 定位到搜索的下一个结点
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

    private void dealSearchNext() {
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
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) Firsttree.getModel().getRoot();
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
                DefaultTreeModel model = (DefaultTreeModel) Firsttree.getModel();

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
        Firsttree.makeVisible( (TreePath) treePath.get(pos));
        Firsttree.setSelectionPath( (TreePath) treePath.get(pos));
        Firsttree.scrollPathToVisible( (TreePath) treePath.get(pos));
        currentShowPos = pos;
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
            list = new BillListPanel(str_2treetempletecode, true, false);
            list.setUnEditable();
            list.initialize();
            list.getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {

                public void valueChanged(ListSelectionEvent e) {
                	if(list.getSelectedRow()<0)
                		return;
                    refreshCard(list.getSelectedRow());
                    refreshTree2( );
                }

            });
        }
        rpanel.add(list, BorderLayout.CENTER);
        rpanel.add(getSysOperatorBtnPanel(), BorderLayout.NORTH);
        return rpanel;
    }

    // 列表选择一条记录后,重新计算当前的PATH,刷新树的显示
	protected void refreshTree2()
	{
		String recode_pk = (String)list.getValueAt( list.getSelectedRow(), str_2treepk );
		for(int i=0;i<curnode.getChildCount( );i++)
			{
				if(((HashVO)((DefaultMutableTreeNode)curnode.getChildAt( i )).getUserObject( )).getStringValue( str_2treepk ).equalsIgnoreCase( recode_pk ))
				{
					curnode=(DefaultMutableTreeNode)curnode.getChildAt( i );
					path = path.pathByAddingChild( curnode );
					tree.setSelectionPath( path );
					tree.expandPath( path );
					tree.scrollPathToVisible( path );
					break;
				}
			}
	}
    protected void setOperatBtnEnable(boolean enable) {
        btn_insert.setEnabled(enable);
        btn_delete.setEnabled(enable);
        btn_edit.setEnabled(enable);
    }

    protected JPanel getSysOperatorBtnPanel() {
        JPanel rpanel = new JPanel();
        rpanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        btn_insert = new JButton("新增");
        btn_insert.setPreferredSize(UIComponentUtil.getButtonDefaultSize());
        btn_delete = new JButton("删除");
        btn_delete.setPreferredSize(UIComponentUtil.getButtonDefaultSize());
        btn_edit = new JButton("修改");
        btn_edit.setPreferredSize(UIComponentUtil.getButtonDefaultSize());
        btn_insert.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                onInsert();
            }
        });
        btn_delete.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                onBtnDelete();
            }
        });

        btn_edit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                onBtnEdit();
            }
        });
        setOperatBtnEnable(false);
        rpanel.add(btn_insert);
        rpanel.add(btn_edit);
        rpanel.add(btn_delete);
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

    /**
     * 获取菜单.
     *
     * @return
     */
    protected JPopupMenu getMenu() {
        if (!hasPermissionOnTree1(vo_firsttree)) {
            return null;
        }
        JPopupMenu menu = new JPopupMenu();
        JMenuItem add = new JMenuItem("新增");
        add.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                onInsert();
            }
        });
        JMenuItem modify = new JMenuItem("修改");
        modify.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                onEdit();
            }
        });
        JMenuItem del = new JMenuItem("删除");
        del.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                onDelete();
            }
        });
        menu.add(add);
        menu.add(modify);
        menu.addSeparator();
        menu.add(del);
        return menu;
    }

    /**
     * 第二棵树的右键菜单
     *
     * @author Administrator
     *
     */
    protected class MyMouseListener extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3) {
                JPopupMenu menu = getMenu();
                if (menu != null) {
                    menu.show(tree, e.getX(), e.getY());
                }
            }
        }
    }

    protected boolean saveBeforeCheck()  throws Exception{
    	return true;
    }
}
