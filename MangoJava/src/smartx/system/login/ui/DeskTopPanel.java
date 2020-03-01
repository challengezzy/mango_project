/**************************************************************************
 * $RCSfile: DeskTopPanel.java,v $  $Revision: 1.27.2.21 $  $Date: 2010/01/14 08:14:25 $
 **************************************************************************/

package smartx.system.login.ui;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import smartx.framework.common.ui.NovaClientEnvironment;
import smartx.framework.common.ui.NovaRemoteServiceFactory;
import smartx.framework.common.ui.UIUtil;
import smartx.framework.common.ui.component.NovaInternalFrame;
import smartx.framework.common.utils.StringUtil;
import smartx.framework.common.utils.Sys;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaLogger;
import smartx.framework.metadata.lookandfeel.ChangeLookAndFeelDialog;
import smartx.framework.metadata.ui.NovaDialog;
import smartx.framework.metadata.ui.NovaMessage;
import smartx.framework.metadata.ui.ShowClientEnvDialog;
import smartx.framework.metadata.ui.ShowServerEnvDialog;
import smartx.framework.metadata.util.TreeStructBuilder;
import smartx.framework.metadata.util.UIComponentUtil;
import smartx.system.common.constant.CommonSysConst;
import smartx.system.login.ui.deskmodule.NovaDeskTopModuleRegistInfo;
import smartx.system.login.vo.DeskTopVO;

public class DeskTopPanel extends JPanel {

    private static final long serialVersionUID = 2349708048405530570L;

    /**
     * 下面是一个界面定时刷新处理。
     * 主要逻辑：
     *   1）通过timer启动一个定时器，这个定时器每隔0.5秒执行一次（TimeTask），执行下面2和3的任务
     *   2）给计数器timerCounter加1；
     *   3）执行桌面组件检索数据线程（RefreshThread），线程内部按照配置的间隔刷新数据。
     */
    //定时器和计数器
    private Timer timer = null;
    private long timerCounter=0;//每隔0.5秒计数一次
    //界面组件注册
    private ArrayList modules=new ArrayList();
    
    private LoginAppletLoader loader = null;
    
    private static JTree tree = null;
    private JTree plant_tree = null;

    private JSplitPane splitPanelHorizon = null;//splitPanelVertical
    private JSplitPane splitPanelVertical = null;
    
    //    added by john_liu, 2007.09.06    for MR#: 把主标题的label开发出来，可以被业务重新设置    begin
    private JLabel mainTitleLabel = null;
    //    added by john_liu, 2007.09.06    for MR#: 把主标题的label开发出来，可以被业务重新设置    end

    private String userID = null;

    private JToolBar buttonBarPanel = null;

    private JScrollPane jScrollPane_left = null;

    private JScrollPane jScrollPane_plantform_menu = null;
    
    //右边的内容面板
    NovaDeskTopPane desktop;

    private DeskTopVO deskTopVO = null;
    
    
    JPanel panel_sysbtn;//系统按纽，项目可以根据需要添加按纽，所以暴露出去

    ClientPanelInitInterface clientPanelInitInterface=null;

    
    
    /**
     * 
     * @param _loader
     * @param _username
     * @param _logintime
     */
    public DeskTopPanel(LoginAppletLoader _loader, String _userID) {
        this.loader = _loader;
        this.userID = _userID;
        initialize();
    }

    /**
     * 初始化过程
     */
    private void initialize() {
    	this.setBackground(new java.awt.Color(236, 255, 255 , 100));
        this.setLayout(new BorderLayout());
    	
    	/**
         * 业务逻辑：
         * 0、获得当前用户的桌面配置信息（菜单、工具栏等等）
         *   
         * 1、生成左边TAB菜单区，包含两颗树 应用菜单树和系统菜单树
         * 2、...
         */
    	
    	//检索桌面功能配置信息
    	this.getDeskTopVO(true);//第一次强制从后台检索 
    	DesktopUtil.getStyleSetting();
    	DesktopUtil.initNovaComponentSetting();
    	
    	String deskmode=(String)NovaClientEnvironment.getInstance().get("deskmode");
    	if("menuframe".equalsIgnoreCase(deskmode)){
    		initialize_menuframe();
    	}else if("treeframe".equalsIgnoreCase(deskmode)){
    		initialize_treeframe();
    	}else if("menutab".equalsIgnoreCase(deskmode)){
    		NovaMessage.show("指定的桌面定义还未启用，默认传统桌面！");
    		initialize_treeframe();
    	}else if("treetab".equalsIgnoreCase(deskmode)){
    		NovaMessage.show("指定的桌面定义还未启用，默认传统桌面！");
    		initialize_treeframe();
    	}else{
    		NovaMessage.show("没有设置正确的桌面定义，默认传统桌面！");
    		initialize_treeframe();
    	}
    	
    }
    
    /**
     * 初始化过程
     */
    private void initialize_menuframe() {
    	//TODO 下面是界面布局处理
        //====界面装载开始============================// 
        //     创建菜单树
    	
        
        //把所有的菜单合并到菜单树上
        Vector cmds=DesktopUtil.getDeskOperationList(this.deskTopVO);
        smartx.framework.metadata.util.TreeNode root=TreeStructBuilder.buildTree(null,cmds,"id","parentmenuid");
        if(NovaClientEnvironment.getInstance().isAdmin()){        	
        	cmds=DesktopUtil.getSysOperationList();
            root=TreeStructBuilder.buildTree(root,cmds,"id","parentmenuid");
        }
        JMenuBar mb=DesktopUtil.getMenuBar(null,root,this);        
        JRootPane rpanel=this.loader.getApplet().getRootPane();
        rpanel.setJMenuBar(mb);
        
    	//中间主界面
        JPanel panel_pp = new JPanel();
        panel_pp.setLayout(new BorderLayout());
        panel_pp.add(getContentSplitPanel(), BorderLayout.CENTER);

        //
        JPanel panel_news = new JPanel();
        panel_news.setLayout(new FlowLayout()); //
        panel_news.setBackground(new java.awt.Color(255, 255, 245)); //

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());

        String loginUserInfo=(String)(NovaClientEnvironment.getInstance().get(CommonSysConst.SYS_LOGIN_USERINFO));
        String mainTitle =(loginUserInfo!=null)?loginUserInfo:(
    		  "欢迎【" + NovaClientEnvironment.getInstance().get("SYS_LOGINUSER_NAME") + "】使用" +
                                NovaClientEnvironment.getInstance().get("PROJECT_NAME") + "  [今天是 " +
                                calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月" +
                                calendar.get(Calendar.DAY_OF_MONTH)
                                + "日 星期" + DesktopUtil.getWeekDayCHS( (calendar.get(Calendar.DAY_OF_WEEK) - 1)) + "]");
	    mainTitleLabel = new JLabel();
	    mainTitleLabel.setText(mainTitle);
	    panel_news.add(mainTitleLabel);
	      
	    
	    //上端的工具栏所在的panel
	    JPanel panel_btn = new JPanel(new BorderLayout()); //
	    panel_btn.setBackground(new java.awt.Color(255, 255, 245)); //
	    
	    //创建左边的工具栏
	    //FlowLayout fl = new FlowLayout(FlowLayout.LEFT);
	    //fl.setVgap(1);
	    //fl.setHgap(1);
	    //JPanel panel_allbuttons = new JPanel();//(fl);
        buttonBarPanel=new JToolBar();
        buttonBarPanel.setFloatable(false);
        buttonBarPanel.setBackground(new java.awt.Color(255, 255, 245)); //
	    //panel_allbuttons.add(getButtonBarPanel()); //
	    addToolBarButtons(buttonBarPanel);

	    //创建右边的工具栏
	    panel_sysbtn = new JPanel();//(fl);
        panel_sysbtn.setBackground(new java.awt.Color(255, 255, 245)); //
        if (NovaClientEnvironment.getInstance().isAdmin()) {
        	//如果是管理登录，则增加几个管理按钮            
            JButton btn_1 = new JButton(UIUtil.getImage("images/platform/site.gif")); //
            JButton btn_2 = new JButton(UIUtil.getImage("images/platform/save.gif")); //
            btn_1.setToolTipText("查看客户端所有环境变量");
            btn_2.setToolTipText("查看服务器端所有环境变量");
            btn_1.setPreferredSize(new Dimension(16, 16));
            btn_2.setPreferredSize(new Dimension(16, 16));
            btn_1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    onShowClientEnv();
                }
            });
            btn_2.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    onShowServerEnv(); //
                }
            });
            panel_sysbtn.add(btn_1); //
            panel_sysbtn.add(btn_2); //            
        }
        
        //切换风格模板
        JButton btn_lookandfeel = UIComponentUtil.getIconButton(
            Sys.getSysRes("sys.toolbar.lookandfeel.msg"), 
            UIUtil.getImage(Sys.getSysRes("sys.toolbar.lookandfeel.icon")),
            UIComponentUtil.getIconButtonDefaultSize(),
            true,false,
            new ActionListener() {
                public void actionPerformed(ActionEvent a) {
                	//NovaMessage.show(DeskTopPanel.this, Sys.getSysRes("sys.toolbar.lookandfeel.msg"));
                	new ChangeLookAndFeelDialog(DeskTopPanel.this,"模板风格切换",true); 
                }
            }
        );
        panel_sysbtn.add(btn_lookandfeel); //
        
        //修改密码按钮
        JButton btn_5 = new JButton(UIUtil.getImage("images/platform/resetpwd.gif")); //
        btn_5.setToolTipText("修改密码"); //
        btn_5.setPreferredSize(new Dimension(16, 16));
        btn_5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                	NovaDialog frame = (NovaDialog)Class.forName("smartx.system.login.ui.ResetPwdFrame").newInstance();
                    if (!frame.isVisible()) {
                        frame.setVisible(true);
                    }
                    frame.toFront();
                } catch (InstantiationException e1) {
                    e1.printStackTrace();
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        });
        panel_sysbtn.add(btn_5); // 
        
        JButton btn_relogin = new JButton(UIUtil.getImage("images/platform/relogin.gif"));
        btn_relogin.setToolTipText("重新登录");
        btn_relogin.setPreferredSize(new Dimension(20, 20)); //
        btn_relogin.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (JOptionPane.showConfirmDialog(DeskTopPanel.this, "您真的想退出重新登录吗?", "提示", JOptionPane.YES_NO_OPTION) !=
                    JOptionPane.YES_OPTION) {
                    return;
                }
                onRelogin();
            }
        });
        panel_sysbtn.add(btn_relogin);
        
        JButton btn_exit = new JButton(UIUtil.getImage("images/platform/exit.gif"));
        btn_exit.setToolTipText("退出");
        btn_exit.setPreferredSize(new Dimension(20, 20)); //
        btn_exit.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (JOptionPane.showConfirmDialog(DeskTopPanel.this, "您真的想退出系统吗?", "提示", JOptionPane.YES_NO_OPTION) !=
                    JOptionPane.YES_OPTION) {
                    return;
                }
                onExit();
            }
        });
        panel_sysbtn.add(btn_exit);
        
        
        //工具条栏 = 左工具条 + 中间提示 + 右边工具 
        panel_btn.add(panel_sysbtn, BorderLayout.EAST);     // 系统按钮：系统管理和密码修改
        panel_btn.add(buttonBarPanel, BorderLayout.WEST); // 菜单快捷按钮 
        panel_btn.add(panel_news, BorderLayout.CENTER);     // 信息提示按钮（将来可以用于滚动提示信息）

        //主界面面板 上面 = 工具条栏
        panel_pp.add(panel_btn, BorderLayout.NORTH);
        
        this.add(panel_pp, BorderLayout.CENTER);
        //最外层界面增加 状态栏
        this.add(DesktopUtil.getStatuPanel(), BorderLayout.SOUTH);

        //====界面装载结束============================// 
        //启动桌面计时器
        this.timer= new Timer(true);
		this.timer.schedule(new TimeTask(), 500,500);   
    }

    /**
     * 初始化过程
     */
    private void initialize_treeframe() {
    	/**
         * 业务逻辑：
         * 0、获得当前用户的桌面配置信息（菜单、工具栏等等）
         * 1、生成左边TAB菜单区，包含两颗树 应用菜单树和系统菜单树
         * 2、...
         */
    	
        //1、当前用户的功能菜单树 和 系统管理菜单树
        jScrollPane_left = new JScrollPane(getTree()); // 所有应用菜单形成的树
        JTabbedPane tabPanel = new JTabbedPane(JTabbedPane.TOP);
        tabPanel.addTab("所有应用", jScrollPane_left); //把功能菜单树放到TAB内
        if (NovaClientEnvironment.getInstance().isAdmin()) {
        	//是否管理登录        
        	plant_tree = getPlantformTree(); // 系统功能的树!!
            jScrollPane_plantform_menu = new JScrollPane(plant_tree); // 滚动框
            tabPanel.addTab("系统管理", jScrollPane_plantform_menu); //
        }
        
        //公告板处理
        JTabbedPane callboardPanel=DesktopUtil.getCallboard(this.userID, this.deskTopVO, this.modules);
        if(callboardPanel!=null){
        	JSplitPane leftPanel=new JSplitPane(JSplitPane.VERTICAL_SPLIT,tabPanel,callboardPanel);
        	leftPanel.setDividerSize(8);
        	leftPanel.setDividerLocation(350);
        	leftPanel.setEnabled(true);//可以左右拖动
        	leftPanel.setOneTouchExpandable(true); //快捷小按钮处理收放
	        splitPanelHorizon = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, getContentSplitPanel());
        }else{
        	splitPanelHorizon = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tabPanel, getContentSplitPanel());
        }
        

        //建立左右分隔区域，左边是TAB【功能菜单】，右边是Panel【内容区域】
        //splitPanelHorizon = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tabPanel, getContentSplitPanel());
        splitPanelHorizon.setDividerSize(10);
        splitPanelHorizon.setDividerLocation(180);
        splitPanelHorizon.setEnabled(true);
        splitPanelHorizon.setOneTouchExpandable(true); //

        //主界面面板 中间 = 左右分割面板
        JPanel panel_pp = new JPanel();
        panel_pp.setLayout(new BorderLayout());
        panel_pp.add(splitPanelHorizon, BorderLayout.CENTER);

        //
        JPanel panel_news = new JPanel();
        panel_news.setLayout(new FlowLayout()); //
        panel_news.setBackground(new java.awt.Color(255, 255, 245)); //

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());

        String loginUserInfo=(String)(NovaClientEnvironment.getInstance().get(CommonSysConst.SYS_LOGIN_USERINFO));
        String mainTitle =(loginUserInfo!=null)?loginUserInfo:(
    		  "欢迎【" + NovaClientEnvironment.getInstance().get("SYS_LOGINUSER_NAME") + "】使用" +
                                NovaClientEnvironment.getInstance().get("PROJECT_NAME") + "  [今天是 " +
                                calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月" +
                                calendar.get(Calendar.DAY_OF_MONTH)
                                + "日 星期" + DesktopUtil.getWeekDayCHS( (calendar.get(Calendar.DAY_OF_WEEK) - 1)) + "]");
	    mainTitleLabel = new JLabel();
	    mainTitleLabel.setText(mainTitle);
	    panel_news.add(mainTitleLabel);
	      
	    FlowLayout fl = new FlowLayout(FlowLayout.LEFT);
	    fl.setVgap(1);
	    fl.setHgap(1);
	      
	    //创建左边的工具栏
	    //JPanel panel_allbuttons = new JPanel();//(fl);
        buttonBarPanel=new JToolBar();
        buttonBarPanel.setFloatable(false);
        buttonBarPanel.setBackground(new java.awt.Color(255, 255, 245)); //
	    //panel_allbuttons.add(getButtonBarPanel()); //
	    addToolBarButtons(buttonBarPanel);

	    //创建右边的工具栏
	    JPanel panel_btn = new JPanel(new BorderLayout()); //
        panel_btn.setBackground(new java.awt.Color(255, 255, 245)); //
        panel_sysbtn = new JPanel(fl);
        panel_sysbtn.setBackground(new java.awt.Color(255, 255, 245)); //
        if (NovaClientEnvironment.getInstance().isAdmin()) {
        	//如果是管理登录，则增加几个管理按钮            
            JButton btn_1 = new JButton(UIUtil.getImage("images/platform/site.gif")); //
            JButton btn_2 = new JButton(UIUtil.getImage("images/platform/save.gif")); //
            btn_1.setToolTipText("查看客户端所有环境变量");
            btn_2.setToolTipText("查看服务器端所有环境变量");
            btn_1.setPreferredSize(new Dimension(20, 20));
            btn_2.setPreferredSize(new Dimension(20, 20));
            btn_1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    onShowClientEnv();
                }
            });
            btn_2.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    onShowServerEnv(); //
                }
            });
            panel_sysbtn.add(btn_1); //
            panel_sysbtn.add(btn_2); //            
        }
        
        //切换风格模板
        JButton btn_lookandfeel = UIComponentUtil.getIconButton(
            Sys.getSysRes("sys.toolbar.lookandfeel.msg"), 
            UIUtil.getImage(Sys.getSysRes("sys.toolbar.lookandfeel.icon")),
            UIComponentUtil.getIconButtonDefaultSize(),
            true,false,
            new ActionListener() {
                public void actionPerformed(ActionEvent a) {
                	//NovaMessage.show(DeskTopPanel.this, Sys.getSysRes("sys.toolbar.lookandfeel.msg"));
                	new ChangeLookAndFeelDialog(DeskTopPanel.this,"模板风格切换",true); 
                }
            }
        );
        panel_sysbtn.add(btn_lookandfeel); //
        
        //修改密码按钮
        JButton btn_5 = new JButton(UIUtil.getImage("images/platform/resetpwd.gif")); //
        btn_5.setToolTipText("修改密码"); //
        btn_5.setPreferredSize(new Dimension(20, 20));
        btn_5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                	NovaDialog frame = (NovaDialog)Class.forName("smartx.system.login.ui.ResetPwdFrame").newInstance();
                    if (!frame.isVisible()) {
                        frame.setVisible(true);
                    }
                    frame.toFront();
                } catch (InstantiationException e1) {
                    e1.printStackTrace();
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        });
        panel_sysbtn.add(btn_5); // 
        
        JButton btn_relogin = new JButton(UIUtil.getImage("images/platform/relogin.gif"));
        btn_relogin.setToolTipText("重新登录");
        btn_relogin.setPreferredSize(new Dimension(20, 20)); //
        btn_relogin.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (JOptionPane.showConfirmDialog(DeskTopPanel.this, "您真的想退出重新登录吗?", "提示", JOptionPane.YES_NO_OPTION) !=
                    JOptionPane.YES_OPTION) {
                    return;
                }
                onRelogin();
            }
        });
        panel_sysbtn.add(btn_relogin);
        
        JButton btn_exit = new JButton(UIUtil.getImage("images/platform/exit.gif"));
        btn_exit.setToolTipText("退出");
        btn_exit.setPreferredSize(new Dimension(20, 20)); //
        btn_exit.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (JOptionPane.showConfirmDialog(DeskTopPanel.this, "您真的想退出系统吗?", "提示", JOptionPane.YES_NO_OPTION) !=
                    JOptionPane.YES_OPTION) {
                    return;
                }
                onExit();
            }
        });
        panel_sysbtn.add(btn_exit);
        

        

        //工具条栏 = 左工具条 + 中间提示 + 右边工具 
        panel_btn.add(panel_sysbtn, BorderLayout.EAST);     // 系统按钮：系统管理和密码修改
        panel_btn.add(buttonBarPanel, BorderLayout.WEST); // 菜单快捷按钮 
        panel_btn.add(panel_news, BorderLayout.CENTER);     // 信息提示按钮（将来可以用于滚动提示信息）

        //主界面面板 上面 = 工具条栏
        panel_pp.add(panel_btn, BorderLayout.NORTH);
        
        //上下分隔条 上面=标题栏 下面=主界面面板
        splitPanelVertical = new JSplitPane(JSplitPane.VERTICAL_SPLIT, getTitlePanel(), panel_pp);
        splitPanelVertical.setDividerSize(10);
        splitPanelVertical.setDividerLocation(70);
        splitPanelVertical.setEnabled(true);
        splitPanelVertical.setOneTouchExpandable(true); //

        //最外层界面增加 上下分隔条为中间区域
        this.add(splitPanelVertical, BorderLayout.CENTER);
        //最外层界面增加 状态栏
        this.add(DesktopUtil.getStatuPanel(), BorderLayout.SOUTH);

        //初始化结束，启动桌面计时器
        this.timer= new Timer(true);
		this.timer.schedule(new TimeTask(), 500,500);   
    }
    
    /**
     * 1、创建顶端标题栏
     * @return
     */
    //TODO 提供配置标题上的底图、标题区域按钮等等
    private JLayeredPane getTitlePanel() {
    	return new TitlePanel(); 
    }
    
    /**
     * 标题区域窗口
     * @author Administrator
     *
     */
    class TitlePanel extends JLayeredPane{
    	private static final long serialVersionUID = 4161152852750175753L;
    	private JLabel title_bg=null, title_text=null, title_logo=null;
    	private ImageIcon img_bg=null,img_logo=null,img_text=null;
    	
    	
		public TitlePanel(){
    		super();
    		setBorder(BorderFactory.createEmptyBorder());
            setBackground(new java.awt.Color(69, 124, 191)); //======================
            
            int _defaultLayer=JLayeredPane.MODAL_LAYER.intValue();
            
            
            img_bg=UIUtil.getImage(UIUtil.getEnvironmentParamStr("IMAGE_DESKTOPTITLEBG", "images/logo/title-bg.gif"));
            title_bg=new JLabel(img_bg);
            add(title_bg, new Integer(_defaultLayer++));
            
            img_logo=UIUtil.getImage(UIUtil.getEnvironmentParamStr("IMAGE_DESKTOPTITLELOGO", "images/logo/title-logo.gif"));
            title_logo=new JLabel(img_logo);
            add(title_logo, new Integer(_defaultLayer++));
            
            img_text=UIUtil.getImage(UIUtil.getEnvironmentParamStr("IMAGE_DESKTOPTITLETEXT", "images/logo/title-text.gif"));
            title_text=new JLabel(img_text);
            add(title_text, new Integer(_defaultLayer++));
            
            //增加控件的resize事件
    		addComponentListener(new ComponentAdapter(){
    			public void componentResized(ComponentEvent e) {
    				doResize();
    			}
    		});
    	}
		
		private void doResize(){
			int appletWidth = this.getWidth();
			int appletHeight = this.getHeight();
			title_bg.setBounds(0, 0, appletWidth, appletHeight);
			int textwidth=img_text.getIconWidth();
			title_text.setBounds(0, 0, textwidth, appletHeight);
			int logowidth=img_logo.getIconWidth();
			title_logo.setBounds(appletWidth-logowidth, 0, logowidth, appletHeight);
		}
		
		
		
    }
    
    
    //时间计数器线程，应该尽量的短
    class TimeTask extends TimerTask{
    	public void run(){
    		//SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    		//System.out.println(sdf.format(new Date()));
    		timerCounter++;//计数器
    		//解决swing的线程安全问题，具体表现在更新数据时报边界溢出或者新数据不能显示等问题
    		EventQueue.invokeLater(new RefreshThread());    		
    		//(new RefreshThread()).start();//起一个线程处理，让Timer线程尽量的短
    	}
    	
    }
    //桌面组件检索数据线程
    class RefreshThread extends Thread {
        public void run() {
        	int count=modules.size();
        	for(int i=0;i<count;i++){
        		NovaDeskTopModuleRegistInfo reg=(NovaDeskTopModuleRegistInfo)modules.get(i);
        		int delay=reg.getDelay();
        		int interval=reg.getInterval();
        		//第一次执行，在delay相等的时候执行一次
    			if(timerCounter==delay){//如果delay小于0或者等于0根本不会执行，所以不用担心
    				try{
    					reg.getModule().exec();
    				}catch(Exception e){
        				NovaLogger.getLogger(DeskTopPanel.class).error("执行模块处理错误！",e);
        			}
        		}
    			if(interval>=0){//循环任务
    				if((timerCounter-delay)%interval==0){//每次间隔时间到
    					try{
        					reg.getModule().exec();
        				}catch(Exception e){
            				NovaLogger.getLogger(DeskTopPanel.class).error("执行模块处理错误！",e);
            			}
    				}
    			}
        	}
        }
    }
    
    /**
     * 获得deskTopVO配置信息
     * 1、根据客户端环境变量中的配置中获得DeskTopVO的服务配置和参数配置
     * 2、获得参数并调用，获得DeskTopVO
     * fixed since 1.30
     * @return DeskTopVO
     */
    private DeskTopVO getDeskTopVO(boolean refresh) {
    	if (refresh || deskTopVO == null) {
        	this.deskTopVO=DesktopUtil.getDeskTopVO();
        }        
        return this.deskTopVO;
    }

    private void addToolBarButtons(JToolBar tbar) {
    	HashVO[] vos=(HashVO[])(DesktopUtil.getDeskOperationTool(this.deskTopVO)).toArray(new HashVO[0]);
    	
        for (int i = 0; i < vos.length; i++) {
            JButton bt = null;
            String str_iconName = vos[i].getStringValue("icon"); //
            if (str_iconName == null || str_iconName.trim().equals("")) {
                bt = new JButton(UIUtil.getImage("images/platform/blank.gif")); //
            } else {
                ImageIcon icon = UIUtil.getImage(str_iconName);
                if (icon != null) {
                    bt = new JButton(icon); //
                } else {
                    bt = new JButton(UIUtil.getImage("images/platform/blank.gif")); //
                }
            }

            bt.setPreferredSize(new Dimension(20, 16));
            bt.setToolTipText(vos[i].getStringValue("localname")); //
            bt.putClientProperty("LOCALNAME", vos[i].getStringValue("LOCALNAME"));
            bt.putClientProperty("COMMANDTYPE", vos[i].getStringValue("COMMANDTYPE"));
            bt.putClientProperty("COMMAND", vos[i].getStringValue("COMMAND"));
            bt.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JComponent sourceObj = (JComponent) e.getSource();
                    String str_localname = (String) sourceObj.getClientProperty("LOCALNAME"); //
                    String str_commandtype = (String) sourceObj.getClientProperty("COMMANDTYPE"); //
                    String str_command = (String) sourceObj.getClientProperty("COMMAND"); //
                    openAppMainFrameWindow(str_commandtype, str_command, new String[] {str_localname}); //
                }
            });

            tbar.add(bt);
            if (vos[i].getStringValue("TOOLBARISADDSEP") != null && vos[i].getStringValue("TOOLBARISADDSEP").equals("Y")) {
            	tbar.addSeparator(new Dimension(2, 0));
            }
        }
    }

    /**
     * 刷新左边菜单树上面的快捷键面板
     */
    private void refreshButtonBarPanel() {
    	buttonBarPanel.removeAll();
    	addToolBarButtons(buttonBarPanel);
    	buttonBarPanel.updateUI();    
    }
    
    /**
     * 刷新树
     */
    private void refreshTree() {
        this.getDeskTopVO(true);

        getTree(); //
        jScrollPane_left.getViewport().removeAll(); // 滚动框
        jScrollPane_left.getViewport().add(tree); //
        jScrollPane_left.getViewport().updateUI();
        jScrollPane_left.updateUI(); //
    }

    /**
     * 构建业务处理树结构
     * @return
     */
    private JTree getTree() {
    	tree = DesktopUtil.getCommanTree(DesktopUtil.getDeskOperationList(this.deskTopVO));
        tree.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                if (evt.getButton() == MouseEvent.BUTTON3) {
                    showPopMenu(evt.getComponent(), evt.getX(), evt.getY()); // 弹出菜单
                } else if (evt.getClickCount() == 2) {
                    onClickTree(tree);
                }
            }
        });

        tree.setRootVisible(false);
        //创建树节点展现对象
        MyTreeCellRender myTreeCellRender = new MyTreeCellRender();
        tree.setCellRenderer(myTreeCellRender);
        //增加菜单显示的附加字符串
        if (clientPanelInitInterface != null) {
            HashMap map = clientPanelInitInterface.getMenuAppendString();
            appendMenuTreeNodeDisplay(map);
        }
        return tree; //
    }

    /**
     * 创建平台管理树结构
     * @return
     */
    private JTree getPlantformTree() {
    	JTree aJTree = DesktopUtil.getCommanTree(DesktopUtil.getSysOperationList());
        aJTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION); //
        aJTree.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                if (evt.getClickCount() >= 2) {
                    onClickTree(plant_tree);
                }
            }
        });
        aJTree.setRootVisible(false); // 根结点不显示!!
        return aJTree;
    }
    
    /**
     * 菜单点击响应方法
     */
    private void onClickTree(JTree tree) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
        if (node.isLeaf()) {
            HashVO vo = (HashVO) node.getUserObject(); //
            
            // 模板导航路径
            TreePath path = tree.getSelectionPath();
            Object[] treenode = path.getPath();
            String[] str_tree_path = new String[treenode.length - 1];
            for (int i = 0; i < str_tree_path.length; i++) {
                str_tree_path[i] = ( ( HashVO ) ( ( DefaultMutableTreeNode ) treenode[i + 1] ).getUserObject() ).getStringValue( "localname" );                
            }
            
            onCommandAction(vo,str_tree_path);
        }
    }
    /**
     * 执行根据HashVO定义的命令
     * @param vo
     */
    public void onCommandAction(HashVO vo,String[] path) {
       
        String str_commandtype = vo.getStringValue("commandtype"); //
        String str_command = vo.getStringValue("command");
        
        openAppMainFrameWindow(str_commandtype, str_command, path); //
    }
    
    


    /**
     * 创建右键菜单
     * @param _compent
     * @param _x
     * @param _y
     */
    private void showPopMenu(Component _compent, int _x, int _y) {
        ImageIcon icon_refresh=null;
        ImageIcon icon_expand=null;
        ImageIcon icon_collapse=null;
        try {
            icon_refresh = UIUtil.getImage("images/platform/refresh.gif");
            icon_expand = UIUtil.getImage("images/platform/down1.jpg");
            icon_collapse = UIUtil.getImage("images/platform/up1.jpg");
        } catch (Exception ex) {
        }

        JPopupMenu popmenu_header = new JPopupMenu();
        JMenuItem item_refresh = new JMenuItem("刷新数据",icon_refresh); // 解锁
        JMenuItem item_expand = new JMenuItem("展开所有功能点",icon_expand); // 展开所有结点
        JMenuItem item_collapse = new JMenuItem("收缩所有功能点",icon_collapse); // 收缩所有结点

        item_refresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshTree();
                refreshButtonBarPanel(); // 刷新按钮栏
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

        popmenu_header.add(item_refresh); //
        popmenu_header.add(item_expand); //
        popmenu_header.add(item_collapse); //

        ((NovaDeskTopPane)getContentSplitPanel()).addWindowsMenu(popmenu_header);

        popmenu_header.show(_compent, _x, _y); //
    }

    

    
    
    
    //登出前处理
	private void initSecondProjectClientBeforeLogout(Vector v) {
		if (v != null) {
			for (int i = 0; i < v.size(); i++) {
				String className = (String) v.get(i);
				try {
					if (className != null && !className.equals("")) {
						ClientPanelBeforeLogoutInterface client = (ClientPanelBeforeLogoutInterface) Class.forName(className).newInstance();
						client.init();
					}
				} catch (Exception ex) {
					System.out.println("客户端附加登出处理错误：" + className + "\n" + ex.getMessage());
				}
			}
		}
	}
    
    //登出
    private void doLogout(){
    	timer.cancel();//计时器关闭
    	try{
    		Vector secondProjectClientBeforeLogout = null; //附加客户端登出前处理
			HashMap enParam = UIUtil.getEnvironmentParam();
			secondProjectClientBeforeLogout = (Vector) enParam.get(CommonSysConst.CLIENTBEFORELOGOUT);
			//首先处理登出前
			initSecondProjectClientBeforeLogout(secondProjectClientBeforeLogout);
    	}catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(this, e1.getMessage());			
		}
    	
    	// 远程访问处理该用户退出处理逻辑..比如修改该用户在线标记等!
        try {
            String userID = (String) NovaClientEnvironment.getInstance().get("SYS_LOGINUSER_ID");
            if (userID != null && !userID.equals("")) {
                SystemLoginServiceIFC loginService = (SystemLoginServiceIFC) NovaRemoteServiceFactory.getInstance().
                    lookUpService(SystemLoginServiceIFC.class);
                loginService.logout(userID);
            }
        } catch (java.lang.ClassNotFoundException ex) {
            ex.printStackTrace(); //
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    

    /**
     * 重登录
     */
    private void onRelogin() {
        this.doLogout();
        loader.loadLoginPanel();
    }

    private void onExit() {
    	this.doLogout();
        System.exit(0);
    }
    

    /**
     * 打开Clinet区域的窗口
     * @param _commandtype
     * @param _command
     * @param _currpositiondesc
     */
  //TODO 关注打开窗口的方式
    private void openAppMainFrameWindow(String _commandtype, String _command, String[] _currpositiondesc) {
        long ll_1 = System.currentTimeMillis();
        try {
            //通过菜单路径寻找该窗口是否已经打开
            String menuPath = new String();
            if (_currpositiondesc != null) {
                for (int i = 0; i < _currpositiondesc.length; i++) {
                    menuPath = menuPath + _currpositiondesc[i] + "-";
                }
                menuPath=menuPath.substring(0,menuPath.lastIndexOf("-"));
            }
            Object frame = desktop.getOpenedWindow(menuPath);

            this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            if (frame == null) {
                if (_commandtype == null || _commandtype.trim().equals("null") || _commandtype.trim().equals("")) {
                    JOptionPane.showMessageDialog(this,"打开结点失败,结点类型为空,CommandType[" + _commandtype + "],Command[" + _command + "]");
                    return;
                } else if (_commandtype.equals("0")) { 
                	// 自定义Frame,在command中配置类路径
                    if (_command == null || _command.trim().equals("null") || _command.trim().equals("")) {
                        JOptionPane.showMessageDialog(this, "打开结点失败,结点类型为空,CommandType[" + _commandtype + "],Command[" + _command + "]");
                        return;
                    }
                    try {
                        frame = Class.forName(_command.trim()).newInstance();
                    } catch (java.lang.ClassNotFoundException ex) {
                        JOptionPane.showMessageDialog(this, "注册的Frame类[" + _command.trim() + "]不存在!!");
                    }
                } else { 
                	HashMap map =StringUtil.parseParamsUpper(_command);//key处理成大写，因为模板实现类中读取时按照大写参数
                	map.put("SYS_SELECTION_PATH", _currpositiondesc);

                    //通过配置获得风格模板实现类，并通过反射创建类实例
                	//  获得风格模板定义，把系统对应的风格模板和风格模板对应实现文件管理起来。同时保存了风格模板对应的参数定义类。
                	HashMap stylemap=DesktopUtil.getStyleSetting();
                    String framecls=(String)stylemap.get(_commandtype);                    
                    try {
                        Class frameclass = Class.forName(framecls);                        
                        Constructor constructor=frameclass.getConstructor(new Class[]{HashMap.class});
                        frame=constructor.newInstance(new Object[]{map});                        
                    } catch (ClassNotFoundException ex) {
                        JOptionPane.showMessageDialog(this, "指定的风格模板【"+_commandtype+"】参数[" + _command.trim() + "]不存在!!");
                    } catch (NoSuchMethodException e){
                    	JOptionPane.showMessageDialog(this, "指定的风格模板【"+_commandtype+"】参数[" + _command.trim() + "]不存在参数为HashMap的构造方法!!");
                    } catch (SecurityException e){
                    	JOptionPane.showMessageDialog(this, "指定的风格模板【"+_commandtype+"】参数[" + _command.trim() + "]的构造方法（参数HashMap）不能允许调用!!");
                    } catch (Exception e){
                    	JOptionPane.showMessageDialog(this, "指定的风格模板【"+_commandtype+"】参数[" + _command.trim() + "]创建失败!!");
                    }
                    
                }
            }
            
            if (frame != null) {
            	//frame不空，判断frame类型，使用各自的方式展现            	
                if (frame instanceof NovaInternalFrame) {//轻量内部frame
                	desktop.showWindow((NovaInternalFrame)frame,menuPath);
                } else if (frame instanceof JFrame) {//普通frame
                    if (! ( (JFrame) frame).isVisible()) {
                        ( (JFrame) frame).setVisible(true);
                    }
                    ( (JFrame) frame).toFront();
                } else if (frame instanceof NovaDialog) {//对话框
                    if (! ( (NovaDialog) frame).isVisible()) {
                        ( (NovaDialog) frame).setVisible(true);
                    }
                    ( (NovaDialog) frame).setDeskTopPane(desktop);
                    ( (NovaDialog) frame).setMenuPath(menuPath);
                    ( (NovaDialog) frame).toFront();
                } else {//其他
                    NovaMessage.show(this, "目前不支持该种窗体对象的显示！"+frame.getClass().getName());
                }
            } else {
            	NovaMessage.show(this, "没有生成对应窗体！");
            }
            long ll_2 = System.currentTimeMillis();
            System.out.println("加载页面[" + _commandtype + "] [" + _command + "]成功,耗时[" + (ll_2 - ll_1) + "]!!");
        } catch (Exception ex) {
            //ex.printStackTrace();
            NovaMessage.show(this, "打开窗口失败,原因:" + ex.getMessage());
        } finally {
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); //
        }
    }

    private void onShowClientEnv() {
        new ShowClientEnvDialog(this); //
    }

    private void onShowServerEnv() {
        new ShowServerEnvDialog(this); //
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

    private void myExpandAll() {
        expandAll(tree, true);
    }

    private void myCollapseAll() {
        expandAll(tree, false);
    }


    private JDesktopPane getContentSplitPanel() {
        if(desktop==null) {
            desktop = new NovaDeskTopPane();
        }
        return desktop;
    }

    /**
     * 菜单展示对象
     * @author James.W
     *
     */
    class MyTreeCellRender extends DefaultTreeCellRenderer {

        private static final long serialVersionUID = 7173353751862932053L;

        public MyTreeCellRender() {

        }

        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
            boolean leaf, int row, boolean hasFocus) {
            JLabel label = (JLabel)super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            if (value instanceof DefaultMutableTreeNode) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                if (!node.isRoot()) {
                    HashVO nodeVO = (HashVO) node.getUserObject();
                    String str_icon = nodeVO.getStringValue("icon");
                    String str_commandtype = nodeVO.getStringValue("commandtype");
                    String str_command = nodeVO.getStringValue("command");
                    if (str_icon != null && !str_icon.trim().equals("")) {
                        ImageIcon icon = UIUtil.getImage(str_icon);
                        if (icon != null) {
                            label.setIcon(icon); //
                        }
                    }
                    label.setToolTipText("[" + str_commandtype + "][" + str_command + "]"); //
                }
            }
            return label;
        }
    }

    
    public JPanel getSysbtnPanel() {
        return panel_sysbtn;
    }
    //    added by john_liu, 2007.09.06    for MR#: 把主标题的label开发出来，可以被业务重新设置    begin
    public JLabel getMainTitleLabel()
    {
        return this.mainTitleLabel;
    }
    //    added by john_liu, 2007.09.06    for MR#: 把主标题的label开发出来，可以被业务重新设置    end

    // James.W 2007.10.11 增加 synchronized
    public synchronized static void appendMenuTreeNodeDisplay(HashMap map) {
        if (map != null) {
            Iterator it = map.keySet().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                appendMenuTreeNodeDisplay(key, (String) map.get(key));
            }
        }
    }

    // James.W 2007.10.11 增加 synchronized
    public synchronized static void appendMenuTreeNodeDisplay( String menukey, String appendName )
    {
        synchronized ( tree )
        {
            DefaultMutableTreeNode node = findTreeNode( ( DefaultMutableTreeNode ) tree.getModel().getRoot(), menukey );
            if ( node != null )
            {
                HashVO vo = ( HashVO ) node.getUserObject();
                String value = vo.getStringValue( "localname" );
                String s = "<html>" + value + "<font color='#0080ff'>" + appendName + "</font></html>";
                vo.setToStringValue( s );
                node.setUserObject( vo );
                tree.updateUI();
            }
        }
    }

    private static DefaultMutableTreeNode findTreeNode(DefaultMutableTreeNode node,String menukey) {
        if((node.getUserObject() instanceof HashVO) &&
           ((HashVO)node.getUserObject()).getStringValue("name").equals(menukey)) {
            return node;
        }
        if(node.getChildCount()>0) {
            for (int i = 0; i < node.getChildCount(); i++) {
                DefaultMutableTreeNode child=findTreeNode( (DefaultMutableTreeNode) node.getChildAt(i), menukey);
                if(child!=null) {
                    return child;
                }
            }
        }
        return null;
    }

    public void setClientPanelInitInterface(ClientPanelInitInterface _clientPanelInitInterface) {
        this.clientPanelInitInterface=_clientPanelInitInterface;
    }
}
/*******************************************************************************
 * $RCSfile: DeskTopPanel.java,v $ $Revision: 1.27.2.21 $ $Date: 2010/01/14 08:14:25 $
 *
 * $Log: DeskTopPanel.java,v $
 * Revision 1.27.2.21  2010/01/14 08:14:25  wangqi
 * *** empty log message ***
 *
 * Revision 1.27.2.20  2010/01/13 03:10:17  wangqi
 * *** empty log message ***
 *
 * Revision 1.27.2.19  2009/12/15 04:56:00  wangqi
 * *** empty log message ***
 *
 * Revision 1.27.2.18  2009/10/16 03:49:30  wangqi
 * *** empty log message ***
 *
 * Revision 1.27.2.17  2009/09/09 02:32:37  wangqi
 * *** empty log message ***
 *
 * Revision 1.27.2.16  2009/09/03 07:15:09  wangqi
 * *** empty log message ***
 *
 * Revision 1.27.2.15  2009/08/14 09:23:53  wangqi
 * *** empty log message ***
 *
 * Revision 1.27.2.14  2009/03/16 07:24:46  wangqi
 * *** empty log message ***
 *
 * Revision 1.27.2.13  2008/12/02 09:31:43  wangqi
 * *** empty log message ***
 *
 * Revision 1.27.2.12  2008/11/05 08:16:26  wangqi
 * *** empty log message ***
 *
 * Revision 1.27.2.11  2008/10/29 09:31:29  wangqi
 * *** empty log message ***
 *
 * Revision 1.42  2008/10/15 08:26:59  wangqi
 * *** empty log message ***
 *
 * Revision 1.41  2008/10/15 07:25:34  wangqi
 * *** empty log message ***
 *
 * Revision 1.40  2008/09/19 02:01:18  wangqi
 * *** empty log message ***
 *
 * Revision 1.39  2008/09/10 09:37:42  wangqi
 * *** empty log message ***
 *
 * Revision 1.38  2008/09/09 08:04:02  wangqi
 * patch   : 20080909
 * file    : nova_20080704_20080908.jar,WebRoot_20080704_20080909.zip
 * content : MR nova20-50，配置见Nova2Desktop.xml
 *
 * Revision 1.1  2008/09/01 07:53:52  wangqi
 * *** empty log message ***
 *
 * Revision 1.37  2008/08/28 02:33:40  wangqi
 * *** empty log message ***
 *
 * Revision 1.36  2008/07/11 08:22:11  wangqi
 * *** empty log message ***
 *
 * Revision 1.35  2008/07/11 06:22:32  wangqi
 * *** empty log message ***
 *
 * Revision 1.34  2008/06/02 03:27:52  wangqi
 * *** empty log message ***
 *
 * Revision 1.33  2008/05/23 01:26:41  wangqi
 * *** empty log message ***
 *
 * Revision 1.32  2008/05/21 09:00:38  wangqi
 * 为动态配置登录逻辑而作的修改
 *
 * Revision 1.31  2008/05/19 05:41:05  wangqi
 *  获得deskTopVO配置信息
 *  1、根据客户端环境变量中的配置中获得DeskTopVO的服务配置和参数配置
 *  2、获得参数并调用，获得DeskTopVO
 *  return DeskTopVO
 *  private DeskTopVO getDeskTopVO()
 *
 * >>>还没有真正修改，仅仅是写了一下说明
 *
 *
 ******************************************************************************/
