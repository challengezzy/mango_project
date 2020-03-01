/**************************************************************************
 * $RCSfile: ClusterTool.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:23 $
 **************************************************************************/
package smartx.framework.common.ui;

import smartx.framework.common.bs.*;
import smartx.framework.metadata.vo.*;

//test
public class ClusterTool {

    public static String getHost() {
        String result = "";
        String getSQL = "select IP,PORT,COUNT from PUB_HOST";
        TableDataStruct datastruct;
        try {
            datastruct = new CommDMO().getTableDataStructByDS(null, getSQL);
            int ip_pos = -1;
            int port_pos = -1;
            int count_pos = -1;
            for (int i = 0; i < datastruct.getTable_header().length; i++) {
                if (datastruct.getTable_header()[i].equals("IP")) {
                    ip_pos = i;
                } else if (datastruct.getTable_header()[i].equals("PORT")) {
                    port_pos = i;
                } else if (datastruct.getTable_header()[i].equals("COUNT")) {
                    count_pos = i;
                }
            }
            int min = new Integer(datastruct.getTable_body()[0][count_pos]).intValue();
            result = datastruct.getTable_body()[0][ip_pos] + ":" + datastruct.getTable_body()[0][port_pos];
            int i = 1;
            do {
                if (min > new Integer(datastruct.getTable_body()[i][count_pos]).intValue()) {
                    min = new Integer(datastruct.getTable_body()[i][count_pos]).intValue();
                    result = datastruct.getTable_body()[i][ip_pos] + ":" + datastruct.getTable_body()[i][port_pos];
                }
                i++;
            } while (i < datastruct.getTable_body().length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String[] getRealHostAndPort() {
        String str_ip = null;
        String str_port = null;
        String str_sql = "select ip,port from pub_clusterhost order by count asc";
        try {
            String[][] str_data = new CommDMO().getStringArrayByDS(null, str_sql);
            if (str_data != null && str_data.length > 0) {
                str_ip = str_data[0][0];
                str_port = str_data[0][1];
                new CommDMO().executeUpdateByDS(null,
                                                "update pub_clusterhost set count=count+1 where ip='" + str_ip +
                                                "' and port='" +
                                                str_port + "'"); //
                return new String[] {str_ip, str_port};
            } else {
                return null;
            }
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }

    }

    public static void addConnection(String ip, String port) {
        if (ip != null && port != null) {
            String sql = "update PUB_HOST set count=count+1 where ip='" + ip + "' and port=" + port;

            try {
                new CommDMO().executeUpdateByDS(null, sql);
                System.out.println("New connection established to [" + ip + ":" + port + "]");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void delConnection(String ip, String port) {
        if (ip != null && port != null) {
            String sql = "update PUB_HOST set count=count-1 where ip='" + ip + "' and port=" + port;

            try {
                new CommDMO().executeUpdateByDS(null, sql);
                System.out.println("Release connection established to [" + ip + ":" + port + "]");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
/**************************************************************************
 * $RCSfile: ClusterTool.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:23 $
 *
 * $Log: ClusterTool.java,v $
 * Revision 1.2  2007/05/31 07:38:23  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 05:56:36  qilin
 * no message
 *
 * Revision 1.8  2007/04/04 02:04:37  shxch
 * *** empty log message ***
 *
 * Revision 1.7  2007/03/02 05:16:43  shxch
 * *** empty log message ***
 *
 * Revision 1.6  2007/03/02 05:02:50  shxch
 * *** empty log message ***
 *
 * Revision 1.5  2007/03/01 09:06:56  shxch
 * *** empty log message ***
 *
 * Revision 1.4  2007/03/01 06:44:42  shxch
 * *** empty log message ***
 *
 * Revision 1.3  2007/02/26 09:35:49  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 03:41:28  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/
