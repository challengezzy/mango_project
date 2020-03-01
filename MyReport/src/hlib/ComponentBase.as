/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


界面基类，实现IDispose接口，能够自动清理事件。


*/

package hlib
{
	import flash.events.Event;
	
	import mx.core.UIComponent;
	import mx.events.ResizeEvent;
	
	
	public class ComponentBase extends UIComponent implements IDispose
	{
		//item=>{Type:String, Func:Function, UseCapture:Boolean}
		private var _Events:Array = new Array();
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
		
		public function ComponentBase()
		{
			super();
			addEventListener(ResizeEvent.RESIZE, OnResizeEvent);
		}
		
		public final function RemoveAllChildren():void
		{
			//trace("ComponentBase.RemoveAllChildren");
			while (numChildren > 0)
			{
				DisposeUtil.DisposeObject(removeChildAt(0));
			}
		}
		
		private function OnResizeEvent(e:ResizeEvent):void
		{
			SuperResize();
		}
		protected function SuperResize():void
		{
			OnResize();
		}
		protected function OnResize():void
		{
			
		}
		public override function addEventListener(type:String, listener:Function, useCapture:Boolean=false, priority:int=0, useWeakReference:Boolean=false):void
		{
			super.addEventListener(type, listener, useCapture, priority, useWeakReference);
			_Events.push({Type:type, Func:listener, UseCapture:useCapture});
		}
		public final function RemoveAllEvents():void
		{
			if(!_Events) return;
			while(_Events.length)
			{
				var item:Object = _Events.pop();
				removeEventListener(item.Type, item.Func, item.UseCapture);
			}
		}
	}
}