/**********************************************************************
 *$RCSfile: SysReloginAction.java,v $  $Revision: 1.1.2.1 $  $Date: 2008/10/29 09:31:39 $
 *********************************************************************/ 
package smartx.system.login.ui.deskaction;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;

/**
 * <li>Title: SysReloginAction.java</li>
 * <li>Description: 简介</li>
 * <li>Project: NOVA2_MAINBRANCH</li>
 * <li>Copyright: Copyright (c) 2008</li>
 * @Company: GXLU. All Rights Reserved.
 * @author James.W
 * @version 1.0
 */
public class SysReloginAction extends AbstractAction {
	
	
	/**
	 * 创建重新登录响应
	 * @param name
	 * @param shortdesc
	 * @param longdesc
	 * @param ico
	 */
	public SysReloginAction(String name,String shortdesc,String longdesc,String ico){
		
		
	}
	
	public void actionPerformed(ActionEvent e) {
		Object o=e.getSource();
		if(o instanceof JComponent){
			
		}

	}

}

/**********************************************************************
 *$RCSfile: SysReloginAction.java,v $  $Revision: 1.1.2.1 $  $Date: 2008/10/29 09:31:39 $
 *
 *$Log: SysReloginAction.java,v $
 *Revision 1.1.2.1  2008/10/29 09:31:39  wangqi
 **** empty log message ***
 *
 *********************************************************************/