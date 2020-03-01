package myreport.util
{
	import flash.events.Event;
	
 
	public final class AsyncUtil
	{
		/**
		 * @param callback: function():void
		 */ 
		public static function RunJob(job:AsyncJob, callback:Function = null):void
		{
			var worker:AsyncWorker = new AsyncWorker();
			worker.addEventListener(Event.COMPLETE, function(e:Event):void
			{
				ProgressForm.Instance.Close();
				if(callback!=null)
					callback();
				worker.Dispose();
				
			});
			
			worker.addEventListener(AsyncProgressEvent.PROGRESS, function(e:AsyncProgressEvent):void
			{
				ProgressForm.Instance.Show(e.Label, e.Current, e.Total);
			});
			
			worker.AddJob(job);
			worker.WorkAsync();
		}
	}
}