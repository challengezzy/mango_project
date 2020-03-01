/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


界面布局工具类。


*/
package hlib
{
	import flash.display.DisplayObject;
	import flash.display.DisplayObjectContainer;
	
	public final class LayoutUtil
	{
		/**
		 * 搜索指定类型组件
		 */ 
		public static function FindType(find:DisplayObject, type:Class):*
		{
			if(find is type)
				return find;
			var p:DisplayObjectContainer = find.parent;
			while(p)
			{
				if(p is type)
				{
					return p;
				}
				p = p.parent;
			}
			
			return null;
		}
		/**
		 * 获取最大子元件宽度
		 * @param ignoreHidden: 忽略隐藏的子元件
		 */ 
		public static function GetMaxChildWidth(container:DisplayObjectContainer, ignoreHidden:Boolean = true):Number
		{
			var max:Number = 0;
			for(var i:int=0;i<container.numChildren; i++)
			{
				var child:DisplayObject = container.getChildAt(i);
				if(ignoreHidden && !child.visible)
					continue;
				max = Math.max(child.width, max);
			}
			return max;
		}
		/**
		 * 获取最大子元件高度
		 * @param ignoreHidden: 忽略隐藏的子元件
		 */ 
		public static function GetMaxChildHeight(container:DisplayObjectContainer, ignoreHidden:Boolean = true):Number
		{
			var max:Number = 0;
			for(var i:int=0;i<container.numChildren; i++)
			{
				var child:DisplayObject = container.getChildAt(i);
				if(ignoreHidden && !child.visible)
					continue;
				max = Math.max(child.height, max);
			}
			return max;
		}
		/**
		 * 根据边距、间距、子元件计算总宽度，子元件必须是水平布局
		 * @param parent: 支持DisplayObjectContainer,Array
		 * @param ignoreHidden: 忽略隐藏的子元件
		 */
		public static function CalculateWidth(parent:*, paddingLeft:Number = 0, paddingRight:Number =0, 
											  gap:Number = 0, ignoreHidden:Boolean = true):Number
		{
			var w:Number = paddingLeft + paddingRight;
			var count:int = 0;
			var child:DisplayObject;
			var i:int;
			if(parent is DisplayObjectContainer)
			{
				var container:DisplayObjectContainer = parent as DisplayObjectContainer;
				for(i=0;i<container.numChildren; i++)
				{
					child = container.getChildAt(i);
					if(ignoreHidden && !child.visible)
						continue;
					w+=child.width+gap;
					count++;
				}
				if(count)
					w-=gap;
			}
			else if(parent is Array)
			{
				var array:Array = parent as Array;
				for(i=0;i<array.length; i++)
				{
					child = array[i];
					if(ignoreHidden && !child.visible)
						continue;
					w+=child.width+gap;
					count++;
				}
				if(count)
					w-=gap;
			}
			
			return w;
		}
		/**
		 * 根据边距、间距、子元件计算总高度，子元件必须是垂直布局
		 * @param parent: 支持DisplayObjectContainer,Array
		 * @param ignoreHidden: 忽略隐藏的子元件
		 */
		public static function CalculateHeight(parent:*, paddingTop:Number = 0, paddingBottom:Number =0, 
											   gap:Number = 0, ignoreHidden:Boolean = true):Number
		{
			var h:Number = paddingTop + paddingBottom;
			var count:int = 0;
			var child:DisplayObject;
			var i:int;
			if(parent is DisplayObjectContainer)
			{
				var container:DisplayObjectContainer = parent as DisplayObjectContainer;
				for(i=0;i<container.numChildren; i++)
				{
					child = container.getChildAt(i);
					if(ignoreHidden && !child.visible)
						continue;
					h+=child.height+gap;
					count++;
				}
				if(count)
					h-=gap;
			}
			else if(parent is Array)
			{
				var array:Array = parent as Array;
				for(i=0;i<array.length; i++)
				{
					child = array[i];
					if(ignoreHidden && !child.visible)
						continue;
					h+=child.height+gap;
					count++;
				}
				if(count)
					h-=gap;
			}
			return h;
		}
		/**
		 * 垂直对齐
		 * @param align: top, middle, bottom
		 * @param ch: d的高度，通过外部传入
		 */ 
		public static function VerticalAlign(d:DisplayObject, h:Number, align:String="top", y:Number = 0, ch:Number = NaN):void
		{
			if(!d) return;
			switch(align)
			{
				case "top":
					d.y = y;
					break;
				case "middle":
					if(isNaN(ch))
						d.y = y + (h - d.height)/2;
					else
						d.y = y + (h - ch)/2;
					break;
				case "bottom":
					if(isNaN(ch))
						d.y = y + h - d.height;
					else
						d.y = y + h - ch;
					break;
			}
		}
		/**
		 * 垂直对齐子元件
		 * @param parent: 支持DisplayObjectContainer,Array
		 * @param align: top, middle, bottom
		 */ 
		public static function VerticalAlignChildren(parent:*, h:Number, align:String="top", y:Number = 0):void
		{
			var child:DisplayObject;
			var i:int;
			if(parent is DisplayObjectContainer)
			{
				var container:DisplayObjectContainer = parent as DisplayObjectContainer;
				for(i=0; i<container.numChildren; i++)
				{
					child = container.getChildAt(i);
					VerticalAlign(child, h, align, y);
				}
			}
			else if(parent is Array)
			{
				var array:Array = parent as Array;
				for(i=0;i<array.length; i++)
				{
					child = array[i];
					VerticalAlign(child, h, align, y);
				}
			}
		}
		
		/**
		 * 水平对齐
		 * @param align: left, center, right
		 * @param cw: d的宽度，通过外部传入
		 */ 
		public static function HorizontalAlign(d:DisplayObject, w:Number, align:String="left", x:Number = 0, cw:Number = NaN):void
		{
			if(!d) return;
			switch(align)
			{
				case "left":
					d.x = x;
					break;
				case "center":
					if(isNaN(cw))
						d.x = x + (w - d.width)/2;
					else
						d.x = x + (w - cw)/2;
					break;
				case "right":
					if(isNaN(cw))
						d.x = x + w - d.width;
					else
						d.x = x + w - cw;
					break;
			}
		}
		/**
		 * 水平对齐子元件
		 * @param parent: 支持DisplayObjectContainer,Array
		 * @param align: left, center, right
		 */ 
		public static function HorizontalAlignChildren(parent:*, w:Number, align:String="left", x:Number = 0):void
		{
			var child:DisplayObject;
			var i:int;
			if(parent is DisplayObjectContainer)
			{
				var container:DisplayObjectContainer = parent as DisplayObjectContainer;
				for(i=0; i<container.numChildren; i++)
				{
					child = container.getChildAt(i);
					HorizontalAlign(child, w, align, x);
				}
			}
			else if(parent is Array)
			{
				var array:Array = parent as Array;
				for(i=0;i<array.length; i++)
				{
					child = array[i];
					HorizontalAlign(child, w, align, x);
				}
			}
		}
		
		/**
		 * 垂直水平居中
		 */ 
		public static function CenterLayout(d:DisplayObject, w:Number, h:Number, x:Number = 0, y:Number = 0, cw:Number = NaN, ch:Number = NaN):void
		{
			if(!d) return;
			if(isNaN(cw))
				d.x = x + (w - d.width)/2;
			else
				d.x = x + (w - cw)/2;
			if(isNaN(ch))
				d.y = y + (h - d.height)/2;
			else
				d.y = y + (h - ch)/2;
		}
		/**
		 * 垂直水平居中多个元件
		 */ 
		public static function CenterLayout2(children:Array, w:Number, h:Number, x:Number = 0, y:Number = 0, gap:Number = 0, ignoreHidden:Boolean = true):void
		{
			if(!children && children.length==0)
				return;
			var totalWidth:Number = 0;
			var child:DisplayObject;
			for each(child in children)
			{
				if(child.visible)
					totalWidth += child.width + gap;
			}
			if(totalWidth>0)
				totalWidth += gap;
			var left:Number = x + (w - totalWidth)/2;
			for each(child in children)
			{
				if(child.visible)
				{
					child.x = left;
					left += child.width + gap;
					child.y = y + (h - child.height)/2;					
				}
			}
		}
		
		/**
		 * 水平布局子元件
		 * @param parent: 支持DisplayObjectContainer,Array
		 * @param ignoreHidden: 忽略隐藏的子元件
		 */
		public static function HorizontalLayoutChildren(parent:*, padding:Number = 0, gap:Number = 0, ignoreHidden:Boolean = true):void
		{
			var left:Number = padding;
			var child:DisplayObject;
			var i:int;
			if(parent is DisplayObjectContainer)
			{
				var container:DisplayObjectContainer = parent as DisplayObjectContainer;
				for(i=0;i<container.numChildren; i++)
				{
					child = container.getChildAt(i);
					if(ignoreHidden && !child.visible)
						continue;
					child.x = left;
					left += child.width + gap;
				}
			}
			else if(parent is Array)
			{
				var array:Array = parent as Array;
				for(i=0;i<array.length; i++)
				{
					child = array[i];
					if(ignoreHidden && !child.visible)
						continue;
					child.x = left;
					left += child.width + gap;
				}
			}
		}
		
		
		/**
		 * 水平布局子元件
		 * @param parent: 支持DisplayObjectContainer,Array
		 * @param ignoreHidden: 忽略隐藏的子元件
		 */
		public static function HorizontalLayoutChildren2(parent:*, w:Number, h:Number,
														 paddingLeft:Number = 0, paddingRight:Number = 0,
														 paddingTop:Number = 0, paddingBottom:Number = 0,
														 gap:Number = 0, 
														 horizontalAlign:String = "left", verticalAlign:String = "top",
														 ignoreHidden:Boolean = true):void
		{
			
			
			var left:Number = paddingLeft;
			var width:Number = CalculateWidth(parent, paddingLeft, paddingRight, gap, ignoreHidden);
			var height:Number = h - paddingTop - paddingBottom;
			if(horizontalAlign == "center")
			{
				left = (w - width + paddingLeft)/2;
			}
			else if(horizontalAlign == "right")
			{
				left = w - width + paddingLeft;
			}
			
			var child:DisplayObject;
			var i:int;
			if(parent is DisplayObjectContainer)
			{
				var container:DisplayObjectContainer = parent as DisplayObjectContainer;
				for(i=0;i<container.numChildren; i++)
				{
					child = container.getChildAt(i);
					if(ignoreHidden && !child.visible)
						continue;
					child.x = left;
					left += child.width + gap;
					
					VerticalAlign(child, height, verticalAlign, paddingTop);
				}
			}
			else if(parent is Array)
			{
				var array:Array = parent as Array;
				for(i=0;i<array.length; i++)
				{
					child = array[i];
					if(ignoreHidden && !child.visible)
						continue;
					child.x = left;
					left += child.width + gap;
					
					VerticalAlign(child, height, verticalAlign, paddingTop);
				}
			}
		}
		
		/**
		 * 垂直布局子元件
		 * @param parent: 支持DisplayObjectContainer,Array
		 * @param ignoreHidden: 忽略隐藏的子元件
		 */
		public static function VerticalLayoutChildren(parent:*, padding:Number = 0, gap:Number = 0, ignoreHidden:Boolean = true):void
		{
			var top:Number = padding;
			var child:DisplayObject;
			var i:int;
			if(parent is DisplayObjectContainer)
			{
				var container:DisplayObjectContainer = parent as DisplayObjectContainer;
				for(i=0;i<container.numChildren; i++)
				{
					child = container.getChildAt(i);
					if(ignoreHidden && !child.visible)
						continue;
					child.y = top;
					top += child.height + gap;
				}
			}
			else if(parent is Array)
			{
				var array:Array = parent as Array;
				for(i=0;i<array.length; i++)
				{
					child = array[i];
					if(ignoreHidden && !child.visible)
						continue;
					child.y = top;
					top += child.height + gap;
				}
			}
		}
		
		/**
		 * 垂直布局子元件
		 * @param parent: 支持DisplayObjectContainer,Array
		 * @param ignoreHidden: 忽略隐藏的子元件
		 */
		public static function VerticalLayoutChildren2(parent:*, w:Number, h:Number,
													   paddingLeft:Number = 0, paddingRight:Number = 0,
													   paddingTop:Number = 0, paddingBottom:Number = 0,
													   gap:Number = 0, 
													   horizontalAlign:String = "left", verticalAlign:String = "top",
													   ignoreHidden:Boolean = true):void
		{
			
			
			var top:Number = paddingTop;
			var width:Number = w - paddingLeft - paddingRight; 
			var height:Number = CalculateHeight(parent, paddingTop, paddingBottom, gap, ignoreHidden);
			if(verticalAlign == "middle")
			{
				top = (h - height + paddingTop)/2;
			}
			else if(verticalAlign == "bottom")
			{
				top = (h - height + paddingTop);
			}
			
			var child:DisplayObject;
			var i:int;
			if(parent is DisplayObjectContainer)
			{
				var container:DisplayObjectContainer = parent as DisplayObjectContainer;
				for(i=0;i<container.numChildren; i++)
				{
					child = container.getChildAt(i);
					if(ignoreHidden && !child.visible)
						continue;
					child.y = top;
					top += child.height + gap;
					
					HorizontalAlign(child, width, horizontalAlign, paddingLeft);
					
				}
			}
			else if(parent is Array)
			{
				var array:Array = parent as Array;
				for(i=0;i<array.length; i++)
				{
					child = array[i];
					if(ignoreHidden && !child.visible)
						continue;
					child.y = top;
					top += child.height + gap;
					
					HorizontalAlign(child, width, horizontalAlign, paddingLeft);
				}
			}
		}
		
		/**
		 * 计算平铺布局子项大小
		 * @param parent: 支持DisplayObjectContainer,Array
		 * @param ignoreHidden: 忽略隐藏的子元件
		 * @return {w:Number, h:Number}
		 */
		private static function CalculateTileSize(parent:*, ignoreHidden:Boolean = true):Object
		{
			var w:Number = 0;
			var h:Number = 0;
			var i:int;
			var child:DisplayObject;
			if(parent is DisplayObjectContainer)
			{
				var container:DisplayObjectContainer = parent as DisplayObjectContainer;
				for(i=0;i<container.numChildren; i++)
				{
					child = container.getChildAt(i);
					if(ignoreHidden && !child.visible)
						continue;
					w = Math.max(w, child.width);
					h = Math.max(h, child.height);
				}
			}
			else if(parent is Array)
			{
				var array:Array = parent as Array;
				for(i=0;i<array.length; i++)
				{
					child = array[i];
					if(ignoreHidden && !child.visible)
						continue;
					
					w = Math.max(w, child.width);
					h = Math.max(h, child.height);
				}
			}
			
			return {w:w, h:h};
		}
		/**
		 * 计算平铺显示的列数
		 */ 
		private static function CalculateTileColumn(width:Number, tileWidth:Number, hGap:Number):int
		{
			if(tileWidth <= 0)
				return 1;
			var column:int = 1;
			var w:Number = tileWidth;
			while(w + hGap + tileWidth <width)
			{
				w+= hGap + tileWidth;
				column++;
			}
			return column;
		}
		/**
		 * 计算平铺显示的行数
		 */ 
		private static function CalculateTileRow(height:Number, tileHeight:Number, vGap:Number):int
		{
			if(tileHeight <= 0)
				return 1;
			var row:int = 1;
			var h:Number = tileHeight;
			while(h + vGap + tileHeight < height)
			{
				h+= vGap + tileHeight;
				row++;
			}
			return row;
		}
		/**
		 * 水平平铺布局子元件
		 * @param parent: 支持DisplayObjectContainer,Array
		 * @param ignoreHidden: 忽略隐藏的子元件
		 * @return {w:Number, h:Number}, w:总宽度, h:总高度
		 */
		public static function HorizontalTileLayoutChildren(parent:*, w:Number, h:Number,
															paddingLeft:Number = 0, paddingRight:Number = 0,
															paddingTop:Number = 0, paddingBottom:Number = 0,
															hGap:Number = 0, vGap:Number = 0, 
															horizontalAlign:String = "left", verticalAlign:String = "top",
															ignoreHidden:Boolean = true):Object
		{
			var child:DisplayObject;
			var i:int;
			
			var tileSize:Object = CalculateTileSize(parent, ignoreHidden);
			var tileWidth:Number = tileSize.w;
			var tileHeight:Number = tileSize.h;			
			var column:int = CalculateTileColumn(w, tileWidth, hGap);
			
			var contentWidth:Number = w - paddingLeft - paddingRight;
			if(contentWidth<tileWidth)
				contentWidth = tileWidth;
			
			if(column>1)
			{
				hGap = (contentWidth-column*tileWidth)/(column-1);
			}
			var contentHeight:Number = paddingTop+paddingBottom;
			
			var top:Number=paddingTop, left:Number=paddingLeft;
			var c:int = 0;
			if(parent is DisplayObjectContainer)
			{
				var container:DisplayObjectContainer = parent as DisplayObjectContainer;
				for(i=0;i<container.numChildren; i++)
				{
					child = container.getChildAt(i);
					if(ignoreHidden && !child.visible)
						continue;
					child.x = left;
					child.y = top;
					
					contentHeight = Math.max(top + tileHeight + paddingBottom, contentHeight);
					c++;
					if(c<column)
					{
						left += tileWidth + hGap;
					}
					else
					{
						left = paddingLeft;
						top += tileHeight + vGap;
						c = 0;
					}
				}
				
			}
			else if(parent is Array)
			{
				var array:Array = parent as Array;
				for(i=0;i<array.length; i++)
				{
					child = array[i];
					if(ignoreHidden && !child.visible)
						continue;
					child.x = left;
					child.y = top;
					
					contentHeight = Math.max(top + tileHeight + paddingBottom, contentHeight);
					c++;
					if(c<column)
					{
						left += tileWidth + hGap;
					}
					else
					{
						left = paddingLeft;
						top += tileHeight + vGap;
					}
				}
			}
			var cw:Number = contentWidth;
			var ch:Number = contentHeight - paddingTop - paddingBottom;
			
			var xIndent:Number = 0;
			if(horizontalAlign == "center")
				xIndent = (w - cw) / 2 - paddingLeft;
			else if(horizontalAlign == "right")
				xIndent = w - cw - paddingLeft - paddingRight;
			var yIndent:Number = 0;
			if(verticalAlign == "middle")
				yIndent = (h - ch) / 2 - paddingTop;
			else if(verticalAlign == "bottom")
				yIndent = (h - ch - paddingTop - paddingBottom);
			
			if(xIndent || yIndent)
			{
				if(parent is DisplayObjectContainer)
				{
					for(i=0;i<container.numChildren; i++)
					{
						child = container.getChildAt(i);
						if(ignoreHidden && !child.visible)
							continue;
						child.x += xIndent;
						child.y += yIndent;
					}
				}
				else if(parent is Array)
				{
					for(i=0;i<array.length; i++)
					{
						child = array[i];
						if(ignoreHidden && !child.visible)
							continue;
						child.x += xIndent;
						child.y += yIndent;
					}
				}
			}
			
			return {w:contentWidth, h:contentHeight};
		}	
		/**
		 * 垂直平铺布局子元件
		 * @param parent: 支持DisplayObjectContainer,Array
		 * @param ignoreHidden: 忽略隐藏的子元件
		 * @return {w:Number, h:Number}, w:总宽度, h:总高度
		 */
		public static function VerticalTileLayoutChildren(parent:*, w:Number, h:Number,
														  paddingLeft:Number = 0, paddingRight:Number = 0,
														  paddingTop:Number = 0, paddingBottom:Number = 0,
														  hGap:Number = 0, vGap:Number = 0, 
														  horizontalAlign:String = "left", verticalAlign:String = "top",
														  ignoreHidden:Boolean = true):Object
		{
			
			var child:DisplayObject;
			var i:int;
			
			var tileSize:Object = CalculateTileSize(parent, ignoreHidden);
			var tileWidth:Number = tileSize.w;
			var tileHeight:Number = tileSize.h;		
			var row:int = CalculateTileRow(h, tileHeight, vGap);
			
			var contentHeight:Number = h - paddingTop - paddingBottom;
			
			if(contentHeight<tileHeight)
				contentHeight = tileHeight;
			
			if(row>1)
			{
				vGap = (contentHeight-row*tileHeight)/(row-1);
			}
			
			
			var contentWidth:Number = paddingLeft+paddingRight;
			
			var top:Number=paddingTop, left:Number=paddingLeft;
			var r:int = 0;
			if(parent is DisplayObjectContainer)
			{
				var container:DisplayObjectContainer = parent as DisplayObjectContainer;
				for(i=0;i<container.numChildren; i++)
				{
					child = container.getChildAt(i);
					if(ignoreHidden && !child.visible)
						continue;
					child.x = left;
					child.y = top;
					
					contentWidth = Math.max(left + tileWidth + paddingRight, contentWidth);
					r++;
					if(r<row)
					{
						top += tileHeight + vGap;
					}
					else
					{
						top = paddingTop;
						left += tileWidth + hGap;
						r = 0;
					}
				}
				
			}
			else if(parent is Array)
			{
				var array:Array = parent as Array;
				for(i=0;i<array.length; i++)
				{
					child = array[i];
					if(ignoreHidden && !child.visible)
						continue;
					child.x = left;
					child.y = top;
					
					contentWidth = Math.max(left + tileWidth + paddingRight, contentWidth);
					r++;
					if(r<row)
					{
						top += tileHeight + vGap;
					}
					else
					{
						top = paddingTop;
						left += tileWidth + hGap;
						r = 0;
					}
				}
			}
			
			var cw:Number = contentWidth - paddingLeft - paddingRight;
			var ch:Number = contentHeight;
			
			var xIndent:Number = 0;
			if(horizontalAlign == "center")
				xIndent = (w - cw) / 2 - paddingLeft;
			else if(horizontalAlign == "right")
				xIndent = w - cw - paddingLeft - paddingRight;
			var yIndent:Number = 0;
			if(verticalAlign == "middle")
				yIndent = (h - ch) / 2 - paddingTop;
			else if(verticalAlign == "bottom")
				yIndent = (h - ch - paddingTop - paddingBottom);
			
			if(xIndent || yIndent)
			{
				if(parent is DisplayObjectContainer)
				{
					for(i=0;i<container.numChildren; i++)
					{
						child = container.getChildAt(i);
						if(ignoreHidden && !child.visible)
							continue;
						child.x += xIndent;
						child.y += yIndent;
					}
				}
				else if(parent is Array)
				{
					for(i=0;i<array.length; i++)
					{
						child = array[i];
						if(ignoreHidden && !child.visible)
							continue;
						child.x += xIndent;
						child.y += yIndent;
					}
				}
			}
			
			return {w:contentWidth, h:contentHeight};
		}	
//		/**
//		 * 垂直平铺布局子元件(从最后元件开始)
//		 * @param parent: 支持DisplayObjectContainer,Array
//		 * @param ignoreHidden: 忽略隐藏的子元件
//		 * @return {w:Number, h:Number}, w:总宽度, h:总高度
//		 */
//		public static function VerticalTileLayoutChildrenFromEnd(parent:*, h:Number,
//																 paddingLeft:Number = 0, paddingRight:Number = 0,
//																 paddingTop:Number = 0, paddingBottom:Number = 0,
//																 hGap:Number = 0, vGap:Number = 0, ignoreHidden:Boolean = true):Object
//		{
//			
//			var child:DisplayObject;
//			var i:int;
//			
//			var tileSize:Object = CalculateTileSize(parent, ignoreHidden);
//			var tileWidth:Number = tileSize.w;
//			var tileHeight:Number = tileSize.h;		
//			var row:int = CalculateTileRow(h, tileHeight, vGap);
//			
//			var contentHeight:Number = h - paddingTop - paddingBottom;
//			
//			if(contentHeight<tileHeight)
//				contentHeight = tileHeight;
//			
//			if(row>1)
//			{
//				vGap = (contentHeight-row*tileHeight)/(row-1);
//			}
//			
//			var contentWidth:Number = paddingLeft+paddingRight;
//			
//			var top:Number=paddingTop, left:Number=paddingLeft;
//			var r:int = 0;
//			if(parent is DisplayObjectContainer)
//			{
//				var container:DisplayObjectContainer = parent as DisplayObjectContainer;
//				for(i=container.numChildren-1;i>=0;i--)
//					//for(i=0;i<container.numChildren; i++)
//				{
//					child = container.getChildAt(i);
//					if(ignoreHidden && !child.visible)
//						continue;
//					child.x = left;
//					child.y = top;
//					
//					contentWidth = Math.max(left + tileWidth + paddingRight, contentWidth);
//					r++;
//					if(r<row)
//					{
//						top += tileHeight + vGap;
//					}
//					else
//					{
//						top = paddingTop;
//						left += tileWidth + hGap;
//						r = 0;
//					}
//				}
//			}
//			else if(parent is Array)
//			{
//				var array:Array = parent as Array;
//				for(i=array.length-1;i>=0;i--)
//					//for(i=0;i<array.length; i++)
//				{
//					child = array[i];
//					if(ignoreHidden && !child.visible)
//						continue;
//					child.x = left;
//					child.y = top;
//					
//					contentWidth = Math.max(left + tileWidth + paddingRight, contentWidth);
//					r++;
//					if(r<row)
//					{
//						top += tileHeight + vGap;
//					}
//					else
//					{
//						top = paddingTop;
//						left += tileWidth + hGap;
//						r = 0;
//					}
//				}
//			}
//			
//			return {w:contentWidth, h:contentHeight};
//		}	
		private static function CountChildren(parent:*, ignoreHidden:Boolean = true):int
		{
			var child:DisplayObject;
			var i:int;
			var numChildren:int = 0;
			if(parent is DisplayObjectContainer)
			{
				var container:DisplayObjectContainer = parent as DisplayObjectContainer;
				for(i=0;i<container.numChildren; i++)
				{
					child = container.getChildAt(i);
					if(ignoreHidden && !child.visible)
						continue;
					numChildren++;
				}
				
			}
			else if(parent is Array)
			{
				var array:Array = parent as Array;
				for(i=0;i<array.length; i++)
				{
					child = array[i];
					if(ignoreHidden && !child.visible)
						continue;
					numChildren++;
				}
			}
			return numChildren;
		}
		/**
		 * 按列数布局子元件
		 * @param parent: 支持DisplayObjectContainer,Array
		 * @param ignoreHidden: 忽略隐藏的子元件
		 */
		public static function ColumnLayoutChildren(parent:*, w:Number, h:Number, column:int, 
													paddingLeft:Number = 0, paddingRight:Number = 0,
													paddingTop:Number = 0, paddingBottom:Number = 0,
													hGap:Number = 0, vGap:Number = 0, 
													horizontalAlign:String = "left", verticalAlign:String = "top",
													ignoreHidden:Boolean = true):void
		{
			var numChildren:int = CountChildren(parent, ignoreHidden);
			if(numChildren == 0)
				return;
			
			var child:DisplayObject;
			var i:int;
			column = Math.max(1, column);
			var row:int = numChildren / column;
			if(numChildren%column>0)
				row++;
			
			var tileSize:Object = CalculateTileSize(parent, ignoreHidden);
			var tileWidth:Number = tileSize.w;
			var tileHeight:Number = tileSize.h;	
			
			var layoutWidth:Number = tileWidth * column + (column-1) * hGap;
			var layoutHeight:Number = tileHeight * row + (row-1) * vGap;
			
			var left:Number=paddingLeft;
			if(horizontalAlign == "center")
				left += (w - paddingLeft - paddingRight - layoutWidth)/2;
			else if(horizontalAlign == "right")
				left += w - paddingLeft - paddingRight - layoutWidth;
			
			var top:Number=paddingTop;
			if(verticalAlign == "middle")
				top += (h - paddingTop - paddingBottom - layoutHeight)/2;
			else if(verticalAlign == "bottom")
				top += h - paddingTop - paddingBottom - layoutHeight;			
			
			var x:Number = left;
			var c:int = 0;
			if(parent is DisplayObjectContainer)
			{
				var container:DisplayObjectContainer = parent as DisplayObjectContainer;
				for(i=0;i<container.numChildren; i++)
				{
					child = container.getChildAt(i);
					if(ignoreHidden && !child.visible)
						continue;
					child.x = left;
					child.y = top;
					
					c++;
					if(c<column)
					{
						left += tileWidth + hGap;
					}
					else
					{
						left = x;
						top += tileHeight + vGap;
						c = 0;
					}
				}
				
			}
			else if(parent is Array)
			{
				var array:Array = parent as Array;
				for(i=0;i<array.length; i++)
				{
					child = array[i];
					if(ignoreHidden && !child.visible)
						continue;
					child.x = left;
					child.y = top;
					
					c++;
					if(c<column)
					{
						left += tileWidth + hGap;
					}
					else
					{
						left = x;
						top += tileHeight + vGap;
					}
				}
			}
		}	
		/**
		 * 按行数布局子元件
		 * @param parent: 支持DisplayObjectContainer,Array
		 * @param ignoreHidden: 忽略隐藏的子元件
		 */
		public static function RowLayoutChildren(parent:*, w:Number, h:Number, row:int, 
												 paddingLeft:Number = 0, paddingRight:Number = 0,
												 paddingTop:Number = 0, paddingBottom:Number = 0,
												 hGap:Number = 0, vGap:Number = 0, 
												 horizontalAlign:String = "left", verticalAlign:String = "top",
												 ignoreHidden:Boolean = true):void
		{
			var numChildren:int = CountChildren(parent, ignoreHidden);
			if(numChildren == 0)
				return;
			
			var child:DisplayObject;
			var i:int;
			row = Math.max(1, row);
			var column:int = numChildren / row;
			if(numChildren % row > 0)
				column++;
			
			var tileSize:Object = CalculateTileSize(parent, ignoreHidden);
			var tileWidth:Number = tileSize.w;
			var tileHeight:Number = tileSize.h;	
			
			var layoutWidth:Number = tileWidth * column + (column-1) * hGap;
			var layoutHeight:Number = tileHeight * row + (row-1) * vGap;
			
			var left:Number=paddingLeft;
			if(horizontalAlign == "center")
				left += (w - paddingLeft - paddingRight - layoutWidth)/2;
			else if(horizontalAlign == "right")
				left += w - paddingLeft - paddingRight - layoutWidth;
			var top:Number=paddingTop;
			if(verticalAlign == "middle")
				top += (h - paddingTop - paddingBottom - layoutHeight)/2;
			else if(verticalAlign == "bottom")
				top += h - paddingTop - paddingBottom - layoutHeight;
			
			var y:Number = top;
			var r:int = 0;
			if(parent is DisplayObjectContainer)
			{
				var container:DisplayObjectContainer = parent as DisplayObjectContainer;
				for(i=0;i<container.numChildren; i++)
				{
					child = container.getChildAt(i);
					if(ignoreHidden && !child.visible)
						continue;
					child.x = left;
					child.y = top;
					
					r++;
					if(r<row)
					{
						top += tileHeight + vGap;
					}
					else
					{
						top = y;
						left += tileWidth + hGap;
						r = 0;
					}
				}
				
			}
			else if(parent is Array)
			{
				var array:Array = parent as Array;
				for(i=0;i<array.length; i++)
				{
					child = array[i];
					if(ignoreHidden && !child.visible)
						continue;
					child.x = left;
					child.y = top;
					
					r++;
					if(r<row)
					{
						top += tileHeight + vGap;
					}
					else
					{
						top = y;
						left += tileWidth + hGap;
						r = 0;
					}
				}
			}
		}	
		/**
		 * 设置子元件大小
		 * @param w: 不为NaN时才设置宽度
		 * @param h: 不为NaN时才设置高度
		 */
		public static function SetChildrenSize(container:DisplayObjectContainer, w:Number = NaN, h:Number = NaN):void
		{
			for(var i:int=0; i<container.numChildren; i++)
			{
				var child:DisplayObject = container.getChildAt(i);
				if(!isNaN(w))
					child.width = w;
				if(!isNaN(h))
					child.height = h;
			}
		}
		/**
		 * 设置子元件位置
		 * @param x: 不为NaN时才设置x
		 * @param y: 不为NaN时才设置y
		 */
		public static function SetChildrenLocation(container:DisplayObjectContainer, x:Number = NaN, y:Number = NaN):void
		{
			for(var i:int=0; i<container.numChildren; i++)
			{
				var child:DisplayObject = container.getChildAt(i);
				if(!isNaN(x))
					child.x = x;
				if(!isNaN(y))
					child.y = y;
			}
		}
		
		/**
		 * 设置子元件属性
		 *  @param parent: 支持DisplayObjectContainer,Array
		 */
		public static function SetChildrenProperty(parent:*, p:String, value:*):void
		{
			var i:int;
			var child:DisplayObject;
			if(parent is DisplayObjectContainer)
			{
				var container:DisplayObjectContainer = parent as DisplayObjectContainer;
				for(i=0;i<container.numChildren; i++)
				{
					child = container.getChildAt(i);
					child[p] = value;
				}
			}
			else if(parent is Array)
			{
				var array:Array = parent as Array;
				for(i=0;i<array.length; i++)
				{
					child = array[i];
					child[p] = value;
				}
			}
		}
		/**
		 * 调用子项方法
		 * @param func: 方法名称，方法没有参数
		 */ 
		public static function CallChildrenFunction(container:DisplayObjectContainer, func:String):void
		{
			for(var i:int=0; i<container.numChildren; i++)
			{
				var child:Object = container.getChildAt(i);
				child[func]();
			}
		}
		/**
		 * 调用子项方法
		 * @param func: 方法名称
		 * @param arg: 方法参数
		 */ 
		public static function CallChildrenFunctionWidthArg(container:DisplayObjectContainer, func:String, arg:*):void
		{
			for(var i:int=0; i<container.numChildren; i++)
			{
				var child:Object = container.getChildAt(i);
				child[func](arg);
			}
		}
		/**
		 * 调用子项方法
		 * @param func: 方法名称
		 * @param arg1: 方法参数1
		 * @param arg2: 方法参数2
		 */ 
		public static function CallChildrenFunctionWidthArg2(container:DisplayObjectContainer, func:String, arg1:*, arg2:*):void
		{
			for(var i:int=0; i<container.numChildren; i++)
			{
				var child:Object = container.getChildAt(i);
				child[func](arg1, arg2);
			}
		}
	}
}