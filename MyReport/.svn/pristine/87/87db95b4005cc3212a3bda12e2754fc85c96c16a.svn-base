package myreport.design.cellrender
{
	import hlib.TextBase;
	
	import myreport.data.report.ReportSettings;
	import myreport.data.report.StyleSetting;
	
	internal class TextCellRender extends DesignCellRenderBase
	{
		private var _Text:TextBase;
		
		protected override function Disposing():void
		{
			_Text = null;
			super.Disposing();
		}
		public function TextCellRender(settings:ReportSettings, cell:*)
		{
			super(settings, cell);
			
			_Text = hlib.TextBase.CreateText("");
			//_Text.multiline = false;
			_Text.AutoSize = false;
			addChild(_Text);
		}
		protected override function OnResize():void
		{
 			var style:StyleSetting = Style;
			_Text.width = width;
			if(Style.WordWrap)
				_Text.height = TextBase.MeasureHeight2(_Text.text, _Text.defaultTextFormat, width);
			else
 				_Text.height = _Text.SingleLineHeight;
			hlib.LayoutUtil.HorizontalAlign(_Text, width, style.TextAlign);
			hlib.LayoutUtil.VerticalAlign(_Text, height, style.VerticalAlign);
		}
		
		public override function Refresh():void
		{
			_Text.defaultTextFormat = CreateTextFormat(Style);
			_Text.wordWrap = Style.WordWrap;
			if(IsCaptionCell)
			{
				if(CaptionCell.BindingValue)
					_Text.text = CaptionCell.Value + "#"+ CaptionCell.BindingValue;
				else
					_Text.text = CaptionCell.Value;
			}
			else
			{
				if (TableCell.BindingColumn)
				{
					_Text.text = "#" + TableCell.BindingColumn;
				}
				else if(TableCell.BindingValue)
				{
					_Text.text = TableCell.Value + "#"+ TableCell.BindingValue;
				}
				else
					_Text.text = TableCell.Value
			}

			toolTip = _Text.text;
			
			OnResize();
		}
	}
}