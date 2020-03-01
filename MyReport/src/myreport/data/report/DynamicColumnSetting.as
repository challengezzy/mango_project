/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


报表样式——动态列配置。


*/
package myreport.data.report
{
	
	public final class DynamicColumnSetting extends ItemSetting
	{
		/** 动态列字段 */
		public var Field:String = "";
		/** 动态列显示文本，不设置时使用Field */
		public var Text:String = "";

		/** 动态列宽度，为0时则使用当前列宽度 */
		public var Width:Number = 2;
		/** 动态列表格头文本对齐 */
		public var HeaderTextAlign:String = "";
		/** 动态列表格尾文本对齐 */
		public var FooterTextAlign:String = "";
		/** 动态列表格主体文本对齐 */
		public var DetailTextAlign:String = "";		
		
		/** 【已过时不建议使用】动态列表格头文本(表达式)，设置时应用到所有动态表格头 */
		public var Header:String = "";
		/** 【已过时不建议使用】动态列表格尾文本(表达式)，设置时应用到所有动态表格尾 */
		public var Footer:String = "";
		/** 【已过时不建议使用】动态列表格主体文本(表达式)，设置时应用到所有动态表格主体 */
		public var Detail:String = "";

		override protected function Disposing():void
		{
			
			super.Disposing();
		}
		
		public function DynamicColumnSetting()
		{
			super();
		}
		//================IClone====================
		override public function Clone():*
		{
			var clone:DynamicColumnSetting = new DynamicColumnSetting();
 
			clone.Width = Width;
			clone.Header = Header;
			clone.Footer = Footer;
			clone.Detail = Detail;
			
			clone.HeaderTextAlign = HeaderTextAlign;
			clone.FooterTextAlign = FooterTextAlign;
			clone.DetailTextAlign = DetailTextAlign;
 
			return clone;
		}
		override public function FromXML(xml:XML):void
		{
			if (!xml)
				return;
			if(xml.Width.length())
				Width = xml.Width;
			if(xml.Header.length())
				Header = xml.Header;
			if(xml.Footer.length())
				Footer = xml.Footer;
			if(xml.Detail.length())
				Detail = xml.Detail;
			
			if(xml.HeaderTextAlign.length())
				HeaderTextAlign = xml.HeaderTextAlign;
			if(xml.FooterTextAlign.length())
				FooterTextAlign = xml.FooterTextAlign;
			if(xml.DetailTextAlign.length())
				DetailTextAlign = xml.DetailTextAlign;

		}
		override public function ToXML():String
		{
			var result:String = "";
			result += "<ItemSetting type=\"DynamicColumnSetting\">";
			if(Width != 2)
				result += "<Width>" + Width + "</Width>";
			
			if(Header)
				result += "<Header>" + Header + "</Header>";
			if(Footer)
				result += "<Footer>" + Footer + "</Footer>";
			if(Detail)
				result += "<Detail>" + Detail + "</Detail>";
			
			
			if(HeaderTextAlign)
				result += "<HeaderTextAlign>" + HeaderTextAlign + "</HeaderTextAlign>";
			if(FooterTextAlign)
				result += "<FooterTextAlign>" + FooterTextAlign + "</FooterTextAlign>";
			if(DetailTextAlign)
				result += "<DetailTextAlign>" + DetailTextAlign + "</DetailTextAlign>";
 
			result += "</ItemSetting>";
			return result;
		}
	}
}