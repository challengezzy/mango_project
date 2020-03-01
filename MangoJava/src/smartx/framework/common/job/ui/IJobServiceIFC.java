/**************************************************************************
 * $RCSfile: IJobServiceIFC.java,v $    $Date: 2007/08/30 13:09:24 $
 ***************************************************************************/
package smartx.framework.common.job.ui;

/**
 * <p>Title: Jobservice接口</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Gxlu inc., rm2 </p>
 * @author wuyc
 * @version 2.5
 */
public interface IJobServiceIFC
{
	
	public void jobStart(long jobId) throws Exception ;
	
	public void jobStop(long jobId) throws Exception;

}

/************************************************************************
 * $RCSfile: IJobServiceIFC.java,v $    $Date: 2007/08/30 13:09:24 $
 *
 * $Log: IJobServiceIFC.java,v $
 * Revision 1.2  2007/08/30 13:09:24  wangqi
 * *** empty log message ***
 *
 * Revision 1.1  2007/08/30 12:06:07  wangqi
 * *** empty log message ***
 *  2008/8/16 01:28:06  wuyc
 * create
 *
 *************************************************************************/