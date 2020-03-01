/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


该类是释放对象引用的工具类，用于清理不再使用的对象。


*/

package hlib
{

	import flash.display.Bitmap;
	import flash.display.BitmapData;
	import flash.display.DisplayObject;
	import flash.display.DisplayObjectContainer;
	import flash.display.Loader;
	import flash.utils.Dictionary;
	
	import mx.collections.IList;

	public final class DisposeUtil
	{
		public static function Dispose(value:Object):void
		{
			if (!value) return;
			
			if (value is Array)
			{
				DisposeArray(value as Array);
			}
			else if (value is IList)
			{
				DisposeIList(value as IList);
			}
			else if (value is Dictionary)
			{
				DisposeDictionary(value as Dictionary);
			}
			else if (value.hasOwnProperty("numElements") && value.hasOwnProperty("removeElementAt"))
			{
				RemoveAllElements(value);
			}
			else if (value is DisplayObjectContainer)
			{
				RemoveAllChildren(value as DisplayObjectContainer);
			}
			else if (value is IDispose)
			{
				IDispose(value).Dispose();
			}
			else if (value is Bitmap)
			{
				Bitmap(value).bitmapData.dispose();
			}
			else if (value is BitmapData)
			{
				BitmapData(value).dispose();
			}
			else if (value is Loader)
			{
				DisposeLoader(value as Loader);
			}
		}
		private static function DisposeLoader(value:Loader):void
		{
			if(!value)
				return;
			if(value.content is Bitmap)
				Bitmap(value.content).bitmapData.dispose();
			else
			{
				if(value.hasOwnProperty("unloadAndStop"))
					Object(value).unloadAndStop();
				value.unload();
			}
		}
		public static function DisposeObject(value:Object):void
		{
			if(!value) return;
			if (value is IDispose)
				IDispose(value).Dispose();
			else if (value is Array)
			{
				DisposeArray(value as Array);
			}
			else if (value is IList)
			{
				DisposeIList(value as IList);
			}
			else if (value is Dictionary)
			{
				DisposeDictionary(value as Dictionary);
			}
			else if (value is Bitmap)
			{
				Bitmap(value).bitmapData.dispose();
			}			
			else if (value is BitmapData)
			{
				BitmapData(value).dispose();
			}
			else if (value is Loader)
			{
				DisposeLoader(value as Loader);
			}
		}
		private static function RemoveAllElements(value:Object):void
		{
			if(!value) return;
			while(value.numElements > 0)
			{
				DisposeObject(value.removeElementAt(0));
			}
		}
		
		private static function RemoveAllChildren(value:DisplayObjectContainer):void
		{
			if (!value) return;
			while (value.numChildren > 0)
			{
				DisposeObject(value.removeChildAt(0));
			}
		}

		private static function DisposeArray(value:Array):void
		{
			if (value == null)
				return;
			while (value.length > 0)
			{
				DisposeObject(value.pop());
			}
		}

		private static function DisposeIList(value:IList):void
		{
			if (value == null)
				return;
			while (value.length > 0)
			{
				DisposeObject(value.removeItemAt(0));
			}
		}

		private static function DisposeDictionary(value:Dictionary):void
		{
			if (value == null)
				return;
			for(var key:* in value)
			{
				var val:Object = value[key];
				DisposeObject(val);
				delete value[key];
			}
		}
		
		public static function Clear(value:Object):void
		{
			if(!value)
				return;
			if (value is Array)
			{
				ClearArray(value as Array);
			}
			else if (value is IList)
			{
				ClearIList(value as IList);
			}
			else if (value is Dictionary)
			{
				ClearDictionary(value as Dictionary);
			}
		}
		
		private static function ClearArray(value:Array):void
		{
			if (value == null)
				return;
			while (value.length > 0)
			{
				value.pop();
			}
		}
		private static function ClearIList(value:IList):void
		{
			if (value == null)
				return;
			while (value.length > 0)
			{
				value.removeItemAt(0);
			}
		}
		
		private static function ClearDictionary(value:Dictionary):void
		{
			if (value == null)
				return;
			for(var key:* in value)
			{
				var val:Object = value[key];
				delete value[key];
			}
		}
	}
}