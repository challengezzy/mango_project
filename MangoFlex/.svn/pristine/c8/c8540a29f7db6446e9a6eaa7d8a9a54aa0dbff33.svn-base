package smartx.flex.components.event
{
	import flash.events.Event;
	
	public class MetadataTempletUtilEvent extends Event
	{
		public static const INIT_GLOBAL_COMPLETE:String = "initGlobalComplete";
		public static const INIT_GLOBAL_FAILED:String = "initGlobalFailed";
		public static const INIT_USER_COMPLETE:String = "initUserComplete";
		public static const INIT_USER_FAILED:String = "initUserFailed";
		public static const FLUSH_MTCODE_COMPLETE:String = "flushMtcodeComplete";
		public static const FLUSH_MTCODE_FAILED:String = "flushMtcodeFailed";
		public var eventInfo:String;
		public var mtcode:String;
		
		public function MetadataTempletUtilEvent(type:String,eventInfo:String=null,mtcode:String=null,bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
			this.eventInfo = eventInfo;
			this.mtcode = mtcode;
		}
	}
}