/******************************************************************
*
*$RCSfile: ClientPanelInitInterface.java,v $ $Revision: 1.2 $ $Author: qilin $ $Date: 2007/09/27 05:37:14 $
*
*$Log: ClientPanelInitInterface.java,v $
*Revision 1.2  2007/09/27 05:37:14  qilin
*no message
*
*Revision 1.1  2007/07/11 03:19:49  qilin
*为当前系统启动另一个nova系统所作的修改
*
*
********************************************************************/

package smartx.system.login.ui;

import java.util.HashMap;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public interface ClientPanelInitInterface {
    public void setDeskTopPanel(DeskTopPanel panel);
    public void init();

    public HashMap getMenuAppendString();
}
