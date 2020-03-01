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
	
	import hlib.ComponentBase;
	import hlib.DisposeUtil;
	import hlib.DrawUtil;
	import hlib.IDispose;
	import hlib.LayoutUtil;
 
	import hlib.MsgUtil;
	import hlib.TextBase;
	
	import mx.collections.ArrayCollection;
	import mx.core.DragSource;
	import mx.events.DragEvent;
	import mx.events.ResizeEvent;
	import mx.managers.DragManager;
	
	import myreport.data.report.Define;
	import myreport.data.report.ReportSettings;
	import myreport.data.report.SubReportRowSetting;
	import myreport.data.report.TableCellSetting;
	import myreport.data.report.TableRowSetting;
	import myreport.design.cellrender.DesignCellRenderBase;
	import myreport.design.cellrender.DesignCellRenderUtil;
	import myreport.expression.ExpressionEngine;
	import myreport.res.Asset;
	import myreport.util.ToolButton;

	internal class TableCellItem extends ComponentBase implements IDesignItem, IDispose
	{

		private var _RollOver:Boolean = false;

		private var _Settings:ReportSettings;
		private var _Data:TableCellSetting;
		private var _Row:TableRowSetting;
 
		private var _ControlType:String;
		private var _Render:DesignCellRenderBase;
		
		private var _OldColSpan:uint = 1;
		private var _OldRowSpan:uint = 1;
		
		private var _Dragable:Boolean = false;
		//================IDispose====================
		protected override function Disposing():void
		{
 
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

		public function TableCellItem(settings:ReportSettings, row:TableRowSetting, item:TableCellSetting)
		{
			super();
			_Settings = settings;
			_Data = item;
			_Row = row;

			width = 100;
			height = 16;
 
			addEventListener(MouseEvent.MOUSE_DOWN, OnMouseEvent);
			addEventListener(MouseEvent.ROLL_OVER, OnMouseEvent);
			addEventListener(MouseEvent.ROLL_OUT, OnMouseEvent);
			addEventListener(DragEvent.DRAG_ENTER, OnDragEvent);
			addEventListener(DragEvent.DRAG_DROP, OnDragEvent);
			addEventListener(MouseEvent.MOUSE_MOVE, OnMouseEvent);

			_OldColSpan = _Data.ColSpan;
			_OldRowSpan = _Data.RowSpan;

			Refresh();
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

		private function OnDragEvent(event:DragEvent):void
		{
			if (!event.dragSource.hasFormat("TableCellItem") || event.currentTarget != this)
				return;
			if (event.type == DragEvent.DRAG_ENTER)
			{
				if (event.dragInitiator == this || event.dragInitiator.parent != parent)
					return;
 
				DragManager.acceptDragDrop(this);
			}
			else if (event.type == DragEvent.DRAG_DROP)
			{
 
				var srcItem:TableCellItem = event.dragInitiator as TableCellItem;
				if (!srcItem)
					return;
 				
				var list:Array = _Row.TableCellSettings;
 
				var srcIndex:int = list.indexOf(srcItem._Data);
				list.splice(srcIndex, 1);
				var destIndex:int = list.indexOf(_Data);
				list.splice(destIndex, 0, srcItem._Data);
				
				srcItem.parent.removeChild(srcItem);
				parent.addChildAt(srcItem, destIndex);
 
				var evt:DesignEvent = new DesignEvent(DesignEvent.CELL_DROP, true);
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
				if (event.currentTarget == this)
				{
					setFocus();
					ReportItemMouseDown(event.ctrlKey);
				}
				_Dragable = true;
			}
			else if (event.type == MouseEvent.MOUSE_MOVE)
			{
				//trace(_Dragable, DragManager.isDragging);
				if (_Dragable && !DragManager.isDragging)
				{
					//trace("drag...");s
					var ds:DragSource = new DragSource();
					ds.addData("TableCellItem", "TableCellItem");
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
			
			
			if (_Data && _Data.Style )
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
			var settings:ReportSettings = _Settings;
			var subItem:SubReportRowItem = hlib.LayoutUtil.FindType(parent, SubReportRowItem);
			if(subItem)
			{
				var sub:SubReportRowSetting = subItem.Data;
				settings = new ReportSettings();
				settings.TableColumnSettings = _Settings.TableColumnSettings;
				settings.ParameterData = ExpressionEngine.InvokeByData(sub.SubReportParameters,
					_Settings.TableData, _Settings.ParameterData);
				settings.TableData = ExpressionEngine.InvokeByData(sub.SubReportTable,
					_Settings.TableData, _Settings.ParameterData) as ArrayCollection;

			}
	 
			TableCellEditor.Instance.SetData(settings, _Data);
			return TableCellEditor.Instance;
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
 
			OnResize();
			invalidateDisplayList();
			var e:DesignEvent;
			if(_OldColSpan != _Data.ColSpan)
			{
				_OldColSpan = _Data.ColSpan;
				e = new DesignEvent(DesignEvent.COL_SPAN_CHANGED, true);
				dispatchEvent(e);
			}
			if(_OldRowSpan != _Data.RowSpan)
			{
				_OldRowSpan = _Data.RowSpan;
				e = new DesignEvent(DesignEvent.ROW_SPAN_CHANGED, true);
				dispatchEvent(e);
			}
		}

	}
}