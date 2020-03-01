/**********************************************************************
 *$RCSfile: AbstractNovaComponent.java,v $  $Revision: 1.1.2.3 $  $Date: 2009/02/13 09:58:46 $
 *********************************************************************/ 
package smartx.framework.metadata.ui.componentscard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;


import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import smartx.framework.common.ui.NovaClientEnvironment;
import smartx.framework.common.ui.UIUtil;
import smartx.framework.common.utils.StringUtil;
import smartx.framework.common.utils.Sys;
import smartx.framework.metadata.ui.NovaEvent;
import smartx.framework.metadata.ui.NovaEventListener;
import smartx.framework.metadata.vo.Pub_Templet_1_ItemVO;


/**
 * <li>Title: AbstractNovaComponent.java</li>
 * <li>Description: 组件抽象实现类</li>
 * 通过本类规范各界面展现控件，主要是Card面板和QuickQuery面板上的控件的行为。
 * <li>Project: NOVA2_MAINBRANCH</li>
 * <li>Copyright: Copyright (c) 2008</li>
 * @Company: GXLU. All Rights Reserved.
 * @author James.W
 * @version 1.0
 */
public abstract class AbstractNovaComponent extends JPanel implements INovaCompent {
	
	
	/**
	 * 获得组件的key
	 * @return
	 */
    public String getKey(){
    	return this.key;
    }
    /**
     * 设置组件key
     */
    public void setKey(String key){
    	this.key=key;
    }

    /**
     * 获得组件显示名称
     * @return
     */
    public String getName(){
    	return this.name;
    }
    /**
     * 设置组件显示名称
     */
    public void setName(String name){
    	this.name=name;
    }

    /**
     * 获得组件真实值
     * @return
     */
    public abstract String getInputValue();
    
    /**
     * 获得组件取值对象
     * @return
     */
    public abstract Object getObject(); //
    
    /**
     * 
     */
    public abstract String getValue();
    

    /**
     * 设置组件值
     * @param _value
     */
    public abstract void setObject(Object _value);

    /**
     * 重置组件
     */
    public abstract void reset(); //

    /**
     * 设置是否允许编辑
     * @param _bo
     */
    public abstract void setEditable(boolean _bo);

    /**
     * 获得组件标签对象
     * @return
     */
    public JLabel getLabel(){
    	return _label;
    }

    /**
     * 组件设置焦点
     */
    public abstract void focus();
    
    /**
     * 获得组件模板设置对象
     * @return
     */
    public Pub_Templet_1_ItemVO getTempletItemVO(){
    	return _vo;
    }
	
    /**
     * 本组件的域是否必填
     * @return
     */
    public boolean isNeed() {
		return isNeed;
	}
    
    /**
     * 设置事件侦听器
     * @param _listener
     */
    public void addNovaEventListener(NovaEventListener _listener) {
    	if(_listeners==null) _listeners=new ArrayList();
    	_listeners.add(_listener);
    }
    
    /**
     * 移除侦听器
     * @param i
     */
    public NovaEventListener removeNovaEventListener(int idx){
    	if(_listeners==null) return null;
    	return (NovaEventListener)_listeners.remove(idx);
    }

    /**
     * 触发事件处理，具体控件实现时可以根据需要通过本方法出发事件。
     * @param _evt
     */
    protected void fireValueChanged(NovaEvent _evt) {
    	if(_listeners==null) return;
        for (int i = 0; i < _listeners.size(); i++) {
            ((NovaEventListener)_listeners.get(i)).onValueChanged(_evt);            
        }
    }

    //*****组件界面控制*****************************************//
    /**标签是否显示*/
    protected boolean LABEL_VISIBLE=true;
    /**域是否可用*/
    protected boolean FIELD_ENABLE=true;    
    /**标签图标*/
    protected Icon LABEL_ICON=null;
    /**组件背景色*/
    protected Color COMPONENT_BGCOLOR=UIUtil.getColor((String)Sys.getInfo("UI_COMPONENT_BGCOLOR"));
    /**标签前景色*/
    protected Color LABEL_FGCOLOR=UIUtil.getColor((String)Sys.getInfo("UI_LABEL_FGCOLOR"));
    /**标签备景色*/
    protected Color LABEL_BGCOLOR=UIUtil.getColor((String)Sys.getInfo("UI_LABEL_BGCOLOR"));
    /**标签宽度*/
    protected int LABEL_WIDTH=StringUtil.parseInt(Sys.getInfo("UI_LABEL_WIDTH"),100);
    /**标签高度，一般不可改*/
    protected int LABEL_HEIGHT=StringUtil.parseInt(Sys.getInfo("UI_LABEL_HEIGHT"),20);      
    /**标签水平对齐*/
    protected String LABEL_HALIGN=StringUtil.parseString(Sys.getInfo("UI_LABEL_HALIGN"),"left");
    /**标签垂直对齐*/
    protected String LABEL_VALIGN=StringUtil.parseString(Sys.getInfo("UI_LABEL_VALIGN"),"top");    
    /**标签位置*/
    protected String LABEL_DIRECT=StringUtil.parseString(Sys.getInfo("UI_LABEL_DIRECT"),"left");
    /**域宽度*/
    protected int FIELD_WIDTH=StringUtil.parseInt(Sys.getInfo("UI_FIELD_WIDTH"),150);
    /**域高度*/
    protected int FIELD_HEIGHT=StringUtil.parseInt(Sys.getInfo("UI_FIELD_HEIGHT"),20);          
    /**上面留白*/
    protected int MERGE_NORTH=StringUtil.parseInt(Sys.getInfo("UI_MERGE_NORTH"),0);            
    /**左面留白*/
    protected int MERGE_WEST=StringUtil.parseInt(Sys.getInfo("UI_MERGE_WEST"),5);
    /**下面留白*/
    protected int MERGE_SOUTH=StringUtil.parseInt(Sys.getInfo("UI_MERGE_SOUTH"),0);
    /**右面留白*/
    protected int MERGE_EAST=StringUtil.parseInt(Sys.getInfo("UI_MERGE_EAST"),5);             
    /**留白填充色*/
    protected Color MERGE_COLOR=UIUtil.getColor((String)Sys.getInfo("UI_MERGE_COLOR"));       
    /**留白填充图*/
    protected Icon MERGE_ICON=UIUtil.getImage((String)Sys.getInfo("UI_MERGE_ICON"));         
    //*****组件业务属性*****************************************//
    /**组件定义VO*/
    protected Pub_Templet_1_ItemVO _vo=null;
    /**标签对象*/
    protected JLabel _label = null;         
    /**组件事件侦听器队列*/
    protected ArrayList _listeners=null;
    /**组件key，程序识别用*/
    protected String key = null;
    /**组件name，界面显示用*/
    protected String name = null;
    /**组件是否必填*/
    protected boolean isNeed = false;
    
    
    /**
     * 设置标签图标
     * @param ico
     */
    public void setLabelIcon(ImageIcon ico){
    	LABEL_ICON=ico;
    }
    
    /**
     * 设置标签图标
     * @param icopath
     */
    public void setLabelIcon(String path){
    	LABEL_ICON=UIUtil.getImage(path);
    }
    
    /**
     * 设置标签宽度
     * @param width
     */
    public void setLabelWidth(int width){
    	LABEL_WIDTH = width;
    }
    
    
    /**
     * 设置编辑区域宽度
     */
    public void setFieldWidth(int width){
    	FIELD_WIDTH = width;
    }
    
    /**
     * 设置组件高度
     * @param height
     */
    public void setFieldHeight(int height){
    	FIELD_HEIGHT=height;
    }
    
    /**
     * 设置留白宽度
     * @param nouth
     * @param east
     * @param south
     * @param west
     */
    public void setMergeWidth(int north,int west,int south,int east,Color color){
    	MERGE_NORTH=north;
    	MERGE_WEST=west;
    	MERGE_SOUTH=south;
    	MERGE_EAST=east;
    	MERGE_COLOR=color;
    }
    /**
     * 设置边框
     * @param nouth
     * @param east
     * @param south
     * @param west
     * @param ico
     */
    public void setMergeWidth(int north,int east,int south,int west,Icon ico){
    	MERGE_NORTH=north;
    	MERGE_WEST=west;
    	MERGE_SOUTH=south;
    	MERGE_EAST=east;
    	MERGE_ICON=ico;
    }
    
    /**
     * 设置标对齐方式
     * @param halign 水平 left/center/right
     * @param valign 垂直 top/middle/bottom
     */
    public void setLabelAlign(String halign,String valign){
    	LABEL_HALIGN=halign==null?"left":halign;
    	LABEL_VALIGN=valign==null?"top":valign;
    }
    
    /**
     * 设置标签位置方向
     * @param direct 位置方向 left/top，默认 left
     */
    public void setLabelDirect(String direct){
    	LABEL_DIRECT=direct==null?"left":direct;
    }
    
    /**
     * 标签是否展现
     * @param b
     */
    public void setLabelVisible(boolean b){
    	LABEL_VISIBLE=b;
    }
    /*****界面控制属性*****************************************/
    
    /**
     * 初始化方法
     */
    protected void init(){
    	//设置组件首选大小
    	setPreferredSize(getComponetDimension());
    	//设置组件边框
    	Border border=null;
    	if(MERGE_ICON!=null){
    		border=BorderFactory.createMatteBorder(MERGE_NORTH, MERGE_WEST, MERGE_SOUTH, MERGE_EAST, MERGE_ICON);
    	}else if(MERGE_COLOR!=null){
    		border=BorderFactory.createMatteBorder(MERGE_NORTH, MERGE_WEST, MERGE_SOUTH, MERGE_EAST, MERGE_COLOR);
    	}else{
    		border=BorderFactory.createEmptyBorder(MERGE_NORTH, MERGE_WEST, MERGE_SOUTH, MERGE_EAST);
    	}
    	setBorder(border);
    	
        //设置组件内的部件排列方向
    	FlowLayout layout = new FlowLayout(FlowLayout.LEFT);        
    	layout.setHgap(0);
    	layout.setVgap(0);
        setLayout(layout);
        
        //背景色
        if(COMPONENT_BGCOLOR!=null){
        	setBackground(COMPONENT_BGCOLOR);
        }
        
        //生成控件
        _label=getLabelComponent();
        if(_label!=null)add(_label);
    	JComponent[] cmps=getFieldComponents();    	
    	for(int i=0;i<cmps.length;i++){
    		add(cmps[i]);
    	}
    }
    
    /**
     * 获得组件首选大小
     * @return Dimension
     */
    protected Dimension getComponetDimension(){
    	int width=0;
    	int height=0;
    	
    	if(LABEL_DIRECT.equalsIgnoreCase("top")){//标签在编辑域上面
    		if(this.LABEL_VISIBLE){
	    		width=(LABEL_WIDTH>=FIELD_WIDTH?LABEL_WIDTH:FIELD_WIDTH)+MERGE_WEST+MERGE_EAST;
	    		height=LABEL_HEIGHT+FIELD_HEIGHT+MERGE_NORTH+MERGE_SOUTH;
    		}else{
    			width=FIELD_WIDTH+MERGE_WEST+MERGE_EAST;
	    		height=FIELD_HEIGHT+MERGE_NORTH+MERGE_SOUTH;
    		}
    	}else{//default
    		if(this.LABEL_VISIBLE){
	    		width=LABEL_WIDTH+FIELD_WIDTH+MERGE_WEST+MERGE_EAST;
	    		height=(LABEL_HEIGHT>=FIELD_HEIGHT?LABEL_HEIGHT:FIELD_HEIGHT)+MERGE_NORTH+MERGE_SOUTH;
    		}else{
    			width=FIELD_WIDTH+MERGE_WEST+MERGE_EAST;
	    		height=FIELD_HEIGHT+MERGE_NORTH+MERGE_SOUTH;
    		}
    	}
    	
    	return new Dimension(width+10, height);    	
    }
    
    
    /**
     * 获得标签对象
     */
	protected JLabel getLabelComponent(){
		if(!this.LABEL_VISIBLE) return null;
		
		//必须从getName方法中获得name属性，继承的类如果想动态改变name就会重载getName方法。
		String sname=getName();
		JLabel label=new JLabel(sname);
		label.setPreferredSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
		/**
		 * 如果设置了标签图标，默认在左边显示图标
		 */
		if(LABEL_ICON!=null){
			label.setIcon(LABEL_ICON);
			label.setHorizontalTextPosition(SwingConstants.RIGHT);
		}
		
        
		
		/**
		 * 管理调试设置状态下，界面增加一些调试信息
		 */
		if (NovaClientEnvironment.getInstance().isAdmin()) {
        	String str_text = sname;
            String str_tooltiptext = "";
            if (_vo != null) {
            	if (_vo.isNeedSave()) {
                    if (_vo.isCanSave()) {
                        str_tooltiptext = str_tooltiptext + " [" + _vo.getPub_Templet_1VO().getSavedtablename() +
                            "]";
                    } else {
                        str_tooltiptext = str_tooltiptext + " [" + _vo.getPub_Templet_1VO().getSavedtablename() +
                            "],该表没有该列!!将会保存失败!!";
                    }
                }
                if (_vo.isViewColumn()) {
                    str_text = str_text+"*";
                    str_tooltiptext = "[" + _vo.getPub_Templet_1VO().getTablename() + "." +
                        _vo.getItemkey() + "]" + str_tooltiptext;
                } else {
                    str_tooltiptext = "[" + _vo.getItemkey() + "]" + str_tooltiptext;
                }
            }
            label.setText(str_text);
            label.setToolTipText(str_tooltiptext);
        } else {
            label.setText(sname);
        }
		/**
		 * 1、如果vo存在
		 *    判断是否主键，蓝色
		 *    判断是否必输，蓝色
		 *    判断是否作为条件时必输，红色
		 * 2、如果vo不存在
		 *    判读是否必输，蓝色   
		 */
        if(_vo!=null){
	        if (_vo.isPrimaryKey()) {
	            label.setForeground(java.awt.Color.BLUE);
	        } else if (_vo.isMustInput()) {//必输控制
	            label.setForeground(Color.BLUE);
	        } else if (_vo.isMustCondition()) {//必输条件控制
	            label.setForeground(Color.RED);
	        } else {
	            label.setForeground(Color.BLACK);
	        }            
	        if (_vo.isNeedSave()) {
	        	//如果管理调试状态下，需要保存的字段为红色
	            if (!_vo.isCanSave()&&NovaClientEnvironment.getInstance().isAdmin()) {
	                label.setForeground(java.awt.Color.RED);
	            }
	        }
        }else{
	        if (isNeed()) {
	            label.setForeground(Color.BLUE);
	        }
        }
        
        /**
         * 设置Label前景色、背景色
         */
        //if(LABEL_FGCOLOR!=null){
        //	label.setForeground(LABEL_FGCOLOR);
        //}
        if(LABEL_BGCOLOR!=null){
        	label.setBackground(LABEL_BGCOLOR);
        }
        
        
		/**
		 * 判断文本水平对齐方向
		 */
		if(LABEL_HALIGN.equalsIgnoreCase("right")){
		    label.setHorizontalAlignment(SwingConstants.RIGHT);
		}else if(LABEL_HALIGN.equalsIgnoreCase("center")){
		    label.setHorizontalAlignment(SwingConstants.CENTER);
		}else{
			label.setHorizontalAlignment(SwingConstants.LEFT);
		}
		
		/**
		 * 判断文本垂直对齐方向
		 */
		if(LABEL_VALIGN.equals("bottom")){
			label.setVerticalAlignment(SwingConstants.BOTTOM);
		}else if(LABEL_VALIGN.equals("middle")){
			label.setVerticalAlignment(SwingConstants.CENTER);
		}else{
			label.setVerticalAlignment(SwingConstants.TOP);
		}
		
		return label;
	}
	
	
	
	/**
     * 获得对象组件数组，实际展现对象
     */
	protected abstract JComponent[] getFieldComponents();
}

/**********************************************************************
 *$RCSfile: AbstractNovaComponent.java,v $  $Revision: 1.1.2.3 $  $Date: 2009/02/13 09:58:46 $
 *
 *$Log: AbstractNovaComponent.java,v $
 *Revision 1.1.2.3  2009/02/13 09:58:46  wangqi
 **** empty log message ***
 *
 *Revision 1.1.2.2  2008/11/17 06:48:04  wangqi
 **** empty log message ***
 *
 *Revision 1.1.2.1  2008/11/05 05:21:03  wangqi
 **** empty log message ***
 *
 *Revision 1.3  2008/11/04 13:58:03  wangqi
 **** empty log message ***
 *
 *Revision 1.2  2008/11/03 07:18:46  wangqi
 **** empty log message ***
 *
 *Revision 1.1  2008/10/30 08:18:38  wangqi
 **** empty log message ***
 *
 *********************************************************************/