package smartx.bam.flex.modules.report.utils
{
	import flash.events.Event;
	
	public class ReportEvent extends Event
	{
		public static const QUERY_COMPLETE:String = "queryComplete";
		public static const DATA_LOAD_COMPLETE:String = "dataLoadComplete";
		public static const REAL_VALUE_CHANGE:String = "realValueChange";
		public static const INIT_COMPLETE:String = "initComplete";
		public static const SET_DEFAULT_VALUE:String = "setDefaultValue";
		public var version:String;
		public function ReportEvent(type:String,version:String=null,bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
			this.version = version;
		}
	}
}