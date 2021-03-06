/**************************************************************************
 * $RCSfile: NumberFieldCellEditor.java,v $  $Revision: 1.2.8.2 $  $Date: 2009/06/12 05:25:16 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentslist;

import java.util.*;
import java.util.regex.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import smartx.framework.metadata.ui.componentscard.QueryNumericPanel;
import smartx.framework.metadata.vo.*;


public class NumberFieldCellEditor extends DefaultCellEditor implements INovaCellEditor {
	private static final long serialVersionUID = 6048305821930562109L;

	protected int _type = QueryNumericPanel.TYPE_FLOAT;  //控件类型
	protected Pub_Templet_1_ItemVO itemVO = null;
    protected JTextField textField = null;

    public NumberFieldCellEditor(JFormattedTextField _textField, Pub_Templet_1_ItemVO _itemvo) {
        super(_textField);
        this.itemVO = _itemvo;
        
        //TODO 根据vo类型设置_type
        
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        textField = (JTextField)super.getTableCellEditorComponent(table, value, isSelected, row, column);
        textField.setHorizontalAlignment(JTextField.RIGHT);
        //设置文本过滤
        textField.setDocument(new PlainDocument(){
        	    private static final long serialVersionUID = -6162395929065942879L;
				public void insertString(int offset, String s, AttributeSet attributeSet) throws BadLocationException {
		            String pstr=getPsttern();        	
		        	
		        	int len=super.getLength();
		        	String str=super.getText(0, len);
		        	str=(offset==len)?(str+s):(str.substring(0,offset)+s+str.substring(offset));
		        	
		        	Pattern p = Pattern.compile(pstr);
		        	boolean isNum = p.matcher(str).matches();
		        	
		        	if (isNum) { // 如果是数字就插入!!
		                if (offset == 0 && (s.equals("."))) {
		                    Toolkit.getDefaultToolkit().beep(); // 放个声音!!
		                    return; //
		                } else {
		                    super.insertString(offset, s, attributeSet);
		                }
		            } else {
		                Toolkit.getDefaultToolkit().beep(); // 放个声音!!
		                return; // 直接返回不插入了!!
		            }
		        }
	
		    }
        
        );
        //离开焦点时候校验
        textField.setInputVerifier(new InputVerifier() {   
    	        public boolean verify(JComponent comp) {   
	    	        String s = ((JTextField) comp).getText();   
	    	        if(s.equals("")) return true;
	    	        String pstr=getPsttern();   
	            	
	            	Pattern p = Pattern.compile(pstr);
	            	boolean rt=p.matcher(s).matches();
	            	if(!rt){
	            		Toolkit.getDefaultToolkit().beep(); 
	            	}
	            	return rt;
    	        }   
    	    }
    	);
        
        
        textField.setText(String.valueOf(value));

        if (itemVO.getListiseditable() == null 
        		|| itemVO.getListiseditable().equals("1") 
        		|| itemVO.getListiseditable().equals("2") 
        		|| itemVO.getListiseditable().equals("3")) {
            textField.setEditable(true); //
        } else {
            textField.setEditable(false); //
        }

        return textField;
    }

    public Object getCellEditorValue() {
        return textField.getText();
    }

    public boolean isCellEditable(EventObject anEvent) {
        if (itemVO.getListiseditable() != null && !itemVO.getListiseditable().equals("1")) {
            return false;
        } else {
            if (anEvent instanceof MouseEvent) {
                return ( (MouseEvent) anEvent).getClickCount() >= 2;
            } else {
                return true;
            }
        }
    }

    public javax.swing.JComponent getNovaCompent() {
        return textField;
    }
    
    /**
     * 获得格式串
     * 根据当前控件的类型
     * @return
     */
    public String getPsttern(){
    	if(this._type==QueryNumericPanel.TYPE_INTEGER){
    		return "[+-]{0,1}[0-9]{1,}";
    	}else if(this._type==QueryNumericPanel.TYPE_FLOAT){
    		return "[+-]{0,1}[0-9]{1,}[.]{0,1}[0-9]{0,}";
    	}else{//QueryNumericPanel.TYPE_CODE
    		return "[A-Z,0-9]{1,}[A-Z,0-9,-]{0,}[A-Z,0-9]{1,}";
    	}
    }

}
/**************************************************************************
 * $RCSfile: NumberFieldCellEditor.java,v $  $Revision: 1.2.8.2 $  $Date: 2009/06/12 05:25:16 $
 *
 * $Log: NumberFieldCellEditor.java,v $
 * Revision 1.2.8.2  2009/06/12 05:25:16  wangqi
 * *** empty log message ***
 *
 * Revision 1.2.8.1  2009/01/15 15:52:44  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2007/05/31 07:38:21  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:02:00  qilin
 * no message
 *
 * Revision 1.4  2007/03/30 10:00:06  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/03/28 11:23:02  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 05:05:40  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
