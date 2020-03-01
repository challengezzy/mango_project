/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


页码


*/

package myreport.report
{
	import flash.text.TextFormat;
	
	import hlib.LayoutUtil;
	import hlib.StringUtil;
	import hlib.TextBase;
	
	import myreport.data.report.ReportSettings;
	
	public final class ReportPageNumber extends ReportItem
	{
		private var _Text:TextBase;
		public function ReportPageNumber(settings:ReportSettings)
		{
			super(settings);
		}
		
		public function Render(num:int, total:int):void
		{
			if(!_Text)
			{
				var format:TextFormat = new TextFormat();
				format.font = "Simsun";
				format.size = 10;				
				_Text = TextBase.CreateText("", format);
				addChild(_Text);
			}
			_Text.text = StringUtil.Format(Settings.PageNumberFormat, num, total);
			LayoutUtil.VerticalAlign(_Text, height, "middle");
			LayoutUtil.HorizontalAlign(_Text, width, "right");
			//RenderBorder(true, true, true, true);
		}
		private function RenderBorder(left:Boolean, right:Boolean, top:Boolean, bottom:Boolean):void
		{
			if(!width || !height) 
				return;
			graphics.clear();
			
			graphics.lineStyle(1, 0x000000, 1);
			if(left)
			{
				graphics.moveTo(0, 0);
				graphics.lineTo(0, height);
			}
			if(top)
			{
				graphics.moveTo(0, 0);
				graphics.lineTo(width, 0);
			}
			if(right)
			{
				graphics.moveTo(width, 0);
				graphics.lineTo(width, height);
			}
			if(bottom)
			{
				graphics.moveTo(0, height);
				graphics.lineTo(width, height);
			}
		}
	}
}