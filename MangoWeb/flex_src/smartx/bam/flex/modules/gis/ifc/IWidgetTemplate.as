package smartx.bam.flex.modules.gis.ifc
{
	public interface IWidgetTemplate
	{
		function set baseWidget(value:IWidgetControl):void;
		
		function set widgetState(value:String):void;
		
		function set draggable(value:Boolean):void;
		
		function set resizable(value:Boolean):void;
	}
}