/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


报表样式——分组。


*/
package myreport.data.report
{
	import hlib.CloneUtil;
	import hlib.DisposeUtil;
	
	public final class TableGroupSetting extends ItemSetting
	{
		public var Enabled:Boolean = false;
		public var PageBreakAtEnd:Boolean = true;
		public var GroupColumn:String = "";
		public var OrderColumn:String = "";
		public var GroupSize:int = 10;
		/**
		 * 适应分组大小，当数据条数不是分组大小的整数倍时，自动填充空行
		 */ 
		public var FitToGroupSize:Boolean = false;
		/**
		 * item => TableRowSetting
		 */ 
		public var TableGroupHeaderSettings:Array = new Array();
		/**
		 * item => TableRowSetting
		 */ 
		public var TableGroupFooterSettings:Array = new Array();	
		
		override protected function Disposing():void
		{
			DisposeUtil.Dispose(TableGroupHeaderSettings);
			DisposeUtil.Dispose(TableGroupFooterSettings);
			TableGroupHeaderSettings = null;
			TableGroupFooterSettings = null;
			super.Disposing();
		}
		
		public function TableGroupSetting(xml:XML = null)
		{
			super();
			FromXML(xml);
		}
		
		//================IClone====================
		override public function Clone():*
		{
			var clone:TableGroupSetting = new TableGroupSetting();
			clone.PageBreakAtEnd = PageBreakAtEnd;
			clone.GroupColumn = GroupColumn;
			clone.OrderColumn = OrderColumn;
			clone.GroupSize = GroupSize;
			clone.TableGroupHeaderSettings = CloneUtil.Clone(TableGroupHeaderSettings);
			clone.TableGroupFooterSettings = CloneUtil.Clone(TableGroupFooterSettings);
			clone.Enabled = Enabled;
			clone.FitToGroupSize = FitToGroupSize;
			return clone;
		}
		
		override public function FromXML(xml:XML):void
		{
			if (!xml)
				return;
			if(xml.GroupColumn.length())
				GroupColumn = xml.GroupColumn;
			if(xml.OrderColumn.length())
				OrderColumn = xml.OrderColumn;
			if(xml.GroupSize.length())
				GroupSize = xml.GroupSize;
			if(xml.PageBreakAtEnd.length())
				PageBreakAtEnd = ReadBoolean(xml.PageBreakAtEnd);
			Enabled = GroupColumn || OrderColumn;
			if(xml.Enabled.length())
				Enabled = ReadBoolean(xml.Enabled);
			if(xml.FitToGroupSize.length())
				FitToGroupSize = ReadBoolean(xml.FitToGroupSize);
			
			FillItemSettings(TableGroupHeaderSettings, xml, "TableGroupHeaderSettings");
			FillItemSettings(TableGroupFooterSettings, xml, "TableGroupFooterSettings");
			
		}
		
		override public function ToXML():String
		{
			var result:String = "";
			result += "<ItemSetting type=\"TableGroupSetting\">";
			if(GroupColumn)
				result += "<GroupColumn>" + GroupColumn + "</GroupColumn>";
			if(OrderColumn)
				result += "<OrderColumn>" + OrderColumn + "</OrderColumn>";
			if(GroupSize!=10)
				result += "<GroupSize>" + GroupSize + "</GroupSize>";
			if(!PageBreakAtEnd)
				result += "<PageBreakAtEnd>" + PageBreakAtEnd + "</PageBreakAtEnd>";
			if(Enabled)
				result += "<Enabled>" + Enabled + "</Enabled>";
			if(FitToGroupSize)
				result += "<FitToGroupSize>" + FitToGroupSize + "</FitToGroupSize>";
			result += GetItemSettingsXML(TableGroupHeaderSettings, "TableGroupHeaderSettings");
			result += GetItemSettingsXML(TableGroupFooterSettings, "TableGroupFooterSettings");
			result += "</ItemSetting>";
			return result;
		}
	}
}