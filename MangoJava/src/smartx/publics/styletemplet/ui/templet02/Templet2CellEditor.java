/**************************************************************************
 * $RCSfile: Templet2CellEditor.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:02 $
 **************************************************************************/
package smartx.publics.styletemplet.ui.templet02;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

import smartx.framework.metadata.ui.componentslist.*;


public class Templet2CellEditor extends AbstractCellEditor implements TableCellEditor {
    UIRefPanel_List ref = new UIRefPanel_List("templet2", "templet2pars",
                                              "select templetcode 模板编码,tablename 物理表名,templetname 模板名称 from pub_templet_1");
    JTextField field = null;
    int row;
    int col;
    JTable table = null;
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col) {
        this.table = table;
        this.row = row;
        this.col = col;
        if (table.getValueAt(row, 0).toString().equals("模板编码")) {
            ref.setRefCode(value.toString());
            ref.setRefCode(value.toString());
            return ref;
        }
        field = new JTextField(value.toString());
        return field;
    }

    public Object getCellEditorValue() {
        if (table.getValueAt(row, 0).toString().equals("模板编码")) {
            return ref.getValue();
        } else {
            return field.getText();
        }
    }

}
/**************************************************************************
 * $RCSfile: Templet2CellEditor.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:02 $
 *
 * $Log: Templet2CellEditor.java,v $
 * Revision 1.2  2007/05/31 07:39:02  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:14:27  qilin
 * no message
 *
 * Revision 1.2  2007/01/30 04:48:30  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/