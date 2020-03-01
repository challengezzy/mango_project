package smartx.flex.modules.basic.system.sysdictionarymgmt
{
	import mx.controls.Alert;
	import mx.managers.PopUpManager;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.core.BillListPanel;
	import smartx.flex.components.event.BasicEvent;
	import smartx.flex.components.styletemplate.ifc.ListButtonListener;
	import smartx.flex.components.util.FileUploadPanel;
	import smartx.flex.components.util.TextAreaWindow;
	import smartx.flex.components.vo.GlobalConst;
	
	public class ImportButtonListener implements ListButtonListener
	{
		private var destination:String = GlobalConst.SERVICE_FORM;
		private var formService:RemoteObject;
		private var endpoint:String  = ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT) as String;
		private var listPanel:BillListPanel;
		
		public function ImportButtonListener()
		{
			formService = new RemoteObject(destination);
			if(endpoint != null)
				formService.endpoint = endpoint;
		}
		
		public function buttonClick(listPanel:BillListPanel):void
		{
			this.listPanel = listPanel;
			var fup:FileUploadPanel = new FileUploadPanel();
			
			fup.addEventListener(BasicEvent.LOADDATA_SUCCESSFUL,uploadSuccessfulHandler);
			fup.datasource = listPanel.datasourceName;
			fup.flagName = "sysdictionary";
			
			PopUpManager.addPopUp(fup,listPanel,true);
			PopUpManager.centerPopUp(fup);
		}
		
		private function uploadSuccessfulHandler(event:BasicEvent):void{
			listPanel.query();
		}
	}
}