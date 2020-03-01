package smartx.flex.components.basic.workflow
{
	import mx.collections.ArrayCollection;
	import mx.core.UIComponent;
	
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.core.workflow.task.TaskHandler;
	import smartx.flex.components.vo.workflow.TaskAssign;

	public class DefaultTaskHandler implements TaskHandler
	{
		public function DefaultTaskHandler()
		{
		}

		public function getHandlerUI(taskAssign:TaskAssign):UIComponent
		{
			var window:DefaultTaskHandlerWindow = new DefaultTaskHandlerWindow();
			window.text = "本工作项无需操作，请直接提交！";
			window.taskAssign = taskAssign;
			window.userId = String(ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_LOGIN_USER_ID));
			window.endpointForWorkflow = String(ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT));
			return window;
		}
		
		public function getDetailUI(taskAssign:TaskAssign):UIComponent{
			var window:TaskAlertWindow = new TaskAlertWindow();
			window.text = "无详细信息！";
			return window;
		}
		
		public function getBatchHandlerUI(taskAssignList:ArrayCollection):UIComponent{
		
			return null;
		}
	}
}