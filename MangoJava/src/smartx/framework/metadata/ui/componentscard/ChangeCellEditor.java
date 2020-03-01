/**************************************************************************
 * $RCSfile: ChangeCellEditor.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:17 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentscard;

import java.util.*;
import java.util.regex.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import smartx.framework.metadata.ui.componentslist.*;
import smartx.framework.metadata.vo.*;


class ChangeCellEditor extends AbstractCellEditor implements TableCellEditor {

    private static final long serialVersionUID = 1L;

    private Pub_Templet_1_ItemVO[] templetItemVOs = null;

    private JTextField textField = null;

    private JComboBox comboBox = null;
    ;

    private UIRefPanel_List uiref = null;

    private UITimeSetPanel_List uitime = null;

    private UIDateTimePanel_List uidatetime = null;

    private JCheckBox checkBox = null;

    private JTextField number = null;

    private String str_type = null;

    private JPasswordField passwordField = null;

    private UIFilePathPanel_List filePathPanel_List = null;

    private UIColorPanel_List colorPanel_List = null;

    private UIImagePanel_list imagePanel_list = null;

    private UITextAreaDetailPanel_List textAreaDetailPanel_List = null;

    private UITextAreaDetailPanel_List textAreaPanel_List = null;

    public ChangeCellEditor(Pub_Templet_1VO _templetVO) {
        this.templetItemVOs = _templetVO.getItemVos();
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

        ComBoxItemVO vo = (ComBoxItemVO) table.getValueAt(row, 1);
        str_type = findType(vo.getId());

        if (str_type.equals("文本框")) {
            textField = new JTextField(value == null ? "" : value + "");
            return textField;
        } else if (str_type.equals("下拉框")) {
            comboBox = new JComboBox();
            ComBoxItemVO[] itemVos = findComBoxItemVO(vo.getId());
            for (int i = 0; i < itemVos.length; i++) {
                comboBox.addItem(itemVos[i]);
            }
            return comboBox;
        } else if (str_type.equals("参照")) {
            uiref = new UIRefPanel_List(templetItemVOs[findPos(vo.getId())]);
            if (value instanceof RefItemVO) {
                uiref.setObject( (RefItemVO) value);
            }
            return uiref;
        } else if (str_type.equals("日历")) {
            uidatetime = new UIDateTimePanel_List(templetItemVOs[findPos(vo.getId())]);
            uidatetime.setObject(value);
            return uidatetime;
        } else if (str_type.equals("时间")) {
            uitime = new UITimeSetPanel_List(templetItemVOs[findPos(vo.getId())]);
            uitime.setObject(value);
            return uitime;
        } else if (str_type.equals("勾选框")) {
            checkBox = new JCheckBox();
            checkBox.setHorizontalAlignment(SwingConstants.CENTER);
            if (value != null && value.toString().equals("Y")) {
                checkBox.setSelected(true);
            } else {
                checkBox.setSelected(false);
            }
            return checkBox;
        } else if (str_type.equals("密码框")) {
            passwordField = new JPasswordField(value == null ? "" : value + "");
            return passwordField;
        } else if (str_type.equals("文件选择框")) {
            filePathPanel_List = new UIFilePathPanel_List(templetItemVOs[findPos(vo.getId())]);
            filePathPanel_List.setObject(value);
            return filePathPanel_List;
        } else if (str_type.equals("颜色")) {
            colorPanel_List = new UIColorPanel_List(templetItemVOs[findPos(vo.getId())]);
            colorPanel_List.setObject(value);
            return colorPanel_List;
        } else if (str_type.equals("大文本框")) {
            textAreaDetailPanel_List = new UITextAreaDetailPanel_List(templetItemVOs[findPos(vo.getId())]);
            textAreaDetailPanel_List.setObject(value);
            return textAreaDetailPanel_List;
        } else if (str_type.equals("多行文本框")) {
            textAreaPanel_List = new UITextAreaDetailPanel_List(templetItemVOs[findPos(vo.getId())]);
            textAreaPanel_List.setObject(value);
            return textAreaPanel_List;
        } else if (str_type.equals("图片选择框")) {
            imagePanel_list = new UIImagePanel_list(templetItemVOs[findPos(vo.getId())]);
            imagePanel_list.setObject(value);
            return imagePanel_list;
        } else if (str_type.equals("数字框")) {
            number = new JTextField(value == null ? "" : value + "");
            number.setHorizontalAlignment(JTextField.RIGHT);
            number.addKeyListener(new KeyListener() {

                public void keyPressed(KeyEvent e) {
                    // TODO Auto-generated method stub

                }

                public void keyReleased(KeyEvent e) {
                    // TODO Auto-generated method stub

                }

                public void keyTyped(KeyEvent e) {
                    // TODO Auto-generated method stub

                    Object o = e.getSource();
                    Pattern NUM_PATTERN = Pattern.compile("(\\-|\\.|\\d)*");

                    if (o == number) {
                        String str = number.getText();
                        Matcher m = NUM_PATTERN.matcher(str);
                        if (!m.matches()) {
                            e.consume();
                            str = str.substring(0, str.length() - 1);
                            number.setText(str);
                        }
                    }
                }

            });
            return number;
        } else {
            return new JTextField(value + "");
        }
    }

    private int findPos(String _key) {
        for (int i = 0; i < templetItemVOs.length; i++) {
            if (templetItemVOs[i].getItemkey().equals(_key)) {
                return i;
            }
        }
        return -1;
    }

    private String findType(String _key) {
        for (int i = 0; i < templetItemVOs.length; i++) {
            if (templetItemVOs[i].getItemkey().equals(_key)) {
                return templetItemVOs[i].getItemtype();
            }
        }
        return null;
    }

    private ComBoxItemVO[] findComBoxItemVO(String _key) {
        for (int i = 0; i < templetItemVOs.length; i++) {
            if (templetItemVOs[i].getItemkey().equals(_key)) {
                return templetItemVOs[i].getComBoxItemVos();
            }
        }
        return null;
    }

    public Object getCellEditorValue() {
        if (str_type == null) {
            return null;
        }
        if (str_type.equals("文本框")) {
            return textField.getText();
        } else if (str_type.equals("下拉框")) {
            ComBoxItemVO vo = (ComBoxItemVO) comboBox.getSelectedItem();
            return vo;
        } else if (str_type.equals("参照")) {
            return (RefItemVO) uiref.getObject();
        } else if (str_type.equals("日历")) {
            return uidatetime.getObject();
        } else if (str_type.equals("时间")) {
            return uitime.getObject();
        } else if (str_type.equals("勾选框")) {
            return checkBox.isSelected() ? "Y" : "N";
        } else if (str_type.equals("数字框")) {
            return number.getText();
        } else if (str_type.equals("密码框")) {
            return new String(passwordField.getPassword());
        } else if (str_type.equals("文件选择框")) {
            return filePathPanel_List.getObject();
        } else if (str_type.equals("颜色")) {
            return colorPanel_List.getObject();
        } else if (str_type.equals("大文本框")) {
            return textAreaDetailPanel_List.getObject();
        } else if (str_type.equals("多行文本框")) {
            return textAreaPanel_List.getObject();
        } else if (str_type.equals("图片选择框")) {
            return imagePanel_list.getObject();
        } else {
            return null;
        }
    }

    public boolean isCellEditable(EventObject evt) {
        if (evt instanceof MouseEvent) {
            return ( (MouseEvent) evt).getClickCount() >= 2;
        }
        return true;
    }

}
/**************************************************************************
 * $RCSfile: ChangeCellEditor.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:17 $
 *
 * $Log: ChangeCellEditor.java,v $
 * Revision 1.2  2007/05/31 07:38:17  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:01:07  qilin
 * no message
 *
 * Revision 1.3  2007/05/15 09:29:20  qilin
 * no message
 *
 * Revision 1.2  2007/01/30 05:14:31  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
