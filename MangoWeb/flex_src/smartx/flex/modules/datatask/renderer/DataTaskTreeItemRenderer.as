package smartx.flex.modules.datatask.renderer
{
	import mx.controls.treeClasses.TreeItemRenderer;
	import mx.controls.treeClasses.TreeListData;
	
	public class DataTaskTreeItemRenderer extends TreeItemRenderer
	{
		public function DataTaskTreeItemRenderer()
		{
			super();
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void{
			super.updateDisplayList(unscaledWidth,unscaledHeight);
			var mylistData:TreeListData=listData as TreeListData;
			if(mylistData == null)
				return;
			if(String(mylistData.item.@isNeedRun) == "false")
				label.setColor(0x999999);
			else
				label.setColor(0x000000);
		}
	}
}