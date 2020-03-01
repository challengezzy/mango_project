/**************************************************************************
 * $RCSfile: AbstractTempletFrame03.java,v $  $Revision: 1.15.2.20 $  $Date: 2010/02/05 02:17:29 $
 **************************************************************************/

package smartx.publics.styletemplet.ui.templet03;


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
 * 模板3 左边树，右边卡。。。右边卡中的纪录为树结点的信息。对树的结点进行操作。 模板3-2
 * 左边树，右边卡。右边卡中的纪录为根据树找到的另外一张表的一条纪录，此表与树相关联。
 */
public abstract class AbstractTempletFrame03 extends AbstractStyleFrame implements NovaEventListener {

	protected String curname="";
	protected int numF3=0;
	protected Hashtable ht;                                        //缓冲所有已经加载节点
	protected HashVO[] hashVOc=null;                               //TODO 检索树节点时的检索返回列表
	protected ArrayList al=null; //用来存储每一级遍历的节点
    protected String str_templetecode = null;
    protected String str_roottitle = null;
    protected String str_treetitle = null;
    protected String str_treesql = null;
    protected String str_treepk = null;
    protected String str_treeparentpk = null;
    protected String[] menu = null;

    protected IUIIntercept_03 uiIntercept = null;
    protected String bsinterceptor = null;
    protected String customerpanel = null;

    protected AbstractCustomerButtonBarPanel panel_customer = null;  //自定义面板
    protected DefaultMutableTreeNode curnode = null;

    protected JTree ltree = null;                                    //树对象
    protected JTextField jtf_search = null;                          //树检索控件 
    protected BillCardPanel card = null;                             //树节点编辑面板
    protected JButton btn_save=null,btn_cancel=null;
    
    protected boolean oninsert = false;
    
    protected ArrayList treePath = null;

    protected int currentShowPos = -1;
    protected boolean cascadeondelete = false;
    
    //存储在懒加载时，已经搜寻过的标点，在双击时不再去数据库取数据
    protected LinkedList dealedNodeList = null;
    
    
    public AbstractTempletFrame03() {
        super();
        init();
    }

    public AbstractTempletFrame03(String _title) {
        super(_title);
    }

    public void init() {
        str_templetecode = getTempletcode();
        str_roottitle = getRootTitle();
        str_treetitle=getTreeTitle();
        str_treepk = getTreePrimarykey();
        str_treeparentpk = getTreeParentkey();
        str_treesql = getTreeSQL();
        menu = getSys_Selection_Path();
        customerpanel = getCustomerpanel();
        /**
         * 创建UI端拦截器
         */
        if (getUiIntercept() != null && !getUiIntercept().trim().equals("")) {
            try {
                uiIntercept = (IUIIntercept_03) Class.forName(getUiIntercept().trim()).newInstance(); //
            } catch (Exception e) {
                //e.printStackTrace();
                NovaMessage.showException(this, e);
            }
        }

        bsinterceptor = getBsinterceptor();
        cascadeondelete = isCascadeonDelete();
        this.setTitle(getTempletTitle());
        this.setSize(getTempletSize());
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(getBtnPanel(), BorderLayout.NORTH);
        this.getContentPane().add(getBody(), BorderLayout.CENTER);
//		this.setVisible( true ); //
//		this.toFront( );
        card.addNovaEventListener(this); // 注册自己事件监听!!
    }

    public abstract String getTempletcode(); //

    public abstract String getTreeSQL();

    public abstract String getTreePrimarykey();

    public abstract String getTreeParentkey();
    
    public abstract String getTreeTitle();
    
    /**
     * 获得检索键
     * @return
     */
    public abstract String getFetchKey();
    
    public String[] getSys_Selection_Path() {
        return menu;
    }

    /**
     * 是否显示系统按钮。需要子类继承实现本方法
     * @return
     */
    public boolean isShowsystembutton() {
        return true;
    }

    public boolean isCascadeonDelete() {
        return cascadeondelete;
    }

    public String getCustomerpanel() {
        return customerpanel;
    }

    public  String getRootTitle(){
    	return "根节点";
    }

    public String getUiIntercept() {
        return "";
    }

    public String getBsinterceptor() {
        return bsinterceptor;
    }

    protected Dimension getTempletSize() {
        return new Dimension(1000, 750);
    }

    public String getTempletCode() {
        return this.str_templetecode;
    }

    /**
     * 获取窗口标题
     *
     * @return String
     */
    protected String getTempletTitle() {
        return (menu==null)?str_templetecode:(menu[menu.length - 1]+" ["+getNavigation()+"]");        
    }
    
    /**
     * 获取系统导航
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

    protected JPanel getBody() {
        JPanel rpanel = new JPanel();
        rpanel.setLayout(new BorderLayout());
        JSplitPane jsp = new JSplitPane();
        jsp.setOneTouchExpandable(true);
        jsp.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        jsp.setDividerLocation(200);
        jsp.setRightComponent(getCardPanel());
        jsp.setLeftComponent(getLeftPanel());
        rpanel.add(jsp, BorderLayout.CENTER);
        return rpanel;
    }

    protected JPanel getLeftPanel() {
        JPanel rpanel = new JPanel();
        rpanel.setLayout(new BorderLayout());        
        rpanel.add(getTreePanel(), BorderLayout.CENTER);
        return rpanel;
    }


    /**
     * 所有按钮面板
     *
     * @return JPanel
     */
    protected JComponent getBtnPanel() {
    	JToolBar tbar = new JToolBar();
        tbar.setFloatable(false);
        tbar.setBorderPainted(false);
        tbar.add(new JLabel("操作："));
        
        if (isShowsystembutton()) {
            getSysBtnPanel(tbar);
        }else{        
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
	        }else{
	        	tbar.add(new JLabel(" "));
	        }
        }
        
        return tbar;
    }
    
    protected void getSysBtnPanel(JToolBar tbar) {
    	tbar.add(new JLabel("检索"));
    	jtf_search = new JTextField();
    	jtf_search.setMaximumSize(new Dimension(90, 20));
        jtf_search.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                dealKeyPerform(e);
            }
        });
        tbar.add(jtf_search);
    	
           
    	JButton btn = UIComponentUtil.getButton(
            "检索节点", 
            UIUtil.getImage(Sys.getSysRes("edit.query.icon")),
            UIComponentUtil.getButtonDefaultSize(),
            true,false,
            new ActionListener() {
                public void actionPerformed(ActionEvent a) {
                	dealSearch(jtf_search.getText());
                }
            },
            new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                	dealKeyPerform(e);
                }
            }
        );        
        tbar.add(btn);      
        //树节点编辑面板的保存按钮    
        btn_save = UIComponentUtil.getButton(
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
        // 树节点编辑面板的放弃保存按钮
        btn_cancel = UIComponentUtil.getButton(
        	Sys.getSysRes("edit.canceledit.msg"), 
            UIUtil.getImage(Sys.getSysRes("edit.canceledit.icon")),
            UIComponentUtil.getButtonDefaultSize(),
            true,false,
            new ActionListener() {
                public void actionPerformed(ActionEvent a) {
                	onCancel();
                }
            }
        );
        tbar.add(btn_save);      
        tbar.add(btn_cancel);
        setBtnEnable(false); 
        
        tbar.addSeparator();
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
     * 导航路径
     *
     * @return String[]
     */
    public String[] getNagivationPath() {
        return menu;
    }

    public String getCustomerPanelNames() {
        return this.customerpanel;
    }

    public JTable getTable() {
        return null;
    }

    

    /**
     * 处理键盘事件
     *
     * @param e
     */
    protected void dealKeyPerform(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
        	SwingUtilities.invokeLater(new dealkeysearch());
        } else if (e.getKeyCode() == KeyEvent.VK_F3) {
			SwingUtilities.invokeLater(new dealkeysearch());		
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
     * 根据一个节点的值，递归查找其父节点直到最后一层
     * @param pkvalue
     */
    private void getLoadNode(String pkvalue){   	
    	if(al==null){
    		al=new ArrayList();
    	}
    	if(pkvalue==null){
    		return ;
    	}
    	String parid=null;
    	HashVO [] hashvo=null;
    	try {
			hashvo = UIUtil.getHashVoArrayByDS( card.getDataSourceName(),UIUtil.rebuildLazyLoadTreeSQL(str_treesql)+" and "+str_treepk+"='"+pkvalue+"'");
		} catch (NovaRemoteException e) {
			e.printStackTrace();
		} catch (Exception e) {	
			e.printStackTrace();
		}
		if(hashvo.length==0){
			return;
		}else{
			parid=hashvo[0].getStringValue(str_treeparentpk); //取出当前节点的父节点
		}
		
		if(parid!=null){                                //如果没到根节点则继续查找
			if(ht.containsKey(parid)){          //如果当前结点在hashtable中则直接加载，如果没有则继续查其父节点 如果找到则直接加载				
			al.add(parid);
			for(int i=0;i<al.size();i++){
				String psid=(String)al.get(al.size()-1-i);
				DefaultMutableTreeNode node=dealSearchById(psid);
				
				try {
					UIUtil.fetchChildofTreeNode(card.getDataSourceName(),str_treesql, node, str_treeparentpk,psid);
				} catch (Exception e1) {
					NovaMessage.show(ltree, e1.getMessage(), NovaConstants.MESSAGE_ERROR);					
				}                                                                                                                   
				Enumeration e = node.preorderEnumeration();		       
		        while (e.hasMoreElements()) {      //遍历整个子节点只为查找符合条件的节点加入到treepath当中
		            DefaultMutableTreeNode subnode = (DefaultMutableTreeNode) e.nextElement();
		            if (node.isRoot()) {
		                continue;
		            }	   
		            HashVO vo = (HashVO) subnode.getUserObject();
		            ht.put(vo.getStringValue(str_treepk), subnode);    //将已经加载过的结点加入到缓存hashtable中.
		            if(hashVOc[numF3].getStringValue(str_treepk).equals(vo.getStringValue(str_treepk))){
		            	DefaultTreeModel model = (DefaultTreeModel) ltree.getModel();
		                TreeNode[] nodes = model.getPathToRoot(subnode);
		                TreePath path = new TreePath(nodes);
		                ltree.expandPath(path);
		                ltree.setSelectionPath(path);
		                ltree.scrollPathToVisible(path);
		                ltree.updateUI();		                
		            }
		        }
			}
			}else{
				al.add(parid);
				getLoadNode(parid);
			}
		}else{                                        //如果一直查找到根节点，则将符合条件的node加入到ht缓存中
			DefaultMutableTreeNode RootNode =(DefaultMutableTreeNode) ltree.getModel().getRoot();
			Enumeration e = RootNode.preorderEnumeration();		       
	        while (e.hasMoreElements()) {     
	            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
	            if (node.isRoot()) {
	                continue;
	            }
	            
	            HashVO vo = (HashVO) node.getUserObject();
	            String id=vo.getStringValue(str_treepk);
	            if(id.equals(hashvo[0].getStringValue(str_treepk))){
	            	for(int i=0;i<al.size();i++){
	            		
	            		String psid=(String)al.get(al.size()-1-i);
	            		DefaultMutableTreeNode nodtest=dealSearchById(psid);
	    				try {
							UIUtil.fetchChildofTreeNode(card.getDataSourceName(), str_treesql, nodtest, str_treeparentpk, psid);
						} catch (Exception e1) {
							NovaMessage.show(ltree, e1.getMessage(), NovaConstants.MESSAGE_ERROR);	
						}
	    				Enumeration en = nodtest.preorderEnumeration();		       
	    		        while (en.hasMoreElements()) {      
	    		            DefaultMutableTreeNode subnode = (DefaultMutableTreeNode) en.nextElement();
	    		            if (subnode.isRoot()) {
	    		                continue;
	    		            }
	    		            HashVO subvo = (HashVO) subnode.getUserObject();
	    		            ht.put(subvo.getStringValue(str_treepk), subnode);    //将已经加载过的结点加入到缓存hashtable中.
	    		            if(hashVOc[numF3].getStringValue(str_treepk).equals(subvo.getStringValue(str_treepk))){
	    		            	DefaultTreeModel model = (DefaultTreeModel) ltree.getModel();
	    		                TreeNode[] nodes = model.getPathToRoot(subnode);
	    		                TreePath path = new TreePath(nodes);
	    		                ltree.expandPath(path);
	    		                ltree.setSelectionPath(path);
	    		                ltree.scrollPathToVisible(path);
	    		                ltree.updateUI();
	    		                
	    		            }
	    		        }
	    			}			            
	            }	            
	        }	             
		}		
    }
    
    /**
     * 根据_name来处理搜索
     *
     * @param _name
     */
    private void dealSearch(String _name) {
		if(_name.trim().length()<2){
			NovaMessage.show(this, "请输入至少两个汉字！", NovaConstants.MESSAGE_INFO);
			return; 
		}
		if(al==null){
			al=new ArrayList();
		}	        
		if(hashVOc==null||!curname.equals(_name)){
			hashVOc=getHashVo(_name);
			curname=_name;
			numF3=-1; //如果执行的是新的查找，则需要设置重头开始检索
		}
		if(hashVOc.length==0){
			NovaMessage.show(this, "没有检测到你要找的节点！", NovaConstants.MESSAGE_WARN);
		    return;
		}
		if(ht==null){
			ht=new Hashtable();
		}
		//
		numF3++;
		if(numF3==hashVOc.length){//如果已经查找到最后一个符合的记录，则直接回到第一个符合条件的记录
		   numF3=0;
		}
	    String curid=hashVOc[numF3].getStringValue(str_treepk);
	    if(ht.containsKey(curid)){
	    	DefaultMutableTreeNode node=(DefaultMutableTreeNode)ht.get(curid);
	    	TreeNode[] nodes = node.getPath();
	        TreePath path = new TreePath(nodes);
	        ltree.expandPath(path);
	        ltree.setSelectionPath(path);
	        ltree.scrollPathToVisible(path);
	        ltree.updateUI();
	    }else{    //如果当前结点不在ht缓存中
	    	al.clear();
	    	this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	    	getLoadNode(curid); //根据ID取得当前节点直到父节点放入al中
	    	this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	    }
    }

    
    /**
    * 取得符合条件的数据
    * @param _name
    * @return
    */
    private HashVO[] getHashVo(String _name){ 	
    	String _sql=UIUtil.rebuildLazyLoadTreeSQL(str_treesql)+"and "+getFetchKey()+" Like '"+_name+"%' order by "+str_treeparentpk+","+str_treepk;
        try {
       		hashVOc = UIUtil.getHashVoArrayByDS( card.getDataSourceName(),_sql);          				
		} catch ( Exception e ){
            if(e.getMessage()==null){
          	  NovaMessage.show(null, "出现异常【?】，请关闭窗口重试！！！", NovaConstants.MESSAGE_ERROR);
            }else{
          	   NovaMessage.show(null, "出现异常【"+e.getMessage()+"】，请关闭窗口重试！！！", NovaConstants.MESSAGE_ERROR);
            }
            return null;
        }
		return hashVOc;
   }

    /**
     * 根据ID寻找节点
     * @param sid
     * @return
     */
    private DefaultMutableTreeNode dealSearchById(String sid) {
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) ltree.getModel().getRoot();
        if(sid==null)return rootNode;
        
        //int id=Integer.parseInt(sid);
        Enumeration e = rootNode.preorderEnumeration();        
        while (e.hasMoreElements()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
            if (node.isRoot()) {
                continue;
            }
            HashVO vo = (HashVO) node.getUserObject();
            //int _id=vo.getIntegerValue(str_treepk).intValue();
            String _sid=vo.getStringValue(str_treepk);
            //if(_id==id){
            if(_sid.equals(sid)){
            	return node;
            }
        }
        return rootNode;//检索不到返回根节点
    }
    
    protected void showTreeNode(int pos) {
        if (pos < 0 || pos >= treePath.size()) {
            return;
        }
        ltree.makeVisible( (TreePath) treePath.get(pos));
        ltree.setSelectionPath( (TreePath) treePath.get(pos));
        ltree.scrollPathToVisible( (TreePath) treePath.get(pos));
        currentShowPos = pos;
    }

    
    

    protected JPanel getCardPanel() {
        JPanel rpanel = new JPanel();
        rpanel.setLayout(new BorderLayout());
        card = new BillCardPanel(str_templetecode);
        card.setEditState(NovaConstants.BILLDATAEDITSTATE_INIT);
        rpanel.add(card, BorderLayout.CENTER);
        return rpanel;
    }

    public void onInsert() {
        if (oninsert) {
            NovaMessage.show(this, "当前已经处于新增状态！", NovaConstants.MESSAGE_WARN);
            return;
        }
        HashVO vo2 = getCurrSelectedTreeVO();
        if (vo2 == null) {
            return;
        }
        oninsert = true;
        card.createNewRecord();
//		card.setEditable( true );
        if (!curnode.isRoot()) {
            if (card.getCompentByKey(str_treeparentpk) instanceof UIRefPanel) {
                card.getCompentByKey(str_treeparentpk).setObject(new RefItemVO(vo2));
            } else {
                card.getCompentByKey(str_treeparentpk).setObject(
                    vo2.getIntegerValue(str_treepk).intValue() == -1
                    ? ""
                    : (vo2.getIntegerValue(str_treepk) + ""));
            }
            card.getCompentByKey(str_treeparentpk).setEditable(false);
        }
        setBtnEnable(true);
        // 执行拦截器操作!!
        if (uiIntercept != null) {
            try {
                uiIntercept.actionAfterInsert(card); // 执行删除前的动作!!
            } catch (Exception e) {
                //e.printStackTrace(); //
                NovaMessage.showException(this, e);
                return; // 不往下走了!!
            }
        }

    }

    public void onEdit() {
    	TreePath path = ltree.getSelectionPath();
        if (path == null) {
            NovaMessage.show(this, "请先选择一个结点!", NovaConstants.MESSAGE_WARN);
            return ;
        }
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
        if (node.isRoot()) {
            NovaMessage.show(this, "不能修改根结点!!!", NovaConstants.MESSAGE_ERROR);
            return;
        }
        card.setEditable(true);
        card.setEditState(NovaConstants.BILLDATAEDITSTATE_UPDATE);
        setBtnEnable(true);
    }

    /**
     * 点击删除按钮时执行的操作!!
     *
     */
    public void onDelete() {
    	TreePath path = ltree.getSelectionPath();
        if (path == null) {
            NovaMessage.show(this, "请先选择一个结点!", NovaConstants.MESSAGE_WARN);
            return ;
        }
    	
    	try {
        	if (!cascadeondelete) {
                if (!curnode.isLeaf()) {
                    NovaMessage.show(this, "该结点下有子结点不能删除", NovaConstants.MESSAGE_ERROR);
                    return;
                }
                if (!NovaMessage.confirm("你真的想删除此结点吗?")) {
                    return;
                }
            } else {
                if (!NovaMessage.confirm("确定删除此结点和它的所有子结点吗?")) {
                    return;
                }
            }
            
            
            
            // 执行拦截器操作!!
            if (uiIntercept != null) {
                uiIntercept.actionBeforeDelete(card); // 执行删除前的动作!!
            }
            HashVO hashvo  = (HashVO)curnode.getUserObject();
            BillVO billVO = hashvo.convertToBillVO(this.getTempletcode());
//            BillVO billVO = card.getBillVO(); //
            // 执行修改提交前的拦截器
            if (this.uiIntercept != null) {
                uiIntercept.dealCommitBeforeDelete(this, billVO);
            }

            
            try {
                if (!cascadeondelete) {//非级联删除
                    getService().style03_dealDelete(card.getDataSourceName(), getBsinterceptor(), billVO);
                } else {
                    HashVO[] vo = getCurrSelectedTreeVOWithChildren();
                    getService().style03_dealCascadeDelete(card.getDataSourceName(), getBsinterceptor(), vo, billVO.getSaveTableName(), str_treepk);
                }
                card.reset();
                
                //删除后清理节点
                DefaultTreeModel   treeModel=   (DefaultTreeModel)ltree.getModel(); 
            	DefaultMutableTreeNode   selNode   =(DefaultMutableTreeNode)ltree.getLastSelectedPathComponent(); 
            	treeModel.removeNodeFromParent(selNode); 
                
            } catch (Exception e1) {
                //e1.printStackTrace();
                NovaMessage.showException(this, e1);
            } // 直接提交数据库,这里可能抛异常!!
            // 执行修改提交后的拦截器
            if (this.uiIntercept != null) {
                try {
                    uiIntercept.dealCommitAfterDelete(this, billVO);
                } catch (Exception e) {
                    //e.printStackTrace();
                    NovaMessage.showException(this, e);
                }
            }
            
            //使节点可见  
            String parentid=hashvo.getStringValue(str_treeparentpk);
            DefaultMutableTreeNode newnode=dealSearchById(parentid);
            path = new TreePath(((DefaultTreeModel)ltree.getModel()).getPathToRoot(newnode)); 
            ltree.makeVisible(path); 
            ltree.setSelectionPath(path); 
            ltree.scrollPathToVisible(path);
            
        } catch (Exception ex) {
            //ex.printStackTrace();
            NovaMessage.showException(this, ex);
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
                    //e.printStackTrace();
                    if (!e.getMessage().trim().equals("")) {
                        JOptionPane.showMessageDialog(this, e.getMessage()); //
                    }
                }
            }
        }
    }
    
    /**
     * 从数据库刷新树节点
     */
    protected void onRefreshDB() {
    	hashVOc=null;//清空节点检索结果
    	
    	try{
        	//判断全加载或者分级加载
        	if(isLoadAll()){
            	ltree = UIUtil.getLoadedJTree(card.getDataSourceName(), str_treetitle, str_treesql, str_treepk, getTreeParentkey(), ltree);
            }else{
            	ltree = UIUtil.getLazyLoadTree(card.getDataSourceName(), str_treetitle, str_treesql, getTreeParentkey(), ltree);
            }
        }catch ( Exception e ){
            NovaMessage.show(null, e.getMessage(), NovaConstants.MESSAGE_ERROR);            
        }  
        
        //缓冲已经加载的节点
        ht=UIUtil.getTreeHashVONodeTable(ltree, ht, str_treepk);
        
        ltree.updateUI();
    }
    
    /**
     * 获得树所在面板。
     * 在本方法会实例化变量ltree
     * @return
     */
    protected JPanel getTreePanel() {
    	hashVOc=null;//清空节点检索结果
        try{
        	//判断全加载或者分级加载
        	if(isLoadAll()){
            	ltree = UIUtil.getLoadedJTree(card.getDataSourceName(), str_treetitle, str_treesql, str_treepk, getTreeParentkey(), null);
            }else{
            	ltree = UIUtil.getLazyLoadTree(card.getDataSourceName(), str_treetitle, str_treesql, getTreeParentkey(), null);
            }
        }catch ( Exception e ){
            NovaMessage.show(null, e.getMessage(), NovaConstants.MESSAGE_ERROR);            
        }
        
        ltree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent evt) {
                if (oninsert) {
                    if (!NovaMessage.confirm("确定终止新增结点任务?")) {
                        return;
                    } else {
                        oninsert = false;
                    }
                }
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
        ltree.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
            	dealKeyPerform(e);
            }
        });
        ltree.addMouseListener(new MyMouseListener());
        
        //缓冲已经加载的节点
        ht=UIUtil.getTreeHashVONodeTable(ltree, ht, str_treepk);
        
        JPanel rpanel = new JPanel();
        rpanel.setLayout(new BorderLayout());
        
        JScrollPane treeScroll = new JScrollPane(ltree);
        rpanel.add(treeScroll, BorderLayout.CENTER);
        return rpanel;
    }
    
    
    

    protected void onSave() {
        if (!card.checkValidate()) { //校验
            return;
        }

        if(!checkBeforeSave()){
        	return;
        }

        try {
            if (card.getEditState().equalsIgnoreCase(NovaConstants.BILLDATAEDITSTATE_INSERT)) { // 如果是新增状态
                insertSave();
            } else if (card.getEditState().equalsIgnoreCase(NovaConstants.BILLDATAEDITSTATE_UPDATE)) { // 如果是编辑状态
                updateSave();
            } else {
                NovaMessage.show("当前不处于新增或修改状态!!", NovaConstants.MESSAGE_ERROR);
                return;
            }
            card.setEditState(NovaConstants.BILLDATAEDITSTATE_INIT);
            setBtnEnable(false);
        } catch (Exception e) {
            NovaMessage.showException(this, e);
            //e.printStackTrace();
        }
    }

    protected void insertSave() throws Exception {
        BillVO billVO = card.getBillVO(); //
        // 执行新增提交前的拦截器
        if (this.uiIntercept != null) {
            uiIntercept.dealCommitBeforeInsert(this, billVO);
        }

        BillVO returnVO = getService().style03_dealInsert(card.getDataSourceName(), getBsinterceptor(), billVO);
        //生成新节点
        String[] keys = returnVO.getNeedSaveKeys();
        String[] values = returnVO.getNeedSaveDataRealValue();
        HashVO vo = new HashVO();
        for(int i=0;i<keys.length;i++){
        	vo.setAttributeValue(keys[i], values[i]);
        }
        
        DefaultMutableTreeNode newnode = new DefaultMutableTreeNode(vo);        
        //寻找父节点
        String parentid=vo.getStringValue(str_treeparentpk);
        DefaultMutableTreeNode parentnode=dealSearchById(parentid);
        parentnode.add(newnode);
        
        //缓冲本节点
        ht.put(vo.getStringValue(str_treepk), newnode);
        
        curnode = newnode;
        card.setEditable(false);
        oninsert = false;
        
        //使节点可见
        TreeNode[] nodes = newnode.getPath();
        TreePath path = new TreePath(nodes);
        ltree.expandPath(path);
        ltree.setSelectionPath(path);
        ltree.scrollPathToVisible(path);
        ltree.updateUI();
        
        
        // 执行新增提交后的拦截器
        if (this.uiIntercept != null) {
            try {
                uiIntercept.dealCommitAfterInsert(this, returnVO); //
            } catch (Exception e) {
                //e.printStackTrace(); //
                NovaMessage.showException(this, e);
            }  
        }
    }

    protected void updateSave() throws Exception {
        BillVO billVO = card.getBillVO(); //
        // 执行修改提交前的拦截器
        if (this.uiIntercept != null) {
                uiIntercept.dealCommitBeforeUpdate(this, billVO);
        }

        BillVO returnVO = null;
        try {
        	billVO.updateVersion();
            returnVO = getService().style03_dealUpdate(card.getDataSourceName(), getBsinterceptor(), billVO);
            card.updateVersion();
        } catch (Exception e1) {
            //e1.printStackTrace();
            throw e1;
        } // 直接提交数据库,这里可能抛异常!!
        // 执行修改提交后的拦截器
        if (this.uiIntercept != null) {
            uiIntercept.dealCommitAfterUpdate(this, returnVO); //            
        }
        // 设置显示信息
        VectorMap map = card.getAllObjectValuesWithVectorMap();
        HashVO nvo = new HashVO();
        String[] keys = map.getKeysAsString();
        for (int i = 0; i < keys.length; i++) {
            nvo.setAttributeValue(keys[i], map.get(keys[i]));
        }
        HashVO ovo = (HashVO) curnode.getUserObject();
        curnode.setUserObject(nvo);
        card.setEditable(false);

        // 得到修改结点的原先父结点和新的父结点，比较两个是否相同.如果相同，刚不用从数据库刷新，如果不同，则要再从数据库刷新一遍
        String oldparent = ovo.getStringValue(str_treeparentpk);
        String newparent = nvo.getStringValue(str_treeparentpk);
        if (oldparent != null && newparent != null && !oldparent.equals(newparent)) {
        	onRefreshDB();
        } 
    }

    protected void onChangeSelectTree(DefaultMutableTreeNode _node) {
        if(_node.equals(curnode)) return;
    	curnode = _node;
        if (_node.isRoot()) {
            card.reset(); //
            card.setEditState(NovaConstants.BILLDATAEDITSTATE_INIT);
        } else {
            new Thread(){
            	public void run(){
            		showTreeNodeInfo();
            	}
            }.start();
        }
        setBtnEnable(false);
    }

    //TODO  这里应该在Card上面增加个东西指示正在检索
    protected synchronized void showTreeNodeInfo(){
    	HashVO vo = (HashVO) curnode.getUserObject();
        String str_id = vo.getStringValue(str_treepk);
        String str_wherecondition = str_treepk + "='" + str_id + "'";
        card.refreshData(str_wherecondition);
        card.setEditable(false);
    }
    

    protected void onCancel() {
        oninsert = false;
        card.reset();
        card.setEditable(false);
        setBtnEnable(true);
    }

    protected HashVO[] getCurrSelectedTreeVOWithChildren() {
        TreePath path = ltree.getSelectionPath();
        if (path == null) {
            NovaMessage.show(this, "请先选择一个结点!", NovaConstants.MESSAGE_WARN);
            return null;
        }
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
        ArrayList children = getChildrenNode(node);

        if (node.isRoot()) {
            NovaMessage.show(this, "不能删除根结点!!!", NovaConstants.MESSAGE_ERROR);
            return null;
        }
        HashVO[] vos = new HashVO[children.size()];
        for (int i = 0; i < children.size(); i++) {
            DefaultMutableTreeNode tnode = (DefaultMutableTreeNode) children.get(i);
            vos[i] = (HashVO) tnode.getUserObject();
        }
        return vos; //
    }

    

    protected HashVO getCurrSelectedTreeVO() {
        TreePath path = ltree.getSelectionPath();
        if (path == null) {
            NovaMessage.show(this, "请先选择一个结点!", NovaConstants.MESSAGE_WARN);
            return null;
        }

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
        if (node.isRoot()) {
            HashVO vo = new HashVO();
            vo.setAttributeValue(str_treepk, new Integer( -1));
            return vo;
        }
        HashVO vo = (HashVO) node.getUserObject();
        return vo; //
    }

    protected ArrayList getChildrenNode(DefaultMutableTreeNode node) { // 得到所选结点的所有子结点.删除时顺序遍历即可
        ArrayList temp = new ArrayList();
        if (node.getChildCount() > 0) {
            for (int i = 0; i < node.getChildCount(); i++) {
                temp.addAll(getChildrenNode( (DefaultMutableTreeNode) node.getChildAt(i)));
            }

        }
        temp.add(node);
        return temp;

    }

    private void setBtnEnable(boolean enable) {
        btn_save.setEnabled(enable);
        btn_cancel.setEnabled(enable);
    }

    protected boolean checkBeforeSave(){
    	return true;
    }
    protected boolean isLoadAll()
    {
    	return true;
    }
    
    /**
     * 鼠标操作侦听
     * @author James.W
     *
     */
    protected class MyMouseListener extends MouseAdapter {
    	private JPopupMenu menu = null;
    	
        public void mouseClicked(MouseEvent e) {
        	if (!isLoadAll() && e.getClickCount() == 2) {
				TreePath path = ltree.getSelectionPath();
				if (path == null) return;
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
				if (node.isRoot()) return;
				if(ht==null) ht=new Hashtable();
				
				//如果已经有子节点，则不用检索
				if(node.getChildCount()>0){
					return;
				}
				
				/*
				Enumeration en = node.preorderEnumeration();
				while (en.hasMoreElements()) {     
			        DefaultMutableTreeNode subnode = (DefaultMutableTreeNode) en.nextElement();
			        if(subnode.equals(node)) continue;
			        HashVO vo = (HashVO) subnode.getUserObject();
			        if(ht.containsKey(vo.getStringValue(str_treepk))){
					    return;
			        }
				}
				*/
				
				ltree.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));				 
				try {
					UIUtil.fetchChildofTreeNode(card.getDataSourceName(), str_treesql, node, str_treeparentpk, ((HashVO) node.getUserObject()).getStringValue(str_treepk));
				} catch (Exception e1) {
					NovaMessage.show(ltree, e1.getMessage(), NovaConstants.MESSAGE_ERROR);	
				}
				ltree.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				Enumeration em = node.preorderEnumeration();
				 while (em.hasMoreElements()) {      
			            DefaultMutableTreeNode subnode = (DefaultMutableTreeNode) em.nextElement();
			            if (subnode.isRoot()) {
			                continue;
			            }
			            HashVO vo = (HashVO) subnode.getUserObject();
			            ht.put(vo.getStringValue(str_treepk), subnode);
				 }
				if (node.getChildCount() != 0) {
					DefaultTreeModel model = (DefaultTreeModel) ltree.getModel();
					TreePath p = new TreePath(model.getPathToRoot(node.getFirstChild()));
					ltree.expandPath(p);
					ltree.setSelectionPath(p);
					ltree.scrollPathToVisible(p);
					ltree.updateUI();
				}
			}
            if (e.getButton() == MouseEvent.BUTTON3) {
                getMenu().show(ltree, e.getX(), e.getY());                
            }
        }
        
        private JPopupMenu getMenu(){
        	if(menu!=null){
        		return menu;
        	}
        	
        	menu = new JPopupMenu();
        	if (isShowsystembutton()) {
                JMenuItem add = new JMenuItem(Sys.getSysRes("edit.new.msg"), UIUtil.getImage(Sys.getSysRes("edit.new.icon")));
                add.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    	getMenu().setVisible(false);
                        onInsert();
                    }
                });
                JMenuItem modify = new JMenuItem(Sys.getSysRes("edit.edit.msg"), UIUtil.getImage(Sys.getSysRes("edit.edit.icon")));
                modify.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    	getMenu().setVisible(false);
                        onEdit();
                    }
                });
                JMenuItem del = new JMenuItem(Sys.getSysRes("edit.delete.msg"), UIUtil.getImage(Sys.getSysRes("edit.delete.icon")));
                del.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    	getMenu().setVisible(false);
                        onDelete();
                    }
                });
                menu.add(add);
                menu.add(modify);
                menu.add(del);
                menu.addSeparator();
        	}
            JMenuItem refresh = new JMenuItem(Sys.getSysRes("edit.refresh.msg"), UIUtil.getImage(Sys.getSysRes("edit.refresh.icon")));
            refresh.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	onRefreshDB();
                }
            });              
            menu.add(refresh);
            
            return menu;
        }
        
    }
    
   
    
    
    
    /**
     * @author yangjm
     *增加一个线程用来处理按键后的查询操作
     */
    protected class  dealkeysearch implements Runnable{   

		public void run() {
			 dealSearch(jtf_search.getText());
		}
    	
    }

}
/*******************************************************************************
 * $RCSfile: AbstractTempletFrame03.java,v $ $Revision: 1.15.2.20 $ $Date: 2007/01/30
 * 04:48:32 $
 *
 * Revision 1.14  2007/08/23 03:44:52  yh
 * 解决点击取消再点击删除却无法删除的bug
 * 
 * $Log: AbstractTempletFrame03.java,v $
 * Revision 1.15.2.20  2010/02/05 02:17:29  wangqi
 * *** empty log message ***
 *
 * Revision 1.15.2.19  2010/01/26 01:47:50  wangqi
 * *** empty log message ***
 *
 * Revision 1.15.2.18  2010/01/13 02:48:43  wangqi
 * *** empty log message ***
 *
 * Revision 1.15.2.17  2010/01/05 10:20:36  wangqi
 * *** empty log message ***
 *
 * Revision 1.15.2.16  2010/01/05 09:10:55  wangqi
 * *** empty log message ***
 *
 * Revision 1.15.2.15  2009/12/21 02:57:33  wangqi
 * *** empty log message ***
 *
 * Revision 1.15.2.14  2009/12/16 05:34:11  wangqi
 * *** empty log message ***
 *
 * Revision 1.15.2.13  2009/12/16 04:13:50  wangqi
 * *** empty log message ***
 *
 * Revision 1.15.2.12  2009/12/02 08:50:10  wangqi
 * *** empty log message ***
 *
 * Revision 1.15.2.11  2009/09/08 06:48:50  yangjm
 * *** empty log message ***
 *
 * Revision 1.15.2.10  2009/09/07 06:54:06  yangjm
 * *** empty log message ***
 *
 * Revision 1.15.2.9  2009/09/01 08:25:34  yangjm
 * *** empty log message ***
 *
 * Revision 1.15.2.8  2009/04/22 01:52:45  wangqi
 * *** empty log message ***
 *
 * Revision 1.15.2.7  2009/04/21 07:55:26  wangqi
 * *** empty log message ***
 *
 * Revision 1.15.2.6  2008/11/05 05:21:14  wangqi
 * *** empty log message ***
 *
 * Revision 1.15.2.5  2008/09/19 02:22:07  wangqi
 * *** empty log message ***
 *
 * Revision 1.15.2.4  2008/09/16 06:13:19  wangqi
 * patch   : 20080916
 * file    : nova_20080128_20080916.jar
 * content : 处理 MR nova20-87,nova20-30；
 * 另外，改写了快速查询面板的处理。
 *
 * Revision 1.17  2008/09/01 02:47:22  wangqi
 * *** empty log message ***
 *
 * Revision 1.16  2008/02/25 02:38:59  wangqi
 * *** empty log message ***
 *
 * Revision 1.15  2008/01/15 09:03:27  wangqi
 * 孙雪峰修改MR
 *
 * Revision 1.14  2007/08/24 06:40:56  yanghuan
 * 将删除采用的方式从卡片获取BillVO改为获取this.getTempletcode()。然后再转为BillVO。
 *
 * Revision 1.13  2007/07/23 10:59:00  sunxf
 * Nova 20-14    平台层面更新数据的地方提供乐观锁控制，解决并发访问问题
 *	
 * Revision 1.12  2007/07/04 01:38:52  qilin
 * 去掉系统状态栏
 *
 * Revision 1.11  2007/07/02 02:18:46  qilin
 * 去掉系统状态栏
 *
 * Revision 1.10  2007/06/29 09:24:21  lst
 * 提交前拦截器不应该用try包着
 *
 * Revision 1.9  2007/06/18 09:38:37  sunxb
 * *** empty log message ***
 *
 * Revision 1.8  2007/06/16 10:53:02  sunxb
 * *** empty log message ***
 *
 * Revision 1.7  2007/06/07 10:33:20  lst
 * MR#:NM30-0000 异常时弹出消息框
 *
 * Revision 1.6  2007/05/31 07:39:02  qilin
 * code format
 *
 * Revision 1.4  2007/05/23 09:06:13  qilin
 * no message
 *
 * Revision 1.3  2007/05/23 03:29:04  qilin
 * no message
 *
 * Revision 1.2  2007/05/21 09:40:01  sunxb
 * *** empty log message ***
 *
 * Revision 1.1  2007/05/17 06:14:27  qilin
 * no message
 *
 * Revision 1.12  2007/03/28 09:16:06  sunxf
 * *** empty log message ***
 *
 * Revision 1.11  2007/03/27 07:57:40  shxch
 * *** empty log message ***
 *
 * Revision 1.10  2007/03/27 05:17:29  sunxf
 * MR#:NOVA-PLUTO1.0-33
 *
 * Revision 1.9  2007/03/12 04:45:31  sunxf
 * *** empty log message ***
 * Revision 1.8 2007/03/09 01:15:39 sunxf
 * *** empty log message ***
 *
 * Revision 1.7 2007/03/08 10:53:19 shxch *** empty log message ***
 *
 * Revision 1.6 2007/03/08 08:24:34 shxch *** empty log message ***
 *
 * Revision 1.5 2007/03/08 07:45:09 shxch *** empty log message ***
 *
 * Revision 1.4 2007/03/07 01:46:07 shxch *** empty log message ***
 *
 * Revision 1.3 2007/03/07 01:45:31 shxch *** empty log message ***
 *
 * Revision 1.2 2007/03/07 01:41:18 shxch *** empty log message ***
 *
 * Revision 1.1 2007/03/05 09:59:13 shxch *** empty log message ***
 *
 * Revision 1.10 2007/03/05 09:26:28 sunxf *** empty log message ***
 *
 * Revision 1.9 2007/03/05 09:13:50 sunxf *** empty log message ***
 *
 * Revision 1.8 2007/03/05 01:12:37 sunxf *** empty log message ***
 *
 * Revision 1.7 2007/03/02 09:49:31 sunxf *** empty log message ***
 *
 * Revision 1.6 2007/02/27 06:57:20 shxch *** empty log message ***
 *
 * Revision 1.5 2007/02/27 06:03:03 shxch *** empty log message ***
 *
 * Revision 1.4 2007/02/10 08:59:36 shxch *** empty log message ***
 *
 * Revision 1.3 2007/01/30 05:22:55 sunxf 增加F3功能 Revision 1.2 2007/01/30
 * 04:48:32 lujian *** empty log message ***
 *
 *
 ******************************************************************************/
