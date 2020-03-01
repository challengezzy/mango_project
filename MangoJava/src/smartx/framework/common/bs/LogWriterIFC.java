/***********************************************************************
 * $RCSfile: LogWriterIFC.java,v $  $Revision: 1.1 $  $Date: 2007/06/05 10:25:10 $
 * $Log: LogWriterIFC.java,v $
 * Revision 1.1  2007/06/05 10:25:10  qilin
 * no message
 *
*************************************************************************/
package smartx.framework.common.bs;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public abstract class LogWriterIFC {
     abstract public void writeLog(String sql) throws Exception;
     abstract public void writeLog(String operation, String sql) throws Exception;
}
