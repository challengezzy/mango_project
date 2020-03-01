package smartx.framework.metadata.lookandfeel;


import javax.swing.JApplet;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import smartx.framework.common.utils.Sys;
import smartx.framework.metadata.ui.NovaMessage;

public class Office2003LookAndFeel implements LookAndFeelIFC {

	public void updateUI() {
		JApplet applet=(JApplet)Sys.getInfo("applet");
		try {
			UIManager.setLookAndFeel("org.fife.plaf.Office2003.Office2003LookAndFeel");

			//以下为共用设置
			UIManager.put("swing.boldMetal", Boolean.valueOf(false));
    		//调用更新，可以在任意的位置更新
    		SwingUtilities.updateComponentTreeUI(applet.getContentPane());  
		} catch (Exception e) {
			 NovaMessage.show(applet, "调用插入风格切换失败！");
		} 
		
	}

}