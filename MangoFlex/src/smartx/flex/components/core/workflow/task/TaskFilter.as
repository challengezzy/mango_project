package smartx.flex.components.core.workflow.task
{
	import smartx.flex.components.vo.workflow.TaskAssign;
	
	public interface TaskFilter
	{
		//判断给定的工作项是否符合条件
		function check(taskAssign:TaskAssign):Boolean;
		//构造函数
		function TaskFilter(properties:Object=null);
	}
}