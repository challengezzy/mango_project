/*
Copyright (c) 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


命令事件。


*/
package myreport.util
{
	import flash.events.Event;
	
	public class AsyncProgressEvent extends Event
	{
		public static const PROGRESS:String = "hui.async.Progress";
 
		public var Label:String;
		public var Current:Number = 0;
		public var Total:Number = 0;
 
		public function AsyncProgressEvent(label:String, current:Number, total:Number)
		{
			super(PROGRESS);
			Label = label;
			Current = current;
			Total = total;
		}
		
		public override function clone():Event
		{
			var e:AsyncProgressEvent = new AsyncProgressEvent(Label, Current, Total);
			return e;
		}
	}
}