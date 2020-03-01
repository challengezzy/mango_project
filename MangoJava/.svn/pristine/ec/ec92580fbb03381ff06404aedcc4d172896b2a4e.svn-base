package smartx.framework.metadata.ui.componentscard;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import smartx.framework.common.ui.NovaConstants;
import smartx.framework.common.ui.UIUtil;
import smartx.framework.metadata.ui.NovaDialog;
import smartx.framework.metadata.ui.NovaEvent;
import smartx.framework.metadata.ui.NovaEventListener;
import smartx.framework.metadata.ui.NovaMessage;
import smartx.framework.metadata.vo.Pub_Templet_1_ItemVO;

/**
 * 数值查询控件
 * @author James.W
 *
 */
public class QueryNumericPanel extends AbstractNovaComponent implements INovaCompent {
	
	private static final long serialVersionUID = -4237294182484819459L;
	
	public static int TYPE_INTEGER = 0; //0- 整数型，纯由数字组成
	public static int TYPE_FLOAT = 1;   //1- 浮点型，由数字和一个小数点组成
	public static int TYPE_CODE = 2;    //2- 数字、英文字母、中划线、下划线组成
	
	protected int _type = TYPE_FLOAT;           //当前控件类型
	
	protected String REF_IMAGE="images/platform/detail.png"; //点选参照按钮图标
	protected String CLEAR_IMAGE = "images/platform/clear.gif";
    protected int ICO_WIDTH=18;   //点选参照按钮宽度
    protected int ICO_HEIGHT=18;  //点选参照按钮高度
    
    //控件宽度需要去掉两个按钮的宽度
	protected int REAL_FIELD_WIDTH=this.FIELD_WIDTH - this.ICO_WIDTH - this.ICO_HEIGHT;
	
	protected JTextField textfield = null;
	protected JButton btn = null;         //明细按钮
	protected JButton btn_clear = null;   //清除按钮
	
	protected QueryNumericItemPanel eqitem = null;  //==	
	protected QueryNumericItemPanel biitem = null;  //>	
	protected QueryNumericItemPanel smitem = null;  //<	
	protected QueryNumericItemPanel biitem2 = null; //>=	
	protected QueryNumericItemPanel smitem2 = null; //<=
	
	
	protected NovaDialog dialog=null;
	protected String str_result = null;
	
	protected Vector v_Listeners = new Vector();//事件跟踪列表
	
	/**
	 * 构造方法
	 * @param itemkey 域名
	 * @param itemname 域名称
	 * @param type 域类型。0- 整数型，纯由数字组成；1- 浮点型，由数字和一个小数点组成；2- 数字、英文字母、中划线、下划线组成。
	 */
	public QueryNumericPanel(String itemkey,String itemname, int type){
		this.key = itemkey;
		this.name = itemname;
		this._type = type;
		init();
	}
	
	/**
	 * 构造方法
	 * @param _vo 域定义VO
	 * @param type 域类型。0- 整数型，纯由数字组成；1- 浮点型，由数字和一个小数点组成；2- 数字、英文字母、中划线、下划线组成。
	 */
	public QueryNumericPanel(Pub_Templet_1_ItemVO _vo, int type){
		this._vo=_vo;
		this.key = this._vo.getItemkey();
		this.name = this._vo.getItemname();
		this._type = type;
		this.FIELD_WIDTH = this._vo.getCardwidth().intValue(); // 设置宽度
		REAL_FIELD_WIDTH = this.FIELD_WIDTH - this.ICO_WIDTH - this.ICO_HEIGHT;
		init();
	}
	
	
	
	//Override
	protected JComponent[] getFieldComponents() {
		textfield = new JTextField();
		textfield.setPreferredSize(new Dimension(this.REAL_FIELD_WIDTH,this.FIELD_HEIGHT));
		textfield.setEditable(false);

		//明细按钮
		btn= new JButton(UIUtil.getImage(this.REF_IMAGE));
		btn.setPreferredSize(new Dimension(this.ICO_WIDTH,this.ICO_HEIGHT));
		btn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				showDialog();
			}			
		});
		//清除按钮
		btn_clear = new JButton(UIUtil.getImage(this.CLEAR_IMAGE));
        btn_clear.setPreferredSize(new Dimension(this.ICO_WIDTH, this.ICO_HEIGHT));
        btn_clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	emptyField();                
            }

        });
		
		
        /**
         * TODO 增加编辑事件，填入和清空的时候出发，参考UIRefPanel
         *   fireValueChanged 
         */
		return new JComponent[]{textfield,btn,btn_clear};
	}
	
	
	private void showDialog(){
		Point rect = textfield.getLocationOnScreen();
		dialog= new NovaDialog(this,"请输入查询条件",true);
		dialog.getContentPane().setLayout(new BorderLayout());
		dialog.getContentPane().add(getDialogBtnPane(),BorderLayout.SOUTH);
		dialog.getContentPane().add(getDialogSelectPane(),BorderLayout.CENTER);
		dialog.setLocation(new Double(rect.getX()).intValue(), new Double(rect.getY()).intValue()+20);
		dialog.setSize(400,200);
		dialog.setVisible(true);
	}
	
	public void emptyField(){
    	reset(); //
        onValueChanged(new NovaEvent(this)); //
    }
	
	/**
	 * 增加控件编辑响应事件
	 * @param _listener
	 */
	public void addPlutoListener(NovaEventListener _listener) {
        v_Listeners.add(_listener);
    }

    public void onValueChanged(NovaEvent _evt) {
        for (int i = 0; i < v_Listeners.size(); i++) {
            NovaEventListener listener = (NovaEventListener) v_Listeners.get(i);
            listener.onValueChanged(_evt); //
        }
    }
	
	private void onConfirm()
	{
		StringBuffer result = new StringBuffer();//sql片段
		StringBuffer str_showtext = new StringBuffer();//界面描述片段
		
		String seqitem=eqitem.getValue();
		String sbiitem=biitem.getValue();
		String ssmitem=smitem.getValue();
		String sbiitem2=biitem2.getValue();
		String ssmitem2=smitem2.getValue();
		
		//等于某时间判断
		if(seqitem!=null){
			if(this._type==TYPE_CODE){
				result.append("and "+key+"= '"+seqitem+"' ");
				str_showtext.append(name).append("等于【").append(seqitem).append("】;");
			}else{
				result.append("and "+key+"="+seqitem+" ");
				str_showtext.append(name).append("等于【").append(seqitem).append("】;");
			}						
		}
		if(sbiitem!=null){
			if(this._type==TYPE_CODE){
				result.append("and "+key+"> '"+sbiitem+"' ");
				str_showtext.append(name).append("大于【").append(sbiitem).append("】;");
			}else{
				result.append("and "+key+"> "+sbiitem+" ");
				str_showtext.append(name).append("大于【").append(sbiitem).append("】;");
			}
		}
		if(ssmitem!=null){
			if(this._type==TYPE_CODE){
				result.append("and "+key+"< '"+ssmitem+"' ");
				str_showtext.append(name).append("小于【").append(ssmitem).append("】;");
			}else{
				result.append("and "+key+"< "+ssmitem+" ");
				str_showtext.append(name).append("小于【").append(ssmitem).append("】;");
			}
		}
		if(sbiitem2!=null){
			if(this._type==TYPE_CODE){
				result.append("and "+key+" >= '"+sbiitem2+"' ");
				str_showtext.append(name).append("大于等于【").append(sbiitem2).append("】;");
			}else{
				result.append("and "+key+" >= "+sbiitem2+" ");
				str_showtext.append(name).append("大于等于【").append(sbiitem2).append("】;");
			}			
		}
		if(ssmitem2!=null){
			if(this._type==TYPE_CODE){
				result.append("and "+key+" <= '"+ssmitem2+"' ");
				str_showtext.append(name).append("小于等于【").append(ssmitem2).append("】;");
			}else{
				result.append("and "+key+" <= "+ssmitem2+" ");
				str_showtext.append(name).append("小于等于【").append(ssmitem2).append("】;");
			}
		}
		

		/**
		 * 下面过程用于判断日期范围是否合法 begin
		 */
		if(seqitem!=null&&(sbiitem!=null||ssmitem!=null||sbiitem2!=null||ssmitem2!=null)){//==
			NovaMessage.show(this, "选择了【等于】就不能选择其他条件！", NovaConstants.MESSAGE_ERROR);				
		}else if((sbiitem!=null&&sbiitem2!=null)||(ssmitem!=null&&ssmitem2!=null)){
			NovaMessage.show(this, "选择了【大于】就不能选择【大于等于】，同样【小于】和【小于等于】不能同时选择！", NovaConstants.MESSAGE_ERROR);
		}else if(sbiitem!=null&&ssmitem!=null&&sbiitem.compareTo(ssmitem)>=0){
			NovaMessage.show(this, "选择了【大于】和【小于】条件，条件不能颠倒或者相等！", NovaConstants.MESSAGE_ERROR);			
		}else if(sbiitem!=null&&ssmitem2!=null&&sbiitem.compareTo(ssmitem2)>0){
			NovaMessage.show(this, "选择了【大于】和【小于等于】条件，条件不能颠倒！", NovaConstants.MESSAGE_ERROR);
		}else if(sbiitem2!=null&&ssmitem!=null&&sbiitem2.compareTo(ssmitem)>=0){
			NovaMessage.show(this, "选择了【大于等于】和【小于】条件，条件不能颠倒或者相等！", NovaConstants.MESSAGE_ERROR);
		}else if(sbiitem2!=null&&ssmitem2!=null&&sbiitem2.compareTo(ssmitem2)>0){
			NovaMessage.show(this, "选择了【大于等于】和【小于等于】条件，条件不能颠倒！", NovaConstants.MESSAGE_ERROR);
		}else{
			String rt=result.toString();
			if(rt.trim().equals("")){
				textfield.setText("");
				textfield.setToolTipText(null);
				this.setValue(null);				
			}else{
				textfield.setText(str_showtext.toString());
				textfield.setToolTipText(str_showtext.toString());
				setValue(result.toString().substring(3));
			}
			dialog.dispose();
		}
	}
	
	
	
	private void onCancle()	{
		this.reset();
		dialog.dispose();
	}
	
	/**
	 * 明细界面上的确认按钮
	 * @return
	 */
	private JPanel getDialogBtnPane(){
		JPanel rpanel = new JPanel();
		rpanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		JButton ok_btn = new JButton("确定");
		ok_btn.setPreferredSize(new Dimension(60,20));
		ok_btn.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				
				onConfirm();
			}
			
			
		});
		JButton cancel_btn = new JButton("取消");
		cancel_btn.setPreferredSize(new Dimension(60,20));
		cancel_btn.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				onCancle();
			}
		});
		rpanel.add(ok_btn);
		rpanel.add(cancel_btn);
		return rpanel;
	}
	
	/**
	 * 明细界面上的明细条件，多个条件打勾选择
	 * @return
	 */
	private JPanel getDialogSelectPane(){
		JPanel rpanel = new JPanel();
		rpanel.setLayout(new BoxLayout(rpanel,BoxLayout.Y_AXIS));
		
		eqitem = new QueryNumericItemPanel("等于",key,name,this._type);
		biitem = new QueryNumericItemPanel("大于",key,name,this._type);
		smitem = new QueryNumericItemPanel("小于",key,name,this._type);
		biitem2 = new QueryNumericItemPanel("大于等于",key,name,this._type);
		smitem2 = new QueryNumericItemPanel("小于等于",key,name,this._type);
		rpanel.add(eqitem);
		rpanel.add(biitem);
		rpanel.add(smitem);
		rpanel.add(biitem2);
		rpanel.add(smitem2);
		return rpanel;
	}
	
	
	public void focus() {
		
	}

	
	public Object getObject() {
		return str_result;
	}

	public String getValue() {
		return str_result;
	}

	public void reset() {
		textfield.setText("");
		textfield.setToolTipText(null);
		str_result=null;
	}

	public void setEditable(boolean _bo) {
		btn.setEnabled(_bo);
	}

	public void setObject(Object _obj) {
		setValue(String.valueOf(_obj));
	}

	public void setValue(String _value) {
		str_result = _value;
	}
	
	//add by James.W to get real value
	public String getInputValue(){		
		return str_result;
	}
	
}
