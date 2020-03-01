package smartx.flex.components.core.workflow.task
{
	import mx.collections.ArrayCollection;
	import mx.core.UIComponent;
	
	import smartx.flex.components.vo.workflow.TaskAssign;
	
	public interface TaskHandler
	{
		//获取工作项处理界面
		function getHandlerUI(taskAssign:TaskAssign):UIComponent;
		
		//获取工作项详情界面
		function getDetailUI(taskAssign:TaskAssign):UIComponent;
		
		//获取工作项批量处理界面
		function getBatchHandlerUI(taskAssignList:ArrayCollection):UIComponent;
	}
}