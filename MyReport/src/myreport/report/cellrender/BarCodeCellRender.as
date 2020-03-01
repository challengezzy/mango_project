/*
Copyright (c) 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


显示对象呈现器


*/
package myreport.report.cellrender
{
	import flash.display.DisplayObject;
	
	import myreport.data.report.ControlSetting;
	import myreport.data.report.Define;
	import myreport.render.CurrencyLabel;
	import myreport.render.IRender;
	import myreport.render.RenderUtil;
	import myreport.report.ReportCell;
 
	internal class BarCodeCellRender extends ReportCellRenderBase
	{
		private static const DISPLAY_PADDING:Number = 3;
		private var _Display:DisplayObject;
		protected override function Disposing():void
		{
			_Display = null;
			super.Dispose();
		}
 
		public function BarCodeCellRender(cell:ReportCell)
		{
			super(cell);
 
		}
		public override function Render():void
		{
			var data:ControlSetting = Cell.Control;
			if(!data)
				return;
			
			var type:String = data.GetStyle(Define.CONTROL_BAR_CODE_TYPE);
			switch(type)
			{
				case "Code128B":
					_Display = RenderUtil.CreateBarCode128B(Value) as DisplayObject;
					break;
				case "EAN13":
					_Display = RenderUtil.CreateBarEan13(Value) as DisplayObject;
					break;
				case "EAN8":
					_Display = RenderUtil.CreateBarEan8(Value) as DisplayObject;
					break;
				case "QR_CODE":
					_Display = RenderUtil.CreateQRCode(Value) as DisplayObject;
					break;
			}
  
			if(!Cell.contains(_Display))
				Cell.addChild(_Display);
 
			_Display.x = DISPLAY_PADDING;
			_Display.y = DISPLAY_PADDING;
			_Display.width = Cell.width - DISPLAY_PADDING * 2;
			_Display.height = Cell.height - DISPLAY_PADDING * 2;
			
			if(_Display is IRender)
				IRender(_Display).Render();
 
		}
 
		public override function RefreshHeight():void
		{
			if(!_Display)
				return;
			_Display.height = Math.max(0, Cell.height - DISPLAY_PADDING * 2);
			if(_Display is IRender)
				IRender(_Display).Render();
 
		}
	}
}