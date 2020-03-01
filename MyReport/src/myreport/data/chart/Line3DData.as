package myreport.data.chart
{
	import hlib.XmlWriter;

	public class Line3DData extends Canvas3DData
	{
		/** 线条宽度 */
		public var LineThickness:Number = 2;
		/** 折线值表达式 */
		public var LineValueField:String = "";
		/** 显示折线标签 */
		public var LineLabelVisible:Boolean = true;
		/** 折线标签文本颜色 */
		public var LineLabelTextColor:uint = 0x000000;
		/** 折线标签字体大小 */
		public var LineLabelFontSize:Number = 11;
		/** 折线标签字体加粗 */
		public var LineLabelFontBold:Boolean = false;
		/** 折线标签表达式 */
		public var LineLabelField:String = "";
		
		public function Line3DData()
		{
			super();
			Type  =  "Line3D";
		}
		
		public override function Clone():*
		{
			var clone:Line3DData = new Line3DData();
			CloneTo(clone);
			
			return clone;
		}
		protected override function CloneTo(clone:ChartData):void
		{
			var data:Line3DData = clone as Line3DData;
			
			data.LineThickness = LineThickness;
			data.LineValueField = LineValueField;
			data.LineLabelVisible = LineLabelVisible;
			data.LineLabelTextColor = LineLabelTextColor;
			data.LineLabelFontSize = LineLabelFontSize;
			data.LineLabelFontBold = LineLabelFontBold;
			data.LineLabelField = LineLabelField;
			
			super.CloneTo(clone);
		}
		
		protected override function OnFromXML(xml:XML):void
		{
			if(xml.LineThickness.length())
				LineThickness = xml.LineThickness;
			if(xml.LineValueField.length())
				LineValueField = xml.LineValueField;
			if(xml.LineLabelVisible.length())
				LineLabelVisible = ReadBoolean(xml.LineLabelVisible);
			if(xml.LineLabelTextColor.length())
				LineLabelTextColor = ReadColor(xml.LineLabelTextColor);
			if(xml.LineLabelFontSize.length())
				LineLabelFontSize = xml.LineLabelFontSize;
			if(xml.LineLabelFontBold.length())
				LineLabelFontBold = ReadBoolean(xml.LineLabelFontBold);
			if(xml.LineLabelField.length())
				LineLabelField = xml.LineLabelField;

			super.OnFromXML(xml);
		}
		
		protected override function OnToXML(xml:XmlWriter):void
		{
			if(LineThickness!=2)
				xml.Ele("LineThickness", LineThickness);
			if(LineValueField)
				xml.Ele("LineValueField", LineValueField);
			if(!LineLabelVisible)
				xml.Ele("LineLabelVisible", LineLabelVisible);
			if(LineLabelTextColor != 0x000000)
				xml.Ele("LineLabelTextColor", LineLabelTextColor);
			if(LineLabelFontSize != 11)
				xml.Ele("LineLabelFontSize", LineLabelFontSize);
			if(LineLabelFontBold)
				xml.Ele("LineLabelFontBold", LineLabelFontBold);
			if(LineLabelField)
				xml.Ele("LineLabelField", LineLabelField);
			
			
			super.OnToXML(xml);
		}
	}
}