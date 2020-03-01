package smartx.bam.flex.modules.dataanalyze
{
	import mx.managers.PopUpManager;
	
	import smartx.flex.components.core.BillListPanel;
	import smartx.flex.components.event.BasicEvent;
	import smartx.flex.components.styletemplate.ifc.ListButtonListener;
	import smartx.flex.components.util.ZipFileUploadPanel;
	
	public class DataAnalyzeImportListener implements ListButtonListener
	{
		public function DataAnalyzeImportListener()
		{
		}
		
		public function buttonClick(blp:BillListPanel):void
		{
			var fup:ZipFileUploadPanel = new ZipFileUploadPanel();
			fup.flagName = "dataanalyze";
			PopUpManager.addPopUp(fup,blp,true);
			PopUpManager.centerPopUp(fup);
			
			fup.addEventListener(BasicEvent.LOADDATA_SUCCESSFUL,function(event:BasicEvent):void{
				blp.simpleQuery(blp.simpleQueryTextInput.text);
			});
		}
	}
}