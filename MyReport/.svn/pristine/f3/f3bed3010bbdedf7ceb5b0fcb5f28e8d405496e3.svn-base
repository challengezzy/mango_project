/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


*/
package myreport.design
{
	import flash.events.MouseEvent;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	import flash.text.TextField;
	import flash.text.TextFormat;
	
	import hlib.ComponentBase;
	import hlib.DisposeUtil;
	import hlib.DrawUtil;
	import hlib.IDispose;
	import hlib.TextBase;
	
	import myreport.data.report.ReportSettings;

	internal class DesignGroup extends ComponentBase implements IDispose
	{
		protected var _RollOver:Boolean = false;
		protected var _Title:TextField;
		private var _Settings:ReportSettings;
		protected override function Disposing():void
		{
			_Title = null;
			_Settings = null;
			super.Disposing();
		}

		public function DesignGroup(title:String = "")
		{
			super();
 
			var tf:TextFormat = new TextFormat();
			tf.size = 12;
			tf.font = "Simsun";
			tf.color = 0xbbbbbb;
			_Title = TextBase.CreateText(title, tf);
			addChild(_Title);
			height = 32;

			addEventListener(MouseEvent.ROLL_OVER, OnMouseEvent);
			addEventListener(MouseEvent.ROLL_OUT, OnMouseEvent);
 
		}

		public function get Settings():ReportSettings
		{
			return _Settings;
		}
		
		public function set Settings(value:ReportSettings):void
		{
			_Settings = value;
		}
		
		protected function OnRollOver(event:MouseEvent):void
		{

		}

		protected function OnRollOut(event:MouseEvent):void
		{

		}

		private function OnMouseEvent(event:MouseEvent):void
		{
			if (event.currentTarget != this)
				return;
			if (event.type == MouseEvent.ROLL_OVER)
			{
				_RollOver = true;
				invalidateDisplayList();
				OnRollOver(event);
			}
			else if (event.type == MouseEvent.ROLL_OUT)
			{
				_RollOver = false;
				invalidateDisplayList();
				OnRollOut(event);
			}
		}
 
		protected override function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
		{
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			graphics.clear();
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

	}
}