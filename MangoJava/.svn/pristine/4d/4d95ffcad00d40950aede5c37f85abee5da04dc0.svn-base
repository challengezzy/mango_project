/**************************************************************************
 * $RCSfile: UITimeSetPanel.java,v $  $Revision: 1.6.2.5 $  $Date: 2009/05/22 02:38:55 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentscard;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;

import smartx.framework.common.ui.*;
import smartx.framework.metadata.vo.*;


public class UITimeSetPanel extends AbstractNovaComponent implements INovaCompent {
    private static final long serialVersionUID = 1L;
    private static SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:SS");
    
    protected String REF_IMAGE="images/platform/time.gif"; //点选参照按钮图标
    protected int REF_WIDTH=18;   //点选参照按钮宽度
    protected int REF_HEIGHT=18;  //点选参照按钮高度
    protected int TIME_WIDTH=this.FIELD_WIDTH-this.REF_WIDTH;
    protected JTextField textField = null;
    protected TimePicker tp;
    protected JButton btn_ref = null;
    //Point origin = new Point();

    public UITimeSetPanel(Pub_Templet_1_ItemVO _templetVO) {
        this._vo = _templetVO; //

        this.key = this._vo.getItemkey(); //
        this.name = this._vo.getItemname(); //
        this.FIELD_WIDTH = this._vo.getCardwidth().intValue();
        this.TIME_WIDTH=this.FIELD_WIDTH - this.REF_WIDTH;
        init();
    }

    public UITimeSetPanel(String _key, String _name) {
        this.key = _key; //
        this.name = _name; //
        init();
    }

    public UITimeSetPanel(String _key, String _name, boolean isNeed) {
        this.key = _key; //
        this.name = _name; //
        this.isNeed = isNeed;
        init();
    }
    
	protected JComponent[] getFieldComponents() {
    	textField = new JTextField();
    	textField.setEditable(false);
        textField.setPreferredSize(new Dimension(this.TIME_WIDTH, this.FIELD_HEIGHT));
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

        tp = new TimePicker(this, "时间设置", null);
        String dt = tp.getTimeStr();
        if (dt != null) {
            textField.setText(dt);
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
	//TODO 对于时间设置框获得界面选择真实值的处理
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
     * 设置控件取值
     * 
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
    public void setValue(String _value) {
        textField.setText(_value); //
    }
    
    public void setValue(Date _v) {
    	setValue(sdf.format(_v));   
    	
    }

    public void focus() {
        textField.requestFocus();
        textField.requestFocusInWindow();
    }
}
/**************************************************************************
 * $RCSfile: UITimeSetPanel.java,v $  $Revision: 1.6.2.5 $  $Date: 2009/05/22 02:38:55 $
 *
 * $Log: UITimeSetPanel.java,v $
 * Revision 1.6.2.5  2009/05/22 02:38:55  wangqi
 * *** empty log message ***
 *
 * Revision 1.6.2.4  2008/12/09 07:40:07  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2008/11/04 13:58:04  wangqi
 * *** empty log message ***
 *
 * Revision 1.1  2008/09/01 07:38:11  wangqi
 * *** empty log message ***
 *
 * Revision 1.7  2008/06/11 16:40:36  wangqi
 * *** empty log message ***
 *
 * Revision 1.6  2007/11/28 05:35:18  wangqi
 * *** empty log message ***
 *
 * Revision 1.5  2007/07/23 11:34:28  sunxf
 * MR#:Nova20-15
 *
 * Revision 1.4  2007/05/31 07:38:19  qilin
 * code format
 *
 * Revision 1.3  2007/05/22 07:58:46  qilin
 * no message
 *
 * Revision 1.2  2007/05/22 02:03:34  sunxb
 * *** empty log message ***
 *
 * Revision 1.1  2007/05/17 06:01:06  qilin
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
