/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


表达式基类

*/
package hlib
{
	public class Expression implements IDispose
	{
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
			if(_Disposed) return;
			_Disposed = true;
			Disposing();
		}
		public function Expression()
		{
		}
		public function Execute(context:*):*
		{
			return null;
		}
 
	}
}