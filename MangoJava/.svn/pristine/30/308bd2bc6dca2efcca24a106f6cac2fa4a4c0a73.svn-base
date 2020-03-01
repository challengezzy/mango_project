/**************************************************************************
 * $RCSfile: UIRefDialog.java,v $  $Revision: 1.4.8.4 $  $Date: 2010/01/25 06:17:09 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentscard;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.EventObject;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import smartx.framework.common.ui.NovaConstants;
import smartx.framework.common.ui.UIUtil;
import smartx.framework.common.vo.HashVO;
import smartx.framework.metadata.ui.NovaDialog;
import smartx.framework.metadata.ui.NovaMessage;
import smartx.framework.metadata.vo.RefItemVO;
import smartx.framework.metadata.vo.TableDataStruct;

public class UIRefDialog extends NovaDialog implements RefDialogIFC {
    private static final long serialVersionUID = -389160228409656932L;

	protected JPanel northpanel = null;
	protected AbstractRefModel model=null;
	protected String str_sql = null;
	protected String str_datasourcename = null;
	protected TableDataStruct struct = null;
	protected JComboBox combox_condition = null;
	protected JComboBox combox_compare = null;
	protected JTextField textField;
	protected JTable table = null;
	protected String str_initvalue;   //初始值
	protected int li_closeType = -1;  //0-确认 1-清空 2-退出(取消)
	protected RefItemVO refVO=null;
	
	
    public UIRefDialog(Container _parent, String _name, String _refinitvalue, AbstractRefModel _model) throws Exception {
        super(_parent, _name, 500, 400); //
        this.model = _model; //
        this.str_sql = _model.getSQL();
        this.str_datasourcename = _model.getDataSourceName(); //数据源名称
        this.str_initvalue = _refinitvalue;
        initialize(); 
        this.setVisible(true);
    }

    private void initialize() throws Exception {
        
        struct = UIUtil.getTableDataStructByDS(this.str_datasourcename, model.getSQL()); // 真正的数据

        this.getContentPane().setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        mainPanel.add(getNorthPanel(), BorderLayout.NORTH);
        mainPanel.add(getMainPanel(), BorderLayout.CENTER);
        mainPanel.add(getSouthPanel(), BorderLayout.SOUTH);

        this.getContentPane().add(mainPanel);

        int row = getInitRow(this.table.getModel(), this.str_initvalue, 0);
        if (row >= 0) {
            Rectangle rect = table.getCellRect(row, 0, true);
            table.scrollRectToVisible(rect);
            table.setRowSelectionInterval(row, row); 
        }

        this.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    onClose();
                }
            }

            public void keyReleased(KeyEvent e) {
            }

            public void keyTyped(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    onClose();
                }
            }
        });

        textField.requestFocus(); //
        textField.requestFocusInWindow(); //

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                textField.requestFocus(); //
                textField.requestFocusInWindow(); //
            }
        });        
    }

    private JPanel getNorthPanel() {
        if (northpanel != null) {
            return northpanel;
        }

        northpanel = new JPanel();
        northpanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        northpanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    onShowSQL();
                }
            }

        });

        combox_condition = new JComboBox();
        combox_condition.setPreferredSize(new Dimension(150, 20));
        String[] items = struct.getTable_header();
        for (int i = 0; i < items.length; i++) {
            if (!items[i].endsWith("#")) {
                combox_condition.addItem(items[i]);
            }
        }

        combox_compare = new JComboBox();
        combox_compare.setPreferredSize(new Dimension(70, 20));
        combox_compare.addItem("like");
        combox_compare.addItem("=");
        combox_compare.addItem(">");
        combox_compare.addItem(">=");
        combox_compare.addItem("<");
        combox_compare.addItem("<=");

        textField = new JTextField();
        textField.setPreferredSize(new Dimension(125, 20));

        textField.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
                onKeyReleased();
            }

            public void keyTyped(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    onClose();
                }
            }
        });

        JButton btn_search = new JButton("检索", UIUtil.getImage("images/office/(03,31).png","快速检索"));
        btn_search.setPreferredSize(new Dimension(85, 20));
        btn_search.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onSearch();
            }
        });

        northpanel.add(combox_condition);
        northpanel.add(combox_compare);
        northpanel.add(textField);
        //northpanel.add(checkbox_iflike);
        northpanel.add(btn_search);

        return northpanel;
    }

    private JScrollPane getMainPanel() {
        JScrollPane scrollPanel = new JScrollPane();
        DefaultTableModel model = new DefaultTableModel(struct.getTable_body(), struct.getTable_header());
        DefaultTableColumnModel colmodel = new DefaultTableColumnModel();
        for (int i = 0; i < struct.getTable_header().length; i++) {
            if (!struct.getTable_header()[i].endsWith(NovaConstants.STRING_REFPANEL_UNSHOWSIGN)) {
                TableColumn col = new TableColumn(i, 150, new MyCellRender(), new MyCellEditor());
                col.setHeaderValue(struct.getTable_header()[i]);
                col.setIdentifier(struct.getTable_header()[i]);
                colmodel.addColumn(col);
            }
        }
        table = new JTable(model, colmodel);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setAutoscrolls(true);
        table.setRowHeight(18);
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    onConfirm(); //
                }
            }
        });

        JButton btn_showsql = new JButton(UIUtil.getImage("images/platform/scrollpanel_corn.gif"));
        btn_showsql.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onShowSQL();
            }
        }); //

        scrollPanel.setCorner(JScrollPane.UPPER_RIGHT_CORNER, btn_showsql);

        scrollPanel.getViewport().add(table);
        return scrollPanel;
    }

    /**
     * 获得指定字串所在行。比较使用字符串方式。
     * @param tblmodel 表格的数据模型
     * @param v 需要比较的值
     * @param col 需要比较的字段
     * @return 没有找到对应记录返回-1，其他返回大于等于0的取值。
     * TODO 本方法可以抽象出来
     */
    private int getInitRow(TableModel tblmodel, String v, int col) {
        int li_rowcount = table.getModel().getRowCount();
        String temp_str;
        for (int i = 0; i < li_rowcount; i++) {
            temp_str = (String)tblmodel.getValueAt(i, col);
            if (temp_str.equals(v)) {
                return i;
            }
        }
        return -1;
    }

    private JPanel getSouthPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        panel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    onShowSQL();
                }
            }

        });

        JButton btn_1 = new JButton("确定");
        btn_1.setPreferredSize(new Dimension(85, 20));
        btn_1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                onConfirm();
            }
        });

        JButton btn_3 = new JButton("取消");
        btn_3.setPreferredSize(new Dimension(85, 20));
        btn_3.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                onClose();
            }
        });

        panel.add(btn_1);
        panel.add(btn_3);

        return panel;
    }

    private void onKeyReleased() {
    	onSearch();
    }

    private void onSearch() {
        clearTableData(); // 清空数据

        String str_text = textField.getText(); //
        String str_condition = combox_condition.getSelectedItem().toString();
        int li_pos = findPos(str_condition);

        try {
            struct = UIUtil.getTableDataStructByDS(model.getDataSourceName(), model.getSQL()); // 取数
            String[][] str_data = struct.getTable_body();

            String str_compare = combox_compare.getSelectedItem().toString();
            DefaultTableModel dtm=( (DefaultTableModel) table.getModel());
            
            for (int i = 0; i < str_data.length; i++) {
                if (str_text == null || str_text.trim().equals("")) {//没有条件
                	dtm.addRow(str_data[i]);
                } else {
                    if (str_compare.equals("=")) {
                        if (str_data[i][li_pos].toLowerCase().equals(str_text.toLowerCase())) {
                        	dtm.addRow(str_data[i]);
                        }
                    } else if (str_compare.equals(">")) {
                        if (str_data[i][li_pos].toLowerCase().compareTo(str_text.toLowerCase()) > 0) {
                            dtm.addRow(str_data[i]);
                        }
                    } else if (str_compare.equals(">=")) {
                        if (str_data[i][li_pos].toLowerCase().compareTo(str_text.toLowerCase()) >= 0) {
                           dtm.addRow(str_data[i]);
                        }
                    } else if (str_compare.equals("<")) {
                        if (str_data[i][li_pos].toLowerCase().compareTo(str_text.toLowerCase()) < 0) {
                            dtm.addRow(str_data[i]);
                        }
                    } else if (str_compare.equals("<=")) {
                    	if (str_data[i][li_pos].toLowerCase().compareTo(str_text.toLowerCase()) <= 0) {
                            dtm.addRow(str_data[i]);
                        }
                    } else if (str_compare.equals("<>")) {
                        if (!str_data[i][li_pos].toLowerCase().equals(str_text.toLowerCase())) {
                            dtm.addRow(str_data[i]);
                        }
                    } else if (str_compare.equals("like")) {
                        if (str_data[i][li_pos].toLowerCase().indexOf(str_text.toLowerCase()) >= 0) {
                        	dtm.addRow(str_data[i]);
                        }
                    }
                }
            }
            table.updateUI();
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
    }

    private int findPos(String _str_condition) {
        String[] items = struct.getTable_header();

        for (int i = 0; i < items.length; i++) {
            if (items[i].equals(_str_condition)) {
                return i;
            }
        }
        return -1;
    }

    private void clearTableData() {
        int li_rowcount = table.getModel().getRowCount();
        for (int i = 0; i < li_rowcount; i++) {
            ( (DefaultTableModel) table.getModel()).removeRow(0);
        }
    }

    
    /**
     * 取消操作
     */
    private void onCancel() {
		this.refVO=null;
		li_closeType = 1;
		this.dispose();
	}
    /**
     * 关闭窗口
     */
	protected void onClose() {
		this.refVO=null;
		li_closeType = 2;
		this.dispose();
	}
    
    /**
     * 确定
     *
     */
    private void onConfirm() {
        int row = table.getSelectedRow(); //
        if (row < 0) {
        	NovaMessage.show(this, "请选择一条记录");
            this.requestFocus();
            return;
        }
        
        HashVO vo = new HashVO(); 
        String[] keys = struct.getTable_header(); 
        TableModel tm=table.getModel();
        for (int i = 0; i < keys.length; i++) {
        	vo.setAttributeValue(keys[i], tm.getValueAt(row, i));            
        }        
        
        this.refVO=new RefItemVO(vo);
        this.li_closeType = 0;
        this.dispose();
    }

    

    private void onShowSQL() {
        NovaMessage.show(this, this.str_sql);        
    }
    
    /**
     * 表格编辑控件
     * @author James.W
     */
    class MyCellEditor extends DefaultCellEditor {
        private static final long serialVersionUID = -3299249534053887111L;

        public MyCellEditor() {
            this(new JTextField());
        }

        public MyCellEditor(JTextField _textfield) {
            super(_textfield);
        }

        public boolean isCellEditable(EventObject evt) {
            if (evt instanceof MouseEvent) {
                int li_count = ( (MouseEvent) evt).getClickCount();
                if (li_count >= 3) {
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        }

    }
    
    //TODO 颜色等配置以后要采用系统配置列表
    class MyCellRender extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
            JLabel label = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            label.setOpaque(true); //
            if (isSelected) {
                label.setBackground(table.getSelectionBackground()); //
            } else {
                if (row % 2 == 0) {
                    label.setBackground(Color.WHITE);
                } else {
                    label.setBackground(new Color(245, 255, 255));
                }
            }

            return label;
        }

    }
    
    
    
    
    /**
     * 返回参照选项值
     * @return
     */
    public RefItemVO getRefVO() {
        return refVO;
    }
    
    
    /**
	 * 对话框的操作类型：确认/取消/关闭
	 * @return
	 */
    public int getCloseType() {
        return li_closeType;
    }

}
/**************************************************************************
 * $RCSfile: UIRefDialog.java,v $  $Revision: 1.4.8.4 $  $Date: 2010/01/25 06:17:09 $
 *
 * $Log: UIRefDialog.java,v $
 * Revision 1.4.8.4  2010/01/25 06:17:09  wangqi
 * *** empty log message ***
 *
 * Revision 1.4.8.3  2010/01/21 08:11:20  wangqi
 * *** empty log message ***
 *
 * Revision 1.4.8.2  2010/01/20 09:55:27  wangqi
 * *** empty log message ***
 *
 * Revision 1.4.8.1  2008/09/18 04:51:42  wangqi
 * *** empty log message ***
 *
 * Revision 1.4  2007/05/31 07:38:19  qilin
 * code format
 *
 * Revision 1.3  2007/05/22 07:58:46  qilin
 * no message
 *
 * Revision 1.2  2007/05/22 02:03:33  sunxb
 * *** empty log message ***
 *
 * Revision 1.1  2007/05/17 06:01:06  qilin
 * no message
 *
 * Revision 1.9  2007/04/05 09:40:15  shxch
 * *** empty log message ***
 *
 * Revision 1.8  2007/03/28 11:35:15  shxch
 * *** empty log message ***
 *
 * Revision 1.7  2007/03/28 10:10:42  shxch
 * *** empty log message ***
 *
 * Revision 1.6  2007/03/28 10:02:54  shxch
 * *** empty log message ***
 *
 * Revision 1.5  2007/02/27 06:03:02  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/02/10 08:51:57  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/01/31 03:01:17  sunxf
 * 参照隐藏列不应该出现在查询条件的下拉框中
 *
 * Revision 1.2  2007/01/30 05:14:29  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
