/***********************************************************************
 * $RCSfile: NovaBSCommUtil.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:21 $
 * $Log: NovaBSCommUtil.java,v $
 * Revision 1.2  2007/05/31 07:38:21  qilin
 * code format
 *
 * Revision 1.1  2007/05/21 03:04:18  qilin
 * no message
 *
 *************************************************************************/
package smartx.framework.metadata.bs;

import java.util.*;

public class NovaBSCommUtil {

    public HashMap getSystemProperties(HashMap _parmap) {
        HashMap returnMap = new HashMap();
        returnMap.put("return_1", System.getProperties());
        return returnMap;
    }

    public HashMap getServerEnvData(HashMap _parmap) {
        HashMap returnMap = new HashMap();
        returnMap.put("return_1", NovaServerEnvironment.getInstance().getAllData());
        return returnMap;
    }

    public HashMap getImagesFileName(HashMap _parmap) {
        HashMap returnMap = new HashMap();
        returnMap.put("return_1", (String[]) NovaServerEnvironment.getInstance().get("imagefiles"));
        return returnMap;
    }

}
