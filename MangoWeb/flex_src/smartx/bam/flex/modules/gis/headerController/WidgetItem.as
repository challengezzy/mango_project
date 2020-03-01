package smartx.bam.flex.modules.gis.headerController
{
	import flash.events.EventDispatcher;

	[Bindable]
	public class WidgetItem
	{
		public var id:Number; // id of the associated widget
		public var isGroup:Boolean;
		public var icon:Object;
		public var url:String; // url
		public var label:String;
		public var open:Boolean; // indicates whether the associated widget is open or closed
		public var widgets:Array;
	}
}