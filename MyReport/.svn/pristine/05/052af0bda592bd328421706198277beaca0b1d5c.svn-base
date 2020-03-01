/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


报表样式——表格列。


*/
package myreport.data.report
{
	
	public final class TableColumnSetting extends ItemSetting
	{
 
		/** 动态列字段 */
		public var Field:String = "";
		/** 动态列显示文本，不设置时使用Field */
		public var Text:String = "";
		//====================
		public var Width:Number = 2;
		public var Dynamic:Boolean = false;
		public var DynamicColumnSettings:String = "";
 
		override protected function Disposing():void
		{
			
			super.Disposing();
		}
		
		public function TableColumnSetting(xml:XML = null)
		{
			super();
			FromXML(xml);
		}
		//================IClone====================
		override public function Clone():*
		{
			var clone:TableColumnSetting = new TableColumnSetting();
 
			clone.Width = Width;
			clone.Dynamic = Dynamic;
			clone.DynamicColumnSettings = DynamicColumnSettings;
	 
			return clone;
		}
		
		override public function FromXML(xml:XML):void
		{
			if (!xml)
				return;
 
			if(xml.Width.length())
				Width = xml.Width;
			if(xml.Dynamic.length())
				Dynamic = ReadBoolean(xml.Dynamic);
			if(xml.DynamicColumnSettings.length())
				DynamicColumnSettings = xml.DynamicColumnSettings;

		}
		override public function ToXML():String
		{
			var result:String = "";
			result += "<ItemSetting type=\"TableColumnSetting\">";
 
			if(Width!=2)
				result += "<Width>" + Width + "</Width>";
			if(Dynamic)
				result += "<Dynamic>" + Dynamic + "</Dynamic>";
			if(DynamicColumnSettings)
				result += "<DynamicColumnSettings>" + DynamicColumnSettings + "</DynamicColumnSettings>";
 
			result += "</ItemSetting>";
			return result;
		}
		
	}
}