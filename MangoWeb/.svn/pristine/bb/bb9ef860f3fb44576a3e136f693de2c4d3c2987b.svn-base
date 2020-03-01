package smartx.bam.flex.modules.businessview.listener
{
	import mx.controls.Alert;
	import mx.events.CloseEvent;
	import mx.managers.PopUpManager;
	
	import smartx.bam.flex.modules.businessview.config.BasicBusinessViewConfig;
	import smartx.bam.flex.modules.businessview.config.BusinessViewConfig;
	import smartx.bam.flex.modules.businessview.config.DataStreamBusinessViewConfig;
	import smartx.bam.flex.modules.businessview.config.NormalBusinessViewConfig;
	import smartx.bam.flex.vo.BusinessViewType;
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.basic.DeskTopFrame;
	import smartx.flex.components.core.BillCardPanel;
	import smartx.flex.components.styletemplate.ifc.CardButtonListener;
	import smartx.flex.components.util.TextAreaWindow;
	
	/**
	 * sky zhangzz
	 **/
	public class DesignCardButtonListener implements CardButtonListener
	{
		private var businessViewConfig:BusinessViewConfig;
		
		private var dataValue:Object;
		
		private var bcp:BillCardPanel;
		
		private var textareaWindow:TextAreaWindow;
		
		private var desktop:DeskTopFrame = ClientEnviorment.getInstance().getVar(ClientEnviorment.DESKTOP_FRAME) as DeskTopFrame;
		
		public function DesignCardButtonListener()
		{
			//TODO: implement function
		}
		
		public function buttonClick(cardPanel:BillCardPanel):void
		{
			bcp = cardPanel;
			dataValue = cardPanel.getDataValue();
			if(dataValue["TYPE"] == null){
				Alert.show("请选择业务视图类型");
				return;
			}
			var type:String = dataValue["TYPE"]["id"];
			switch(type){
				case BusinessViewType.NORMAL:
					businessViewConfig = new NormalBusinessViewConfig();
					break;
				case BusinessViewType.STRUCT:
					businessViewConfig = new BasicBusinessViewConfig();
					break;
				case BusinessViewType.DATASTREAM:
					businessViewConfig = new DataStreamBusinessViewConfig();
					break;
				case BusinessViewType.ADVANCED:
					textareaWindow = new TextAreaWindow();
					textareaWindow.text = dataValue["SOURCE"];
					textareaWindow.title = "高级视图";
					textareaWindow.showCloseButton = true;
					textareaWindow.addEventListener(CloseEvent.CLOSE,function(event:CloseEvent):void{
						PopUpManager.removePopUp(textareaWindow);
					});
					textareaWindow.confirmFunc = textAreaWindowConfirmFun;
					PopUpManager.addPopUp(textareaWindow,desktop,true);
					PopUpManager.centerPopUp(textareaWindow);
					break;
				default:
					Alert.show("业务视图类型错误!");
					break;
			}
			if(businessViewConfig && type != BusinessViewType.ADVANCED){
				businessViewConfig.bvCode = dataValue["CODE"];
				businessViewConfig.windowName = dataValue["STREAMWINDOWNAME"];
				businessViewConfig.eventName = dataValue["STREAMNAME"];
				if(dataValue["METADATA"] != null)
					businessViewConfig.contentXml = new XML(dataValue["METADATA"]);
				businessViewConfig.confirmFun = confirmFun;
				PopUpManager.addPopUp(businessViewConfig,desktop,true);
				PopUpManager.centerPopUp(businessViewConfig);
			}
		}
		
		private function textAreaWindowConfirmFun():void{
			dataValue["SOURCE"] = textareaWindow.text;
			bcp.setDataValue(dataValue);
			PopUpManager.removePopUp(textareaWindow);
		}
		
		private function confirmFun():void{
			if(businessViewConfig.checkOut()){
				dataValue["SOURCE"] = businessViewConfig.getEpl();
				dataValue["METADATA"] = businessViewConfig.getContentXml();
				bcp.setDataValue(dataValue);
				PopUpManager.removePopUp(businessViewConfig);
			}
		}
	}
}