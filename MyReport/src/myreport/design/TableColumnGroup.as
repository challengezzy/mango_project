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
	
	import hlib.DisposeUtil;
	import hlib.IDispose;
	
	import mx.core.UIComponent;
	
	import myreport.data.report.ReportSettings;
	import myreport.data.report.TableCellSetting;
	import myreport.data.report.TableColumnSetting;
	import myreport.data.report.TableGroupSetting;
	import myreport.data.report.TableRowSetting;
	import myreport.res.Asset;
	import myreport.util.ToolButton;

	internal class TableColumnGroup extends DesignGroup
	{
		private var _AddButton:ToolButton;
		private var _ChildPanel:UIComponent;
		//================IDispose====================
		protected override function Disposing():void
		{
			_AddButton.removeEventListener(MouseEvent.CLICK, OnAddButtonMouseEvent);
			DisposeUtil.Dispose(_ChildPanel);
			_ChildPanel = null;
			_AddButton = null;
 
			super.Disposing();
		}
		public function TableColumnGroup()
		{
			super("表格列");
			_AddButton = new ToolButton();
			_AddButton.toolTip = "添加表格列";
			_AddButton.height = 16;
			_AddButton.width = 16;
			_AddButton.setStyle("icon", Asset.ICON_ADD12);
			_AddButton.addEventListener(MouseEvent.CLICK, OnAddButtonMouseEvent);
			addChild(_AddButton);

			_ChildPanel = new UIComponent();
			_ChildPanel.y = 16;
			_ChildPanel.height = 24;
			addChild(_ChildPanel);
			height = 40;

			OnResize();
			addEventListener(DesignEvent.DELETE, OnMyReportEvent);
		}

		private function OnMyReportEvent(event:DesignEvent):void
		{
			if (event.type == DesignEvent.DELETE)
			{
				var item:TableColumnItem = event.target as TableColumnItem;
				if (item == null)
					return;
				var index:int = Settings.TableColumnSettings.indexOf(item.Data);
				if (index >= 0)
					Settings.TableColumnSettings.splice(index,1);
				if (item.Data is IDispose)
					IDispose(item.Data).Dispose();
				item.parent.removeChild(item);
				item.Dispose();
				RefreshWidth();
				var evt:DesignEvent;
				evt = new DesignEvent(DesignEvent.COLUMN_DELETED, true);
				evt.ColumnIndex = index;
				dispatchEvent(evt);
			}
		}

		public function RefreshWidth(refreshEditor:Boolean = false):void
		{
			if (Settings == null)
				return;
			var w:Number = 0;
			for each (var column:TableColumnSetting in Settings.TableColumnSettings)
			{
				w += column.Width;
			}
			w = Number(w.toFixed(1));
			_Title.text = "表格列    【总列宽：" + w + "cm】";
			w = 0;
			for (var i:int = 0; i < _ChildPanel.numChildren; i++)
			{
				var item:DisplayObject = _ChildPanel.getChildAt(i);
				item.x = w;
				w += item.width;
			}
		}

		private function OnAddButtonMouseEvent(event:MouseEvent):void
		{
			NewColumnForm.Instance.Show(Settings, function(title:String, column:String, width:Number):void
				{
					var col:TableColumnSetting = new TableColumnSetting();
					col.Width = width;
					Settings.TableColumnSettings.push(col);
					var item:DisplayObject = DesignUtil.CreateItem(col, null, Settings);
					_ChildPanel.addChild(item);
					RefreshWidth();

					var evt:DesignEvent = new DesignEvent(DesignEvent.COLUMN_ADDED, true);
					evt.Value = {Title:title, Column:column};
					dispatchEvent(evt);
				});

		}
 
		override protected function OnResize():void
		{
			_AddButton.x = width - _AddButton.width;
			_ChildPanel.width = width;
		}

		public override function set Settings(value:ReportSettings):void
		{
			super.Settings = value;
			DisposeUtil.Dispose(_ChildPanel);
			DesignUtil.AddItems(Settings.TableColumnSettings, null, Settings, _ChildPanel);
			RefreshWidth();
		}
	}
}