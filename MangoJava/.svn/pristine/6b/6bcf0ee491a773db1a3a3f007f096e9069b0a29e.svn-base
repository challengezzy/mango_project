package smartx.framework.metadata.ui.componentscard;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import smartx.framework.common.ui.NovaConstants;
import smartx.framework.common.ui.UIUtil;
import smartx.framework.common.vo.HashVO;
import smartx.framework.metadata.ui.NovaDialog;
import smartx.framework.metadata.ui.NovaMessage;
import smartx.framework.metadata.vo.RefItemVO;
import smartx.framework.metadata.vo.TableDataStruct;

public class UIRefMutiTreeDialog extends NovaDialog implements RefDialogIFC {
	private static final long serialVersionUID = 1L;

	private static int id_index = 0;

	private static int name_index = 2;

	private TableDataStruct struct = null;

	private JTree jt_menu = null;

	private JTree jt_sub = null;

	private JTable table = null;

	private String str_RefName;

	private String str_treesql;

	private String tree_ID = null;

	private String tree_parentID = "parentmenuid";

	private JButton[] jbt_operator = null;

	private JTextField jtf_search = null;

	private String[][] str_data = null;

	private DefaultTableModel tableModel = null;

	private String[] table_header = null;

	private boolean tableFlag = false;

	private boolean treeFlag = false;

	private HashVO[] subtree_vo = null;

	private int li_closeType = -1;

	private JPanel jpn_btn = null;

	private ActionListener listener = null;

	private KeyAdapter adapter = null;

	private MouseAdapter m_adapter = null;

	private String str_datasourcename = null; // 数据源名称

	private String str_table_sql = null;

	private String str_table_foreignkey = null;
	
	private String ref_id = null;
	private boolean loadall = true;
	  //存储在懒加载时，已经搜寻过的标点，在双击时不再去数据库取数据
    protected LinkedList dealedNodeList = null;
    
    protected RefItemVO refVO=null;
    
    
	public UIRefMutiTreeDialog(Container _parent, String _name, String refid,
			String _refname_str, String _treesql, String _id, String _parentid,
			String _tablesql, String _tablefk, String _datasourcename)
			throws Exception {
		this(_parent, _name, refid, _refname_str, _treesql, _id, _parentid,
				_tablesql, _tablefk, _datasourcename, "FALSE");
	}

	public UIRefMutiTreeDialog(Container _parent, String _name, String refid,
			String _refname_str, String _treesql, String _id, String _parentid,
			String _tablesql, String _tablefk, String _datasourcename,
			String loadall) throws Exception {
		super(_parent, _name, 700, 500); //
		checkLoadAll(loadall);
		this.ref_id = refid;
		this.str_treesql = _treesql;
		this.tree_ID = _id;
		this.tree_parentID = _parentid;
		this.str_RefName = _refname_str;
		this.str_table_sql = _tablesql;
		this.str_table_foreignkey = _tablefk;
		this.str_datasourcename = _datasourcename; //
		initialize(); //
		this.setVisible(true);
	}
	protected void checkLoadAll(String isload) {
		if (isload.equalsIgnoreCase("FALSE"))
			this.loadall = false;
	}
	private void initialize() throws Exception {
		getData(UIUtil.rebuildLazyLoadTreeSQL(this.str_table_sql,null));
		listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dealAcitonPerform(e);
			}
		};
		adapter = new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				dealKeyPerform(e);
			}
		};
		m_adapter = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				dealMousePerform(e);
			}
		};
		getMenuTree();
		getSubMenuTree(null);

		JPanel jpn_left = new JPanel();
		jpn_left.setLayout(new BorderLayout());

		JScrollPane jsp_tree = new JScrollPane(jt_menu);
		JScrollPane jsp_subtree = new JScrollPane(jt_sub);
		jt_sub.setBackground(new Color(240, 240, 240));
		JSplitPane splitMenuPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				jsp_tree, jsp_subtree);

		splitMenuPane.setDividerLocation(275);
		splitMenuPane.setDividerSize(5);
		splitMenuPane.setOneTouchExpandable(true);

		jbt_operator = new JButton[4];
		JPanel searchPanel = getSearchPanel();
		jpn_left.add(splitMenuPane, BorderLayout.CENTER);
		jpn_left.add(searchPanel, BorderLayout.NORTH);

		JPanel jpn_right = new JPanel();

		JScrollPane jsp_table = getJSPTable();
		int temp = getInitRow();
		if (temp >= 0) {
			Rectangle rect = table.getCellRect(temp, 0, true);
			table.scrollRectToVisible(rect);
			table.setRowSelectionInterval(getInitRow(), getInitRow());
		}
		tableFlag = true;
		resetTree();
		tableFlag = false;
		jbt_operator[0] = getBtn("确定", new Dimension(85, 20));
		jbt_operator[2] = getBtn("取消", new Dimension(85, 20)); // 将原来关闭改名为取消,将原来的取消注销了!!

		jpn_btn = new JPanel();
		jpn_btn.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));
		jpn_btn.add(jbt_operator[0]);
		// jpn_btn.add(jbt_operator[1]); //将取消按钮去掉!!
		jpn_btn.add(jbt_operator[2]);
		jpn_btn.addMouseListener(m_adapter);

		jpn_right.setLayout(new BorderLayout());
		jpn_right.add(jsp_table, BorderLayout.CENTER);
		jpn_right.add(new JLabel("  "), BorderLayout.EAST);
		jpn_right.add(new JLabel("  "), BorderLayout.WEST);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				jpn_left, jpn_right);
		splitPane.setDividerLocation(220);
		splitPane.setDividerSize(10);
		splitPane.setOneTouchExpandable(true);

		this.getContentPane().add(splitPane, BorderLayout.CENTER);
		this.getContentPane().add(jpn_btn, BorderLayout.SOUTH);

	}

	/**
	 * 集中处理鼠标事件
	 * 
	 * @param e
	 */
	protected void dealMousePerform(MouseEvent e) {
		Object obj = e.getSource();
		if (obj.equals(jpn_btn)) {
			if (e.getButton() == MouseEvent.BUTTON3) {
				showSQL();
			}
		} else if (obj.equals(table)) {
			if (treeFlag) {
				return;
			}
			tableFlag = true;
			resetTree();
			getFocuse();
			tableFlag = false;
		}
	}

	/**
	 * 集中处理按钮事件
	 * 
	 * @param e
	 */
	protected void dealAcitonPerform(ActionEvent e) {
		Object obj = e.getSource();
		if (obj.equals(jbt_operator[0])) {
			onConfirm();
		} else if (obj.equals(jbt_operator[1])) {
			onCancel();
		} else if (obj.equals(jbt_operator[2])) {
			onClose();
		} else if (obj.equals(jbt_operator[3])) {
			dealSearch(jtf_search.getText());
		}
	}

	/**
	 * 处理键盘事件
	 * 
	 * @param e
	 */
	protected void dealKeyPerform(KeyEvent e) {
		Object obj = e.getSource();
		if (obj.equals(jtf_search)) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				dealSearch(jtf_search.getText());
			}
		} else if (obj.equals(jt_menu)) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) jt_menu
						.getLastSelectedPathComponent();

				if (node.isLeaf()) {
					return;
				}
				DefaultMutableTreeNode node_child = (DefaultMutableTreeNode) node
						.getChildAt(0);

				DefaultTreeModel model = (DefaultTreeModel) jt_menu.getModel();

				TreeNode[] nodes = model.getPathToRoot(node_child);
				TreePath path = new TreePath(nodes);

				jt_menu.makeVisible(path);
				jt_menu.scrollPathToVisible(path);
			}
		}
	}

	/**
	 * 获得左边树上边的搜索框
	 * 
	 * @return
	 */
	private JPanel getSearchPanel() {
		JPanel panel = new JPanel();

		jtf_search = new JTextField();
		jtf_search.setPreferredSize(new Dimension(90, 20));
		jtf_search.addKeyListener(adapter);

		jbt_operator[3] = getBtn(UIUtil.getImage("images/platform/find.gif"),
				new Dimension(18, 18));

		FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.CENTER);
		panel.setLayout(layout);
		panel.add(jtf_search);
		panel.add(jbt_operator[3]);

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
			jbt_temp = new JButton((Icon) _obj);
		}
		jbt_temp.setPreferredSize(_demension);
		jbt_temp.addActionListener(listener);
		jbt_temp.addKeyListener(adapter);
		return jbt_temp;
	}

	/**
	 * 根据_name来处理搜索
	 * 
	 * @param _name
	 */
	private void dealSearch(String _name) {
		DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) jt_menu
				.getModel().getRoot();
		Enumeration e = rootNode.preorderEnumeration();
		while (e.hasMoreElements()) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) e
					.nextElement();
			if (node.isRoot()) {
				continue;
			}
			HashVO vo = (HashVO) node.getUserObject();
			String temp_name = vo.getStringValue(2); //
			int compareCount = temp_name.indexOf(_name.trim());
			if (compareCount == 0) {
				DefaultTreeModel model = (DefaultTreeModel) jt_menu.getModel();

				TreeNode[] nodes = model.getPathToRoot(node);
				TreePath path = new TreePath(nodes);

				jt_menu.makeVisible(path);
				jt_menu.setSelectionPath(path);
				jt_menu.scrollPathToVisible(path);
				return;
			}
		}
		JOptionPane.showMessageDialog(this, "非常抱歉，没有检测到你要找的节点！");
		return;
	}

	/**
	 * 根据母节点_node来构建子树
	 * 
	 * @param _node
	 */
	private void getSubMenuTree(DefaultMutableTreeNode _node) {
		subtree_vo = getSubHashVO(_node);

		if (subtree_vo == null) {
			DefaultMutableTreeNode root = new DefaultMutableTreeNode("根节点"); // 创建根结点
			DefaultTreeModel subtree_model = new DefaultTreeModel(root);
			jt_sub = new JTree(subtree_model);

			TreeNode[] nodes = subtree_model.getPathToRoot(root);
			TreePath path = new TreePath(nodes);
			jt_sub.scrollPathToVisible(path);
			jt_sub.setSelectionPath(path);
			jt_sub.makeVisible(path);

			SubMenuTreeRender aMyTreeRender = new SubMenuTreeRender(path);
			jt_sub.setCellRenderer(aMyTreeRender);

			return;
		}
		if (jt_sub == null) {
			DefaultMutableTreeNode root = new DefaultMutableTreeNode("根节点"); // 创建根结点
			DefaultTreeModel subtree_model = new DefaultTreeModel(root);
			jt_sub = new JTree(subtree_model);
		}

		DefaultTreeModel subtree_model = (DefaultTreeModel) jt_sub.getModel();
		if (((DefaultMutableTreeNode) subtree_model.getRoot()).getChildCount() > 0) {
			subtree_model
					.removeNodeFromParent((DefaultMutableTreeNode) subtree_model
							.getChild(subtree_model.getRoot(), 0));
		}
		DefaultMutableTreeNode node_root = (DefaultMutableTreeNode) subtree_model
				.getRoot(); // 创建根结点
		DefaultMutableTreeNode newChild = null;
		for (int i = 0; i < subtree_vo.length; i++) {
			newChild = new DefaultMutableTreeNode(subtree_vo[i]);
			subtree_model.insertNodeInto(newChild, node_root, 0);
			node_root = (DefaultMutableTreeNode) subtree_model.getChild(
					node_root, 0);

			TreeNode[] nodes = subtree_model.getPathToRoot(node_root);
			TreePath path = new TreePath(nodes);
			jt_sub.scrollPathToVisible(path);
			jt_sub.setSelectionPath(path);
			jt_sub.makeVisible(path);

			SubMenuTreeRender aMyTreeRender = new SubMenuTreeRender(path);
			jt_sub.setCellRenderer(aMyTreeRender);
		}
	}

	/**
	 * 根据母节点来获得子树的HashVO[]
	 * 
	 * @param _node
	 * @return
	 */
	private HashVO[] getSubHashVO(DefaultMutableTreeNode _node) {
		if (_node == null) {
			return null;
		}
		Vector _vec = new Vector();
		while (true) {
			if (_node.isRoot()) {
				break;
			}
			HashVO vo = (HashVO) _node.getUserObject();
			_vec.add(vo);
			_node = (DefaultMutableTreeNode) _node.getParent();
		}
		if (_vec.size() > 0) {
			HashVO[] _vo = new HashVO[_vec.size()];
			for (int i = 0; i < _vec.size(); i++) {
				_vo[i] = (HashVO) _vec.get(_vec.size() - 1 - i);
			}
			return _vo;
		}
		return null;
	}

	/**
	 * 获得表数据的初始行，主要在初始化时调用
	 * 
	 * @return
	 */
	private int getInitRow() {
		int li_rowcount = table.getModel().getRowCount();
		String temp_str;
		for (int i = 0; i < li_rowcount; i++) {
			temp_str = (String) this.table.getModel().getValueAt(i, name_index);
			if (temp_str.equals(this.str_RefName)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 获得带有滚动条的JTable
	 * 
	 * @return
	 */
	private JScrollPane getJSPTable() {
		ListAndTableFactoty latf_table = new ListAndTableFactoty();

		JScrollPane jsp_1 = latf_table
		.getJSPTable(str_data, table_header, null);
		latf_table.setAllColumnUnEditeable();
		table = latf_table.getTable();
		tableModel = latf_table.getTableModel();
		table.addMouseListener(m_adapter);
		return jsp_1;
	}

	/**
	 * 根据表的选择行，重新设置树
	 */
	protected void resetTree() {
		if(table.getSelectedRow()==-1)
			return;
		int pk_index = findIndex(this.str_table_foreignkey);
		if(pk_index==-1)
			return;
		int tree_id = Integer.parseInt(table.getModel().getValueAt(table.getSelectedRow(), pk_index).toString());
		getTreePath(tree_id+"");
	}
	protected int findIndex(String key)
	{
		if(key==null||key.equals(""))
			return -1;
		for (int i = 0; i < table_header.length; i++) {
			if(table_header[i].equalsIgnoreCase(key))
			{
				return i;
			}
		}
		return -1;
	}
	/**
	 * 获得选择的节点,主要指jt_menu
	 * 
	 * @param _id
	 * @return
	 */
	private DefaultMutableTreeNode getSelectedNode(String _id) {
		DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) jt_menu
				.getModel().getRoot();
		Enumeration e = rootNode.breadthFirstEnumeration();
		while (e.hasMoreElements()) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) e
					.nextElement();
			if (node.isRoot()) {
				continue;
			}
			HashVO vo = (HashVO) node.getUserObject();
			String temp_id = vo.getStringValue(0);
			if (temp_id.equals(_id)) {
				return node;
			}
		}
		return null;
	}

	/**
	 * 设置选择路径的颜色
	 * 
	 * @param _node
	 */
	private void setSelectedPathColor(DefaultMutableTreeNode _node) {
		if (_node == null) {
			return;
		}

		DefaultTreeModel model = (DefaultTreeModel) jt_menu.getModel();

		TreeNode[] nodes = model.getPathToRoot(_node);
		TreePath path = new TreePath(nodes);

		MyTreeRender aMyTreeRender = new MyTreeRender(path);
		jt_menu.setCellRenderer(aMyTreeRender);
	}

	/**
	 * 根据_id来获得树的路径
	 * 
	 * @param _id
	 * @return
	 */
	private TreePath getTreePath(String _id) {
		DefaultMutableTreeNode node = getSelectedNode(_id);
		setSelectedPathColor(node);
		this.getSubMenuTree(node);
		if (node == null) {
			return null;
		}
		DefaultTreeModel model = (DefaultTreeModel) jt_menu.getModel();

		TreeNode[] nodes = model.getPathToRoot(node);
		TreePath path = new TreePath(nodes);

		jt_menu.makeVisible(path);
		jt_menu.setSelectionPath(path);
		jt_menu.scrollPathToVisible(path);
		return path;
	}

	/**
	 * 初始化表数据和表头
	 * 
	 * @return
	 * @throws Exception
	 */
	private Object[][] getData(String _sql) throws Exception {
		if (_sql != null && !_sql.equals("")) {
			struct = UIUtil.getTableDataStructByDS(this.str_datasourcename,
					_sql); //
			table_header = struct.getTable_header();
			str_data = struct.getTable_body();
		}
		return str_data;
	}

	/**
	 * 获得menu树
	 */
	private void getMenuTree() {
		if (jt_menu == null) {
			if(this.loadall)
				jt_menu = UIUtil.getJTreeByParentPK_HashVO(str_datasourcename, "根结点", this.str_treesql, this.tree_ID,this.tree_parentID);
			else
				jt_menu = UIUtil.getLazyLoadJTree(str_datasourcename, "根结点", str_treesql, this.tree_parentID);
			jt_menu.addTreeSelectionListener(new TreeSelectionListener() {
				public void valueChanged(TreeSelectionEvent evt) {
					if (tableFlag) {
						return;
					}
					treeFlag = true;
					TreePath[] paths = evt.getPaths();

					for (int i = 0; i < paths.length; i++) {
						if (evt.isAddedPath(i)) {
							DefaultMutableTreeNode node = (DefaultMutableTreeNode) paths[i]
									.getLastPathComponent();
							try {
								onChangeSelectTree(node);
							} catch (Exception e) {
								System.out.println("刷新列表失败：");
								e.printStackTrace();
							} 
						}
					}
					treeFlag = false;
				}
			});
			jt_menu.addMouseListener(new MyMouseListener());
			jt_menu.addKeyListener(adapter);
			jt_menu.setRootVisible(false); //
		}
		return;
	}


	/**
	 * 处理树的选择节点的变更的事件
	 * 
	 * @param _node
	 */
	private void onChangeSelectTree(DefaultMutableTreeNode _node)
			throws Exception {
		if (_node.isRoot()) {
			refreshTable(this.str_table_sql);
		} else {
			setSelectedPathColor(_node);
			getSubMenuTree(_node);
			HashVO vo_node = (HashVO) _node.getUserObject();

			String str_id = vo_node.getStringValue(0);
			refreshTable("select * from ("+this.str_table_sql + ") where "
						+ str_table_foreignkey + "='" + str_id + "'");

		}
	}

	/**
	 * 刷新数据表
	 */
	private void refreshTable(String sql) throws Exception {
		getData(sql);
		clearTable();
		for (int i = 0; i < str_data.length; i++) {
			Vector _vec = new Vector();
			for (int j = 0; j < str_data[i].length; j++) {
				_vec.add(str_data[i][j]);
			}
			tableModel.addRow(_vec);
		}
		table.updateUI();

	}

	/**
	 * 清空数据表
	 */
	public void clearTable() {
		int li_rowcount = tableModel.getRowCount();
		for (int i = li_rowcount - 1; i >= 0; i--) {
			tableModel.removeRow(i);
		}
		table.updateUI();
	}

	protected void getFocuse() {
	}

	protected void onConfirm() {
		if(table.getSelectedRowCount()!=1)
		{
			JOptionPane.showMessageDialog(this, "只能选择一行数据");
			return;
		}
		int row = table.getSelectedRow();
		
		HashVO vo = new HashVO();
		
		this.refVO=new RefItemVO(vo);
		li_closeType = 0;
		this.dispose();
	}

	private void onCancel() {
		this.refVO=null;
		li_closeType = 1;
		this.dispose();
	}

	protected void onClose() {
		li_closeType = 2;
		this.dispose();
	}

	
	class MyCellEditor extends DefaultCellEditor {
		private static final long serialVersionUID = 1259100940105603890L;

		public MyCellEditor() {
			this(new JTextField());
		}

		public MyCellEditor(JTextField _textfield) {
			super(_textfield);
		}

		public boolean isCellEditable(EventObject evt) {
			if (evt instanceof MouseEvent) {
				int li_count = ((MouseEvent) evt).getClickCount();
				if (li_count >= 3) {
					return true;
				} else {
					return false;
				}
			}
			return false;
		}

	}
	 protected class MyMouseListener extends MouseAdapter {

	        public void mouseClicked(MouseEvent e) {
	        	if (!loadall && e.getClickCount() == 2) {
					TreePath path = jt_menu.getSelectionPath();
					if (path == null) {
						return;
					}
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
							.getLastPathComponent();
					if (node.isRoot()) {
						return;
					}
					if (dealedNodeList == null)
						dealedNodeList = new LinkedList();
					for(int i=0; i<dealedNodeList.size(); i++)
					{
						if(dealedNodeList.get(i).equals(((HashVO) node.getUserObject())
							.getStringValue(tree_ID)))
							return;
					}
					
					try {
						UIUtil.fetchChildofTreeNode(str_datasourcename,
								str_treesql, node, tree_parentID, ((HashVO) node
										.getUserObject()).getStringValue(tree_ID));
					} catch (Exception e1) {
						NovaMessage.show(jt_menu, e1.getMessage(), NovaConstants.MESSAGE_ERROR);
					}
					dealedNodeList.add(((HashVO) node.getUserObject())
							.getStringValue(tree_ID));
					if (node.getChildCount() != 0) {
						DefaultTreeModel model = (DefaultTreeModel) jt_menu.getModel();
						TreePath p = new TreePath(model.getPathToRoot(node
								.getFirstChild()));
						jt_menu.expandPath(p);
						jt_menu.setSelectionPath(p);
						jt_menu.scrollPathToVisible(p);
						jt_menu.updateUI();
					}
					
				}
	        }
	    }
	class MyTreeRender extends DefaultTreeCellRenderer {
		private static final long serialVersionUID = 4104163070905939850L;

		TreePath path;

		public MyTreeRender(TreePath path) {
			this.path = path;
		}

		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean sel, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {
			Component cell = super.getTreeCellRendererComponent(tree, value,
					sel, expanded, leaf, row, hasFocus);
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
			DefaultMutableTreeNode[] nodes = (DefaultMutableTreeNode[]) Arrays
					.asList(path.getPath()).toArray(
							new DefaultMutableTreeNode[0]);
			boolean iffind = false;
			for (int i = 0; i < nodes.length; i++) {
				if (nodes[i] == node) {
					iffind = true;
					break;
				}
			}

			if (iffind) {
				cell.setForeground(Color.red);
			} else {
				cell.setForeground(Color.black);
			}
			return cell;
		}
	}

	/**
	 * 对话框的操作类型：确认/取消/关闭
	 * @return
	 */
	public int getCloseType() {
		return li_closeType;
	}

	/**
     * 返回参照选项值
     * @return
     */
	public RefItemVO getRefVO() {
		return this.refVO;
	}


	private void showSQL() {
		System.out.println(this.str_treesql);
		JOptionPane.showMessageDialog(this, this.str_treesql); //
	}

	class SubMenuTreeRender extends DefaultTreeCellRenderer {
		private static final long serialVersionUID = 2347459122605778243L;

		TreePath path;

		public SubMenuTreeRender(TreePath path) {
			this.path = path;
		}

		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean sel, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {
			JLabel cell = (JLabel) super.getTreeCellRendererComponent(tree,
					value, sel, expanded, leaf, row, hasFocus);
			cell.setOpaque(true);
			cell.setBackground(new Color(250, 250, 250));
			cell.setBackground(new Color(240, 240, 240));
			cell.setEnabled(false);
			return cell;
		}
	}

}