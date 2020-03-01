/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


加载计数器，用于管理多个物件的加载


*/
package hlib
{
	import flash.events.Event;
	import flash.events.IEventDispatcher;
	import flash.events.ProgressEvent;
	
	public class LoadCounter extends EventDispatcherBase
	{
		private var _TotalLoaded:int = 0;
		private var _CurrentLoaded:int = 0;
		private var _IsCommitTotal:Boolean = false;
		
		public function LoadCounter()
		{
			super(null);
		}
		
		/** 重置加载 */
		public function Reset():void
		{
			_TotalLoaded = 0;
			_CurrentLoaded = 0;
			_IsCommitTotal = false;
		}
		public function get CurrentLoaded():uint
		{
			return _CurrentLoaded;
		}
		
		public function get TotalLoaded():uint
		{
			return  _TotalLoaded;
		}
		public function get LoadCompleted():Boolean
		{
			return _CurrentLoaded >= _TotalLoaded;
		}
		/** 提交加载总数，初始化完成后调用，必须提交后才触发加载事件 */
		public function CommitTotal():void
		{
			_IsCommitTotal = true;
			ReportEvents();
		}
		/** 新增一个加载 */
		public function WaitOne():void
		{
			_TotalLoaded ++;
		}
		/** 完成一个加载 */
		public function CompleteOne():void
		{
			_CurrentLoaded ++;
			ReportEvents();
		}
		
		private function ReportEvents():void
		{
			if(!_IsCommitTotal)
				return;
			ReportProgress();
			if(LoadCompleted)
				ReportComplete();
		}
		
		private function ReportProgress():void
		{
			var e:ProgressEvent = new ProgressEvent(ProgressEvent.PROGRESS, false, false, _CurrentLoaded, _TotalLoaded);
			dispatchEvent(e);
		}
		
		private function ReportComplete():void
		{
			dispatchEvent(new Event(Event.COMPLETE));
		}
	}
}