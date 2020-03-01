/**************************************************************************
 * $RCSfile: DatePanelCellEditor.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:21 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentslist;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import smartx.framework.metadata.vo.*;


public class DatePanelCellEditor extends AbstractCellEditor implements TableCellEditor, INovaCellEditor {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    UIDateTimePanel_List dateTimePanel = null;

    Pub_Templet_1_ItemVO itemVO = null;

    public DatePanelCellEditor(Pub_Templet_1_ItemVO _itemvo) {
        itemVO = _itemvo;
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        dateTimePanel = new UIDateTimePanel_List(itemVO); //
        dateTimePanel.setObject(value);

        if (itemVO.getListiseditable() == null || itemVO.getListiseditable().equals("1") ||
            itemVO.getListiseditable().equals("2") || itemVO.getListiseditable().equals("3")) {
            dateTimePanel.setEditable(true); //
        } else {
            dateTimePanel.setEditable(false); //
        }

        return dateTimePanel; //
    }

    public Object getCellEditorValue() {
        return dateTimePanel.getTextField().getText();
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
        return dateTimePanel;
    }

}
/**************************************************************************
 * $RCSfile: DatePanelCellEditor.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:21 $
 *
 * $Log: DatePanelCellEditor.java,v $
 * Revision 1.2  2007/05/31 07:38:21  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:02:00  qilin
 * no message
 *
 * Revision 1.4  2007/03/30 10:00:07  shxch
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
