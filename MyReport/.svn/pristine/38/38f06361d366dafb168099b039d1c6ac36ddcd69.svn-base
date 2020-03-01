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
	import myreport.data.report.TableGroupSetting;
	
	internal class TableGroupItem extends DesignGroup implements IDesignItem
	{
		
		public function TableGroupItem()
		{
			super("分组小计");
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
		
		public override function set Settings(value:ReportSettings):void
		{
			super.Settings = value;
			Refresh();
		}
		public function get Data():*
		{
			if (Settings.TableGroupSettings.length > 0)
			{
				return Settings.TableGroupSettings[0];
				
			}
			return null;
		}
		protected override function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
		{
			graphics.clear();
			graphics.beginFill(0, 0);
			graphics.drawRect(0, 0, unscaledWidth, unscaledHeight);
			graphics.endFill();
			
			if (Selected)
				graphics.beginFill(0xF9FF9D, 0.5);
			else if (_RollOver)
				graphics.beginFill(0xdddddd, 0.5);
			else
				graphics.beginFill(0, 0);
			graphics.drawRect(0, 0, unscaledWidth, 16);
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
		
		//分页小计
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
		
		public function get Editor():DisplayObject
		{
			if (Settings.TableGroupSettings.length > 0)
			{
				var group:TableGroupSetting = Settings.TableGroupSettings[0] as TableGroupSetting;
				TableGroupEditor.Instance.SetData(Settings, group);
			}
			else
				TableGroupEditor.Instance.SetData(Settings, null);
			return TableGroupEditor.Instance;
		}
		
		public function Refresh():void
		{
			var txt:String = "分组小计";
			if (Settings.TableGroupSettings.length > 0)
			{
				var group:TableGroupSetting = Settings.TableGroupSettings[0] as TableGroupSetting;
				if(group.GroupColumn)
					txt += "    【分组字段：" + group.GroupColumn;
				else
					txt += "    【分组大小：" + group.GroupSize;
				
				if(group.OrderColumn)
					txt +="，排序字段：" + group.OrderColumn;
				
				txt+="】";
			}
 
			
			_Title.text = txt;
			
		}
	}
}