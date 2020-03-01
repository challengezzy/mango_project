/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


异步操作时间片工作类


*/
package myreport.util
{
	import flash.events.Event;
	import flash.events.TimerEvent;
	import flash.utils.Timer;
	
	import hlib.DisposeUtil;
	import hlib.EventDispatcherBase;
	import hlib.MsgUtil;

	public class AsyncWorker extends EventDispatcherBase
	{
		private var _Timer:Timer;
		private var _Jobs:Array = new Array();
		private var _Completed:Boolean = false;
		
		protected override function Disposing():void
		{
			_Timer.removeEventListener(TimerEvent.TIMER, OnTimerEvent);
			_Timer = null;
			hlib.DisposeUtil.Dispose(_Jobs);
			_Jobs = null;
			super.Disposing();
		}
		
		public function AsyncWorker()
		{
			super();
			_Timer = new Timer(1);
			_Timer.addEventListener(TimerEvent.TIMER, OnTimerEvent);
		}
		
		private function OnTimerEvent(e:TimerEvent):void
		{
			try
			{
				if(_Completed)
				{
					_Timer.stop();
					OnWorkCompleted();
					var event:Event = new Event(Event.COMPLETE, true);
					dispatchEvent(event);
					return;
				}
				var job:AsyncJob;
				for each(job in _Jobs)
				{
					job.Execute();
				}
				
				var index:int = 0;
				while(index<_Jobs.length)
				{
					job = _Jobs[index];
					if(job.Finished)
					{
						RemoveJob(job);
					}
					else
						index++;
				}
			}
			catch(error:Error)
			{
				if(_Timer)
					_Timer.stop();
				hlib.MsgUtil.ShowInfo(error.message);
				trace(error.getStackTrace());
			}
		}
		
		public final function WorkAsync():void
		{
			_Completed = false;
			try
			{
				OnWorkBegin();
			}
			catch(error:Error)
			{
				if(_Timer)
					_Timer.stop();
				hlib.MsgUtil.ShowInfo(error.getStackTrace());
				return;
			}				
			
			_Timer.start();
		}
		
		public final function Complete():void
		{
			_Completed = true;
		}
		
		protected function OnWorkBegin():void
		{
			
		}
		
		protected function OnWorkCompleted():void
		{
			
		}
		
		public function ReportProgress(label:String, current:Number, total:Number):void
		{
			var e:AsyncProgressEvent = new AsyncProgressEvent(label, current, total);
			dispatchEvent(e);	
		}
		
		public final function AddJob(job:AsyncJob):Boolean
		{
			var index:int = _Jobs.indexOf(job);
			if(index < 0)
			{
				_Jobs.push(job);
				job.Worker = this;
				return true;
			}
			return false;
		}
		
		public final function RemoveJob(job:AsyncJob):Boolean
		{
			var index:int = _Jobs.indexOf(job);
			if(index >= 0)
			{
				var job:AsyncJob = _Jobs.splice(index,1)[0];
				job.Dispose();
				return true;
			}
			return false;
		}
	}
}