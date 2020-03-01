/**************************************************************************
 *
 * $RCSfile: SysDictionaryCategory.java,v $    $Revision: 1.2 $    $Date: 2007/05/31 07:41:36 $
 *
 * $Log: SysDictionaryCategory.java,v $
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
 * Revision 1.7  2004/09/29 05:16:28  yuhong
 * MR#: NM6XTASK-463
 *
 * Revision 1.6  2004/08/19 06:45:24  yuhong
 * MR#:DN2-696
 *
 * Revision 1.5  2004/03/23 02:36:45  taienzhi
 * MR#:NM60-9999
 *
 * Revision 1.4  2004/03/23 02:05:09  miemie
 * MR#:NM60-0000
 *
 * Revision 1.3  2004/03/23 01:32:57  miemie
 * MR#:NM60-0000
 *
 * Revision 1.2  2004/03/22 10:18:25  miemie
 * MR#:NM60-0000
 *
 * Revision 1.1  2004/03/22 08:26:05  miemie
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

public class SysDictionaryCategory implements Serializable, DObjectInterface {
	private static final long serialVersionUID = 1L;
	protected String className;
    protected String attributeName;
    protected String classID;
    protected String attributeID;
    protected long id;
    public int oid;
    protected byte persistType;
    private long version;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

//    public Vector getSysDictionarys() {
//        return (Vector) sysDictionarys.getValue();
//    }
//
//    public void setSysDictionarys(Vector _sysDictionarys) {
//        sysDictionarys.setValue(_sysDictionarys);
//    }
//
//    public void addSysDictionary(SysDictionary _SysDictionary) {
//        Vector vt = (Vector) sysDictionarys.getValue();
//        vt.addElement(_SysDictionary);
//    }
//
//    public void removeSysDictionary(SysDictionary _SysDictionary) {
//        Vector vt = (Vector) sysDictionarys.getValue();
//        vt.removeElement(_SysDictionary);
//    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    //add by yuhong 2004/08/19
    private byte subSystem;
    private byte module;
    private byte modifyType;

    public void setSubSystem(byte _subSystem) {
        subSystem = _subSystem;
    }

    public byte getSubSystem() {
        return subSystem;
    }

    public void setModule(byte _module) {
        module = _module;
    }

    public byte getModule() {
        return module;
    }

    public void setModifyType(byte _modifyType) {
        modifyType = _modifyType;
    }

    public byte getModifyType() {
        return modifyType;
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
