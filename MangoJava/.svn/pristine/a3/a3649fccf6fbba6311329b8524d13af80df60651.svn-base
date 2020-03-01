/**************************************************************************
 *
 * $RCSfile: ArchLog.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:41:29 $
 *
 * $Log: ArchLog.java,v $
 * Revision 1.2  2007/05/31 07:41:29  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:22:03  qilin
 * no message
 *
 * Revision 1.2  2007/02/28 07:53:23  yuhong
 * MR#:NMBF30-9999 2007/02/28
 * implements DObjectInterface
 *
 * Revision 1.1  2007/02/27 07:21:43  yuhong
 * MR#:NMBF30-9999 2007/02/27
 * moved from afx
 *
 * Revision 1.3  2003/11/19 03:15:56  fly_zzz
 * add comments
 *
 * Revision 1.2  2003/11/06 10:01:30  fly_zzz
 * ADD AFFECTEDOBJECTID
 *
 * Revision 1.1  2002/02/20 07:59:36  wzwu
 * no message
 *
 * Revision 1.4  2002/02/05 10:19:39  yxjjxy
 * change package
 *
 * Revision 1.3  2002/02/05 09:31:40  yxjjxy
 * change package
 *
 * Revision 1.2  2001/10/08 03:22:23  zma
 * Synchronize Core1.1 with Core1.0
 *
 * Revision 1.2  2001/09/24 03:51:25  yxjjxy
 * archLog: add appModule,change length of "operation"
 *
 * Revision 1.1  2001/08/27 06:42:53  zma
 * initialize for core
 *
 * Revision 1.1  2001/06/29 02:50:25  yxjjxy
 * archived log
 *
 *
 *
 ***************************************************************************/

package smartx.system.log.vo;

import java.io.*;
import java.util.*;

import smartx.system.common.interfaces.*;


public class ArchLog implements Serializable, DObjectInterface {

    protected long id;
    protected Date time;
    protected String source;
    protected String ipAddress;
    protected int serviceModule;
    protected String operation;
    protected String affectedObjectType;
    protected String affectedObjectName;
    protected String comments;
    public int oid;
    protected String fileName;

    protected String appModule;

    public ArchLog() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getServiceModule() {
        return serviceModule;
    }

    public void setServiceModule(int serviceModule) {
        this.serviceModule = serviceModule;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getAffectedObjectType() {
        return affectedObjectType;
    }

    public void setAffectedObjectType(String affectedObjectType) {
        this.affectedObjectType = affectedObjectType;
    }

    public String getAffectedObjectName() {
        return affectedObjectName;
    }

    public void setAffectedObjectName(String name) {
        this.affectedObjectName = name;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

//add 2000.5.12 by lsrong
    public void setPersistType(byte pt) {
        this.persistType = pt;
    }

    public byte getPersistType() {
        return this.persistType;
    }

    protected byte persistType;

    /**
      used as optimitic lock control
     */
    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    private long version;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String _fileName) {
        fileName = _fileName;
    }

    public String getAppmodule() {
        return appModule;
    }

    public void setAppmodule(String _appModule) {
        this.appModule = _appModule;
    }

//add by wxy for mr:NM50-6822 at 2003/11/06
    private long affectedObjectId;
    public long getAffectedObjectId() {
        return affectedObjectId;
    }

    public void setAffectedObjectId(long _affectedObjectId) {
        affectedObjectId = _affectedObjectId;
    }

    //add by wxy at 2003/11/19
    //--NM50-7178
    protected String comments2;
    public String getComments2() {
        return comments2;
    }

    public void setComments2(String _comments2) {
        this.comments2 = _comments2;
    }

    protected String comments3;
    public String getComments3() {
        return comments3;
    }

    public void setComments3(String _comments3) {
        this.comments3 = _comments3;
    }

    protected String comments4;
    public String getComments4() {
        return comments4;
    }

    public void setComments4(String _comments4) {
        this.comments4 = _comments4;
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int _oid) {
        this.oid = _oid;
    }

}
