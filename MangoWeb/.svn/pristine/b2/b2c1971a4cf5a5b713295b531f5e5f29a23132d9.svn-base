package smartx.bam.flex.modules.gis.event
{
	import flash.events.Event;
	
	public class GisEvent extends Event
	{
		public static const WIDGET_STATE_CHANGED:String = "widgetStateChanged";
		
		public static const WIDGET_RUN:String = "widgetRunRequested";
		
		private var _data:Object;
		
		public function GisEvent(type:String,data:Object=null, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
			_data = data;
		}
		
		public function get data():Object
		{
			return _data;
		}
		
	}
}