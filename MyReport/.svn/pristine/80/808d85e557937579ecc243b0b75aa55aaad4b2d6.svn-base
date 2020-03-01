package myreport.util
{
	import flash.display.DisplayObject;
	
	import mx.containers.TitleWindow;
	import mx.core.Application;
	import mx.managers.PopUpManager;
 
	public class PopUpWindow extends mx.containers.TitleWindow
	{
		private var _IsPopUp:Boolean = false;
		public function PopUpWindow()
		{
			super();
			showCloseButton = true;
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
			PopUpManager.centerPopUp(this);
			PopUpManager.bringToFront(this);
		}
		
		public function Close():void
		{
			if(_IsPopUp)
			{
				_IsPopUp = false;
				visible = false;
				PopUpManager.removePopUp(this);		
				OnClose();
			}
		}
		
		protected function OnClose():void
		{
			
		}
	}
}