package smartx.framework.metadata.vo;

import java.io.*;

public class Pub_Templet_1_ItemVO implements Serializable {

    private static final long serialVersionUID = -9091409633160181848L;

    private Pub_Templet_1VO pub_Templet_1VO = null;
    private String itemkey;
    private String itemname;
    private String itemtype; //
    private String savedcolumndatatype; //保存表中列的真正数据类型,比如varchar,number,date
    private String comboxdesc; //下拉框对应的SQL
    private ComBoxItemVO[] comBoxItemVos = null; //下拉框对应的所有数据项.下拉框的数据是在取查询模板时就直接取出来的!!!,而参照是点击按钮时才取的!!
    private String refdesc; //参照对应的SQL
    private String clientrefdesc;//参照客户端取值sql
    private String refdesc_type; //参照类型,包括"TABLE","TREE","CUST",即表型参照,树型参照,自定义参照!!!
    private String refdesc_realsql; //参照对应的SQL,即去掉前面的TREE:,CUST:等,但包括{}
    private String refdesc_datasourcename; //参照对应的数据源名称!!!
    private String refdesc_firstColName; //参照对应的SQL中的第一个列的名称!!!!!非常重要,最后都是通过它在SQL后面为条件过滤的!!
    private Boolean issave;
    private String delfaultquerylevel; //1 在面板中直接可见的查询，2 在查询对话框中可见的本询，3 不参与查询
    private Boolean ismustinput;
    private String loadformula;
    private String editformula;
    private Integer showorder;
    private Integer listwidth;
    private Integer cardwidth;
    private Boolean listisshowable;
    private String listiseditable; //列表是否可编辑1都可以编辑/2只有新增可以编辑/3新增不能编辑修改可以编辑/4全部不能编辑
    private Boolean cardisshowable;
    private String cardiseditable; //卡片是否可编辑
    private String defaultvalueformula;
    private String colorformula;
    //James.W Add 2008.06.11
    private String bcolorformula;  //字段背景色公式
    private String itemaction;     //字段响应设置
    
    private Boolean isMustCondition;  //作为条件时是否必输
    private String defaultCondition; //作为条件时的默认值公式
    private String conditionItemType=null;  //条件控件类型
    private String conditionComboxDesc=null;  //条件下拉设置
    private ComBoxItemVO[] conditionComBoxItemVos=null;//条件下拉列表
    private String conditionRefDesc=null;     //条件参照设置
    private String conditionRefDescType=null;
    private String conditionRefDescRealSql=null;
    private String conditionRefDescFirstColName=null;
    private String conditionRefDescDataSourceName=null;
    private Integer conditionShowOrder=new Integer(0); //条件显示次序
    //add by xuzhilin for flex
    private Pub_Templet_1_ItemGroupVO itemGroup;
    
    private String extattr01;
    private String extattr02;
    private String extattr03;
    private String extattr04;
    private String extattr05;
    private String extattr06;
    private String extattr07;
    private String extattr08;
    private String extattr09;
    private String extattr10;
    
    private String id;

    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Pub_Templet_1_ItemGroupVO getItemGroup() {
		return itemGroup;
	}

	public void setItemGroup(Pub_Templet_1_ItemGroupVO itemGroup) {
		this.itemGroup = itemGroup;
	}

	public String isCardiseditable() {
        return cardiseditable;
    }

    public void setCardiseditable(String _cardiseditable) {
        this.cardiseditable = _cardiseditable;
    }

    public Boolean isCardisshowable() {
        return cardisshowable;
    }

    public void setCardisshowable(Boolean cardisshowable) {
        this.cardisshowable = cardisshowable;
    }

    public Integer getCardwidth() {
        return cardwidth;
    }

    public void setCardwidth(Integer cardwidth) {
        this.cardwidth = cardwidth;
    }

    //	public String getColumnname() {
    //		return columnname;
    //	}
    //
    //	public void setColumnname(String columnname) {
    //		this.columnname = columnname;
    //	}

    public String getComboxdesc() {
        return comboxdesc;
    }

    public void setComboxdesc(String comboxdesc) {
        this.comboxdesc = comboxdesc;
    }

    public String getEditformula() {
        return editformula;
    }

    public void setEditformula(String editformula) {
        this.editformula = editformula;
    }

    public String getItemkey() {
        return itemkey;
    }

    public void setItemkey(String itemkey) {
        this.itemkey = itemkey;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getItemtype() {
        return itemtype;
    }

    public void setItemtype(String itemtype) {
        this.itemtype = itemtype;
    }

    public String isListiseditable() {
        return listiseditable;
    }

    public void setListiseditable(String _listiseditable) {
        this.listiseditable = _listiseditable;
    }

    public Boolean isListisshowable() {
        return listisshowable;
    }

    public void setListisshowable(Boolean listisshowable) {
        this.listisshowable = listisshowable;
    }

    public Integer getListwidth() {
        return listwidth;
    }

    public void setListwidth(Integer listwidth) {
        this.listwidth = listwidth;
    }

    public String getLoadformula() {
        return loadformula;
    }

    public void setLoadformula(String loadformula) {
        this.loadformula = loadformula;
    }

    public String getRefdesc() {
        return refdesc;
    }

    public void setRefdesc(String refdesc) {
        this.refdesc = refdesc;
    }

    //	public String getRefItemSQLFirstFieldName() {
    //		return refItemSQLFirstFieldName;
    //	}
    //
    //	public void setRefItemSQLFirstFieldName(String refItemSQLFirstFieldName) {
    //		this.refItemSQLFirstFieldName = refItemSQLFirstFieldName;
    //	}

    public Integer getShoworder() {
        return showorder;
    }

    public void setShoworder(Integer showorder) {
        this.showorder = showorder;
    }

    public String getCardiseditable() {
        return cardiseditable;
    }

    public Boolean getCardisshowable() {
        return cardisshowable;
    }

    public String getListiseditable() {
        return listiseditable;
    }

    public Boolean getListisshowable() {
        return listisshowable;
    }

    public ComBoxItemVO[] getComBoxItemVos() {
        return comBoxItemVos;
    }

    public void setComBoxItemVos(ComBoxItemVO[] comBoxItemVos) {
        this.comBoxItemVos = comBoxItemVos;
    }

    public String getDelfaultquerylevel() {
        return delfaultquerylevel;
    }

    public void setDelfaultquerylevel(String _level) {
        this.delfaultquerylevel = _level;
    }

    public Boolean getIssave() {
        return issave;
    }

    public void setIssave(Boolean issave) {
        this.issave = issave;
    }

    public Pub_Templet_1VO getPub_Templet_1VO() {
        return pub_Templet_1VO;
    }

    public void setPub_Templet_1VO(Pub_Templet_1VO pub_Templet_1VO) {
        this.pub_Templet_1VO = pub_Templet_1VO;
    }

    //是否是主键
    public boolean isPrimaryKey() {
        String str_okname = getPub_Templet_1VO().getPkname();
        if (getItemkey().equals(str_okname)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isNeedSave() {
        return issave.booleanValue();
    }

    /**
     * 是否能够成功保存
     * @return
     */
    public boolean isCanSave() {
        String[] str_SavedTableHaveColumns = getPub_Templet_1VO().getRealSavedTableHaveColumns();
        if (str_SavedTableHaveColumns == null || str_SavedTableHaveColumns.length == 0) {
            return false;
        }

        for (int i = 0; i < str_SavedTableHaveColumns.length; i++) {
            if (getItemkey().equalsIgnoreCase(str_SavedTableHaveColumns[i])) {
                return true;
            }
        }

        return false;
    }

    public boolean isViewColumn() {
        String[] str_viewcolumns = getPub_Templet_1VO().getRealViewColumns(); //
        if (str_viewcolumns == null || str_viewcolumns.length == 0) {
            return false;
        }

        for (int i = 0; i < str_viewcolumns.length; i++) {
            if (getItemkey().equalsIgnoreCase(str_viewcolumns[i])) {
                return true;
            }
        }
        return false;
    }
    /**
     * 是否必输项
     * @return
     */
    public boolean isMustInput() {
        return ismustinput.booleanValue();
    }
    /**
     * 获得是否必输项
     * @return
     */
    public Boolean getIsMustInput() {
        return ismustinput;
    }

    /**
     * 设置是否必输项
     * @param _ismustinput
     */
    public void setIsMustInput(Boolean _ismustinput) {
        this.ismustinput = _ismustinput;
    }
    
    /**
     * 是否必输条件项
     * @return
     */
    public boolean isMustCondition() {
        return this.isMustCondition.booleanValue();
    }
    /**
     * 获得是否必输条件项
     * @return
     */
    public Boolean getIsMustCondition() {
        return isMustCondition;
    }

    /**
     * 设置是否必输条件项
     * @param _ismustinput
     */
    public void setIsMustCondition(Boolean _isMustCondition) {
        this.isMustCondition = _isMustCondition;
    }
    
    /**
     * 获得条件的默认值公式
     * @return
     */
    public String getDefaultCondition() {
        return defaultCondition;
    }

    /**
     * 设置条件的默认值公式
     * @param formula
     */
    public void setDefaultCondition(String formula) {
        this.defaultCondition = formula;
    }
    
    /**
     * 获得条件控件类型
     * @return
     */
    public String getConditionItemType() {
        return conditionItemType;
    }

    /**
     * 设置条件控件类型
     * @param formula
     */
    public void setConditionItemType(String conditionItemType) {
        this.conditionItemType = conditionItemType;
    }
    
    /**
     * 获得条件的下拉框定义
     * @return
     */
    public String getConditionComboxDesc() {
        return conditionComboxDesc;
    }

    /**
     * 设置条件的下拉框定义
     * @param formula
     */
    public void setConditionComboxDesc(String conditionComboxDesc) {
        this.conditionComboxDesc = conditionComboxDesc;
    }
    
    /**
     * 设置条件下拉框ItemVo
     * @return
     */
    public ComBoxItemVO[] getConditionComBoxItemVos() {
        return conditionComBoxItemVos;
    }

    /**
     * 获得条件下拉框的itemvo
     * @param comBoxItemVos
     */
    public void setConditionComBoxItemVos(ComBoxItemVO[] conditionComBoxItemVos) {
        this.conditionComBoxItemVos = conditionComBoxItemVos;
    }
    
    /**
     * 获得条件的参照定义
     * @return
     */
    public String getConditionRefDesc() {
        return conditionRefDesc;
    }

    /**
     * 设置条件的参照定义
     * @param formula
     */
    public void setConditionRefDesc(String conditionRefDesc) {
        this.conditionRefDesc = conditionRefDesc;
    }

    /**
     * 获得条件的参照定义
     * @return
     */
    public Integer getConditionShowOrder() {
        return conditionShowOrder;
    }

    /**
     * 设置条件的参照定义
     * @param formula
     */
    public void setConditionShowOrder(Integer conditionShowOrder) {
        this.conditionShowOrder = conditionShowOrder;
    }
    
    public String getConditionRefDescType() {
        return conditionRefDescType;
    }

    public void setConditionRefDescType(String conditionRefDescType) {
        this.conditionRefDescType = conditionRefDescType;
    }

    public String getConditionRefDescRealSql() {
        return conditionRefDescRealSql;
    }

    public void setConditionRefDescRealSql(String conditionRefDescRealSql) {
        this.conditionRefDescRealSql = conditionRefDescRealSql;
    }

    public String getConditionRefDescFirstColName() {
        return conditionRefDescFirstColName;
    }

    public void setConditionRefDescFirstColName(String conditionRefDescFirstColName) {
        this.conditionRefDescFirstColName = conditionRefDescFirstColName;
    }

    public String getConditionRefDescDataSourceName() {
        return conditionRefDescDataSourceName;
    }

    public void setConditionRefDescDataSourceName(String conditionRefDescDataSourceName) {
        this.conditionRefDescDataSourceName = conditionRefDescDataSourceName;
    }

    /**
     * 设置属性值
     * @param _key
     * @param _value
     */
    public void setAttributeValue(String _key, Object _value) {
        if (_key.equalsIgnoreCase("itemkey")) {
            setItemkey( (String) _value);
        } else if (_key.equalsIgnoreCase("itemname")) {
            setItemname( (String) _value);
        } else if (_key.equalsIgnoreCase("itemtype")) {
            setItemtype( (String) _value);
        } else if (_key.equalsIgnoreCase("comboxdesc")) {
            setComboxdesc( (String) _value);
        } else if (_key.equalsIgnoreCase("refdesc")) {
            setRefdesc( (String) _value); //参照说明
        } else if (_key.equalsIgnoreCase("issave")) {
            setIssave( (Boolean) _value);
        } else if (_key.equalsIgnoreCase("isdefaultquery")) {
            setDelfaultquerylevel( (String) _value);
        } else if (_key.equalsIgnoreCase("ismustinput")) {
            setIsMustInput( (Boolean) _value);
        } else if (_key.equalsIgnoreCase("IsMustCondition")) {
        	setIsMustCondition( (Boolean) _value);
        } else if (_key.equalsIgnoreCase("DefaultCondition")) {
        	setDefaultCondition( (String) _value);
        } else if (_key.equalsIgnoreCase("ConditionItemType")) {
        	setConditionItemType( (String) _value);
        } else if (_key.equalsIgnoreCase("ConditionComboxDesc")) {
        	setConditionComboxDesc( (String) _value);
        } else if (_key.equalsIgnoreCase("ConditionRefDesc")) {
        	setConditionRefDesc( (String) _value);
        } else if (_key.equalsIgnoreCase("ConditionShowOrder")) {
        	setConditionShowOrder( (Integer) _value);
        } else if (_key.equalsIgnoreCase("loadformula")) {
            setLoadformula( (String) _value);
        } else if (_key.equalsIgnoreCase("editformula")) {
            setEditformula( (String) _value); //
        } else if (_key.equalsIgnoreCase("showorder")) {
            setShoworder( (Integer) _value);
        } else if (_key.equalsIgnoreCase("listwidth")) {
            setListwidth( (Integer) _value);
        } else if (_key.equalsIgnoreCase("cardwidth")) {
            setCardwidth( (Integer) _value);
        } else if (_key.equalsIgnoreCase("listisshowable")) {
            setListisshowable( (Boolean) _value);
        } else if (_key.equalsIgnoreCase("listiseditable")) {
            setListiseditable( (String) _value);
        } else if (_key.equalsIgnoreCase("cardisshowable")) {
            setCardisshowable( (Boolean) _value);
        } else if (_key.equalsIgnoreCase("cardiseditable")) {
            setCardiseditable( (String) _value);
        } else if (_key.equalsIgnoreCase("defaultvalueformula")) {
            setDefaultvalueformula( (String) _value);
        } else if (_key.equalsIgnoreCase("colorformula")) {
            setColorformula( (String) _value);
        } else if (_key.equalsIgnoreCase("savedcolumndatatype")) {
            setSavedcolumndatatype( (String) _value);
        } else if (_key.equalsIgnoreCase("bcolorformula")){
        	this.setBcolorformula((String) _value);
        } else if (_key.equalsIgnoreCase("itemaction")){
        	this.setItemaction((String) _value);
        }
    }

    public String getColorformula() {
        return colorformula;
    }

    public void setColorformula(String colorformula) {
        this.colorformula = colorformula;
    }

    public String getDefaultvalueformula() {
        return defaultvalueformula;
    }

    public void setDefaultvalueformula(String defaultvalueformula) {
        this.defaultvalueformula = defaultvalueformula;
    }

    public String getSavedcolumndatatype() {
        return savedcolumndatatype;
    }

    public void setSavedcolumndatatype(String savedcolumndatatype) {
        this.savedcolumndatatype = savedcolumndatatype;
    }

    public String getRefdesc_type() {
        return refdesc_type;
    }

    public void setRefdesc_type(String refdesc_type) {
        this.refdesc_type = refdesc_type;
    }

    public String getRefdesc_realsql() {
        return refdesc_realsql;
    }

    public void setRefdesc_realsql(String refdesc_realsql) {
        this.refdesc_realsql = refdesc_realsql;
    }

    public String getRefdesc_firstColName() {
        return refdesc_firstColName;
    }

    public void setRefdesc_firstColName(String refdesc_firstColName) {
        this.refdesc_firstColName = refdesc_firstColName;
    }

    public String getRefdesc_datasourcename() {
        return refdesc_datasourcename;
    }

    public void setRefdesc_datasourcename(String refdesc_datasourcename) {
        this.refdesc_datasourcename = refdesc_datasourcename;
    }
    
    public String getBcolorformula() {
		return bcolorformula;
	}

	public void setBcolorformula(String bcolorformula) {
		this.bcolorformula = bcolorformula;
	}

	public String getItemaction() {
		return itemaction;
	}

	public void setItemaction(String itemaction) {
		this.itemaction = itemaction;
	}

	public String getClientrefdesc() {
		return clientrefdesc;
	}

	public void setClientrefdesc(String clientrefdesc) {
		this.clientrefdesc = clientrefdesc;
	}

	public String getExtattr01() {
		return extattr01;
	}

	public void setExtattr01(String extattr01) {
		this.extattr01 = extattr01;
	}

	public String getExtattr02() {
		return extattr02;
	}

	public void setExtattr02(String extattr02) {
		this.extattr02 = extattr02;
	}

	public String getExtattr03() {
		return extattr03;
	}

	public void setExtattr03(String extattr03) {
		this.extattr03 = extattr03;
	}

	public String getExtattr04() {
		return extattr04;
	}

	public void setExtattr04(String extattr04) {
		this.extattr04 = extattr04;
	}

	public String getExtattr05() {
		return extattr05;
	}

	public void setExtattr05(String extattr05) {
		this.extattr05 = extattr05;
	}

	public String getExtattr06() {
		return extattr06;
	}

	public void setExtattr06(String extattr06) {
		this.extattr06 = extattr06;
	}

	public String getExtattr07() {
		return extattr07;
	}

	public void setExtattr07(String extattr07) {
		this.extattr07 = extattr07;
	}

	public String getExtattr08() {
		return extattr08;
	}

	public void setExtattr08(String extattr08) {
		this.extattr08 = extattr08;
	}

	public String getExtattr09() {
		return extattr09;
	}

	public void setExtattr09(String extattr09) {
		this.extattr09 = extattr09;
	}

	public String getExtattr10() {
		return extattr10;
	}

	public void setExtattr10(String extattr10) {
		this.extattr10 = extattr10;
	}

}
/**************************************************************************
 * $RCSfile: Pub_Templet_1_ItemVO.java,v $  $Revision: 1.2 $  $Date: 2010/05/15 10:36:03 $
 *
 * $Log: Pub_Templet_1_ItemVO.java,v $
 * Revision 1.2  2010/05/15 10:36:03  xuzhil
 * *** empty log message ***
 *
 * Revision 1.1  2010/05/07 01:20:39  xuzhil
 * *** empty log message ***
 *
 * Revision 1.2.8.3  2009/07/21 08:41:15  wangqi
 * *** empty log message ***
 *
 * Revision 1.2.8.2  2008/09/16 06:13:00  wangqi
 * patch   : 20080916
 * file    : nova_20080128_20080916.jar
 * content : 处理 MR nova20-87,nova20-30；
 * 另外，改写了快速查询面板的处理。
 *
 * Revision 1.1  2008/09/01 07:38:12  wangqi
 * *** empty log message ***
 *
 * Revision 1.4  2008/06/11 16:41:03  wangqi
 * *** empty log message ***
 *
 * Revision 1.3  2008/03/31 00:37:00  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2007/05/31 07:38:20  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:03:16  qilin
 * no message
 *
 * Revision 1.2  2007/01/30 04:16:43  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/