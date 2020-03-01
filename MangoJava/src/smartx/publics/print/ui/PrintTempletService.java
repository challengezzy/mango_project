/************************************************************************
 * $RCSfile: PrintTempletService.java,v $  $Revision: 1.3 $  $Date: 2007/10/10 03:27:52 $
 * $Log: PrintTempletService.java,v $
 * Revision 1.3  2007/10/10 03:27:52  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2007/09/11 08:04:31  john_liu
 * no message
 *
 * Revision
 *
 * created by john_liu, 2007.09.11    for MR#: 0000, 迁移孙雪峰代码
 *
 ************************************************************************/


package smartx.publics.print.ui;

import smartx.framework.common.ui.NovaClientEnvironment;
import smartx.framework.common.ui.NovaRemoteCallServiceIfc;
import smartx.publics.print.vo.Pub_PrintTempletVO;

public interface PrintTempletService
    extends NovaRemoteCallServiceIfc
{
//	取得元数据模板配置数据,永远从默认数据源取!!
    public Pub_PrintTempletVO getPub_PrintTempletVO( String _code, NovaClientEnvironment _clientEnv )
        throws Exception;
    
    public Pub_PrintTempletVO getPub_PrintTempletVO( String _code, NovaClientEnvironment _clientEnv,boolean querybyprintTempCode )
    throws Exception;

}
