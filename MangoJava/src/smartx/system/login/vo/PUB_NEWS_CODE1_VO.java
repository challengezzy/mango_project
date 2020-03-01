/**************************************************************************
 * $RCSfile: PUB_NEWS_CODE1_VO.java,v $  $Revision: 1.3 $  $Date: 2007/07/02 00:30:45 $
 **************************************************************************/
package smartx.system.login.vo;

import java.util.*;

import smartx.framework.common.vo.*;
import smartx.framework.metadata.vo.templetvo.*;


public class PUB_NEWS_CODE1_VO extends AbstractTempletVO {
    private static final long serialVersionUID = 8057184541083294474L;

    public HashVO getPub_templet_1Data() {
        HashVO vo = new HashVO(); //
        vo.setAttributeValue("templetcode", "PUB_NEWS_CODE1"); //模板编码，请勿随便修改
        vo.setAttributeValue("templetname", "PUB_NEWS_NAME1"); //模板名称
        vo.setAttributeValue("tablename", "PUB_NEWS"); //查询数据的表(视图)名
        vo.setAttributeValue("pkname", "PK_PUB_NEWS"); //主键名
        vo.setAttributeValue("pksequencename", "S_PUB_NEWS"); //序列名
        vo.setAttributeValue("savedtablename", "PUB_NEWS"); //保存数据的表名
        vo.setAttributeValue("listcustpanel", null); //列表自定义面板
        vo.setAttributeValue("cardcustpanel", null); //卡片自定义面板
        return vo;
    }

    public HashVO[] getPub_templet_1_itemData() {
        Vector vector = new Vector();
        HashVO itemVO = null;

        itemVO = new HashVO();
        itemVO.setAttributeValue("itemkey", "PK_PUB_NEWS"); //唯一标识,用于取数与保存
        itemVO.setAttributeValue("itemname", "主键"); //显示名称
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
        itemVO.setAttributeValue("itemkey", "TITLE"); //唯一标识,用于取数与保存
        itemVO.setAttributeValue("itemname", "标题"); //显示名称
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
        itemVO.setAttributeValue("itemkey", "CONTENT"); //唯一标识,用于取数与保存
        itemVO.setAttributeValue("itemname", "内容"); //显示名称
        itemVO.setAttributeValue("itemtype", "多行文本框"); //控件类型
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
        itemVO.setAttributeValue("itemkey", "ISSCROLLVIEW"); //唯一标识,用于取数与保存
        itemVO.setAttributeValue("itemname", "是否滚动"); //显示名称
        itemVO.setAttributeValue("itemtype", "勾选框"); //控件类型
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
        itemVO.setAttributeValue("itemkey", "CREATETIME"); //唯一标识,用于取数与保存
        itemVO.setAttributeValue("itemname", "创建时间"); //显示名称
        itemVO.setAttributeValue("itemtype", "时间"); //控件类型
        itemVO.setAttributeValue("comboxdesc", null); //下拉框定义
        itemVO.setAttributeValue("refdesc", null); //参照定义
        itemVO.setAttributeValue("issave", "Y"); //是否参与保存(Y,N)
        itemVO.setAttributeValue("isdefaultquery", "2"); //1-快速查询;2-通用查询;3-不参与查询
        itemVO.setAttributeValue("ismustinput", "N"); //是否必输项(Y,N)
        itemVO.setAttributeValue("loadformula", null); //加载公式
        itemVO.setAttributeValue("editformula", null); //编辑公式
        itemVO.setAttributeValue("defaultvalueformula", "getCurrDBTime()"); //默认值公式
        itemVO.setAttributeValue("colorformula", null); //颜色公式
        itemVO.setAttributeValue("listwidth", "145"); //列表是宽度
        itemVO.setAttributeValue("cardwidth", "150"); //卡片时宽度
        itemVO.setAttributeValue("listisshowable", "Y"); //列表时是否显示(Y,N)
        itemVO.setAttributeValue("listiseditable", "1"); //列表时是否可编辑,1-全部可编辑,2-仅新增可编辑,3-仅修改可编辑;4-全部禁用
        itemVO.setAttributeValue("cardisshowable", "Y"); //卡片时是否显示(Y,N)
        itemVO.setAttributeValue("cardiseditable", "1"); //卡片时是否可编辑,1-全部可编辑,2-仅新增可编辑,3-仅修改可编辑;4-全部禁用
        vector.add(itemVO);

        itemVO = new HashVO();
        itemVO.setAttributeValue("itemkey", "CREATER"); //唯一标识,用于取数与保存
        itemVO.setAttributeValue("itemname", "创建人"); //显示名称
        itemVO.setAttributeValue("itemtype", "文本框"); //控件类型
        itemVO.setAttributeValue("comboxdesc", null); //下拉框定义
        itemVO.setAttributeValue("refdesc", null); //参照定义
        itemVO.setAttributeValue("issave", "Y"); //是否参与保存(Y,N)
        itemVO.setAttributeValue("isdefaultquery", "2"); //1-快速查询;2-通用查询;3-不参与查询
        itemVO.setAttributeValue("ismustinput", "N"); //是否必输项(Y,N)
        itemVO.setAttributeValue("loadformula", null); //加载公式
        itemVO.setAttributeValue("editformula", null); //编辑公式
        itemVO.setAttributeValue("defaultvalueformula", "getLoginName()"); //默认值公式
        itemVO.setAttributeValue("colorformula", null); //颜色公式
        itemVO.setAttributeValue("listwidth", "145"); //列表是宽度
        itemVO.setAttributeValue("cardwidth", "150"); //卡片时宽度
        itemVO.setAttributeValue("listisshowable", "Y"); //列表时是否显示(Y,N)
        itemVO.setAttributeValue("listiseditable", "1"); //列表时是否可编辑,1-全部可编辑,2-仅新增可编辑,3-仅修改可编辑;4-全部禁用
        itemVO.setAttributeValue("cardisshowable", "Y"); //卡片时是否显示(Y,N)
        itemVO.setAttributeValue("cardiseditable", "1"); //卡片时是否可编辑,1-全部可编辑,2-仅新增可编辑,3-仅修改可编辑;4-全部禁用
        vector.add(itemVO);

        return (HashVO[]) vector.toArray(new HashVO[0]);
    }
}
/**************************************************************************
 * $RCSfile: PUB_NEWS_CODE1_VO.java,v $  $Revision: 1.3 $  $Date: 2007/07/02 00:30:45 $
 *
 * $Log: PUB_NEWS_CODE1_VO.java,v $
 * Revision 1.3  2007/07/02 00:30:45  sunxb
 * *** empty log message ***
 *
 * Revision 1.2  2007/05/31 07:41:33  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:22:09  qilin
 * no message
 *
 * Revision 1.4  2007/02/27 06:03:02  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/02/07 04:48:32  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:20:38  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/