package smartx.bam.flex.modules.task.listener
{
	import mx.managers.PopUpManager;
	import mx.rpc.remoting.mxml.RemoteObject;
	
	import smartx.bam.flex.modules.task.TaskTypeManagerWindow;
	import smartx.flex.components.core.BillListPanel;
	import smartx.flex.components.styletemplate.ifc.ListButtonListener;

	/**
	 * @author zzy
	 * @date Aug 10, 2011
	 */
	public class TaskTypeManagerListener implements ListButtonListener
	{
		protected  var remoteObj:RemoteObject;
		
		private var typeManagerWindow:TaskTypeManagerWindow;
		
		private var billListPanel:BillListPanel;
				
		public function TaskTypeManagerListener()
		{
		}
		
		public function buttonClick(listPanel:BillListPanel):void{
			billListPanel = listPanel;
			typeManagerWindow = new TaskTypeManagerWindow();
			
			typeManagerWindow.confirmFunc = confirmClicked;
			
			PopUpManager.addPopUp(typeManagerWindow,listPanel,true);
			PopUpManager.centerPopUp(typeManagerWindow);
			
		}
		
		private function confirmClicked():void{
			billListPanel.query();
		}
		
	}
}