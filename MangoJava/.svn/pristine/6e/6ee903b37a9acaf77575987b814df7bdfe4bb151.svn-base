/**************************************************************************
 * $RCSfile: UIDateTimePanel.java,v $  $Revision: 1.6.2.6 $  $Date: 2009/06/12 05:25:16 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentscard;

import java.text.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import smartx.framework.common.ui.*;
import smartx.framework.common.utils.StringUtil;
import smartx.framework.metadata.vo.*;


public class UIDateTimePanel extends AbstractNovaComponent implements INovaCompent {
    private static final long serialVersionUID = 1L;
    
    private static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
    
    protected JTextField textField = null;
    protected DatePicker dp;
    protected JButton btn_ref = null;
    protected String REF_IMAGE= "images/platform/date.jpg"; //点选参照按钮图标
    protected int REF_WIDTH=18;   //点选参照按钮宽度
    protected int REF_HEIGHT=18;  //点选参照按钮高度
    protected int DATE_WIDTH = this.FIELD_WIDTH - REF_WIDTH;//日期框控件宽度

    public UIDateTimePanel(Pub_Templet_1_ItemVO _templetVO) {
        this._vo = _templetVO; //

        this.key = this._vo.getItemkey(); //
        this.name = this._vo.getItemname(); //
        this.FIELD_WIDTH=this._vo.getCardwidth().intValue();
        this.DATE_WIDTH = this.FIELD_WIDTH - REF_WIDTH; //
        init();
    }

    public UIDateTimePanel(String _key, String _name) {
        this.key = _key; //
        this.name = _name; //
        init();
    }

    public UIDateTimePanel(String _key, String _name, boolean isNeed) {
        this.key = _key; //
        this.name = _name; //
        this.isNeed = isNeed;
        init();
    }
    
    
	protected JComponent[] getFieldComponents() {
    	textField = new JTextField();
    	textField.setEditable(false);
        textField.setPreferredSize(new Dimension(this.DATE_WIDTH, this.FIELD_HEIGHT));
        textField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
            }

            public void focusLost(FocusEvent e) {
            }
        });

        btn_ref = new JButton(UIUtil.getImage(this.REF_IMAGE));
        btn_ref.setPreferredSize(new Dimension(this.REF_WIDTH, this.REF_HEIGHT)); // 按扭的宽度与高度
        btn_ref.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onButtonClicked();
            }

        });
        /**
         * TODO 增加编辑事件，填入和清空的时候出发，参考UIRefPanel
         *   fireValueChanged 
         */
		return new JComponent[]{textField,btn_ref};
	}
    
    

    
    public JTextField getTextField() {
        return textField;
    }

    

    private void onButtonClicked() {
        if (textField.equals("")) {
            dp = new DatePicker(this, "日期设置", null);
        } else {
            dp = new DatePicker(this, "日期设置", null, StringUtil.shortStringToDate(textField
                .getText()));
        }
        Date dt = dp.getDate();
        if (dt != null) {
            textField.setText(StringUtil.shortDateToString(dt));            
        }
    }

    public JButton getBtn_ref() {
        return btn_ref;
    }

    
    public void setText(String _text) {
        textField.setText(_text);
    }

    public void reset() {
        textField.setText(""); //
    }

    public void setEditable(boolean _bo) {
        textField.setEditable(_bo);
        btn_ref.setEnabled(_bo);
    }

    public String getValue() {
        return textField.getText();
    }
    
    //add by James.W
	//TODO 对于日期时间框获得界面选择真实值的处理
	/**
	 * 获得界面选择的真实值
	 * @return String value
	 * @deprecated 本方法新增加，首先满足了日历的使用，其他类型的数据需要以后支持
	 */
    public String getInputValue() {
		return null;		
	}

    public Object getObject() {
        return getValue();
    }

    /**
     * 设置控件值
     * @param _obj 日期取值，格式字符串（yyyy-MM-dd HH:mm:SS）或者日期对象
     */
    public void setObject(Object _obj) {
    	if (_obj != null) {
        	if(_obj instanceof Date){
        		setValue(sdf.format((Date)_obj));
        	}else{
        		setValue(_obj.toString());
        	}
        }
    }
    
    /**
     * 设置取值
     * @param _value 日期取值，格式字符串（yyyy-MM-dd HH:mm:SS）
     */
    public void setValue(String _value) {
        textField.setText(_value); //
    }
    /**
     * 设置控件值
     * @param _obj 日期取值，格式字符串（yyyy-MM-dd HH:mm:SS）或者日期对象
     */
    public void setValue(Date _obj) {
    	setValue(sdf.format(_obj));   
    }


    public void focus() {
        textField.requestFocus();
        textField.requestFocusInWindow();
    }
    
    
    /**
     * 获得Lable的宽度
     * 2007.09.10 应陈学进邮件要求加入，代码实现：陈学进
     * @return 
     * @deprecated 应该通过共性属性设置
     */
    public int getLabelWidth(){
        return this.LABEL_WIDTH;
    }
    
    /**
     * 设置Lable的宽度
     * 2007.09.10 应陈学进邮件要求加入，代码实现：陈学进
     * @param _label_width the li_label_width to set
     * @deprecated 应该通过共性属性设置
     */
    public void setLabelWidth(int _label_width){
        this.LABEL_WIDTH = _label_width;        
    }

    /**
     * 获得文本框宽度
     * 2007.09.10 应陈学进邮件要求加入，代码实现：陈学进
     * @return the li_textfield_width
     */
    public int getTextfieldWidth(){
        return this.FIELD_WIDTH;
    }

     
    /**
     * 设置文本框宽度
     * 2007.09.10 应陈学进邮件要求加入，代码实现：陈学进
     * @param _textfield_width the li_textfield_width to set
     */
    public void setTextfieldWidth(int _textfield_width) {
        this.FIELD_WIDTH = _textfield_width;        
    }
    
}
/**************************************************************************
 * $RCSfile: UIDateTimePanel.java,v $  $Revision: 1.6.2.6 $  $Date: 2009/06/12 05:25:16 $
 *
 * $Log: UIDateTimePanel.java,v $
 * Revision 1.6.2.6  2009/06/12 05:25:16  wangqi
 * *** empty log message ***
 *
 * Revision 1.6.2.5  2009/05/22 02:38:55  wangqi
 * *** empty log message ***
 *
 * Revision 1.6.2.4  2008/12/09 07:40:07  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2008/11/04 13:58:03  wangqi
 * *** empty log message ***
 *
 * Revision 1.1  2008/09/01 07:38:10  wangqi
 * *** empty log message ***
 *
 * Revision 1.8  2008/06/11 16:40:35  wangqi
 * *** empty log message ***
 *
 * Revision 1.7  2008/06/02 03:26:21  wangqi
 * *** empty log message ***
 *
 *
 *
 **************************************************************************/
