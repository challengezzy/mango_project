/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


报表样式。


*/
package myreport.data.report
{
	import hlib.CloneUtil;
	import hlib.DisposeUtil;
	import hlib.Parser;
	
	import mx.collections.ArrayCollection;
	
	public final class ReportSettings extends ItemSetting
	{

		public var PageWidth:Number = 21;
		public var PageHeight:Number = 29.7;
		public var LeftMargin:Number = 1.5;
		public var RightMargin:Number = 1.5;
		public var TopMargin:Number = 2.5;
		public var BottomMargin:Number = 2.5;
		public var Unit:String = "cm";
		public var PageHeaderRepeat:Boolean = true;
		public var PageFooterRepeat:Boolean = true;
		public var TableHeaderRepeat:Boolean = false;
		public var TableFooterRepeat:Boolean = false;
 
		public var ShowPageNumber:Boolean = false;
		/**
		 * 页码格式
		 */ 
		public var PageNumberFormat:String = "{0}";
		/**
		 * 报表高度自动递增
		 */ 
		public var CanGrow:Boolean = false;
		/**
		 * 报表宽度自动递增
		 */ 
		public var AutoWidth:Boolean = false;
		/**
		 * 适应打印方向
		 */ 
		public var AdjustPrintDirection:Boolean = false;
		/**
		 * 适应打印大小
		 */ 
		public var FitToPrintSize:Boolean = false;

		/**
		 * 报表尾显示在页底
		 */ 
		public var PageFooterShowAtEnd:Boolean = true;
		/**
		 * Item => CaptionRowSetting
		 */ 
		public var PageHeaderSettings:Array = new Array();
		/**
		 * Item => CaptionRowSetting
		 */ 
		public var PageFooterSettings:Array = new Array();
		/**
		 * Item => TableColumnSetting
		 */ 
		public var TableColumnSettings:Array = new Array();
		/**
		 * Item => TableRowSetting, SubReportRowSetting
		 */ 
		public var TableHeaderSettings:Array = new Array();
		/**
		 * Item => TableRowSetting, SubReportRowSetting
		 */ 
		public var TableDetailSettings:Array = new Array();
		/**
		 * Item => TableRowSetting, SubReportRowSetting
		 */ 
		public var TableFooterSettings:Array = new Array();
		/**
		 * item => TableGroupSetting
		 */ 
		public var TableGroupSettings:Array = new Array();
		
		public var TableData:ArrayCollection;
		public var ParameterData:Object;
		
		override protected function Disposing():void
		{
			DisposeUtil.Dispose(PageHeaderSettings);
			PageHeaderSettings = null;
			DisposeUtil.Dispose(PageFooterSettings);
			PageFooterSettings = null;
			DisposeUtil.Dispose(TableColumnSettings);
			TableColumnSettings = null;
			DisposeUtil.Dispose(TableHeaderSettings);
			TableHeaderSettings = null;
			DisposeUtil.Dispose(TableDetailSettings);
			TableDetailSettings = null;
			DisposeUtil.Dispose(TableFooterSettings);
			TableFooterSettings = null;
			DisposeUtil.Dispose(TableGroupSettings);
			TableGroupSettings = null;
			ParameterData = null;
			TableData = null;
			super.Disposing();
		}
		
		public function ReportSettings(xml:XML = null)
		{
			super();
			FromXML(xml);
			if(TableGroupSettings.length == 0)
				TableGroupSettings.push(new TableGroupSetting());
		}
		
		//================IClone====================
		override public function Clone():*
		{
			var clone:ReportSettings = new ReportSettings();
			clone.PageWidth = PageWidth;
			clone.PageHeight = PageHeight;
			clone.LeftMargin = LeftMargin;
			clone.RightMargin = RightMargin;
			clone.TopMargin = TopMargin;
			clone.BottomMargin = BottomMargin;
			clone.Unit = Unit;
			clone.PageHeaderRepeat = PageHeaderRepeat;
			clone.PageFooterRepeat = PageFooterRepeat;
			clone.TableHeaderRepeat = TableHeaderRepeat;
			clone.TableFooterRepeat = TableFooterRepeat;
 
			clone.ShowPageNumber = ShowPageNumber;
			clone.PageNumberFormat = PageNumberFormat;
			clone.CanGrow = CanGrow;
			clone.AutoWidth = AutoWidth;
			clone.AdjustPrintDirection = AdjustPrintDirection;
			clone.FitToPrintSize = FitToPrintSize;
			clone.PageFooterShowAtEnd = PageFooterShowAtEnd;
			clone.PageHeaderSettings = CloneUtil.Clone(PageHeaderSettings);
			clone.PageFooterSettings = CloneUtil.Clone(PageFooterSettings);
			clone.TableColumnSettings = CloneUtil.Clone(TableColumnSettings);
			clone.TableHeaderSettings = CloneUtil.Clone(TableHeaderSettings);
			clone.TableDetailSettings = CloneUtil.Clone(TableDetailSettings);
			clone.TableFooterSettings = CloneUtil.Clone(TableFooterSettings);
			clone.TableGroupSettings = CloneUtil.Clone(TableGroupSettings);
			
			return clone;
		}
		
		override public function FromXML(xml:XML):void
		{
			if (!xml)
				return;
			if(xml.PageWidth.length())
				PageWidth = xml.PageWidth;
			if(xml.PageHeight.length())
				PageHeight = xml.PageHeight;
			if(xml.LeftMargin.length())
				LeftMargin = xml.LeftMargin;
			if(xml.RightMargin.length())
				RightMargin = xml.RightMargin;
			if(xml.TopMargin.length())
				TopMargin = xml.TopMargin;
			if(xml.BottomMargin.length())
				BottomMargin = xml.BottomMargin;
			if(xml.Unit.length())
				Unit = xml.Unit;
			if(xml.PageHeaderRepeat.length())
				PageHeaderRepeat = ReadBoolean(xml.PageHeaderRepeat);
			if(xml.PageFooterRepeat.length())
				PageFooterRepeat = ReadBoolean(xml.PageFooterRepeat);
			if(xml.TableHeaderRepeat.length())
				TableHeaderRepeat = ReadBoolean(xml.TableHeaderRepeat);
			if(xml.TableFooterRepeat.length())
				TableFooterRepeat = ReadBoolean(xml.TableFooterRepeat);
 
			
			if(xml.ShowPageNumber.length())
				ShowPageNumber = ReadBoolean(xml.ShowPageNumber);
			if(xml.PageNumberFormat.length())
				PageNumberFormat = xml.PageNumberFormat;
			if(xml.CanGrow.length())
				CanGrow = ReadBoolean(xml.CanGrow);
			if(xml.AutoWidth.length())
				AutoWidth = ReadBoolean(xml.AutoWidth);
			if(xml.AdjustPrintDirection.length())
				AdjustPrintDirection = ReadBoolean(xml.AdjustPrintDirection);
			if(xml.FitToPrintSize.length())
				FitToPrintSize = ReadBoolean(xml.FitToPrintSize);
			if(xml.PageFooterShowAtEnd.length())
				PageFooterShowAtEnd = ReadBoolean(xml.PageFooterShowAtEnd);
			
			FillItemSettings(PageHeaderSettings, xml, "PageHeaderSettings");
			FillItemSettings(PageFooterSettings, xml, "PageFooterSettings");
			FillItemSettings(TableColumnSettings, xml, "TableColumnSettings");
			FillItemSettings(TableHeaderSettings, xml, "TableHeaderSettings");
			FillItemSettings(TableDetailSettings, xml, "TableDetailsSettings");//兼容代码
			FillItemSettings(TableDetailSettings, xml, "TableDetailSettings");
			FillItemSettings(TableFooterSettings, xml, "TableFooterSettings");
			FillItemSettings(TableGroupSettings, xml, "TableGroupSettings");
			
			AdjustAllTalbeRows();
		}
		
		override public function ToXML():String
		{
			var result:String = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
			result += "<ReportSettings version=\"1.1\">";
			if(PageWidth!=21)
				result += "<PageWidth>" + PageWidth + "</PageWidth>";
			if(PageHeight!=29.7)
				result += "<PageHeight>" + PageHeight + "</PageHeight>";
			if(LeftMargin!=1.5)
				result += "<LeftMargin>" + LeftMargin + "</LeftMargin>";
			if(RightMargin!=1.5)
				result += "<RightMargin>" + RightMargin + "</RightMargin>";
			if(TopMargin!=2.5)
				result += "<TopMargin>" + TopMargin + "</TopMargin>";
			if(BottomMargin!=2.5)
				result += "<BottomMargin>" + BottomMargin + "</BottomMargin>";
			if(Unit!="cm")
				result += "<Unit>" + Unit + "</Unit>";
			if(!PageHeaderRepeat)
				result += "<PageHeaderRepeat>" + PageHeaderRepeat + "</PageHeaderRepeat>";
			if(!PageFooterRepeat)
				result += "<PageFooterRepeat>" + PageFooterRepeat + "</PageFooterRepeat>";
			if(TableHeaderRepeat)
				result += "<TableHeaderRepeat>" + TableHeaderRepeat + "</TableHeaderRepeat>";
			if(TableFooterRepeat)
				result += "<TableFooterRepeat>" + TableFooterRepeat + "</TableFooterRepeat>";
 
			if(ShowPageNumber)
				result += "<ShowPageNumber>" + ShowPageNumber + "</ShowPageNumber>";
			if(PageNumberFormat!="{0}")
				result += "<PageNumberFormat>" + EscapeXML(PageNumberFormat) + "</PageNumberFormat>";
			if(CanGrow)
				result += "<CanGrow>" + CanGrow + "</CanGrow>";
			if(AutoWidth)
				result += "<AutoWidth>" + AutoWidth + "</AutoWidth>";
			if(AdjustPrintDirection)
				result += "<AdjustPrintDirection>" + AdjustPrintDirection + "</AdjustPrintDirection>";
			if(FitToPrintSize)
				result += "<FitToPrintSize>" + FitToPrintSize + "</FitToPrintSize>";
			if(!PageFooterShowAtEnd)
				result += "<PageFooterShowAtEnd>" + PageFooterShowAtEnd + "</PageFooterShowAtEnd>";

			result += GetItemSettingsXML(PageHeaderSettings, "PageHeaderSettings");
			result += GetItemSettingsXML(PageFooterSettings, "PageFooterSettings");
			result += GetItemSettingsXML(TableColumnSettings, "TableColumnSettings");
			result += GetItemSettingsXML(TableHeaderSettings, "TableHeaderSettings");
			result += GetItemSettingsXML(TableDetailSettings, "TableDetailSettings");
			result += GetItemSettingsXML(TableFooterSettings, "TableFooterSettings");
			result += GetItemSettingsXML(TableGroupSettings, "TableGroupSettings");
			
			result += "</ReportSettings>";
			return result;
		}
		
		public function get ClientWidth():Number
		{
			return PageWidth - LeftMargin - RightMargin;
		}
		
		public function get Group():TableGroupSetting
		{
			if (TableGroupSettings.length > 0)
			{
				var group:TableGroupSetting = TableGroupSettings[0] as TableGroupSetting;
				if (group && group.Enabled)
				{
					return group;
				}
			}
			return null;
		}
		//==========================
		private function AdjustAllTalbeRows():void
		{
			AdjustTableRows(TableHeaderSettings);
			AdjustTableRows(TableDetailSettings);
			AdjustTableRows(TableFooterSettings);
			for each(var group:TableGroupSetting in TableGroupSettings)
			{
				AdjustTableRows(group.TableGroupHeaderSettings);
				AdjustTableRows(group.TableGroupFooterSettings);
			}
		}
		
		private function AdjustTableRows(rows:Array):void
		{
			for each(var item:Object in rows)
			{
				if(item is 	TableRowSetting)
				{
					var row:TableRowSetting = TableRowSetting(item);
					AdjustTableCells(row.TableCellSettings);
				}
				else if(item is SubReportRowSetting)
				{
					var sub:SubReportRowSetting = SubReportRowSetting(item);
					AdjustTableRows(sub.TableHeaderSettings);
					AdjustTableRows(sub.TableDetailSettings);
					AdjustTableRows(sub.TableFooterSettings);
				}
			}
		}
		/**
		 * 修正单元格数量
		 */
		private function AdjustTableCells(cells:Array):void
		{
			var count:int = cells.length - TableColumnSettings.length;
			var cell:TableCellSetting;
			
			if(count>0)
			{
				while(count>0)
				{
					cell = cells.pop();
					if(cell.ColSpan>1)
					{
						cell.ColSpan--;
						cells.push(cell);
					}
					count--;
				}
			}
			else if(count<0)
			{
				count = -1*count;
				var i:int=0;
				while(count>0)
				{
					var num:int=0;
					if(i>=cells.length)
					{
						num = count;
					}
					else
					{
						cell = cells[i];
						if(cell.ColSpan>1)
							num = Math.min(cell.ColSpan-1, count);
					}
					
					while(num>0)
					{
						i = Math.min(cells.length, i+1);
						cells.splice(i, 0, new TableCellSetting());
						count--;
						num--;
						i++;
					}
					
					i++;
				}
				
			}
			
		}
		//==========================
		public function SetUnit(value:String):void
		{
			if(Unit == value)
				return;
			
			if(value == "px")
			{
				ConvertToPixel();
			}
			else if(value == "cm")
			{
				ConvertToCM();
			}
			Unit = value;
		}
		
		private function ConvertToPixel():void
		{
			PageHeight = Parser.GetPixel(PageHeight, Unit);
			PageWidth = Parser.GetPixel(PageWidth, Unit);
			LeftMargin = Parser.GetPixel(LeftMargin, Unit);
			RightMargin = Parser.GetPixel(RightMargin, Unit);
			TopMargin = Parser.GetPixel(TopMargin, Unit);
			BottomMargin = Parser.GetPixel(BottomMargin, Unit);
			for each(var column:TableColumnSetting in TableColumnSettings)
			{
				column.Width = Parser.GetPixel(column.Width, Unit);
			}
			ConvertRowsToPixel(PageHeaderSettings);
			ConvertRowsToPixel(PageFooterSettings);
			ConvertRowsToPixel(TableHeaderSettings);
			ConvertRowsToPixel(TableDetailSettings);
			ConvertRowsToPixel(TableFooterSettings);
			for each(var group:TableGroupSetting in TableGroupSettings)
			{
				ConvertRowsToPixel(group.TableGroupHeaderSettings);
				ConvertRowsToPixel(group.TableGroupFooterSettings);
			}
		}
		private function ConvertRowsToPixel(rows:Array):void
		{
			for each(var row:Object in rows)
			{
				if(row is CaptionRowSetting)
				{
					var captionRow:CaptionRowSetting = CaptionRowSetting(row);
					captionRow.Height = Parser.GetPixel(captionRow.Height, Unit);
					for each(var captionCell:CaptionCellSetting in captionRow.CaptionCellSettings)
					{
						captionCell.Width = Parser.GetPixel(captionCell.Width, Unit);
					}
				}
				else if(row is TableRowSetting)
				{
					var tableRow:TableRowSetting = TableRowSetting(row);
					tableRow.Height = Parser.GetPixel(tableRow.Height, Unit);
				}
				else if(row is SubReportRowSetting)
				{
					var sub:SubReportRowSetting = SubReportRowSetting(row);
					ConvertRowsToPixel(sub.TableHeaderSettings);
					ConvertRowsToPixel(sub.TableDetailSettings);
					ConvertRowsToPixel(sub.TableFooterSettings);
				}
			}
		}
		private function ConvertToCM():void
		{
			PageHeight = Parser.GetUnit(PageHeight, Unit);
			PageWidth = Parser.GetUnit(PageWidth, Unit);
			LeftMargin = Parser.GetUnit(LeftMargin, Unit);
			RightMargin = Parser.GetUnit(RightMargin, Unit);
			TopMargin = Parser.GetUnit(TopMargin, Unit);
			BottomMargin = Parser.GetUnit(BottomMargin, Unit);
			for each(var column:TableColumnSetting in TableColumnSettings)
			{
				column.Width = Parser.GetUnit(column.Width, Unit);
			}
			ConvertRowsToCM(PageHeaderSettings);
			ConvertRowsToCM(PageFooterSettings);
			ConvertRowsToCM(TableHeaderSettings);
			ConvertRowsToCM(TableDetailSettings);
			ConvertRowsToCM(TableFooterSettings);
			for each(var group:TableGroupSetting in TableGroupSettings)
			{
				ConvertRowsToCM(group.TableGroupHeaderSettings);
				ConvertRowsToCM(group.TableGroupFooterSettings);
			}
		}
		private function ConvertRowsToCM(rows:Array):void
		{
			for each(var row:Object in rows)
			{
				if(row is CaptionRowSetting)
				{
					var captionRow:CaptionRowSetting = CaptionRowSetting(row);
					captionRow.Height = Parser.GetUnit(captionRow.Height, Unit);
					for each(var captionCell:CaptionCellSetting in captionRow.CaptionCellSettings)
					{
						captionCell.Width = Parser.GetUnit(captionCell.Width, Unit);
					}
				}
				else if(row is TableRowSetting)
				{
					var tableRow:TableRowSetting = TableRowSetting(row);
					tableRow.Height = Parser.GetUnit(tableRow.Height, Unit);
				}
				else if(row is SubReportRowSetting)
				{
					var sub:SubReportRowSetting = SubReportRowSetting(row);
					ConvertRowsToCM(sub.TableHeaderSettings);
					ConvertRowsToCM(sub.TableDetailSettings);
					ConvertRowsToCM(sub.TableFooterSettings);
				}
			}
		}
		//==========================
		public function RefreshAllLayout():void
		{
			RefreshRowsLayout(TableHeaderSettings);
			RefreshRowsLayout(TableDetailSettings);
			RefreshRowsLayout(TableFooterSettings);
			for each(var group:TableGroupSetting in TableGroupSettings)
			{
				RefreshRowsLayout(group.TableGroupHeaderSettings);
				RefreshRowsLayout(group.TableGroupFooterSettings);
			}
		}
		/**
		 * 设置单元格Visible，RowIndex，ColumnIndex
		 */ 
		public function RefreshRowsLayout(rows:Array):void
		{
			var c:int, r:int;
			var cells:Array = new Array();
			var cell:TableCellSetting;
			var row:TableRowSetting
			for(r=0;r<rows.length;r++)
			{
				row = rows[r] as TableRowSetting;
				if(!row)
				{
					if(rows[r] is SubReportRowSetting)
					{
						var sub:SubReportRowSetting = rows[r];
						RefreshRowsLayout(sub.TableHeaderSettings);
						RefreshRowsLayout(sub.TableDetailSettings);
						RefreshRowsLayout(sub.TableFooterSettings);
					}
					continue;
				}
				for(c=0;c<TableColumnSettings.length;c++)
				{
					cell = row.TableCellSettings[c];
					cell.Visible = true;
					cell.RowIndex = r;
					cell.ColumnIndex = c;
					if(cell.RowSpan>1 || cell.ColSpan>1)
						cells.push(cell);
				}
			}
			for each(cell in cells)
			{
				if(!cell.Visible)
					continue;
				var maxCol:uint = Math.min(cell.ColumnIndex + cell.ColSpan, TableColumnSettings.length);
				var maxRow:uint = Math.min(cell.RowIndex + cell.RowSpan, rows.length);
				
				for(c=cell.ColumnIndex; c<maxCol; c++)
				{
					for(r=cell.RowIndex; r<maxRow; r++)
					{
						if(c!=cell.ColumnIndex || r!=cell.RowIndex)
						{
							row = rows[r] as TableRowSetting;
							if(!row)
								continue;
							var hide:TableCellSetting = row.TableCellSettings[c];
							hide.Visible = false;
							//hide.ColSpan = 1;
						}
					}
				}
			}
		}
		/**
		 * 获取列宽度，单位：px
		 */ 
		public function GetColumnWidth(c:int):Number
		{
			var col:TableColumnSetting = TableColumnSettings[c];
			if(Unit == "px")
				return col.Width;
			else
				return Parser.GetPixel(col.Width, Unit);
		}
		/**
		 * 获取列合并单元格宽度，单位：px
		 */ 
		public function GetColSpanWidth(c:int, colSpan:uint):Number
		{
			var start:int = c;
			var end:int = start + colSpan;
			var cellWidth:Number = 0;
			for (var i:int = start; i < end; i++)
			{
				if(i>=0 && i<TableColumnSettings.length)
				{
					var col:TableColumnSetting = TableColumnSettings[i];
					if(col)
					{
						if(Unit == "px")
							cellWidth += col.Width;
						else
							cellWidth += Parser.GetPixel(col.Width, Unit);
					}
				}
			}
			return cellWidth;
		}
		/**
		 * 获取行高度，单位：px
		 */ 
		public function GetRowHeight(rows:Array, r:int):Number
		{
			var row:TableRowSetting = rows[r];
			if(Unit == "px")
				return row.Height;
			else
				return Parser.GetPixel(row.Height, Unit);
		}
		/**
		 * 获取行合并单元格宽度，单位：px
		 */ 
		public function GetRowSpanHeight(rows:Array, r:int, rowSpan:uint):Number
		{
			var start:int = r;
			var end:int = start + rowSpan;
			var rowHeight:Number = 0;
			for (var i:int = start; i < end; i++)
			{
				if(i>=0 && i<rows.length)
				{
					var row:TableRowSetting = rows[i] as TableRowSetting;
					if(row)
					{
						if(Unit == "px")
							rowHeight += row.Height; 
						else
							rowHeight += Parser.GetPixel(row.Height, Unit);
					}
				}
			}
			return rowHeight;
		}
	}
}