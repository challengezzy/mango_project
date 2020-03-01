/**************************************************************************
 * $RCSfile: Templet1RefPanel.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:02 $
 **************************************************************************/
package smartx.publics.styletemplet.ui.templet01;

import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import smartx.framework.common.ui.*;
import smartx.framework.common.vo.*;
import smartx.publics.styletemplet.ui.*;


public class Templet1RefPanel extends AbstractTempletRefPars {
    private VectorMap paras = new VectorMap();

    ArrayList keys = new ArrayList();

    ArrayList values = new ArrayList();

    JTable table = null;
    private HashMap localname = new HashMap();

    public VectorMap getParameters() {
        paras.clear();
        if (keys.size() > 0) {
            for (int i = 0; i < keys.size(); i++) {
                paras.put(localname.get(keys.get(i)) == null ? keys.get(i) : localname.get(keys.get(i)), values.get(i));
            }
        }
        if (!super.getCustomerpanel().equals("")) {
            paras.put("CUSTOMERPANEL", super.getCustomerpanel());
        }
        if (!super.getUIInterceptor().equals("")) {
            paras.put("UIINTERCEPTOR", super.getUIInterceptor());
        }
        if (!super.getBSInterceptor().equals("")) {
            paras.put("BSINTERCEPTOR", super.getBSInterceptor());
        }
        if (!super.isShowSysBtn().equals("")) {
            paras.put("SHOWSYSBUTTON", super.isShowSysBtn());
        }
        return paras;
    }

    public Templet1RefPanel(String initString) {
        init(initString);
    }

    public Templet1RefPanel() {
        init("");
    }

    private void init(String initstring) {
        if (initstring.equals("")) {
            paras.put("templet_code", "");
            localname.put("模板编码", "templet_code");
            keys.add("模板编码");
            values.add("");
        } else {
            String[] str_items = initstring.split(";");
            for (int i = 0; i < str_items.length; i++) {
                String item = str_items[i];
                int li_pos = item.indexOf("=");
                String prefix = item.substring(0, li_pos);
                String subfix = item.substring(li_pos + 1, item.length());
                if (prefix.equals("CUSTOMERPANEL")) {
                    super.setCustomerpanel(subfix);
                    continue;
                } else if (prefix.equals("UIINTERCEPTOR")) {
                    super.setUIInterceptor(subfix);
                    continue;
                } else if (prefix.equals("BSINTERCEPTOR")) {
                    super.setBSInterceptor(subfix);
                    continue;
                } else if (prefix.equals("SHOWSYSBUTTON")) {
                    super.setShowSysBtn(subfix);
                    continue;
                }
                paras.put(prefix, subfix); //
                localname.put(prefix.equals("templet_code") ? "模板编码" : prefix, prefix);
                keys.add(prefix.equals("templet_code") ? "模板编码" : prefix);
                values.add(subfix);

            }
        }

        MyTableModel model = new MyTableModel();
        table = new JTable(model);
        table.setRowHeight(20);

        Templet1CellEditor celleditor = new Templet1CellEditor();
        table.getColumnModel().getColumn(1).setCellEditor(celleditor);
        table.getColumnModel().getColumn(0).setCellEditor(celleditor);
        TempletCellRender cellrendor = new TempletCellRender();
        table.getColumnModel().getColumn(0).setCellRenderer(cellrendor);
        table.getColumnModel().getColumn(2).setCellRenderer(cellrendor);
        table.getColumnModel().getColumn(3).setCellRenderer(cellrendor);
        table.getColumnModel().getColumn(0).setPreferredWidth(20);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(10);
        super.initialization(setContent());
    }

    private VectorMap setContent() {
        VectorMap map = new VectorMap();
        map.put(NovaConstants.STRING_REFPANEL_COMMON_TITLE, table);
        return map;
    }

    public void stopEdit() {
        table.editingStopped(new ChangeEvent(table));
        super.stopTableEditing();
    }

    private class MyTableModel extends AbstractTableModel {
        public void addRow() {
            keys.add("");
            values.add("");
            table.updateUI();
        }

        public void removeRow(int r) {
            keys.remove(r);
            values.remove(r);
            table.updateUI();
        }

        public Class getColumnClass(int e) {
            if (e == 3) {
                return Boolean.class;
            }
            return String.class;
        }

        public int getColumnCount() {
            return 4;
        }

        public String getColumnName(int e) {
            String re = "";
            if (e == 0) {
                re = "参数";
            } else if (e == 1) {
                re = "值";
            } else if (e == 2) {
                re = "说明";
            } else if (e == 3) {
                re = "必需";
            }
            return re;
        }

        public int getRowCount() {
            return keys.size();
        }

        public Object getValueAt(int r, int c) {
            Object re = "";
            if (c == 3) {
                if (keys.get(r).equals("模板编码")) {
                    return new Boolean(true);
                } else {
                    return new Boolean(false);
                }
            }
            if (c == 2) {
                if (keys.get(r).equals("模板编码")) {
                    return "用于生成数据的模板编码";
                }
            }
            if (c == 0) {
                re = keys.get(r);
            } else if (c == 1) {
                re = values.get(r);
            }
            return re;
        }

        public boolean isCellEditable(int r, int c) {
            if (c != 1) {
                return false;
            }
            return true;
        }

        public void setValueAt(Object arg0, int r, int c) {
            if (c == 0) {
                keys.set(r, arg0.toString());
            } else if (c == 1) {
                if (arg0 == null) {
                    values.set(r, "");
                } else {
                    values.set(r, arg0.toString());
                }
            }
        }

    }

    protected String bsInformation() {
        return "BS拦截器,需要实现IBSIntercept_01";
    }

    protected String uiInformation() {
        return "UI拦截器,需要实现IUIntercept_01";
    }
}
/**************************************************************************
 * $RCSfile: Templet1RefPanel.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:02 $
 *
 * $Log: Templet1RefPanel.java,v $
 * Revision 1.2  2007/05/31 07:39:02  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:14:26  qilin
 * no message
 *
 * Revision 1.4  2007/03/21 07:48:53  sunxf
 * *** empty log message ***
 *
 * Revision 1.3  2007/02/27 06:57:20  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:48:33  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/