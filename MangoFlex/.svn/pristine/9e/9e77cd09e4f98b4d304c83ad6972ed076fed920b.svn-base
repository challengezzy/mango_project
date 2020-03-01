package smartx.flex.components.print
{
	import com.hurlant.eval.ast.PrivateNamespace;
	
	import flash.display.Bitmap;
	import flash.display.BitmapData;
	import flash.display.DisplayObject;
	import flash.display.Loader;
	import flash.display.Sprite;
	import flash.display.Stage;
	import flash.geom.Matrix;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	import flash.printing.PrintJob;
	import flash.printing.PrintJobOptions;
	import flash.printing.PrintJobOrientation;
	
	import mx.collections.ArrayCollection;
	import mx.containers.Panel;
	import mx.controls.Alert;
	import mx.core.Application;
	import mx.core.Container;
	import mx.core.FlexGlobals;
	import mx.core.IFlexDisplayObject;
	import mx.core.IMXMLObject;
	import mx.core.IUIComponent;
	import mx.core.UIComponent;
	import mx.core.UIComponentGlobals;
	import mx.core.mx_internal;
	import mx.managers.ISystemManager;
	import mx.managers.PopUpManager;
	import mx.printing.FlexPrintJobScaleType;
	
	import smartx.flex.components.print.PrintJobUtil;
	import smartx.flex.components.print.PrintPreviewPanel;
	
	use namespace mx_internal;
	
	/**
	 * @author zzy
	 * @description 打印工具类，支持打印预览和直接打印
	 * @date Aug 17, 2011
	 */
	public class SmartXPrintJob
	{
		private var printJob:PrintJob = null;
		
		private var preViewPanel:PrintPreviewPanel;//打印预览窗口
		
		private var printUtil:PrintJobUtil = new PrintJobUtil();
		
		//---------------------------
		//    打印预览窗口弹出的容器对象
		//---------------------------
		private var app:Sprite = null;
		
		public function set container(container:Container):void
		{
			app = container;
		}
		
		/**
		 * 得到打印机设定的打印纸的高度和宽度。
		 * 会弹出一个打印提示框。
		 * 点击打印按钮才能打印纸的高度和宽度，但是不会真的打印。
		 * 
		 * 成功时返回一个Object对象，直接取pageWidth和pageHeight属性就可以。
		 * 失败时返回null
		 * 
		 * 这个方法可以用来在编码期间得到打印纸的信息，不推荐在程序中使用。
		 */
		public function getPageInfo():Object
		{
			var printJ:PrintJob = new PrintJob();
			var ok:Boolean = printJ.start();
			var rs:Object = null
			if (ok)
			{
				//_pageWidth = printJ.pageWidth;
				//_pageHeight = printJ.pageHeight;
				printJ.send();
				rs = new Object();
				//rs.pageWidth = _pageWidth;
				//rs.pageHeight = _pageHeight;
			}
			
			return rs;
		}
		
		//---------------------------
		//    打印对象
		//---------------------------
		private var printObj:ArrayCollection = new ArrayCollection();
		
		public function set printObject(obj:IUIComponent):void
		{
			printObj.removeAll();
			printObj.addItem(obj);
		}
		
		public function addPrintObject(obj:IUIComponent):void
		{
			printObj.addItem(obj);
		}
		public function clearPrintObject():void
		{
			printObj.removeAll();
		}
		
		//---------------------------
		//    打印预览窗口缩放比例
		//---------------------------
		private var _previewPanelRatio:Number = 1.0;
		
		public function set previewPanelRatio(ratio:Number):void
		{
			_previewPanelRatio * ratio;
		}
		
		//打印预览
		public function preview():void
		{
			if (!app)
			{
				var sm:ISystemManager = ISystemManager(FlexGlobals.topLevelApplication.systemManager);
				// no types so no dependencies
				var mp:Object = sm.getImplementation("mx.managers.IMarshallPlanSystemManager");
				if (mp && mp.useSWFBridge())
					app = Sprite(sm.getSandboxRoot());
				else
					app = Sprite(FlexGlobals.topLevelApplication);
			}
			
			preViewPanel = PopUpManager.createPopUp(app,PrintPreviewPanel,true) as PrintPreviewPanel;
			preViewPanel.printObjArr = printObj;
			preViewPanel.previewRatio =_previewPanelRatio;
			
			preViewPanel.analyzePreview();
			PopUpManager.centerPopUp(preViewPanel);			
		}
		
		//打印
		public function print(orientation:String=PrintJobOrientation.PORTRAIT,scaleType:String=FlexPrintJobScaleType.SHOW_ALL):void
		{
			printJob = printUtil.getPrintJob();
			
			if (printJob.start())
			{
				for each (var po:IUIComponent in printObj)
				{
					printUtil.analyseObject(po,scaleType,orientation,true);
				}
				printJob.send();
			}
		}
		
	}
}