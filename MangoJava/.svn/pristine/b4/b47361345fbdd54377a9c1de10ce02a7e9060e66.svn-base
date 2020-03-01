/**************************************************************************
 * $RCSfile: TextAreaPanelCellEditor.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:22 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentslist;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import smartx.framework.metadata.vo.*;


public class TextAreaPanelCellEditor extends AbstractCellEditor implements TableCellEditor, INovaCellEditor {

    private static final long serialVersionUID = -2548115764335107645L;

    UITextAreaDetailPanel_List textAreaPanel = null;

    Pub_Templet_1_ItemVO itemVO = null;

    public TextAreaPanelCellEditor(Pub_Templet_1_ItemVO _itemvo) {
        itemVO = _itemvo;
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        textAreaPanel = new UITextAreaDetailPanel_List(itemVO); //
        textAreaPanel.setObject(value);

        if (itemVO.getListiseditable() == null || itemVO.getListiseditable().equals("1") ||
            itemVO.getListiseditable().equals("2") || itemVO.getListiseditable().equals("3")) {
            textAreaPanel.setEditable(true); //
        } else {
            textAreaPanel.setEditable(false); //
        }

        return textAreaPanel; //
    }

    public Object getCellEditorValue() {
        return textAreaPanel.getTextField().getText();
    }

    public boolean isCellEditable(EventObject evt) {
        if (itemVO.getListiseditable() != null && itemVO.getListiseditable().equals("1")) {
            if (evt instanceof MouseEvent) {
                return ( (MouseEvent) evt).getClickCount() >= 2;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public javax.swing.JComponent getNovaCompent() {
        return textAreaPanel;
    }

}
/**************************************************************************
 * $RCSfile: TextAreaPanelCellEditor.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:22 $
 *
 * $Log: TextAreaPanelCellEditor.java,v $
 * Revision 1.2  2007/05/31 07:38:22  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:01:59  qilin
 * no message
 *
 * Revision 1.4  2007/03/30 10:00:06  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/03/28 11:23:02  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 05:05:41  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
