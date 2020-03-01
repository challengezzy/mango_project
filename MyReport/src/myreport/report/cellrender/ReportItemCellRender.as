/*
Copyright (c) 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


子报表呈现器


*/
package myreport.report.cellrender
{
	import hlib.LayoutUtil;
	import myreport.report.ReportCell;
	import myreport.report.ReportItem;

	internal class ReportItemCellRender extends ReportCellRenderBase
	{
		private var _Item:ReportItem;
		protected override function Disposing():void
		{
			_Item = null;
			super.Dispose();
		}
		
		public function ReportItemCellRender(cell:ReportCell, item:ReportItem)
		{
			super(cell);
			_Item = item;
		}
		
		public override function Render():void
		{
			var item:ReportItem = _Item;
			if(!Cell.contains(item))
				Cell.addChild(item);
			LayoutUtil.HorizontalAlign(item, Cell.width, Style.TextAlign);
			if(CanGrow && item.height > Cell.height)
			{
				Cell.height = item.height;
			}
			
			RefreshY();
		}
		private function RefreshY():void
		{
			LayoutUtil.VerticalAlign(_Item, Cell.height, Style.VerticalAlign);
			
		}
		public override function RefreshHeight():void
		{
			RefreshY();
		}
	}
}