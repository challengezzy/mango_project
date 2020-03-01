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
	
	import myreport.data.chart.Column3DSData;
	import myreport.data.chart.SeriesData;
	
	public class Column3DS extends ChartCanvas3D
	{
		private var _NegativeIndex:int;
		public function Column3DS()
		{
			super();
		}

		public override function Render():void
		{
			super.Render();
			var data:Column3DSData = Data as Column3DSData;
			
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
 
	 
			var columnValues:Array;
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
			
			var groups:Array = GetChartGroups(xAxisLabelValues, labelValues, columnValues);
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
				var maxValue:Number = 0;
				var minValue:Number = 0;
				
				group.XAxisLabel = CreateAxisLabel(group.Name);
				for(i=0;i<group.Items.length;i++)
				{
					item = group.Items[i];
					var value:Number = Number(item.Value);
					if(value>=0)
					{
						maxValue += value;
					}
					else
					{
						minValue += value;
					}
					var column:ChartShape = new ChartShape();
					column.Value = item.Value;
					column.Color = colors[i];
					
					group.ChartShapes.push(column);
				}
				
				if(columnLabelVisible)
				{
					group.MaxLabel = ChartUtil.CreateText(maxValue, 
						columnLabelFontSize, columnLabelFontBold, columnLabelTextColor);
		 
					group.MinLabel = ChartUtil.CreateText(minValue, 
						columnLabelFontSize, columnLabelFontBold, columnLabelTextColor);
				}
			}

			RenderLabels(labels, data.LabelColumn, data.LabelGap, data.LabelWidth, data.BorderColor);
			var columnWidth:Number = 25;
			if(groups.length>0)
				columnWidth = canvasWidth/(1+groups.length)*columnWidthScale;
 
			InitializeParameters(columnWidth);
			RenderCanvas();
			_NegativeIndex = Background.numChildren;
			RenderGroups(groups);
 
		}
		
		private function RenderGroups(groups:Array):void
		{
			var data:Column3DSData = Data as Column3DSData;
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
				RenderColumns(groups, groupX, group, true);
			}
			
			RenderZeroPane();
			
			for(i=0; i<groups.length; i++)
			{
				group = groups[i];
				groupX = CanvasX + groupGap*(1-columnWidthScale/2+i) + XShift;
				RenderColumns(groups, groupX, group, false);
			}
 
		}
 
		private function RenderColumns(groups:Array, groupX:Number, group:ChartGroup, negative:Boolean):void
		{
			var data:Column3DSData = Data as Column3DSData;
			
			var columnWidthScale:Number = data.ColumnWidthScale;
			var columnFillAlpha:Number = data.ColumnFillAlpha;
 
			var columns:Array = group.ChartShapes;
			var columnHeight:Number;
			var columnX:Number = groupX;
			var columnY:Number;
			
			var zeroUp:Number = 0;
			var zeroDown:Number = 0;
			
			for(var i:int=0; i<columns.length; i++)
			{
				var column:ChartShape = columns[i];
				var render:Boolean = false;
				var value:Number = Number(column.Value);
				if(!negative && value>=0)
				{
					columnHeight = CanvasHeight*value/(YAxisMax-YAxisMin);
					columnY = YAxisZero - zeroUp;
					zeroUp += columnHeight;
					render = true;
				}
				else if(negative && value<0)
				{
					columnHeight = 0-CanvasHeight*value/(YAxisMax-YAxisMin);
					columnY = YAxisZero + zeroDown;
					zeroDown += columnHeight;
					render = true;
				}
				
				if(render)
				{
					ColumnUtil.DrawColumn3D(column.graphics, columnX, columnY, ColumnWidth, columnHeight, 
						column.Color, columnFillAlpha, negative, XShift, YShift, FrontWidth);
					if(!negative)
					{
						Background.addChild(column);
					}
					else
					{
						Background.addChildAt(column, _NegativeIndex);
					}
					
				}
			}
			if(group.MaxLabel)
			{
				group.MaxLabel.x = columnX;
				group.MaxLabel.y = YAxisZero - zeroUp - data.Gap - group.MaxLabel.height;
				Foreground.addChild(group.MaxLabel);
			}
			if(YAxisMin<0 && group.MinLabel)
			{
				group.MinLabel.x = columnX - XShift;
				group.MinLabel.y = YAxisZero + zeroDown + data.Gap + YShift;
				Foreground.addChild(group.MinLabel);
				
			}		
		}

	}
}