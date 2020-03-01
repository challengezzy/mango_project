/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


报表样式——标题单元格。


*/
package myreport.data.report
{
	
	public final class CaptionCellSetting extends ItemSetting
	{
		public var Value:String = "";
		public var BindingValue:String = "";
		public var Width:Number = 2;
		public var Style:StyleSetting = new StyleSetting();
		public var Format:String = "";
		public var CanGrow:Boolean = true;
		public var Control:ControlSetting = new ControlSetting();
		
		override protected function Disposing():void
		{
			Style.Dispose();
			Style = null;
			Control.Dispose();
			Control = null;
			super.Disposing();
		}
		
		public function CaptionCellSetting(xml:XML = null)
		{
			super();
 
			Style.DefaultBorder = false;
			Style.Border = false;
			Style.LeftBorder = false;
			Style.RightBorder = false;
			Style.TopBorder = false;
			Style.BottomBorder = false;
 
			FromXML(xml);
		}
		
		//================IClone====================
		override public function Clone():*
		{
			var clone:CaptionCellSetting = new CaptionCellSetting();
			clone.Value = Value;
			clone.BindingValue = BindingValue;
			clone.Width = Width;
			clone.Style = Style.Clone();
			clone.Format = Format;
			clone.CanGrow = CanGrow;
			clone.Control = Control.Clone();
			
			return clone;
		}
		
		override public function FromXML(xml:XML):void
		{
			if (!xml)
				return;
			if(xml.Value.length())
				Value = xml.Value;
			if(xml.BindingValue.length())
				BindingValue = xml.BindingValue;
			if(xml.Width.length())
				Width = xml.Width;
			if(xml.Format.length())
				Format = xml.Format;
			if(xml.CanGrow.length())
				CanGrow = ReadBoolean(xml.CanGrow);
			if(xml.Style.length())
				Style.FromXML(XML(xml.Style));			
			if(xml.Control.length())
				Control.FromXML(XML(xml.Control));
		}
		
		override public function ToXML():String
		{
			var result:String = "";
			result += "<ItemSetting type=\"CaptionCellSetting\">";
			if(Value)
				result += "<Value>" + EscapeXML(Value) + "</Value>";
			if(BindingValue)
				result += "<BindingValue>" + BindingValue + "</BindingValue>";
			if(Width!=2)
				result += "<Width>" + Width + "</Width>";
			result += Style.ToXML();
			if(Format)
				result += "<Format>" + Format + "</Format>";
			if(!CanGrow)
				result += "<CanGrow>" + CanGrow + "</CanGrow>";
			result += Control.ToXML();
			result += "</ItemSetting>";
			return result;
		}
		
		
	}
}