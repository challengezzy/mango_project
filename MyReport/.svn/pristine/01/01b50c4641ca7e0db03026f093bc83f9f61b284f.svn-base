/*
Copyright (c) 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


可弹出布局容器


*/
package myreport.util
{
	import flash.display.DisplayObject;
	
	import mx.containers.Canvas;
	import mx.core.Application;
	import mx.events.CloseEvent;
	import mx.managers.PopUpManager;
 
	public class PopUpPanel extends Canvas
	{
		private var _IsPopUp:Boolean = false;
		public function PopUpPanel()
		{
			super();
		    horizontalScrollPolicy = "off";
			verticalScrollPolicy = "off";
		}

		public function get IsPopUp():Boolean
		{
			return _IsPopUp;
		}
		
		public function PopUp():void
		{
			if(_IsPopUp)
				return;
			_IsPopUp = true;
			var app:DisplayObject = mx.core.Application.application as DisplayObject;
			PopUpManager.addPopUp(this, app, true);
			visible = true;
			x = 0;
			y = 0;
			width = app.width;
			height = app.height;
			PopUpManager.bringToFront(this);
		}
		
		public function Close():void
		{
			if(_IsPopUp)
			{
				_IsPopUp = false;
				visible = false;
				PopUpManager.removePopUp(this);				
			}
			OnClose();
			var event:CloseEvent = new CloseEvent(CloseEvent.CLOSE);
			dispatchEvent(event);
		}
		protected function OnClose():void
		{
			
		}
	}
}