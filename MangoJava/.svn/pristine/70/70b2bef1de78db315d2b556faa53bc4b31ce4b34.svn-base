/**
 * 
 */

package smartx.bam.bs.eventsource;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;

/**
 * @author sky Description 告警消息事件源
 */
public class TopBusinessEventSource implements EventSourceAdapter, Runnable
{
	private Logger				logger			= Logger.getLogger( this.getClass() );

	public static final String	providerURI		= "DEFAULTPROVIDERNAME";
													
	public static final String	eventType		= "bvTOPBUSINESSEvent";

	private int					intervalTime	= 5000;

	public int getIntervalTime()
	{
		return intervalTime;
	}

	public void setIntervalTime( int intervalTime )
	{
		this.intervalTime = intervalTime;
	}

	private Thread				thread	= new Thread( this );

	private Map<String, Object>	datas	= new HashMap<String, Object>();

	public Map<String, Object> getDatas()
	{
		return datas;
	}

	public void setDatas( Map<String, Object> datas )
	{
		this.datas = datas;
	}

	private EPServiceProvider	provider;

	private boolean				isStart	= false;

	public TopBusinessEventSource()
	{
		provider = EPServiceProviderManager.getProvider( providerURI );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * smartx.bam.bs.eventsource.EventSourceAdapter#start(org.dom4j.Document)
	 */
	@Override
	public void start( Document config )
	{
		isStart = true;
		if( Thread.State.NEW.equals( thread.getState() ) )
			thread.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see smartx.bam.bs.eventsource.EventSourceAdapter#stop()
	 */
	@Override
	public void stop()
	{
		isStart = false;
	}

	@Override
	public void run()
	{

		while( isStart )
		{
			
			Map<String, Object> data = new HashMap<String,Object>();
			
//			data.put( "RAN", new Date() );
			provider.getEPRuntime().sendEvent( data, eventType );
 			System.out.println("事件源["+this.getClass().getName()+"]成功发送事件["+eventType+"]");
			try
			{
				Thread.sleep( intervalTime );
			}
			catch( InterruptedException e )
			{
				logger.error( "启动事件源错误！", e );
			}
		}

	}

	public static void main( String[] args )
	{
		TopBusinessEventSource t = new TopBusinessEventSource();

		t.start( null );

	}
}
