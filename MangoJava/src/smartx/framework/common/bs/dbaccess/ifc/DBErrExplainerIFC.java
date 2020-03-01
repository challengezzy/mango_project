/**************************************************************************
 *
 * $RCSfile: DBErrExplainerIFC.java,v $  $Revision: 1.1 $  $Date: 2007/06/16 02:48:21 $
 *
 * $Log: DBErrExplainerIFC.java,v $
 * Revision 1.1  2007/06/16 02:48:21  qilin
 * no message
 *
 * Revision 1.2  2007/05/31 07:41:32  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:22:08  qilin
 * no message
 *
 * Revision 1.1  2007/02/27 07:21:02  yuhong
 * MR#:NMBF30-9999 2007/02/27
 * moved from afx
 *
 * Revision 1.1  2007/01/11 12:21:54  john_liu
 * 2006.01.11 by john_liu
 * MR#: BZM10-13
 *
 * Revision
 *
 * created by john_liu, 2007.01.05    for MR#: BZM10-13
 *
 ***************************************************************************/

package smartx.framework.common.bs.dbaccess.ifc;

import java.util.*;
import java.sql.SQLException;

/**
 * <p>Title: 数据库错误转换</p>
 * <p>Description: Web & Workflow Framework</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Gxlu inc., nm2 </p>
 * @author Jedi H. Zheng
 * @version 1.0
 */
public interface DBErrExplainerIFC {
    public SQLException resolve(Vector errInd) throws Exception;
}
