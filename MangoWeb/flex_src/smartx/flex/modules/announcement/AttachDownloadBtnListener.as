package smartx.flex.modules.announcement
{
	import flash.net.URLRequest;
	import flash.net.navigateToURL;
	
	import mx.controls.Alert;
	import mx.events.CloseEvent;
	import mx.managers.PopUpManager;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.mxml.RemoteObject;
	
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.core.BillListPanel;
	import smartx.flex.components.styletemplate.ifc.ListButtonListener;
	import smartx.flex.components.util.OperatingTipUtil;
	import smartx.flex.components.util.SmartXMessage;
	import smartx.flex.components.vo.GlobalConst;
	
	/**
	 * @author zzy
	 * @date Aug 10, 2011
	 */
	public class AttachDownloadBtnListener implements ListButtonListener
	{
		private var destination:String = GlobalConst.SERVICE_FORM;
		private var formService:RemoteObject;
		private var endpoint:String  = ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT) as String;
		private var listPanel:BillListPanel;
		private var selectObj:Object;
		
		public function AttachDownloadBtnListener()
		{
			formService = new RemoteObject(destination);
			if(endpoint != null)
				formService.endpoint = endpoint;
			
			formService.attachFileDownload.addEventListener(ResultEvent.RESULT,attachFileDownloadHandler);
			formService.attachFileDownload.addEventListener(FaultEvent.FAULT,faultHandler);
		}
		
		public function buttonClick(listPanel:BillListPanel):void{
			this.listPanel = listPanel;
			if(listPanel.getSelectedRowValues().length != 1){
				SmartXMessage.show("请选择一个附件记录下载！");
				return;
			}
			selectObj = listPanel.getSelectedRowValue();
			
			SmartXMessage.show("确认要下载该附件？",SmartXMessage.MESSAGE_CONFIRM,null,listPanel,confirmHandler);
		}
		
		private function faultHandler(event:FaultEvent):void{
			OperatingTipUtil.endOperat();
			SmartXMessage.show("文件下载失败",SmartXMessage.MESSAGE_ERROR,event.fault.faultString);
		}
		
		private function attachFileDownloadHandler(event:ResultEvent):void{
			var url:String = event.result as String;
			navigateToURL(new URLRequest(encodeURI(url)), "_blank");
			OperatingTipUtil.endOperat();
			listPanel.query();
		}
		
		private function confirmHandler(event:CloseEvent):void{
			if(event.detail == Alert.YES){
				OperatingTipUtil.startOperat("正在下载附件......",listPanel);
				
				formService.attachFileDownload(selectObj["ID"],selectObj["FILENAME"]);
			}
		}
		
	}
}