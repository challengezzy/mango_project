package smartx.flex.components.event
{
	import flash.events.Event;

	public class StyleTemplateEvent extends Event
	{
		public static const EDIT:String = "edit";
		public static const VIEW:String = "view";
		public static const BEFORE_SAVE:String = "beforeSave";
		public static const SAVE:String = "save";
		public static const INSERT:String = "insert";
		public static const RETURN_TO_LIST:String = "returnToList";
		//add by zhangzz 20110321
		public static const SAVESUCCESSFUL:String = "saveSuccessful";
		//add by xuzhilin 20110407
		public static const ITEMCHANGE:String = "itemChange";//for styletemplate03
		public static const AFTERINIT:String = "afterInit";
		
		public var saveObject:Object;
		public function StyleTemplateEvent(type:String, saveObject:Object = null,bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
			this.saveObject = saveObject;
		}
		
	}
}