package smartx.flex.components.core.workflow.task
{
	import smartx.flex.components.vo.workflow.TaskAssign;

	public class AllTaskFilter implements TaskFilter
	{
		public function AllTaskFilter(properties:Object)
		{
		}

		public function check(taskAssign:TaskAssign):Boolean
		{
			return true;
		}
		
	}
}