package smartx.bam.flex.modules.datatask
{
	import mx.managers.PopUpManager;
	
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.basic.DeskTopFrame;
	import smartx.flex.components.core.BillListPanel;
	import smartx.flex.components.styletemplate.ifc.ListButtonListener;
	
	public class ViewExtractTaskLogListener implements ListButtonListener
	{
		private var listPanel:BillListPanel;
		private var desktop:DeskTopFrame = ClientEnviorment.getInstance().getVar(ClientEnviorment.DESKTOP_FRAME) as DeskTopFrame;
		
		public function ViewExtractTaskLogListener()
		{
		}
		
		public function buttonClick(listPanel:BillListPanel):void{
			this.listPanel = listPanel;
			if(listPanel.getSelectedRowValue() == null)
				return;
			var taskCategory:String = "1";//数据采集任务
			var dataTaskTemplateId:String = listPanel.getSelectedRowValue()["ID"];
			
			var viewWindow:DataTaskLogViewWindow = new DataTaskLogViewWindow();
			viewWindow.taskCategory = taskCategory;
			viewWindow.dataTaskTemplateId = dataTaskTemplateId;
			
			PopUpManager.addPopUp(viewWindow,desktop,true);
			PopUpManager.centerPopUp(viewWindow);
		}
		
	}
}