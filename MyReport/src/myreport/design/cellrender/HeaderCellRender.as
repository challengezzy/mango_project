/*
Copyright (c) 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


*/
package myreport.design.cellrender
{
	import flash.display.DisplayObject;
	import flash.text.TextFormat;
	
	import hlib.TextBase;

	import myreport.data.report.ControlSetting;
	import myreport.data.report.Define;
	import myreport.data.report.ReportSettings;
	import myreport.data.report.StyleSetting;
 
	import myreport.render.HeaderLabel;
	
	internal class HeaderCellRender extends DesignCellRenderBase
	{
 
		private var _Display:HeaderLabel;
 
		protected override function Disposing():void
		{
			_Display = null;
			super.Disposing();
		}
		public function HeaderCellRender(settings:ReportSettings, cell:*)
		{
			super(settings, cell);
 
			_Display = new HeaderLabel();
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
			
			var left:String = control.GetStyle(Define.CONTROL_LEFT_LABEL);
			var right:String = control.GetStyle(Define.CONTROL_RIGHT_LABEL);
			_Display.Format = format;
			_Display.SetText(left, right);

			OnResize();
		}
	}
}