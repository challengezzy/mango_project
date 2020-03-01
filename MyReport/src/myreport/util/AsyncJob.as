/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


异步时间片作业


*/
package myreport.util
{
	import hlib.IDispose;
	
	public class AsyncJob implements IDispose
	{
		public var Worker:AsyncWorker;
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
			Worker = null;
		}
		public function AsyncJob()
		{
 
		}
		
 		public final function Execute():void
		{
			if(Finished)
				return;
			OnExecute();
		}
 
		protected function OnExecute():void
		{
			
		}
		
		public function get Finished():Boolean
		{
			return false;
		}
		
		public final function ReportProgress(label:String, current:Number, total:Number):void
		{
			if(Worker)
				Worker.ReportProgress(label, current, total);
		}
		
		public final function Complete():void
		{
			if(Worker)
				Worker.Complete();
		}
 
	}
}