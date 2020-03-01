/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


图表——饼图3D

*/

package myreport.chart
{

	import flash.geom.Rectangle;
	
	import hlib.DisposeUtil;
	
	import myreport.data.chart.Pie3DData;
	import myreport.data.chart.SeriesData;

	public final class Pie3D extends ChartBase
	{
		private var _LabelRects:Array = new Array();
		public function Pie3D()
		{
			super();
		}
		public override function Render():void
		{
			super.Render();
			
			hlib.DisposeUtil.Clear(_LabelRects);
			
			var data:Pie3DData = Data as Pie3DData;
			
			var labelVisible:Boolean = data.LabelVisible;
			var labelField:String = data.LabelField;
			
			var pieValueField:String = data.PieValueField;
			var pieLabelVisible:Boolean = data.PieLabelVisible;
			var pieLabelTextColor:uint = data.PieLabelTextColor;
			var pieLabelFontSize:Number = data.PieLabelFontSize;
			var pieLabelFontBold:Boolean = data.PieLabelFontBold;
			var pieLabelField:String = data.PieLabelField;
 
			if(!labelField)
				labelField = pieLabelField;
			
			var pieValues:Array;
			var pieLabelValues:Array;
			var labelValues:Array;
			var series:SeriesData;
			if(IsExpression(pieValueField))
			{
				pieValues = Invoke(pieValueField) as Array;
			}
			else
			{
				series = data.GetSeries(pieValueField);
				if(series)
					pieValues = series.Values;
			}
			if(IsExpression(pieLabelField))
			{
				pieLabelValues = Invoke(pieLabelField) as Array;
			}
			else
			{
				series = data.GetSeries(pieLabelField);
				if(series)
					pieLabelValues = series.Values;
			}
			if(IsExpression(labelField))
			{
				labelValues = Invoke(labelField) as Array;
			}
			else
			{
				series = data.GetSeries(labelField);
				if(series)
					labelValues = series.Values;
			}

			if(!pieValues)
				return;
			
			var labels:Array = new Array();
			var pies:Array = new Array();
			var total:Number = NaN;
			for each(var item:Object in pieValues)
			{
				var num:Number = Number(item);
				if(isNaN(total))
					total = num;
				else
					total += num;
			}

			for(var i:int=0; i<pieValues.length; i++)
			{
				var color:int;
				if(i < data.Colors.length)
					color = data.Colors[i];
				else
					color = ChartUtil.GetColor(i);
				if(labelVisible && labelValues)
				{
					var labelText:String = String(labelValues[i]);
					var label:ChartLabel = new ChartLabel(color, labelText, data.LabelWidth, data.BorderColor,
						data.LabelTextColor, data.LabelFontSize, data.LabelFontBold);
					labels.push(label);
				}
				
				var pie:ChartShape = new ChartShape();
				pie.Value = Number(pieValues[i])/total;
				pie.Color = color;

				if(pieLabelVisible && pieLabelValues)
				{
					pie.Label = ChartUtil.CreateText(String(pieLabelValues[i]), 
						pieLabelFontSize, pieLabelFontBold, pieLabelTextColor);
				}
				pies.push(pie);
			}
			
			RenderLabels(labels, data.LabelColumn, data.LabelGap, data.LabelWidth, data.BorderColor);
			RenderPies(pies);

		}
		private function RenderPies(pies:Array):void
		{
			var data:Pie3DData = Data as Pie3DData;
			var pieRadius:Number = data.PieRadius;
			var pieYScale:Number = data.PieYScale;
			var pieHeight:Number = data.PieHeight;
			var pieBorderThickness:Number = data.PieBorderThickness;
			var pieBorderAlpha:Number = data.PieBorderAlpha;
			var pieFillAlpha:Number = data.PieFillAlpha;
			
			var startAngle:Number = 0;
			var centerX:Number = PaddingLeft + ClientWidth/2;
			var centerY:Number = PaddingTop + ClientHeight/2;
			var pieYRadius:Number = Math.floor(pieRadius*pieYScale);
			var i:int;
			var pie:ChartShape;
			for(i=0; i<pies.length; i++)
			{
				pie = pies[i];
				var sweepAngle:Number = Number(pie.Value) * 360;
				//value = {LabelX:*, LabelY:*}
				var value:Object = PieUtil.DrawPie3D(pie.graphics, centerX, centerY, pieRadius, pieYRadius, 
					startAngle, sweepAngle, pieHeight, pie.Color, 
					pieBorderThickness, pieBorderAlpha, pieFillAlpha);
				startAngle += sweepAngle;
				RenderPieLabel(pie, value.LabelX, value.LabelY);
				Background.addChild(pie);
			}
//			trace("====");
//			for each(var r:* in _LabelRects)
//			{
//				trace(r);
//			}
		}
		private function RenderPieLabel(pie:ChartShape, lx:Number, ly:Number):void
		{
			if(!pie.Label)
				return;
			var data:Pie3DData = Data as Pie3DData;
			var pieLabelDistanceScale:Number = data.PieLabelDistanceScale;
			var centerX:Number = PaddingLeft + ClientWidth/2;
			var centerY:Number = PaddingTop + ClientHeight/2;
			
			var len:Number = 6;//横线长
			
			//计算标签范围
			var rect:Rectangle = new Rectangle();
			rect.x = centerX + lx * pieLabelDistanceScale;
			rect.y = centerY + ly * pieLabelDistanceScale - pie.Label.height/2;
			rect.width = pie.Label.width;
			rect.height = pie.Label.height;
			if(rect.x >= centerX)
			{
				rect.x += len;
			}
			else
			{
				rect.x = rect.x - len - rect.width;
			}
			
			var i:int = _LabelRects.length-1;
			while(i>=0 && i<_LabelRects.length)
			{
				var rect2:Rectangle = _LabelRects[i];
				if(rect.intersects(rect2))
				{
					rect.y -= rect.height;
				}
				else
				{
					i--;
				}
			}
			_LabelRects.push(rect);
			_LabelRects.sortOn("y");
 
			var lx2:Number = centerX + lx * pieLabelDistanceScale;
			var ly2:Number = rect.y + pie.Label.height/2;
			pie.graphics.lineStyle(1, data.BorderColor, 1);
			pie.graphics.moveTo(centerX + lx, centerY + ly);
			pie.graphics.lineTo(lx2, ly2);
			
			if(lx2>=centerX)
			{
				pie.graphics.lineTo(lx2 + len, ly2);
			}
			else
			{
				pie.graphics.lineTo(lx2 - len, ly2);
			}
			
			pie.Label.x = rect.x;
			pie.Label.y = rect.y;

			Foreground.addChild(pie.Label);
		}
	}
}