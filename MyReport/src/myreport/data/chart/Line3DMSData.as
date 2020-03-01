package myreport.data.chart
{
	import hlib.XmlWriter;
 
	public class Line3DMSData extends Line3DData
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
		
		public function Line3DMSData()
		{
			super();
			Type = "Line3DMS";
		}
		
		public override function Clone():*
		{
			var clone:Line3DMSData = new Line3DMSData();
			CloneTo(clone);
			return clone;
		}
		
		protected override function CloneTo(clone:ChartData):void
		{
			var data:Line3DMSData = clone as Line3DMSData;
			data.LabelVisible = LabelVisible;
			data.LabelTextColor = LabelTextColor;
			data.LabelFontSize = LabelFontSize;
			data.LabelFontBold = LabelFontBold;
			data.LabelWidth = LabelWidth;
			data.LabelGap = LabelGap;
			data.LabelColumn = LabelColumn;
			data.LabelField = LabelField;
 
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
			
			super.OnToXML(xml);
		}
	}
}