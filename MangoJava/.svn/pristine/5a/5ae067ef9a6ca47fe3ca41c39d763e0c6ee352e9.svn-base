/**
 * 
 */

package smartx.bam.bs.eventsource;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;

import smartx.bam.vo.SysConst;
import smartx.framework.common.bs.CommDMO;
import smartx.framework.common.vo.NovaLogger;

/**
 * @author sky Description
 */
public class EventSourceManager
{
	private Logger logger = NovaLogger.getLogger(this.getClass());
	public static Map<String, Object>	eventSourceObjs	= new HashMap<String, Object>();		// 存放事件源对象

	/**
	 * 启动事件源
	 * 
	 * @param className
	 */
	public void start( String className, String config ) throws Exception
	{
		logger.debug( "启动事件源[" + className + "]" );
		Document doc = null;
		if( config != null && !"".equals( config.trim() ))
			doc = DocumentHelper.parseText( config );
		Object eventSource = eventSourceObjs.get( className );
		if( eventSource != null && (eventSource instanceof EventSourceAdapter) )
		{
			((EventSourceAdapter) eventSource).start( doc );
			updateStatus( SysConst.STARTED, className );
		}
		else
		{
			eventSource = Class.forName( className ).newInstance();
			if( eventSource instanceof EventSourceAdapter )
			{
				((EventSourceAdapter) eventSource).start( doc );
				eventSourceObjs.put( className, eventSource );
				updateStatus( SysConst.STARTED, className );
			}
		}
	}

	/**
	 * 停止数据源
	 * 
	 * @param className
	 */
	public void stop( String className ) throws Exception
	{
		logger.debug( "停止事件源[" + className + "]" );
		Object eventSource = eventSourceObjs.get( className );
		if( eventSource != null && (eventSource instanceof EventSourceAdapter) )
		{
			((EventSourceAdapter) eventSource).stop();
			eventSourceObjs.remove( className );
			updateStatus( SysConst.STOPED, className );
		}
		else
		{
			eventSource = Class.forName( className ).newInstance();
			if( eventSource instanceof EventSourceAdapter )
			{
				((EventSourceAdapter) eventSource).stop();
				eventSourceObjs.put( className, eventSource );
				updateStatus( SysConst.STOPED, className );
			}
		}
	}

	/**
	 * 更新事件源状态
	 * 
	 * @param status
	 * @param className
	 */
	public void updateStatus( int status, String className )
	{
		CommDMO comm = new CommDMO();
		StringBuilder sql = new StringBuilder();
		sql.append( "update bam_eventdatasource b set b.status=? where b.ADAPTORCLASS=?" );
		try
		{
			logger.debug( "更新事件源状态sql..." + sql.toString() );
			comm.execAtOnceByDS( null, sql.toString(),status,className );
		}
		catch( Exception e )
		{
			logger.error( "更新状态错误！", e );
		}
		finally
		{
			comm.releaseContext( null );
		}

	}
}
