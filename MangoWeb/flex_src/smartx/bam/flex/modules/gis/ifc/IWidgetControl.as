package smartx.bam.flex.modules.gis.ifc
{
	import org.openscales.core.control.IControl;
	
	public interface IWidgetControl extends IControl
	{
		function set widgetId(value:Number):void;
		function get widgetId():Number;
		
		function set widgetTitle(value:String):void;
		function get widgetTitle():String;
		
		function set widgetIcon(value:String):void;
		function get widgetIcon():String;
		
		function getState():String;
		function setState(value:String):void;
		
		function set preload(value:String):void;
		
		function get initialWidth():Number;
		function set initialWidth(value:Number):void;
		
		function get initialHeight():Number;
		function set initialHeight(value:Number):void;
		
		function get contentXml():XML;
		function set contentXml(value:XML):void;
		
		function get isDraggable():Boolean;
		function set isDraggable(value:Boolean):void;
		
		function get isResizeable():Boolean;
		function set isResizeable(value:Boolean):void;
	}
}