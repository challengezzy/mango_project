package smartx.flex.components.core.ext
{
	import flash.events.MouseEvent;
	
	import mx.controls.DataGrid;
	import mx.controls.listClasses.IListItemRenderer;
	
	public class DataGridWrapper extends DataGrid
	{
		public function DataGridWrapper()
		{
			super();
		}
		
		public function selectItemByMouseEvent(e:MouseEvent):int{
			var render:IListItemRenderer = this.mouseEventToItemRenderer(e);
			if(render != null){
				var i:int = this.itemRendererToIndex(render);
				this.selectedIndex = i;
				return i;
			}
			return -1;
		}
	}
}