/*
Copyright (c) 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


呈现器工具类


*/
package myreport.report.cellrender
{
	import flash.display.DisplayObject;
	
	import myreport.data.report.ControlSetting;
	import myreport.data.report.Define;
	import myreport.report.ReportCell;
	import myreport.report.ReportItem;

	public final class ReportCellRenderUtil
	{
 
		public static function CreateRender(cell:ReportCell):ReportCellRenderBase
		{
			var render:ReportCellRenderBase;
			var data:ControlSetting = cell.Control;
			if(data)
			{
				if(data.Type == Define.CONTROL_TYPE_CURRENCY)
					render = new CurrencyCellRender(cell);
				else if(data.Type == Define.CONTROL_TYPE_BAR_CODE)
					render = new BarCodeCellRender(cell);
				else if(data.Type == Define.CONTROL_TYPE_HEADER)
					render = new HeaderCellRender(cell);
				else if(data.Type == Define.CONTROL_TYPE_HEADER2)
					render = new HeaderCellRender2(cell);
				else if(data.Chart)
					render = new ChartCellRender(cell);
			}
			if(render)
				return render;
			if(cell.CellValue is ReportItem)
			{
				render = new ReportItemCellRender(cell, cell.CellValue as ReportItem);
			}
			else if(cell.CellValue is DisplayObject)
			{
				render = new DisplayCellRender(cell, cell.CellValue as DisplayObject);
			}
			else
				render = new TextCellRender(cell);
			
			return render;
		}
	}
}