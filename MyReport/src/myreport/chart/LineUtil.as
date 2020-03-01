/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


线条绘制工具类

*/

package myreport.chart
{
	import flash.display.Graphics;
	internal final class LineUtil
	{
		internal static function DrawPoint(g:Graphics, x:Number, y:Number, radius:Number, thickness:Number, color:uint):void
		{
			g.lineStyle(thickness, 0, 0.5);
			g.beginFill(9, 0.7);
			g.drawCircle(x+thickness, y+thickness, radius);
			g.endFill();
			
			g.lineStyle(thickness, color);
			g.beginFill(0xFFFFFF, thickness);
			g.drawCircle(x, y, radius);
			g.endFill();
 
		}
		
		public static function DrawFoldLine(g:Graphics, points:Array, thickness:Number, color:uint):void
		{
			if (points.length < 2)
				return;
			var i:int;
			var p0:Object = points[0];
			var p:Object;
			g.lineStyle(thickness, 0, 0.5);
			var shift:Number = thickness/2;
			g.moveTo(p0.x+shift, p0.y+shift);
			for (i = 1; i < points.length; i++)
			{
				p = points[i];
				g.lineTo(p.x+shift, p.y+shift);
			}
			g.lineStyle(thickness, color);
			g.moveTo(p0.x, p0.y);
			for (i = 1; i < points.length; i++)
			{
				p = points[i];
				g.lineTo(p.x, p.y);
			}
		}
	}
}