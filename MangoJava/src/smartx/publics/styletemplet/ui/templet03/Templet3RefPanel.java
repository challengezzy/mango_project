/**************************************************************************
 * $RCSfile: Templet3RefPanel.java,v $  $Revision: 1.3.2.3 $  $Date: 2010/01/06 07:52:15 $
 **************************************************************************/

package smartx.publics.styletemplet.ui.templet03;

import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import smartx.framework.common.ui.*;
import smartx.framework.common.vo.*;
import smartx.publics.styletemplet.ui.*;


public class Templet3RefPanel extends AbstractTempletRefPars {

    private VectorMap paras = new VectorMap();
    ArrayList keys = new ArrayList();
    ArrayList values = new ArrayList();
    JTable table = null;
    private HashMap localname = new HashMap();
    Templet3CellEditor editor = null;
    JComboBox cascadeondelete = null;
    JComboBox isloadall = null;
    public VectorMap getParameters() {
        paras.clear();
        if (keys.size() > 0) {
            for (int i = 0; i < keys.size(); i++) {
                paras.put(localname.get(keys.get(i)) == null ? keys.get(i) : localname.get(keys.get(i)),
                          values.get(i));
            }
        }
        // paras.put("sql", "select * from "+editor.getSelectedTablename());
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

    public Templet3RefPanel(String initString) {
        init(initString);
    }

    public Templet3RefPanel() {
        init("");
    }

    private void init(String initstring) {
		
        paras.put("templet_code", "");
        localname.put("树模板编码", "templet_code");
        keys.add("树模板编码");
        
        paras.put("parentpk", "");
        localname.put("树父主键", "parentpk");
        keys.add("树父主键");
        
        paras.put("pk", "");
        localname.put("树主键", "pk");
        keys.add("树主键");
        
        paras.put("fetchkey", "");
        localname.put("树检索字段", "fetchkey");
        keys.add("树检索字段");
        
        paras.put("treetitle", "");
        localname.put("根节点名称", "treetitle");
        keys.add("根节点名称");
        
        paras.put("cascadeondelete", "否");
        localname.put("级联删除", "cascadeondelete");
        keys.add("级联删除");
        
        paras.put("isloadall", "是");
        localname.put("加载全部数据", "isloadall");
        keys.add("加载全部数据");
        
        if (!initstring.equals("")){ 
            String[] str_items = initstring.split(";");
            for (int i = 0; i < str_items.length; i++) {
                String item = str_items[i];
                int li_pos = item.indexOf("=");
                String prefix = item.substring(0, li_pos);
                String subfix = item.substring(li_pos + 1, item.length());
                if (prefix.equals("sql")) {
                    continue;
                }
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
                paras.put(prefix.toLowerCase(), subfix); //
            }            
        }
        values.clear();
        String[] keys=paras.getKeysAsString();
        for(int i=0;i<keys.length;i++){
        	values.add(paras.get(keys[i]));
        }
        
        
        
        MyTableModel model = new MyTableModel();
        table = new JTable(model);
        table.setRowHeight(20);
        editor = new Templet3CellEditor();
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
                if (keys.get(r).equals("树模板编码") || keys.get(r).equals("树标题") || keys.get(r).equals("树主键")||keys.get(r).equals("根节点名称")
                    || keys.get(r).equals("树父主键") || keys.get(r).equals("级联删除") || keys.get(r).equals("树检索字段")) {
                    return new Boolean(true);
                }
                return new Boolean(false);
            }
            if (c == 2) {
                if (keys.get(r).equals("树模板编码")) {
                    return "用于生成树数据的模板编码";
                }
                if (keys.get(r).equals("树标题")) {
                    return "树最外层节点显示的名称";
                }if(keys.get(r).equals("节点名称")){
                	return "用于查询的字段名称";
                }
                if (keys.get(r).equals("树主键")) {
                    return "树模板的标识主键字段";
                }
                if (keys.get(r).equals("树父主键")) {
                    return "树模板的标识父亲主键的字段";
                }
                if (keys.get(r).equals("加载全部数据")) {
                    return "构造树时是否一次性全部加载";
                } 
                if (keys.get(r).equals("")){
                	
                }else {
                    return "";
                }
            }
            if (c == 0) {
                re = keys.get(r);
            }
            if (c == 1) {
                re = values.get(r);
            }
            return re;
        }

        public boolean isCellEditable(int r, int c) {
            if ( ( (keys.get(r).equals("树模板编码") || keys.get(r).equals("树标题") || keys.get(r).equals("树主键")||keys.get(r).equals("根节点名称")
                    || keys.get(r).equals("树父主键") || keys.get(r).equals("级联删除")|| keys.get(r).equals("加载全部数据")||keys.get(r).equals("树检索字段")) && c == 0)
                || c == 2 || c == 3) {
                return false;
            }
            return true;
        }

        public void setValueAt(Object arg0, int r, int c) {
            if (c == 0) {
                if (arg0 instanceof JComboBox) {
                    keys.set(r, arg0);
                } else {
                    keys.set(r, arg0.toString());
                }
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
        return "BS拦截器,需要实现IBSIntercept_03";
    }

    protected String uiInformation() {
        return "UI拦截器,需要实现IUIIntercept_03";
    }
}
/*******************************************************************************
 * $RCSfile: Templet3RefPanel.java,v $ $Revision: 1.3.2.3 $ $Date: 2007/02/27
 * 06:57:20 $
 *
 * $Log: Templet3RefPanel.java,v $
 * Revision 1.3.2.3  2010/01/06 07:52:15  wangqi
 * *** empty log message ***
 *
 * Revision 1.3.2.2  2010/01/05 10:20:36  wangqi
 * *** empty log message ***
 *
 * Revision 1.3.2.1  2009/09/01 08:25:34  yangjm
 * *** empty log message ***
 *
 * Revision 1.3  2008/01/15 09:02:36  wangqi
 * 孙雪峰修改MR
 *
 * Revision 1.2  2007/05/31 07:39:03  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:14:27  qilin
 * no message
 *
 * Revision 1.4  2007/03/12 04:45:31  sunxf
 * *** empty log message ***
 * Revision 1.3 2007/02/27 06:57:20 shxch ***
 * empty log message ***
 *
 * Revision 1.2 2007/01/30 04:48:33 lujian *** empty log message ***
 *
 *
 ******************************************************************************/
