package smartx.bam.flex.modules.entitymodel.listener
{
	import mx.managers.PopUpManager;
	
	import smartx.flex.components.core.BillListPanel;
	import smartx.flex.components.styletemplate.ifc.ListButtonListener;
	import smartx.flex.components.util.FileUploadPanel;

	public class DQEmImportListButtonListener implements ListButtonListener
	{
		public function DQEmImportListButtonListener(){
			
		}
		
		public function buttonClick(listPanel:BillListPanel):void{
			
			var fup:FileUploadPanel = new FileUploadPanel();
			fup.datasource = listPanel.datasourceName;
			fup.flagName = "entitymodel";
			
			PopUpManager.addPopUp(fup,listPanel,true);
			PopUpManager.centerPopUp(fup);
		}
	}
}