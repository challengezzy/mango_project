/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


url参数工具类。


*/

package hlib
{
	import flash.external.ExternalInterface;
	import flash.net.URLVariables;
	
	public final class UrlParams implements IDispose
	{
		private var _Params:URLVariables;
		//================IDispose====================
		protected var _Disposed:Boolean = false;
		
		public function Dispose():void
		{
			if (_Disposed)
				return;
			_Disposed = true;
			_Params = null;
		}
		
		public function UrlParams(source:String = null)
		{
			if(!source && ExternalInterface.available)
			{
				source = ExternalInterface.call("eval","window.top.location.search");
			}
			if(source)
			{
				var index:int = source.indexOf("?");
				if(index >= 0)
					source = source.substr(index+1);
			}			
			if(!source)
				source = null;
			_Params = new URLVariables(source);
		}
		public function GetNames():Array
		{
			var list:Array = new Array();
			for(var key:String in _Params)
			{
				list.push(key);
			}
			return list;
		}
		public function HasParam(value:String):Boolean
		{
			for(var key:String in _Params)
			{
				if(key.toLowerCase() == value.toLowerCase())
					return true;
			}
			return false;
		}
		public function GetParam(value:String):String
		{
			for(var key:String in _Params)
			{
				if(key.toLowerCase() == value.toLowerCase())
					return _Params[key];
			}
			return null;
		}
	}
}