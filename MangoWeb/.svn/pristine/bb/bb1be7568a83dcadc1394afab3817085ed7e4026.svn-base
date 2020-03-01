package smartx.bam.flex.modules.businessscenario
{
	import mx.managers.PopUpManager;
	
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.basic.DeskTopFrame;
	import smartx.flex.components.core.BillTreePanel;
	import smartx.flex.components.event.BasicEvent;
	import smartx.flex.components.styletemplate.ifc.TreeButtonListener;
	import smartx.flex.components.util.FileUploadPanel;
	
	public class ScenarioImportButtonListener implements TreeButtonListener
	{
		private var tree:BillTreePanel = null;
		
		private var desktop:DeskTopFrame = ClientEnviorment.getInstance().getVar(ClientEnviorment.DESKTOP_FRAME) as DeskTopFrame;
		
		public function ScenarioImportButtonListener()
		{
		}
		
		public function buttonClick(treePanel:BillTreePanel):void
		{
			tree = treePanel;
			
			var fup:FileUploadPanel = new FileUploadPanel();
			
			fup.addEventListener(BasicEvent.LOADDATA_SUCCESSFUL,uploadSuccessfulHandler);
			fup.datasource = treePanel.datasource;
			fup.flagName = "scenario";
			
			PopUpManager.addPopUp(fup,desktop,true);
			PopUpManager.centerPopUp(fup);
		}
		
		private function uploadSuccessfulHandler(event:BasicEvent):void{
			tree.load();
		}
	}
}