/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


报表标题行。


*/
package myreport.report
{
	import hlib.DisposeUtil;
	
	import myreport.data.report.CaptionCellSetting;
	import myreport.data.report.CaptionRowSetting;
	import myreport.data.report.ReportSettings;
	import myreport.expression.ExpressionContext;
	
	internal class ReportCaptionRow extends ReportItem
	{
		private var _Data:CaptionRowSetting;
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
		
		public function ReportCaptionRow(settings:ReportSettings, context:ExpressionContext, data:CaptionRowSetting)
		{
			super(settings);
			_Context = context;
			_Data = data;
			
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
			var cell:CaptionCellSetting;
			var item:ReportCell;
			var i:int;
			for(i=0;i<_Data.CaptionCellSettings.length;i++)
			{
				cell = _Data.CaptionCellSettings[i];
				item = new ReportCaptionCell(Settings, _Context, cell);
				item.x = left;
				left += cell.Width;
				item.Render(height);
				height = Math.max(height, item.height);
				Cells.push(item);
			}
			
			for each(item in Cells)
			{
				item.AdjustHeight(height);
				addChild(item);
			}
			RenderBorder(_Data.LeftBorder, _Data.RightBorder, _Data.TopBorder, _Data.BottomBorder);
		}
		private function RenderBorder(left:Boolean, right:Boolean, top:Boolean, bottom:Boolean):void
		{
			if(!width || !height) 
				return;
			graphics.clear();
			
			graphics.lineStyle(1, 0x000000, 1);
			if(left)
			{
				graphics.moveTo(0, 0);
				graphics.lineTo(0, height);
			}
			if(top)
			{
				graphics.moveTo(0, 0);
				graphics.lineTo(width, 0);
			}
			if(right)
			{
				graphics.moveTo(width, 0);
				graphics.lineTo(width, height);
			}
			if(bottom)
			{
				graphics.moveTo(0, height);
				graphics.lineTo(width, height);
			}
		}
	}
}