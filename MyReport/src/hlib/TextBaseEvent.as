/*
Copyright (c) 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


文本控件事件。


*/
package hlib
{
	import flash.events.Event;
	
	public class TextBaseEvent extends Event
	{
		public static const BEGIN_EDIT:String = "hlib.BeginEdit";
		public static const END_EDIT:String = "hlib.EndEdit";
		public static const HTML_TEXT_CHANGED:String = "hlib.HtmlTextChanged";

		public function TextBaseEvent(type:String, bubbles:Boolean = false)
		{
			super(type, bubbles);
		}
		
		public override function clone():Event
		{
			var e:TextBaseEvent = new TextBaseEvent(type, bubbles);
			return e;
		}
	}
}