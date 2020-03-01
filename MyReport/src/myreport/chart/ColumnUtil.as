/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


柱状图绘制工具类

*/
package myreport.chart
{
	import flash.display.Graphics;
	
	import hlib.DrawUtil;
	public final class ColumnUtil
	{
		private static const DARK_SHADOW_INTENSITY:Number = -35;
		private static const LIGHT_SHADOW_INTENSITY:Number = -20;
		private static const COLUMN_TILT_ANGLE:Number = 45;
 
		internal static function Create3DParameters(columnWidth:Number):Object
		{
			var params:Object = {XShift:0, YShift:0, FrontWidth:0};
			//This function precalculates the x and y shift of the column based on the width of the column
			//columnWidth - is the width of the column to be drawn	
			//First, calculate the width of the front part and the shadow part
			var wFront:Number, wShadow:Number;
			//Initial ratio= front part:shadow=2:1
			wFront = columnWidth*(2/3);
			//If the column width is more than 25 pixels, restrict the shadow to max 15 pixels
			if (columnWidth>=25) {
				wShadow = 15;
				wFront = columnWidth - wShadow * Math.cos(COLUMN_TILT_ANGLE*(Math.PI/180));
			} else {
				//If the entire column width is less than 25 pixels, then we increase the depth of the shadow to 20 pixels
				//to give a richer 3D look
				//However, the column face would still be 2/3rd of the entire width available
				//Although it will not seem so owing to the column depth
				wShadow = 20;
			}
			var pShadowBaseRightY:Number, pShadowBaseRightX:Number;
			pShadowBaseRightX = 0;
			pShadowBaseRightY = 0;
			var angle:Number = (180+COLUMN_TILT_ANGLE)*(Math.PI/180);
			var pShadowBaseLeftX:Number = pShadowBaseRightX + wShadow * Math.cos(angle);
			var pShadowBaseLeftY:Number = pShadowBaseRightY + wShadow * Math.sin(angle);
			
			params.XShift = Math.abs(pShadowBaseLeftX-pShadowBaseRightX);
			params.YShift = Math.abs(pShadowBaseLeftY-pShadowBaseRightY);
			params.FrontWidth = wFront;
			return params;
		}
 
		
		public static function DrawColumn3D(g:Graphics, x:Number, y:Number, w:Number, h:Number,
											color:uint, alpha:Number, negative:Boolean, 
											xShift:Number, yShift:Number, frontWidth:Number):void
		{
			var darkShadowColor:uint = DrawUtil.AdjustColorBrightness(color, DARK_SHADOW_INTENSITY);
			var lightShadowColor:uint = DrawUtil.AdjustColorBrightness(color, LIGHT_SHADOW_INTENSITY);
			
			//Calculate the various positions of the points
			var pShadowBaseRightX:Number, pShadowBaseRightY:Number;
			var pShadowBaseLeftX:Number, pShadowBaseLeftY:Number;
			var pShadowTopLeftX:Number, pShadowTopLeftY:Number;
			var pShadowTopRightX:Number, pShadowTopRightY:Number;
			var pCapTopLeftX:Number, pCapTopLeftY:Number;
			var pCapBaseLeftX:Number, pCapBaseLeftY:Number;
			var pColBaseLeftX:Number, pColBaseLeftY:Number;
			//Three full filled polygons to be drawn
			//1 (Shadow) - (pShadowBaseRightX, pShadowBaseRightY) - (pShadowBaseLeftX, pShadowBaseLeftY) - ( pShadowTopLeftX, pShadowTopLeftY) - (pShadowTopRightX, pShadowTopRightY)
			//2 (Cap) - (pShadowTopLeftX, pShadowTopLeftY) - (pShadowTopRightX, pShadowTopRightY)- (pCapTopLeftX, pCapTopLeftY) - (pCapBaseLeftX, pCapBaseLeftY)
			//3 (Front) - (pCapBaseLeftX, pCapBaseLeftY) - (pShadowTopLeftX, pShadowTopLeftY) - (pShadowBaseLeftX, pShadowBaseLeftY) - (pColBaseLeftX, pColBaseLeftY)
			if(!negative)
			{
				//Draw column
				pShadowBaseRightX = x + frontWidth;
				pShadowBaseRightY = y;
				pShadowBaseLeftX = pShadowBaseRightX - xShift;
				pShadowBaseLeftY = pShadowBaseRightY + yShift;
				pShadowTopLeftX = pShadowBaseLeftX;
				pShadowTopLeftY = pShadowBaseLeftY - h;
				pShadowTopRightX = pShadowBaseRightX;
				pShadowTopRightY = pShadowBaseRightY - h;
				pCapTopLeftX = pShadowTopRightX - frontWidth;
				pCapTopLeftY = pShadowTopRightY;
				pCapBaseLeftX = pShadowTopLeftX - frontWidth;
				pCapBaseLeftY = pShadowTopLeftY;
				pColBaseLeftX = pCapBaseLeftX;
				pColBaseLeftY = pCapBaseLeftY + h;
				
				//g.clear();
				g.beginFill(darkShadowColor, alpha);
				
				//Draw the Shadow
				g.moveTo(pShadowBaseRightX, pShadowBaseRightY);
				g.lineTo(pShadowBaseLeftX, pShadowBaseLeftY);
				g.lineTo(pShadowTopLeftX, pShadowTopLeftY);
				g.lineTo(pShadowTopRightX, pShadowTopRightY);
				g.lineTo(pShadowBaseRightX, pShadowBaseRightY);
				g.endFill();
				//Draw the column Cap
				g.beginFill(lightShadowColor, alpha);
				g.moveTo(pShadowTopLeftX, pShadowTopLeftY);
				g.lineTo(pShadowTopRightX, pShadowTopRightY);
				g.lineTo(pCapTopLeftX, pCapTopLeftY);
				g.lineTo(pCapBaseLeftX, pCapBaseLeftY);
				g.lineTo(pShadowTopLeftX, pShadowTopLeftY);
				g.endFill();
				//Draw the column Front
				g.beginFill(color, alpha);
				g.moveTo(pCapBaseLeftX, pCapBaseLeftY);
				g.lineTo(pShadowTopLeftX, pShadowTopLeftY);
				g.lineTo(pShadowBaseLeftX, pShadowBaseLeftY);
				g.lineTo(pColBaseLeftX, pColBaseLeftY);
				g.lineTo(pCapBaseLeftX, pCapBaseLeftY);
				g.endFill();
			}
			else
			{
				//Draw negative column
				pShadowTopRightX = x + frontWidth;
				pShadowTopRightY = y;
				pShadowTopLeftX = pShadowTopRightX - xShift;
				pShadowTopLeftY = pShadowTopRightY + yShift;
				pCapTopLeftX = pShadowTopRightX - frontWidth;
				pCapTopLeftY = pShadowTopRightY;
				pCapBaseLeftX = pShadowTopLeftX - frontWidth;
				pCapBaseLeftY = pShadowTopLeftY;
				pShadowBaseRightX = pShadowTopRightX;
				pShadowBaseRightY = pShadowTopRightY + h;
				pShadowBaseLeftX = pShadowTopLeftX;
				pShadowBaseLeftY = pShadowTopLeftY + h;
				pColBaseLeftX = pCapBaseLeftX;
				pColBaseLeftY = pCapBaseLeftY + h;
				
				//g.clear();
				g.beginFill(darkShadowColor, alpha);
				//Draw the Shadow
				g.moveTo(pShadowBaseRightX, pShadowBaseRightY);
				g.lineTo(pShadowBaseLeftX, pShadowBaseLeftY);
				g.lineTo(pShadowTopLeftX, pShadowTopLeftY);
				g.lineTo(pShadowTopRightX, pShadowTopRightY);
				g.lineTo(pShadowBaseRightX, pShadowBaseRightY);
				g.endFill();
				//Draw the column Cap
				g.beginFill(lightShadowColor, alpha);
				g.moveTo(pShadowTopLeftX, pShadowTopLeftY);
				g.lineTo(pShadowTopRightX, pShadowTopRightY);
				g.lineTo(pCapTopLeftX, pCapTopLeftY);
				g.lineTo(pCapBaseLeftX, pCapBaseLeftY);
				g.lineTo(pShadowTopLeftX, pShadowTopLeftY);
				g.endFill();
				//Draw the column Front
				g.beginFill(color, alpha);
				g.moveTo(pCapBaseLeftX, pCapBaseLeftY);
				g.lineTo(pShadowTopLeftX, pShadowTopLeftY);
				g.lineTo(pShadowBaseLeftX, pShadowBaseLeftY);
				g.lineTo(pColBaseLeftX, pColBaseLeftY);
				g.lineTo(pCapBaseLeftX, pCapBaseLeftY);
				g.endFill();
			}
//			g.lineStyle(1);
//			g.moveTo(x,0);
//			g.lineTo(x,400);
//			g.moveTo(x-XShift,0);
//			g.lineTo(x-XShift,400);
		}
		
		internal static function DrawCanvas(g:Graphics, x:Number, y:Number, w:Number, h:Number, color:uint, alpha:Number):void
		{
			var canvasBgStartX:Number = x;
			var canvasBgStartY:Number = y;
			var canvasBgEndX:Number = canvasBgStartX + w;
			var canvasBgEndY:Number = canvasBgStartY + h;
			var canvasBgDepth:Number = 3;
			
			g.beginFill(color, alpha);
			g.moveTo(canvasBgStartX, canvasBgStartY);
			g.lineTo(canvasBgEndX, canvasBgStartY);
			g.lineTo(canvasBgEndX, canvasBgEndY);
			g.lineTo(canvasBgStartX, canvasBgEndY);
			g.lineTo(canvasBgStartX, canvasBgStartY);
			g.endFill();
			//Create the shadow now (right side)
			var canvasShadowColor:uint = DrawUtil.AdjustColorBrightness(color, LIGHT_SHADOW_INTENSITY);
			g.beginFill(canvasShadowColor, alpha);
			g.moveTo(canvasBgEndX, canvasBgStartY);
			g.lineTo(canvasBgEndX+canvasBgDepth, canvasBgStartY+canvasBgDepth);
			g.lineTo(canvasBgEndX+canvasBgDepth, canvasBgEndY-canvasBgDepth);
			g.lineTo(canvasBgEndX, canvasBgEndY);
			g.lineTo(canvasBgEndX, canvasBgStartY);
			g.endFill();
		}
		
		internal static function DrawCanvasBar(g:Graphics, x:Number, y:Number, w:Number, h:Number, color:uint, alpha:Number, 
											   xShift:Number, yShift:Number):void
		{
			var canvasDarkColor:uint, canvasLightColor:uint;
			canvasDarkColor = DrawUtil.AdjustColorBrightness(color, DARK_SHADOW_INTENSITY);
			canvasLightColor = DrawUtil.AdjustColorBrightness(color, LIGHT_SHADOW_INTENSITY);
			//Re-allocation of variables for local access
			var canvasStartX:Number = x;
			var canvasStartY:Number = y;
			var canvasWidth:Number = w;
			var canvasDepth:Number = h;
			//Create the top part
			g.beginFill(canvasLightColor, alpha);
			g.lineStyle(1, canvasLightColor, alpha);
			g.moveTo(canvasStartX, canvasStartY);
			g.lineTo(canvasStartX+canvasWidth, canvasStartY);
			g.lineTo((canvasStartX+canvasWidth)-xShift, canvasStartY+yShift);
			g.lineTo(canvasStartX-xShift, canvasStartY+yShift);
			g.lineTo(canvasStartX, canvasStartY);
			g.endFill();
			//Create the front part
			g.beginFill(color, alpha);
			g.lineStyle(1, color, alpha);
			g.moveTo(canvasStartX-xShift, canvasStartY+yShift);
			g.lineTo(canvasStartX-xShift, canvasStartY+yShift+canvasDepth);
			g.lineTo((canvasStartX+canvasWidth)-xShift, canvasStartY+yShift+canvasDepth);
			g.lineTo(canvasStartX+canvasWidth-xShift, canvasStartY+canvasDepth+yShift);
			g.lineTo(canvasStartX+canvasWidth-xShift, canvasStartY+yShift);
			g.endFill();
			//Create the right most part
			g.beginFill(canvasDarkColor, alpha);
			g.lineStyle(1, canvasDarkColor, alpha);
			g.moveTo(canvasStartX+canvasWidth-xShift, canvasStartY+yShift);
			g.lineTo(canvasStartX+canvasWidth, canvasStartY);
			g.lineTo(canvasStartX+canvasWidth, canvasStartY+canvasDepth);
			g.lineTo(canvasStartX+canvasWidth-xShift, canvasStartY+canvasDepth+yShift);
			g.lineTo(canvasStartX+canvasWidth-xShift, canvasStartY+yShift);
			g.endFill();
		}
		
		internal static function DrawCanvasZero(g:Graphics, x:Number, y:Number, w:Number, color:uint, alpha:Number,
												xShift:Number, yShift:Number):void
		{
			var canvasStartX:Number = x;
			var canvasStartY:Number = y;
			var canvasWidth:Number = w;
			//Create the top part
			g.beginFill(color, alpha * 0.4);
			g.lineStyle(1, color, alpha * 0.7);
			g.moveTo(canvasStartX, canvasStartY);
			g.lineTo(canvasStartX+canvasWidth, canvasStartY);
			g.lineTo((canvasStartX+canvasWidth)-xShift, canvasStartY+yShift);
			g.lineTo(canvasStartX-xShift, canvasStartY+yShift);
			g.lineTo(canvasStartX, canvasStartY);
			g.endFill();
		}
	}
}