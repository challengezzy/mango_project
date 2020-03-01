package smartx.flex.components.event
{
	import flash.events.Event;
	
	public class UIComponentEvent extends Event
	{
		public static const REAL_VALUE_CHANGE:String = "realValueChange";
		public function UIComponentEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}

	}
}