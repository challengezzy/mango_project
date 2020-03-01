/*
Copyright (c) 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


分隔扩展类，支持水平分割，垂直分割

*/
package myreport.design
{
	import flash.display.DisplayObject;
	import flash.display.DisplayObjectContainer;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	
	import hlib.MovableExtension;
	
	import mx.events.ResizeEvent;
	
	public final class DividableExtension extends MovableExtension
	{
		private static const WIDTH:Number = 2;
		private static var _Line:DivideLine;	
		private var _Divider:Divider;
		private var _Parent:DisplayObjectContainer;
		private var _OldWidth:Number;
		private var _OldHeight:Number;
		private var _LineParentFunc:Function;
		private var _DividedFunc:Function;
 
		protected override function Disposing():void
		{
			if(_Parent.contains(_Divider))
			{
				_Parent.removeChild(_Divider);
			}
			_Parent.removeEventListener(ResizeEvent.RESIZE, OnParentResize);
			_Divider.Dispose();
			_Divider = null;
			_Parent = null;
			_LineParentFunc = null;
			_DividedFunc = null;
			
			super.Disposing();
		}
		/**
		 * @param lineParentFunc: function():DisplayObjectContainer，分割线停靠的容器
		 * @param dividedFunc: function(oldValue:Number, newValue:Number):void，当发生改变时才触发
		 */
		public function DividableExtension(parent:DisplayObjectContainer, vertical:Boolean, 
										   lineParentFunc:Function, dividedFunc:Function)
		{
			_Divider = new Divider(vertical);
			super(_Divider, OnMouseDown, OnStateMouseEvent, OnStateMouseEvent);
			_Parent = parent;
			if(vertical)
				_Divider.height = WIDTH;
			else
				_Divider.width = WIDTH;
			_Parent.addChild(_Divider);
			
			_LineParentFunc = lineParentFunc;
			_DividedFunc = dividedFunc;
			
			_Parent.addEventListener(ResizeEvent.RESIZE, OnParentResize);
		}
		private function OnParentResize(e:ResizeEvent):void
		{
			if(_Divider)
			{
				if(_Divider.Vertical)
				{
					_Divider.y = _Parent.height - _Divider.height;
					_Divider.width = _Parent.width;					
					
				}
				else
				{
					_Divider.x = _Parent.width - _Divider.width;
					_Divider.height = _Parent.height;					
					
				}
			}
		}
		private static function GetLine():DivideLine
		{
			if(!_Line)
			{
				_Line = new DivideLine();
			}
			return _Line;
		}
		public function get IsDividing():Boolean
		{
			return _Divider.Selected;
		}
		public function get Vertical():Boolean
		{
			return _Divider.Vertical;
		}
		public function get DivideEnabled():Boolean
		{
			return SuspendStageEvent;
		}
		public function set Width(value:Number):void
		{
			if(Vertical)
				_Divider.height = value;
			else
				_Divider.width = value;
		}
		public function set DivideEnabled(value:Boolean):void
		{
			SuspendStageEvent = !value;
			_Divider.visible = value;
		}
		private function OnMouseDown(e:MouseEvent):void
		{
			if(SuspendStageEvent)
				return;
 
			_OldWidth = _Parent.width;
			_OldHeight = _Parent.height;
			
			_Divider.Selected = true;
			var line:DivideLine = GetLine();
			
			var lineParent:DisplayObjectContainer = _LineParentFunc();
			var point:Point;
			if(_Divider.Vertical)
				point = _Parent.localToGlobal(new Point(0, _Parent.height));				
			else
				point = _Parent.localToGlobal(new Point(_Parent.width, 0));
			
			point = lineParent.globalToLocal(point);
			if(_Divider.Vertical)
			{
				line.x = 0;
				line.y = point.y;
				line.OldValue = point.y;
				line.width = lineParent.width;			
				line.height = 2;
			}
			else
			{
				line.x = point.x;
				line.y = 0;
				line.OldValue = point.x;
				line.width = 2;
				line.height = lineParent.height;
			}
			
			if(!lineParent.contains(line))
				lineParent.addChild(line);
			else
				lineParent.setChildIndex(line, lineParent.numChildren-1);
		}
		private function OnStateMouseEvent(e:MouseEvent):void
		{
			var line:DivideLine;
			if(e.type == MouseEvent.MOUSE_MOVE)
			{
				if(_Divider.Vertical)
				{
					if(ChangeY != 0)
					{
						line = GetLine();
						line.y = Math.floor(line.OldValue + ChangeY);
					}
				}
				else
				{
					if(ChangeX != 0)
					{
						line = GetLine();
						line.x = Math.floor(line.OldValue + ChangeX);
					}
				}
			}
			else if(e.type == MouseEvent.MOUSE_UP)
			{
 
				_Divider.Selected = false;
				line = GetLine();
				if(line.parent)
					line.parent.removeChild(line);
				
				if(_Divider.Vertical)
				{
					if(ChangeY != 0)
					{
						var newHeight:Number = Math.floor(_OldHeight + ChangeY);
						OnDivided(_OldHeight, newHeight);
					}	
				}
				else
				{
					if(ChangeX != 0)
					{
						var newWidth:Number = Math.floor(_OldWidth + ChangeX);
						OnDivided(_OldWidth, newWidth);
					}
				}
			}
		}
		protected function OnDivided(oldValue:Number, newValue:Number):void
		{
			if(_DividedFunc!=null)
				_DividedFunc(oldValue, newValue);
		}
	}
}