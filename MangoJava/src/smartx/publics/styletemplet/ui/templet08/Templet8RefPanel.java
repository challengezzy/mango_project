/**************************************************************************
 * $RCSfile: Templet8RefPanel.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:05 $
 **************************************************************************/
package smartx.publics.styletemplet.ui.templet08;

import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import smartx.framework.common.ui.*;
import smartx.framework.common.vo.*;
import smartx.publics.styletemplet.ui.*;


public class Templet8RefPanel extends AbstractTempletRefPars {
    private VectorMap paras = new VectorMap();
    ArrayList keys = new ArrayList();
    ArrayList values = new ArrayList();
    JTable table = null;
    private HashMap localname = new HashMap();
    public VectorMap getParameters() {
        paras.clear();
        if (keys.size() > 0) {
            for (int i = 0; i < keys.size(); i++) {
//				if(keys.get(i).equals("排列方向"))
//				{
//					paras.put(localname.get(keys.get(i)),values.get(i).equals("上下")?"1":"0");
//					continue;
//				}
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

    public Templet8RefPanel(String initString) {
        init(initString);
    }

    public Templet8RefPanel() {
        init("");
    }

    private void init(String initstring) {
        if (initstring.equals("")) {
            paras.put("parenttemplete_code", "");
            localname.put("主表编码", "parenttemplete_code");
            keys.add("主表编码");
            values.add("");

            paras.put("parent_pkname", "");
            localname.put("主表主键", "parent_pkname");
            keys.add("主表主键");
            values.add("");
            paras.put("childtemplete_code", "");
            localname.put("子表编码", "childtemplete_code");
            keys.add("子表编码");
            values.add("");
            paras.put("child_pkname", "");
            localname.put("子表主键", "child_pkname");
            keys.add("子表主键");
            values.add("");

            paras.put("child_forpkname", "");
            localname.put("子表外键", "child_forpkname");
            keys.add("子表外键");
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
                if (prefix.equals("parenttemplete_code")) {
                    localname.put("主表编码", prefix);
                    keys.add("主表编码");
                } else if (prefix.equals("childtemplete_code")) {
                    localname.put("子表编码", prefix);
                    keys.add("子表编码");
                }

                else if (prefix.equals("parent_pkname")) {
                    localname.put("主表主键", prefix);
                    keys.add("主表主键");
                } else if (prefix.equals("child_pkname")) {
                    localname.put("子表主键", prefix);
                    keys.add("子表主键");
                } else if (prefix.equals("child_forpkname")) {
                    localname.put("子表外键", prefix);
                    keys.add("子表外键");
                }

                else {
                    localname.put(prefix, prefix);
                    keys.add(prefix);
                }
                values.add(subfix);

            }
        }
        MyTableModel model = new MyTableModel();
        table = new JTable(model);
        table.setRowHeight(20);
        Templet8CellEditor editor = new Templet8CellEditor();
        table.getColumnModel().getColumn(0).setCellEditor(editor);
        table.getColumnModel().getColumn(1).setCellEditor(editor);
        TempletCellRender cellrendor = new TempletCellRender();
        table.getColumnModel().getColumn(0).setCellRenderer(cellrendor);
        table.getColumnModel().getColumn(2).setCellRenderer(cellrendor);
        table.getColumnModel().getColumn(3).setCellRenderer(cellrendor);
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
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
                if (keys.get(r).equals("主表编码") || keys.get(r).equals("子表编码") || keys.get(r).equals("主表主键")
                    || keys.get(r).equals("子表主键")
                    || keys.get(r).equals("子表外键")) {

                    return new Boolean(true);
                } else {
                    return new Boolean(false);
                }
            }
            if (c == 2) {
                if (keys.get(r).equals("主表编码")) {
                    return "主表的模板编码";
                }
                if (keys.get(r).equals("子表编码")) {
                    return "子表的模板编码";
                }
                if (keys.get(r).equals("主表主键")) {
                    return "主表的主键";
                }
                if (keys.get(r).equals("子表主键")) {
                    return "子表的主键";
                }
                if (keys.get(r).equals("子表外键")) {
                    return "子表与主表关联字段";
                } else {
                    return "";
                }
            }
            if (c == 0) {
                re = keys.get(r);
            }
            if (c == 1) {
//				if(keys.get(r).toString().equals("排列方向"))
//				{
//					if(values.get(r).equals("1"))
//						re="上下";
//					else
//						re = "左右";
//				}
//				else
                re = values.get(r);
            }
            return re;
        }

        public boolean isCellEditable(int r, int c) {
            if ( ( (keys.get(r).equals("主表编码") || keys.get(r).equals("子表编码") || keys.get(r).equals("主表主键")
                    || keys.get(r).equals("子表主键")
                    || keys.get(r).equals("子表外键")) && c == 0) || c == 2 || c == 3) {
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
        return "BS拦截器,需要实现IBSIntercept_08";
    }

    protected String uiInformation() {
        return "UI拦截器,需要实现IUIIntercept_08";
    }
}
/**************************************************************************
 * $RCSfile: Templet8RefPanel.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:39:05 $
 *
 * $Log: Templet8RefPanel.java,v $
 * Revision 1.2  2007/05/31 07:39:05  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:14:28  qilin
 * no message
 *
 * Revision 1.4  2007/03/21 07:48:53  sunxf
 * *** empty log message ***
 *
 * Revision 1.3  2007/02/27 06:57:19  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:48:33  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/