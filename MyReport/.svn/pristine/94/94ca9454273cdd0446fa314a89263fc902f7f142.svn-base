/*
Copyright (c), Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：

*/
package myreport.render
{
	import barcode.BarCodeUtil;
	
	import flash.display.BitmapData;
	
	import hlib.SpriteBase;
	/**  @private */
	public class BarCode extends SpriteBase implements IRender
	{
		private var _Type:String;
		private var _Value:String;
		private var _Error:String;
		private var _PaddingLeft:Number = 0;
		private var _PaddingRight:Number = 0;
		private var _PaddingTop:Number = 0;
		private var _PaddingBottom:Number = 0;
		public function BarCode()
		{
			super();
		}
		
		public function set Type(value:String):void
		{
			_Type = value;
		}
		
		public function set Value(value:String):void
		{
			_Value = value;
		}
		
		public function set PaddingLeft(value:Number):void
		{
			_PaddingLeft = value;
		}
		
		public function set PaddingRight(value:Number):void
		{
			_PaddingRight = value;
		}
		
		public function set PaddingTop(value:Number):void
		{
			_PaddingTop = value;
		}
		
		public function set PaddingBottom(value:Number):void
		{
			_PaddingBottom = value;
		}
		
		public function get ErrorMessage():String
		{
			return _Error;
		}
		
		public function Render():void
		{
			graphics.clear();
			if(!width || !height)
				return;
			
			graphics.beginFill(0xffffff, 1);
			graphics.drawRect(1, 1, width-1, height-1);
			graphics.endFill();
			
			if(!_Value)
				return;
			var type:String = _Type;
			try
			{
				var paddingLeft:Number = _PaddingLeft;
				var paddingRight:Number = _PaddingRight;
				var paddingTop:Number = _PaddingTop;
				var paddingBottom:Number = _PaddingBottom;		
				
				var w:Number = width - paddingLeft - paddingRight;
				var h:Number = height - paddingTop - paddingBottom;
		 
				if(type == BarCodeUtil.TYPE_QR_CODE)
				{
					h = w = Math.min(w, h);
				}
				if(!w || !h)
					return;
				
				var bmpd:BitmapData = barcode.BarCodeUtil.Generate(type, _Value, w, h);
				graphics.beginBitmapFill(bmpd);
				graphics.drawRect(paddingLeft, paddingTop, w, h);
				graphics.endFill();
				_Error = "";
			}
			catch(e:Error)
			{
				_Error = e.message;
				trace(_Error);
			}
		}
	}
}