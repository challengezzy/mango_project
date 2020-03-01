package smartx.flex.modules.basic.system.menumgmt
{
	import mx.controls.Alert;
	import mx.managers.PopUpManager;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.core.BillTreePanel;
	import smartx.flex.components.event.BasicEvent;
	import smartx.flex.components.styletemplate.ifc.TreeButtonListener;
	import smartx.flex.components.util.FileUploadPanel;
	import smartx.flex.components.util.TextAreaWindow;
	import smartx.flex.components.vo.GlobalConst;
	
	public class MenuImportButtonListener implements TreeButtonListener
	{
		private var destination:String = GlobalConst.SERVICE_FORM;
		private var endpoint:String  = ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT) as String;
		private var formService:RemoteObject;
		private var treePanel:BillTreePanel;
		
		public function MenuImportButtonListener()
		{
			formService = new RemoteObject(destination);
			if(endpoint != null)
				formService.endpoint = endpoint;
		}
		
		public function buttonClick(treePanel:BillTreePanel):void
		{
			this.treePanel = treePanel;
			
			var fup:FileUploadPanel = new FileUploadPanel();
			
			fup.addEventListener(BasicEvent.LOADDATA_SUCCESSFUL,uploadSuccessfulHandler);
			fup.datasource = treePanel.datasource;
			fup.flagName ="menu";
			
			PopUpManager.addPopUp(fup,treePanel,true);
			PopUpManager.centerPopUp(fup);
		}

		private function uploadSuccessfulHandler(event:BasicEvent):void{
			treePanel.load();
		}
	}
}