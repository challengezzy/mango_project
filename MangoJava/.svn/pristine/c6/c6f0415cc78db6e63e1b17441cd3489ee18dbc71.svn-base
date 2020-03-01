/**************************************************************************
 * $RCSfile: Templet5CellEditor.java,v $  $Revision: 1.2.8.1 $  $Date: 2009/12/02 05:48:17 $
 **************************************************************************/
package smartx.publics.styletemplet.ui.templet05;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

import smartx.framework.metadata.ui.componentslist.*;


public class Templet5CellEditor extends AbstractCellEditor implements
    TableCellEditor {
    UIRefPanel_List reftreetable = new UIRefPanel_List("templet4",
        "templet4pars",
        "select templetcode as 模板编码,tablename 物理表名,templetname 模板名称 from pub_templet_1");

    UIRefPanel_List reflisttable = new UIRefPanel_List("templet4_list",
        "templet4pars_list",
        "select templetcode as 模板编码,tablename 物理表名,templetname 模板名称 from pub_templet_1");

    UIRefPanel_List reftreefield = null;

    UIRefPanel_List reflistfield = null;

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

            if (str.equals("树模板编码")) {
                reftreetable.setValue(value.toString());
                return reftreetable;
            } else if (str.equals("列表模板编码")) {
                reflisttable.setValue(value.toString());
                return reflisttable;
            } else if (str.equals("树主键") || str.equals("树父主键")
                       || str.equals("树关联字段")) {
                String listtableid = reftreetable.getRefID();
                if (!listtableid.equals("")) {
                    reftreefield = new UIRefPanel_List("templet4_treefield",
                        "templet4_treefield",
                        //"select cname as 列名,coltype 列类型,cname 显示列名　from col where tname='" + treetablecode + "'"
                        "select itemkey as 列名,itemtype as 列类型,itemname as 显示列名 from pub_templet_1_item where pk_pub_templet_1=(select pk_pub_templet_1 from pub_templet_1 where templetcode='"+listtableid+"')"
                        );
                } else {
                    reftreefield = new UIRefPanel_List("templet4_treefield",
                        "templet4_treefield",
                        //"select cname as 列名,coltype 列类型,cname 显示列名　from col"
                        "select itemkey as 列名,itemtype as 列类型,itemname as 显示列名 from pub_templet_1_item "
                        );
                }
                reftreefield.setValue(value.toString());
                return reftreefield;

            } else if (str.equals("列表关联字段")) {
                String listtableid = reflisttable.getRefID();
                if (!listtableid.equals("")) {
                    reflistfield = new UIRefPanel_List("templet4_listfield",
                        "templet4_listfield",
                        //"select cname as 列名,coltype 列类型,cname　显示列名 from col where tname='" + listtablecode + "'"
                        "select itemkey as 列名,itemtype as 列类型,itemname as 显示列名 from pub_templet_1_item where pk_pub_templet_1=(select pk_pub_templet_1 from pub_templet_1 where templetcode='"+listtableid+"')"
                        );
                } else {
                    reflistfield = new UIRefPanel_List("templet4_listfield",
                        "templet4_listfield",
                        //"select cname as 列名,coltype 列类型,cname　显示列名 from col"
                        "select itemkey as 列名,itemtype as 列类型,itemname as 显示列名 from pub_templet_1_item "
                        );
                }
                reflistfield.setValue(value.toString());
                return reflistfield;
            }
        }
        field = new JTextField(value.toString());
        return field;
    }

    public Object getCellEditorValue() {
        if (col == 1 && row < 9) {
            String str = (String) table.getValueAt(row, 0);

            if (str.equals("树模板编码")) {
                return reftreetable.getValue();
            } else if (str.equals("列表模板编码")) {
                return reflisttable.getValue();
            } else if (str.equals("树主键") || str.equals("树父主键")
                       || str.equals("树关联字段")) {
                String treetablecode = reftreetable.getValue();
                if (!treetablecode.equals("")) {
                    return reftreefield.getValue();
                }
                return value;
            } else if (str.equals("列表关联字段")) {
                String listtablecode = reflisttable.getValue();
                if (!listtablecode.equals("")) {
                    return reflistfield.getValue();
                }
                return value;
            }
        }

        return field == null ? "" : field.getText();
    }

    public String getSelectedTablename() {
        return reftreetable.getRefCode();
    }
}
/**************************************************************************
 * $RCSfile: Templet5CellEditor.java,v $  $Revision: 1.2.8.1 $  $Date: 2009/12/02 05:48:17 $
 *
 * $Log: Templet5CellEditor.java,v $
 * Revision 1.2.8.1  2009/12/02 05:48:17  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2007/05/31 07:39:03  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:14:28  qilin
 * no message
 *
 * Revision 1.2  2007/01/30 04:48:31  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/