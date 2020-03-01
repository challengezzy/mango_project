/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


3D画布图表基类

*/
package myreport.chart
{
	import flash.text.TextField;
	
	import hlib.StringUtil;
	
	import myreport.data.chart.Canvas3DData;
	import myreport.data.chart.SeriesData;

	public class ChartCanvas3D extends ChartBase
	{
		protected var XShift:Number;//x方向3D偏移量
		protected var YShift:Number;//y方向3D偏移量
		protected var ColumnWidth:Number//柱体宽度
		protected var FrontWidth:Number//3D化后的前景宽度，根据ColumnWidth计算
		
		protected var Canvas:ChartShape;
		protected var CanvasBarHeight:Number = 10;
		
		protected var CanvasX:Number;
		protected var CanvasY:Number;
		protected var CanvasWidth:Number;
		protected var CanvasHeight:Number;
		
		protected var YAxisValues:Array;
		
		protected var YAxisZero:Number;
		protected var YAxisMax:Number;
		protected var YAxisMin:Number;
		
		protected override function Disposing():void
		{
			Canvas = null;
			super.Disposing();
		}
		
		public function ChartCanvas3D()
		{
			super();
		}
		
		protected function InitializeParameters(columnWidth:Number):void
		{
			var data:Canvas3DData = Data as Canvas3DData;
			
			ColumnWidth = columnWidth;
			var params:Object = ColumnUtil.Create3DParameters(columnWidth);
			
			XShift = params.XShift;
			YShift = params.YShift;
			FrontWidth = params.FrontWidth;
			
			CanvasWidth = data.CanvasWidth;
			CanvasHeight = data.CanvasHeight;
			CanvasX = PaddingLeft + (ClientWidth - CanvasWidth)/2;
			CanvasY = PaddingTop;

			var series:SeriesData;
			
			if(IsExpression(data.YAxisLabelField))
			{
				YAxisValues = Invoke(data.YAxisLabelField) as Array;
			}
			else
			{	
				series = data.GetSeries(data.YAxisLabelField);
				if(series)
					YAxisValues = series.Values;
				else
					YAxisValues = StringUtil.SplitTrim(data.YAxisLabelField, ",");
			}
			YAxisMax = YAxisMin = 0;
			if(YAxisValues && YAxisValues.length)
			{
				YAxisValues.sort(Array.NUMERIC);
				YAxisMax = YAxisValues[YAxisValues.length-1];
				YAxisMin = YAxisValues[0];
			}

			YAxisZero = CanvasY + CanvasHeight*(1-(0-YAxisMin)/(YAxisMax-YAxisMin));
		}
		
		protected function RenderCanvas():void
		{
			var data:Canvas3DData = Data as Canvas3DData;
			
			var canvasBackgroundColor:Number = data.CanvasBackgroundColor;
			var canvasBackgroundAlpha:Number = data.CanvasBackgroundAlpha;
			var canvasBarColor:Number = data.CanvasBarColor;
			var canvasBarAlpha:Number = data.CanvasBarAlpha;
			
			Canvas = new ChartShape();
			ColumnUtil.DrawCanvas(Canvas.graphics, CanvasX, CanvasY, 
				CanvasWidth, CanvasHeight - data.Gap, canvasBackgroundColor, canvasBackgroundAlpha);
			ColumnUtil.DrawCanvasBar(Canvas.graphics, CanvasX, CanvasY + CanvasHeight, 
				CanvasWidth, CanvasBarHeight, canvasBarColor, canvasBarAlpha, XShift, YShift);
			Background.addChild(Canvas);
			
			RenderAxisName();
			RenderYAxis();
		}
		private function RenderAxisName():void
		{
			var data:Canvas3DData = Data as Canvas3DData;
			
			var axisNameTextColor:uint = data.AxisNameTextColor;
			var axisNameFontSize:Number = data.AxisNameFontSize;
			var axisNameFontBold:Boolean = data.AxisNameFontBold;
			var xAxisName:String = data.XAxisName;
			var yAxisName:String = data.YAxisName;
			
			var xAxisNameLabel:TextField = ChartUtil.CreateText(xAxisName,axisNameFontSize,
				axisNameFontBold,axisNameTextColor);
			xAxisNameLabel.x =(width - xAxisNameLabel.width)/2;
			xAxisNameLabel.y = height - PaddingBottom - xAxisNameLabel.height;
			Foreground.addChild(xAxisNameLabel);
			
			var yAxisNameLabel:TextField = ChartUtil.CreateText(yAxisName,axisNameFontSize,
				axisNameFontBold,axisNameTextColor);
			yAxisNameLabel.x = CanvasX - yAxisNameLabel.width - data.Gap;
			yAxisNameLabel.y = PaddingTop - yAxisNameLabel.height*2 - data.Gap;
			Foreground.addChild(yAxisNameLabel);
		}
		
		protected function CreateAxisLabel(text:Object):TextField
		{
			var data:Canvas3DData = Data as Canvas3DData;
			
			var axisTextColor:uint = data.AxisLabelTextColor;
			var axisFontSize:Number = data.AxisLabelFontSize;
			var axisFontBold:Boolean = data.AxisLabelFontBold;
			
			var label:TextField = ChartUtil.CreateText(text, axisFontSize, axisFontBold, axisTextColor);
			return label;
		}
		
		private function RenderYAxis():void
		{
			var data:Canvas3DData = Data as Canvas3DData;

			var canvasLineThickness:Number = data.CanvasLineThickness;
			var canvasLineColor:uint = data.CanvasLineColor;
			var canvasLineAlpha:Number = data.CanvasLineAlpha;
			var value:Number;
			
			var label:TextField;
			for each(value in YAxisValues)
			{
				var yAxisValue:Number = CanvasY + CanvasHeight*(1-(value-YAxisMin)/(YAxisMax-YAxisMin));
				label = CreateAxisLabel(value);
				label.x = CanvasX - label.width - data.Gap;
				label.y = yAxisValue - label.height/2;
				Foreground.addChild(label);
				Canvas.graphics.lineStyle(canvasLineThickness, canvasLineColor, canvasLineAlpha);
				Canvas.graphics.moveTo(CanvasX, yAxisValue);
				Canvas.graphics.lineTo(CanvasX+CanvasWidth, yAxisValue);
			}

		}
		protected function RenderZeroLine():void
		{
			if(YAxisMin<0)
			{
				var data:Canvas3DData = Data as Canvas3DData;
				
				var canvasLineThickness:Number = data.CanvasLineThickness;
				var canvasLineColor:uint = data.CanvasLineColor;
				var canvasLineAlpha:Number = data.CanvasLineAlpha;
				
				Canvas.graphics.lineStyle(canvasLineThickness, canvasLineColor, canvasLineAlpha);
				Canvas.graphics.moveTo(CanvasX, YAxisZero);
				Canvas.graphics.lineTo(CanvasX+CanvasWidth, YAxisZero);
			}
		}
		protected function RenderZeroPane():void
		{
			if(YAxisMin<0)
			{
				var data:Canvas3DData = Data as Canvas3DData;
				
				var canvasBarColor:Number = data.CanvasBarColor;
				var canvasBarAlpha:Number = data.CanvasBarAlpha;

				var zeroPane:ChartShape = new ChartShape();
				ColumnUtil.DrawCanvasZero(zeroPane.graphics, CanvasX, YAxisZero, CanvasWidth, 
					canvasBarColor, canvasBarAlpha, XShift, YShift);
				
				Background.addChild(zeroPane);
			}
		}
		/**
		 * item => {Name:*, Label:*, ShapeValue:*, ShapeLabel:*}
		 * @param xAxisLabels: X轴标签
		 * @param labels: 图例
		 * @param shapeValues: 图形值
		 * @param shapeLabels: 图形标签
		 */ 
		private static function ArraysToTable(xAxisLabels:Array, labels:Array, shapeValues:Array, 
											  shapeLabels:Array = null):Array
		{
			var table:Array = new Array();
			var item:Object;
 			for(var i:int = 0;i< xAxisLabels.length; i++)
			{
				item = {};
				item.Name = xAxisLabels[i];
				item.Label = labels[i];
				item.ShapeValue = shapeValues[i];
				item.ShapeLabel = null;
				if(shapeLabels)
					item.ShapeLabel = shapeLabels[i];
				
				table.push(item);
			}
			return table;
		}
		/**
		 * item => ChartGroup
		 * @param xAxisLabels: X轴标签
		 * @param labels: 图例
		 * @param shapeValues: 图形值
		 * @param shapeLabels: 图形标签
		 */ 
		protected static function GetChartGroups(xAxisLabels:Array, labels:Array, shapeValues:Array,
													 shapeLabels:Array = null):Array
		{
			var table:Array = ArraysToTable(xAxisLabels, labels, shapeValues, shapeLabels);
			
			var groups:Array = new Array();
			var groupNames:Array = new Array();
			var chartLabels:Array = new Array();
			var group:ChartGroup;
			for each(var row:Object in table)
			{
				if(chartLabels.indexOf(row.Label)<0)
					chartLabels.push(row.Label);
				
				if(groupNames.indexOf(row.Name)>=0)
					continue;
				groupNames.push(row.Name);
				group = new ChartGroup();
				group.Name = row.Name;
				group.Labels = chartLabels;
				groups.push(group);
			}
			
			for each(group in groups)
			{
				FillGroupItems(group, table);
			}
			
			return groups;
		}
		
		private static function FillGroupItems(group:ChartGroup,table:Array):void
		{
			var rows:Array = new Array();
			var row:Object;
			for each(row in table)
			{
				if(row.Name == group.Name)
				{
					rows.push(row);
				}
			}
			for each(var label:Object in group.Labels)
			{
				var item:ChartGroupItem = new ChartGroupItem();
				group.Items.push(item);
				row = GetRow(label, rows);
				if(row)
				{
					item.Label = row.ShapeLabel;
					item.Value = row.ShapeValue;
				}
			}
		}
		
		private static function GetRow(label:Object, rows:Array):Object
		{
			var row:Object;
			for each(row in rows)
			{
				if(row.Label == label)
					return row;
			}
			return null;
		}
	}
}