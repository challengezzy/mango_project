/**************************************************************************
 * $RCSfile: TableField_VO.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:24 $
 **************************************************************************/
package smartx.framework.metadata.ui.dictmanager;

import smartx.framework.common.vo.*;
import smartx.framework.metadata.vo.templetvo.*;

public class TableField_VO extends AbstractTempletVO {

    /**
     *
     */
    private static final long serialVersionUID = -8736539743771349601L;
    private static int VO_LENGTH = 9;
    private String str_flag = "N";

    public TableField_VO() {
    }

    public TableField_VO(String _table_field) {
        if (_table_field.equalsIgnoreCase("FIELD")) {
            str_flag = "Y";
        } else {
            str_flag = "N";
        }
    }

    public HashVO getPub_templet_1Data() {
        HashVO vo = new HashVO(); //
        vo.setAttributeValue("templetcode", "field"); //
        vo.setAttributeValue("templetname", "columns");
        vo.setAttributeValue("tablename", "DB_TABLE_FIELD");
        vo.setAttributeValue("pkname", "COLUMN_NAME");
        vo.setAttributeValue("pksequencename", null);
        vo.setAttributeValue("savedtablename", "DB_TABLE_FIELD");
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
        vos[li_index].setAttributeValue("listisshowable", str_flag);
        vos[li_index].setAttributeValue("listiseditable", str_flag);
        vos[li_index].setAttributeValue("cardisshowable", str_flag);
        vos[li_index].setAttributeValue("cardiseditable", str_flag);
        vos[li_index].setAttributeValue("lastmodifytime", "Y");
        li_index++;

        vos[li_index] = new HashVO();
        vos[li_index].setAttributeValue("itemkey", "COLUMN_NAME"); //
        vos[li_index].setAttributeValue("itemname", "列名");
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
        vos[li_index].setAttributeValue("itemkey", "DATA_TYPE"); //
        vos[li_index].setAttributeValue("itemname", "数据类型");
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
        vos[li_index].setAttributeValue("listwidth", "80");
        vos[li_index].setAttributeValue("cardwidth", "80");
        vos[li_index].setAttributeValue("listisshowable", "Y");
        vos[li_index].setAttributeValue("listiseditable", "Y");
        vos[li_index].setAttributeValue("cardisshowable", "Y");
        vos[li_index].setAttributeValue("cardiseditable", "Y");
        vos[li_index].setAttributeValue("lastmodifytime", "Y");
        li_index++;

        vos[li_index] = new HashVO();
        vos[li_index].setAttributeValue("itemkey", "DATA_LENGTH"); //
        vos[li_index].setAttributeValue("itemname", "类型长度");
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
        vos[li_index].setAttributeValue("itemkey", "DATA_PRECISION"); //
        vos[li_index].setAttributeValue("itemname", "数据描述");
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
        vos[li_index].setAttributeValue("itemkey", "NULLABLE"); //
        vos[li_index].setAttributeValue("itemname", "空值");
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
        vos[li_index].setAttributeValue("listwidth", "50");
        vos[li_index].setAttributeValue("cardwidth", "50");
        vos[li_index].setAttributeValue("listisshowable", "Y");
        vos[li_index].setAttributeValue("listiseditable", "Y");
        vos[li_index].setAttributeValue("cardisshowable", "Y");
        vos[li_index].setAttributeValue("cardiseditable", "Y");
        vos[li_index].setAttributeValue("lastmodifytime", "Y");
        li_index++;

        vos[li_index] = new HashVO();
        vos[li_index].setAttributeValue("itemkey", "COLUMN_ID"); //
        vos[li_index].setAttributeValue("itemname", "列顺序");
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
        vos[li_index].setAttributeValue("listwidth", "60");
        vos[li_index].setAttributeValue("cardwidth", "60");
        vos[li_index].setAttributeValue("listisshowable", "Y");
        vos[li_index].setAttributeValue("listiseditable", "Y");
        vos[li_index].setAttributeValue("cardisshowable", "Y");
        vos[li_index].setAttributeValue("cardiseditable", "Y");
        vos[li_index].setAttributeValue("lastmodifytime", "Y");
        li_index++;

        vos[li_index] = new HashVO();
        vos[li_index].setAttributeValue("itemkey", "LAST_ANALYZED"); //
        vos[li_index].setAttributeValue("itemname", "最后修改时间"); //
        vos[li_index].setAttributeValue("itemtype", "时间"); //
        vos[li_index].setAttributeValue("comboxdesc", null);
        vos[li_index].setAttributeValue("refdesc", "select * from tab");
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
        vos[li_index].setAttributeValue("itemkey", "COMMENTS"); //
        vos[li_index].setAttributeValue("itemname", "列说明"); //
        vos[li_index].setAttributeValue("itemtype", "文本框"); //
        vos[li_index].setAttributeValue("comboxdesc", null);
        vos[li_index].setAttributeValue("refdesc", "select * from tab");
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
 * $RCSfile: TableField_VO.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:24 $
 *
 * $Log: TableField_VO.java,v $
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