package myreport.design.cellrender
{
	import myreport.data.report.CaptionCellSetting;
	import myreport.data.report.ControlSetting;
	import myreport.data.report.Define;
	import myreport.data.report.ReportSettings;
	import myreport.data.report.TableCellSetting;

	public final class DesignCellRenderUtil
	{
		public static function CreateRender(type:String, settings:ReportSettings, cell:*):DesignCellRenderBase
		{
			var render:DesignCellRenderBase;
			var control:ControlSetting;
			if(cell is CaptionCellSetting)
				control = CaptionCellSetting(cell).Control;
			else if(cell is TableCellSetting)
				control = TableCellSetting(cell).Control;
			
			if(type == Define.CONTROL_TYPE_BAR_CODE)
				render = new BarCodeCellRender(settings, cell);
			else if(type == Define.CONTROL_TYPE_CURRENCY)
				render = new CurrencyCellRender(settings, cell);
			else if(type == Define.CONTROL_TYPE_HEADER)
				render = new HeaderCellRender(settings, cell);
			else if(type == Define.CONTROL_TYPE_HEADER2)
				render = new HeaderCellRender2(settings, cell);
			else if(control && control.Chart)
				render = new ChartCellRender(settings, cell);
			else
				render = new TextCellRender(settings, cell);
			return render;
		}
	}
}