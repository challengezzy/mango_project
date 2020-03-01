package smartx.bam.flex.modules.entitymodel.listener
{
	import mx.managers.PopUpManager;
	import mx.rpc.remoting.RemoteObject;
	
	import smartx.bam.flex.vo.BAMConst;
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.core.BillListPanel;
	import smartx.flex.components.event.BasicEvent;
	import smartx.flex.components.styletemplate.ifc.ListButtonListener;
	import smartx.flex.components.util.ZipFileUploadPanel;
	
	public class DQEmRuleImportButton implements ListButtonListener
	{
		private var adaptorClass:String;
		private var billListPanel:BillListPanel;
		
		public function DQEmRuleImportButton()
		{
			
		}
		
		public function buttonClick(blp:BillListPanel):void{
			
			this.billListPanel = blp;
			
			var fup:ZipFileUploadPanel = new ZipFileUploadPanel();
			fup.flagName = "rules";
			PopUpManager.addPopUp(fup,billListPanel,true);
			PopUpManager.centerPopUp(fup);
			
			fup.addEventListener(BasicEvent.LOADDATA_SUCCESSFUL,function(event:BasicEvent):void{
				billListPanel.simpleQuery(billListPanel.simpleQueryTextInput.text);
			});
		}
	}
}