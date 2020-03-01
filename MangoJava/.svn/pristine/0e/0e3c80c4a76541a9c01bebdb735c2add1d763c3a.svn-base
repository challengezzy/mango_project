/**************************************************************************
 * $RCSfile: DefaultMainFrame.java,v $  $Revision: 1.2.8.1 $  $Date: 2009/12/21 02:57:33 $
 **************************************************************************/
package smartx.publics.styletemplet.ui.templet06;

import java.util.*;

public class DefaultMainFrame extends AbstractTempletFrame06 {
	protected HashMap map = new HashMap(); //
    

    /**
     * 构造方法
     */
    public DefaultMainFrame(){
    	
    }
    
    public DefaultMainFrame(HashMap _map) {
        super("" + _map.get("SYS_SELECTION_PATH").toString());
        this.map = _map;
        init();
    }
    
    public int getOrientation() {
        return new Integer( ( (String) map.get("DIRECTION")).toUpperCase()).intValue();
    }

    public String getPriTableAssocField() {
        return ( (String) map.get("PRIFIELD")).toUpperCase();
    }

    public String getPriTableTempletcode() {
        return ( (String) map.get("PRITABLENAME")).toUpperCase();
    }

    public String getSubTableAssocField() {
        return ( (String) map.get("SUBFIELD")).toUpperCase();
    }

    public String getSubTableTempletcode() {
        return ( (String) map.get("SUBTABLENAME")).toUpperCase();
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
 * $RCSfile: DefaultMainFrame.java,v $  $Revision: 1.2.8.1 $  $Date: 2009/12/21 02:57:33 $
 *
 * $Log: DefaultMainFrame.java,v $
 * Revision 1.2.8.1  2009/12/21 02:57:33  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2007/05/31 07:39:04  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:14:28  qilin
 * no message
 *
 * Revision 1.3  2007/03/05 09:59:12  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:48:32  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/