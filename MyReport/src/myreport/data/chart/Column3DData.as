package myreport.data.chart
{
	import hlib.XmlWriter;

	public class Column3DData extends Canvas3DData
	{
		/** 显示标签栏 */
		public var LabelVisible:Boolean = true;
		/** 标签文本颜色 */
		public var LabelTextColor:uint = 0x000000;
		/** 标签题字体大小 */
		public var LabelFontSize:Number = 10;
		/** 标签题字体加粗 */
		public var LabelFontBold:Boolean = false;
		/** 标签宽度 */
		public var LabelWidth:Number = 120;
		/** 标签间距 */
		public var LabelGap:Number = 2;
		/** 标签布局列数 */
		public var LabelColumn:uint = 3;
		/** 标签内容表达式 */
		public var LabelField:String = "";
		
		/** 柱形宽度比 */
		public var ColumnWidthScale:Number = 0.6;
		/** 柱形填充透明度 */
		public var ColumnFillAlpha:Number = 1;
		/** 柱形值 */
		public var ColumnValueField:String ="";
		/** 显示柱形标签 */
		public var ColumnLabelVisible:Boolean = true;
		/** 柱形标签文本颜色 */
		public var ColumnLabelTextColor:uint = 0x000000;
		/** 柱形标签字体大小 */
		public var ColumnLabelFontSize:Number = 11;
		/** 柱形标签字体加粗 */
		public var ColumnLabelFontBold:Boolean = false;
		/** 柱形标签值 */
		public var ColumnLabelField:String = "";
		public function Column3DData()
		{
			super();
			Type = "Column3D";
		}
		
		public override function Clone():*
		{
			var clone:Column3DData = new Column3DData();
			CloneTo(clone);
			return clone;
		}
		
		protected override function CloneTo(clone:ChartData):void
		{
			var data:Column3DData = clone as Column3DData;
			data.LabelVisible = LabelVisible;
			data.LabelTextColor = LabelTextColor;
			data.LabelFontSize = LabelFontSize;
			data.LabelFontBold = LabelFontBold;
			data.LabelWidth = LabelWidth;
			data.LabelGap = LabelGap;
			data.LabelColumn = LabelColumn;
			data.LabelField = LabelField;
			
			data.ColumnWidthScale = ColumnWidthScale;
			data.ColumnFillAlpha = ColumnFillAlpha;
			data.ColumnValueField = ColumnValueField;
			data.ColumnLabelVisible = ColumnLabelVisible;
			data.ColumnLabelTextColor = ColumnLabelTextColor;
			data.ColumnLabelFontSize = ColumnLabelFontSize;
			data.ColumnLabelFontBold = ColumnLabelFontBold;
			data.ColumnLabelField = ColumnLabelField;
			
			super.CloneTo(clone);
		}
		
		protected override function OnFromXML(xml:XML):void
		{
			
			if(xml.LabelVisible.length())
				LabelVisible = ReadBoolean(xml.LabelVisible);
			if(xml.LabelTextColor.length())
				LabelTextColor = ReadColor(xml.LabelTextColor);
			if(xml.LabelFontSize.length())
				LabelFontSize = xml.LabelFontSize;
			if(xml.LabelFontBold.length())
				LabelFontBold = ReadBoolean(xml.LabelFontBold);
			if(xml.LabelWidth.length())
				LabelWidth = xml.LabelWidth;
			if(xml.LabelGap.length())
				LabelGap = xml.LabelGap;
			if(xml.LabelColumn.length())
				LabelColumn = xml.LabelColumn;
			if(xml.LabelField.length())
				LabelField = xml.LabelField;
			
			if(xml.ColumnWidthScale.length())
				ColumnWidthScale = xml.ColumnWidthScale;
			if(xml.ColumnFillAlpha.length())
				ColumnFillAlpha = xml.ColumnFillAlpha;
			if(xml.ColumnValueField.length())
				ColumnValueField = xml.ColumnValueField;
			if(xml.ColumnLabelVisible.length())
				ColumnLabelVisible = ReadBoolean(xml.ColumnLabelVisible);
			if(xml.ColumnLabelTextColor.length())
				ColumnLabelTextColor = ReadColor(xml.ColumnLabelTextColor);
			if(xml.ColumnLabelFontSize.length())
				ColumnLabelFontSize = xml.ColumnLabelFontSize;
			if(xml.ColumnLabelFontBold.length())
				ColumnLabelFontBold = ReadBoolean(xml.ColumnLabelFontBold);
			if(xml.ColumnLabelField.length())
				ColumnLabelField = xml.ColumnLabelField;
 
			super.OnFromXML(xml);
		}
		
		protected override function OnToXML(xml:XmlWriter):void
		{
			if(!LabelVisible)
				xml.Ele("LabelVisible", LabelVisible);
			if(LabelTextColor!=0x000000)
				xml.Ele("LabelTextColor", LabelTextColor);
			if(LabelFontSize!=10)
				xml.Ele("LabelFontSize", LabelFontSize);
			if(LabelFontBold)
				xml.Ele("LabelFontBold", LabelFontBold);
			if(LabelWidth!=120)
				xml.Ele("LabelWidth", LabelWidth);
			if(LabelGap!=2)
				xml.Ele("LabelGap", LabelGap);
			if(LabelColumn!=3)
				xml.Ele("LabelColumn", LabelColumn);
			if(LabelField)
				xml.Ele("LabelField", LabelField);
			
			if(ColumnWidthScale != 0.6)
				xml.Ele("ColumnWidthScale", ColumnWidthScale);
			if(ColumnFillAlpha != 1)
				xml.Ele("ColumnFillAlpha", ColumnFillAlpha);
			if(ColumnValueField)
				xml.Ele("ColumnValueField", ColumnValueField);
			if(!ColumnLabelVisible)
				xml.Ele("ColumnLabelVisible", ColumnLabelVisible);
			if(ColumnLabelTextColor != 0x000000)
				xml.Ele("ColumnLabelTextColor", ColumnLabelTextColor);
			if(ColumnLabelFontSize != 11)
				xml.Ele("ColumnLabelFontSize", ColumnLabelFontSize);
			if(ColumnLabelFontBold)
				xml.Ele("ColumnLabelFontBold", ColumnLabelFontBold);
			if(ColumnLabelField)
				xml.Ele("ColumnLabelField", ColumnLabelField);
			
 
			super.OnToXML(xml);
		}
	}
}