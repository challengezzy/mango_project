/**************************************************************************
 * $RCSfile: QuickQueryPanel.java,v $  $Revision: 1.13.2.15 $  $Date: 2009/12/02 05:36:02 $
 **************************************************************************/
package smartx.publics.styletemplet.ui;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import smartx.framework.common.ui.*;
import smartx.framework.common.utils.StringUtil;
import smartx.framework.common.utils.Sys;
import smartx.framework.common.vo.FrameWorkTBUtil;
import smartx.framework.common.vo.ModifySqlUtil;
import smartx.framework.metadata.ui.*;
import smartx.framework.metadata.ui.componentscard.*;
import smartx.framework.metadata.vo.*;
import smartx.framework.metadata.vo.jepfunctions.JepFormulaParse;
import smartx.framework.metadata.vo.jepfunctions.JepFormulaUtil;


/**
 * 
 * 模板快速查询组件
 *
 * @author Sun xuefeng
 */
public class QuickQueryPanel extends JPanel {

    private static final long serialVersionUID = 4130562298534355459L;

    protected Pub_Templet_1VO templetVO = null;
    
    protected JPanel showpanel=null;
    protected JPanel querybtnpanel=null;

    private Vector v_compents = new Vector();

    private Vector v_showcompents = null;

    private HashMap maplist = new HashMap();

    private HashMap map = new HashMap();

    private StringBuffer sbcard = null;

    private boolean isFirst2 = true;

    private boolean bo_iscardquery = true;

    protected BillListPanel billlist = null;

    private String condition = null;              //元原模板的条件
    private String ordersetting = null;           //元原模板的排序

    QueryKeyListener querylistener = new QueryKeyListener();

    String realqueySQL = null;

    public QuickQueryPanel(String code) {
        this(code, null);
    }

    public QuickQueryPanel(String code, String condition) {
        try {
            templetVO = UIUtil.getPub_Templet_1VO(code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.condition = condition;
        init();
    }

    public QuickQueryPanel(BillListPanel _panel) {
        this(_panel, null);
    }

    public QuickQueryPanel(BillListPanel _panel, String condition) {
        this.billlist = _panel;
        this.templetVO = billlist.getTempletVO();
        this.condition = condition;
        init();
    }

    public QuickQueryPanel(Pub_Templet_1VO templetVO) {
        this.templetVO = templetVO;
        init();
    }

    public void init() {
    	//获得元原模板的排序
    	this.ordersetting=	templetVO.getOrdersetting();
        if (NovaClientEnvironment.getInstance().isAdmin()) {
            this.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        showPopupMenu(e);
                    }
                }
            });
        }

        showpanel = new JPanel();
        showpanel.setName("quickquerypanel");
        this.setBorder(BorderFactory.createMatteBorder(0,0,1,0,Color.LIGHT_GRAY ));
        
        FlowLayout flayout = new FlowLayout(FlowLayout.LEFT);
        flayout.setHgap(0);
        flayout.setVgap(2);
        showpanel.setLayout(flayout);
        if (getTempletViewItemVOs() != null) {
        	Pub_Templet_1_ItemVO[] vos=getTempletViewItemVOs();
            for (int i = 0; i < vos.length; i++) {
            	//首先查看有没有设置条件类型，如果没有设置，则取默认字段类型。后面的参照和下拉都要有相应的处理
            	String str_type = vos[i].getConditionItemType();
            	str_type = (str_type==null)?vos[i].getItemtype():str_type;
            	//如果设置为直接查询【值为1】，才允许展现
                if ("1".equals(vos[i].getDelfaultquerylevel())) {
	                if (str_type.equals("文本框")) {
	                	TextFieldPanel panel = new TextFieldPanel(vos[i]);
	                    showpanel.add(panel);
	                    panel.getTextField().addKeyListener(querylistener);//增回车触发查询监听
	                    panel.setValue(execFormula(vos[i].getDefaultCondition()));
	                    v_compents.add(panel);
	                } else if (str_type.equals("数字框")) {
	                	QueryNumericPanel panel=new QueryNumericPanel(vos[i],QueryNumericPanel.TYPE_FLOAT);
	                	panel.setValue(execFormula(vos[i].getDefaultCondition()));
	                	showpanel.add(panel);
	                	v_compents.add(panel);
	                } else if (str_type.equals("下拉框")) {
	                	AbstractNovaComponent panel=null;
	                	if(vos[i].getConditionComBoxItemVos()!=null){
	                		panel = new QueryComboxPanel(vos[i]);
	                	}else{
	                		panel = new ComBoxPanel(vos[i]);
	                	}
	                	showpanel.add(panel);
	                	//TODO 这里的setValue应该和setObject统一起来
	                    ((ComBoxPanel)panel).setValue(execFormula(vos[i].getDefaultCondition()));
	                    v_compents.add(panel);
	                } else if (str_type.equals("参照")) {
	                	AbstractNovaComponent panel=null;
	                	if(vos[i].getConditionRefDesc()!=null){
	                		panel = new QueryUIRefPanel(vos[i]);
	                	}else{
	                		panel = new UIRefPanel(vos[i]);
	                	}
	                    showpanel.add(panel);
	                    //TODO 这里的setValue应该和setObject统一起来
	                    ((UIRefPanel)panel).setValue(execFormula(vos[i].getDefaultCondition()));
	                    v_compents.add(panel);
	                } else if (str_type.equals("日历")) {
	                    QueryCalendarPanel Panel = new QueryCalendarPanel(vos[i],QueryCalendarPanel.TYPE_DATE);
	                    showpanel.add(Panel);
	                    Panel.setValue(execFormula(vos[i].getDefaultCondition()));
	                    v_compents.add(Panel);
	                } else if (str_type.equals("时间")) {
	                    QueryCalendarPanel Panel = new QueryCalendarPanel(vos[i],QueryCalendarPanel.TYPE_TIME);
	                	showpanel.add(Panel);
	                    Panel.setValue(execFormula(vos[i].getDefaultCondition()));
	                    v_compents.add(Panel);
	                } else if (str_type.equals("勾选框")) {
	                    UICheckBoxPanel panel = new UICheckBoxPanel(vos[i]);
	                    showpanel.add(panel);
	                    panel.setValue(execFormula(vos[i].getDefaultCondition()));
	                    v_compents.add(panel);
	                }
                }
            }
        }
        showpanel.addComponentListener(new ComponentAdapter(){
			public void componentResized(ComponentEvent e) {
				//Object o=e.getSource();
				Object o=e.getComponent();
				
				if(o instanceof JPanel){
					if(((JPanel)o).getName().equalsIgnoreCase("quickquerypanel")){
						//System.out.println(e.paramString());
						int width=((JPanel)o).getWidth();
						int height=(int)((JPanel)o).getSize().getHeight();
						//System.out.println("新高度"+height);
						int newheight=NovaPanelUtil.getPanelHeight(width,(JPanel[])v_compents.toArray(new JPanel[0]),StringUtil.parseInt(Sys.getInfo("UI_FIELD_VGAP"),1));
						newheight=newheight+querybtnpanel.getHeight();
						if(height!=newheight){
							//((JPanel)o).setPreferredSize(null);
							JInternalFrame frame =getJInternalFrame((JPanel)o);				
							if(frame==null)return ;
							//((JPanel)o).setPreferredSize(new Dimension(((JPanel)o).getWidth(),getPanelHeight(((JPanel)o),v_compents,5)));
							//((JPanel)o).setMinimumSize(new Dimension(width,newheight));
							
											
							((JPanel)o).setPreferredSize(new Dimension(width,newheight));
							//((JPanel)o).updateUI();
							((JPanel)o).invalidate();
							//frame.getRootPane().invalidate();
							frame.getRootPane().revalidate();
						}
						//JOptionPane.showMessageDialog(null,"大小事件，宽："+((JPanel)o).getWidth());
						//JOptionPane.showMessageDialog(null,"大小事件，高："+((JPanel)o).getHeight());
						
						
					}
					
				}

			}

			//找出最高一级的JInternalFrame、JFrame、Window
        	private JInternalFrame getJInternalFrame(JComponent obj){
        		Container parent=obj;
        		while(!((parent=parent.getParent()) instanceof JInternalFrame)){
        			if(obj==null) return null;
        			
        		}
        		return (JInternalFrame)parent;
        	}
        	
        	
        });
        this.setLayout(new BorderLayout());
        this.add(showpanel, BorderLayout.CENTER);
        //快速检索命令按钮
        if (this.billlist != null) {
            querybtnpanel = new JPanel();
            FlowLayout f = new FlowLayout(FlowLayout.RIGHT);
            flayout.setHgap(0);
            flayout.setVgap(3);
            querybtnpanel.setLayout(f);            
            //JButton btn = new JButton("检索");
            
            JButton btn = new JButton("检索", UIUtil.getImage("images/office/(03,31).png","快速检索"));
            btn.setPreferredSize(new Dimension(80, 20));
            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	try{
                        onQuery();
                	}catch(Exception ee){
                	    NovaMessage.show(billlist, "操作错误："+ee.getMessage(), NovaConstants.MESSAGE_ERROR);                		
                	}
                }
            });
            querybtnpanel.add(btn);            
            this.add(querybtnpanel, BorderLayout.SOUTH);
        }
        
        if (NovaClientEnvironment.getInstance().isAdmin()) {
            this.setToolTipText("点击右键可以查看详细查询SQL!");
        }
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
    
    
    private class QueryKeyListener extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            	try{
                    onQuery();
            	}catch(Exception ee){
            	    NovaMessage.show(billlist, "操作错误："+ee.getMessage(), NovaConstants.MESSAGE_ERROR);                		
            	}
            }
        }
    }

    private void showPopupMenu(MouseEvent e) {
        JPopupMenu menu = new JPopupMenu("查看SQL");
        JMenuItem item = new JMenuItem("查看SQL");
        menu.add(item);
        menu.show(this, e.getX(), e.getY());
        item.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    showSQLDialog();
                }
            }
        });
    }

    private void showSQLDialog() {
    	NovaMessage.show(this, this.realqueySQL, NovaConstants.MESSAGE_INFO);        
    }

    
   
    /**
     * 执行检索
     */
    public void onQuery()throws Exception {
        /**
         * 1、创建sql
         * 2、判断附加条件
         * 3、判断是否具有排序处理   
         * 4、组合sql
         */
        if (billlist == null) {
            return;
        }
        String querysql = this.getQuerySQL();
        if (querysql == null || querysql.equals("")) {
            querysql = "1=1";
        }
        if (condition != null && !condition.equals("")) {
            querysql = querysql + " and " + condition;
        }

        //通过BillListPanel对sql进行组合完整
        this.realqueySQL = billlist.getSQL(querysql);
        //3、判断是否具有排序处理   
        this.realqueySQL+=(this.ordersetting!=null&&!this.ordersetting.trim().equals(""))?(" order by "+this.ordersetting):"";
        

        //执行查询
        billlist.QueryData(this.realqueySQL);
        

        Component frame = billlist.getLoadedFrame();

        //如果是模板9,还要清除所有子表数据!!
        if (frame != null && frame instanceof smartx.publics.styletemplet.ui.templet09.AbstractTempletFrame09) {
            smartx.publics.styletemplet.ui.templet09.AbstractTempletFrame09 new_name = (smartx.publics.styletemplet.ui.templet09.AbstractTempletFrame09) frame;
            BillListPanel[] billListPanels = new_name.getChild_BillListPanels();
            for (int i = 0; i < billListPanels.length; i++) {
                billListPanels[i].clearTable(); //
            }
        }

    }

    public void setQueryCondition(String condition){
    	this.condition=condition;
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

    
    /**
     * 获得检索Sql
     * @return sql
     */
    public String getQuerySQL() throws Exception {
        //initcard();
        sbcard = new StringBuffer("1=1 ");
        //应该使用v_compents来判断所有条件，没有必要重新读取vo
        for (int i = 0; i < v_compents.size(); i++) {
        	INovaCompent icomp=(INovaCompent)v_compents.get(i);
        	Pub_Templet_1_ItemVO vo=icomp.getTempletItemVO();
        	String votype=vo.getConditionItemType();
        	votype=(votype==null)?vo.getItemtype():votype;
        	 
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
            	sbcard.append("and "+icomp.getObject());
            } else if (votype.equals("日历")) {
            	sbcard.append("and "+icomp.getObject());
            } else if (votype.equals("数字框")) {
            	sbcard.append("and "+icomp.getValue());
            } else if (votype.equals("下拉框") || votype.equals("参照") || votype.equals("勾选框")) {
            	String v=icomp.getValue();
            	if(v!=null){
            		sbcard.append(" and " + vo.getItemkey() + " = '" + v + "' ");
            	}
            }else{
            	sbcard.append(" and upper(" + vo.getItemkey() + ") LIKE upper('%" + icomp.getObject() + "%') ");
            }
        }
        
        return sbcard.toString();
    }
    
    //界面字段是否为空
    private boolean isEmptyCompentByKey(String _key) {
        INovaCompent[] compents = (INovaCompent[]) v_compents.toArray(new INovaCompent[0]);
        for (int i = 0; i < compents.length; i++) {
            if (compents[i].getKey().equalsIgnoreCase(_key)) {
            	Object v=compents[i].getObject();
                return v==null||v.toString().equals("");
            }
        }
        return false;
    }
    //界面字段是否为空
    private boolean isEmptyCompentByKey(INovaCompent com) {
        Object v=com.getObject();
        return v==null||v.toString().equals("");        
    }

    public ArrayList findIndexMap(Object _ob) {
        ArrayList index = new ArrayList();
        if (map.get(_ob) instanceof ArrayList) {
            index.add( ( (ArrayList) (map.get(_ob))).get(0).toString());
            index.add( ( (ArrayList) (map.get(_ob))).get(1).toString());
        }
        return index;
    }
    
    /**
     * 当前类用到了好多个"getTempletViewItemVOs()", 其返回的是当前表(视图)中查询的列
     * 在查询统计时,会要求快速查询界面不光显示表(视图)中查询的列,有可能显示一些用于过滤数据的列,
     * 计划通过之类重写该方法,对于表(视图)中没有查询的列,同样可以作为查询条件来显示
     * @return Pub_Templet_1_ItemVO[]
     */
    public Pub_Templet_1_ItemVO[] getTempletViewItemVOs(){
        return templetVO.getRealViewItemVOs();
    }

}

/**************************************************************************
 * $RCSfile: QuickQueryPanel.java,v $  $Revision: 1.13.2.15 $  $Date: 2009/12/02 05:36:02 $
 *
 * $Log: QuickQueryPanel.java,v $
 * Revision 1.13.2.15  2009/12/02 05:36:02  wangqi
 * *** empty log message ***
 *
 * Revision 1.13.2.14  2009/07/30 07:19:59  wangqi
 * *** empty log message ***
 *
 * Revision 1.13.2.13  2009/07/28 08:36:42  wangqi
 * *** empty log message ***
 *
 * Revision 1.13.2.12  2009/07/23 09:34:13  wangqi
 * *** empty log message ***
 *
 * Revision 1.13.2.11  2009/07/22 08:23:44  wangqi
 * *** empty log message ***
 *
 * Revision 1.13.2.10  2009/07/21 08:41:30  wangqi
 * *** empty log message ***
 *
 * Revision 1.13.2.9  2009/06/24 08:36:12  wangqi
 * *** empty log message ***
 *
 * Revision 1.13.2.8  2009/06/12 05:25:22  wangqi
 * *** empty log message ***
 *
 * Revision 1.13.2.7  2009/05/20 10:25:08  wangqi
 * *** empty log message ***
 *
 * Revision 1.13.2.6  2009/02/05 12:02:53  wangqi
 * *** empty log message ***
 *
 * Revision 1.13.2.5  2008/11/20 05:32:02  wangqi
 * *** empty log message ***
 *
 * Revision 1.13.2.4  2008/11/17 06:48:13  wangqi
 * *** empty log message ***
 *
 * Revision 1.13.2.3  2008/11/05 05:21:14  wangqi
 * *** empty log message ***
 *
 * Revision 1.13.2.2  2008/10/20 14:39:56  wangqi
 * *** empty log message ***
 *
 * Revision 1.13.2.1  2008/09/16 06:13:41  wangqi
 * patch   : 20080916
 * file    : nova_20080128_20080916.jar
 * content : 处理 MR nova20-87,nova20-30；
 * 另外，改写了快速查询面板的处理。
 *
 * Revision 1.15  2008/05/26 07:48:44  wangqi
 * *** empty log message ***
 *
 * Revision 1.14  2008/02/27 09:07:59  wangqi
 * *** empty log message ***
 *
 * Revision 1.13  2008/01/15 04:36:23  wangqi
 * *** empty log message ***
 *
 * Revision 1.12  2007/12/04 07:14:39  wangqi
 * *** empty log message ***
 *
 * Revision 1.11  2007/08/27 09:01:37  yanghuan
 * 修改bug,该bug描述:计算的高度不正确导致快速查询面板不能显示完整的查询条件
 *
 * Revision 1.10  2007/08/24 07:06:53  yanghuan
 * 解决计算高度错误的bug
 *
 * Revision 1.9  2007/08/07 08:18:14  sunxf
 * 修改bug
 *
 * Revision 1.8  2007/07/31 04:50:06  sunxf
 * MR#:Nova 20-16  更改高度及滚动条显示时机
 *
 * Revision 1.7  2007/07/24 05:47:53  lst
 * no message
 *
 * Revision 1.6  2007/07/18 11:08:42  sunxf
 * 日期时间查询控件
 *
 * Revision 1.5  2007/07/17 06:14:47  sunxf
 * 修改多行显示不下的问题
 *
 * Revision 1.4  2007/06/07 00:56:38  sunxb
 * *** empty log message ***
 *
 * Revision 1.3  2007/05/31 07:39:01  qilin
 * code format
 *
 * Revision 1.2  2007/05/31 06:47:51  qilin
 * 界面重构，所有的JFrame改为JInternalFrame样式
 *
 * Revision 1.1  2007/05/17 06:14:29  qilin
 * no message
 *
 * Revision 1.13  2007/05/15 08:29:33  qilin
 * no message
 *
 * Revision 1.12  2007/03/29 05:29:58  shxch
 * *** empty log message ***
 *
 * Revision 1.11  2007/03/21 03:09:42  qilin
 * no message
 *
 * Revision 1.10  2007/03/15 05:27:05  shxch
 * *** empty log message ***
 *
 * Revision 1.9  2007/03/15 05:10:11  shxch
 * *** empty log message ***
 *
 * Revision 1.8  2007/03/15 04:59:43  shxch
 * *** empty log message ***
 *
 * Revision 1.7  2007/03/14 02:11:15  sunxf
 * *** empty log message ***
 *
 * Revision 1.6  2007/03/14 01:51:12  sunxf
 * *** empty log message ***
 *
 * Revision 1.5  2007/03/14 01:31:03  sunxf
 * *** empty log message ***
 *
 * Revision 1.4  2007/03/05 09:59:15  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/03/02 05:28:06  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:23:45  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
