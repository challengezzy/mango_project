package smartx.flex.modules.datatask
{
	import flash.events.Event;
	
	public class DataTaskEvent extends Event
	{
		public static const SUB_DATATASK_EDIT:String = "subDataTaskEdit";//进入子任务设计
		
		public static const RETURN_MAIN_DATATASK:String = "returnMainDataTask";//返回主任务
		
		public var contentXml:XML;
		
		public function DataTaskEvent(type:String,contentXml:XML=null, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			//TODO: implement function
			super(type, bubbles, cancelable);
			this.contentXml = contentXml;
		}
	}
}