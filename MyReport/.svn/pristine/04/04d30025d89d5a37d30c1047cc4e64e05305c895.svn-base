/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


报表数据——属性设置数据。


*/
package myreport.data.reportdata
{
	import flash.display.Bitmap;
	import flash.display.Loader;
	import flash.events.Event;
	import flash.events.IOErrorEvent;
	import flash.events.ProgressEvent;
	import flash.events.SecurityErrorEvent;
	import flash.net.URLRequest;
	import flash.utils.Dictionary;
	
	import hlib.LoadCounter;
	import hlib.MsgUtil;
	import hlib.Parser;
	import hlib.StringUtil;
	import hlib.UrlLoader;
	
	internal final class SetData extends ItemData
	{
		public var Name:String = "";
		public var Type:String = "String";
		public var Value:*;
		public var IsURL:Boolean = false;
		
		override protected function Disposing():void
		{
			if(Value is TableData)
				TableData(Value).Dispose();
			else
				Value = null;
			super.Disposing();
		}
		
		public function SetData(counter:LoadCounter, xml:XML = null)
		{
			super();
			Counter = counter;
			FromXML(xml);
		}
		private function LoadXML(url:String):void
		{
			if(Counter)
				Counter.WaitOne();
			var loader:UrlLoader = new UrlLoader(url);
			loader.addEventListener(Event.COMPLETE, function(e:Event):void
			{
				Value = new XML(loader.Data);
				if(Counter)
					Counter.CompleteOne();
			});
			loader.Load();
		}
		private function LoadImage(url:String):void
		{
			if(Counter)
				Counter.WaitOne();
			
			var image:Loader = new Loader();
			image.contentLoaderInfo.addEventListener(IOErrorEvent.IO_ERROR, OnImageLoadIOError);
			image.contentLoaderInfo.addEventListener(SecurityErrorEvent.SECURITY_ERROR, OnImageLoadSecurityError);
			image.contentLoaderInfo.addEventListener(Event.COMPLETE, function(e:Event):void
			{
				if(image.content is Bitmap)
					Bitmap(image.content).smoothing = true;
				Value = image;
				if(Counter)
					Counter.CompleteOne();
				
			});
	 
			image.load(new URLRequest(url));
		}
		
		private function OnImageLoadIOError(e:IOErrorEvent):void
		{
			e.stopPropagation();
			MsgUtil.ShowInfo(e.text);
 
		}
		
		private function OnImageLoadSecurityError(e:SecurityErrorEvent):void
		{
			e.stopPropagation();
			MsgUtil.ShowInfo(e.text);
		}
		
		private function CreateImage(base64:String):*
		{
			var image:Loader = new Loader();
			image.loadBytes(hlib.StringUtil.Base64StringToByteArray(base64));
			return image;
		}
		override public function FromXML(xml:XML):void
		{
			if (!xml)
				return;
			Name = xml.@name;
			if(xml.@type.length())
				Type = xml.@type;
			if(xml.@url.length())
				IsURL = ReadBoolean(xml.@url);
			
			var type:String = Type.toLowerCase();
			switch (type)
			{
				case "string":
					Value = String(xml.text());
					break;
				case "byte":
				case "short":		
				case "int":
				case "float":
				case "long":	
				case "double":
				case "decimal":
				case "number":
					Value = Number(String(xml.text()));
					break;
				case "date":
					Value = hlib.Parser.ParseDate(String(xml.text()));
					break;
				case "bool":
					Value = hlib.Parser.ParseBoolean(String(xml.text()));
					break;
				case "image":
					if(IsURL)
						LoadImage(String(xml.text()));
					else
						Value = CreateImage(String(xml.text()));
					break;
				case "xml":
					if(IsURL)
						LoadXML(String(xml.text()));
					else
						Value = XML(xml.elements()[0]);
					break;
				case "table":
					Value = new TableData(Counter, XML(xml.elements()[0]));
					break;
				case "params":
					Value = CreateParams(XML(xml.elements()[0]));
					break;
				default:
					throw new Error("不支持类型" + Type);
					break;
			}
			
		}
		private function CreateParams(xml:XML):*
		{
			var dict:Dictionary = new Dictionary();
			for each(var item:XML in xml.Set)
			{
				var set:SetData = new SetData(Counter, item);
				dict[set.Name] = set;
			}
			return dict;
		}
		override public function Clone():*
		{
			var clone:SetData = new SetData(Counter);
			clone.Name = Name;
			clone.Type = Type;
			clone.Value = Value;
			return clone;
		}
	}
}