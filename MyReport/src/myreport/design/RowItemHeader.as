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
	
	internal class RowItemHeader extends ComponentBase implements IDispose
	{
		public function RowItemHeader()
		{
			super();
			addEventListener(MouseEvent.ROLL_OVER, OnMouseEvent);
			addEventListener(MouseEvent.ROLL_OUT, OnMouseEvent);
			
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
		{
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			graphics.clear();
			
			if (Selected)
				graphics.beginFill(0xF9FF9D, 0.5);
			else if (_RollOver)
				graphics.beginFill(0xdddddd, 0.5);
			else
				graphics.beginFill(0, 0);
			
			graphics.drawRect(0, 0, unscaledWidth, unscaledHeight);
			graphics.endFill();
		}
		private var _RollOver:Boolean = false;
		//===================ISelection===================
		private var _Selected:Boolean = false;
		
		public function get Selected():Boolean
		{
			return _Selected;
		}
		
		public function set Selected(value:Boolean):void
		{
			if (_Selected == value || Disposed)
				return;
			_Selected = value;
			invalidateDisplayList();
		}
		
		private function OnMouseEvent(event:MouseEvent):void
		{
			if (event.currentTarget != this)
				return;
			_RollOver = event.type == MouseEvent.ROLL_OVER;
 
			invalidateDisplayList();
		}
		
		
	}
}