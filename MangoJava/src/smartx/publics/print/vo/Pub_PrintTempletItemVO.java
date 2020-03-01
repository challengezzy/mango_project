/************************************************************************
 * $RCSfile: Pub_PrintTempletItemVO.java,v $  $Revision: 1.2 $  $Date: 2007/09/11 08:04:39 $
 * $Log: Pub_PrintTempletItemVO.java,v $
 * Revision 1.2  2007/09/11 08:04:39  john_liu
 * no message
 *
 * Revision
 *
 * created by john_liu, 2007.09.11    for MR#: 0000, 迁移孙雪峰代码
 *
 ************************************************************************/


package smartx.publics.print.vo;

import java.io.Serializable;

public class Pub_PrintTempletItemVO
    implements Serializable
{
    private String id = null;

    private String printtempletid = null;

    private String itemkey = null;

    private String showname = null;

    private String namewidthabsolute = null;

    private String namewidthpercent = null;

    private String valuewidthabsolute = null;

    private String valuewidthpercent = null;

    private String aligntype = null;

    private String isshownull = null;

    private String isonerow = null;

    private String value = null;

    private String valueformula = null;

    private String isshow = null;

    public String getAligntype()
    {
        return aligntype;
    }

    public void setAligntype( String aligntype )
    {
        this.aligntype = aligntype;
    }

    public String getId()
    {
        return id;
    }

    public void setId( String id )
    {
        this.id = id;
    }

    public String getIsonerow()
    {
        return isonerow;
    }

    public void setIsonerow( String isonerow )
    {
        this.isonerow = isonerow;
    }

    public String getIsshow()
    {
        return isshow;
    }

    public void setIsshow( String isshow )
    {
        this.isshow = isshow;
    }

    public String getIsshownull()
    {
        return isshownull;
    }

    public void setIsshownull( String isshownull )
    {
        this.isshownull = isshownull;
    }

    public String getItemkey()
    {
        return itemkey;
    }

    public void setItemkey( String itemkey )
    {
        this.itemkey = itemkey;
    }

    public String getNamewidthabsolute()
    {
        return namewidthabsolute;
    }

    public void setNamewidthabsolute( String namewidthabsolute )
    {
        this.namewidthabsolute = namewidthabsolute;
    }

    public String getNamewidthpercent()
    {
        return namewidthpercent;
    }

    public void setNamewidthpercent( String namewidthpercent )
    {
        this.namewidthpercent = namewidthpercent;
    }

    public String getPrinttempletid()
    {
        return printtempletid;
    }

    public void setPrinttempletid( String printtempletid )
    {
        this.printtempletid = printtempletid;
    }

    public String getShowname()
    {
        return showname;
    }

    public void setShowname( String showname )
    {
        this.showname = showname;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue( String value )
    {
        this.value = value;
    }

    public String getValueformula()
    {
        return valueformula;
    }

    public void setValueformula( String valueformula )
    {
        this.valueformula = valueformula;
    }

    public String getValuewidthabsolute()
    {
        return valuewidthabsolute;
    }

    public void setValuewidthabsolute( String valuewidthabsolute )
    {
        this.valuewidthabsolute = valuewidthabsolute;
    }

    public String getValuewidthpercent()
    {
        return valuewidthpercent;
    }

    public void setValuewidthpercent( String valuewidthpercent )
    {
        this.valuewidthpercent = valuewidthpercent;
    }
}
