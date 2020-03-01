package myreport.design.cellrender
{
	import myreport.chart.ChartBase;
	import myreport.chart.ChartUtil;
	import myreport.data.report.ReportSettings;
	
	internal class ChartCellRender extends DesignCellRenderBase
	{
		private var _Chart:ChartBase;
		private var _Data:Object;
		protected override function Disposing():void
		{
			_Chart = null;
			super.Disposing();
		}
		public function ChartCellRender(settings:ReportSettings, cell:*)
		{
			super(settings, cell);
			
			_Chart = ChartUtil.CreateChart(Control.Type);
			_Chart.SetExternalData(settings.TableData, settings.ParameterData);
			addChild(_Chart);
		}
		protected override function OnResize():void
		{
			if(_Chart)
			{
				_Chart.width = width;
				_Chart.height = height;
				_Chart.Render();
			}
		}
		
		public override function Refresh():void
		{
			if(_Data == Control.Chart)
				return;
			//trace("Refresh chart...");
			_Data = Control.Chart;			
			_Chart.Data = Control.Chart;
			OnResize(); 
		}
	}
}