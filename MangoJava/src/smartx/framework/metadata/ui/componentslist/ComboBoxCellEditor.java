/**************************************************************************
 * $RCSfile: ComboBoxCellEditor.java,v $  $Revision: 1.2.8.1 $  $Date: 2009/04/23 09:15:35 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentslist;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import smartx.framework.metadata.vo.*;


public class ComboBoxCellEditor extends DefaultCellEditor implements INovaCellEditor {

    private static final long serialVersionUID = 1L;

    Pub_Templet_1_ItemVO itemVO = null; //

    JComboBox comboBox = null; //

    public ComboBoxCellEditor(JComboBox _comboBox, Pub_Templet_1_ItemVO _itemvo) {
        super(_comboBox);
        this.itemVO = _itemvo;
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        ComBoxItemVO vo = (ComBoxItemVO) value;
        comboBox = (JComboBox)super.getTableCellEditorComponent(table, value, isSelected, row, column);
        comboBox.setEditable(false); //

        String str_id = null;
        if (vo != null) {
            str_id = vo.getId();
        }
        initComboBox(str_id); //

        if (itemVO.getListiseditable() == null || itemVO.getListiseditable().equals("1") ||
            itemVO.getListiseditable().equals("2") || itemVO.getListiseditable().equals("3")) {
            comboBox.setEnabled(true);
        } else {
            comboBox.setEnabled(false);
        }

        return comboBox; //
    }

    private void initComboBox(String _id) {
        comboBox.removeAllItems();
        ComBoxItemVO[] comBoBoxitemvos = itemVO.getComBoxItemVos(); //

        comboBox.addItem(new ComBoxItemVO("", "", "")); //
        if (comBoBoxitemvos != null) {
            for (int i = 0; i < comBoBoxitemvos.length; i++) {
                comboBox.addItem(comBoBoxitemvos[i]);
                if (comBoBoxitemvos[i].getId().equals(_id)) {
                    comboBox.setSelectedIndex(i + 1);
                }
            }
        }

    }

    public Object getCellEditorValue() {
        Object obj = comboBox.getSelectedItem();
        if (obj == null) {
            return null;
        }
        if (obj instanceof ComBoxItemVO) {
            return (ComBoxItemVO) comboBox.getSelectedItem(); //返回下拉框中的VO
        } else {
            return null;
        }
    }

    /**
     * 是否可编辑
     */
    public boolean isCellEditable(EventObject evt) {
    	if (itemVO.getListiseditable() != null && !itemVO.getListiseditable().equals("1")) {
            return false;
        } else {
	    	if (evt instanceof MouseEvent) {
	            return ( (MouseEvent) evt).getClickCount() >= 2;
	        }
	        return true;
        }
    }

    public javax.swing.JComponent getNovaCompent() {
        return comboBox;
    }

}
/**************************************************************************
 * $RCSfile: ComboBoxCellEditor.java,v $  $Revision: 1.2.8.1 $  $Date: 2009/04/23 09:15:35 $
 *
 * $Log: ComboBoxCellEditor.java,v $
 * Revision 1.2.8.1  2009/04/23 09:15:35  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2007/05/31 07:38:21  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:02:00  qilin
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
