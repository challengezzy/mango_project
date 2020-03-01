/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


表格单元格。


*/
package myreport.report
{
 
	import myreport.data.report.CaptionCellSetting;
	import myreport.data.report.ReportSettings;
	import myreport.data.report.StyleSetting;
	import myreport.expression.ExpressionContext;
 
	public final class ReportCaptionCell extends ReportCell
	{
 
		private var _Cell:CaptionCellSetting;
 
		protected override function Disposing():void
		{
			_Cell = null;
 
			super.Disposing();
		}
		public function ReportCaptionCell(settings:ReportSettings, context:ExpressionContext, cell:CaptionCellSetting)
		{
			super(settings,context, cell.CanGrow, cell);
 			_Cell = cell;
		}
 
		protected override function OnExecute():void
		{
			if (_Cell.Value.indexOf("=") == 0)
			{
				ExecuteExpression(_Cell.Value, _Cell.Format);
			}
			else
			{
				ExecuteParameter(_Cell.Value, _Cell.BindingValue, _Cell.Format);
			}
		}
		protected override function OnExcuteStyle():StyleSetting
		{
			return _Cell.Style;
		}
 
		protected override function OnRefreshWidth():void
		{
			width = _Cell.Width;
		}

	}
}