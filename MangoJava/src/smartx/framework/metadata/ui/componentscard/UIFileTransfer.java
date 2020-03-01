/**************************************************************************
 * $RCSfile: UIFileTransfer.java,v $  $Revision: 1.5.2.2 $  $Date: 2008/11/05 05:21:06 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentscard;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import smartx.framework.common.ui.*;
import smartx.framework.metadata.vo.*;



public class UIFileTransfer extends AbstractNovaComponent implements INovaCompent {
    private static final long serialVersionUID = -8620859341820273023L;

    protected String REF_IMAGE="images/platform/filepath.gif"; //点选参照按钮图标
    protected int REF_WIDTH=18;   //点选参照按钮宽度
    protected int REF_HEIGHT=18;  //点选参照按钮高度
    protected int TRANS_WIDTH=this.FIELD_WIDTH-this.REF_WIDTH;


    protected JTextField textField = null;
    protected String code = null;

    protected JButton btn_ref = null;

    protected UIFileTransferDialog dialog = null;

    public UIFileTransfer(Pub_Templet_1_ItemVO _templetVO) {
        this._vo = _templetVO; //

        this.key = this._vo.getItemkey(); //
        this.name = this._vo.getItemname(); //
        this.FIELD_WIDTH = this._vo.getCardwidth().intValue();
        this.TRANS_WIDTH=this.FIELD_WIDTH-this.REF_WIDTH;
        init();
    }
    
    public UIFileTransfer(String name,String key){
    	this.name=name;
    	this.key=key;
    	init();
    }
    
    //Override
	protected JComponent[] getFieldComponents() {
    	textField = new JTextField();
        textField.setPreferredSize(new Dimension(this.TRANS_WIDTH, this.FIELD_HEIGHT));
        textField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
            }

            public void focusLost(FocusEvent e) {
            }
        });

        btn_ref = new JButton(UIUtil.getImage(this.REF_IMAGE));
        btn_ref.setToolTipText("选择路径……");
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

    private void onButtonClicked() {
        getUIFileTransferDialog();
    }

    private UIFileTransferDialog getUIFileTransferDialog() {
        dialog = new UIFileTransferDialog(this, this._vo);
        return dialog;
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
        return textField.getText(); //
    }
    
    //add by James.W
	//TODO 对于文件传送框获得界面选择真实值的处理
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

    public String getCode() {
        return this.code;
    }

    public void setCode(String _code) {
        this.code = _code;
    }

    public void setObject(Object _obj) {
        setValue(_obj == null ? "" : (String) _obj);
    }

    public void focus() {
        textField.requestFocus();
        textField.requestFocusInWindow();
    }
}
/**************************************************************************
 * $RCSfile: UIFileTransfer.java,v $  $Revision: 1.5.2.2 $  $Date: 2008/11/05 05:21:06 $
 *
 * $Log: UIFileTransfer.java,v $
 * Revision 1.5.2.2  2008/11/05 05:21:06  wangqi
 * *** empty log message ***
 *
 * Revision 1.1  2008/09/01 07:38:11  wangqi
 * *** empty log message ***
 *
 * Revision 1.6  2008/06/11 16:40:35  wangqi
 * *** empty log message ***
 *
 * Revision 1.5  2007/11/28 05:35:19  wangqi
 * *** empty log message ***
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
 * Revision 1.2  2007/01/30 05:14:29  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
