/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


报表样式——标题行。


*/
package myreport.data.report
{
	import hlib.CloneUtil;
	import hlib.DisposeUtil;
	
	public final class CaptionRowSetting extends ItemSetting
	{
		public var Height:Number = 0.75;
		//item => CaptionCellSetting
		public var CaptionCellSettings:Array = new Array();		
		
		public var LeftBorder:Boolean = false;
		public var RightBorder:Boolean = false;
		public var TopBorder:Boolean = false;
		public var BottomBorder:Boolean = false;
		
		public var SubReportRow:Boolean = false;
		public var SubReportStyle:String = "";
		public var SubReportParameters:String = "";
		public var SubReportTable:String = "";
		public var AdjustMainReport:Boolean = false;

		override protected function Disposing():void
		{
			DisposeUtil.Dispose(CaptionCellSettings);
			CaptionCellSettings = null;
			super.Disposing();
		}
		
		public function CaptionRowSetting(xml:XML = null)
		{
			super();
			FromXML(xml);
		}
		//================IClone====================
		override public function Clone():*
		{
			var clone:CaptionRowSetting = new CaptionRowSetting();
			clone.Height = Height;
			clone.LeftBorder = LeftBorder;
			clone.RightBorder = RightBorder;
			clone.TopBorder = TopBorder;
			clone.BottomBorder = BottomBorder;
			
			clone.SubReportRow = SubReportRow;
			clone.SubReportStyle = SubReportStyle;
			clone.SubReportParameters = SubReportParameters;
			clone.SubReportTable = SubReportTable;
			clone.AdjustMainReport = AdjustMainReport;
			
			clone.CaptionCellSettings = CloneUtil.Clone(CaptionCellSettings);
 
			return clone;
		}
		
		override public function FromXML(xml:XML):void
		{
			if (!xml)
				return;
			if(xml.Height.length())
				Height = xml.Height;
			
			if(xml.LeftBorder.length())
				LeftBorder = ReadBoolean(xml.LeftBorder);
			if(xml.RightBorder.length())
				RightBorder = ReadBoolean(xml.RightBorder);
			if(xml.TopBorder.length())
				TopBorder = ReadBoolean(xml.TopBorder);
			if(xml.BottomBorder.length())
				BottomBorder = ReadBoolean(xml.BottomBorder);
			
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
			
			FillItemSettings(CaptionCellSettings, xml, "CaptionCellSettings");
			
		}
		override public function ToXML():String
		{
			var result:String = "";
			result += "<ItemSetting type=\"CaptionRowSetting\">";
			
			if(Height!=0.75)
				result += "<Height>" + Height + "</Height>";
			
			if(LeftBorder)
				result += "<LeftBorder>" + LeftBorder + "</LeftBorder>";
			if(RightBorder)
				result += "<RightBorder>" + RightBorder + "</RightBorder>";
			if(TopBorder)
				result += "<TopBorder>" + TopBorder + "</TopBorder>";
			if(BottomBorder)
				result += "<BottomBorder>" + BottomBorder + "</BottomBorder>";
			
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
			
			result += GetItemSettingsXML(CaptionCellSettings, "CaptionCellSettings");
			
			result += "</ItemSetting>";
			return result;
		}
		
	}
}