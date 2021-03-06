package smartx.framework.metadata.lookandfeel;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import smartx.framework.metadata.ui.NovaDialog;

/**
 * 风格选择界面
 * @author gxlu-user
 *
 */
public class ChangeLookAndFeelDialog extends NovaDialog{	
	private static final long serialVersionUID = 4806564684569287746L;
	private final static int WEIGHT = 400;
	private final static int HEIGHT = 110;
	public ChangeLookAndFeelDialog(){
		super();
		init();
	} 
	public ChangeLookAndFeelDialog(JComponent parent,String titleStr,boolean modal){
		super(parent,titleStr, modal);
		init();
	}
	
	private void init(){
		LookAndFeelLocalSetting lfSetting= new LookAndFeelLocalSetting();
		String curLF = lfSetting.getLookAndFeel();
		final String[][] sysLFs=lfSetting.getSysLookAndFeels();
		
		Border loweredetched = BorderFactory.createEmptyBorder(2,5,3,5);
		((JPanel)this.getContentPane()).setBorder(loweredetched);
		this.getContentPane().setLayout(new BorderLayout());
		
		TitledBorder titledBorder = new TitledBorder("展现风格选择");
		titledBorder.setTitleFont(new Font("宋体", Font.PLAIN, 12));
		titledBorder.setTitleColor(Color.BLUE);
		
		JLabel lbltitle = new JLabel("可选风格:");
		
		final JComboBox jcomboBox = new JComboBox();
		jcomboBox.setPreferredSize(new Dimension(280,20));		
		
		int lie=-1;
		for (int i = 0; i < sysLFs.length; i++) {
			jcomboBox.addItem(sysLFs[i][0]);
			
			//设置当前客户端历史选择的风格
			if(sysLFs[i][1].equals(curLF)){
				lie=i;
			}
		}
		
		//设置当前客户端历史选择的风格
		if(lie>=0){
			jcomboBox.setSelectedIndex(lie);
		}
		
				
		JPanel paneUp = new JPanel(new FlowLayout(FlowLayout.LEFT));
		lbltitle.setPreferredSize(new Dimension(60,20));
		paneUp.setBorder(titledBorder);
		paneUp.add(lbltitle);
		paneUp.add(jcomboBox);
		
		JButton btn_Confirm = new JButton("确定");
		btn_Confirm.setPreferredSize(new Dimension(80,20));
		JButton btn_Cancel = new JButton("取消");
		btn_Cancel.setPreferredSize(new Dimension(80,20));
		JPanel panelDown = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panelDown.add(btn_Confirm);
		panelDown.add(btn_Cancel);		
		btn_Confirm.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {	
				int sel=jcomboBox.getSelectedIndex();
				if(sel>=0){
					new LookAndFeelLocalSetting().setLookAndFeel(sysLFs[sel][1]);
				}
				ChangeLookAndFeelDialog.this.dispose();
				
			}
		});
		btn_Cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				ChangeLookAndFeelDialog.this.dispose();
			}			
		});
		
		
		this.getContentPane().add(paneUp,BorderLayout.CENTER);
		this.getContentPane().add(panelDown,BorderLayout.SOUTH);
		this.setSize(new Dimension(WEIGHT,HEIGHT));
		this.centerDialog();
		this.setResizable(false);
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		new ChangeLookAndFeelDialog(null,"测试",true);
	}
	
}
