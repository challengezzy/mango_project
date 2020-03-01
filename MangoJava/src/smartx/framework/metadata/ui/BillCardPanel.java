/**************************************************************************
 * $RCSfile: BillCardPanel.java,v $  $Revision: 1.9.2.19 $  $Date: 2010/02/21 02:07:58 $
 **************************************************************************/

package smartx.framework.metadata.ui;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import smartx.framework.common.ui.NovaClientEnvironment;
import smartx.framework.common.ui.NovaConstants;
import smartx.framework.common.ui.NovaPanelUtil;
import smartx.framework.common.ui.NovaRemoteServiceFactory;
import smartx.framework.common.ui.UIUtil;
import smartx.framework.common.utils.StringUtil;
import smartx.framework.common.vo.FrameWorkTBUtil;
import smartx.framework.common.vo.HashVO;
import smartx.framework.common.vo.NovaRemoteException;
import smartx.framework.common.vo.VectorMap;
import smartx.framework.metadata.intercept.DefaultUICardIntercept;
import smartx.framework.metadata.intercept.UICardInterceptIFC;
import smartx.framework.metadata.ui.componentscard.ComBoxPanel;
import smartx.framework.metadata.ui.componentscard.INovaCompent;
import smartx.framework.metadata.ui.componentscard.NumberFieldPanel;
import smartx.framework.metadata.ui.componentscard.PasswordFieldPanel;
import smartx.framework.metadata.ui.componentscard.RecordShowDialog;
import smartx.framework.metadata.ui.componentscard.TextAreaPanel;
import smartx.framework.metadata.ui.componentscard.TextFieldPanel;
import smartx.framework.metadata.ui.componentscard.UICalculatorPanel;
import smartx.framework.metadata.ui.componentscard.UICheckBoxPanel;
import smartx.framework.metadata.ui.componentscard.UIColorPanel;
import smartx.framework.metadata.ui.componentscard.UIDateTimePanel;
import smartx.framework.metadata.ui.componentscard.UIFilePathPanel;
import smartx.framework.metadata.ui.componentscard.UIImagePanel;
import smartx.framework.metadata.ui.componentscard.UIRefPanel;
import smartx.framework.metadata.ui.componentscard.UITextAreaPanel;
import smartx.framework.metadata.ui.componentscard.UITimeSetPanel;
import smartx.framework.metadata.vo.BillVO;
import smartx.framework.metadata.vo.ComBoxItemVO;
import smartx.framework.metadata.vo.Pub_Templet_1VO;
import smartx.framework.metadata.vo.Pub_Templet_1_ItemVO;
import smartx.framework.metadata.vo.RefItemVO;
import smartx.framework.metadata.vo.RowNumberItemVO;
import smartx.framework.metadata.vo.jepfunctions.JepFormulaParse;
import smartx.framework.metadata.vo.jepfunctions.JepFormulaUtil;
import smartx.framework.metadata.vo.templetvo.AbstractTempletVO;

public class BillCardPanel extends JPanel {
	private static final long serialVersionUID = 9086081912297545806L; //
    
    protected Component loadedFrame = null;                 // 卡片面板的所在父对象,比如是各种风格模板Frame
    protected JPanel mainPanel=null;                        //主控件面板 
    protected String uiIntercept=null;                      //UI拦截器
    protected UICardInterceptIFC uiInterceptImpl=null;      //UI拦截器
    protected String bsIntercept=null;                      //BS拦截器
    
    
    private boolean is_admin = false;
    private HashMap v_compents = new HashMap();                        
    public String str_rownumberMark = "_RECORD_ROW_NUMBER";
    private String str_templetcode = null;
    private Pub_Templet_1VO templetVO = null;
    private Pub_Templet_1_ItemVO[] templetItemVOs = null;
    private Vector v_listeners = new Vector(); // 反射注册的事件监听者!!!
    private ArrayList unvisibleitem = null; // 将本界面上不想要显示的ITEMKEY放进来
    private TitledBorder border = null;
    private RowNumberItemVO rowNumberItemVO = null; // 行号数据VO.....
    private boolean bo_isallowtriggereditevent = true; // 是否允许触发编辑事件
    private AbstractTempletVO abstractTempletVO = null;
    private String bordertitle = "";
    
    
    /**
     * 构造方法
     */
    private BillCardPanel() {
    }

    /**
     * 构造方法
     * @param _templetcode
     */
    public BillCardPanel(String _templetcode) {
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
     * 构造方法
     * @param _TempletVO
     */
    public BillCardPanel(Pub_Templet_1VO _TempletVO) {
        this.str_templetcode = _TempletVO.getTempletcode();
        templetVO = _TempletVO;
        templetItemVOs = templetVO.getItemVos(); // 各项
        initialize(); //
    } 
    

    /**
     * 构造方法
     * @param _templetcode
     * @param _unvisibleitem
     */
    public BillCardPanel(String _templetcode, ArrayList _unvisibleitem) {
        this.str_templetcode = _templetcode;
        unvisibleitem = _unvisibleitem;
        try {
            templetVO = UIUtil.getPub_Templet_1VO(str_templetcode);
        } catch (Exception e) {
            e.printStackTrace();
        } // 取得单据模板VO
        templetItemVOs = templetVO.getItemVos(); // 各项
        initialize();
    }

    /**
     * 根据直接生成的抽象模板VO创建页面!!!,即配置数据不存在 pub_templet_1表与pub_templet_1_item表中的
     * @param _abstractTempletVO
     */
    public BillCardPanel(AbstractTempletVO _abstractTempletVO) {
        this.str_templetcode = _abstractTempletVO.getPub_templet_1Data().getStringValue(""); //
        try {
            templetVO = UIUtil.getPub_Templet_1VO(_abstractTempletVO.getPub_templet_1Data(),
                                                  _abstractTempletVO.getPub_templet_1_itemData());
        } catch (Exception e) {
            e.printStackTrace();
        } //
        templetItemVOs = templetVO.getItemVos(); // 各项
        abstractTempletVO = _abstractTempletVO; // 创建方式
        initialize(); //
    }

    /**
     * 设置UI拦截器
     * @param uiIntercept
     */
    public void setUIIntercept(String uiIntercept){
    	this.uiIntercept=uiIntercept;
    }
    
    /**
     * 获得UI拦截器设置
     * @param uiIntercept
     */
    public String getUIIntercept(){
    	return this.uiIntercept;
    }
    
    /**
     * 设置BS拦截器
     * @param uiIntercept
     */
    public void setBSIntercept(String bsIntercept){
    	this.bsIntercept=bsIntercept;
    }
    
    /**
     * 获得BS拦截器设置
     * @param uiIntercept
     */
    public String getBSIntercept(){
    	return this.bsIntercept;
    }
    
    /**
     * 重新初始化
     */
    private void reInitialize() {
        this.removeAll(); //
        this.v_compents.clear();
        this.initialize();
    }

    /**
     * 初始化页面
     *
     */
    private void initialize() {
        this.is_admin = NovaClientEnvironment.getInstance().isAdmin();
        this.setRowNumberItemVO(new RowNumberItemVO(NovaConstants.BILLDATAEDITSTATE_INIT, 0)); //
        bordertitle = "[" + templetVO.getTempletname() + "]";
        border = BorderFactory.createTitledBorder(bordertitle); // 创建边框
        border.setTitleFont(new Font("宋体", Font.PLAIN, 12));
        border.setTitleColor(Color.BLACK); //

        this.setLayout(new BorderLayout());
        
        mainPanel=buildMainPanel();       
        
        JScrollPane scrollPanel = new JScrollPane(mainPanel);
        scrollPanel.setBackground(Color.WHITE); //

        scrollPanel.setBorder(border); //
        this.add(scrollPanel, BorderLayout.CENTER);
        this.validate();
        this.updateUI();
        
        //如何获得条件区域的高度的事件
        mainPanel.addComponentListener(new ComponentAdapter(){
			public void componentResized(ComponentEvent e) {
				int oldwidth=mainPanel.getWidth();
				int oldheight=mainPanel.getHeight();
				JPanel[] ps=(JPanel[])v_compents.values().toArray(new JPanel[0]);
				int newheight=NovaPanelUtil.getPanelHeight(oldwidth, ps, 5);
				if(oldheight!=newheight){
					mainPanel.setPreferredSize(new Dimension(oldwidth,newheight));
					mainPanel.invalidate();
					mainPanel.getRootPane().revalidate();
				}
			}
        });
        
    }

    private JPanel buildMainPanel() {
        JPanel mainPanel = new JPanel();
        int li_height = getCardPaneHeight();
        mainPanel.setPreferredSize(new Dimension(510, li_height)); //
        FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
        layout.setHgap(10);
        mainPanel.setLayout(layout);
        if (is_admin) {
            mainPanel.setToolTipText("模板编码[" + templetVO.getTempletcode().toLowerCase() + "],模板名称[" +
                                     templetVO.getTempletname().toLowerCase() + "],对应表名[" + templetVO.getTablename() +
                                     "]");
        }

        for (int i = 0; i < templetItemVOs.length; i++) {
            String str_type = templetItemVOs[i].getItemtype();
            boolean bo_iscardshow = true;
            boolean bo_iscardedit = false; // 初始界面不能编辑!!

            // 控件是否需要显示
            if (templetItemVOs[i].getCardisshowable() != null && !templetItemVOs[i].getCardisshowable().booleanValue() ||
                !chechItemVisible(templetItemVOs[i].getItemkey())) {
                bo_iscardshow = false;
            }

            if (str_type.equals("文本框")) {
                TextFieldPanel panel = new TextFieldPanel(templetItemVOs[i]);
                if (templetItemVOs[i].isPrimaryKey()) { // 如果是主键，不允许直接编辑
                    panel.setEditable(false);
                    panel.setValue("主键自动增长");
                }

                if (bo_iscardshow) {
                    mainPanel.add(panel); //
                    panel.setEditable(bo_iscardedit);
                    final String str_itemkey = templetItemVOs[i].getItemkey();
                    panel.getTextField().addFocusListener(new FocusListener() {

                        public void focusGained(FocusEvent e) {
                        }

                        public void focusLost(FocusEvent e) {
                            onChanged(str_itemkey);
                        }
                    });
                    panel.getTextField().addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            onChanged(str_itemkey);
                        }
                    });
                }
                v_compents.put(panel.getKey().toLowerCase(),panel); // 数据对象列表中加入，便于快速检索                
            } else if (str_type.equals("数字框")) {
                NumberFieldPanel panel = new NumberFieldPanel(templetItemVOs[i]);
                if (bo_iscardshow) {
                    mainPanel.add(panel); //
                    panel.setEditable(bo_iscardedit);
                    final String str_itemkey = templetItemVOs[i].getItemkey();
                    panel.getTextField().addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent e) {
                            onChanged(str_itemkey);
                        }
                    });
                    panel.getTextField().addFocusListener(new FocusListener() {

                        public void focusGained(FocusEvent e) {
                        }

                        public void focusLost(FocusEvent e) {
                            onChanged(str_itemkey);
                        }
                    });
                }
                v_compents.put(panel.getKey().toLowerCase(),panel); // 数据对象列表中加入，便于快速检索
            } else if (str_type.equals("密码框")) {
                PasswordFieldPanel panel = new PasswordFieldPanel(templetItemVOs[i]);
                if (bo_iscardshow) {
                    mainPanel.add(panel); //
                    panel.setEditable(bo_iscardedit);
                    final String str_itemkey = templetItemVOs[i].getItemkey();
                    panel.getTextField().addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent e) {
                            onChanged(str_itemkey);
                        }
                    });
                }
                v_compents.put(panel.getKey().toLowerCase(),panel); // 数据对象列表中加入，便于快速检索
            } else if (str_type.equals("下拉框")) {
                ComBoxPanel panel = new ComBoxPanel(templetItemVOs[i]);
                if (bo_iscardshow) {
                    mainPanel.add(panel); //
                    panel.setEditable(bo_iscardedit);
                    final String str_itemkey = templetItemVOs[i].getItemkey();
                    panel.getComBox().addItemListener(new ItemListener() {

                        public void itemStateChanged(ItemEvent e) {
                            if (e.getStateChange() == ItemEvent.SELECTED) {
                                onChanged(str_itemkey);
                            }
                        }
                    });
                }
                v_compents.put(panel.getKey().toLowerCase(),panel); // 数据对象列表中加入，便于快速检索
            } else if (str_type.equals("参照")) {
                UIRefPanel panel = new UIRefPanel(templetItemVOs[i]); // 创建参照时要将
                panel.setParentCardPanel(this); //
                // BillCardPanel
                // 的句柄带入!!
                if (bo_iscardshow) {
                    mainPanel.add(panel); //
                    panel.setEditable(bo_iscardedit);
                    final String str_itemkey = templetItemVOs[i].getItemkey();
                    panel.addPlutoListener(new NovaEventListener() {

                        public void onValueChanged(NovaEvent _evt) {
                            onChanged(str_itemkey);
                        }
                    });
                }
                v_compents.put(panel.getKey().toLowerCase(),panel); // 数据对象列表中加入，便于快速检索
            } else if (str_type.equals("多行文本框")) {
                TextAreaPanel panel = new TextAreaPanel(templetItemVOs[i]);
                if (bo_iscardshow) {
                    mainPanel.add(panel); //
                    panel.setEditable(bo_iscardedit);
                }
                v_compents.put(panel.getKey().toLowerCase(),panel); // 数据对象列表中加入，便于快速检索
            } else if (str_type.equals("日历")) {
                UIDateTimePanel panel = new UIDateTimePanel(templetItemVOs[i]);
                if (bo_iscardshow) {
                    mainPanel.add(panel); //
                    panel.setEditable(bo_iscardedit);
                }
                v_compents.put(panel.getKey().toLowerCase(),panel); // 数据对象列表中加入，便于快速检索
            } else if (str_type.equals("文件选择框")) {
                UIFilePathPanel panel = new UIFilePathPanel(templetItemVOs[i]);
                panel.setCardPanel(this);
                if (bo_iscardshow) {
                    mainPanel.add(panel); //
                    panel.setEditable(bo_iscardedit);
                }
                v_compents.put(panel.getKey().toLowerCase(),panel); // 数据对象列表中加入，便于快速检索
            } else if (str_type.equals("颜色")) {
                UIColorPanel panel = new UIColorPanel(templetItemVOs[i]);
                if (bo_iscardshow) {
                    mainPanel.add(panel); //
                    panel.setEditable(bo_iscardedit);
                }
                v_compents.put(panel.getKey().toLowerCase(),panel); // 数据对象列表中加入，便于快速检索
            } else if (str_type.equals("大文本框")) {
                UITextAreaPanel panel = new UITextAreaPanel(templetItemVOs[i]);
                if (bo_iscardshow) {
                    mainPanel.add(panel); //
                    panel.setEditable(bo_iscardedit);
                }
                v_compents.put(panel.getKey().toLowerCase(),panel); // 数据对象列表中加入，便于快速检索
            } else if (str_type.equals("计算器")) {
                UICalculatorPanel panel = new UICalculatorPanel(templetItemVOs[i]);
                mainPanel.add(panel);
                v_compents.put(panel.getKey().toLowerCase(),panel); // 数据对象列表中加入，便于快速检索
            } else if (str_type.equals("时间")) {
                UITimeSetPanel panel = new UITimeSetPanel(templetItemVOs[i]);
                if (bo_iscardshow) {
                    mainPanel.add(panel); //
                    panel.setEditable(bo_iscardedit);
                }
                v_compents.put(panel.getKey().toLowerCase(),panel); // 数据对象列表中加入，便于快速检索
            } else if (str_type.equals("勾选框")) {
                UICheckBoxPanel panel = new UICheckBoxPanel(templetItemVOs[i]);
                if (bo_iscardshow) {
                    mainPanel.add(panel); //
                    panel.setEditable(bo_iscardedit);
                }
                v_compents.put(panel.getKey().toLowerCase(),panel); // 数据对象列表中加入，便于快速检索
            } else if (str_type.equals("图片选择框")) {
                UIImagePanel panel = new UIImagePanel(templetItemVOs[i]);
                if (bo_iscardshow) {
                    mainPanel.add(panel); //
                    panel.setEditable(bo_iscardedit);
                }
                v_compents.put(panel.getKey().toLowerCase(),panel); // 数据对象列表中加入，便于快速检索
            } else {

            }

        }
        if (is_admin) {
            mainPanel.addMouseListener(new MouseAdapter() {

                public void mousePressed(MouseEvent evt) {
                    if (evt.getButton() == MouseEvent.BUTTON3) {
                        showPopMenu(evt.getComponent(), evt.getX(), evt.getY()); // 弹出菜单
                    }
                }

                public void mouseReleased(MouseEvent e) {
                }
            });
        }
        return mainPanel;
    }

    private void showPopMenu(Component _compent, int _x, int _y) {
        JPopupMenu popmenu_header = new JPopupMenu();
        JMenuItem menu_table_showRecord = new JMenuItem("查看数据库数据");

        JMenu menu_table_templetmodify = new JMenu("快速模板编辑");
        JMenuItem item_table_templetmodify_1 = new JMenuItem("第一种编辑方式");
        JMenuItem item_table_templetmodify_2 = new JMenuItem("第二种编辑方式");

        menu_table_templetmodify.add(item_table_templetmodify_1); //
        menu_table_templetmodify.add(item_table_templetmodify_2); //

        if (!is_admin) {
            menu_table_templetmodify.setEnabled(false);
        }

        menu_table_showRecord.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (templetVO.getPkname() == null || templetVO.getPkname().length() <= 0) {
                    try {
                        String[] allValues = new String[templetVO.getRealViewItemVOs().length];
                        for (int i = 0; i < templetVO.getRealViewItemVOs().length; i++) {
                            allValues[i] = String.valueOf(getRealValueAt(templetVO.getItemKeys()[i]));
                        }
                        new RecordShowDialog(BillCardPanel.this, templetVO.getTablename(), templetVO.getItemKeys(),
                                             allValues);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        NovaMessage.show(BillCardPanel.this, "该表在数据库中不存在！", NovaConstants.MESSAGE_ERROR);
                    }
                } else {
                    try {
                        new RecordShowDialog(BillCardPanel.this, templetVO.getTablename(), templetVO.getPkname(),
                                             getCompentRealValue(templetVO.getPkname()));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        NovaMessage.show(BillCardPanel.this, "该表在数据库中不存在！", NovaConstants.MESSAGE_ERROR);
                    }
                }
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

        popmenu_header.add(menu_table_templetmodify); // 快速模板编辑
        popmenu_header.add(menu_table_showRecord);
        popmenu_header.show(_compent, _x, _y); //
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

    private boolean chechItemVisible(String itemkey) {
        if (unvisibleitem == null || unvisibleitem.size() == 0) {
            return true;
        }
        for (int i = 0; i < unvisibleitem.size(); i++) {
            if ( ( (String) unvisibleitem.get(i)).equalsIgnoreCase(itemkey)) {
                return false;
            }
        }
        return true;
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

        li_height = li_height - 20;
        return li_height / 2; // + li_textareacount * 60;
    }

    public String getSQL(String _condition) {
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
                str_return = "select * from " + templetVO.getTablename() + " where (" + str_constraintFilterCondition +
                    ")";
            } else {
                str_return = "select * from " + templetVO.getTablename() + " where (" + str_constraintFilterCondition +
                    ") and (" + _condition + ")"; // 把RowID加上!!
            }
        }

        return str_return;
    }

    /**
     * 刷新数据
     *
     * @param _condition
     */
    public void refreshData(String _condition) {
        queryData(getSQL(_condition)); //
    }

    public void queryDataByCondition(String _condition) {
        queryData(getSQL(_condition)); //
    }

    public String getDataSourceName() {
        if (templetVO.getDatasourcename() == null || templetVO.getDatasourcename().trim().equals("null") ||
            templetVO.getDatasourcename().trim().equals("")) {
            return NovaClientEnvironment.getInstance().getDefaultDatasourceName(); // 默认数据源
        } else {
            return new FrameWorkTBUtil().convertExpression(NovaClientEnvironment.getInstance(),
                templetVO.getDatasourcename()); // 算出数据源!!
        }
    }

    /**
     * 得到数据过滤条件
     * @return
     */
    public String getDataconstraint() {
        if (templetVO.getDataconstraint() == null || templetVO.getDataconstraint().trim().equals("null") ||
            templetVO.getDataconstraint().trim().equals("")) {
            return null; // 默认数据源
        } else {
            return new FrameWorkTBUtil().convertExpression(NovaClientEnvironment.getInstance(),
                templetVO.getDataconstraint()); // 算出数据源!!
        }
    }

    /**
     * 默认取数数!
     * @param _sql
     */
    public void queryData(String _sql) {
        queryDataByDS(getDataSourceName(), _sql); //
    }

    /**
     * 到指定数据源中取数!
     * @param _dsName
     * @param _sql
     */
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
            setValue(objs); // 设置数据,包括行号中的RowID!!
        }

        this.getRowNumberItemVO().setState(NovaConstants.BILLDATAEDITSTATE_INIT); // 设置为初始状态
        this.border.setTitleColor(Color.BLACK); // 设置边框颜色
        this.border.setTitle("[" + templetVO.getTempletname() + "]"); // 设置模板名称!!
        this.updateUI(); //
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

    /**
     * 初始化界面，清空控件
     */
    public void reset() {
        this.setEditState(NovaConstants.BILLDATAEDITSTATE_INIT); // 重置行号VO!!
        INovaCompent[] compents = (INovaCompent[]) v_compents.values().toArray(new INovaCompent[0]);
        for (int i = 0; i < compents.length; i++) {
            compents[i].reset(); // 设置值
        }
    }

    public void setEditable(boolean _bo) {
        INovaCompent[] compents = (INovaCompent[]) v_compents.values().toArray(new INovaCompent[0]);
        for (int i = 0; i < compents.length; i++) {
            if (!compents[i].getKey().equalsIgnoreCase(this.templetVO.getPkname())) {
                compents[i].setEditable(_bo); // 设置值
            }
        }
    }

    public void setEditable(String _itemkey, boolean _bo) {
        INovaCompent[] compents = (INovaCompent[]) v_compents.values().toArray(new INovaCompent[0]);
        for (int i = 0; i < compents.length; i++) {
            if (compents[i].getKey().equalsIgnoreCase(_itemkey)) {
                compents[i].setEditable(_bo); // 设置值
            }
        }
    }

    public void setVisiable(String _itemkey, boolean _bo) {
        Pub_Templet_1_ItemVO itemVO = getTempletItemVO(_itemkey);
        if (itemVO != null) {
            itemVO.setCardisshowable(new Boolean(_bo));
            reInitialize(); //
        }
    }

    public void setBorderTitle(String _title) {
        if (_title != null) {
            bordertitle = "[" + _title + "]";
            border.setTitle(bordertitle);
        }
    }

    public String getInsertSQL() {
        return this.getBillVO().getInsertSQL();
    }

    public String getUpdateSQL() {
        return this.getBillVO().getUpdateSQL();
    }

    
    /**
     * 返回UI拦截器对象
     * @return UI拦截器对象，不会为空。
     * @throws Exception
     */
    private UICardInterceptIFC getUIInterceptImpl()throws Exception{
    	if(this.uiInterceptImpl==null){
    		if(this.uiIntercept==null){
    			//没有设置uiIntercept，启用默认处理
    			this.uiInterceptImpl=new DefaultUICardIntercept();
        		return this.uiInterceptImpl;
        	}
    		
    		try {
				this.uiInterceptImpl=(UICardInterceptIFC)Class.forName(this.uiIntercept).newInstance();
			} catch (InstantiationException e) {
				throw new Exception("不能创建指定的拦截器！");
			} catch (IllegalAccessException e) {
				throw new Exception("不能创建指定的拦截器！");
			} catch (ClassNotFoundException e) {
				throw new Exception("拦截器指定的类不存在！");
			}
    	}
    	return this.uiInterceptImpl;
    }
    
    /**
     * 保存数据
     * @throws Exception 提示异常
     */
    public void saveData()throws Exception{
    	// 界面验证合法性 新增和更新校验
    	if(this.getRowNumberItemVO().getState().equals(NovaConstants.BILLDATAEDITSTATE_INSERT)
    			|| this.getRowNumberItemVO().getState().equals(NovaConstants.BILLDATAEDITSTATE_UPDATE)){
    		checkInputValidate();
    	}

    	//根据需要调用提交保存
    	BillVO vo = getBillVO(); //
		//UI插入前拦截
    	getUIInterceptImpl().actionBefore(this);
		//提交处理
		getService().doCardSave(getDataSourceName(), this.bsIntercept, vo); //
		//UI插入后拦截
		getUIInterceptImpl().actionAfter(this);
		
    }
    
    /**
     * 获得后台提交服务接口
     * @return
     * @throws Exception
     */
    protected BillOperateServiceIFC getService() throws Exception {
    	BillOperateServiceIFC service = (BillOperateServiceIFC) NovaRemoteServiceFactory.getInstance().lookUpService(
    			BillOperateServiceIFC.class);
        return service;
    }
    
    public void updateData() {
        String str_sql = getUpdateSQL();
        try {
            UIUtil.executeUpdateByDS(str_templetcode, str_sql); //有问题!!
            NovaMessage.show(this, NovaConstants.STRING_OPERATION_SUCCESS, NovaConstants.MESSAGE_INFO);
        } catch (Exception e) {
            NovaMessage.show(this, NovaConstants.STRING_OPERATION_FAILED + ":" + e.getMessage(), NovaConstants.MESSAGE_ERROR);
        }
        
        
        
        
    }
    
    
    
    /**
     * 控制记录修改版本
     */
    public void updateVersion(){
    	
    	Object ver =  this.getRealValueAt("VERSION");
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
    	this.setValueAt("VERSION", ver);
    }

    // 新增一行!!
    /**
     * @deprecated 调用createNewRecord
     */
    public void newRow2() {
        createNewRecord(); //
    }

    // 新增一行!!
    /**
     * @deprecated 调用createNewRecord
     */
    public void addRow2() {
        createNewRecord(); //
    }

    // 新增一行!!
    /**
     * @deprecated 调用createNewRecord
     */
    public void insertRow2() {
        createNewRecord(); //
    }

    /**
     * 新增一条记录
     */
    public void createNewRecord() {
        reset(); // 先清空数据

        this.border.setTitleColor(Color.GREEN); //
        this.border.setTitle(bordertitle); //

        this.setEditState(NovaConstants.BILLDATAEDITSTATE_INSERT); // 设置行号VO,状态处于新增状态!!!这里rowId为空!!

        // 设置处理主键值!!!!
        String str_sequencename = templetVO.getPksequencename(); //
        if (str_sequencename == null || str_sequencename.trim().equals("")) {
            // JOptionPane.showMessageDialog(this, "没有定义序列名,无法为主键项设值!!"); //
        } else {
            if (templetVO.getPkname() != null) {
                INovaCompent compent = getCompentByKey(templetVO.getPkname());
                if (compent == null) {
                    //JOptionPane.showMessageDialog(this, "面板中没有ItemKey等于主键名[" + templetVO.getPkname() + "]的组件!!");
                } else {
                    try {
                        String sequenceValue = UIUtil.getSequenceNextValByDS(getDataSourceName(), str_sequencename);
                        compent.setObject(sequenceValue); //设置主键值!!!
                    } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(this, "创建序列[" + str_sequencename + "]的nextVal失败!" + e.getMessage());
                    } //
                }
            }

        }

        // 处理默认值公式!!!!
        execDefaultValueFormula(); // //执行加载公式

        // this.setEditState(BillCardPanel.INSERT_SATE); //设置单据状态!!!
        this.updateUI(); //
    }

    /**
     * 修改当前行记录!!!!
     *
     */
    public void updateCurrRow() {
        this.getRowNumberItemVO().setState(NovaConstants.BILLDATAEDITSTATE_UPDATE); //
        border.setTitleColor(Color.BLUE); // 处于蓝色状态
        border.setTitle(bordertitle); //

        // 设置各控件是否可编辑!!!!!
        for (int i = 0; i < this.templetItemVOs.length; i++) {
            INovaCompent compent = getCompentByKey(templetItemVOs[i].getItemkey());
            if (compent != null) {
                if (templetItemVOs[i].getItemkey().equalsIgnoreCase(templetVO.getPkname())) {
                    compent.setEditable(false); // 如果是主键,则始终不能编辑!!!
                } else {
                    if (templetItemVOs[i].getCardiseditable().equals("1") ||
                        templetItemVOs[i].getCardiseditable().equals("3")) {
                        compent.setEditable(true);
                    } else {
                        compent.setEditable(false);
                    }
                }
            }
        } //

        this.updateUI(); //
    }

    /**
     * 修改当前行记录!!!!
     *
     */
    public void initCurrRow() {
        this.getRowNumberItemVO().setState(NovaConstants.BILLDATAEDITSTATE_INIT); //
        border.setTitleColor(Color.BLACK); // 处于蓝色状态
        border.setTitle(bordertitle); //

        // 设置各控件是否可编辑!!!!!
        for (int i = 0; i < this.templetItemVOs.length; i++) {
            INovaCompent compent = getCompentByKey(templetItemVOs[i].getItemkey());
            compent.setEditable(false);
        } //

        this.updateUI(); //
    }

    public Pub_Templet_1_ItemVO getTempletItemVO(String key) {
        for (int i = 0; i < templetItemVOs.length; i++) {
            if (templetItemVOs[i].getItemkey().equalsIgnoreCase(key)) {
                return templetItemVOs[i];
            }
        }
        return null;
    }

    /**
     * 获得组件对象值
     * @param _key
     * @return
     */
    public Object getCompentObjectValue(String _key) {        
    	INovaCompent cmp=(INovaCompent)v_compents.get(_key.toLowerCase());
    	return (cmp==null)?null:cmp.getObject();
    }

    /**
     * 获得返回值对象
     * @param _key
     * @return Object 字段对象
     */
    public Object getValueAt(String _key) {
        return getCompentObjectValue(_key);
    }

    /**
     * 取得真实值
     * @param _key
     * @return String 界面实际录入值
     */
    public String getRealValueAt(String _key) {
        return getCompentRealValue(_key);
    }
    
    /**
     * 获得对象真实值
     * @param _key
     * @return
     */
    public String getCompentRealValue(String _key) {
    	INovaCompent cmp=(INovaCompent)v_compents.get(_key.toLowerCase());
    	return (cmp==null)?null:cmp.getValue();    	
    }

    /**
     * 设置真正的值
     *
     * @param _key
     * @param _value
     */
    public void setRealValueAt(String _key, String _value) {
        INovaCompent compent = getCompentByKey(_key);
        compent.reset(); //

        Pub_Templet_1_ItemVO itemVO = getTempletItemVO(_key);
        String str_type = itemVO.getItemtype();
        if (str_type.equals("文本框")) {
            setCompentObjectValue(_key, _value);
        } else if (str_type.equals("数字框")) {
            if (_value.endsWith(".0")) {
                setCompentObjectValue(_key, _value.substring(0, _value.length()-2)); //
            } else {
                setCompentObjectValue(_key, _value); //
            }
        } else if (str_type.equals("下拉框")) {
            ComBoxPanel comBoxPanel = (ComBoxPanel) getCompentByKey(_key); //
            JComboBox comboBox = comBoxPanel.getComBox(); // 取得下拉框
            for (int i = 0; i < comboBox.getItemCount(); i++) { // 遍历!!
                ComBoxItemVO vo = (ComBoxItemVO) comboBox.getItemAt(i);
                if (vo.getId().equals(_value)) {
                    comboBox.setSelectedIndex(i);
                    break;
                }
            }
        } else if (str_type.equals("参照")) { // 应该去找出其SQL定义然后检索后创建RefItemVO!!
            String str_reftype = itemVO.getRefdesc_type();
            if (str_reftype!=null && (str_reftype.equals("TABLE") || str_reftype.equals("TREE"))) { // 如果是表型或树型参照
                String str_refdescsql = itemVO.getRefdesc_realsql();
                FrameWorkTBUtil tbUtil = new FrameWorkTBUtil(); //
                String str_newsql = null;
                try {
                    str_newsql = tbUtil.convertFormulaMacPars(str_refdescsql, NovaClientEnvironment.getInstance(),
                        this.getAllObjectValuesWithHashMap());
                } catch (Exception e) {
                    System.out.println("设置默认值公式时,参照[" + itemVO.getItemkey() + "][" + itemVO.getItemname() + "]的SQL[" +
                                       str_refdescsql + "]转换转换失败!!"); //
                    e.printStackTrace();
                }

                if (str_newsql != null) {
                    String str_refidFieldName = itemVO.getRefdesc_firstColName();
                    String str_newsql2 = StringUtil.replaceAll(str_newsql, "1=1", str_refidFieldName + "='" + _value + "'"); //
                    System.out.println("真正执行参照定义的SQL:" + str_newsql2); //
                    HashVO[] vos = null;
                    try {
                        vos = UIUtil.getHashVoArrayByDS(getDataSourceName(), str_newsql2); //
                    } catch (NovaRemoteException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } //
                    if (vos != null && vos.length > 0) {
                        RefItemVO refVO = new RefItemVO(vos[0]); //
                        setCompentObjectValue(_key, refVO); // 设置参照的值!!!
                    }
                }
            } else {
                setCompentObjectValue(_key, new RefItemVO(_value, _value, _value)); //
            }
        } else {
            setCompentObjectValue(_key, _value);
        }
    }

    /**
     * 获得组件真实值的名值对照
     * @return HashMap
     */
    public HashMap getAllRealValuesWithHashMap() {
        HashMap map = new HashMap();
        for (int i = 0; i < templetItemVOs.length; i++) {
        	map.put(templetItemVOs[i].getItemkey(), getRealValueAt(templetItemVOs[i].getItemkey()));
        }
        return map;
    }

    /**
     * 获得组件值对象列表
     * @return
     */
    public Object[] getAllObjectValues() {
        Object[] objs = new Object[templetItemVOs.length];
        for (int i = 0; i < objs.length; i++) {
            objs[i] = getCompentObjectValue(templetItemVOs[i].getItemkey()); // 取得所有对象
        }
        return objs;
    }

    /**
     * 获得字段的名称和值对照
     * @return HashMap
     */
    public HashMap getAllObjectValuesWithHashMap() {
        HashMap map = new HashMap();
        for (int i = 0; i < templetItemVOs.length; i++) {
        	map.put(templetItemVOs[i].getItemkey(), getObjectValue(templetItemVOs[i].getItemkey()));            
        }
        return map;
    }
    
    private Object getObjectValue(String key){
    	if (key.equalsIgnoreCase("version")) {
        	Object ver=getCompentObjectValue("version");
    		if (ver == null){
    			return new Integer(1);
    		}else{
    			try{
    				return new Integer((new Integer(ver.toString())).intValue()+1);
    			}catch(Exception e){
    				return new Integer(1);
    			}
            }
        } else {
            return getCompentObjectValue(key);
        }
    }

    /**
     * 获得字段的值
     * @return VectorMap
     */
    public VectorMap getAllObjectValuesWithVectorMap() {
        VectorMap map = new VectorMap();
        for (int i = 0; i < templetItemVOs.length; i++) {
        	map.put(templetItemVOs[i].getItemkey(), getObjectValue(templetItemVOs[i].getItemkey()));
        }
        
        return map;
    }

    /**
     * 设置Card内的控件取值（清空后赋值）
     * @param _objs
     * @deprecated 使用此方法设置控件取值，容易引起错误。
     */
    public void setValue(Object[] _objs) {
        reset(); // 重置所有控件
        this.setRowNumberItemVO( (RowNumberItemVO) _objs[0]); // 设置行号数据VO!!!
        for (int i = 1; i < _objs.length; i++) {
            INovaCompent compent = getCompentByIndex(i - 1);
            if (compent != null) {
                compent.setObject(_objs[i]);
            }
        }
    }

    
    /**
     * 设置Card内的控件取值（清空后赋值）
     * @param _map
     */
    public void setValue(HashMap _map) {
        reset();
        if (_map == null) {return;}
        String[] keys=(String[])_map.keySet().toArray(new String[0]);
        for (int i = 0; i < keys.length; i++) {
        	setCompentObjectValue(keys[i],_map.get(keys[i]));        	
        }
    }

    /**
     * 设置Card内的控件取值（清空后赋值）
     * @param _billVO
     */
    public void setBillVO(BillVO _billVO) {
        reset();
        String[] keys=_billVO.getKeys();        
        for (int i = 0; i < keys.length; i++) {
        	setCompentObjectValue(keys[i],_billVO.getObject(keys[i]));
        }
    }

    /**
     * 设置Card内控件取值（清空后赋值）
     * @param _vo
     */
    public void setValue(HashVO _vo) {
        reset();
        String[] keys=_vo.getKeys();
        for (int i = 0; i < keys.length; i++) {
        	setCompentObjectValue(keys[i],_vo.getObjectValue(keys[i]));            
        }
    }

    
    /**
     * 设置Card内的指定组件的取值
     * @param _key
     * @param _obj
     */
    public void setCompentObjectValue(String _key, Object _obj) {
        INovaCompent compent = getCompentByKey(_key);
        if(compent==null)return;
        compent.setObject(_obj); //
    }

    /**
     * 设置Card内的指定组件的取值
     * @param _key
     * @param _obj
     */
    public void setValueAt(String _key, Object _obj) {
        setCompentObjectValue(_key, _obj);
    }
    
    
    

    public INovaCompent[] getAllCompents() {
        return (INovaCompent[]) v_compents.values().toArray(new INovaCompent[0]);
    }

    public INovaCompent getCompentByIndex(int _index) {
        String key = templetItemVOs[_index].getItemkey();
        return getCompentByKey(key);
    }

    public INovaCompent getCompentByKey(String _key) {
        return (INovaCompent)v_compents.get(_key.toLowerCase());
    }

    /**
     * 取得某一项的所有公式,它是一个数组
     *
     * @param _itemkey
     * @return
     */
    public String[] getEditFormulas(String _itemkey) {
        for (int i = 0; i < this.templetItemVOs.length; i++) {
            if (templetItemVOs[i].getItemkey().equalsIgnoreCase(_itemkey)) {
                String str_formula = templetItemVOs[i].getEditformula();
                if (str_formula != null && !str_formula.trim().equals("null") && !str_formula.trim().equals("")) {
                    return str_formula.trim().split(";");
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    private synchronized void onChanged(String _itemkey) {
        if (bo_isallowtriggereditevent) { // 如果允许触发
            bo_isallowtriggereditevent = false;

            Object obj = this.getCompentObjectValue(_itemkey); // 取得当前值

            execEditFormula(_itemkey); // 执行编辑公式..它与下一行代码谁先谁后还有待进一步考虑!!!!!!

            for (int i = 0; i < v_listeners.size(); i++) {
                NovaEventListener listener = (NovaEventListener) v_listeners.get(i);
                listener.onValueChanged(new NovaEvent(NovaEvent.CardChanged, _itemkey, obj, this)); //
            }

            //光标跳到下一个!
            bo_isallowtriggereditevent = true;
        }
    }

    /**
     * 注册事件
     *
     * @param _listener
     */
    public void addNovaEventListener(NovaEventListener _listener) {
        v_listeners.add(_listener); //
    }

    /**
     * 执行某一项的加载公式!
     */
    public void execEditFormula(String _itemkey) {
        String[] str_editformulas = getEditFormulas(_itemkey); //
        if (str_editformulas == null || str_editformulas.length == 0) {
            return;
        }

        // 循环处理公式...
        for (int i = 0; i < str_editformulas.length; i++) {
            dealFormula(str_editformulas[i]); // 真正执行公式!!!
        }
    }

    /**
     * 执行默认值方式
     *
     */
    private void execDefaultValueFormula() { // 目前直接的字符串或者{Date(10)},{Date(19)}
        for (int i = 0; i < templetItemVOs.length; i++) {
            Pub_Templet_1_ItemVO item = templetItemVOs[i];
            String formula = item.getDefaultvalueformula();

            if (formula != null && !formula.trim().equals("")) {
                String modify_formula = null;
                try {
                    modify_formula = new FrameWorkTBUtil().convertFormulaMacPars(formula.trim(),
                        NovaClientEnvironment.getInstance(), this.getAllObjectValuesWithHashMap());
                } catch (Exception e) {
                    System.out.println("执行[" + item.getItemkey() + "][" + item.getItemname() + "]的默认值公式:[" + formula + "]失败!!!!");
                    //e.printStackTrace(); //
                }

                if (modify_formula != null) {
                    String str_value = JepFormulaUtil.getJepFormulaValue(modify_formula,JepFormulaParse.li_ui); // 真正执行转换后的公式!!//
                    System.out.println("执行默认值公式:[" + formula + "],转换后[" + modify_formula + "],执行结果[" + str_value + "]");
                    // this.setCompentObjectValue(tempitem.getItemkey(),
                    // str_value); //设置控件值,这里应该是送Object!!有待进一步改进!!
                    this.setRealValueAt(item.getItemkey(), str_value); //
                }
            }
        }
    } //

    /**
     * 真正执行某一个公式..使用JEP去执行!!
     *
     * @param _formula
     */
    private void dealFormula(String _formula) {
        String str_formula = UIUtil.replaceAll(_formula, " ", "");
        System.out.println("执行公式:[" + str_formula + "]"); //
        int li_pos = str_formula.indexOf("=>");
        String str_prefix = str_formula.substring(0, li_pos);
        String str_subfix = str_formula.substring(li_pos + 2, str_formula.length());
//        System.out.println("前辍:[" + str_prefix + "]"); //
//        System.out.println("后辍:[" + str_subfix + "]"); //

        String modify_formula = null;
        try {
            modify_formula = new FrameWorkTBUtil().convertFormulaMacPars(str_subfix, NovaClientEnvironment.getInstance(),
                this.getAllObjectValuesWithHashMap());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "执行编辑公式[" + _formula + "]失败!!!"); //
            return;
        }
        if (modify_formula != null) { // 如果转换成功
            System.out.println("新后辍:[" + modify_formula + "],即Jep执行的公式!!"); //
            Pub_Templet_1_ItemVO itemvo = this.getTempletItemVO(str_prefix);
        	String type = itemvo.getItemtype();
            if(str_subfix.equalsIgnoreCase("refresh()"))
            {
            	if(type.equals("下拉框"))
            	{
            		try {
            			ComBoxItemVO[] comboxs= ((FrameWorkMetaDataService)NovaRemoteServiceFactory.getInstance().lookUpService(FrameWorkMetaDataService.class)).resetComBoxItemvo(itemvo, NovaClientEnvironment.getInstance());
            			itemvo.setComBoxItemVos(comboxs);
            			((ComBoxPanel)this.getCompentByKey(itemvo.getItemkey())).setItemVOs(comboxs);
            			setValueAt(str_prefix,comboxs[0]);
            		} catch (NovaRemoteException e) {
    					e.printStackTrace();
    				} catch (Exception e) {
    					e.printStackTrace();
    				}
            	}
            	return; //
            }
            if(str_subfix.equalsIgnoreCase("reset()"))
            {
            	if(type.equals("下拉框"))
            	{
            			setValueAt(str_prefix,itemvo.getComBoxItemVos()[0]);
            	}
            	else
            		this.setValueAt("", str_prefix);
            	return ; //
            }
            if(str_subfix.equalsIgnoreCase("enable()"))
            {
            	this.getCompentByKey(str_prefix).setEditable(true);
            	return ; //
            }
            if(str_subfix.equalsIgnoreCase("unable()"))
            {
            	this.getCompentByKey(str_prefix).setEditable(false);
            	return ; //
            }
            String str_subfix_new_value = JepFormulaUtil.getJepFormulaValue(modify_formula,JepFormulaParse.li_ui); //
            System.out.println(str_prefix + "->" + str_subfix_new_value);

            // this.setCompentObjectValue(str_prefix, str_subfix_new_value); //
            setRealValueAt(str_prefix, str_subfix_new_value);
        }
    }

    

    /**
     * 取得行号数据VO
     *
     * @return
     */
    public RowNumberItemVO getRowNumberItemVO() {
        return rowNumberItemVO; //
    }

    /**
     * 设置行号数据VO
     *
     * @param rowNumberItemVO
     */
    public void setRowNumberItemVO(RowNumberItemVO rowNumberItemVO) {
        this.rowNumberItemVO = rowNumberItemVO;
    }

    public void setAllUnable() {
    	String[] keys=(String[])v_compents.keySet().toArray(new String[0]);
        for (int i = 0; i < keys.length; i++) {
            ( (INovaCompent) v_compents.get(keys[i])).setEditable(false);
        }
    }

    /**
     * 快速取得当前状态!
     *
     * @return
     */
    public String getEditState() {
        return rowNumberItemVO.getState(); //
    }

    public void setEditState(String _state) {
        if (_state.equalsIgnoreCase(NovaConstants.BILLDATAEDITSTATE_INIT)) {
            this.rowNumberItemVO.setState(_state);
            this.border.setTitleColor(Color.BLACK); //
            this.updateUI();
        } else if (_state.equalsIgnoreCase(NovaConstants.BILLDATAEDITSTATE_INSERT)) {
            this.rowNumberItemVO.setState(_state);
            this.setEditable(true);

            // 设置各控件是否可编辑!!!
            for (int i = 0; i < this.templetItemVOs.length; i++) {
                INovaCompent compent = getCompentByKey(templetItemVOs[i].getItemkey());
                if (compent != null) {
                    if (templetItemVOs[i].getItemkey().equalsIgnoreCase(templetVO.getPkname())) {
                        compent.setEditable(false); // 如果是主键,则始终不能编辑!!!
                    } else {
                        if (templetItemVOs[i].getCardiseditable().equals("1") ||
                            templetItemVOs[i].getCardiseditable().equals("2")) {
                            compent.setEditable(true);
                        } else {
                            compent.setEditable(false);
                        }
                    }
                }
            } //

            this.border.setTitleColor(Color.GREEN); //
            this.updateUI();
        } else if (_state.equalsIgnoreCase(NovaConstants.BILLDATAEDITSTATE_UPDATE)) {
            this.rowNumberItemVO.setState(_state);
            this.setEditable(true);

            // 设置各控件是否可编辑!!!
            for (int i = 0; i < this.templetItemVOs.length; i++) {
                INovaCompent compent = getCompentByKey(templetItemVOs[i].getItemkey());
                if (compent != null) {
                    if (templetItemVOs[i].getItemkey().equalsIgnoreCase(templetVO.getPkname())) {
                        compent.setEditable(false); // 如果是主键,则始终不能编辑!!!
                    } else {
                        if (templetItemVOs[i].getCardiseditable().equals("1") ||
                            templetItemVOs[i].getCardiseditable().equals("3")) {
                            compent.setEditable(true);
                        } else {
                            compent.setEditable(false);
                        }
                    }
                }
            } //

            this.border.setTitleColor(Color.BLUE); //
            this.updateUI();
        } else if (_state.equalsIgnoreCase(NovaConstants.BILLDATAEDITSTATE_DELETE)) {
            this.rowNumberItemVO.setState(_state);
            this.setEditable(false);
            // 设置各控件是否可编辑!!!
            for (int i = 0; i < this.templetItemVOs.length; i++) {
                INovaCompent compent = getCompentByKey(templetItemVOs[i].getItemkey());
                if (compent != null) {
                    if (templetItemVOs[i].getItemkey().equalsIgnoreCase(templetVO.getPkname())) {
                        compent.setEditable(false); // 如果是主键,则始终不能编辑!!!
                    } else {
                        if (templetItemVOs[i].getCardiseditable().equals("1") ||
                            templetItemVOs[i].getCardiseditable().equals("2")) {
                            compent.setEditable(true);
                        } else {
                            compent.setEditable(false);
                        }
                    }
                }
            } //
            this.border.setTitleColor(Color.BLUE); //
            this.updateUI();
        } else {
            NovaMessage.show("未知状态，请检查!", NovaConstants.MESSAGE_ERROR);
        }
    }

    /**
     * 得到当前数据
     *
     * @return
     */
    public BillVO getBillVO() {
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

        Object[] allObjs = getAllObjectValues();
        Object[] newObjs = new Object[allObjs.length + 1]; //
        newObjs[0] = getRowNumberItemVO(); //
        for (int i = 0; i < allObjs.length; i++) {
            newObjs[i + 1] = allObjs[i];
        }
        vo.setDatas(newObjs); //
        return vo; //
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
    private void checkInputValidate() throws Exception {
        String[] str_keys = this.getTempletVO().getItemKeys(); //所有的key
        String[] str_names = this.getTempletVO().getItemNames(); //所有的Name
        boolean[] bo_isMustInputs = this.getTempletVO().getItemIsMustInputs(); //是否必输入

        for (int i = 0; i < str_keys.length; i++) {
            if (bo_isMustInputs[i]) { //如果是必输项!!
                Object obj = getCompentObjectValue(str_keys[i]);
                if (obj == null) {
                    getCompentByKey(str_keys[i]).focus(); //
                    throw new Exception(this.getTempletVO().getTempletname()+"的"+str_names[i]+"不能为空！");
                } else {
                    if (obj instanceof String) {
                        String new_name = (String) obj;
                        if (new_name.trim().equals("")) {
                            getCompentByKey(str_keys[i]).focus(); //
                            throw new Exception(this.getTempletVO().getTempletname()+ "的"+str_names[i]+"不能为空！");                            
                        }
                    }
                }
            }
        }
    }
    

    /**
     * 非空校验,
     * @return
     */
    private boolean checkIsNullValidate() {
        String[] str_keys = this.getTempletVO().getItemKeys(); //所有的key
        String[] str_names = this.getTempletVO().getItemNames(); //所有的Name
        boolean[] bo_isMustInputs = this.getTempletVO().getItemIsMustInputs(); //是否必输入

        for (int i = 0; i < str_keys.length; i++) {
            if (bo_isMustInputs[i]) { //如果是必输项!!
                Object obj = getCompentObjectValue(str_keys[i]);
                if (obj == null) {
                    JOptionPane.showMessageDialog(this, this.getTempletVO().getTempletname()+"的"+str_names[i]+"不能为空！"); //
                    getCompentByKey(str_keys[i]).focus(); //
                    return false;
                } else {
                    if (obj instanceof String) {
                        String new_name = (String) obj;
                        if (new_name.trim().equals("")) {
                            JOptionPane.showMessageDialog(this, this.getTempletVO().getTempletname()+ "的"+str_names[i]+"不能为空！"); //
                            getCompentByKey(str_keys[i]).focus(); //
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    /**
     * 取得加载这个billListPanel的Frame,比如是各种风格模板!!
     *
     * @return
     */
    public Component getLoadedFrame() {
        return loadedFrame;
    }

    /**
     * 设置加载这个billListPanel的Frame,比如是各种风格模板!!
     *
     * @param loadedFrame
     */
    public void setLoadedFrame(Component loadedFrame) {
        this.loadedFrame = loadedFrame;
    }

    public Pub_Templet_1VO getTempletVO() {
        return templetVO;
    }

    public void setTempletVO(Pub_Templet_1VO templetVO) {
        this.templetVO = templetVO;
    }

    public boolean containsItemKey(String _itemKey) {
        return this.getTempletVO().containsItemKey(_itemKey);
    }
}
/*******************************************************************************
 * $RCSfile: BillCardPanel.java,v $ $Revision: 1.9.2.19 $ $Date: 2010/02/21 02:07:58 $
 *
 * $Log: BillCardPanel.java,v $
 * Revision 1.9.2.19  2010/02/21 02:07:58  wangqi
 * *** empty log message ***
 *
 * Revision 1.9.2.18  2010/01/15 04:33:02  wangqi
 * *** empty log message ***
 *
 * Revision 1.9.2.17  2010/01/04 08:45:58  wangqi
 * *** empty log message ***
 *
 * Revision 1.9.2.16  2009/12/28 09:32:15  wangqi
 * *** empty log message ***
 *
 * Revision 1.9.2.15  2009/12/16 04:14:01  wangqi
 * *** empty log message ***
 *
 * Revision 1.9.2.14  2009/12/04 07:06:28  wangqi
 * *** empty log message ***
 *
 * Revision 1.9.2.13  2009/07/23 09:39:16  wangqi
 * *** empty log message ***
 *
 * Revision 1.9.2.12  2009/07/23 09:36:00  wangqi
 * *** empty log message ***
 *
 * Revision 1.9.2.11  2009/07/23 09:34:19  wangqi
 * *** empty log message ***
 *
 * Revision 1.9.2.10  2009/02/05 12:02:43  wangqi
 * *** empty log message ***
 *
 * Revision 1.9.2.9  2009/02/02 16:12:54  wangqi
 * *** empty log message ***
 *
 * Revision 1.9.2.8  2009/01/16 08:51:12  wangqi
 * *** empty log message ***
 *
 * Revision 1.8  2008/12/17 06:45:53  wangqi
 * *** empty log message ***
 *
 * Revision 1.7  2008/12/08 10:05:23  wangqi
 * *** empty log message ***
 *
 * Revision 1.6  2008/11/20 05:43:28  wangqi
 * *** empty log message ***
 *
 * Revision 1.5  2008/11/04 14:00:39  wangqi
 * *** empty log message ***
 *
 * Revision 1.4  2008/09/16 07:11:46  wangqi
 * *** empty log message ***
 *
 * Revision 1.3  2008/09/10 15:41:22  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2008/09/09 08:42:45  wangqi
 * 处理了界面上的控件缓冲，由Vector变为HashMap
 * //private Vector v_compents = new Vector();
 * private HashMap v_compents = new HashMap();
 *
 * Revision 1.1  2008/09/01 07:38:13  wangqi
 * *** empty log message ***
 *
 * Revision 1.10  2008/06/11 16:40:22  wangqi
 * *** empty log message ***
 *
 * Revision 1.9  2007/12/07 09:43:36  wangqi
 * *** empty log message ***
 *
 * Revision 1.8  2007/07/31 08:22:13  sunxf
 * MR#:Nova 20-17
 *
 * Revision 1.7  2007/07/23 10:59:01  sunxf
 * Nova 20-14    平台层面更新数据的地方提供乐观锁控制，解决并发访问问题
 *
 * Revision 1.6  2007/07/05 12:16:20  sunxf
 * 编辑公式中增加xxx=>refresh() ,xxx=>reset(),xxx=>enable(),xxx=>unable()方法
 *
 * Revision 1.5  2007/06/28 03:15:19  qilin
 * no message
 *
 * Revision 1.4  2007/06/14 08:04:26  lst
 * no message
 *
 * Revision 1.3  2007/05/31 07:38:14  qilin
 * code format
 *
 * Revision 1.2  2007/05/31 06:47:50  qilin
 * 界面重构，所有的JFrame改为JInternalFrame样式
 *
 * Revision 1.1  2007/05/17 06:01:37  qilin
 * no message
 *
 * Revision 1.36  2007/04/24 05:59:35  qilin
 * no message
 *
 * Revision 1.35  2007/04/04 01:57:59  shxch
 * *** empty log message ***
 *
 * Revision 1.34  2007/04/02 06:58:01  shxch
 * *** empty log message ***
 *
 * Revision 1.33  2007/04/02 04:45:22  shxch
 * *** empty log message ***
 *
 * Revision 1.32  2007/03/28 11:22:52  shxch
 * *** empty log message ***
 *
 * Revision 1.31  2007/03/28 10:15:18  shxch
 * *** empty log message ***
 *
 * Revision 1.30  2007/03/28 09:22:09  shxch
 * *** empty log message ***
 *
 * Revision 1.29  2007/03/28 09:11:07  shxch
 * *** empty log message ***
 *
 * Revision 1.28  2007/03/28 09:04:06  shxch
 * *** empty log message ***
 *
 * Revision 1.27  2007/03/28 05:48:47  shxch
 * *** empty log message ***
 *
 * Revision 1.26  2007/03/27 07:43:30  shxch
 * *** empty log message ***
 *
 * Revision 1.25  2007/03/27 07:37:02  shxch
 * *** empty log message ***
 *
 * Revision 1.24  2007/03/27 07:34:10  shxch
 * *** empty log message ***
 *
 * Revision 1.23  2007/03/21 08:00:11  shxch
 * *** empty log message ***
 *
 * Revision 1.22  2007/03/20 07:53:29  sunxf
 * *** empty log message ***
 *
 * Revision 1.21  2007/03/19 08:10:01  sunxf
 * 添加方法getAllRealValuesWithHashMap()
 *
 * Revision 1.20  2007/03/15 06:45:16  shxch
 * *** empty log message ***
 *
 * Revision 1.19  2007/03/15 03:16:03  shxch
 * *** empty log message ***
 *
 * Revision 1.18  2007/03/14 06:33:27  shxch
 * *** empty log message ***
 *
 * Revision 1.17  2007/03/13 08:43:23  shxch
 * *** empty log message ***
 *
 * Revision 1.16  2007/03/13 08:41:19  shxch
 * *** empty log message ***
 *
 * Revision 1.15  2007/03/12 07:28:01  sunxf
 * *** empty log message ***
 *
 * Revision 1.14  2007/03/07 02:25:01  shxch
 * *** empty log message ***
 *
 * Revision 1.13  2007/03/07 02:01:54  shxch
 * *** empty log message ***
 *
 * Revision 1.12  2007/03/02 05:28:06  shxch
 * *** empty log message ***
 *
 * Revision 1.11  2007/03/02 05:16:42  shxch
 * *** empty log message ***
 *
 * Revision 1.10  2007/03/02 05:02:48  shxch
 * *** empty log message ***
 *
 * Revision 1.9  2007/03/01 09:06:55  shxch
 * *** empty log message ***
 *
 * Revision 1.8  2007/02/27 06:57:18  shxch
 * *** empty log message ***
 *
 * Revision 1.7  2007/02/27 06:03:01  shxch
 * *** empty log message ***
 *
 * Revision 1.6  2007/02/27 05:15:54  shxch
 * *** empty log message ***
 *
 * Revision 1.5  2007/02/10 08:59:51  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/02/07 06:28:49  lujian
 * *** empty log message ***
 * Revision 1.3 2007/02/07 03:43:26 lujian
 * getAllObjectValuesWithHashMap()方法将version字段添加进map Revision 1.2 2007/01/30
 * 04:56:13 lujian *** empty log message ***
 *
 *
 ******************************************************************************/
