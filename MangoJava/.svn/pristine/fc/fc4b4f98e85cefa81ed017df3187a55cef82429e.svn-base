/**************************************************************************
 * $RCSfile: DefaultMainFrame.java,v $  $Revision: 1.2.8.1 $  $Date: 2009/12/21 02:57:33 $
 **************************************************************************/
package smartx.publics.styletemplet.ui.templet08;

import java.util.*;

/**
 * 第8种模板,即主子表模板!!!
 * 主表是卡片与下个星期表的多页签,子表是列表
 * @author sunxf
 */
public class DefaultMainFrame extends AbstractTempletFrame08 {

    private static final long serialVersionUID = 1L;
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

    public String getChildTableFK() {
        return (String) map.get("CHILD_FORPKNAME");
    }

    public String getChildTablePK() {
        return (String) map.get("CHILD_PKNAME");
    }

    public String getChildTableTempletcode() {
        return (String) map.get("CHILDTEMPLETE_CODE");
    }

    public String getParentTablePK() {
        return (String) map.get("PARENT_PKNAME");
    }

    public String getParentTableTempletcode() {
        return (String) map.get("PARENTTEMPLETE_CODE");
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
 * Revision 1.3  2007/03/05 09:59:14  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:48:33  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/