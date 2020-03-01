/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


报表页。


*/

package myreport.report
{
	import flash.text.TextField;
	import flash.text.TextFormat;
	
	import hlib.DisposeUtil;
	import hlib.LayoutUtil;
	import hlib.TextBase;
	
	import myreport.Assembly;
	import myreport.data.report.ReportSettings;
	import myreport.data.report.StyleSetting;
	import myreport.data.report.TableCellSetting;
	import myreport.expression.ExpressionContext;
	
	public final class ReportPage extends ReportItem
	{
		private var _Top:Number = 0;
		private var _Bottom:Number = 0;
		private var _Left:Number = 0;
		private var _Right:Number = 0;
		private var _HasError:Boolean = false;
 
		private var _ClientHeight:Number = 0;
		private var _Contents:Array = new Array();
		private var _RepeatPageHeaders:Array;
		private var _RepeatTableHeaders:Array;
		private var _RepeatTableFooters:Array;
		private var _RepeatPageFooters:Array;
		private var _PageNumber:ReportItem;
		
		private var _Index:uint;
		protected override function Disposing():void
		{
			DisposeUtil.Clear(_RepeatPageHeaders);
			DisposeUtil.Clear(_RepeatTableHeaders);
			DisposeUtil.Clear(_RepeatTableFooters);
			DisposeUtil.Clear(_RepeatPageFooters);
			DisposeUtil.Clear(_Contents);
			_RepeatPageHeaders = null;
			_RepeatTableHeaders = null;
			_RepeatTableFooters = null;
			_RepeatPageFooters = null;
			_PageNumber = null;
			_Contents = null;

			super.Disposing();
		}
		public function ReportPage(settings:ReportSettings, index:uint)
		{
			super(settings);
			RefreshSize();
			
			_Index = index;
 
		}
		private function RefreshSize():void
		{
			width = Settings.PageWidth;
			height = Settings.PageHeight;
			_Top = Settings.TopMargin;
			_Left = Settings.LeftMargin;
			_Bottom = height - Settings.BottomMargin;
			_Right = width - Settings.RightMargin;
		}
 		public function get Index():uint
		{
			return _Index;
		}
		public function CheckOverflow(appendHeight:Number):Boolean
		{
			return !Settings.CanGrow && _ClientHeight + appendHeight > _Bottom - _Top;
		}
		public function get Overflow():Boolean
		{
			return !Settings.CanGrow && _ClientHeight > _Bottom - _Top;
		}
		internal function get ClientWidth():Number
		{
			return _Right - _Left;
		}

		public function set RepeatPageHeaders(items:Array):void
		{
			_RepeatPageHeaders = items;
			if(_RepeatPageHeaders)
				_ClientHeight += LayoutUtil.CalculateHeight(_RepeatPageHeaders);
		}
		public function set RepeatPageFooters(items:Array):void
		{
			_RepeatPageFooters = items;
			if(_RepeatPageFooters)
				_ClientHeight += LayoutUtil.CalculateHeight(_RepeatPageFooters);
		}
		public function set RepeatTableHeaders(items:Array):void
		{
			_RepeatTableHeaders = items;
			if(_RepeatTableHeaders)
				_ClientHeight += LayoutUtil.CalculateHeight(_RepeatTableHeaders);
		}
		public function set RepeatTableFooters(items:Array):void
		{
			_RepeatTableFooters = items;
			if(_RepeatTableFooters)
				_ClientHeight += LayoutUtil.CalculateHeight(_RepeatTableFooters);
		}

		public function AddContent(item:ReportItem):void
		{
			_Contents.push(item);
			_ClientHeight += item.height;
		}

		public function get PageNumber():ReportItem
		{
			return _PageNumber;
		}		
		public function set PageNumber(value:ReportItem):void
		{
			_PageNumber = value;
			if(_PageNumber)
				_ClientHeight += _PageNumber.height;
		}
 
		public function EndRender():void
		{
			var item:ReportItem;
			var i:int = 0;
			if(_RepeatPageHeaders)
			{
				for each(item in _RepeatPageHeaders)
				{
					_Contents.splice(i, 0, item);
					i++;
				}
			}
			if(_RepeatTableHeaders)
			{
				for each(item in _RepeatTableHeaders)
				{
					_Contents.splice(i, 0, item);
					i++;
				}
			}
			if(_RepeatTableFooters)
			{
				for each(item in _RepeatTableFooters)
				{
					_Contents.push(item);
				}
			}
			if(_RepeatPageFooters)
			{
				for each(item in _RepeatPageFooters)
				{
					_Contents.push(item);
				}
			}
			MergeAll();
			
			i = 0;
			var top:Number = _Top;
			for each(item in _Contents)
			{
				if(_RepeatPageFooters && Settings.PageFooterShowAtEnd && 
					i>=_Contents.length - _RepeatPageFooters.length)
					break;
				item.x = _Left;
				item.y = top;
				addChild(item);
				top += item.height;
				i++;
			}
			
			
			
			var bottom:Number = _Bottom;
			if(Settings.CanGrow)
				bottom = _ClientHeight + Settings.TopMargin;
			
			if(_PageNumber)
			{
				bottom -= _PageNumber.height;
				_PageNumber.y = bottom;
				addChild(_PageNumber);
			}
			
			if(_RepeatPageFooters && Settings.PageFooterShowAtEnd)
			{
				for(i = _RepeatPageFooters.length-1;i>=0;i--)
				{
					item = _RepeatPageFooters[i];
					bottom -= item.height;
					item.x = _Left;
					item.y = bottom;
					addChild(item);
				}
			}

			AddWartermark();
			
			if(Settings.CanGrow)
				height = _ClientHeight + Settings.TopMargin + Settings.BottomMargin;
		}
		
		public function RenderPageError(message:String):void
		{
			RemoveAllChildren();
			DisposeUtil.Clear(_Contents);
			DisposeUtil.Clear(_RepeatPageHeaders);
			DisposeUtil.Clear(_RepeatTableHeaders);
			DisposeUtil.Clear(_RepeatTableFooters);
			DisposeUtil.Clear(_RepeatPageFooters);
			
			_RepeatPageHeaders = null;
			_RepeatTableHeaders = null;
			_RepeatTableFooters = null;
			_RepeatPageFooters = null;
			_PageNumber = null;
			_Contents = null;	
			RefreshSize();
			_HasError = true;
			
			var format:TextFormat = new TextFormat();
			format.align = "center";
			format.font = "Simsun";
			format.size = 11;
			var text:TextBase = TextBase.CreateText(message, format, ClientWidth);
 
			text.x = _Left;
			text.y = _Top;
			addChild(text);
		}
 
		private function AddWartermark():void
		{
			if(!myreport.Assembly.SHOW_WATERMARK)
				return;
			var format:TextFormat = new TextFormat();
			format.bold = true;
			format.italic = true;
			format.size = 18;
			format.font = "Simsun";
			format.color = 0xCCCCCC;
			
			var text:TextBase = TextBase.CreateText(Assembly.NAME + Assembly.VERSION + "——演示版", format);
			addChild(text);
		}
 
		//====================合并=======================
		private function MergeAll():void
		{
			MergeAllHeadersOrFooters(ReportItem.TYPE_TABLE_HEADER);
			MergeAllHeadersOrFooters(ReportItem.TYPE_TABLE_FOOTER);
			MergeAllContents();
		}
		
		private function IndexOfItem(type:int, start:int=0):int
		{
			var item:ReportItem;
			for(var i:int = start; i<_Contents.length; i++)
			{
				item = _Contents[i];
				if(item.Type == type)
					return i;
			}
			return -1;
		}
		private function CalculateItemsHeight(start:int, end:int):Number
		{
			var h:Number = 0;
			var item:ReportTableRow; 
			for(var i:int = start; i<=end; i++)
			{
				item = _Contents[i];
				h+=item.height;
			}
			return h;
		}
		
		private function MergeAllHeadersOrFooters(type:int):void
		{
			var item:ReportItem;
			var start:int = IndexOfItem(type);
			
			while(start>=0)
			{
				var count:int = 0;
				for(var i:int = start+1; i<_Contents.length; i++)
				{
					item = _Contents[i];
					if(item.Type == type && !item.SubReportBegin && !item.SubReportEnd)
						count++;
					else
					{
						break;
					}
				}
				var end:int = start+count;
				MergeVerticalCells(start, end);
				start = IndexOfItem(type, end+1);
			}
		}
		
		private function MergeAllContents():void
		{
			var type:int = ReportItem.TYPE_TABLE_CONTENT;
			var item:ReportItem;
			var groupIndex:int = 0;
			var start:int = IndexOfItem(type);
			while(start>=0)
			{
				item = _Contents[start]
				groupIndex = item.GroupIndex;
				var count:int = 0;
				for(var i:int = start+1; i<_Contents.length; i++)
				{
					item = _Contents[i];
					if(item.Type == type && item.GroupIndex == groupIndex &&
						!item.SubReportBegin)
					{
						count++;
						if(item.SubReportEnd)
							break;
					}
					else
					{
						break;
					}
				}
				var end:int = start+count;
				MergeVerticalCells(start, end);
				MergeVerticalSameContents(start, end);
				start = IndexOfItem(type, end+1);
			}
		}
		
		private function MergeVerticalCells(start:int, end:int):void
		{
			if(start<0 || end==start)
				return;
			var item:ReportTableRow;
			var cell:ReportCell;
			//trace("start:", start, "end:", end);
			for(var i:int = start; i<=end; i++)
			{
				item = _Contents[i];
				for(var c:int=0;c<Settings.TableColumnSettings.length;c++)
				{
					cell = item.Cells[c];
					var data:TableCellSetting = cell.Data;
					if(!cell.visible || data.RowSpan<=1)
						continue;
					
					var end2:int = i + Math.min(data.RowSpan-1, end-i);
					var h:Number = CalculateItemsHeight(i, end2);
	 
					cell.AdjustHeight(h);
				}
			}
		}
		
		private function MergeVerticalSameContents(start:int, end:int):void
		{
			if(start<0 || end==start)
				return;
			var item:ReportTableRow;
			var cell:ReportCell;
			var item2:ReportTableRow;
			var cell2:ReportCell;
			//trace("start:", start, "end:", end);
			for(var i:int = start; i<=end; i++)
			{
				item = _Contents[i];
				for(var c:int=0;c<Settings.TableColumnSettings.length;c++)
				{
					cell = item.Cells[c];
					var data:TableCellSetting = cell.Data;
					if(!cell.visible || !data.MergeSameContent)
						continue;
					var h:Number = item.height;
					
					for(var j:int = i+1; j<=end; j++)
					{
						item2 = _Contents[j];
						cell2 = item2.Cells[c];
						if(!cell2.visible || cell2.CellValue == cell.CellValue)
						{
							h+=item2.height;
							cell2.visible = false;
						}
						else
							break;
					}

					cell.AdjustHeight(h);
				}
			}
		}
 
	}
}