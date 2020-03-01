/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


绘图工具类。


*/
package hlib
{
	import flash.display.GradientType;
	import flash.display.Graphics;
	import flash.geom.Matrix;
	import flash.geom.Point;
	
	public final class DrawUtil
	{
		private static var _Matrix:Matrix = new Matrix();
		/**
		 * Draws a rounded rectangle using the size of a radius to draw the rounded corners. 
		 * You must set the line style, fill, or both 
		 * on the Graphics object before 
		 * you call the <code>drawRoundRectComplex()</code> method 
		 * by calling the <code>linestyle()</code>, 
		 * <code>lineGradientStyle()</code>, <code>beginFill()</code>, 
		 * <code>beginGradientFill()</code>, or 
		 * <code>beginBitmapFill()</code> method.
		 * 
		 * @param graphics The Graphics object that draws the rounded rectangle.
		 *
		 * @param x The horizontal position relative to the 
		 * registration point of the parent display object, in pixels.
		 * 
		 * @param y The vertical position relative to the 
		 * registration point of the parent display object, in pixels.
		 * 
		 * @param width The width of the round rectangle, in pixels.
		 * 
		 * @param height The height of the round rectangle, in pixels.
		 * 
		 * @param topLeftRadius The radius of the upper-left corner, in pixels.
		 * 
		 * @param topRightRadius The radius of the upper-right corner, in pixels.
		 * 
		 * @param bottomLeftRadius The radius of the bottom-left corner, in pixels.
		 * 
		 * @param bottomRightRadius The radius of the bottom-right corner, in pixels.
		 *
		 *  
		 *  @langversion 3.0
		 *  @playerversion Flash 9
		 *  @playerversion AIR 1.1
		 *  @productversion Flex 3
		 */
		private static function DrawRoundRectComplex(graphics:Graphics, x:Number, y:Number, 
													 width:Number, height:Number, 
													 topLeftRadius:Number, topRightRadius:Number, 
													 bottomLeftRadius:Number, bottomRightRadius:Number):void
		{
			var xw:Number = x + width;
			var yh:Number = y + height;
			
			// Make sure none of the radius values are greater than w/h.
			// These are all inlined to avoid function calling overhead
			var minSize:Number = width < height ? width * 2 : height * 2;
			topLeftRadius = topLeftRadius < minSize ? topLeftRadius : minSize;
			topRightRadius = topRightRadius < minSize ? topRightRadius : minSize;
			bottomLeftRadius = bottomLeftRadius < minSize ? bottomLeftRadius : minSize;
			bottomRightRadius = bottomRightRadius < minSize ? bottomRightRadius : minSize;
			
			// Math.sin and Math,tan values for optimal performance.
			// Math.rad = Math.PI / 180 = 0.0174532925199433
			// r * Math.sin(45 * Math.rad) =  (r * 0.707106781186547);
			// r * Math.tan(22.5 * Math.rad) = (r * 0.414213562373095);
			//
			// We can save further cycles by precalculating
			// 1.0 - 0.707106781186547 = 0.292893218813453 and
			// 1.0 - 0.414213562373095 = 0.585786437626905
			
			// bottom-right corner
			var a:Number = bottomRightRadius * 0.292893218813453;		// radius - anchor pt;
			var s:Number = bottomRightRadius * 0.585786437626905; 	// radius - control pt;
			graphics.moveTo(xw, yh - bottomRightRadius);
			graphics.curveTo(xw, yh - s, xw - a, yh - a);
			graphics.curveTo(xw - s, yh, xw - bottomRightRadius, yh);
			
			// bottom-left corner
			a = bottomLeftRadius * 0.292893218813453;
			s = bottomLeftRadius * 0.585786437626905;
			graphics.lineTo(x + bottomLeftRadius, yh);
			graphics.curveTo(x + s, yh, x + a, yh - a);
			graphics.curveTo(x, yh - s, x, yh - bottomLeftRadius);
			
			// top-left corner
			a = topLeftRadius * 0.292893218813453;
			s = topLeftRadius * 0.585786437626905;
			graphics.lineTo(x, y + topLeftRadius);
			graphics.curveTo(x, y + s, x + a, y + a);
			graphics.curveTo(x + s, y, x + topLeftRadius, y);
			
			// top-right corner
			a = topRightRadius * 0.292893218813453;
			s = topRightRadius * 0.585786437626905;
			graphics.lineTo(xw - topRightRadius, y);
			graphics.curveTo(xw - s, y, xw - a, y + a);
			graphics.curveTo(xw, y + s, xw, y + topRightRadius);
			graphics.lineTo(xw, yh - bottomRightRadius);
		}
		
		/**
		 * Draws a rounded rectangle using the size of individual x and y radii to 
		 * draw the rounded corners. 
		 * You must set the line style, fill, or both 
		 * on the Graphics object before 
		 * you call the <code>drawRoundRectComplex2()</code> method 
		 * by calling the <code>linestyle()</code>, 
		 * <code>lineGradientStyle()</code>, <code>beginFill()</code>, 
		 * <code>beginGradientFill()</code>, or 
		 * <code>beginBitmapFill()</code> method.
		 * 
		 * @param graphics The Graphics object that draws the rounded rectangle.
		 *
		 * @param x The horizontal position relative to the 
		 * registration point of the parent display object, in pixels.
		 * 
		 * @param y The vertical position relative to the 
		 * registration point of the parent display object, in pixels.
		 * 
		 * @param width The width of the round rectangle, in pixels.
		 * 
		 * @param height The height of the round rectangle, in pixels.
		 * 
		 * @param radiusX The default radiusX to use, if corner-specific values are not specified.
		 * This value must be specified.
		 * 
		 * @param radiusY The default radiusY to use, if corner-specific values are not specified. 
		 * If 0, the value of radiusX is used.
		 * 
		 * @param topLeftRadiusX The x radius of the upper-left corner, in pixels. If NaN, 
		 * the value of radiusX is used.
		 * 
		 * @param topLeftRadiusY The y radius of the upper-left corner, in pixels. If NaN,
		 * the value of topLeftRadiusX is used.
		 * 
		 * @param topRightRadiusX The x radius of the upper-right corner, in pixels. If NaN,
		 * the value of radiusX is used.
		 * 
		 * @param topRightRadiusY The y radius of the upper-right corner, in pixels. If NaN,
		 * the value of topRightRadiusX is used.
		 * 
		 * @param bottomLeftRadiusX The x radius of the bottom-left corner, in pixels. If NaN,
		 * the value of radiusX is used.
		 * 
		 * @param bottomLeftRadiusY The y radius of the bottom-left corner, in pixels. If NaN,
		 * the value of bottomLeftRadiusX is used.
		 * 
		 * @param bottomRightRadiusX The x radius of the bottom-right corner, in pixels. If NaN,
		 * the value of radiusX is used.
		 * 
		 * @param bottomRightRadiusY The y radius of the bottom-right corner, in pixels. If NaN,
		 * the value of bottomRightRadiusX is used.
		 * 
		 *  
		 *  @langversion 3.0
		 *  @playerversion Flash 10
		 *  @playerversion AIR 1.5
		 *  @productversion Flex 4
		 */
		private static function DrawRoundRectComplex2(graphics:Graphics, x:Number, y:Number, 
													  width:Number, height:Number, 
													  radiusX:Number, radiusY:Number,
													  topLeftRadiusX:Number, topLeftRadiusY:Number,
													  topRightRadiusX:Number, topRightRadiusY:Number,
													  bottomLeftRadiusX:Number, bottomLeftRadiusY:Number,
													  bottomRightRadiusX:Number, bottomRightRadiusY:Number):void
		{
			var xw:Number = x + width;
			var yh:Number = y + height;
			var maxXRadius:Number = width / 2;
			var maxYRadius:Number = height / 2;
			
			// Rules for determining radius for each corner:
			//  - If explicit nnnRadiusX value is set, use it. Otherwise use radiusX.
			//  - If explicit nnnRadiusY value is set, use it. Otherwise use corresponding nnnRadiusX.
			if (radiusY == 0)
				radiusY = radiusX;
			if (isNaN(topLeftRadiusX))
				topLeftRadiusX = radiusX;
			if (isNaN(topLeftRadiusY))
				topLeftRadiusY = topLeftRadiusX;
			if (isNaN(topRightRadiusX))
				topRightRadiusX = radiusX;
			if (isNaN(topRightRadiusY))
				topRightRadiusY = topRightRadiusX;
			if (isNaN(bottomLeftRadiusX))
				bottomLeftRadiusX = radiusX;
			if (isNaN(bottomLeftRadiusY))
				bottomLeftRadiusY = bottomLeftRadiusX;
			if (isNaN(bottomRightRadiusX))
				bottomRightRadiusX = radiusX;
			if (isNaN(bottomRightRadiusY))
				bottomRightRadiusY = bottomRightRadiusX;
			
			// Pin radius values to half of the width/height
			if (topLeftRadiusX > maxXRadius)
				topLeftRadiusX = maxXRadius;
			if (topLeftRadiusY > maxYRadius)
				topLeftRadiusY = maxYRadius;
			if (topRightRadiusX > maxXRadius)
				topRightRadiusX = maxXRadius;
			if (topRightRadiusY > maxYRadius)
				topRightRadiusY = maxYRadius;
			if (bottomLeftRadiusX > maxXRadius)
				bottomLeftRadiusX = maxXRadius;
			if (bottomLeftRadiusY > maxYRadius)
				bottomLeftRadiusY = maxYRadius;
			if (bottomRightRadiusX > maxXRadius)
				bottomRightRadiusX = maxXRadius;
			if (bottomRightRadiusY > maxYRadius)
				bottomRightRadiusY = maxYRadius;
			
			// Math.sin and Math,tan values for optimal performance.
			// Math.rad = Math.PI / 180 = 0.0174532925199433
			// r * Math.sin(45 * Math.rad) =  (r * 0.707106781186547);
			// r * Math.tan(22.5 * Math.rad) = (r * 0.414213562373095);
			//
			// We can save further cycles by precalculating
			// 1.0 - 0.707106781186547 = 0.292893218813453 and
			// 1.0 - 0.414213562373095 = 0.585786437626905
			
			// bottom-right corner
			var aX:Number = bottomRightRadiusX * 0.292893218813453;		// radius - anchor pt;
			var aY:Number = bottomRightRadiusY * 0.292893218813453;		// radius - anchor pt;
			var sX:Number = bottomRightRadiusX * 0.585786437626905; 	// radius - control pt;
			var sY:Number = bottomRightRadiusY * 0.585786437626905; 	// radius - control pt;
			graphics.moveTo(xw, yh - bottomRightRadiusY);
			graphics.curveTo(xw, yh - sY, xw - aX, yh - aY);
			graphics.curveTo(xw - sX, yh, xw - bottomRightRadiusX, yh);
			
			// bottom-left corner
			aX = bottomLeftRadiusX * 0.292893218813453;
			aY = bottomLeftRadiusY * 0.292893218813453;
			sX = bottomLeftRadiusX * 0.585786437626905;
			sY = bottomLeftRadiusY * 0.585786437626905;
			graphics.lineTo(x + bottomLeftRadiusX, yh);
			graphics.curveTo(x + sX, yh, x + aX, yh - aY);
			graphics.curveTo(x, yh - sY, x, yh - bottomLeftRadiusY);
			
			// top-left corner
			aX = topLeftRadiusX * 0.292893218813453;
			aY = topLeftRadiusY * 0.292893218813453;
			sX = topLeftRadiusX * 0.585786437626905;
			sY = topLeftRadiusY * 0.585786437626905;
			graphics.lineTo(x, y + topLeftRadiusY);
			graphics.curveTo(x, y + sY, x + aX, y + aY);
			graphics.curveTo(x + sX, y, x + topLeftRadiusX, y);
			
			// top-right corner
			aX = topRightRadiusX * 0.292893218813453;
			aY = topRightRadiusY * 0.292893218813453;
			sX = topRightRadiusX * 0.585786437626905;
			sY = topRightRadiusY * 0.585786437626905;
			graphics.lineTo(xw - topRightRadiusX, y);
			graphics.curveTo(xw - sX, y, xw - aX, y + aY);
			graphics.curveTo(xw, y + sY, xw, y + topRightRadiusY);
			graphics.lineTo(xw, yh - bottomRightRadiusY);
		}
		/**
		 * 开始填充
		 * @param c： color，颜色，uint或Array或null不填充
		 * @param alpha： Number或Array或null不透明
		 * @param rotation： 角度，Number或{ x: #, y: #, w: #, h: #, r:}或Matrix
		 * @param gradient： GradientType.LINEAR或GradientType.RADIAL
		 * @param ratios： [0,255]
		 */  
		public static function BeginFill(g:Graphics, x:Number, y:Number, w:Number, h:Number,
										 c:Object = null, alpha:Object = null, rotation:Object = null,
										 gradient:String = null, ratios:Array = null):void
		{
			if (c !== null)
			{
				if (c is Array)
				{
					var alphas:Array;
					if (alpha is Array)
						alphas = alpha as Array;
					else
						alphas = [alpha, alpha];
					
					if (!ratios)
						ratios = [0, 0xFF];
					
					var matrix:Matrix = null;
					
					
					if (rotation is Matrix)
					{
						matrix = Matrix(rotation);
					}
					else if(rotation is Number)
					{
						matrix = _Matrix;
						matrix.createGradientBox(w, h, Number(rotation)*Math.PI/180, x, y);
					}
					else if(rotation)
					{
						matrix.createGradientBox(rotation.w, rotation.h, rotation.r, rotation.x, rotation.y);
					}
					
					
					if (gradient == GradientType.RADIAL)
					{
						g.beginGradientFill(GradientType.RADIAL, c as Array, alphas, ratios, matrix);
					}
					else
					{
						g.beginGradientFill(GradientType.LINEAR, c as Array, alphas, ratios, matrix);
					}
				}
				else
				{
					g.beginFill(uint(c), Number(alpha));
				}
			}
		}
		/**
		 * 画圆角矩形
		 * @param r： radius，圆角半径，Number或Object{ tl: 5, tr: 5, bl: 0, br: 0 }或null没有圆角
		 * @param c： color，颜色，uint或Array或null不填充
		 * @param alpha： Number或Array或null不透明
		 * @param rotation： 角度，Number或{ x: #, y: #, w: #, h: #, r:}或Matrix
		 * @param gradient： GradientType.LINEAR或GradientType.RADIAL
		 * @param ratios： [0,255]
		 * @param hole： 画矩形边框{ x: #, y: #, w: #, h: #, r: # 或 { br: #, bl: #, tl: #, tr: # } }或null
		 */  
		private static function DrawRoundRect(g:Graphics, x:Number, y:Number, w:Number, h:Number,
											  r:Object = null, c:Object = null,
											  alpha:Object = null, rotation:Object = null,
											  gradient:String = null, ratios:Array = null,
											  hole:Object = null):void
		{
			if (!w || !h)
				return;
			
			// If color is an object then allow for complex fills.
			BeginFill(g, x, y, w, h, c, alpha, rotation, gradient, ratios);

			var ellipseSize:Number;
			// Stroke the rectangle.
			if (!r)
			{
				g.drawRect(x, y, w, h);
			}
			else if (r is Number)
			{
				ellipseSize = Number(r) * 2;
				g.drawRoundRect(x, y, w, h, ellipseSize, ellipseSize);
			}
			else
			{
				DrawRoundRectComplex(g, x, y, w, h, r.tl, r.tr, r.bl, r.br);
			}
			
			// Carve a rectangular hole out of the middle of the rounded rect.
			if (hole)
			{
				var holeR:Object = hole.r;
				if (holeR is Number)
				{
					ellipseSize = Number(holeR) * 2;
					g.drawRoundRect(hole.x, hole.y, hole.w, hole.h, ellipseSize, ellipseSize);
				}
				else
				{
					DrawRoundRectComplex(g, hole.x, hole.y, hole.w, hole.h,
						holeR.tl, holeR.tr, holeR.bl, holeR.br);
				}
			}
			
			if (c !== null)
				g.endFill();
		}
		/**
		 * 调整颜色亮度
		 * @param brite： -100 ~ 100，正数变亮，负数变暗
		 */ 
		public static function AdjustColorBrightness(rgb:uint, brite:Number):uint
		{
			var r:Number;
			var g:Number;
			var b:Number;
			
			if (brite == 0)
				return rgb;
			
			if (brite < 0)
			{
				brite = (100 + brite) / 100;
				r = ((rgb >> 16) & 0xFF) * brite;
				g = ((rgb >> 8) & 0xFF) * brite;
				b = (rgb & 0xFF) * brite;
			}
			else // bright > 0
			{
				brite /= 100;
				r = ((rgb >> 16) & 0xFF);
				g = ((rgb >> 8) & 0xFF);
				b = (rgb & 0xFF);
				
				r += ((0xFF - r) * brite);
				g += ((0xFF - g) * brite);
				b += ((0xFF - b) * brite);
				
				r = Math.min(r, 255);
				g = Math.min(g, 255);
				b = Math.min(b, 255);
			}
			
			return (r << 16) | (g << 8) | b;
		}
		/**
		 * 画虚线
		 * length： 线条长度
		 * gap： 线条间距
		 */ 
		public static function DrawDashLine(g:Graphics, p1:Point, p2:Point, length:Number = 5, gap:Number = 5):void
		{
			var max:Number = Point.distance(p1, p2);
			var l:Number = 0;
			var p3:Point;
			var p4:Point;
			while (l < max)
			{
				p3 = Point.interpolate(p2, p1, l / max);
				l += length;
				if (l > max)
					l = max;
				p4 = Point.interpolate(p2, p1, l / max);
				g.moveTo(p3.x, p3.y)
				g.lineTo(p4.x, p4.y)
				l += gap;
			}
		}
		/**
		 * 画虚线矩形
		 */  
		public static function DrawDashRect(g:Graphics, x:Number = 0, y:Number = 0, w:Number = 200, h:Number = 100):void
		{
			DrawDashLine(g, new Point(x, y), new Point(x + w, y));
			DrawDashLine(g, new Point(x + w, y), new Point(x + w, y + h));
			DrawDashLine(g, new Point(x + w, y + h), new Point(x, y + h));
			DrawDashLine(g, new Point(x, y + h), new Point(x, y));
		}
		
		/**
		 * 绘制背景
		 * @param c： color，颜色，uint或Array或null不填充
		 * @param a： alpha，Number或Array或null不透明
		 * @param fillMode： null，linear，vblock，hblock
		 * @param r： radius，圆角半径，Number或Object{ tl: 5, tr: 5, bl: 0, br: 0 }或null没有圆角
		 * @param rotation： 水平0，垂直90
		 */ 
		public static function DrawBackgrouand(g:Graphics, x:Number, y:Number, w:Number, h:Number, 
											   c:Object, a:Object, fillMode:String = null, r:Object = null, 
											   rotation:Number = 90):void
		{
			var radius:Object;
			if(!fillMode)
			{
				DrawRoundRect(g, x, y, w, h, r, c, a, rotation);
				return;
			}
			fillMode = fillMode.toLowerCase();
			
			if (fillMode == "linear")
			{
				DrawRoundRect(g, x, y, w, h, r, c, a, rotation, GradientType.LINEAR);
			}
			else if (fillMode == "radial")
			{
				DrawRoundRect(g, x, y, w, h, r, c, a, rotation, GradientType.RADIAL);
			}
			else if (fillMode == "vblock" && c is Array)
			{
				if(!r)
					radius = null;
				else if(r is Number)
					radius = {tl: r, tr: r, bl: 0, br: 0};
				else
					radius = {tl: r.tl, tr: r.tr, bl: 0, br: 0};
				
				DrawRoundRect(g, x, y, w, h/2, radius, c[0], a[0]);
				
				if(!r)
					radius = null;
				else if(r is Number)
					radius = {tl: 0, tr: 0, bl: r, br: r};
				else
					radius = {tl: 0, tr: 0, bl: r.bl, br: r.br};
				
				DrawRoundRect(g, x, y+h/2, w, h/2, radius, c[1], a[1]);
				
			}
			else if (fillMode == "hblock" && c is Array)
			{
				if(!r)
					radius = null;
				else if(r is Number)
					radius = {tl: r, tr: 0, bl: r, br: 0};
				else
					radius = {tl: r.tl, tr: 0, bl: r.bl, br: 0};
				
				DrawRoundRect(g, x, y, w/2, h, radius, c[0], a[0]);
				
				if(!r)
					radius = null;
				else if(r is Number)
					radius = {tl: 0, tr: r, bl: 0, br: r};
				else
					radius = {tl: 0, tr: r.tr, bl: 0, br: r.br};
				
				DrawRoundRect(g, x+w/2, y, w/2, h, radius, c[1], a[1]);
			}
		}
		/**
		 * 绘制边框
		 * @param r： radius，圆角半径，Number或Object{ tl: 5, tr: 5, bl: 0, br: 0 }或null没有圆角
		 */ 
		public static function DrawBorder(g:Graphics, x:Number, y:Number, w:Number, h:Number, 
										  c:uint, a:Number, r:Object = null, thickness:Number = 1, innerBorder:Boolean = false):void
		{
			var bt:Number = thickness;
			
			var radius:Object;
			var hole:Object;
			
			if(!r)
			{
				radius = null;
				hole = {x: x+bt, y: y+bt, w: w-bt*2, h: h-bt*2, r: 0};
			}
			else if(r is Number)
			{
				radius = {tl: r, tr: r, bl: r, br: r};
				hole = {x: x+bt, y: y+bt, w: w-bt*2, h: h-bt*2, r: Math.max(Number(r)-bt, 0)};
				
			}
			else
			{
				radius = {tl: r.tl, tr: r.tr, bl: r.bl, br: r.br};
				hole = {x: x+bt, y: y+bt, w: w-bt*2, h: h-bt*2, 
					r:{tl: Math.max(r.tl-bt,0), tr: Math.max(r.tr-bt,0), 
						bl: Math.max(r.bl-bt,0), br: Math.max(r.br-bt,0)}};
			}
			DrawRoundRect(g, x, y, w, h, radius, c, a, null, null, null, hole);
			
			if (innerBorder)
			{
				x += bt;
				y += bt;
				w = Math.max(w - bt * 2, 0);
				h = Math.max(h - bt * 2, 0);
				if(radius)
				{
					radius.tl = Math.max(radius.tl-bt, 0);
					radius.tr = Math.max(radius.tr-bt, 0);
					radius.bl = Math.max(radius.bl-bt, 0);
					radius.br = Math.max(radius.br-bt, 0);
				}
				hole.x += bt;
				hole.y += bt;
				hole.w = Math.max(hole.w - bt * 2, 0);
				hole.h = Math.max(hole.h - bt * 2, 0);
				if(hole.r is Number)
				{
					hole.r = Math.max(hole.r-bt, 0);
				}
				else
				{
					hole.r.tl = Math.max(hole.r.tl-bt, 0);
					hole.r.tr = Math.max(hole.r.tr-bt, 0);
					hole.r.bl = Math.max(hole.r.bl-bt, 0);
					hole.r.br = Math.max(hole.r.br-bt, 0);
				}
				
				c = AdjustColorBrightness(c, 40);
				DrawRoundRect(g, x, y, w, h, radius, c, a, null, null, null, hole);
			}
			
		}
		public static function DrawBorder3D(graphics:Graphics, x:Number, y:Number, w:Number, h:Number, 
												color:uint, style:String):void
		{
			
			var borderColorDrk1:uint;
			var borderColorDrk2:uint;
			var borderColorLt1:uint;
			
			var c1:Number;
			var c2:Number;
			var c3:Number;
			var c4:Number;
			var c5:Number;
			var c6:Number;
			
			if (style == "outset")
			{
				borderColorDrk1 = AdjustColorBrightness(color, -40);
				borderColorDrk2 = AdjustColorBrightness(color, -25);
				borderColorLt1 = AdjustColorBrightness(color, +40);
				
				c1 = borderColorDrk2;
				c2 = borderColorLt1;
				c3 = borderColorDrk1;
			}
			else if (style == "inset")
			{
				borderColorDrk1 = AdjustColorBrightness(color, -40);
				borderColorDrk2 = AdjustColorBrightness(color, +25);
				borderColorLt1 = AdjustColorBrightness(color, +40);
				
				c1 = borderColorDrk2;
				c2 = borderColorDrk1;
				c3 = borderColorLt1;
				
			}
			c4 = color;
			c5 = color;
			c6 = color;
			
			var g:Graphics = graphics;
			
			// outside sides:1
			g.beginFill(c1);
			g.drawRect(x, y, w, h);
			g.drawRect(x + 1, y, w - 2, h);
			g.endFill();
			
			// outside top:1
			g.beginFill(c2);
			g.drawRect(x + 1, y, w - 2, 1);
			g.endFill();
			
			// outside bottom:1
			g.beginFill(c3);
			g.drawRect(x + 1, y + h - 1, w - 2, 1);
			g.endFill();
			
			// inside top:1
			g.beginFill(c4);
			g.drawRect(x + 1, y + 1, w - 2, 1);
			g.endFill();
			
			// inside bottom:1
			g.beginFill(c5);
			g.drawRect(x + 1, y + h - 2, w - 2, 1);
			g.endFill();
			
			// inside sides:2
			g.beginFill(c6);
			g.drawRect(x + 1, y + 2, w - 2, h - 4);
			g.drawRect(x + 2, y + 2, w - 4, h - 4);
			g.endFill();
		}
		/**
		 * 绘制多边形
		 * @param points: item=>{x:Number, y:Number}
		 * @param c： color，颜色，uint或Array或null不填充
		 * @param alpha： Number或Array或null不透明
		 * @param rotation： 角度，Number或{ x: #, y: #, w: #, h: #, r:}或Matrix
		 * @param gradient： GradientType.LINEAR或GradientType.RADIAL
		 * @param ratios： [0,255]
		 */ 
		public static function DrawPoints(g:Graphics, points:Array, x:Number, y:Number, w:Number, h:Number, 
										  borderColor:uint, borderThickness:Number,
										  c:Object, alpha:Object, rotation:Object = null,
										  gradient:String = null, ratios:Array = null):void
		{
			if(!points||points.length<=1) 
				return;
 
			if(borderThickness)
			{
				g.lineStyle(borderThickness, borderColor);
			}
			
			if (c is Array)
			{
				var alphas:Array;
				if (alpha is Array)
					alphas = alpha as Array;
				else
					alphas = [alpha, alpha];
				
				if (!ratios)
					ratios = [0, 0xFF];
				
				var matrix:Matrix = null;
 
				if (rotation is Matrix)
				{
					matrix = Matrix(rotation);
				}
				else if(rotation is Number)
				{
					matrix = _Matrix;
					matrix.createGradientBox(w, h, Number(rotation)*Math.PI/180, x, y);
				}
				else if(rotation)
				{
					matrix.createGradientBox(rotation.w, rotation.h, rotation.r, rotation.x, rotation.y);
				}
 
				if (gradient == GradientType.RADIAL)
				{
					g.beginGradientFill(GradientType.RADIAL, c as Array, alphas, ratios, matrix);
				}
				else
				{
					g.beginGradientFill(GradientType.LINEAR, c as Array, alphas, ratios, matrix);
				}
			}
			else
			{
				g.beginFill(uint(c), Number(alpha));
			}
 
			g.moveTo(points[0].x, points[0].y);
			for(var i:int=1;i<points.length;i++)
			{
				g.lineTo(points[i].x, points[i].y);
			}
			g.lineTo(points[0].x, points[0].y);
			g.endFill();
			g.lineStyle();
			g.endFill();
		}
		/**
		 * p1 -> p2 
		 */
		public static function DrawArrowLine(g:Graphics, p1:Point, p2:Point):void
		{
			var rotate:Number = Math.atan2(p2.y - p1.y, p2.x - p1.x) * 180 / Math.PI;
			var arrowLength:Number = 10;
			var xlength:Number = arrowLength * Math.cos(rotate * Math.PI / 180);
			var ylength:Number = arrowLength * Math.sin(rotate * Math.PI / 180);
			//============开始画线============================
			g.moveTo(p1.x, p1.y);
			g.lineTo(p2.x, p2.y);
			var pointC1:Point = new Point();
			var pointC2:Point = new Point();
			var pointD:Point = new Point();
			var pointF:Point = new Point();
			pointF.x = p2.x - xlength;
			pointF.y = p2.y - ylength;
			var kb:Number = (p2.y - p1.y) / (p2.x - p1.x);
			var k:Number = -1 / kb
			var b:Number = pointF.y - k * pointF.x;
			var widthArrow:Number = 5;
			//===求根公式====
			var a:Number = 1 + k * k;
			var bm:Number = -2 * pointF.x + 2 * k * b - 2 * k * pointF.y;
			var c:Number = pointF.x * pointF.x + b * b + pointF.y * pointF.y - 2 * b * pointF.y - widthArrow * widthArrow;
			var judge:Number = bm * bm - 4 * a * c;
			
			if (judge >= 0 && a != 0)
			{
				pointC1.x = (-bm + Math.sqrt(bm * bm - 4 * a * c)) / (2 * a);
				pointC1.y = k * pointC1.x + b;
			}
			else
			{
				pointC1.x = pointF.x;
				pointC1.y = pointF.y + widthArrow;
			}
			//======利用中点公式=========
			pointD.x = 2 * pointF.x - pointC1.x;
			pointD.y = 2 * pointF.y - pointC1.y;
			//===========画三角======================
			g.moveTo(p2.x, p2.y);
			g.lineTo(pointC1.x, pointC1.y);
			g.moveTo(p2.x, p2.y);
			g.lineTo(pointD.x, pointD.y);
		}
	}
}