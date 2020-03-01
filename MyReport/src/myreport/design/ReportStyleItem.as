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
	import hlib.DrawUtil;
	import myreport.data.report.ReportSettings;

	internal class ReportStyleItem extends DesignGroup implements IDesignItem
	{
 
		public function ReportStyleItem()
		{
			super("报表设置");
			height = 16;
			addEventListener(MouseEvent.MOUSE_DOWN, OnMouseDown);
		}
		private function ReportItemMouseDown(append:Boolean):void
		{
			var e:DesignEvent = new DesignEvent(DesignEvent.ITEM_MOUSE_DOWN, true);
			e.Append = append;
			dispatchEvent(e);
		}
		private function OnMouseDown(event:MouseEvent):void
		{
			if (event.currentTarget != this)
				return;
			setFocus();	
			ReportItemMouseDown(event.ctrlKey);
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
		public function get Data():*
		{
			return Settings;
		}
		protected override function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
		{
			graphics.clear();

			if (Selected)
				graphics.beginFill(0xF9FF9D, 0.5);
			else if (_RollOver)
				graphics.beginFill(0xdddddd, 0.5);	
			else
				graphics.beginFill(0, 0);
			graphics.drawRect(0, 0, unscaledWidth, unscaledHeight);
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
 
		public override function set Settings(value:ReportSettings):void
		{
			super.Settings = value;
			Refresh();
		}
		
		public function get Editor():DisplayObject
		{
			ReportStyleEditor.Instance.Data = Settings;
			return ReportStyleEditor.Instance;
		}
		
		public function Refresh():void
		{
			_Title.text = "报表设置    【大小：" + Settings.PageWidth + "cm x " + Settings.PageHeight + "cm，有效宽度：" + Settings.ClientWidth.toFixed(1) + "cm】";
			
		}
	}
}