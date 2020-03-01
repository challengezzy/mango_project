/**************************************************************************
 * $RCSfile: Templet1CellEditor.java,v $  $Revision: 1.2.8.1 $  $Date: 2009/12/16 04:13:50 $
 **************************************************************************/
package smartx.publics.styletemplet.ui.templet01;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

import smartx.framework.metadata.ui.componentslist.*;


public class Templet1CellEditor extends AbstractCellEditor implements TableCellEditor {
	private static final long serialVersionUID = -805350266253243121L;

	protected UIRefPanel_List ref = null;

    protected JTextField field = null;

    protected int row;

    protected int col;
    protected JTable table = null;
    
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col) {
        this.row = row;
        this.col = col;
        this.table = table;
        if (table.getValueAt(row, 0).toString().equals("模板编码")) {
            ref = new UIRefPanel_List("templet1", "templet1pars",
                                      "select templetcode 模板编码,tablename 物理表名,templetname 模板名称 from pub_templet_1");
            ref.setRefID(value.toString());
            ref.setRefCode(value.toString());
            return ref;
        } else {
            field = new JTextField(value.toString());
            return field;
        }

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
 * $RCSfile: Templet1CellEditor.java,v $  $Revision: 1.2.8.1 $  $Date: 2009/12/16 04:13:50 $
 *
 * $Log: Templet1CellEditor.java,v $
 * Revision 1.2.8.1  2009/12/16 04:13:50  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2007/05/31 07:39:01  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:14:26  qilin
 * no message
 *
 * Revision 1.2  2007/01/30 04:48:33  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/