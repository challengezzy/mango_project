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
	import hlib.IDispose;
	import hlib.LayoutUtil;
	import hlib.MovableExtension;
	import hlib.MsgUtil;
	import hlib.Parser;
	
	import mx.collections.ArrayCollection;
	import mx.core.DragSource;
	import mx.core.UIComponent;
	import mx.events.DragEvent;
	import mx.managers.DragManager;
	
	import myreport.data.report.ReportSettings;
	import myreport.data.report.TableCellSetting;
	import myreport.data.report.TableColumnSetting;
	import myreport.data.report.TableRowSetting;
	import myreport.res.Asset;
	import myreport.util.ToolButton;

	internal class TableRowItem extends DesignGroup implements IDesignItem, ITableRowItem
	{
		private static var HEADER_WIDTH:Number = 20;
		
		private var _DeleteButton:ToolButton;
		private var _ChildPanel:UIComponent;
		private var _Header:RowItemHeader;
 
		private var _Ext:DividableExtension;

		private var _Dragable:Boolean = false;
		private var _Data:TableRowSetting;
		private var _Parent:Array;

		protected override function Disposing():void
		{
			_DeleteButton.removeEventListener(MouseEvent.CLICK, OnButtonMouseEvent);
			_Ext.Dispose();
			_Ext = null;
			DisposeUtil.Dispose(_ChildPanel);
			_ChildPanel = null;
			_Data = null;
			_Parent = null;
			_DeleteButton = null;
			_Header = null;
			
			super.Disposing();
		}		
		
		public function get Data():*
		{
			return _Data;
		}

		public function TableRowItem(settings:ReportSettings, parent:Array, item:TableRowSetting)
		{
			super("");
			_Header = new RowItemHeader();
			_Header.x = 0-HEADER_WIDTH;
			_Header.width = HEADER_WIDTH;
			_Header.addEventListener(MouseEvent.MOUSE_DOWN, OnMouseEvent);
			addChild(_Header);
			
			_DeleteButton = new ToolButton();
			_DeleteButton.toolTip = "删除";
			_DeleteButton.height = 16;
			_DeleteButton.width = 16;
			_DeleteButton.visible = false;
			_DeleteButton.setStyle("icon", Asset.ICON_DELETE12);
			_DeleteButton.addEventListener(MouseEvent.CLICK, OnButtonMouseEvent);
			addChild(_DeleteButton);

			_ChildPanel = new UIComponent();
			addChild(_ChildPanel);

			_Ext = new DividableExtension(this, true, GetLineParent, OnDivided);
			_Ext.Width = 6;
			_Ext.MinTop = 6;

			OnResize();

			addEventListener(MouseEvent.MOUSE_MOVE, OnMouseEvent);
			addEventListener(DragEvent.DRAG_ENTER, OnDragEvent);
			addEventListener(DragEvent.DRAG_DROP, OnDragEvent);
			
			_Title.visible = false;

			Settings = settings;
			_Data = item;
			_Parent = parent;

			DesignUtil.AddItems(_Data.TableCellSettings, _Data, Settings, _ChildPanel);
			Refresh();
			RefreshWidth();
			RefreshText();
		}
 
		private function OnDragEvent(event:DragEvent):void
		{
			if (!event.dragSource.hasFormat("TableRowItem") || event.currentTarget != this)
				return;
			if (event.type == DragEvent.DRAG_ENTER)
			{
				if (event.dragInitiator == this || event.dragInitiator.parent != parent)
					return;
				DragManager.acceptDragDrop(this);
			}
			else if (event.type == DragEvent.DRAG_DROP)
			{
				var srcItem:ITableRowItem = event.dragInitiator as ITableRowItem;
				if (!srcItem)
					return;
				var list:Array = _Parent;
				var srcIndex:int = list.indexOf(srcItem.Data);
				list.splice(srcIndex,1);
				var destIndex:int = list.indexOf(Data);
				list.splice(destIndex, 0, srcItem.Data);
				
				var group:DesignGroup = srcItem as DesignGroup;
				group.parent.removeChild(group);
				parent.addChildAt(group, destIndex);
				
				var evt:DesignEvent = new DesignEvent(DesignEvent.ROW_DROP, true);
				dispatchEvent(evt);
			}
		}
		private function ReportItemMouseDown(append:Boolean):void
		{
			var e:DesignEvent = new DesignEvent(DesignEvent.ITEM_MOUSE_DOWN, true);
			e.Append = append;
			dispatchEvent(e);
		}

		private function OnMouseEvent(event:MouseEvent):void
		{
			if (event.type == MouseEvent.MOUSE_DOWN)
			{

				if (event.currentTarget == _Header)
				{
					setFocus();
					ReportItemMouseDown(event.ctrlKey);

					_Dragable = !_Ext.IsDividing;
					if (_DeleteButton.hitTestPoint(event.stageX, event.stageY))
					{
						_Dragable = false;
					}
				}
			}
			else if (event.type == MouseEvent.MOUSE_MOVE)
			{
				if (_Dragable && !DragManager.isDragging)
				{
					var ds:DragSource = new DragSource();
					ds.addData("TableRowItem", "TableRowItem");
					DragManager.doDrag(this, ds, event, DesignUtil.CreateDragImage(this));
					_Dragable = false;
				}
			}
		}
		private function GetLineParent():*
		{
			return parent;
		}
		
		private function OnDivided(oldValue:Number, newValue:Number):void
		{
			var value:Number;
			
			value = Number(Parser.GetUnit(newValue, Settings.Unit).toFixed(1));
			_Data.Height = value;
			RefreshHeight(true);
		}

		protected override function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
		{
			graphics.clear();
			graphics.beginFill(0, 0);
			graphics.drawRect(0-HEADER_WIDTH, 0, unscaledWidth+HEADER_WIDTH*2, unscaledHeight);
			graphics.endFill();
 
			if (_RollOver)
			{
				graphics.lineStyle(1, 0xbbbbbb, 1);
				graphics.drawRect(0-HEADER_WIDTH, 0, unscaledWidth+HEADER_WIDTH*2, unscaledHeight);
			}
		}
 
		public function RefreshColSpan():void
		{
			var cell:TableCellItem;	
			var i:int, j:int;
			for (i = 0; i < _ChildPanel.numChildren; i++)
			{
				cell = _ChildPanel.getChildAt(i) as TableCellItem;
				cell.visible = cell.Data.Visible;
			}
			RefreshWidth();
		}
		public function RefreshRowSpan():void
		{
			var cell:TableCellItem;	
			var i:int, j:int;
			for (i = 0; i < _ChildPanel.numChildren; i++)
			{
				cell = _ChildPanel.getChildAt(i) as TableCellItem;
				cell.visible = cell.Data.Visible;
				cell.height = Settings.GetRowSpanHeight(_Parent, cell.Data.RowIndex, cell.Data.RowSpan);
			}
		}
 
		public function RefreshText():void
		{
			//拖放时索引改变
			var i:int = _Parent.indexOf(_Data) + 1;
			_Title.text = i.toString();
			LayoutUtil.CenterLayout(_Title, HEADER_WIDTH, height, 0-HEADER_WIDTH, 0);
		}

		public function RefreshWidth():void
		{
			if (_Data == null)
				return;
 
			var cellLeft:Number = 0;
			for (var i:int = 0; i < _ChildPanel.numChildren; i++)
			{
				var cell:TableCellItem = _ChildPanel.getChildAt(i) as TableCellItem;
				var cellWidth:Number = Settings.GetColSpanWidth(i, cell.Data.ColSpan);
				var colWidth:Number = Settings.GetColumnWidth(i);
				cell.x = cellLeft;
				cell.width = cellWidth;
				cellLeft+=colWidth;
			}
		}
		public function DeleteCellAt(index:int):void
		{
			if (index >= 0 && index<_Data.TableCellSettings.length)
				_Data.TableCellSettings.splice(index,1);
			var item:TableCellItem = _ChildPanel.getChildAt(index) as TableCellItem;
			item.Data.Dispose();
			item.parent.removeChild(item);
			item.Dispose();
		}
		public function AddCell(type:String, title:String, column:String):void
		{
			switch(type)
			{
				case "表格头":
				case "分组头":
					column = "";
					break;
				case "表格主体":
					title = "";
					break;
				case "表格尾":
				case "分组尾":
					title = "";
					column = "";
					break;
			}
			
			var cell:TableCellSetting;
			cell = new TableCellSetting();
			cell.Value = title;
			cell.BindingColumn = column;
			_Data.TableCellSettings.push(cell);
			var item:DisplayObject = DesignUtil.CreateItem(cell, _Data, Settings);
			item.height = _ChildPanel.height;
			_ChildPanel.addChild(item);
		}

		private function OnButtonMouseEvent(event:MouseEvent):void
		{
			if (_Data == null)
				return;
			if (event.currentTarget == _DeleteButton)
			{
				MsgUtil.ShowQuestion("确定要删除该行吗？", function():void
					{
						var event:DesignEvent = new DesignEvent(DesignEvent.DELETE, true);
						dispatchEvent(event);
					});
			}
		}

		override protected function OnRollOver(event:MouseEvent):void
		{
			_Title.visible = true;
			_DeleteButton.visible = true;
		}

		override protected function OnRollOut(event:MouseEvent):void
		{
			_Title.visible = _Selected || _RollOver;
			_DeleteButton.visible = _Selected || _RollOver;
		}

		override protected function OnResize():void
		{
			_ChildPanel.height = height;
			_ChildPanel.width = width;	
			
			_Header.height = height;
			_Header.width = width+HEADER_WIDTH*2;
			
			LayoutUtil.CenterLayout(_Title, HEADER_WIDTH, height, 0-HEADER_WIDTH, 0);
			LayoutUtil.CenterLayout(_DeleteButton, HEADER_WIDTH, height, width, 0);
		}

		private var _Selected:Boolean = false;

		public function get Selected():Boolean
		{
			return _Selected;
		}

		public function set Selected(value:Boolean):void
		{
			if (_Selected == value || Disposed)
				return;
			_Selected = value;
			
			_Title.visible = _Selected || _RollOver;
			_DeleteButton.visible = _Selected || _RollOver;
			_Header.Selected = _Selected;
			invalidateDisplayList();
		}

		public function get Editor():DisplayObject
		{
			TableRowEditor.Instance.SetData(Settings, _Data);
			return TableRowEditor.Instance;
		}

		public function Refresh():void
		{
			
			RefreshHeight();
 
		}

		private function RefreshHeight(refreshEditor:Boolean = false):void
		{
			var h:Number = Parser.GetPixel(_Data.Height, Settings.Unit);
			var event:DesignEvent = null;
			if (height != h)
				event = new DesignEvent(DesignEvent.HEIGHT_CHANGED, true);
			height = h;
 
			if (event != null)
			{
				dispatchEvent(event);
				if (refreshEditor)
				{
					TableRowEditor.Instance.Refresh();
				}
			}
		}
		
		internal function GetCell(c:int):TableCellItem
		{
			return _ChildPanel.getChildAt(c) as TableCellItem;
		}
	}
}