/**************************************************************************
 * $RCSfile: UIRefPanel_List.java,v $  $Revision: 1.10.2.8 $  $Date: 2010/05/05 14:04:32 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentslist;

import java.lang.reflect.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import smartx.framework.common.ui.*;
import smartx.framework.common.vo.*;
import smartx.framework.metadata.ui.*;
import smartx.framework.metadata.ui.componentscard.*;
import smartx.framework.metadata.vo.*;
import smartx.framework.metadata.vo.jepfunctions.JepFormulaParse;


public class UIRefPanel_List extends JPanel implements INovaCompent {
    private static final long serialVersionUID = 1L;

    protected Pub_Templet_1_ItemVO templetItemVO = null; //

    protected BillListModel billListModel = null;

    protected AbstractCellEditor cellEditor = null;

    protected int li_currrow = -1;

    protected String refID = "";

    protected String refCode = "";

    protected String refName = "";

    protected HashVO hashVO = null; //直接的数据

    protected String refDesc = "";

    protected String key = null;

    protected String name = null;

    protected int li_textfield_width = 150 - 18;

    protected JTextField textField = null;
    protected RefDialogIFC refDialog= null;

    protected JButton btn_ref = null; //参照按钮

    protected JButton btn_clearbtn = null; //清除按钮

    protected boolean isNeed = false;

    protected RefItemVO currRefItemVO = null; //该参照对应的数据VO

    protected String str_type = null;

    protected String str_realsql = null;

    protected String str_parentfieldname = null;

    protected String str_pkfieldname = null; //
    protected String str_table = null;//树表参照表SQL
    protected String str_table_fk = null;//树表参照表外键
    protected String str_datasourcename = null; //数据源名字
    protected String loadall = null;
    protected String str_refvalid=null;//参照前置校验
    
    
    public UIRefPanel_List(Pub_Templet_1_ItemVO _templetVO) {
        this.templetItemVO = _templetVO; //

        this.key = templetItemVO.getItemkey(); //
        this.name = templetItemVO.getItemname(); //
        this.refDesc = templetItemVO.getRefdesc();
        this.li_textfield_width = templetItemVO.getCardwidth().intValue() - 18;
        initialize();
    }

    public UIRefPanel_List(Pub_Templet_1_ItemVO _templetVO, BillListModel _billListModel, int _currrow,
                           AbstractCellEditor _cellEditor) {
        this.templetItemVO = _templetVO; //
        this.billListModel = _billListModel; //
        this.li_currrow = _currrow;
        this.cellEditor = _cellEditor;
        this.key = templetItemVO.getItemkey(); //
        this.name = templetItemVO.getItemname(); //
        this.refDesc = templetItemVO.getRefdesc();
        this.li_textfield_width = templetItemVO.getCardwidth().intValue() - 18;
        initialize();
    }

    public UIRefPanel_List(String _key, String _name, String _refSQL) {
        this.key = _key; //
        this.name = _name; //
        this.refDesc = _refSQL;

        initialize();
    }

    public UIRefPanel_List(String _key, String _name, String _refSQL, boolean isNeed) {
        this.key = _key; //
        this.name = _name; //
        this.refDesc = _refSQL;
        this.isNeed = isNeed;
        initialize();
    }

    public Pub_Templet_1_ItemVO getTempletItemVO() {
		return this.templetItemVO;
	}
    
    private void initialize() {
        if (refDesc != null && !refDesc.trim().equals("")) { //参照定义
            String[] str_refdefines = new ModifySqlUtil().getRefDescTypeAndSQL(refDesc);
            str_type = str_refdefines[0]; // 类型
            str_realsql = str_refdefines[1]; //
            str_parentfieldname = str_refdefines[2]; //
            str_pkfieldname = str_refdefines[3]; //
            str_table = str_refdefines[4];
            str_table_fk = str_refdefines[5];
            if (str_refdefines[6] == null) {
                str_datasourcename = NovaClientEnvironment.getInstance().getDefaultDatasourceName();
            } else {
                str_datasourcename = new FrameWorkTBUtil().convertExpression(NovaClientEnvironment.getInstance(),
                    str_refdefines[6]); //取得数据源
            }
            loadall = str_refdefines[7];
            str_refvalid=str_refdefines[8];
        }

        //this.setPreferredSize(new Dimension(li_width_all, 20));
        this.setLayout(new BorderLayout());

        textField = new JTextField();
        //textField.setPreferredSize(new Dimension(li_textfield_width, 20));
        textField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                myOnFocus();
            }

            public void focusLost(FocusEvent e) {
                myLostFocus();
            }
        });

        //textField.setToolTipText("[" + getRefID() + "][" + getRefCode() + "][" + getRefName() + "]");

        btn_ref = new JButton(UIUtil.getImage("images/platform/refsearch.gif"));
        btn_ref.setPreferredSize(new Dimension(16, 16)); //按扭的宽度与高度
        btn_ref.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onButtonClicked();
            }

        });

        btn_clearbtn = new JButton(UIUtil.getImage("images/platform/clear.gif")); //清除按钮
        btn_clearbtn.setPreferredSize(new Dimension(16, 16)); // 按扭的宽度与高度
        btn_clearbtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reset(); //
            }

        });

        this.add(textField, BorderLayout.CENTER);

        JPanel panel_tmp = new JPanel(); //
        //panel_tmp.setPreferredSize(new Dimension(40, 22)); //
        FlowLayout fllayout_tmp = new FlowLayout();
        fllayout_tmp.setHgap(1);
        fllayout_tmp.setVgap(2);
        panel_tmp.setLayout(fllayout_tmp); //
        panel_tmp.add(btn_ref);
        panel_tmp.add(btn_clearbtn);

        this.add(panel_tmp, BorderLayout.EAST); //
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public JTextField getTextField() {
        return textField;
    }

    private void onButtonClicked() {
        if (this.refDesc == null || this.refDesc.trim().equalsIgnoreCase("null") || this.refDesc.trim().equals("")) {
            NovaMessage.show(this, "没有定义参照说明！", NovaConstants.MESSAGE_ERROR);
            return;
        }
        
        HashMap map=(this.billListModel == null)?null:this.billListModel.getValueAtRowWithHashMap(li_currrow);
        
        String sql = null, ds=null;
        try {
        	JepFormulaParse jepParse = new JepFormulaParse(JepFormulaParse.li_ui);
        	//参照前校验公式处理
            if(!"true".equalsIgnoreCase(this.str_refvalid)){
            	String tmp=new FrameWorkTBUtil().convertFormulaMacPars(this.str_refvalid, NovaClientEnvironment.getInstance(), map);
                Object ot = jepParse.execFormula(tmp);
                if(!"true".equalsIgnoreCase((String)ot)){
                	return;
                }
            }
            sql = new FrameWorkTBUtil().convertFormulaMacPars(str_realsql, NovaClientEnvironment.getInstance(), map); 
            String tmp = (String)jepParse.execFormula(sql);
            sql = (tmp == null) ? sql : tmp;
            ds = new FrameWorkTBUtil().convertFormulaMacPars(str_datasourcename, NovaClientEnvironment.getInstance(), map);
            tmp = (String)jepParse.execFormula(ds);
            ds = (tmp == null) ? ds : tmp;
        } catch (Exception ex) {
            NovaMessage.show(this, "在转换参照定义【"+str_realsql+"】和数据源定义【"+str_datasourcename+"】的时候发生错误！"+ex.getMessage(), NovaConstants.MESSAGE_ERROR);
            return; 
        }
        
        try {
	        if (str_type.equalsIgnoreCase("TABLE")) {
	            refDialog = new UIRefDialog(this, "选择: " + name, getRefID(), new DefaultRefModel(ds, sql));
	        } else if (str_type.equalsIgnoreCase("TABLE2")) {
	            refDialog = new UIRefDialog(this, "选择: " + name, getRefID(), new DefaultRefModel(ds, sql));
	        } else if (str_type.equalsIgnoreCase("TABLE3")) {
	            refDialog = new UIRefDialog(this, "选择: " + name, getRefID(), new DefaultRefModel(ds, sql));
	        } else if (str_type.equalsIgnoreCase("TREE")) {
	            refDialog = new UIRefTreeDialog(this, "选择: " + name , textField.getText(), sql, str_pkfieldname, str_parentfieldname, ds,loadall);
	        } else if (str_type.equalsIgnoreCase("TREE2")) {
	            refDialog = new UIRefTreeDialog(this, "选择: " + name , textField.getText(), sql, str_pkfieldname, str_parentfieldname, ds,loadall);
	        } else if (str_type.equalsIgnoreCase("TREE3")) {
	            refDialog = new UIRefTreeDialog(this, "选择: " + name , textField.getText(), sql, str_pkfieldname, str_parentfieldname, ds,loadall);
	        }else if (str_type.equalsIgnoreCase("MUTITREE")) {
	            refDialog = new UIRefMutiTreeDialog(this, "选择: " + name ,this.refID, textField.getText(), sql, str_pkfieldname, str_parentfieldname,str_table,str_table_fk, ds,loadall);            
	        } else if (str_type.equalsIgnoreCase("CUST")) {            
	            Class dialog_class = Class.forName(sql); 
	            Class cp[] = {java.awt.Container.class, String.class, HashMap.class};
	            Constructor constructor = dialog_class.getConstructor(cp);
	            refDialog = (AbstractRefDialog) constructor.newInstance(new Object[] {this, getRefID(), map});            
	        } else {
	        	NovaMessage.show(this, "没有对应的参照类型[" + str_type + "]", NovaConstants.MESSAGE_ERROR);
	            return;
	        }
        } catch (Exception ex) {
        	NovaMessage.show(this, "表型参照定义[" + this.refDesc + "]格式不对！" + ex.getMessage(), NovaConstants.MESSAGE_ERROR);
            return;
        }
        
        refDialog.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
            	RefDialogIFC dialog=(RefDialogIFC)e.getSource();
            	if (dialog.getCloseType() == 0 || dialog.getCloseType() == 1) {
                    setCurrRefItemVO(dialog.getRefVO());
                }
            }
        });
        
        if (cellEditor != null) {
            this.cellEditor.stopCellEditing();
        }
    }

    public JButton getBtn_ref() {
        return btn_ref;
    }

    public String getRefCode() {
        return refCode;
    }

    public void setRefCode(String _code) {
        this.refCode = _code;
    }

    public String getRefID() {
        return refID;
    }

    public void setRefID(String refID) {
        this.refID = refID; //
    }

    public String getRefName() {
        return refName;
    }

    public void setRefName(String refName) {
        this.refName = refName;
    }

    public HashVO getHashVO() {
        return this.hashVO;
    }

    public void setHashVO(HashVO _hashVO) {
        this.hashVO = _hashVO;
    }

    public String getValue() {
        return this.refID;
    }
  //add by James.W
	//TODO 获得界面选择真实值的处理
	/**
	 * 获得界面选择的真实值
	 * @return String value
	 * @deprecated 本方法新增加，首先满足了日历的使用，其他类型的数据需要以后支持
	 */
    public String getInputValue() {
		return null;		
	}

    public void setValue(String _value) {
        this.refID = _value;
        this.refCode = _value;
        this.refName = _value;
        textField.setText(_value); //
    }

    public void setText(String _text) {
        textField.setText(_text);
    }

    private void myOnFocus() {
        String str_code = this.getRefCode();
        if (str_code != null) {
            this.getTextField().setText(str_code);
        }
    }

    private void myLostFocus() {
        String str_name = this.getRefName();
        if (str_name != null) {
            this.getTextField().setText(str_name);
        }
    }

    public void reset() {
        this.refID = null;
        this.refCode = null;
        this.refName = null;
        textField.setText(null); //
    }

    public void setEditable(boolean _bo) {
        textField.setEditable(_bo);
        btn_ref.setEnabled(_bo);
        btn_clearbtn.setEnabled(_bo);
    }

    public Object getObject() {
        return refDialog.getRefVO(); //
    }

    public void setObject(Object _obj) {
        RefItemVO vo = (RefItemVO) _obj;
        if (vo != null) {
            setRefID(vo.getId());
            setRefCode(vo.getCode());
            setRefName(vo.getName());
            setText(vo.getName());
            setHashVO(vo.getHashVO()); //
            this.setCurrRefItemVO(vo); //设置数据
        } else {
            reset();
        }
    }

    public JLabel getLabel() {
        return null; //
    }

    public RefItemVO getCurrRefItemVO() {
        return currRefItemVO;
    }

    public void setCurrRefItemVO(RefItemVO refVO) {
        this.currRefItemVO = refVO;        
        if(refVO==null){
        	this.setRefID(null); 
            this.setRefCode(null); 
            this.setRefName(null); 
            this.getTextField().setText(""); 
        }else{
	        this.setRefID(refVO.getId()); 
	        this.setRefCode(refVO.getCode()); 
	        this.setRefName(refVO.getName());
	        this.getTextField().setText(refVO.toString()); //
        }
    }

    public void focus() {
        textField.requestFocus();
        textField.requestFocusInWindow(); //
    }

}
/**************************************************************************
 * $RCSfile: UIRefPanel_List.java,v $  $Revision: 1.10.2.8 $  $Date: 2010/05/05 14:04:32 $
 *
 * $Log: UIRefPanel_List.java,v $
 * Revision 1.10.2.8  2010/05/05 14:04:32  wangqi
 * *** empty log message ***
 *
 * Revision 1.10.2.7  2010/03/05 13:28:30  wangqi
 * *** empty log message ***
 *
 * Revision 1.10.2.6  2010/01/21 08:20:29  wangqi
 * *** empty log message ***
 *
 * Revision 1.10.2.5  2010/01/20 10:03:16  wangqi
 * *** empty log message ***
 *
 * Revision 1.10.2.4  2009/02/05 12:02:43  wangqi
 * *** empty log message ***
 *
 * Revision 1.10.2.3  2009/02/02 16:12:54  wangqi
 * *** empty log message ***
 *
 * Revision 1.10.2.2  2008/10/09 03:19:51  wangqi
 * *** empty log message ***
 *
 * Revision 1.10.2.1  2008/06/11 11:41:18  wangqi
 * *** empty log message ***
 *
 * Revision 1.10  2008/01/15 09:17:10  wangqi
 * 孙雪峰修改MR
 *
 * Revision 1.8  2007/11/28 05:35:28  wangqi
 * *** empty log message ***
 *
 * Revision 1.7  2007/07/11 11:11:01  sunxf
 * 增加MutiTree
 *
 * Revision 1.6  2007/06/19 03:13:03  qilin
 * MR#:BZM10-58
 * 去掉数据源的显示
 *
 * Revision 1.5  2007/06/01 13:01:57  sunxb
 * *** empty log message ***
 *
 * Revision 1.4  2007/05/31 07:38:22  qilin
 * code format
 *
 * Revision 1.3  2007/05/22 07:58:47  qilin
 * no message
 *
 * Revision 1.2  2007/05/22 02:03:59  sunxb
 * *** empty log message ***
 *
 * Revision 1.1  2007/05/17 06:01:59  qilin
 * no message
 *
 * Revision 1.7  2007/03/28 10:52:14  shxch
 * *** empty log message ***
 *
 * Revision 1.6  2007/03/28 09:51:08  shxch
 * *** empty log message ***
 *
 * Revision 1.5  2007/03/07 02:01:55  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/02/27 06:03:03  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/02/27 05:15:54  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 05:05:40  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
