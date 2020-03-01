/**************************************************************************
 * $RCSfile: QueryUITimeSetendPanel.java,v $  $Revision: 1.5.2.2 $  $Date: 2008/11/05 05:21:04 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentscard;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import smartx.framework.common.ui.*;
import smartx.framework.metadata.vo.*;


public class QueryUITimeSetendPanel extends AbstractNovaComponent implements INovaCompent {
    private static final long serialVersionUID = 1L;

    protected String REF_IMAGE="images/platform/time.jpg"; //点选参照按钮图标
    protected int REF_WIDTH=18;   //点选参照按钮宽度
    protected int REF_HEIGHT=18;  //点选参照按钮高度
    protected int DATE_WIDTH = this.FIELD_WIDTH - REF_WIDTH;//日期框控件宽度
    protected JTextField textField = null;
    protected TimePicker tp;
    protected JButton btn_ref = null;

    public QueryUITimeSetendPanel(Pub_Templet_1_ItemVO _templetVO) {
        this._vo = _templetVO;
        this.key = this._vo.getItemkey() + "END";
        this.name = "终止" +this._vo.getItemname();
        this.FIELD_WIDTH = this._vo.getCardwidth().intValue();
        this.DATE_WIDTH=this.FIELD_WIDTH-this.REF_WIDTH;
        init();
    }

    

    public QueryUITimeSetendPanel(String _key, String _name) {
        this.key = _key + "END";
        this.name ="终止" + _name;        
        init();
    }
    
    

    //Override
	protected JComponent[] getFieldComponents() {
    	textField = new JTextField();
        textField.setPreferredSize(new Dimension(this.DATE_WIDTH, this.FIELD_HEIGHT));
        textField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
            }

            public void focusLost(FocusEvent e) {
            }
        });

        btn_ref = new JButton(UIUtil.getImage(this.REF_IMAGE));
        btn_ref.setPreferredSize(new Dimension(this.REF_WIDTH,this.REF_HEIGHT));
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

    public void setValue(String _value) {
        textField.setText(_value);
    }

    public void setText(String _text) {
        textField.setText(_text);
    }

    public void reset() {
        textField.setText("");
    }

    public String getValue() {
        return textField.getText();
    }
    
    //add by James.W
	//TODO 对于结束时间框获得界面选择真实值的处理
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
        setValue( (String) _obj);
    }

    public void focus() {
        textField.requestFocus();
        textField.requestFocusInWindow();
    }

    public void setEditable(boolean _bo) {
        // TODO Auto-generated method stub

    }
}
/**************************************************************************
 * $RCSfile: QueryUITimeSetendPanel.java,v $  $Revision: 1.5.2.2 $  $Date: 2008/11/05 05:21:04 $
 *
 * $Log: QueryUITimeSetendPanel.java,v $
 * Revision 1.5.2.2  2008/11/05 05:21:04  wangqi
 * *** empty log message ***
 *
 * Revision 1.1  2008/09/01 07:38:11  wangqi
 * *** empty log message ***
 *
 * Revision 1.6  2008/06/11 16:40:34  wangqi
 * *** empty log message ***
 *
 * Revision 1.5  2007/11/28 05:35:19  wangqi
 * *** empty log message ***
 *
 * Revision 1.4  2007/05/31 07:38:18  qilin
 * code format
 *
 * Revision 1.3  2007/05/22 07:58:46  qilin
 * no message
 *
 * Revision 1.2  2007/05/22 02:03:34  sunxb
 * *** empty log message ***
 *
 * Revision 1.1  2007/05/17 06:01:07  qilin
 * no message
 *
 * Revision 1.3  2007/03/28 09:51:09  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 05:14:31  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
