/**************************************************************************
 * $RCSfile: UIRefPanelCellEditor.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:22 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentslist;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import smartx.framework.metadata.ui.*;
import smartx.framework.metadata.vo.*;


public class UIRefPanelCellEditor extends AbstractCellEditor implements TableCellEditor, INovaCellEditor {

    private static final long serialVersionUID = 1L;

    UIRefPanel_List refPanel = null;

    Pub_Templet_1_ItemVO itemVO = null;

    public UIRefPanelCellEditor(Pub_Templet_1_ItemVO _itemvo) {
        itemVO = _itemvo;
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        BillListModel model = (BillListModel) table.getModel();

        refPanel = new UIRefPanel_List(itemVO, model, row, this); //创建控件!!
        RefItemVO vo = (RefItemVO) value; //取得数据
        refPanel.setObject(vo);

        if (itemVO.getListiseditable() == null || !itemVO.getListiseditable().equals("1") ||
            !itemVO.getListiseditable().equals("2") || !itemVO.getListiseditable().equals("3")) {
            refPanel.setEditable(true); //
        } else {
            refPanel.setEditable(false); //
        }

        return refPanel; //
    }

    public Object getCellEditorValue() {
        return refPanel.getObject();
    }

    public boolean isCellEditable(EventObject evt) {
        if (itemVO.getListiseditable() != null && itemVO.getListiseditable().equals("1")) {
            if (evt instanceof MouseEvent) {
                return ( (MouseEvent) evt).getClickCount() >= 2;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public javax.swing.JComponent getNovaCompent() {
        return refPanel;
    }

}
/**************************************************************************
 * $RCSfile: UIRefPanelCellEditor.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:22 $
 *
 * $Log: UIRefPanelCellEditor.java,v $
 * Revision 1.2  2007/05/31 07:38:22  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:01:59  qilin
 * no message
 *
 * Revision 1.5  2007/03/30 10:00:07  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/03/28 11:23:02  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/03/28 10:52:14  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 05:05:40  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
