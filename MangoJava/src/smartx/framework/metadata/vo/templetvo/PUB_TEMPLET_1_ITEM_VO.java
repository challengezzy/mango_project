/**************************************************************************
 * $RCSfile: PUB_TEMPLET_1_ITEM_VO.java,v $  $Revision: 1.3.8.2 $  $Date: 2009/07/21 08:41:20 $
 **************************************************************************/
package smartx.framework.metadata.vo.templetvo;

import java.util.*;

import smartx.framework.common.vo.*;


public class PUB_TEMPLET_1_ITEM_VO extends AbstractTempletVO {
    private static final long serialVersionUID = 8057184541083294474L;

    public HashVO getPub_templet_1Data() {
        HashVO vo = new HashVO(); //
        vo.setAttributeValue("templetcode", "PUB_TEMPLET_1_ITEM");     //模板编码，请勿随便修改
        vo.setAttributeValue("templetname", "Pub_Templet_1_Item");     //模板名称
        vo.setAttributeValue("tablename", "PUB_TEMPLET_1_ITEM");       //查询数据的表(视图)名
        vo.setAttributeValue("pkname", "PK_PUB_TEMPLET_1_ITEM");       //主键名
        vo.setAttributeValue("pksequencename", "S_PUB_TEMPLET_1_ITEM");//序列名
        vo.setAttributeValue("savedtablename", "PUB_TEMPLET_1_ITEM");  //保存数据的表名
        vo.setAttributeValue("listcustpanel", null);                   //列表自定义面板
        vo.setAttributeValue("cardcustpanel", null);                   //卡片自定义面板
        return vo;
    }

    public HashVO[] getPub_templet_1_itemData() {
        Vector vector = new Vector();
        HashVO itemVO = null;

        itemVO = new HashVO();
        itemVO.setAttributeValue("itemkey", "PK_PUB_TEMPLET_1_ITEM");  //唯一标识,用于取数与保存
        itemVO.setAttributeValue("itemname", "PK_PUB_TEMPLET_1");      //显示名称
        itemVO.setAttributeValue("itemtype", "文本框");                 //控件类型
        itemVO.setAttributeValue("comboxdesc", null);                  //下拉框定义
        itemVO.setAttributeValue("refdesc", null);                     //参照定义
        itemVO.setAttributeValue("issave", "Y");                       //是否参与保存(Y,N)
        itemVO.setAttributeValue("isdefaultquery", "2");               //1-快速查询;2-通用查询;3-不参与查询
        itemVO.setAttributeValue("ismustinput", "N");                  //是否必输项(Y,N)
        itemVO.setAttributeValue("loadformula", null);                 //加载公式
        itemVO.setAttributeValue("editformula", null);                 //编辑公式
        itemVO.setAttributeValue("defaultvalueformula", null);         //默认值公式
        itemVO.setAttributeValue("colorformula", null);                //颜色公式
        itemVO.setAttributeValue("listwidth", "145");                  //列表是宽度
        itemVO.setAttributeValue("cardwidth", "150");                  //卡片时宽度
        itemVO.setAttributeValue("listisshowable", "N");               //列表时是否显示(Y,N)
        itemVO.setAttributeValue("listiseditable", "1");               //列表时是否可编辑,1-全部可编辑,2-仅新增可编辑,3-仅修改可编辑;4-全部禁用
        itemVO.setAttributeValue("cardisshowable", "Y");               //卡片时是否显示(Y,N)
        itemVO.setAttributeValue("cardiseditable", "1");               //卡片时是否可编辑,1-全部可编辑,2-仅新增可编辑,3-仅修改可编辑;4-全部禁用
        vector.add(itemVO);

        itemVO = new HashVO();
        itemVO.setAttributeValue("itemkey", "PK_PUB_TEMPLET_1"); //唯一标识,用于取数与保存
        itemVO.setAttributeValue("itemname", "PK_PUB_TEMPLET_1"); //显示名称
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
        itemVO.setAttributeValue("listisshowable", "N"); //列表时是否显示(Y,N)
        itemVO.setAttributeValue("listiseditable", "1"); //列表时是否可编辑,1-全部可编辑,2-仅新增可编辑,3-仅修改可编辑;4-全部禁用
        itemVO.setAttributeValue("cardisshowable", "Y"); //卡片时是否显示(Y,N)
        itemVO.setAttributeValue("cardiseditable", "1"); //卡片时是否可编辑,1-全部可编辑,2-仅新增可编辑,3-仅修改可编辑;4-全部禁用
        vector.add(itemVO);

        itemVO = new HashVO();
        itemVO.setAttributeValue("itemkey", "ITEMKEY"); //唯一标识,用于取数与保存
        itemVO.setAttributeValue("itemname", "ItemKey"); //显示名称
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
        itemVO.setAttributeValue("itemkey", "ITEMNAME"); //唯一标识,用于取数与保存
        itemVO.setAttributeValue("itemname", "ItemName"); //显示名称
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
        itemVO.setAttributeValue("itemkey", "ITEMTYPE"); //唯一标识,用于取数与保存
        itemVO.setAttributeValue("itemname", "控件类型"); //显示名称
        itemVO.setAttributeValue("itemtype", "下拉框"); //控件类型
        itemVO.setAttributeValue("comboxdesc","Select Id,code,Name From pub_comboboxdict Where type='平台控件类型'  and 1=1 Order By seq"); //下拉框定义
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
        itemVO.setAttributeValue("itemkey", "COMBOXDESC"); //唯一标识,用于取数与保存
        itemVO.setAttributeValue("itemname", "下拉框说明"); //显示名称
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
        itemVO.setAttributeValue("listwidth", "250"); //列表是宽度
        itemVO.setAttributeValue("cardwidth", "150"); //卡片时宽度
        itemVO.setAttributeValue("listisshowable", "Y"); //列表时是否显示(Y,N)
        itemVO.setAttributeValue("listiseditable", "1"); //列表时是否可编辑,1-全部可编辑,2-仅新增可编辑,3-仅修改可编辑;4-全部禁用
        itemVO.setAttributeValue("cardisshowable", "Y"); //卡片时是否显示(Y,N)
        itemVO.setAttributeValue("cardiseditable", "1"); //卡片时是否可编辑,1-全部可编辑,2-仅新增可编辑,3-仅修改可编辑;4-全部禁用
        vector.add(itemVO);

        itemVO = new HashVO();
        itemVO.setAttributeValue("itemkey", "REFDESC"); //唯一标识,用于取数与保存
        itemVO.setAttributeValue("itemname", "参照说明"); //显示名称
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
        itemVO.setAttributeValue("listwidth", "250"); //列表是宽度
        itemVO.setAttributeValue("cardwidth", "150"); //卡片时宽度
        itemVO.setAttributeValue("listisshowable", "Y"); //列表时是否显示(Y,N)
        itemVO.setAttributeValue("listiseditable", "1"); //列表时是否可编辑,1-全部可编辑,2-仅新增可编辑,3-仅修改可编辑;4-全部禁用
        itemVO.setAttributeValue("cardisshowable", "Y"); //卡片时是否显示(Y,N)
        itemVO.setAttributeValue("cardiseditable", "1"); //卡片时是否可编辑,1-全部可编辑,2-仅新增可编辑,3-仅修改可编辑;4-全部禁用
        vector.add(itemVO);

        itemVO = new HashVO();
        itemVO.setAttributeValue("itemkey", "ISSAVE"); //唯一标识,用于取数与保存
        itemVO.setAttributeValue("itemname", "是否参与保存"); //显示名称
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
        itemVO.setAttributeValue("itemkey", "ISDEFAULTQUERY"); //唯一标识,用于取数与保存
        itemVO.setAttributeValue("itemname", "是否默认查询条件"); //显示名称
        itemVO.setAttributeValue("itemtype", "下拉框"); //控件类型
        itemVO.setAttributeValue("comboxdesc",
                                 "Select Id,code,Name From pub_comboboxdict Where type='参与查询条件类型'  and 1=1 Order By seq"); //下拉框定义
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
        
        //James.W 20080909 add  mr.nova20-30 查询界面需要提供设置必填项以及设置查询条件默认值的方法
        itemVO = new HashVO();
        itemVO.setAttributeValue("itemkey", "ISMUSTCONDITION"); //唯一标识,用于取数与保存
        itemVO.setAttributeValue("itemname", "是否必输条件"); //显示名称
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
        itemVO.setAttributeValue("itemkey", "DEFAULTCONDITION");       //唯一标识,用于取数与保存
        itemVO.setAttributeValue("itemname", "条件默认值公式");           //显示名称
        itemVO.setAttributeValue("itemtype", "大文本框");                //控件类型
        itemVO.setAttributeValue("comboxdesc", null);                  //下拉框定义
        itemVO.setAttributeValue("refdesc", null);                     //参照定义
        itemVO.setAttributeValue("issave", "Y");                       //是否参与保存(Y,N)
        itemVO.setAttributeValue("isdefaultquery", "2"); //1-快速查询;2-通用查询;3-不参与查询
        itemVO.setAttributeValue("ismustinput", "N"); //是否必输项(Y,N)
        itemVO.setAttributeValue("loadformula", null); //加载公式
        itemVO.setAttributeValue("editformula", null); //编辑公式
        itemVO.setAttributeValue("defaultvalueformula", null); //默认值公式
        itemVO.setAttributeValue("colorformula", null); //颜色公式
        itemVO.setAttributeValue("listwidth", "250"); //列表是宽度
        itemVO.setAttributeValue("cardwidth", "150"); //卡片时宽度
        itemVO.setAttributeValue("listisshowable", "Y"); //列表时是否显示(Y,N)
        itemVO.setAttributeValue("listiseditable", "1"); //列表时是否可编辑,1-全部可编辑,2-仅新增可编辑,3-仅修改可编辑;4-全部禁用
        itemVO.setAttributeValue("cardisshowable", "Y"); //卡片时是否显示(Y,N)
        itemVO.setAttributeValue("cardiseditable", "1"); //卡片时是否可编辑,1-全部可编辑,2-仅新增可编辑,3-仅修改可编辑;4-全部禁用
        vector.add(itemVO);
        //James.W 20080909 add  mr.nova20-30 查询界面需要提供设置必填项以及设置查询条件默认值的方法 end
        
      //James.W 20080720 add  条件顺序、条件控件类型、条件下拉、条件参照
        //条件控件类型
        itemVO = new HashVO();
        itemVO.setAttributeValue("itemkey", "CONDITION_ITEMTYPE"); //唯一标识,用于取数与保存
        itemVO.setAttributeValue("itemname", "条件控件"); //显示名称
        itemVO.setAttributeValue("itemtype", "下拉框"); //控件类型
        itemVO.setAttributeValue("comboxdesc","Select Id,code,Name From pub_comboboxdict Where type='平台控件类型'  and 1=1 Order By seq"); //下拉框定义
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
        //条件下拉
        itemVO = new HashVO();
        itemVO.setAttributeValue("itemkey", "CONDITION_COMBOXDESC"); //唯一标识,用于取数与保存
        itemVO.setAttributeValue("itemname", "条件下拉框"); //显示名称
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
        itemVO.setAttributeValue("listwidth", "250"); //列表是宽度
        itemVO.setAttributeValue("cardwidth", "150"); //卡片时宽度
        itemVO.setAttributeValue("listisshowable", "Y"); //列表时是否显示(Y,N)
        itemVO.setAttributeValue("listiseditable", "1"); //列表时是否可编辑,1-全部可编辑,2-仅新增可编辑,3-仅修改可编辑;4-全部禁用
        itemVO.setAttributeValue("cardisshowable", "Y"); //卡片时是否显示(Y,N)
        itemVO.setAttributeValue("cardiseditable", "1"); //卡片时是否可编辑,1-全部可编辑,2-仅新增可编辑,3-仅修改可编辑;4-全部禁用
        vector.add(itemVO);
        //条件参照
        itemVO = new HashVO();
        itemVO.setAttributeValue("itemkey", "CONDITION_REFDESC"); //唯一标识,用于取数与保存
        itemVO.setAttributeValue("itemname", "条件参照"); //显示名称
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
        itemVO.setAttributeValue("listwidth", "250"); //列表是宽度
        itemVO.setAttributeValue("cardwidth", "150"); //卡片时宽度
        itemVO.setAttributeValue("listisshowable", "Y"); //列表时是否显示(Y,N)
        itemVO.setAttributeValue("listiseditable", "1"); //列表时是否可编辑,1-全部可编辑,2-仅新增可编辑,3-仅修改可编辑;4-全部禁用
        itemVO.setAttributeValue("cardisshowable", "Y"); //卡片时是否显示(Y,N)
        itemVO.setAttributeValue("cardiseditable", "1"); //卡片时是否可编辑,1-全部可编辑,2-仅新增可编辑,3-仅修改可编辑;4-全部禁用
        vector.add(itemVO);
        //条件顺序
        itemVO = new HashVO();
        itemVO.setAttributeValue("itemkey", "CONDITION_SHOWORDER"); //唯一标识,用于取数与保存
        itemVO.setAttributeValue("itemname", "条件显示顺序[未启用]"); //显示名称
        itemVO.setAttributeValue("itemtype", "数字框"); //控件类型
        itemVO.setAttributeValue("comboxdesc", null); //下拉框定义
        itemVO.setAttributeValue("refdesc", null); //参照定义
        itemVO.setAttributeValue("issave", "Y"); //是否参与保存(Y,N)
        itemVO.setAttributeValue("isdefaultquery", "2"); //1-快速查询;2-通用查询;3-不参与查询
        itemVO.setAttributeValue("ismustinput", "N"); //是否必输项(Y,N)
        itemVO.setAttributeValue("loadformula", null); //加载公式
        itemVO.setAttributeValue("editformula", null); //编辑公式
        itemVO.setAttributeValue("defaultvalueformula", null); //默认值公式
        itemVO.setAttributeValue("colorformula", null); //颜色公式
        itemVO.setAttributeValue("listwidth", "80"); //列表是宽度
        itemVO.setAttributeValue("cardwidth", "150"); //卡片时宽度
        itemVO.setAttributeValue("listisshowable", "Y"); //列表时是否显示(Y,N)
        itemVO.setAttributeValue("listiseditable", "1"); //列表时是否可编辑,1-全部可编辑,2-仅新增可编辑,3-仅修改可编辑;4-全部禁用
        itemVO.setAttributeValue("cardisshowable", "Y"); //卡片时是否显示(Y,N)
        itemVO.setAttributeValue("cardiseditable", "1"); //卡片时是否可编辑,1-全部可编辑,2-仅新增可编辑,3-仅修改可编辑;4-全部禁用
        vector.add(itemVO);
        //James.W 20090720 add  条件顺序、控件类型、下拉、参照 end
        
        
        itemVO = new HashVO();
        itemVO.setAttributeValue("itemkey", "ISMUSTINPUT"); //唯一标识,用于取数与保存
        itemVO.setAttributeValue("itemname", "是否必输项"); //显示名称
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
        itemVO.setAttributeValue("itemkey", "LOADFORMULA"); //唯一标识,用于取数与保存
        itemVO.setAttributeValue("itemname", "加载公式"); //显示名称
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
        itemVO.setAttributeValue("listwidth", "250"); //列表是宽度
        itemVO.setAttributeValue("cardwidth", "150"); //卡片时宽度
        itemVO.setAttributeValue("listisshowable", "Y"); //列表时是否显示(Y,N)
        itemVO.setAttributeValue("listiseditable", "1"); //列表时是否可编辑,1-全部可编辑,2-仅新增可编辑,3-仅修改可编辑;4-全部禁用
        itemVO.setAttributeValue("cardisshowable", "Y"); //卡片时是否显示(Y,N)
        itemVO.setAttributeValue("cardiseditable", "1"); //卡片时是否可编辑,1-全部可编辑,2-仅新增可编辑,3-仅修改可编辑;4-全部禁用
        vector.add(itemVO);

        itemVO = new HashVO();
        itemVO.setAttributeValue("itemkey", "EDITFORMULA"); //唯一标识,用于取数与保存
        itemVO.setAttributeValue("itemname", "编辑公式"); //显示名称
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
        itemVO.setAttributeValue("listwidth", "250"); //列表是宽度
        itemVO.setAttributeValue("cardwidth", "150"); //卡片时宽度
        itemVO.setAttributeValue("listisshowable", "Y"); //列表时是否显示(Y,N)
        itemVO.setAttributeValue("listiseditable", "1"); //列表时是否可编辑,1-全部可编辑,2-仅新增可编辑,3-仅修改可编辑;4-全部禁用
        itemVO.setAttributeValue("cardisshowable", "Y"); //卡片时是否显示(Y,N)
        itemVO.setAttributeValue("cardiseditable", "1"); //卡片时是否可编辑,1-全部可编辑,2-仅新增可编辑,3-仅修改可编辑;4-全部禁用
        vector.add(itemVO);

        itemVO = new HashVO();
        itemVO.setAttributeValue("itemkey", "SHOWORDER"); //唯一标识,用于取数与保存
        itemVO.setAttributeValue("itemname", "显示顺序"); //显示名称
        itemVO.setAttributeValue("itemtype", "数字框"); //控件类型
        itemVO.setAttributeValue("comboxdesc", null); //下拉框定义
        itemVO.setAttributeValue("refdesc", null); //参照定义
        itemVO.setAttributeValue("issave", "Y"); //是否参与保存(Y,N)
        itemVO.setAttributeValue("isdefaultquery", "2"); //1-快速查询;2-通用查询;3-不参与查询
        itemVO.setAttributeValue("ismustinput", "N"); //是否必输项(Y,N)
        itemVO.setAttributeValue("loadformula", null); //加载公式
        itemVO.setAttributeValue("editformula", null); //编辑公式
        itemVO.setAttributeValue("defaultvalueformula", null); //默认值公式
        itemVO.setAttributeValue("colorformula", null); //颜色公式
        itemVO.setAttributeValue("listwidth", "80"); //列表是宽度
        itemVO.setAttributeValue("cardwidth", "150"); //卡片时宽度
        itemVO.setAttributeValue("listisshowable", "Y"); //列表时是否显示(Y,N)
        itemVO.setAttributeValue("listiseditable", "1"); //列表时是否可编辑,1-全部可编辑,2-仅新增可编辑,3-仅修改可编辑;4-全部禁用
        itemVO.setAttributeValue("cardisshowable", "Y"); //卡片时是否显示(Y,N)
        itemVO.setAttributeValue("cardiseditable", "1"); //卡片时是否可编辑,1-全部可编辑,2-仅新增可编辑,3-仅修改可编辑;4-全部禁用
        vector.add(itemVO);

        itemVO = new HashVO();
        itemVO.setAttributeValue("itemkey", "LISTWIDTH"); //唯一标识,用于取数与保存
        itemVO.setAttributeValue("itemname", "列表宽度"); //显示名称
        itemVO.setAttributeValue("itemtype", "数字框"); //控件类型
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
        itemVO.setAttributeValue("itemkey", "CARDWIDTH"); //唯一标识,用于取数与保存
        itemVO.setAttributeValue("itemname", "卡片宽度"); //显示名称
        itemVO.setAttributeValue("itemtype", "数字框"); //控件类型
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
        itemVO.setAttributeValue("itemkey", "LISTISSHOWABLE"); //唯一标识,用于取数与保存
        itemVO.setAttributeValue("itemname", "列表时是否显示"); //显示名称
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
        itemVO.setAttributeValue("itemkey", "LISTISEDITABLE"); //唯一标识,用于取数与保存
        itemVO.setAttributeValue("itemname", "列表时是否可编辑"); //显示名称
        itemVO.setAttributeValue("itemtype", "下拉框"); //控件类型
        itemVO.setAttributeValue("comboxdesc",
                                 "Select Id,code,Name From pub_comboboxdict Where type='控件编辑属性设置'  and 1=1 Order By seq"); //下拉框定义
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
        itemVO.setAttributeValue("itemkey", "CARDISSHOWABLE"); //唯一标识,用于取数与保存
        itemVO.setAttributeValue("itemname", "卡片时是否显示"); //显示名称
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
        itemVO.setAttributeValue("itemkey", "CARDISEDITABLE"); //唯一标识,用于取数与保存
        itemVO.setAttributeValue("itemname", "卡片时是否可编辑"); //显示名称
        itemVO.setAttributeValue("itemtype", "下拉框"); //控件类型
        itemVO.setAttributeValue("comboxdesc",
                                 "Select Id,code,Name From pub_comboboxdict Where type='控件编辑属性设置'  and 1=1 Order By seq"); //下拉框定义
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
        itemVO.setAttributeValue("itemkey", "DEFAULTVALUEFORMULA"); //唯一标识,用于取数与保存
        itemVO.setAttributeValue("itemname", "默认值公式"); //显示名称
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
        itemVO.setAttributeValue("listwidth", "250"); //列表是宽度
        itemVO.setAttributeValue("cardwidth", "150"); //卡片时宽度
        itemVO.setAttributeValue("listisshowable", "Y"); //列表时是否显示(Y,N)
        itemVO.setAttributeValue("listiseditable", "1"); //列表时是否可编辑,1-全部可编辑,2-仅新增可编辑,3-仅修改可编辑;4-全部禁用
        itemVO.setAttributeValue("cardisshowable", "Y"); //卡片时是否显示(Y,N)
        itemVO.setAttributeValue("cardiseditable", "1"); //卡片时是否可编辑,1-全部可编辑,2-仅新增可编辑,3-仅修改可编辑;4-全部禁用
        vector.add(itemVO);

        itemVO = new HashVO();
        itemVO.setAttributeValue("itemkey", "COLORFORMULA"); //唯一标识,用于取数与保存
        itemVO.setAttributeValue("itemname", "颜色公式"); //显示名称
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
        itemVO.setAttributeValue("listwidth", "250"); //列表是宽度
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
 * $RCSfile: PUB_TEMPLET_1_ITEM_VO.java,v $  $Revision: 1.3.8.2 $  $Date: 2009/07/21 08:41:20 $
 *
 * $Log: PUB_TEMPLET_1_ITEM_VO.java,v $
 * Revision 1.3.8.2  2009/07/21 08:41:20  wangqi
 * *** empty log message ***
 *
 * Revision 1.3.8.1  2008/09/16 06:12:47  wangqi
 * patch   : 20080916
 * file    : nova_20080128_20080916.jar
 * content : 处理 MR nova20-87,nova20-30；
 * 另外，改写了快速查询面板的处理。
 *
 *
 **************************************************************************/