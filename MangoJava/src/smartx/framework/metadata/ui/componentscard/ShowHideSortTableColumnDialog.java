/**************************************************************************
 * $RCSfile: ShowHideSortTableColumnDialog.java,v $  $Revision: 1.4.8.1 $  $Date: 2009/10/16 03:48:33 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentscard;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import smartx.framework.common.ui.*;
import smartx.framework.metadata.ui.*;
import smartx.framework.metadata.ui.localcache.BillListColumnLocalSetting;
import smartx.framework.metadata.vo.*;



/**
 * 自定义列显示控制
 * @author Administrator
 *
 */
public class ShowHideSortTableColumnDialog extends NovaDialog {

    private static final long serialVersionUID = 5787311618948130477L;
	
    
    private String templetCode=null;
	private Pub_Templet_1_ItemVO[] templetItemVOs = null;

    private static final Font smallFont = new Font("宋体", Font.PLAIN, 12);


    private JList[] jl_column = null;

    private JButton[] jbt_operator = null;

    private DefaultListModel showModel = null;

    private DefaultListModel hiddenModel = null;

    private Vector showItem_vec = new Vector();

    private Vector hiddenItem_vec = new Vector();

    private int[] selectedIndex = null;

    private int return_type = 0;

    private String[] str_filterkeys = null;

    private ActionListener jbt_listener = null;

    private KeyAdapter jbt_adapter = null;

    /**
     *
     * @param _parent 父对象
     * @param _title 标题
     * @param _width 指定宽度
     * @param li_height 指定高度
     * @param _templetItemVOs 元原模板定义明细
     * @param _templetCode 元原模板代码
     * @param str_filterkeys 锁定列列表，这些字段是必须的。
     */
    public ShowHideSortTableColumnDialog(Container _parent, String _title,
                                         int _width, int li_height, Pub_Templet_1_ItemVO[] _templetItemVOs,
                                         String _templetCode, String[] str_filterkeys) {
        super(_parent, _title, _width, li_height);
        this.templetCode=_templetCode;
        this.templetItemVOs = _templetItemVOs;
        this.str_filterkeys = str_filterkeys;
        initialize();

    }

    /**
     * 初始化页面
     *
     */
    private void initialize() {
        Container con = getContentPane();
        con.setLayout(new BorderLayout());

        jbt_listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dealAcitonPerform(e);
            }
        };
        jbt_adapter = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                dealKeyPerform(e);
            }
        };
        jbt_operator = new JButton[10];
        jl_column = new JList[2];

        JPanel jpn_show = getListPanel("显示列", 0);
        showModel = (DefaultListModel) jl_column[0].getModel();

        jbt_operator[6] = getBtn(">>", new Dimension(60, 18));
        jbt_operator[7] = getBtn("<<", new Dimension(60, 18));

        Box rightAndLeftBox = Box.createVerticalBox();
        rightAndLeftBox.add(Box.createGlue());
        rightAndLeftBox.add(jbt_operator[6]);
        rightAndLeftBox.add(Box.createVerticalStrut(40));
        rightAndLeftBox.add(jbt_operator[7]);
        rightAndLeftBox.add(Box.createGlue());

        JPanel jpn_hidden = getListPanel("隐藏列", 1);
        hiddenModel = (DefaultListModel) jl_column[1].getModel();

        Box box = Box.createHorizontalBox();

        box.add(Box.createHorizontalStrut(20));
        box.add(jpn_show);
        box.add(rightAndLeftBox);
        box.add(jpn_hidden);
        box.add(Box.createHorizontalStrut(20));

        jbt_operator[8] = getBtn("确定", new Dimension(75, 20));
        jbt_operator[9] = getBtn("取消", new Dimension(75, 20));

        JPanel panel_south = new JPanel();
        panel_south.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 10));
        panel_south.setPreferredSize(new Dimension(200, 40));

        panel_south.add(jbt_operator[8]);
        panel_south.add(jbt_operator[9]);

        con.add(box, BorderLayout.CENTER);
        con.add(panel_south, BorderLayout.SOUTH);

        initList();
    }

    /**
     * 获得带有JList的显示列和隐藏列Panel
     *
     * @param _label_text
     * @param _list_index
     * @return
     */
    private JPanel getListPanel(String _label_text, int _list_index) {
        ListAndTableFactoty latf_show = new ListAndTableFactoty();
        JPanel jpn_temp = latf_show.getJSPList(_label_text);

        jl_column[_list_index] = latf_show.getList();
        jl_column[_list_index].addKeyListener(jbt_adapter);

        int li_base_index = _list_index * 4;
        jbt_operator[li_base_index] = getBtn("全选", new Dimension(60, 18));
        jbt_operator[li_base_index + 1] = getBtn("全消", new Dimension(60, 18));

        Box box_south = Box.createHorizontalBox();
        box_south.add(Box.createGlue());
        box_south.add(jbt_operator[li_base_index]);
        box_south.add(Box.createGlue());
        box_south.add(jbt_operator[li_base_index + 1]);
        box_south.add(Box.createGlue());
        if (li_base_index == 0) {
            jbt_operator[li_base_index + 2] = getBtn(UIUtil
                .getImage("images/platform/up.gif"), new Dimension(20, 20));
            jbt_operator[li_base_index + 3] = getBtn(UIUtil
                .getImage("images/platform/down.gif"), new Dimension(20, 20));

            Box box_east = Box.createVerticalBox();
            box_east.add(Box.createGlue());
            box_east.add(jbt_operator[li_base_index + 2]);
            box_east.add(Box.createGlue());
            box_east.add(jbt_operator[li_base_index + 3]);
            box_east.add(Box.createGlue());
            jpn_temp.add(box_east, BorderLayout.EAST);
        }
        jpn_temp.add(box_south, BorderLayout.SOUTH);

        return jpn_temp;
    }

    /**
     * 处理所有的键盘事件
     *
     * @param e
     */
    protected void dealKeyPerform(KeyEvent e) {
        Object _obj = e.getSource();
        if (_obj.equals(jbt_operator[0])) { // 显示列全选
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                onShowSelectAllButton();
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                onRight();
            }
        } else if (_obj.equals(jbt_operator[1])) { // 显示列全消
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                onShowCancelAllButton();
            }
        } else if (_obj.equals(jbt_operator[2])) { // 显示列上移
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                onUp();
            }
        } else if (_obj.equals(jbt_operator[3])) { // 显示列下移
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                onDown();
            }
        } else if (_obj.equals(jbt_operator[4])) { // 隐藏列全选
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                onHiddenSelectAllButton();
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                onLeft();
            }
        } else if (_obj.equals(jbt_operator[5])) { // 隐藏列全消
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                onHiddenCancelAllButton();
            }
        } else if (_obj.equals(jbt_operator[6])) { // 右移
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                onRight();
            }
        } else if (_obj.equals(jbt_operator[7])) { // 左移
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                onLeft();
            }
        } else if (_obj.equals(jbt_operator[8])) { // 确定
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                onConfirm();
            }
        } else if (_obj.equals(jbt_operator[9])) { // 取消
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                onCancel();
            }
        } else if (_obj.equals(jl_column[0])) { // JList右移
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                onRight();
            }
        } else if (_obj.equals(jl_column[1])) { // JList左移
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                onLeft();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) { // 处理所有的Escape按键事件
            onCancel();
        }
    }

    /**
     * 处理所有按钮被按下事件
     *
     * @param e
     */
    protected void dealAcitonPerform(ActionEvent e) {
        Object _obj = e.getSource();
        if (_obj.equals(jbt_operator[0])) { // 显示列全选
            onShowSelectAllButton();
        } else if (_obj.equals(jbt_operator[1])) { // 显示列全消
            onShowCancelAllButton();
        } else if (_obj.equals(jbt_operator[2])) { // 显示列上移
            onUp();
        } else if (_obj.equals(jbt_operator[3])) { // 显示列下移
            onDown();
        } else if (_obj.equals(jbt_operator[4])) { // 隐藏列全选
            onHiddenSelectAllButton();
        } else if (_obj.equals(jbt_operator[5])) { // 隐藏列全消
            onHiddenCancelAllButton();
        } else if (_obj.equals(jbt_operator[6])) { // 右移
            onRight();
        } else if (_obj.equals(jbt_operator[7])) { // 左移
            onLeft();
        } else if (_obj.equals(jbt_operator[8])) { // 确定
            onConfirm();
        } else if (_obj.equals(jbt_operator[9])) { // 取消
            onCancel();
        }
    }

    /**
     * 定制所有按钮
     *
     * @param _obj:String ||
     *            Icon
     * @param _demension:初始大小
     * @return
     */
    private JButton getBtn(Object _obj, Dimension _demension) {
        JButton jbt_temp = null;
        if (_obj instanceof String) {
            jbt_temp = new JButton(_obj.toString());
        } else if (_obj instanceof Icon) {
            jbt_temp = new JButton( (Icon) _obj);
        }
        jbt_temp.setFont(smallFont);
        jbt_temp.setPreferredSize(_demension);
        jbt_temp.addActionListener(jbt_listener);
        jbt_temp.addKeyListener(jbt_adapter);
        return jbt_temp;
    }

    protected void onHiddenCancelAllButton() {
        refreshList();
    }

    protected void onHiddenSelectAllButton() {
        int hidden_max_index = hiddenItem_vec.size();
        int hiddenIndexs[] = new int[hidden_max_index];
        for (int i = 0; i < hidden_max_index; i++) {
            hiddenIndexs[i] = i;
        }
        jl_column[1].setSelectedIndices(hiddenIndexs);
    }

    protected void onShowCancelAllButton() {
        refreshList();
    }

    protected void onShowSelectAllButton() {
        int show_max_index = showItem_vec.size();
        int showIndexs[] = new int[show_max_index];
        System.out.println("The size is: " + show_max_index);
        for (int i = 0; i < show_max_index; i++) {
            showIndexs[i] = i;
        }
        jl_column[0].setSelectedIndices(showIndexs);
    }

    public Vector getShowListItemVO() {
        if (showItem_vec != null) {
            return showItem_vec;
        }
        return null;
    }

    public Vector getHiddenListItemVo() {
        if (hiddenItem_vec != null) {
            return hiddenItem_vec;
        }
        return null;
    }

    private boolean isLocked(String _itemid) {
        int filterKeys_length = str_filterkeys.length;

        for (int i = 0; i < filterKeys_length; i++) {
            if (str_filterkeys[i].equals(_itemid)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 初始化JList，填装数据
     */
    private void initList() {
        String id = null;
        String name = null;
        String code = null;

        int showColumnCount = templetItemVOs.length;

        for (int i = 0; i < showColumnCount; i++) {
            id = templetItemVOs[i].getItemkey();
            name = templetItemVOs[i].getItemname();
            code = templetItemVOs[i].getItemname();

            if (isLocked(id)) {
                continue;
            }
            if (templetItemVOs[i].getListisshowable().booleanValue()) {
                JListItemVO temp_Item = new JListItemVO(id, name, code);
                showItem_vec.add(temp_Item);

                showModel.addElement(name);
            } else {
                JListItemVO temp_Item = new JListItemVO(id, name, code);
                hiddenItem_vec.add(temp_Item);

                hiddenModel.addElement(templetItemVOs[i].getItemname());
            }
        }
    }

    private void refreshList() {
        showModel.clear();
        hiddenModel.clear();

        for (int i = 0; i < showItem_vec.size(); i++) {
            JListItemVO temp_Item = (JListItemVO) showItem_vec.get(i);
            showModel.addElement(temp_Item.getName());
        }
        for (int j = 0; j < hiddenItem_vec.size(); j++) {
            JListItemVO temp_Item = (JListItemVO) hiddenItem_vec.get(j);
            hiddenModel.addElement(temp_Item.getName());
        }
    }

    private void onUp() {
        int selected_row[] = jl_column[0].getSelectedIndices();
        int selectedHidden_row[] = jl_column[1].getSelectedIndices();
        selectedIndex = selected_row;

        if (selected_row.length == 0) {
            return;
        }
        for (int i = 0; i < selected_row.length; i++) {
            if (selected_row[i] > i) {
                JListItemVO temp_Item = (JListItemVO) showItem_vec
                    .get(selected_row[i] - 1);
                showItem_vec.setElementAt(showItem_vec.get(selected_row[i]),
                                          selected_row[i] - 1);
                showItem_vec.setElementAt(temp_Item, selected_row[i]);
                selectedIndex[i] = selectedIndex[i] - 1;
            }
        }
        refreshList();
        jl_column[0].setSelectedIndices(selectedIndex);
        jl_column[1].setSelectedIndices(selectedHidden_row);
        selectedIndex = null;
    }

    private void onDown() {
        int selected_row[] = jl_column[0].getSelectedIndices();
        int selectedHidden_row[] = jl_column[1].getSelectedIndices();
        selectedIndex = selected_row;

        if (selected_row.length == 0) {
            return;
        }
        for (int i = selected_row.length - 1; i >= 0; i--) {
            if (selected_row[i] < jl_column[0].getModel().getSize()
                - selected_row.length + i) {
                JListItemVO temp_Item = (JListItemVO) showItem_vec
                    .get(selected_row[i] + 1);
                showItem_vec.setElementAt(showItem_vec.get(selected_row[i]),
                                          selected_row[i] + 1);
                showItem_vec.setElementAt(temp_Item, selected_row[i]);
                selectedIndex[i] = selectedIndex[i] + 1;
            }
        }
        refreshList();
        jl_column[0].setSelectedIndices(selectedIndex);
        jl_column[1].setSelectedIndices(selectedHidden_row);
        selectedIndex = null;
    }

    private int getMinIndex(boolean _listflag) {
        if (_listflag) {
            int selected_row[] = jl_column[0].getSelectedIndices();
            if (selected_row.length > 0) {
                return jl_column[0].getMinSelectionIndex();
            }
        } else {
            int selected_row[] = jl_column[1].getSelectedIndices();
            if (selected_row.length > 0) {
                return jl_column[1].getMinSelectionIndex();
            }
        }
        return -1;
    }

    private void onLeft() {
        int selected_row[] = jl_column[1].getSelectedIndices();
        int showItem_vec_size = showItem_vec.size();

        if (selected_row.length == 0) {
            return;
        }
        if (getMinIndex(true) != -1) {
            showItem_vec_size = getMinIndex(true);
        }
        for (int i = selected_row.length - 1; i >= 0; i--) {
            JListItemVO temp_Item = (JListItemVO) hiddenItem_vec
                .get(selected_row[i]);
            showItem_vec.add(showItem_vec_size, temp_Item);
            hiddenItem_vec.remove(selected_row[i]);
        }
        refreshList();
        jl_column[0].setSelectionInterval(showItem_vec_size, showItem_vec_size
                                          + selected_row.length - 1);
        jl_column[0].requestFocus();
    }

    private void onRight() {
        int selected_row[] = jl_column[0].getSelectedIndices();
        int hiddenItem_vec_size = hiddenItem_vec.size();

        if (selected_row.length == 0) {
            return;
        }
        if (getMinIndex(false) != -1) {
            hiddenItem_vec_size = getMinIndex(false);
        }
        for (int i = selected_row.length - 1; i >= 0; i--) {
            JListItemVO temp_Item = (JListItemVO) showItem_vec.get(selected_row[i]);
            hiddenItem_vec.add(hiddenItem_vec_size, temp_Item);
            showItem_vec.remove(selected_row[i]);
        }
        refreshList();
        jl_column[1].setSelectionInterval(hiddenItem_vec_size, hiddenItem_vec_size + selected_row.length - 1);
        jl_column[1].requestFocus();
    }

    private void onConfirm() {
        this.return_type = 0;
        
        //把操作结果放入客户端数据缓冲
        BillListColumnLocalSetting col=new BillListColumnLocalSetting();      
        col.setShowCols(templetCode, getShowColKeys());
        
        this.dispose();
    }

    private void onCancel() {
        this.return_type = 1;
        this.dispose();
    }

    public String[] getShowColKeys(){
    	int count=showItem_vec.size();
    	String[] rt=new String[count];
    	for(int i=0;i<count;i++){
    		JListItemVO vo=(JListItemVO)showItem_vec.get(i);
    		rt[i]=vo.getId();
    	}
    	return rt;
    }
    
    public Vector getResult() {
    	return showItem_vec;
    }

    public int getReturn_type() {
        return return_type;
    }
}
/**************************************************************************
 * $RCSfile: ShowHideSortTableColumnDialog.java,v $  $Revision: 1.4.8.1 $  $Date: 2009/10/16 03:48:33 $
 *
 * $Log: ShowHideSortTableColumnDialog.java,v $
 * Revision 1.4.8.1  2009/10/16 03:48:33  wangqi
 * *** empty log message ***
 *
 * Revision 1.4  2007/05/31 07:38:18  qilin
 * code format
 *
 * Revision 1.3  2007/05/22 07:58:46  qilin
 * no message
 *
 * Revision 1.2  2007/05/22 02:03:34  sunxb
 * *** empty log message ***
 *
 * Revision 1.1  2007/05/17 06:01:06  qilin
 * no message
 *
 * Revision 1.3  2007/02/10 08:51:57  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 05:14:31  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
