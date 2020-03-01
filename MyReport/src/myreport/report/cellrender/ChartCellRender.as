package myreport.report.cellrender
{
 
	import myreport.chart.ChartBase;
	import myreport.chart.ChartUtil;
	import myreport.data.report.ReportSettings;
	import myreport.report.ReportCell;
	
	internal class ChartCellRender extends ReportCellRenderBase
	{
		private var _Chart:ChartBase;
 
		protected override function Disposing():void
		{
			_Chart = null;
			super.Disposing();
		}
		public function ChartCellRender(cell:ReportCell)
		{
			super(cell);
			_Chart = ChartUtil.CreateChart(Cell.Control.Type);
			_Chart.SetExternalData(Cell.Settings.TableData, Cell.Settings.ParameterData);
		}
		public override function Render():void
		{
			if(!Cell.contains(_Chart))
				Cell.addChild(_Chart);
			_Chart.Data = Cell.Control.Chart;
			_Chart.width = Cell.width;
			_Chart.height = Cell.height;
			_Chart.Render();
		}
		
		public override function RefreshHeight():void
		{
			_Chart.height = Cell.height;
			_Chart.Render();
		}
 
	}
}