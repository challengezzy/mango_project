package myreport.data.chart
{
	import hlib.XmlWriter;

	public class Canvas3DData extends ChartData
	{
		/** 画布宽度 */
		public var CanvasWidth:Number = 420;
		/** 画布高度 */
		public var CanvasHeight:Number = 180;
		/** 画布背景颜色 */
		public var CanvasBackgroundColor:uint = 0xDDE3D5;
		/** 画布背景透明度 */
		public var CanvasBackgroundAlpha:Number = 1;
		/** 画布底栏颜色 */
		public var CanvasBarColor:uint = 0xACBB99;
		/** 画布底栏透明度 */
		public var CanvasBarAlpha:Number = 1;
		/** 画布线条宽度 */
		public var CanvasLineThickness:Number = 1;
		/** 画布线条颜色 */
		public var CanvasLineColor:uint = 0xACBB99;
		/** 画布线条透明度 */
		public var CanvasLineAlpha:Number = 1;
		/** 轴名称文本颜色 */
		public var AxisNameTextColor:uint = 0x000000;
		/** 轴名称字体大小 */
		public var AxisNameFontSize:Number = 10;
		/** 轴名称字体加粗 */
		public var AxisNameFontBold:Boolean = true;
		/** X轴名称 */
		public var XAxisName:String = "X轴";
		/** Y轴名称 */
		public var YAxisName:String = "Y轴";
		/** 轴标签文本颜色 */
		public var AxisLabelTextColor:uint = 0x000000;
		/** 轴标签字体大小 */
		public var AxisLabelFontSize:Number = 10;
		/** 轴标签字体加粗 */
		public var AxisLabelFontBold:Boolean = false;
		/** Y轴标签值，“,”分隔  */
		public var YAxisLabelField:String = "0,100";
		/** X轴标签值 */
		public var XAxisLabelField:String = "";
		
		public function Canvas3DData()
		{
			super();
			Type = "Canvas3D";
		}
		
		protected override function CloneTo(clone:ChartData):void
		{
			var data:Canvas3DData = clone as Canvas3DData;
			data.CanvasWidth = CanvasWidth;
			data.CanvasHeight = CanvasHeight;
			data.CanvasBackgroundColor = CanvasBackgroundColor;
			data.CanvasBackgroundAlpha = CanvasBackgroundAlpha;
			data.CanvasBarColor = CanvasBarColor;
			data.CanvasBarAlpha = CanvasBarAlpha;
			data.CanvasLineThickness = CanvasLineThickness;
			data.CanvasLineColor = CanvasLineColor;
			data.CanvasLineAlpha = CanvasLineAlpha;
			data.AxisNameTextColor = AxisNameTextColor;
			data.AxisNameFontSize = AxisNameFontSize;
			data.AxisNameFontBold = AxisNameFontBold;
			data.XAxisName = XAxisName;
			data.YAxisName = YAxisName;
			data.AxisLabelTextColor = AxisLabelTextColor;
			data.AxisLabelFontSize = AxisLabelFontSize;
			data.AxisLabelFontBold = AxisLabelFontBold;
			data.YAxisLabelField = YAxisLabelField;
			data.XAxisLabelField = XAxisLabelField;

 			super.CloneTo(clone);
			
		}
		
		protected override function OnFromXML(xml:XML):void
		{
			if(xml.CanvasWidth.length())
				CanvasWidth = xml.CanvasWidth;
			if(xml.CanvasHeight.length())
				CanvasHeight = xml.CanvasHeight;
			if(xml.CanvasBackgroundColor.length())
				CanvasBackgroundColor = ReadColor(xml.CanvasBackgroundColor);
			if(xml.CanvasBackgroundAlpha.length())
				CanvasBackgroundAlpha = xml.CanvasBackgroundAlpha;
			if(xml.CanvasBarColor.length())
				CanvasBarColor = ReadColor(xml.CanvasBarColor);
			if(xml.CanvasBarAlpha.length())
				CanvasBarAlpha = xml.CanvasBarAlpha;
			if(xml.CanvasLineThickness.length())
				CanvasLineThickness = xml.CanvasLineThickness;
			if(xml.CanvasLineColor.length())
				CanvasLineColor = ReadColor(xml.CanvasLineColor);
			if(xml.CanvasLineAlpha.length())
				CanvasLineAlpha = xml.CanvasLineAlpha;
			if(xml.AxisNameTextColor.length())
				AxisNameTextColor = ReadColor(xml.AxisNameTextColor);
			if(xml.AxisNameFontSize.length())
				AxisNameFontSize = xml.AxisNameFontSize;
			if(xml.AxisNameFontBold.length())
				AxisNameFontBold = ReadBoolean(xml.AxisNameFontBold);
			if(xml.XAxisName.length())
				XAxisName = xml.XAxisName;
			if(xml.YAxisName.length())
				YAxisName = xml.YAxisName;
			if(xml.AxisLabelTextColor.length())
				AxisLabelTextColor = ReadColor(xml.AxisLabelTextColor);
			if(xml.AxisLabelFontSize.length())
				AxisLabelFontSize = xml.AxisLabelFontSize;
			if(xml.AxisLabelFontBold.length())
				AxisLabelFontBold = ReadBoolean(xml.AxisLabelFontBold);
			if(xml.YAxisLabelField.length())
				YAxisLabelField = xml.YAxisLabelField;
			if(xml.XAxisLabelField.length())
				XAxisLabelField = xml.XAxisLabelField;
			
			super.OnFromXML(xml);
		}
		
		protected override function OnToXML(xml:XmlWriter):void
		{
			if(CanvasWidth!=420)
				xml.Ele("CanvasWidth", CanvasWidth);
			if(CanvasHeight!=180)
				xml.Ele("CanvasHeight", CanvasHeight);
			if(CanvasBackgroundColor!=0xDDE3D5)
				xml.Ele("CanvasBackgroundColor", CanvasBackgroundColor);
			if(CanvasBackgroundAlpha!=1)
				xml.Ele("CanvasBackgroundAlpha", CanvasBackgroundAlpha);
			if(CanvasBarColor!=0xACBB99)
				xml.Ele("CanvasBarColor", CanvasBarColor);
			if(CanvasBarAlpha!=1)
				xml.Ele("CanvasBarAlpha", CanvasBarAlpha);
			if(CanvasLineThickness!=1)
				xml.Ele("CanvasLineThickness", CanvasLineThickness);
			if(CanvasLineColor!=0xACBB99)
				xml.Ele("CanvasLineColor", CanvasLineColor);
			if(CanvasLineAlpha!=1)
				xml.Ele("CanvasLineAlpha", CanvasLineAlpha);
			if(AxisNameTextColor!=0x000000)
				xml.Ele("AxisNameTextColor", AxisNameTextColor);
			if(AxisNameFontSize!=10)
				xml.Ele("AxisNameFontSize", AxisNameFontSize);
			if(!AxisNameFontBold)
				xml.Ele("AxisNameFontBold", AxisNameFontBold);
			if(XAxisName != "X轴")
				xml.Ele("XAxisName", XAxisName);
			if(YAxisName != "Y轴")
				xml.Ele("YAxisName", YAxisName);
			if(AxisLabelTextColor!=0x000000)
				xml.Ele("AxisLabelTextColor", AxisLabelTextColor);
			if(AxisLabelFontSize!=10)
				xml.Ele("AxisLabelFontSize", AxisLabelFontSize);
			if(AxisLabelFontBold)
				xml.Ele("AxisLabelFontBold", AxisLabelFontBold);
			if(YAxisLabelField != "0,100")
				xml.Ele("YAxisLabelField", YAxisLabelField);
			if(XAxisLabelField)
				xml.Ele("XAxisLabelField", XAxisLabelField);

			super.OnToXML(xml);
		}
	}
}