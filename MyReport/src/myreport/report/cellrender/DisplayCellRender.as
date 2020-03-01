/*
Copyright (c) 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


显示对象呈现器


*/
package myreport.report.cellrender
{
	import flash.display.DisplayObject;
	
	import myreport.render.IRender;
	import myreport.report.ReportCell;

	internal class DisplayCellRender extends ReportCellRenderBase
	{
		private static const DISPLAY_PADDING:Number = 3;
		private var _Display:DisplayObject;
		protected override function Disposing():void
		{
			_Display = null;
			super.Dispose();
		}
		public function DisplayCellRender(cell:ReportCell, display:DisplayObject)
		{
			super(cell);
			_Display = display;
		}
		public override function Render():void
		{
			var display:DisplayObject = _Display;
			if(!Cell.contains(display))
				Cell.addChild(display);
			display.x = DISPLAY_PADDING;
			display.y = DISPLAY_PADDING;
			display.width = Cell.width - DISPLAY_PADDING * 2;
			display.height = Cell.height - DISPLAY_PADDING * 2;
			if(_Display is IRender)
				IRender(_Display).Render();
		}
 
		public override function RefreshHeight():void
		{
			var display:DisplayObject = _Display;
			display.height = Math.max(0, Cell.height - DISPLAY_PADDING * 2);
			if(_Display is IRender)
				IRender(_Display).Render();
		}
	}
}