/**************************************************************************
 * $RCSfile: UIColorPanel_List.java,v $  $Revision: 1.5.2.1 $  $Date: 2008/06/11 11:41:19 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentslist;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import smartx.framework.common.ui.*;
import smartx.framework.metadata.ui.componentscard.*;
import smartx.framework.metadata.vo.*;


public class UIColorPanel_List extends JPanel implements INovaCompent {

    private static final long serialVersionUID = 1L;

    Pub_Templet_1_ItemVO templetItemVO = null; //

    private String key = null;

    private String name = null;

    private int li_textfield_width = 150 - 18;

    private JTextField textField = null;

    JButton btn_ref = null;

    private boolean isNeed = false;

    public UIColorPanel_List(Pub_Templet_1_ItemVO _templetVO) {
        this.templetItemVO = _templetVO; //

        this.key = templetItemVO.getItemkey(); //
        this.name = templetItemVO.getItemname(); //
        this.li_textfield_width = templetItemVO.getCardwidth().intValue() - 18;
        initialize();
    }

    public UIColorPanel_List(String _key, String _name, String _refSQL) {
        this.key = _key; //
        this.name = _name; //

        initialize();
    }

    public UIColorPanel_List(String _key, String _name, String _refSQL, boolean isNeed) {
        this.key = _key; //
        this.name = _name; //
        this.isNeed = isNeed;
        initialize();
    }

    public Pub_Templet_1_ItemVO getTempletItemVO() {
		return this.templetItemVO;
	}
    
    private void initialize() {
        //this.setPreferredSize(new Dimension(li_width_all, 20));
        this.setLayout(new BorderLayout());

        textField = new JTextField();
        btn_ref = new JButton(UIUtil.getImage("images/platform/colorchoose.gif"));
        btn_ref.setPreferredSize(new Dimension(16, 16)); //按扭的宽度与高度
        btn_ref.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onButtonClicked();
            }

        });

        this.add(textField, BorderLayout.CENTER);
        this.add(btn_ref, BorderLayout.EAST); //
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
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
        Color selectedColor = JColorChooser.showDialog(this, "颜色选择器", Color.WHITE);
        if (selectedColor != null) {
            String color = "#" + getString(selectedColor.getRed()) + getString(selectedColor.getGreen()) +
                getString(selectedColor.getBlue());
            this.textField.setText(color);
            this.textField.setForeground(selectedColor);
        }
    }

    public JButton getBtn_ref() {
        return btn_ref;
    }

    public String getValue() {
        return textField.getText();
    }
    
    //add by James.W
	//TODO 获得界面选择真实值的处理
	/**
	 * 获得界面选择的真实值
	 * @return String value
	 * @deprecated 本方法新增加，首先满足了日历的使用，其他类型的数据需要以后支持
	 */
    public String getInputValue() {
		return null;		
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

    public Object getObject() {
        return getValue();
    }

    public void setObject(Object _obj) {
        setValue( (String) _obj);
    }

    public JLabel getLabel() {
        // TODO Auto-generated method stub
        return null;
    }

    public void focus() {
        textField.requestFocus();
        textField.requestFocusInWindow();
    }
}
/**************************************************************************
 * $RCSfile: UIColorPanel_List.java,v $  $Revision: 1.5.2.1 $  $Date: 2008/06/11 11:41:19 $
 *
 * $Log: UIColorPanel_List.java,v $
 * Revision 1.5.2.1  2008/06/11 11:41:19  wangqi
 * *** empty log message ***
 *
 * Revision 1.5  2007/11/28 05:35:28  wangqi
 * *** empty log message ***
 *
 * Revision 1.4  2007/05/31 07:38:22  qilin
 * code format
 *
 * Revision 1.3  2007/05/22 07:58:47  qilin
 * no message
 *
 * Revision 1.2  2007/05/22 02:03:59  sunxb
 * *** empty log message ***
 *
 * Revision 1.1  2007/05/17 06:01:59  qilin
 * no message
 *
 * Revision 1.4  2007/03/28 10:52:14  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/03/28 09:51:08  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 05:05:41  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
