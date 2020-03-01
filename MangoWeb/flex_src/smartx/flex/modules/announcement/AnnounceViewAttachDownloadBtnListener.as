package smartx.flex.modules.announcement
{
	import mx.events.CloseEvent;
	import mx.managers.PopUpManager;
	
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.basic.DeskTopFrame;
	import smartx.flex.components.core.BillCardPanel;
	import smartx.flex.components.styletemplate.ifc.CardButtonListener;
	
	/**
	 * @author zhanjin
	 * @date 2012-2-1
	 **/
	public class AnnounceViewAttachDownloadBtnListener implements CardButtonListener
	{
		/*private var sp:SuperPanel;*/
		private var attachDownloadWindow:AttachDownloadWindow;
		
		private var deskTop:DeskTopFrame = ClientEnviorment.getInstance().getVar(ClientEnviorment.DESKTOP_FRAME) as DeskTopFrame;
		/*private var moduleInfo:IModuleInfo;*/
		public function AnnounceViewAttachDownloadBtnListener()
		{
			
		}
		
		public function buttonClick(cardPanel:BillCardPanel):void
		{
			// TODO Auto Generated method stub
//			Alert.show("dowmload");
			var keyValue:Number;
			var obj:Object = cardPanel.getDataValue();
			keyValue = obj["ID"];
			var attachDownloadWindow:AttachDownloadWindow = new AttachDownloadWindow();
			attachDownloadWindow.initQueryCondition=" keyvalue="+keyValue;
//			var loader:ModuleLoader = new ModuleLoader();
//			loader.url = "smartx/flex/modules/basic/mt/styletemplate/StyleTemplate01Frame.swf?mtcode=MT_PUB_MESSAGE_ATTACH_DOWNLOAD_VIEW";
//			loader.percentHeight = 100;
//			loader.percentWidth = 100;
			
			/*
			sp = new SuperPanel();
			sp.addChild(loader);
			sp.width = 600;
			sp.height = 500;
			sp.allowClose = true;
			sp.allowDrag = true;
			sp.allowResize = true;
			sp.title="公告信息附件下载";
			
			sp.addEventListener(CloseEvent.CLOSE,spCloseHandler);
			var url:String = "smartx/flex/modules/basic/mt/styletemplate/StyleTemplate01Frame.swf?mtcode=MT_PUB_MESSAGE_ATTACH_DOWNLOAD_VIEW";
			moduleInfo = ModuleManager.getModule(url);
			var tempApplicationDomain:ApplicationDomain = null; 
			
			if (tempApplicationDomain == null && sp.moduleFactory &&         
				FlexVersion.compatibilityVersion >= FlexVersion.VERSION_4_0)
			{
				var currentDomain:ApplicationDomain = sp.moduleFactory.info()["currentDomain"];
				tempApplicationDomain = new ApplicationDomain(currentDomain); 
			}

			moduleInfo.load(null,null,null,sp.moduleFactory);
			moduleInfo.addEventListener(ModuleEvent.READY,function(event:ModuleEvent):void{
				var styleTemplet:StyleTemplate01Frame = moduleInfo.factory.create() as StyleTemplate01Frame;
				//styleTemplet.initQueryCondition = "";
				sp.addChild(styleTemplet);
			});
			*/
			
		
			PopUpManager.addPopUp(attachDownloadWindow,deskTop,false);
			PopUpManager.centerPopUp(attachDownloadWindow);
			
		}
		
		private function spCloseHandler(event:CloseEvent):void{
			PopUpManager.removePopUp(attachDownloadWindow);
		}
		
	}
}