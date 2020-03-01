/*
Copyright (c) 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


呈现器基类


*/
package myreport.report.cellrender
{
	import flash.text.TextFormat;
	
	import hlib.IDispose;
	
	import myreport.data.report.StyleSetting;
	import myreport.report.ReportCell;
	
	public class ReportCellRenderBase implements IDispose
	{
		internal var Cell:ReportCell;
		
		private var _Disposed:Boolean = false;	
		protected final function get Disposed():Boolean
		{
			return _Disposed;
		}
		protected function Disposing():void
		{
			
		}
		public final function Dispose():void
		{
			if(_Disposed) return;
			_Disposed = true;
			Disposing();
			Cell = null;
		}
		
		public function ReportCellRenderBase(cell:ReportCell)
		{
			Cell = cell;
		}
		
		protected function get Style():StyleSetting
		{
			return Cell.Style;
		}
		protected function get Value():Object
		{
			return Cell.Value;
		}
		protected function get CellValue():Object
		{
			return Cell.CellValue;
		}
		protected function get CanGrow():Boolean
		{
			return Cell.CanGrow;
		}
		public function Render():void
		{
			
		}
		public function RefreshHeight():void
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