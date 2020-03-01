/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


饼图绘制工具类

*/
package myreport.chart
{
	import flash.display.Graphics;
	import hlib.DrawUtil;
	internal final class PieUtil
	{
		internal static function DrawPie3D(g:Graphics, x:Number, y:Number, radius:Number, yRadius:Number, startAngle:Number, sweepAngle:Number, 
										 pieH:Number, clr:uint, pieBorderThickness:Number, pieBorderAlpha:Number, pieFillAlpha:Number):Object
		{
			if (sweepAngle>360 || sweepAngle<-360) 
			{
				sweepAngle = 360;
			}
			if (startAngle>360) 
			{
				startAngle %= 360;
			}
			//If no y radius has been defined
			if (isNaN(yRadius)) 
			{
				yRadius = radius;
			}
			var centerX:Number = x;
			var centerY:Number = y-pieH/2;
			var clr2:uint = DrawUtil.AdjustColorBrightness(clr, -30);
 
			g.lineStyle(pieBorderThickness, clr, pieBorderAlpha);
			g.beginFill(clr, pieFillAlpha);
			g.moveTo(centerX, centerY);
			//move to center
			var obj:Object = {};
			//empty object
			DrawArc(g, centerX, centerY, radius, yRadius, startAngle, sweepAngle, obj);
			g.lineTo(centerX, centerY);
			g.endFill();
			//draw visible pie sides if any
			if (pieH) 
			{
				g.lineStyle(pieBorderThickness, clr2, pieBorderAlpha);
				g.beginFill(clr2, pieFillAlpha);
				var mid:Boolean = false;
				var rht:Boolean = false;
				var lft:Boolean = false;
				var bck:Boolean = false;
				//draw height on bottom 
				if (sweepAngle<360 && (obj.y1>=centerY || obj.y2>=centerY)) {
					if (obj.x1<=obj.x2) {
						if (obj.y1>=centerY && obj.y2>=centerY) {
							mid = true;
						} else if (obj.y1>=centerY) {
							rht = true;
						} else if (obj.y2>=centerY) {
							bck = true;
						}
					} else {
						if (obj.y1>centerY) {
							rht = true;
						}
						if (obj.y2>centerY) {
							lft = true;
						}
					}
				} else if (sweepAngle>=360 || obj.x1<obj.x2) {
					bck = true;
				}
				if (mid) {
					g.moveTo(obj.x1, obj.y1);
					DrawArc(g, centerX, centerY+pieH, radius, yRadius, startAngle, sweepAngle);
					DrawArc(g, centerX, centerY, radius, yRadius, startAngle+sweepAngle, -sweepAngle);
				}
				if (rht) {
					g.moveTo(obj.x1, obj.y1);
					DrawArc(g, centerX, centerY+pieH, radius, yRadius, startAngle, 360-startAngle);
					DrawArc(g, centerX, centerY, radius, yRadius, 0, startAngle-360);
				}
				if (lft) {
					var tmp:Number = (startAngle+sweepAngle)%180;
					g.moveTo(centerX-radius, centerY);
					DrawArc(g, centerX, centerY+pieH, radius, yRadius, 180, tmp);
					DrawArc(g, centerX, centerY, radius, yRadius, 180+tmp, -tmp);
				}
				if (bck) {
					g.moveTo(centerX-radius, centerY);
					DrawArc(g, centerX, centerY+pieH, radius, yRadius, 180, 180);
					DrawArc(g, centerX, centerY, radius, yRadius, 0, -180);
				}
				g.endFill();
			}
			//Label text positions
//			var labelOff:Number = -5;
//			var labelAng:Number = (startAngle+sweepAngle/2)+labelOff;
//			var labelX:Number = (radius+pieH)*Math.cos(labelAng*Math.PI/180);
//			var labelY:Number = -(yRadius+pieH)*Math.sin(labelAng*Math.PI/180);
		
			var labelAng2:Number = startAngle+sweepAngle/2;
			var labelX2:Number = (radius)*Math.cos(labelAng2*Math.PI/180);
			var labelY2:Number = -pieH/2-(yRadius)*Math.sin(labelAng2*Math.PI/180);
			
			//return {LabelX:labelX2*1.5, LabelY:labelY2*1.5, LabelX2:labelX2, LabelY2:labelY2};
			return {LabelX:labelX2, LabelY:labelY2};
		}
		/**
		 * This method renders pie shaped arc (wedges)
		 * g is the draw graphic
		 * x, y is the center point of the arc
		 * r is the radius
		 * yRadius is the y-radius
		 * startAngle is the start angle of the arc anti-clockwise w.r.t. x axis
		 * sweepAngle is the arc angle (sweeping angle)
		 * rtn是返回值{x1:Number, y1:Number, x2:Number, y2:Number}
		 */ 
		private static function DrawArc(g:Graphics, x:Number, y:Number, r:Number, yRadius:Number, startAngle:Number, sweepAngle:Number, rtn:Object=null):void
		{
			
			if (startAngle>360) 
			{
				startAngle -= 360;
			}
			//If no y radius has been defined
			if(isNaN(yRadius))
			{
				yRadius = r;
			}
			//	Same with sweepAngle
			if (Math.abs(sweepAngle)>360) 
			{
				sweepAngle = 360;
			}
			// Flash uses 8 segments per circle, to match that, we draw in a maximum
			// of 45 degree segments. First we calculate how many segments are needed
			// for our arc.
			var nSubArcs:Number = Math.floor(Math.abs(sweepAngle)/45);
			//After multiples of 45, we'll have some left over degs
			var leftoverAng:Number = Math.abs(sweepAngle)-nSubArcs*45;
			//Finding the angle (radians) and its half (for control points)
			var ang45:Number = ((sweepAngle>=0) ? 45 : -45)*Math.PI/180;
			var ang225:Number = ang45/2;
			var cosHT:Number = Math.cos(ang225);
			//Convert the offset angle to radians
			startAngle *= (Math.PI/180);
			//Get the starting position of the arc
			var x1:Number = x+Math.cos(startAngle)*r;
			var y1:Number = y-Math.sin(startAngle)*yRadius;
			//Draw a line from the center of the arc to the starting position
			g.lineTo(x1, y1);
			//Determine the mid angle - to calculate the control point
			var angleMid:Number  = startAngle-ang225;
			//x2 and y2 are the end points of arc
			//cx and cy are the control points for each arc segment
			var i:Number, x2:Number, y2:Number, cx:Number , cy:Number;
			var rc:Number = r/cosHT;
			var rc2:Number;
			for (i=0; i<nSubArcs; i++) 
			{
				//Create each arc segment
				x2 = x+Math.cos(startAngle+ang45)*r;
				y2 = y-Math.sin(startAngle+ang45)*yRadius;
				cx = x+Math.cos(startAngle+ang225)*(rc);
				cy = y-Math.sin(startAngle+ang225)*(yRadius/cosHT);
				g.curveTo(cx, cy, x2, y2);
				startAngle += ang45;
			}
			if (leftoverAng) 
			{
				//Now, if some angle is left over, create the arc for that too
				leftoverAng *= ((sweepAngle<0) ? -Math.PI : Math.PI)/180;
				rc = r/Math.cos(leftoverAng/2);
				rc2 = yRadius/Math.cos(leftoverAng/2);
				x2 = x+Math.cos(startAngle+leftoverAng)*r;
				y2 = y-Math.sin(startAngle+leftoverAng)*yRadius;
				cx = x+Math.cos(startAngle+leftoverAng/2)*rc;
				cy = y-Math.sin(startAngle+leftoverAng/2)*rc2;
				//Draw curve to the end of sub-arc segment through control points cx, cy
				g.curveTo(cx, cy, x2, y2);
			}
			
			//Now, return the starting and endind point of arc as object
			if(rtn)
			{
				rtn.x1 = Math.round(x1*1000)/1000;
				rtn.y1 = Math.round(y1*1000)/1000;
				rtn.x2 = Math.round(x2*1000)/1000;
				rtn.y2 = Math.round(y2*1000)/1000;
			}
		}
		
	}
}