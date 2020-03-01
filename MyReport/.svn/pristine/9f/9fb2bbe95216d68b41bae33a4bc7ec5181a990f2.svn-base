/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


报表单元格基类。


*/
package myreport.report
{
	import flash.display.DisplayObject;
	import flash.geom.Point;
	
	import hlib.DrawUtil;
	import hlib.LayoutUtil;
	import hlib.Parser;
	
	import myreport.data.report.CaptionCellSetting;
	import myreport.data.report.ControlSetting;
	import myreport.data.report.Define;
	import myreport.data.report.ReportSettings;
	import myreport.data.report.StyleSetting;
	import myreport.data.report.TableCellSetting;
	import myreport.expression.ExpressionContext;
	import myreport.expression.ExpressionEngine;
	import myreport.render.IRender;
	import myreport.report.cellrender.ReportCellRenderBase;
	import myreport.report.cellrender.ReportCellRenderUtil;
	
	public class ReportCell extends ReportItem
	{
		
		protected var _Context:ExpressionContext;
		private var _Render:ReportCellRenderBase;
		
		public var Data:*;
		
		public var CanGrow:Boolean;
		public var Value:Object;
		public var CellValue:Object;
		public var Style:StyleSetting;
		
		protected override function Disposing():void
		{
			if(_Render)
			{
				_Render.Dispose();
				_Render = null;
			}
			
			_Context = null;	
			Value = null;
			CellValue = null;
			Style = null;
			Data = null;
			
			super.Disposing();
		}
		public function ReportCell(settings:ReportSettings, context:ExpressionContext, canGrow:Boolean, data:*)
		{
			super(settings);
			_Context = context;
			CanGrow = canGrow;
			Data = data;
		}
		public function get Control():ControlSetting
		{
			if(Data is CaptionCellSetting)
				return CaptionCellSetting(Data).Control;
			else if(Data is TableCellSetting)
				return TableCellSetting(Data).Control;
			return null;
		}
		internal function Render(h:Number):void
		{
			Execute();
			height = h;
			OnRefreshWidth();
			RenderDisplay();
			Paint();
		}
		
		private function Execute():void
		{
			Value = null;
			CellValue = null;
			Style = null;
			OnExecute();
			_Context.Current = Value;
			Style = OnExcuteStyle();
		}
		/**
		 * 
		 * 生成Value，CellValue，Style
		 * 
		 */
		protected function OnExecute():void
		{
			
		}
		protected function OnExcuteStyle():StyleSetting
		{
			return null;
		}
		protected function IsExpression(expression:String):Boolean
		{
			return ExpressionEngine.IsExpression(expression);
		}
		protected function ExecuteExpression(expression:String, format:String):void
		{
			CellValue = Value = InvokeValue(expression, _Context);
			if(!(CellValue is DisplayObject))
				CellValue = Format(CellValue, format);
		}
		
		protected function ExecuteParameter(value:String, binding:String, format:String):void
		{
			var parameter:Object = GetParameter(binding);
			Value = value + parameter;
			CellValue = value + Format(parameter, format);
		}
		private function GetParameter(name:String):Object
		{
			if (name && _Context.Parameters && _Context.Parameters.hasOwnProperty(name))
			{
				return _Context.Parameters[name];
			}
			return "";
		}
		
		protected function OnRefreshWidth():void
		{
			
		}
		/**
		 * 修正高度，单元格内容自动递增，可能导致当前行高改变
		 */
		internal function AdjustHeight(h:Number):void
		{
			if(height == h)
				return;
			height = h;
			if(_Render)
				_Render.RefreshHeight();
			
			Paint();
		}
		private function RenderDisplay():void
		{
			RemoveAllChildren();
			_Render = ReportCellRenderUtil.CreateRender(this);
			if(_Render)
				_Render.Render();
			
		}
		
		private function Paint():void
		{
			graphics.clear();
			var thickness:Number = 1;
			
			if(Style.Border)
			{ 
				graphics.lineStyle(thickness, 0x000000, 1);
				if(Style.LeftBorder)
				{
					if(Style.LeftBorderStyle == Define.BORDER_STYLE_DASH)
						DrawUtil.DrawDashLine(graphics, new Point(0,0), new Point(0, height));
					else if(Style.LeftBorderStyle == Define.BORDER_STYLE_SOLID)
					{
						graphics.moveTo(0, 0);
						graphics.lineTo(0, height);
					}
				}
				if(Style.TopBorder)
				{
					if(Style.TopBorderStyle == Define.BORDER_STYLE_DASH)
						DrawUtil.DrawDashLine(graphics, new Point(0,0), new Point(width, 0));
					else if(Style.TopBorderStyle == Define.BORDER_STYLE_SOLID)
					{
						graphics.moveTo(0, 0);
						graphics.lineTo(width, 0);
					}
				}
				if(Style.RightBorder)
				{
					if(Style.RightBorderStyle == Define.BORDER_STYLE_DASH)
						DrawUtil.DrawDashLine(graphics, new Point(width,0), new Point(width, height));
					else if(Style.RightBorderStyle == Define.BORDER_STYLE_SOLID)
					{
						graphics.moveTo(width, 0);
						graphics.lineTo(width, height);
					}
				}
				if(Style.BottomBorder)
				{
					if(Style.BottomBorderStyle == Define.BORDER_STYLE_DASH)
						DrawUtil.DrawDashLine(graphics, new Point(0,height), new Point(width, height));
					else if(Style.BottomBorderStyle == Define.BORDER_STYLE_SOLID)
					{
						graphics.moveTo(0, height);
						graphics.lineTo(width, height);
					}
				}
			}
			if(Style.Underline)
			{
				graphics.lineStyle(thickness);
				graphics.moveTo(2, height-2);
				graphics.lineTo(width-2, height-2);
			}
		}
		
		protected static function InvokeValue(expression:String, context:ExpressionContext):Object
		{
			return ExpressionEngine.Invoke(expression, context);
		}
		
		private static function Format(value:Object, format:String):Object
		{
			return Parser.Format(value, format);
		}
	}
}