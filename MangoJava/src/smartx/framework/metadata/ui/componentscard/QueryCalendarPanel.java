package smartx.framework.metadata.ui.componentscard;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import smartx.framework.common.ui.NovaConstants;
import smartx.framework.common.ui.UIUtil;
import smartx.framework.metadata.ui.NovaDialog;
import smartx.framework.metadata.ui.NovaEvent;
import smartx.framework.metadata.ui.NovaEventListener;
import smartx.framework.metadata.ui.NovaMessage;
import smartx.framework.metadata.vo.Pub_Templet_1_ItemVO;
/**
 * 查询日历控件
 * @author sunxf
 *
 * 2007 7:02:27 PM
 */
public class QueryCalendarPanel extends AbstractNovaComponent implements INovaCompent {
	public static int TYPE_DATE = 0;
	public static int TYPE_TIME = 1;
	private int cal_type = 0;  //日历类型 TODO 如何使用？代码里面没有真正用到这个变量
	
	protected String REF_IMAGE="images/platform/detail.png"; //点选参照按钮图标
	protected String CLEAR_IMAGE = "images/platform/clear.gif";
    protected int REF_WIDTH=18;   //点选参照按钮宽度
    protected int REF_HEIGHT=18;  //点选参照按钮高度
    //控件宽度需要去掉两个按钮的宽度
	protected int CALENDAR_WIDTH=this.FIELD_WIDTH - this.REF_WIDTH - this.REF_WIDTH;
	
	private JTextField textfield = null;
	private JButton btn = null;        //明细按钮
	private JButton btn_clear = null;  //清除按钮
	
	
	private QueryCalendarItemPanel eqitem = null;//==
	private QueryCalendarItemPanel biitem = null;//>
	private QueryCalendarItemPanel smitem = null;//<
	private QueryCalendarItemPanel biitem2 = null;//>=
	private QueryCalendarItemPanel smitem2 = null;//<=
	
	
	private NovaDialog dialog=null;
	private String str_result = null;
	
	
	protected Vector v_Listeners = new Vector();
	
	/**
	 * 
	 * @type 类型 QueryCalendarPanel.TYPE_DATE、QueryCalendarPanel.TYPE_TIME
	 */
	public QueryCalendarPanel(String itemkey,String itemname,int type){
		this.key = itemkey;
		this.name = itemname;
		this.cal_type = type;
		init();
	}
	/**
	 * 
	 * @param _vo
	 * @param type 类型 QueryCalendarPanel.TYPE_DATE、QueryCalendarPanel.TYPE_TIME
	 */
	public QueryCalendarPanel(Pub_Templet_1_ItemVO _vo,int type)
	{
		this._vo=_vo;
		this.key = this._vo.getItemkey();
		this.name = this._vo.getItemname();
		this.cal_type = type;
		this.FIELD_WIDTH = this._vo.getCardwidth().intValue(); // 设置宽度
		CALENDAR_WIDTH = this.FIELD_WIDTH - this.REF_WIDTH - this.REF_WIDTH;
		init();
	}
	
	
	
	//Override
	protected JComponent[] getFieldComponents() {
		textfield = new JTextField();
		textfield.setPreferredSize(new Dimension(this.CALENDAR_WIDTH,this.FIELD_HEIGHT));
		textfield.setEditable(false);

		btn= new JButton(UIUtil.getImage("images/platform/date.jpg"));
		btn.setPreferredSize(new Dimension(this.REF_WIDTH,this.REF_HEIGHT));
		btn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				showDialog();
			}			
		});
        
		btn_clear = new JButton(UIUtil.getImage(this.CLEAR_IMAGE)); // 清除按钮
        btn_clear.setPreferredSize(new Dimension(this.REF_WIDTH, this.REF_HEIGHT)); // 按扭的宽度与高度
        btn_clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	emptyField();                
            }

        });
		
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
		StringBuffer result = new StringBuffer();
		StringBuffer str_showtext = new StringBuffer(); 
		
		//时间格式代码
		String pdate=(this.cal_type==TYPE_TIME)?"YYYY-MM-DD HH24:MI:SS":"YYYY-MM-DD";
		
		String seqitem=null;
		String sbiitem=null;
		String ssmitem=null;
		String sbiitem2=null;
		String ssmitem2=null;
		//等于某时间判断
		if(eqitem.isCheck()&&!eqitem.getTime().equals("")){
			seqitem=eqitem.getTime();
			result.append("and "+key+"=to_date('"+seqitem+"','"+pdate+"') ");
			str_showtext.append(name).append("等于【").append(seqitem).append("】;");			
		}
		if(biitem.isCheck()&&!biitem.getTime().equals("")){
			sbiitem=biitem.getTime();
			result.append("and "+key+"> to_date('"+sbiitem+"','"+pdate+"') ");
			str_showtext.append(name).append("大于【").append(sbiitem).append("】;");
		}
		if(smitem.isCheck()&&!smitem.getTime().equals("")){
			ssmitem=smitem.getTime();
			result.append("and "+key+"< to_date('"+ssmitem+"','"+pdate+"') ");
			str_showtext.append(name).append("小于【").append(ssmitem).append("】;");
		}
		if(biitem2.isCheck()&&!biitem2.getTime().equals("")){
			sbiitem2=biitem2.getTime().toString();
			result.append("and "+key+" >= to_date('"+sbiitem2+"','"+pdate+"') ");
			str_showtext.append(name).append("大于等于【").append(sbiitem2).append("】;");
		}
		if(smitem2.isCheck()&&!smitem2.getTime().equals("")){
			ssmitem2=smitem2.getTime().toString();
			result.append("and "+key+" <= to_date('"+ssmitem2+"','"+pdate+"') ");
			str_showtext.append(name).append("小于等于【").append(ssmitem2).append("】;");
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
				textfield.setText(null);
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
	private JPanel getDialogSelectPane(){
		JPanel rpanel = new JPanel();
		rpanel.setLayout(new BoxLayout(rpanel,BoxLayout.Y_AXIS));
		if(this.cal_type==TYPE_TIME){
			eqitem = new QueryCalendarItemPanel("等于",key,name,TYPE_TIME);
			biitem = new QueryCalendarItemPanel("大于",key,name,TYPE_TIME);
			smitem = new QueryCalendarItemPanel("小于",key,name,TYPE_TIME);
			biitem2 = new QueryCalendarItemPanel("大于等于",key,name,TYPE_TIME);
			smitem2 = new QueryCalendarItemPanel("小于等于",key,name,TYPE_TIME);
		}else{
			eqitem = new QueryCalendarItemPanel("等于",key,name,TYPE_DATE);
			biitem = new QueryCalendarItemPanel("大于",key,name,TYPE_DATE);
			smitem = new QueryCalendarItemPanel("小于",key,name,TYPE_DATE);
			biitem2 = new QueryCalendarItemPanel("大于等于",key,name,TYPE_DATE);
			smitem2 = new QueryCalendarItemPanel("小于等于",key,name,TYPE_DATE);
		}
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
