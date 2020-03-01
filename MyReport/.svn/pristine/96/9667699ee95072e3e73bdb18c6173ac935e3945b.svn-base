/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


图表——折线图3D

*/

package myreport.chart
{
	import myreport.data.chart.Line3DData;
	import myreport.data.chart.SeriesData;
 
	public class Line3D extends ChartCanvas3D
	{
		public function Line3D()
		{
			super();
		}
		
		public override function Render():void
		{
			super.Render();
			var data:Line3DData = Data as Line3DData;
			
			var xAxisLabelField:String = data.XAxisLabelField;
			
			var lineValueField:String = data.LineValueField;
			var lineLabelVisible:Boolean = data.LineLabelVisible;
			var lineLabelTextColor:uint = data.LineLabelTextColor;
			var lineLabelFontSize:Number = data.LineLabelFontSize;
			var lineLabelFontBold:Boolean = data.LineLabelFontBold;
			var lineLabelField:String = data.LineLabelField;
			
			var lineValues:Array;
			var lineLabelValues:Array;
			var xAxisLabelValues:Array;
			var series:SeriesData;
			if(IsExpression(lineValueField))
			{
				lineValues = Invoke(lineValueField) as Array;
			}
			else
			{
				series = data.GetSeries(lineValueField);
				if(series)
					lineValues = series.Values;
			}
			if(IsExpression(lineLabelField))
			{
				lineLabelValues = Invoke(lineLabelField) as Array;
			}
			else
			{
				series = data.GetSeries(lineLabelField);
				if(series)
					lineLabelValues = series.Values;
			}
			if(IsExpression(xAxisLabelField))
			{
				xAxisLabelValues = Invoke(xAxisLabelField) as Array;
			}
			else
			{
				series = data.GetSeries(xAxisLabelField);
				if(series)
					xAxisLabelValues = series.Values;
			}
		 
			if(!lineValues)
				return;
			
			var points:Array = new Array();
			var color:int;
			if(data.Colors.length)
				color = data.Colors[0];
			else
				color = ChartUtil.GetColor(0);
 
			for(var i:int=0; i<lineValues.length; i++)
			{
				var point:ChartShape = new ChartShape();
				point.Value = Number(lineValues[i]);
				point.Color = color;
				if(xAxisLabelValues)
					point.XAxisLabel = CreateAxisLabel(String(xAxisLabelValues[i]));
 
				if(lineLabelVisible && lineLabelValues)
				{
					point.Label = ChartUtil.CreateText(String(lineLabelValues[i]), 
						lineLabelFontSize, lineLabelFontBold, lineLabelTextColor);

				}
 
				points.push(point);
			}
			
			var columnWidth:Number = 25;
			InitializeParameters(columnWidth);
			RenderCanvas();
			RenderZeroLine();
			RenderFoldLine(points);
			RenderPoints(points);

		}
		private function RenderFoldLine(points:Array):void
		{
			if(points.length<2) 
				return;
			var data:Line3DData = Data as Line3DData;
			var lineThickness:Number = data.LineThickness;
			var pointGap:Number = points.length>1 ? CanvasWidth/(points.length-1):CanvasWidth;
			var values:Array = new Array();
			var color:uint = ChartShape(points[0]).Color;
			for(var i:int=0; i<points.length; i++)
			{
				var point:ChartShape = points[i];
				var value:Number = Number(point.Value);
				var pointX:Number = CanvasX + i * pointGap;
				var pointY:Number = YAxisZero - CanvasHeight*value/(YAxisMax-YAxisMin);
				values.push({x:pointX, y:pointY});
			}
			var foldLine:ChartShape = new ChartShape();
			LineUtil.DrawFoldLine(foldLine.graphics, values, lineThickness, color);
			Background.addChild(foldLine);

		}
		
		private function RenderPoints(points:Array):void
		{
			var data:Line3DData = Data as Line3DData;
			var lineThickness:Number = data.LineThickness;
			
			var pointGap:Number = points.length>1 ? CanvasWidth/(points.length-1):CanvasWidth;
			var xAxisLabelY:Number = CanvasY + CanvasHeight + CanvasBarHeight + YShift + Data.Gap;
			for(var i:int=0; i<points.length; i++)
			{
				var point:ChartShape = points[i];
				var value:Number = Number(point.Value);
				var pointX:Number = CanvasX + i * pointGap;
				var pointY:Number = YAxisZero - CanvasHeight*value/(YAxisMax-YAxisMin);
				LineUtil.DrawPoint(point.graphics, pointX, pointY, lineThickness*2, lineThickness, point.Color);
				
				if(point.XAxisLabel)
				{
					point.XAxisLabel.x = pointX - XShift;
					point.XAxisLabel.y = xAxisLabelY;
					Foreground.addChild(point.XAxisLabel);
				}
				if(point.Label)
				{
					point.Label.x = pointX - lineThickness*2;
					point.Label.y = pointY - Data.Gap - point.Label.height- lineThickness*2;
					Foreground.addChild(point.Label);
				}
				Background.addChild(point);
			}
		}

	}
}