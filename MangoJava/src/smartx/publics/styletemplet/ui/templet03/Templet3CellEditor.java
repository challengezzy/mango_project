/**************************************************************************
 * $RCSfile: Templet3CellEditor.java,v $  $Revision: 1.3.2.5 $  $Date: 2010/02/05 02:17:29 $
 **************************************************************************/
package smartx.publics.styletemplet.ui.templet03;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

import smartx.framework.metadata.ui.componentslist.*;


public class Templet3CellEditor extends AbstractCellEditor implements TableCellEditor {
    
	protected UIRefPanel_List _ref = null;    //当前单元格的参照编辑框	
	protected JTextField _field = null;       //当前单元格的编辑框
	protected JComboBox cascadeondelete = null; //当前单元格的级联删除下拉框
	protected JComboBox isloadall = null;       //当前单元格的全加载下拉框
	
	protected int row;                        //当前单元格的行号
	protected int col;                        //当前单元格的列号
	protected JTable table = null;            //当前单元格所在表格
	protected Object value = null;            //修改前的旧值
	
	
    
    
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int col) {
        this.table = table;
        this.row = row;
        this.col = col;
        this.value = value;
        if (col == 1) {
            String str = (String) table.getValueAt(row, 0);

            if (str.equals("树模板编码")) {
            	_ref = new UIRefPanel_List("templet3",
            	        "templet3pars",
            	        "select templetcode 模板编码,tablename 物理表名,templetname 模板名称 from pub_templet_1");
            	_ref.setValue(value.toString());
                return _ref;
            } else if (str.equals("树主键") || str.equals("树父主键")
                       || str.equals("树关联字段")||str.equals("树检索字段")) {
            	String tmp = (String)table.getValueAt(0, 1);
            	
                if (!tmp.equals("")) {
                	_ref = new UIRefPanel_List("templet3_treefield",
                        "templet3_treefield",
                        "select itemkey as 列名,itemtype as 列类型,itemname as 显示列名 from pub_templet_1_item where pk_pub_templet_1=(select pk_pub_templet_1 from pub_templet_1 where templetcode='"+tmp+"' or templetname='"+tmp+"')"
                    );                    
                } else {
                	_ref = new UIRefPanel_List("templet3_treefield",
                        "templet3_treefield",
                        "select itemkey as 列名,itemtype as 列类型,itemname as 显示列名 from pub_templet_1_item"
                    );
                }
                _ref.setValue(value.toString());
                return _ref;
            } else if (str.equals("级联删除")) {
            	cascadeondelete = new JComboBox(new Object[] {"是", "否"});
            	if ( "是".equals(value)) {
                    cascadeondelete.setSelectedIndex(0);
                } else {
                    cascadeondelete.setSelectedIndex(1);
                }
                return cascadeondelete;                
            } else if (str.equals("加载全部数据")) {
            	isloadall = new JComboBox(new Object[] {"是", "否"});
            	if ( "是".equals(value)) {
                	isloadall.setSelectedIndex(0);
                } else {
                	isloadall.setSelectedIndex(1);
                }
                return isloadall;
            }
        }
        _field = new JTextField(value.toString());
        return _field;
    }

    public Object getCellEditorValue() {
        if (col == 1) {
            String str = (String) table.getValueAt(row, 0);

            if (str.equals("树模板编码")) {
                return _ref.getValue();
            } else if (str.equals("树主键")) {
            	return _ref.getValue();
            } else if (str.equals("根节点名称")) {
            	value=_field.getText();
            	return value;                
            } else if (str.equals("卡片关联字段")||str.equals("树父主键")
            		|| str.equals("树关联字段")||str.equals("树检索字段")) {
                return _ref.getValue();                
            } else if (str.equals("级联删除")) {
                String cascade = cascadeondelete.getSelectedItem().toString();
                if (!cascade.equals("")) {
                    return cascade;
                }
                return "否";
            }else if (str.equals("加载全部数据")) {
                String cascade = isloadall.getSelectedItem().toString();
                if (!cascade.equals("")) {
                    return cascade;
                }
                return "是";
            }
        }

        return _field == null ? "" : _field.getText();
    }
    
}
/**************************************************************************
 * $RCSfile: Templet3CellEditor.java,v $  $Revision: 1.3.2.5 $  $Date: 2010/02/05 02:17:29 $
 *
 * $Log: Templet3CellEditor.java,v $
 * Revision 1.3.2.5  2010/02/05 02:17:29  wangqi
 * *** empty log message ***
 *
 * Revision 1.3.2.4  2010/01/06 07:52:15  wangqi
 * *** empty log message ***
 *
 * Revision 1.3.2.3  2010/01/05 10:20:36  wangqi
 * *** empty log message ***
 *
 * Revision 1.3.2.2  2009/09/01 08:25:34  yangjm
 * *** empty log message ***
 *
 * Revision 1.3.2.1  2008/09/16 06:13:25  wangqi
 * patch   : 20080916
 * file    : nova_20080128_20080916.jar
 * content : 处理 MR nova20-87,nova20-30；
 * 另外，改写了快速查询面板的处理。
 *
 * Revision 1.3  2008/01/15 09:03:08  wangqi
 * 孙雪峰修改MR
 *
 * Revision 1.2  2007/05/31 07:39:03  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:14:27  qilin
 * no message
 *
 * Revision 1.3  2007/03/12 04:45:31  sunxf
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:48:32  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/