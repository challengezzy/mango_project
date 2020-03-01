/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


加载管理类，能够管理多个url加载，派发进度和完成事件。


*/

package hlib
{
	import flash.events.Event;
	import flash.events.ProgressEvent;
	
	public final class UrlLoaderManager extends EventDispatcherBase
	{
		private var _Hash:Hash = new Hash();
		private var _Counter:LoadCounter = new LoadCounter();
 
		protected override function Disposing():void
		{
			Clear();
			_Hash = null;
			_Counter.Dispose();
			_Counter = null;
			super.Disposing();
		}
 
		public function UrlLoaderManager()
		{
			super(null);
			_Counter.addEventListener(Event.COMPLETE, OnLoadCompleted);
			_Counter.addEventListener(ProgressEvent.PROGRESS, OnProgress);
		}
		
		public function Clear():void
		{
			_Counter.Reset();
			_Hash.Clear();
		}
		
		public function get CurrentLoaded():uint
		{
			return _Counter.CurrentLoaded;
		}
		
		public function get TotalLoaded():uint
		{
			return _Counter.TotalLoaded;
		}
		
		public function get Keys():Array
		{
			return _Hash.Keys;
		}
		
		public function Has(key:String):Boolean
		{
			return _Hash.ContainsKey(key);
		}
		/**
		 * 通过key访问加载的数据
		 */ 
		public function Load(key:String, url:String):void
		{
			if(_Hash.ContainsKey(key))
				return;
			_Counter.WaitOne();
			
			var loader:UrlLoader = new UrlLoader(url);
			loader.addEventListener(Event.COMPLETE, OnLoaderComplete);
			loader.IgnoreError = true;
			loader.ShowErrorMessage = false;
			_Hash.Set(key, loader);
			loader.Load();
		}
		/**
		 * 提交加载，完成则发送Event.COMPLETE事件
		 */ 
		public function Commit():void
		{
			_Counter.CommitTotal();
		}
		
		private function OnLoaderComplete(e:Event):void
		{
 
			_Counter.CompleteOne();
			
		}
 
		private function OnProgress(e:ProgressEvent):void
		{
			//trace(CurrentLoaded,"/",TotalLoaded);
			var e:ProgressEvent = new ProgressEvent(ProgressEvent.PROGRESS, false, false, CurrentLoaded, TotalLoaded);
			dispatchEvent(e);
		}
		private function OnLoadCompleted(e:Event):void
		{
			//trace("Load completed...");
			dispatchEvent(new Event(Event.COMPLETE));
		}
		public function GetData(key:String):*
		{
			var loader:UrlLoader = _Hash.Get(key) as UrlLoader;
			if(loader)
			{
				return loader.Data;
			}
			return null;
		}

	}
}