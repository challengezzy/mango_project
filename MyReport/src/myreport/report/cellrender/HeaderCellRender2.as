/*
Copyright (c) 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


显示对象呈现器


*/
package myreport.report.cellrender
{
	import flash.text.TextFormat;
	
	import myreport.data.report.ControlSetting;
	import myreport.data.report.Define;
	import myreport.render.HeaderLabel2;
	import myreport.report.ReportCell;
 
	internal class HeaderCellRender2 extends ReportCellRenderBase
	{
		private var _Display:HeaderLabel2;
		protected override function Disposing():void
		{
			_Display = null;
			super.Dispose();
		}
 
		public function HeaderCellRender2(cell:ReportCell)
		{
			super(cell);
 
		}
		public override function Render():void
		{
			var data:ControlSetting = Cell.Control;
			if(!data)
				return;
			
			_Display = new HeaderLabel2();
			_Display.width = Cell.width;
			_Display.height = Cell.height;
 
			if(!Cell.contains(_Display))
				Cell.addChild(_Display);
			var format:TextFormat = CreateTextFormat(Style);

			var left:String = data.GetStyle(Define.CONTROL_LEFT_LABEL);
			var middle:String = data.GetStyle(Define.CONTROL_MIDDLE_LABEL);
			var right:String = data.GetStyle(Define.CONTROL_RIGHT_LABEL);
			var rightScale:Number = data.GetStyle(Define.CONTROL_RIGHT_SCALE);
			var bottomScale:Number = data.GetStyle(Define.CONTROL_BOTTOM_SCALE);
			_Display.SetStyle(rightScale, bottomScale, format);
			_Display.SetText(left, middle, right);

		}
 
		public override function RefreshHeight():void
		{
			if(_Display)
				_Display.height = Cell.height;
		}
	}
}