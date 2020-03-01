package smartx.flex.components.event
{
	import flash.events.Event;

	public class BillCardPanelEvent extends Event
	{
		public static const SAVE:String = "save";
		public static const INSERT:String = "insert";
		public static const SAVESUCCESSFUL:String = "saveSuccessful";
		public static const INITCOMPLETE:String = "initComplete";
		public static const DELETESUCCESSFUL:String = "deleteSuccessful";
		public static const SETDATAVALUE_COMPLETE:String = "setDataValueComplete";
		public static const SETSEQ_COMPLETE:String = "setSeqComplete";
		public static const REFRESHCHILDLIST:String = "refreshChildList";//标志需要刷新子列表
		public static const OPERATION_FAILED:String = "operationFailed";
		
		public function BillCardPanelEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
		
	}
}