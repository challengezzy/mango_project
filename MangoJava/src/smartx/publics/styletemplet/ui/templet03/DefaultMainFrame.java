/**************************************************************************
 * $RCSfile: DefaultMainFrame.java,v $  $Revision: 1.3.2.5 $  $Date: 2010/01/26 01:47:56 $
 **************************************************************************/
package smartx.publics.styletemplet.ui.templet03;

//单表—树卡

import java.util.HashMap;

import smartx.framework.common.ui.NovaClientEnvironment;
import smartx.framework.common.ui.UIUtil;
import smartx.framework.common.vo.FrameWorkTBUtil;
import smartx.framework.common.vo.HashVO;

public class DefaultMainFrame extends AbstractTempletFrame03 {

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

    public String getTempletcode() {
        return ( (String) _map.get("TEMPLET_CODE")).toUpperCase();
    }

    /**
     * 生成主sql
     */
    public String getTreeSQL() {
        String templetcode = ( (String) _map.get("TEMPLET_CODE")).toUpperCase();
        String sql = "select * from PUB_TEMPLET_1 WHERE TEMPLETCODE='" + templetcode + "'";
        HashVO[] vos = null;
        try {
            vos = UIUtil.getHashVoArrayByDS(null, sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        //条件子句
        String swhere = vos[0].getStringValue("DATACONSTRAINT");
        swhere=(swhere!=null)?new FrameWorkTBUtil().convertExpression(NovaClientEnvironment.getInstance(), swhere):"1=1";
        //加入了分级加载条件子句。
        swhere=" where "+swhere+" -=【 and "+str_treeparentpk+" _treeparent_ 】=-";
        return "select * from " + vos[0].getStringValue("TABLENAME") + swhere;
    }

    public String getTreePrimarykey() {
        return (String) _map.get("PK");
    }

    public String getTreeParentkey() {
        return (String) _map.get("PARENTPK");
    }

    public String[] getSys_Selection_Path() {
        return ( (String[]) _map.get("SYS_SELECTION_PATH"));
    }

    public String getCustomerpanel() {
        return (String) _map.get("CUSTOMERPANEL");
    }

    public String getRootTitle(){
    	return (String) _map.get("ROOTTITLE");
    }
    public String getTreeTitle() {
        return (String) _map.get("TREETITLE");
    }

    /**
     * 获得检索键
     * @return
     */
    public String getFetchKey(){
    	return (String) _map.get("FETCHKEY");
    }
    
    public String getUiIntercept() {
        return (String) _map.get("UIINTERCEPTOR");
    }

    public String getBsinterceptor() {
        return (String) _map.get("BSINTERCEPTOR");
    }

    public boolean isShowsystembutton() {
        if (_map.get("SHOWSYSBUTTON") == null) {
            return true;
        }
        return ( (String) _map.get("SHOWSYSBUTTON")).equals("是") ? true : false;
    }

    public boolean isCascadeonDelete() {
        if (_map.get("CASCADEONDELETE") == null) {
            return false;
        }
        return ( (String) _map.get("CASCADEONDELETE")).equals("是") ? true : false;
    }
    protected boolean isLoadAll()
    {
    	  if (_map.get("ISLOADALL") == null) {
              return true;
          }
          return ( (String) _map.get("ISLOADALL")).equals("是") ? true : false;
    }
}
/**************************************************************************
 * $RCSfile: DefaultMainFrame.java,v $  $Revision: 1.3.2.5 $  $Date: 2010/01/26 01:47:56 $
 *
 * $Log: DefaultMainFrame.java,v $
 * Revision 1.3.2.5  2010/01/26 01:47:56  wangqi
 * *** empty log message ***
 *
 * Revision 1.3.2.4  2010/01/05 10:20:37  wangqi
 * *** empty log message ***
 *
 * Revision 1.3.2.3  2009/12/21 02:57:33  wangqi
 * *** empty log message ***
 *
 * Revision 1.3.2.2  2009/09/01 08:25:34  yangjm
 * *** empty log message ***
 *
 * Revision 1.3.2.1  2008/08/28 09:15:52  wangqi
 * *** empty log message ***
 *
 * Revision 1.3  2008/01/15 09:03:22  wangqi
 * 孙雪峰修改MR
 *
 * Revision 1.2  2007/05/31 07:39:02  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:14:27  qilin
 * no message
 *
 * Revision 1.6  2007/03/12 04:45:31  sunxf
 * *** empty log message ***
 *
 * Revision 1.5  2007/03/07 02:01:57  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/03/05 09:59:13  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/02/27 06:03:03  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:48:33  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/