/**************************************************************************
 * $RCSfile: CheckBoxCellEditor.java,v $  $Revision: 1.2.8.1 $  $Date: 2010/01/20 09:57:06 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentslist;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import smartx.framework.metadata.vo.*;


public class CheckBoxCellEditor extends DefaultCellEditor implements INovaCellEditor {

    private static final long serialVersionUID = 1L;

    Pub_Templet_1_ItemVO itemVO = null;

    JCheckBox checkBox = null;

    public CheckBoxCellEditor(JCheckBox checkBox, Pub_Templet_1_ItemVO _itemvo) {
        super(checkBox);
        this.itemVO = _itemvo;
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        checkBox = (JCheckBox)super.getTableCellEditorComponent(table, value, isSelected, row, column);
        checkBox.setBackground(table.getSelectionBackground()); //背景颜色是表格选择的背景颜色!!
        checkBox.setHorizontalAlignment(SwingConstants.CENTER);

        if (value != null && value.toString().equals("Y")) {
            checkBox.setSelected(true);
        } else {
            checkBox.setSelected(false);
        }

        if (itemVO.getListiseditable() == null || itemVO.getListiseditable().equals("1") ||
            itemVO.getListiseditable().equals("2") || itemVO.getListiseditable().equals("3")) {
            checkBox.setEnabled(true);
        } else {
            checkBox.setEnabled(false); //
        }

        return checkBox;
    }

    public Object getCellEditorValue() {
        return checkBox.isSelected() ? "Y" : "N"; //返回下拉框中的VO
    }

    public boolean isCellEditable(EventObject anEvent) {
        if (itemVO.getListiseditable() != null && !itemVO.getListiseditable().equals("1")) {
            return false;
        } else {
            if (anEvent instanceof MouseEvent) {
                return ( (MouseEvent) anEvent).getClickCount() >= 1;
            } else {
                return true;
            }
        }
    }

    public javax.swing.JComponent getNovaCompent() {
        return checkBox;
    }
}
/**************************************************************************
 * $RCSfile: CheckBoxCellEditor.java,v $  $Revision: 1.2.8.1 $  $Date: 2010/01/20 09:57:06 $
 *
 * $Log: CheckBoxCellEditor.java,v $
 * Revision 1.2.8.1  2010/01/20 09:57:06  wangqi
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
