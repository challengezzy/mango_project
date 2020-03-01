/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


*/
package myreport.design
{
	import flash.display.DisplayObject;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	
	import hlib.DisposeUtil;
	import hlib.DrawUtil;
	import hlib.IDispose;
	
	import mx.core.UIComponent;
	
	import myreport.data.report.ReportSettings;
	import myreport.data.report.SubReportRowSetting;
	import myreport.data.report.TableCellSetting;
	import myreport.data.report.TableGroupSetting;
	import myreport.data.report.TableRowSetting;
	import myreport.res.Asset;
	import myreport.util.ToolButton;
	
	
	internal class TableRowGroup extends DesignGroup
	{
		private var _AddButton:ToolButton;
		private var _ChildPanel:UIComponent;
		
		private var _Parent:Array;
		private var _Type:String;
		protected override function Disposing():void
		{
			_AddButton.removeEventListener(MouseEvent.CLICK, OnAddButtonMouseEvent);
			DisposeUtil.Dispose(_ChildPanel);
			_ChildPanel = null;
			_Parent = null;
			_AddButton = null;
			
			super.Disposing();
		}
		public function TableRowGroup(title:String = "")
		{
			super(title);
			_Type = title;
			_AddButton = new ToolButton();
			_AddButton.toolTip = "添加行";
			_AddButton.height = 16;
			_AddButton.width = 16;
			_AddButton.setStyle("icon", Asset.ICON_ADD12);
			_AddButton.addEventListener(MouseEvent.CLICK, OnAddButtonMouseEvent);
			addChild(_AddButton);
			
			_ChildPanel = new UIComponent();
			_ChildPanel.y = 16;
			addChild(_ChildPanel);
			
			OnResize();
			addEventListener(DesignEvent.COL_SPAN_CHANGED, OnMyReportEvent);
			addEventListener(DesignEvent.ROW_SPAN_CHANGED, OnMyReportEvent);
			addEventListener(DesignEvent.ROW_DROP, OnMyReportEvent);
			addEventListener(DesignEvent.CELL_DROP, OnMyReportEvent);
			addEventListener(DesignEvent.HEIGHT_CHANGED, OnMyReportEvent);
			addEventListener(DesignEvent.DELETE, OnMyReportEvent);
			
		}
		
		private function OnMyReportEvent(event:DesignEvent):void
		{
			//trace(event.type);
			if (event.type == DesignEvent.HEIGHT_CHANGED)
			{
				if(event.target != this)
					RefreshHeight(false);
			}
			else if(event.type == DesignEvent.COL_SPAN_CHANGED || event.type == DesignEvent.CELL_DROP)
			{
				event.stopPropagation();
				RefreshColSpan();
			}
			else if(event.type == DesignEvent.ROW_SPAN_CHANGED)
			{
				event.stopPropagation();
				RefreshRowSpan();
			}
			else if(event.type == DesignEvent.ROW_DROP)
			{
				event.stopPropagation();
				OnRowDrop();
			}
			else if (event.type == DesignEvent.DELETE)
			{
				event.stopPropagation();
				var item:ITableRowItem = event.target as ITableRowItem;
				if (!item || !_Parent)
					return;
				var index:int = _Parent.indexOf(item.Data);
				if (index >= 0)
					_Parent.splice(index,1);
				if (item.Data is IDispose)
					IDispose(item.Data).Dispose();
				
				var design:DesignGroup = item as DesignGroup;
				design.parent.removeChild(design);
				design.Dispose();
				
				RefreshColSpan();
				RefreshHeight(true);
			}
		}
		
		private function OnAddButtonMouseEvent(event:MouseEvent):void
		{
 
			switch(_Type)
			{
				case "表格头":
				case "表格主体":
				case "表格尾":
					ShowMenu();
					break;
			 	default:
					AddTableRow();
					break;
			}
		}
		
		private function ShowMenu():void
		{
			var p:Point = localToGlobal(new Point(_AddButton.x, _AddButton.y));
			TableRowMenu.Instance.Show(p.x, p.y, OnMenuItemClick);
			
		}
		private function OnMenuItemClick(command:String):void
		{
			var action:String = command;
			if(action == "AddTableRow")
			{
				AddTableRow();
			}
			else if(action == "AddSubReportRow")
			{
				AddSubReportRow();
			}
		}
		private function AddSubReportRow():void
		{
			if(!_Parent) return;
			var sub:SubReportRowSetting = new SubReportRowSetting();
			_Parent.push(sub);
			var item:DisplayObject = DesignUtil.CreateItem(sub, _Parent, Settings);
			item.width = width;
			_ChildPanel.addChild(item);
			RefreshColSpan();
			RefreshHeight(true);
		}
		private function AddTableRow():void
		{
			if(!_Parent)
				return;
			var row:TableRowSetting = new TableRowSetting();
			for (var i:int = 0; i < Settings.TableColumnSettings.length; i++)
			{
				row.TableCellSettings.push(new TableCellSetting());
			}
			_Parent.push(row);
			var item:DisplayObject = DesignUtil.CreateItem(row, _Parent, Settings);
			item.width = width;
			_ChildPanel.addChild(item);
			RefreshColSpan();
			RefreshHeight(true);
		}
		
		override protected function OnResize():void
		{
			_AddButton.x = width - _AddButton.width;
			_ChildPanel.width = width;
			for (var i:int = 0; i < _ChildPanel.numChildren; i++)
			{
				var item:DisplayObject = _ChildPanel.getChildAt(i);
				item.width = width;
			}
		}
		
		public function SetData(settings:ReportSettings, parent:Array):void
		{
			super.Settings = settings;
			_Parent = parent;
			DisposeUtil.Dispose(_ChildPanel);
			if(_Parent)
			{
				DesignUtil.AddItems(_Parent, _Parent, Settings, _ChildPanel);
			}
			RefreshColSpan();
			RefreshRowSpan();
			RefreshHeight(false);
			OnResize();
		}
		
		public override function set Settings(value:ReportSettings):void
		{
			super.Settings = value;
			DisposeUtil.Dispose(_ChildPanel);
			switch(_Type)
			{
				case "表格头":
					_Parent = Settings.TableHeaderSettings;
					break;
				case "表格主体":
					_Parent = Settings.TableDetailSettings;
					break;
				case "表格尾":
					_Parent = Settings.TableFooterSettings;
					break;
				case "分组头":
					if(Settings.TableGroupSettings.length>0)
						_Parent = TableGroupSetting(Settings.TableGroupSettings[0]).TableGroupHeaderSettings;
					break;
				case "分组尾":
					if(Settings.TableGroupSettings.length>0)
						_Parent = TableGroupSetting(Settings.TableGroupSettings[0]).TableGroupFooterSettings;
					break;
			}
 
			if(_Parent)
			{
				DesignUtil.AddItems(_Parent, _Parent, Settings, _ChildPanel);
			}
			RefreshColSpan();
			RefreshRowSpan();
			RefreshHeight(false);
			OnResize();
		}
 
		public function RefreshWidth():void
		{
			for (var i:int = 0; i < _ChildPanel.numChildren; i++)
			{
				var item:ITableRowItem = _ChildPanel.getChildAt(i) as ITableRowItem;
				item.RefreshWidth();
			}
		}
 
		public function DeleteCellAt(index:int):void
		{
			for (var i:int = 0; i < _ChildPanel.numChildren; i++)
			{
				var item:ITableRowItem = _ChildPanel.getChildAt(i) as ITableRowItem;
				item.DeleteCellAt(index);
			}
			RefreshColSpan();
			RefreshWidth();
		}
		public function AddCell(title:String, column:String):void
		{
			for (var i:int = 0; i < _ChildPanel.numChildren; i++)
			{
				var item:ITableRowItem = _ChildPanel.getChildAt(i) as ITableRowItem;
				item.AddCell(_Type, title, column);
			}
			RefreshColSpan();
			RefreshWidth();
		}
		
		internal function RefreshColSpan():void
		{
			Settings.RefreshRowsLayout(_Parent);
			for (var i:int = 0; i < _ChildPanel.numChildren; i++)
			{
				var item:ITableRowItem = _ChildPanel.getChildAt(i) as ITableRowItem;
				item.RefreshColSpan();
			}
		}

		internal function RefreshRowSpan():void
		{
			Settings.RefreshRowsLayout(_Parent);
			for (var i:int = 0; i < _ChildPanel.numChildren; i++)
			{
				var item:ITableRowItem = _ChildPanel.getChildAt(i) as ITableRowItem;
				item.RefreshRowSpan();
			}
		}
		
		internal function RefreshText():void
		{
			Settings.RefreshRowsLayout(_Parent);
			for (var i:int = 0; i < _ChildPanel.numChildren; i++)
			{
				var item:ITableRowItem = _ChildPanel.getChildAt(i) as ITableRowItem;
				item.RefreshText();
			}
		}
		
		private function OnRowDrop():void
		{
			Settings.RefreshRowsLayout(_Parent);
			var h:Number = 0;
			for (var i:int = 0; i < _ChildPanel.numChildren; i++)
			{
				var item:ITableRowItem = _ChildPanel.getChildAt(i) as ITableRowItem;
				item.RefreshRowSpan();
				item.RefreshText();
				var design:DesignGroup = item as DesignGroup;
				design.y = h;
				h += design.height;
			}
		}
		
		private function RefreshHeight(reportChanged:Boolean):void
		{
			var h:Number = 0;
			for (var i:int = 0; i < _ChildPanel.numChildren; i++)
			{
				var item:ITableRowItem = _ChildPanel.getChildAt(i) as ITableRowItem;
				item.RefreshRowSpan();
				var design:DesignGroup = item as DesignGroup;
				design.y = h;
				h += design.height;
			}
			_ChildPanel.height = h;
			
			var changed:Boolean = height != h + 16;
			height = h + 16;
			
			if(reportChanged && changed)
			{
				var e:DesignEvent = new DesignEvent(DesignEvent.HEIGHT_CHANGED, true);
				dispatchEvent(e);
			}
		}
		
	}
}