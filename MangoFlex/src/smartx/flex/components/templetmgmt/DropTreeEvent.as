package smartx.flex.components.templetmgmt
{
	import flash.events.Event;

	public class DropTreeEvent extends Event
	{
		public static const DROP_FINISHED:String = "dropFinished";
		public var currentItem:*;
		public var targetParentItem:*;
		public function DropTreeEvent(type:String, currentItem:*,targetParentItem:*,bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
			this.currentItem = currentItem;
			this.targetParentItem = targetParentItem;
		}
		
	}
}