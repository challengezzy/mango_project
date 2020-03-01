/**************************************************************************
 * $RCSfile: PasswordTextFieldCellEditor.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:21 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentslist;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import smartx.framework.metadata.vo.*;


public class PasswordTextFieldCellEditor extends DefaultCellEditor implements INovaCellEditor {

    private static final long serialVersionUID = 1L;

    Pub_Templet_1_ItemVO itemVO = null;

    JPasswordField textField = null;

    public PasswordTextFieldCellEditor(JPasswordField _textField, Pub_Templet_1_ItemVO _itemvo) {
        super(_textField);
        this.itemVO = _itemvo;
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        textField = (JPasswordField)super.getTableCellEditorComponent(table, value, isSelected, row, column);
        if (itemVO.getListiseditable() == null || itemVO.getListiseditable().equals("1") ||
            itemVO.getListiseditable().equals("2") || itemVO.getListiseditable().equals("3")) {
            textField.setEditable(true); //
        } else {
            textField.setEditable(false); //
        }

        return textField;
    }

    public Object getCellEditorValue() {
        return new String(textField.getPassword());
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

}
/**************************************************************************
 * $RCSfile: PasswordTextFieldCellEditor.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:21 $
 *
 * $Log: PasswordTextFieldCellEditor.java,v $
 * Revision 1.2  2007/05/31 07:38:21  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:02:00  qilin
 * no message
 *
 * Revision 1.5  2007/03/30 10:00:06  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/03/28 11:23:02  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/01/31 08:57:00  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 05:05:40  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
