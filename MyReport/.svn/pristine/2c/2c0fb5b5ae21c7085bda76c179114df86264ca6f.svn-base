/*
Copyright (c) 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


会计金额标签


*/
package myreport.render
{
	import flash.text.TextFormat;
 
	import hlib.DisposeUtil;
	import hlib.LayoutUtil;
	import hlib.MathUtil;
	import hlib.SpriteBase;
	import hlib.TextBase;

	public class CurrencyLabel extends SpriteBase
	{
		private static var HEADER_STRING:String = "千百十亿千百十万千百十元角分";
 
		private var _Header:Boolean = false;
		private var _Digit:int = 11;
		private var _TextList:Array = new Array();
		private var _Value:*;
		private var _Format:TextFormat;
		private var _TextVisible:Boolean = true;
		
		protected override function Disposing():void
		{
			Clear();
			_TextList = null;
			_Format = null;
			super.Dispose();
		}
		
		public function CurrencyLabel()
		{
			super();
			_Format = new TextFormat();
			_Format.font = "Simsun";
			_Format.size = 12;
			
			mouseChildren = false;
 
		}
		private function Clear():void
		{
			RemoveAllChildren();
			hlib.DisposeUtil.Clear(_TextList);
		}
		
		public function SetStyles(digit:int, header:Boolean, format:TextFormat):void
		{
			digit = Math.min(HEADER_STRING.length, digit);
			digit = Math.max(1, digit);
			if(_Digit != digit || _Header != header)
			{
				_Digit = digit;
				_Header = header;
				CreateTextList();
				Paint();
			}
			Format = format;
		}
 
		public function set Digit(value:int):void
		{
			value = Math.min(HEADER_STRING.length, value);
			value = Math.max(1, value);
			
			if(_Digit == value)
				return;
			_Digit = value;
			CreateTextList();
			RefreshLayout();
			Paint();
		}
		
		public function set Header(value:Boolean):void
		{
			if(_Header == value)
				return;
			_Header = value;
			CreateTextList();
			RefreshLayout();
		}
		
		public function set TextVisible(value:Boolean):void
		{
			if(_TextVisible == value)
				return;
			_TextVisible = value;
			for each(var text:TextBase in _TextList)
			{
				text.visible = _TextVisible;
			}
		}
		
		public function set Format(value:TextFormat):void
		{
			_Format = value;
			for each(var text:TextBase in _TextList)
			{
				text.Format = value;
			}
			RefreshLayout();
		}

		public function set Value(value:*):void
		{
			if(_Value == value)
				return;
			_Value = value;
			if(!_Header)
			{
				CreateTextList();
				RefreshLayout();
			}
		}
		
		protected override function OnResize():void
		{
			RefreshLayout();
			Paint();
		}

		private function CreateTextList():void
		{
			Clear();
			var i:int;
			var text:TextBase;
			var value:String = "";
			if(_Header)
			{
				value = HEADER_STRING;
			}
			else
			{
				if(String(_Value))
				{
					var num:Number = Number(_Value);
					if(!isNaN(num))
					{
						value = MathUtil.Fixed(num * 100);
					}
				}
			}
			
			for(i=0;i<_Digit;i++)
			{
				if(i<value.length)
				{
					text = TextBase.CreateText(value.charAt(value.length-i-1), _Format);
					text.visible = _TextVisible;
					_TextList.push(text);
					addChild(text);
				}
			}
			//trace("CreateTextList:", name);
		}

		private function Paint():void
		{
			graphics.clear();
			if(!width || !height)
				return;
			graphics.lineStyle(1);
			
			var w:Number = width/_Digit;
			var left:Number = width;
			for(var i:int =0; i<_Digit-1; i++)
			{
				left -= w;
				graphics.moveTo(left, 0);
				graphics.lineTo(left, height);
			}
			
			//graphics.drawRect(0,0,width, height);
			
			//trace("Paint:", name);
		}
		private function RefreshLayout():void
		{
			var w:Number = width/_Digit;
			var left:Number = width;
			for(var i:int =0; i<_TextList.length; i++)
			{
				var text:TextBase = _TextList[i];
				left -= w;
				hlib.LayoutUtil.CenterLayout(text, w, height, left);
			}
			
			//trace("RefreshLayout:", name);
		}
		
	}
}