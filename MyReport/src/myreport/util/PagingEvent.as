/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


*/
package myreport.util
{
	import flash.events.Event;

	public class PagingEvent extends Event
	{
		public static const PAGE_CHANGED:String = "MyReportPageChanged";
		
		public function PagingEvent(type:String, bubbles:Boolean = false)
		{
			super(type, bubbles);
		}
		
		override public function clone():Event
		{
			var e:PagingEvent = new PagingEvent(type, bubbles);
			return e;
		}
	}
}