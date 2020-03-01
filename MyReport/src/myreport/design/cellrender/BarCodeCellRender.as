package myreport.design.cellrender
{
	import flash.display.DisplayObject;
	
	import hlib.TextBase;
	
	import myreport.data.report.Define;
	import myreport.data.report.ReportSettings;
	import myreport.data.report.StyleSetting;
	import myreport.render.RenderUtil;
	import myreport.render.IRender;
	
	internal class BarCodeCellRender extends DesignCellRenderBase
	{
		private static const DISPLAY_PADDING:Number = 3;
		private var _Display:DisplayObject;
		private var _Type:String;
		protected override function Disposing():void
		{
			_Display = null;
			super.Disposing();
		}
		public function BarCodeCellRender(settings:ReportSettings, cell:*)
		{
			super(settings, cell);
		}
		protected override function OnResize():void
		{
 			if(_Display)
			{
				_Display.x = DISPLAY_PADDING;
				_Display.y = DISPLAY_PADDING;
				_Display.width = width - DISPLAY_PADDING * 2;
				_Display.height = height - DISPLAY_PADDING * 2;
				IRender(_Display).Render();
			}
		}
		
		public override function Refresh():void
		{
			var type:String = Control.GetStyle(Define.CONTROL_BAR_CODE_TYPE);
			if(!_Display || _Type != type)
			{
				_Type = type;
				if(_Display && contains(_Display))
					removeChild(_Display);
				switch(type)
				{
					case "Code128B":
						_Display = RenderUtil.CreateBarCode128B("1234567890128") as DisplayObject;
						break;
					case "EAN13":
						_Display = RenderUtil.CreateBarEan13("1234567890128") as DisplayObject;
						break;
					case "EAN8":
						_Display = RenderUtil.CreateBarEan8("12345670") as DisplayObject;
						break;
					case "QR_CODE":
						_Display = RenderUtil.CreateQRCode("QRCode中文") as DisplayObject;
						break;
					
//					case "Code128B":
//						_Display = new BarCode128B();
//						BarCode128B(_Display).Data = "1234567890128";
//						break;
//					case "EAN13":
//						_Display = new BarEan13();
//						BarEan13(_Display).Data = "1234567890128";
//						break;
//					case "EAN8":
//						_Display = new BarEan8();
//						BarEan8(_Display).Data = "12345670";
//						break;
				}
				addChild(_Display);
				OnResize();
				toolTip = "一维码" + _Type;
			}
		}
	}
}