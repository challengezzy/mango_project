/**************************************************************************
 * $RCSfile: UICalculatorPanel.java,v $  $Revision: 1.3.2.2 $  $Date: 2008/11/05 05:21:07 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentscard;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import smartx.framework.common.ui.UIUtil;
import smartx.framework.metadata.vo.*;


public class UICalculatorPanel extends AbstractNovaComponent implements INovaCompent {
    private static final long serialVersionUID = 1L;
    protected String REF_IMAGE="images/platform/calculator.jpg"; //点选参照按钮图标
    protected int REF_WIDTH=40;   //点选参照按钮宽度
    protected int REF_HEIGHT=20;  //点选参照按钮高度
    protected int CALCULATOR_WIDTH=this.FIELD_WIDTH-this.REF_WIDTH;
    protected JTextField textField = null;
    protected JButton btn_ref = null;
    //protected Point origin = new Point();

    public UICalculatorPanel(Pub_Templet_1_ItemVO _templetVO) {
        this._vo = _templetVO; //

        this.key = this._vo.getItemkey(); //
        this.name = this._vo.getItemname(); //
        this.FIELD_WIDTH=this._vo.getCardwidth().intValue();
        this.CALCULATOR_WIDTH = this.FIELD_WIDTH - REF_WIDTH;
        init();
    }

    public UICalculatorPanel(String _key, String _name) {
        this.key = _key; //
        this.name = _name; //

        init();
    }

    public UICalculatorPanel(String _key, String _name, boolean isNeed) {
        this.key = _key; //
        this.name = _name; //
        this.isNeed = isNeed;
        init();
    }
    
    //Override
	protected JComponent[] getFieldComponents() {
    	textField = new JTextField();
        textField.setPreferredSize(new Dimension(this.CALCULATOR_WIDTH, this.FIELD_HEIGHT));
        textField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
            }

            public void focusLost(FocusEvent e) {
            }
        });

        btn_ref = new JButton("计算器");
        btn_ref.setFont(new Font("宋体", Font.PLAIN, 12));
        btn_ref.setPreferredSize(new Dimension(this.REF_WIDTH, this.REF_HEIGHT)); //按扭的宽度与高度
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
        Color selectedColor = JColorChooser.showDialog(this, "颜色选择器", Color.BLACK);
        this.textField.setBackground(selectedColor);
        this.textField.setText("" + selectedColor.toString());
    }

    public JButton getBtn_ref() {
        return btn_ref;
    }

    public void setValue(String _value) {
        textField.setText(_value); //
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
	//TODO 对于计算框获得界面选择真实值的处理
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
 * $RCSfile: UICalculatorPanel.java,v $  $Revision: 1.3.2.2 $  $Date: 2008/11/05 05:21:07 $
 *
 * $Log: UICalculatorPanel.java,v $
 * Revision 1.3.2.2  2008/11/05 05:21:07  wangqi
 * *** empty log message ***
 *
 * Revision 1.1  2008/09/01 07:38:10  wangqi
 * *** empty log message ***
 *
 * Revision 1.4  2008/06/11 16:40:34  wangqi
 * *** empty log message ***
 *
 * Revision 1.3  2007/11/28 05:35:20  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2007/05/31 07:38:19  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:01:06  qilin
 * no message
 *
 * Revision 1.4  2007/03/28 09:51:09  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/03/09 01:49:36  sunxf
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 05:14:29  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/