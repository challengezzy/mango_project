package smartx.flex.components.core.ext
{
	import flash.events.MouseEvent;
	
	import mx.controls.Tree;
	import mx.controls.listClasses.IListItemRenderer;
	
	public class TreeWrapper extends Tree
	{
		public function TreeWrapper()
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