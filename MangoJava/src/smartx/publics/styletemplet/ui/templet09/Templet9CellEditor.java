/**************************************************************************
 * $RCSfile: Templet9CellEditor.java,v $  $Revision: 1.2.8.1 $  $Date: 2009/12/02 05:48:36 $
 **************************************************************************/
package smartx.publics.styletemplet.ui.templet09;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

import smartx.framework.metadata.ui.componentslist.*;


public class Templet9CellEditor extends AbstractCellEditor implements
    TableCellEditor {
    UIRefPanel_List pritable = new UIRefPanel_List("templet8",
        "templet8pars",
        "select templetcode as 模板编码,tablename 物理表名,templetname 模板名称 from pub_templet_1");

    UIRefPanel_List prifield = null;

    JTextField field = null;

    int row;

    int col;

    JTable table = null;
    Object value = null;
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int col) {
        this.table = table;
        this.row = row;
        this.col = col;
        this.value = value;
        if (col == 1) {
            String str = (String) table.getValueAt(row, 0);

            if (str.equals("主表编码")) {
                pritable.setValue(value.toString());
                return pritable;
            } else if (str.equals("主表主键")) {
                String listtableid = pritable.getRefID();
                if (!listtableid.equals("")) {
                    prifield = new UIRefPanel_List("templet9_treefield",
                        "templet9_treefield",
                        //"select cname 列名,coltype 列类型,cname 显示列名 from col where tname='" + treetablecode + "'"
                        "select itemkey as 列名,itemtype as 列类型,itemname as 显示列名 from pub_templet_1_item where pk_pub_templet_1=(select pk_pub_templet_1 from pub_templet_1 where templetcode='"+listtableid+"')"
                        );
                } else {
                    prifield = new UIRefPanel_List("templet9_treefield",
                        "templet9_treefield",
                        //"select cname 列名,coltype 列类型,cname 显示列名 from col "
                        "select itemkey as 列名,itemtype as 列类型,itemname as 显示列名 from pub_templet_1_item "
                        );
                }
                prifield.setValue(value.toString());
                return prifield;

            }
        }
        field = new JTextField(value.toString());
        return field;
    }

    public Object getCellEditorValue() {
        if (col == 1 && row < 5) {
            String str = (String) table.getValueAt(row, 0);

            if (str.equals("主表编码")) {
                return pritable.getValue();
            } else if (str.equals("主表主键")) {
                String treetablecode = pritable.getValue();
                if (!treetablecode.equals("")) {
                    return prifield.getValue();
                }
                return value;
            }
        }
        return value;
    }
}
/**************************************************************************
 * $RCSfile: Templet9CellEditor.java,v $  $Revision: 1.2.8.1 $  $Date: 2009/12/02 05:48:36 $
 *
 * $Log: Templet9CellEditor.java,v $
 * Revision 1.2.8.1  2009/12/02 05:48:36  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2007/05/31 07:39:05  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:14:29  qilin
 * no message
 *
 * Revision 1.2  2007/01/30 04:48:31  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/