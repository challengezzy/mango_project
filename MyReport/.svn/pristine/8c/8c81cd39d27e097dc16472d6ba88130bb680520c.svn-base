/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


界面拖放扩展类。


*/
package hlib
{
	import flash.display.DisplayObject;
	import flash.display.Sprite;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	import flash.geom.Rectangle;

	public class MovableExtension implements IDispose
	{
		protected var _Target:DisplayObject;
		private var _MouseMoveCallback:Function;
		private var _MouseDownCallback:Function;
		private var _MouseUpCallback:Function;
		public var SuspendVertical:Boolean = true;
		public var SuspendHorizontal:Boolean = true;
		public var MinLeft:Number = NaN;
		public var MaxLeft:Number = NaN;
		public var MinTop:Number = NaN;
		public var MaxTop:Number = NaN;
		public var SuspendStageEvent:Boolean = false; //挂起场景事件
		public var StagePoint:Point = new Point();
		public var StageRectangle:Rectangle = new Rectangle();
		public var LocalPoint:Point = new Point();
		public var ChangeX:Number = 0;
		public var ChangeY:Number = 0;	
		public var Editing:Boolean = false;
		
		//================IDispose====================
		protected var _Disposed:Boolean = false;
		protected function Disposing():void
		{
			
		}
		public final function Dispose():void
		{
			if (_Disposed)
				return;
			_Disposed = true;
			Disposing();
			_Target.removeEventListener(MouseEvent.MOUSE_DOWN, MouseDownHandler);
			_Target = null;
			_MouseMoveCallback = null;
			_MouseDownCallback = null;
			_MouseUpCallback = null;
			StagePoint = null;
			LocalPoint = null;
			
		}
		
		/**
		 * @param target： 扩展应用的对象
		 * @param targetMouseDownFunc： 鼠标按下回调函数，function(e:MouseEvent):void
		 * @param stageMouseMoveFunc： 鼠标移动回调函数，function(e:MouseEvent):void
		 * @param stageMouseUpFunc： 鼠标弹起回调函数，function(e:MouseEvent):void
		 */
		public function MovableExtension(target:DisplayObject, targetMouseDownFunc:Function = null, 
										 stageMouseMoveFunc:Function = null, stageMouseUpFunc:Function = null)
		{
			_Target = target;
			_MouseDownCallback = targetMouseDownFunc;
			_MouseMoveCallback = stageMouseMoveFunc;
			_MouseUpCallback = stageMouseUpFunc;
			
			_Target.addEventListener(MouseEvent.MOUSE_DOWN, MouseDownHandler);
		}

		private function MouseDownHandler(event:MouseEvent):void
		{
			if (event.currentTarget != _Target || event.type != MouseEvent.MOUSE_DOWN)
				return;
			
			StagePoint.x = event.stageX;
			StagePoint.y = event.stageY;
			StageRectangle.x = StagePoint.x;
			StageRectangle.y = StagePoint.y;
			LocalPoint.x = _Target.x;
			LocalPoint.y = _Target.y;
			
			ChangeX = 0;
			ChangeY = 0;
			StageRectangle.width = ChangeX;
			StageRectangle.height = ChangeY;

			if (_MouseDownCallback != null)
				_MouseDownCallback(event);

			if (SuspendStageEvent)
				return;
			_Target.stage.removeEventListener(MouseEvent.MOUSE_MOVE, MouseMoveLayoutHandler);
			_Target.stage.removeEventListener(MouseEvent.MOUSE_UP, MouseUpLayoutHandler);		
			_Target.stage.addEventListener(MouseEvent.MOUSE_MOVE, MouseMoveLayoutHandler);
			_Target.stage.addEventListener(MouseEvent.MOUSE_UP, MouseUpLayoutHandler);
			Editing = true;
		}
 
		private function MouseMoveLayoutHandler(event:MouseEvent):void
		{
			if (event.buttonDown)
			{
				ChangeY = event.stageY - StagePoint.y;
				ChangeX = event.stageX - StagePoint.x;
 
				if (!isNaN(MinLeft) && LocalPoint.x + ChangeX < MinLeft)
				{
					ChangeX = MinLeft - LocalPoint.x;
				}
				if (!isNaN(MaxLeft) && LocalPoint.x + ChangeX > MaxLeft)
				{
					ChangeX = MaxLeft - LocalPoint.x;
				}
				if (!isNaN(MinTop) && LocalPoint.y + ChangeY < MinTop)
				{
					ChangeY = MinTop - LocalPoint.y;
				}
				if (!isNaN(MaxTop) && LocalPoint.y + ChangeY > MaxTop)
				{
					ChangeY = MaxTop - LocalPoint.y;
				}				
				if (!SuspendVertical)
				{
					_Target.y = LocalPoint.y + ChangeY;
				}
				if (!SuspendHorizontal)
				{
					_Target.x = LocalPoint.x + ChangeX;
				}
				ChangeX = Math.floor(ChangeX);
				ChangeY = Math.floor(ChangeY);
				StageRectangle.width = Math.abs(ChangeX);
				if(ChangeX<0)
					StageRectangle.x = StagePoint.x + ChangeX;
				StageRectangle.height = Math.abs(ChangeY);
				if(ChangeY<0)
					StageRectangle.y = StagePoint.y + ChangeY;
				if (_MouseMoveCallback != null)
					_MouseMoveCallback(event);
				event.stopPropagation();
			}
		}
		
		private function MouseUpLayoutHandler(event:MouseEvent):void
		{
			_Target.stage.removeEventListener(MouseEvent.MOUSE_MOVE, MouseMoveLayoutHandler);
			_Target.stage.removeEventListener(MouseEvent.MOUSE_UP, MouseUpLayoutHandler);
			if (_MouseUpCallback != null)
				_MouseUpCallback(event);
			event.stopPropagation();
			Editing = false;
			ChangeX = 0;
			ChangeY = 0;
			StageRectangle.width = ChangeX;
			StageRectangle.height = ChangeY;
		}
		
		/**
		 * Y轴平移，一般用于y轴滚动时改变选区Y轴范围
		 */
		public function TransformY(change:Number):void
		{
			StagePoint.y += change;
			LocalPoint.y += change;
			ChangeY -= change;
			
			StageRectangle.y = StagePoint.y;
 
			if (!isNaN(MinTop) && LocalPoint.y + ChangeY < MinTop)
			{
				ChangeY = MinTop - LocalPoint.y;
			}
			if (!isNaN(MaxTop) && LocalPoint.y + ChangeY > MaxTop)
			{
				ChangeY = MaxTop - LocalPoint.y;
			}				
			if (!SuspendVertical)
			{
				_Target.y = LocalPoint.y + ChangeY;
			}
 
			StageRectangle.height = Math.abs(ChangeY);
			if(ChangeY<0)
				StageRectangle.y = StagePoint.y + ChangeY;
		}
		/**
		 * 绘制区域
		 */
		public function DrawStageRectangleTo(target:Sprite):void
		{
			target.graphics.clear();
			target.graphics.lineStyle(1);
			var rect:Rectangle = StageRectangle;
			var p:Point = target.globalToLocal(new Point(rect.x, rect.y));
			target.graphics.drawRect(p.x,p.y,rect.width, rect.height);
		}
		/**
		 * 判断选区与矩形是否相交
		 */ 
		public function IntersectsRect(rect:Rectangle):Boolean
		{
			var intersectX:Boolean = false;
			var intersectY:Boolean = false;
			if(StageRectangle.x<rect.x)
			{
				intersectX = rect.x<=StageRectangle.right;
			}
			else
			{
				intersectX = StageRectangle.x<=rect.right;
			}
			if(StageRectangle.y<rect.y)
			{
				intersectY = rect.y<=StageRectangle.bottom;
			}
			else
			{
				intersectY = StageRectangle.y<=rect.bottom;
			}
			return intersectX && intersectY;
		}
	}
}