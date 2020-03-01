package smartx.system.common.sysexception;

import java.io.*;

public class NovaExceptionHandler {

    public NovaExceptionHandler() {

    }

    public static NovaException createNovaEx(String _detailInfo) {
        return createNovaEx(_detailInfo, null, new Exception());
    }

    public static NovaException createNovaEx(Exception _ex) {
        return createNovaEx(null, null, _ex);
    }

    public static NovaException createNovaEx(String _detailInfo,
                                             Exception _ex) {
        return createNovaEx(_detailInfo, null, _ex);
    }

    public static NovaException createNovaEx(String _detailInfo, String _debugInfo,
                                             Exception _ex) {
        NovaException sdhEx = (_ex instanceof NovaException ? (NovaException) _ex : new NovaException());

        NovaExceptionItem item = new NovaExceptionItem();
        item.setDetailInfo(_detailInfo);
        item.setDebugInfo(_debugInfo);

        if (_ex != null) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(out);
            _ex.printStackTrace(ps);
            item.addStackTrace(out.toString());
        }
        sdhEx.addException(item);
        return sdhEx;
    }
}
/***********************************************************************
 * $RCSfile: NovaExceptionHandler.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:41:35 $
 * $Log: NovaExceptionHandler.java,v $
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
