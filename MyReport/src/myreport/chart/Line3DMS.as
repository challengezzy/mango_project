/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


图表——折线图3D

*/

package myreport.chart
{
	import myreport.data.chart.Line3DMSData;
	import myreport.data.chart.SeriesData;
 
	public class Line3DMS extends ChartCanvas3D
	{
		public function Line3DMS()
		{
			super();
		}
		
		public override function Render():void
		{
			super.Render();
			var data:Line3DMSData = Data as Line3DMSData;
			
			var labelVisible:Boolean = data.LabelVisible;
			var labelField:String = data.LabelField;
			
			var xAxisLabelField:String = data.XAxisLabelField;
			
			var lineValueField:String = data.LineValueField;
			var lineLabelVisible:Boolean = data.LineLabelVisible;
			var lineLabelTextColor:uint = data.LineLabelTextColor;
			var lineLabelFontSize:Number = data.LineLabelFontSize;
			var lineLabelFontBold:Boolean = data.LineLabelFontBold;
			var lineLabelField:String = data.LineLabelField;
			
			if(!lineLabelField)
				lineLabelField = lineValueField;
			
			var lineValues:Array;
			var lineLabelValues:Array;
			var xAxisLabelValues:Array;
			var labelValues:Array;
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
		 
			if(!xAxisLabelValues || !labelValues || !lineValues)
				return;
			
			var groups:Array = GetChartGroups(xAxisLabelValues, labelValues, lineValues, lineLabelValues);
			if(groups.length==0)
				return;
			var group:ChartGroup;
			var item:ChartGroupItem;
			group = groups[0];
			
			var colors:Array = new Array();
			var labels:Array = new Array();
			var i:int;
			for(i=0;i<group.Labels.length;i++)
			{
				var color:int;
				if(i < data.Colors.length)
					color = data.Colors[i];
				else
					color = ChartUtil.GetColor(i);
				colors.push(color);
				if(labelVisible)
				{
					var labelText:String = group.Labels[i];
					var label:ChartLabel = new ChartLabel(color, labelText, data.LabelWidth, data.BorderColor,
						data.LabelTextColor, data.LabelFontSize, data.LabelFontBold);
					labels.push(label);
				}
			}
			
			for each(group in groups)
			{
				group.XAxisLabel = CreateAxisLabel(group.Name);
				for(i=0;i<group.Items.length;i++)
				{
					item = group.Items[i];
					var point:ChartShape = new ChartShape();
					point.Value = item.Value;
					point.Color = colors[i];
					if(lineLabelVisible)
					{
						point.Label = ChartUtil.CreateText(item.Label, 
							lineLabelFontSize, lineLabelFontBold, lineLabelTextColor);
					}
					group.ChartShapes.push(point);
				}
			}
			
			RenderLabels(labels, data.LabelColumn, data.LabelGap, data.LabelWidth, data.BorderColor);
			var columnWidth:Number = 25;
			InitializeParameters(columnWidth);
			RenderZeroLine();
			RenderCanvas();
			RenderGroups(groups);
 
		}
		private function RenderGroups(groups:Array):void
		{
			var data:Line3DMSData = Data as Line3DMSData;
			RenderFoldLines(groups);			
			
			var xAxisLabelY:Number = CanvasY + CanvasHeight + CanvasBarHeight + YShift + data.Gap;
			var groupGap:Number = groups.length>1 ?  CanvasWidth/(groups.length-1):CanvasWidth;
			var groupX:Number;
			var group:ChartGroup;
			var i:int;
			
			for(i=0; i<groups.length; i++)
			{
				group = groups[i];
				groupX = CanvasX + groupGap*i;
				if(group.XAxisLabel)
				{
					group.XAxisLabel.x = groupX;
					group.XAxisLabel.y = xAxisLabelY;
					Foreground.addChild(group.XAxisLabel);
				}
				RenderPoints(groupX, group.ChartShapes);
			}
		}
		private function RenderPoints(groupX:Number, points:Array):void
		{
			var data:Line3DMSData = Data as Line3DMSData;
			var lineThickness:Number = data.LineThickness;
			
			for(var i:int=0; i<points.length; i++)
			{
				var point:ChartShape = points[i];
				var pointX:Number = groupX;
				var pointY:Number = YAxisZero - CanvasHeight*Number(point.Value)/(YAxisMax-YAxisMin);
				LineUtil.DrawPoint(point.graphics, pointX, pointY, lineThickness*2, lineThickness, point.Color);
				if(point.Label)
				{
					point.Label.x = pointX - lineThickness*2;
					point.Label.y = pointY - data.Gap - point.Label.height- lineThickness*2;
					Foreground.addChild(point.Label);
				}
				Background.addChild(point);
			}
		}
		private function GetFoldLinePoints(groups:Array, index:int):Array
		{
			var groupGap:Number = groups.length>1 ? CanvasWidth/(groups.length-1):CanvasWidth;
			var values:Array = new Array();
			for(var i:int=0; i<groups.length; i++)
			{
				var group:ChartGroup = groups[i];
				var points:Array = group.ChartShapes;
				if(points.length <= index)
					continue;
				var point:ChartShape = points[index];
				var pointX:Number = CanvasX + groupGap*i;
				var pointY:Number = YAxisZero - CanvasHeight*Number(point.Value)/(YAxisMax-YAxisMin);
				values.push({x:pointX, y:pointY});
			}
			return values;
		}
		
		private function RenderFoldLines(groups:Array):void
		{
			if(groups.length<2) 
				return;
			var data:Line3DMSData = Data as Line3DMSData;
			var lineThickness:Number = data.LineThickness;
			for(var i:int=0; i<groups.length; i++)
			{
				var group:ChartGroup = groups[i];
				var points:Array = group.ChartShapes;
				if(points.length <= i)
					continue;
				var color:uint = ChartShape(points[i]).Color;
				var foldLine:ChartShape = new ChartShape();
				LineUtil.DrawFoldLine(foldLine.graphics, GetFoldLinePoints(groups,i), lineThickness, color);
				Background.addChild(foldLine);
			}
			
		}
 
	}
}