/**************************************************************************
 * $RCSfile: UIRefPanel.java,v $  $Revision: 1.20.2.12 $  $Date: 2010/05/05 14:02:37 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentscard;

import java.lang.reflect.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import smartx.framework.common.ui.*;
import smartx.framework.common.vo.*;
import smartx.framework.metadata.ui.*;
import smartx.framework.metadata.vo.*;
import smartx.framework.metadata.vo.jepfunctions.JepFormulaParse;



/**
 * 参照控件
 * @author James.W
 *
 */
public class UIRefPanel extends AbstractNovaComponent implements INovaCompent, NovaEventListener {
	private static final long serialVersionUID = 5310375486883000632L;
	protected String REF_IMAGE="images/platform/refsearch.gif"; //点选参照按钮图标
    protected int REF_WIDTH=18;   //点选参照按钮宽度
    protected int REF_HEIGHT=18;  //点选参照按钮高度
    protected String CLEAR_IMAGE = "images/platform/clear.gif";//TODO 适当时候提升到父类上 清除按钮宽度
    protected int CLEAR_WIDTH = 18;                            //TODO 适当时候提升到父类上 清除按钮宽度
    protected int CLEAR_HEIGHT = 18;                            //TODO 适当时候提升到父类上 清除按钮宽度
    protected int UREF_WIDTH = this.FIELD_WIDTH - REF_WIDTH- CLEAR_WIDTH;//控件宽度
    
    protected BillCardPanel parentPanel = null;

    protected String refID = "";              //参照定义字串
    protected String refCode = "";
    protected String refName = "";
    protected String refDesc = null; // 参照原因定义...
    
    
    protected JTextField textField = null;
    protected JButton btn_ref = null; // 参照按钮
    protected JButton btn_clearbtn = null; // 清除按钮
    protected Vector v_Listeners = new Vector();
    protected RefItemVO currRefItemVO = null; // 该参照对应的数据VO
    protected String str_type = null;
    protected String str_realsql = null;
    protected String str_parentfieldname = null;
    protected String str_pkfieldname = null; //
    protected String str_datasourcename = null; //数据源名字    
    protected String str_table = null;//树表参照表SQL
    protected String str_table_fk = null;//树表参照表外键
    protected String loadall = null;
    protected String str_refvalid=null;//参照前置校验
    
    protected String location = null;
    

    /**
     * 本构造方法，只为了继承使用，不要用于直接创建对象
     */
    public UIRefPanel() {
    	
    }
    
    public UIRefPanel(Pub_Templet_1_ItemVO _templetVO) {
        this._vo = _templetVO; //

        this.key = this._vo.getItemkey(); //
        this.name = this._vo.getItemname(); //
        this.refDesc = this._vo.getRefdesc(); //原始定义

        this.FIELD_WIDTH = this._vo.getCardwidth().intValue(); //默认宽度
        this.UREF_WIDTH = this.FIELD_WIDTH - REF_WIDTH- CLEAR_WIDTH;
        initRefDesc();
        init();
    }

    public UIRefPanel(String _key, String _name, String _refdesc) {
        this.key = _key; //
        this.name = _name; //
        this.refDesc = _refdesc;
        initRefDesc();
        init();
    }
    
    //TODO add by Lujian
    public UIRefPanel(String _key, String _name, String _refdesc,
			String _location, int _label_width, int _textfield_width) {
		this.key = _key; //
		this.name = _name; //
		this.refDesc = _refdesc;
		this.location = _location;
		this.LABEL_WIDTH = _label_width;
		this.FIELD_WIDTH = _textfield_width;
		this.UREF_WIDTH = this.FIELD_WIDTH - REF_WIDTH- CLEAR_WIDTH;
		initRefDesc();
		init();
	}

    /**
     * 将BillCardPanel句柄传进来,因为公式中可能要用到卡片中别的控件的值!!!
     * @param _parentPanel
     */
    public void setParentCardPanel(BillCardPanel _parentPanel) {
        parentPanel = _parentPanel;
    }
    
    
    
    

    protected void initRefDesc() {
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
    }

    
    //Override
	protected JComponent[] getFieldComponents() {
    	textField = new JTextField();
        textField.setPreferredSize(new Dimension(this.UREF_WIDTH, this.FIELD_HEIGHT));
       
        btn_ref = new JButton(UIUtil.getImage(this.REF_IMAGE));
        btn_ref.setPreferredSize(new Dimension(this.REF_WIDTH, this.REF_HEIGHT)); // 按扭的宽度与高度
        btn_ref.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onButtonClicked();
            }

        });

        btn_clearbtn = new JButton(UIUtil.getImage(this.CLEAR_IMAGE)); // 清除按钮
        btn_clearbtn.setPreferredSize(new Dimension(this.CLEAR_WIDTH, this.CLEAR_HEIGHT)); // 按扭的宽度与高度
        btn_clearbtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reset(); //
            }

        });
        /**
         * TODO 增加编辑事件，填入和清空的时候出发，参考UIRefPanel
         *   fireValueChanged 
         */
		return new JComponent[]{textField,btn_ref,btn_clearbtn};
	}
    
    public JTextField getTextField() {
        return textField;
    }

    protected void onButtonClicked() {
        if (this.refDesc == null || this.refDesc.trim().equalsIgnoreCase("null") || this.refDesc.trim().equals("")) {
        	NovaMessage.show("没有定义参照说明！", NovaConstants.MESSAGE_ERROR);
            return;
        }

        HashMap map=(this.parentPanel == null)?null:this.parentPanel.getAllObjectValuesWithHashMap();
        
        String sql = null,ds=null;
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
        	NovaMessage.show("在转换参照定义【"+str_realsql+"】和数据源定义【"+str_datasourcename+"】的时候发生错误！"+ex.getMessage(), NovaConstants.MESSAGE_ERROR);
            return; // 直接返回
        }
        
        RefDialogIFC refDialog = null;
        try {
	        if (str_type.equalsIgnoreCase("TABLE")) {
	        	refDialog = new UIRefDialog(this, "选择: " + name, this.getRefID(), new DefaultRefModel(ds, sql));                
	        } else if (str_type.equalsIgnoreCase("TABLE2")) { // 如果是Table2
	            refDialog = new UIRefDialog(this, "选择: " + name , this.getRefID(), new DefaultRefModel(ds, sql));
	        } else if (str_type.equalsIgnoreCase("TABLE3")) { // 如果是Table3
	            refDialog = new UIRefDialog(this, "选择: " + name , this.getRefID(), new DefaultRefModel(ds, sql));
	        } else if (str_type.equalsIgnoreCase("TREE")) {
	            refDialog = new UIRefTreeDialog(this, "选择: " + name , textField.getText(), sql, str_pkfieldname, str_parentfieldname, ds,loadall);
	        } else if (str_type.equalsIgnoreCase("TREE2")) {
	            refDialog = new UIRefTreeDialog(this, "选择: " + name ,textField.getText(), sql, str_pkfieldname, str_parentfieldname, ds,loadall);
	        } else if (str_type.equalsIgnoreCase("TREE3")) {
	            refDialog = new UIRefTreeDialog(this, "选择: " + name , textField.getText(), sql, str_pkfieldname, str_parentfieldname, ds,loadall);
	        }else if (str_type.equalsIgnoreCase("MUTITREE")) {//树表，类似模板３
	            refDialog = new UIRefMutiTreeDialog(this, "选择: " + name ,this.refID, textField.getText(), sql, str_pkfieldname, str_parentfieldname,str_table,str_table_fk, ds,loadall);
	        } else if (str_type.equalsIgnoreCase("CUST")) {//自定义参照
		        Class dialog_class = Class.forName(sql); // 找到对应的ClassLoader
		        Class cp[] = {java.awt.Container.class, String.class, HashMap.class};
		        Constructor constructor = dialog_class.getConstructor(cp);
		        refDialog = (AbstractRefDialog) constructor.newInstance(new Object[] {this, getRefID(), map});
	        } else {
	            NovaMessage.show(this, "没有对应的参照类型[" + str_type + "]"); //
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
                    onValueChanged(new NovaEvent(this));
                }
            }            
        });
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

    public String getValue() {
        return this.refID;
    }
    
    //add by James.W
	//TODO 对于引用框获得界面选择真实值的处理
	/**
	 * 获得界面选择的真实值
	 * @return String value
	 * @deprecated 本方法新增加，首先满足了日历的使用，其他类型的数据需要以后支持
	 */
    public String getInputValue() {
		return null;		
	}

    public void setValue(String _value) {
        this.refID = _value; //
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
        this.setCurrRefItemVO(null); //
        textField.setText(null); //
    }

    public void setEditable(boolean _bo) {
        textField.setEditable(_bo);
        btn_ref.setEnabled(_bo);
        btn_clearbtn.setEnabled(_bo);
    }

    public Object getObject() {
        return getCurrRefItemVO(); // 应该是要取得整个HashVO!!!
    }

    public void setObject(Object _obj) {
        RefItemVO vo = (RefItemVO) _obj; //
        setCurrRefItemVO(vo); // 设置数据!!
        if (vo != null) {
            setRefID(vo.getId());
            setRefCode(vo.getCode());
            setRefName(vo.getName()); //
            setText(vo.getName());
        } else {
            reset();
        }
    }

    public void addPlutoListener(NovaEventListener _listener) {
        v_Listeners.add(_listener);
    }

    public void onValueChanged(NovaEvent _evt) {
        for (int i = 0; i < v_Listeners.size(); i++) {
            NovaEventListener listener = (NovaEventListener) v_Listeners.get(i);
            listener.onValueChanged(_evt); //
        }
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

    public JButton getBtn_clearbtn() {
        return btn_clearbtn;
    }

    public void setBtn_clearbtn(JButton btn_clearbtn) {
        this.btn_clearbtn = btn_clearbtn;
    }

    public void focus() {
        textField.requestFocus();
        textField.requestFocusInWindow();
    }

    public void setRefDesc(String _refDesc) {
        refDesc=_refDesc;
        initRefDesc();
    }

    public String getRefDesc() {
        return refDesc;
    }
    
    /**
     * 获得Lable的宽度
     * 2007.09.10 应陈学进邮件要求加入，代码实现：陈学进
     * @return the li_label_width
     */
    public int getLabelWidth(){
        return this.LABEL_WIDTH;
    }
    
    /**
     * 设置Lable的宽度
     * 2007.09.10 应陈学进邮件要求加入，代码实现：陈学进
     * @param _label_width the li_label_width to set
     */
    public void setLabelWidth(int _label_width){
        this.LABEL_WIDTH = _label_width;
        
    }

    /**
     * 获得文本框宽度
     * 2007.09.10 应陈学进邮件要求加入，代码实现：陈学进
     * @return the li_textfield_width
     */
    public int getTextfieldWidth(){
        return this.FIELD_WIDTH;
    }

     
    /**
     * 设置文本框宽度
     * 2007.09.10 应陈学进邮件要求加入，代码实现：陈学进
     * @param _textfield_width the li_textfield_width to set
     */
    public void setTextfieldWidth(int width) {
    	this.UREF_WIDTH = this.FIELD_WIDTH - REF_WIDTH- CLEAR_WIDTH;
    }
   
}
/**************************************************************************
 * $RCSfile: UIRefPanel.java,v $  $Revision: 1.20.2.12 $  $Date: 2010/05/05 14:02:37 $
 *
 * $Log: UIRefPanel.java,v $
 * Revision 1.20.2.12  2010/05/05 14:02:37  wangqi
 * *** empty log message ***
 *
 * Revision 1.20.2.11  2010/01/21 08:12:55  wangqi
 * *** empty log message ***
 *
 * Revision 1.20.2.10  2010/01/21 05:04:20  wangqi
 * *** empty log message ***
 *
 * Revision 1.20.2.9  2010/01/20 09:55:27  wangqi
 * *** empty log message ***
 *
 * Revision 1.20.2.8  2009/07/21 08:41:08  wangqi
 * *** empty log message ***
 *
 * Revision 1.20.2.7  2009/02/05 12:02:42  wangqi
 * *** empty log message ***
 *
 * Revision 1.20.2.6  2009/02/02 16:12:54  wangqi
 * *** empty log message ***
 *
 * Revision 1.20.2.5  2008/11/05 05:21:05  wangqi
 * *** empty log message ***
 *
 * Revision 1.4  2008/09/19 02:51:29  wangqi
 * *** empty log message ***
 *
 * Revision 1.3  2008/09/18 03:04:08  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2008/09/16 07:11:36  wangqi
 * *** empty log message ***
 *
 * Revision 1.1  2008/09/01 07:38:10  wangqi
 * *** empty log message ***
 *
 * Revision 1.21  2008/06/11 16:40:35  wangqi
 * *** empty log message ***
 *
 * Revision 1.20  2008/01/24 03:31:23  wangqi
 * 鲁健提交
 *
 * Revision 1.19  2008/01/15 09:03:51  wangqi
 * *** empty log message ***
 *
 * Revision 1.18  2007/12/19 04:56:00  wangqi
 * *** empty log message ***
 *
 * Revision 1.17  2007/11/28 05:35:18  wangqi
 * *** empty log message ***
 *
 * Revision 1.16  2007/09/11 01:41:40  wangqi
 * *** empty log message ***
 *
 * Revision 1.15  2007/09/04 05:54:32  yanghuan
 * 根据陈学进需求,更改变量属性为protected:refID、refCode、refName、textField、btn_ref、btn_clearbtn
 *
 * Revision 1.14  2007/09/03 06:36:11  yanghuan
 * private void onButtonClicked()方法的private属性更改为protected
 *
 * Revision 1.13  2007/07/23 11:34:28  sunxf
 * MR#:Nova20-15
 *
 * Revision 1.12  2007/07/11 11:10:38  sunxf
 * 增加MutiTree
 *
 * Revision 1.11  2007/06/28 07:43:45  qilin
 * no message
 *
 * Revision 1.10  2007/06/28 03:29:11  qilin
 * no message
 *
 * Revision 1.9  2007/06/28 03:15:52  qilin
 * no message
 *
 * Revision 1.8  2007/06/19 03:11:37  qilin
 * MR#:BZM10-58
 * 去掉数据源的显示
 *
 * Revision 1.7  2007/05/31 12:02:04  qilin
 * no message
 *
 * Revision 1.6  2007/05/31 07:38:19  qilin
 * code format
 *
 * Revision 1.5  2007/05/22 08:01:48  qilin
 * no message
 *
 * Revision 1.4  2007/05/22 06:48:38  lst
 * MR#:NM30-0000 remove focus event
 *
 * Revision 1.3  2007/05/22 02:03:34  sunxb
 * *** empty log message ***
 *
 * Revision 1.2  2007/05/21 08:26:21  sunxb
 * *** empty log message ***
 *
 * Revision 1.1  2007/05/17 06:01:06  qilin
 * no message
 *
 * Revision 1.7  2007/03/28 09:51:09  shxch
 * *** empty log message ***
 *
 * Revision 1.6  2007/03/15 03:34:50  shxch
 * *** empty log message ***
 *
 * Revision 1.5  2007/03/07 02:01:57  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/02/27 06:03:02  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/02/27 05:15:55  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 05:14:30  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
