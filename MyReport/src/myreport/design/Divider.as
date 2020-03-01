/*
Copyright (c) 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


分隔控件，支持水平分割，垂直分割

*/
package myreport.design
{
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import hlib.ComponentBase;
	
	import myreport.res.Asset;
 
	internal final class Divider extends ComponentBase
	{
		private var _Selected:Boolean = false;
		private var _Vertical:Boolean = false;

		public function Divider(vertical:Boolean)
		{
			super();
			addEventListener(MouseEvent.ROLL_OUT, OnMouseEvent);
			addEventListener(MouseEvent.ROLL_OVER, OnMouseEvent);
			_Vertical = vertical;
		}
		public function get Vertical():Boolean
		{
			return _Vertical;
		}
		public function get Selected():Boolean
		{
			return _Selected;
		}
		public function set Selected(value:Boolean):void
		{
			_Selected = value;
			if(!value)
				cursorManager.removeAllCursors();
		}
		private function OnMouseEvent(e:MouseEvent):void
		{
			if(e.buttonDown)
				return;
			if(e.type == MouseEvent.ROLL_OVER)
			{
				cursorManager.removeAllCursors();
				if(Vertical)
					cursorManager.setCursor(myreport.res.Asset.ICON_V_DIVIDER, 1, -10, -10);
				else
					cursorManager.setCursor(myreport.res.Asset.ICON_H_DIVIDER, 1, -10, -10);
			}
			else if(e.type == MouseEvent.ROLL_OUT)
			{
				if(!_Selected)
					cursorManager.removeAllCursors();
			}
		}

		protected override function OnResize():void
		{
			graphics.clear();
			graphics.beginFill(0,0);
			//graphics.beginFill(0x009DFF, 0.5);
			graphics.drawRect(0, 0, width, height);
			graphics.endFill();
		}
	}
}