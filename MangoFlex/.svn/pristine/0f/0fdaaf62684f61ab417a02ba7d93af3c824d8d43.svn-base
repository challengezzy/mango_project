package smartx.flex.components.print
{
	import com.hurlant.eval.ast.Catch;
	
	import flash.display.Bitmap;
	import flash.display.BitmapData;
	import flash.display.DisplayObject;
	import flash.display.Sprite;
	import flash.display.Stage;
	import flash.geom.Matrix;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	import flash.printing.PrintJob;
	import flash.printing.PrintJobOptions;
	import flash.printing.PrintJobOrientation;
	
	import mx.collections.ArrayCollection;
	import mx.core.Application;
	import mx.core.IFlexDisplayObject;
	import mx.core.IUIComponent;
	import mx.core.UIComponent;
	import mx.core.UIComponentGlobals;
	import mx.core.mx_internal;
	import mx.graphics.ImageSnapshot;
	import mx.printing.FlexPrintJobScaleType;

	use namespace mx_internal;
	
	/**
	 * @author zzy
	 * @date Aug 18, 2011
	 */
	public class PrintJobUtil
	{
		private var printJob:PrintJob = null;
		
		public function getPrintJob():PrintJob{
			if(printJob == null)
				printJob = new PrintJob();
			
			return printJob;
		}
		
		public var imgList:ArrayCollection = new ArrayCollection();
		
		//-------------------------------
		//        纸张默认为A4纸竖版
		//-------------------------------
		
		//----------------------
		//        纸张高度
		//----------------------
		private var _pageHeight:Number = 806;
		public function get pageHeight():Number
		{
			return _pageHeight;
		}
		
		public function set pageHeight(ph:Number):void
		{
			this._pageHeight = ph;
		}
		
		//----------------------
		//        纸张宽度
		//----------------------
		private var _pageWidth:Number = 560;
		
		public function get pageWidth():Number
		{
			return _pageWidth;
		}
		
		public function set pageWidth(pw:Number):void
		{
			this._pageWidth = pw;
		}
		
		//----------------------
		//    printAsBitmap
		//----------------------
		private var _printAsBitmap:Boolean = true;
		
		public function get printAsBitmap():Boolean
		{
			return _printAsBitmap;
		}
		
		public function set printAsBitmap(value:Boolean):void
		{
			_printAsBitmap = value;
		}
		
		/**
		 * 分析打印对象
		 * */
		public function analyseObject(obj:IUIComponent,scaleType:String = "showAll",orientation:String="portrait",printFlag:Boolean = false):Number
		{
			var objWidth:Number;
			var objHeight:Number;
			
			var objPercWidth:Number;
			var objPercHeight:Number;
			
			var n:int;
			var i:int;
			var j:int;
			
			var child:IFlexDisplayObject;
			var childPercentSizes:Object = {};
			
			var appExplicitWidth:Number;
			var appExplicitHeight:Number;
			
			//判断横向与纵向打印
			if(orientation == PrintJobOrientation.LANDSCAPE){
				swapHeightWidth();
			}
			
			if (obj is Application)
			{
				n = Application(obj).numChildren
				for (i = 0; i < n; i++)
				{
					child = IFlexDisplayObject(Application(obj).getChildAt(i));
					
					if (child is UIComponent &&
						(!isNaN(UIComponent(child).percentWidth) ||
							!isNaN(UIComponent(child).percentHeight)))
					{
						childPercentSizes[child.name] = {};
						
						if (!isNaN(UIComponent(child).percentWidth) &&
							isNaN(UIComponent(child).explicitWidth))
						{
							childPercentSizes[child.name].percentWidth =
								UIComponent(child).percentWidth;
							UIComponent(child).percentWidth = NaN;
							UIComponent(child).explicitWidth =
								UIComponent(child).width;
						}
						
						if (!isNaN(UIComponent(child).percentHeight) &&
							isNaN(UIComponent(child).explicitHeight))
						{
							childPercentSizes[child.name].percentHeight =
								UIComponent(child).percentHeight;
							UIComponent(child).percentHeight = NaN;
							UIComponent(child).explicitHeight =
								UIComponent(child).height;
						}
					}
				}
				
				if (!isNaN(UIComponent(obj).explicitWidth) 
					&& !isNaN(UIComponent(obj).explicitHeight))
				{
					appExplicitWidth = UIComponent(obj).explicitWidth;
					appExplicitHeight = UIComponent(obj).explicitHeight;
					
					UIComponent(obj).explicitWidth = NaN;
					UIComponent(obj).explicitHeight = NaN;
					
					UIComponent(obj).measuredWidth = appExplicitWidth;
					UIComponent(obj).measuredHeight = appExplicitHeight;
				}
				
				if (isNaN(obj.percentWidth) && isNaN(obj.percentHeight))
					UIComponent(obj).invalidateSizeFlag = false;
				
				UIComponent(obj).validateSize();
				
				objWidth = obj.measuredWidth;
				objHeight = obj.measuredHeight;
			}
			else
			{
				// Lock if the content is percent width or height.
				if (!isNaN(obj.percentWidth) && isNaN(obj.explicitWidth))
				{
					objPercWidth = obj.percentWidth;
					obj.percentWidth = NaN;
					obj.explicitWidth = obj.width;
				}
				
				if (!isNaN(obj.percentHeight) && isNaN(obj.explicitHeight))
				{
					objPercHeight = obj.percentHeight;
					obj.percentHeight = NaN;
					obj.explicitHeight = obj.height;
				}
				
				objWidth = obj.getExplicitOrMeasuredWidth();
				objHeight = obj.getExplicitOrMeasuredHeight();
			}
			
			var widthRatio:Number = _pageWidth/objWidth;
			var heightRatio:Number = _pageHeight/objHeight;
			
			var ratio:Number = 1;//缩放比例
			
			if (scaleType == FlexPrintJobScaleType.SHOW_ALL)
			{
				// Smaller of the two ratios for showAll.
				ratio = (widthRatio < heightRatio) ? widthRatio : heightRatio;
			}
			else if (scaleType == FlexPrintJobScaleType.FILL_PAGE)
			{
				// Bigger of the two ratios for fillPage.
				ratio = (widthRatio > heightRatio) ? widthRatio : heightRatio;
			}
			else if (scaleType == FlexPrintJobScaleType.NONE)
			{
			}
			else if (scaleType == FlexPrintJobScaleType.MATCH_HEIGHT)
			{
				ratio = heightRatio;
			}
			else
			{
				ratio = widthRatio;
			}
			
			//预览用快照
			var imgJob:BitmapData = this.getBitmapData(UIComponent(obj),ratio,ratio);
			// Scale it to the required value.
			obj.scaleX *= ratio;
			obj.scaleY *= ratio;
			
			UIComponentGlobals.layoutManager.usePhasedInstantiation = false;
			UIComponentGlobals.layoutManager.validateNow();
			
			var arrPrintData:Array = prepareToPrintObject(obj);
			
			objWidth *= ratio;
			objHeight *= ratio;
			
			// Find the number of pages required in vertical and horizontal.
			var hPages:int = Math.ceil(objWidth / _pageWidth);
			var vPages:int = Math.ceil(objHeight / _pageHeight);
			
			// when sent to addPage, scaling is to be ignored.
			var incrX:Number = _pageWidth / ratio;
			var incrY:Number = _pageHeight / ratio;
			
			var lastPageWidth:Number = (objWidth % _pageWidth) / ratio;
			var lastPageHeight:Number = (objHeight % _pageHeight) / ratio;
			
			for (j = 0; j < vPages; j++)
			{
				for (i = 0; i < hPages; i++)
				{
					var r:Rectangle = new Rectangle(i * incrX, j * incrY, incrX, incrY);
					
					// For last pages send only the remaining amount
					// so that rest of the paper is printed white
					// else it prints that in gray.
					if (i == hPages - 1 && lastPageWidth != 0)
						r.width = lastPageWidth;
					
					if (j == vPages - 1 && lastPageHeight != 0)
						r.height = lastPageHeight;
					
					// The final edge may have got fractioned as
					// contents may not be complete multiple of pageWidth/Height.
					// This may result in a blank area at the end of page.
					// Tthis rounding off ensures no small blank area in the end 
					// but results in some part of next page getting reprinted
					// this page but it does not result in loss of any information.
					r.width = Math.ceil(r.width);
					r.height = Math.ceil(r.height);
					
					if (printFlag)
					{	
						var printJobOptions:PrintJobOptions = new PrintJobOptions();
						printJobOptions.printAsBitmap = _printAsBitmap;
						printJob.addPage(Sprite(obj), r, printJobOptions);
					}
					else
					{
						try{
							var bmp:BitmapData = new BitmapData(incrX,incrY);
							bmp.copyPixels(imgJob,r,new Point());
							imgList.addItem(new Bitmap(bmp));
						}catch(e:Error){
							trace("位图数据转换错误！");
						}
					}
				}
			}
			
			finishPrintObject(obj, arrPrintData);
			
			//判断横向与纵向打印
			if(orientation == PrintJobOrientation.LANDSCAPE){
				swapHeightWidth();
			}
			
			// Scale it back.
			obj.scaleX /= ratio;
			obj.scaleY /= ratio;
			
			if (obj is Application)
			{
				if (!isNaN(appExplicitWidth)) //&& !isNaN(appExplicitHeight))
				{
					UIComponent(obj).setActualSize(appExplicitWidth,appExplicitHeight);
					//UIComponent(obj).explicitWidth = appExplicitWidth;
					//UIComponent(obj).explicitHeight = appExplicitHeight;
					
					appExplicitWidth = NaN;
					appExplicitHeight = NaN;
					
					UIComponent(obj).measuredWidth = 0;
					UIComponent(obj).measuredHeight = 0;
				}
				
				// The following loop is required only for scenario
				// where application may have a few children
				// with percent width or height.
				n = Application(obj).numChildren
				for (i = 0; i < n; i++)
				{
					child = IFlexDisplayObject(Application(obj).getChildAt(i));
					if (child is UIComponent && childPercentSizes[child.name])
					{
						var childPercentSize:Object = childPercentSizes[child.name];
						if (childPercentSize &&
							!isNaN(childPercentSize.percentWidth))
						{
							UIComponent(child).percentWidth =
								childPercentSize.percentWidth;
							UIComponent(child).explicitWidth = NaN;
						}
						
						if (childPercentSize &&
							!isNaN(childPercentSize.percentHeight))
						{
							UIComponent(child).percentHeight =
								childPercentSize.percentHeight;
							UIComponent(child).explicitHeight = NaN;
						}
					}
				}
				UIComponent(obj).invalidateSizeFlag = false;
				UIComponent(obj).validateSize();
			}
			else
			{
				// Unlock if the content was percent width or height.
				if (!isNaN(objPercWidth))
				{
					obj.percentWidth = objPercWidth;
					obj.explicitWidth = NaN;
				}
				
				if (!isNaN(objPercHeight))
				{
					obj.percentHeight = objPercHeight;
					obj.explicitHeight = NaN;
				}
			}
			
			UIComponentGlobals.layoutManager.usePhasedInstantiation = false;
			UIComponentGlobals.layoutManager.validateNow();	
			
			return ratio;
		}
		
		public function getBitmapData(target:UIComponent,scaleX:Number, scaleY:Number):BitmapData
		{
//			var bd : BitmapData = new BitmapData(target.width, target.height);
//			var m : Matrix = new Matrix();
//			//m.scale(scaleX,scaleY);//对图像进行缩放处理
//			bd.draw(target, m);
//			return bd;
			
			var bd:BitmapData = ImageSnapshot.captureBitmapData(target);
			
			return bd;
			
		}
		
		/**
		 *  @private
		 *  Prepare the target and its parents to print.
		 *  If the content is inside a Container with scrollBars,
		 *  it still gets printed all right. 
		 */
		public function prepareToPrintObject(target:IUIComponent):Array
		{
			var arrPrintData:Array = [];
			
			var obj:DisplayObject = (target is DisplayObject) ? DisplayObject(target) : null;
			var index:Number = 0;
			
			while (obj)
			{
				if (obj is UIComponent)
					arrPrintData[index++] = UIComponent(obj).prepareToPrint(UIComponent(target));
				else if (obj is DisplayObject && !(obj is Stage))
				{
					arrPrintData[index++] = DisplayObject(obj).mask;
					DisplayObject(obj).mask = null;
				}
				
				obj = (obj.parent is DisplayObject) ? DisplayObject(obj.parent) :null;
			}
			
			return arrPrintData;
		}
		
		/**
		 *  @private
		 *  Reverts the target and its parents back from Print state, 
		 */
		public function finishPrintObject(target:IUIComponent,arrPrintData:Array):void
		{
			var obj:DisplayObject 
			= (target is DisplayObject) ? DisplayObject(target) : null;
			var index:Number = 0;
			while (obj)
			{
				if (obj is UIComponent)
					UIComponent(obj).finishPrint(arrPrintData[index++],
						UIComponent(target));
				else if (obj is DisplayObject && !(obj is Stage))
				{
					DisplayObject(obj).mask = arrPrintData[index++];
				}
				
				obj = (obj.parent is DisplayObject) ?
					DisplayObject(obj.parent) :
					null;
			}
		}
		
		//高度与宽度互换
		private function swapHeightWidth():void{
			var tempNum:Number = _pageHeight;
			_pageHeight = _pageWidth;
			_pageWidth = tempNum;
		}
	}
}