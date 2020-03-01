package myreport.util
{
	import flash.display.DisplayObject;
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.filters.DropShadowFilter;
 
	import mx.containers.VBox;
	import mx.core.Application;
	import mx.events.SandboxMouseEvent;
	import mx.managers.ISystemManager;
	import mx.managers.PopUpManager;
 
	public class PopUpMenu extends VBox
	{
		private var _IsPopUp:Boolean = false;
		private var _Shadow:DropShadowFilter;
		public function PopUpMenu()
		{
			super();
			_Shadow = new DropShadowFilter(4, 45, 0, 0.5, 4, 4);
			filters = [_Shadow];
			setStyle("backgroundColor", 0xffffff);
		}
		
		protected override function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
		{
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			graphics.clear();
			graphics.lineStyle(1,0x696969);
			graphics.beginFill(0xffffff);
			graphics.drawRect(0,0,unscaledWidth, unscaledHeight);
			graphics.endFill();
		}
 
		public function PopUp(stageX:Number, stageY:Number):void
		{
			if(_IsPopUp)
				return;
			_IsPopUp = true;
			var app:DisplayObject = mx.core.Application.application as DisplayObject;
			PopUpManager.addPopUp(this, app, false);
			visible = true;
			PopUpManager.bringToFront(this);
			move(stageX, stageY);
			
			
			var sm:ISystemManager = systemManager.topLevelSystemManager;
			var sbRoot:DisplayObject = sm.getSandboxRoot();
			sbRoot.removeEventListener(MouseEvent.MOUSE_DOWN, mouseDownOutsideHandler);
			removeEventListener(SandboxMouseEvent.MOUSE_DOWN_SOMEWHERE, mouseDownOutsideHandler);
			sbRoot.addEventListener(MouseEvent.MOUSE_DOWN, mouseDownOutsideHandler, false, 0, true);
			addEventListener(SandboxMouseEvent.MOUSE_DOWN_SOMEWHERE, mouseDownOutsideHandler, false, 0, true);
			
			setFocus();
		}
		
		private function mouseDownOutsideHandler(event:Event):void
		{
			var sm:ISystemManager = systemManager.topLevelSystemManager;
			var sbRoot:DisplayObject = sm.getSandboxRoot();
			sbRoot.removeEventListener(MouseEvent.MOUSE_DOWN, mouseDownOutsideHandler);
			removeEventListener(SandboxMouseEvent.MOUSE_DOWN_SOMEWHERE, mouseDownOutsideHandler);
			
			if(!OnHandleMouseDown(event))
				Hide();
 
		}
		
		protected function OnHandleMouseDown(e:Event):Boolean
		{
			return false;
		}
		
		public function Hide():void
		{
			if(_IsPopUp)
			{
				_IsPopUp = false;
				visible = false;
				PopUpManager.removePopUp(this);		
 
			}
		}
 
	}
}