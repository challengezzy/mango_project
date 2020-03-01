package smartx.system.common.sysexception;

import java.io.*;
import java.util.*;

public class NovaExceptionItem implements Serializable {
    /** 异常的StackTrace信息 */
    private String debugInfo = "";

    /** 提示用户的信息 */
    private String detailInfo = "";

    /** 异常发生的时间 */
    private Date date;

    public NovaExceptionItem() {
        date = new Date();
    }

    public NovaExceptionItem(String _detailInfo) {
        this();
        detailInfo = _detailInfo;
    }

    public NovaExceptionItem(String _detailInfo, String _debugInfo) {
        this();
        debugInfo = _debugInfo;
        detailInfo = _detailInfo;
    }

    public void setDebugInfo(String _debugInfo) {
        debugInfo = _debugInfo;
    }

    public void setDetailInfo(String _detailInfo) {
        detailInfo = _detailInfo;
    }

    public String getDebugInfo() {
        return debugInfo;
    }

    public String getDetailInfo() {
        return detailInfo;
    }

    public void addStackTrace(String _trace) {
        String s = super.toString();
        int i;

        if (debugInfo==null || debugInfo.equals("")) {
            i = 0;
        } else {
            i = s.length() + 2; //去掉换行，回车
        }
        this.debugInfo += _trace.substring(i);
    }

    public Date getDate() {
        return this.date;
    }
}
/***********************************************************************
 * $RCSfile: NovaExceptionItem.java,v $  $Revision: 1.3 $  $Date: 2007/06/06 12:59:21 $
 * $Log: NovaExceptionItem.java,v $
 * Revision 1.3  2007/06/06 12:59:21  qilin
 * no message
 *
 * Revision 1.2  2007/05/31 07:41:35  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:22:11  qilin
 * no message
 *
 * Revision 1.1  2007/03/07 05:49:13  qilin
 * no message
 *
 *************************************************************************/