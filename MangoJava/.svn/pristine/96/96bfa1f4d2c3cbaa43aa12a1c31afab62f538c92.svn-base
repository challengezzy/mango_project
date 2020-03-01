/**************************************************************************
 * $RCSfile: UIImagePanel_list.java,v $  $Revision: 1.5.2.1 $  $Date: 2008/06/11 11:41:18 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentslist;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import smartx.framework.common.ui.*;
import smartx.framework.metadata.ui.componentscard.*;
import smartx.framework.metadata.vo.*;


public class UIImagePanel_list extends JPanel implements INovaCompent {
    private static final long serialVersionUID = 1L;

    Pub_Templet_1_ItemVO templetItemVO = null; //

    private AbstractCellEditor cellEditor = null;

    private String key = null;

    private String name = null;

    private JTextField textField = null;

    JButton btn_ref = null;

    public UIImagePanel_list(Pub_Templet_1_ItemVO _templetVO) {
        this.templetItemVO = _templetVO; //

        this.key = templetItemVO.getItemkey(); //
        this.name = templetItemVO.getItemname(); //
        initialize();
    }

    public UIImagePanel_list(Pub_Templet_1_ItemVO _templetVO, int _currrow, AbstractCellEditor _cellEditor) {
        this.templetItemVO = _templetVO; //
        this.cellEditor = _cellEditor;
        this.key = templetItemVO.getItemkey(); //
        this.name = templetItemVO.getItemname(); //
        initialize();
    }

    public UIImagePanel_list(String _key, String _name, String _refSQL) {
        this.key = _key; //
        this.name = _name; //
        initialize();
    }

    public UIImagePanel_list(String _key, String _name, String _refSQL, boolean isNeed) {
        this.key = _key; //
        this.name = _name; //
        initialize();
    }

    public Pub_Templet_1_ItemVO getTempletItemVO() {
		return this.templetItemVO;
	}
    
    private void initialize() {
        //this.setPreferredSize(new Dimension(li_width_all, 20));
        this.setLayout(new BorderLayout());

        textField = new JTextField();

        btn_ref = new JButton(UIUtil.getImage("images/platform/pic2.gif"));
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
        UIPhotoRefDialog dlg_photo = new UIPhotoRefDialog(this, "图片选择", textField.getText());
        dlg_photo.setVisible(true);
        if (dlg_photo.getCloseType() == 0) {
            String str_name = dlg_photo.getSelectedIcon();
            if (str_name != null && !str_name.equals("")) {
                textField.setText(str_name);
            }
        }
        if (cellEditor != null) {
            cellEditor.stopCellEditing();
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
 * $RCSfile: UIImagePanel_list.java,v $  $Revision: 1.5.2.1 $  $Date: 2008/06/11 11:41:18 $
 *
 * $Log: UIImagePanel_list.java,v $
 * Revision 1.5.2.1  2008/06/11 11:41:18  wangqi
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
 * Revision 1.2  2007/01/30 05:05:40  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
