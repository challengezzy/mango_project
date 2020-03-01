/*
Copyright (c) 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


数据基类。


*/
package hlib
{
	public class ItemData implements IDispose, IClone, IXmlSerializable
	{
		public function ItemData()
		{
		}

		public function FromXML(xml:XML):void
		{
		}

		public function ToXML():String
		{
			return "";
		}

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
			if (_Disposed)
				return;
			_Disposed = true;
			Disposing();
		}
		//================IClone====================
		public function Clone():*
		{
			var clone:ItemData = new ItemData();
			return clone;
		}
		//=================static======================
 
		public static function ReadBoolean(value:String):Boolean
		{
			return Parser.ParseBoolean(value);
		}
		public static function ReadColor(value:String):uint
		{
			return Parser.ParseColor(value);
		}
		public static function ToHexColor(color:uint):String
		{
			return Parser.ToHexColor(color);
		}
		public static function EscapeXML(value:String):String
		{
			return StringUtil.EscapeXML(value);
		}
		public static function EscapeCSV(value:String):String
		{
			return StringUtil.EscapeCSV(value);
		}
 		public function toString():String
		{
			return ToXML();
		}
 
	}
}