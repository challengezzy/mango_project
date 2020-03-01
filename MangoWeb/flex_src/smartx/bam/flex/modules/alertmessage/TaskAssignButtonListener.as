package smartx.bam.flex.modules.alertmessage
{
	import mx.controls.Alert;
	import mx.managers.PopUpManager;
	
	import smartx.bam.flex.modules.task.TaskCreatorWindow;
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.core.BillListPanel;
	import smartx.flex.components.styletemplate.ifc.ListButtonListener;
	
	public class TaskAssignButtonListener implements ListButtonListener
	{
		
		private var endpoint:String = String(ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT));
		
		public function TaskAssignButtonListener()
		{
		}
		
		public function buttonClick(listPanel:BillListPanel):void
		{
			if(listPanel.getSelectedRowValue() != null){
				var row:Object = listPanel.getSelectedRowValue();
				var taskWindow:TaskCreatorWindow = new TaskCreatorWindow();
				taskWindow.messageObj = row;
				taskWindow.endpoint = endpoint;
				
				PopUpManager.addPopUp(taskWindow,listPanel,true);
				PopUpManager.centerPopUp(taskWindow);
			}
			else
				Alert.show("请选择一条记录");
		}
	}
}