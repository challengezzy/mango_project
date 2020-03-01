/**
 * 
 */
package smartx.bam.bs.eventsource;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;
import org.dom4j.Document;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;

/**
 * @author sky
 * Description 用于测试的事件源
 */
public class TestEventSource implements EventSourceAdapter,Runnable
{
	private Logger logger = Logger.getLogger( this.getClass() );
	
	public static final String providerURI = "DEFAULTPROVIDERNAME";
	
	public static final String eventType = "bv4event";
	
	private int intervalTime = 10000;
	
	public int getIntervalTime()
	{
		return intervalTime;
	}

	public void setIntervalTime( int intervalTime )
	{
		this.intervalTime = intervalTime;
	}

	private Thread thread = new Thread(this);
	
	private Map<String,Object> datas = new HashMap<String,Object>();
	
	public Map<String, Object> getDatas()
	{
		return datas;
	}

	public void setDatas( Map<String, Object> datas )
	{
		this.datas = datas;
	}

	private EPServiceProvider provider ;
	
	private boolean isStart = false;
	
	public TestEventSource()
	{
		provider = EPServiceProviderManager.getProvider( providerURI );
	}

	/* (non-Javadoc)
	 * @see smartx.bam.bs.eventsource.EventSourceAdapter#start(org.dom4j.Document)
	 */
	@Override
	public void start( Document config )
	{
		System.out.println("threadId..."+thread.getId());
		isStart = true;
		if(Thread.State.NEW.equals( thread.getState() ))
			thread.start();
	}

	/* (non-Javadoc)
	 * @see smartx.bam.bs.eventsource.EventSourceAdapter#stop()
	 */
	@Override
	public void stop()
	{
		System.out.println("threadId..."+thread.getId());
		isStart = false;
	}

	@Override
	public void run()
	{
		
//		datas.put("ID", "12");
		Map<String,Object> data ;

		while(isStart)
		{
			data = new HashMap<String,Object>();
			
			int temp= new Random().nextInt(10);
			int nametemp = new Random().nextInt(7);
			String[] names = new String[]{"sky","holy","vampire","ADC","ADA","上海","北京","杭州"};
			data.put("name", names[nametemp]);
			data.put("code", "code"+temp);
			data.put("value", temp);
			System.out.println("事件源["+this.getClass().getName()+"]成功发送事件["+eventType+"];value = "+temp);
			provider.getEPRuntime().sendEvent( data,  eventType );
			try
			{
				Thread.sleep( intervalTime );
			}
			catch( InterruptedException e )
			{
				logger.error( "启动事件源错误！",e);
			}
		}
		
	}
	
	public static void main(String[] args)
	{
		TestEventSource t = new TestEventSource();
		
		t.start(null);
		
	}
}
