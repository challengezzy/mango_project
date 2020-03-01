/**************************************************************************
 * $RCSfile: UICheckBoxPanel.java,v $  $Revision: 1.4.2.3 $  $Date: 2008/11/05 05:21:01 $
 **************************************************************************/

package smartx.framework.metadata.ui.componentscard;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import smartx.framework.common.ui.*;
import smartx.framework.metadata.vo.*;


public class UICheckBoxPanel extends AbstractNovaComponent implements INovaCompent {

    private static final long serialVersionUID = 1L;

    private JCheckBox checkBox = null;
    
    public UICheckBoxPanel(Pub_Templet_1_ItemVO _templetVO) {
        this._vo = _templetVO; //

        this.key = this._vo.getItemkey(); //
        this.name = this._vo.getItemname(); //
        this.FIELD_WIDTH = this._vo.getCardwidth().intValue();
        init();
    }

    public UICheckBoxPanel(String _key, String _name) {
        this.key = _key; //
        this.name = _name; //
        init();
    }

    public UICheckBoxPanel(String _key, String _name, boolean isNeed) {
        this.key = _key; //
        this.name = _name; //
        this.isNeed = isNeed;
        init();
    }
    
    //Override
	protected JComponent[] getFieldComponents() {
    	checkBox = new JCheckBox();

        // checkBox.setToolTipText("选择");
        // checkBox.setFont(new Font("宋体",Font.PLAIN,12));
        checkBox.setBackground(new Color(255, 255, 255));
        checkBox.setHorizontalAlignment(JCheckBox.CENTER);
        checkBox.setPreferredSize(new Dimension(this.FIELD_WIDTH, this.FIELD_HEIGHT)); // 按扭的宽度与高度
        checkBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                onCheckBoxClicked();
            }

        });
        /**
         * TODO 增加编辑事件，填入和清空的时候出发，参考UIRefPanel
         *   fireValueChanged 
         */
		return new JComponent[]{checkBox};
	}
    
    
    public JCheckBox getCheckBox() {
        return checkBox;
    }

    private void onCheckBoxClicked() {
    }

    public void setValue(String _value) {
        if (_value.equals("Y")) {
            this.checkBox.setSelected(true);
        } else {
            this.checkBox.setSelected(false);
        }
    }

    public void reset() {
        checkBox.setSelected(false); //
    }

    public void setEditable(boolean _bo) {
        checkBox.setEnabled(_bo); //
    }

    public String getValue() {
        if (checkBox.isSelected()) {
            return "Y";
        } else {
            return "N";
        }
    }
    
    //add by James.W
	//TODO 对于选择框获得界面选择真实值的处理
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
            String value = _obj.toString();

            if (value != null && value.equalsIgnoreCase("Y")) {
                this.checkBox.setSelected(true);
            } else {
                this.checkBox.setSelected(false);
            }
        }
    }

    public void focus() {
        checkBox.requestFocus();
        checkBox.requestFocusInWindow();
    }
}
/*******************************************************************************
 * $RCSfile: UICheckBoxPanel.java,v $ $Revision: 1.4.2.3 $ $Date: 2007/01/30
 * 05:14:30 $
 *
 * $Log: UICheckBoxPanel.java,v $
 * Revision 1.4.2.3  2008/11/05 05:21:01  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2008/09/16 07:11:36  wangqi
 * *** empty log message ***
 *
 * Revision 1.1  2008/09/01 07:38:11  wangqi
 * *** empty log message ***
 *
 * Revision 1.5  2008/06/11 16:40:35  wangqi
 * *** empty log message ***
 *
 * Revision 1.4  2007/11/28 05:35:18  wangqi
 * *** empty log message ***
 *
 * Revision 1.3  2007/07/23 11:34:28  sunxf
 * MR#:Nova20-15
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
 * Revision 1.2 2007/01/30 05:14:30 lujian ***
 * empty log message ***
 *
 *
 ******************************************************************************/
