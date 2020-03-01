/*
Copyright (c) 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


文本呈现器


*/
package myreport.report.cellrender
{
	import flash.text.TextFormat;
	
	import hlib.TextBase;
	import myreport.report.ReportCell;

	internal class TextCellRender extends ReportCellRenderBase
	{
		private static const TEXT_PADDING:Number = 1;
		
		private var _Text:TextBase;
		private var _TextHeight:Number;
		protected override function Disposing():void
		{
			_Text = null;
			super.Dispose();
		}
		
		public function TextCellRender(cell:ReportCell)
		{
			super(cell);
		}
		
		public override function Render():void
		{
			var format:TextFormat = CreateTextFormat(Style);
			_Text = TextBase.CreateText(CellValue, format, Cell.width-2*TEXT_PADDING);
			if(Style.WordWrap)
				_Text.wordWrap = Style.WordWrap;
			
			Cell.addChild(_Text);
			
			_Text.x = TEXT_PADDING;
			
			if(CanGrow && _Text.height > Cell.height - 2 * TEXT_PADDING)
			{
				Cell.height = _Text.height + 2 * TEXT_PADDING;
			}
			
			_TextHeight = _Text.height;
			_Text.AutoSize = false;
			_Text.height += 2;

			if(!CanGrow && _TextHeight > Cell.height - 2 * TEXT_PADDING)
			{
				if(Style.WordWrap)
					_TextHeight = _Text.height-2;
				else
				{
					_Text.height = Cell.height;
					_TextHeight =_Text.GetDisplayTextHeight(Cell.height);
				}
			}
			RefreshTextY();
		}
		private function RefreshTextY():void
		{
			_Text.y = TEXT_PADDING;
			if (Style.VerticalAlign == "top")
			{
			}
			else if (Style.VerticalAlign == "middle")
			{
				_Text.y = (Cell.height - _TextHeight) / 2;
			}
			else if (Style.VerticalAlign == "bottom")
			{
				_Text.y = Cell.height - _TextHeight - TEXT_PADDING;
			}
		}
		public override function RefreshHeight():void
		{
			if(!CanGrow)
			{
				_TextHeight =_Text.GetDisplayTextHeight(Cell.height);
				_Text.height = Math.min(_TextHeight+2, Cell.height);
			}
			RefreshTextY();
			
		}
	}
}