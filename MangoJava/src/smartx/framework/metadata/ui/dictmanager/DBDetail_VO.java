/**************************************************************************
 * $RCSfile: DBDetail_VO.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:24 $
 **************************************************************************/
package smartx.framework.metadata.ui.dictmanager;

import smartx.framework.common.vo.*;
import smartx.framework.metadata.vo.templetvo.*;

public class DBDetail_VO extends AbstractTempletVO {

    /**
     *
     */
    private static final long serialVersionUID = -6873221178935355339L;

    private static int li_vo_length = 1;

    private String str_type = null;

    private String str_code_type = null;

    private String str_table = "USER_SOURCE";

    private String str_key_name = "NAME";

    public DBDetail_VO() {
        str_type = "存储函数";
        str_code_type = "FOUCTION";
    }

    public DBDetail_VO(String _type) {
        if (_type.equalsIgnoreCase("FUNCTION")) {
            str_type = "存储函数";
        } else if (_type.equalsIgnoreCase("TRIGGER")) {
            str_type = "触发器";
            li_vo_length = 3;
            str_table = "USER_TRIGGERS";
            str_key_name = "TRIGGER_NAME";
        } else if (_type.equalsIgnoreCase("PROCEDURE")) {
            str_type = "存储过程";
        } else {
            str_type = "存储函数";
        }
    }

    public HashVO getPub_templet_1Data() {
        HashVO vo = new HashVO(); //
        vo.setAttributeValue("templetcode", str_code_type); //
        vo.setAttributeValue("templetname", str_code_type);
        vo.setAttributeValue("tablename", str_table);
        vo.setAttributeValue("pkname", "NAME");
        vo.setAttributeValue("pksequencename", null);
        vo.setAttributeValue("savedtablename", str_table);
        vo.setAttributeValue("lastmodifytime", null);
        vo.setAttributeValue("ui_intercept", null);
        vo.setAttributeValue("bs_intercept", null);
        vo.setAttributeValue("ui_eventlistener", null);
        return vo;
    }

    public HashVO[] getPub_templet_1_itemData() {
        HashVO[] vos = new HashVO[li_vo_length]; //
        int li_index = 0;

        vos[li_index] = new HashVO();
        vos[li_index].setAttributeValue("itemkey", str_key_name); //
        vos[li_index].setAttributeValue("itemname", str_type);
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
        vos[li_index].setAttributeValue("listwidth", "230");
        vos[li_index].setAttributeValue("cardwidth", "230");
        vos[li_index].setAttributeValue("listisshowable", "Y");
        vos[li_index].setAttributeValue("listiseditable", "N");
        vos[li_index].setAttributeValue("cardisshowable", "Y");
        vos[li_index].setAttributeValue("cardiseditable", "N");
        vos[li_index].setAttributeValue("lastmodifytime", "Y");
        li_index++;

        if (li_index == li_vo_length) {
            return vos;
        }

        vos[li_index] = new HashVO();
        vos[li_index].setAttributeValue("itemkey", "TABLE_NAME"); //
        vos[li_index].setAttributeValue("itemname", "表名");
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
        vos[li_index].setAttributeValue("itemkey", "STATUS"); //
        vos[li_index].setAttributeValue("itemname", "状态");
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

        return vos;
    }
}
/**************************************************************************
 * $RCSfile: DBDetail_VO.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:24 $
 *
 * $Log: DBDetail_VO.java,v $
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