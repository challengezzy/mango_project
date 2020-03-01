/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


图表基类

*/
package myreport.chart
{
 
	import hlib.DisposeUtil;
	import hlib.DrawUtil;
	import hlib.SpriteBase;
	import hlib.TextBase;
	
	import mx.collections.ArrayCollection;
	import mx.collections.Sort;
	import mx.collections.SortField;
	
	import myreport.data.chart.ChartData;
	import myreport.expression.ExpressionContext;
	import myreport.expression.ExpressionEngine;
 
	public class ChartBase extends SpriteBase
	{
		protected var PaddingLeft:Number = 2;
		protected var PaddingRight:Number = 2;
		protected var PaddingTop:Number = 2;
		protected var PaddingBottom:Number = 2;
		protected var Context:ExpressionContext;
		protected var Background:SpriteBase;//背景，一般存放各种图表图形
		protected var Foreground:SpriteBase;//前景，一般存放文本
 
		private var _Data:ChartData;
		
		protected override function Disposing():void
		{
			_Data = null;
			Background = null;
			Foreground = null;
			super.Disposing();
		}
		
		public function ChartBase()
		{
			super();
			Context = new ExpressionContext();
			Background = new SpriteBase();
			Foreground = new SpriteBase();
			addChild(Background);
			addChild(Foreground);
		}
		
		public function SetExternalData(table:ArrayCollection, parameters:Object):void
		{
			Context.Table = table as ArrayCollection;
			Context.Parameters = parameters;
		}
		
		public function get Data():ChartData
		{
			return _Data;
		}
		
		public function set Data(value:ChartData):void
		{
			_Data = value;
		}
  
		public function Render():void
		{
			Background.RemoveAllChildren();
			Foreground.RemoveAllChildren();

			if(!width || !height || !Data) return;
			
			PaddingLeft = Data.PaddingLeft;
			PaddingRight = Data.PaddingRight;
			PaddingTop = Data.PaddingTop;
			PaddingBottom = Data.PaddingBottom;
			
			var beginBackgroundColor:uint = Data.BeginBackgroundColor;
			var endBackgroundColor:uint = Data.EndBackgroundColor;
			graphics.clear();
			DrawUtil.DrawBackgrouand(graphics, 1, 1, width - 1, height - 1, 
				[beginBackgroundColor,endBackgroundColor],[1,1],"linear");
			
			RenderTitle();
			RenderSubtitle();
		}
		
		protected function get ClientWidth():Number
		{
			return width - PaddingLeft - PaddingRight;
		}
		
		protected function get ClientHeight():Number
		{
			return height - PaddingTop - PaddingBottom;
		}
		
		private function RenderTitle():void
		{
			if(!Data.TitleVisible) 
				return;
			
			var fontSize:Number = Data.TitleFontSize;
			var fontBold:Boolean = Data.TitleFontBold;
			var textColor:uint = Data.TitleTextColor;
			var text:String = String(Invoke(Data.Title));

			AddTitleText(text, fontSize, fontBold, textColor);
		}
		
		private function RenderSubtitle():void
		{
			if(!Data.SubtitleVisible) 
				return;
			
			var fontSize:Number = Data.SubtitleFontSize;
			var fontBold:Boolean = Data.SubtitleFontBold;
			var textColor:uint = Data.SubtitleTextColor;
			var text:String = String(Invoke(Data.Subtitle));
			
			AddTitleText(text, fontSize, fontBold, textColor);
		}
		
		private function AddTitleText(text:Object, fontSize:Number, fontBold:Boolean, textColor:uint):void
		{
 
			var title:TextBase = ChartUtil.CreateText(text, fontSize, fontBold, textColor, ClientWidth, "center"); 
			title.x = PaddingLeft;
			title.y = PaddingTop;
			Foreground.addChild(title);
			
			PaddingTop += title.height + Data.Gap;
		}
		
		protected function RenderLabels(labels:Array, labelColumn:uint = 1, 
										labelGap:Number = 2, labelWidth:Number = 120,
										borderColor:uint = 0x696969):void
		{
			var panel:ChartLabelPanel = new ChartLabelPanel(labels, labelColumn, 
				labelGap, labelWidth, borderColor);
			panel.Render();
			panel.y = height - PaddingBottom - panel.height;
			panel.x = (width-panel.width)/2;
			Foreground.addChild(panel);
			PaddingBottom += panel.height + Data.Gap;
		}
		
		protected function IsExpression(expression:String):Boolean
		{
			return ExpressionEngine.IsExpression(expression);
		}
		
		protected function Invoke(expression:String):Object
		{
			return ExpressionEngine.Invoke(expression, Context);
		}

//		/**
//		 * 排序表格
//		 * @param sortDirection: asc, desc
//		 */ 
//		private function SortTable(table:ArrayCollection, sortField:String, sortExpression:String, sortDirection:String = "asc"):ArrayCollection
//		{
//			if(!sortExpression || !sortField)
//				return table;
//			var sortTable:ArrayCollection = new ArrayCollection();
//			var newTable:ArrayCollection = new ArrayCollection();
// 
//			for each(var row:Object in table)
//			{
//				Context.Current = row[sortField];
//				var key:Object = Invoke(sortExpression);
//				sortTable.addItem({Key:key, Row:row});
//			}
//			var sort:Sort = new Sort();
//			var sortFields:Array = new Array();
//			sortFields.push(new SortField("Key", false, String(sortDirection).toLowerCase() == "desc"));
//			sort.fields = sortFields;
//			sortTable.sort = sort;
//			sortTable.refresh();
//			
//			for each(var item:Object in sortTable)
//			{
//				newTable.addItem(item.Row);
//			}
//			
//			sortTable.removeAll();
//			return newTable;
//			
//		}
//		/**
//		 * 获取表格
//		 */ 
//		protected function GetTable(key:String, fields:String, 
//			sortField:String, sortExpression:String, sortDirection:String = "asc"):ArrayCollection
//		{
//			var table:ArrayCollection = Data.GetTable(key, fields);
//			table = SortTable(table, sortField, sortExpression, sortDirection);
//			return table;
//		}
//		
//		/**
//		 * 
//		 * 返回值：ArrayCollection : item => {Key:String, Table:ArrayCollection}
//		 * 
//		 */ 
//		protected function GetGroupData(groupByField:String, labelField:String, valueField:String,
//										sortExpression:String, sortDirection:String = "asc"):ArrayCollection
//		{
//			var groups:ArrayCollection = Data.GroupBy(groupByField, labelField, valueField);
//			groups = SortTable(groups, "Name", sortExpression, sortDirection);
//			return groups;
//		}
//		/**
//		 * 
//		 * 返回值：{Key:String, Table:ArrayCollection}
//		 * 
//		 */ 
//		private function GetGroupItem(groupData:ArrayCollection, name:String):Object
//		{
//			for each(var item:Object in groupData)
//			{
//				if(item.Name == name)
//					return item;
//			}
//			return null;
//		}
//		/**
//		 * 
//		 * 返回值：ArrayCollection : item => {Label:String, Color:uint}
//		 * 
//		 */ 
//		protected function GetLabelColors(groupData:ArrayCollection, labelField:String):Array
//		{
//			var labels:Array = new Array();
//			for each(var item:Object in groupData)
//			{
//				var table:ArrayCollection = item.Table;
//				for each(var row:Object in table)
//				{
//					var value:String = String(row[labelField]);
//					if(labels.indexOf(value)<0)
//						labels.push(value);
//				}
//			}
//			
//			var labelColor:Array = new Array();
//			for(var i:int=0; i<labels.length; i++)
//			{
//				labelColor.push({Label:labels[i], Color:ChartUtil.GetColor(i)});//匿名数据
//			}
//			
//			return labelColor;
//		}
//		
//		/**
//		 * 
//		 * 按labelColors排序
//		 * 
//		 */
//		protected function GetGroupTable(groupData:ArrayCollection, name:String, labelField:String, labelColors:Array):ArrayCollection
//		{
//			var group:Object = GetGroupItem(groupData, name);
//			var table:ArrayCollection = group.Table;
//			var newTable:ArrayCollection = new ArrayCollection();
//			for each(var item:Object in labelColors)
//			{
//				for(var i:int=0; i<table.length; i++)
//				{
//					var row:Object = table[i];
//					if(item.Label == row[labelField])
//					{
//						table.removeItemAt(i);
//						newTable.addItem(row);
//						break;
//					}
//				}
//			}
//			group.Table = newTable;
//			return newTable;
//		}
		
//		protected function RenderLabelsBy(labelColors:Array, labelVisible:Boolean, labelExpression:String,
//										  labelWidth:Number, borderColor:uint, textColor:uint, 
//										  fontSize:Number, fontBold:Boolean):void
//		{
//			if(!labelVisible) 
//				return;
//			var labels:Array = new Array();
//			if(labelVisible)//创建Label
//			{
//				for each(var item:Object in labelColors)
//				{
//					Context.Current = item.Label;
//					var labelText:String = String(Invoke(labelExpression));
//					var label:ChartLabel = new ChartLabel(item.Color, labelText, 
//						labelWidth, borderColor, textColor, fontSize, fontBold);
//					labels.push(label);
//				}
//			}
//			RenderLabels(labels);
//		}
		
		
		
	}
}