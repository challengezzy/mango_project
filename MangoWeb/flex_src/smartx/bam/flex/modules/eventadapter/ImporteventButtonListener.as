package smartx.bam.flex.modules.eventadapter
{
	import flash.utils.getDefinitionByName;
	
	import mx.controls.Alert;
	import mx.managers.PopUpManager;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	import smartx.bam.flex.vo.BAMConst;
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.core.BillListPanel;
	import smartx.flex.components.styletemplate.ifc.ListButtonListener;
	import smartx.flex.components.util.FileUploadPanel;
	
	/**
	 * sky zhangzz
	 **/
	public class ImporteventButtonListener implements ListButtonListener
	{
		private var remoteObj:RemoteObject;
		private var adaptorClass:String;
		private var billListPanel:BillListPanel;
		
		public function ImporteventButtonListener()
		{
			remoteObj = new RemoteObject(BAMConst.BAM_Service);
			remoteObj.endpoint = String(ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT));

		}
		
		public function buttonClick(blp:BillListPanel):void{
			
			this.billListPanel = blp;
			
			var fup:FileUploadPanel = new FileUploadPanel();
			fup.datasource = billListPanel.datasourceName;
			fup.flagName = "event";
			
			PopUpManager.addPopUp(fup,billListPanel,true);
			PopUpManager.centerPopUp(fup);
			
		}
		
	}
}