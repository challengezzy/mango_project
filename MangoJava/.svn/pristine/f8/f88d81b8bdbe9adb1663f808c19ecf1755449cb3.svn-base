package smartx.system.common.sysexception;

import java.util.*;

public class NovaException extends Exception {
    private Vector exceptionItemList = null;
    public NovaException() {
        exceptionItemList = new Vector();
    }

    public NovaException(String s) {
        super(s);
        exceptionItemList = new Vector();
    }

    public String getOriginalMessage() {
        return super.getMessage();
    }

    public Vector getExceptionItemList() {
        return exceptionItemList;
    }

    public Object getTopExceptionItem() {
        if (exceptionItemList.isEmpty()) {
            return null;
        }
        int i = exceptionItemList.size() - 1;
        return exceptionItemList.get(i);
    }

    public void addException(NovaExceptionItem exceptionItem) {
        exceptionItemList.addElement(exceptionItem);
    }

    public void addException(String detailInfo) {
        NovaExceptionItem seItem = new NovaExceptionItem();
        seItem.setDetailInfo(detailInfo);
        addException(seItem);
    }

    public void addException(String detailInfo, String debugInfo) {
        NovaExceptionItem seItem = new NovaExceptionItem();
        seItem.setDebugInfo(debugInfo);
        seItem.setDetailInfo(detailInfo);
        addException(seItem);
    }

    public void addException(Exception e) {
        exceptionItemList.addElement(e);
    }

    public void removeException() {
        int i = 0;
        i = exceptionItemList.size() - 1;
        exceptionItemList.removeElementAt(i);
    }

    public void removeAllExceptions() {
        exceptionItemList.removeAllElements();
    }

    public String toString() {
        String str = getMessage();
        return str;
    }

    public String getMessage() {
        String str = "";
        for (int i = exceptionItemList.size() - 1; i >= 0; i--) {
            Object obj = exceptionItemList.elementAt(i);
            if (obj instanceof NovaExceptionItem) {
                NovaExceptionItem exceptionItem = (NovaExceptionItem) obj;
                str = str + exceptionItem.getDetailInfo() + "\n";
            }else if (obj instanceof Exception) {
                Exception e = (Exception) obj;
                str = str + e.getMessage() + "\n";
            }
        }
        return str;
    }

    public String toDebugInfo() {
        String str = "";
        for (int i = exceptionItemList.size() - 1; i >= 0; i--) {
            Object obj = exceptionItemList.elementAt(i);
            if (obj instanceof NovaExceptionItem) {
                NovaExceptionItem sdhExcItem = (NovaExceptionItem) exceptionItemList.elementAt(i);
                str = str + sdhExcItem.getDebugInfo() + "\n";
            } else if (obj instanceof Exception) {
                Exception e = (Exception) obj;
                str = str + e.getMessage() + "\n";
            }
        }
        return str;
    }

    public String getSimpleMessage() {
        if (exceptionItemList == null || exceptionItemList.isEmpty()) {
            return "";
        }

        StringBuffer result = new StringBuffer();

        for (int i = 0, n = exceptionItemList.size(); i < n; i++) {
            NovaExceptionItem item = (NovaExceptionItem) exceptionItemList.get(i);
            String info = item.getDetailInfo();
            if (!"".equals(info)) {
                result.append(info);
                if (i != n - 1) {
                    result.append('\n');
                }
            }
        }
        return result.toString();
    }

    public void printStackTrace() {
        synchronized (System.err) {
            System.err.println("[" + new Date().toString() + "]");
            super.printStackTrace();
            System.err.println();
        }
    }
}

/***********************************************************************
 * $RCSfile: NovaException.java,v $  $Revision: 1.3 $  $Date: 2007/06/06 12:59:21 $
 * $Log: NovaException.java,v $
 * Revision 1.3  2007/06/06 12:59:21  qilin
 * no message
 *
 * Revision 1.2  2007/05/31 07:41:35  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:22:11  qilin
 * no message
 *
 * Revision 1.2  2007/03/29 06:18:26  shxch
 * 删除了不需要引用的包
 *
 * Revision 1.1  2007/03/07 05:49:14  qilin
 * no message
 *
 *************************************************************************/
