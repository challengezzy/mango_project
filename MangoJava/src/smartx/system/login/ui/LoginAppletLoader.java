/**************************************************************************
 * $RCSfile: LoginAppletLoader.java,v $  $Revision: 1.18.2.25 $  $Date: 2010/02/03 09:22:09 $
 **************************************************************************/
package smartx.system.login.ui;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;
import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;

import smartx.framework.common.ui.*;
import smartx.framework.common.utils.Sys;
import smartx.framework.common.vo.NovaLogger;
import smartx.framework.common.vo.NovaRemoteException;
import smartx.framework.metadata.lookandfeel.LookAndFeelLocalSetting;
import smartx.framework.metadata.ui.NovaMessage;
import smartx.system.common.constant.CommonSysConst;
import smartx.system.login.vo.LoginInfoVO;


/**
 * 加载器
 *
 * @author Administrator
 *
 */
public class LoginAppletLoader implements INovaAppletLoader, Serializable {

    private static final long serialVersionUID = 1L;
    protected  JApplet applet;
    protected LoginPanel contentPanel =null;
    protected  JPanel mainPanel;
    protected  String fileBase;
    protected  String servletURL;
    protected  String hostname;
    protected  String hostport;
    protected  String str_user;
    protected  String str_pwd;
    protected  String str_adpwd;
    protected  String autologin;

    /**
     * 启动应用
     * 
     */
    public void loadApplet(JApplet _applet, JPanel _mainPanel, String _fileBase, String _servletURL) {
    	//对输入框进行控制
		System.setProperty("java.awt.im.style","on-the-spot");
    	
		applet = _applet;
		Sys.putInfo("applet", applet);//设置客户端系统applet对象，以备全局访问
		
		
		
		
		
		NovaClientEnvironment.newInstance();
        
		String calltype = _applet.getParameter("CALLTYPE");
        if(calltype==null||calltype.equalsIgnoreCase("null")){
        	calltype = _applet.getParameter("calltype");
        	if(calltype == null||calltype.equalsIgnoreCase("null")) {
	        	calltype = _applet.getParameter("STR_ADMIN");
	            if (calltype == null||calltype.equalsIgnoreCase("null")) {
	            	calltype = _applet.getParameter("admin");
	                if(calltype == null||calltype.equalsIgnoreCase("null")) {
	                	calltype="unknown";
	                }
	            }
        	}
        }
        NovaClientEnvironment.getInstance().put("CALLTYPE", "远程登录类型", calltype); //
        
        /**
         * 获得本地数据缓冲定义目录
         */
        String localCacheDataPath=System.getProperty("ClientDataCache");
        if(localCacheDataPath==null){
        	localCacheDataPath=System.getProperty("ClientCodeCache")+"_localdata/";
        }
        File file=new File(localCacheDataPath);
		if(!file.exists()){
			file.mkdirs();
		}
        Sys.putInfo("CLIENT_DATACACHE", localCacheDataPath);
        
        //风格设置
    	setLookAndFeel();
        
        
        /**
         * 获得桌面定义类型
         */
        String deskmode = _applet.getParameter("deskmode");
        if(deskmode==null||deskmode.equalsIgnoreCase("null")){deskmode="";}
        NovaClientEnvironment.getInstance().put("deskmode", "桌面类型", deskmode); //
		
        

        mainPanel = _mainPanel;
        fileBase = _fileBase;
        servletURL = _servletURL;

        if (_applet.getParameter("url") != null) { //开发模式叫url
            System.out.println("取得到url变量:" + _applet.getParameter("url"));
            System.setProperty("WebURL", _applet.getParameter("url")); //
            NovaClientEnvironment.getInstance().put("WebURL", "远程WEB URL", _applet.getParameter("url")); //
        } else { //运行模板由三个组成,以后也可以考虑有个叫"url"的
            String str_weburl = "http://" + _applet.getParameter("SERVER_HOST_NAME") + ":" +
                _applet.getParameter("SERVER_PORT") + _applet.getParameter("APP_CONTEXT"); //
            System.setProperty("WebURL", str_weburl); //
            NovaClientEnvironment.getInstance().put("WebURL", "远程WEB URL", str_weburl); //
        }
        
        //System.out.println("登录参数信息："+Arrays.deepToString(new String[]{"CALLTYPE","pnames","pvalue","WebURL"}));
        //System.out.println("登录参数信息："+Arrays.deepToString(new String[]{calltype,pnames,pvalues,(String)NovaClientEnvironment.getInstance().get("WebURL")}));
        
        initParameters();
        
        //直接调用就可以了，客户端可以自动初始化
//      new Runnable(){
//      	public void run(){
      	NovaLogger.getLogger(this).debug("日志初始化完成！");
//      	}
//      }.run();
        
        /**
         * 判断系统登录方式
         *   如果第三方登录控制，则获取登录用户名（USERNAME）即可
         *   如果本地登录控制，则走正常流程
         */
        String loginType=null;
        try{
        	loginType=UIUtil.getEnvironmentParamStr("LOGIN_TYPE");
        }catch(Exception e){
        	loginType=NovaConstants.SYS_LOGIN_LOCAL;
        }
        
        //执行登录
    	doLogin(loginType);
    }
    
    public void doLogin(String loginType){
    	str_user=applet.getParameter("user");
        str_pwd=applet.getParameter("pwd");
        str_adpwd=applet.getParameter("adpwd");
        autologin=applet.getParameter("AUTOLOGIN");
        autologin=("true".equalsIgnoreCase(autologin))?"true":"false";
    	if(NovaConstants.SYS_LOGIN_3TH.equals(loginType)){
        	if(str_user==null||str_user.trim().equals("")){
        		NovaMessage.show(applet, "登录失败,原因:登录用户【"+str_user+"】不合法！");
        		return ;
        	}
        	new NovaSplashWindow(applet, new AbstractAction() {
    			private static final long serialVersionUID = -8004126324740194199L;
				public void actionPerformed(ActionEvent e) {
    				dealLogin(doLoginOther()); //
    			}
    		});
        }else{
        	loadLoginPanel();
        }
    }  

    public void loadLoginPanel() {        
    	HashMap map=new HashMap();
    	map.put("user", str_user);
    	map.put("pwd", str_pwd);
    	map.put("adpwd", str_adpwd);
    	
        contentPanel = new LoginPanel(this, map);
        mainPanel.removeAll();
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.updateUI();
        
    }
    
    
    
	
	
	
	/**
	 * 本地自管理登录控制
	 * @return
	 */
	private LoginInfoVO doLoginOther(){
		SystemLoginServiceIFC loginService = null;
		LoginInfoVO loginInfo = null;
		try {
			loginService = (SystemLoginServiceIFC) NovaRemoteServiceFactory.getInstance().lookUpService(SystemLoginServiceIFC.class);
		} catch (java.lang.ClassNotFoundException ex) {
			NovaMessage.show(applet, "登录失败,没有找到登录逻辑处理类:[" + ex.getMessage() + "]");
			return null;
		} catch (Exception e) {
			NovaMessage.show(applet, "登录失败,原因:" + e.getMessage());
			return null;
		}

		try {
			//这里设置的环境变量是为了server端写日志用
			NovaClientEnvironment.getInstance().put("SYS_LOGINUSER_LOGINNAME", "登录用户", str_user);
			NovaClientEnvironment.getInstance().put("SYS_LOGINUSER_PWD", "用户密码", str_pwd);
			NovaClientEnvironment.getInstance().put("SYS_LOGINUSER_ADPWD", "用户管理员密码", str_adpwd);
			try {
				NovaClientEnvironment.getInstance().put("CLIENTIP", InetAddress.getLocalHost().getHostAddress());
			} catch (UnknownHostException ex) {
			}

			loginInfo = loginService.getUserInfoByLogin(str_user); //普通登录
			return loginInfo;
		} catch (Exception ex1) {
			NovaMessage.show(applet, ex1.getMessage());
			return null;
		}
	}
	
	
    public void dealLogin(LoginInfoVO loginInfo) {
    	initEnv();
    	
		if (loginInfo == null) {
			NovaMessage.show(applet, "远程访问发生异常,请至控制台查看原因!");
			return;
		}

		if (loginInfo.getLoginStatus() == SystemLoginServiceIFC.USER_ERROR_TYPE) { //普通用户登录失败
			NovaMessage.show(applet, "用户名或密码错误!", NovaConstants.MESSAGE_ERROR);
			if(contentPanel!=null) contentPanel.setFocus(1);
			return;
		} else if (loginInfo.getLoginStatus() == SystemLoginServiceIFC.ADMINUSER_ERROR_TYPE) { //管理员登录失败!
			NovaMessage.show(applet, "用户名或密码错误!", NovaConstants.MESSAGE_ERROR);
			if(contentPanel!=null) contentPanel.setFocus(1);
			return;
		} else if (loginInfo.getLoginStatus() == SystemLoginServiceIFC.USER_ACCOUTSTATUS_DISABLED) {
			NovaMessage.show(applet, "该用户已停用!", NovaConstants.MESSAGE_ERROR);
			if(contentPanel!=null) contentPanel.setFocus(1);
			return;
		} else if (loginInfo.getLoginStatus() == SystemLoginServiceIFC.USER_ACCOUT_EXPDATE) {
			NovaMessage.show(applet, "该用户已过期!", NovaConstants.MESSAGE_ERROR);
			if(contentPanel!=null) contentPanel.setFocus(1);
			return;
		} else if (loginInfo.getLoginStatus() == SystemLoginServiceIFC.USER_PWD_EXPDATE) {
			NovaMessage.show(applet, "该用户密码已过期!", NovaConstants.MESSAGE_ERROR);
			if(contentPanel!=null) contentPanel.setFocus(1);
			return;
		}
			
			
		Vector secondProjectClientInit = null; //附加客户端初始化
		Vector secondProjectClientAfterLogin = null; //附加客户端登录后处理
		Vector secondProjectClientPanelInit = null; //附加客户端初始化
		try {
			//TODO 不能在此设定硬编码，应该是给定访问接口直接获得服务器端参数
	    	//     而这个接口不应该简单的返回，而应该通过其他方式返回。
	    	//     例如远程传递序列化数据，而使用的时候再反序列化
			HashMap enParam = UIUtil.getEnvironmentParam();
			secondProjectClientInit = (Vector) enParam.get(CommonSysConst.CLIENTINIT);
			secondProjectClientAfterLogin = (Vector) enParam.get(CommonSysConst.CLIENTAFTERLOGIN);
			secondProjectClientPanelInit = (Vector) enParam.get(CommonSysConst.CLIENTPANELINIT);

			//System.out.println("项目名称[" + str_projectname + "],默认数据源[" + str_defaultdatasource + "]");

			String str_projectname = UIUtil.getEnvironmentParamStr("PROJECT_NAME");			
			if (str_projectname != null) {
				//TODO 所有UI里设置的System.setProperty对其他系统没有影响，但还是应该避免的。
				//     除非是像这种全局性的属性。
				System.setProperty("PROJECT_NAME", str_projectname); 
				NovaClientEnvironment.getInstance().put("PROJECT_NAME", "项目名称", str_projectname);
			}
			String copyrightinfo =UIUtil.getEnvironmentParamStr("COPYRIGHT_INFO");				
			if (copyrightinfo != null) {
				System.setProperty("COPYRIGHT_INFO", copyrightinfo); //
				NovaClientEnvironment.getInstance().put("COPYRIGHT_INFO", "版权声明", copyrightinfo);
			}
			String str_defaultdatasource = (String) enParam.get("defaultdatasource");			
			if (str_defaultdatasource != null) {
				System.setProperty("defaultdatasource", str_defaultdatasource); //
				NovaClientEnvironment.getInstance().put("defaultdatasource", "默认数据源", str_defaultdatasource);
			}
			String str_appmodule = (String) enParam.get("APPMODULE");
			if (str_appmodule != null) {
				NovaClientEnvironment.getInstance().put("APPMODULE", "模块名称", str_appmodule);
				NovaClientEnvironment.getInstance().setAppModuleName(str_appmodule);
			}

		} catch (Exception e1) {
			NovaMessage.show(applet, e1.getMessage());
			return;
		}	
		
		boolean isAdmin=loginInfo.isAdmin();
		if (!isAdmin) {  //普通用户登录成功!!!
			NovaClientEnvironment.getInstance().put("SYS_LOGINUSER_TYPE", "是否以管理员身份登录", "N");
			NovaClientEnvironment.getInstance().put("SYS_LOGINUSER_REGIONCODE", "登录用户所属区域", loginInfo.getRegionCode());

			putEnvironmentParam(loginInfo);
			initSecondProjectClient(secondProjectClientInit);


			//加入登录完成后的处理逻辑<nva2config>内
			initSecondProjectClientAfterLogin(secondProjectClientAfterLogin);
			//展示主界面
			DeskTopPanel deskTopPanel = loadDeskTopPanel(loginInfo.getId()); 


			initSecondProjectClientPanelInit(secondProjectClientPanelInit, deskTopPanel);
		} else { //管理员登录成功!!!
			NovaClientEnvironment.getInstance().put("SYS_LOGINUSER_TYPE", "是否以管理员身份登录", "Y");

			putEnvironmentParam(loginInfo);
			initSecondProjectClient(secondProjectClientInit);

			//加入登录完成后的处理逻辑<nva2config>内
			initSecondProjectClientAfterLogin(secondProjectClientAfterLogin);
			
			//展示主界面
			DeskTopPanel deskTopPanel = loadDeskTopPanel(loginInfo.getId()); // 进入主界面!!!!!!!!!!!!!!!!!!!


			initSecondProjectClientPanelInit(secondProjectClientPanelInit, deskTopPanel);
		}
	}

	//2
	private void initSecondProjectClient(Vector v) {
		if (v != null) {
			for (int i = 0; i < v.size(); i++) {
				String className = (String) v.get(i);
				try {
					if (className != null && !className.equals("")) {
						Class.forName(className).newInstance();
					}
				} catch (Exception ex) {
					NovaLogger.getLogger(this).error("客户端附加初始化业务执行错误！",ex);
				}
			}
		}
	}
	//3
	private void initSecondProjectClientAfterLogin(Vector v) {
		if (v != null) {
			for (int i = 0; i < v.size(); i++) {
				String className = (String) v.get(i);
				try {
					if (className != null && !className.equals("")) {
						ClientPanelAfterLoginInterface client = (ClientPanelAfterLoginInterface) Class.forName(className).newInstance();
						client.init();
					}
				} catch (Exception ex) {
					NovaLogger.getLogger(this).error("客户端附加登录后业务【ClientPanelAfterLoginInterface】执行错误！",ex);					
				}
			}
		}
	}
	//4
	private void initSecondProjectClientPanelInit(Vector v, DeskTopPanel deskTopPanel) {
        //此处修改为只加载第一个配置类，由于需要将其句柄传给DeskTopPanel,而且一个项目一个初始化类应该够用了
		if (v != null && v.size()>0) {
            String className = (String) v.get(0);
            try {
                if (className != null && !className.equals("")) {
                    ClientPanelInitInterface client = (ClientPanelInitInterface) Class.forName(className).newInstance();
                    client.setDeskTopPanel(deskTopPanel);
                    client.init();
                    deskTopPanel.setClientPanelInitInterface(client);
                }
            } catch (Exception ex) {
                NovaLogger.getLogger(this).error("客户端桌面附加业务【ClientPanelInitInterface】执行错误！",ex);
            }
		}
	}
    


	private void putEnvironmentParam(LoginInfoVO loginInfo) {
		NovaClientEnvironment.getInstance().put("SYS_LOGINUSER_ID", "登录用户ID", loginInfo.getId());
		NovaClientEnvironment.getInstance().put("SYS_LOGINUSER_CODE", "登录用户CODE", loginInfo.getCode());
		NovaClientEnvironment.getInstance().put("SYS_LOGINUSER_LOGINNAME", "登录用户", loginInfo.getLoginName());
		NovaClientEnvironment.getInstance().put("SYS_LOGINUSER_NAME", "登录用户名称", loginInfo.getName());
		NovaClientEnvironment.getInstance().put("SYS_LOGIN_TIME", "登录时间", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(loginInfo.getLoginTime()));
		NovaClientEnvironment.getInstance().put("USER_LOGINCOUNT", new Long(loginInfo.getUserLoginCount()));
		NovaClientEnvironment.getInstance().put("WORKPOSITION", loginInfo.getWorkPositionID());
		NovaClientEnvironment.getInstance().put("SYS_LOGINUSER_TYPE", String.valueOf(loginInfo.isAdmin()));//
		

		HashMap userParam = loginInfo.getUserParam();
		Iterator it = userParam.keySet().iterator();
		if (it != null) {
			while (it.hasNext()) {
				Object key = it.next();
				NovaClientEnvironment.getInstance().put(key, userParam.get(key));
			}
		}
	}
    
	private void initEnv(){
		Properties prop_jndi = new Properties();
		try {
			prop_jndi.load(this.getClass().getResourceAsStream("/jndi.properties")); //
			System.getProperties().putAll(prop_jndi); //
			System.out.println("java.naming.factory.initial=" + System.getProperty("java.naming.factory.initial")); //
			System.out.println("java.naming.factory.url.pkgs=" + System.getProperty("java.naming.factory.url.pkgs")); //
			System.out.println("java.naming.provider.url=" + System.getProperty("java.naming.provider.url")); //
		} catch (Exception ex) {
			//ex.printStackTrace();  //如果没有jndi.properties配置文件,则吃掉异常,没必要输出.
		}
	}
	

    private DeskTopPanel loadDeskTopPanel(String _username) {
        //long ll_1 = System.currentTimeMillis();
        mainPanel.removeAll();
        DeskTopPanel deskTopPanel = new DeskTopPanel(this, _username); //
        mainPanel.add(deskTopPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.updateUI();
        //long ll_2 = System.currentTimeMillis();
        return deskTopPanel;
        //System.out.println("加载主界面耗时[" + (ll_2 - ll_1) + "]");
    }

    public JApplet getApplet() {
        return applet;
    }

    public void setApplet(JApplet applet) {
        this.applet = applet;
    }

    public String getFileBase() {
        return fileBase;
    }

    public void setFileBase(String fileBase) {
        this.fileBase = fileBase;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public String getServletURL() {
        return servletURL;
    }

    public void setServletURL(String servletURL) {
        this.servletURL = servletURL;
    }

    public String getHostName() {
        if (hostname == null) {
            initServerURL();
        }
        return hostname;

    }

    public String getHostPort() {
        if (hostport == null) {
            initServerURL();
        }
        return hostport;
    }

    private void initServerURL() {
        String url = System.getProperty("RemoteCallServletURL");
        url = url.replaceAll("//", "/");
        String[] host = url.split("/");
        hostname = host[1].substring(0, host[1].indexOf(":"));
        hostport = host[1].substring(host[1].indexOf(":") + 1);
    }
    
    /**
     * 初始化系统参数
     * 
     */
    private void initParameters(){
    	String setting= null;
    	try{
    		setting=UIUtil.getEnvironmentParamStr("CLIENT_PARAMETERS");  
    		System.out.println("客户端缓冲参数表【CLIENT_PARAMETERS】："+setting);
    		
    		String[] ps=setting.split(",");
    		for(int i=0;i<ps.length;i++){
    			String tmp=UIUtil.getEnvironmentParamStr(ps[i]);
    			System.out.println(ps[i]+"："+tmp);
    			Sys.putInfo(ps[i], tmp);
    		}    		
		}catch(NovaRemoteException e){
		      //与远程Server联系出错
		      System.out.println("获得服务器端信息时发生远程错误："+e.getMessage());
		      //e.printStackTrace();		      
		}catch(Exception e){
		      System.out.println("获得服务器端信息时发生其它错误："+e.getMessage());
		      //e.printStackTrace();		      
		}
		
    }
    
    //设置外观
	private void setLookAndFeel(){
		LookAndFeelLocalSetting lfSetting= new LookAndFeelLocalSetting();
		String curLF = lfSetting.getLookAndFeel();
		if(curLF==null){
			setDefaultLookAndFeel();
		}else{
			lfSetting.setLookAndFeel(curLF);
		}
		
	}
    
	//设置外观
	private void setDefaultLookAndFeel(){
		try {
			//系统默认风格 
			UIManager.setLookAndFeel("smartx.framework.common.ui.NovaLookAndFeel");
			
			//以下为共用设置
			UIManager.put("swing.boldMetal", Boolean.valueOf(false));
    		//调用更新，可以在任意的位置更新
    		SwingUtilities.updateComponentTreeUI(applet.getContentPane());    		
    		
	    } catch (Exception e) {
	        System.err.println("Oops!  Something is wrong!");
	    }
	    
	    /*****************************************************/
		
	}
	
	//设置外观
	private void setLookAndFeel_old(){
		
		/*
		 * 第一种方案，通过直接参数设置风格。
		 * 缺陷，直接把风格名称设置进来，有时候某些风格还需要做一些设置，例如substancelaf，见后面第二种方案，这样的方法不太适合。
		 */
		/*
		String lookandfeel=applet.getParameter("LOOK_AND_FEEL");
		if(lookandfeel==null){
			lookandfeel="smartx.framework.common.ui.NovaLookAndFeel";
		}
				
		try {				
			UIManager.setLookAndFeel(lookandfeel);			
			SwingUtilities.updateComponentTreeUI(applet.getContentPane());
		} catch (Exception ex) {
			//ex.printStackTrace();
		}
		*/
		
		/*
		 * 第二种方案，允许有一些专门设置的定制的东西
		 * 1、把各种优秀的方案包装一下，按照统一接口：
		 *   NovaLookAndFeelIFC{
		 *       //获得可用风格列表返回名称和说明的列表
		 *       list<name,describ> getList();
		 *       //通过名称给容器设置风格
		 *       void setLookAndFeel(name,Container);
		 *   }
		 * 2、把各种封装的方案在后台DeskTop配置文件中设置，
		 * 3、DeskTop读入后台设置后由用户自行设置自己的风格，
		 *    工具栏上面放个风格设置按钮。
		 *    系统保存选择的风格name到缓冲目录名为skin.set
		 *    如C:\Documents and Settings\James.W\NOVA_CODECACHE\localhost_8888_nova\skin.set
		 *    
		 */
		
		try {
			//系统默认风格 
			UIManager.setLookAndFeel("smartx.framework.common.ui.NovaLookAndFeel");
			
			//风格 substance 
            /*
            UIManager.setLookAndFeel(new SubstanceLookAndFeel());
            UIManager.put("swing.boldMetal", Boolean.valueOf(false));
            if (System.getProperty("substancelaf.useDecorations") == null) {
                    JFrame.setDefaultLookAndFeelDecorated(true);
                    JDialog.setDefaultLookAndFeelDecorated(true);
            }
            System.setProperty("sun.awt.noerasebackground", "true");
            //设置当前的主题风格，同样我 们还可以设置当前的按钮形状，水印风格等等 
            SubstanceLookAndFeel.setCurrentTheme(new SubstanceLightAquaTheme());
            
            
			//下面的风格应该首先使用org.jvnet.substance.SubstanceLookAndFeel，然后再使用其他风格
    		UIManager.setLookAndFeel("org.jvnet.substance.SubstanceLookAndFeel"); 
    	    //   另外substance还提供了下面的控制，建议启用
    		if (System.getProperty("substancelaf.useDecorations") == null) {
                JFrame.setDefaultLookAndFeelDecorated(true);
                JDialog.setDefaultLookAndFeelDecorated(true);
            }
    		//UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceBusinessLookAndFeel");
			//UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceBusinessBlueSteelLookAndFeel");
			UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceBusinessBlackSteelLookAndFeel"); 
			//UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceCremeLookAndFeel"); 
			//UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceSaharaLookAndFeel"); 
			//UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceModerateLookAndFeel");
			//UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceOfficeSilver2007LookAndFeel"); 
			//UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceRavenGraphiteLookAndFeel"); 
            //UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceMagmaLookAndFeel"); 
			//UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceOfficeBlue2007LookAndFeel");
			//UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceMangoLookAndFeel");
			//UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceFieldOfWheatLookAndFeel");
    		
    		// */
    		
    		
    		//风格 officeLnFs 可以独立使用
    		//UIManager.setLookAndFeel("org.fife.plaf.OfficeXP.OfficeXPLookAndFeel");
    		//UIManager.setLookAndFeel("org.fife.plaf.Office2003.Office2003LookAndFeel");
            //UIManager.setLookAndFeel("org.fife.plaf.VisualStudio2005.VisualStudio2005LookAndFeel");
    		
			//风格 Quaqua
			//System.setProperty( "Quaqua.tabLayoutPolicy","wrap");
			//UIManager.setLookAndFeel("ch.randelshofer.quaqua.QuaquaLookAndFeel");    
			
			
			
			//以下为共用设置
			UIManager.put("swing.boldMetal", Boolean.valueOf(false));
    		//调用更新，可以在任意的位置更新
    		SwingUtilities.updateComponentTreeUI(applet.getContentPane());    		
    		
	    } catch (Exception e) {
	        System.err.println("Oops!  Something is wrong!");
	    }
	    
	    /*****************************************************/
		
	}
}
/**************************************************************************
 * $RCSfile: LoginAppletLoader.java,v $  $Revision: 1.18.2.25 $  $Date: 2010/02/03 09:22:09 $
 *
 * $Log: LoginAppletLoader.java,v $
 * Revision 1.18.2.25  2010/02/03 09:22:09  wangqi
 * *** empty log message ***
 *
 * Revision 1.18.2.24  2010/01/13 02:26:28  wangqi
 * *** empty log message ***
 *
 * Revision 1.18.2.23  2009/10/21 06:25:46  wangqi
 * *** empty log message ***
 *
 * Revision 1.18.2.22  2009/10/16 03:49:30  wangqi
 * *** empty log message ***
 *
 * Revision 1.18.2.21  2009/09/03 07:25:05  wangqi
 * *** empty log message ***
 *
 * Revision 1.18.2.20  2009/08/14 09:23:53  wangqi
 * *** empty log message ***
 *
 * Revision 1.18.2.18  2009/08/13 01:58:31  wangqi
 * *** empty log message ***
 *
 * Revision 1.18.2.17  2009/05/22 02:39:18  wangqi
 * *** empty log message ***
 *
 * Revision 1.18.2.16  2009/05/18 08:12:09  wangqi
 * *** empty log message ***
 *
 * Revision 1.18.2.15  2009/05/12 04:04:37  wangqi
 * *** empty log message ***
 *
 * Revision 1.18.2.14  2009/03/16 07:24:46  wangqi
 * *** empty log message ***
 *
 * Revision 1.18.2.13  2009/02/24 06:21:34  wangqi
 * *** empty log message ***
 *
 * Revision 1.18.2.12  2008/12/16 06:29:15  wangqi
 * *** empty log message ***
 *
 * Revision 1.18.2.11  2008/11/14 05:59:56  wangqi
 * *** empty log message ***
 *
 * Revision 1.18.2.10  2008/11/13 06:41:51  wangqi
 * *** empty log message ***
 *
 * Revision 1.18.2.9  2008/10/29 09:31:28  wangqi
 * *** empty log message ***
 *
 * Revision 1.18.2.8  2008/10/14 18:16:19  wangqi
 * *** empty log message ***
 *
 * Revision 1.18.2.7  2008/09/03 03:28:28  wangqi
 * *** empty log message ***
 *
 * Revision 1.18.2.6  2008/09/01 02:30:26  wangqi
 * *** empty log message ***
 *
 * Revision 1.18.2.5  2008/08/25 09:17:10  wangqi
 * *** empty log message ***
 *
 * Revision 1.18.2.4  2008/06/18 08:09:02  wangqi
 * *** empty log message ***
 *
 * Revision 1.18.2.3  2008/03/19 07:20:26  wangqi
 * *** empty log message ***
 *
 * Revision 1.18.2.2  2008/03/10 07:25:45  wangqi
 * *** empty log message ***
 *
 * Revision 1.18.2.1  2008/02/28 09:14:05  wangqi
 * *** empty log message ***
 *
 * Revision 1.18  2008/01/02 14:52:37  wangqi
 * *** empty log message ***
 *
 * Revision 1.17  2008/01/02 14:52:23  wangqi
 * *** empty log message ***
 *
 * Revision 1.16  2008/01/02 14:52:06  wangqi
 * *** empty log message ***
 *
 * Revision 1.15  2007/10/15 02:46:50  wangqi
 * *** empty log message ***
 *
 * Revision 1.14  2007/10/09 05:42:12  wangqi
 * *** empty log message ***
 *
 * Revision 1.13  2007/10/09 05:35:06  wangqi
 * *** empty log message ***
 *
 * Revision 1.12  2007/10/09 02:50:21  wangqi
 * *** empty log message ***
 *
 * Revision 1.11  2007/10/09 01:34:24  wangqi
 * *** empty log message ***
 *
 * Revision 1.10  2007/10/08 10:58:33  wangqi
 * *** empty log message ***
 *
 * Revision 1.9  2007/10/08 07:20:15  wangqi
 * *** empty log message ***
 *
 * Revision 1.8  2007/09/19 08:44:19  wangqi
 * *** empty log message ***
 *
 * Revision 1.7  2007/08/23 09:43:26  wangqi
 * 增加一个环境判断，调试的时候不更新jar包
 *
 * Revision 1.6  2007/08/15 11:14:14  wangqi
 * *** empty log message ***
 *
 * Revision 1.5  2007/07/30 06:45:21  qilin
 * no message
 *
 * Revision 1.4  2007/07/18 06:29:13  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/07/11 03:09:15  qilin
 * 为当前系统启动另一个nova系统所作的修改
 *
 * Revision 1.2  2007/05/31 07:41:32  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:22:08  qilin
 * no message
 *
 * Revision 1.8  2007/03/22 02:47:24  shxch
 * *** empty log message ***
 *
 * Revision 1.7  2007/03/05 06:34:34  shxch
 * *** empty log message ***
 *
 * Revision 1.6  2007/02/27 06:02:59  shxch
 * *** empty log message ***
 *
 * Revision 1.5  2007/02/02 08:55:54  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/02/02 08:41:42  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/02/01 07:08:40  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:20:39  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
