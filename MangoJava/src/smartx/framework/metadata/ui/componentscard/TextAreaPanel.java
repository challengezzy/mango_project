/**************************************************************************
 * $RCSfile: TextAreaPanel.java,v $  $Revision: 1.4.2.3 $  $Date: 2008/11/05 07:47:03 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentscard;

import java.awt.*;

import javax.swing.*;

import smartx.framework.common.ui.*;
import smartx.framework.metadata.vo.*;


public class TextAreaPanel extends AbstractNovaComponent implements INovaCompent {
    /**
     *
     */
    private static final long serialVersionUID = 1523290372757996226L;

    protected JTextArea area = null;
    protected int row = 10;
    protected int col = 10;    

    public TextAreaPanel(Pub_Templet_1_ItemVO _templetVO) {
        this._vo = _templetVO;

        this.key = this._vo.getItemkey();
        this.name = this._vo.getItemname();
        this.FIELD_HEIGHT = 75; //注意：重载父类的变量。高75,即可视4行数据
        this.FIELD_WIDTH = this._vo.getCardwidth().intValue(); // 设置宽度
        init();

    }

    public TextAreaPanel(String key, String name) {
    	this.key = key;
        this.name = name;
        this.FIELD_HEIGHT = 75; //注意：重载父类的变量。高75,即可视4行数据
        init();
    }

    public TextAreaPanel(String key, String name, int r, int c) {
        this.key = key;
        this.name = name;
        this.row = r;
        this.col = c;
        this.FIELD_HEIGHT = 75; //注意：重载父类的变量。高75,即可视4行数据
        init();
    }
    
    //Override
	protected JComponent[] getFieldComponents() {
    	area = new JTextArea(row, col);
        area.setBackground(new Color(255, 255, 255));
        JScrollPane jsp = new JScrollPane(area);
        jsp.setPreferredSize(new Dimension(this.FIELD_WIDTH,this.FIELD_HEIGHT));
        jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jsp.getViewport().add(area);
        /**
         * TODO 增加编辑事件，填入和清空的时候出发，参考UIRefPanel
         *   fireValueChanged 
         */
		return new JComponent[]{jsp};
	}

    public String getDataType() {
        return null;
    }

    public String getValue() {
        return area.getText();
    }
    //add by James.W
	//TODO 对于多行文本框获得界面选择真实值的处理
	/**
	 * 获得界面选择的真实值
	 * @return String value
	 * @deprecated 本方法新增加，首先满足了日历的使用，其他类型的数据需要以后支持
	 */
    public String getInputValue() {
		return null;		
	}

    public void setText(String str) {
        this.area.setText(str);
    }

    public void setValue(String _value) {
        area.setText(_value);
    }

    public void reset() {
        area.setText(""); //
    }

    public void setEditable(boolean _bo) {
        area.setEditable(_bo);

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
        area.requestFocus();
        area.requestFocusInWindow();
    }
}
/**************************************************************************
 * $RCSfile: TextAreaPanel.java,v $  $Revision: 1.4.2.3 $  $Date: 2008/11/05 07:47:03 $
 *
 * $Log: TextAreaPanel.java,v $
 * Revision 1.4.2.3  2008/11/05 07:47:03  wangqi
 * *** empty log message ***
 *
 * Revision 1.4.2.2  2008/11/05 05:21:00  wangqi
 * *** empty log message ***
 *
 * Revision 1.1  2008/09/01 07:38:11  wangqi
 * *** empty log message ***
 *
 * Revision 1.5  2008/06/11 16:40:35  wangqi
 * *** empty log message ***
 *
 * Revision 1.4  2007/11/28 05:35:20  wangqi
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
 * Revision 1.4  2007/03/28 09:51:08  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/03/09 01:49:36  sunxf
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 05:14:30  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/