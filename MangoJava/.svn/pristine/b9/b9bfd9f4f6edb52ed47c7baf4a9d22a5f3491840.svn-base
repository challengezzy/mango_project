/************************************************************************
 * $RCSfile: PrintTempletServiceImpl.java,v $  $Revision: 1.3 $  $Date: 2007/10/10 03:27:41 $
 * $Log: PrintTempletServiceImpl.java,v $
 * Revision 1.3  2007/10/10 03:27:41  wangqi
 * *** empty log message ***
 *
 * Revision 1.2  2007/09/11 08:04:25  john_liu
 * no message
 *
 * Revision
 *
 * created by john_liu, 2007.09.11    for MR#: 0000, 迁移孙雪峰代码
 *
 ************************************************************************/

package smartx.publics.print.bs;

import smartx.framework.common.ui.NovaClientEnvironment;
import smartx.publics.print.ui.PrintTempletService;
import smartx.publics.print.vo.Pub_PrintTempletVO;

public class PrintTempletServiceImpl implements PrintTempletService
{

	public Pub_PrintTempletVO getPub_PrintTempletVO( String _code, NovaClientEnvironment _clientEnv ) throws Exception
	{
		PrintTempletDMO dmo = new PrintTempletDMO( );
		return dmo.getPub_PrintTempletVO( _code, _clientEnv );
	}

	public Pub_PrintTempletVO getPub_PrintTempletVO( String _code, NovaClientEnvironment _clientEnv,
			boolean querybyprintTempCode ) throws Exception
	{
		PrintTempletDMO dmo = new PrintTempletDMO( );
		return dmo.getPub_PrintTempletVO( _code, _clientEnv, querybyprintTempCode );
	}
}
