/**************************************************************************
 * $RCSfile: View_VO.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:24 $
 **************************************************************************/
package smartx.framework.metadata.ui.dictmanager;

import smartx.framework.common.vo.*;
import smartx.framework.metadata.vo.templetvo.*;

public class View_VO extends AbstractTempletVO {

    /**
     *
     */
    private static final long serialVersionUID = 2886608133011683450L;

    public HashVO getPub_templet_1Data() {
        HashVO vo = new HashVO(); //
        vo.setAttributeValue("templetcode", "view"); //
        vo.setAttributeValue("templetname", "view");
        vo.setAttributeValue("tablename", "DB_VIEW");
        vo.setAttributeValue("pkname", "TNAME");
        vo.setAttributeValue("pksequencename", null);
        vo.setAttributeValue("savedtablename", "DB_VIEW");
        vo.setAttributeValue("lastmodifytime", null);
        vo.setAttributeValue("ui_intercept", null);
        vo.setAttributeValue("bs_intercept", null);
        vo.setAttributeValue("ui_eventlistener", null);
        return vo;
    }

    public HashVO[] getPub_templet_1_itemData() {
        HashVO[] vos = new HashVO[2]; //

        vos[0] = new HashVO();
        vos[0].setAttributeValue("itemkey", "TNAME"); //
        vos[0].setAttributeValue("itemname", "表名");
        vos[0].setAttributeValue("itemtype", "文本框");
        vos[0].setAttributeValue("comboxdesc", null);
        vos[0].setAttributeValue("refdesc", null);
        vos[0].setAttributeValue("issave", "Y");
        vos[0].setAttributeValue("isdefaultquery", "N");
        vos[0].setAttributeValue("ismustinput", "N");
        vos[0].setAttributeValue("loadformula", null);
        vos[0].setAttributeValue("editformula", null);
        vos[0].setAttributeValue("defaultvalueformula", null);
        vos[0].setAttributeValue("colorformula", null);
        vos[0].setAttributeValue("showorder", "1");
        vos[0].setAttributeValue("listwidth", "150");
        vos[0].setAttributeValue("cardwidth", "150");
        vos[0].setAttributeValue("listisshowable", "Y");
        vos[0].setAttributeValue("listiseditable", "N");
        vos[0].setAttributeValue("cardisshowable", "Y");
        vos[0].setAttributeValue("cardiseditable", "N");
        vos[0].setAttributeValue("lastmodifytime", "Y");

        vos[1] = new HashVO();
        vos[1].setAttributeValue("itemkey", "COMMENTS"); //
        vos[1].setAttributeValue("itemname", "说明");
        vos[1].setAttributeValue("itemtype", "文本框");
        vos[1].setAttributeValue("comboxdesc", null);
        vos[1].setAttributeValue("refdesc", null);
        vos[1].setAttributeValue("issave", "Y");
        vos[1].setAttributeValue("isdefaultquery", "N");
        vos[1].setAttributeValue("ismustinput", "N");
        vos[1].setAttributeValue("loadformula", null);
        vos[1].setAttributeValue("editformula", null);
        vos[1].setAttributeValue("defaultvalueformula", null);
        vos[1].setAttributeValue("colorformula", null);
        vos[1].setAttributeValue("showorder", "1");
        vos[1].setAttributeValue("listwidth", "150");
        vos[1].setAttributeValue("cardwidth", "150");
        vos[1].setAttributeValue("listisshowable", "Y");
        vos[1].setAttributeValue("listiseditable", "Y");
        vos[1].setAttributeValue("cardisshowable", "Y");
        vos[1].setAttributeValue("cardiseditable", "Y");
        vos[1].setAttributeValue("lastmodifytime", "Y");

        return vos;
    }

}
/**************************************************************************
 * $RCSfile: View_VO.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:24 $
 *
 * $Log: View_VO.java,v $
 * Revision 1.2  2007/05/31 07:38:24  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:02:12  qilin
 * no message
 *
 * Revision 1.3  2007/02/27 06:03:00  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 05:01:56  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/