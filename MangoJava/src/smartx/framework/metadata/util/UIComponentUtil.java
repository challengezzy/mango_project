package smartx.framework.metadata.util;


import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToolBar;

import smartx.framework.common.utils.StringUtil;
import smartx.framework.common.utils.Sys;


/**
 * 界面控件工具
 * @author Administrator
 *
 */
public class UIComponentUtil {

	private UIComponentUtil(){		
	}
	
	/**
	 * 获得指定按钮
	 * @param text 按钮文本
	 * @param icon 图标
	 * @param size 大小
	 * @return
	 */
	public static JButton getButton(String text,Icon icon,Dimension size){
		JButton btn=new JButton(text, icon);
		btn.setPreferredSize(size);
		return btn;
	}
	
	/**
	 * 获得指定图标按钮
	 * @param text 按钮文本
	 * @param icon 图标
	 * @param size 大小
	 * @return
	 */
	public static JButton getIconButton(String text,Icon icon,Dimension size){
		JButton btn=new JButton(icon);
		btn.setToolTipText(text);
		btn.setPreferredSize(size);
		return btn;
	}
	
	/**
	 * 获得指定按钮
	 * @param text 按钮文本
	 * @param icon 图标
	 * @param size 大小
	 * @param hasBorder 是否显示边框
	 * @param hasFocus 是否显示焦点（焦点框）
	 * @return
	 */
	public static JButton getButton(String text,Icon icon,Dimension size,boolean hasBorder,boolean hasFocus){
		JButton btn=getButton(text,icon,size);
		btn.setBorderPainted(hasBorder);
		btn.setFocusPainted(hasFocus);
		return btn;
	}
	
	/**
	 * 获得指定按钮
	 * @param text 按钮文本
	 * @param icon 图标
	 * @param size 大小
	 * @param hasBorder 是否显示边框
	 * @param hasFocus 是否显示焦点（焦点框）
	 * @return
	 */
	public static JButton getIconButton(String text,Icon icon,Dimension size,boolean hasBorder,boolean hasFocus){
		JButton btn=getIconButton(text,icon,size);
		btn.setBorderPainted(hasBorder);
		btn.setFocusPainted(hasFocus);
		return btn;
	}
	
	/**
	 * 获得指定按钮
	 * @param text 按钮文本
	 * @param icon 图标
	 * @param size 大小
	 * @param hasBorder 是否显示边框
	 * @param hasFocus 是否显示焦点（焦点框）
	 * @param actionlistener 响应监听器
	 * @return
	 */
	public static JButton getButton(String text,Icon icon,Dimension size,boolean hasBorder,boolean hasFocusBorder,
			ActionListener actionlistener){
		JButton btn=getButton(text,icon,size,hasBorder,hasFocusBorder);
		btn.addActionListener(actionlistener);
		return btn;
	}
	
	/**
	 * 获得指定按钮
	 * @param text 按钮文本
	 * @param icon 图标
	 * @param size 大小
	 * @param hasBorder 是否显示边框
	 * @param hasFocus 是否显示焦点（焦点框）
	 * @param actionlistener 响应监听器
	 * @return
	 */
	public static JButton getIconButton(String text,Icon icon,Dimension size,boolean hasBorder,boolean hasFocusBorder,
			ActionListener actionlistener){
		JButton btn=getIconButton(text,icon,size,hasBorder,hasFocusBorder);
		btn.addActionListener(actionlistener);
		return btn;
	}
	
	/**
	 * 获得指定按钮
	 * @param text 按钮文本
	 * @param icon 图标
	 * @param size 大小
	 * @param hasBorder 是否显示边框
	 * @param hasFocus 是否显示焦点（焦点框）
	 * @param actionlistener 响应监听器
	 * @param keyListener 键盘响应监听器
	 * @return
	 */
	public static JButton getButton(String text,Icon icon,Dimension size,boolean hasBorder,boolean hasFocusBorder,
			ActionListener actionlistener, KeyListener keyListener){
		JButton btn=getButton(text,icon,size,hasBorder,hasFocusBorder);
		btn.addActionListener(actionlistener);
		btn.addKeyListener(keyListener);
		return btn;
	}
	
	/**
	 * 获得指定按钮
	 * @param text 按钮文本
	 * @param icon 图标
	 * @param size 大小
	 * @param hasBorder 是否显示边框
	 * @param hasFocus 是否显示焦点（焦点框）
	 * @param actionlistener 响应监听器
	 * @param keyListener 键盘响应监听器
	 * @return
	 */
	public static JButton getIconButton(String text,Icon icon,Dimension size,boolean hasBorder,boolean hasFocusBorder,
			ActionListener actionlistener, KeyListener keyListener){
		JButton btn=getIconButton(text,icon,size,hasBorder,hasFocusBorder);
		btn.addActionListener(actionlistener);
		btn.addKeyListener(keyListener);
		return btn;
	}
	
	/**
	 * 获得按钮配置定义大小
	 * @return
	 */
	public static Dimension getButtonDefaultSize(){
		UI_TOOLBAR_BTN_WIDTH=(UI_TOOLBAR_BTN_WIDTH==-1)
		            ?StringUtil.parseInt(Sys.getInfo("UI_TOOLBAR_BTN_WIDTH"),80)
		            :UI_TOOLBAR_BTN_WIDTH;
		UI_TOOLBAR_BTN_HEIGHT=(UI_TOOLBAR_BTN_HEIGHT==-1)
		            ?StringUtil.parseInt(Sys.getInfo("UI_TOOLBAR_BTN_HEIGHT"),20)
		            :UI_TOOLBAR_BTN_HEIGHT;
		return new Dimension(UI_TOOLBAR_BTN_WIDTH, UI_TOOLBAR_BTN_HEIGHT);		
	}
	private static int UI_TOOLBAR_BTN_WIDTH=-1;
	private static int UI_TOOLBAR_BTN_HEIGHT=-1;
	
	/**
	 * 获得按钮配置定义大小
	 * @return
	 */
	public static Dimension getIconButtonDefaultSize(){
		UI_TOOLBAR_ICONBTN_WIDTH=(UI_TOOLBAR_ICONBTN_WIDTH==-1)
		            ?StringUtil.parseInt(Sys.getInfo("UI_TOOLBAR_ICONBTN_WIDTH"),80)
		            :UI_TOOLBAR_BTN_WIDTH;
	    UI_TOOLBAR_ICONBTN_HEIGHT=(UI_TOOLBAR_ICONBTN_HEIGHT==-1)
		            ?StringUtil.parseInt(Sys.getInfo("UI_TOOLBAR_ICONBTN_HEIGHT"),20)
		            :UI_TOOLBAR_BTN_HEIGHT;
		return new Dimension(UI_TOOLBAR_ICONBTN_WIDTH, UI_TOOLBAR_ICONBTN_HEIGHT);		
	}
	private static int UI_TOOLBAR_ICONBTN_WIDTH=-1;
	private static int UI_TOOLBAR_ICONBTN_HEIGHT=-1;
	
	
	/**
	 * 获得InternalFrame配置定义大小
	 * @return
	 */
	public static Dimension getInternalFrameDefaultSize(){
		int width=StringUtil.parseInt(Sys.getInfo("UI_FRAME_WIDTH"),1000);
		int height=StringUtil.parseInt(Sys.getInfo("UI_FRAME_HEIGHT"),750);
		return new Dimension(width, height);		
	}
	
	
	/**
	 * 根据给定的控件创建Bar，默认横向Bar
	 * @param ctrls 控件列表，如果数组中某个元素为空，则在此位置插入一个间隔。
	 * @param tbar 需要增加控件的bar，如果为空则创建一个。
	 * @return JToolBar
	 */
	public static JToolBar buildToolBar(JComponent[] ctrls, JToolBar tbar){
		if(ctrls==null||ctrls.length==0) return tbar;
		JToolBar rt=(tbar==null)?(new JToolBar()):tbar;
		for(int i=0;i<ctrls.length;i++){
			if(ctrls[i]==null){
				rt.addSeparator();
			}else{
				rt.add(ctrls[i]);
			}
		}
		return rt;		
	}
	
	/**
	 * 根据给定的控件创建Bar，默认横向Bar
	 * @param ctrls 控件列表，如果数组中某个元素为空，则在此位置插入一个间隔。
	 * @param tbar 需要增加控件的bar，如果为空则创建一个。
	 * @return JToolBar
	 */
	public static JToolBar buildToolBar(Action[] acts, JToolBar tbar){
		if(acts==null||acts.length==0) return tbar;
		JToolBar rt=(tbar==null)?(new JToolBar()):tbar;
		for(int i=0;i<acts.length;i++){
			if(acts[i]==null){
				rt.addSeparator();
			}else{
				rt.add(acts[i]);
			}
		}
		return rt;		
	}
	
}
