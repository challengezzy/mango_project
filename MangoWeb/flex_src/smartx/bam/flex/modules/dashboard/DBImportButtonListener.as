package smartx.bam.flex.modules.dashboard
{
	import mx.managers.PopUpManager;
	
	import smartx.flex.components.core.BillTreePanel;
	import smartx.flex.components.event.BasicEvent;
	import smartx.flex.components.styletemplate.ifc.TreeButtonListener;
	import smartx.flex.components.util.FileUploadPanel;
	
	public class DBImportButtonListener implements TreeButtonListener
	{
		private var treePanel:BillTreePanel;
		public function DBImportButtonListener()
		{
		}
		
		public function buttonClick(treePanel:BillTreePanel):void
		{
			this.treePanel = treePanel;
			var fup:FileUploadPanel = new FileUploadPanel();
			
			fup.addEventListener(BasicEvent.LOADDATA_SUCCESSFUL,uploadSuccessfulHandler);
			fup.datasource = treePanel.datasource;
			fup.flagName = "dashboard";
			
			PopUpManager.addPopUp(fup,treePanel,true);
			PopUpManager.centerPopUp(fup);
			
		}
		
		private function uploadSuccessfulHandler(event:BasicEvent):void{
			treePanel.load();
		}
	}
}