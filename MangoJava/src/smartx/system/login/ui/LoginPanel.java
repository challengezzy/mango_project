/**************************************************************************
 * $RCSfile: LoginPanel.java,v $  $Revision: 1.23.2.8 $  $Date: 2009/12/16 04:14:08 $
 **************************************************************************/
package smartx.system.login.ui;

import java.text.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import org.apache.log4j.Logger;
import org.jdom.Element;

import smartx.framework.common.ui.*;
import smartx.framework.common.vo.NovaLogger;
import smartx.framework.metadata.ui.*;
import smartx.system.common.constant.CommonSysConst;
import smartx.system.login.vo.*;


import java.net.*;

/**
 * 登录面板
 * @author xch
 *
 */
public class LoginPanel extends JPanel {
	private static final long serialVersionUID = -4300687713407623152L;
	
	protected LoginAppletLoader loader = null;
	protected String calltype=null;
	protected ArrayList _ctrlList=new ArrayList();//可见控件列表（包括背景和控件）
	protected HashMap _ctrlMap=new HashMap();//编辑控件引用(key)
	protected HashMap initParam=null;//初始化参数值map
	protected HashMap _editMap=new HashMap();//编辑控件引用(key)
	protected ArrayList _editList=new ArrayList();//编辑控件引用列表(id)
	
	//键盘按键响应处理
	protected KeyAdapter keyadapter = new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
			dealKeyPressed(e);
		}

		public void keyTyped(KeyEvent e) {
			dealKeyTyped(e);
		}
	};
	
	//焦点响应处理
	protected FocusAdapter focusAdapter=new FocusAdapter() {
		public void focusGained(FocusEvent evt) {
			Object obj=evt.getSource();
			if(obj instanceof JTextField){
				((JTextField)obj).selectAll();
			}
		}
	};
	
	//按钮点击响应处理
	protected ActionListener doAction=new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			Object src=e.getSource();
			String id=((JButton)src).getName();
			ViewControl vc=(ViewControl)_ctrlMap.get(id);
			String action=vc.getAction();
			if(action.equals("onLogin")){
				onLogin();
			}else if(action.equals("onReset")){
				onReset();
			}else if(action.equals("onExit")){
				onExit();
			}else{
				
			}
		}
	};
	
	protected void dealKeyPressed(KeyEvent e) {
		Object obj = e.getSource();
		if(obj instanceof JButton){
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				String id=((JButton)obj).getName();
				ViewControl vc=(ViewControl)_ctrlMap.get(id);
				String action=vc.getAction();
				if(action.equals("onLogin")){
					onLogin();
				}else if(action.equals("onReset")){
					onReset();
				}else if(action.equals("onExit")){
					onExit();
				}
			}
		}else if (obj instanceof JTextField){
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				String id=((JTextField)obj).getName();
				int idx=this._editList.indexOf(id);
				if(idx< this._editList.size()-1){
					setFocus((String)_editList.get(idx+1));					
				}else{
					onLogin();
				}
			}else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				onReset();
			}
		}
	}

	protected void dealKeyTyped(KeyEvent e) {
		Object obj = e.getSource();
		if (obj instanceof JTextField){
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				onLogin();
			}
		}
	}
	

	/**
	 * 构造方法
	 * @param _loader
	 * @param _isadmin
	 */
	public LoginPanel(LoginAppletLoader _loader) {
		this.loader = _loader;
		this.calltype=(String)NovaClientEnvironment.getInstance().get("CALLTYPE");
		
		initPanel();
	}

	/**
	 * 构造方法
	 * @param _loader
	 * @param _isadmin
	 * @param user
	 * @param pwd
	 * @param adpwd
	 */
	public LoginPanel(LoginAppletLoader _loader, HashMap param) {
		this.loader = _loader;
		this.calltype=(String)NovaClientEnvironment.getInstance().get("CALLTYPE");
		this.initParam=param;
		
		initPanel();
		
		onInit();//默认值
	}

	/**
	 * 初始化面板
	 *
	 */
	protected void initPanel() {
		HashMap design=DesktopUtil.getLoginDesign(calltype);
		this.setLayout(new BorderLayout()); 
		
		
		//建立一个多层的面板处理 登录界面
		JLayeredPane layerpanel = new JLayeredPane(); //
        int _defaultLayer=JLayeredPane.DEFAULT_LAYER.intValue();
        
        //background
        HashMap bgmap=(HashMap)design.get("background");
        layerpanel.setBackground(UIUtil.getColor((String)bgmap.get("bg-color")));
        
        //ctrl-list
        ArrayList lst=(ArrayList)design.get("ctrl-list");
        for(int i=0;i<lst.size();i++){
        	HashMap cmap=(HashMap)lst.get(i);
        	ViewControl vc=new ViewControl(cmap);
        	
        	
        	String id=vc.getId();
        	String type=vc.getType();        	
        	if(type.equals("img")){
        		layerpanel.add(vc.getComponent(), new Integer(_defaultLayer++));
        		_ctrlMap.put(id, vc);
        		_ctrlList.add(id);
        	}else if(type.equals("label")){
        		layerpanel.add(vc.getComponent(), new Integer(_defaultLayer++));
        		_ctrlMap.put(id, vc);
        		_ctrlList.add(id);        		
        	}else if(type.equals("text")){
        		layerpanel.add(vc.getComponent(), new Integer(_defaultLayer++));
        		_ctrlMap.put(id, vc);
        		_ctrlList.add(id);
        		_editMap.put(vc.getKey(), vc);
        		_editList.add(id);
        	}else if(type.equals("password")){
        		layerpanel.add(vc.getComponent(), new Integer(_defaultLayer++));
        		_ctrlMap.put(id, vc);
        		_ctrlList.add(id);
        		_editMap.put(vc.getKey(), vc);
        		_editList.add(id);
        	}else if(type.equals("button")){
        		layerpanel.add(vc.getComponent(), new Integer(_defaultLayer++));
        		_ctrlMap.put(id, vc);
        		_ctrlList.add(id);        		
        	}else{
        		;
        	}
        }

		//增加控件的resize事件
		layerpanel.addComponentListener(new ComponentAdapter(){
			public void componentResized(ComponentEvent e) {
				doResize();
			}
		});
		
		this.add(layerpanel, BorderLayout.CENTER);

		setFocus(1);
	}
	
	/**
	 * 界面大小发生变化后的处理
	 */
	protected void doResize(){
		int appletWidth = loader.getApplet().getWidth();
		int appletHeight = loader.getApplet().getHeight();
		if (appletWidth <= 0) { appletWidth = 1000; }
		if (appletHeight <= 0) { appletHeight = 700; }
		
		for(int i=0;i<_ctrlList.size();i++){
			String id=(String)_ctrlList.get(i);
			moveCtrl((ViewControl)_ctrlMap.get(id), appletWidth, appletHeight);			
		}
			
	}
	
	
	private void moveCtrl(ViewControl vc, int pwidth, int pheight){
		Component comp=vc.getComponent();
		if(vc.getWidth()==-1){
			//大小忽略，直接按照容器的大小
			vc.setLeft(0);
			vc.setTop(0);
			comp.setBounds(0, 0, pwidth, pheight);
			return;
		}
		
		int left=-1,top=-1;
		//按照参照计算，参照和比例两者只能取一
		if(vc.getRefCtrl()!=null){
			String ref=vc.getRefCtrl();
			ViewControl refobj=(ViewControl)_ctrlMap.get(ref);
			left=refobj.getLeft();
			top=refobj.getTop();
		}else if(vc.getLeftScale()>=0){
			//按照比例计算
			left=(new Double((pwidth-vc.getWidth())*vc.getLeftScale())).intValue();
			top=(new Double((pheight-vc.getHeight())*vc.getTopScale())).intValue();
		}
		//按照偏移计算
		if(vc.getOffLeft()>=0){
			left=(left==-1?0:left)+vc.getOffLeft();
			top=(top==-1?0:top)+vc.getOffTop();
		}
		//如果前面都没有计算，则按照直接设置位置计算
		if(left==-1&&vc.getLeft()>=0){
			left=vc.getLeft();
			top=vc.getTop();
		}
		//把位置合法化
		left=left==-1?0:left;
		top=top==-1?0:top;
		vc.setLeft(left);
		vc.setTop(top);
		comp.setBounds(left, top, vc.getWidth(), vc.getHeight());		
	}
	
	/**
	 * 设置控件初始值
	 * @param param
	 */
	private void onInit(){
		String[] keys=(String[])initParam.keySet().toArray(new String[0]);
		for(int i=0;i<keys.length;i++){
			String value=(String)initParam.get(keys[i]);
			if(value==null) continue;
			ViewControl vc=(ViewControl)_editMap.get(keys[i]);
			if(vc==null) continue;
			Component comp=vc.getComponent();
			if(vc.getType().equals("password")){
				((JPasswordField)comp).setText(value);
			}else{
				((JTextField)comp).setText(value);
			}
			
		}
	}
	
	
	/**
	 * 获得登录参数
	 * @return
	 */
	public HashMap getParams(){
		HashMap rt=new HashMap();
		String[] keys=(String[])_editMap.keySet().toArray(new String[0]);
		for(int i=0;i<keys.length;i++){
			ViewControl vc=(ViewControl)_editMap.get(keys[i]);
			Component comp=vc.getComponent();
			if(vc.getType().equals("password")){
				rt.put(vc.getKey(), new String(((JPasswordField)comp).getPassword()));
			}else{
				rt.put(vc.getKey(), ((JTextField)comp).getText());
			}
		}
		return rt;
	}
	
	
	
	

	

	public void setFocus(int idx) {
		String id=(String)_editList.get(idx);
		setFocus(id);
	}
	
	public void setFocus(String id) {
		ViewControl vc=(ViewControl)_ctrlMap.get(id); 
		vc.getComponent().requestFocus();
	}

	private void onClean() {
		String[] keys=(String[])_editMap.keySet().toArray(new String[0]);
		for(int i=0;i<keys.length;i++){
			ViewControl vc=(ViewControl)_editMap.get(keys[i]);
			Component comp=vc.getComponent();
			if(vc.getType().equals("password")){
				((JPasswordField)comp).setText("");
			}else{
				((JTextField)comp).setText("");
			}
		}
	}

	private void onLogin(){
		String[] keys=(String[])_editMap.keySet().toArray(new String[0]);
		for(int i=0;i<keys.length;i++){
			ViewControl vc=(ViewControl)_editMap.get(keys[i]);
			if(vc.isEmpty()) continue;//允许空，不用判断了。
			String tmp=null;
			Component comp=vc.getComponent();
			if(vc.getType().equals("password")){
				tmp=new String(((JPasswordField)comp).getPassword());
			}else{
				tmp=((JTextField)comp).getText();
			}
			if(tmp.equals("")){
				NovaMessage.show(this, vc.getEmptyMsg(), NovaConstants.MESSAGE_ERROR);
				return;
			}
		}
		
		dealLogin();
	}
	
	/**
	 *
	 */
	public void dealLogin() {
		new NovaSplashWindow(loader.getApplet(), new AbstractAction() {
			private static final long serialVersionUID = -287905438900197436L;

			public void actionPerformed(ActionEvent e) {
				loader.dealLogin(doLoginLocal()); //
			}
		});
	}
	
	/**
	 * 本地自管理登录控制
	 * @return
	 */
	private LoginInfoVO doLoginLocal(){
		SystemLoginServiceIFC loginService = null;
		LoginInfoVO loginInfo = null;
		try {
			loginService = (SystemLoginServiceIFC) NovaRemoteServiceFactory.getInstance().lookUpService(SystemLoginServiceIFC.class);
		} catch (java.lang.ClassNotFoundException ex) {
			NovaMessage.show(loader.getApplet(), "登录失败,没有找到登录逻辑处理类:[" + ex.getMessage() + "]");
			return null;
		} catch (Exception e) {
			NovaMessage.show(loader.getApplet(), "登录失败,原因:" + e.getMessage());
			return null;
		}

		try {
			HashMap login_params=getParams();
			
			//这里设置的环境变量是为了server端写日志用
			NovaClientEnvironment.getInstance().put("SYS_LOGINUSER_LOGINNAME", "登录用户", String.valueOf(login_params.get("user")));
			NovaClientEnvironment.getInstance().put("SYS_LOGINUSER_PWD", "用户密码", String.valueOf(login_params.get("pwd")));
			NovaClientEnvironment.getInstance().put("SYS_LOGINUSER_ADPWD", "用户管理员密码", String.valueOf(login_params.get("adpwd")));
			try {
				NovaClientEnvironment.getInstance().put("CLIENTIP", InetAddress.getLocalHost().getHostAddress());
			} catch (UnknownHostException ex) {
			}
			loginInfo = loginService.login(login_params);			
			return loginInfo;
		} catch (Exception ex1) {
			NovaMessage.show(loader.getApplet(), ex1.getMessage());
			return null;
		}
	}
	

	private void onReset() {
		onClean();
		onInit();		
		setFocus(1);
	}

	private void onExit() {
		if (JOptionPane.showConfirmDialog(this, "您真的想退出系统吗?", "提示", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}

	
	/**
	 * 界面展现控件
	 * @author Administrator
	 *
	 */
	class ViewControl{
		public ViewControl(HashMap map){
			init(map,null);
		}
		public ViewControl(HashMap map, Component comp){
			init(map,comp);
		}
		private void init(HashMap map, Component comp){
			this.id=(String)map.get("id");
			this.type=(String)map.get("type");
			this.key=(String)map.get("key");
			this.src=(String)map.get("src");
			this.width=Integer.parseInt((String)map.get("width"));
			this.height=Integer.parseInt((String)map.get("height"));
			this.leftscale=map.containsKey("leftscale")?Double.parseDouble((String)map.get("leftscale")):0;
			this.topscale=map.containsKey("topscale")?Double.parseDouble((String)map.get("topscale")):0;
			this.offleft=map.containsKey("offleft")?Integer.parseInt((String)map.get("offleft")):0;
			this.offtop=map.containsKey("offtop")?Integer.parseInt((String)map.get("offtop")):0;
			this.left=map.containsKey("left")?Integer.parseInt((String)map.get("left")):0;
			this.top=map.containsKey("top")?Integer.parseInt((String)map.get("top")):0;
			this.color=(String)map.get("color");
			this.text=(String)map.get("text");
			this.refctrl=(String)map.get("refctrl");
			this.empty=map.containsKey("empty")?("true".equalsIgnoreCase((String)map.get("empty"))):true;
			this.emptymsg=(String)map.get("emptymsg");
			this.action=(String)map.get("action");
			
			if(comp==null){
				buildComponent();
			}else{
				this.comp=comp;
			}
		}
		
		public String getId(){
			return this.id;
		}
		public String getType(){
			return this.type;
		}
		public String getKey(){
			return this.key;
		}
		public String getSrc(){
			return this.src;
		}
		public int getWidth(){
			return this.width;
		}
		public int getHeight(){
			return this.height;
		}
		public int getOffLeft(){
			return this.offleft;
		}
		public int getOffTop(){
			return this.offtop;
		}
		public int getLeft(){
			return this.left;
		}
		public void setLeft(int left){
			this.left=left;
		}
		public int getTop(){
			return this.top;
		}
		public void setTop(int top){
			this.top=top;
		}
		public double getLeftScale(){
			return this.leftscale;
		}
		public double getTopScale(){
			return this.topscale;
		}
		public String getColor(){
			return this.color;
		}
		public String getText(){
			return this.text;
		}
		public String getRefCtrl(){
			return this.refctrl;
		}
		public boolean isEmpty(){
			return this.empty;
		}
		public String getEmptyMsg(){
			return this.emptymsg;
		}
		public String getAction(){
			return this.action;
		}
		
		public Component getComponent(){
			return this.comp;
		}
		private void buildComponent(){
			if(type.equals("img")){
        		JLabel lbl=new JLabel(UIUtil.getImage(src));
        		lbl.setName(id);
        		this.comp=lbl;
        	}else if(type.equals("label")){
        		JLabel lbl=new JLabel(text);
        		lbl.setForeground(UIUtil.getColor(color));
        		lbl.setName(id);
        		this.comp=lbl;        		
        	}else if(type.equals("text")){
        		JTextField tf=new JTextField(text);
        		tf.setName(id);
        		tf.addKeyListener(keyadapter);
        		this.comp=tf;
        	}else if(type.equals("password")){
        		JPasswordField tf=new JPasswordField(text);
        		tf.setName(id);
        		tf.addKeyListener(keyadapter);
        		this.comp=tf;
        	}else if(type.equals("button")){
        		JButton btn=new JButton(text);
        		btn.setName(id);
        		btn.addKeyListener(keyadapter);
        		btn.addActionListener(doAction);
        		this.comp=btn;        		
        	}else{
        		this.comp=null;
        	}
		}
		
		
		private String id=null;
		private String type=null;
		private String key=null;	
		private String src=null;
		private int width=0;
		private int height=0;
		private double leftscale=0;
		private double topscale=0;
		private int offleft=0;
		private int offtop=0;
		private int left=0;
		private int top=0;
		private String color=null;
		private String text=null;
		private String refctrl=null;
		private boolean empty=true;
		private String emptymsg=null;
		private String action=null;
		
		private Component comp=null;
	}
	
		
}
/*******************************************************************************
 * $RCSfile: LoginPanel.java,v $ $Revision: 1.23.2.8 $ $Date: 2009/12/16 04:14:08 $
 *
 * Revision 1.17  2007/03/14 10:24:25  qilin
 * 登陆方式的修改，合并登陆service为SystemLoginServiceIFC
 * 将登陆信息封装为LoginInfoVO对象
 *
 * Revision 1.4  2007/01/31 09:17:05  shxch
 * 添加等待框
 * Revision 1.3 2007/01/31 03:25:03 shxch 添加等待提示框
 * Revision 1.2 2007/01/30 04:20:39 lujian *** empty log message ***
 *
 *
 ******************************************************************************/
