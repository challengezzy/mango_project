/**************************************************************************
 * $RCSfile: TextFieldPanel.java,v $  $Revision: 1.4.2.7 $  $Date: 2009/11/04 08:31:21 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentscard;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.net.URLConnection;
import java.util.Vector;

import javax.swing.*;

import smartx.framework.common.ui.*;
import smartx.framework.metadata.ui.NovaEvent;
import smartx.framework.metadata.ui.NovaEventListener;
import smartx.framework.metadata.vo.*;



/**
 * Card类型面板使用文本编辑框
 * @author James.W
 *
 */
public class TextFieldPanel extends AbstractNovaComponent implements INovaCompent {

    private static final long serialVersionUID = -6967496356909878013L;

    protected boolean isNeed = false;       //是否必填
    protected String _defaultValue="";      //默认值设置 
    
    protected String CLEAR_IMAGE = "images/platform/clear.gif";
    protected int ICO_WIDTH=18;   
    protected int ICO_HEIGHT=18;  
    protected int REAL_FIELD_WIDTH = this.FIELD_WIDTH - ICO_WIDTH;
    
    protected JTextField textField = null;  //编辑框控件
    protected JButton btn_clear = null;   //清除按钮
    protected boolean isEditable = true;    //是否可编辑
    protected Vector v_Listeners = new Vector();//事件跟踪列表
    
    /**
     * 构造方法
     * @param _templetVO
     */
    public TextFieldPanel(Pub_Templet_1_ItemVO _templetVO) {
        this._vo = _templetVO;
        this.key = this._vo.getItemkey();
        this.name = this._vo.getItemname();
        this.FIELD_WIDTH = this._vo.getCardwidth().intValue(); // 设置宽度
        REAL_FIELD_WIDTH = this.FIELD_WIDTH - ICO_WIDTH;
        init();

    }

    /**
     * 构造方法
     * @param _key
     * @param _name
     */
    public TextFieldPanel(String _key, String _name) {
        this.key = _key;
        this.name = _name;
        init();
    }

    /**
     * 构造方法
     * @param _key
     * @param _name
     * @param isNeed
     */
    public TextFieldPanel(String _key, String _name, boolean isNeed) {
        this.key = _key;
        this.name = _name;
        this.isNeed = isNeed;
        init();
    }

    /**
     * 构造方法
     * @param _key
     * @param _name
     * @param isNeed
     * @param isEditable
     */
    
    public TextFieldPanel(String _key, String _name, boolean isNeed, boolean isEditable) {
        this.key = _key;
        this.name = _name;
        this.isNeed = isNeed;
        this.isEditable = isEditable;
        init();
    }
    

    /**
     * 构造方法
     * @param _key
     * @param _name
     * @param _width
     */
    public TextFieldPanel(String _key, String _name, int _width) {
        this.key = _key;
        this.name = _name;
        this.FIELD_WIDTH = _width;
        REAL_FIELD_WIDTH = this.FIELD_WIDTH - ICO_WIDTH;
        init();
    }

    /**
     * 构造方法
     * @param _key
     * @param _name
     * @param _width
     * @param isNeed 是否必须
     */
    public TextFieldPanel(String _key, String _name, int _width, boolean isNeed) {
        this.key = _key;
        this.name = _name;
        this.FIELD_WIDTH = _width;
        REAL_FIELD_WIDTH = this.FIELD_WIDTH - ICO_WIDTH;
        this.isNeed = isNeed;
        init();
    }
    
    
    public JTextField getTextField() {
        return textField;
    }

    

    /**
     * @deprecated 不建议使用，请改用 getObject
     */
    public String getValue() {
        return String.valueOf(getObject());
    }
    

	/**
	 * 获得界面选择的真实值
	 * @return String value
	 * @deprecated 本方法新增加，首先满足了日历的使用，其他类型的数据需要以后支持
	 */
    public String getInputValue() {
		return textField.getText();		
	}

    public String getText() {
        return textField.getText();
    }
    
    public Object getObject() {
    	return textField.getText();
    }

    public void setText(String _text) {
        textField.setText(_text);
    }

    public void setValue(String _value) {
        textField.setText(_value);
    }
    
    //Override
	public void setObject(Object o) {
    	if (o != null) {
            setValue(o.toString());
        }
	}
    
	public void reset() {
        textField.setText("");
    }

	/**
	 * 设置编辑控制
	 */
    public void setEditable(boolean _bo) {
        textField.setEditable(_bo);
        btn_clear.setEnabled(_bo);
    }

    

    

    public void focus() {
        textField.requestFocus(); //
        textField.requestFocusInWindow();
    }

	//Override
	protected JComponent[] getFieldComponents() {
		textField = new JTextField();
        if (!isEditable) {
            textField.setText("序列自动增长");
            textField.setEditable(false);
        }
        textField.setPreferredSize(new Dimension(REAL_FIELD_WIDTH, FIELD_HEIGHT));
        try{
			Method method = JTextField.class.getMethod("setComponentPopupMenu", new Class[]{JPopupMenu.class});
			method.invoke(textField, new Object[]{getPopMenu()});			
		}catch(NoSuchMethodException e){ 
			;
		}catch(Exception e){ 
			;
		}
        
        btn_clear = new JButton(UIUtil.getImage(this.CLEAR_IMAGE));
        btn_clear.setPreferredSize(new Dimension(this.ICO_WIDTH, this.ICO_HEIGHT));
        btn_clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	emptyField();                
            }
        });
        
		return new JComponent[]{textField,btn_clear};
	}

	public void emptyField(){
    	reset(); //
        onValueChanged(new NovaEvent(this)); //
    }
	
	
	
	
	/**
	 * 增加控件编辑响应事件
	 * 
	 * @param _listener
	 */
	public void addPlutoListener(NovaEventListener _listener) {
        v_Listeners.add(_listener);
    }

    public void onValueChanged(NovaEvent _evt) {
        for (int i = 0; i < v_Listeners.size(); i++) {
            NovaEventListener listener = (NovaEventListener) v_Listeners.get(i);
            listener.onValueChanged(_evt); //
        }
    }
    
    /**
	 * 控件右键菜单
	 * @return
	 */
	protected JPopupMenu getPopMenu(){
		Action cut = new ClipboardAction("剪切", UIUtil.getImage("images/office/(18,00).png"));
		Action copy = new ClipboardAction("拷贝", UIUtil.getImage("images/office/(31,34).png"));
		Action paste = new ClipboardAction("粘帖", UIUtil.getImage("images/office/(43,28).png")); 
		JPopupMenu pop=new JPopupMenu("剪切板");
		pop.add(cut);
		pop.add(copy);
		pop.add(paste);
		return pop;
	}
    
    
    /**
     * 文本框的剪切板操作
     * @author Jamae.W
     *
     */
    class ClipboardAction extends AbstractAction {
    	public ClipboardAction(String name){
    		super(name);   
    	} 
    	public ClipboardAction(String name, Icon icon){
    		super(name, icon);   
    	} 
    	
        public void actionPerformed(ActionEvent   event){
        	if(this.getValue(this.NAME).equals("剪切")){
        		textField.cut();
        	}else if(this.getValue(this.NAME).equals("拷贝")){
        		textField.copy();
        	}else if(this.getValue(this.NAME).equals("粘帖")){
        		textField.paste();
        	}else{
        		;
        	}
        } 
    }
    
    
}
/**************************************************************************
 * $RCSfile: TextFieldPanel.java,v $  $Revision: 1.4.2.7 $  $Date: 2009/11/04 08:31:21 $
 *
 * $Log: TextFieldPanel.java,v $
 * Revision 1.4.2.7  2009/11/04 08:31:21  wangqi
 * 增加TextFiledPanel控件右键菜单（剪切、拷贝、粘帖）支持。
 *
 * Revision 1.4.2.6  2009/06/26 08:32:23  wangqi
 * *** empty log message ***
 *
 * Revision 1.4.2.5  2009/06/24 14:01:44  wangqi
 * *** empty log message ***
 *
 * Revision 1.4.2.4  2009/06/12 05:25:15  wangqi
 * *** empty log message ***
 *
 * Revision 1.4.2.3  2008/11/05 05:21:03  wangqi
 * *** empty log message ***
 *
 *
 **************************************************************************/
