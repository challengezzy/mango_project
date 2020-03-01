/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


图表数据基类。


*/
package myreport.data.chart
{
	import flash.utils.Dictionary;
	
	import hlib.CloneUtil;
	import hlib.DisposeUtil;
	import hlib.ItemData;
	import hlib.StringUtil;
	import hlib.XmlWriter;
	
	import mx.collections.ArrayCollection;
	
	public class ChartData extends ItemData
	{
		public var Type:String = "";
		
		public var PaddingLeft:Number = 8;
		public var PaddingRight:Number = 8;
		public var PaddingTop:Number = 8;
		public var PaddingBottom:Number = 8;
		/** 内部子元件间距 */ 
		public var Gap:Number = 2;
		
		public var BeginBackgroundColor:uint = 0xffffff;
		public var EndBackgroundColor:uint = 0xf1f1f1;
		public var BorderColor:uint = 0x696969;
		
		public var TitleVisible:Boolean = true;
		public var Title:String = "";
		public var TitleTextColor:uint = 0x000000;
		public var TitleFontSize:Number = 16;
		public var TitleFontBold:Boolean = true;
		
		public var SubtitleVisible:Boolean = true;
		public var Subtitle:String = "";
		public var SubtitleTextColor:uint = 0x000000;
		public var SubtitleFontSize:Number = 12;
		public var SubtitleFontBold:Boolean = false;
		
		public var DataSource:DataSourceData = new DataSourceData();
		/** 颜色表 */
		public var Colors:Array = new Array();
		
		override protected function Disposing():void
		{
			DataSource.Dispose();
			DataSource = null;
			hlib.DisposeUtil.Dispose(Colors);
			Colors = null;
			
			super.Disposing();
		}
		
		public function ChartData()
		{
			super();
		}
		
		protected function CloneTo(clone:ChartData):void
		{
			clone.Type = Type;
			
			clone.PaddingLeft = PaddingLeft;
			clone.PaddingRight = PaddingRight;
			clone.PaddingTop = PaddingTop;
			clone.PaddingBottom = PaddingBottom;
			clone.Gap = Gap;
			
			clone.BeginBackgroundColor = BeginBackgroundColor;
			clone.EndBackgroundColor = EndBackgroundColor;
			clone.BorderColor = BorderColor;
			
			clone.TitleVisible = TitleVisible;
			clone.Title = Title;
			clone.TitleTextColor = TitleTextColor;
			clone.TitleFontSize = TitleFontSize;
			clone.TitleFontBold = TitleFontBold;
			
			clone.SubtitleVisible = SubtitleVisible;
			clone.Subtitle = Subtitle;
			clone.SubtitleTextColor = SubtitleTextColor;
			clone.SubtitleFontSize = SubtitleFontSize;
			clone.SubtitleFontBold = SubtitleFontBold;
			
			clone.DataSource = DataSource.Clone();
			clone.Colors = CloneUtil.Clone(Colors);
			
			
		}
		
		public override function FromXML(xml:XML):void
		{
			if (!xml)
				return;
			Type = xml.@type;
			
			if(xml.PaddingLeft.length())
				PaddingLeft = xml.PaddingLeft;
			if(xml.PaddingRight.length())
				PaddingRight = xml.PaddingRight;
			if(xml.PaddingTop.length())
				PaddingTop = xml.PaddingTop;
			if(xml.PaddingBottom.length())
				PaddingBottom = xml.PaddingBottom;
			if(xml.Gap.length())
				Gap = xml.Gap;
			
			if(xml.BeginBackgroundColor.length())
				BeginBackgroundColor = ReadColor(xml.BeginBackgroundColor);
			if(xml.EndBackgroundColor.length())
				EndBackgroundColor = ReadColor(xml.EndBackgroundColor);
			if(xml.BorderColor.length())
				BorderColor = ReadColor(xml.BorderColor);
			
			if(xml.TitleVisible.length())
				TitleVisible = ReadBoolean(xml.TitleVisible);
			if(xml.Title.length())
				Title = xml.Title;
			if(xml.TitleTextColor.length())
				TitleTextColor = ReadColor(xml.TitleTextColor);
			if(xml.TitleFontSize.length())
				TitleFontSize = xml.TitleFontSize;
			if(xml.TitleFontBold.length())
				TitleFontBold = ReadBoolean(xml.TitleFontBold);
			
			if(xml.SubtitleVisible.length())
				SubtitleVisible = ReadBoolean(xml.SubtitleVisible);
			if(xml.Subtitle.length())
				Subtitle = xml.Subtitle;
			if(xml.SubtitleTextColor.length())
				SubtitleTextColor = ReadColor(xml.SubtitleTextColor);
			if(xml.SubtitleFontSize.length())
				SubtitleFontSize = xml.SubtitleFontSize;
			if(xml.SubtitleFontBold.length())
				SubtitleFontBold = ReadBoolean(xml.SubtitleFontBold);
			
			if(xml.DataSource.length())
				DataSource.FromXML(XML(xml.DataSource));
			
			if(xml.Colors.length())
			{
				for each(var color:XML in xml.Colors.Value)
				{
					Colors.push(ReadColor(color));
				}
			}
			
			OnFromXML(xml);
		}
		
		protected function OnFromXML(xml:XML):void
		{
			
		}
		
		public override function ToXML():String
		{
			var xml:XmlWriter = new XmlWriter();
			xml.BeginEle("Chart")
				.Attr("type", Type).EndEle();
			
			if(PaddingLeft!=8)
				xml.Ele("PaddingLeft", PaddingLeft);
			if(PaddingRight!=8)
				xml.Ele("PaddingRight", PaddingRight);
			if(PaddingTop!=8)
				xml.Ele("PaddingTop", PaddingTop);
			if(PaddingBottom!=8)
				xml.Ele("PaddingBottom", PaddingBottom);
			if(Gap!=2)
				xml.Ele("Gap", Gap);
			
			if(BeginBackgroundColor!=0xffffff)
				xml.Ele("BeginBackgroundColor", ToHexColor(BeginBackgroundColor));
			if(EndBackgroundColor!=0xf1f1f1)
				xml.Ele("EndBackgroundColor", ToHexColor(EndBackgroundColor));
			if(BorderColor!=0x696969)
				xml.Ele("BorderColor", ToHexColor(BorderColor));
			
			if(!TitleVisible)
				xml.Ele("TitleVisible", TitleVisible);
			if(Title)
				xml.Ele("Title", Title);
			if(TitleTextColor!=0x000000)
				xml.Ele("TitleTextColor", ToHexColor(TitleTextColor));
			if(TitleFontSize!=16)
				xml.Ele("TitleFontSize", TitleFontSize);
			if(!TitleFontBold)
				xml.Ele("TitleFontBold", TitleFontBold);
			
			if(!SubtitleVisible)
				xml.Ele("SubtitleVisible", SubtitleVisible);
			if(Subtitle)
				xml.Ele("Subtitle", Subtitle);
			if(SubtitleTextColor!=0x000000)
				xml.Ele("SubtitleTextColor", ToHexColor(SubtitleTextColor));
			if(SubtitleFontSize!=12)
				xml.Ele("SubtitleFontSize", SubtitleFontSize);
			if(SubtitleFontBold)
				xml.Ele("SubtitleFontBold", SubtitleFontBold);
				
			OnToXML(xml);
			
			if(DataSource.Serieses.length)
				xml.WriteObject(DataSource);
			
			if(Colors.length)
			{
				xml.Begin("Colors");
				for each(var color:uint in Colors)
				{
					xml.Ele("Value", ToHexColor(color));
				}
				xml.End("Colors");
			}
			
			xml.End("Chart");
			
			return xml.toString();
			
		}
		
		protected function OnToXML(xml:XmlWriter):void
		{
			
		}
		
		//================数据源====================
		public function GetSeries(name:String):SeriesData
		{
			for each(var series:SeriesData in DataSource.Serieses)
			{
				if(series.Name == name)
					return series;
			}
			return null;
		}
		
		public function SetSeries(name:String, values:Array):void
		{
			var series:SeriesData = GetSeries(name);
			if(!series)
			{
				series = new SeriesData();
				series.Name = name;
				DataSource.Serieses.push(series);
			}
			series.Values = values.slice();
		}
		
//		private function GetSeriesLength(name:String):uint
//		{
//			var series:SeriesData = GetSeries(name);
//			if(series)
//				return series.Values.length;
//			return 0;
//		}
//		
//		private function GetSeriesValue(name:String, index:int):*
//		{
//			var series:SeriesData = GetSeries(name);
//			if(series && series.Values.length>index)
//				return series.Values[index];
//			return null;
//		}
		
//		public function ClearDataSource():void
//		{
//			DisposeUtil.Dispose(DataSource.Serieses);
//		}
		
//		public function AddSeries(name:String, values:Array):void
//		{
//			var series:SeriesData = GetSeries(name);
//			if(!series)
//			{
//				series = new SeriesData();
//				series.Name = name;
//				DataSource.Serieses.push(series);
//			}
//			DisposeUtil.Dispose(series.Values);
//			for each(var value:* in values)
//			{
//				series.Values.push(value);
//			}
//		}
//		/**
//		 * 获取数据表
//		 * 返回值：ArrayCollection : row => Dictionary
//		 * 
//		 */ 
//		public function GetTable(key:String, fields:String):ArrayCollection
//		{
//			var len:uint = GetSeriesLength(key);
//			var table:ArrayCollection = new ArrayCollection();
//			
//			var columns:Array = StringUtil.SplitTrim(fields, ",");
//			var colIndex:int,rowIndex:int;
//			
//			for(rowIndex=0; rowIndex<len; rowIndex++)
//			{
//				var row:Dictionary = new Dictionary();
//				for(colIndex=0; colIndex<columns.length; colIndex++)
//				{
//					var column:String = columns[colIndex];
//					row[column] = GetSeriesValue(column, rowIndex);
//				}
//				table.addItem(row);
//			}
//			
//			return table;
//		}
//		/**
//		 * 
//		 * 数据分组
//		 * 返回值：ArrayCollection : item => {Key:String, Table:ArrayCollection}
//		 * 
//		 */
//		public function GroupBy(groupByField:String, labelField:String, valueField:String):ArrayCollection
//		{
//			var result:ArrayCollection = new ArrayCollection();
//			var series:SeriesData = GetSeries(groupByField);
//			if(!series) 
//				return result;
//			var groups:Dictionary = new Dictionary();
//			var name:String;
//			var i:int;
//			var table:ArrayCollection;
//			for(i=0; i<series.Values.length; i++)
//			{
//				name = series.Values[i];
//				table = groups[name] as ArrayCollection;
//				if(!table)
//				{
//					table = new ArrayCollection();
//					groups[name] = table;
//				}
//				var row:Dictionary = new Dictionary();
//				row[labelField] = GetSeriesValue(labelField, i);
//				row[valueField] = GetSeriesValue(valueField, i);
//				table.addItem(row);
//			}
//			
//			for(name in groups)
//			{
//				result.addItem({Name:name, Table:groups[name]});
//			}
//			
//			return result;
//		}
	}
}