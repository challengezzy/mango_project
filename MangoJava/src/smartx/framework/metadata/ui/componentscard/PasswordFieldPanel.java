/**************************************************************************
 * $RCSfile: PasswordFieldPanel.java,v $  $Revision: 1.4.2.2 $  $Date: 2008/11/05 05:21:06 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentscard;

import java.awt.*;

import javax.swing.*;

import smartx.framework.common.ui.*;
import smartx.framework.metadata.ui.componentscard.QueryNumberFieldendPanel.FormattedTextFieldVerifier;
import smartx.framework.metadata.ui.componentscard.QueryNumberFieldendPanel.IntegerDoucument;
import smartx.framework.metadata.vo.*;


public class PasswordFieldPanel extends AbstractNovaComponent implements INovaCompent {
    private static final long serialVersionUID = -3535276535045219676L;

    protected JPasswordField textField = null;
    protected boolean isEditable = true;

    public PasswordFieldPanel(Pub_Templet_1_ItemVO _templetVO) {
        this._vo = _templetVO;

        this.key = this._vo.getItemkey();
        this.name = this._vo.getItemname();
        this.FIELD_WIDTH = this._vo.getCardwidth().intValue(); // 设置宽度
        init();

    }

    public PasswordFieldPanel(String _key, String _name) {
        this.key = _key;
        this.name = _name;
        init();
    }

    public PasswordFieldPanel(String _key, String _name, boolean isNeed) {
        this.key = _key;
        this.name = _name;
        this.isNeed = isNeed;
        init();
    }

    public PasswordFieldPanel(String _key, String _name, boolean isNeed,
                              boolean isEditable) {
        this.key = _key;
        this.name = _name;
        this.isNeed = isNeed;
        this.isEditable = isEditable;
        init();
    }

    public PasswordFieldPanel(String _key, String _name, int _width) {
        this.key = _key;
        this.name = _name;
        this.FIELD_WIDTH = _width;
        init();
    }

    public PasswordFieldPanel(String _key, String _name, int _width,
                              boolean isNeed) {
        this.key = _key;
        this.name = _name;
        this.FIELD_WIDTH = _width;
        this.isNeed = isNeed;
        init();
    }
    
    
    //Override
	protected JComponent[] getFieldComponents() {
    	textField = new JPasswordField();
        if (!isEditable) {
            textField.setText("序列自动增长");
            textField.setEditable(false);
        }
        textField.setPreferredSize(new Dimension(this.FIELD_WIDTH, this.FIELD_HEIGHT));
        /**
         * TODO 增加编辑事件，填入和清空的时候出发，参考UIRefPanel
         *   fireValueChanged 
         */
		return new JComponent[]{textField};
	}
    
    

    
   

    public JTextField getTextField() {
        return textField;
    }

    public String getValue() {
        return new String(textField.getPassword());
    }
    
    //add by James.W
	//TODO 对于密码框获得界面选择真实值的处理
	/**
	 * 获得界面选择的真实值
	 * @return String value
	 * @deprecated 本方法新增加，首先满足了日历的使用，其他类型的数据需要以后支持
	 */
    public String getInputValue() {
		return null;		
	}

    
    public String getText() {
        return new String(textField.getPassword());
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

    public void setEditable(boolean _bo) {
        textField.setEditable(_bo);
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
}
/**************************************************************************
 * $RCSfile: PasswordFieldPanel.java,v $  $Revision: 1.4.2.2 $  $Date: 2008/11/05 05:21:06 $
 *
 * $Log: PasswordFieldPanel.java,v $
 * Revision 1.4.2.2  2008/11/05 05:21:06  wangqi
 * *** empty log message ***
 *
 * Revision 1.1  2008/09/01 07:38:10  wangqi
 * *** empty log message ***
 *
 * Revision 1.5  2008/06/11 16:40:35  wangqi
 * *** empty log message ***
 *
 * Revision 1.4  2007/11/28 05:35:18  wangqi
 * *** empty log message ***
 *
 * Revision 1.3  2007/07/23 11:34:29  sunxf
 * MR#:Nova20-15
 *
 * Revision 1.2  2007/05/31 07:38:17  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:01:07  qilin
 * no message
 *
 * Revision 1.5  2007/03/28 09:51:09  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/03/09 01:49:36  sunxf
 * *** empty log message ***
 *
 * Revision 1.3  2007/01/31 08:58:07  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 05:14:31  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/