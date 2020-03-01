/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


报表样式——表格行。


*/
package myreport.data.report
{
	import hlib.CloneUtil;
	import hlib.DisposeUtil;
	
	public final class TableRowSetting extends ItemSetting
	{
		public var Height:Number = 0.75;
		//item => TableCellSetting
		public var TableCellSettings:Array = new Array();		
		
		public var SubReportRow:Boolean = false;
		public var SubReportStyle:String = "";
		public var SubReportParameters:String = "";
		public var SubReportTable:String = "";
		public var AdjustMainReport:Boolean = false;
		
		override protected function Disposing():void
		{
			DisposeUtil.Dispose(TableCellSettings);
			TableCellSettings = null;
			super.Disposing();
		}
		
		public function TableRowSetting(xml:XML = null)
		{
			super();
			FromXML(xml);
		}
		
		//================IClone====================
		override public function Clone():*
		{
			var clone:TableRowSetting = new TableRowSetting();
			clone.Height = Height;
			clone.SubReportRow = SubReportRow;
			clone.SubReportStyle = SubReportStyle;
			clone.SubReportParameters = SubReportParameters;
			clone.SubReportTable = SubReportTable;
			clone.AdjustMainReport = AdjustMainReport;
			
			clone.TableCellSettings = CloneUtil.Clone(TableCellSettings);
 
			return clone;
		}
		
		override public function FromXML(xml:XML):void
		{
			if (!xml)
				return;
			if(xml.Height.length())
				Height = xml.Height;
			
			if(xml.SubReportRow.length())
				SubReportRow = ReadBoolean(xml.SubReportRow);
			if(xml.SubReportStyle.length())
				SubReportStyle = xml.SubReportStyle;
			if(xml.SubReportParameters.length())
				SubReportParameters = xml.SubReportParameters;
			if(xml.SubReportTable.length())
				SubReportTable = xml.SubReportTable;
			if(xml.AdjustMainReport.length())
				AdjustMainReport = ReadBoolean(xml.AdjustMainReport);
			
			FillItemSettings(TableCellSettings, xml, "TableCellSettings");
			
		}
		override public function ToXML():String
		{
			var result:String = "";
			result += "<ItemSetting type=\"TableRowSetting\">";
			if(Height != 0.75)
				result += "<Height>" + Height + "</Height>";
			
			if(SubReportRow)
				result += "<SubReportRow>" + SubReportRow + "</SubReportRow>";
			if(SubReportStyle)
				result += "<SubReportStyle>" + SubReportStyle + "</SubReportStyle>";
			if(SubReportParameters)
				result += "<SubReportParameters>" + SubReportParameters + "</SubReportParameters>";
			if(SubReportTable)
				result += "<SubReportTable>" + SubReportTable + "</SubReportTable>";
			if(AdjustMainReport)
				result += "<AdjustMainReport>" + AdjustMainReport + "</AdjustMainReport>";
			
			result += GetItemSettingsXML(TableCellSettings, "TableCellSettings");
			result += "</ItemSetting>";
			return result;
		}
	}
}