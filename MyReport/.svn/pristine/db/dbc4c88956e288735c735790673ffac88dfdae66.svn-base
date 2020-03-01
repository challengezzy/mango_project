/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


图表图形，用于绘制各种形状，能够存储中间数据Data

*/
package myreport.chart
{
	import flash.display.DisplayObject;
	import flash.events.MouseEvent;
	
	import hlib.SpriteBase;
	
	internal final class ChartShape extends SpriteBase
	{
		protected override function Disposing():void
		{
			Label = null;
			XAxisLabel = null;
			super.Disposing();
		}
		/** 图形标签 */
		public var Label:DisplayObject;
		/** 图形值 */
		public var Value:Object;
		/** 图形颜色 */
		public var Color:uint;
		
		public var XAxisLabel:DisplayObject;
		public function ChartShape()
		{
			super();
			//addEventListener(MouseEvent.CLICK, OnMouseEvent);
		}
		private function OnMouseEvent(e:MouseEvent):void
		{
			//trace(this,Data.Label.text);
		}
	}
}