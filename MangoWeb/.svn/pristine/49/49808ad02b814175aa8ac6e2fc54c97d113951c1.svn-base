package smartx.flex.modules.announcement
{
	import mx.controls.Alert;
	import mx.managers.PopUpManager;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.mxml.RemoteObject;
	
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.core.BillCardPanel;
	import smartx.flex.components.event.BasicEvent;
	import smartx.flex.components.event.BillCardPanelEvent;
	import smartx.flex.components.styletemplate.ifc.CardButtonListener;
	import smartx.flex.components.util.SmartXMessage;
	import smartx.flex.components.util.StringUtils;
	import smartx.flex.components.util.TextAreaWindow;
	import smartx.flex.components.vo.GlobalConst;
	import smartx.flex.modules.announcement.AttachUploadWindow;
	
	/**
	 * @author zzy
	 * @date Jun 14, 2011
	 * 添加公告的附件信息
	 */
	public class AnnounceAttachAddBtnListener implements CardButtonListener
	{
		
		private var cardData:Object;
		private var billCardPanel:BillCardPanel;
		private var taskId:String;
	
		public function buttonClick(cardPanel:BillCardPanel):void{
			cardData = cardPanel.getDataValue();
			billCardPanel = cardPanel;
			var uploadWindow:AttachUploadWindow = new AttachUploadWindow();
			
			uploadWindow.addEventListener(BasicEvent.LOADDATA_SUCCESSFUL,uploadSuccessfulHandler);
			uploadWindow.tableName = "PUB_MESSAGES";
			uploadWindow.keyColumn = "公告附件";
			uploadWindow.keyValue = cardData["ID"];
			
			PopUpManager.addPopUp(uploadWindow,cardPanel,true);
			PopUpManager.centerPopUp(uploadWindow);
		}
		
		private function uploadSuccessfulHandler(event:BasicEvent):void{
			
			SmartXMessage.show("附件上传成功,附件列表该刷新啦！！");
			billCardPanel.dispatchEvent(new BillCardPanelEvent(BillCardPanelEvent.REFRESHCHILDLIST));
		}

		
	}
}