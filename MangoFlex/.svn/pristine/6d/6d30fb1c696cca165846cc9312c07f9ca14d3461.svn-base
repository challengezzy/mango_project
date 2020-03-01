package smartx.flex.components.util
{
	import flash.display.Sprite;
	
	import mx.core.FlexGlobals;
	import mx.core.IFlexDisplayObject;
	import mx.core.IFlexModule;
	import mx.core.IFlexModuleFactory;
	import mx.core.UIComponent;
	import mx.managers.ISystemManager;
	import mx.managers.PopUpManager;

	/**
	 * @author zzy
	 * @date Aug 15, 2011
	 */
	public class OperatingTipUtil
	{
		private static var loadingWindow:LoadingWindow = new LoadingWindow();
		/** 结束提示符 */
		private static var endTipMessage:String;
		
		/**
		 * 开始执行操作提示
		 * */
		public static function startOperat(text:String,parent:Sprite=null,endTipMsg:String=""):void{
			loadingWindow.text = text;
			endTipMessage = endTipMsg;
			if (!parent)
			{
				var sm:ISystemManager = ISystemManager(FlexGlobals.topLevelApplication.systemManager);
				// no types so no dependencies
				var mp:Object = sm.getImplementation("mx.managers.IMarshallPlanSystemManager");
				if (mp && mp.useSWFBridge())
					parent = Sprite(sm.getSandboxRoot());
				else
					parent = Sprite(FlexGlobals.topLevelApplication);
			}
			
			// Setting a module factory allows the correct embedded font to be found.
			if (parent is IFlexModule)
				loadingWindow.moduleFactory = IFlexModule(parent).moduleFactory;
			else
			{
				if (parent is IFlexModuleFactory)
					loadingWindow.moduleFactory = IFlexModuleFactory(parent);
				else                
					loadingWindow.moduleFactory = FlexGlobals.topLevelApplication.moduleFactory;
				
				// also set document if parent isn't a UIComponent
				if (!parent is UIComponent)
					loadingWindow.document = FlexGlobals.topLevelApplication.document;
			}
			
			if(!loadingWindow.isPopUp){
				PopUpManager.addPopUp(loadingWindow,parent,true);
				PopUpManager.centerPopUp(loadingWindow);
			}
		}
		
		/**
		 * 结束提示
		 * */
		public static function endOperat(showEndTipMsg:Boolean=false):void{
			if(loadingWindow.isPopUp)
				PopUpManager.removePopUp(loadingWindow);
			if(showEndTipMsg && endTipMessage != null && endTipMessage != "")
				SmartXMessage.show(endTipMessage);
		}
	}
}