/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


图像显示控件，支持IBitmapDrawable，BitmapData的显示。


*/

package myreport.render
{
	import flash.display.Bitmap;
	import flash.display.BitmapData;
	import flash.display.IBitmapDrawable;
	import flash.geom.Matrix;
	
	import hlib.IDispose;
	import hlib.SpriteBase;

	public final class Image extends SpriteBase implements IRender
	{
		private var _Buffer:BitmapData;
		private var _Content:Object;
		//================IDispose====================
 
		protected override function Disposing():void
		{
			if(_Buffer)
				_Buffer.dispose();
			_Buffer = null;
			_Content = null;
			
			super.Disposing();
		}
 
		public function Image()
		{
			super();
		}		
 
		//================IReportItemRender====================
		public function get Data():Object
		{
			return _Content;
		}
		public function set Data(value:Object):void
		{
			if(_Content == value) return;
			_Content = value;
		}
		public function Render():void
		{
			if(_Buffer)
				_Buffer.dispose();
			_Buffer = null;
			graphics.clear();
			var w:Number = width;
			var h:Number = height;
			if(w<=0 || h<=0) return;
			
			_Buffer = new BitmapData(w, h);
			
			var draw:IBitmapDrawable = _Content as IBitmapDrawable;
			
			if(!draw)
			{
				if(_Content is BitmapData)
				{
					var bmp:Bitmap = new Bitmap(BitmapData(_Content));
					draw = bmp;
				}
			}
			
			if(draw)
			{
				var m:Matrix = new Matrix();
				m.scale(Math.max(0.01,w/_Content.width), Math.max(0.01, h/_Content.height));
				_Buffer.draw(draw, m, null, null, null, true);
			}
			
			graphics.beginBitmapFill(_Buffer);
			graphics.drawRect(0, 0, w, h);
			graphics.endFill();
		}

	}
}