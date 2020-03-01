/************************************************************************
 * $RCSfile: AbstractPrintProcess.java,v $  $Revision: 1.2 $  $Date: 2007/09/11 08:04:31 $
 * $Log: AbstractPrintProcess.java,v $
 * Revision 1.2  2007/09/11 08:04:31  john_liu
 * no message
 *
 * Revision
 *
 * created by john_liu, 2007.09.11    for MR#: 0000, 迁移孙雪峰代码
 *
 ************************************************************************/


package smartx.publics.print.ui;

import java.util.HashMap;

import smartx.publics.print.vo.Pub_PrintTempletVO;


/**
 * 打印处理,抛异常则跳过该次打印
 * @author Administrator
 *
 */
public abstract class AbstractPrintProcess
{
    public abstract void processData( Pub_PrintTempletVO printvo, HashMap data )
        throws Exception;
}
