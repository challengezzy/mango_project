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
	
	import hlib.DrawUtil;
	import hlib.LayoutUtil;
	import hlib.MsgUtil;
	
	import mx.core.DragSource;
	import mx.events.DragEvent;
	import mx.managers.DragManager;
	
	import myreport.data.report.ReportSettings;
	import myreport.data.report.SubReportRowSetting;
	import myreport.res.Asset;
	import myreport.util.ToolButton;

	internal class SubReportRowItem extends DesignGroup implements IDesignItem, ITableRowItem
	{
		private static const HEADER_HEIGHT:Number = 16;
		private var _Data:SubReportRowSetting;
		private var _Parent:Array;
		private var _TableHeader:TableRowGroup;
		private var _TableDetail:TableRowGroup;
		private var _TableFooter:TableRowGroup;
		private var _DeleteButton:ToolButton;
	 
		private var _Dragable:Boolean = false;
		
		protected override function Disposing():void
		{
			_DeleteButton.removeEventListener(MouseEvent.CLICK, OnButtonMouseEvent);
			
			_Data = null;
			_Parent = null;
			_DeleteButton = null;
 
			super.Disposing();
		}
		
		public function SubReportRowItem(settings:ReportSettings, parent:Array, item:SubReportRowSetting)
		{
			super("子报表行");
			
			_DeleteButton = new ToolButton();
			_DeleteButton.toolTip = "删除";
			_DeleteButton.height = 16;
			_DeleteButton.width = 16;
			_DeleteButton.visible = false;
			_DeleteButton.setStyle("icon", Asset.ICON_DELETE12);
			_DeleteButton.addEventListener(MouseEvent.CLICK, OnButtonMouseEvent);
			addChild(_DeleteButton);
			
			_TableHeader = new TableRowGroup("子表格头");
			_TableDetail = new TableRowGroup("子表格主体");
			_TableFooter = new TableRowGroup("子表格尾");
			addChild(_TableHeader);
			addChild(_TableDetail);
			addChild(_TableFooter);

			Settings = settings;
			_Data = item;
			_Parent = parent;
			
			_TableHeader.SetData(settings, _Data.TableHeaderSettings);
			_TableDetail.SetData(settings, _Data.TableDetailSettings);
			_TableFooter.SetData(settings, _Data.TableFooterSettings);
			
			RefreshHeight();
			Refresh();
			addEventListener(DesignEvent.HEIGHT_CHANGED, OnMyReportEvent);
			addEventListener(DragEvent.DRAG_ENTER, OnDragEvent);
			addEventListener(DragEvent.DRAG_DROP, OnDragEvent);
			addEventListener(MouseEvent.MOUSE_DOWN, OnMouseEvent);
			addEventListener(MouseEvent.MOUSE_MOVE, OnMouseEvent);
		}
		private function OnButtonMouseEvent(event:MouseEvent):void
		{
			if (_Data == null)
				return;
			if (event.currentTarget == _DeleteButton)
			{
				MsgUtil.ShowQuestion("确定要删除该子报表行吗？", function():void
				{
					var event:DesignEvent = new DesignEvent(DesignEvent.DELETE, true);
					dispatchEvent(event);
				});
			}
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
		override protected function OnRollOver(event:MouseEvent):void
		{
			_DeleteButton.visible = true;
		}
		
		override protected function OnRollOut(event:MouseEvent):void
		{
			_DeleteButton.visible = _Selected || _RollOver;
		}
		private function OnMyReportEvent(event:DesignEvent):void
		{
			//trace(event.type);
			if (event.type == DesignEvent.HEIGHT_CHANGED)
			{
				if(event.target != this)
					RefreshHeight();
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
				if (event.currentTarget != this)
					return;
				var p:Point = globalToLocal(new Point(event.stageX, event.stageY));
				if(p.y>=0 && p.y<= HEADER_HEIGHT)
				{
					setFocus();	
					ReportItemMouseDown(false);
					_Dragable = true;
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
			_DeleteButton.visible = _Selected || _RollOver;
			invalidateDisplayList();
		}
		public function get Data():*
		{
			return _Data;
		}
		protected override function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
		{
			graphics.clear();

			if (_RollOver)
				graphics.beginFill(0xdddddd, 0.2);
			else
				graphics.beginFill(0, 0);
			graphics.drawRect(0, 0, unscaledWidth, unscaledHeight);
			graphics.endFill();
			
			if (Selected)
				graphics.beginFill(0xF9FF9D, 0.5);
			else if (_RollOver)
				graphics.beginFill(0xdddddd, 0.5);	
			else
				graphics.beginFill(0, 0);
			var h:Number = HEADER_HEIGHT;
			graphics.drawRect(0, 0, unscaledWidth, h);
			graphics.endFill();
			
			if (_RollOver)
			{
				graphics.lineStyle(1, 0xF0B22D, 1);
				_Title.textColor = 0xF0B22D;
			}
			else
			{
				graphics.lineStyle(1, 0xbbbbbb, 1);
				_Title.textColor = 0xbbbbbb;
			}
			DrawUtil.DrawDashRect(graphics, 0, 0, unscaledWidth, unscaledHeight);
		}		
		override protected function OnResize():void
		{
			_TableHeader.width = width;
			_TableDetail.width = width;
			_TableFooter.width = width;
			_DeleteButton.x = width - _DeleteButton.width;
 
		}
		public function get Editor():DisplayObject
		{
			SubReportRowEditor.Instance.SetData(Settings, _Data);
			return SubReportRowEditor.Instance;
		}
		public function RefreshHeight():void
		{
			var h:Number = HEADER_HEIGHT;
			_TableHeader.y = h;
			h += _TableHeader.height;
			_TableDetail.y = h;
			h += _TableDetail.height;
			_TableFooter.y = h;
			h += _TableFooter.height;
			height = h;
		}
		public function Refresh():void
		{
			_Title.text = "子报表行";
			
		}
		
		public function RefreshColSpan():void
		{
			_TableHeader.RefreshColSpan();
			_TableDetail.RefreshColSpan();
			_TableFooter.RefreshColSpan();
		}
		public function RefreshRowSpan():void
		{
			_TableHeader.RefreshRowSpan();
			_TableDetail.RefreshRowSpan();
			_TableFooter.RefreshRowSpan();
		}
		public function RefreshText():void
		{
			_TableHeader.RefreshText();
			_TableDetail.RefreshText();
			_TableFooter.RefreshText();
		}
		public function RefreshWidth():void
		{
			_TableHeader.RefreshWidth();
			_TableDetail.RefreshWidth();
			_TableFooter.RefreshWidth();
		}
		public function DeleteCellAt(index:int):void
		{
			_TableHeader.DeleteCellAt(index);
			_TableDetail.DeleteCellAt(index);
			_TableFooter.DeleteCellAt(index);
		}
		public function AddCell(type:String, title:String, column:String):void
		{
			_TableHeader.AddCell(title, "");
			_TableDetail.AddCell("", column);
			_TableFooter.AddCell("", "");
		}
 
	}
}