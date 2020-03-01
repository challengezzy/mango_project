/**************************************************************************
 * $RCSfile: AbstractTempletRefPars.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:01 $
 **************************************************************************/
package smartx.publics.styletemplet.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import smartx.framework.common.ui.*;
import smartx.framework.common.vo.*;


public abstract class AbstractTempletRefPars extends JPanel {
    private VectorMap components_map = null;
    private String uiinterceptor = "";
    private String bsinterceptor = "";
    private String customerpanel = "";
    private String showsysbutton = "是";
    JTable interceptortable = new JTable();
    public AbstractTempletRefPars() {
        super();
    }

    public AbstractTempletRefPars(VectorMap _map) {
        super();
        this.components_map = _map;
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        createView();
    }

    public void initialization(VectorMap _map) {
        this.components_map = _map;
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        createView();
    }

    private void createView() {
        JSplitPane split = new JSplitPane();
        split.setOneTouchExpandable(true);
        split.setOrientation(JSplitPane.VERTICAL_SPLIT);
        split.setDividerLocation(200);
        split.setTopComponent(getTopComponent());
        split.setBottomComponent(getBottomComponent());
        this.add(split);
    }

    private JScrollPane getTopComponent() {
        JComponent comp = (JComponent)this.components_map.get(NovaConstants.STRING_REFPANEL_COMMON_TITLE);
        JScrollPane jsp = new JScrollPane(comp);
        return jsp;
    }

    private JScrollPane getBottomComponent() {
        MyTableModel model = new MyTableModel();
        MyCellEditor editor = new MyCellEditor();
        interceptortable.setModel(model);
        interceptortable.setRowHeight(20);
        interceptortable.getColumnModel().getColumn(0).setCellEditor(editor);
        interceptortable.getColumnModel().getColumn(1).setCellRenderer(new AbstractTempletRefPars.TempletRefCellRender());
        interceptortable.getColumnModel().getColumn(0).setPreferredWidth(150);
        interceptortable.getColumnModel().getColumn(1).setPreferredWidth(150);
        JScrollPane jsp = new JScrollPane(interceptortable);
        return jsp;
    }

    private class TempletRefCellRender implements TableCellRenderer {
        JTextArea field = new JTextArea();
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int r, int c) {
            field.setText(value.toString());
            field.setForeground(Color.GRAY);
            field.setBackground(new Color(240, 240, 240));
            return field;
        }

    }

    private class MyTableModel extends AbstractTableModel {

        public int getColumnCount() {
            return 2;
        }

        public String getColumnName(int e) {
            String re = "";
            if (e == 0) {
                re = "选项";
            } else if (e == 1) {
                re = "说明";
            }
            return re;
        }

        public int getRowCount() {
            return 4;
        }

        public boolean isCellEditable(int r, int c) {
            if (c == 1) {
                return false;
            }
            return true;
        }

        public void setValueAt(Object arg0, int r, int c) {
            if (arg0 == null) {
                return;
            }
            if (c == 0) {
                if (r == 0) {
                    customerpanel = arg0.toString();
                } else if (r == 1) {
                    uiinterceptor = arg0.toString();
                } else if (r == 2) {
                    bsinterceptor = arg0.toString();
                } else if (r == 3) {
                    showsysbutton = arg0.toString();
                }

            }
        }

        public Object getValueAt(int r, int c) {
            if (c == 0) {
                if (r == 0) {
                    return customerpanel;
                } else if (r == 1) {
                    return uiinterceptor;
                } else if (r == 2) {
                    return bsinterceptor;
                } else if (r == 3) {
                    return showsysbutton;
                }
            } else {
                if (r == 0) {
                    return "自定义面板";
                } else if (r == 1) {
                    return uiInformation();
                } else if (r == 2) {
                    return bsInformation();
                } else if (r == 3) {
                    return "是否显示系统按钮栏";
                }
            }
            return "";
        }

    }

    public class MyCellEditor extends AbstractCellEditor implements TableCellEditor {
        JTextField text = null;
        Object[] items = new Object[] {"是", "否"};
        JComboBox showsysbtn = new JComboBox(items);
        boolean onselected = false;
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col) {
            if (row == 3 && col == 0) {
                onselected = true;
                if (value instanceof String) {
                    if ( ( (String) value).equals("是")) {
                        showsysbtn.setSelectedIndex(0);
                    } else {
                        showsysbtn.setSelectedIndex(1);
                    }
                    return showsysbtn;
                }
            }
            text = new JTextField(value.toString());
            return text;
        }

        public Object getCellEditorValue() {
            if (onselected) {
                return showsysbtn.getSelectedItem();
            }
            return text.getText();
        }
    }

    public String getUIInterceptor() {
        return this.uiinterceptor;
    }

    public String getBSInterceptor() {
        return this.bsinterceptor;
    }

    public String getCustomerpanel() {
        return this.customerpanel;
    }

    public String isShowSysBtn() {
        return this.showsysbutton;
    }

    public void setShowSysBtn(String _show) {
        this.showsysbutton = _show;
    }

    public void setUIInterceptor(String _ui) {
        this.uiinterceptor = _ui;
    }

    public void setBSInterceptor(String _bs) {
        this.bsinterceptor = _bs;
    }

    public void setCustomerpanel(String _customer) {
        this.customerpanel = _customer;
    }

    public void stopTableEditing() {
        interceptortable.editingStopped(new ChangeEvent(interceptortable));
    }

    public JTable getInterceptorTable() {
        return interceptortable;
    }

    public abstract VectorMap getParameters();

    public abstract void stopEdit();

    protected abstract String uiInformation();

    protected abstract String bsInformation();
}
/**************************************************************************
 * $RCSfile: AbstractTempletRefPars.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:01 $
 *
 * $Log: AbstractTempletRefPars.java,v $
 * Revision 1.2  2007/05/31 07:39:01  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:14:29  qilin
 * no message
 *
 * Revision 1.3  2007/02/27 06:57:19  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:23:45  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/