/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


图表标签

*/

package myreport.chart
{
	import flash.display.Graphics;
 
	import hlib.SpriteBase;
	import hlib.TextBase;
 
	internal final class ChartLabel extends SpriteBase
	{
		private var ICON_SIZE:int = 10;
		
		private var _BulletColor:uint = 0xffffff;
		private var _BorderColor:uint = 0x696969;
		private var _Text:String="";
		private var _TextColor:uint;
		private var _FontSize:Number = 10;
		private var _FontBold:Boolean = false;
		private var _Width:Number = 120;
 
		private var _Label:TextBase;
 
		protected override function Disposing():void
		{
			_Label = null;
			super.Disposing();
		}
		public function ChartLabel(bulletColor:uint = 0xffffff, text:String = "", width:Number = 120,
								   borderColor:uint = 0x696969, textColor:uint = 0x000000, fontSize:Number = 10,
								   fontBold:Boolean = false)
		{
			super();
			_BulletColor = bulletColor;
			_BorderColor = borderColor;
			_Text = text;
			_TextColor = textColor;
			_FontSize = fontSize;
			_FontBold = fontBold;
			_Width = width;
		}
		public function Render():void
		{
			_Label = ChartUtil.CreateText(_Text, _FontSize, _FontBold, _TextColor , _Width - ICON_SIZE - 4);
			
			var g:Graphics = graphics;
			g.clear();
			g.lineStyle(1, _BorderColor, 1);
			g.beginFill(_BulletColor, 1);
			g.drawRect(2, 2, ICON_SIZE, ICON_SIZE);
			g.endFill();
 
			_Label.x = ICON_SIZE + 4;
			width = _Width;
			height = Math.max(ICON_SIZE+2, _Label.height);
 
			addChild(_Label);
		}
	}
}