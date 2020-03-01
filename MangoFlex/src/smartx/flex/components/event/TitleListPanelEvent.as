package smartx.flex.components.event
{
	import flash.events.Event;
	
	public class TitleListPanelEvent extends Event
	{
		
		public var itemObj:Object = null;
		
		public static const ITEM_CLICK_SUCCESSFUL:String = "itemClickSuccessful";
		
		public function TitleListPanelEvent(type:String, item:Object=null, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
			this.itemObj = item;
		}
	}
}