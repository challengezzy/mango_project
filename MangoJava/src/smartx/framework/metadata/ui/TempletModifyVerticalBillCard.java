/**************************************************************************
 * $RCSfile: TempletModifyVerticalBillCard.java,v $  $Revision: 1.2.8.2 $  $Date: 2009/02/02 16:12:54 $
 **************************************************************************/
package smartx.framework.metadata.ui;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import smartx.framework.common.ui.*;
import smartx.framework.common.vo.*;
import smartx.framework.metadata.ui.componentscard.*;
import smartx.framework.metadata.vo.*;


public class TempletModifyVerticalBillCard extends JPanel {
    private static final long serialVersionUID = 1L;

    Vector v_compents = new Vector();

    String str_templetcode = null;

    Pub_Templet_1VO templetVO = null;

    Pub_Templet_1_ItemVO[] templetItemVOs = null;

    KeyListener valueListener = null;

    CombChangeListener comblistener = null;

    Border redborder = BorderFactory.createLineBorder(Color.red);

    Border emptyborder = BorderFactory.createEmptyBorder();

    TempletModify modify = null;

    MouseListener mouselistener = null;

    public static boolean change = false;

    private ArrayList UICheckBoxPanelList = new ArrayList();

    public TempletModifyVerticalBillCard(TempletModify parent, String _templetcode) {
        this.modify = parent;
        this.str_templetcode = _templetcode;
        try {
            templetVO = UIUtil.getPub_Templet_1VO(str_templetcode);
        } catch (Exception e) {
            e.printStackTrace();
        } // 取得单据模板VO
        templetItemVOs = templetVO.getItemVos(); // 各项
        initialize();
    }

    /**
     * 初始化页面
     *
     */
    private void initialize() {
        this.setLayout(new BorderLayout());
        JScrollPane scrollPanel = new JScrollPane(getMainPanel());
        scrollPanel.setBackground(Color.WHITE); //
        this.add(scrollPanel, BorderLayout.CENTER);
        this.validate();
        this.updateUI();
    }

    public void refresh(Pub_Templet_1VO _templetvo) {
        this.removeAll();
        this.templetVO = _templetvo;
        templetItemVOs = templetVO.getItemVos(); // 各项
        initialize();
    }

    public void refresh() {
        this.removeAll();
        initialize();
    }

    private JPanel getMainPanel() {
        JPanel mainPanel = new JPanel();
        int li_height = getCardPaneHeight();

        mainPanel.setPreferredSize(new Dimension(200, li_height)); //
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(new JLabel("       "));
        for (int i = 0; i < templetItemVOs.length; i++) {
            String str_type = templetItemVOs[i].getItemtype();

            if (str_type.equals("文本框")) {
                TextFieldPanel panel = new TextFieldPanel(templetItemVOs[i]);
                if (templetItemVOs[i].getItemkey() != null &&
                    templetItemVOs[i].getItemkey().equalsIgnoreCase(templetVO.getPkname())) {
                    panel.setEditable(false);
                    panel.setValue("主键自动增长");
                } else if (templetItemVOs[i].getItemkey() != null &&
                           templetItemVOs[i].getItemkey().equalsIgnoreCase("PK_PUB_TEMPLET_1")) {
                    panel.setEditable(false);
                }
                // 为竖立CARD添加事件监听
                addTextFieldValueListener(panel);
                mainPanel.add(panel); //
                v_compents.add(panel); // 数据中也加入
            } else if (str_type.equals("数字框")) {
                NumberFieldPanel panel = new NumberFieldPanel(templetItemVOs[i]);
                mainPanel.add(panel); //
                addNumFieldValueListener(panel);
                v_compents.add(panel);
            } else if (str_type.equals("密码框")) {
                PasswordFieldPanel panel = new PasswordFieldPanel(templetItemVOs[i]);
                mainPanel.add(panel); //
                v_compents.add(panel);
            } else if (str_type.equals("下拉框")) {
                ComBoxPanel panel = new ComBoxPanel(templetItemVOs[i]);
                // if(isVertList)//为竖立CARD添加事件监听
                addComboChangeListener(panel);
                mainPanel.add(panel);
                v_compents.add(panel);
            } else if (str_type.equals("参照")) {
                UIRefPanel panel = new UIRefPanel(templetItemVOs[i]);
                mainPanel.add(panel); //
                v_compents.add(panel);
            } else if (str_type.equals("多行文本框")) {
                TextAreaPanel panel = new TextAreaPanel(templetItemVOs[i]);
                mainPanel.add(panel); //
                v_compents.add(panel);
            } else if (str_type.equals("日历")) {
                UIDateTimePanel panel = new UIDateTimePanel(templetItemVOs[i]);

                mainPanel.add(panel); //
                v_compents.add(panel);
            } else if (str_type.equals("文件选择框")) {
                UIFilePathPanel panel = new UIFilePathPanel(templetItemVOs[i]);
                mainPanel.add(panel); //

                v_compents.add(panel);
            } else if (str_type.equals("颜色")) {
                UIColorPanel panel = new UIColorPanel(templetItemVOs[i]);
                mainPanel.add(panel); //

                v_compents.add(panel);
            } else if (str_type.equals("大文本框")) {
                UITextAreaPanel panel = new UITextAreaPanel(templetItemVOs[i]);
                mainPanel.add(panel); //

                v_compents.add(panel);
            } else if (str_type.equals("计算器")) {
                // JPanel panel = new UICalculatorPanel(templetItemVOs[i]);
                // mainPanel.add(panel);
                // v_compents.add(panel);
            } else if (str_type.equals("时间")) {
                UITimeSetPanel panel = new UITimeSetPanel(templetItemVOs[i]);
                mainPanel.add(panel); //

                v_compents.add(panel);
            } else if (str_type.equals("勾选框")) {
                UICheckBoxPanel panel = new UICheckBoxPanel(templetItemVOs[i]);

                UICheckBoxPanelList.add(panel);

            } else {

            }

        }
        for (int i = 0; i < UICheckBoxPanelList.size(); i++) {
            UICheckBoxPanel panel = (UICheckBoxPanel) UICheckBoxPanelList.get(i);
            mainPanel.add(panel); //
            v_compents.add(panel);
        }

        return mainPanel;
    }

    private class CombChangeListener implements ItemListener {

        public void itemStateChanged(ItemEvent e) {
            if (!change) {
                return;
            }
            ComBoxPanel panel = (ComBoxPanel) ( (JComboBox) e.getSource()).getParent();
            resetItemType(panel.getValue());
        }

    }

    public void resetItemType(String _value) {
        String itemkey = modify.getSelectedVO();
        if (itemkey != null) {
            Pub_Templet_1_ItemVO item = modify.getBillCard().getItemVO(itemkey);
            item.setItemtype(_value);
            modify.refreshCard(modify.getBillCard().getTempletVO());
        }
    }

    private class ValueListener extends KeyAdapter {
        public void keyReleased(KeyEvent e) { // 只响应在显示界面上有变化的属性.其它不与更改.
            if (e.getSource() instanceof JFormattedTextField) {
                if ( ( (JFormattedTextField) e.getSource()).getText().equals("")) {
                    return;
                }
                NumberFieldPanel panel = (NumberFieldPanel) ( (JFormattedTextField) e.getSource()).getParent();
                if (panel.getKey().equals("SHOWORDER")) {
                    if ( (e.getKeyCode() >= KeyEvent.VK_0 || e.getKeyCode() <= KeyEvent.VK_9) && e.getKeyChar() != '\n') { // 更改itemvo的显示顺序.
                        String itemkey = modify.getSelectedVO();
                        if (itemkey != null) {
                            Pub_Templet_1_ItemVO item = modify.getBillCard().getItemVO(itemkey);
                            String order = ( (JTextField) e.getSource()).getText();
                            item.setShoworder(new Integer(order));
                            modify.refreshCard(modify.getBillCard().getTempletVO());
                        }
                    }
                } else if (panel.getKey().equals("CARDWIDTH")) {
                    if ( (e.getKeyCode() >= KeyEvent.VK_0 || e.getKeyCode() <= KeyEvent.VK_9) && e.getKeyChar() != '\n') { // 更改itemvo的列宽度..
                        String itemkey = modify.getSelectedVO();
                        if (itemkey != null) {
                            Pub_Templet_1_ItemVO item = modify.getBillCard().getItemVO(itemkey);
                            item.setCardwidth(new Integer( ( (JTextField) e.getSource()).getText()));
                            modify.refreshCard(modify.getBillCard().getTempletVO());
                        }
                    }
                }
            } else if (e.getSource() instanceof JTextField) {
                if ( ( (JTextField) e.getSource()).getText().equals("")) {
                    return;
                }
                TextFieldPanel panel = (TextFieldPanel) ( (JTextField) e.getSource()).getParent();
                if (panel.getKey().equals("ITEMNAME")) {

                    if (e.getKeyChar() != '\n') { // 实时更改名称
                        String itemkey = modify.getSelectedVO();
                        if (itemkey != null) {
                            Pub_Templet_1_ItemVO item = modify.getBillCard().getItemVO(itemkey);
                            item.setItemname( ( (JTextField) e.getSource()).getText());
                            modify.setName( ( (JTextField) e.getSource()).getText());
                        }
                    }
                }
            }

            // else if (panel.getKey().equals("LISTWIDTH"))
            // {
            // if
            // ((e.getKeyCode()>=KeyEvent.VK_0||e.getKeyCode()<=KeyEvent.VK_9)&&e.getKeyChar()
            // != '\n')
            // { // 更改itemvo的列宽度..
            // Pub_Templet_1_ItemVO item = modify.getSelectedVO();
            // if(item!=null)
            // {
            // item.setListwidth(new Integer(((JTextField)
            // e.getSource()).getText()));
            // modify.refreshCard(templetVO);
            // }
            // }
            // }

        }
    }

    private void addTextFieldValueListener(TextFieldPanel panel) {
        if (valueListener == null) {
            valueListener = new ValueListener();
        }

        panel.getTextField().addKeyListener(valueListener);

    }

    private void addNumFieldValueListener(NumberFieldPanel panel) {
        if (valueListener == null) {
            valueListener = new ValueListener();
        }

        panel.getTextField().addKeyListener(valueListener);

    }

    private void addComboChangeListener(ComBoxPanel panel) {
        if (comblistener == null) {
            comblistener = new CombChangeListener();
        }
        //		if (mouselistener == null)
        //			mouselistener = new MyMouseListener();

        panel.getComBox().addItemListener(comblistener);
    }

    private int getCardPaneHeight() {
        int li_height = 0;
        int li_textareacount = 0;
        for (int i = 0; i < templetItemVOs.length; i++) {
            if (templetItemVOs[i].getItemtype().equals("多行文本框")) {
                li_height = li_height + 80;
                li_textareacount++;
            } else {
                li_height = li_height + 23;
            }
        }
        return li_height / 2; // + li_textareacount * 60;
    }

    /**
     * 刷新数据
     *
     * @param _condition
     */
    public void refreshData(String _condition) {
        change = false;
        String str_sql = "select * from " + templetVO.getTablename() + " where " + _condition; // 拼成SQL
        //		System.out.println("查询SQL：" + str_sql); //
        queryData(str_sql);
        change = true;
    }

    private String getDataSourceName() {
        if (templetVO.getDatasourcename() == null || templetVO.getDatasourcename().trim().equals("null") ||
            templetVO.getDatasourcename().trim().equals("")) {
            return NovaClientEnvironment.getInstance().getDefaultDatasourceName(); // 默认数据源
        } else {
            return new FrameWorkTBUtil().convertExpression(NovaClientEnvironment.getInstance(),
                templetVO.getDatasourcename()); // 算出数据源!!
        }
    }

    public void queryData(String _sql) {
        queryDataByDS(getDataSourceName(), _sql); //

    }

    //
    public void queryDataByDS(String _dsName, String _sql) {
        Object[] objs = null;
        try {
            objs = UIUtil.getBillCardDataByDS(_dsName, _sql, this.templetVO, NovaClientEnvironment.getInstance());
        } catch (NovaRemoteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (objs != null) {
            setValue(objs); // 设置数据
        }
    }

    public HashMap getAllObjectValuesWithHashMap() {
        HashMap map = new HashMap();
        for (int i = 0; i < templetItemVOs.length; i++) {
            if (getCompentObjectValue(templetItemVOs[i].getItemkey()) instanceof ComBoxItemVO) {
                ComBoxItemVO itemvo = (ComBoxItemVO) getCompentObjectValue(templetItemVOs[i].getItemkey());
                map.put(templetItemVOs[i].getItemkey(), itemvo.getId());
                continue;
            }

            map.put(templetItemVOs[i].getItemkey(), getCompentObjectValue(templetItemVOs[i].getItemkey()));
        }
        return map;
    }

    public HashMap getValueAsItemVO() {
        return this.getAllObjectValuesWithHashMap();

    }

    public void setValue(HashMap _map) {
        reset();
        for (int i = 0; i < templetItemVOs.length; i++) {
            String str_key = templetItemVOs[i].getItemkey();
            INovaCompent compent = getCompentByKey(str_key); //
            modify.getBillCard().getCompentByKey(str_key);
            if (compent instanceof ComBoxPanel) {
                ComBoxPanel c = (ComBoxPanel) compent;
                c.setValue(new String( (String) _map.get(str_key)));
                continue;
            }
            compent.setObject(_map.get(str_key)); //
        }
    }

    public String getRefDescSQL(String _allrefdesc) {
        String str_type = null;
        String str_sql = null;
        int li_pos = _allrefdesc.indexOf(":"); //
        if (li_pos < 0) {
            str_type = "TABLE";
        } else {
            str_type = _allrefdesc.substring(0, li_pos).toUpperCase(); //
        }

        if (str_type.equalsIgnoreCase("TABLE")) {
            if (li_pos < 0) {
                str_sql = _allrefdesc;
            } else {
                str_sql = _allrefdesc.substring(li_pos + 1, _allrefdesc.length()); //
            }
        } else if (str_type.equalsIgnoreCase("TREE")) {
            _allrefdesc = _allrefdesc.trim(); //截去空格
            String str_remain = _allrefdesc.substring(li_pos + 1, _allrefdesc.length()); //
            int li_pos_tree_1 = str_remain.indexOf(";"); //
            str_sql = str_remain.substring(0, li_pos_tree_1); //SQL语句
        } else if (str_type.equalsIgnoreCase("CUST")) {
        }
        return str_sql;
    }

    public void reset() {
        INovaCompent[] compents = (INovaCompent[]) v_compents.toArray(new INovaCompent[0]);
        for (int i = 0; i < compents.length; i++) {
            compents[i].reset(); // 设置值
        }
    }

    public void setEditable(boolean _bo) {
        INovaCompent[] compents = (INovaCompent[]) v_compents.toArray(new INovaCompent[0]);
        for (int i = 0; i < compents.length; i++) {
            compents[i].setEditable(_bo); // 设置值
        }
    }

    public String getUpdateSQL() {
        String sb_sql = "";
        String str_table = templetVO.getTablename();
        String str_pkname = templetVO.getPkname();
        sb_sql = sb_sql + "update " + str_table + " set ";
        for (int i = 0; i < templetItemVOs.length; i++) {
            String str_itemkey = templetItemVOs[i].getItemkey();
            String str_columnname = templetItemVOs[i].getItemkey(); //
            if (str_columnname != null && !str_columnname.trim().equals("null") && !str_columnname.trim().equals("")) {
                if (!str_itemkey.equals(str_pkname) && !str_itemkey.equals("LASTMODIFYTIME")) { // 如果是主键，则跳过
                    sb_sql = sb_sql + templetItemVOs[i].getItemkey() + " = '" + convert(getCompentRealValue(str_itemkey)) +
                        "',"; //
                }
            }
        }

        sb_sql = sb_sql.substring(0, sb_sql.length() - 1); // 去掉最后一个逗号
        sb_sql = sb_sql + " where " + str_pkname + " = '" + getCompentRealValue(str_pkname) + "'";
        return sb_sql;
    }

    public void saveData() {
        String str_sql = getUpdateSQL();
        try {
            UIUtil.executeUpdateByDS(null, str_sql);
            // JOptionPane.showMessageDialog(this, "保存数据成功!!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "保存数据失败,原因:" + e.getMessage());
        }
    }

    public String getCompentRealValue(String _key) {
        INovaCompent[] compents = (INovaCompent[]) v_compents.toArray(new INovaCompent[0]);
        for (int i = 0; i < compents.length; i++) {
            if (compents[i].getKey().equalsIgnoreCase(_key)) {
                return String.valueOf(compents[i].getObject());
            }
        }
        return "";
    }

    public Object getCompentObjectValue(String _key) {
        INovaCompent[] compents = (INovaCompent[]) v_compents.toArray(new INovaCompent[0]);
        for (int i = 0; i < compents.length; i++) {
            if (compents[i].getKey().equalsIgnoreCase(_key)) {
                if (compents[i].getObject() instanceof String) {
                    return new String( (String) compents[i].getObject());
                } else if (compents[i].getObject() instanceof ComBoxItemVO) {
                    ComBoxItemVO itemvo = (ComBoxItemVO) compents[i].getObject();
                    return new String(itemvo.getId());
                }
            }
        }
        return null;
    }

    public void setValue(Object[] _objs) {
        reset();
        for (int i = 0; i < _objs.length; i++) {
            INovaCompent compent = getCompentByIndex(i);
            if (compent != null) {
                compent.setObject(_objs[i]);
            }
        }
    }

    public void setCompentObjectValue(String _key, Object _obj) {
        INovaCompent compent = getCompentByKey(_key);
        compent.setObject(_obj); //
    }

    public INovaCompent[] getAllCompents() {
        return (INovaCompent[]) v_compents.toArray(new INovaCompent[0]);
    }

    public INovaCompent getCompentByIndex(int _index) {
        String key = templetItemVOs[_index].getItemkey();
        return getCompentByKey(key);
    }

    public INovaCompent getCompentByKey(String _key) {
        INovaCompent[] compents = (INovaCompent[]) v_compents.toArray(new INovaCompent[0]);
        for (int i = 0; i < compents.length; i++) {
            if (compents[i].getKey().equalsIgnoreCase(_key)) {
                return compents[i];
            }
        }
        return null;
    }

    private String convert(String _str) {
        if (_str == null) {
            return "";
        }
        return _str.replaceAll("'", "''");
    }

    public Pub_Templet_1VO getTempletVO() {
        return this.templetVO;
    }
}
/**************************************************************************
 * $RCSfile: TempletModifyVerticalBillCard.java,v $  $Revision: 1.2.8.2 $  $Date: 2009/02/02 16:12:54 $
 *
 * $Log: TempletModifyVerticalBillCard.java,v $
 * Revision 1.2.8.2  2009/02/02 16:12:54  wangqi
 * *** empty log message ***
 *
 * Revision 1.2.8.1  2008/11/05 05:21:09  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2007/05/31 07:38:16  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:01:36  qilin
 * no message
 *
 * Revision 1.7  2007/03/07 02:01:55  shxch
 * *** empty log message ***
 *
 * Revision 1.6  2007/03/02 05:28:05  shxch
 * *** empty log message ***
 *
 * Revision 1.5  2007/03/02 05:16:41  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/03/02 04:53:39  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/03/01 09:06:55  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:56:14  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
