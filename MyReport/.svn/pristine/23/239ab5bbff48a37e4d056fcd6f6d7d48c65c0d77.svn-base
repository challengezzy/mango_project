/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


报表样式——样式。


*/
package myreport.data.report
{
	
	public final class StyleSetting extends ItemSetting
	{
		/**
		 * 默认边框，用于控制XML输出
		 */ 
		internal var DefaultBorder:Boolean = true;
		
		public var TextAlign:String = "left";
		public var VerticalAlign:String = "middle";
		public var TextColor:uint = 0x000000;
		public var FontName:String = "SimSun";
		public var FontSize:Number = 11;
		public var FontBold:Boolean = false;
		public var Border:Boolean = true;
		public var TextUnderline:Boolean = false;
		public var Underline:Boolean = false;
		public var WordWrap:Boolean = false;
		
		public var LeftBorder:Boolean = true;
		public var RightBorder:Boolean = true;
		public var TopBorder:Boolean = true;
		public var BottomBorder:Boolean = true;

		public var LetterSpacing:Number = 0;
		public var Leading:Number = 0;
		
		public var LeftBorderStyle:String = "solid";
		public var RightBorderStyle:String = "solid";
		public var TopBorderStyle:String = "solid";
		public var BottomBorderStyle:String = "solid";
		
		override protected function Disposing():void
		{
			
			super.Disposing();
		}
		
		public function StyleSetting(xml:XML = null)
		{
			super();
			FromXML(xml);
		}
		
		//================IClone====================
		override public function Clone():*
		{
			var clone:StyleSetting = new StyleSetting();
			clone.DefaultBorder = DefaultBorder;
			clone.TextAlign = TextAlign;
			clone.VerticalAlign = VerticalAlign;
			clone.TextColor = TextColor;
			clone.FontName = FontName;
			clone.FontSize = FontSize;
			clone.FontBold = FontBold;
			clone.Border = Border;
			clone.Underline = Underline;
			clone.TextUnderline = TextUnderline;
			clone.WordWrap = WordWrap;
			
			clone.LeftBorder = LeftBorder;
			clone.RightBorder = RightBorder;
			clone.TopBorder = TopBorder;
			clone.BottomBorder = BottomBorder;
			
			clone.LetterSpacing = LetterSpacing;
			clone.Leading = Leading;
			
			clone.LeftBorderStyle = LeftBorderStyle;
			clone.RightBorderStyle = RightBorderStyle;
			clone.TopBorderStyle = TopBorderStyle;
			clone.BottomBorderStyle = BottomBorderStyle;
			
			return clone;
		}
		
		override public function FromXML(xml:XML):void
		{
			if (!xml)
				return;
			
			if(xml.TextAlign.length())
				TextAlign = String(xml.TextAlign).toLowerCase();
			if(xml.VerticalAlign.length())
				VerticalAlign = String(xml.VerticalAlign).toLowerCase();
			if(xml.TextColor.length())
				TextColor = ReadColor(xml.TextColor);
			if(xml.FontName.length())
				FontName = xml.FontName;
			if(xml.FontSize.length())
				FontSize = ReadFontSize(xml.FontSize);
			if(xml.FontBold.length())
				FontBold = ReadBoolean(xml.FontBold);
			if(xml.Border.length())
				Border = ReadBoolean(xml.Border);
			if(xml.Underline.length())
				Underline = ReadBoolean(xml.Underline);
			if(xml.TextUnderline.length())
				TextUnderline = ReadBoolean(xml.TextUnderline);
			if(xml.WordWrap.length())
				WordWrap = ReadBoolean(xml.WordWrap);
			
			if(xml.LeftBorder.length())
				LeftBorder = ReadBoolean(xml.LeftBorder);
			if(xml.RightBorder.length())
				RightBorder = ReadBoolean(xml.RightBorder);
			if(xml.TopBorder.length())
				TopBorder = ReadBoolean(xml.TopBorder);
			if(xml.BottomBorder.length())
				BottomBorder = ReadBoolean(xml.BottomBorder);
			
			if(xml.LetterSpacing.length())
				LetterSpacing = Number(xml.LetterSpacing);
			if(xml.Leading.length())
				Leading = Number(xml.Leading);
			
			if(xml.LeftBorderStyle.length())
				LeftBorderStyle = String(xml.LeftBorderStyle).toLowerCase();
			if(xml.RightBorderStyle.length())
				RightBorderStyle = String(xml.RightBorderStyle).toLowerCase();
			if(xml.TopBorderStyle.length())
				TopBorderStyle = String(xml.TopBorderStyle).toLowerCase();
			if(xml.BottomBorderStyle.length())
				BottomBorderStyle = String(xml.BottomBorderStyle).toLowerCase();
		}
		
		private function get IsNotDefault():Boolean
		{
			return TextAlign!="left"||
				VerticalAlign!="middle"||
				TextColor!=0||
				FontName.toLowerCase() != "simsun"||
				FontSize!=11||
				FontBold||
				Border!=DefaultBorder||
				Underline||
				TextUnderline||
				LeftBorder!=DefaultBorder||
				RightBorder!=DefaultBorder||
				TopBorder!=DefaultBorder||
				BottomBorder!=DefaultBorder||
				LetterSpacing||
				Leading||
				LeftBorderStyle!="solid"||
				RightBorderStyle!="solid"||
				TopBorderStyle!="solid"||
				BottomBorderStyle!="solid"||
				WordWrap;
		}
		
		override public function ToXML():String
		{
			var result:String = "";
			
			if(IsNotDefault)
			{
				result += "<Style>";
				if(TextAlign!="left")
					result += "<TextAlign>" + TextAlign + "</TextAlign>";
				if(VerticalAlign!="middle")
					result += "<VerticalAlign>" + VerticalAlign + "</VerticalAlign>";
				if(TextColor!=0)
					result += "<TextColor>" + ToHexColor(TextColor) + "</TextColor>";
				if(FontName.toLowerCase() != "simsun")
					result += "<FontName>" + FontName + "</FontName>";
				if(FontSize!=11)
					result += "<FontSize>" + FontSize + "</FontSize>";
				if(FontBold!=false)
					result += "<FontBold>" + FontBold + "</FontBold>";
				if(Border!=DefaultBorder)
					result += "<Border>" + Border + "</Border>";
				if(Underline)
					result += "<Underline>" + Underline + "</Underline>";
				if(TextUnderline)
					result += "<TextUnderline>" + TextUnderline + "</TextUnderline>";
				if(WordWrap)
					result += "<WordWrap>" + WordWrap + "</WordWrap>";
				
				if(LeftBorder!=DefaultBorder)
					result += "<LeftBorder>" + LeftBorder + "</LeftBorder>";
				if(RightBorder!=DefaultBorder)
					result += "<RightBorder>" + RightBorder + "</RightBorder>";
				if(TopBorder!=DefaultBorder)
					result += "<TopBorder>" + TopBorder + "</TopBorder>";
				if(BottomBorder!=DefaultBorder)
					result += "<BottomBorder>" + BottomBorder + "</BottomBorder>";
				
				if(LetterSpacing)
					result += "<LetterSpacing>" + LetterSpacing + "</LetterSpacing>";
				if(Leading)
					result += "<Leading>" + Leading + "</Leading>";
				
				if(LeftBorderStyle!="solid")
					result += "<LeftBorderStyle>" + LeftBorderStyle + "</LeftBorderStyle>";
				if(RightBorderStyle!="solid")
					result += "<RightBorderStyle>" + RightBorderStyle + "</RightBorderStyle>";
				if(TopBorderStyle!="solid")
					result += "<TopBorderStyle>" + TopBorderStyle + "</TopBorderStyle>";
				if(BottomBorderStyle!="solid")
					result += "<BottomBorderStyle>" + BottomBorderStyle + "</BottomBorderStyle>";
				
				result += "</Style>";
			}
			return result;
		}
		
		
	}
}