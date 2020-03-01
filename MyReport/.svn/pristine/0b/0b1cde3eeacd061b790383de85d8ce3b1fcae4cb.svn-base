/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


表头显示控件：双斜线分隔。

*/
package myreport.render
{
	import flash.text.TextFormat;
	
	import hlib.SpriteBase;
	import hlib.TextBase;
	
	public class HeaderLabel extends SpriteBase
	{
		private static const PADDING:int = 3;
		private var _Left:TextBase;
		private var _Right:TextBase;
		
		public function HeaderLabel()
		{
			super();
			
			_Left = TextBase.CreateText("");
			_Right = TextBase.CreateText("");
			addChild(_Left);
			addChild(_Right);
			
			mouseChildren = false;
		}
		public function set Format(value:TextFormat):void
		{
			_Left.defaultTextFormat = 
				_Left.Format = 
				_Right.defaultTextFormat = 
				_Right.Format = value;
			
			RefreshLayout();
		}
		public function SetText(left:*, right:*):void
		{
			_Left.text = left;
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
			graphics.lineTo(width, height);
		}
		private function RefreshLayout():void
		{
			_Right.y = PADDING;
			hlib.LayoutUtil.HorizontalAlign(_Right, width-PADDING, "right");
			
			_Left.x = PADDING;
			_Left.y = height-PADDING-_Left.height;
		}
	}
}