package smartx.flex.components.itemcomponent.ext 
{
	import flash.events.KeyboardEvent;
	
	import mx.collections.ArrayCollection;
	import mx.controls.DataGrid;
	import mx.controls.dataGridClasses.DataGridColumn;
	import mx.controls.listClasses.IListItemRenderer;
	import mx.core.ClassFactory;
	import smartx.flex.components.itemcomponent.renderer.CheckBoxHeaderRenderer;
	import smartx.flex.components.itemcomponent.renderer.CheckBoxItemRenderer;
	
	
	/** 
	 *  DataGrid that uses checkboxes for multiple selection
	 */
	public class CheckBoxDataGrid extends DataGrid
	{
		public function CheckBoxDataGrid()
		{
			allowMultipleSelection = true;
		}
		
		override public function set columns(value:Array):void
		{   
			var arr:Array = value.concat();
			
			super.columns = arr;
		}
			
		override protected function selectItem(item:IListItemRenderer,
			shiftKey:Boolean, ctrlKey:Boolean,
			transition:Boolean = true):Boolean
		{
			if(item is CheckBoxItemRenderer)
				return super.selectItem(item, false, true, transition);
			return false;
		}
		
		override protected function keyDownHandler(event:KeyboardEvent):void
		{
			event.ctrlKey = true;
			event.shiftKey = false;
			super.keyDownHandler(event);
		}
		
		public function deleteItems():void
		{
			for each(var item:Object in selectedItems)
			{
				var index:int = dataProvider.getItemIndex(item);
				dataProvider.removeItemAt(index);
			}

		}
	}
	
}