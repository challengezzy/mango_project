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
	import flash.text.TextField;
	import flash.ui.ContextMenuItem;
	
	import hlib.ComponentBase;
	import hlib.DisposeUtil;
	import hlib.DrawUtil;
	import hlib.IDispose;
	import hlib.MovableExtension;
	import hlib.MsgUtil;
	import hlib.Parser;
	import hlib.TextBase;
	
	import mx.core.DragSource;
	import mx.core.UIComponent;
	import mx.events.DragEvent;
	import mx.managers.DragManager;
	
	import myreport.data.report.CaptionCellSetting;
	import myreport.data.report.CaptionRowSetting;
	import myreport.data.report.Define;
	import myreport.data.report.ReportSettings;
	import myreport.data.report.TableCellSetting;
	import myreport.design.cellrender.DesignCellRenderBase;
	import myreport.design.cellrender.DesignCellRenderUtil;
	import myreport.res.Asset;
	import myreport.util.ContextMenuExtension;
	import myreport.util.ToolButton;

	internal class CaptionCellItem extends ComponentBase implements IDesignItem, IDispose
	{
		private var _RollOver:Boolean = false;

		private var _Settings:ReportSettings;
		private var _Data:CaptionCellSetting;
		private var _Row:CaptionRowSetting;

		private var _ControlType:String;
		private var _Render:DesignCellRenderBase;
 
		private var _Ext:DividableExtension;

		private var _Dragable:Boolean = false;
		
		private var _Menu:ContextMenuExtension;
 
		protected override function Disposing():void
		{

			if (_Ext)
			{
				_Ext.Dispose();
				_Ext = null;
			}
			if(_Menu)
			{
				_Menu.Dispose();
				_Menu = null;
				contextMenu = null;
			}
			
			_Settings = null;
			_Data = null;
			_Row = null;
			
			_Render = null;
			
			super.Disposing();
		}
 
		public function get Data():*
		{
			return _Data;
		}
		public function CaptionCellItem(settings:ReportSettings, row:CaptionRowSetting, item:CaptionCellSetting)
		{
			super();
			_Settings = settings;
			_Data = item;
			_Row = row;
 
			height = 16;
			
			_Ext = new DividableExtension(this, false, GetLineParent, OnDivided);
 			_Ext.Width = 6;
			_Ext.MinLeft = 6;
 
			_Menu = new ContextMenuExtension(["删除单元格"], OnMenuShow, OnMenuItemClick);
			contextMenu = _Menu.Memu;

			addEventListener(MouseEvent.MOUSE_DOWN, OnMouseEvent);
			addEventListener(MouseEvent.ROLL_OVER, OnMouseEvent);
			addEventListener(MouseEvent.ROLL_OUT, OnMouseEvent);
			addEventListener(DragEvent.DRAG_ENTER, OnDragEvent);
			addEventListener(DragEvent.DRAG_DROP, OnDragEvent);
			addEventListener(MouseEvent.MOUSE_MOVE, OnMouseEvent);

			
			Refresh();
		}
		private function OnMenuShow():void
		{
			_Menu.GetItem("删除单元格").visible = Selected;
		}
		private function OnMenuItemClick(item:ContextMenuItem):void
		{
			if(item.caption == "删除单元格")
			{
				MsgUtil.ShowQuestion("确定要删除该单元格吗？", function():void
				{
					var event:DesignEvent = new DesignEvent(DesignEvent.DELETE, true);
					dispatchEvent(event);
				});
			}
		}
		protected override function OnResize():void
		{
			if (_Data == null)
				return;
 
			if(_Render)
			{
				_Render.width = width;
				_Render.height = height;
			}

		}
		
		private function GetLineParent():*
		{
			return parent;
		}
		
		private function OnDivided(oldValue:Number, newValue:Number):void
		{
			var value:Number;
			
			value = Number(Parser.GetUnit(newValue, _Settings.Unit).toFixed(1));
			_Data.Width = value;
			RefreshWidth(true);
		}
 
		private function RefreshWidth(refreshEditor:Boolean = false):void
		{
			if (_Data == null)
				return;
			var w:Number = Parser.GetPixel(_Data.Width, _Settings.Unit);
			var event:DesignEvent = null;
			if (width != w)
				event = new DesignEvent(DesignEvent.CELL_WIDTH_CHANGED, true);
			width = w;
			if (event != null)
				dispatchEvent(event);

			if (refreshEditor)
			{
				invalidateDisplayList();
				CaptionCellEditor.Instance.Refresh();
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
			invalidateDisplayList();
		}
		
		private function ReportItemMouseDown(append:Boolean):void
		{
			var e:DesignEvent = new DesignEvent(DesignEvent.ITEM_MOUSE_DOWN, true);
			e.Append = append;
			dispatchEvent(e);
		}

		private function OnDragEvent(event:DragEvent):void
		{
			if (!event.dragSource.hasFormat("CaptionCellItem") || event.currentTarget != this)
				return;
			if (event.type == DragEvent.DRAG_ENTER)
			{
				if (event.dragInitiator == this || event.dragInitiator.parent != parent)
					return;
 
				DragManager.acceptDragDrop(this);
			}
			else if (event.type == DragEvent.DRAG_DROP)
			{
 
				var srcItem:CaptionCellItem = event.dragInitiator as CaptionCellItem;
				if (!srcItem)
					return;
				
				var list:Array = _Row.CaptionCellSettings;
				
				var srcIndex:int = list.indexOf(srcItem._Data);
				list.splice(srcIndex, 1);
				var destIndex:int = list.indexOf(_Data);
				list.splice(destIndex, 0, srcItem._Data);
				
				srcItem.parent.removeChild(srcItem);
				parent.addChildAt(srcItem, destIndex);
				var evt:DesignEvent = new DesignEvent(DesignEvent.CELL_WIDTH_CHANGED, true);
				dispatchEvent(evt);
			}
		}
		private function OnMouseEvent(event:MouseEvent):void
		{
			if (event.type == MouseEvent.MOUSE_DOWN)
			{
				if (event.currentTarget == this)
				{
					setFocus();
					ReportItemMouseDown(event.ctrlKey);
					
					_Dragable = !_Ext.IsDividing;
				}
			}
			else if (event.type == MouseEvent.MOUSE_MOVE)
			{
				if (_Dragable && !DragManager.isDragging)
				{
					var ds:DragSource = new DragSource();
					ds.addData("CaptionCellItem", "CaptionCellItem");
					DragManager.doDrag(this, ds, event, DesignUtil.CreateDragImage(this));
					_Dragable = false;
				}
			}
			else if (event.type == MouseEvent.ROLL_OVER)
			{
				_RollOver = true;
				invalidateDisplayList();
			}
			else if (event.type == MouseEvent.ROLL_OUT)
			{
				_RollOver = false;
				invalidateDisplayList();
			}
		}

		protected override function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
		{
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			graphics.clear();
			if (Selected)
				graphics.beginFill(0xF9FF9D, 0.5);
			else if (_RollOver)
				graphics.beginFill(0xdddddd, 0.5);
			else
				graphics.beginFill(0, 0);
			graphics.drawRect(0, 0, unscaledWidth, unscaledHeight);
			graphics.endFill();
 
			if (_Data && _Data.Style)
			{
				if(_Data.Style.Border)
				{
					graphics.lineStyle(1, 0x000000, 1);
					
					if(_Data.Style.LeftBorder)
					{
						if(_Data.Style.LeftBorderStyle == Define.BORDER_STYLE_DASH)
							DrawUtil.DrawDashLine(graphics, new Point(0,0), new Point(0, height));
						else if(_Data.Style.LeftBorderStyle == Define.BORDER_STYLE_SOLID)
						{
							graphics.moveTo(0, 0);
							graphics.lineTo(0, height);
						}
					}
					if(_Data.Style.TopBorder)
					{
						if(_Data.Style.TopBorderStyle == Define.BORDER_STYLE_DASH)
							DrawUtil.DrawDashLine(graphics, new Point(0,0), new Point(width, 0));
						else if(_Data.Style.TopBorderStyle == Define.BORDER_STYLE_SOLID)
						{
							graphics.moveTo(0, 0);
							graphics.lineTo(width, 0);
						}
					}
					if(_Data.Style.RightBorder)
					{
						if(_Data.Style.RightBorderStyle == Define.BORDER_STYLE_DASH)
							DrawUtil.DrawDashLine(graphics, new Point(width,0), new Point(width, height));
						else if(_Data.Style.RightBorderStyle == Define.BORDER_STYLE_SOLID)
						{
							graphics.moveTo(width, 0);
							graphics.lineTo(width, height);
						}
					}
					if(_Data.Style.BottomBorder)
					{
						if(_Data.Style.BottomBorderStyle == Define.BORDER_STYLE_DASH)
							DrawUtil.DrawDashLine(graphics, new Point(0,height), new Point(width, height));
						else if(_Data.Style.BottomBorderStyle == Define.BORDER_STYLE_SOLID)
						{
							graphics.moveTo(0, height);
							graphics.lineTo(width, height);
						}
					}
				}
				if(_Data.Style.Underline)
				{
					graphics.lineStyle(1, 0x000000, 1);
					graphics.moveTo(2, height-2);
					graphics.lineTo(width-2, height-2);
					
				}
			}
		}

		public function get Editor():DisplayObject
		{
			CaptionCellEditor.Instance.SetData(_Settings, _Data);
			return CaptionCellEditor.Instance;
		}

		public function Refresh():void
		{
			if(!_Render || _ControlType != _Data.Control.Type)
			{
				if(_Render && contains(_Render))
					removeChild(_Render);
				_Render = DesignCellRenderUtil.CreateRender(_Data.Control.Type, _Settings, _Data);
				_ControlType = _Data.Control.Type;
				addChildAt(_Render, 0);
			}
			if(_Render)
				_Render.Refresh();
 
			RefreshWidth();
			OnResize();
			invalidateDisplayList();
		}

	}
}