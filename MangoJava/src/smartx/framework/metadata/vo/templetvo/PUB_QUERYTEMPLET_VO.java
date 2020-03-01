/**************************************************************************
 * $RCSfile: PUB_QUERYTEMPLET_VO.java,v $  $Revision: 1.3 $  $Date: 2007/07/02 00:30:26 $
 **************************************************************************/
package smartx.framework.metadata.vo.templetvo;

import java.util.*;

import smartx.framework.common.vo.*;


public class PUB_QUERYTEMPLET_VO extends AbstractTempletVO {
    private static final long serialVersionUID = 8057184541083294474L;

    public HashVO getPub_templet_1Data() {
        HashVO vo = new HashVO(); //
        vo.setAttributeValue("templetcode", "PUB_QUERYTEMPLET"); //模板编码，请勿随便修改
        vo.setAttributeValue("templetname", "PUB_QUERYTEMPLET"); //模板名称
        vo.setAttributeValue("tablename", "PUB_QUERYTEMPLET"); //查询数据的表(视图)名
        vo.setAttributeValue("pkname", "PK_PUB_QUERYTEMPLET"); //主键名
        vo.setAttributeValue("pksequencename", "S_PUB_QUERYTEMPLET"); //序列名
        vo.setAttributeValue("savedtablename", "PUB_QUERYTEMPLET"); //保存数据的表名
        vo.setAttributeValue("listcustpanel", null); //列表自定义面板
        vo.setAttributeValue("cardcustpanel", null); //卡片自定义面板
        return vo;
    }

    public HashVO[] getPub_templet_1_itemData() {
        Vector vector = new Vector();
        HashVO itemVO = null;

        itemVO = new HashVO();
        itemVO.setAttributeValue("itemkey", "PK_PUB_QUERYTEMPLET"); //唯一标识,用于取数与保存
        itemVO.setAttributeValue("itemname", "主键"); //显示名称
        itemVO.setAttributeValue("itemtype", "数字框"); //控件类型
        itemVO.setAttributeValue("comboxdesc", null); //下拉框定义
        itemVO.setAttributeValue("refdesc", null); //参照定义
        itemVO.setAttributeValue("issave", "Y"); //是否参与保存(Y,N)
        itemVO.setAttributeValue("isdefaultquery", "1"); //1-快速查询;2-通用查询;3-不参与查询
        itemVO.setAttributeValue("ismustinput", "N"); //是否必输项(Y,N)
        itemVO.setAttributeValue("loadformula", null); //加载公式
        itemVO.setAttributeValue("editformula", null); //编辑公式
        itemVO.setAttributeValue("defaultvalueformula", null); //默认值公式
        itemVO.setAttributeValue("colorformula", null); //颜色公式
        itemVO.setAttributeValue("listwidth", "145"); //列表是宽度
        itemVO.setAttributeValue("cardwidth", "150"); //卡片时宽度
        itemVO.setAttributeValue("listisshowable", "N"); //列表时是否显示(Y,N)
        itemVO.setAttributeValue("listiseditable", "1"); //列表时是否可编辑,1-全部可编辑,2-仅新增可编辑,3-仅修改可编辑;4-全部禁用
        itemVO.setAttributeValue("cardisshowable", "Y"); //卡片时是否显示(Y,N)
        itemVO.setAttributeValue("cardiseditable", "4"); //卡片时是否可编辑,1-全部可编辑,2-仅新增可编辑,3-仅修改可编辑;4-全部禁用
        vector.add(itemVO);

        itemVO = new HashVO();
        itemVO.setAttributeValue("itemkey", "TEMPLETCODE"); //唯一标识,用于取数与保存
        itemVO.setAttributeValue("itemname", "模板编码"); //显示名称
        itemVO.setAttributeValue("itemtype", "文本框"); //控件类型
        itemVO.setAttributeValue("comboxdesc", null); //下拉框定义
        itemVO.setAttributeValue("refdesc",
                                 "SELECT TEMPLETCODE 模板编码,TEMPLETNAME 模板名称,SAVEDTABLENAME 保存表名 from PUB_TEMPLET_1 WHERE 1=1"); //参照定义
        itemVO.setAttributeValue("issave", "Y"); //是否参与保存(Y,N)
        itemVO.setAttributeValue("isdefaultquery", "2"); //1-快速查询;2-通用查询;3-不参与查询
        itemVO.setAttributeValue("ismustinput", "N"); //是否必输项(Y,N)
        itemVO.setAttributeValue("loadformula", null); //加载公式
        itemVO.setAttributeValue("editformula", null); //编辑公式
        itemVO.setAttributeValue("defaultvalueformula", null); //默认值公式
        itemVO.setAttributeValue("colorformula", null); //颜色公式
        itemVO.setAttributeValue("listwidth", "145"); //列表是宽度
        itemVO.setAttributeValue("cardwidth", "150"); //卡片时宽度
        itemVO.setAttributeValue("listisshowable", "Y"); //列表时是否显示(Y,N)
        itemVO.setAttributeValue("listiseditable", "1"); //列表时是否可编辑,1-全部可编辑,2-仅新增可编辑,3-仅修改可编辑;4-全部禁用
        itemVO.setAttributeValue("cardisshowable", "Y"); //卡片时是否显示(Y,N)
        itemVO.setAttributeValue("cardiseditable", "1"); //卡片时是否可编辑,1-全部可编辑,2-仅新增可编辑,3-仅修改可编辑;4-全部禁用
        vector.add(itemVO);

        itemVO = new HashVO();
        itemVO.setAttributeValue("itemkey", "TEMPLETNAME"); //唯一标识,用于取数与保存
        itemVO.setAttributeValue("itemname", "模板名称"); //显示名称
        itemVO.setAttributeValue("itemtype", "文本框"); //控件类型
        itemVO.setAttributeValue("comboxdesc", null); //下拉框定义
        itemVO.setAttributeValue("refdesc", null); //参照定义
        itemVO.setAttributeValue("issave", "Y"); //是否参与保存(Y,N)
        itemVO.setAttributeValue("isdefaultquery", "2"); //1-快速查询;2-通用查询;3-不参与查询
        itemVO.setAttributeValue("ismustinput", "N"); //是否必输项(Y,N)
        itemVO.setAttributeValue("loadformula", null); //加载公式
        itemVO.setAttributeValue("editformula", null); //编辑公式
        itemVO.setAttributeValue("defaultvalueformula", null); //默认值公式
        itemVO.setAttributeValue("colorformula", null); //颜色公式
        itemVO.setAttributeValue("listwidth", "145"); //列表是宽度
        itemVO.setAttributeValue("cardwidth", "150"); //卡片时宽度
        itemVO.setAttributeValue("listisshowable", "Y"); //列表时是否显示(Y,N)
        itemVO.setAttributeValue("listiseditable", "1"); //列表时是否可编辑,1-全部可编辑,2-仅新增可编辑,3-仅修改可编辑;4-全部禁用
        itemVO.setAttributeValue("cardisshowable", "Y"); //卡片时是否显示(Y,N)
        itemVO.setAttributeValue("cardiseditable", "1"); //卡片时是否可编辑,1-全部可编辑,2-仅新增可编辑,3-仅修改可编辑;4-全部禁用
        vector.add(itemVO);

        itemVO = new HashVO();
        itemVO.setAttributeValue("itemkey", "SQL"); //唯一标识,用于取数与保存
        itemVO.setAttributeValue("itemname", "SQL语句"); //显示名称
        itemVO.setAttributeValue("itemtype", "大文本框"); //控件类型
        itemVO.setAttributeValue("comboxdesc", null); //下拉框定义
        itemVO.setAttributeValue("refdesc", null); //参照定义
        itemVO.setAttributeValue("issave", "Y"); //是否参与保存(Y,N)
        itemVO.setAttributeValue("isdefaultquery", "2"); //1-快速查询;2-通用查询;3-不参与查询
        itemVO.setAttributeValue("ismustinput", "N"); //是否必输项(Y,N)
        itemVO.setAttributeValue("loadformula", null); //加载公式
        itemVO.setAttributeValue("editformula", null); //编辑公式
        itemVO.setAttributeValue("defaultvalueformula", null); //默认值公式
        itemVO.setAttributeValue("colorformula", null); //颜色公式
        itemVO.setAttributeValue("listwidth", "560"); //列表是宽度
        itemVO.setAttributeValue("cardwidth", "430"); //卡片时宽度
        itemVO.setAttributeValue("listisshowable", "Y"); //列表时是否显示(Y,N)
        itemVO.setAttributeValue("listiseditable", "1"); //列表时是否可编辑,1-全部可编辑,2-仅新增可编辑,3-仅修改可编辑;4-全部禁用
        itemVO.setAttributeValue("cardisshowable", "Y"); //卡片时是否显示(Y,N)
        itemVO.setAttributeValue("cardiseditable", "1"); //卡片时是否可编辑,1-全部可编辑,2-仅新增可编辑,3-仅修改可编辑;4-全部禁用
        vector.add(itemVO);

        return (HashVO[]) vector.toArray(new HashVO[0]);
    }
}
/**************************************************************************
 * $RCSfile: PUB_QUERYTEMPLET_VO.java,v $  $Revision: 1.3 $  $Date: 2007/07/02 00:30:26 $
 *
 * $Log: PUB_QUERYTEMPLET_VO.java,v $
 * Revision 1.3  2007/07/02 00:30:26  sunxb
 * *** empty log message ***
 *
 * Revision 1.2  2007/05/31 07:38:20  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:03:52  qilin
 * no message
 *
 * Revision 1.3  2007/02/27 06:03:01  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:12:30  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/