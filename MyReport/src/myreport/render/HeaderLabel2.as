/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


表头显示控件：斜线分隔。

*/
package myreport.render
{
	import flash.text.TextFormat;
	
	import hlib.LayoutUtil;
	import hlib.SpriteBase;
	import hlib.TextBase;
	
	public class HeaderLabel2 extends SpriteBase
	{
		private static const PADDING:int = 3;
		private var _Left:TextBase;
		private var _Middle:TextBase;
		private var _Right:TextBase;
		private var _RightScale:Number = 0.6;
		private var _BottomScale:Number = 0.6;
		
		public function HeaderLabel2()
		{
			super();
			
			_Left = TextBase.CreateText("");
			_Middle = TextBase.CreateText("");
			_Right = TextBase.CreateText("");
			addChild(_Left);
			addChild(_Middle);
			addChild(_Right);
			
			mouseChildren = false;
		}
		public function SetStyle(rightScale:Number, bottomScale:Number, format:TextFormat):void
		{
			_RightScale = Math.min(rightScale, 1);
			_BottomScale = Math.min(bottomScale, 1);
			_Left.defaultTextFormat = 
				_Left.Format = 
				_Middle.defaultTextFormat = 
				_Middle.Format = 
				_Right.defaultTextFormat = 
				_Right.Format = format;
			
			RefreshLayout();
			Paint();
		}
		public function SetText(left:*, middle:*, right:*):void
		{
			_Left.text = left;
			_Middle.text = middle;
			_Right.text = right;
			RefreshLayout();
		}
		
		protected override function OnResize():void
		{
			RefreshLayout();
			Paint();
		}
		private function Paint():void
		{
			graphics.clear();
			if(!width || !height)
				return;
			graphics.lineStyle(1);
			graphics.moveTo(0, 0);
			graphics.lineTo(width, height * _RightScale);
			graphics.moveTo(0, 0);
			graphics.lineTo(width * _BottomScale, height);
		}
		private function RefreshLayout():void
		{
			_Right.x = width-PADDING - _Right.width;
			_Right.y = PADDING;
			
			_Middle.x = width * _BottomScale;
			_Middle.y = height-PADDING - _Middle.height;
			
			_Left.x = PADDING;
			_Left.y = height-PADDING-_Left.height;
			
			
		}
		
	}
}