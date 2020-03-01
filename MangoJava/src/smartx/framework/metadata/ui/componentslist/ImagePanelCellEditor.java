/**************************************************************************
 * $RCSfile: ImagePanelCellEditor.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:21 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentslist;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import smartx.framework.metadata.vo.*;


public class ImagePanelCellEditor extends AbstractCellEditor implements TableCellEditor, INovaCellEditor {
    private static final long serialVersionUID = 1L;

    UIImagePanel_list imagepanel = null;

    Pub_Templet_1_ItemVO itemVO = null;

    public ImagePanelCellEditor(Pub_Templet_1_ItemVO _itemvo) {
        itemVO = _itemvo;
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        imagepanel = new UIImagePanel_list(itemVO, row, this); //
        imagepanel.setObject(value);

        if (itemVO.getListiseditable() == null || itemVO.getListiseditable().equals("1") ||
            itemVO.getListiseditable().equals("2") || itemVO.getListiseditable().equals("3")) {
            imagepanel.setEditable(true); //
        } else {
            imagepanel.setEditable(false); //
        }

        return imagepanel; //
    }

    public Object getCellEditorValue() {
        return imagepanel.getTextField().getText();
    }

    public boolean isCellEditable(EventObject evt) {
        if (itemVO.getListiseditable() != null && !itemVO.getListiseditable().equals("1")) {
            return false;
        } else {
            if (evt instanceof MouseEvent) {
                return ( (MouseEvent) evt).getClickCount() >= 2;
            } else {
                return true;
            }
        }
    }

    public javax.swing.JComponent getNovaCompent() {
        return imagepanel;
    }

}
/**************************************************************************
 * $RCSfile: ImagePanelCellEditor.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:21 $
 *
 * $Log: ImagePanelCellEditor.java,v $
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
