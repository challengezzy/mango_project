/**************************************************************************
 * $RCSfile: QueryNumberFieldstartPanel.java,v $  $Revision: 1.3.2.4 $  $Date: 2009/06/12 05:25:15 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentscard;

import java.awt.*;

import javax.swing.*;
import javax.swing.text.*;

import smartx.framework.metadata.vo.*;


/**
 * 开始数值控件
 *  
 * @deprecated 本控件及相关控件由QueryNumericPanel代替
 */
public class QueryNumberFieldstartPanel extends AbstractNovaComponent implements INovaCompent {
    
    protected JFormattedTextField textField = null;

    

    public QueryNumberFieldstartPanel(Pub_Templet_1_ItemVO _templetVO) {
        this._vo = _templetVO;
        this.key = this._vo.getItemkey() + "START";
        this.name = "起始"+this._vo.getItemname();
        this.FIELD_WIDTH = this._vo.getCardwidth().intValue();
        init();

    }

    public QueryNumberFieldstartPanel(String _key, String _name) {
        this.key = _key + "START";
        this.name = "起始"+_name;
        init();
    }
    
    
    
    
    
    //Override
	protected JComponent[] getFieldComponents() {
    	textField = new JFormattedTextField();
        textField.setDocument(new IntegerDoucument());
        textField.setInputVerifier(new FormattedTextFieldVerifier());
        textField.setPreferredSize(new Dimension(this.FIELD_WIDTH, this.FIELD_HEIGHT));
        /**
         * TODO 增加编辑事件，填入和清空的时候出发，参考UIRefPanel
         *   fireValueChanged 
         */
		return new JComponent[]{textField};
	}

	//Override
	public void setObject(Object _value) {
		setValue( (String) _value);
	}    

    public JTextField getTextField() {
        return textField;
    }

    public String getKey() {
        return key;
    }

    

    public String getValue() {
        return textField.getText() == null ? "" : textField.getText()
            .toString();
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

    

    public String getText() {
        return textField.getText();
    }

    public void setText(String _text) {
        textField.setText(_text);
    }

    public void setValue(String _value) {
        textField.setText(_value == null ? "" : _value);
    }

    public void reset() {
        textField.setText("");
    }

    public void setEditable(boolean _bo) {
        textField.setEditable(_bo);
    }

    public Object getObject() {
        return getValue();
    }

    public void focus() {
        textField.requestFocus();
        textField.requestFocusInWindow();
    }

    //TODO 重复定义，几个Number相关的组件都有类似处理
    class FormattedTextFieldVerifier extends InputVerifier {

        public boolean verify(JComponent input) {
            JFormattedTextField field = (JFormattedTextField) input;
            return field.isEditValid();
        }

    }
    //TODO 重复定义，几个Number相关的组件都有类似处理
    class IntegerDoucument extends PlainDocument {
        public void insertString(int offset, String s, AttributeSet attributeSet) throws BadLocationException {
            try {
                Integer.parseInt(s);
            } catch (Exception e) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }
            super.insertString(offset, s, attributeSet);
        }
    }
}
/**************************************************************************
 * $RCSfile: QueryNumberFieldstartPanel.java,v $  $Revision: 1.3.2.4 $  $Date: 2009/06/12 05:25:15 $
 *
 * $Log: QueryNumberFieldstartPanel.java,v $
 * Revision 1.3.2.4  2009/06/12 05:25:15  wangqi
 * *** empty log message ***
 *
 * Revision 1.3.2.3  2008/11/05 05:21:00  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2008/09/16 07:11:36  wangqi
 * *** empty log message ***
 *
 * Revision 1.1  2008/09/01 07:38:11  wangqi
 * *** empty log message ***
 *
 * Revision 1.4  2008/06/11 16:40:34  wangqi
 * *** empty log message ***
 *
 * Revision 1.3  2007/11/28 05:35:19  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2007/05/31 07:38:18  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:01:07  qilin
 * no message
 *
 * Revision 1.3  2007/03/28 09:51:09  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 05:14:30  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/