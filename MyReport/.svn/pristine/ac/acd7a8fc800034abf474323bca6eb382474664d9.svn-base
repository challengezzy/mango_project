/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


图像基类


*/
package hlib
{
	import flash.display.Bitmap;
	import flash.display.BitmapData;
	import flash.display.DisplayObject;
	import flash.display.Loader;
	import flash.events.Event;
	import flash.events.IOErrorEvent;
	import flash.events.ProgressEvent;
	import flash.events.SecurityErrorEvent;
	import flash.net.URLRequest;
	import flash.utils.ByteArray;
	
	
	public class ImageBase extends SpriteBase
	{
		private var _Source:*;
		private var _Content:DisplayObject;
		private var _Loader:Loader;
		public var ShowErrorMessage:Boolean = true;
		public var IgnoreError:Boolean = false;
		
		private var _BytesLoaded:Number = NaN;
		private var _BytesTotal:Number = NaN;
 
		override protected function Disposing():void
		{
			Clear();
			
			_Source = null;
			
			super.Disposing();
		}
		
		public function ImageBase()
		{
			super();
		}
		public function get BytesLoaded():Number
		{
			return _BytesLoaded;
		}
		
		public function get BytesTotal():Number
		{
			return _BytesTotal;
		}
		
		private function get Content():DisplayObject
		{
			if(_Loader)
				return _Loader.content;
			return _Content;
		}
		
		public override function get width():Number
		{
			return super.width * scaleX;
		}
		
		public override function get height():Number
		{
			return super.height * scaleX;
		}
		
		public function get UnscaledWidth():Number
		{
			return super.width;
		}
		
		public function get UnscaledHeight():Number
		{
			return super.height;
		}
		
		public function get ContentWidth():Number
		{
			if(Content)
				return Content.width;
			
			return 0;
		}
		public function get ContentHeight():Number
		{
			if(Content)
				return Content.height;
			return 0;
		}
		
		public function Clear():void
		{
			if(_Loader && _Loader.contentLoaderInfo)
			{
				_Loader.contentLoaderInfo.removeEventListener(Event.COMPLETE, OnLoadComplete);
				_Loader.contentLoaderInfo.removeEventListener(IOErrorEvent.IO_ERROR, OnLoadIOError);
				_Loader.contentLoaderInfo.removeEventListener(ProgressEvent.PROGRESS, OnLoadProgress);
				_Loader.contentLoaderInfo.removeEventListener(SecurityErrorEvent.SECURITY_ERROR, OnLoadSecurityError);
			}
			
			RemoveAllChildren();
			
			
			_Content = null;
			_Loader = null;
			_Source = null;
		}
		
		private function ReportComplete():void
		{
//			trace("Image load complete.");
			dispatchEvent(new Event(Event.COMPLETE));
		}
			
 		private function ReportProgress():void
		{
			dispatchEvent(new ProgressEvent(ProgressEvent.PROGRESS, false, false, _BytesLoaded, _BytesTotal));
		}
		
		public function get Source():*
		{
			return _Source;
		}
 
		public function Load(source:*):void
		{
			if(_Source == source)
				return;
			Clear();
			_Source = source;
			
			if(_Source is Class)//EmbedAsset
			{
				_Content = new _Source() as DisplayObject;
				if(_Content)
				{
					addChild(_Content);
					RefreshAndComplete();
				}
			}
			else if(_Source is DisplayObject)
			{
				_Content = DisplayObject(_Source);
				addChild(_Content);
				RefreshAndComplete();
			}
			else if(_Source is BitmapData)
			{
				_Content = new Bitmap(BitmapData(_Source));
				addChild(_Content);
				RefreshAndComplete();
			}
			else if(_Source is ByteArray)
			{
				_Loader = new Loader();
				_Content = _Loader;
				addChild(_Loader);
				_Loader.contentLoaderInfo.addEventListener(Event.COMPLETE, OnLoadComplete);
				_Loader.contentLoaderInfo.addEventListener(IOErrorEvent.IO_ERROR, OnLoadIOError);
				_Loader.contentLoaderInfo.addEventListener(ProgressEvent.PROGRESS, OnLoadProgress);
				_Loader.contentLoaderInfo.addEventListener(SecurityErrorEvent.SECURITY_ERROR, OnLoadSecurityError);
				_Loader.loadBytes(_Source);
			}
			else if(_Source)
			{
				_Loader = new Loader();
				_Content = _Loader;
				addChild(_Loader);
				_Loader.contentLoaderInfo.addEventListener(Event.COMPLETE, OnLoadComplete);
				_Loader.contentLoaderInfo.addEventListener(IOErrorEvent.IO_ERROR, OnLoadIOError);
				_Loader.contentLoaderInfo.addEventListener(ProgressEvent.PROGRESS, OnLoadProgress);
				_Loader.contentLoaderInfo.addEventListener(SecurityErrorEvent.SECURITY_ERROR, OnLoadSecurityError);
  
				var req:URLRequest = new URLRequest(String(_Source));
 
				_Loader.load(req);
			}
			else
			{
				ReportComplete();
			}
			
		}
		protected override function SuperResize():void
		{
			RefreshSize();
			super.SuperResize();
		}
		
		private function RefreshAndComplete():void
		{
			RefreshSmoothing();
			RefreshSize();
			ReportComplete();
		}
			
		private function RefreshSize():void
		{
			if (!_Content)
				return;
 			
			if(_Loader is Loader)
			{
				_Loader.width = width;
				_Loader.height = height;
 
//				if (_Loader.contentLoaderInfo)
//				{
//					_Loader.scaleX = width / _Loader.contentLoaderInfo.width;
//					_Loader.scaleY = height / _Loader.contentLoaderInfo.height;
//				}
 
			}
			else
			{
				_Content.width = width;
				_Content.height = height;
			}
		}
		
		
		private function RefreshSmoothing():void
		{
			if(Content && Content is Bitmap)
				Bitmap(Content).smoothing = true;
		}
		
		private function OnLoadProgress(e:ProgressEvent):void
		{
			e.stopPropagation();
			_BytesLoaded = e.bytesLoaded;
			_BytesTotal = e.bytesTotal;
			ReportProgress();
		}
		
		private function OnLoadIOError(e:IOErrorEvent):void
		{
			e.stopPropagation();
			
			if(ShowErrorMessage)
				MsgUtil.ShowInfo(e.text);
			
			if(IgnoreError)
				dispatchEvent(new Event(Event.COMPLETE));
		}
		
		private function OnLoadSecurityError(e:SecurityErrorEvent):void
		{
			e.stopPropagation();
			
			if(ShowErrorMessage)
				MsgUtil.ShowInfo(e.text);
			
			if(IgnoreError)
				dispatchEvent(new Event(Event.COMPLETE));
		}
		
		private function OnLoadComplete(e:Event):void
		{
			e.stopPropagation();
			RefreshAndComplete();
		}


 
	}
}