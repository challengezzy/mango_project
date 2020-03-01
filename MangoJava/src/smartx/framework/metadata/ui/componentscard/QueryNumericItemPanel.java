package smartx.framework.metadata.ui.componentscard;


import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import smartx.framework.metadata.vo.Pub_Templet_1_ItemVO;
/**
 * 查询数值条件控件
 * @author James.W
 */
public class QueryNumericItemPanel extends JPanel {
	private JCheckBox checkbox = null;
	private int type;  //控件类型
	private AbstractNovaComponent _comp = null;
	private Pub_Templet_1_ItemVO itemvo = null;
	private String key = null;
	private String name = null;
	private String text = null;
	
	/**
	 * 数值条件控件
	 * @param text 显示文本
	 * @param itemvo 控件定义VO
	 * @param type 数值类型。
	 *             QueryNumericPanel.TYPE_INTEGER - 整数型，纯由数字组成；
	 *             QueryNumericPanel.TYPE_FLOAT - 浮点型，由数字和一个小数点组成；
	 *             QueryNumericPanel.TYPE_CODE - 数字、英文字母、中划线、下划线组成。
	 */
	public QueryNumericItemPanel(String text,Pub_Templet_1_ItemVO itemvo, int type) {
		this.text = text;
		this.itemvo = itemvo;
		this.type = type;
		init();
	}
	
	/**
	 * 数值条件控件
	 * @param text 显示文本
	 * @param key 域名
	 * @param name 域名称
	 * @param type 数值类型。<br/>
	 *             QueryNumericPanel.TYPE_INTEGER - 整数型，纯由数字组成；<br/>
	 *             QueryNumericPanel.TYPE_FLOAT - 浮点型，由数字和一个小数点组成；<br/>
	 *             QueryNumericPanel.TYPE_CODE - 数字、英文字母、中划线、下划线组成。
	 */
	public QueryNumericItemPanel(String text,String key,String name, int type) {
		this.text = text;
		this.key = key;
		this.name = name;
		this.type = type;
		init();
	}
	
	/**
	 * 初始化
	 */
	private void init() {
		this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		this.add(getCheckBox());
		this.add(getPanel());
	}

	
	/**
	 * 根据指定类型创建控件面板
	 * @return
	 */
	private AbstractNovaComponent getPanel() {
		if (itemvo != null)
			_comp= new NumberFieldPanel(itemvo, this.type, -1);
		else
			_comp= new NumberFieldPanel(key, name, this.type, -1);		
		
		return _comp;
	}
	
	
	public boolean isCheck(){
		return checkbox.isSelected();
	}
	
	public String getValue(){
		return isCheck()?_comp.getValue():null;
	}
	
	private JCheckBox getCheckBox()
	{
		checkbox = new JCheckBox(text);
		checkbox.setPreferredSize(new Dimension(100,20));
		checkbox.setBackground(Color.WHITE);
		return checkbox;
	}
}
