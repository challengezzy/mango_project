/**************************************************************************
 * $RCSfile: UIFilePathPanel.java,v $  $Revision: 1.6.2.4 $  $Date: 2008/12/09 07:40:07 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentscard;

import java.io.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import smartx.framework.common.ui.*;
import smartx.framework.common.vo.NovaLogger;
import smartx.framework.metadata.ui.BillCardFieldActionIFC;
import smartx.framework.metadata.ui.BillCardPanel;
import smartx.framework.metadata.vo.*;


public class UIFilePathPanel extends AbstractNovaComponent implements INovaCompent {
    private static final long serialVersionUID = 1L;

    protected String REF_IMAGE="images/platform/filepath.jpg"; //点选参照按钮图标
    protected int REF_WIDTH=18;   //点选参照按钮宽度
    protected int REF_HEIGHT=18;  //点选参照按钮高度
    protected int FILE_WIDTH = this.FIELD_WIDTH - REF_WIDTH;
    protected JButton btn_ref = null;
    protected JTextField textField = null;
    
    protected BillCardPanel card=null;

    protected Point origin = new Point();
    
    private boolean isedit=true;  //是否编辑

    public UIFilePathPanel(Pub_Templet_1_ItemVO _templetVO) {
        this._vo = _templetVO; //

        this.key = this._vo.getItemkey(); //
        this.name = this._vo.getItemname(); //
        this.FIELD_WIDTH=this._vo.getCardwidth().intValue();
        this.FILE_WIDTH = this.FIELD_WIDTH - this.REF_WIDTH;
        init();
    }

    public UIFilePathPanel(String _key, String _name) {
        this.key = _key; //
        this.name = _name; //

        init();
    }

    public UIFilePathPanel(String _key, String _name, boolean isNeed) {
        this.key = _key; //
        this.name = _name; //
        this.isNeed = isNeed;
        init();
    }

    //Override
	protected JComponent[] getFieldComponents() {
    	textField = new JTextField();
        textField.setPreferredSize(new Dimension(this.FILE_WIDTH, this.FIELD_HEIGHT));
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

    /**
     * 
     * @param card
     */
    public void setCardPanel(BillCardPanel card){
    	this.card=card;
    }
    
    
    private void onButtonClicked() {
    	if(this.isedit){
	        JFileChooser chooser = new JFileChooser();
	        chooser.setCurrentDirectory(new File("."));
	        int result = chooser.showOpenDialog(this);
	
	        if (result == 0 && chooser.getSelectedFile() != null) {
	            String filePath = chooser.getSelectedFile().getPath();
	            this.textField.setText(filePath);
	        }
    	}else{    		
    		String act=_vo.getItemaction();
			//System.out.println("设置响应！！！"+act);
			//System.out.println(getParent().getClass().getName());
			try{
				((BillCardFieldActionIFC)(Class.forName(act).newInstance())).action(card.getBillVO());
			}catch(Exception eee){
				NovaLogger.getLogger(this).error("执行字段响应的时候出现错误！",eee);
			}
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
    	this.isedit=_bo;
        textField.setEditable(_bo);
        if(this._vo.getItemaction()==null
        		||this._vo.getItemaction().trim().equals("")){
        	btn_ref.setEnabled(_bo);
        }else{
        	if(!_bo){
	        	btn_ref.setIcon(UIUtil.getImage("images/office/(02,04).png"));
	        }else{
	        	btn_ref.setIcon(UIUtil.getImage("images/office/(01,04).png"));
	        }
        }
        
    }

    public String getValue() {
        return textField.getText(); //
    }
    
    //add by James.W
	//TODO 对于文件选择框获得界面选择真实值的处理
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
        setValue(_obj == null ? "" : (String) _obj);
    }

    public void focus() {
        textField.requestFocus();
        textField.requestFocusInWindow();
    }
}
/**************************************************************************
 * $RCSfile: UIFilePathPanel.java,v $  $Revision: 1.6.2.4 $  $Date: 2008/12/09 07:40:07 $
 *
 * $Log: UIFilePathPanel.java,v $
 * Revision 1.6.2.4  2008/12/09 07:40:07  wangqi
 * *** empty log message ***
 *
 * Revision 1.6.2.3  2008/11/05 05:21:01  wangqi
 * *** empty log message ***
 *
 * Revision 1.1  2008/09/01 07:38:11  wangqi
 * *** empty log message ***
 *
 * Revision 1.7  2008/06/11 16:40:35  wangqi
 * *** empty log message ***
 *
 * Revision 1.6  2007/11/28 05:35:17  wangqi
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
 * Revision 1.2  2007/05/22 02:03:33  sunxb
 * *** empty log message ***
 *
 * Revision 1.1  2007/05/17 06:01:06  qilin
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
