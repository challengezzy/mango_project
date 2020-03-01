/**************************************************************************
 * $RCSfile: DefaultMainFrame.java,v $  $Revision: 1.4.2.2 $  $Date: 2009/12/21 02:57:33 $
 **************************************************************************/
package smartx.publics.styletemplet.ui.templet02;

import java.util.*;

import javax.swing.*;

public class DefaultMainFrame extends AbstractTempletFrame02 {

    private static final long serialVersionUID = -7870282092481757876L; //

    protected JPanel parentPanel=null;
    protected HashMap map = new HashMap(); //

    /**
     * 构造方法
     */
    public DefaultMainFrame(){
    	
    }
    
    public DefaultMainFrame(HashMap _map) {
        super("" + _map.get("SYS_SELECTION_PATH").toString());
        this.map = _map; //
        init(); //
    }
    
    public DefaultMainFrame(JPanel caller,HashMap _map) {    	
    	super("" + _map.get("SYS_SELECTION_PATH").toString());
    	parentPanel=caller;
    	this.map = _map; //
        init(); //        
    }

    public JPanel getParentPanel() {
		return parentPanel;
	}

	public void setParentPanel(JPanel parentPanel) {
		this.parentPanel = parentPanel;
	}

	public String getTempletcode() {
        return (String) map.get("TEMPLET_CODE"); //
    }

    public String[] getSys_Selection_Path() {
        return (String[]) map.get("SYS_SELECTION_PATH"); //
    }

    public String getCustomerpanel() {
        return (String) map.get("CUSTOMERPANEL"); //
    }

    public String getUiinterceptor() {
        return (String) map.get("UIINTERCEPTOR"); //
    }

    public String getBsinterceptor() {
        return (String) map.get("BSINTERCEPTOR"); //
    }

    public boolean isShowsystembutton() {
        if (map.get("SHOWSYSBUTTON") == null) {
            return true;
        }
        return ( (String) map.get("SHOWSYSBUTTON")).equals("是") ? true : false;
    }
}
/**************************************************************************
 * $RCSfile: DefaultMainFrame.java,v $  $Revision: 1.4.2.2 $  $Date: 2009/12/21 02:57:33 $
 *
 * $Log: DefaultMainFrame.java,v $
 * Revision 1.4.2.2  2009/12/21 02:57:33  wangqi
 * *** empty log message ***
 *
 * Revision 1.4.2.1  2009/12/04 07:07:09  wangqi
 * *** empty log message ***
 *
 * Revision 1.4  2007/11/29 05:40:15  wangqi
 * *** empty log message ***
 *
 * Revision 1.3  2007/11/29 05:23:56  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2007/05/31 07:39:02  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:14:27  qilin
 * no message
 *
 * Revision 1.3  2007/03/05 09:59:14  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:48:30  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/