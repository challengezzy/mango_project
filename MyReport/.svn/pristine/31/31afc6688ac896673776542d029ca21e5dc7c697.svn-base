/*
Copyright (c) 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


呈现器基类


*/
package myreport.design.cellrender
{
	import flash.text.TextFormat;
	
	import hlib.ComponentBase;
	
	import myreport.data.report.CaptionCellSetting;
	import myreport.data.report.ControlSetting;
	import myreport.data.report.ReportSettings;
	import myreport.data.report.StyleSetting;
	import myreport.data.report.TableCellSetting;
	
	public class DesignCellRenderBase extends ComponentBase
	{
		public var Settings:ReportSettings;
		public var Cell:*;
 
		protected override function Disposing():void
		{
			Settings = null;
			Cell = null;
			super.Disposing();
		}
 
		public function DesignCellRenderBase(settings:ReportSettings, cell:*)
		{
			Settings = settings;
			Cell = cell;
		}
		
		protected function get IsCaptionCell():Boolean
		{
			return Cell is CaptionCellSetting;
		}
		
		protected function get CaptionCell():CaptionCellSetting
		{
			return Cell;
		}
		
		protected function get TableCell():TableCellSetting
		{
			return Cell;
		}
		
		protected function get Style():StyleSetting
		{
			if(IsCaptionCell)
				return CaptionCell.Style;
			return TableCell.Style;
			
		}
		
		protected function get Control():ControlSetting
		{
			if(IsCaptionCell)
				return CaptionCell.Control;
			return TableCell.Control;
		}
 
		public function Refresh():void
		{
			
		}
		public static function CreateTextFormat(style:StyleSetting):TextFormat
		{
			var tf:TextFormat = new TextFormat();
			tf.align = style.TextAlign;
			tf.bold = style.FontBold;
			tf.size = style.FontSize;
			tf.color = style.TextColor;
			tf.letterSpacing = style.LetterSpacing;
			tf.leading = style.Leading;
			tf.font = style.FontName;
			tf.underline = style.TextUnderline;
			return tf;
		}
	}
}