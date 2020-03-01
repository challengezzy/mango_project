/**************************************************************************
 * $RCSfile: UIFilePathPanel_List.java,v $  $Revision: 1.5.2.1 $  $Date: 2008/06/11 11:41:18 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentslist;

import java.io.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import smartx.framework.common.ui.*;
import smartx.framework.metadata.ui.componentscard.*;
import smartx.framework.metadata.vo.*;


public class UIFilePathPanel_List extends JPanel implements INovaCompent {
    private static final long serialVersionUID = 1L;

    Pub_Templet_1_ItemVO templetItemVO = null; //

    private String key = null;

    private String name = null;

    private int li_textfield_width = 150 - 18;

    private JTextField textField = null;

    JButton btn_ref = null;

    private boolean isNeed = false;

    public UIFilePathPanel_List(Pub_Templet_1_ItemVO _templetVO) {
        this.templetItemVO = _templetVO; //

        this.key = templetItemVO.getItemkey(); //
        this.name = templetItemVO.getItemname(); //
        this.li_textfield_width = templetItemVO.getCardwidth().intValue() - 18;
        initialize();
    }

    public UIFilePathPanel_List(String _key, String _name, String _refSQL) {
        this.key = _key; //
        this.name = _name; //
        initialize();
    }

    public UIFilePathPanel_List(String _key, String _name, String _refSQL, boolean isNeed) {
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

        btn_ref = new JButton(UIUtil.getImage("images/platform/filepath.gif"));
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

    private void onButtonClicked() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        int result = chooser.showOpenDialog(this);

        if (result == 0 && chooser.getSelectedFile() != null) {
            String filePath = chooser.getSelectedFile().getPath();
            this.textField.setText(filePath);
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
 * $RCSfile: UIFilePathPanel_List.java,v $  $Revision: 1.5.2.1 $  $Date: 2008/06/11 11:41:18 $
 *
 * $Log: UIFilePathPanel_List.java,v $
 * Revision 1.5.2.1  2008/06/11 11:41:18  wangqi
 * *** empty log message ***
 *
 * Revision 1.5  2007/11/28 05:35:29  wangqi
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
 * Revision 1.2  2007/01/30 05:05:40  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
