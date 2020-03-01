package smartx.bam.flex.modules.gis.headerController
{
	import mx.core.ClassFactory;
	
	import spark.components.DataGroup;
	
	[Event(name="widgetItemClick", type="flash.events.Event")]
	[Event(name="widgetItemMouseOver", type="flash.events.Event")]
	[Event(name="widgetItemMouseOut", type="flash.events.Event")]
	public class WidgetItemDataGroup extends DataGroup
	{
		public function WidgetItemDataGroup()
		{
			super();
			this.itemRendererFunction = rendererFunction;
		}
		
		private function rendererFunction(item:Object):ClassFactory
		{
			/**
			 * 先不考虑分组的情况
			 */
			return new ClassFactory(WidgetItemDataGroupRenderer);
		}
	}
}