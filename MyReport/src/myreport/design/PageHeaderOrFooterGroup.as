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
	
	import myreport.data.report.CaptionRowSetting;
	import myreport.data.report.ReportSettings;
	import myreport.res.Asset;
	import myreport.util.ToolButton;

	internal class PageHeaderOrFooterGroup extends DesignGroup
	{
		private var _AddButton:ToolButton;
		private var _ChildPanel:UIComponent;
		private var _IsHeader:Boolean = true;

		protected override function Disposing():void
		{
			DisposeUtil.Dispose(_ChildPanel);
			_ChildPanel = null;
			_AddButton = null;
			super.Disposing();
		}
		
		public function PageHeaderOrFooterGroup(title:String = "", isHeader:Boolean = true)
		{
			super(title);
			_IsHeader = isHeader;
			_AddButton = new ToolButton();
			_AddButton.toolTip = "添加标题";
			_AddButton.height = 16;
			_AddButton.width = 16;
			_AddButton.setStyle("icon", Asset.ICON_ADD12);
			_AddButton.addEventListener(MouseEvent.CLICK, OnAddButtonMouseEvent);
			addChild(_AddButton);

			_ChildPanel = new UIComponent();
			_ChildPanel.y = 16;
			addChild(_ChildPanel);

			OnResize();

			addEventListener(DesignEvent.HEIGHT_CHANGED, OnMyReportEvent);
			addEventListener(DesignEvent.DELETE, OnMyReportEvent);
			addEventListener(DesignEvent.ROW_DROP, OnMyReportEvent);
		}

		//使用匿名属性
		private function OnMyReportEvent(event:DesignEvent):void
		{
			if (event.type == DesignEvent.HEIGHT_CHANGED)
			{
				if(event.target != this)
					RefreshHeight(false);
			}
			else if(event.type == DesignEvent.ROW_DROP)
			{
				event.stopPropagation();
				OnRowDrop();
			}
			else if (event.type == DesignEvent.DELETE)
			{
				var item:DisplayObject = event.target as CaptionRowItem;
				if (!item)
					return;
				var index:int;
				if (_IsHeader)
				{
					index = Settings.PageHeaderSettings.indexOf(Object(item).Data);
					if (index >= 0)
						Settings.PageHeaderSettings.splice(index,1);
				}
				else
				{
					index = Settings.PageFooterSettings.indexOf(Object(item).Data);
					if (index >= 0)
						Settings.PageFooterSettings.splice(index,1);
				}
				if (Object(item).Data is IDispose)
					IDispose(Object(item).Data).Dispose();
				item.parent.removeChild(item);
				if (item is IDispose)
					IDispose(item).Dispose();
				RefreshHeight(true);
			}
		}

		private function OnAddButtonMouseEvent(event:MouseEvent):void
		{
			var item:Object = null;
			var caption:DisplayObject = null;

			item = new CaptionRowSetting();

			if (item)
			{
				var _parent:Array;
				if (_IsHeader)
				{
					Settings.PageHeaderSettings.push(item);
					_parent = Settings.PageHeaderSettings;
				}
				else
				{
					Settings.PageFooterSettings.push(item);
					_parent = Settings.PageFooterSettings;
				}
				caption = DesignUtil.CreateItem(item, _parent, Settings);
				caption.width = width;
			}
			if (caption)
				_ChildPanel.addChild(caption);

			RefreshHeight(true);
		}
 
		override protected function OnResize():void
		{
			_AddButton.x = width - _AddButton.width;
			_ChildPanel.width = width;
			RefreshWidth();
		}

		public override function set Settings(value:ReportSettings):void
		{
			super.Settings = value;
			DisposeUtil.Dispose(_ChildPanel);
			if (_IsHeader)
			{
				DesignUtil.AddItems(Settings.PageHeaderSettings, Settings.PageHeaderSettings, Settings, _ChildPanel);
			}
			else
			{
				DesignUtil.AddItems(Settings.PageFooterSettings, Settings.PageFooterSettings, Settings, _ChildPanel);
			}
			RefreshWidth();
			RefreshHeight(false);
		}

		private function RefreshWidth():void
		{
			for (var i:int = 0; i < _ChildPanel.numChildren; i++)
			{
				var item:DisplayObject = _ChildPanel.getChildAt(i);
				item.width = width;
			}
		}
		
		private function OnRowDrop():void
		{
			var h:Number = 0;
			for (var i:int = 0; i < _ChildPanel.numChildren; i++)
			{
				var item:CaptionRowItem = _ChildPanel.getChildAt(i) as CaptionRowItem;
				item.y = h;
				item.RefreshText();
				h += item.height;
			}
		}

		private function RefreshHeight(reportChanged:Boolean):void
		{
			var h:Number = 0;
			for (var i:int = 0; i < _ChildPanel.numChildren; i++)
			{
				var item:DisplayObject = _ChildPanel.getChildAt(i);
				item.y = h;
				h += item.height;
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