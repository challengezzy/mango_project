/**************************************************************************
 * $RCSfile: BillListPanel.java,v $  $Revision: 1.25.2.33 $  $Date: 2010/04/07 09:30:28 $
 **************************************************************************/

package smartx.framework.metadata.ui;

import java.text.*;
import java.util.*;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;

import smartx.framework.common.ui.*;
import smartx.framework.common.ui.component.JNovaFileChooser;
import smartx.framework.common.ui.component.JNovaFileFilter;
import smartx.framework.common.utils.Sys;
import smartx.framework.common.vo.*;
import smartx.framework.metadata.bs.NovaServerEnvironment;
import smartx.framework.metadata.ui.ExportToExcel;
import smartx.framework.metadata.ui.NovaMessage;
import smartx.framework.metadata.ui.componentscard.*;
import smartx.framework.metadata.ui.componentslist.*;
import smartx.framework.metadata.ui.localcache.BillListColumnLocalSetting;
import smartx.framework.metadata.vo.*;
import smartx.framework.metadata.vo.jepfunctions.*;
import smartx.framework.metadata.vo.templetvo.*;


import java.io.File;


/**
 * 列表组件
 * 
 * @author James.W
 *
 */
public class BillListPanel extends JPanel implements QuickQueryActionIFC {

    private static final long serialVersionUID = 8051676743971788439L;

    private Component loadedFrame = null;
    private String str_templetcode = null;
    private Pub_Templet_1VO templetVO = null;
    private Pub_Templet_1_ItemVO[] templetItemVOs = null;
    JScrollPane scrollPanel_main = null;
    private JTable table = null;
    private JTable rowHeaderTable = null;
    private DefaultTableColumnModel rowHeaderColumnModel = null;
    private DefaultTableColumnModel columnModel = null;
    private TableColumn[] allTableColumns = null;
    private BillListModel tableModel = null;
    private String str_realsql = null; // 实际的SQL
    private Object[][] all_realValueData = null; // 当前数据
    private Object[][] curr_realValueData = null; // 当前数据
    private String _dataFrom="";//"Fetch";
    private int  _dataRows=0;//数据行数
    private int li_currpage = 1; // 当前数据
    private int datalength = 0;
    private int li_mouse_x, li_mouse_y = 0;
    boolean bo_tableislockcolumn = false; //
    Vector v_lockedcolumns = new Vector();
    private boolean showPopMenu = true;
    // private boolean hideItemenable = true;// 是否隐藏设置为不显示的列.默认为隐藏，在编辑模板时不隐藏.
    
    private int li_onepagerecords = 100; // 每页有多少条记录
    private JButton firstPageButton = null;
    private JButton PageupButton = null;
    private JButton PagedownButton = null;
    private JButton lastPageButton = null;
    private JButton goToPageButton = null;
    
    /**
     * 是否显示分页导航条
     */
    private boolean bo_isShowPageNavigation = true; 
    private boolean bo_isShowOperatorNavigation = false; // 是否显示操作数据库导航条...
    public JTextField goToPageTextField = null;
    public JLabel label_pagedesc = null;
    public String str_rownumberMark = "_RECORD_ROW_NUMBER";
    public boolean bo_ifProgramIsEditing = false; // 是否程序真正编辑
    public int[] searchcolumn = null;
    private String str_currsortcolumnkey = null;
    private int li_currsorttype = 0;   //当前的排序方向
    private Vector v_deleted_row = new Vector();
    private JPanel customerNavigationJPanel = null;
    private int templete_codecolumn_index = -1;
    private JPanel OperatorPanel = null;
    private JPanel PagePanel = null;
    private Vector v_listeners = new Vector(); // 反射注册的事件监听者!!!
    private boolean is_admin = false;
    private AbstractTempletVO abstractTempletVO = null;
    private Vector menuItem = null;

    private static Color[] RowColors=null;
	static{
		if(Sys.getInfo("UI_ROW_BGCOLOR")==null){
			RowColors=new Color[]{
				new Color(255,255,255),new Color(239,239,239)
			};
		}else{
			RowColors=UIUtil.getColors((String[])Sys.getInfo("UI_ROW_BGCOLOR"));			
		}		
	}
	
	////以下方法似乎没有必要/////////////////////////
    
    public int getDatalength() {
        return datalength;
    }
    
    public void setDatalength(int datalength) {
        this.datalength = datalength;
    }
    ////以下方法似乎没有必要/////////////////////////
    
    /**
     * 构造函数：不允许无参数创建
     */
    private BillListPanel() {
    }

    /**
     * 构造函数：创建按照指定元原模板编码设置的列表控件
     * 这里的元原模板，既可能是一个编码代号，又可能是元原模板的类实现。
     * @param _templetcode
     */
    public BillListPanel(String _templetcode) {
    	this(_templetcode, true, false);
    }

    /**
     * 构造函数：根据元原模板编码展示
     * @param _templetcode
     * @param _isShowPageNavigation
     * @param _isShowOperatorNavigation
     */
    public BillListPanel(String _templetcode, boolean _isShowPageNavigation, boolean _isShowOperatorNavigation) {
        if (_templetcode.indexOf(".") > 0) { //如果是个类名
            try {
                abstractTempletVO = (AbstractTempletVO) Class.forName(_templetcode).newInstance();
                init(abstractTempletVO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                templetVO = UIUtil.getPub_Templet_1VO(_templetcode);
            } catch (Exception e) {
                e.printStackTrace();
            } // 取得单据模板VO
            templetItemVOs = templetVO.getItemVos(); // 各项
            this.bo_isShowPageNavigation = _isShowPageNavigation; // 是否显示分页导航条
            this.bo_isShowOperatorNavigation = _isShowOperatorNavigation; // 是否显示数据库操作导航条
            initialize(); //
        }
    }
    
    /**
     * 构造函数：根据元原模板编码展示
     * @param tmpl
     * @param _isShowPageNavigation
     * @param _isShowOperatorNavigation
     */
    public BillListPanel(Pub_Templet_1VO tmpl, boolean _isShowPageNavigation, boolean _isShowOperatorNavigation) {
        templetVO = tmpl;
        templetItemVOs = templetVO.getItemVos(); // 各项
        bo_isShowPageNavigation = _isShowPageNavigation; // 是否显示分页导航条
        bo_isShowOperatorNavigation = _isShowOperatorNavigation; // 是否显示数据库操作导航条
        initialize(); 
    }

    /**
     * 构造函数：根据直接生成的抽象模板VO创建页面!!!,即配置数据不存在 pub_teplet_1表与pub_teplet_1_item表中的!!! 
     * @param _abstractTempletVO
     */
    public BillListPanel(AbstractTempletVO _abstractTempletVO) {
        init(_abstractTempletVO);
    }

    /**
     * 构造函数：根据VO创建
     * @param _abstractTempletVO
     * @param _isShowPageNavigation
     * @param _isShowOperatorNavigation
     */
    public BillListPanel(AbstractTempletVO _abstractTempletVO, boolean _isShowPageNavigation,
                         boolean _isShowOperatorNavigation) {
        try {
            templetVO = UIUtil.getPub_Templet_1VO(_abstractTempletVO.getPub_templet_1Data(),
                                                  _abstractTempletVO.getPub_templet_1_itemData());
        } catch (Exception e) {
            e.printStackTrace();
        } //
        templetItemVOs = templetVO.getItemVos(); // 各项
        this.bo_isShowPageNavigation = _isShowPageNavigation; // 是否显示分页导航条
        this.bo_isShowOperatorNavigation = _isShowOperatorNavigation; // 是否显示数据库操作导航条
        abstractTempletVO = _abstractTempletVO; //
        initialize(); //
    }
    
    
    /**
     * 初始化ListPanel，按照VO
     * @param _abstractTempletVO
     */
    private void init(AbstractTempletVO _abstractTempletVO) {
    	//this.str_templetcode = _abstractTempletVO.getPub_templet_1Data().getStringValue("templetcode"); //
        try {
            templetVO = UIUtil.getPub_Templet_1VO(_abstractTempletVO.getPub_templet_1Data(),
                                                  _abstractTempletVO.getPub_templet_1_itemData());
        } catch (Exception e) {
            e.printStackTrace();
        } //
        templetItemVOs = templetVO.getItemVos(); // 各项
        abstractTempletVO = _abstractTempletVO; //
        initialize(); //
    }
    
    /**
     * 获得数据的操作 
     * 处理分页操作的初始获得数据部分 James.W
     * 主要处理了获得记录总行数，判断是否显示页控制，如果不显示则直接取出所有记录，否则默认只获取第一页。 
     * @param _ds
     * @param _sql
     */
    public void QueryDataByDS(final String _ds, final String _sql) {
        this.stopEditing(); //如果正在编辑就放弃了
        this.str_realsql = _sql;
        //清空表格
        clearTable(); 
        
        try {
        	//明确 数据来源由检索获得
        	this._dataFrom="Fetch";
        	
        	//如果不分页或者行数在1000行以内，则直接读出数据
        	if(this.bo_isShowPageNavigation==false){
        		//判断行数是否过多（10000行以上）
            	//if(this._dataRows>=10000){
            	//	NovaMessage.show("检索返回数据集过大（超过10000行），将会显示较长时间！");
            	//}
            	//取数包括行号
            	all_realValueData = fetchData(_ds,_sql,-1,-1);
            	setTableValue();
        	}else{
        		//把获得记录总行数作为异步处理
                (new Thread(){
            		public void run(){    			
            			try {
            				//sleep(5000);
            				//获得数据的行数
        					_dataRows=UIUtil.getBillListRowCountByDS(_ds, _sql, templetVO, NovaClientEnvironment.getInstance());
        				} catch (Exception e) {
        					_dataRows=0;
        				}
        				refreshPageDesc();				
            		}
            	}).start();
        		
        		
        		//默认调用第一页
        		onGoToPage();
        	}
        } catch (Exception e) {
            //e.printStackTrace();
        	all_realValueData=null;
        }
    }
    
    
    

    /**
     * 根据直接生成的抽象模板VO创建页面!!!,即配置数据不存在 pub_teplet_1表与pub_teplet_1_item表中的!!!
     *
     * @param _abstractTempletVO
     */
    public void initialize() {
    	this.str_templetcode = templetVO.getTempletcode();
    	//1. 是否管理员
        if (NovaClientEnvironment.getInstance().isAdmin()) {
            this.is_admin = true;
        }        
        //2. 设置layout布局
        this.setLayout(new BorderLayout());
        //3. 设置标题 
        //TODO 区域标题？窗口标题？
        TitledBorder border = BorderFactory.createTitledBorder("[" + templetVO.getTempletname() + "]");
        //TODO 设置字体风格样式，将来应该用统一的方式设置，方法待处理
        border.setTitleFont(new Font("宋体", Font.PLAIN, 12));        
        border.setTitleColor(Color.BLUE); //

        //4. 创建滑动面板
        scrollPanel_main = new JScrollPane(); //
        //5. 获得表格并加入面板
        scrollPanel_main.getViewport().add(getTable()); // 实际内容的表格!!

        //
        JPanel panel_north = new JPanel();
        panel_north.setLayout(new FlowLayout(FlowLayout.LEFT));

        
        //为什么这里要调用getPagePanel()两次？ 注释掉下面这句
        //getPagePanel(); // 生成分页栏
        if (bo_isShowPageNavigation) {
            panel_north.add(getPagePanel()); // 分页导航条
        }

        if (bo_isShowOperatorNavigation) {
            panel_north.add(getOperatorPanel()); // 操作数据库导航条
        }

        if (getCustomerNavigationJPanel() != null) { // 如果有用户自定义导航条,则加入
            panel_north.add(getCustomerNavigationJPanel()); // 用户自定义航条
        }

        AbstractListCustomerButtonBarPanel listCustPanel = getListCustPanel();

        if (listCustPanel != null) {
            panel_north.add(listCustPanel);
        }

        this.add(panel_north, BorderLayout.NORTH); // 分页导航条

        this.add(scrollPanel_main, BorderLayout.CENTER);

        rowHeaderColumnModel = new DefaultTableColumnModel();
        rowHeaderColumnModel.addColumn(getRowNumberColumn()); // 加入行号列

        rowHeaderTable = new JTable(getTableModel(), rowHeaderColumnModel); // 创建新的表..
        rowHeaderTable.setRowHeight(20);
        rowHeaderTable.setRowSelectionAllowed(true);
        rowHeaderTable.setColumnSelectionAllowed(false);
        rowHeaderTable.setBackground(new Color(240, 240, 240));
        rowHeaderTable.getTableHeader().setReorderingAllowed(false);
        rowHeaderTable.getTableHeader().setResizingAllowed(false);
        rowHeaderTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        getTable().setSelectionModel(rowHeaderTable.getSelectionModel());
        JViewport jv = new JViewport();
        jv.setView(rowHeaderTable);
        int li_height = new Double(rowHeaderTable.getMaximumSize().getHeight()).intValue();
        jv.setPreferredSize(new Dimension(45, li_height));
        scrollPanel_main.setRowHeader(jv);
        scrollPanel_main.setCorner(JScrollPane.UPPER_LEFT_CORNER, rowHeaderTable.getTableHeader());
        scrollPanel_main.updateUI(); //
        
        //获得并设置缓冲的列显示信息
        BillListColumnLocalSetting col=new BillListColumnLocalSetting();
        String[] colkeys=col.getShowCols(this.str_templetcode);
        if(colkeys.length>0){
	        clearAllColumn();
	        for (int i = 0; i < colkeys.length; i++) {
	            int li_modelindex = findModelIndex(colkeys[i]) - 1;
	            templetItemVOs[li_modelindex].setListisshowable(Boolean.TRUE);
	            insertColumn(i, li_modelindex);
	        }
        }        
    }

    private AbstractListCustomerButtonBarPanel getListCustPanel() {
        if (this.templetVO.getListcustpanel() == null || this.templetVO.getListcustpanel().trim().equals("")) {
            return null;
        }
        try {
            AbstractListCustomerButtonBarPanel panel = 
            	(AbstractListCustomerButtonBarPanel) Class.forName(this.
                templetVO.getListcustpanel()).newInstance();

            panel.setBillListPanel(this);
            panel.initialize();
            return panel;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    
    //页控制面板
    private JPanel getPagePanel() {
        
    	if (PagePanel != null) {
            return PagePanel;
        }

        PagePanel = new JPanel();
        PagePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        firstPageButton = new JButton(UIUtil.getImage(Sys.getSysRes("pageorder.first.icon")));
        PageupButton = new JButton(UIUtil.getImage(Sys.getSysRes("pageorder.previous.icon")));
        PagedownButton = new JButton(UIUtil.getImage(Sys.getSysRes("pageorder.next.icon")));
        lastPageButton = new JButton(UIUtil.getImage(Sys.getSysRes("pageorder.last.icon")));
        JLabel Label1 = new JLabel(" 到页码");
        
        goToPageTextField = new JTextField("1");
        goToPageTextField.setHorizontalAlignment(SwingConstants.CENTER);

        goToPageButton = new JButton(UIUtil.getImage(Sys.getSysRes("pageorder.goto.icon")));

        label_pagedesc = new JLabel("", SwingConstants.CENTER);

        goToPageTextField.setPreferredSize(new Dimension(20, 18)); //
        firstPageButton.setPreferredSize(new Dimension(17, 17));
        PageupButton.setPreferredSize(new Dimension(17, 17));
        PagedownButton.setPreferredSize(new Dimension(17, 17));
        lastPageButton.setPreferredSize(new Dimension(17, 17));
        goToPageButton.setPreferredSize(new Dimension(17, 17));

        firstPageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onGoToFirstPage(); // 跳到首页
            }
        });

        PageupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onGoToPreviousPage(); // 跳到前一页
            }
        });

        PagedownButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                onGoToNextPage(); // 跳到下一页
            }
        });

        lastPageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onGoToLastPage(); // 跳到最后一页
            }
        });

        goToPageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onGoToPage(); // 跳到首页
            }
        });

        PagePanel.add(firstPageButton);
        PagePanel.add(PageupButton);
        PagePanel.add(PagedownButton);
        PagePanel.add(lastPageButton);
        PagePanel.add(Label1);
        PagePanel.add(goToPageTextField);
        PagePanel.add(goToPageButton);
        PagePanel.add(label_pagedesc);
        return PagePanel;
    }

    /**
     * 设置所有Item中的某一项为某值
     *
     * @param _itemkey
     * @param _obj
     */
    public void setAllItemValue(String _itemkey, Object _obj) {
        for (int i = 0; i < this.templetItemVOs.length; i++) {
            templetItemVOs[i].setAttributeValue(_itemkey, _obj);
        }
    }

    //	设置是否可以编辑!!
    public void setEditable(String _itemkey, boolean _ifedit) {
        for (int i = 0; i < this.templetItemVOs.length; i++) {
            if (templetItemVOs[i].getItemkey().equalsIgnoreCase(_itemkey)) {
                if (_ifedit) {
                    templetItemVOs[i].setAttributeValue("listiseditable", "1"); //
                } else {
                    templetItemVOs[i].setAttributeValue("listiseditable", "4"); //
                }
                break;
            }
        }
    }

    //设置是否可以编辑!!
    public void setEditable(boolean _ifedit) {
        for (int i = 0; i < this.templetItemVOs.length; i++) {
            if (_ifedit) {
                templetItemVOs[i].setAttributeValue("listiseditable", "1"); //
            } else {
                templetItemVOs[i].setAttributeValue("listiseditable", "4"); //
            }
        }
    }

    private JPanel getOperatorPanel() {
        OperatorPanel = new JPanel();
        FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
        layout.setHgap(10);
        OperatorPanel.setLayout(layout);

        JButton btn_insert = new JButton(UIUtil.getImage("images/platform/insert.gif")); //
        JButton btn_delete = new JButton(UIUtil.getImage("images/platform/delete.gif")); //
        JButton btn_save = new JButton(UIUtil.getImage("images/platform/savedata.gif")); //
        JButton btn_refresh = new JButton(UIUtil.getImage("images/platform/refresh.gif")); //
        JButton btn_query = new JButton(UIUtil.getImage("images/platform/query.gif")); //

        btn_insert.setToolTipText("新增记录");
        btn_delete.setToolTipText("删除记录");
        btn_save.setToolTipText("保存");
        btn_refresh.setToolTipText("刷新");
        btn_query.setToolTipText("查询"); //

        btn_insert.setPreferredSize(new Dimension(18, 18));
        btn_delete.setPreferredSize(new Dimension(18, 18));
        btn_save.setPreferredSize(new Dimension(18, 18));
        btn_refresh.setPreferredSize(new Dimension(18, 18));
        btn_query.setPreferredSize(new Dimension(18, 18));

        btn_insert.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                newRow();
            }
        });

        btn_delete.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                removeRow();
            }
        });

        btn_save.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SaveData();
            }
        });

        btn_refresh.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                refreshData(); //
            }
        });

        btn_query.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                onQuery(); //
            }
        });

        OperatorPanel.add(btn_insert);
        OperatorPanel.add(btn_delete);
        OperatorPanel.add(btn_save);
        OperatorPanel.add(btn_refresh);
        OperatorPanel.add(btn_query);

        return OperatorPanel;

    }
    
    /**
     * 获得对表格空间的引用
     * @return JTable控件
     */
    public JTable getTable() {
        if (table == null) {
        	table = new JTable(getTableModel(), getColumnModel());
            
        	table.setShowHorizontalLines(false);
        	table.setShowVerticalLines(false);
        	
        	table.setRowHeight(20);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            new TableAdapter(table);
            if (is_admin) {
            	JMenuItem item = new JMenuItem("查看记录");
            	item.addActionListener(new ActionListener(){

					public void actionPerformed(ActionEvent e) {
						showDetail();
					}
            		
            	});
            	addPopupMenuItems(item);
            }
            JTableHeader jtableheader = table.getTableHeader();
            
            jtableheader.addMouseListener(new MouseAdapter() {

                public void mousePressed(MouseEvent evt) {
                    if (evt.getButton() == MouseEvent.BUTTON3 && showPopMenu) {
                        showPopMenu(evt.getComponent(), evt.getX(), evt.getY()); // 弹出菜单
                    } else if (evt.getButton() == MouseEvent.BUTTON1 && evt.getClickCount() == 2) {
                        sortByColumn(evt.getPoint());
                    }
                }

                public void mouseReleased(MouseEvent e) {
                }
            });
            jtableheader.setDefaultRenderer(new SortHeaderRenderer(this.templetVO, this.is_admin));
            
            
            table.addMouseListener(new MouseAdapter() {

                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        JPopupMenu menu = new JPopupMenu();
                        for(int k=0;k<menuItem.size();k++)
                        {
                        	JMenuItem item = (JMenuItem)menuItem.elementAt(k);
                        	menu.add(item);
                        }
                        menu.show(table, e.getX(), e.getY());
                    }
                }
            });
            
            
//            //TODO 增加列侦听事件，用于
//            table.getColumnModel().addColumnModelListener(new TableColumnModelListener(){
//            		
//            	}
//            );
            
            
        }
        

        return table;
    }

    /**
     * 弹出菜单
     *
     * @param _compent
     * @param _x
     * @param _y
     */
    private void showPopMenu(Component _compent, int _x, int _y) {
        JPopupMenu popmenu_header = new JPopupMenu();
        JMenuItem item_column_lock = new JMenuItem("锁定"); // 锁定
        JMenuItem item_column_search = new JMenuItem("定位"); // 定位
        JMenuItem item_column_quickputvalue = new JMenuItem("快速赋值"); // 快速赋值

        JMenu menu_table_exportprint = new JMenu("导出Excel");
        JMenuItem item_column_exportprintAllData = new JMenuItem("全数据");
        JMenuItem item_column_exportprintShowData = new JMenuItem("显示数据");
        JMenuItem item_column_exportAllSeletedData = new JMenuItem("所有显示列数据");//added by chenxj 2008/08/30 for MR#:Nova 20-29 
        
        menu_table_exportprint.add(item_column_exportprintAllData);
        menu_table_exportprint.add(item_column_exportprintShowData);
        menu_table_exportprint.add(item_column_exportAllSeletedData);//added by chenxj 2008/08/30 for MR#:Nova 20-29 

        JMenuItem item_column_unlock = new JMenuItem("解锁"); // 解锁
        JMenuItem item_column_piechart = new JMenuItem("饼图"); // 解锁
//        JMenuItem item_column_ascsort = new JMenuItem("升序排序"); // 列锁定
//        JMenuItem item_column_descsort = new JMenuItem("降序排序"); // 列锁定

        
        // fucunzhan add 2008.3.17 begin
		JMenuItem item_table_showall_asc = new JMenuItem("对结果集升序排序");
		JMenuItem item_table_showall_desc = new JMenuItem("对结果集降序排序");
		// fucunzhan add 2008.3.17 end
		
        
		JMenuItem item_table_showhidecolumn = new JMenuItem("列显示排序");
        JMenuItem item_table_sortgrouprow = new JMenuItem("行排序与分组");

        JMenu menu_table_templetmodify = new JMenu("快速模板编辑");

        JMenuItem item_table_templetmodify_1 = new JMenuItem("第一种编辑方式");
        JMenuItem item_table_templetmodify_2 = new JMenuItem("第二种编辑方式");

        menu_table_templetmodify.add(item_table_templetmodify_1);
        menu_table_templetmodify.add(item_table_templetmodify_2);

        TableColumn col = this.getClickedTableColumn();
        Pub_Templet_1_ItemVO current_item = null;
        String key = (String) col.getIdentifier();
        for (int i = 0; i < templetItemVOs.length; i++) {
            if (key.equals(templetItemVOs[i].getItemkey())) {
                current_item = templetItemVOs[i];
                break;
            }
        }
        if (current_item != null && !current_item.isListiseditable().equalsIgnoreCase("1")) {
            item_column_quickputvalue.setEnabled(false);
        }
        JMenuItem item_table_showsql = new JMenuItem("查看当前SQL"); //

        if (this.bo_tableislockcolumn) {
            item_column_lock.setEnabled(false);
            item_column_unlock.setEnabled(true);
        } else {
            item_column_lock.setEnabled(true);
            item_column_unlock.setEnabled(false);
        }

        item_column_lock.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                lockColumn();
            }
        });
        
        // fucunzhan add 2008.3.17 begin
		item_table_showall_asc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				sortShowAllAsc();
				
			}
		});

		item_table_showall_desc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sortShowAllDesc();
			}
		});
		// fucunzhan add 2008.3.17 end

        item_column_quickputvalue.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                quickPutValue();
            }
        });

        item_column_exportprintAllData.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                exportExcelAllData();
            }
        });

        item_column_exportprintShowData.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                exportExcelShowData();
            }
        });
        //added by chenxj 2008/08/30 for MR#:Nova 20-29 BEGIN
        item_column_exportAllSeletedData.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				exportAllSelectedData();
			}
        });
        //added by chenxj 2008/08/30 for MR#:Nova 20-29 END

        item_column_unlock.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                unlockColumn();
            }
        });

        item_column_piechart.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                showPieChart();
            }
        });
        item_column_search.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                quickSearch();
            }
        });
        item_table_showhidecolumn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                reShowHideTableColumn();
            }
        });

        item_table_sortgrouprow.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                reSortRow();
            }
        });

        item_table_templetmodify_1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                modifyTemplet(templetVO.getTempletcode());
            }
        });

        item_table_templetmodify_2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                modifyTemplet2(templetVO.getTempletcode());
            }
        });

        item_table_showsql.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                showSQL();
            }
        });
        popmenu_header.add(item_column_lock); //
        popmenu_header.add(item_column_search);
        popmenu_header.add(item_column_quickputvalue); //
        popmenu_header.add(menu_table_exportprint); //
        popmenu_header.add(item_column_unlock); //
        popmenu_header.add(item_column_piechart); //
//        popmenu_header.add(item_column_ascsort);
//        popmenu_header.add(item_column_descsort);

        popmenu_header.addSeparator();
        
        // fucunzhan add 2008.3.17 begin
		popmenu_header.add(item_table_showall_asc);
		popmenu_header.add(item_table_showall_desc);
		// fucunzhan add 2008.3.17 end
		
		popmenu_header.addSeparator();

        popmenu_header.add(item_table_showhidecolumn);
        popmenu_header.add(item_table_sortgrouprow);
        if (is_admin) {
            popmenu_header.add(menu_table_templetmodify); // 快速模板编辑
            popmenu_header.add(item_table_showsql); // 快速模板编辑
        }
        popmenu_header.show(_compent, _x, _y); //
        li_mouse_x = _x;
        li_mouse_y = _y;
    }

    private void showPopMenu2(Component _compent, int _x, int _y) {
        JPopupMenu popmenu_header2 = new JPopupMenu();
        JMenuItem item_column_unlock = new JMenuItem("解锁"); // 解锁
        item_column_unlock.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                unlockColumn();
            }
        });

        popmenu_header2.add(item_column_unlock); //
        popmenu_header2.show(_compent, _x, _y); //
    }

    public void hidePopMenu() {
        this.showPopMenu = false;
    }

    public Object getValueAtTable(int _row, int _column) {
        return getTable().getValueAt(_row, _column); //
    }

    public Object getValueAt(int _row, int _column) {
        return getTableModel().getValueAt(_row, _column); //
    }

    public Object getValueAt(int _row, String _key) {
        return getTableModel().getValueAt(_row, _key);
    }

    public Object[] getValueAtRow(int _row) {
        return getTableModel().getValueAtRow(_row);
    }

    public HashMap getValueAtRowWithHashMap(int _row) {
        return getTableModel().getValueAtRowWithHashMap(_row);
    }

    public VectorMap getValueAtRowWithVectorMap(int _row) {
        return getTableModel().getValueAtRowWithVectorMap(_row);
    }

    public VectorMap getValueAtModelWithVectorMap(int _row) {
        return getTableModel().getValueAtModelWithVectorMap(_row);
    }

    public VectorMap getSavedValueAtModelWithVectorMap(int _row) {
        return getTableModel().getSavedValueAtModelWithVectorMap(_row);
    }

    public Object[][] getValueAtAll() {
        return getTableModel().getValueAtAll();
    }

    public String getViewValueAtData(int _row, int _col) {
        Object obj = all_realValueData[_row][_col];
        return getObjectViewValue(obj);
    }

    public String getViewValueAtModel(int _row, int _col) {
        Object obj = getValueAtTable(_row, _col);
        return getObjectViewValue(obj);
    }

    public String getRealValueAtTable(int _row, int _column) {
        Object obj = getValueAtTable(_row, _column);
        return getObjectRealValue(obj);
    }

    public String getRealValueAtModel(int _row, int _column) {
        return getTableModel().getRealValueAtModel(_row, _column);
    }

    public String getRealValueAtModel(int _row, String _key) {
        return getTableModel().getRealValueAtModel(_row, _key);
    }

    public String[][] getRealValueAtModel() {
        return getTableModel().getRealValueAtModel();
    }

    public void setValueAt(Object _obj, int _row, int _column) {
        this.getTableModel().setValueAt(_obj, _row, _column);
    }

    public void setValueAt(Object _obj, int _row, String _key) {
        this.getTableModel().setValueAt(_obj, _row, _key); //
    }

    public void setRealValueAt(String _value, int _row, String _key) {
        this.getTableModel().setRealValueAt(_value, _row, _key); //
    }

    /**
     * 在末尾加入一个空行
     *
     * @return
     */
    public int addEmptyRow() {
        return this.getTableModel().addEmptyRow(); //
    }

    public int addRowWithDefaultValue() {
        int li_newrow = this.getTableModel().addEmptyRow(); //
        VectorMap defaultvalue = getDefaultValue();
        Object[][] value = defaultvalue.getAllData();
        for (int i = 0; i < value.length; i++) {
            this.setRealValueAt("" + value[i][1], li_newrow, (String) value[i][0]);
        }
        return li_newrow;
    }

    public boolean moveUpRow() {
        stopEditing();
        int li_currrow = getTable().getSelectedRow(); // 取得当前行
        if (li_currrow < 0) {
            return false;
        }

        if (li_currrow > 0) {
            bo_ifProgramIsEditing = true;
            getTableModel().moveRow(li_currrow, li_currrow, li_currrow - 1);
            getTable().setRowSelectionInterval(li_currrow - 1, li_currrow - 1);
            bo_ifProgramIsEditing = false;
            return true;
        }

        return false;
    }

    public boolean moveDownRow() {
        stopEditing();
        int li_currrow = getTable().getSelectedRow(); // 取得当前行
        if (li_currrow < 0) {
            return false;
        }

        if (li_currrow < getTableModel().getRowCount() - 1) {
            bo_ifProgramIsEditing = true;
            getTableModel().moveRow(li_currrow, li_currrow, li_currrow + 1);
            getTable().setRowSelectionInterval(li_currrow + 1, li_currrow + 1);
            bo_ifProgramIsEditing = false;
            return true;
        }
        return false;
    }

    public int newRow() {
        stopEditing();
        int li_currrow = getTable().getSelectedRow(); // 取得当前行
        int li_newrow = -1;
        if (li_currrow < 0) { // 如果没有选中行,则在末尾加
            li_newrow = addEmptyRow(); // 在末尾加入
        } else {
            li_newrow = insertEmptyRow(li_currrow); // 插入
        }

        if (li_newrow >= 0) {
            getTable().setRowSelectionInterval(li_newrow, li_newrow);
            setValueAt(new RowNumberItemVO("INSERT", li_newrow), li_newrow, this.str_rownumberMark); //

            if (templetVO.getPksequencename() != null) {
                String str_sql = "select " + templetVO.getPksequencename() + ".nextval from dual";
                String[][] str_data = null;
                try {
                    str_data = UIUtil.getStringArrayByDS(getDataSourceName(), str_sql);
                } catch (NovaRemoteException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (str_data != null && str_data.length > 0) {
                    String str_newid = str_data[0][0];
                    String str_pkfieldname = templetVO.getPkname();
                    if (str_pkfieldname != null) {
                        setValueAt(str_newid, li_newrow, str_pkfieldname); // 设置主键值
                    }
                }
            }

            // 设置默认值..
            VectorMap defaultvalue = getDefaultValue();
            Object[][] value = defaultvalue.getAllData();
            for (int i = 0; i < value.length; i++) {
                this.setRealValueAt("" + value[i][1], li_newrow, (String) value[i][0]);
            }

            Rectangle rect = getTable().getCellRect(li_newrow, 0, true);
            getTable().scrollRectToVisible(rect);

        }

        return li_newrow;
    }

    /**
     * 删除指定的记录
     * @param _row
     */
    public void removeRow(int _row) {
    	//设定标志为删除
        this.setRowStatusAs(_row, NovaConstants.BILLDATAEDITSTATE_DELETE);
        //取出记录BillVO
        BillVO billVO = getBillVO(_row); //
        v_deleted_row.add(billVO); //
        getTableModel().removeRow(_row); //
    }

    public void removeAllDeleteSQLS() {
        v_deleted_row.clear();
    }

    /**
     * 删除所有记录行
     */
    public void removeAllRows() {
        for (int i = getTable().getRowCount() - 1; i >= 0; i--) {
            removeRow(i);
        }
    }

    public void removeRow() {
        stopEditing();
        int li_currrow = getTable().getSelectedRow(); // 取得当前行
        if (li_currrow < 0) {
            JOptionPane.showMessageDialog(this, "请选择一条记录进行此操作!!");
            return;
        }
        this.setRowStatusAs(li_currrow, NovaConstants.BILLDATAEDITSTATE_DELETE);
        BillVO billVO = getBillVO(li_currrow); //
        v_deleted_row.add(billVO); //
        getTableModel().removeRow(li_currrow); //
    }

    public void removeRow(boolean quiet) {
        stopEditing();
        int li_currrow = getTable().getSelectedRow(); // 取得当前行
        if (li_currrow < 0) {
            if (!quiet) {
                JOptionPane.showMessageDialog(this, "请选择一条记录进行此操作!!");
            }
            return;
        }
        this.setRowStatusAs(li_currrow, NovaConstants.BILLDATAEDITSTATE_DELETE);
        BillVO billVO = getBillVO(li_currrow); //
        v_deleted_row.add(billVO); //
        getTableModel().removeRow(li_currrow); //
    }

    public void stopEditing() {
        try {
            if (getTable().getRowCount() >= 0 && getTable().getCellEditor() != null) {
                getTable().getCellEditor().stopCellEditing(); //
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String[] getDeleteRowValues() {
        return (String[]) v_deleted_row.toArray(new String[0]);
    }

    public String[] getOperatorSQLs() {
        Vector v_all = new Vector();
        v_all.addAll(getDeleteSQLVector());

        for (int i = 0; i < getTableModel().getRowCount(); i++) {
            String str_type = ( (RowNumberItemVO) getTableModel().getValueAt(i, str_rownumberMark)).getState(); //
            if (str_type.equals("INSERT")) {
                v_all.add(getInsertSQL(i));
            } else if (str_type.equals("UPDATE")) {
                v_all.add(getUpdateSQL(i));
            } else if (str_type.equals("INIT")) {

            } else {
            }
        }

        return (String[]) v_all.toArray(new String[0]); //
    }

    public void executeDeleteOperationOnly() {
        this.stopEditing(); // 结束编辑
        String[] delsqls = getDeleteSQLs();
        v_deleted_row.clear();
        try {
            UIUtil.executeBatchByDS(str_templetcode, delsqls); // 有问题!!
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "删除数据失败!!" + e.getMessage());
        }
    }

    public String[] clearDeleteSQLs() {
        String[] delsqls = getDeleteSQLs();
        this.v_deleted_row.clear();
        return delsqls;
    }

    public Integer[] getNotInitRowNums() {
        ArrayList list = new ArrayList();
        for (int i = 0; i < getTableModel().getRowCount(); i++) {
            String str_type = ( (RowNumberItemVO) getTableModel().getValueAt(i, str_rownumberMark)).getState();
            if (str_type.equalsIgnoreCase(NovaConstants.BILLDATAEDITSTATE_INIT)) {
                continue;
            }
            list.add(new Integer(i));
        }
        return (Integer[]) list.toArray(new Integer[0]);

    }

    public String[] getDeleteSQLs() {
        return (String[]) getDeleteSQLVector().toArray(new String[0]); //
    }

    /**
     * 
     * @return
     */
    public Vector getDeleteSQLVector() {
        Vector v_return = new Vector();
        String str_del_sql = "";
        for (int i = 0; i < v_deleted_row.size(); i++) {
            BillVO billVO = (BillVO) v_deleted_row.get(i);
            String str_pkvalue = (String) billVO.getObject(templetVO.getPkname()); // //
            str_del_sql = "delete from " + templetVO.getSavedtablename() + " where " + templetVO.getPkname() + "='" +
                str_pkvalue + "'"; //
            v_return.add(str_del_sql);
        }
        return v_return;
    }

    public int insertEmptyRow(int _row) {
        return this.getTableModel().insertEmptyRow(_row); //
    }

    public void addRow(Object[] _objs) {
        this.getTableModel().addRow(_objs);
    }

    public void addRow(HashMap _map) {
        this.getTableModel().addRow(_map); //
    }

    public void addRow(VectorMap _map) {
        this.getTableModel().addRow(_map); //
    }

    public void insertRow(int _row, Object[] _objs) {
        this.getTableModel().insertRow(_row, _objs); //
    }

    /**
     * 取得一行的BillVO
     *
     * @param _row
     * @return
     */
    public BillVO getBillVO(int _row) {
        BillVO vo = new BillVO();
        vo.setQueryTableName(templetVO.getTablename());
        vo.setSaveTableName(templetVO.getSavedtablename()); //
        vo.setPkName(templetVO.getPkname()); // 主键字段名
        vo.setSequenceName(templetVO.getPksequencename()); // 序列名

        int li_length = templetItemVOs.length;

        // 所有ItemKey
        String[] all_Keys = new String[li_length + 1]; //
        all_Keys[0] = this.str_rownumberMark; // 行号
        for (int i = 1; i < all_Keys.length; i++) {
            all_Keys[i] = this.templetVO.getItemKeys()[i - 1];
        }

        // 所有的名称
        String[] all_Names = new String[li_length + 1]; //
        all_Names[0] = "行号"; // 行号
        for (int i = 1; i < all_Names.length; i++) {
            all_Names[i] = this.templetVO.getItemNames()[i - 1];
        }

        String[] all_Types = new String[li_length + 1]; //
        all_Types[0] = "行号"; // 行号
        for (int i = 1; i < all_Types.length; i++) {
            all_Types[i] = this.templetVO.getItemTypes()[i - 1];
        }

        String[] all_ColumnTypes = new String[li_length + 1]; //
        all_ColumnTypes[0] = "NUMBER"; // 行号
        for (int i = 1; i < all_ColumnTypes.length; i++) {
            all_ColumnTypes[i] = this.templetItemVOs[i - 1].getSavedcolumndatatype(); //
        }

        boolean[] bo_isNeedSaves = new boolean[li_length + 1];
        bo_isNeedSaves[0] = false; // 行号
        for (int i = 1; i < bo_isNeedSaves.length; i++) {
            bo_isNeedSaves[i] = this.templetItemVOs[i - 1].isNeedSave();
        }

        vo.setKeys(all_Keys); //
        vo.setNames(all_Names); //
        vo.setItemType(all_Types); // 控件类型!!
        vo.setColumnType(all_ColumnTypes); // 数据库类型!!
        vo.setNeedSaves(bo_isNeedSaves); // 是否需要保存!!
        vo.setDatas(this.getValueAtRow(_row)); // 设置真正的数据!!
        return vo;
    }

    // 得到删除的BillVO[]
    public BillVO[] getDeleteBillVOs() {
        return (BillVO[]) v_deleted_row.toArray(new BillVO[0]);
    }

    // 得到所有新增的BillVO
    public BillVO[] getInsertBillVOs() {
        ArrayList deletevos = new ArrayList();
        for (int i = 0; i < this.getTable().getRowCount(); i++) {
            if (this.getRowNumberEditState(i).equalsIgnoreCase(NovaConstants.BILLDATAEDITSTATE_INSERT)) {
                deletevos.add(this.getBillVO(i));
            }
        }
        return (BillVO[]) deletevos.toArray(new BillVO[0]);
    }

    // 得到所有修改过的BillVO
    public BillVO[] getUpdateBillVOs() {
        ArrayList deletevos = new ArrayList();
        for (int i = 0; i < this.getTable().getRowCount(); i++) {
            if (this.getRowNumberEditState(i).equalsIgnoreCase(NovaConstants.BILLDATAEDITSTATE_UPDATE)) {
                deletevos.add(this.getBillVO(i));
            }
        }
        return (BillVO[]) deletevos.toArray(new BillVO[0]);
    }
    
    public void updateVersion(){
    	for (int i = 0; i < this.getTable().getRowCount(); i++) {
            if (this.getRowNumberEditState(i).equalsIgnoreCase(NovaConstants.BILLDATAEDITSTATE_UPDATE)) {
            	if (this.containsItemKey("VERSION")) { //如果有版本字段!!
            		Object ver = this.getValueAt(i, "version");
            		int version=0;
                	if(ver==null){
                		version=0;
                	}else if(ver instanceof String){
                		String tmp=(String)ver;
                		if(tmp.indexOf(".")>=0){
                			try{
                    			version=(new Double(tmp)).intValue();
                    		}catch(Exception e){
                    			version=0;
                    		}	
                		}
                		try{
                			version=Integer.parseInt(tmp);
                		}catch(Exception e){
                			version=0;
                		}
                		
                	}else if(ver instanceof Integer){
                		version=((Integer)ver).intValue()+1;
                	}else{
                		version=0;
                	}
                	ver=new Integer(version+1);
            		this.setValueAt(ver, i, "version");
            	}
            }
        }
    }
    public BillVO[] getBillVOs() {
        ArrayList _temp = new ArrayList();
        _temp.addAll(v_deleted_row);
        this.clearDeleteBillVOs();
        for (int i = 0; i < this.getTable().getRowCount(); i++) {
            _temp.add(this.getBillVO(i));
        }
        return (BillVO[]) _temp.toArray(new BillVO[0]);
    }

    private String getObjectRealValue(Object _obj) {
        if (_obj == null) {
            return null;
        }

        if (_obj instanceof String) {
            return (String) _obj;
        } else if (_obj instanceof ComBoxItemVO) {
            ComBoxItemVO vo = (ComBoxItemVO) _obj;
            return vo.getId();
        } else if (_obj instanceof RefItemVO) {
            RefItemVO vo = (RefItemVO) _obj;
            return vo.getId();
        } else {
            return _obj.toString();
        }
    }

    private String getObjectViewValue(Object _obj) {
        if (_obj == null) {
            return null;
        }

        if (_obj instanceof String) {
            return (String) _obj;
        } else if (_obj instanceof ComBoxItemVO) {
            ComBoxItemVO vo = (ComBoxItemVO) _obj;
            return vo.getName();
        } else if (_obj instanceof RefItemVO) {
            RefItemVO vo = (RefItemVO) _obj;
            return vo.getName();
        } else {
            return _obj.toString();
        }
    }

    public int findColumnIndex(String _key) {
        int li_columncount = this.getColumnModel().getColumnCount();
        for (int i = 0; i < li_columncount; i++) {
            TableColumn column = this.getColumnModel().getColumn(i);
            if ( ( (String) column.getIdentifier()).equals(_key)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 插入一行，并将该行状态设为init
     *
     * @param _row
     * @param _values
     */
    public void insertRowWithInitStatus(int _row, HashMap _values) {
        if (_row < 0) {
            this.addEmptyRow(); //
            int li_rowcount = this.getTable().getModel().getRowCount();
            Set keys = _values.keySet();
            for (Iterator it = keys.iterator(); it.hasNext(); ) {
                String key = (String) it.next();
                if (key.equalsIgnoreCase("VERSION") && this.getTableModel().findModelIndex(key) < 0) {
                    continue;
                } else {
                    this.setValueAt(_values.get(key), li_rowcount - 1, key);
                }
            }
            if (li_rowcount - 1 >= 0) {
                Rectangle rect = this.getTable().getCellRect(li_rowcount - 1, 0, true);
                this.getTable().scrollRectToVisible(rect);
                this.getTable().setRowSelectionInterval(li_rowcount - 1, li_rowcount - 1);
            }
            bo_ifProgramIsEditing = true;
            this.setValueAt(new RowNumberItemVO(NovaConstants.BILLDATAEDITSTATE_INIT, li_rowcount - 1), li_rowcount - 1,
                            "_RECORD_ROW_NUMBER");
        } else {
            this.insertEmptyRow(_row + 1);
            Set keys = _values.keySet();
            for (Iterator it = keys.iterator(); it.hasNext(); ) {
                String key = (String) it.next();
                if (key.equalsIgnoreCase("VERSION") && this.getTableModel().findModelIndex(key) < 0) {
                    continue;
                } else {
                    this.setValueAt(_values.get(key), _row + 1, key);
                }
            }
            this.getTable().setRowSelectionInterval(_row + 1, _row + 1);
            bo_ifProgramIsEditing = true;
            this.setValueAt(new RowNumberItemVO(NovaConstants.BILLDATAEDITSTATE_INIT, _row + 1), _row + 1,
                            "_RECORD_ROW_NUMBER");

        }
        bo_ifProgramIsEditing = false;
    }

    /**
     * 设置所有行的状态
     *
     * @param status
     */
    public void setAllRowStatusAs(String status) {
        bo_ifProgramIsEditing = true;
        for (int i = 0; i < table.getRowCount(); i++) {
            if (status.equalsIgnoreCase(NovaConstants.BILLDATAEDITSTATE_INIT)) {
                this.setValueAt(new RowNumberItemVO(NovaConstants.BILLDATAEDITSTATE_INIT, i), i, "_RECORD_ROW_NUMBER");
            } else if (status.equalsIgnoreCase(NovaConstants.BILLDATAEDITSTATE_INSERT)) {
                this.setValueAt(new RowNumberItemVO(NovaConstants.BILLDATAEDITSTATE_INSERT, i), i, "_RECORD_ROW_NUMBER");
            } else if (status.equalsIgnoreCase(NovaConstants.BILLDATAEDITSTATE_UPDATE)) {
                this.setValueAt(new RowNumberItemVO(NovaConstants.BILLDATAEDITSTATE_UPDATE, i), i, "_RECORD_ROW_NUMBER");
            }
        }
        bo_ifProgramIsEditing = false;
    }

    /**
     * 设置某一行的状态
     *
     * @param _row
     * @param status
     */
    public void setRowStatusAs(int _row, String status) {

        bo_ifProgramIsEditing = true;
        if (status.equalsIgnoreCase(NovaConstants.BILLDATAEDITSTATE_INIT)) {
            this.setValueAt(new RowNumberItemVO(NovaConstants.BILLDATAEDITSTATE_INIT, _row), _row, "_RECORD_ROW_NUMBER");
        } else if (status.equalsIgnoreCase(NovaConstants.BILLDATAEDITSTATE_INSERT)) {
            this.setValueAt(new RowNumberItemVO(NovaConstants.BILLDATAEDITSTATE_INSERT, _row), _row, "_RECORD_ROW_NUMBER");
        } else if (status.equalsIgnoreCase(NovaConstants.BILLDATAEDITSTATE_UPDATE)) {
            this.setValueAt(new RowNumberItemVO(NovaConstants.BILLDATAEDITSTATE_UPDATE, _row), _row, "_RECORD_ROW_NUMBER");
        } else if (status.equalsIgnoreCase(NovaConstants.BILLDATAEDITSTATE_DELETE)) {
            this.setValueAt(new RowNumberItemVO(NovaConstants.BILLDATAEDITSTATE_DELETE, _row), _row, "_RECORD_ROW_NUMBER");
        }
        bo_ifProgramIsEditing = false;
    }

    public TableColumnModel getColumnModel() {
        if (columnModel != null) {
            return columnModel;
        }

        columnModel = new DefaultTableColumnModel(); // 创建列模式
        for (int i = 0; i < templetItemVOs.length; i++) {
            if (templetItemVOs[i].getListisshowable().booleanValue()) { // 如果该列是列表情况显示!!!,则加入该列
                columnModel.addColumn(getTableColumns()[i]);
            }
        }
        return columnModel;
    }

    public int getRowCount() {
        return this.getTableModel().getRowCount();
    }

    private TableColumn getRowNumberColumn() {
        TableCellRenderer render = new RowNumberCellRender();        
        JTextField textField = new JTextField();
        textField.setEditable(false);
        textField.setEnabled(false);
        textField.setAlignmentX(Component.CENTER_ALIGNMENT);
        DefaultCellEditor editor = new RowNumberCellEditor(textField);        
        TableColumn rowNumberColumn = new TableColumn(0, 45, render, editor); // 创建列,对应第一列数据
        rowNumberColumn.setHeaderValue("行号"); //
        rowNumberColumn.setIdentifier(this.str_rownumberMark);
        
        return rowNumberColumn;
    }

    /**
	 * 创建所有的列
	 * 
	 * @return
	 */
	private TableColumn[] getTableColumns() {
		if (this.allTableColumns != null) {
			return allTableColumns;
		}

		allTableColumns = new TableColumn[templetItemVOs.length]; // 列的总数等于模板中的总数!
		for (int i = 0; i < allTableColumns.length; i++) {
			String str_key = templetItemVOs[i].getItemkey(); // key
			String str_type = templetItemVOs[i].getItemtype(); // 类型
			// String str_code = templetItemVOs[i].;

			int li_width = templetItemVOs[i].getListwidth().intValue(); // 宽度

			if (str_key.equals("TEMPLETCODE")) {
				templete_codecolumn_index = i;
			}
			
			
			TableCellEditor cellEditor = null;
			TableCellRenderer cellRender = null;
			if (str_type.equals("文本框")) {
				cellRender = new JLabelCellRender(templetItemVOs[i]); //
				cellEditor = new TextFieldCellEditor(new JTextField(),templetItemVOs[i]);
				allTableColumns[i] = new TableColumn(i + 1, li_width, cellRender,cellEditor); // 创建列
				HashMap param=new HashMap();
				param.put("showtip", "true");
				param.put("showbgcolor", "true");
				param.put("bgcolorexp", templetItemVOs[i].getColorformula());//暂时用colorformula代替，以后需要用bcolorformula
				allTableColumns[i].setCellRenderer(new CellTextRenderer(param));
			} else if (str_type.equals("数字框")) {
				cellRender = new JLabelCellRender(templetItemVOs[i]); //
				cellEditor = new NumberFieldCellEditor(new JFormattedTextField(), templetItemVOs[i]);
				allTableColumns[i] = new TableColumn(i + 1, li_width, cellRender,cellEditor); // 创建列
				HashMap param=new HashMap();
				param.put("showtip", "true");
				param.put("showbgcolor", "true");
				param.put("bgcolorexp", templetItemVOs[i].getColorformula());//暂时用colorformula代替，以后需要用bcolorformula
				allTableColumns[i].setCellRenderer(new CellTextRenderer(param));
			} else if (str_type.equals("密码框")) {
				cellRender = new JLabelCellRender(templetItemVOs[i]); //
				cellEditor = new PasswordTextFieldCellEditor(new JPasswordField(), templetItemVOs[i]);
				allTableColumns[i] = new TableColumn(i + 1, li_width, cellRender,cellEditor); // 创建列
				HashMap param=new HashMap();
				param.put("showtip", "false");
				param.put("showbgcolor", "true");
				param.put("bgcolorexp", templetItemVOs[i].getColorformula());//暂时用colorformula代替，以后需要用bcolorformula
				allTableColumns[i].setCellRenderer(new CellTextRenderer(param));
			} else if (str_type.equals("下拉框")) {
				cellRender = new JLabelCellRender(templetItemVOs[i]); //
				cellEditor = new ComboBoxCellEditor(new JComboBox(),templetItemVOs[i]);
				allTableColumns[i] = new TableColumn(i + 1, li_width, cellRender,cellEditor); // 创建列
				HashMap param=new HashMap();
				param.put("showtip", "true");
				param.put("showbgcolor", "true");
				param.put("bgcolorexp", templetItemVOs[i].getColorformula());//暂时用colorformula代替，以后需要用bcolorformula
				allTableColumns[i].setCellRenderer(new CellTextRenderer(param));
			} else if (str_type.equals("参照")) {
				cellRender = new JLabelCellRender(templetItemVOs[i]); //
				cellEditor = new UIRefPanelCellEditor(templetItemVOs[i]);
				allTableColumns[i] = new TableColumn(i + 1, li_width, cellRender,cellEditor); // 创建列
				HashMap param=new HashMap();
				param.put("showtip", "true");
				param.put("showbgcolor", "true");
				param.put("bgcolorexp", templetItemVOs[i].getColorformula());//暂时用colorformula代替，以后需要用bcolorformula
				allTableColumns[i].setCellRenderer(new CellTextRenderer(param));
			} else if (str_type.equals("时间")) {
				cellRender = new JLabelCellRender(templetItemVOs[i]); //
				cellEditor = new TimeSetCellEditor(templetItemVOs[i]);
				allTableColumns[i] = new TableColumn(i + 1, li_width, cellRender,cellEditor); // 创建列
				HashMap param=new HashMap();
				param.put("showtip", "true");
				param.put("showbgcolor", "true");
				param.put("bgcolorexp", templetItemVOs[i].getColorformula());//暂时用colorformula代替，以后需要用bcolorformula
				allTableColumns[i].setCellRenderer(new CellTextRenderer(param));
			} else if (str_type.equals("日历")) {
				cellRender = new JLabelCellRender(templetItemVOs[i]); //
				cellEditor = new DatePanelCellEditor(templetItemVOs[i]);
				allTableColumns[i] = new TableColumn(i + 1, li_width, cellRender,cellEditor); // 创建列
				HashMap param=new HashMap();
				param.put("showtip", "true");
				param.put("showbgcolor", "true");
				param.put("bgcolorexp", templetItemVOs[i].getColorformula());//暂时用colorformula代替，以后需要用bcolorformula
				allTableColumns[i].setCellRenderer(new CellTextRenderer(param));
			} else if (str_type.equals("文件选择框")) {
				cellRender = new JLabelCellRender(templetItemVOs[i]); //
				cellEditor = new FilePathPanelCellEditor(templetItemVOs[i]);
				allTableColumns[i] = new TableColumn(i + 1, li_width, cellRender,cellEditor); // 创建列
				HashMap param=new HashMap();
				param.put("showtip", "true");
				param.put("showbgcolor", "true");
				param.put("bgcolorexp", templetItemVOs[i].getColorformula());//暂时用colorformula代替，以后需要用bcolorformula
				allTableColumns[i].setCellRenderer(new CellTextRenderer(param));
			} else if (str_type.equals("颜色")) {
				cellRender = new JLabelCellRender(templetItemVOs[i]); //
				cellEditor = new ColorPanelCellEditor(templetItemVOs[i]);
				allTableColumns[i] = new TableColumn(i + 1, li_width, cellRender,cellEditor); // 创建列
				HashMap param=new HashMap();
				param.put("showtip", "true");
				param.put("showbgcolor", "true");
				param.put("bgcolorexp", templetItemVOs[i].getColorformula());//暂时用colorformula代替，以后需要用bcolorformula
				allTableColumns[i].setCellRenderer(new CellTextRenderer(param));
			} else if (str_type.equals("大文本框")) {
				cellRender = new JLabelCellRender(templetItemVOs[i]); //
				cellEditor = new TextAreaPanelCellEditor(templetItemVOs[i]);
				allTableColumns[i] = new TableColumn(i + 1, li_width, cellRender,cellEditor); // 创建列
				HashMap param=new HashMap();
				param.put("showtip", "true");
				param.put("showbgcolor", "true");
				param.put("bgcolorexp", templetItemVOs[i].getColorformula());//暂时用colorformula代替，以后需要用bcolorformula
				allTableColumns[i].setCellRenderer(new CellTextRenderer(param));
			} else if (str_type.equals("勾选框")) {
				cellRender = new CheckBoxCellRender(); //
				cellEditor = new CheckBoxCellEditor(new JCheckBox(),templetItemVOs[i]); //
				allTableColumns[i] = new TableColumn(i + 1, li_width, cellRender, cellEditor); // 创建列
				HashMap param=new HashMap();
				param.put("showtip", "false");
				param.put("showbgcolor", "true");
				param.put("bgcolorexp", templetItemVOs[i].getColorformula());//暂时用colorformula代替，以后需要用bcolorformula
				allTableColumns[i].setCellRenderer(new CellCheckboxRenderer(param));
			} else if (str_type.equals("图片选择框")) {
				cellRender = new ImageCellRender(templetItemVOs[i]); //
				cellEditor = new ImagePanelCellEditor(templetItemVOs[i]); //
				allTableColumns[i] = new TableColumn(i + 1, li_width, cellRender,cellEditor); // 创建列
				HashMap param=new HashMap();
				param.put("showtip", "false");
				param.put("showbgcolor", "true");
				param.put("bgcolorexp", templetItemVOs[i].getColorformula());//暂时用colorformula代替，以后需要用bcolorformula
				allTableColumns[i].setCellRenderer(new CellTextRenderer(param));
			} else {
				cellRender = null; //
				cellEditor = null;
				allTableColumns[i] = new TableColumn(i + 1, li_width, cellRender,cellEditor); // 创建列
				HashMap param=new HashMap();
				param.put("showtip", "false");
				param.put("showbgcolor", "true");
				param.put("bgcolorexp", templetItemVOs[i].getColorformula());//暂时用colorformula代替，以后需要用bcolorformula
				allTableColumns[i].setCellRenderer(new CellTextRenderer(param));
			}

			//
			allTableColumns[i].setHeaderValue(templetItemVOs[i].getItemname()); // 列的标题
			allTableColumns[i].setIdentifier(templetItemVOs[i].getItemkey()); // 唯一性标识
			
			
			
		}
		return allTableColumns;
	}


    /**
     * 创建数据Model,数据Model也是与模板一一对应的!!!!
     *
     * @return
     */
    public BillListModel getTableModel() {
        if (tableModel != null) {
            return tableModel;
        }

        int li_columncount = templetItemVOs.length;

        Object[][] data = new Object[0][li_columncount + 1]; //
        String[] columns = templetVO.getItemKeys(); // //
        String[] new_columns = new String[columns.length + 1];
        new_columns[0] = str_rownumberMark; //
        for (int i = 0; i < columns.length; i++) {
            new_columns[i + 1] = columns[i];
        }

        tableModel = new BillListModel(data, new_columns, this.templetVO); // 创建数据Model
        tableModel.addTableModelListener(new TableModelListener() {

            public void tableChanged(TableModelEvent e) {
                int lastRow = e.getLastRow(); //
                int mColIndex = e.getColumn(); //

                if(e.getType()== TableModelEvent.UPDATE) {
                    if (!bo_ifProgramIsEditing) {
                        onChanged(lastRow, mColIndex);
                    }
                }
            }
        });

        return tableModel; //
    }

    /**
     * 表格数据发生变化
     *
     * @param _row
     * @param _col
     */
    private synchronized void onChanged(int _row, int _col) {
        if (_col == 0) { 
            return;
        }

        Object obj = getValueAt(_row, _col);
        if (obj instanceof RefItemVO) {
            RefItemVO refVO = (RefItemVO) obj;
//            if (!refVO.getIfValueChanged()) {
//                return;
//            }

        }

        bo_ifProgramIsEditing = true; // 设置程序真在编辑
        setRowNumberState(_row, _col); // 设置行号状态为处理修改状态

        execEditFormula(_row, _col); // 执行加载公式
        String str_itemkey = findModelItemKey(_col); // 变化的Key
        for (int i = 0; i < v_listeners.size(); i++) {
            NovaEventListener listener = (NovaEventListener) v_listeners.get(i);
            listener.onValueChanged(new NovaEvent(NovaEvent.ListChanged, str_itemkey, obj, _row, this)); //
        }

        bo_ifProgramIsEditing = false; // 程序真在编辑结束!!
    }

    /**
     * 注册事件
     *
     * @param _listener
     */
    public void addNovaEventListener(NovaEventListener _listener) {
        v_listeners.add(_listener); //
    }

    protected synchronized void setRowNumberState(int _row, int _col) {
        RowNumberItemVO valueVO = (RowNumberItemVO) getValueAt(_row, str_rownumberMark);
        if (valueVO == null) {
            valueVO = new RowNumberItemVO(NovaConstants.BILLDATAEDITSTATE_INIT, _row);
        }
        String str_oldstate = valueVO.getState(); //
        if (str_oldstate != null && (str_oldstate.equals("INSERT") || str_oldstate.equals("UPDATE"))) {
        } else {
        	//判断当前列的itemvo对象的类型是否保存列
			if(templetItemVOs[_col-1].isNeedSave()){
	            valueVO.setState(NovaConstants.BILLDATAEDITSTATE_UPDATE);
	            setValueAt(valueVO, _row, str_rownumberMark);
			}
        }
    }

    public void execViewFormula() {

    }

    /**
     * 执行编辑公式..
     *
     * @param _row
     */
    public void execEditFormula(int _row, int _col) {
        if (_col != 0) {
            // String str_itemkey = findModelItemKey(_col); //
            // System.out.println("[" + _row + "][" + _col + "]发生变化!!");
            String str_editFormuladesc = this.getTempletItemVOs()[_col - 1].getEditformula();
            if (str_editFormuladesc != null && !str_editFormuladesc.trim().equals("")) {
                HashMap map_rowcache = this.getValueAtRowWithHashMap(_row); // 将当前数据取得置入一个HashMap
                JepFormulaParse jepParse = new JepFormulaParse(JepFormulaParse.li_ui);
                // System.out.println("触发[" + str_itemkey + "]的编辑公式["
                // + str_editFormuladesc + "]"); //
                String[] str_editorFormulas = str_editFormuladesc.trim().split(";"); //
                for (int i = 0; i < str_editorFormulas.length; i++) { // 遍历执行所有公式
                    String[] str_execResult = dealEditFormula(jepParse, str_editorFormulas[i],
                        NovaClientEnvironment.getInstance(), map_rowcache);
                    //如果没有结果，则继续执行下一个编辑公式
                    if(str_execResult[1]==null)
                    	continue;
                    int li_pos = findItemPos(this.templetItemVOs, str_execResult[0]); //
                    Object obj_value = null;
                    if (li_pos >= 0) { // 看公式前辍是不是在模板中定义的,如果是
                        String str_type = findItemType(this.templetItemVOs, str_execResult[0]); // 看是什么类型
                        if (str_type.equals("文本框")) { // 如果是文本框,则直接赋值,绝大多数情况是这样!!!
                            obj_value = str_execResult[1];
                        } else if (str_type.equals("下拉框")) {
                            obj_value = new ComBoxItemVO(str_execResult[1], str_execResult[1], str_execResult[1]);
                        } else if (str_type.equals("参照")) {
                            obj_value = new RefItemVO(str_execResult[1], str_execResult[1], str_execResult[1]);
                        } else {
                            obj_value = str_execResult[1];
                        }
                        map_rowcache.put(str_execResult[0], obj_value); // 往缓存中加入
                        this.setValueAt(obj_value, _row, str_execResult[0]); // 设置界面
                    }
                } // 遍历公式结束!!!
            }
        }
    }

    private int findItemPos(Pub_Templet_1_ItemVO[] itemVos, String _itemkey) {
        for (int i = 0; i < itemVos.length; i++) {
            if (itemVos[i].getItemkey().equalsIgnoreCase(_itemkey)) {
                return i;
            }
        }
        return -1;
    }

    private String findItemType(Pub_Templet_1_ItemVO[] itemVos, String _itemkey) {
        for (int i = 0; i < itemVos.length; i++) {
            if (itemVos[i].getItemkey().equalsIgnoreCase(_itemkey)) {
                return itemVos[i].getItemtype();
            }
        }
        return null;
    }

    private VectorMap getDefaultValue() { // 目前直接的字符串或者{Date(10)},{Date(19)}
        VectorMap result = new VectorMap();
        for (int i = 0; i < templetItemVOs.length; i++) {
            Pub_Templet_1_ItemVO tempitem = templetItemVOs[i];
            String formula = tempitem.getDefaultvalueformula();
            if (formula != null && !formula.equals("")) {
                formula = formula.trim(); //
                formula = UIUtil.replaceAll(formula, " ", "");
                ModifySqlUtil util = new ModifySqlUtil(); //
                try {
                    Vector v_itemkey = util.findItemKey(formula);
                    for (int j = 0; j < v_itemkey.size(); j++) {
                        formula = UIUtil.replaceAll(formula, "{" + v_itemkey.get(j) + "}",
                            "" + NovaClientEnvironment.getInstance().get(v_itemkey.get(j)));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                System.out.println("执行默认值公式:[" + formula + "]");
                String str_value = getJepFormulaValue(formula);

                result.put(tempitem.getItemkey(), str_value); //
            }
        }
        return result;
    }

    /**
     * 真正执行某一个编辑公式..使用JEP去执行!!
     *
     * @param _formula
     */
    private String[] dealEditFormula(JepFormulaParse _jepParse, String _formula, NovaClientEnvironment _env,
                                     HashMap _map) {
        String str_formula = UIUtil.replaceAll(_formula, " ", "");
        int li_pos = str_formula.indexOf("=>");
        String str_prefix = str_formula.substring(0, li_pos);
        String str_subfix = str_formula.substring(li_pos + 2, str_formula.length());

        String str_subfix_new = str_subfix;
        if(str_subfix.equalsIgnoreCase("refresh()"))
        {
        	Pub_Templet_1_ItemVO itemvo = this.getTempletItemVO(str_prefix);
        	String type = itemvo.getItemtype();
        	if(type.equals("下拉框"))
        	{
        		try {
        			ComBoxItemVO[] comboxs= ((FrameWorkMetaDataService)NovaRemoteServiceFactory.getInstance().lookUpService(FrameWorkMetaDataService.class)).resetComBoxItemvo(itemvo, NovaClientEnvironment.getInstance());
        			itemvo.setComBoxItemVos(comboxs);
        			this.setValueAt(new ComBoxItemVO("", "", ""), this.getSelectedRow(), str_prefix);
        		} catch (NovaRemoteException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
        	}
        	return new String[] {str_prefix, null}; //
        }
        if(str_subfix.equalsIgnoreCase("reset()"))
        {
        	Pub_Templet_1_ItemVO itemvo = this.getTempletItemVO(str_prefix);
        	String type = itemvo.getItemtype();
        	if(type.equals("下拉框"))
        	{
        			this.setValueAt(new ComBoxItemVO("", "", ""), this.getSelectedRow(), str_prefix);
        	}
        	else
        		this.setValueAt("", this.getSelectedRow(), str_prefix);
        	return new String[] {str_prefix, null}; //
        }
        try {
            str_subfix_new = new ModifySqlUtil().convertMacroStr(str_subfix, _env, _map); // 新的公式,即过转换过后的公式
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Object obj = null;

        try {
            obj = _jepParse.execFormula(str_subfix_new); // 使用JEP执行公式!!!!
            String str_return = (obj == null ? "" : "" + obj);
            System.out.println("成功执行编辑公式项:[" + str_formula + "],前辍:[" + str_prefix + "],后辍:[" + str_subfix +
                               "],宏代码转换后的后辍:[" + str_subfix_new + "],JEP运算结果[" + str_return + "]"); //
            return new String[] {str_prefix, str_return}; //
        } catch (Exception ex) {
            System.out.println("失败执行编辑公式项:[" + str_formula + "],前辍:[" + str_prefix + "],后辍:[" + str_subfix +
                               "],宏代码转换后的后辍:[" + str_subfix_new + "]"); //
            ex.printStackTrace(); //
            return null;
        }
    }

    private String getJepFormulaValue(String _exp) {
        JepFormulaParse jepParse = new JepFormulaParse(JepFormulaParse.li_ui); //
        String str_return = "" + jepParse.execFormula(_exp);
        return str_return;
    }

    public void setSelectedRowCode(String _code) {
        int li_rowcount = table.getModel().getRowCount();
        String temp_str;
        for (int i = 0; i < li_rowcount; i++) {
            temp_str = (String)this.table.getValueAt(i, this.templete_codecolumn_index);
            if (temp_str.equals(_code)) {
                setSelectedRow(i);
                return;
            }
        }
    }

    private void setSelectedRow(int _row) {

        if (_row >= 0) {
            Rectangle rect = table.getCellRect(_row, 0, true);

            table.scrollRectToVisible(rect);
            table.setRowSelectionInterval(_row, _row);
        }
    }

    /**
     * 得到数据源名称
     * @return
     */
    public String getDataSourceName() {
        return getTableModel().getDataSourceName(); //
    }

    /**
     * 得到数据过滤条件
     * @return
     */
    public String getDataconstraint() {
		if (templetVO.getDataconstraint() == null
				|| templetVO.getDataconstraint().trim().equals("null")
				|| templetVO.getDataconstraint().trim().equals("")) {
			return null; // 默认数据源
		} else {
			return new FrameWorkTBUtil().convertExpression(NovaClientEnvironment.getInstance(), templetVO.getDataconstraint()); // 算出数据源!!
			
		}
	}

    //TODO 根据条件取数 对于检索也有影响
    public void QueryDataByCondition(final String _condition) {
        QueryData(getSQL(_condition));
    }
    
    //TODO 根据条件取数 对于检索也有影响
    public void QueryDataByConditionNoSplash(final String _condition) {
        this.QueryDataNoSplash(getSQL(_condition));
    }

    /**
     * 根据条件子句拼出完整查询Sql
     * @param _condition
     * @return
     */
    public String getSQL(String _condition) {
    	if(_condition!=null && _condition != ""){
    		_condition = _condition.trim();
    		
    		//以下是判断是否具有条件和排序子句（两个&之间），然后把条件重整了一下
    		//TODO 算法比较麻烦，应该重构去掉，传入的参数应该分别控制条件和排序
        	int pos1 = _condition.indexOf("&");
        	if(pos1 != -1){	
        		String condition_split = _condition.substring(pos1+1);
        		int pos2 = condition_split.indexOf("&");//if pos2-1>pos1+1
        		pos2 = pos1 + 1 + pos2;
        		String order = _condition.substring(pos1+1,pos2);
        		String condition_split_2 = _condition.substring(pos2+1); 
        		int pos_and = condition_split_2.indexOf("and");
        		pos_and = pos2 + 1 + pos_and;

        		if(_condition.startsWith("&")){
        			String condition_head =_condition.substring(pos_and+3);
        			_condition = condition_head + " " + order;
        		}else{
        			String condition_head = _condition.substring(0, pos1);
            		String condition_tail = _condition.substring(pos_and-1);
            		_condition = condition_head + condition_tail + " " + order;
        		}
        	}
    	}
  
        String str_constraintFilterCondition = getDataconstraint();
        String str_return = null; //
        if (str_constraintFilterCondition == null) {
            if (_condition == null) {
                str_return = "select * from " + templetVO.getTablename();
            } else {
                str_return = "select * from " + templetVO.getTablename() + " where " + _condition; // 把RowID加上!!
            }
        } else {
            if (_condition == null) {
                str_return = "select * from " + templetVO.getTablename() + " where (" + str_constraintFilterCondition + ")";
            } else {
                str_return = "select * from " + templetVO.getTablename() + " where (" + str_constraintFilterCondition + ") and (" + _condition + ")"; // 把RowID加上!!
            }
        }

        return str_return;
    }

    /**
     * 根据真正的SQL取数【默认不显示等待窗口】
     * 
     * @param _sql
     */
    public void QueryDataNoSplash(final String _sql) {
    	QueryDataByDS(getDataSourceName(), _sql); 
    }
    
    /**
     * 根据真正的SQL取数【显示等待窗口】
     * 
     * @param _sql
     */
    public void QueryData(final String _sql) {
	    new NovaSplashWindow(this, Sys.getSysRes("query.show.message"),new AbstractAction() {
	        private static final long serialVersionUID = 3194520704829358152L;
			public void actionPerformed(ActionEvent e) {
				QueryDataByDS(getDataSourceName(), _sql);        
			}
		});
    }
    
    /**
     * 获得所有数据
     * @param _ds
     * @param _sql
     * @param sRow
     * @param eRow
     * @return
     */
    public Object[][] fetchData(String _ds,String _sql,int sRow,int eRow) {    
    	try{
    		if(sRow<0||eRow<0){
    			return UIUtil.getBillListDataByDS(_ds, _sql, templetVO,NovaClientEnvironment.getInstance());
    		}else{    		
    	        return UIUtil.getBillListSubDataByDS(_ds, _sql, sRow, eRow, templetVO,NovaClientEnvironment.getInstance());
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    		return null;
    	}
    }
        

    

    public void getQueryDataBySql(String _sql) {
        getTable().updateUI();
    }
    
    

    /**
     * 调用查询刷新数据
     */
    public void refreshData() {
        if (str_realsql != null) {
            QueryData(str_realsql); //
        }
    }

    
    /**
     * 用外来给定数据刷新显示，不执行检索动作
     * @param _data
     */
    public void refreshData(Object[][] _data) {
    	if(_data==null||_data.length==0){
    		return;
    	}
        this.all_realValueData = _data;
        this._dataRows=_data.length;
        
        this._dataFrom="Setting";//指明数据不是检索获得
        
        setTableValue();
    }

    //设置表格数据，更新展现
    private void setTableValue() {
        if (bo_isShowPageNavigation) {
            goToPage(1);
        } else {
            curr_realValueData = all_realValueData; //
            putValue(all_realValueData);        
        }
    }

    public void setPageScrollable(boolean _enable) {
        bo_isShowPageNavigation = _enable;
        setTableValue();
    }

    public boolean getPageScrollable() {
        return bo_isShowPageNavigation;
    }

    public void refreshCurrData() {
        if (curr_realValueData != null) {
            putValue(curr_realValueData); //
        }
    }

    public void putValue(Object[][] _objs) {
        clearTable();
        for (int i = 0; i < _objs.length; i++) {
            addRow(_objs[i]);
        }
    }

    public Object[][] getAllValue() {
        return this.all_realValueData;
    }

    /**
     * 当前页的内容
     * @return
     */
    public Object[][] currPageValue() {
        return this.curr_realValueData;
    }

    
    
    //首页
    private void onGoToFirstPage() {
        goToPage(1);
    }

    //前翻一页
    private void onGoToPreviousPage() {
        int li_newpage = li_currpage - 1;
        goToPage(li_newpage);
    }

    //后翻一页
    private void onGoToNextPage() {
        int li_newpage = li_currpage + 1;
        goToPage(li_newpage);
    }

    //末页
    private void onGoToLastPage() {
        goToPage(getMaxPages());
    }

    //到某页
    private void onGoToPage() {
        int page = 0;
        try {
            page = Integer.parseInt(goToPageTextField.getText()); //
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "跳转页数必须是数字");
            return;
        }

        goToPage(page);
    }

    //更新页提示信息
    protected synchronized void refreshPageDesc() {    	
    	
    	label_pagedesc.setText("   共[" + this._dataRows + "]记录,[" + getMaxPages() + "]页,每页[" +
                li_onepagerecords + "]条,当前第[" + this.li_currpage + "]页");
    }

    /**
     * 获得当前页号
     * @return
     */
    public int getCurPage(){
    	return this.li_currpage;
    }
    
    /**
     * 跳至指定页
     * @param _lipage
     */
    public void goToPage(int _lipage) {
    	table.removeEditor();
    	
    	//如果大于页面总数，则取最后一页
    	if(this._dataRows>0){
	        if(_lipage > getMaxPages()) {
	        	_lipage=getMaxPages();            
	        }
    	}
        if (_lipage < 1) {//没有检索到数据        	
    		_lipage=1;
        }
    	
    	Object[][] objes = getOnePageData(_lipage);
        if (objes != null) {
            putValue(objes); // 直接的向页面置入数据!!!
            this.curr_realValueData = objes;
            this.li_currpage = _lipage;
            li_currsorttype = 0;
        }
        
        if (bo_isShowPageNavigation) {    		
    		goToPageTextField.setText("" + _lipage);
    		refreshPageDesc(); //刷新页信息
    	}
    	getTable().getTableHeader().updateUI();
    }

    /**
     * 获得页数
     * @return
     */
    public int getMaxPages() {
        if(this._dataRows==0){
        	return 0;
        }
        int li_1 = this._dataRows / li_onepagerecords;
        int li_2 = this._dataRows % li_onepagerecords;
        if (li_2 == 0) {
            return li_1;
        } else {
            return li_1 + 1;
        }
    }

    /**
     * 取得某一页数据
     * 解决翻页控制问题就在这里了（远程获取单独一页数据），原先这里仅仅是读取已经缓冲的数据，新的处理是在此远程读取一页数据。
     * 特殊控制：
     *    如果正常检索，则属性dataFrom=Fetch，翻页执行正常翻页（1000行以内和1000行以外）
     *    如果数据由refreshData(Object[][] data)接口获得，则属性dataFrom=Setting，翻页只在客户端处理
     *    
     * @param _lipage
     * @return
     */
    public Object[][] getOnePageData(int _lipage) {
    	//起始行号
        int li_begin_pos = li_onepagerecords * (_lipage - 1);
        //截止行号
        int li_end_pos = li_begin_pos + li_onepagerecords - 1;
        
    	//如果数据不是检索得来的，那么取下一页就不用执行检索，直接从分页中获取
    	if(!this._dataFrom.equalsIgnoreCase("Fetch")){
    		Object[][] rt=(li_end_pos>=all_realValueData.length)
    		              ?new Object[all_realValueData.length-li_begin_pos][all_realValueData[0].length]
    		              :new Object[li_onepagerecords][all_realValueData[0].length];
    		for(int i=li_begin_pos,j=0;i<=li_end_pos&&i<all_realValueData.length;i++,j++){
    			rt[j]=all_realValueData[i];
    		}
    		return rt;
    	}else{     
    		return fetchData(this.getDataSourceName(),this.str_realsql,li_begin_pos,li_end_pos);                
    	}
        
    }

    /**
     * 取得参照说明中的SQL语句!!!
     *
     * @param _allrefdesc
     * @return
     */
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
            _allrefdesc = _allrefdesc.trim(); // 截去空格
            String str_remain = _allrefdesc.substring(li_pos + 1, _allrefdesc.length()); //
            int li_pos_tree_1 = str_remain.indexOf(";"); //
            str_sql = str_remain.substring(0, li_pos_tree_1); // SQL语句
        } else if (str_type.equalsIgnoreCase("CUST")) {
        }
        return str_sql;
    }

    private int findModelIndex(String _key) {
        if (_key.equalsIgnoreCase(str_rownumberMark)) {
            return 0;
        }

        for (int i = 0; i < templetItemVOs.length; i++) {
            if (templetItemVOs[i].getItemkey().equalsIgnoreCase(_key)) {
                return i + 1;
            }
        }
        return -1;
    }

    private String findModelItemKey(int _pos) {
        return templetItemVOs[_pos - 1].getItemkey();
    }
    
    
    /**
     * 清空表格控件
     */
    public void clearTable() {
        this.removeAllRows();//20080423 James.W Fix
        //清空保持下来的删除记录缓冲
        v_deleted_row.removeAllElements();
        //更新界面
        getTable().updateUI();
    }

    /**
     * 新增的SQL!
     *
     * @return
     */
    public String getInsertSQL(int _row) {
        return this.getBillVO(_row).getInsertSQL();
    }

    /**
     * 删除的SQL!
     *
     * @return
     */
    public String getDeleteSQL(int _row) {
        return this.getBillVO(_row).getDeleteSQL();
    }

    /**
     * 得到某一条记录的update SQL
     *
     * @param _row
     * @return
     */
    public String getUpdateSQL(int _row) {
        return this.getBillVO(_row).getUpdateSQL();
    }

    /**
     * 得到所有的updateSQL
     *
     * @return
     */
    public String[] getUpdateSQLs() {
        int li_count = getTable().getRowCount();
        String[] str_sqls = new String[li_count];
        for (int i = 0; i < li_count; i++) {
            str_sqls[i] = getUpdateSQL(i);
        }
        return str_sqls;
    }

    public String[] getInsertSQLs() {
        int li_count = getTable().getRowCount();
        String[] str_sqls = new String[li_count];
        for (int i = 0; i < li_count; i++) {
            str_sqls[i] = getInsertSQL(i);
        }
        return str_sqls;
    }

    public String[] getUpdateSQLWithStatus() {
        int li_count = getTable().getRowCount();
        ArrayList sql = new ArrayList();
        for (int i = 0; i < li_count; i++) {
            if (this.getValueAt(i, "_RECORD_ROW_NUMBER").equals("UPDATE")) {
                sql.add(getUpdateSQL(i));
            }
        }
        return (String[]) sql.toArray(new String[0]);
    }

    public String[] getInsertSQLWithStatus() {
        int li_count = getTable().getRowCount();
        ArrayList sql = new ArrayList();
        for (int i = 0; i < li_count; i++) {
            if (this.getValueAt(i, "_RECORD_ROW_NUMBER").equals("INSERT")) {
                sql.add(getInsertSQL(i));
            }
        }
        return (String[]) sql.toArray(new String[0]);
    }

    public ArrayList getRowNumsWithStatus(String status) {
        int li_count = getTable().getRowCount();
        ArrayList temp = new ArrayList();
        for (int i = 0; i < li_count; i++) {
            if (this.getValueAt(i, "_RECORD_ROW_NUMBER").equals(status)) {
                temp.add(new Integer(i));
            }
        }
        return temp;
    }

    public RowNumberItemVO getRowNumberVO(int _row) {
        return (RowNumberItemVO) getValueAt(_row, this.str_rownumberMark); //
    }

    public String getRowNumberEditState(int _row) {
        return ( (RowNumberItemVO) getValueAt(_row, this.str_rownumberMark)).getState(); //
    }

    public String getEditState(int _row) {
        return getRowNumberEditState(_row); //
    }

    public int getSelectedRow() {
        return getTable().getSelectedRow();
    }

    public void removeRows(int[] _rowindex) {
        if (_rowindex == null || _rowindex.length == 0) {
            return;
        }
        DefaultTableModel model = (DefaultTableModel) getTable().getModel();
        Arrays.sort(_rowindex);

        for (int i = _rowindex.length - 1; i >= 0; i--) {
            this.setRowStatusAs(_rowindex[i], NovaConstants.BILLDATAEDITSTATE_DELETE);
            BillVO billVO = getBillVO(_rowindex[i]); //
            v_deleted_row.add(billVO); //
            model.removeRow(_rowindex[i]);
        }
    }

    //自定义列显示控制
    private void reShowHideTableColumn() {
        String[] str_filterkeys = (String[]) v_lockedcolumns.toArray(new String[0]);
        ShowHideSortTableColumnDialog showHideTableColumnDialog = new ShowHideSortTableColumnDialog(
        		this, "列显示排序", 600, 400, this.templetItemVOs, this.str_templetcode, str_filterkeys);

        showHideTableColumnDialog.setVisible(true); //
        int li_returntype = showHideTableColumnDialog.getReturn_type();
        if (li_returntype == 0) {
            Vector v = showHideTableColumnDialog.getResult();
            clearAllColumn(); // 清空所有列

            JListItemVO[] vos = (JListItemVO[]) v.toArray(new JListItemVO[0]); //
            for (int i = 0; i < vos.length; i++) {
                int li_modelindex = findModelIndex(vos[i].getId()) - 1;
                templetItemVOs[li_modelindex].setListisshowable(Boolean.TRUE);
                insertColumn(i, li_modelindex);
            }
        }
    }

    private void clearAllColumn() {
        int li_columncount = getColumnModel().getColumnCount();
        for (int i = 0; i < li_columncount; i++) {
            getColumnModel().removeColumn(getColumnModel().getColumn(0));
        }

        for (int i = 0; i < templetItemVOs.length; i++) {
            templetItemVOs[i].setListisshowable(Boolean.FALSE);
        }
    }

    private void reSortRow() {
        String[] str_filterkeys = (String[]) v_lockedcolumns.toArray(new String[0]);
        ShowSortAndGroupColumnDialog ShowSortAndGroupColumnDialog = new ShowSortAndGroupColumnDialog(this, "行排序和分组",
            600, 400, templetItemVOs, str_filterkeys);

        ShowSortAndGroupColumnDialog.setVisible(true); //
        int li_returntype = ShowSortAndGroupColumnDialog.getReturn_type();
        if (li_returntype == 0) {
            //Vector v_sort = ShowSortAndGroupColumnDialog.getSortResult();
            VectorMap ht_sort = ShowSortAndGroupColumnDialog.getSortBySqenceResult();
            Vector v_group = ShowSortAndGroupColumnDialog.getGroupResult();

            // DefaultTableModel temp_model = this.tableModel;
            // DefaultTableColumnModel temp_columnmodel = this.columnModel;
            // JTable temp_table= new JTable(temp_model,temp_columnmodel);
            SortAndGroupDialog SortAndGroupDialog = new SortAndGroupDialog(this, "行排序和分组", 700, 500, curr_realValueData,
                templetVO, ht_sort, v_group);
            SortAndGroupDialog.setVisible(true);
        }
    }

    private void modifyTemplet(String templetname) {
        if (abstractTempletVO != null) {
            JOptionPane.showMessageDialog(this,
                                          "直接由smartx.system.login.vo.AbstractTempletVO创建,实现类名是:[" +
                                          abstractTempletVO.getClass().getName() + "]"); //
            return;
        }
        new TempletModify(templetname);
    }

    private void modifyTemplet2(String templetname) {
        if (abstractTempletVO != null) {
            JOptionPane.showMessageDialog(this,
                                          "直接由smartx.system.login.vo.AbstractTempletVO创建,实现类名是:[" +
                                          abstractTempletVO.getClass().getName() + "]"); //
            return;
        }
        new TempletModify2(this, templetname);
    }

    private int getClickedColumnPos() {
        Point point = new Point(li_mouse_x, li_mouse_y);
        int li_pos = getTable().getTableHeader().columnAtPoint(point);
        return li_pos;
    }

    private TableColumn getClickedTableColumn() {
        int li_pos = getClickedColumnPos();
        return getColumnModel().getColumn(li_pos);
    }

    private void lockColumn() {
        lockColumn(getClickedColumnPos());
    }

    public void setUnEditable() {
        setAllItemValue("listiseditable", NovaConstants.BILLCOMPENTEDITABLE_NONE);
    }

    //锁定列
    private void lockColumn(int _pos) {
        String[] str_keys = new String[_pos + 1];
        int li_newwidth = 0;
        for (int i = 0; i <= _pos; i++) {
            TableColumn tableColumn = getColumnModel().getColumn(i);
            li_newwidth = li_newwidth + tableColumn.getWidth();
            str_keys[i] = (String) tableColumn.getIdentifier();
            v_lockedcolumns.add(str_keys[i]);
            int li_modexindex = findModelIndex(str_keys[i]);
            rowHeaderColumnModel.addColumn(getTableColumns()[li_modexindex - 1]);
        }
        ListSelectionModel lsm_table = rowHeaderTable.getSelectionModel();

        rowHeaderTable = new JTable(getTableModel(), rowHeaderColumnModel); // 创建新的表..

        // newtable.createDefaultColumnsFromModel(); //
        rowHeaderTable.setRowHeight(20);
        rowHeaderTable.setRowSelectionAllowed(true);
        rowHeaderTable.setColumnSelectionAllowed(false);
        // newtable.setCellSelectionEnabled(false); //
        rowHeaderTable.setBackground(new Color(240, 240, 240));
        rowHeaderTable.getTableHeader().setReorderingAllowed(false);
        rowHeaderTable.getTableHeader().setResizingAllowed(false);
        rowHeaderTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        rowHeaderTable.setSelectionModel(lsm_table);

        rowHeaderTable.getTableHeader().addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent evt) {
                if (evt.getButton() == MouseEvent.BUTTON3) {
                    Point point = new Point(evt.getX(), evt.getY());
                    int li_pos = rowHeaderTable.getTableHeader().columnAtPoint(point);
                    if (li_pos != 0) {
                        showPopMenu2(evt.getComponent(), evt.getX(), evt.getY()); // 弹出菜单
                    }
                }
            }

            public void mouseReleased(MouseEvent e) {
            }
        });

        if (getTable().getSelectedRow() >= 0) {
            rowHeaderTable.setRowSelectionInterval(getTable().getSelectedRow(), getTable().getSelectedRow());
        }

        rowHeaderTable.setSelectionModel(getTable().getSelectionModel()); // 解决解锁后事件消失的问题!!

        JViewport jv = new JViewport();
        jv.setView(rowHeaderTable);
        int li_height = new Double(rowHeaderTable.getMaximumSize().getHeight()).intValue();
        jv.setPreferredSize(new Dimension(li_newwidth + 45, li_height));

        scrollPanel_main.setRowHeader(jv);
        scrollPanel_main.setCorner(JScrollPane.UPPER_LEFT_CORNER, rowHeaderTable.getTableHeader());
        scrollPanel_main.updateUI(); //

        for (int i = 0; i <= _pos; i++) {
            this.deleteColumn(str_keys[i]);
        }

        bo_tableislockcolumn = true;
    }

    private void unlockColumn() {
        int li_count = rowHeaderTable.getColumnModel().getColumnCount();
        for (int i = li_count - 1; i >= 1; i--) {
            TableColumn column = rowHeaderTable.getColumnModel().getColumn(i); //
            String str_key = (String) column.getIdentifier();
            this.insertColumn(0, str_key);
        }

        for (int i = li_count - 1; i >= 1; i--) {
            TableColumn column = rowHeaderTable.getColumnModel().getColumn(i); //
            rowHeaderColumnModel.removeColumn(column);
        }

        int li_height = new Double(rowHeaderTable.getMaximumSize().getHeight()).intValue();
        scrollPanel_main.getRowHeader().setPreferredSize(new Dimension(45, li_height));
        // scrollPanel_main.setCorner(JScrollPane.UPPER_LEFT_CORNER, null);

        scrollPanel_main.updateUI(); //
        v_lockedcolumns.removeAllElements(); // 清空数组
        bo_tableislockcolumn = false;
    }

    private void showPieChart() {
        int li_clickedColumnpos = getClickedColumnPos();
        TableColumn column = getClickedTableColumn();
        //String str_key = (String) column.getIdentifier();
        String str_name = (String) column.getHeaderValue();

        ShowColumnDataAsPieChartDialog dialog = new ShowColumnDataAsPieChartDialog(this, str_name,
            getColumnValues(li_clickedColumnpos));
        dialog.setVisible(true);
    }
    
    
    
    /**
     * 对指定的字段进行全结果集的正排序
     */
	protected void sortShowAllAsc() {
		if (this.str_realsql != null && this.str_realsql.toLowerCase().lastIndexOf("order")==-1) {
			String sql = this.str_realsql + " order by " + getClickedTableColumn().getIdentifier();
			QueryData(sql);
			refreshData();
		}else{
			String sql=this.str_realsql.substring(0,this.str_realsql.toLowerCase().lastIndexOf("order")).toString() + " order by "
			+ getClickedTableColumn().getIdentifier() + " asc ";
			QueryData(sql);
			refreshData();
		}
		li_currsorttype=1;
		str_currsortcolumnkey=(String)getClickedTableColumn().getIdentifier();
		
	}
	/**
	 * 对指定的字段进行全结果集的反排序
	 */
	protected void sortShowAllDesc() {
		if (this.str_realsql != null && this.str_realsql.toLowerCase().lastIndexOf("order")==-1) {
			String sql = this.str_realsql +" order by "	+ getClickedTableColumn().getIdentifier() + " desc ";
			QueryData(sql);
			refreshData();
		}else{
			String sql=this.str_realsql.substring(0,this.str_realsql.toLowerCase().lastIndexOf("order")).toString() + " order by "
			+ getClickedTableColumn().getIdentifier() + " desc ";
			QueryData(sql);
			refreshData();
		}
		li_currsorttype=-1;
		str_currsortcolumnkey=(String)getClickedTableColumn().getIdentifier();
	}
	
    

    public String[] getColumnValues(int _index) {
        int li_rowcount = getTable().getRowCount();
        String[] str_data = new String[li_rowcount];
        for (int i = 0; i < li_rowcount; i++) {
            str_data[i] = String.valueOf(getValueAtTable(i, _index));
        }
        return str_data;
    }

    /**
     * 在某一位置插入一列,
     *
     * @param _columnindex
     *            想要插入的表中列的位置
     * @param _modelindex
     *            插入数据的哪一列
     */
    public void insertColumn(int _columnindex, int _modelindex) {
        TableColumn column = getTableColumns()[_modelindex]; // 找到那一列
        this.getColumnModel().addColumn(column);
        this.getColumnModel().moveColumn(table.getColumnCount() - 1, _columnindex);
    }

    /**
     * 在某一位置插入一列,
     *
     * @param _columnindex
     *            想要插入的表中列的位置
     * @param _modelindex
     *            插入数据的哪一列
     */
    public void insertColumn(int _columnindex, String _modelkey) {
        int li_modelindex = findModelIndex(_modelkey);

        TableColumn column = getTableColumns()[li_modelindex - 1]; // 找到那一列
        this.getColumnModel().addColumn(column);
        this.getColumnModel().moveColumn(table.getColumnCount() - 1, _columnindex);
    }

    /**
     * 在某一位置插入一列,
     *
     * @param _columnindex
     *            想要插入的表中列的位置
     * @param _modelindex
     *            插入数据的哪一列
     */
    public void insertColumn(String _columnkey, String _modelkey) {
        int li_modelindex = findModelIndex(_modelkey);
        int li_columnindex = this.getColumnModel().getColumnIndex(_columnkey.toUpperCase());

        TableColumn column = getTableColumns()[li_modelindex]; // 找到那一列
        this.getColumnModel().addColumn(column); //
        this.getColumnModel().moveColumn(table.getColumnCount() - 1, li_columnindex);
    }

    /**
     * 删除某一列,显示的列
     *
     * @param _index
     */
    public void deleteColumn(int _index) {
        TableColumn column = this.getColumnModel().getColumn(_index);
        this.getColumnModel().removeColumn(column);
    }

    /**
     * 删除某一列,根据key
     *
     * @param _key
     */
    public void deleteColumn(String _key) {
        int li_index = this.getColumnModel().getColumnIndex(_key.toUpperCase());
        TableColumn column = this.getColumnModel().getColumn(li_index);
        this.getColumnModel().removeColumn(column);
    }

    private void showDetail() {
        int li_row = getSelectedRow();
        if (li_row < 0) {
            return;
        }
        if (templetVO.getPkname() == null || templetVO.getPkname().length() <= 0) {
            try {
                String[] allValues = new String[templetVO.getRealViewItemVOs().length];
                for (int i = 0; i < templetVO.getRealViewItemVOs().length; i++) {
                    allValues[i] = String.valueOf(getRealValueAtModel(li_row, templetVO.getItemKeys()[i]));
                }
                new RecordShowDialog(this, templetVO.getTablename(), templetVO.getItemKeys(), allValues);
            } catch (Exception e) {
                e.printStackTrace();
                NovaMessage.show(this, "该表在数据库中不存在！", NovaConstants.MESSAGE_ERROR);
            }
        } else {
            try {
                String pk_value = String.valueOf(getRealValueAtModel(li_row,templetVO.getPkname()));
                if(pk_value==null||pk_value.equals("")||pk_value.equalsIgnoreCase("null"))
                {
                	JOptionPane.showMessageDialog(this, "主键为空，无法查看!");
                	return;
                }
                new RecordShowDialog(this, templetVO.getTablename(), templetVO.getPkname(), pk_value);
            } catch (Exception ex) {
                ex.printStackTrace();
                NovaMessage.show(this, "该表在数据库中不存在！", NovaConstants.MESSAGE_ERROR);
            }
        }
    }

    //快速查询
    public void quickSearch() {
        int li_clickedColumnpos = getClickedColumnPos();
        TableColumn column = getClickedTableColumn();
        String str_name = (String) column.getHeaderValue();
        int selectedRow = 0;
        if (getTable().getSelectedRowCount() == 1) {
            selectedRow = getTable().getSelectedRow();
        }
        new QuickSearchDialog(this, str_name, selectedRow, getColumnValues(li_clickedColumnpos));
    }

    /**
     * 设置是否显示分页.
     *
     * @param _isshow
     */
    public void setPagePanelVisible(boolean _isshow) {
        if (PagePanel == null) {
            return;
        }
        this.PagePanel.setVisible(_isshow);
        this.updateUI();
    }

    /**
     * 设置是否显示基本操作的工具条
     *
     * @param _isshow
     */
    public void setOperatorPanelVisible(boolean _isshow) {
        if (OperatorPanel == null) {
            return;
        }
        this.OperatorPanel.setVisible(_isshow);
        this.updateUI();
    }

    public int[] getSearchcolumn() {
        return searchcolumn;
    }

    public void setSearchcolumn(int[] _searchcolumn) {
        searchcolumn = _searchcolumn;
    }

    

    public Pub_Templet_1_ItemVO[] getTempletItemVOs() {
        return templetItemVOs;
    }

    public void setTempletItemVOs(Pub_Templet_1_ItemVO[] templetItemVOs) {
        this.templetItemVOs = templetItemVOs;
    }

    public Pub_Templet_1VO getTempletVO() {
        return templetVO;
    }

    public void setTempletVO(Pub_Templet_1VO templetVO) {
        this.templetVO = templetVO;
    }

    public String getStr_realsql() {
        return str_realsql;
    }

    public void setValueAtRow(int _row, BillVO _vo) {
        for (int i = 0; i < _vo.getKeys().length; i++) {
            this.setValueAt(_vo.getDatas()[i], _row, _vo.getKeys()[i]);
        }
    }

    public void setValueAtRow(int _row, HashVO _vo) {
        for (int i = 0; i < _vo.getKeys().length; i++) {
            this.setValueAt(_vo.getStringValue(_vo.getKeys()[i]), _row, _vo.getKeys()[i]); //
        }
    }

    public void setValueAtRow(int _row, HashMap _map) {
        Iterator it = _map.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            this.setValueAt(_map.get(key), _row, key); //
        }
    }

    public void setValueAtAll(BillVO[] _vos) {
        if (_vos != null) {
            for (int i = 0; i < _vos.length; i++) {
                int row = this.newRow();
                setValueAtRow(row, _vos[i]);
            }
        }
    }

    public void clearDeleteBillVOs() {
        this.v_deleted_row.clear();
    }

    public void SaveData() {
        this.stopEditing(); // 结束编辑
        String[] str_sql = getOperatorSQLs(); //
        try {
        	String _datasourcename = NovaServerEnvironment.getInstance().getDefaultDataSourceName();
            //UIUtil.executeBatchByDS(str_templetcode, str_sql); // 有问题!!
        	UIUtil.executeBatchByDS(_datasourcename, str_sql); // 2007.09.12 fixed James.W
            JOptionPane.showMessageDialog(this, "保存数据成功!!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "保存数据失败!!" + e.getMessage());
        }
    }

    public void showSQL() {
        JOptionPane.showMessageDialog(this, "数据源:[" + getDataSourceName() + "]\nSQL语句:" + getStr_realsql());
    }

    public void onQuery() {
        new QueryDialog(this, templetVO);
    }

    /**
     * 快速赋值,锁定一列,迅速将这一列的值赋同一值
     */

    private void quickPutValue() {
        TableColumn col = this.getClickedTableColumn();
        int pos = -1;
        String key = (String) col.getIdentifier();
        for (int i = 0; i < templetItemVOs.length; i++) {
            if (key.equals(templetItemVOs[i].getItemkey())) {
                pos = i;
                break;
            }
        }
        if (pos != -1) {
            QuickPutValueDialog dialog = new QuickPutValueDialog(this, "快速赋值", templetItemVOs[pos], true);
            if (!dialog.isModified()) {
                return;
            }
            Object value = dialog.getValue();
            if (value.equals("") || (value instanceof RefItemVO) && ( (RefItemVO) value).getId().equals("")) {
                if (JOptionPane.showConfirmDialog(this, "确定要将为列赋空值吗?", "提示", JOptionPane.YES_NO_OPTION) !=
                    JOptionPane.YES_NO_OPTION) {
                    return;
                }
            }
            for (int i = 0; i < table.getRowCount(); i++) {
                if ( ( (BillListModel) table.getModel()).isCellEditable(i, col.getModelIndex())) {
                    ( (BillListModel) table.getModel()).setValueAt(value, i, key);
                }
            }
        }
    }

    private Object[][] getValueWithoutID(Object[][] _obj) {
        if(_obj==null || _obj.length==0) {
            return null;
        }
        Object[][] obj = new Object[_obj.length][_obj[0].length];
        for (int i = 0; i < _obj.length; i++) {
            for (int j = 0; j < _obj[i].length - 1; j++) {
                obj[i][j] = _obj[i][j + 1];
            }
        }
        return obj;
    }

    /**
     * 导出所有数据
     */
    public void exportExcelAllData() {
    	if(this._dataRows>65536){
    		NovaMessage.show("检索返回数据集过大（超过65536行）可能导致失败！若导出过程长时间没有回应则表示已经失败！请通知管理员调整客户端设置或者换用另外方式处理。");
    	}    	
        String[] str_names = this.templetVO.getItemNames(); //
        String[] str_types = this.templetVO.getItemTypes(); //
        int[] li_widths = new int[str_names.length]; //
        for (int i = 0; i < li_widths.length; i++) {
            li_widths[i] = this.templetItemVOs[i].getListwidth().intValue();
        }
        
        try{        
	        //选择导出Excel文件
	        String file_name=chooseFile(null);
	        if(file_name==null) return;
	        ExportToExcel expxls=new ExportToExcel(this, file_name, str_names, str_types, li_widths, null, false); //
	        //循环获得所有数据
	        int pages=this.getMaxPages();
	        for(int i=1;i<=pages;i++){
	        	System.out.println("当前导出页号="+i);
	        	Object[][] values = getValueWithoutID(getOnePageData(i)); 
	        	expxls.printValues(values);        	
	        }
	        expxls.endPrintValue();
        }catch(Exception e){
        	String ermsg=e.getMessage();
        	System.out.println("错误信息："+ermsg);
        	if(ermsg.indexOf("OutOfMemoryError")>=0||ermsg.indexOf("Java heap space")>=0){
        		NovaMessage.show("导出失败，原因是内存溢出！");
        	}else{
        		NovaMessage.show("导出失败！");
        	}        	
        }
    }
    
    public void exportExcelShowData() {
        Vector vec_names = new Vector();
        Vector vec_types = new Vector();
        Vector vec_widths = new Vector();
        for (int i = 0; i < templetItemVOs.length; i++) {
            if (templetItemVOs[i].getListisshowable().booleanValue()) {
                vec_names.add(templetItemVOs[i].getItemname());
                vec_types.add(templetItemVOs[i].getItemtype());
                vec_widths.add(templetItemVOs[i].getListwidth());
            }
        }
        String[] str_names = new String[vec_names.size()];
        String[] str_types = new String[vec_types.size()];
        int[] li_widths = new int[vec_widths.size()];
        for (int i = 0; i < vec_widths.size(); i++) {
            str_names[i] = (String) vec_names.get(i);
            str_types[i] = (String) vec_types.get(i);
            li_widths[i] = ( (Integer) vec_widths.get(i)).intValue();
        }

        Object[][] values = new Object[table.getRowCount()][table.getColumnCount() + 1];
        for (int i = 0; i < table.getRowCount(); i++) {
            int columnsIndex = 0;
            values[i][columnsIndex++] = String.valueOf(i);
            for (int j = 0; j < templetItemVOs.length; j++) {
                if (templetItemVOs[j].getListisshowable().booleanValue()) {
                    values[i][columnsIndex++] = this.getTableModel().getValueAt(i, j + 1);
                }
            }
        }
        exportExcel(str_names, str_types, li_widths, getValueWithoutID(values));
    }
    
    /**
     * 导出所有可见数据
     */
    public void exportAllSelectedData() {
    	if(this._dataRows>65536){
    		NovaMessage.show("检索返回数据集过大（超过65536行）可能导致失败！若导出过程长时间没有回应则表示已经失败！请通知管理员调整客户端设置或者换用另外方式处理。");
    	}    
    	
    	
    	Vector vec_names = new Vector();
        Vector vec_types = new Vector();
        Vector vec_widths = new Vector();
        boolean[] col_visible=new boolean[templetItemVOs.length];
        for (int i = 0; i < templetItemVOs.length; i++) {
        	col_visible[i]=false;//default
            if (templetItemVOs[i].getListisshowable().booleanValue()) {
                vec_names.add(templetItemVOs[i].getItemname());
                vec_types.add(templetItemVOs[i].getItemtype());
                vec_widths.add(templetItemVOs[i].getListwidth());
                col_visible[i]=true;//显示列
            }
        }
        String[] str_names = new String[vec_names.size()];
        String[] str_types = new String[vec_types.size()];
        int[] li_widths = new int[vec_widths.size()];
        for (int i = 0; i < vec_widths.size(); i++) {
            str_names[i] = (String) vec_names.get(i);
            str_types[i] = (String) vec_types.get(i);
            li_widths[i] = ( (Integer) vec_widths.get(i)).intValue();
        }
    	
        try{        
	        //选择导出Excel文件
	        String file_name=chooseFile(null);
	        if(file_name==null) return;
	        ExportToExcel expxls=new ExportToExcel(this, file_name, str_names, str_types, li_widths, null, false); //
	        //循环获得所有数据
	        int pages=this.getMaxPages();
	        for(int i=1;i<=pages;i++){
	        	//System.out.println("当前导出页号="+i);
	        	Object[][] values = getValueWithoutID(getOnePageData(i)); 
	        	Object[][] v_values=new Object[values.length][str_names.length];
	        	for(int m=0;m<values.length;m++){
	        		for(int n=0,r=0;n<col_visible.length;n++){
	        			if(col_visible[n]){
	        				v_values[m][r]=values[m][n];
	        				r++;
	        			}	        			
	        		}
	        	}	        	
	        	expxls.printValues(v_values);        	
	        }
	        expxls.endPrintValue();
        }catch(Exception e){
        	String ermsg=e.getMessage();
        	System.out.println("错误信息："+ermsg);
        	if(ermsg.indexOf("OutOfMemoryError")>=0||ermsg.indexOf("Java heap space")>=0){
        		NovaMessage.show("导出失败，原因是内存溢出！");
        	}else{
        		NovaMessage.show("导出失败！");
        	}        	
        }
    }

    

    /**
     * @param str_names
     * @param str_types
     * @param li_width
     * @param values
     */
    private void exportExcel(String[] str_names, String[] str_types, int[] li_width, Object[][] values) {
        if(values==null) {
            NovaMessage.show("没有数据可以导出！");
            return;
        }
        String file_name=chooseFile(null);
        if(file_name==null) return;
        new ExportToExcel(this, file_name, str_names, str_types, li_width, values, false); //
    }

    public static String chooseFile(Component parent) {
        JNovaFileChooser chooser = new JNovaFileChooser();
        JNovaFileFilter filter = new JNovaFileFilter("xls", "Microsoft Excel文件");
        chooser.addChoosableFileFilter(filter);
        File fOutPut = new File("output");
        try {
            if (!fOutPut.exists()) {
                fOutPut.mkdir();
            }
            chooser.setCurrentDirectory(fOutPut);
        } catch (Exception e) {
            chooser.setCurrentDirectory(new File("."));
        }
        int returnVal = chooser.showDialog(parent, "输出");
        if (returnVal == JNovaFileChooser.APPROVE_OPTION) {
            String fileName = chooser.getSelectedFile().getAbsolutePath();

            if (!fileName.endsWith(".xls")) {
                fileName += ".xls";
            }

            File f = new File(fileName);
            if (f.exists()) {
                if (JOptionPane.showConfirmDialog(parent, "您选择的文件已经存在，是否覆盖？", "提示", JOptionPane.YES_NO_OPTION,
                                                  JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION) {
                    return null;
                }
            }
            return fileName;
        }
        return null;
    }


    /**
     * 对结果集进行排序
     * @param _point
     */
    private void sortByColumn(Point _point) {
        if (this.curr_realValueData == null || this.curr_realValueData.length == 0) {
            return;
        }
        ListSelectionModel lsm_table = getTable().getSelectionModel();
        getTable().setSelectionModel(new DefaultListSelectionModel());

        int li_pos = getTable().getTableHeader().columnAtPoint(_point);

        TableColumn column = getTable().getColumnModel().getColumn(li_pos);

        String str_key = (String) column.getIdentifier();
        if (str_currsortcolumnkey != null && !str_key.equals(str_currsortcolumnkey)) {
            li_currsorttype = 0;
        }

        int li_modelondex = findModelIndex(str_key); //
        // System.out.println("位置[" + li_pos + "] key:" + str_key);
        if (li_currsorttype == 0 || li_currsorttype == -1) {
            Object[][] newObjects = sortObjs(this.curr_realValueData, li_modelondex, true); // 升序
            putValue(newObjects);
            li_currsorttype = 1;
        } else {
            Object[][] newObjects = sortObjs(this.curr_realValueData, li_modelondex, false); // 升序
            putValue(newObjects);
            li_currsorttype = -1;
        }
        getTable().setSelectionModel(lsm_table);
        str_currsortcolumnkey = str_key; //
    }
    

    /**
     * 排序给定结果
     * @param _objs
     * @param _pos 
     * @param _isdesc
     * @return
     */
    public Object[][] sortObjs(Object[][] _objs, int _pos, boolean _isdesc) {
        Vector v_tmp = new Vector();

        for (int i = 0; i < _objs.length; i++) {
            // Object obj = _objs[i][_pos];
            int li_pos = findPos(v_tmp, _objs[i], _pos); // 找出对应的位置
            // System.out.println("[" + i + "]位置:[" + li_pos + "]");
            v_tmp.insertElementAt(_objs[i], li_pos); // 插入数据
        }

        Object[][] objs_return = new Object[v_tmp.size()][_objs[0].length];

        if (_isdesc) { // 升序
            for (int i = 0; i < v_tmp.size(); i++) {
                objs_return[i] = (Object[]) v_tmp.get(i);
            }
        } else {
            int li_cycle = 0;
            for (int i = v_tmp.size() - 1; i >= 0; i--) {
                objs_return[li_cycle] = (Object[]) v_tmp.get(i);
                li_cycle++;
            }
        }

        return objs_return;
    }

    private int findPos(Vector _v, Object[] _objs, int _pos) {
        int li_pos = 0;
        // 比较templetItemVOs[_pos-1]处的ITEMVO，因为在findModelIndex中增加了1
        boolean isInt = templetItemVOs[_pos - 1].getItemtype().equalsIgnoreCase("数字框");
        for (int i = 0; i < _v.size(); i++) {
            Object[] objs = (Object[]) _v.get(i); // 队列中的对象
            int li_compareresult = -1;
            if (isInt) {
                li_compareresult = compareInt(objs[_pos], _objs[_pos]);
            } else {
                li_compareresult = compareObject(objs[_pos], _objs[_pos]);
            }
            if (li_compareresult > 0) { // 如果发现我比队列中的数据大,则继续往下走

            } else if (li_compareresult < 0) { // 如果发现我比队列中的数据小，则返回
                return i;
            } else {
                return i;
            }
            li_pos++;
        }
        return li_pos;
    }

    private int compareInt(Object _obj1, Object _obj2) {
        //
        String str_1 = null;
        String str_2 = null;
        if (_obj1 != null) {
            str_1 = _obj1.toString();
        }
        if (_obj2 != null) {
            str_2 = _obj2.toString();
        }

        if (str_1 == null) {
            if (str_2 == null) {
                return 0;
            } else {
                return 1;
            }
        } else {
            if (str_2 == null) {
                return -1;
            } else {
                int int_1 = Integer.parseInt(str_1);
                int int_2 = Integer.parseInt(str_2);
                return int_1 - int_2 > 0 ? 0 : 1;
            }
        }

    }

    private int compareObject(Object _obj1, Object _obj2) {
        //
        String str_1 = null;
        String str_2 = null;
        if (_obj1 != null) {
            str_1 = _obj1.toString();
        }
        if (_obj2 != null) {
            str_2 = _obj2.toString();
        }

        if (str_1 == null) {
            if (str_2 == null) {
                return 0;
            } else {
                return 1;
            }
        } else {
            if (str_2 == null) {
                return -1;
            } else {
                Collator myCollator = Collator.getInstance();
                return myCollator.compare(str_2, str_1);
                // System.out.println(str_1 + "," + str_2);
                // return str_2.compareTo(str_1);
            }
        }

    }

    public String replaceAll(String str_par, String old_item, String new_item) {
        String str_return = "";
        String str_remain = str_par;
        boolean bo_1 = true;
        while (bo_1) {
            int li_pos = str_remain.indexOf(old_item);
            if (li_pos < 0) {
                break;
            } // 如果找不到,则返回
            String str_prefix = str_remain.substring(0, li_pos);
            str_return = str_return + str_prefix + new_item; // 将结果字符串加上原来前辍
            str_remain = str_remain.substring(li_pos + old_item.length(), str_remain.length());
        }
        str_return = str_return + str_remain; // 将剩余的加上
        return str_return;
    }

    public JPanel getCustomerNavigationJPanel() {
        return customerNavigationJPanel;
    }

    public void setCustomerNavigationJPanel(JPanel customerNavigationJPanel) {
        this.customerNavigationJPanel = customerNavigationJPanel;
    }

    public Pub_Templet_1_ItemVO getTempletItemVO(String key) {
        for (int i = 0; i < templetItemVOs.length; i++) {
            if (templetItemVOs[i].getItemkey().equals(key)) {
                return templetItemVOs[i];
            }
        }
        return null;
    }

    public boolean containsItemKey(String _itemKey) {
        return this.getTableModel().containsItemKey(_itemKey); //
    }

    /**
     * 校验
     * @return
     */
    public boolean checkValidate() {
        return checkIsNullValidate(); //先只处理非空校验,以后还会增加校验公式!!
    }

    /**
     * 非空校验,
     * @return
     */
    private boolean checkIsNullValidate() {
        String[] str_keys = this.getTempletVO().getItemKeys(); //所有的key
        String[] str_names = this.getTempletVO().getItemNames(); //所有的Name
        boolean[] bo_isMustInputs = this.getTempletVO().getItemIsMustInputs(); //是否必输入

        int li_rowcount = getTableModel().getRowCount();
        for (int i = 0; i < li_rowcount; i++) {
            for (int j = 0; j < str_keys.length; j++) {
                if (bo_isMustInputs[j]) { //如果是必输入项!!
                    Object obj = getValueAt(i, str_keys[j]); //
                    if (obj == null) {
                        JOptionPane.showMessageDialog(this,
                            "模板[" + this.getTempletVO().getTempletname() + "],第[" + (i + 1) + "]行数据,[" + str_names[j] +
                            "]不能为空!"); //
                        focusOn(i, str_keys[j]); //
                        return false;
                    } else {
                        if (obj instanceof String) { //..
                            String new_name = (String) obj;
                            if (new_name.trim().equals("")) {
                                JOptionPane.showMessageDialog(this,
                                    "模板[" + this.getTempletVO().getTempletname() + "],第[" + (i + 1) + "]行数据,[" +
                                    str_names[j] + "]不能为空!"); //
                                focusOn(i, str_keys[j]); //
                                return false;
                            }
                        }

                    }
                }
            }
        }

        return true;
    }

    /**
     * 光标停在某一列!!
     * @param _row
     * @param _itemKey
     */
    private void focusOn(int _row, String _itemKey) {
        int li_colIndex = findColumnIndex(_itemKey); //如果这一列是隐藏的,则返回-1
        if (li_colIndex < 0) {
            return;
        }

        this.getTable().editCellAt(_row, li_colIndex); //
        INovaCellEditor cellEditor = (INovaCellEditor)this.getTable().getCellEditor(_row, li_colIndex); //取得编辑器!!
        JComponent obj_comp = cellEditor.getNovaCompent(); //以得控件!!
        if (obj_comp instanceof JTextField) { //如果文本框!!
            JTextField new_obj = (JTextField) obj_comp;
            new_obj.requestFocus();
            new_obj.requestFocusInWindow(); //
        } else if (obj_comp instanceof JComboBox) { //如果是下拉框!!
            JComboBox new_obj = (JComboBox) obj_comp;
            new_obj.requestFocus();
            new_obj.requestFocusInWindow(); //
        } else if (obj_comp instanceof JCheckBox) { //如果是勾选框
            JCheckBox new_obj = (JCheckBox) obj_comp;
            new_obj.requestFocus();
            new_obj.requestFocusInWindow(); //
        } else if (obj_comp instanceof INovaCompent) { //如果是INovaCompent
            INovaCompent new_obj = (INovaCompent) obj_comp;
            new_obj.focus();
        } else {
        }
        this.updateUI(); //
    }

    /**
     * 取得加载这个billListPanel的Frame,比如是各种风格模板!!
     *
     * @return
     */
    public Component getLoadedFrame() {
        return loadedFrame;
    }
    //获取右键菜单项
    public JMenuItem[] getPopupMenuItem()
    {
    	if(menuItem!=null)
    		return (JMenuItem[])menuItem.toArray(new JMenuItem[0]);
    	return null;
    }
    //设置右键菜单项
    public void addPopupMenuItems(JMenuItem item)
    {
    	if(menuItem==null)
    		menuItem = new Vector();
    	menuItem.addElement(item);
    }
    //设置右键菜单项
    public void addPopupMenuItems(JMenuItem[] items)
    {
    	if(menuItem==null)
    		menuItem = new Vector(items.length);
    	for(int i=0;i<items.length;i++)
    		menuItem.addElement(items[i]);
    }
    /**
     * 设置加载这个billListPanel的Frame,比如是各种风格模板!!
     *
     * @param loadedFrame
     */
    public void setLoadedFrame(Component loadedtFrame) {
        this.loadedFrame = loadedFrame;
    }

    public void setColumnVisible(String _itemkey,boolean b){
        int width =0;

        for (int i = 0; i < this.templetItemVOs.length; i++) {
            if (templetItemVOs[i].getItemkey().equalsIgnoreCase(_itemkey)) {
                if (b)
                    width = templetItemVOs[i].getCardwidth().intValue();
                TableColumn  tc  =  getColumnModel().getColumn(i-1);
                tc.setMaxWidth(width);
                tc.setPreferredWidth(width);
                tc.setWidth(width);
                tc.setMinWidth(width);

                table.getColumnModel().getColumn(i-1).setMaxWidth(width);
                table.getColumnModel().getColumn(i-1).setMinWidth(width);

                break;
            }
        }

    }
    /**
     * 嵌入类 表格适配器
     * @author all
     *
     */
    class TableAdapter implements ActionListener {

        JTable jt_table;

        public TableAdapter(JTable _table) {
            jt_table = _table;
            KeyStroke ks_copy = KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK, false);
            jt_table.registerKeyboardAction(this, "Copy", ks_copy, JComponent.WHEN_FOCUSED); //
        }

        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("Copy")) {
                int li_row = jt_table.getSelectedRow();
                int li_col = jt_table.getSelectedColumn();

                if (li_row >= 0 && li_col >= 0) {
                    String str_value = "";
                    Object obj = jt_table.getValueAt(li_row, li_col);
                    if (obj != null) {
                        str_value = obj.toString();
                        StringSelection ss = new StringSelection(str_value);
                        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
                    }
                }
            }
        }
    }

    /**
     * 表头双击排序时显示不同图标!!
     * @author user
     *
     */
    class SortHeaderRenderer extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 2924685007982535240L;

        Pub_Templet_1VO parentVO = null;

        Pub_Templet_1_ItemVO[] childVOs = null;

        boolean isadmin = true;

        public SortHeaderRenderer(Pub_Templet_1VO _templetVO, boolean isadmin) {
            this.parentVO = _templetVO;
            this.childVOs = _templetVO.getItemVos();
            this.isadmin = isadmin;
        }

        public Component getTableCellRendererComponent(JTable jtable, Object obj, boolean isSelected, boolean hasFocus,
            int _row, int _column) {
            if (jtable != null) {
                JTableHeader jtableheader = jtable.getTableHeader();
                if (jtableheader != null) {
                    this.setBackground(jtableheader.getBackground());
                    this.setFont(jtableheader.getFont());
                }
            }

            this.setHorizontalAlignment(SwingConstants.CENTER);

            TableColumn column = jtable.getColumnModel().getColumn(_column);
            String str_key = (String) column.getIdentifier(); //

            String str_text = (obj != null ? obj.toString() : ""); //
            String original_str_text = str_text;
            String str_tooltiptext = "";

            Pub_Templet_1_ItemVO itemVO = parentVO.getItemVo(str_key); //

            if (itemVO == null) {
                this.setForeground(java.awt.Color.RED); //
                this.setToolTipText("没有找到对应的ItemKey"); //
            } else {
                if (itemVO.isNeedSave()) {

                    if (itemVO.isCanSave()) {
                        str_text = "+" + str_text; //
                        str_tooltiptext = str_tooltiptext + " [" + itemVO.getPub_Templet_1VO().getSavedtablename() +
                            "]"; //
                    } else {
                        str_text = "+" + str_text; //
                        str_tooltiptext = str_tooltiptext + " [" + itemVO.getPub_Templet_1VO().getSavedtablename() +
                            "],该表没有该列!!将会保存失败!!"; //
                    }

                }

                if (itemVO.isViewColumn()) {
                    str_text = "☆" + str_text; //
                    str_tooltiptext = "[" + itemVO.getPub_Templet_1VO().getTablename() + "." + itemVO.getItemkey() +
                        "]" + str_tooltiptext;
                } else {
                    str_tooltiptext = "[" + itemVO.getItemkey() + "]" + str_tooltiptext; //
                }
                if (itemVO.isPrimaryKey()) {
                    this.setForeground(java.awt.Color.BLUE); //
                } else if (itemVO.isMustInput()) {
                    if (isadmin) {
                        this.setForeground(new java.awt.Color(38, 147, 255)); //
                    }
                } else {
                    this.setForeground(java.awt.Color.BLACK); //
                }

                if (itemVO.isNeedSave()) {
                    if (isadmin) {
                        if (!itemVO.isCanSave()&&NovaClientEnvironment.getInstance().isAdmin()) {
                            this.setForeground(java.awt.Color.RED); //
                        }
                    }
                }
                // 如果是管理员，则显示提示信息.
                if (isadmin) {
                    this.setText(str_text);
                    this.setToolTipText(str_tooltiptext);
                }
                // 否则不显示提示信息
                else {
                    this.setText(original_str_text);
                }
            }

            if (str_key.equals(str_currsortcolumnkey)) {
                if (li_currsorttype == 1) {
                    setIcon(new UpDownArrow(0));
                } else if (li_currsorttype == -1) {
                    setIcon(new UpDownArrow(1));
                } else {
                    setIcon(null);
                }
            } else {
                setIcon(null);
            }

            setBorder(UIManager.getBorder("TableHeader.cellBorder"));
            return this;
        }
    }

    /**
     * 显示正反排序的上下箭头
     * @author Administrator
     *
     */
    class UpDownArrow implements Icon {

        private int size = 12;

        public static final int UP = 0;

        public static final int DOWN = 1;

        private int direction;

        public UpDownArrow(int i) {
            direction = i;
        }

        public int getIconHeight() {
            return size;
        }

        public int getIconWidth() {
            return size;
        }

        public void paintIcon(Component component, Graphics g, int i, int j) {
            int k = i + size / 2;
            int l = i + 1;
            int i1 = (i + size) - 2;
            int j1 = j + 1;
            int k1 = (j + size) - 2;
            Color color = (Color) UIManager.get("controlDkShadow");
            if (direction == 0) {
                g.setColor(Color.white);
                g.drawLine(l, k1, i1, k1);
                g.drawLine(i1, k1, k, j1);
                g.setColor(color);
                g.drawLine(l, k1, k, j1);
            } else {
                g.setColor(color);
                g.drawLine(l, j1, i1, j1);
                g.drawLine(l, j1, k, k1);
                g.setColor(Color.white);
                g.drawLine(i1, j1, k, k1);
            }
        }
    }
    
    /**
     * 根据背景色公式，计算背景色
     * @return
     */
    private String getColor(int row,String colorexp) {        
        String str_colorFormula = colorexp;
        if (str_colorFormula != null && !str_colorFormula.trim().equals("")) {
            HashMap map_rowcache = this.getValueAtRowWithHashMap(row); // 将当前数据取得置入一个HashMap
            
            JepFormulaParse jepParse = new JepFormulaParse(JepFormulaParse.li_ui);
            
            Object obj = null;

            try {
            	//解析一下公式
                str_colorFormula=(new ModifySqlUtil()).convertMacroStr(str_colorFormula,NovaClientEnvironment.getInstance(), map_rowcache);
                obj = jepParse.execFormula(str_colorFormula); // 使用JEP执行公式!!!!
                String str_return = (obj == null ? "" : "" + obj);
                //System.out.println("成功执行颜色公式项:[" + str_colorFormula + "],JEP运算结果[" + str_return + "]"); //
                return str_return; //
            } catch (Exception ex) {
                //System.out.println("失败执行颜色公式项:[" + str_colorFormula + "]！"); //
                //ex.printStackTrace(); //
                return null;
            }
        }else{
        	return null;
        }        
    }
    
    /**
	 * 选择框展现
	 * @author James.W
	 */
	class CellCheckboxRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 2924685023482535240L;

		private HashMap _param=null;
		/**
		 * 构造文件，处理一些显示控制等等
		 */
		public CellCheckboxRenderer(HashMap param){
			_param=param;
		}
		
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
			
			String v=(value==null)?"N":value.toString();
			//String simg="Y".equalsIgnoreCase(v)?"images/office/(19,45).png":"images/office/(18,45).png";
			String simg="Y".equalsIgnoreCase(v)?"images/office/(01,10).png":null;			
			
			JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
			label.setText("");
			label.setIcon(UIUtil.getImage(simg));
			label.setHorizontalAlignment(SwingConstants.CENTER);
			
			//设置文本底色//调用颜色公式
			String color=(_param.get("showbgcolor")!=null&&((String)_param.get("showbgcolor")).equalsIgnoreCase("true"))
			             ?getColor(row,(String)_param.get("bgcolorexp")):null;
			if(color!=null&&!color.equals("")){
				if(color.startsWith("#")){
					int rgb=Integer.parseInt(color.substring(1),16);
					setBackground(new Color(rgb));
				}else{
					String[] rgbs=color.split(",");
					setBackground(new Color(Integer.parseInt(rgbs[0]),Integer.parseInt(rgbs[1]),Integer.parseInt(rgbs[2])));
				}				
			}else{
				if((String)(_param.get("showbgcolor"))!=null&&((String)_param.get("showbgcolor")).equalsIgnoreCase("true")){
					Color c=RowColors[row%RowColors.length];
					if(isSelected){
						setBackground(new Color(176,196,222));//TODO 放到配置文件中
					}else{
						setBackground(c);
					}			
				}
			}
			
			return label;
		}
	}
    
    /**
	 * 添加单元格提示信息，背景底色等等操作
	 * @author Sushangdian
	 */
	class CellTextRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 2924685023482535240L;

		private HashMap _param=null;
		/**
		 * 构造文件，处理一些显示控制等等
		 */
		public CellTextRenderer(HashMap param){
			_param=param;
		}
		
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int col) {
			JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
			label=(label==null)?(new JLabel("")):label;
			//设置文本提示
			if((String)(_param.get("showtip"))!=null&&((String)_param.get("showtip")).equalsIgnoreCase("true")){
				String stip=(value != null)?value.toString():"";
				label.setToolTipText(stip);				
			}
			
			//设置文本底色//调用颜色公式
			String color=(_param.get("showbgcolor")!=null&&((String)_param.get("showbgcolor")).equalsIgnoreCase("true"))
			             ?getColor(row,(String)_param.get("bgcolorexp")):null;
			if(color!=null&&!color.equals("")){
				if(color.startsWith("#")){
					int rgb=Integer.parseInt(color.substring(1),16);
					setBackground(new Color(rgb));
				}else{
					String[] rgbs=color.split(",");
					setBackground(new Color(Integer.parseInt(rgbs[0]),Integer.parseInt(rgbs[1]),Integer.parseInt(rgbs[2])));
				}				
			}else{
				if((String)(_param.get("showbgcolor"))!=null&&((String)_param.get("showbgcolor")).equalsIgnoreCase("true")){
					Color c=RowColors[row%RowColors.length];
					if(isSelected){
						setBackground(new Color(176,196,222));//TODO 放到配置文件中
					}else{
						setBackground(c);
					}			
				}
			}
			
			return label;
		}
	}

	public String buildSql(String subSql) {
		return getSQL(subSql);
	}
	public void doQuery(String sql) {
		QueryData(sql);		
	}


    
}
/*******************************************************************************
 * $RCSfile: BillListPanel.java,v $ $Revision: 1.25.2.33 $ $Date: 2010/04/07 09:30:28 $
 *
 * $Log: BillListPanel.java,v $
 * Revision 1.25.2.33  2010/04/07 09:30:28  wangqi
 * *** empty log message ***
 *
 * Revision 1.25.2.32  2010/02/03 08:14:29  wangqi
 * *** empty log message ***
 *
 * Revision 1.25.2.31  2010/01/25 09:51:40  wangqi
 * *** empty log message ***
 *
 * Revision 1.25.2.30  2010/01/20 10:08:30  wangqi
 * *** empty log message ***
 *
 * Revision 1.25.2.29  2010/01/11 02:23:14  wangqi
 * *** empty log message ***
 *
 * Revision 1.25.2.28  2009/12/16 04:14:01  wangqi
 * *** empty log message ***
 *
 * Revision 1.25.2.27  2009/12/04 07:06:39  wangqi
 * *** empty log message ***
 *
 * Revision 1.25.2.26  2009/12/02 08:49:57  wangqi
 * *** empty log message ***
 *
 * Revision 1.25.2.25  2009/12/02 05:47:58  wangqi
 * *** empty log message ***
 *
 * Revision 1.25.2.24  2009/10/16 03:49:01  wangqi
 * *** empty log message ***
 *
 * Revision 1.25.2.23  2009/09/07 06:53:08  yangjm
 * *** empty log message ***
 *
 * Revision 1.25.2.22  2009/07/10 09:14:38  wangqi
 * *** empty log message ***
 *
 * Revision 1.25.2.21  2009/06/24 08:38:49  wangqi
 * *** empty log message ***
 *
 * Revision 1.25.2.20  2009/06/12 05:25:15  wangqi
 * *** empty log message ***
 *
 * Revision 1.25.2.19  2009/06/02 05:16:50  wangqi
 * *** empty log message ***
 *
 * Revision 1.25.2.18  2009/05/25 07:50:20  wangqi
 * *** empty log message ***
 *
 * Revision 1.25.2.17  2009/03/16 07:24:41  wangqi
 * *** empty log message ***
 *
 * Revision 1.25.2.16  2009/02/20 02:41:25  wangqi
 * *** empty log message ***
 *
 * Revision 1.25.2.15  2009/02/19 10:40:32  wangqi
 * *** empty log message ***
 *
 *
 * Revision 1.18  2007/08/30 07:22:41  yanghuan
 * 陈学进根据mr Nova 20-29 实现：查询出来的数据导出到excel的时候，在原基础上再加一个“所有显示列数据”的菜单项。
 *
 * Revision 1.17  2007/08/24 07:10:39  yanghuan
 * 解决QueryDialog中增加了排序条件就查询失败的bug
 *
 * Revision 1.16  2007/08/22 03:47:07  wangqi
 * *** empty log message ***
 *
 * Revision 1.15  2007/07/31 08:22:13  sunxf
 * MR#:Nova 20-17
 *
 * Revision 1.14  2007/07/23 11:34:29  sunxf
 * MR#:Nova20-15
 *
 * Revision 1.13  2007/07/23 10:59:01  sunxf
 * Nova 20-14    平台层面更新数据的地方提供乐观锁控制，解决并发访问问题
 *
 * Revision 1.12  2007/07/19 03:42:50  sunxf
 * MR#:Nova 20-9
 *
 * Revision 1.11  2007/07/05 12:16:20  sunxf
 * 编辑公式中增加xxx=>refresh() ,xxx=>reset(),xxx=>enable(),xxx=>unable()方法
 *
 * Revision 1.10  2007/06/14 08:04:26  lst
 * no message
 *
 * Revision 1.9  2007/06/11 03:53:08  qilin
 * no message
 *
 * Revision 1.8  2007/06/11 03:31:18  qilin
 * no message
 *
 * Revision 1.7  2007/05/31 07:38:14  qilin
 * code format
 *
 * Revision 1.6  2007/05/31 06:47:50  qilin
 * 界面重构，所有的JFrame改为JInternalFrame样式
 *
 * Revision 1.5  2007/05/22 07:58:45  qilin
 * no message
 *
 * Revision 1.4  2007/05/22 02:03:21  sunxb
 * *** empty log message ***
 *
 * Revision 1.3  2007/05/21 10:03:17  sunxb
 * *** empty log message ***
 *
 * Revision 1.2  2007/05/17 07:36:37  qilin
 * no message
 *
 * Revision 1.1  2007/05/17 06:01:37  qilin
 * no message
 *
 * Revision 1.47  2007/04/24 05:59:35  qilin
 * no message
 *
 * Revision 1.46  2007/04/04 02:01:41  shxch
 * *** empty log message ***
 *
 * Revision 1.45  2007/03/30 10:13:34  shxch
 * *** empty log message ***
 *
 * Revision 1.44  2007/03/30 10:04:34  shxch
 * *** empty log message ***
 *
 * Revision 1.42  2007/03/30 10:00:08  shxch
 * *** empty log message ***
 *
 * Revision 1.41  2007/03/28 11:31:44  shxch
 * *** empty log message ***
 *
 * Revision 1.40  2007/03/28 11:22:52  shxch
 * *** empty log message ***
 *
 * Revision 1.39  2007/03/28 10:52:14  shxch
 * *** empty log message ***
 *
 * Revision 1.38  2007/03/28 05:48:07  shxch
 * *** empty log message ***
 *
 * Revision 1.37  2007/03/28 05:46:14  shxch
 * *** empty log message ***
 *
 * Revision 1.36  2007/03/27 08:01:14  shxch
 * *** empty log message ***
 *
 * Revision 1.35  2007/03/23 05:05:19  shxch
 * *** empty log message ***
 *
 * Revision 1.34  2007/03/22 07:10:26  shxch
 * *** empty log message ***
 *
 * Revision 1.33  2007/03/22 07:09:33  shxch
 * *** empty log message ***
 *
 * Revision 1.32  2007/03/16 03:27:53  sunxf
 * *** empty log message ***
 *
 * Revision 1.31  2007/03/15 06:45:17  shxch
 * *** empty log message ***
 *
 * Revision 1.30  2007/03/15 05:09:57  shxch
 * *** empty log message ***
 *
 * Revision 1.29  2007/03/15 02:33:14  shxch
 * *** empty log message ***
 *
 * Revision 1.28  2007/03/13 09:03:04  shxch
 * *** empty log message ***
 *
 * Revision 1.27  2007/03/13 08:41:19  shxch
 * *** empty log message ***
 *
 * Revision 1.26  2007/03/12 13:21:19  shxch
 * *** empty log message ***
 * Revision 1.25 2007/03/10 03:56:29 shxch
 * 添加导出Excel表格功能 Revision 1.24 2007/03/09 10:24:40 shxch *** empty log message
 * ***
 *
 * Revision 1.23 2007/03/08 13:11:55 shxch *** empty log message ***
 *
 * Revision 1.22 2007/03/07 02:25:02 shxch *** empty log message ***
 *
 * Revision 1.21 2007/03/07 02:01:54 shxch *** empty log message ***
 *
 * Revision 1.20 2007/03/05 02:49:17 shxch *** empty log message ***
 *
 * Revision 1.19 2007/03/05 02:11:46 shxch *** empty log message ***
 *
 * Revision 1.18 2007/03/02 05:28:06 shxch *** empty log message ***
 *
 * Revision 1.17 2007/03/02 05:16:42 shxch *** empty log message ***
 *
 * Revision 1.16 2007/03/02 05:08:24 shxch *** empty log message ***
 *
 * Revision 1.15 2007/03/02 05:02:49 shxch *** empty log message ***
 *
 * Revision 1.14 2007/03/01 09:06:56 shxch *** empty log message ***
 *
 * Revision 1.13 2007/02/27 06:57:18 shxch *** empty log message ***
 *
 * Revision 1.12 2007/02/27 06:03:00 shxch *** empty log message ***
 *
 * Revision 1.11 2007/02/27 05:28:04 shxch *** empty log message ***
 *
 * Revision 1.10 2007/02/27 05:15:54 shxch *** empty log message ***
 *
 * Revision 1.9 2007/02/10 08:59:51 shxch *** empty log message ***
 *
 * Revision 1.8 2007/02/07 06:01:23 shxch *** empty log message ***
 *
 * Revision 1.7 2007/02/07 03:46:08 lujian
 * 修改insertRowWithInitStatus()方法，添加version判断 Revision 1.6 2007/02/02 06:37:32
 * shxch *** empty log message ***
 *
 * Revision 1.5 2007/02/01 08:04:20 shxch *** empty log message ***
 *
 * Revision 1.4 2007/02/01 02:47:31 shxch 添加数据处理时的等待框
 *
 * Revision 1.3 2007/01/30 05:15:46 sunxf refreshData(Object[][] _data)
 *
 * Revision 1.2 2007/01/30 04:56:14 lujian *** empty log message ***
 *
 *
 ******************************************************************************/
