/**
 * $RCSfile: NovaLookAndFeel.java,v $  $Revision: 1.1.2.1 $  $Date: 2010/01/13 02:27:13 $
 */
package smartx.framework.metadata.lookandfeel;


import java.awt.Component;

import javax.swing.JApplet;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import smartx.framework.common.utils.Sys;
import smartx.framework.metadata.ui.NovaMessage;

/**
 * 系统默认的LoolAndFeel
 * @author James.W
 *
 */
public class NovaLookAndFeel implements LookAndFeelIFC {

	/* (non-Javadoc)
	 * @see smartx.framework.common.ui.lookandfeel.LookAndFeelIFC#updateUI()
	 */
	public void updateUI() {
		JApplet applet=(JApplet)Sys.getInfo("applet");
		try {
			//系统默认风格 
			UIManager.setLookAndFeel("smartx.framework.common.ui.NovaLookAndFeel");
			
			//以下为共用设置
			UIManager.put("swing.boldMetal", Boolean.valueOf(false));
    		//调用更新，可以在任意的位置更新
			SwingUtilities.updateComponentTreeUI(applet.getContentPane());   		
    		
	    } catch (Exception e) {
	        NovaMessage.show(applet, "调用插入风格切换失败！");
	    }

	}

}
