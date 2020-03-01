/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


报表表格行。


*/
package myreport.report
{
	import hlib.DisposeUtil;
	
	import myreport.data.report.ReportSettings;
	import myreport.data.report.TableCellSetting;
	import myreport.data.report.TableRowSetting;
	import myreport.expression.ExpressionContext;
	
	internal class ReportTableRow extends ReportItem
	{
		private var _Data:TableRowSetting;
		private var _Context:ExpressionContext;
		
		public var Cells:Array = new Array();
		protected override function Disposing():void
		{
			_Data = null;
			_Context = null;
			DisposeUtil.Clear(Cells);
			Cells = null;
			super.Disposing();
		}
		
		public function ReportTableRow(settings:ReportSettings, context:ExpressionContext, data:TableRowSetting)
		{
			super(settings);
			_Context = context;
			_Data = data;
			GroupIndex = context.GroupStartIndex;
			Render();
		}
		
		private function Clear():void
		{
			DisposeUtil.Clear(Cells);
			RemoveAllChildren();
		}
		
		private function Render():void
		{
			Clear();
			width = Settings.ClientWidth;
			height = _Data.Height;
			
			var left:Number = 0;
			var cell:TableCellSetting;
			var item:ReportCell;
			var i:int;
			for(i=0;i<_Data.TableCellSettings.length;i++)
			{
				cell = _Data.TableCellSettings[i];
				item = new ReportTableCell(Settings, _Context, cell);
				item.x = left;
				item.visible = cell.Visible;
				left += Settings.GetColumnWidth(i);
				item.Render(height);
				height = Math.max(height, item.height);
				Cells.push(item);
			}
			
			for each(item in Cells)
			{
				item.AdjustHeight(height);
				addChild(item);
			}

		}
 
	}
}