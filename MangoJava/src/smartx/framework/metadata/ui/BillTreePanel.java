/**************************************************************************
 * $RCSfile: BillTreePanel.java,v $  $Revision: 1.5.6.2 $  $Date: 2009/02/02 16:12:54 $
 **************************************************************************/

package smartx.framework.metadata.ui;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.plaf.TreeUI;
import javax.swing.tree.*;

import smartx.framework.common.ui.*;
import smartx.framework.common.vo.*;
import smartx.framework.metadata.vo.*;
import smartx.framework.metadata.vo.templetvo.*;


public class BillTreePanel extends JPanel {

    private static final long serialVersionUID = 8051676743971788439L;

    private Pub_Templet_1VO templetVO = null;               // 元原模板主表
    private Pub_Templet_1_ItemVO[] templetItemVOs = null;   // 元原模板子表

    private String str_roottitle = null;                    // 根节点名称
    private String str_treeViewFieldName = null;            // 树显示文本对应字段名

    private String str_keyname = null;                      // 数据主键字段
    private String str_parentkeyname = null;                // 数据父键字段

    private String[] iconNames = null;                      // 父节点图标
    private String leaficonNames = null;                    // 叶节点图标 

    private boolean ifShowRoot = true;                      // 是否显示根

    private DefaultMutableTreeNode rootNode = null;
    private DefaultMutableTreeNode rootNode2 = null;

    private JScrollPane mainScrollPane = null;
    private JScrollPane secondScrollPane = null;            // 第二个树,用来快速显示所有直接路径的!

    private JTree jTree = null;
    private JTree jTree2 = null;                            // 第二颗树!!

    private String str_realsql = null;

    private Vector v_BillTreeSelectListeners = new Vector();

    private boolean bo_isShowSecondTree = false;
    private BillTreeTransferHandler handler = null;
    private boolean enable_all_drag = false;
    private Vector menuItem = null;
    private BillTreePanel() {
    }

    public BillTreePanel(String _templetcode, String _rootTitle, String _treeViewFieldName, String _keyname,
                         String _parentkeyname, boolean _ifShowRoot) {
        try {
            templetVO = UIUtil.getPub_Templet_1VO(_templetcode); // 取得元原模板主表
            templetItemVOs = templetVO.getItemVos(); // 各项.....
            this.str_roottitle = _rootTitle; //
            this.str_treeViewFieldName = _treeViewFieldName; // 树的render显示的的列名!!!
            this.str_keyname = _keyname; // 列名
            this.str_parentkeyname = _parentkeyname; // 父列名!!
            this.ifShowRoot = _ifShowRoot;
            initialize(); // 初始化!!
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BillTreePanel(AbstractTempletVO _templetVO, String _rootTitle, String _treeViewFieldName, String _keyname,
                         String _parentkeyname, boolean _ifShowRoot) {
        try {
            templetVO = UIUtil.getPub_Templet_1VO(_templetVO.getPub_templet_1Data(), _templetVO
                                                  .getPub_templet_1_itemData()); // 取得元原模板主表
            templetItemVOs = templetVO.getItemVos(); // 各项..
            this.str_roottitle = _rootTitle; //
            this.str_treeViewFieldName = _treeViewFieldName; // 树的render显示的的列名!!!
            this.str_keyname = _keyname; // 列名
            this.str_parentkeyname = _parentkeyname; // 父列名!!
            this.ifShowRoot = _ifShowRoot;
            initialize(); // 初始化!!
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final void initialize() {
        this.removeAll(); //
        this.setLayout(new BorderLayout());

        this.add(getMainScrollPane(), BorderLayout.CENTER); //
        getSecondScrollPane(); // 先将第二个也取出来!!
        this.updateUI();
        bo_isShowSecondTree = false;
    }

    private void initialize2() {
        this.removeAll(); //
        this.setLayout(new BorderLayout());
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT); // 水平分割!!
        splitPane.setDividerSize(10);
        splitPane.setOneTouchExpandable(true); //
        double li_height = this.getSize().getHeight() * 0.75;
        splitPane.setDividerLocation(new Double(li_height).intValue());
        this.add(splitPane, BorderLayout.CENTER); //
        splitPane.add(getMainScrollPane(), JSplitPane.LEFT); //
        splitPane.add(getSecondScrollPane(), JSplitPane.RIGHT);
        this.updateUI();
        bo_isShowSecondTree = true;
    }

    private JScrollPane getMainScrollPane() {
        if (mainScrollPane != null) {
            return mainScrollPane;
        }

        mainScrollPane = new JScrollPane();
        BillVO root_billvo = new BillVO();
        root_billvo.setKeys(new String[]{"title"});
        root_billvo.setDatas(new Object[]{str_roottitle});
        root_billvo.setToStringKeyName("title");
        rootNode = new DefaultMutableTreeNode(root_billvo); // 创建根结点!!
        //由于JDK存在BUG,此处更改树的显示方式，以便托拽时可以刷新UI。　　
        //Bug ID:   	 4760426
        //详情:http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4760426
        jTree = new JTree(new DefaultTreeModel(rootNode)){
            public void setUI(TreeUI newUI) {
                super.setUI(newUI);
                TransferHandler handler = getTransferHandler();
                setTransferHandler(null);
                setTransferHandler(handler);
            }
        }; // 创建树!!

        MyTreeRender aMyTreeRender = new MyTreeRender(null);
        jTree.setCellRenderer(aMyTreeRender);
        jTree.setDragEnabled(true);
        handler = new BillTreeTransferHandler();
        jTree.setTransferHandler(handler);
        initPopMenu();
        jTree.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent evt) {
                if (evt.getButton() == MouseEvent.BUTTON3) {
                	JPopupMenu popmenu_header = new JPopupMenu();
                	for(int i=0;i<menuItem.size();i++)
                		popmenu_header.add((JMenuItem)menuItem.elementAt(i));
                	popmenu_header.show(evt.getComponent(), evt.getX(), evt.getY()); // 弹出菜单
                } 
                if(evt.getButton()==MouseEvent.BUTTON1&&evt.getClickCount()==1)
                {
                	super.mousePressed(evt); 
                	TreePath path= jTree.getPathForLocation(evt.getX(), evt.getY());
                	if(path!=null&&path.getLastPathComponent()!=null)
                	{
                		if(!((DefaultMutableTreeNode)path.getLastPathComponent()).isLeaf()&&!enable_all_drag)
                			return;
                		handler.setNode((DefaultMutableTreeNode)path.getLastPathComponent());
                		handler.exportAsDrag(jTree, evt, TransferHandler.COPY);
                	}
                }
                else {
                	super.mousePressed(evt); //
                }
            }

        });

        jTree.addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent evt) {
                Object obj = evt.getSource();
                TreePath[] paths = evt.getPaths();
                for (int i = 0; i < paths.length; i++) {
                    if (evt.isAddedPath(i)) {
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) paths[i].getLastPathComponent();
                        onChangeSelectTree( ( (JTree) evt.getSource()), node); // This
                        // node
                        // has
                        // been
                    }
                }
            }
        });

        jTree.setRootVisible(ifShowRoot); // 设置是否显示根结点!!
        mainScrollPane.getViewport().add(jTree); // 加入树!!
        return mainScrollPane;
    }

    private JScrollPane getSecondScrollPane() {
        if (secondScrollPane != null) {
            return secondScrollPane;
        }

        rootNode2 = new DefaultMutableTreeNode(str_roottitle); // 创建第二颗树的根结点!!!!
        jTree2 = new JTree(new DefaultTreeModel(rootNode2)); // 创建树!!
        // jTree2.setEditable(false);
        jTree2.setBackground(new Color(240, 240, 240));
        jTree2.setCellRenderer(new SecondTreeRender());
        jTree2.setRootVisible(ifShowRoot); // 设置是否显示根结点!!
        secondScrollPane = new JScrollPane();
        // secondScrollPane.setBackground(new Color(240, 240, 240));
        secondScrollPane.getViewport().add(jTree2); // 加入树!!
        return secondScrollPane;
    }

    public String getSQL(String _condition) {
        String str_sql = "select * from " + templetVO.getTablename() + " where ";
        if (_condition == null) {
            str_sql = str_sql + " 1=1 "; //
        } else {
            str_sql = str_sql + _condition; //
        }
        return str_sql; //
    }

    //
    public void queryDataByCondition(String _condition) {
        queryData(getSQL(_condition));
    }

    //
    public void queryData(String _sql) {
        queryDataByDS(getDataSourceName(), _sql); //
    }

    //
    public void queryDataByDS(final String _datasourcename, final String _sql) {
        this.str_realsql = _sql; //
        String str_sql = _sql;
        BillVO[] billVOs = null;
        try {
            billVOs = getFWMetaService().getBillVOsByDS(_datasourcename, _sql, templetVO); //
            for (int i = 0; i < billVOs.length; i++) {
                billVOs[i].setToStringKeyName(str_treeViewFieldName); // 重置toString显示的列名!!
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        } //

        rootNode.removeAllChildren(); // 清空所有儿子!!

        DefaultMutableTreeNode[] node_level_1 = new DefaultMutableTreeNode[billVOs.length]; // 创建所有结点数组
        for (int i = 0; i < billVOs.length; i++) {
            node_level_1[i] = new DefaultMutableTreeNode(billVOs[i]); // 创建各个结点
            rootNode.add(node_level_1[i]); // 加入根结点
        }

        // 构建树
        for (int i = 0; i < node_level_1.length; i++) {
            BillVO nodeVO = (BillVO) node_level_1[i].getUserObject();
            // String str_pk = nodeVO.getRealValue(str_keyname); // 主键
            String str_pk_parentPK = nodeVO.getRealValue(str_parentkeyname); // 父亲主键
            if (str_pk_parentPK == null || str_pk_parentPK.equals("")) {
                continue;
            }
            for (int j = 0; j < node_level_1.length; j++) {
                BillVO nodeVO_2 = (BillVO) node_level_1[j].getUserObject();
                String str_pk_2 = nodeVO_2.getRealValue(str_keyname); // 主键
                // String str_pk_parentPK_2 =
                // nodeVO_2.getRealValue(str_parentkeyname); // 父亲主键
                if (str_pk_2.equals(str_pk_parentPK)) { // 如果发现该结点主键正好是上层循环的父亲结点,则将其作为我的儿子处理加入
                    try {
                        node_level_1[j].add(node_level_1[i]);
                    } catch (Exception ex) {
                        System.out.println("在[" + node_level_1[j] + "]上创建子结点[" + node_level_1[i] + "]失败!!");
                        ex.printStackTrace();
                    }
                }
            }
        }

        jTree.expandPath(new TreePath(rootNode)); // 展开第一层!!
        jTree.updateUI(); //
    }

    /**
     * 得到数据源名称
     *
     * @return
     */
    public String getDataSourceName() {
        if (templetVO.getDatasourcename() == null || templetVO.getDatasourcename().trim().equals("null")
            || templetVO.getDatasourcename().trim().equals("")) {
            return NovaClientEnvironment.getInstance().getDefaultDatasourceName(); // 默认数据源
        } else {
            return new FrameWorkTBUtil().convertExpression(NovaClientEnvironment.getInstance(), templetVO.getDatasourcename()); // 算出数据源!!
        }
    }

    /**
     * 处理树的选择节点的变更的事件
     *
     * @param _node
     */
    private void onChangeSelectTree(JTree _tree, DefaultMutableTreeNode _node) {
        // 先调用所有监听者的选择变化事件!!
        for (int i = 0; i < v_BillTreeSelectListeners.size(); i++) {
            BillTreeSelectListener listener = (BillTreeSelectListener) v_BillTreeSelectListeners.get(i);
            BillVO billVO = null; //
            if ( (_node.getUserObject() != null) && (_node.getUserObject() instanceof BillVO)) {
                billVO = (BillVO) _node.getUserObject();
            }
            BillTreeSelectionEvent event = new BillTreeSelectionEvent(this, _node, billVO); // 当前选中的结点
            listener.selectChanged(event); //
        }

        refreshSecondTree(_node); // 刷新第二颗树!!
        DefaultTreeModel model = (DefaultTreeModel) _tree.getModel();
        TreeNode[] nodes = model.getPathToRoot(_node);
        TreePath path = new TreePath(nodes);
        MyTreeRender aMyTreeRender = new MyTreeRender(path);
        _tree.setCellRenderer(aMyTreeRender);
    }

    public TreePath getSelectedPath() {
        return jTree.getSelectionPath();
    }

    /**
     * 取得当前选中的结点
     *
     * @return
     */
    public DefaultMutableTreeNode getSelectedNode() {
        TreePath path = jTree.getSelectionPath();
        if (path == null) {
            return null;
        }
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
        return node;
    }

    /**
     * 取得当前选中的结点到根结点的所有结点..
     *
     * @return
     */
    public DefaultMutableTreeNode[] getSelectedParentPathNodes() {
        TreePath path = jTree.getSelectionPath();
        if (path == null) {
            return null;
        }

        Object[] objs = path.getPath(); //
        DefaultMutableTreeNode[] nodes = new DefaultMutableTreeNode[objs.length];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = (DefaultMutableTreeNode) objs[i];
        }
        return nodes; //
    }

    /**
     * 取得选中结点路径上的所有数据对象!!,即从根结点到选中结点的所有数据对象,但根结点除外
     * 如果没有选中,则返回null,如果只选中根结点,则返回BillVO[0]
     *
     * @return
     */
    public BillVO[] getSelectedParentPathVOs() {
        TreePath path = jTree.getSelectionPath();
        if (path == null) {
            return null;
        }
        DefaultMutableTreeNode[] nodes = getSelectedParentPathNodes();
        BillVO[] vos = new BillVO[nodes.length - 1];
        for (int i = 0; i < vos.length; i++) {
            vos[i] = (BillVO) nodes[i + 1].getUserObject();
        }
        return vos;
    }

    // 取得选中行的BillVOs,即多选时
    public BillVO[] getSelectedVOs() {
        TreePath[] path = jTree.getSelectionPaths();
        if (path == null) {
            return null;
        }

        Vector v_vos = new Vector();
        DefaultMutableTreeNode[] nodes = new DefaultMutableTreeNode[path.length];
        for (int i = 0; i < path.length; i++) {
            nodes[i] = (DefaultMutableTreeNode) path[i].getLastPathComponent();
            if (!nodes[i].isRoot()) {
                v_vos.add( (BillVO) nodes[i].getUserObject());
            }
        }
        return (BillVO[]) v_vos.toArray(new BillVO[0]); //
    }

    /**
     * 取得所有结点
     *
     * @return
     */
    public DefaultMutableTreeNode[] getAllNodes() {
        Vector vector = new Vector();
        visitAllNodes(vector, this.rootNode); //
        return (DefaultMutableTreeNode[]) vector.toArray(new DefaultMutableTreeNode[0]); //
    }

    public BillVO[] getAllBillVOs() {
        DefaultMutableTreeNode[] nodes = getAllNodes(); //
        BillVO[] vos = new BillVO[nodes.length - 1]; //
        for (int i = 0; i < vos.length; i++) {
            vos[i] = (BillVO) nodes[i + 1].getUserObject();
        }
        return vos;
    }

    /**
     * 取得当前结点的所有儿子结点!!
     *
     * @return
     */
    public DefaultMutableTreeNode[] getSelectedChildPathNodes() {
        DefaultMutableTreeNode currNode = this.getSelectedNode(); //
        Vector vector = new Vector();
        visitAllNodes(vector, currNode); //
        return (DefaultMutableTreeNode[]) vector.toArray(new DefaultMutableTreeNode[0]); //
    }

    /**
     * 取得选中结点的所有子孙数据VO
     *
     * @return
     */
    public BillVO[] getSelectedChildPathBillVOs() {
        DefaultMutableTreeNode currNode = this.getSelectedNode(); //
        if (currNode.isRoot()) { // 如果选中的是根结点,则返回所有数据VO
            return getAllBillVOs();
        }
        DefaultMutableTreeNode[] nodes = getSelectedChildPathNodes(); // 取得所子孙结点..
        BillVO[] vos = new BillVO[nodes.length]; // ..
        for (int i = 0; i < vos.length; i++) {
            vos[i] = (BillVO) nodes[i].getUserObject();
        }
        return vos; //
    }

    public DefaultMutableTreeNode[] getSelectedParentAndChildPathNodes() {
        TreePath path = jTree.getSelectionPath();
        if (path == null) {
            return null;
        }

        DefaultMutableTreeNode[] parentNodes = getSelectedParentPathNodes(); // 所有父亲
        DefaultMutableTreeNode[] childNodes = getSelectedChildPathNodes(); // 所有子孙

        DefaultMutableTreeNode[] allNodes = new DefaultMutableTreeNode[parentNodes.length + childNodes.length - 1];
        for (int i = 0; i < parentNodes.length; i++) {
            allNodes[i] = parentNodes[i];
        }

        for (int j = 1; j < childNodes.length; j++) { // 第一个跳过!!
            allNodes[parentNodes.length + j - 1] = childNodes[j]; //
        }
        return allNodes;
    }

    /**
     * 取得选中结点的父亲与子孙的所有数据对象...
     *
     * @return
     */
    public BillVO[] getSelectedParentAndChildPathBillVOs() {
        TreePath path = jTree.getSelectionPath();
        if (path == null) {
            return null;
        }
        DefaultMutableTreeNode[] allNodes = getSelectedParentAndChildPathNodes(); // 取得所有父亲与儿子结点
        BillVO[] vos = new BillVO[allNodes.length - 1]; //
        for (int i = 0; i < vos.length; i++) {
            vos[i] = (BillVO) allNodes[i + 1].getUserObject();
        }
        return vos;
    }

    /**
     * 递归遍历某结点的所有子结点!
     *
     * @param _vector
     *            向量
     * @param node
     *            ,某开始结点!
     */
    private void visitAllNodes(Vector _vector, TreeNode node) {
        _vector.add(node); // 加入该结点
        if (node.getChildCount() >= 0) {
            for (Enumeration e = node.children(); e.hasMoreElements(); ) {
                DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) e.nextElement(); // 找到该儿子!!
                visitAllNodes(_vector, childNode); // 继续查找该儿子
            }
        }
    }

    /**
     * 取得当前选中的所有结点
     *
     * @return
     */
    public DefaultMutableTreeNode[] getSelectedNodes() {
        TreePath[] paths = jTree.getSelectionPaths();
        if (paths == null) {
            return null;
        }

        DefaultMutableTreeNode[] nodes = new DefaultMutableTreeNode[paths.length];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = (DefaultMutableTreeNode) paths[i].getLastPathComponent();
        }
        return nodes;
    }

    // 取得选中行的BillVO
    public BillVO getSelectedVO() {
        TreePath path = jTree.getSelectionPath();
        if (path == null) {
            return null;
        }
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
        if (node.isRoot()) {
            return null;
        } else {
            return (BillVO) node.getUserObject();
        }
    }

    public DefaultMutableTreeNode findNodeByKey(String _id) {
        DefaultMutableTreeNode[] nodes = this.getAllNodes();
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i].isRoot()) {
                continue;
            }
            BillVO vo = (BillVO) nodes[i].getUserObject();
            if ( ( (String) vo.getObject(this.str_keyname)).equals(_id)) {
                return nodes[i];
            }
        }
        return null;
    }

    private void initPopMenu() {
    	menuItem = new Vector();
        
        JMenuItem item_refresh = new JMenuItem("刷新"); // 解锁
        JMenuItem item_expand = new JMenuItem("展开树"); // 展开所有结点
        JMenuItem item_collapse = new JMenuItem("收缩树"); // 收缩所有结点

        JMenuItem item_showDirTree = null;
        if (bo_isShowSecondTree) { // 如果已显示!!
            item_showDirTree = new JMenuItem("隐藏直接路径"); // 显示直接路径
        } else {
            item_showDirTree = new JMenuItem("显示直接路径"); // 显示直接路径
        }

        JMenuItem item_quickLocate = new JMenuItem("快速定位"); // 显示SQL

        JMenuItem item_showSQL = new JMenuItem("显示SQL"); // 显示SQL

        JMenuItem item_addNode = new JMenuItem("增加结点"); // 显示SQL
        JMenuItem item_delNode = new JMenuItem("删除结点"); // 显示SQL

        item_refresh.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                refreshTree(); //
            }
        });
        item_expand.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                myExpandAll();
            }
        });

        item_collapse.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                myCollapseAll();
            }
        });

        item_showDirTree.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                showSecondTree();
            }
        });

        item_quickLocate.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                quickLocate();
            }
        });

        item_showSQL.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                showSQL();
            }
        });

        item_addNode.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                addNode(null);
            }
        });

        item_delNode.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                delCurrNode();
            }
        });

        menuItem.addElement(item_refresh); //
        menuItem.addElement(item_expand); //
        menuItem.addElement(item_collapse); //
        menuItem.addElement(item_showDirTree); //

        menuItem.addElement(item_quickLocate); // 快速定位..
        menuItem.addElement(item_showSQL); // 显示SQL..

        // popmenu_header.add(item_addNode); //增加结点
        // popmenu_header.add(item_delNode); //删除结点
    }

    private void refreshTree() {
        if (this.str_realsql != null) {
            queryData(str_realsql); // 刷新数据!!
            refreshSecondTree(null);
        }

    }

    public void myExpandAll() {
        expandAll(jTree, true);
    }

    public void myCollapseAll() {
        expandAll(jTree, false);
    }

    /**
     * 展开某一个结点!
     *
     * @param _node
     */
    public void expandOneNode(TreeNode _node) {
        TreePath path = new TreePath(getJTreeModel().getPathToRoot(_node));
        expandAll(jTree, path, true);
        jTree.makeVisible(path);
        jTree.setSelectionPath(path);
        jTree.scrollPathToVisible(path);
    }

    public void expandAll(JTree tree, boolean expand) {
        TreeNode root = (TreeNode) tree.getModel().getRoot();
        expandAll(tree, new TreePath(root), expand);
    }

    private void expandAll(JTree tree, TreePath parent, boolean expand) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e = node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, expand);
            }
        }
        if (expand) {
            tree.expandPath(parent);
        } else {
            if (!node.isRoot()) {
                tree.collapsePath(parent);
            }
        }
    }

    // 增加监听器
    public void addBillTreeSelectListener(BillTreeSelectListener _listener) {
        v_BillTreeSelectListeners.add(_listener);
    }

    // 移去所有选择监听器!
    public void removeAllBillTreeSelectListener() {
        v_BillTreeSelectListeners.removeAllElements(); //
    }

    /**
     * 显示第二颗树
     */
    private void showSecondTree() {
        if (bo_isShowSecondTree) {
            initialize(); //
        } else {
            initialize2(); //
        }
    }

    private void refreshSecondTree(DefaultMutableTreeNode _node) {
        rootNode2.removeAllChildren(); //
        if (_node != null) {
            TreeNode[] nodes_1 = _node.getPath(); //
            if (nodes_1 != null && nodes_1.length > 1) {
                DefaultMutableTreeNode[] newNode = new DefaultMutableTreeNode[nodes_1.length - 1];

                for (int i = 0; i < newNode.length; i++) {
                    newNode[i] = new DefaultMutableTreeNode(nodes_1[i + 1].toString()); //
                }

                for (int i = 0; i < newNode.length - 1; i++) {
                    newNode[i].add(newNode[i + 1]);
                }
                rootNode2.add(newNode[0]);
                expandAll(jTree2, true); // 展示整个树!!
            }
        }
        jTree2.updateUI(); //
    }

    /**
     * 快速定位
     *
     */
    private void quickLocate() {
        // 先弹出一个对话框,然后根据返回条件,查找所有结点,只要满足其的就
        QuickLocateDialog dialog = new QuickLocateDialog(this);
        dialog.setVisible(true); //
    }

    private void showSQL() {
        System.out.println(this.str_realsql); //
        JOptionPane.showMessageDialog(this, this.str_realsql); //
    }

    private FrameWorkMetaDataService getFWMetaService() throws NovaRemoteException, Exception {
        FrameWorkMetaDataService service = (FrameWorkMetaDataService) NovaRemoteServiceFactory.getInstance()
            .lookUpService(FrameWorkMetaDataService.class);
        return service;
    }

    public JTree getJTree() {
        return jTree;
    }

    public DefaultTreeModel getJTreeModel() {
        return (DefaultTreeModel) jTree.getModel();
    }

    public Pub_Templet_1_ItemVO[] getTempletItemVOs() {
        return templetItemVOs;
    }

    public Pub_Templet_1VO getTempletVO() {
        return templetVO;
    }

    public String[] getIconNames() {
        return iconNames;
    }

    public void setIconNames(String[] iconNames) {
        this.iconNames = iconNames;
    }

    public String getIconName(int _level) {
        if (this.iconNames == null) {
            return null;
        }

        if (this.iconNames.length >= _level) {
            return iconNames[_level - 1];
        }
        if (iconNames.length == 1) {
            return iconNames[0];
        }
        return null;
    }

    public String getLeafNodeIcon() {
        if (leaficonNames == null) {
            if (iconNames != null && iconNames.length > 0) {
                return iconNames[iconNames.length - 1];
            }
        }
        return leaficonNames;
    }

    public void setLeafIcon(String icon) {
        leaficonNames = icon;
    }

    /**
     * 增加某个结点,数据类型必须是BillVO
     */
    public void addNode(BillVO _billVO) {
        DefaultMutableTreeNode node = getSelectedNode();
        if (node == null) {
            return;
        }
        _billVO.setToStringKeyName(this.str_treeViewFieldName); // 设置显示名称!!
        getJTreeModel().insertNodeInto(new DefaultMutableTreeNode(_billVO), node, node.getChildCount()); //
        getJTree().expandPath(getSelectedPath()); // 展开该结点
    }

    /**
     * 删除当前结点,只是从页面上删除!!
     */
    public void delCurrNode() {
        DefaultMutableTreeNode node = getSelectedNode();
        if (node == null) {
            return;
        }

        getJTreeModel().removeNodeFromParent(node); //
    }

    class MyTreeRender extends DefaultTreeCellRenderer {

        private static final long serialVersionUID = 4104163070905939850L;

        TreePath path;

        DefaultMutableTreeNode[] allNodes = null;

        public MyTreeRender(TreePath path) {
            this.path = path;
            if (path != null) {
                allNodes = (DefaultMutableTreeNode[]) Arrays.asList(path.getPath()).toArray(
                    new DefaultMutableTreeNode[0]);
            }
        }

        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
            boolean leaf, int row, boolean hasFocus) {
            JLabel cell = (JLabel)super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value; // 当前Node!
            boolean iffind = false;
            if (allNodes != null) {
                for (int i = 0; i < allNodes.length; i++) {
                    if (allNodes[i] == node) {
                        iffind = true;
                        break;
                    }
                }
            }

            if (iffind) { // 如果找到!!
                cell.setForeground(Color.red);
            } else {
                cell.setForeground(Color.black);
            }
            if (node.isLeaf()) {
                String iconname = getLeafNodeIcon();
                if (iconname != null) {
                    ImageIcon icon = UIUtil.getImage(iconname);
                    if (icon != null) {
                        cell.setIcon(icon);
                    }
                }
            } else {
                int li_level = node.getLevel();
                if (li_level > 0) { // 如果不是根结点!!
                    String iconName = getIconName(li_level); //
                    if (iconName != null) {
                        ImageIcon icon = UIUtil.getImage(iconName);
                        if (icon != null) {
                            cell.setIcon(icon);
                        }
                    }
                }
            }

            return cell;
        }
    }

    class SecondTreeRender extends DefaultTreeCellRenderer {

        private static final long serialVersionUID = 2347459122605778243L;

        public SecondTreeRender() {
        }

        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
            boolean leaf, int row, boolean hasFocus) {
            JLabel cell = (JLabel)super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            cell.setOpaque(true);
            cell.setIcon(null); //
            cell.setBackground(new Color(240, 240, 240));
            return cell;
        }
    }

    class QuickLocateDialog extends NovaDialog {

        private static final long serialVersionUID = -3876863474793078470L;

        private Hashtable ht_condition = null;

        public QuickLocateDialog(Container _parent) {
            super(_parent, "快速定位", 622, 300);
            this.setModal(true); //
            initialize();
        }

        private void initialize() {
            this.getContentPane().setLayout(new BorderLayout()); //

            String[] str_keys = templetVO.getItemKeys();
            for (int i = 0; i < str_keys.length; i++) {
                String str_queryLevel = templetVO.getItemVo(str_keys[i]).getDelfaultquerylevel(); //
                boolean ifShow = false;
                if (str_queryLevel != null && str_queryLevel.equals("1")) {
                    ifShow = true;
                }
                templetVO.getItemVo(str_keys[i]).setCardisshowable(new Boolean(ifShow)); // 不显示!!
            }

            BillCardPanel card = new BillCardPanel(templetVO);
            card.setEditable(true); //
            this.getContentPane().add(card, BorderLayout.CENTER);
            this.getContentPane().add(getSouthPanel(), BorderLayout.SOUTH);

        }

        private JPanel getSouthPanel() {
            JPanel panel = new JPanel(); //
            panel.setLayout(new FlowLayout());
            JButton btn_confirm = new JButton("确定");
            JButton btn_cancel = new JButton("取消");

            btn_confirm.setPreferredSize(new Dimension(75, 20));
            btn_cancel.setPreferredSize(new Dimension(75, 20));

            btn_confirm.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    onConfirm();
                }
            });
            btn_cancel.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    onCancel();
                }
            });
            panel.add(btn_confirm);
            panel.add(btn_cancel);
            return panel;
        }

        private void onConfirm() {
            ht_condition = new Hashtable();

            this.dispose();
        }

        private void onCancel() {
            ht_condition = null;
            this.dispose();
        }

        public Hashtable getCondition() {
            return ht_condition;
        }
    }
    //获取右键菜单项
    public JMenuItem[] getPopupMenuItem()
    {
    	if(menuItem!=null)
    		return (JMenuItem[])menuItem.toArray(new JMenuItem[0]);
    	return null;
    }
    //设置右键菜单项
    public void addPopupMenuItems(JMenuItem item)
    {
    	if(menuItem==null)
    		menuItem = new Vector();
    	menuItem.addElement(item);
    }
    //设置右键菜单项
    public void addPopupMenuItems(JMenuItem[] items)
    {
    	if(menuItem==null)
    		menuItem = new Vector(items.length);
    	for(int i=0;i<items.length;i++)
    		menuItem.addElement(items[i]);
    }
	public TransferHandler getTransferHandler() {
		return handler;
	}
	//使所有结点都可以托拽
	public void setDragEnableForAll()
	{
		enable_all_drag = true;
	}
}
