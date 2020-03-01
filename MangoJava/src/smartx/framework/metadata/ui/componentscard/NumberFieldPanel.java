/**************************************************************************
 * $RCSfile: NumberFieldPanel.java,v $  $Revision: 1.4.2.8 $  $Date: 2009/06/26 08:32:23 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentscard;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.text.*;

import smartx.framework.common.ui.*;
import smartx.framework.metadata.ui.NovaEvent;
import smartx.framework.metadata.ui.NovaEventListener;
import smartx.framework.metadata.vo.*;


public class NumberFieldPanel extends AbstractNovaComponent implements INovaCompent {

    private static final long serialVersionUID = -6792616872473650976L;

    protected int _type = QueryNumericPanel.TYPE_FLOAT;  //控件类型
    
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
    public NumberFieldPanel(Pub_Templet_1_ItemVO _templetVO) {
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
    public NumberFieldPanel(String _key, String _name) {
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
    public NumberFieldPanel(String _key, String _name, boolean isNeed) {
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
    public NumberFieldPanel(String _key, String _name, boolean isNeed, boolean isEditable) {
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
     * @param _width 域宽度。小于等于0表示取默认。
     */
    public NumberFieldPanel(String _key, String _name, int _width) {
        this.key = _key;
        this.name = _name;
        if(_width>0){
        	this.FIELD_WIDTH = _width; // 设置宽度
        	REAL_FIELD_WIDTH = this.FIELD_WIDTH - ICO_WIDTH;
        }
        init();
    }
    
    
    

    /**
     * 构造方法
     * @param _key
     * @param _name
     * @param _width 域宽度。-1表示取默认。
     * @param isNeed
     */
    public NumberFieldPanel(String _key, String _name, int _width, boolean isNeed) {
        this.key = _key;
        this.name = _name;
        if(_width>0){
        	this.FIELD_WIDTH = _width; // 设置宽度
        	REAL_FIELD_WIDTH = this.FIELD_WIDTH - ICO_WIDTH;
        }
        this.isNeed = isNeed;
        init();
    }
    
    /**
     * 构造方法(指定类型和宽度)
     * @author James.W 20090601
     * @param _key
     * @param _name
     * @param _type 数值类型。<br/>
	 *             QueryNumericPanel.TYPE_INTEGER - 整数型，纯由数字组成；<br/>
	 *             QueryNumericPanel.TYPE_FLOAT - 浮点型，由数字和一个小数点组成；<br/>
	 *             QueryNumericPanel.TYPE_CODE - 数字、英文字母、中划线、下划线组成。
     * @param _width 域宽度。小于等于0表示取默认。
     */
    public NumberFieldPanel(String _key, String _name, int _type, int _width) {
        this.key = _key;
        this.name = _name;
        this._type = _type;
        if(_width>0){
        	this.FIELD_WIDTH = _width; // 设置宽度
        	REAL_FIELD_WIDTH = this.FIELD_WIDTH - ICO_WIDTH;
        }
        init();
    }
    
    /**
     * 构造方法
     * @param _templetVO
     * @param _type 数值类型。<br/>
	 *             QueryNumericPanel.TYPE_INTEGER - 整数型，纯由数字组成；<br/>
	 *             QueryNumericPanel.TYPE_FLOAT - 浮点型，由数字和一个小数点组成；<br/>
	 *             QueryNumericPanel.TYPE_CODE - 数字、英文字母、中划线、下划线组成。
     * @param _width 域宽度。小于等于0表示取默认。
     */
    public NumberFieldPanel(Pub_Templet_1_ItemVO _templetVO, int _type, int _width) {
    	this._vo = _templetVO;
    	this._type=_type;
        this.key = this._vo.getItemkey();
        this.name = this._vo.getItemname();
        if(_width>0){
        	this.FIELD_WIDTH = _width; // 设置宽度
        	REAL_FIELD_WIDTH = this.FIELD_WIDTH - ICO_WIDTH;
        }
        init();
    }
    
    
    
    
    
    /**
     * 创建用于展现的对象（从左向右排列）
     */
	protected JComponent[] getFieldComponents() {
    	/**
    	 * 根据type指定类型生成控件
    	 */
    	textField = new JTextField();
        if (!isEditable) {
            //textField.setText("序列自动增长");
            textField.setEditable(false);
        }
        textField.setPreferredSize(new Dimension(this.REAL_FIELD_WIDTH, this.FIELD_HEIGHT));
        textField.setHorizontalAlignment(JTextField.RIGHT); //
        
        //设置文本过滤
        textField.setDocument(new PlainDocument(){
        	    private static final long serialVersionUID = -6162395929065942879L;
				public void insertString(int offset, String s, AttributeSet attributeSet) throws BadLocationException {
		            String pstr=getPsttern();        	
		        	
		        	int len=super.getLength();
		        	String str=super.getText(0, len);
		        	str=(offset==len)?(str+s):(str.substring(0,offset)+s+str.substring(offset));
		        	
		        	Pattern p = Pattern.compile(pstr);
		        	boolean isNum = p.matcher(str).matches();
		        	
		        	if (isNum) { // 如果是数字就插入!!
		                if (offset == 0 && (s.equals("."))) {
		                    Toolkit.getDefaultToolkit().beep(); // 放个声音!!
		                    return; //
		                } else {
		                    super.insertString(offset, s, attributeSet);
		                }
		            } else {
		                Toolkit.getDefaultToolkit().beep(); // 放个声音!!
		                return; // 直接返回不插入了!!
		            }
		        }
	
		    }
        
        );
        //离开焦点时候校验
        textField.setInputVerifier(new InputVerifier() {   
    	        public boolean verify(JComponent comp) {   
	    	        String s = ((JTextField) comp).getText();   
	    	        if(s.equals("")) return true;
	    	        String pstr=getPsttern();   
	            	
	            	Pattern p = Pattern.compile(pstr);
	            	boolean rt=p.matcher(s).matches();
	            	if(!rt){
	            		Toolkit.getDefaultToolkit().beep(); 
	            	}
	            	return rt;
    	        }   
    	    }
    	);
        
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

	
    public JTextField getTextField() {
        return textField;
    }

    
    
    public String getValue() { 
    	return textField.getText() == null ? "" : textField.getText().trim();
    }
    
    //add by James.W
	//TODO 对于数值框获得界面选择真实值的处理
	/**
	 * 获得界面选择的真实值
	 * @return String value
	 * @deprecated 本方法新增加，首先满足了日历的使用，其他类型的数据需要以后支持
	 */
    public String getInputValue() {
		return null;		
	}

    public void setKey(String key) {
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return textField.getText();
    }

    public void setText(String _text) {
        textField.setText(_text);
    }

    public void setValue(String _value) {
        textField.setText(_value);
    }

    public void reset() {
        textField.setText("");
    }

    /**
     * 设置是否编辑
     */
    public void setEditable(boolean _bo) {
        textField.setEditable(_bo);
        btn_clear.setEnabled(_bo);
    }

    public Object getObject() {
        return getValue();
    }

    public void setObject(Object _obj) {
        if (_obj != null) {
            setValue(_obj.toString());
        }
    }

    public void focus() {
        textField.requestFocus();
        textField.requestFocusInWindow();
    }
    
    
    /**
     * 获得格式串
     * 根据当前控件的类型
     * @return
     */
    public String getPsttern(){
    	if(this._type==QueryNumericPanel.TYPE_INTEGER){
    		return "[+-]{0,1}[0-9]{1,}";
    	}else if(this._type==QueryNumericPanel.TYPE_FLOAT){
    		return "[+-]{0,1}[0-9]{1,}[.]{0,1}[0-9]{0,}";
    	}else{//QueryNumericPanel.TYPE_CODE
    		return "[A-Z,0-9]{1,}[A-Z,0-9,-]{0,}[A-Z,0-9]{1,}";
    	}
    }
    
}
/**************************************************************************
 * $RCSfile: NumberFieldPanel.java,v $  $Revision: 1.4.2.8 $  $Date: 2009/06/26 08:32:23 $
 *
 * $Log: NumberFieldPanel.java,v $
 * Revision 1.4.2.8  2009/06/26 08:32:23  wangqi
 * *** empty log message ***
 *
 * Revision 1.4.2.7  2009/06/24 14:00:40  wangqi
 * *** empty log message ***
 *
 * Revision 1.4.2.6  2009/06/12 05:25:15  wangqi
 * *** empty log message ***
 *
 * Revision 1.3  2009/01/16 08:58:18  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2008/11/04 13:58:04  wangqi
 * *** empty log message ***
 *
 * 
 *
 **************************************************************************/
