/**************************************************************************
 * $RCSfile: ComBoxPanel.java,v $  $Revision: 1.6.2.6 $  $Date: 2009/07/21 08:41:08 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentscard;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import smartx.framework.common.ui.*;
import smartx.framework.metadata.vo.*;


public class ComBoxPanel extends AbstractNovaComponent implements INovaCompent {
	private static final long serialVersionUID = 2871043456879883350L;
	protected ComBoxItemVO[] itemVOs = null;                   //下拉列表内容    
    protected JComboBox combox = null;                         //下拉框对象
    protected JButton btn_clear = null;                        //TODO 适当时候提升到父类上 清除按钮 
    protected String CLEAR_IMAGE = "images/platform/clear.gif";//TODO 适当时候提升到父类上 清除按钮宽度
    protected int CLEAR_WIDTH = 18;                            //TODO 适当时候提升到父类上 清除按钮宽度
    protected int CLEAR_HEIGHT = 18;                            //TODO 适当时候提升到父类上 清除按钮宽度
    
    protected int COMBOX_WIDTH = this.FIELD_WIDTH - CLEAR_WIDTH;//下拉框控件宽度
    
   
    protected String inputValue=null;                          //TODO  干什么的？

    //不建议使用
    public ComBoxPanel(){
    	
    }
    
    public ComBoxPanel(Pub_Templet_1_ItemVO _templetVO) {
        this._vo = _templetVO;

        this.key = this._vo.getItemkey();
        this.name = this._vo.getItemname();
        this.itemVOs = this._vo.getComBoxItemVos();
        this.FIELD_WIDTH=this._vo.getCardwidth().intValue();
        this.COMBOX_WIDTH = this.FIELD_WIDTH - CLEAR_WIDTH; //
        init();
    }

    public ComBoxPanel(String _key, String _name, ComBoxItemVO[] _itemVOs) {
        this.key = _key;
        this.name = _name;
        this.itemVOs = _itemVOs;
        init();
    }
    
    //Override
	protected JComponent[] getFieldComponents() {
    	combox = new JComboBox();
        combox.setEditable(false); //
        combox.setPreferredSize(new Dimension(this.COMBOX_WIDTH, this.FIELD_HEIGHT)); //

        combox.addItem(new ComBoxItemVO("", "", "")); //
        if (itemVOs != null) {
            for (int i = 0; i < itemVOs.length; i++) {
                combox.addItem(itemVOs[i]);
            }
        }

        btn_clear = new JButton(UIUtil.getImage(CLEAR_IMAGE)); //
        btn_clear.setPreferredSize(new Dimension(this.CLEAR_WIDTH, this.CLEAR_HEIGHT)); //
        btn_clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                combox.setSelectedIndex( -1); //

            }
        });
        /**
         * TODO 增加编辑事件，填入和清空的时候出发，参考UIRefPanel
         *   fireValueChanged 
         */
		return new JComponent[]{combox,btn_clear};
	}
    
    public JComboBox getComBox() {
        return combox;
    }
    
    public String getValue() {
        Object selectedObj = combox.getSelectedItem();
        if (selectedObj == null) {
            return null;
        }

        ComBoxItemVO vo = (ComBoxItemVO) selectedObj;
        return vo.getId();
    }
    
    
	public void setValue(String _value) {
        if (itemVOs != null) {
            for (int i = 0; i < itemVOs.length; i++) {
                if (itemVOs[i].getId().equalsIgnoreCase(_value)) {
                    getComBox().setSelectedIndex(i + 1);
                    return;
                }
            }
        }

    }
	
	//add by James.W
	//TODO 对于下拉框获得界面选择真实值的处理
	/**
	 * 获得界面选择的真实值
	 * @return String value 
	 */
    public String getInputValue() {
    	Object selectedObj = combox.getSelectedItem();
        if (selectedObj == null) {
            return null;
        }

        ComBoxItemVO vo = (ComBoxItemVO) selectedObj;
        return vo.getId();	
	}


    public void reset() {
        try {
            getComBox().setSelectedIndex( -1);
        } catch (Exception ex) {
        }

    }

    public void setEditable(boolean _bo) {
        //getComBox().setEditable(_bo);
        getComBox().setEnabled(_bo);
        getBtn_clear().setEnabled(_bo);
    }

    public Object getObject() {
        Object obj = combox.getSelectedItem();
        if (obj!=null && obj instanceof ComBoxItemVO) {
        	if(((ComBoxItemVO)obj).getId().equals("")){//是空选项
        		return null;
        	}
            return (ComBoxItemVO) obj;
        } else {
            return null;
        }
    }

    public void focus() {
        combox.requestFocus();
        combox.requestFocusInWindow();
    }

    public void setObject(Object _obj) {
        ComBoxItemVO vo = (ComBoxItemVO) _obj;
        combox.setSelectedItem(vo);
    }

    public JButton getBtn_clear() {
        return btn_clear;
    }

    public void setBtn_clear(JButton btn_clear) {
        this.btn_clear = btn_clear;
    }

    public ComBoxItemVO[] getItemVOs() {
        return itemVOs;
    }

    public void setItemVOs(ComBoxItemVO[] itemVOs) {
        this.itemVOs = itemVOs;
    }
}
/**************************************************************************
 * $RCSfile: ComBoxPanel.java,v $  $Revision: 1.6.2.6 $  $Date: 2009/07/21 08:41:08 $
 *
 * $Log: ComBoxPanel.java,v $
 * Revision 1.6.2.6  2009/07/21 08:41:08  wangqi
 * *** empty log message ***
 *
 * Revision 1.6.2.5  2008/11/20 05:32:16  wangqi
 * *** empty log message ***
 *
 * Revision 1.6.2.4  2008/11/17 06:48:04  wangqi
 * *** empty log message ***
 *
 * Revision 1.6.2.3  2008/11/05 05:21:02  wangqi
 * *** empty log message ***
 *
 * Revision 1.3  2008/09/16 07:11:36  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2008/09/08 08:10:54  wangqi
 * *** empty log message ***
 *
 * Revision 1.1  2008/09/01 07:38:11  wangqi
 * *** empty log message ***
 *
 * Revision 1.7  2008/06/11 16:40:36  wangqi
 * *** empty log message ***
 *
 * Revision 1.6  2007/11/28 05:35:20  wangqi
 * *** empty log message ***
 *
 * Revision 1.5  2007/07/23 11:34:28  sunxf
 * MR#:Nova20-15
 *
 * Revision 1.4  2007/05/31 07:38:17  qilin
 * code format
 *
 * Revision 1.3  2007/05/22 07:58:45  qilin
 * no message
 *
 * Revision 1.2  2007/05/22 02:03:34  sunxb
 * *** empty log message ***
 *
 * Revision 1.1  2007/05/17 06:01:07  qilin
 * no message
 *
 * Revision 1.4  2007/03/28 10:15:06  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/03/28 09:51:09  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 05:14:31  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
