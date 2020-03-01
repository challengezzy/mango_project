package smartx.framework.metadata.ui.componentscard;


import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import smartx.framework.metadata.vo.Pub_Templet_1_ItemVO;
/**
 * 查询日历控件
 * @author sunxf
 *
 * 2007 7:02:27 PM
 */
public class QueryCalendarItemPanel extends JPanel {
	private JCheckBox checkbox = null;
	private int type;
	private INovaCompent value_comp = null;
	private Pub_Templet_1_ItemVO itemvo = null;
	private String key = null;
	private String name = null;
	private String text = null;
	public QueryCalendarItemPanel(String text,Pub_Templet_1_ItemVO itemvo, int type) {
		this.text = text;
		this.itemvo = itemvo;
		this.type = type;
		init();
	}
	public QueryCalendarItemPanel(String text,String key,String name, int type) {
		this.text = text;
		this.key = key;
		this.name = name;
		this.type = type;
		init();
	}
	private void init() {
//		this.setPreferredSize(new Dimension(400,20));
		this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		this.add(getCheckBox());
		this.add((JPanel)getTimePanel());
	}

	private INovaCompent getTimePanel() {
		if (type == QueryCalendarPanel.TYPE_DATE) {
			if (itemvo != null)
				value_comp = new UIDateTimePanel(itemvo);
			else
				value_comp = new UIDateTimePanel(key,name);
		}
		else 
		{
			if (itemvo != null)
				value_comp = new UITimeSetPanel(itemvo);
			else
				value_comp = new UITimeSetPanel(key,name);
		}
		return value_comp;
	}
	public boolean isCheck()
	{
		return checkbox.isSelected();
	}
	public String getTime()
	{
		return value_comp.getValue();
	}
	private JCheckBox getCheckBox()
	{
		checkbox = new JCheckBox(text);
		checkbox.setPreferredSize(new Dimension(100,20));
		checkbox.setBackground(Color.WHITE);
		return checkbox;
	}
}
