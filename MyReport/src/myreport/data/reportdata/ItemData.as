/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


报表数据——数据基类。


*/
package myreport.data.reportdata
{
	import hlib.IClone;
	import hlib.IDispose;
	import hlib.LoadCounter;
	import hlib.Parser;

	internal class ItemData implements IDispose, IClone
	{
		public var Counter:LoadCounter;
		
		public function ItemData()
		{

		}
 
		public function FromXML(xml:XML):void
		{
		}

		//================IDispose====================
		private var _Disposed:Boolean = false;
		protected function get Disposed():Boolean
		{
			return _Disposed;
		}
		protected function Disposing():void
		{
		}
		//================IClone====================
		public function Clone():*
		{
			var clone:ItemData = new ItemData();
			return clone;
		}
		public function Dispose():void
		{
			if (_Disposed)
				return;
			_Disposed = true;
			Disposing();
		}
 
		internal static function ReadBoolean(value:String):Boolean
		{
			return Parser.ParseBoolean(value);
		}
 
	}
}