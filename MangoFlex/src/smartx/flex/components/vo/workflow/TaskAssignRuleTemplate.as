package smartx.flex.components.vo.workflow
{
	[RemoteClass(alias="smartx.publics.workflow2.vo.TaskAssignRuleTemplate")]
	public class TaskAssignRuleTemplate
	{
		public var id:Number;
		public var name:String;
		public var executorClass:String;
		public var taskAssignObjectRefSql:String;
		
		public function TaskAssignRuleTemplate()
		{
		}

	}
}