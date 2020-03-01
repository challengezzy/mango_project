/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


图表——柱状图3D-Muti-Series(多序列）

*/
package myreport.chart
{
	import mx.collections.ArrayCollection;
	
	import myreport.data.chart.Column3DMSData;
	import myreport.data.chart.SeriesData;
	
	public class Column3DMS extends ChartCanvas3D
	{
		public function Column3DMS()
		{
			super();
		}

		public override function Render():void
		{
			super.Render();
			var data:Column3DMSData = Data as Column3DMSData;
			
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
			
			if(!xAxisLabelValues || !labelValues || !columnValues)
				return;
			
			var groups:Array = GetChartGroups(xAxisLabelValues, labelValues, columnValues, columnLabelValues);
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
					var column:ChartShape = new ChartShape();
					column.Value = item.Value;
					column.Color = colors[i];
					if(columnLabelVisible)
					{
						column.Label = ChartUtil.CreateText(item.Label, 
							columnLabelFontSize, columnLabelFontBold, columnLabelTextColor);
					}
					group.ChartShapes.push(column);
				}
			}

			RenderLabels(labels, data.LabelColumn, data.LabelGap, data.LabelWidth, data.BorderColor);
			var columnWidth:Number = 25;
			if(groups.length>0)
				columnWidth = canvasWidth/(1+groups.length)/colors.length*columnWidthScale;
			InitializeParameters(columnWidth);
			RenderCanvas();
			RenderGroups(groups);
 
		}
		
		private function RenderGroups(groups:Array):void
		{
			var data:Column3DMSData = Data as Column3DMSData;
			
			var columnWidthScale:Number = data.ColumnWidthScale;
 
			var xAxisLabelY:Number = CanvasY + CanvasHeight + CanvasBarHeight + YShift + data.Gap;
			var groupGap:Number = CanvasWidth/(1+groups.length);
			var groupX:Number;
			var group:ChartGroup;
			var i:int;
			for(i=0; i<groups.length; i++)
			{
				group = groups[i];
				groupX = CanvasX + groupGap*(1-columnWidthScale/2+i) + XShift;
				if(group.XAxisLabel)
				{
					group.XAxisLabel.x = groupX - XShift + (groupGap*columnWidthScale-group.XAxisLabel.width)/2;
					group.XAxisLabel.y = xAxisLabelY;
					Foreground.addChild(group.XAxisLabel);
				}
				RenderColumns(groups, groupX, group.ChartShapes, true);
			}

			RenderZeroPane();
			
			for(i=0; i<groups.length; i++)
			{
				group = groups[i];
				groupX = CanvasX + groupGap*(1-columnWidthScale/2+i) + XShift;
				RenderColumns(groups, groupX, group.ChartShapes, false);
			}
 
		}
 
		private function RenderColumns(groups:Array, groupX:Number, columns:Array, negative:Boolean):void
		{
			var data:Column3DMSData = Data as Column3DMSData;
			
			var columnWidthScale:Number = data.ColumnWidthScale;
			var columnFillAlpha:Number = data.ColumnFillAlpha;
 
			var groupGap:Number = CanvasWidth/(1+groups.length);
			var columnHeight:Number;
			var columnX:Number;
			var columnLabelY:Number;
			var columnGap:Number = groupGap*columnWidthScale/columns.length;
 
			for(var i:int=0; i<columns.length; i++)
			{
				var column:ChartShape = columns[i];
				var value:Number = Number(column.Value);
				columnX = groupX + columnGap*i;
				var render:Boolean = false;
				
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