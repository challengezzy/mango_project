/**************************************************************************
 * $RCSfile: UIColorPanel.java,v $  $Revision: 1.6.2.2 $  $Date: 2008/11/05 05:21:00 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentscard;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import smartx.framework.common.ui.*;
import smartx.framework.metadata.vo.*;


public class UIColorPanel extends AbstractNovaComponent implements INovaCompent {
    private static final long serialVersionUID = 1L;

    
    protected String REF_IMAGE="images/platform/colorchoose.gif"; //点选参照按钮图标
    protected int REF_WIDTH=18;   //点选参照按钮宽度
    protected int REF_HEIGHT=18;  //点选参照按钮高度
    protected int COLOR_WIDTH = this.FIELD_WIDTH - REF_WIDTH;

    protected JTextField textField = null;

    protected JButton btn_ref = null;

    protected Point origin = new Point();

    public UIColorPanel(Pub_Templet_1_ItemVO _templetVO) {
        this._vo = _templetVO; //

        this.key = this._vo.getItemkey(); //
        this.name = this._vo.getItemname(); //
        this.FIELD_WIDTH = this._vo.getCardwidth().intValue();
        this.COLOR_WIDTH=FIELD_WIDTH-REF_WIDTH;
        init();
    }

    public UIColorPanel(String _key, String _name) {
        this.key = _key; //
        this.name = _name; //
        init();
    }

    public UIColorPanel(String _key, String _name,boolean isNeed) {
        this.key = _key; //
        this.name = _name; //
        this.isNeed = isNeed;
        init();
    }
    
    //Override
	protected JComponent[] getFieldComponents() {
    	textField = new JTextField();
        textField.setPreferredSize(new Dimension(this.COLOR_WIDTH, this.FIELD_HEIGHT));
        textField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
            }

            public void focusLost(FocusEvent e) {
            }
        });

        btn_ref = new JButton(UIUtil.getImage(this.REF_IMAGE));
        btn_ref.setToolTipText("更改颜色");
        btn_ref.setFont(new Font("宋体", Font.PLAIN, 12));
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

    private String getChar(int value) {
        if (value < 10) {
            return "" + value;
        }
        switch (value) {
            case 10:
                return "A";
            case 11:
                return "B";
            case 12:
                return "C";
            case 13:
                return "D";
            case 14:
                return "E";
            case 15:
                return "F";
            default:
                return null;
        }
    }

    private String getString(int color) {
        int temp_first = color / 16;
        int temp_second = color % 16;

        return getChar(temp_first) + getChar(temp_second);
    }

    private void onButtonClicked() {
        Color selectedColor = JColorChooser.showDialog(this, "颜色选择器",
            Color.WHITE);
        if (selectedColor != null) {
            String color = "#" + getString(selectedColor.getRed())
                + getString(selectedColor.getGreen())
                + getString(selectedColor.getBlue());
            this.textField.setText(color);
            // this.textField.setForeground(selectedColor);
        }
    }

    public JButton getBtn_ref() {
        return btn_ref;
    }

    public void setValue(String _value) {
        textField.setText(_value); //
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
	//TODO 对于颜色框获得界面选择真实值的处理
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
 * $RCSfile: UIColorPanel.java,v $  $Revision: 1.6.2.2 $  $Date: 2008/11/05 05:21:00 $
 *
 * $Log: UIColorPanel.java,v $
 * Revision 1.6.2.2  2008/11/05 05:21:00  wangqi
 * *** empty log message ***
 *
 * Revision 1.1  2008/09/01 07:38:11  wangqi
 * *** empty log message ***
 *
 * Revision 1.7  2008/06/11 16:40:36  wangqi
 * *** empty log message ***
 *
 * Revision 1.6  2007/11/28 05:35:19  wangqi
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
