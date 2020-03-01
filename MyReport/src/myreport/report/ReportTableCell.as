/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


表格单元格。


*/
package myreport.report
{
 
	import myreport.data.report.ConditionStyleSetting;
	import myreport.data.report.ReportSettings;
	import myreport.data.report.StyleSetting;
	import myreport.data.report.TableCellSetting;
	import myreport.data.report.TableColumnSetting;
	import myreport.data.report.TableRowSetting;
	import myreport.data.reportdata.DummyData;
	import myreport.expression.ExpressionContext;
 
	public final class ReportTableCell extends ReportCell
	{
		private var _Cell:TableCellSetting;
	 
		protected override function Disposing():void
		{
			_Cell = null;
 
			super.Disposing();
		}
		public function ReportTableCell(settings:ReportSettings, context:ExpressionContext, 
										cell:TableCellSetting)
		{
			super(settings,context, cell.CanGrow, cell);
 			_Cell = cell;
 
		}
 
		protected override function OnExecute():void
		{
			if(!_Cell.ShowNullValue && Settings.TableData && _Context.RowIndex<Settings.TableData.length)
			{
				var obj:Object = Settings.TableData[_Context.RowIndex];
				if(obj is DummyData)
				{
					CellValue = Value = "";
					return;
				}
			}
			_Context.ColumnIndex = _Cell.ColumnIndex;
			_Context.Settings = Settings;
			if (_Cell.BindingColumn)
			{
				ExecuteExpression("=Fields!" + _Cell.BindingColumn + ".Value", _Cell.Format);
			}
			else if (IsExpression(_Cell.Value))
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
			for each(var condition:ConditionStyleSetting in _Cell.ConditionStyleSettings)
			{
				if(IsExpression(condition.Condition))
				{
					var result:Object = InvokeValue(condition.Condition, _Context);
					if(result)
						return condition.Style;
				}
			}
			return _Cell.Style;
		}
 
		protected override function OnRefreshWidth():void
		{
			width = Settings.GetColSpanWidth(_Cell.ColumnIndex, _Cell.ColSpan);
		}

	}
}