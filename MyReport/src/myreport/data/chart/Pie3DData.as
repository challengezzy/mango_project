package myreport.data.chart
{
	import hlib.XmlWriter;
	
	public class Pie3DData extends ChartData
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
		
		/** 饼图半径 */
		public var PieRadius:Number = 140;
		/** 饼图Y轴比例：饼图Y轴半径 = PieRadius * PieYScale */
		public var PieYScale:Number = 0.5;
		/** 饼图高度 */
		public var PieHeight:Number = 20;
		/** 饼图边框宽度 */
		public var PieBorderThickness:Number = 1;
		/** 饼图边框透明度 */
		public var PieBorderAlpha:Number = 0.4;
		/** 饼图填充透明度 */
		public var PieFillAlpha:Number = 0.7;
		/** 饼图角度值表达式 */
		public var PieValueField:String = "";
		/** 显示饼图标签 */
		public var PieLabelVisible:Boolean = true;
		/** 饼图标签文本颜色 */
		public var PieLabelTextColor:uint = 0x000000;
		/** 饼图标签题字体大小 */
		public var PieLabelFontSize:Number = 11;
		/** 饼图标签题字体加粗 */
		public var PieLabelFontBold:Boolean = false;
		/** 饼图标签宽度 */
		public var PieLabelWidth:Number = 120;
		/** 饼图标签与饼图的距离比例 */
		public var PieLabelDistanceScale:Number = 1.5;
		/** 饼图标签内容表达式 */
		public var PieLabelField:String = "";
		
		public function Pie3DData()
		{
			super();
			Type = "Pie3D";
		}
		
		public override function Clone():*
		{
			var clone:Pie3DData = new Pie3DData();
			
			CloneTo(clone);
 
			clone.LabelVisible = LabelVisible;
			clone.LabelTextColor = LabelTextColor;
			clone.LabelFontSize = LabelFontSize;
			clone.LabelFontBold = LabelFontBold;
			clone.LabelWidth = LabelWidth;
			clone.LabelGap = LabelGap;
			clone.LabelColumn = LabelColumn;
			clone.LabelField = LabelField;
 
			clone.PieRadius = PieRadius;
			clone.PieYScale = PieYScale;
			clone.PieHeight = PieHeight;
			clone.PieBorderThickness = PieBorderThickness;
			clone.PieBorderAlpha = PieBorderAlpha;
			clone.PieFillAlpha = PieFillAlpha;
			clone.PieValueField = PieValueField;
			clone.PieLabelVisible = PieLabelVisible;
			clone.PieLabelTextColor = PieLabelTextColor;
			clone.PieLabelFontSize = PieLabelFontSize;
			clone.PieLabelFontBold = PieLabelFontBold;
			clone.PieLabelWidth = PieLabelWidth;
			clone.PieLabelDistanceScale = PieLabelDistanceScale;
			clone.PieLabelField = PieLabelField;
			
			return clone;
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
 
			if(xml.PieRadius.length())
				PieRadius = xml.PieRadius;
			if(xml.PieYScale.length())
				PieYScale = xml.PieYScale;
			if(xml.PieHeight.length())
				PieHeight = xml.PieHeight;
			if(xml.PieBorderThickness.length())
				PieBorderThickness = xml.PieBorderThickness;
			if(xml.PieBorderAlpha.length())
				PieBorderAlpha = xml.PieBorderAlpha;
			if(xml.PieFillAlpha.length())
				PieFillAlpha = xml.PieFillAlpha;
			if(xml.PieValueField.length())
				PieValueField = xml.PieValueField;
			if(xml.PieLabelVisible.length())
				PieLabelVisible = ReadBoolean(xml.PieLabelVisible);
			if(xml.PieLabelTextColor.length())
				PieLabelTextColor = xml.PieLabelTextColor;
			if(xml.PieLabelFontSize.length())
				PieLabelFontSize = xml.PieLabelFontSize;
			if(xml.PieLabelFontBold.length())
				PieLabelFontBold = ReadBoolean(xml.PieLabelFontBold);
			if(xml.PieLabelWidth.length())
				PieLabelWidth = xml.PieLabelWidth;
			if(xml.PieLabelDistanceScale.length())
				PieLabelDistanceScale = xml.PieLabelDistanceScale;
			if(xml.PieLabelField.length())
				PieLabelField = xml.PieLabelField;
 
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
			
			if(PieRadius!=140)
				xml.Ele("PieRadius", PieRadius);
			if(PieYScale!=0.5)
				xml.Ele("PieYScale", PieYScale);
			if(PieHeight!=20)
				xml.Ele("PieHeight", PieHeight);
			if(PieBorderThickness!=1)
				xml.Ele("PieBorderThickness", PieBorderThickness);
			if(PieBorderAlpha!=0.4)
				xml.Ele("PieBorderAlpha", PieBorderAlpha);
			if(PieFillAlpha!=0.7)
				xml.Ele("PieFillAlpha", PieFillAlpha);
			if(PieValueField)
				xml.Ele("PieValueField", PieValueField);
			if(!PieLabelVisible)
				xml.Ele("PieLabelVisible", PieLabelVisible);
			if(PieLabelTextColor!=0x000000)
				xml.Ele("PieLabelTextColor", PieLabelTextColor);
			if(PieLabelFontSize!=11)
				xml.Ele("PieLabelFontSize", PieLabelFontSize);
			if(PieLabelFontBold)
				xml.Ele("PieLabelFontBold", PieLabelFontBold);
			if(PieLabelWidth!=120)
				xml.Ele("PieLabelWidth", PieLabelWidth);
			if(PieLabelDistanceScale!=1.5)
				xml.Ele("PieLabelDistanceScale", PieLabelDistanceScale);
			if(PieLabelField)
				xml.Ele("PieLabelField", PieLabelField);

			super.OnToXML(xml);
		}
	}
}