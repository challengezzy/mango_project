/*
Copyright (c) 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


文本控件基类。


*/
package hlib
{
	import flash.text.TextField;
	import flash.text.TextFieldAutoSize;
	import flash.text.TextFieldType;
	import flash.text.TextFormat;
	import flash.text.TextLineMetrics;
	
	import hlib.IDispose;
 
	public class TextBase extends TextField implements IDispose
	{
		private static var _MeasureHeightTextField:TextBase;
		
		public static function MeasureHeight2(text:Object, format:TextFormat, width:Number):Number
		{
			if(!_MeasureHeightTextField)
			{
				_MeasureHeightTextField = new TextBase();
				_MeasureHeightTextField.autoSize = TextFieldAutoSize.LEFT;
				_MeasureHeightTextField.wordWrap = true;
				_MeasureHeightTextField.multiline = true;
			}
			_MeasureHeightTextField.width = width;
			_MeasureHeightTextField.defaultTextFormat = format;
			_MeasureHeightTextField.text = String(text);
//			var h:Number = _MeasureHeightTextField.height;
			var h:Number = _MeasureHeightTextField.GetMultiTextHeight(true);
			return h;
		}
 
		private static var _MeasureTextField:TextField;
		/**
		 * 计算文本宽度
		 */ 
		public static function MeasureWidth(text:Object, format:TextFormat):Number
		{
			var size:Object = MeasureSize(text, format);
			return size.w;
		}
		/**
		 * 计算文本高度
		 */ 
		public static function MeasureHeight(text:Object, format:TextFormat):Number
		{
			var size:Object = MeasureSize(text, format);
			return size.h;
		}
		/**
		 * 计算文本大小
		 * {w:Number, h:Number}
		 */ 
		private static function MeasureSize(text:Object, format:TextFormat):Object
		{
			if(!_MeasureTextField)
			{
				_MeasureTextField = new TextField();
				_MeasureTextField.autoSize = TextFieldAutoSize.LEFT;
			}
			_MeasureTextField.defaultTextFormat = format;
			_MeasureTextField.text = String(text);
			var w:Number = _MeasureTextField.width;
			var h:Number = _MeasureTextField.height;
			return {w:w, h:h};
		}
		public static function CreateText(text:Object, format:TextFormat=null, width:Number = NaN):TextBase
		{
			var txt:TextBase = new TextBase();
			txt.wordWrap = false;
			if(format)
				txt.defaultTextFormat = format;
			txt.text = String(text);
			if(!isNaN(width))
			{
				txt.width = width;
				txt.wordWrap = true;
			}
			return txt;
		}
		
		//item=>{Type:String, Func:Function, UseCapture:Boolean}
		private var _Events:Array = new Array();
		//================IDispose====================
		private var _Disposed:Boolean = false;	
		protected final function get Disposed():Boolean
		{
			return _Disposed;
		}
		protected function Disposing():void
		{
			
		}
		public final function Dispose():void
		{
			if(_Disposed) return;
			_Disposed = true;
			Disposing();
			RemoveAllEvents();
			_Events = null;
		}
		
		public function TextBase()
		{
			super();
			autoSize = TextFieldAutoSize.LEFT;
			mouseWheelEnabled = false;
			multiline = true;
			selectable = false;
		}

		public override function addEventListener(type:String, listener:Function, useCapture:Boolean=false, priority:int=0, useWeakReference:Boolean=false):void
		{
			super.addEventListener(type, listener, useCapture, priority, useWeakReference);
			_Events.push({Type:type, Func:listener, UseCapture:useCapture});
		}
		public function RemoveAllEvents():void
		{
			if(!_Events) return;
			while(_Events.length)
			{
				var item:Object = _Events.pop();
				removeEventListener(item.Type, item.Func, item.UseCapture);
			}
		}
		
		public function get Editing():Boolean
		{
			return type == TextFieldType.INPUT;
		}
		
		public function set Editing(value:Boolean):void
		{
			if (Editing == value)
				return;
			SetEditable(value);
		}
		private function SetEditable(value:Boolean):void
		{
			//trace("[TextBase::SetEditable]",value);
			var e:TextBaseEvent;
			selectable = value;
			type = value ? TextFieldType.INPUT : TextFieldType.DYNAMIC;
			if (value)
			{
				e = new TextBaseEvent(TextBaseEvent.BEGIN_EDIT, false);
				dispatchEvent(e);
				setSelection(0, text.length);
				scrollV = 1;
			}
			else
			{
				setSelection(0, 0);
				e = new TextBaseEvent(TextBaseEvent.END_EDIT, false);
				dispatchEvent(e);
			}
		}
		public function get SingleLineHeight():Number
		{
			if(numLines)
				return getLineMetrics(0).height + 4;
			else
				return MeasureHeight("Wj", defaultTextFormat);
		}
//		public function get TextHeight():Number
//		{
//			var h:Number = height;
//			//trace("[TextBase::TextHeight]",h);
//			if(h > 4)
//				return h;
//			
//			return MeasureHeight("Wj", defaultTextFormat);
//		}
		
		/**
		 * 获取文本显示高度
		 * @param height: 显示高度
		 */ 
		public function GetDisplayTextHeight(height:Number):Number
		{
			var h:Number = 4;
			for(var i:int = 0; i<numLines; i++)
			{
				var line:TextLineMetrics = getLineMetrics(i);
				if(h+line.height>=height)
					break;
				h+=line.height;
			}
			if(h > 4)
				return Math.ceil(h);
			return h;
		}
		
		public function GetMultiTextHeight(ignoreLastLine:Boolean = true):Number
		{
			var numLines:Number = numLines;
			if(numLines <= 1) 
				ignoreLastLine = false;
			var h:Number = 4;
			for(var i:int = numLines-1;i>=0;i--)
			{
				var line:TextLineMetrics = getLineMetrics(i);
				if(ignoreLastLine && i!=0)
				{
					var txt:String = getLineText(i);
					if(!txt || txt=="\r" || txt=="\n")
						continue;
				}
				
				h+=line.height;
			}
			if(h > 4)
				return Math.ceil(h);
			return MeasureHeight("Wj", defaultTextFormat);
		}
 
		public function get Format():TextFormat
		{
			var textFormat:TextFormat = null;
			if (Editing && selectionEndIndex <= text.length && selectionEndIndex > selectionBeginIndex)
			{
				textFormat = getTextFormat(selectionBeginIndex, selectionEndIndex);
			}
			else
				textFormat = getTextFormat();
			return textFormat;
		}
		
		public function get AutoSize():Boolean
		{
			return autoSize != TextFieldAutoSize.NONE;
		}
		public function set AutoSize(value:Boolean):void
		{
			if(value)
				autoSize = TextFieldAutoSize.LEFT;
			else
				autoSize = TextFieldAutoSize.NONE;
		}
		public function set Format(value:TextFormat):void
		{
			if (Editing && selectionEndIndex <= text.length && selectionEndIndex > selectionBeginIndex)
			{
				setTextFormat(value, selectionBeginIndex, selectionEndIndex);
			}
			else
			{
				setTextFormat(value);
			}
		}
		
		public function set Leading(value:Object):void
		{
			var format:TextFormat = Format;
			format.leading = value;
			defaultTextFormat = format;
			Format = format;
		}
		
		public function set Bold(value:Object):void
		{
			var format:TextFormat = Format;
			format.bold = value;
			defaultTextFormat = format;
			Format = format;
		}
		
		public function set FontSize(value:Object):void
		{
			var format:TextFormat = Format;
			format.size = value;
			defaultTextFormat = format;
			Format = format;
		}
		public function set FontFamily(value:String):void
		{
			var format:TextFormat = Format;
			format.font = value;
			defaultTextFormat = format;
			Format = format;
		}

		public function set TextAlign(value:String):void
		{
			var format:TextFormat = Format;
			format.align = value;
			defaultTextFormat = format;
			Format = format;
		}
		
		public function set TextColor(value:uint):void
		{
			var format:TextFormat = Format;
			format.color = value;
			defaultTextFormat = format;
			Format = format;
		}
		
		public override function set htmlText(value:String):void
		{
			super.htmlText = value;
			var e:TextBaseEvent;
			e = new TextBaseEvent(TextBaseEvent.HTML_TEXT_CHANGED, false);
			dispatchEvent(e);
		}
	}
}