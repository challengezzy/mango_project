/*
Copyright (c), Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


子报表行。


*/
package myreport.data.report
{
	import hlib.CloneUtil;
	import hlib.DisposeUtil;
	import hlib.Parser;
 
	public final class SubReportRowSetting extends ItemSetting
	{
		public var GroupEnabled:Boolean = false;
		public var GroupColumn:String = "";
		public var OrderColumn:String = "";
		public var GroupSize:int = 10;
		/**
		 * Item => TableRowSetting
		 */ 
		public var TableHeaderSettings:Array = new Array();
		/**
		 * Item => TableRowSetting
		 */ 
		public var TableDetailSettings:Array = new Array();
		/**
		 * Item => TableRowSetting
		 */ 
		public var TableFooterSettings:Array = new Array();

		public var SubReportParameters:String = "";
		public var SubReportTable:String = "";
		
		override protected function Disposing():void
		{
 
			DisposeUtil.Dispose(TableHeaderSettings);
			TableHeaderSettings = null;
			DisposeUtil.Dispose(TableDetailSettings);
			TableDetailSettings = null;
			DisposeUtil.Dispose(TableFooterSettings);
			TableFooterSettings = null;
	 
			super.Disposing();
		}
		
		public function SubReportRowSetting(xml:XML = null)
		{
			super();
			FromXML(xml);
		}
		
		//================IClone====================
		override public function Clone():*
		{
			var clone:SubReportRowSetting = new SubReportRowSetting(null);
  
			clone.TableHeaderSettings = CloneUtil.Clone(TableHeaderSettings);
			clone.TableDetailSettings = CloneUtil.Clone(TableDetailSettings);
			clone.TableFooterSettings = CloneUtil.Clone(TableFooterSettings);
			
			clone.SubReportParameters = SubReportParameters;
			clone.SubReportTable = SubReportTable;
 
			clone.GroupColumn = GroupColumn;
			clone.OrderColumn = OrderColumn;
			clone.GroupSize = GroupSize;
			clone.GroupEnabled = GroupEnabled;
			
			return clone;
		}
		
		override public function FromXML(xml:XML):void
		{
			if(!xml)
				return;
 
			FillItemSettings(TableHeaderSettings, xml, "TableHeaderSettings");
			FillItemSettings(TableDetailSettings, xml, "TableDetailSettings");
			FillItemSettings(TableFooterSettings, xml, "TableFooterSettings");
 
			if(xml.SubReportParameters.length())
				SubReportParameters = xml.SubReportParameters;
			if(xml.SubReportTable.length())
				SubReportTable = xml.SubReportTable;
 
			if(xml.GroupColumn.length())
				GroupColumn = xml.GroupColumn;
			if(xml.OrderColumn.length())
				OrderColumn = xml.OrderColumn;
			if(xml.GroupSize.length())
				GroupSize = xml.GroupSize;
			if(xml.GroupEnabled.length())
				GroupEnabled = ReadBoolean(xml.GroupEnabled);
		}
		
		override public function ToXML():String
		{
			var result:String = "";
			result += "<ItemSetting type=\"SubReportRowSetting\">";
			
 			if(SubReportParameters)
				result += "<SubReportParameters>" + SubReportParameters + "</SubReportParameters>";
			if(SubReportTable)
				result += "<SubReportTable>" + SubReportTable + "</SubReportTable>";
			
			if(GroupColumn)
				result += "<GroupColumn>" + GroupColumn + "</GroupColumn>";
			if(OrderColumn)
				result += "<OrderColumn>" + OrderColumn + "</OrderColumn>";
			if(GroupSize!=10)
				result += "<GroupSize>" + GroupSize + "</GroupSize>";
			if(GroupEnabled)
				result += "<GroupEnabled>" + GroupEnabled + "</GroupEnabled>";
			
			result += GetItemSettingsXML(TableHeaderSettings, "TableHeaderSettings");
			result += GetItemSettingsXML(TableDetailSettings, "TableDetailSettings");
			result += GetItemSettingsXML(TableFooterSettings, "TableFooterSettings");

			result += "</ItemSetting>";
			return result;
		}
  
	  
	}
}