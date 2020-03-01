/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


底层控件基类。


*/
package hlib
{
	import flash.display.Sprite;
	
	public class SpriteBase extends Sprite implements IDispose
	{
		//item=>{Type:String, Func:Function, UseCapture:Boolean}
		private var _Events:Array = new Array();
		private var _Width:Number = 0;
		private var _Height:Number = 0;
		//================IDispose====================
		private var _Disposed:Boolean = false;	
		public final function get Disposed():Boolean
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
			RemoveAllChildren();
		}
		
		public function SpriteBase()
		{
			super();
		}
		
		public function RemoveAllChildren():void
		{
			while (numChildren > 0)
			{
				DisposeUtil.DisposeObject(removeChildAt(0));
			}
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
		
		public override function get width():Number
		{
			return _Width;
		}
		
		public override function set width(value:Number):void
		{
			if(_Width == value) return;
			_Width = value;
			SuperWidthChanged();
			SuperResize();
		}

		protected function SuperWidthChanged():void
		{
			OnWidthChanged();
		}
		protected function OnWidthChanged():void
		{
			
		}
		
		public override function get height():Number
		{
			return _Height;
		}
		
		public override function set height(value:Number):void
		{
			if(_Height == value) return;
			_Height = value;
			SuperHeightChanged();
			SuperResize();
		}

		protected function SuperHeightChanged():void
		{
			OnHeightChanged();
		}
		protected function OnHeightChanged():void
		{
			
		}
		protected function SuperResize():void
		{
			OnResize();
		}
		protected function OnResize():void
		{
			
		}
	}
}