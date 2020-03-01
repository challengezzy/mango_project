package smartx.bam.flex.modules.queryview
{
	import mx.managers.PopUpManager;
	
	import smartx.flex.components.core.BillTreePanel;
	import smartx.flex.components.event.BasicEvent;
	import smartx.flex.components.styletemplate.ifc.TreeButtonListener;
	import smartx.flex.components.util.FileUploadPanel;
	
	public class QueryViewImportButtonListener implements TreeButtonListener
	{
		private var tree:BillTreePanel = null;
		
		public function QueryViewImportButtonListener()
		{
		}
		
		public function buttonClick(treePanel:BillTreePanel):void
		{
			
			tree = treePanel;
			
			var fup:FileUploadPanel = new FileUploadPanel();
			
			fup.addEventListener(BasicEvent.LOADDATA_SUCCESSFUL,uploadSuccessfulHandler);
			fup.datasource = treePanel.datasource;
			fup.flagName = "queryview";
			
			PopUpManager.addPopUp(fup,treePanel,true);
			PopUpManager.centerPopUp(fup);
			
		}
		
		private function uploadSuccessfulHandler(event:BasicEvent):void{
			tree.load();
		}
		
	}
}