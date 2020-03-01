package smartx.flex.components.core.mtchart.event
{
	import flash.events.Event;

	/**
	 * @author zzy
	 * @date Jul 5, 2011
	 */
	public class MTProcessDataEvent extends Event
	{
		public static const PROCESSDATACONVERT_OK:String = "processDataConvertOK";
		
		public var xmlData:XML;
		
		public function MTProcessDataEvent(type:String,xmlData:XML,bubbles:Boolean=false,cancelable:Boolean=false)
		{
			//TODO: implement function
			super(type, bubbles, cancelable);
			this.xmlData = xmlData;
		}
	}
}