/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


加载类，内部封装了基本的错误事件处理。


*/
package hlib
{
	import flash.display.Loader;
	import flash.display.LoaderInfo;
	import flash.events.Event;
	import flash.events.IOErrorEvent;
	import flash.events.SecurityErrorEvent;
	import flash.net.URLLoader;
	import flash.net.URLRequest;
	
	public final class UrlLoader extends EventDispatcherBase
	{
		private var _Url:String;
		private var _Loader:URLLoader;
		public var ShowErrorMessage:Boolean = true;
		/**
		 * 忽略错误，发送错误时同样派发完成事件
		 */ 
		public var IgnoreError:Boolean = false;
		protected override function Disposing():void
		{
			_Loader.removeEventListener(IOErrorEvent.IO_ERROR, OnLoadIOError);
			_Loader.removeEventListener(SecurityErrorEvent.SECURITY_ERROR, OnLoadSecurityError);
			_Loader.removeEventListener(Event.COMPLETE, OnLoadComplete);
			_Loader = null;

			super.Disposing();
		}
		public function UrlLoader(url:String = null)
		{
			super(null);
			_Url = url;
			_Loader = new URLLoader();
			_Loader.addEventListener(IOErrorEvent.IO_ERROR, OnLoadIOError);
			_Loader.addEventListener(SecurityErrorEvent.SECURITY_ERROR, OnLoadSecurityError);
			_Loader.addEventListener(Event.COMPLETE, OnLoadComplete);
		}
		public function get Url():String
		{
			return _Url;
		}
		public function get Data():*
		{
			return _Loader.data;
		}
		/**
		 * @param url: 为null时使用构造函数的url地址
		 */ 
		public function Load(url:String = null):void
		{
			if(url)
				_Url = url;
			_Loader.load(new URLRequest(_Url));
		}
		private function OnLoadIOError(e:IOErrorEvent):void
		{
			if(ShowErrorMessage)
				MsgUtil.ShowInfo(e.text + "\n无法加载" + _Url);
			
			if(IgnoreError)
				dispatchEvent(new Event(Event.COMPLETE));
		}
		
		private function OnLoadSecurityError(e:SecurityErrorEvent):void
		{
			if(ShowErrorMessage)
				MsgUtil.ShowInfo(e.text + "\n无法加载" + _Url);
			
			if(IgnoreError)
				dispatchEvent(new Event(Event.COMPLETE));
		}
		private function OnLoadComplete(e:Event):void
		{
			dispatchEvent(new Event(Event.COMPLETE));
		}
	}
}