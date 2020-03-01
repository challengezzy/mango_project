/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


图表——柱状图3D

*/
package myreport.chart
{
	import myreport.data.chart.Column3DData;
	import myreport.data.chart.SeriesData;
 
	public class Column3D extends ChartCanvas3D
	{
 
		public function Column3D()
		{
			super();
		}
		
		public override function Render():void
		{
			super.Render();
 
			var data:Column3DData = Data as Column3DData;
			
			var labelVisible:Boolean = data.LabelVisible;
			var labelField:String = data.LabelField;
			
			var canvasWidth:Number = data.CanvasWidth;
			var columnWidthScale:Number = data.ColumnWidthScale;
			
			var xAxisLabelField:String = data.XAxisLabelField;
			
			var columnValueField:String = data.ColumnValueField;
			var columnLabelVisible:Boolean = data.ColumnLabelVisible;
			var columnLabelTextColor:uint = data.ColumnLabelTextColor;
			var columnLabelFontSize:Number = data.ColumnLabelFontSize;
			var columnLabelFontBold:Boolean = data.ColumnLabelFontBold;
			var columnLabelField:String = data.ColumnLabelField;
			
			if(!columnLabelField)
				columnLabelField = columnValueField;
			
			if(!labelField)
				labelField = xAxisLabelField;
			
			var columnValues:Array;
			var columnLabelValues:Array;
			var xAxisLabelValues:Array;
			var labelValues:Array;
			var series:SeriesData;
			if(IsExpression(columnValueField))
			{
				columnValues = Invoke(columnValueField) as Array;
			}
			else
			{
				series = data.GetSeries(columnValueField);
				if(series)
					columnValues = series.Values;
			}
			if(IsExpression(columnLabelField))
			{
				columnLabelValues = Invoke(columnLabelField) as Array;
			}
			else
			{
				series = data.GetSeries(columnLabelField);
				if(series)
					columnLabelValues = series.Values;
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
			
			if(!columnValues)
				return;

			var labels:Array = new Array();
			var columns:Array = new Array();
			
			for(var i:int=0; i<columnValues.length; i++)
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
				
				var column:ChartShape = new ChartShape();
				column.Value = Number(columnValues[i]);
				column.Color = color;
				if(xAxisLabelValues)
					column.XAxisLabel = CreateAxisLabel(String(xAxisLabelValues[i]));
				if(columnLabelVisible && columnLabelValues)
				{
					column.Label = ChartUtil.CreateText(String(columnLabelValues[i]), 
						columnLabelFontSize, columnLabelFontBold, columnLabelTextColor);
				}
				
				columns.push(column);
			}
			
			RenderLabels(labels, data.LabelColumn, data.LabelGap, data.LabelWidth, data.BorderColor);
			var columnWidth:Number = 25;
			if(columns.length>0)
				columnWidth = canvasWidth/(1+columns.length)*columnWidthScale;
			InitializeParameters(columnWidth);
			RenderCanvas();
			RenderColumns(columns, true);
			RenderZeroPane();
			RenderColumns(columns, false);
 
		}

		private function RenderColumns(columns:Array, negative:Boolean):void
		{
			var data:Column3DData = Data as Column3DData;
			
			var columnFillAlpha:Number = data.ColumnFillAlpha;
			var columnWidthScale:Number = data.ColumnWidthScale;
			
			var columnHeight:Number;
			var columnX:Number;
			var columnLabelY:Number;
			var columnGap:Number = CanvasWidth/(1+columns.length);
			var xAxisLabelY:Number = CanvasY + CanvasHeight + CanvasBarHeight + YShift + data.Gap;
			for(var i:int=0; i<columns.length; i++)
			{
				var column:ChartShape = columns[i];
				columnX = CanvasX + columnGap*(1-columnWidthScale/2+i) + XShift;
				var render:Boolean = false;
				var value:Number = Number(column.Value);
				if(!negative && value>=0)
				{
					columnHeight = CanvasHeight*value/(YAxisMax-YAxisMin);
					if(column.Label)
						columnLabelY = YAxisZero - columnHeight - data.Gap - column.Label.height;
					render = true;
				}
				else if(negative && value<0)
				{
					columnHeight = 0-CanvasHeight*value/(YAxisMax-YAxisMin);
					if(column.Label)
						columnLabelY = YAxisZero - data.Gap - column.Label.height;
					render = true;
				}
				
				if(render)
				{
					ColumnUtil.DrawColumn3D(column.graphics, columnX, YAxisZero, ColumnWidth, columnHeight, 
						column.Color, columnFillAlpha, negative, XShift, YShift, FrontWidth);
					Background.addChild(column);
					if(column.XAxisLabel)
					{
						column.XAxisLabel.x = columnX - XShift;
						column.XAxisLabel.y = xAxisLabelY;
						Background.addChild(column.XAxisLabel);
					}
					if(column.Label)
					{
						column.Label.x = columnX;
						column.Label.y = columnLabelY;
						Foreground.addChild(column.Label);
					}
				}
			}
			
			
		}
		
	}
}