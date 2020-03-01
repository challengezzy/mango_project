/**************************************************************************
 * $RCSfile: DefaultMainFrame.java,v $  $Revision: 1.2.8.1 $  $Date: 2009/12/21 02:57:33 $
 **************************************************************************/
package smartx.publics.styletemplet.ui.templet10;

import java.util.*;

/**
 * 第10种模板,即主子孙表,三张表一起保存
 * 三个表,第二张表有个外键指向主表主键,第三张表有个外键指向第二张表的主键
 * @author user
 *
 */
public class DefaultMainFrame extends AbstractTempletFrame {

    private static final long serialVersionUID = 1L;
    protected HashMap _map = new HashMap(); //
    
    /**
     * 构造方法
     */
    public DefaultMainFrame(){
    	
    }
    
    public DefaultMainFrame(HashMap _map) {
        super("" + _map.get("SYS_SELECTION_PATH").toString());
        this._map = _map;
        init();
    }

    public String getChildTableFK() {
        return (String) _map.get("CHILD_FORPKNAME"); // 子表外键
    }

    public String getChildTablePK() {
        return (String) _map.get("CHILD_PKNAME");
    }

    public String getChildTableTempletcode() {
        return (String) _map.get("CHILDTEMPLETE_CODE");
    }

    public String getGrandChildTableFK() {
        return (String) _map.get("GRANDCHILD_FORPKNAME");
    }

    public String getGrandChildTablePK() {
        return (String) _map.get("GRANDCHILD_PKNAME");
    }

    public String getGrandChildTableTempletcode() {
        return (String) _map.get("GRANDCHILDTEMPLETE_CODE"); // 孙表代码
    }

    public String getParentTablePK() {
        return (String) _map.get("PARENT_PKNAME");
    }

    public String getParentTableTempletcode() {
        return (String) _map.get("PARENTTEMPLETE_CODE");
    }

    public String[] getSys_Selection_Path() {
        return (String[]) _map.get("SYS_SELECTION_PATH"); //
    }

    public String getCustomerpanel() {
        return (String) _map.get("CUSTOMERPANEL"); //
    }

    public String getUiinterceptor() {
        return (String) _map.get("UIINTERCEPTOR"); //
    }

    public String getBsinterceptor() {
        return (String) _map.get("BSINTERCEPTOR"); //
    }

    public boolean isShowsystembutton() {
        if (_map.get("SHOWSYSBUTTON") == null) {
            return true;
        }
        return ( (String) _map.get("SHOWSYSBUTTON")).equals("是") ? true : false;
    }
}
/**************************************************************************
 * $RCSfile: DefaultMainFrame.java,v $  $Revision: 1.2.8.1 $  $Date: 2009/12/21 02:57:33 $
 *
 * $Log: DefaultMainFrame.java,v $
 * Revision 1.2.8.1  2009/12/21 02:57:33  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2007/05/31 07:39:01  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:14:29  qilin
 * no message
 *
 * Revision 1.2  2007/01/30 04:48:32  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/