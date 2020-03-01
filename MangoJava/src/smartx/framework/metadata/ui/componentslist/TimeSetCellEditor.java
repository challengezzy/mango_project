/**************************************************************************
 * $RCSfile: TimeSetCellEditor.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:22 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentslist;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import smartx.framework.metadata.vo.*;


public class TimeSetCellEditor extends AbstractCellEditor implements TableCellEditor, INovaCellEditor {

    private static final long serialVersionUID = 1L;

    UITimeSetPanel_List timesetPanel = null;

    Pub_Templet_1_ItemVO itemVO = null;

    public TimeSetCellEditor(Pub_Templet_1_ItemVO _itemvo) {
        itemVO = _itemvo;
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        timesetPanel = new UITimeSetPanel_List(itemVO); //
        timesetPanel.setObject(value);

        if (itemVO.getListiseditable() == null || itemVO.getListiseditable().equals("1") ||
            itemVO.getListiseditable().equals("2") || itemVO.getListiseditable().equals("3")) {
            timesetPanel.setEditable(true); //
        } else {
            timesetPanel.setEditable(false); //
        }
        return timesetPanel; //
    }

    public Object getCellEditorValue() {
        return timesetPanel.getTextField().getText();
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
        return timesetPanel;
    }

}
/**************************************************************************
 * $RCSfile: TimeSetCellEditor.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:22 $
 *
 * $Log: TimeSetCellEditor.java,v $
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
