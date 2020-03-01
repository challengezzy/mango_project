/**************************************************************************
 *
 * $RCSfile: ExtendAttribute.java,v $    $Revision: 1.2 $    $Date: 2007/05/31 07:41:33 $
 *
 * $Log: ExtendAttribute.java,v $
 * Revision 1.2  2007/05/31 07:41:33  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:22:09  qilin
 * no message
 *
 * Revision 1.2  2007/02/28 07:52:15  yuhong
 * MR#:NMBF30-9999 2007/02/28
 * implements DObjectInterface
 *
 * Revision 1.1  2007/02/27 07:21:29  yuhong
 * MR#:NMBF30-9999 2007/02/27
 * moved from afx
 *
 * Revision 1.2  2004/09/29 05:47:42  yuhong
 * MR#: NM6XTASK-463
 * add appmodule
 *
 * Revision 1.1  2004/08/31 02:45:49  yuhong
 * MR#:DN2-9999
 * extendattribute moved from DN
 *
 * Revision 1.6  2004/08/25 02:55:41  yuhong
 * MR#:NM6XTASK-412
 *
 * Revision 1.5  2004/03/29 04:54:05  taienzhi
 * MR#:NM60-9999 ExtendAttribute
 *
 * Revision 1.4  2004/03/25 08:21:11  miemie
 * no message
 *
 * Revision 1.3  2004/03/25 02:30:10  miemie
 * no message
 *
 * Revision 1.2  2004/03/23 10:22:34  miemie
 * no message
 *
 * Revision 1.1  2004/03/22 03:26:14  miemie
 * no message
 *
 * Revision 1.1  2004/03/19 05:51:34  miemie
 * no message
 *
 * Revision 1.1  2004/03/16 06:55:59  taienzhi
 * DataNetWork Initialization!
 *
 ***************************************************************************/
package smartx.system.extendattribute.vo;

import java.io.*;

import smartx.system.common.interfaces.*;


public class ExtendAttribute implements Serializable, DObjectInterface {
    private long id;
    private int entity;
    private int attributeName;
    private String displayString;
    private boolean displayAble;
    private String comments;
    private long version;
    private byte persistType;
    private int oid;

    public ExtendAttribute() {
        displayAble = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getEntity() {
        return entity;
    }

    public void setEntity(int entity) {
        this.entity = entity;
    }

    public int getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(int attributeName) {
        this.attributeName = attributeName;
    }

    public String getDisplayString() {
        return displayString;
    }

    public void setDisplayString(String displayString) {
        this.displayString = displayString;
    }

    public boolean isDisplayAble() {
        return displayAble;
    }

    public void setDisplayAble(boolean displayAble) {
        this.displayAble = displayAble;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public byte getPersistType() {
        return persistType;
    }

    public void setPersistType(byte persistType) {
        this.persistType = persistType;
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    //add by yuhong 2004/08/25
    private byte type;
    public void setType(byte _type) {
        type = _type;
    }

    public byte getType() {
        return type;
    }

    //add by yuhong 2004/09/28
    private String appModule;
    public void setAppModule(String _appModule) {
        appModule = _appModule;
    }

    public String getAppModule() {
        return appModule;
    }

}
