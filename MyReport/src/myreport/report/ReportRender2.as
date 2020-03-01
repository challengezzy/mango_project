/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


报表呈现引擎2。


*/

package myreport.report
{
	import flash.utils.Dictionary;
	import flash.utils.getTimer;
	
	import hlib.CollectionUtil;
	import hlib.LayoutUtil;
	import hlib.MsgUtil;
	
	import mx.collections.ArrayCollection;
	import mx.collections.Sort;
	import mx.collections.SortField;
	
	import myreport.ReportEngine;
	import myreport.data.report.CaptionCellSetting;
	import myreport.data.report.CaptionRowSetting;
	import myreport.data.report.DynamicColumnSetting;
	import myreport.data.report.ReportSettings;
	import myreport.data.report.StyleSetting;
	import myreport.data.report.SubReportRowSetting;
	import myreport.data.report.TableCellSetting;
	import myreport.data.report.TableColumnSetting;
	import myreport.data.report.TableGroupSetting;
	import myreport.data.report.TableRowSetting;
	import myreport.data.reportdata.DummyData;
	import myreport.expression.ExpressionContext;
	import myreport.expression.ExpressionEngine;
	
	public final class ReportRender2
	{
		public static var MAX_PAGE:int = 500;
		/**
		 * 筛选表格数据
		 * @param filter: 表格数据筛选表达式，为空时返回原表格数据
		 */ 
		public static function FilterTable(table:ArrayCollection, filter:String = null):ArrayCollection
		{
			if(!filter)
				return table;
			
			var context:ExpressionContext = new ExpressionContext();
			context.Table = table;
			var newTable:ArrayCollection = new ArrayCollection();
			for(var i:int=0; i<table.length; i++)
			{
				context.RowIndex = i;
				if(ExpressionEngine.Invoke(filter, context))
				{
					newTable.addItem(table[i]);
				}
			}
			return newTable;
		}
		
		public static function GroupTable(settings:ReportSettings):void
		{
			var tableData:ArrayCollection = settings.TableData;
			var group:TableGroupSetting = settings.Group;
			if(tableData && group && (group.GroupColumn || group.OrderColumn))
			{
				settings.TableData = CollectionUtil.GroupAndOrderTable(tableData, group.GroupColumn, group.OrderColumn);
			}
			else if(tableData && group && group.FitToGroupSize && tableData.length && group.GroupSize)
			{
				var len:int = tableData.length % group.GroupSize;
				if(len)
				{
					len = group.GroupSize - len;
					var copy:ArrayCollection = new ArrayCollection();
					copy.addAll(tableData);
					for(var i:int=0;i<len;i++)
					{
						copy.addItem(new DummyData());
					}
					
					settings.TableData = copy;
				}
			}
		}
		
		public static function RenderPages(settings:ReportSettings):Array
		{
			ExecuteColSpan(settings);
			RenderColumns(settings);
			settings.SetUnit("px");
			settings.RefreshAllLayout();
			var page:ReportPage;
			var pages:Array = new Array();
			try
			{
				var tableData:ArrayCollection = settings.TableData;//临时缓存表数据，有可能执行分组排序
				GroupTable(settings);

				var item:ReportItem;
				var contentIndex:int = 0;
				var contents:Array = new Array();
				FillTableContent(contents, settings);
				while (true)
				{
					try
					{
						page = CreatePage(settings, pages.length);
						pages.push(page);
						BeginRenderPage(page, settings);
						if(page.Overflow)
							throw new Error("页面内容溢出，增加页面高度才能正常显示。");
						//Render Content
						for (; contentIndex < contents.length; contentIndex++)
						{
							item = contents[contentIndex];
							if(!page.CheckOverflow(item.height))
							{
								page.AddContent(item);
								if(!settings.CanGrow && item.PageBreak)
								{
									contentIndex++;
									break;
								}
							}
							else
								break;
						}
						page.EndRender();
						
					}
					catch (e:Error)
					{
						page.RenderPageError(e.getStackTrace());
						break;
					}
					if (contentIndex >= contents.length)
						break;
					if (pages.length > MAX_PAGE)
					{
						MsgUtil.ShowInfo("报表引擎支持的页数上限是" + MAX_PAGE + "页，" + MAX_PAGE + "页之后的页面已被忽略。");
						break;
					}
				}
				
				//刷新页码
				if(settings.ShowPageNumber)
				{
					for each(page in pages)
					{
						if(page.PageNumber)
						{
							ReportPageNumber(page.PageNumber).Render(page.Index+1, pages.length);
						}
					}
				}
				
				settings.TableData = tableData;//还原表数据，有可能执行分组排序
			}
			catch (e:Error)
			{
				if (pages.length == 0)
				{
					page = CreatePage(settings, pages.length);
					pages.push(page);
					page.RenderPageError(e.getStackTrace());
				}
				else
					MsgUtil.ShowInfo(e.getStackTrace());
			}

			return pages;
		}
		
		public static function BeginRenderPage(page:ReportPage, settings:ReportSettings):void
		{
			var items:Array;
			var item:ReportItem;
			
			if(settings.PageHeaderRepeat || page.Index == 0)
			{
				items = new Array();
				FillPageHeaderOrFooter(items, settings, true);
				page.RepeatPageHeaders = items;
			}
			
			if(settings.ShowPageNumber)
			{
				item = RenderPageNumber(settings);
				page.PageNumber = item;
			}
			
			if(settings.PageFooterRepeat)
			{
				items = new Array();
				FillPageHeaderOrFooter(items, settings, false);
				page.RepeatPageFooters = items;
			}
			
			if(settings.TableHeaderRepeat)
			{
				items = new Array();
				FillTableHeaderOrFooter(items, settings, true);
				page.RepeatTableHeaders = items;
			}
			
			if(settings.TableFooterRepeat)
			{
				items = new Array();
				FillTableHeaderOrFooter(items, settings, false);
				page.RepeatTableFooters = items;
			}
		}
		
		private static function FillSubReport(items:Array, settings:ReportSettings, context:ExpressionContext, 
											  style:String, parameters:String, table:String, adjust:Boolean):void
		{
			var styleData:Object = ExpressionEngine.Invoke(style, context);
			var parameterData:Object = ExpressionEngine.Invoke(parameters, context);
			var tableData:Object = ExpressionEngine.Invoke(table, context);
			
			var subSettings:ReportSettings = myreport.ReportEngine.ToSettings(styleData, 
				tableData as ArrayCollection, parameterData);
 
			if(!subSettings)
				return;
			RenderColumns(subSettings);
			ExecuteColSpan(subSettings);
			subSettings.SetUnit("px");
			subSettings.RefreshAllLayout();
			if(adjust)
			{
				for(var c:int=0;c<settings.TableColumnSettings.length;c++)
				{
					if(c<subSettings.TableColumnSettings.length)
					{
						var dest:TableColumnSetting = subSettings.TableColumnSettings[c];
						var src:TableColumnSetting = settings.TableColumnSettings[c];
						dest.Width = src.Width;
					}
				}
			}
			
			GroupTable(subSettings);
			
			var subItems:Array = new Array();
			FillPageHeaderOrFooter(subItems, subSettings, true);
			
			if(subSettings.TableHeaderRepeat)
			{
				FillTableHeaderOrFooter(subItems, subSettings, true);
			}
			
			FillTableContent(subItems, subSettings);
			
			if(subSettings.TableFooterRepeat)
			{
				FillTableHeaderOrFooter(subItems, subSettings, false);
			}
			
			FillPageHeaderOrFooter(subItems, subSettings, false);
			var item:ReportItem;
			if(subItems.length)
			{
				item = subItems[0];
				item.SubReportBegin = true;
				item = subItems[subItems.length-1];
				item.SubReportEnd = true;
			}
			
			for each(item in subItems)
			{
				items.push(item);
			}
		}
		
		private static function FillSubReportRow(items:Array, settings:ReportSettings, context:ExpressionContext, 
											  row:SubReportRowSetting):void
		{
			var parameterData:Object = ExpressionEngine.Invoke(row.SubReportParameters, context);
			var tableData:Object = ExpressionEngine.Invoke(row.SubReportTable, context);
			
			var subSettings:ReportSettings = new ReportSettings();
			subSettings.ParameterData = parameterData;
			subSettings.TableData = tableData as ArrayCollection;
			
			subSettings.SetUnit("px");
			subSettings.TableColumnSettings = settings.TableColumnSettings;
			subSettings.TableHeaderSettings = row.TableHeaderSettings;
			subSettings.TableDetailSettings = row.TableDetailSettings;
			subSettings.TableFooterSettings = row.TableFooterSettings;
			subSettings.RefreshAllLayout();
			
			var group:TableGroupSetting = subSettings.TableGroupSettings[0];
			group.Enabled = row.GroupEnabled;
			group.GroupColumn = row.GroupColumn;
			group.GroupSize = row.GroupSize;
			group.OrderColumn = row.OrderColumn;
			GroupTable(subSettings);
			
			var subItems:Array = new Array();
			if(subSettings.TableHeaderRepeat)
			{
				FillTableHeaderOrFooter(subItems, subSettings, true);
			}
			
			FillTableContent(subItems, subSettings);
			
			if(subSettings.TableFooterRepeat)
			{
				FillTableHeaderOrFooter(subItems, subSettings, false);
			}
 
			var item:ReportItem;
			if(subItems.length)
			{
				item = subItems[0];
				item.SubReportBegin = true;
				item = subItems[subItems.length-1];
				item.SubReportEnd = true;
			}
			
			for each(item in subItems)
			{
				items.push(item);
			}
		}
		public static function ExecuteColSpan(settings:ReportSettings):void
		{
			var context:ExpressionContext = new ExpressionContext();
			context.Table = settings.TableData;
			context.Parameters = settings.ParameterData;
			ExecuteRowsColSpan(context, settings.TableHeaderSettings);
			ExecuteRowsColSpan(context, settings.TableDetailSettings);
			ExecuteRowsColSpan(context, settings.TableFooterSettings);
			for each(var group:TableGroupSetting in settings.TableGroupSettings)
			{
				ExecuteRowsColSpan(context, group.TableGroupHeaderSettings);
				ExecuteRowsColSpan(context, group.TableGroupFooterSettings);
			}
		}
		
		private static function ExecuteRowsColSpan(context:ExpressionContext, rows:Array):void
		{
 
			var c:int, r:int;
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
						var parameterData:Object = ExpressionEngine.Invoke(sub.SubReportParameters, context);
						var tableData:Object = ExpressionEngine.Invoke(sub.SubReportTable, context);
						var context2:ExpressionContext = new ExpressionContext();
						context2.Table = tableData as ArrayCollection;
						context2.Parameters = parameterData;
						ExecuteRowsColSpan(context2, sub.TableHeaderSettings);
						ExecuteRowsColSpan(context2, sub.TableDetailSettings);
						ExecuteRowsColSpan(context2, sub.TableFooterSettings);
					}
					continue;
				}
				
				for each(cell in row.TableCellSettings)
				{
					if(ExpressionEngine.IsExpression(cell.ColSpanExpression))
					{
						var colSpan:uint = uint(ExpressionEngine.Invoke(cell.ColSpanExpression, context));
						cell.ColSpan = Math.max(1, colSpan);
					}
				}
 
			}
		}
		private static function ExpandCellSetting(rows:Array, i:int, c:int, value:String, textAlign:String):void
		{
			var r:int;
			var row:TableRowSetting;
			var cell:TableCellSetting;
			var insertIndex:int = i + c;
			for(r = 0; r<rows.length;r++)
			{
				row = rows[r];
				cell = row.TableCellSettings[i];
				if(c>0)
				{
					cell = cell.Clone();
					row.TableCellSettings.splice(insertIndex, 0, cell);
				}
				if(textAlign)
					cell.Style.TextAlign = textAlign;
				if(value)
					cell.Value = value;
				//				else
				//					cell.Value = "";
			}
		}
		public static function RenderColumns(settings:ReportSettings):void
		{
			var i:int=0;
			var col:TableColumnSetting;
			while(i<settings.TableColumnSettings.length)
			{
				col = settings.TableColumnSettings[i];
				if(col.Dynamic)
				{
					col.Dynamic = false;
					var context:ExpressionContext = new ExpressionContext();
					context.Table = settings.TableData;
					context.Parameters = settings.ParameterData;
					
					var columns:Object = ExpressionEngine.Invoke(col.DynamicColumnSettings, context);
					if(columns && columns.length)
					{
						for(var c:int=0; c<columns.length; c++)
						{
							var column:DynamicColumnSetting = columns[c];
							var insertIndex:int = i + c;
							if(c>0)
							{
								var newCol:TableColumnSetting = col.Clone();
								if(column.Width)
									newCol.Width = column.Width;
								newCol.Field = column.Field;
								newCol.Text = column.Text;
								settings.TableColumnSettings.splice(insertIndex, 0, newCol);
							}
							else
							{
								if(column.Width)
									col.Width = column.Width;
								col.Field = column.Field;
								col.Text = column.Text;
							}
							
							//trace("Render Column:",column.Header,",", column.Detail,",",column.Footer,",",column.Width);
							ExpandCellSetting(settings.TableHeaderSettings, i, c, column.Header, column.HeaderTextAlign);
							ExpandCellSetting(settings.TableDetailSettings, i, c, column.Detail, column.DetailTextAlign);
							ExpandCellSetting(settings.TableFooterSettings, i, c, column.Footer, column.FooterTextAlign);
							
						}
						i += columns.length
						continue;
					}
					
				}
			 
				i ++;
			}
			if(settings.AutoWidth)
			{
				var w:Number = settings.LeftMargin + settings.RightMargin;
				for each(col in settings.TableColumnSettings)
				{
					w += col.Width;
				}
				settings.PageWidth = w;
			}
			
		}


 
		public static function CreatePage(settings:ReportSettings, pageIndex:int):ReportPage
		{
			var page:ReportPage = new ReportPage(settings, pageIndex);
			return page;
		}

		public static function FillPageHeaderOrFooter(items:Array, settings:ReportSettings, header:Boolean):void
		{
			var context:ExpressionContext = new ExpressionContext();
			context.Table = settings.TableData;
			context.Parameters = settings.ParameterData;
			context.RowIndex = 0;
			if(!header && context.Table && context.Table.length)
				context.RowIndex = context.Table.length - 1;
			
			if(header)
				FillCaptionRows(items, settings, context, settings.PageHeaderSettings, ReportItem.TYPE_PAGE_HEADER);
			else
				FillCaptionRows(items, settings, context, settings.PageFooterSettings, ReportItem.TYPE_PAGE_FOOTER);
		}
		/**
		 * @param rows: item => CaptionRowSetting
		 */ 
		private static function FillCaptionRows(items:Array, settings:ReportSettings, context:ExpressionContext, rows:Array, type:int):void
		{
			for each(var row:CaptionRowSetting in rows)
			{
				if(row.SubReportRow)
				{
					FillSubReport(items, settings, context, row.SubReportStyle, row.SubReportParameters,
						row.SubReportTable, row.AdjustMainReport);
				}
				else
				{
					var item:ReportItem = new ReportCaptionRow(settings, context, row);
					item.Type = type;
					items.push(item);
				}
			}
		}
		
		private static function RenderPageNumber(settings:ReportSettings):ReportItem
		{
			var item:ReportItem = new ReportPageNumber(settings);
			item.width = settings.ClientWidth;
			item.height = 22;
			return item;
		}
		
		/**
		 * @param rows: item => TableRowSetting
		 * @param type: 行类型
		 */ 
		private static function FillTableRows(items:Array, settings:ReportSettings, context:ExpressionContext, rows:Array, type:int):void
		{
			
			for each(var r:Object in rows)
			{
				if(r is TableRowSetting)
				{
					var row:TableRowSetting = TableRowSetting(r);
					if(row.SubReportRow)
					{
						FillSubReport(items, settings, context, row.SubReportStyle, row.SubReportParameters,
							row.SubReportTable, row.AdjustMainReport);
					}
					else
					{
						var item:ReportItem = new ReportTableRow(settings, context, row);
						item.Type = type;
						items.push(item);
					}
				}
				else if(r is SubReportRowSetting)
				{
					var sub:SubReportRowSetting = SubReportRowSetting(r);
					FillSubReportRow(items, settings, context, sub);
				}
			}
		}
 
		public static function FillTableHeaderOrFooter(items:Array, settings:ReportSettings, header:Boolean):void
		{
			var context:ExpressionContext = new ExpressionContext();
			context.Table = settings.TableData;
			context.Parameters = settings.ParameterData;
			context.RowIndex = 0;
			if(!header && context.Table && context.Table.length)
				context.RowIndex = context.Table.length - 1;
			
			if(header)
				FillTableRows(items, settings, context, settings.TableHeaderSettings, ReportItem.TYPE_TABLE_HEADER);
			else
				FillTableRows(items, settings, context, settings.TableFooterSettings, ReportItem.TYPE_TABLE_FOOTER);
			
		}
		
		public static function FillTableGroupHeaderOrFooter(items:Array, settings:ReportSettings, rowIndex:int, groupIndex:int, groupStartIndex:int, groupEndIndex:int, header:Boolean):void
		{
			var context:ExpressionContext = new ExpressionContext();
			context.Table = settings.TableData;
			context.Parameters = settings.ParameterData;
			context.RowIndex = rowIndex;
			context.GroupRowIndex = context.RowIndex - groupStartIndex;
			context.GroupStartIndex = groupStartIndex;
			context.GroupEndIndex = groupEndIndex;
			context.GroupIndex = groupIndex;
			
			var group:TableGroupSetting = settings.Group;
			if (group)
			{
				if(header)
				{
					FillTableRows(items, settings, context, group.TableGroupHeaderSettings, ReportItem.TYPE_TABLE_HEADER);
				}
				else
				{
					FillTableRows(items, settings, context, group.TableGroupFooterSettings, ReportItem.TYPE_TABLE_FOOTER);
					if(settings.TableColumnSettings.length &&  group.TableGroupFooterSettings.length)
						ReportItem(items[items.length-1]).PageBreak = group.PageBreakAtEnd;
				}
			}
		}
		
		public static function FillTableDetail(items:Array, settings:ReportSettings, rows:Array, rowIndex:int, groupIndex:int, groupStartIndex:int, groupEndIndex:int):void
		{
			var context:ExpressionContext = new ExpressionContext();
			context.Table = settings.TableData;
			context.Parameters = settings.ParameterData;
			context.RowIndex = rowIndex;
			context.GroupRowIndex = context.RowIndex - groupStartIndex;
			context.GroupStartIndex = groupStartIndex;
			context.GroupEndIndex = groupEndIndex;
			context.GroupIndex = groupIndex;
			FillTableRows(items, settings, context, rows, ReportItem.TYPE_TABLE_CONTENT);
		}
		
		public static function ValidateGroup(settings:ReportSettings, group:TableGroupSetting):void
		{
			if(group.GroupColumn)
			{
				if(settings.TableData && settings.TableData.length)
				{
					var row:Object = settings.TableData[0];
					if(!row.hasOwnProperty(group.GroupColumn))
						throw new Error("表格不包含列“" + group.GroupColumn + "”。");
				}
			}
			else
			{
				if(group.GroupSize <= 0) 
					throw new Error("分组大小不能为0。");
			}
		}
 
		public static function GetGroupEndIndex(settings:ReportSettings, group:TableGroupSetting, rowIndex:int):int
		{
			var groupEnd:int = 0;
			if(group.GroupColumn)
			{
				var row:Object = settings.TableData[rowIndex];
				groupEnd = rowIndex;
				for(var j:int=rowIndex+1;j<settings.TableData.length;j++)
				{
					if(row[group.GroupColumn] != settings.TableData[j][group.GroupColumn])
						break;
					groupEnd = j;
				}
			}
			else
			{
				groupEnd = rowIndex + group.GroupSize - 1;
			}
			groupEnd = Math.min(settings.TableData.length - 1, groupEnd);
			return groupEnd;
		}

		private static function FillTableContent(items:Array, settings:ReportSettings):void
		{

			if(!settings.TableHeaderRepeat)
			{
				FillTableHeaderOrFooter(items, settings, true);
			}
			
			//TableDetail
			var group:TableGroupSetting = settings.Group;
			var groupStart:int = 0;
			var groupEnd:int = 0;
			var groupIndex:int = 0;
			var generateGroupHeader:Boolean = false;
			var generateGroupFooter:Boolean = false;
			if(group)
			{
				ValidateGroup(settings, group);
				generateGroupHeader = true;
			}
			if(settings.TableData)
			{
				for (var rowIndex:int = 0; rowIndex < settings.TableData.length; rowIndex++)
				{
					//GroupHeaders
					if(generateGroupHeader)
					{
						groupStart = rowIndex;
						groupEnd = GetGroupEndIndex(settings, group, rowIndex);
						FillTableGroupHeaderOrFooter(items, settings, rowIndex, groupIndex, groupStart, groupEnd, true);
						generateGroupHeader = false;
					}
					
					//TableDetails
					FillTableDetail(items, settings, settings.TableDetailSettings, rowIndex, groupIndex, groupStart, groupEnd);
					
					//GroupFooters
					generateGroupFooter = group && rowIndex == groupEnd;
					if(generateGroupFooter)
					{
						FillTableGroupHeaderOrFooter(items, settings, rowIndex, groupIndex, groupStart, groupEnd, false);
						generateGroupFooter = false;
						generateGroupHeader = true;
						groupIndex++;
					}
				}
			}
			if(!settings.TableFooterRepeat)
			{
				FillTableHeaderOrFooter(items, settings, false);
			}
			
			if(!settings.PageFooterRepeat)
			{
				FillPageHeaderOrFooter(items, settings, false);
			}
			
		}
 
	}
}
