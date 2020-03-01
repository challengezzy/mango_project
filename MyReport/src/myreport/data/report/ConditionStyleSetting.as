/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


报表样式——条件样式。


*/
package myreport.data.report
{

	public final class ConditionStyleSetting extends ItemSetting
	{
		public var Condition:String = "";
		public var Style:StyleSetting = new StyleSetting();		
		
		override protected function Disposing():void
		{
			Style.Dispose();
			Style = null;
			super.Disposing();
		}
		
		public function ConditionStyleSetting(xml:XML = null)
		{
			super();
			FromXML(xml);
		}
		
		//================IClone====================
		override public function Clone():*
		{
			var clone:ConditionStyleSetting = new ConditionStyleSetting();
			clone.Condition = Condition;
			clone.Style = Style.Clone();
			
			return clone;
		}
		
		override public function FromXML(xml:XML):void
		{
			if (!xml)
				return;
			if(xml.Condition.length())
				Condition = xml.Condition;
			if(xml.Style.length())
				Style.FromXML(XML(xml.Style));
		}
		override public function ToXML():String
		{
			var result:String = "";
			result += "<ItemSetting type=\"ConditionStyleSetting\">";
			if(Condition)
				result += "<Condition>" + EscapeXML(Condition) + "</Condition>";
			result += Style.ToXML();
			result += "</ItemSetting>";
			return result;
		}

	}
}