/**************************************************************************
 *
 * $RCSfile: SysDictionary.java,v $    $Revision: 1.2 $    $Date: 2007/05/31 07:41:36 $
 *
 * $Log: SysDictionary.java,v $
 * Revision 1.2  2007/05/31 07:41:36  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:22:11  qilin
 * no message
 *
 * Revision 1.2  2007/02/28 07:53:29  yuhong
 * MR#:NMBF30-9999 2007/02/28
 * implements DObjectInterface
 *
 * Revision 1.1  2007/02/27 07:22:07  yuhong
 * MR#:NMBF30-9999 2007/02/27
 * moved from afx
 *
 * Revision 1.10  2005/06/17 03:00:11  yuhong
 * MR#:DN2-2582
 * (可维护数据字典管理支持停用、启用功能)
 *
 * Revision 1.9  2004/09/29 05:16:28  yuhong
 * MR#: NM6XTASK-463
 *
 * Revision 1.8  2004/03/30 09:17:49  taienzhi
 * MR#:NM60-9999
 *
 * Revision 1.7  2004/03/23 02:36:31  taienzhi
 * MR#:NM60-9999
 *
 * Revision 1.6  2004/03/23 02:05:09  miemie
 * MR#:NM60-0000
 *
 * Revision 1.5  2004/03/23 02:02:22  miemie
 * MR#:NM60-0000
 *
 * Revision 1.4  2004/03/23 02:01:12  miemie
 * MR#:NM60-0000
 *
 * Revision 1.3  2004/03/23 01:32:52  miemie
 * MR#:NM60-0000
 *
 * Revision 1.2  2004/03/22 09:55:47  miemie
 * MR#:NM60-0000
 *
 * Revision 1.1  2004/03/22 08:26:04  miemie
 * MR#:NM60-0000
 *
 * Revision 1.1  2004/03/22 03:26:14  miemie
 * no message
 *
 * Revision 1.1  2004/03/19 05:51:35  miemie
 * no message
 *
 * Revision 1.1  2004/03/16 06:55:59  taienzhi
 * DataNetWork Initialization!
 *
 package gxlu.ossc.datanetwork.common.dataobject;
 import TOPLink.Public.Indirection.ValueHolder;
 import TOPLink.Public.Indirection.ValueHolderInterface;
 import gxlu.ossc.datanetwork.common.constant.SysConst;
 import java.io.Serializable;
 import java.util.Vector;
 /**
        Site class corresponding to the table site
        @version 1.0 3/31/2000
        @author Lu Shen Rong
  */
package smartx.system.sysdata.vo;

import java.io.*;

import smartx.system.common.interfaces.*;

public class SysDictionary implements Serializable, DObjectInterface {
	private static final long serialVersionUID = -666421032084930235L;
	protected String classID;
    protected String attributeID;
    protected int value;
    protected int seqNo;
    protected String valueCN;
    protected String valueEN;
    protected long id;
    protected long sysDictionaryCategoryId;
    protected byte type; //0-系统，1－用户
    protected String userComments;
    protected String systemComments;
    protected String abbrev;
    public int oid;
    protected byte persistType;
    private long version;

    public SysDictionary() {
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    public String getAttributeID() {
        return attributeID;
    }

    public void setAttributeID(String attributeID) {
        this.attributeID = attributeID;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    public String getValueCN() {
        return valueCN;
    }

    public void setValueCN(String valueCN) {
        this.valueCN = valueCN;
    }

    public String getValueEN() {
        return valueEN;
    }

    public void setValueEN(String valueEN) {
        this.valueEN = valueEN;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public String getUserComments() {
        return userComments;
    }

    public void setUserComments(String userComments) {
        this.userComments = userComments;
    }

    public String getSystemComments() {
        return systemComments;
    }

    public void setSystemComments(String systemComments) {
        this.systemComments = systemComments;
    }

    public String getAbbrev() {
        return abbrev;
    }

    public void setAbbrev(String abbrev) {
        this.abbrev = abbrev;
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public byte getPersistType() {
        return persistType;
    }

    public void setPersistType(byte persistType) {
        this.persistType = persistType;
    }

    public long getSysDictionaryCategoryId() {
        return sysDictionaryCategoryId;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    //add by yuhong 2004/09/28
    private String appModule;
    public void setAppModule(String _appModule) {
        appModule = _appModule;
    }

    public String getAppModule() {
        return appModule;
    }

    //add by yuhong 2005/06/17
    private byte state; //状态（启用、停用）
    public void setState(byte _state) {
        state = _state;
    }

    public byte getState() {
        return state;
    }

}
