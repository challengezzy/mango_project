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
	
	import mx.core.DragSource;
	import mx.core.UIComponent;
	import mx.events.DragEvent;
	import mx.events.MenuEvent;
	import mx.managers.DragManager;
	
	import myreport.data.report.CaptionCellSetting;
	import myreport.data.report.CaptionRowSetting;
	import myreport.data.report.ReportSettings;
	import myreport.res.Asset;
	import myreport.util.ToolButton;

	internal class CaptionRowItem extends DesignGroup implements IDesignItem
	{
		private static var HEADER_WIDTH:Number = 20;
 
		private var _AddButton:ToolButton;
		private var _DeleteButton:ToolButton;
		private var _ChildPanel:UIComponent;
		private var _Header:RowItemHeader;
 
		private var _Ext:DividableExtension;

		private var _Dragable:Boolean = false;

		private var _Data:CaptionRowSetting;
		private var _Parent:Array;
		
		protected override function Disposing():void
		{
			_AddButton.removeEventListener(MouseEvent.CLICK, OnButtonMouseEvent);
			_DeleteButton.removeEventListener(MouseEvent.CLICK, OnButtonMouseEvent);
			_Header.removeEventListener(MouseEvent.MOUSE_DOWN, OnMouseEvent);
			DisposeUtil.Dispose(_ChildPanel);
			
			_Ext.Dispose();
			_Ext = null;
			_Data = null;
			_Parent = null;
			_ChildPanel = null;
			_AddButton = null;
			_DeleteButton = null;
			_Header = null;
			super.Disposing();
		}
		


		public function CaptionRowItem(settings:ReportSettings, parent:Array, item:CaptionRowSetting)
		{
			super("标题行");
			_Header = new RowItemHeader();
			_Header.x = 0-HEADER_WIDTH;
			_Header.width = HEADER_WIDTH;
			_Header.addEventListener(MouseEvent.MOUSE_DOWN, OnMouseEvent);
			addChild(_Header);
			
			_AddButton = new ToolButton();
			_AddButton.toolTip = "添加单元格";
			_AddButton.height = 16;
			_AddButton.width = 16;
			_AddButton.visible = false;
			_AddButton.setStyle("icon", Asset.ICON_ADD12);
			_AddButton.addEventListener(MouseEvent.CLICK, OnButtonMouseEvent);
			addChild(_AddButton);

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
			addEventListener(DesignEvent.CELL_WIDTH_CHANGED, OnMyReportEvent);
			addEventListener(DesignEvent.DELETE, OnMyReportEvent);
			addEventListener(MouseEvent.MOUSE_MOVE, OnMouseEvent);
			addEventListener(DragEvent.DRAG_ENTER, OnDragEvent);
			addEventListener(DragEvent.DRAG_DROP, OnDragEvent);
			_Title.visible = false;

			Settings = settings;
			_Data = item;
			_Parent = parent;

			DesignUtil.AddItems(_Data.CaptionCellSettings, _Data, Settings, _ChildPanel);
			Refresh();
			RefreshWidth();
			RefreshText();
		}
		public function get Data():*
		{
			return _Data;
		}
		private function OnDragEvent(event:DragEvent):void
		{
			if (!event.dragSource.hasFormat("CaptionRowItem") || event.currentTarget != this)
				return;
			if (event.type == DragEvent.DRAG_ENTER)
			{
				if (event.dragInitiator == this || event.dragInitiator.parent != parent)
					return;
 
				DragManager.acceptDragDrop(this);
			}
			else if (event.type == DragEvent.DRAG_DROP)
			{
				var srcItem:CaptionRowItem = event.dragInitiator as CaptionRowItem;
				if (srcItem == null)
					return;
				var list:Array = _Parent;
				var srcIndex:int = list.indexOf(srcItem._Data);
				list.splice(srcIndex, 1);			
				var destIndex:int = list.indexOf(_Data);
				list.splice(destIndex, 0, srcItem._Data);
				srcItem.parent.removeChild(srcItem);
				parent.addChildAt(srcItem, destIndex);
				
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
					if (_DeleteButton.hitTestPoint(event.stageX, event.stageY) || _AddButton.hitTestPoint(event.stageX, event.stageY))
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
					ds.addData("CaptionRowItem", "CaptionRowItem");
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
			if(!_DeleteButton) 
				return;
			graphics.beginFill(0, 0);
			graphics.drawRect(0-HEADER_WIDTH, 0, unscaledWidth+HEADER_WIDTH*2 + _DeleteButton.width, unscaledHeight);
			graphics.endFill();

			if (_RollOver)
			{
				graphics.lineStyle(1, 0xbbbbbb, 1);
				graphics.drawRect(0-HEADER_WIDTH, 0, unscaledWidth+HEADER_WIDTH*2 + _DeleteButton.width, unscaledHeight);
			}
			
			if (_Data )
			{
				graphics.lineStyle(1, 0x000000, 1);
 				if(_Data.LeftBorder)
				{
					graphics.moveTo(0, 0);
					graphics.lineTo(0, height);
				}
				if(_Data.TopBorder)
				{
					graphics.moveTo(0, 0);
					graphics.lineTo(width, 0);
				}
				if(_Data.RightBorder)
				{
					graphics.moveTo(width, 0);
					graphics.lineTo(width, height);
				}
				if(_Data.BottomBorder)
				{
					graphics.moveTo(0, height);
					graphics.lineTo(width, height);
				}
			}
		}

		private function OnMyReportEvent(event:DesignEvent):void
		{
			//trace(event.type);
			if(event.type == DesignEvent.CELL_WIDTH_CHANGED)
			{
				RefreshWidth();
				event.stopPropagation();
			}
			else if (event.type == DesignEvent.DELETE)
			{
				var item:CaptionCellItem = event.target as CaptionCellItem;
				if (item == null)
					return;
				var index:int = _Data.CaptionCellSettings.indexOf(item.Data);
				if (index >= 0)
					_Data.CaptionCellSettings.splice(index,1);
				if (item.Data is IDispose)
					IDispose(item.Data).Dispose();
				item.parent.removeChild(item);
				item.Dispose();
				RefreshWidth();
			}
		}
		public function RefreshText():void
		{
			//拖放时索引改变
			var i:int = _Parent.indexOf(_Data) + 1;
			_Title.text = i.toString();
			LayoutUtil.CenterLayout(_Title, HEADER_WIDTH, height, 0-HEADER_WIDTH, 0);
		}
		private function RefreshWidth():void
		{
			if (_Data == null)
				return;
				
			var w:Number = 0;
			for (var i:int = 0; i < _ChildPanel.numChildren; i++)
			{
				var item:DisplayObject = _ChildPanel.getChildAt(i);
				item.x = w;
				w += item.width;
			}	
		}

		private function OnButtonMouseEvent(event:MouseEvent):void
		{
			if (_Data == null)
				return;
			if (event.currentTarget == _AddButton)
			{
				ShowMenu();
				

			}
			else if (event.currentTarget == _DeleteButton)
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
			_AddButton.visible = true;
			_DeleteButton.visible = true;
		}

		override protected function OnRollOut(event:MouseEvent):void
		{
			_Title.visible = _Selected || _RollOver;
			_AddButton.visible = _Selected || _RollOver;
			_DeleteButton.visible = _Selected || _RollOver;
		}

		override protected function OnResize():void
		{
			_ChildPanel.width = width;
			_ChildPanel.height = height;

			_Header.height = height;
			_Header.width = width+HEADER_WIDTH*2+_DeleteButton.width;
			
			LayoutUtil.CenterLayout(_Title, HEADER_WIDTH, height, 0-HEADER_WIDTH, 0);
			
			LayoutUtil.CenterLayout(_AddButton, HEADER_WIDTH, height, width, 0);
			LayoutUtil.CenterLayout(_DeleteButton, HEADER_WIDTH, height, width+_AddButton.width, 0);
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
			_AddButton.visible = _Selected || _RollOver;
			_DeleteButton.visible = _Selected || _RollOver;
			_Header.Selected = _Selected;
			invalidateDisplayList();
		}

		public function get Editor():DisplayObject
		{
			CaptionRowEditor.Instance.SetData(Settings, _Data);
			return CaptionRowEditor.Instance;
		}

		public function Refresh():void
		{
			RefreshHeight();
			invalidateDisplayList();
		}

		private function RefreshHeight(refreshEditor:Boolean = false):void
		{
			var h:Number = Parser.GetPixel(_Data.Height, Settings.Unit);
			var event:DesignEvent = null;
			if (height != h)
				event = new DesignEvent(DesignEvent.HEIGHT_CHANGED, true);

			height = h;

			for (var i:int = 0; i < _ChildPanel.numChildren; i++)
			{
				var item:DisplayObject = _ChildPanel.getChildAt(i);
				item.height = h;
			}

			if (event != null)
			{
				dispatchEvent(event);
				if (refreshEditor)
				{
					CaptionRowEditor.Instance.Refresh();
				}
			}
		}
 
		private function ShowMenu():void
		{
			var p:Point = localToGlobal(new Point(_AddButton.x, _AddButton.y));
			CaptionRowMenu.Instance.Show(p.x, p.y, OnMenuItemClick);

		}
		private function OnMenuItemClick(command:String):void
		{
			var action:String = command;
			if(action == "AddCell")
			{
				AddCells(1);
			}
			else if(action == "AddCells")
			{
				NewCellsForm.Instance.Show(AddCells);
			}
		}
 
		private function AddCells(count:int):void
		{
			var w:Number = 0;
			var cell:CaptionCellSetting;
			for each(cell in _Data.CaptionCellSettings)
			{
				w+=cell.Width;
			}
			w = (Settings.ClientWidth-w)/count;
			w = Number(w.toFixed(1));
			if(w<0)
				w = 8;
			for(var i:int=0;i<count;i++)
			{
				cell = new CaptionCellSetting();
				cell.Value="请输入内容...";
				cell.Width = w;
				_Data.CaptionCellSettings.push(cell);
				var item:DisplayObject = DesignUtil.CreateItem(cell, _Data, Settings);
				item.height = _ChildPanel.height;
				_ChildPanel.addChild(item);
			}

			RefreshWidth();
		}
	}
}