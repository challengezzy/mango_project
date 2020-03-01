/**************************************************************************
 * $RCSfile: TableSequences_VO.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:24 $
 **************************************************************************/
package smartx.framework.metadata.ui.dictmanager;

import smartx.framework.common.vo.*;
import smartx.framework.metadata.vo.templetvo.*;

public class TableSequences_VO extends AbstractTempletVO {

    /**
     *
     */
    private static final long serialVersionUID = -8736539743771349601L;

    private static int VO_LENGTH = 8;

    public TableSequences_VO() {
    }

    public TableSequences_VO(String _type) {
    }

    public HashVO getPub_templet_1Data() {
        HashVO vo = new HashVO(); //
        vo.setAttributeValue("templetcode", "tableSequences"); //
        vo.setAttributeValue("templetname", "tableSequences");
        vo.setAttributeValue("tablename", "USER_SEQUENCES");
        vo.setAttributeValue("pkname", "SEQUENCE_NAME");
        vo.setAttributeValue("pksequencename", null);
        vo.setAttributeValue("savedtablename", "USER_SEQUENCES");
        vo.setAttributeValue("lastmodifytime", null);
        vo.setAttributeValue("ui_intercept", null);
        vo.setAttributeValue("bs_intercept", null);
        vo.setAttributeValue("ui_eventlistener", null);
        return vo;
    }

    public HashVO[] getPub_templet_1_itemData() {
        HashVO[] vos = new HashVO[VO_LENGTH]; //
        int li_index = 0;

        vos[li_index] = new HashVO();
        vos[li_index].setAttributeValue("itemkey", "SEQUENCE_NAME"); //
        vos[li_index].setAttributeValue("itemname", "序列名称");
        vos[li_index].setAttributeValue("itemtype", "文本框");
        vos[li_index].setAttributeValue("comboxdesc", null);
        vos[li_index].setAttributeValue("refdesc", null);
        vos[li_index].setAttributeValue("issave", "Y");
        vos[li_index].setAttributeValue("isdefaultquery", "N");
        vos[li_index].setAttributeValue("ismustinput", "N");
        vos[li_index].setAttributeValue("loadformula", null);
        vos[li_index].setAttributeValue("editformula", null);
        vos[li_index].setAttributeValue("defaultvalueformula", null);
        vos[li_index].setAttributeValue("colorformula", null);
        vos[li_index].setAttributeValue("showorder", "1");
        vos[li_index].setAttributeValue("listwidth", "100");
        vos[li_index].setAttributeValue("cardwidth", "100");
        vos[li_index].setAttributeValue("listisshowable", "Y");
        vos[li_index].setAttributeValue("listiseditable", "N");
        vos[li_index].setAttributeValue("cardisshowable", "Y");
        vos[li_index].setAttributeValue("cardiseditable", "N");
        vos[li_index].setAttributeValue("lastmodifytime", "Y");
        li_index++;

        vos[li_index] = new HashVO();
        vos[li_index].setAttributeValue("itemkey", "MIN_VALUE"); //
        vos[li_index].setAttributeValue("itemname", "最小值");
        vos[li_index].setAttributeValue("itemtype", "文本框");
        vos[li_index].setAttributeValue("comboxdesc", null);
        vos[li_index].setAttributeValue("refdesc", null);
        vos[li_index].setAttributeValue("issave", "Y");
        vos[li_index].setAttributeValue("isdefaultquery", "N");
        vos[li_index].setAttributeValue("ismustinput", "N");
        vos[li_index].setAttributeValue("loadformula", null);
        vos[li_index].setAttributeValue("editformula", null);
        vos[li_index].setAttributeValue("defaultvalueformula", null);
        vos[li_index].setAttributeValue("colorformula", null);
        vos[li_index].setAttributeValue("showorder", "1");
        vos[li_index].setAttributeValue("listwidth", "70");
        vos[li_index].setAttributeValue("cardwidth", "70");
        vos[li_index].setAttributeValue("listisshowable", "Y");
        vos[li_index].setAttributeValue("listiseditable", "Y");
        vos[li_index].setAttributeValue("cardisshowable", "Y");
        vos[li_index].setAttributeValue("cardiseditable", "Y");
        vos[li_index].setAttributeValue("lastmodifytime", "Y");
        li_index++;

        vos[li_index] = new HashVO();
        vos[li_index].setAttributeValue("itemkey", "MAX_VALUE"); //
        vos[li_index].setAttributeValue("itemname", "最大值");
        vos[li_index].setAttributeValue("itemtype", "文本框");
        vos[li_index].setAttributeValue("comboxdesc", null);
        vos[li_index].setAttributeValue("refdesc", null);
        vos[li_index].setAttributeValue("issave", "Y");
        vos[li_index].setAttributeValue("isdefaultquery", "N");
        vos[li_index].setAttributeValue("ismustinput", "N");
        vos[li_index].setAttributeValue("loadformula", null);
        vos[li_index].setAttributeValue("editformula", null);
        vos[li_index].setAttributeValue("defaultvalueformula", null);
        vos[li_index].setAttributeValue("colorformula", null);
        vos[li_index].setAttributeValue("showorder", "1");
        vos[li_index].setAttributeValue("listwidth", "100");
        vos[li_index].setAttributeValue("cardwidth", "100");
        vos[li_index].setAttributeValue("listisshowable", "Y");
        vos[li_index].setAttributeValue("listiseditable", "Y");
        vos[li_index].setAttributeValue("cardisshowable", "Y");
        vos[li_index].setAttributeValue("cardiseditable", "Y");
        vos[li_index].setAttributeValue("lastmodifytime", "Y");
        li_index++;

        vos[li_index] = new HashVO();
        vos[li_index].setAttributeValue("itemkey", "INCREMENT_BY"); //
        vos[li_index].setAttributeValue("itemname", "增量");
        vos[li_index].setAttributeValue("itemtype", "文本框");
        vos[li_index].setAttributeValue("comboxdesc", null);
        vos[li_index].setAttributeValue("refdesc", null);
        vos[li_index].setAttributeValue("issave", "Y");
        vos[li_index].setAttributeValue("isdefaultquery", "N");
        vos[li_index].setAttributeValue("ismustinput", "N");
        vos[li_index].setAttributeValue("loadformula", null);
        vos[li_index].setAttributeValue("editformula", null);
        vos[li_index].setAttributeValue("defaultvalueformula", null);
        vos[li_index].setAttributeValue("colorformula", null);
        vos[li_index].setAttributeValue("showorder", "1");
        vos[li_index].setAttributeValue("listwidth", "150");
        vos[li_index].setAttributeValue("cardwidth", "150");
        vos[li_index].setAttributeValue("listisshowable", "Y");
        vos[li_index].setAttributeValue("listiseditable", "Y");
        vos[li_index].setAttributeValue("cardisshowable", "Y");
        vos[li_index].setAttributeValue("cardiseditable", "Y");
        vos[li_index].setAttributeValue("lastmodifytime", "Y");
        li_index++;

        vos[li_index] = new HashVO();
        vos[li_index].setAttributeValue("itemkey", "CYCLE_FLAG"); //
        vos[li_index].setAttributeValue("itemname", "是否循环");
        vos[li_index].setAttributeValue("itemtype", "文本框");
        vos[li_index].setAttributeValue("comboxdesc", null);
        vos[li_index].setAttributeValue("refdesc", null);
        vos[li_index].setAttributeValue("issave", "Y");
        vos[li_index].setAttributeValue("isdefaultquery", "N");
        vos[li_index].setAttributeValue("ismustinput", "N");
        vos[li_index].setAttributeValue("loadformula", null);
        vos[li_index].setAttributeValue("editformula", null);
        vos[li_index].setAttributeValue("defaultvalueformula", null);
        vos[li_index].setAttributeValue("colorformula", null);
        vos[li_index].setAttributeValue("showorder", "1");
        vos[li_index].setAttributeValue("listwidth", "120");
        vos[li_index].setAttributeValue("cardwidth", "120");
        vos[li_index].setAttributeValue("listisshowable", "Y");
        vos[li_index].setAttributeValue("listiseditable", "Y");
        vos[li_index].setAttributeValue("cardisshowable", "Y");
        vos[li_index].setAttributeValue("cardiseditable", "Y");
        vos[li_index].setAttributeValue("lastmodifytime", "Y");
        li_index++;

        vos[li_index] = new HashVO();
        vos[li_index].setAttributeValue("itemkey", "ORDER_FLAG"); //
        vos[li_index].setAttributeValue("itemname", "是否按序列");
        vos[li_index].setAttributeValue("itemtype", "数字框");
        vos[li_index].setAttributeValue("comboxdesc", null);
        vos[li_index].setAttributeValue("refdesc", null);
        vos[li_index].setAttributeValue("issave", "Y");
        vos[li_index].setAttributeValue("isdefaultquery", "N");
        vos[li_index].setAttributeValue("ismustinput", "N");
        vos[li_index].setAttributeValue("loadformula", null);
        vos[li_index].setAttributeValue("editformula", null);
        vos[li_index].setAttributeValue("defaultvalueformula", null);
        vos[li_index].setAttributeValue("colorformula", null);
        vos[li_index].setAttributeValue("showorder", "1");
        vos[li_index].setAttributeValue("listwidth", "70");
        vos[li_index].setAttributeValue("cardwidth", "70");
        vos[li_index].setAttributeValue("listisshowable", "Y");
        vos[li_index].setAttributeValue("listiseditable", "Y");
        vos[li_index].setAttributeValue("cardisshowable", "Y");
        vos[li_index].setAttributeValue("cardiseditable", "Y");
        vos[li_index].setAttributeValue("lastmodifytime", "Y");
        li_index++;

        vos[li_index] = new HashVO();
        vos[li_index].setAttributeValue("itemkey", "CACHE_SIZE"); //
        vos[li_index].setAttributeValue("itemname", "缓存大小");
        vos[li_index].setAttributeValue("itemtype", "数字框");
        vos[li_index].setAttributeValue("comboxdesc", null);
        vos[li_index].setAttributeValue("refdesc", null);
        vos[li_index].setAttributeValue("issave", "Y");
        vos[li_index].setAttributeValue("isdefaultquery", "N");
        vos[li_index].setAttributeValue("ismustinput", "N");
        vos[li_index].setAttributeValue("loadformula", null);
        vos[li_index].setAttributeValue("editformula", null);
        vos[li_index].setAttributeValue("defaultvalueformula", null);
        vos[li_index].setAttributeValue("colorformula", null);
        vos[li_index].setAttributeValue("showorder", "1");
        vos[li_index].setAttributeValue("listwidth", "80");
        vos[li_index].setAttributeValue("cardwidth", "80");
        vos[li_index].setAttributeValue("listisshowable", "Y");
        vos[li_index].setAttributeValue("listiseditable", "Y");
        vos[li_index].setAttributeValue("cardisshowable", "Y");
        vos[li_index].setAttributeValue("cardiseditable", "Y");
        vos[li_index].setAttributeValue("lastmodifytime", "Y");
        li_index++;

        vos[li_index] = new HashVO();
        vos[li_index].setAttributeValue("itemkey", "LAST_NUMBER"); //
        vos[li_index].setAttributeValue("itemname", "当前值");
        vos[li_index].setAttributeValue("itemtype", "数字框");
        vos[li_index].setAttributeValue("comboxdesc", null); //
        vos[li_index].setAttributeValue("refdesc", null);
        vos[li_index].setAttributeValue("issave", "Y");
        vos[li_index].setAttributeValue("isdefaultquery", "N");
        vos[li_index].setAttributeValue("ismustinput", "N");
        vos[li_index].setAttributeValue("loadformula", null);
        vos[li_index].setAttributeValue("editformula", null);
        vos[li_index].setAttributeValue("defaultvalueformula", null);
        vos[li_index].setAttributeValue("colorformula", null);
        vos[li_index].setAttributeValue("showorder", "1");
        vos[li_index].setAttributeValue("listwidth", "80");
        vos[li_index].setAttributeValue("cardwidth", "80");
        vos[li_index].setAttributeValue("listisshowable", "Y");
        vos[li_index].setAttributeValue("listiseditable", "Y");
        vos[li_index].setAttributeValue("cardisshowable", "Y");
        vos[li_index].setAttributeValue("cardiseditable", "Y");
        vos[li_index].setAttributeValue("lastmodifytime", "Y");
        li_index++;

        return vos;
    }

}
/**************************************************************************
 * $RCSfile: TableSequences_VO.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:24 $
 *
 * $Log: TableSequences_VO.java,v $
 * Revision 1.2  2007/05/31 07:38:24  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:02:12  qilin
 * no message
 *
 * Revision 1.3  2007/02/27 06:02:59  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 05:01:56  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/