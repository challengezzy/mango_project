/***********************************************************************
 * $RCSfile: SystemAppDBImpl.java,v $  $Revision: 1.1.4.1 $  $Date: 2009/01/09 03:15:58 $
 * $Log: SystemAppDBImpl.java,v $
 * Revision 1.1.4.1  2009/01/09 03:15:58  wangqi
 * *** empty log message ***
 *
 * Revision 1.1  2007/08/30 13:00:59  wangqi
 * *** empty log message ***
 *
 * Revision 1.3  2007/06/05 10:25:11  qilin
 * no message
 *
 * Revision 1.2  2007/05/31 07:41:36  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:22:12  qilin
 * no message
 *
 * Revision 1.4  2007/05/15 09:29:20  qilin
 * no message
 *
 * Revision 1.3  2007/04/04 01:46:58  qilin
 * no message
 *
 * Revision 1.2  2007/04/02 05:38:12  qilin
 * no message
 *
 * Revision 1.1  2007/03/23 05:52:31  qilin
 * no message
 *
 * Revision 1.1  2007/03/21 06:38:26  qilin
 * no message
 *
 *************************************************************************/
package smartx.framework.common.dsmanager.bs;

import java.util.*;

import smartx.framework.common.bs.dbaccess.dsmgr.DataSourceManager;
import smartx.framework.common.dsmanager.ui.SystemAppDBIFC;


public class SystemAppDBImpl implements SystemAppDBIFC {
    public SystemAppDBImpl() {
    }
    
    /*
     * 传递数据源
     */
     
    public String[] dbTransmission(){    	   
    	return DataSourceManager.getDataSources();        
    }
    

}
