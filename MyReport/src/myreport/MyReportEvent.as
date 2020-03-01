/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


*/
package myreport
{
	import flash.events.Event;

	public class MyReportEvent extends Event
	{
		/**
		 * 打印事件，在向打印机发送打印数据后派发
		 */ 
		public static const PRINT:String = "MyReportPrint";
 
		public function MyReportEvent(type:String, bubbles:Boolean = false)
		{
			super(type, bubbles);
		}
		
		override public function clone():Event
		{
			var e:MyReportEvent = new MyReportEvent(type, bubbles);
 
			return e;
		}
	}
}