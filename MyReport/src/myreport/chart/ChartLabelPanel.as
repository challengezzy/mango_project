/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


图表标签栏

*/
package myreport.chart
{
	import flash.display.Graphics;
	
	import hlib.DisposeUtil;
	import hlib.SpriteBase;
 
	internal final class ChartLabelPanel extends SpriteBase
	{
		private var _Labels:Array;
		private var _LabelColumn:uint = 1;		
		private var _LabelGap:Number = 2;
		private var _LabelWidth:Number = 120;
		private var _BorderColor:uint = 0x696969;
		protected override function Disposing():void
		{
			DisposeUtil.Dispose(_Labels);
			_Labels = null;
 
			super.Disposing();
		}
		public function ChartLabelPanel(labels:Array, labelColumn:uint = 1, 
										labelGap:Number = 2, labelWidth:Number = 120,
										borderColor:uint = 0x696969)
		{
			super();
 
			_Labels = labels;
			_LabelColumn = Math.max(labelColumn, 1);
			_LabelGap = labelGap;
			_LabelWidth = labelWidth;
			_BorderColor = borderColor
		}
		public function Render():void
		{
 
			var labelColumn:uint = _LabelColumn;
			var labelWidth:Number = _LabelWidth;
			var labelGap:Number = _LabelGap;
			
			var w:Number, h:Number;
			var c:uint = 0;
			var left:Number = labelGap;
			var top:Number = labelGap;
			var rowHeight:Number = 0;
			for each(var label:ChartLabel in _Labels)
			{
				label.Render();
				label.x = left;
				label.y = top;
				left += labelWidth + labelGap;
				rowHeight = Math.max(rowHeight, label.height);
				c++;
				if(c%labelColumn==0)
				{
					top+=rowHeight+labelGap;
					left = labelGap;
					rowHeight = 0;
					c = c%labelColumn;
				}
				addChild(label);
			}
			
			w = (labelWidth + labelGap) * Math.min(_Labels.length, labelColumn);
			h = top + rowHeight + labelGap;
			if(_Labels.length % labelColumn==0)
				h -= labelGap;
			
			width = w;
			height = h;
			var g:Graphics = graphics;
			g.clear();
			
			g.lineStyle(1, _BorderColor, 1);
			g.drawRect(0, 0, width, height);
		}

	}
}