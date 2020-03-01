/*
Copyright (c) 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


显示对象呈现器


*/
package myreport.report.cellrender
{
	import flash.text.TextFormat;
	
	import myreport.data.report.ControlSetting;
	import myreport.data.report.Define;
	import myreport.render.CurrencyLabel;
	import myreport.report.ReportCell;
 
	internal class CurrencyCellRender extends ReportCellRenderBase
	{
		private var _Display:CurrencyLabel;
		protected override function Disposing():void
		{
			_Display = null;
			super.Dispose();
		}
 
		public function CurrencyCellRender(cell:ReportCell)
		{
			super(cell);
 
		}
		public override function Render():void
		{
			var data:ControlSetting = Cell.Control;
			if(!data)
				return;
			
			_Display = new CurrencyLabel();
			_Display.width = Cell.width;
			_Display.height = Cell.height;
 
			if(!Cell.contains(_Display))
				Cell.addChild(_Display);
 
			var format:TextFormat = CreateTextFormat(Style);

			var digit:int = data.GetStyle(Define.CONTROL_DIGIT);
			var header:Boolean = data.GetStyle(Define.CONTROL_CURRENCY_HEADER);
			_Display.SetStyles(digit, header, format);
			_Display.Value = Value;
			
		}
 
		public override function RefreshHeight():void
		{
			if(_Display)
				_Display.height = Cell.height;
		}
	}
}