/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


报表样式——表格单元格。


*/
package myreport.data.report
{
	import hlib.CloneUtil;
	import hlib.DisposeUtil;
	
	public final class TableCellSetting extends ItemSetting
	{
		/**
		 * 是否显示，呈现属性
		 */ 
		public var Visible:Boolean = true;
		/**
		 * 呈现属性
		 */ 
		public var ColumnIndex:int;
		/**
		 * 呈现属性
		 */ 
		public var RowIndex:int;

		public var Value:String = "";
		public var BindingValue:String = "";
		public var BindingColumn:String = "";
		public var Style:StyleSetting = new StyleSetting();
		public var Format:String = "";
		public var ColSpan:uint = 1;
		public var ColSpanExpression:String = "";
		public var RowSpan:uint = 1;
		public var CanGrow:Boolean = true;
		/**合并相同值*/
		public var MergeSameContent:Boolean = false;
		public var ShowNullValue:Boolean = true;
		//item => ConditionStyleSetting
		public var ConditionStyleSettings:Array = new Array();
		public var Control:ControlSetting = new ControlSetting();
		
		override protected function Disposing():void
		{
			DisposeUtil.Dispose(ConditionStyleSettings);
			ConditionStyleSettings = null;
			Style.Dispose();
			Style = null;
			Control.Dispose();
			Control = null;
			super.Disposing();
		}
		
		public function TableCellSetting(xml:XML = null)
		{
			super();
			FromXML(xml);
		}
		
		//================IClone====================
		override public function Clone():*
		{
			var clone:TableCellSetting = new TableCellSetting();
			clone.Value = Value;
			clone.BindingValue = BindingValue;
			clone.BindingColumn = BindingColumn;
			clone.Style = Style.Clone();
			clone.Format = Format;
			clone.ColSpan = ColSpan;
			clone.ColSpanExpression = ColSpanExpression;
			clone.RowSpan = RowSpan;
			clone.CanGrow = CanGrow;
			clone.ConditionStyleSettings = CloneUtil.Clone(ConditionStyleSettings);
			
			clone.Visible = Visible;
			clone.ColumnIndex = ColumnIndex;
			clone.RowIndex = RowIndex;
			clone.Control = Control.Clone();
			clone.MergeSameContent = MergeSameContent;
			clone.ShowNullValue = ShowNullValue;
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
			if(xml.BindingColumn.length())
				BindingColumn = xml.BindingColumn;
			if(xml.Format.length())
				Format = xml.Format;
			if(xml.ColSpan.length())
				ColSpan = xml.ColSpan;
			if(xml.ColSpanExpression.length())
				ColSpanExpression = xml.ColSpanExpression;
			if(xml.RowSpan.length())
				RowSpan = xml.RowSpan;			
			if(xml.CanGrow.length())
				CanGrow = ReadBoolean(xml.CanGrow);
			if(xml.MergeSameContent.length())
				MergeSameContent = ReadBoolean(xml.MergeSameContent);
			if(xml.ShowNullValue.length())
				ShowNullValue = ReadBoolean(xml.ShowNullValue);
			
			FillItemSettings(ConditionStyleSettings, xml, "ConditionStyleSettings");
			if(xml.Style.length())
				Style.FromXML(XML(xml.Style));			
			if(xml.Control.length())
				Control.FromXML(XML(xml.Control));
			
		}
		override public function ToXML():String
		{
			var result:String = "";
			result += "<ItemSetting type=\"TableCellSetting\">";
			if(Value)
				result += "<Value>" + EscapeXML(Value) + "</Value>";
			if(BindingValue)
				result += "<BindingValue>" + BindingValue + "</BindingValue>";
			if(BindingColumn)
				result += "<BindingColumn>" + BindingColumn + "</BindingColumn>";
			result += Style.ToXML();
			if(Format)
				result += "<Format>" + Format + "</Format>";
			if(ColSpan!=1)
				result += "<ColSpan>" + ColSpan + "</ColSpan>";
			if(ColSpanExpression)
				result += "<ColSpanExpression>" + EscapeXML(ColSpanExpression)+ "</ColSpanExpression>";
			if(RowSpan!=1)
				result += "<RowSpan>" + RowSpan + "</RowSpan>";
			if(!CanGrow)
				result += "<CanGrow>" + CanGrow + "</CanGrow>";
			if(MergeSameContent)
				result += "<MergeSameContent>" + MergeSameContent + "</MergeSameContent>";
			if(!ShowNullValue)
				result += "<ShowNullValue>" + ShowNullValue + "</ShowNullValue>";
			result += GetItemSettingsXML(ConditionStyleSettings, "ConditionStyleSettings");
			result += Control.ToXML();
			result += "</ItemSetting>";
			return result;
		}
		
	}
}