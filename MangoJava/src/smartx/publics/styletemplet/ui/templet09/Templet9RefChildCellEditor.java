/**************************************************************************
 * $RCSfile: Templet9RefChildCellEditor.java,v $  $Revision: 1.2.8.1 $  $Date: 2009/12/02 05:48:36 $
 **************************************************************************/
package smartx.publics.styletemplet.ui.templet09;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

import smartx.framework.metadata.ui.componentslist.*;


public class Templet9RefChildCellEditor extends AbstractCellEditor implements
    TableCellEditor {
    UIRefPanel_List subtable = new UIRefPanel_List("templet8_list",
        "templet8pars_list",
        "select templetcode as 模板编码,tablename 物理表名,templetname 模板名称 from pub_templet_1");

    UIRefPanel_List subfield = null;

    UIRefPanel_List subfieldforeignkey = null;

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
        if (col == 0) {
            subtable.setValue(value.toString());
            return subtable;
        } else if (col == 1) {
            String listtableid = subtable.getRefID();
            if (!listtableid.equals("")) {

                subfield = new UIRefPanel_List("templet9_listfield",
                                               "templet9_listfield",
                                               //"select cname as 列名,coltype 列类型,cname 显示列名 from col where tname='" + listtablecode + "'"
                                               "select itemkey as 列名,itemtype as 列类型,itemname as 显示列名 from pub_templet_1_item where pk_pub_templet_1=(select pk_pub_templet_1 from pub_templet_1 where templetcode='"+listtableid+"')"
                                               );
            } else {
                subfield = new UIRefPanel_List("templet9_listfield",
                                               "templet9_listfield",
                                               //"select cname as 列名,coltype 列类型,cname 显示列名 from col "
                                               "select itemkey as 列名,itemtype as 列类型,itemname as 显示列名 from pub_templet_1_item "
                                               );
            }
            subfield.setValue(value.toString());
            return subfield;

        } else if (col == 2) {
            String listtableid = subtable.getRefID();
            if (!listtableid.equals("")) {
                subfieldforeignkey = new UIRefPanel_List("templet9_listfield",
                    "templet9_listfield",
                    //"select cname as 列名,coltype 列类型,cname　显示列名 from col where tname='" + listtablecode + "'"
                    "select itemkey as 列名,itemtype as 列类型,itemname as 显示列名 from pub_templet_1_item where pk_pub_templet_1=(select pk_pub_templet_1 from pub_templet_1 where templetcode='"+listtableid+"')"
                    );
            } else {
                subfieldforeignkey = new UIRefPanel_List("templet9_listfield",
                    "templet9_listfield",
                    //"select cname as 列名,coltype 列类型,cname　显示列名 from col "
                    "select itemkey as 列名,itemtype as 列类型,itemname as 显示列名 from pub_templet_1_item "
                    );
            }
            subfieldforeignkey.setValue(value.toString());
            return subfieldforeignkey;
        }

        return new JTextField();
    }

    public Object getCellEditorValue() {
        if (col == 0) {
            return subtable.getValue();
        } else if (col == 1) {
            String listtablecode = subtable.getValue();
            if (!listtablecode.equals("")) {
                return subfield.getValue();
            }
            return value;
        } else if (col == 2) {
            String listtablecode = subtable.getValue();
            if (!listtablecode.equals("")) {
                return subfieldforeignkey.getValue();
            }
            return value;
        }
        return value;
    }
}
/**************************************************************************
 * $RCSfile: Templet9RefChildCellEditor.java,v $  $Revision: 1.2.8.1 $  $Date: 2009/12/02 05:48:36 $
 *
 * $Log: Templet9RefChildCellEditor.java,v $
 * Revision 1.2.8.1  2009/12/02 05:48:36  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2007/05/31 07:39:05  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:14:29  qilin
 * no message
 *
 * Revision 1.2  2007/01/30 04:48:30  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/