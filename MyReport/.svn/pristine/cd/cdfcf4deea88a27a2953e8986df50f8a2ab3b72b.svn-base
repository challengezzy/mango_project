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
	import flash.text.TextField;
	import flash.text.TextFormat;
	import flash.ui.ContextMenuItem;
	
	import hlib.ComponentBase;
	import hlib.DisposeUtil;
	import hlib.IDispose;
	import hlib.LayoutUtil;
	import hlib.MovableExtension;
	import hlib.MsgUtil;
	import hlib.Parser;
	import hlib.TextBase;
	
	import myreport.data.report.ReportSettings;
	import myreport.data.report.TableColumnSetting;
	import myreport.res.Asset;
	import myreport.util.ContextMenuExtension;
	import myreport.util.ToolButton;

	internal class TableColumnItem extends ComponentBase implements IDesignItem, IDispose
	{

		private var _RollOver:Boolean = false;

		private var _Settings:ReportSettings;
		private var _Data:TableColumnSetting;

		private var _Text:TextField;
 
		private var _Ext:DividableExtension;
		private var _Menu:ContextMenuExtension;

		//================IDispose====================
		protected override function Disposing():void
		{
			if (_Ext != null)
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
			DisposeUtil.Dispose(this);
			_Text = null;
			_Settings = null;
			_Data = null;
			super.Disposing();
		}

		public function TableColumnItem(settings:ReportSettings, item:TableColumnSetting)
		{
			super();
			_Settings = settings;
			_Data = item;
  
			var tf:TextFormat = new TextFormat();
			tf.size = 12;
			tf.font = "Simsun";
			tf.color = 0xbbbbbb;
 
 			_Text = hlib.TextBase.CreateText("", tf);
			addChild(_Text);

			_Ext = new DividableExtension(this, false, GetLineParent, OnDivided);
			_Ext.Width = 6;
			_Ext.MinLeft = 6;
			
			_Menu = new ContextMenuExtension(["删除表格列"], OnMenuShow, OnMenuItemClick);
			contextMenu = _Menu.Memu;
			
			addEventListener(MouseEvent.MOUSE_DOWN, OnMouseEvent);
			addEventListener(MouseEvent.ROLL_OVER, OnMouseEvent);
			addEventListener(MouseEvent.ROLL_OUT, OnMouseEvent);
			height = 24;

			Refresh();
			RefreshText();
		}

		public function get Data():*
		{
			return _Data;
		}
		private function OnMenuShow():void
		{
			_Menu.GetItem("删除表格列").visible = Selected;
		}
		private function OnMenuItemClick(item:ContextMenuItem):void
		{
			if(item.caption == "删除表格列")
			{
				MsgUtil.ShowQuestion("确定要删除该表格列吗？", function():void
				{
					var event:DesignEvent = new DesignEvent(DesignEvent.DELETE, true);
					dispatchEvent(event);
				});
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
		private function OnMouseEvent(event:MouseEvent):void
		{
			if (event.type == MouseEvent.MOUSE_DOWN)
			{
				if (event.currentTarget == this)
				{
					setFocus();
					ReportItemMouseDown(event.ctrlKey);
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
		private function GetLineParent():*
		{
			return hlib.LayoutUtil.FindType(parent, DesignContent);
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
				event = new DesignEvent(DesignEvent.COLUMN_WIDTH_CHANGED, true);
			width = w;
			if (event != null)
				dispatchEvent(event);

			LayoutUtil.CenterLayout(_Text, width, height, 0, 0);

			if (refreshEditor)
			{
				invalidateDisplayList();
				TableColumnEditor.Instance.Refresh();
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
			graphics.lineStyle(1, 0xbbbbbb, 1);
			graphics.drawRect(0, 0, unscaledWidth, unscaledHeight);
		}

		public function get Editor():DisplayObject
		{
			TableColumnEditor.Instance.SetData(_Settings, _Data);
			return TableColumnEditor.Instance;
		}
		private function RefreshText():void
		{
			var name:String = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
			var i:int = _Settings.TableColumnSettings.indexOf(_Data);
			if(i<26)
				_Text.text = name.charAt(i) + "列";
			else
				_Text.text = i + "列";
			LayoutUtil.CenterLayout(_Text, width, height, 0, 0);
		}
		public function Refresh():void
		{
			RefreshWidth();
		}

	}
}