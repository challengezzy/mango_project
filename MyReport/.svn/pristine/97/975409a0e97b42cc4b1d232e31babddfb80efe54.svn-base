/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


*/
package myreport.design
{
	import flash.events.MouseEvent;
 
	import hlib.IDispose;
	import myreport.res.Asset;
	import hlib.ComponentBase;

	internal class SelectionIcon extends ComponentBase implements IDispose
	{
		public var Vertical:Boolean = false;
		private var _Icon:Class;
		public static const SIZE:Number = 6;
		//================IDispose====================
 		protected override function Disposing():void
		{
			_Icon = null;
			super.Disposing();
		}
 
		public function SelectionIcon(vertical:Boolean)
		{
			super();
			width = SIZE;
			height = SIZE;
			Vertical = vertical;
			if (vertical)
				_Icon = Asset.ICON_V_DIVIDER;
			else
				_Icon = Asset.ICON_H_DIVIDER;

			if (_Icon != null)
			{
				addEventListener(MouseEvent.ROLL_OVER, OnMouseEvent);
				addEventListener(MouseEvent.ROLL_OUT, OnMouseEvent);
			}
		}

		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
		{
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			graphics.clear();
			if (_RollOver)
			{
				//graphics.lineStyle(1, 0x009DFF, 1);
				//graphics.beginFill(0x009DFF, 0.5);
				graphics.beginFill(0x009DFF, 0);
			}
			else
			{
				graphics.beginFill(0x009DFF, 0.0);
			}
			graphics.drawRect(0, 0, unscaledWidth, unscaledHeight);
			graphics.endFill();
		}
		private var _RollOver:Boolean = false;
		//===================ISelection===================
		private static var _Selected:Boolean = false;

		public function get Selected():Boolean
		{
			return _Selected;
		}

		public function set Selected(value:Boolean):void
		{
			if (_Selected == value || Disposed)
				return;
			_Selected = value;
			if (_Icon == null)
				return;
			if(!_Selected)
				cursorManager.removeAllCursors();

		}

		private function OnMouseEvent(event:MouseEvent):void
		{
			if (event.currentTarget != this && _Icon == null)
				return;
			if (event.type == MouseEvent.ROLL_OVER)
			{
				_RollOver = true;
				invalidateDisplayList();
				if (!Selected)
					cursorManager.setCursor(_Icon, 1, -10, -10);
			}
			else if (event.type == MouseEvent.ROLL_OUT)
			{
				_RollOver = false;
				invalidateDisplayList();
				if (!Selected)
					cursorManager.removeAllCursors();
			}
		}


	}
}