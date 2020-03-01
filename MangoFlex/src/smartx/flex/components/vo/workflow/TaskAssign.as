package smartx.flex.components.vo.workflow
{
	[RemoteClass(alias="smartx.publics.workflow2.vo.TaskAssign")]
	public class TaskAssign
	{
		public var id:Number;
		public var taskAssignRule:TaskAssignRule;
		public var activity:Activity;
		public var userId:Number;
		public var assignTime:Date;	
		public var processInstanceId:Number;
		public var processInstance:ProcessInstance;	
		public function TaskAssign()
		{
		}

	}
}