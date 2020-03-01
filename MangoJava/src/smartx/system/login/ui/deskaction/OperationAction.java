/**********************************************************************
 *$RCSfile: OperationAction.java,v $  $Revision: 1.1.2.1 $  $Date: 2008/10/29 09:31:39 $
 *********************************************************************/ 
package smartx.system.login.ui.deskaction;


import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import smartx.framework.common.vo.HashVO;
import smartx.system.login.ui.DeskTopPanel;

/**
 * <li>Title: OperationAction.java</li>
 * <li>Description: 业务执行动作定义类</li>
 * <li>Project: NOVA2_MAINBRANCH</li>
 * <li>Copyright: Copyright (c) 2008</li>
 * @Company: GXLU. All Rights Reserved.
 * @author James.W
 * @version 1.0
 */
public class OperationAction extends AbstractAction {
	private HashVO _vo=null;
	private String[] _path=null;
	private DeskTopPanel _desk=null;
	
	
	public OperationAction(HashVO vo,String[] path,DeskTopPanel desk){
		//TODO 图标等属性
		//ImageIcon icon=
		//super(name,icon)
		super(vo.getStringValue("localname"));
		
		this._vo=vo;
		this._path=path;
		this._desk=desk;
		
	}
	
	/*public OperationAction(String name,String ico,DeskTopPanel desk){		
	}*/
	
	
	public void actionPerformed(ActionEvent e) {		
		this._desk.onCommandAction(this._vo,this._path);
	}
	
	
	

}

/**********************************************************************
 *$RCSfile: OperationAction.java,v $  $Revision: 1.1.2.1 $  $Date: 2008/10/29 09:31:39 $
 *
 *$Log: OperationAction.java,v $
 *Revision 1.1.2.1  2008/10/29 09:31:39  wangqi
 **** empty log message ***
 *
 *********************************************************************/