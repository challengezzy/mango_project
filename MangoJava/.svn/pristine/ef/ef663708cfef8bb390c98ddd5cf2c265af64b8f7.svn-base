/**********************************************************************
 *$RCSfile: DesktopUtil.java,v $  $Revision: 1.1.2.4 $  $Date: 2009/09/03 07:11:00 $
 *********************************************************************/ 
package smartx.system.login.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;


import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;

import smartx.framework.common.ui.NovaClientEnvironment;
import smartx.framework.common.ui.NovaConstants;
import smartx.framework.common.ui.NovaRemoteServiceFactory;
import smartx.framework.common.ui.UIUtil;
import smartx.framework.common.utils.Sys;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaLogger;
import smartx.framework.metadata.util.TreeNode;
import smartx.system.login.ui.deskaction.OperationAction;
import smartx.system.login.ui.deskmodule.NovaDeskTopModuleIFC;
import smartx.system.login.ui.deskmodule.NovaDeskTopModuleRegistInfo;
import smartx.system.login.ui.deskmodule.NovaMessageBoardPane;
import smartx.system.login.vo.DeskTopVO;

/**
 * <li>Title: DeskTopUtil.java</li>
 * <li>Description: 桌面操作工具类</li>
 * <li>Project: NOVA2_MAINBRANCH</li>
 * <li>Copyright: Copyright (c) 2008</li>
 * @Company: GXLU. All Rights Reserved.
 * @author James.W
 * @version 1.0
 */
public class DesktopUtil {

	/**
     * 获得桌面配置（Nova2Desktop.xml）
     * @param isforce 是否强制重新获取
     * @return
     */
    public static Document getDeskTopDesign(boolean isforce) {
    	Document rt=null;
    	if(isforce){
    		Sys.removeInfo("SYS_DESKTOP_DEFINE");
    	}else{
    		rt=(Document)Sys.getInfo("SYS_DESKTOP_DEFINE");
    		if(rt!=null) return rt;
    	}
        try {
            SystemLoginServiceIFC service = (SystemLoginServiceIFC) NovaRemoteServiceFactory.getInstance().
                lookUpService(SystemLoginServiceIFC.class); //
            rt = service.getDeskTopDesign();
        } catch (Exception e) {
            NovaLogger.getLogger(DesktopUtil.class).error("获得桌面定义错误！",e);
        }
        Sys.putInfo("SYS_DESKTOP_DEFINE", rt);
        return rt;
    }
	
    /**
     * 获得指定的登录定义
     * @param calltype 根据Nova2Desktop.xml中的loginConfig.action.sign定义获得
     * @return
     */
    public static HashMap getLoginDesign(String calltype){
    	if(calltype==null){return null;}
    	HashMap design=(HashMap)Sys.getInfo("SYS_DESKTOP_LOGINDESIGN_"+calltype);
		if(design!=null) {return design;}
		
		Document deskdoc=getDeskTopDesign(false);
		if(deskdoc==null){return null;}
		
		Element ela=deskdoc.getRootElement().getChild("loginConfig").getChild("action-"+calltype);
		if(ela==null){return null;}
		design=new HashMap();
		{//impl-cls
			design.put("implcls", ela.getChild("impl-cls").getText());
		}//impl-cls end
		{//background
			Element ebg=ela.getChild("background");
			HashMap bgmap=new HashMap();
			bgmap.put("bg-color", ebg.getChild("bg-color").getText());
			design.put("background", bgmap);
		}//background end
		{//ctrl-list
			Element ectrls=ela.getChild("ctrl-list");
			List elst=ectrls.getChildren("ctrl");
			ArrayList lst=new ArrayList();
			for(int i=0;i<elst.size();i++){
				Element ectrl=(Element)elst.get(i);
				HashMap ctrl=new HashMap();
				List eclst=ectrl.getAttributes();								
				for(int j=0;j<eclst.size();j++){
					Attribute ea=(Attribute)eclst.get(j);
					ctrl.put(ea.getName(),ea.getValue());
				}
				lst.add(ctrl);
			}
			design.put("ctrl-list", lst);
		}//ctrl-list end
		
		Sys.putInfo("SYS_DESKTOP_LOGINDESIGN_"+calltype, design);//系统风格模板定义缓冲
    	return design;
    }
    
    
    /**
     * 获得
     * @return
     */
    public static HashMap getStyleSetting(){
    	HashMap stylemap=(HashMap)Sys.getInfo("SYS_DESKTOP_STYLETEMPLET");
		if(stylemap!=null) return stylemap;
		
		Document deskdoc=getDeskTopDesign(false);
    	if(deskdoc!=null){
    		stylemap=new HashMap();
    		List styles=deskdoc.getRootElement().getChild("styletempletdesign").getChildren();
    		for(int i=0;i<styles.size();i++){
    			Element e=(Element)styles.get(i);
    			String key=e.getChildText("code");
    			stylemap.put(key, e.getChildTextTrim("class"));
    			stylemap.put(key+"_refdialog", e.getChildTextTrim("refdialog"));
    		}
    	}else{
    		stylemap=new HashMap();
    		stylemap.put("0", "");
    		stylemap.put("11", "smartx.publics.styletemplet.ui.templet01.DefaultMainFrame");
    		stylemap.put("12", "smartx.publics.styletemplet.ui.templet02.DefaultMainFrame");
    		stylemap.put("13", "smartx.publics.styletemplet.ui.templet03.DefaultMainFrame");
    		stylemap.put("14", "smartx.publics.styletemplet.ui.templet04.DefaultMainFrame");
    		stylemap.put("15", "smartx.publics.styletemplet.ui.templet05.DefaultMainFrame");
    		stylemap.put("16", "smartx.publics.styletemplet.ui.templet06.DefaultMainFrame");
    		stylemap.put("17", "smartx.publics.styletemplet.ui.templet07.DefaultMainFrame");
    		stylemap.put("18", "smartx.publics.styletemplet.ui.templet08.DefaultMainFrame");
    		stylemap.put("19", "smartx.publics.styletemplet.ui.templet09.DefaultMainFrame");
    		stylemap.put("20", "smartx.publics.styletemplet.ui.templet10.DefaultMainFrame");
    		stylemap.put("31", "");
    		stylemap.put("0_refdialog",  "smartx.publics.styletemplet.ui.MenuCommdURLPathPanel");
    		stylemap.put("11_refdialog", "smartx.publics.styletemplet.ui.templet01.Templet1RefPanel");
    		stylemap.put("12_refdialog", "smartx.publics.styletemplet.ui.templet02.Templet2RefPanel");
    		stylemap.put("13_refdialog", "smartx.publics.styletemplet.ui.templet03.Templet3RefPanel");
    		stylemap.put("14_refdialog", "smartx.publics.styletemplet.ui.templet04.Templet4RefPanel");
    		stylemap.put("15_refdialog", "smartx.publics.styletemplet.ui.templet05.Templet5RefPanel");
    		stylemap.put("16_refdialog", "smartx.publics.styletemplet.ui.templet06.Templet6RefPanel");
    		stylemap.put("17_refdialog", "smartx.publics.styletemplet.ui.templet07.Templet7RefPanel");
    		stylemap.put("18_refdialog", "smartx.publics.styletemplet.ui.templet08.Templet8RefPanel");
    		stylemap.put("19_refdialog", "smartx.publics.styletemplet.ui.templet09.Templet9RefPanel");
    		stylemap.put("20_refdialog", "smartx.publics.styletemplet.ui.templet10.Templet10RefPanel");
    		stylemap.put("31_refdialog", "smartx.publics.styletemplet.ui.templet31.Templet31RefPanel");
    	}
    	Sys.putInfo("SYS_DESKTOP_STYLETEMPLET", stylemap);//系统风格模板定义缓冲
    	return stylemap;
    }
    
    
    //====下面是获得桌面上的组件的代码，在这些组件中如有后续触发必要则需要在桌面模块管理中注册==============//
    /**
     * 根据当前登录的配置信息，得到桌面功能定义信息
     * 见登录处理的完成部分。
     * 1、根据客户端环境变量中的配置中获得DeskTopVO的服务配置和参数配置
     * 2、获得参数并调用，获得DeskTopVO
     * fixed since 1.30
     * @return DeskTopVO
     */
    public static DeskTopVO getDeskTopVO() {
        DeskTopVO deskTopVO=null;
        
        //从客户端环境变量内获得登录定义逻辑
        HashMap map=(HashMap)Sys.getInfo("loginConfigMap");
        if(map==null){//不是20080704分支后提供的配置登录处理，按传统方式处理
        	try {
                SystemLoginServiceIFC service = (SystemLoginServiceIFC) NovaRemoteServiceFactory.getInstance().lookUpService(SystemLoginServiceIFC.class); //
                Object workpostion = NovaClientEnvironment.getInstance().get(NovaConstants.USER_WORKPOSITION);
                String uid=(String)NovaClientEnvironment.getInstance().get("SYS_LOGINUSER_ID");
                if (workpostion != null && workpostion instanceof String[]) {
                    deskTopVO = service.getDeskTopVO( (String[]) workpostion, uid);
                } else {
                    deskTopVO = service.getDeskTopVO(null, uid);
                }
            } catch (Exception e) {
            	NovaLogger.getLogger(DeskTopPanel.class).error("获得用户登录信息错误！",e);
            }        	
        	return deskTopVO;        	
        }
        HashMap desk=(HashMap)map.get("desktop");
        String svr=(String)desk.get("service");
        String fn=(String)desk.get("fn");
        HashMap[] params=(HashMap[])desk.get("params");
        //创建方法的参数表
        Object[] ps=new Object[params.length];
        Class[] pc=new Class[params.length];
        for(int i=0;i<params.length;i++){
        	/**
        	 * 循环处理远程调用参数
        	 * 1、获得参数来源、名称、默认值、默认类型
        	 * 2、如果来源与ClientEnv-客户端缓存，则直接从缓存中读取
        	 * 3、是其他来源（此时来源就是类名），则需要借个属性fn，通过反射检索出数据（需要完善）
        	 *    3.1、如果来源为this，则表示调用当前对象的某个方法（fn-无参数)，直接反射调用。
        	 *    3.2、来源不是this，那么直接forName以后再，反射调用方法（fn-无参数）
        	 */
        	String from=(String)params[i].get("from");
        	String keyName=(String)params[i].get("keyName");
        	String defValue=(String)params[i].get("defValue");
        	String defType=(String)params[i].get("defType");
        	if(from.equalsIgnoreCase("ClientEnv")){
        		if(keyName.equals("")&&defValue.equals("")){
        			ps[i]=null;
        		}else{
        			ps[i]=NovaClientEnvironment.getInstance().get(keyName);
        			//TODO 结合defValue和defType处理一下默认值问题，写一个公用方法
        		}
        		try{pc[i]=Class.forName(defType);}catch(Exception e){;}
        	}else{
        		String pfn=(String)params[i].get("fn");
        		//TODO 结合签名的描述处理
        		if(pfn.equals("")){//没有指定方法，参数值直接为空
        			ps[i]=null;
        		}else{
        			try{
        				
	        			Class cls=Class.forName(from);
	        			Method m=cls.getMethod(pfn, new Class[]{});
	        			ps[i]=m.invoke(cls.newInstance(), new Object[]{});
	        			pc[i]=Class.forName(defType);
        			}catch(Exception e){
        				;
        			}
        		}        		
        	}        	
        }
        
        try {
            Object remote = NovaRemoteServiceFactory.getInstance().lookUpService(Class.forName(svr)); //
            Method m=remote.getClass().getMethod(fn,pc);
            deskTopVO = (DeskTopVO)m.invoke(remote,ps);            
        } catch (Exception e1) {
            //e1.printStackTrace();
            NovaLogger.getLogger(DesktopUtil.class).error("远程获得桌面对象调用失败！",e1);
        }        
        return deskTopVO;
    }
    
    /**
     * 获得桌面组件基本属性共用定义
     * 主要包括组件背景、宽度、等等
     */
    public static void initNovaComponentSetting(){
    	Document deskdoc=getDeskTopDesign(false);
    	
    	Element uidesign=deskdoc.getRootElement().getChild("ui-design");
    	if(uidesign==null)return;    	
    	//处理field-view，域组件定义，主要针对Card和QuickQuery面板，以及列表面板等组件的单值属性
    	Element fieldview=uidesign.getChild("field-view");
    	if(fieldview!=null){
	    	List lst=fieldview.getChildren("property");
    		int rows=lst.size();
    		for(int i=0;i<rows;i++){
    			Element e=(Element)lst.get(i);
    			String name=e.getAttributeValue("name");
    			String value=e.getAttributeValue("value");
    			//System.out.println("属性名："+name+"，取值："+value);
    			Sys.putInfo(name, value);
    		}
    	}
    	//处理field-view，域组件定义，主要针对Card和QuickQuery面板，以及列表面板等组件的多值属性
    	Element listview=uidesign.getChild("list-view");
    	if(listview!=null){
    		List lst=(List)listview.getChildren("property");
    		int rows=lst.size();
    		for(int i=0;i<rows;i++){
    			Element e=(Element)lst.get(i);
    			String name=e.getAttributeValue("name");
    			List sublst=e.getChildren("value");
    			String[] values=new String[sublst.size()];
    			for(int j=0;j<sublst.size();j++){
    				Element sube=(Element)sublst.get(j);
    				values[j]=sube.getTextTrim();
    				//System.out.println("属性名："+name+"，取值："+values[j]);        			
    			}
    			Sys.putInfo(name, values);    			
    		}    		
    	}    	
    }
    
    /**
     * 获得业务树数据
     */
    public static Vector getDeskOperationList(DeskTopVO desk){
    	HashVO[] vo = desk.getMenuVOs();
        Vector menu = new Vector();
        for (int i = 0; i < vo.length; i++) {
            menu.add(vo[i]);            
        }
        return menu;
    }
    
    /**
     * 获得业务工具栏数据
     */
    public static Vector getDeskOperationTool(DeskTopVO desk){
    	HashVO[] vo = desk.getMenuVOs();
        
    	Vector toolbar=new Vector();
        for (int i = 0; i < vo.length; i++) {
            //是否展现工具条
        	String str_show = vo[i].getStringValue("SHOWINTOOLBAR");
            if (str_show.equals("Y")) {
            	toolbar.add(vo[i]);
            }
        }
        return toolbar;    	
    }
    
    /**
     * 获得管理树数据
     */
    public static Vector getSysOperationList() {
        
        String[] str_name = new String[] {"id", "code", "name","localname", "parentmenuid", "commandtype", "command"}; //
        String[][] str_temp = new String[][] { // 所有系统管理菜单
        		{"0", "", "管理功能", "管理功能","", "0", ""}, //管理菜单的父菜单	
//            { "-1", "", "数据字典管理", "数据字典管理","0", "0", "smartx.framework.metadata.ui.dictmanager.DictManagerFrame" }, // 数据字典管理
            {"-2", "", "元原模板管理", "元原模板管理","0", "0", "smartx.framework.metadata.ui.DeveloperTempleteConfig"}, // 元原模板管理
            {"-3", "", "菜单管理","菜单管理", "0", "0", "smartx.system.login.ui.MenuConfigFrame"}, // 菜单管理
//            { "-4", "", "下拉框字典维护","下拉框字典维护", "0", "0", "smartx.framework.metadata.ui.dictmanager.ComboxDictManagerFrame" }, // 创建模板VO
//            {"-5", "", "查询模板配置", "查询模板配置","0", "0", "smartx.system.login.ui.QuerySelectTempletConfigFrame"}, // 查询选择模板配置界面
            {"-6", "", "导出平台数据", "导出平台数据","0", "0", "smartx.framework.metadata.ui.exportnova.ExportNovaDataFrame"}, // 创建模板VO
//            { "-7", "", "工作流编辑", "工作流编辑", "0", "0", "smartx.publics.workflow.ui.graph.WorkFlowEditFrame" }, // 工作流编辑
//            { "-8", "", "工作流查看","工作流查看", "0", "0", "smartx.publics.workflow.ui.graph.WorkFlowFrame" }, // 工作流查看
//            {"-9", "", "公告栏管理", "公告栏管理", "0", "0", "smartx.system.login.ui.NewsManagerFrame"}, // 工作流查看
//            { "-10", "", "角色管理","角色管理", "0", "0", "smartx.system.login.ui.RoleConfigFrame" }, // 工作流查看
//            { "-11", "", "菜单权限管理","菜单权限管理", "0", "0", "smartx.system.login.ui.AccessConfigFrame" }, // 工作流查看
        };
        Vector vec_plantform = new Vector();
        HashVO voTmp = null;
        for (int i = 0; i < str_temp.length; i++) {
            voTmp = new HashVO();
            for (int j = 0; j < str_temp[i].length; j++) {
                voTmp.setAttributeValue(str_name[j], str_temp[i][j]);
            }
            vec_plantform.add(voTmp);
        }
        
        return vec_plantform;
    }
    
    /**
     * 根据数据创建树对象
     * @param _vec item={,,,}，树的原始数据
     * @return
     */
    public static JTree getCommanTree(Vector vec) {
    	/**
         * 构建树的算法：
         *   1、创建根节点
         *   2、循环加入所有节点作为一级节点
         *   3、循环节点，获得当前节点A的上级节点编号B
         *   4、循环所有节点，找到节点编号为B的节点C
         *   5、把A节点作为C节点的下级节点
         * 分析：
         *   无论什么样的节点数据都可以形成树，如果某个节点的父不存在，则肯定在一级节点上，不会出现丢失节点情况。  
         */
    	
    	DefaultMutableTreeNode node_root = new DefaultMutableTreeNode("菜单"); // 创建根结点
        DefaultMutableTreeNode[] node_level_1 = new DefaultMutableTreeNode[vec.size()]; // 创建所有结点数组
        for (int i = 0; i < vec.size(); i++) {
            node_level_1[i] = new DefaultMutableTreeNode(vec.get(i)); // 创建各个结点
            node_root.add(node_level_1[i]); // 加入根结点
        }
        
        for (int i = 0; i < node_level_1.length; i++) {
            HashVO nodeVO = (HashVO) node_level_1[i].getUserObject();
            String str_pk_parentPK = nodeVO.getStringValue("parentmenuid"); // 父亲主键
            for (int j = 0; j < node_level_1.length; j++) {
                HashVO nodeVO_2 = (HashVO) node_level_1[j].getUserObject();
                String str_pk_2 = nodeVO_2.getStringValue("id"); // 主键
                if (str_pk_2.equals(str_pk_parentPK)) {
                    try {
                        node_level_1[j].add(node_level_1[i]);
                    } catch (Exception ex) {
                        System.out.println("在[" + node_level_1[j] + "]下加入子结点[" + node_level_1[i] + "]失败"); ///
                        //ex.printStackTrace();
                    }
                }
            }
        }
        JTree tree = new JTree(new DefaultTreeModel(node_root));
        return tree;
    } 
    
    /**
     * 根据树层次关系创建菜单条对象
     * @param bar 菜单条，空-新建并返回，非空-在其中增加并返回
     * @param root 菜单树的根节点
     * @return
     */
    public static JMenuBar getMenuBar(JMenuBar bar,TreeNode root,DeskTopPanel desk) {
    	if(bar==null)bar = new JMenuBar();  
    	bar.add(new JLabel(UIUtil.getImage("images/logo/icon.gif")),0);
    	int count=root.getCount();
    	for(int i=0;i<count;i++){
    		TreeNode node=root.getChild(i);
    		HashVO vo=(HashVO)node.getData();
    		String name= vo.getStringValue("localname");
    		//System.out.println("菜单输出："+name);
    		if(!node.isLeaf()){    			
    			//TODO 图标等等
    			JMenu menu=new JMenu(name);
    			//子菜单
    			buildSubMenu(menu,node,desk);
    			bar.add(menu);
    		}else{
    			OperationAction oa=new OperationAction(vo,new String[]{name},desk);
    			JMenu menu=new JMenu(name);
    			menu.add(oa);
    			bar.add(menu);
    			
    		}
    	}
    	return bar;
    }
    private static void buildSubMenu(JMenu menu,TreeNode node,DeskTopPanel desk){
    	if(menu==null)menu = new JMenu();
    	int count=node.getCount();
    	for(int i=0;i<count;i++){
    		TreeNode sub=node.getChild(i);
    		HashVO vo=(HashVO)sub.getData();
    		String name= vo.getStringValue("localname"); 
    		if(!sub.isLeaf()){    			
    			//System.out.println("菜单输出："+name);
    			//TODO 图标等等
    			JMenu submenu=new JMenu(name);
    			//处理子菜单
    			buildSubMenu(submenu,sub,desk);   
    			menu.add(submenu);
    		}else{
    			//命令路径、菜单路径
    			ArrayList path=new ArrayList();
    			TreeNode tmp=sub;
    			while(tmp.getParent()!=null){
    				HashVO v=(HashVO)tmp.getData();
    				String mname= v.getStringValue("localname");
    				path.add(0,mname);
    				tmp=tmp.getParent();
    			}
    			
    			OperationAction oa= new OperationAction(vo,(String[])(path.toArray(new String[0])),desk);
    			menu.add(oa); 			
    		}
    	}
    }
    
    
    
    /**
     * 创建状态栏
     * 
     * @return
     */
    public static JPanel getStatuPanel() {
        JPanel statuPanel = new JPanel();
        // statuPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); //
        statuPanel.setBackground(new java.awt.Color(245, 245, 245));
        statuPanel.setPreferredSize(new Dimension(1000,20));//TODO 需要考虑如何确定预先的大小
        statuPanel.setLayout(new BorderLayout());

        Long l_count2 = (Long) NovaClientEnvironment.getInstance().get("USER_LOGINCOUNT");

        long l_userlogincount = 0; // l_count2.longValue();

        if (l_count2 != null) {
            l_userlogincount = l_count2.longValue();
        }

        String str_user = "登录用户:" + NovaClientEnvironment.getInstance().get("SYS_LOGINUSER_NAME");
        str_user = str_user + " 共登录" + l_userlogincount + "次";

        JLabel userNameLabel = new JLabel(str_user, SwingConstants.LEFT);

        String copyright=(String)NovaClientEnvironment.getInstance().get("COPYRIGHT_INFO"); 
        copyright=(copyright==null)?" 版权所有 \u00a9\u00ae1999-2008，国信朗讯 GXLU Co.,Ltd，保留所有权利。":copyright;
        
        JLabel stateLabel = new JLabel(copyright, SwingConstants.CENTER);
        JLabel loginTimeLabel = new JLabel("  登录时间：" + NovaClientEnvironment.getInstance().get("SYS_LOGIN_TIME")+ "", SwingConstants.RIGHT);

        stateLabel.setBorder(BorderFactory.createLoweredBevelBorder()); //
        userNameLabel.setBorder(BorderFactory.createLoweredBevelBorder()); //
        loginTimeLabel.setBorder(BorderFactory.createLoweredBevelBorder()); //

        statuPanel.add(userNameLabel, BorderLayout.WEST);
        statuPanel.add(stateLabel, BorderLayout.CENTER);
        statuPanel.add(loginTimeLabel, BorderLayout.EAST);

        return statuPanel;
    }
    
    
    /**
	 * 获得公告栏组件
	 * 
	 * @param uid
	 * @param desktopvo
	 * @param modules 界面组件注册管理，用于定时刷新数据等等操作。
	 * @return 
	 */
	public static JTabbedPane getCallboard(String uid,DeskTopVO desktopvo,ArrayList modules){
		Document deskdoc=getDeskTopDesign(false);
		Element callboard=deskdoc.getRootElement().getChild("callboard");
    	if(callboard==null){return null;}
    	
		try{
	        List boards=callboard.getChildren("board");
			if(boards.size()==0){//
				NovaLogger.getLogger(DeskTopPanel.class).error("公告板定义错误！");
				JOptionPane.showMessageDialog(null, "公告板定义错误！", "警告", JOptionPane.ERROR_MESSAGE);
			}
        	//定义一个公告板的放置位置，TAB
	        JTabbedPane callboardPanel=new JTabbedPane(JTabbedPane.TOP); //定义 是否有消息栏，消息栏的tab方向
	        for(int i=0;i<boards.size();i++){
	        	Element board=(Element)boards.get(i);
	        	String title=board.getChildTextTrim("title");
	        	String cls=(board.getChild("class")!=null)?board.getChildTextTrim("class"):null;
	        	int delay=Integer.parseInt((board.getChild("delay")!=null)?board.getChildTextTrim("delay"):"-1");
	        	int interval=Integer.parseInt((board.getChild("interval")!=null)?board.getChildTextTrim("interval"):"-1");
	        	NovaDeskTopModuleIFC mod=cls==null?(new NovaMessageBoardPane()):((NovaDeskTopModuleIFC)(Class.forName(cls).newInstance()));
	        	HashMap params=new HashMap();       
		        params.put("uId", uid);        //默认的所有的组件都必须的参数
		        params.put("_vo", desktopvo);  //默认所有组件必须的参数
	        	Element eparams=board.getChild("params");
	        	List ps=eparams.getChildren();
	        	for(int p=0;p<ps.size();p++){
	        		Element pi=(Element)ps.get(p);
	        		String pname=pi.getName();
	        		String pvalue=pi.getText();
	        		params.put(pname, pvalue);
	        	}
	        	mod.setParams(params); 
	        	NovaDeskTopModuleRegistInfo reg=new NovaDeskTopModuleRegistInfo(mod,delay*2,interval*2);//桌面管理器每半秒计数一次，因此需要乘2
	        	modules.add(reg);//注册模块
	        	
	        	callboardPanel.add(title, mod.getModule());
	        }
	        
	        return callboardPanel;	        
        }catch(Exception e){
        	//e.printStackTrace();
        	return null;
        }
    	
	}
    
    
	
	//====下面的方法应该进一步抽取以满足更加抽象的要求==================================//
	/**
	 * 获得星期的日名称。0-日,1-一,2-二,=3-三,4-四,5-五,6-六。
	 */
	public static String getWeekDayCHS(int i){
    	final String[] _days={"日","一","二","三","四","五","六"};
    	if(i<0) i=0;
    	return _days[i%7];    	
    }
	
}

/**********************************************************************
 *$RCSfile: DesktopUtil.java,v $  $Revision: 1.1.2.4 $  $Date: 2009/09/03 07:11:00 $
 *
 *$Log: DesktopUtil.java,v $
 *Revision 1.1.2.4  2009/09/03 07:11:00  wangqi
 **** empty log message ***
 *
 *Revision 1.1.2.3  2008/12/02 09:31:43  wangqi
 **** empty log message ***
 *
 *Revision 1.1.2.2  2008/10/30 02:45:33  wangqi
 **** empty log message ***
 *
 *Revision 1.1.2.1  2008/10/29 09:31:28  wangqi
 **** empty log message ***
 *
 *********************************************************************/