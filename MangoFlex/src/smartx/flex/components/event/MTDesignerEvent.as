package smartx.flex.components.event
{
	import flash.events.Event;
	
	public class MTDesignerEvent extends Event
	{
		public static const DESIGN_END:String = "designEnd";
		
		public var sourceXML:XML;
		
		public function MTDesignerEvent(type:String, sourceXML:XML, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
			this.sourceXML = sourceXML;
		}
	}
}