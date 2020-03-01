/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


报表项渲染器工具类。

所有渲染器都应该通过该类创建

*/
package myreport.render
{
	import barcode.BarCodeUtil;

	public final class RenderUtil
	{
		public static function CreateImage(data:Object):IRender
		{
			var render:Image = new Image();
			render.Data = data;
			return render;
		}
		
		public static function CreateBarCode128B(data:Object):IRender
		{
			var render:BarCode = new BarCode();
			render.Type = barcode.BarCodeUtil.TYPE_CODE_128;
			render.PaddingTop = 0;
			render.PaddingBottom = 0;
			render.PaddingLeft = 0;
			render.PaddingRight = 0;
			render.Value = String(data);
			
			return render;
		}
		public static function CreateBarEan13(data:Object):IRender
		{
			var render:BarCode = new BarCode();
			render.Type = barcode.BarCodeUtil.TYPE_EAN_13;
			render.PaddingTop = 0;
			render.PaddingBottom = 0;
			render.PaddingLeft = 0;
			render.PaddingRight = 0;
			render.Value = String(data);
			
			return render;
		}
		public static function CreateBarEan8(data:Object):IRender
		{
			var render:BarCode = new BarCode();
			render.Type = barcode.BarCodeUtil.TYPE_EAN_8;
			render.PaddingTop = 0;
			render.PaddingBottom = 0;
			render.PaddingLeft = 0;
			render.PaddingRight = 0;
			render.Value = String(data);
			
			return render;
		}
		public static function CreateQRCode(data:Object):IRender
		{
			var render:BarCode = new BarCode();
			render.Type = barcode.BarCodeUtil.TYPE_QR_CODE;
			render.PaddingTop = 0;
			render.PaddingBottom = 0;
			render.PaddingLeft = 0;
			render.PaddingRight = 0;
			render.Value = String(data);
			
			return render;
		}
//		public static function CreateBarCode128B(data:Object):IRender
//		{
//			var render:BarCode128B = new BarCode128B();
//			render.Data = data;
//			return render;
//		}
//		public static function CreateBarEan13(data:Object):IRender
//		{
//			var render:BarEan13 = new BarEan13();
//			render.Data = data;
//			return render;
//		}
//		public static function CreateBarEan8(data:Object):IRender
//		{
//			var render:BarEan8 = new BarEan8();
//			render.Data = data;
//			return render;
//		}
	}
}