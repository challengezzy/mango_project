package myreport.design.cellrender
{
	import flash.display.DisplayObject;
	import flash.text.TextFormat;
	
	import hlib.TextBase;
	
	import myreport.data.report.ControlSetting;
	import myreport.data.report.Define;
	import myreport.data.report.ReportSettings;
	import myreport.data.report.StyleSetting;

	import myreport.render.CurrencyLabel;
	
	internal class CurrencyCellRender extends DesignCellRenderBase
	{
 
		private var _Display:CurrencyLabel;
 
		protected override function Disposing():void
		{
			_Display = null;
			super.Disposing();
		}
		public function CurrencyCellRender(settings:ReportSettings, cell:*)
		{
			super(settings, cell);
 
			_Display = new CurrencyLabel();
			addChild(_Display);
		}
		protected override function OnResize():void
		{
 			if(_Display)
			{
				_Display.width = width;
				_Display.height = height;
			}
		}
		
		public override function Refresh():void
		{
			var style:StyleSetting = Style;
			var control:ControlSetting = Control;
			
			var format:TextFormat = CreateTextFormat(style);
			
//			var format:TextFormat = new TextFormat();
//			format.align = Style.TextAlign;
//			format.bold = Style.FontBold;
//			format.size = Style.FontSize;
//			format.font = "Simsun";
//			format.color = Style.TextColor;
			
			var digit:int = control.GetStyle(Define.CONTROL_DIGIT);
			var header:Boolean = control.GetStyle(Define.CONTROL_CURRENCY_HEADER);
			_Display.SetStyles(digit, header, format);
			_Display.Value = 123.45;
			OnResize();
		}
	}
}