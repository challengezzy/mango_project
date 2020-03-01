package myreport.design
{
	import flash.display.DisplayObject;
	
	import hlib.CloneUtil;
	import hlib.ComponentBase;
	import hlib.DisposeUtil;
	import hlib.LayoutUtil;
	import hlib.Parser;
	
	import mx.collections.ArrayCollection;
	
	import myreport.data.report.ReportSettings;
	
	public class DesignContent extends ComponentBase
	{
		private var _Data:ReportSettings;
		
		private var _ReportStyle:DesignGroup;
		private var _ReportHeader:DesignGroup;
		private var _ReportFooter:DesignGroup;
		private var _TableColumn:TableColumnGroup;
		private var _TableHeader:TableRowGroup;
		private var _TableDetail:TableRowGroup;
		private var _TableFooter:TableRowGroup;
		private var _TableGroup:DesignGroup;
		private var _TableGroupHeader:TableRowGroup;
		private var _TableGroupFooter:TableRowGroup;
		
		private var _SelectedItem:IDesignItem;
		private var _SelectedItems:Array = new Array();
  
		public function DesignContent()
		{
			super();
			
			_ReportStyle = new ReportStyleItem();
			_ReportHeader = new PageHeaderOrFooterGroup("报表头");
			_TableColumn = new TableColumnGroup();
			_TableHeader = new TableRowGroup("表格头");
			_TableDetail = new TableRowGroup("表格主体");
			_TableFooter = new TableRowGroup("表格尾");
			_TableGroup = new TableGroupItem();
			_TableGroupHeader = new TableRowGroup("分组头");
			_TableGroupFooter = new TableRowGroup("分组尾");
			_ReportFooter = new PageHeaderOrFooterGroup("报表尾", false);
			
			addChild(_ReportStyle);
			addChild(_ReportHeader);
			addChild(_TableColumn);
			addChild(_TableHeader);
			addChild(_TableDetail);
			addChild(_TableFooter);
			addChild(_TableGroup);
			addChild(_TableGroupHeader);
			addChild(_TableGroupFooter);
			addChild(_ReportFooter);
 
			addEventListener(DesignEvent.ITEM_MOUSE_DOWN, OnDesignEvent);
			addEventListener(DesignEvent.COLUMN_WIDTH_CHANGED, OnDesignEvent);
			addEventListener(DesignEvent.COLUMN_ADDED, OnDesignEvent);
			addEventListener(DesignEvent.COLUMN_DELETED, OnDesignEvent);
			addEventListener(DesignEvent.HEIGHT_CHANGED, OnDesignEvent);
		}
		public function get Data():ReportSettings
		{
			return _Data;
		}
		public function set Data(value:ReportSettings):void
		{
			_Data = value;
		}
		public function get SelectedItem():IDesignItem
		{
			return _SelectedItem;
		}
		public function set SelectedItem(value:IDesignItem):void
		{
			_SelectedItem = value;
		}
		public function EditProperty(target:Object, value:Object):void
		{
			for each(var select:IDesignItem in _SelectedItems)
			{
				select.Data[target] = value;
				select.Refresh();
			}
			if(_SelectedItem is ReportStyleItem)
			{
				Refresh();
			}
		}
		public function EditStyle(target:Object, value:Object):void
		{
			for each(var select:IDesignItem in _SelectedItems)
			{
				if(target is Array)
				{
					var targets:Array = target as Array;
					var values:Array = value as Array;
					for(var i:int=0;i<targets.length;i++)
					{
						select.Data.Style[targets[i]] = values[i];
					}
				}
				else
					select.Data.Style[target] = value;
				select.Refresh();
			}
		}
		public function EditControl(value:Object):void
		{
			for each(var select:IDesignItem in _SelectedItems)
			{
				select.Data.Control = CloneUtil.Clone(value);
				select.Refresh();
			}
 
		}
		public function EditConditionStyle(value:Object):void
		{
			for each(var select:IDesignItem in _SelectedItems)
			{
				select.Data.ConditionStyleSettings = CloneUtil.Clone(value);
			}	
		}
		private function UnselectAll():void
		{
			for each(var select:IDesignItem in _SelectedItems)
			{
				select.Selected = false;
			}
			DisposeUtil.Clear(_SelectedItems);
			_SelectedItem = null;
		}
		private function SetSelected(value:IDesignItem):void
		{
			_SelectedItem = value;
			if (_SelectedItem)
			{
				_SelectedItem.Selected = true;
			}
		}
		private function RemoveAt(index:int):void
		{
			var item:IDesignItem = _SelectedItems[index];
			_SelectedItems.splice(index,1);
			item.Selected = false;
			if(item == _SelectedItem)
			{
				_SelectedItem = null;
			}
		}
		private function ReportSelectionChanged():void
		{
			var e:DesignEvent = new DesignEvent(DesignEvent.SELECTION_CHANGED, true);
	 
			dispatchEvent(e);
		}
		private function Select(item:IDesignItem, append:Boolean):void
		{
			var changed:Boolean = true;
			if (!append && _SelectedItem == item)
				return;
			else if (item == null)
			{
				changed = _SelectedItem!=null;
				UnselectAll();
				if(changed)
					ReportSelectionChanged();
				return;
			}
//			if(item)
//				trace(Object(item).constructor);
			if(item && _SelectedItem && 
				Object(_SelectedItem).constructor != Object(item).constructor)
				append = false;
			
			var index:int = _SelectedItems.indexOf(item);
			//append为true时多选
			if (append)
			{
				if (index < 0)
				{
					_SelectedItems.push(item);
					SetSelected(item);
				}
				else
				{
					RemoveAt(index);
					if (!_SelectedItem && _SelectedItems.length > 0)
						SetSelected(_SelectedItems[_SelectedItems.length - 1]);
				}
			}
			else
			{
				if (index < 0)
				{
					UnselectAll();
					_SelectedItems.push(item);
				}
				SetSelected(item);
			}
			if(changed)
				ReportSelectionChanged();
		}
		private function OnDesignEvent(event:DesignEvent):void
		{
			if (event.type == DesignEvent.ITEM_MOUSE_DOWN)
			{
				event.stopPropagation();
				Select(event.target as IDesignItem, event.Append);
 
			}

			else if (event.type == DesignEvent.COLUMN_WIDTH_CHANGED)
			{
				event.stopPropagation();
				_TableColumn.RefreshWidth();
				_TableHeader.RefreshWidth();
				_TableDetail.RefreshWidth();
				_TableFooter.RefreshWidth();
				_TableGroupHeader.RefreshWidth();
				_TableGroupFooter.RefreshWidth();
			}
			else if (event.type == DesignEvent.COLUMN_ADDED)
			{
				event.stopPropagation();
				_TableHeader.AddCell(event.Value.Title, event.Value.Column);
				_TableDetail.AddCell(event.Value.Title, event.Value.Column);
				_TableFooter.AddCell(event.Value.Title, event.Value.Column);
				_TableGroupHeader.AddCell(event.Value.Title, event.Value.Column);
				_TableGroupFooter.AddCell(event.Value.Title, event.Value.Column);
 
			}
			else if(event.type == DesignEvent.COLUMN_DELETED)
			{
				event.stopPropagation();
				_TableHeader.DeleteCellAt(event.ColumnIndex);
				_TableDetail.DeleteCellAt(event.ColumnIndex);
				_TableFooter.DeleteCellAt(event.ColumnIndex);
				_TableGroupHeader.DeleteCellAt(event.ColumnIndex);
				_TableGroupFooter.DeleteCellAt(event.ColumnIndex);
			}
			else if (event.type == DesignEvent.HEIGHT_CHANGED)
			{
				event.stopPropagation();
				Refresh();
			}
		}

		protected override function OnResize():void
		{
			Paint();
		}
		
		public function Refresh():void
		{
			var paddingTop:Number = Parser.GetPixel(Data.TopMargin, Data.Unit);
			var paddingLeft:Number = Parser.GetPixel(Data.LeftMargin, Data.Unit);
			var paddingRight:Number = Parser.GetPixel(Data.RightMargin, Data.Unit);
			var paddingBottom:Number = Parser.GetPixel(Data.BottomMargin, Data.Unit);
			var pageWidth:Number = Parser.GetPixel(Data.PageWidth, Data.Unit);
			
			width = pageWidth;
			var w:Number = Math.max(0, pageWidth - paddingLeft - paddingRight);
			
			var top:Number = paddingTop;
			var gap:Number = 12;
			for(var i:int = 0; i < numChildren; i++)
			{
				var child:DesignGroup = getChildAt(i) as DesignGroup;
				if(child)
				{
					child.width = w;
					child.y = top;
					child.x = paddingLeft;
					
					top += child.height + gap;
				}
			}
			if(numChildren>1)
				top -=gap;
			
			height = top + paddingBottom;
	
		}
		private function Paint():void
		{
			graphics.clear();
			graphics.lineStyle(1, 0x696969);
			graphics.beginFill(0xffffff);
			graphics.drawRect(0, 0, width, height);
			graphics.endFill();
		}
		
		public function Load(setting:XML, tableData:ArrayCollection, parameterData:Object):void
		{
			if (Data != null)
				Data.Dispose();
			try
			{
				Data = new ReportSettings(setting);
			}
			catch (e:Error)
			{
				Data = new ReportSettings(null);
			}
 
			Data.TableData = tableData;
			Data.ParameterData = parameterData;
 
			for(var i:int = 0; i < numChildren; i++)
			{
				var group:DesignGroup = getChildAt(i) as DesignGroup;
				if(group)
					group.Settings = Data;
			}
			
			Refresh();
			
			Select(null, false);

		}
	}
}