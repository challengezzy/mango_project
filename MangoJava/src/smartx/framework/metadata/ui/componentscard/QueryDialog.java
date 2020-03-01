/**************************************************************************
 * $RCSfile: QueryDialog.java,v $  $Revision: 1.6.2.4 $  $Date: 2009/06/12 05:25:15 $
 **************************************************************************/

package smartx.framework.metadata.ui.componentscard;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import smartx.framework.common.ui.NovaClientEnvironment;
import smartx.framework.common.ui.NovaConstants;
import smartx.framework.common.vo.FrameWorkTBUtil;
import smartx.framework.metadata.ui.NovaDialog;
import smartx.framework.metadata.ui.NovaMessage;
import smartx.framework.metadata.vo.ComBoxItemVO;
import smartx.framework.metadata.vo.Pub_Templet_1VO;
import smartx.framework.metadata.vo.Pub_Templet_1_ItemVO;
import smartx.framework.metadata.vo.RefItemVO;
import smartx.framework.metadata.vo.jepfunctions.JepFormulaParse;
import smartx.framework.metadata.vo.jepfunctions.JepFormulaUtil;

/**
 * 高级查询对话框
 * @author James.W
 */
public class QueryDialog extends NovaDialog {

	private static final long serialVersionUID = 1L;

	private Pub_Templet_1VO templetVO = null;

	private JPanel mainPanel = null;

	private JTabbedPane tabpane = null;

	private String tablename = null;

	private DefaultTableModel tableModel = null;

	private TableColumnModel columnModel = null;

	private TableColumn[] allTableColumns = null;

	private JTable queryTable = null;

	private JTable cardorderTable = null;

	private JTable listorderTable = null;

	private DefaultTableModel ordercardtableModel = null;

	private TableColumnModel ordercardcolumnModel = null;

	private TableColumn[] ordercardallTableColumns = null;

	private DefaultTableModel orderlisttableModel = null;

	private TableColumnModel orderlistcolumnModel = null;

	private TableColumn[] orderlistallTableColumns = null;

	private Object[][] queryData = null;

	private StringBuffer sblist = null;

	private JTextArea queryTextareacard = null;

	private JTextArea queryTextarealist = null;

	private Vector v_compents = new Vector();

	private HashMap map = new HashMap();

	private boolean bo_iscardquery = true;

	private String str_return_sql = "";


	private boolean isadmin = true;
	

	public QueryDialog(Container _parent, Pub_Templet_1VO _templetVO) {
		super(_parent, "查询", 650, 550);
		this.templetVO = _templetVO;
		this.tablename = templetVO.getTablename();
		if (NovaClientEnvironment.getInstance().isAdmin() == true) {
			isadmin = true;
		} else {
			isadmin = false;
		}
		this.getContentPane().add(getMainPanel());
		this.setVisible(true);
	}

	private JPanel getMainPanel() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		JTabbedPane tabpane = createTabpane();
		mainPanel.add(tabpane, BorderLayout.CENTER);
		mainPanel.setVisible(true);
		return mainPanel;
	}

	private JTabbedPane createTabpane() {
		tabpane = new JTabbedPane();
		tabpane.addTab("查询", queryCard());
		tabpane.addTab("高级查询", queryList());
		return tabpane;
	}

	private JPanel queryCard() {
		JPanel cardPanel = new JPanel();
		cardPanel.setLayout(new BorderLayout());
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		JButton btn_confirm = new JButton("确定");
		JButton btn_release = new JButton("清空");
		JButton btn_preview = new JButton("预览");
		JButton btn_cancel = new JButton("取消");
		btn_confirm.setPreferredSize(new Dimension(75, 22));
		btn_release.setPreferredSize(new Dimension(75, 22));
		btn_preview.setPreferredSize(new Dimension(75, 22));
		btn_cancel.setPreferredSize(new Dimension(75, 22));
		
		btn_confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					onCardOk();
            	}catch(Exception ee){
            	    NovaMessage.show(tabpane, "操作错误："+ee.getMessage(), NovaConstants.MESSAGE_ERROR);                		
            	}
			}
		});
		
		btn_release.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearCard();
			}
		});
		btn_preview.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					onCardPreview();
            	}catch(Exception ee){
            	    NovaMessage.show(tabpane, "操作错误："+ee.getMessage(), NovaConstants.MESSAGE_ERROR);                		
            	}
			}
		});
		btn_cancel.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				onCancel();
			}
		});
		buttonPanel.add(btn_confirm);
		buttonPanel.add(btn_release);
		if (isadmin == true) {
			buttonPanel.add(btn_preview);
		}
		buttonPanel.add(btn_cancel);
		JPanel tablePanel = new JPanel();
		tablePanel.setPreferredSize(new Dimension(510, getCardPaneHeight()));
		FlowLayout flayout = new FlowLayout(FlowLayout.LEFT);
		flayout.setHgap(10);
		tablePanel.setLayout(flayout);
		
		Pub_Templet_1_ItemVO[] vos=templetVO.getRealViewItemVOs();
		for (int i = 0; i < vos.length; i++) {
			bo_iscardquery = true;
			String str_type = vos[i].getItemtype();
			if (vos[i].getDelfaultquerylevel() != null && (new Integer(vos[i].getDelfaultquerylevel()).intValue() > 2)) {
				bo_iscardquery = false;
			}
			if (!bo_iscardquery)
				continue;
			if (str_type.equals("文本框")) {
				TextFieldPanel panel = new TextFieldPanel(vos[i]);
				panel.setValue(execFormula(vos[i].getDefaultCondition()));
				tablePanel.add(panel);
				v_compents.add(panel);
			} else if (str_type.equals("数字框")) {
				QueryNumericPanel panel=new QueryNumericPanel(vos[i],QueryNumericPanel.TYPE_FLOAT);
            	panel.setValue(execFormula(vos[i].getDefaultCondition()));
            	tablePanel.add(panel);
            	v_compents.add(panel);
			} else if (str_type.equals("密码框")) {
				PasswordFieldPanel panel = new PasswordFieldPanel(vos[i]);
				panel.setValue(execFormula(vos[i].getDefaultCondition()));
				mainPanel.add(panel);
				v_compents.add(panel);
			} else if (str_type.equals("多行文本框")) {
				TextAreaPanel panel = new TextAreaPanel(vos[i]);
				panel.setValue(execFormula(vos[i].getDefaultCondition()));
				mainPanel.add(panel);
				v_compents.add(panel);
			} else if (str_type.equals("文件选择框")) {
				UIFilePathPanel panel = new UIFilePathPanel(vos[i]);
				panel.setValue(execFormula(vos[i].getDefaultCondition()));
				mainPanel.add(panel);
				v_compents.add(panel);
			} else if (str_type.equals("颜色")) {
				UIColorPanel panel = new UIColorPanel(vos[i]);
				panel.setValue(execFormula(vos[i].getDefaultCondition()));
				mainPanel.add(panel);
				v_compents.add(panel);
			} else if (str_type.equals("大文本框")) {
				UITextAreaPanel panel = new UITextAreaPanel(vos[i]);
				panel.setValue(execFormula(vos[i].getDefaultCondition()));
				mainPanel.add(panel);
				v_compents.add(panel);
			} else if (str_type.equals("图片选择框")) {
				UIImagePanel panel = new UIImagePanel(vos[i]);
				panel.setValue(execFormula(vos[i].getDefaultCondition()));
				mainPanel.add(panel);
				v_compents.add(panel);
			} else if (str_type.equals("下拉框")) {
				ComBoxPanel panel = new ComBoxPanel(vos[i]);
				panel.setValue(execFormula(vos[i].getDefaultCondition()));
				tablePanel.add(panel);
				v_compents.add(panel);
			} else if (str_type.equals("参照")) {
				UIRefPanel panel = new UIRefPanel(vos[i]);
				panel.setValue(execFormula(vos[i].getDefaultCondition()));
				tablePanel.add(panel);
				v_compents.add(panel);
			} else if (str_type.equals("日历")) {
				QueryCalendarPanel panel = new QueryCalendarPanel(vos[i],QueryCalendarPanel.TYPE_DATE);
				tablePanel.add(panel);
				panel.setValue(execFormula(vos[i].getDefaultCondition()));
                v_compents.add(panel);
			} else if (str_type.equals("时间")) {
				QueryCalendarPanel panel = new QueryCalendarPanel(vos[i],QueryCalendarPanel.TYPE_TIME);
				tablePanel.add(panel);
                panel.setValue(execFormula(vos[i].getDefaultCondition()));
                v_compents.add(panel);
			} else if (str_type.equals("勾选框")) {
				UICheckBoxPanel panel = new UICheckBoxPanel(vos[i]);
				panel.setValue(execFormula(vos[i].getDefaultCondition()));
				tablePanel.add(panel);
				v_compents.add(panel);
			}
		}
		JPanel orderPanel = new JPanel();
		JPanel order_buttonpanel = new JPanel();
		order_buttonpanel.setLayout(new GridLayout(3, 1, 0, 11));
		JButton btn_order_insert = new JButton("新增");
		JButton btn_order_delete = new JButton("删除");
		JButton btn_order_release = new JButton("清空");
		btn_order_insert.setPreferredSize(new Dimension(70, 22));
		btn_order_delete.setPreferredSize(new Dimension(70, 22));
		btn_order_release.setPreferredSize(new Dimension(70, 22));
		order_buttonpanel.add(btn_order_insert);
		order_buttonpanel.add(btn_order_delete);
		order_buttonpanel.add(btn_order_release);
		orderPanel.setLayout(new BorderLayout());
		orderPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(), "选择排序方式", TitledBorder.LEFT,
				TitledBorder.TOP, new Font("宋体", 0, 12)));
		cardorderTable = new JTable(getOrderTableModel(), getOrderColumnModel());
		cardorderTable.getTableHeader().setReorderingAllowed(false);
		cardorderTable.setRowHeight(18);
		cardorderTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		orderPanel.setPreferredSize(new Dimension(100, 120));
		orderPanel.add(new JScrollPane(cardorderTable), BorderLayout.CENTER);
		orderPanel.add(order_buttonpanel, BorderLayout.EAST);
		btn_order_insert.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				stopEditing(cardorderTable);
				addEmptyRow(ordercardtableModel, 2);
			}
		});
		btn_order_delete.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				stopEditing(cardorderTable);
				int[] li_selectRow = cardorderTable.getSelectedRows();
				if (li_selectRow.length <= 0) {
					JOptionPane.showMessageDialog(QueryDialog.this,
							"          请至少选中一条记录!");
				} else {
					removeRows(cardorderTable, li_selectRow);
				}
			}
		});
		btn_order_release.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				stopEditing(cardorderTable);
				clearorderTable(cardorderTable);
			}
		});
		queryTextareacard = new JTextArea();
		queryTextareacard.setLineWrap(true);
		queryTextareacard.setEditable(false);
		queryTextareacard.setBackground(Color.LIGHT_GRAY);
		JScrollPane scrollPanel = new JScrollPane(queryTextareacard);
		scrollPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(), "预览SQL语句", TitledBorder.LEFT,
				TitledBorder.TOP, new Font("宋体", 0, 12)));
		scrollPanel.setPreferredSize(new Dimension(100, 100));
		scrollPanel
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPanel
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		JScrollPane tablescrollPanel = new JScrollPane(tablePanel);
		tablescrollPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "输入查询条件",
				TitledBorder.LEFT, TitledBorder.TOP, new Font("宋体", 0, 12)));
		tablescrollPanel
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JPanel fixpanel = new JPanel();
		fixpanel.setLayout(new BorderLayout());
		fixpanel.add(orderPanel, BorderLayout.CENTER);
		if (isadmin == true) {
			fixpanel.add(scrollPanel, BorderLayout.SOUTH);
		}
		cardPanel.add(buttonPanel, BorderLayout.NORTH);
		cardPanel.add(tablescrollPanel, BorderLayout.CENTER);
		cardPanel.add(fixpanel, BorderLayout.SOUTH);
		cardPanel.setVisible(true);
		return cardPanel;
	}

	/**
     * 执行默认值方式
     *
     */
    private String execFormula(String formula) {
        if (formula != null && !formula.trim().equals("")) {
            String modify_formula = null;
            try {
                modify_formula = new FrameWorkTBUtil().convertFormulaMacPars(formula.trim(),NovaClientEnvironment.getInstance(), null);
            } catch (Exception e) {
                System.out.println("执行公式:[" + formula + "]失败!!!!");
                //e.printStackTrace(); //
            } 

            if (modify_formula != null) {
                String str_value = JepFormulaUtil.getJepFormulaValue(modify_formula,JepFormulaParse.li_ui); // 真正执行转换后的公式!!//
                //System.out.println("执行默认值公式:[" + formula + "],转换后[" + modify_formula + "],执行结果[" + str_value + "]");
                // this.setCompentObjectValue(tempitem.getItemkey(),
                // str_value); //设置控件值,这里应该是送Object!!有待进一步改进!!
                return str_value;
            }
            return modify_formula;
        }
        return formula;
    }
	
	public int getCardPaneHeight() {
		int li_height = 0;
		for (int i = 0; i < templetVO.getRealViewItemVOs().length; i++) {
			bo_iscardquery = true;
			if (templetVO.getRealViewItemVOs()[i].getDelfaultquerylevel() != null
					&& (new Integer(templetVO.getRealViewItemVOs()[i]
							.getDelfaultquerylevel()).intValue() > 2)) {
				bo_iscardquery = false;
			}
		}
		return li_height / 2;
	}

	public String getCompentRealValue(String _key) {
		INovaCompent[] compents = (INovaCompent[]) v_compents
				.toArray(new INovaCompent[0]);
		for (int i = 0; i < compents.length; i++) {
			if (compents[i].getKey().equalsIgnoreCase(_key)) {
				return compents[i].getValue();
			}
		}
		return "";
	}

	public void setCompentObjectValue(String _key, Object _obj) {
		INovaCompent compent = getCompentByKey(_key);
		if (_obj == null)
			compent.reset();
		compent.setObject(_obj);
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

	public void clearCard() {
		AbstractNovaComponent[] compents = (AbstractNovaComponent[]) v_compents.toArray(new AbstractNovaComponent[0]);
		for (int i = 0; i < compents.length; i++) {
			compents[i].reset();
		}
	}

	public HashMap getChirldren(int _chirld) {
		ArrayList chirldlist = new ArrayList();
		chirldlist.add(this
				.getCompentRealValue(templetVO.getRealViewItemVOs()[_chirld]
						.getItemkey()
						+ "START"));
		chirldlist.add(this
				.getCompentRealValue(templetVO.getRealViewItemVOs()[_chirld]
						.getItemkey()
						+ "END"));
		Object chirld = new Integer(_chirld);
		map.put(chirld, chirldlist);
		return map;
	}

	public ArrayList findIndexMap(Object _ob) {
		ArrayList index = new ArrayList();
		if (map.get(_ob) instanceof ArrayList) {
			index.add(((ArrayList) (map.get(_ob))).get(0).toString());
			index.add(((ArrayList) (map.get(_ob))).get(1).toString());
		}
		return index;
	}

	/**
	 * Card部分的预览
	 * @throws Exception
	 */
	public void onCardPreview() throws Exception{
		String swhere=getCardQueryWhere();
		swhere=swhere.equals("")?"":(" WHERE "+swhere);
    	String sorder=getCardQueryOrder();
    	    	
    	String sql="SELECT  *  FROM  " + tablename + swhere+" "+sorder;
    	queryTextareacard.setText(sql);
	}
	
	/**
	 * Card部分的确认
	 * @throws Exception
	 */
	private void onCardOk() throws Exception {
		String swhere=getCardQueryWhere();
    	String sorder=getCardQueryOrder();
		
    	if(swhere.equals("")){
			str_return_sql = " 1=1 "+sorder;
		} else {
			str_return_sql = swhere+" "+sorder;
		}
		this.dispose();
	}

	//界面字段是否为空
    private boolean isEmptyCompentByKey(INovaCompent com) {
        Object v=com.getObject();
        return v==null||v.toString().equals("");        
    }
	
	/**
	 * Card部分的查询条件的组合查询子句
	 * @return
	 */
	private String getCardQueryWhere()throws Exception {
		StringBuffer sbcard = new StringBuffer();
		
		for (int i = 0; i <  v_compents.size(); i++) {
			INovaCompent icomp=(INovaCompent)v_compents.get(i);
        	Pub_Templet_1_ItemVO vo=icomp.getTempletItemVO();
        	String votype=vo.getItemtype();
			
        	//判断是否必要条件
        	if(vo.isMustCondition()&&isEmptyCompentByKey(icomp)){
        		String n=vo.getItemname();
        		throw new Exception("必选条件【"+n+"】没有填写。");        		        		
        	}
        	//判断是否条件为空
        	if(isEmptyCompentByKey(icomp)){
        		continue;
        	}
        	
        	if (votype.equals("时间")) {
            	sbcard.append(" and ").append(icomp.getObject());
            } else if (votype.equals("日历")) {
            	sbcard.append(" and ").append(icomp.getObject());
            } else if (votype.equals("数字框")) {
            	sbcard.append(" and ").append(icomp.getValue());
            } else if (votype.equals("下拉框") || votype.equals("参照") || votype.equals("勾选框")) {
            	String v=icomp.getValue();
            	if(v!=null){
            		sbcard.append(" and ").append( vo.getItemkey() ).append( " = '" ).append( v ).append( "' ");
            	}
            }else{
            	sbcard.append(" and upper(" ).append( vo.getItemkey() ).append( ") LIKE upper('%" ).append( icomp.getObject() ).append( "%') ");
            }
		}

		

		String rt=sbcard.toString().trim();
		if(rt.startsWith("and")){
			return rt.substring(3);
		}else{//如果连and都没有，那么肯定是没有任何条件的。
			return "";
		}
	}
	
	/**
	 * Card部分的排序子句
	 * @return
	 */
	private String getCardQueryOrder(){
		//排序子句
		String sorder=onRefreshOrderTable(cardorderTable, ordercardtableModel);
		return sorder==null?"":sorder.trim();		
	}

	private DefaultTableModel getOrderTableModel() {
		if (ordercardtableModel != null) {
			return ordercardtableModel;
		}
		Object[][] data = new Object[0][2];
		ordercardtableModel = new DefaultTableModel(data,
				new String[] { "", "" });
		return ordercardtableModel;
	}

	private TableColumnModel getOrderColumnModel() {
		if (ordercardcolumnModel != null) {
			return ordercardcolumnModel;
		}
		ordercardcolumnModel = new DefaultTableColumnModel();
		for (int i = 0; i < 2; i++) {
			ordercardcolumnModel.addColumn(getOrderTableColumns()[i]);
		}
		return ordercardcolumnModel;
	}

	private TableColumn[] getOrderTableColumns() {
		if (ordercardallTableColumns != null) {
			return ordercardallTableColumns;
		}
		ordercardallTableColumns = new TableColumn[2];
		for (int i = 0; i < 2; i++) {
			TableCellEditor cellEditor = null;
			TableCellRenderer cellRender = null;
			cellRender = null;
			cellEditor = new ComboBoxCellorderEditor(new JComboBox(), i,
					templetVO);
			ordercardallTableColumns[i] = new TableColumn(i, 275, cellRender,
					cellEditor);
		}
		ordercardallTableColumns[0].setHeaderValue("项目名称");
		ordercardallTableColumns[1].setHeaderValue("排序方式");
		return ordercardallTableColumns;
	}

	private void clearorderTable(JTable _table) {
		DefaultTableModel model = (DefaultTableModel) _table.getModel();
		int li_rowcount = _table.getRowCount();
		for (int i = 0; i < li_rowcount; i++) {
			model.removeRow(0);
		}
		_table.updateUI();
	}

	
	/**
	 * 获得排序子句
	 * @param _table
	 * @param _tableModel
	 * @return
	 */
	private String onRefreshOrderTable(JTable _table, DefaultTableModel _tableModel) {
		Object[][] _data = new Object[_table.getRowCount()][2];
		StringBuffer _sb = new StringBuffer();
		for (int i = 0; i < _data.length; i++) {
			for (int j = 0; j < 2; j++) {
				_data[i][j] = _table.getValueAt(i, j);
				if (_data[i][j] == null) {
					_data[i][j] = "";
				}
				if (_data[i][j].equals("升序")) {
					_data[i][j] = "ASC";
				}
				if (_data[i][j].equals("降序")) {
					_data[i][j] = "DESC";
				}
				if (j == 0) {
					_data[i][j] = getRealValueAtModel(_tableModel, i, j);
				}
				if (i == 0 && i != _data.length) {
					if (j == 0) {
						_sb.append(" ORDER BY " + _data[i][j] + " ");
					} else {
						_sb.append(" " + _data[i][j] + " ");
					}
				} else if (i != 0 && i != _data.length) {
					if (j == 1) {
						_sb.append(" " + _data[i][j] + " ");
					} else {
						_sb.append(" , " + _data[i][j] + " ");
					}
				} else {
					if (j == 0) {
						_sb.append(" ORDER BY " + _data[i][j] + " ");
					} else {
						_sb.append(" " + _data[i][j] + " ");
					}
				}
			}
		}
		String sql = null;
		if (!_sb.toString().trim().equals("") && _sb.length() != 0) {
			sql = "&" + _sb.toString() + "&";
		}

		return sql;
	}

	class ComboBoxCellorderEditor extends DefaultCellEditor {

		private static final long serialVersionUID = 1L;

		private Pub_Templet_1VO templetVO = null;

		private JComboBox comboBox = null;

		int column = 0;

		public ComboBoxCellorderEditor(JComboBox _comboBox, int _column,
				Pub_Templet_1VO _templetVO) {
			super(_comboBox);
			this.column = _column;
			this.templetVO = _templetVO;
		}

		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			comboBox = (JComboBox) super.getTableCellEditorComponent(table,
					value, isSelected, row, column);
			initComboBox();
			comboBox.setEditable(false);
			comboBox.setEnabled(true);
			return comboBox;
		}

		private void initComboBox() {
			comboBox.removeAllItems();
			if (column == 0) {
				for (int i = 0; i < templetVO.getRealViewItemVOs().length; i++) {
					ComBoxItemVO comBoBoxitemvo = new ComBoxItemVO(templetVO
							.getRealViewItemVOs()[i].getItemkey(), templetVO
							.getRealViewItemVOs()[i].getItemkey(), templetVO
							.getRealViewItemVOs()[i].getItemname());
					comboBox.addItem(comBoBoxitemvo);
				}
			} else {
				comboBox.addItem("升序");
				comboBox.addItem("降序");
			}
		}

		public Object getCellEditorValue() {
			Object obj = comboBox.getSelectedItem();
			if (obj == null) {
				return null;
			}
			if (obj instanceof ComBoxItemVO) {
				return (ComBoxItemVO) comboBox.getSelectedItem();
			} else {
				return comboBox.getSelectedItem();
			}
		}

		public boolean isCellEditable(EventObject evt) {
			if (evt instanceof MouseEvent) {
				return ((MouseEvent) evt).getClickCount() >= 2;
			}
			return true;
		}
	}

	private JPanel queryList() {
		JPanel listPanel = new JPanel();
		listPanel.setLayout(new BorderLayout());
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		JButton btn_insert = new JButton("新增");
		JButton btn_delete = new JButton("删除");
		JButton btn_confirm = new JButton("确定");
		JButton btn_release = new JButton("清空");
		JButton btn_preview = new JButton("预览");
		JButton btn_cancel = new JButton("取消");
		btn_insert.setPreferredSize(new Dimension(75, 22));
		btn_delete.setPreferredSize(new Dimension(75, 22));
		btn_confirm.setPreferredSize(new Dimension(75, 22));
		btn_release.setPreferredSize(new Dimension(75, 22));
		btn_preview.setPreferredSize(new Dimension(75, 22));
		btn_cancel.setPreferredSize(new Dimension(75, 22));
		btn_insert.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				stopEditing(queryTable);
				addEmptyRow(getTableModel(), 4);
			}
		});
		btn_delete.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				stopEditing(queryTable);
				int[] li_selectRow = queryTable.getSelectedRows();
				if (li_selectRow.length <= 0) {
					JOptionPane.showMessageDialog(QueryDialog.this,
							"          请至少选中一条记录!");
				} else {
					removeRows(queryTable, li_selectRow);
				}
			}
		});
		btn_release.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				stopEditing(queryTable);
				clearTable();
			}
		});
		btn_confirm.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (!checkQueryCondition()) {
					NovaMessage.show(QueryDialog.this, "查询条件不完整",
							NovaConstants.MESSAGE_ERROR);
					return;
				}
				stopEditing(queryTable);
				onListOk();
			}
		});
		btn_preview.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopEditing(queryTable);
				onListPreview();
			}
		});
		btn_cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopEditing(queryTable);
				onCancel();
			}
		});
		buttonPanel.add(btn_insert);
		buttonPanel.add(btn_delete);
		buttonPanel.add(btn_confirm);
		buttonPanel.add(btn_release);
		if (isadmin == true) {
			buttonPanel.add(btn_preview);
		}
		buttonPanel.add(btn_cancel);
		JPanel tablePanel = new JPanel();
		tablePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(), "输入查询条件", TitledBorder.LEFT,
				TitledBorder.TOP, new Font("宋体", 0, 12)));
		tablePanel.setLayout(new BorderLayout());
		queryTable = new JTable(getTableModel(), getColumnModel());
		queryTable.getTableHeader().setReorderingAllowed(false);
		queryTable.setRowHeight(18);
		queryTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tablePanel.add(new JScrollPane(queryTable));
		JPanel orderlistPanel = new JPanel();
		JPanel orderlist_buttonpanel = new JPanel();
		orderlist_buttonpanel.setLayout(new GridLayout(3, 1, 0, 11));
		JButton btn_orderlist_insert = new JButton("新增");
		JButton btn_orderlist_delete = new JButton("删除");
		JButton btn_orderlist_release = new JButton("清空");
		btn_orderlist_insert.setPreferredSize(new Dimension(70, 22));
		btn_orderlist_delete.setPreferredSize(new Dimension(70, 22));
		btn_orderlist_release.setPreferredSize(new Dimension(70, 22));
		orderlist_buttonpanel.add(btn_orderlist_insert);
		orderlist_buttonpanel.add(btn_orderlist_delete);
		orderlist_buttonpanel.add(btn_orderlist_release);
		orderlistPanel.setLayout(new BorderLayout());
		orderlistPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(), "选择排序方式", TitledBorder.LEFT,
				TitledBorder.TOP, new Font("宋体", 0, 12)));
		listorderTable = new JTable(getOrderlistTableModel(),
				getOrderlistColumnModel());
		listorderTable.getTableHeader().setReorderingAllowed(false);
		listorderTable.setRowHeight(18);
		listorderTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		orderlistPanel.setPreferredSize(new Dimension(100, 120));
		orderlistPanel
				.add(new JScrollPane(listorderTable), BorderLayout.CENTER);
		orderlistPanel.add(orderlist_buttonpanel, BorderLayout.EAST);
		btn_orderlist_insert.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				stopEditing(listorderTable);
				addEmptyRow(orderlisttableModel, 2);
			}
		});
		btn_orderlist_delete.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				stopEditing(listorderTable);
				int[] li_selectRow = listorderTable.getSelectedRows();
				if (li_selectRow.length <= 0) {
					JOptionPane.showMessageDialog(QueryDialog.this,"          请至少选中一条记录!");
				} else {
					removeRows(listorderTable, li_selectRow);
				}
			}
		});
		btn_orderlist_release.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				stopEditing(listorderTable);
				clearorderTable(listorderTable);
			}
		});
		queryTextarealist = new JTextArea();
		queryTextarealist.setLineWrap(true);
		queryTextarealist.setEditable(false);
		queryTextarealist.setBackground(Color.LIGHT_GRAY);
		JScrollPane scrollPanel = new JScrollPane(queryTextarealist);
		scrollPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(), "预览SQL语句", TitledBorder.LEFT,
				TitledBorder.TOP, new Font("宋体", 0, 12)));
		scrollPanel.setPreferredSize(new Dimension(100, 100));
		scrollPanel
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPanel
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		JPanel fixpanel = new JPanel();
		fixpanel.setLayout(new BorderLayout());
		fixpanel.add(orderlistPanel, BorderLayout.CENTER);
		if (isadmin == true) {
			fixpanel.add(scrollPanel, BorderLayout.SOUTH);
		}
		listPanel.add(buttonPanel, BorderLayout.NORTH);
		listPanel.add(tablePanel, BorderLayout.CENTER);
		listPanel.add(fixpanel, BorderLayout.SOUTH);
		listPanel.setVisible(true);
		return listPanel;
	}

	private void addEmptyRow(DefaultTableModel _tableModel, int _length) {
		Object[] allobjs = new Object[_length];
		for (int i = 0; i < allobjs.length; i++) {
			allobjs[i] = null;
		}
		_tableModel.addRow(allobjs);
	}

	public void removeRows(JTable _table, int[] _rowindex) {
		DefaultTableModel model = (DefaultTableModel) _table.getModel();
		Arrays.sort(_rowindex);
		for (int i = _rowindex.length - 1; i >= 0; i--) {
			model.removeRow(_rowindex[i]);
		}
	}

	private void clearTable() {
		for (int i = 0; i < queryTable.getRowCount(); i++) {
			queryTable.setValueAt(null, i, 3);
		}
		queryTable.updateUI();
	};

	

	private void onListOk() {
		if (getQuerysqllist() == null || getQuerysqllist().length() <= 0) {
			str_return_sql = " 1=1 ";
		} else {
			str_return_sql = getQuerysqllist();
		}
		this.dispose();
	}

	private void onCancel() {
		this.dispose();
	}

	private void onRefresh() {
		queryData = new Object[queryTable.getRowCount()][4];
		sblist = new StringBuffer();
		for (int i = 0; i < queryData.length; i++) {
			for (int j = 0; j < 4; j++) {
				queryData[i][j] = queryTable.getValueAt(i, j);
				if (queryData[i][j] == null) {
					queryData[i][j] = "";
				}
				queryData[0][0] = "";
				if (queryData[i][j].equals("并且")) {
					queryData[i][j] = "AND";
				}
				if (queryData[i][j].equals("或者")) {
					queryData[i][j] = "OR";
				}
				if (queryData[i][j].equals("大于等于")) {
					queryData[i][j] = ">=";
				}
				if (queryData[i][j].equals("等于")) {
					queryData[i][j] = "=";
				}
				if (queryData[i][j].equals("小于等于")) {
					queryData[i][j] = "<=";
				}
				if (queryData[i][j].equals("小于")) {
					queryData[i][j] = "<";
				}
				if (queryData[i][j].equals("大于")) {
					queryData[i][j] = ">";
				}
				if (queryData[i][j].equals("类似")) {
					queryData[i][j] = "LIKE";
				}
				if (j == 1 || j == 3) {
					queryData[i][j] = getRealValueAtModel(getTableModel(), i, j);
				}
				if (j == 3) {
					sblist.append(" '" + queryData[i][j] + "' ");
				} else {
					sblist.append(" " + queryData[i][j] + " ");
				}
			}
		}
		
		//排序子句处理
		String sorder=onRefreshOrderTable(listorderTable, orderlisttableModel);
		if (sorder != null && sorder.length() > 0) {
			sblist.append(sorder);
		}
	}

	private void onListPreview() {
		if (getQuerysqllist() == null || getQuerysqllist().length() <= 0) {
			queryTextarealist.setText("SELECT  *  FROM  " + tablename + "");
		} else {
			queryTextarealist.setText("SELECT  *  FROM  " + tablename
					+ "  WHERE" + getQuerysqllist());
		}
	}

	public Object getValueAtModel(DefaultTableModel _tableModel, int _row,
			int _column) {
		return _tableModel.getValueAt(_row, _column);
	}

	public String getRealValueAtModel(DefaultTableModel _tableModel, int _row,
			int _column) {
		Object obj = getValueAtModel(_tableModel, _row, _column);
		return getObjectRealValue(obj);
	}

	private String getObjectRealValue(Object _obj) {
		if (_obj == null) {
			return "";
		}
		if (_obj instanceof ComBoxItemVO) {
			ComBoxItemVO vo = (ComBoxItemVO) _obj;
			return vo.getId();
		} else if (_obj instanceof RefItemVO) {
			RefItemVO vo = (RefItemVO) _obj;
			return vo.getId();
		} else {
			return _obj.toString();
		}
	}

	public String getQuerysqllist() {
		onRefresh();
		return sblist.toString();
	}

	private void stopEditing(JTable _table) {
		try {
			if (_table.getCellEditor() != null) {
				_table.getCellEditor().stopCellEditing();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private DefaultTableModel getTableModel() {
		if (tableModel != null) {
			return tableModel;
		}
		Object[][] data = new Object[0][4];
		tableModel = new DefaultTableModel(data,
				new String[] { "", "", "", "" });
		return tableModel;
	}

	private TableColumnModel getColumnModel() {
		if (columnModel != null) {
			return columnModel;
		}
		columnModel = new DefaultTableColumnModel();
		for (int i = 0; i < 4; i++) {
			columnModel.addColumn(getTableColumns()[i]);
		}
		return columnModel;
	}

	private TableColumn[] getTableColumns() {
		if (this.allTableColumns != null) {
			return allTableColumns;
		}
		allTableColumns = new TableColumn[4];
		for (int i = 0; i < 4; i++) {
			TableCellEditor cellEditor = null;
			TableCellRenderer cellRender = null;

			if (i == 0 || i == 1 || i == 2) {
				cellRender = null;
				cellEditor = new ComboBoxCellEditor(new JComboBox(), i,
						templetVO);
			}

			if (i == 3) {
				cellRender = null;
				cellEditor = new ChangeCellEditor(templetVO);
			}

			allTableColumns[i] = new TableColumn(i, 155, cellRender, cellEditor);
		}

		allTableColumns[0].setHeaderValue("连接条件");
		allTableColumns[1].setHeaderValue("项目名称");
		allTableColumns[2].setHeaderValue("选择条件");
		allTableColumns[3].setHeaderValue("输入条件");
		return allTableColumns;
	}

	private DefaultTableModel getOrderlistTableModel() {
		if (orderlisttableModel != null) {
			return orderlisttableModel;
		}
		Object[][] data = new Object[0][2];
		orderlisttableModel = new DefaultTableModel(data,
				new String[] { "", "" });
		return orderlisttableModel;
	}

	private TableColumnModel getOrderlistColumnModel() {
		if (orderlistcolumnModel != null) {
			return orderlistcolumnModel;
		}
		orderlistcolumnModel = new DefaultTableColumnModel();
		for (int i = 0; i < 2; i++) {
			orderlistcolumnModel.addColumn(getOrderlistTableColumns()[i]);
		}
		return orderlistcolumnModel;
	}

	private TableColumn[] getOrderlistTableColumns() {
		if (this.orderlistallTableColumns != null) {
			return orderlistallTableColumns;
		}
		orderlistallTableColumns = new TableColumn[2];
		for (int i = 0; i < 2; i++) {
			TableCellEditor cellEditor = null;
			TableCellRenderer cellRender = null;
			cellRender = null;
			cellEditor = new ComboBoxCellorderEditor(new JComboBox(), i,
					templetVO);
			orderlistallTableColumns[i] = new TableColumn(i, 275, cellRender,
					cellEditor);
		}
		orderlistallTableColumns[0].setHeaderValue("项目名称");
		orderlistallTableColumns[1].setHeaderValue("排序方式");
		return orderlistallTableColumns;
	}

	private boolean checkQueryCondition() {
		for (int i = 0; i < queryTable.getRowCount(); i++) {
			for (int j = 0; j < 4; j++) {

				if (i == 0 && j == 0) {
					continue;
				}

				if (j == 1 || j == 3) {
					String value = getRealValueAtModel(getTableModel(), i, j);
					if (value == null || value.equalsIgnoreCase("")) {
						return false;
					}
				} else if (j == 0 || j == 2) {
					String value = (String) queryTable.getValueAt(i, j);
					if (value == null || value.equalsIgnoreCase("")) {
						return false;
					}
				}
			}
		}

		return true;
	}

	class ComboBoxCellEditor extends DefaultCellEditor {

		private static final long serialVersionUID = 1L;

		private Pub_Templet_1VO templetVO = null;

		private JComboBox comboBox = null;

		int column = 0;

		public ComboBoxCellEditor(JComboBox _comboBox, int _column,
				Pub_Templet_1VO _templetVO) {
			super(_comboBox);
			this.column = _column;
			this.templetVO = _templetVO;
		}

		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			comboBox = (JComboBox) super.getTableCellEditorComponent(table,
					value, isSelected, row, column);
			initComboBox();
			comboBox.setEditable(false);
			comboBox.setEnabled(true);
			if (row == 0 && column == 0) {
				comboBox.setSelectedItem(null);
				comboBox.setEnabled(false);
			}
			return comboBox;
		}

		private void initComboBox() {
			comboBox.removeAllItems();
			if (column == 0) {
				comboBox.addItem("并且");
				comboBox.addItem("或者");
			} else if (column == 1) {
				for (int i = 0; i < templetVO.getRealViewItemVOs().length; i++) {
					ComBoxItemVO comBoBoxitemvo = new ComBoxItemVO(templetVO
							.getRealViewItemVOs()[i].getItemkey(), templetVO
							.getRealViewItemVOs()[i].getItemkey(), templetVO
							.getRealViewItemVOs()[i].getItemname());
					comboBox.addItem(comBoBoxitemvo);
				}
			} else {
				comboBox.addItem("等于");
				comboBox.addItem("大于");
				comboBox.addItem("小于");
				comboBox.addItem("大于等于");
				comboBox.addItem("小于等于");
				comboBox.addItem("类似");
			}
		}

		public Object getCellEditorValue() {
			Object obj = comboBox.getSelectedItem();
			if (obj == null) {
				return null;
			}
			if (obj instanceof ComBoxItemVO) {
				return (ComBoxItemVO) comboBox.getSelectedItem();
			} else {
				return comboBox.getSelectedItem();
			}
		}

		public boolean isCellEditable(EventObject evt) {
			if (evt instanceof MouseEvent) {
				return ((MouseEvent) evt).getClickCount() >= 2;
			}
			return true;
		}
	}

	public String getStr_return_sql() {
		return str_return_sql;
	}
}
/*******************************************************************************
 * $RCSfile: QueryDialog.java,v $ $Revision: 1.6.2.4 $ $Date: 2009/06/12 05:25:15 $
 * 
 * $Log: QueryDialog.java,v $
 * Revision 1.6.2.4  2009/06/12 05:25:15  wangqi
 * *** empty log message ***
 *
 * Revision 1.6.2.3  2009/02/13 09:58:46  wangqi
 * *** empty log message ***
 *
 * Revision 1.6.2.2  2008/11/05 05:21:04  wangqi
 * *** empty log message ***
 *
 * Revision 1.1  2008/09/01 07:38:11  wangqi
 * *** empty log message ***
 *
 * Revision 1.8  2008/03/18 11:53:10  fuczh
 * 解决了查询时间判定:如果查询开始时间大于查询结束时间,终止查询并给于客户端提示
 * Revision 1.6 2007/09/18 06:33:44 yanghuan
 * 增加判断sql.substring(0,sql.indexOf("&"))返回-1时不做处理 Revision 1.5 2007/08/24
 * 07:09:14 yanghuan 解决增加了排序条件后就查询失败的bug
 * 
 * Revision 1.4 2007/07/31 08:47:07 sunxf MR#:Nova 20-18
 * 
 * Revision 1.3 2007/07/02 08:55:13 sunxb *** empty log message ***
 * 
 * Revision 1.2 2007/05/31 07:38:18 qilin code format
 * 
 * Revision 1.1 2007/05/17 06:01:07 qilin no message
 * 
 * Revision 1.5 2007/02/10 08:51:57 shxch *** empty log message ***
 * 
 * Revision 1.4 2007/02/07 01:54:00 lujian 调整页面高度 Revision 1.3 2007/02/05
 * 08:22:56 shxch *** empty log message ***
 * 
 * Revision 1.2 2007/01/30 05:14:31 lujian *** empty log message ***
 * 
 * 
 ******************************************************************************/
