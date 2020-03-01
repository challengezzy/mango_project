/**************************************************************************
 * $RCSfile: DefaultMainFrame.java,v $  $Revision: 1.2.8.2 $  $Date: 2009/12/21 02:57:33 $
 **************************************************************************/
package smartx.publics.styletemplet.ui.templet01;

import java.util.*;


/**
 * 风格模板1的默认实现，通过系统自动配置即执行本类
 * @author James.W
 *
 */
public class DefaultMainFrame extends AbstractTempletFrame01 {

    private static final long serialVersionUID = 1L;

    protected HashMap map = new HashMap(); //
    
    /**
     * 构造方法
     */
    public DefaultMainFrame(){
    	
    }

    /**
     * 构造方法
     * @param _map 界面定义参数
     */
    public DefaultMainFrame(HashMap _map) {
        super("" + _map.get("SYS_SELECTION_PATH").toString());
        this.map = _map; //
        init(); //
    }

    /**
     * 获得模板数据
     * @return
     */
    public String getTempletcode() {
        return (String) map.get("TEMPLET_CODE"); //
    }

    /**
     * 获得导航列表
     * @return
     */
    public String[] getSys_Selection_Path() {
        return (String[]) map.get("SYS_SELECTION_PATH"); //
    }

    /**
     * 获得自定义面板类定义
     * @return
     */
    public String getCustomerpanel() {
        return (String) map.get("CUSTOMERPANEL"); //
    }

    /**
     * 是否显示系统按钮
     * @return
     */
    public boolean isShowsystembutton() {
        if (map.get("SHOWSYSBUTTON") == null) {
            return true;
        }
        return ( (String) map.get("SHOWSYSBUTTON")).equals("是") ? true : false;
    }
    
    /**
     * 获得UI拦截器定义
     * @return
     */
    public String getUiinterceptor() {
        return (String) map.get("UIINTERCEPTOR"); //
    }

    /**
     * 获得BS拦截器定义
     * @return
     */
    public String getBsinterceptor() {
        return (String) map.get("BSINTERCEPTOR"); //
    }

    
}
/**************************************************************************
 * $RCSfile: DefaultMainFrame.java,v $  $Revision: 1.2.8.2 $  $Date: 2009/12/21 02:57:33 $
 *
 * $Log: DefaultMainFrame.java,v $
 * Revision 1.2.8.2  2009/12/21 02:57:33  wangqi
 * *** empty log message ***
 *
 * Revision 1.2.8.1  2009/12/16 04:13:50  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2007/05/31 07:39:01  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:14:26  qilin
 * no message
 *
 * Revision 1.3  2007/03/05 09:53:53  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:48:33  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/