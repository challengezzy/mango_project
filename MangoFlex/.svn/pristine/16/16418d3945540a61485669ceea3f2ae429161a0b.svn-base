package smartx.flex.components.core.workflow.task
{
	import smartx.flex.components.vo.workflow.TaskAssign;

	public class DefaultTaskFilter implements TaskFilter
	{
		public var properties:Object;
		
		public static const PROP_ACTIVITYCODE:String = "ActivityCode";
		public static const PROP_PROCESSCODE:String = "ProcessCode";
		
		public function DefaultTaskFilter(properties:Object)
		{
			if(properties == null)
				throw new Error("属性值不能为null");
			if(properties[PROP_ACTIVITYCODE] == null)
				throw new Error("属性["+PROP_ACTIVITYCODE+"]的值不能为null");
			if(properties[PROP_PROCESSCODE] == null)
				throw new Error("属性["+PROP_PROCESSCODE+"]的值不能为null");
			this.properties = properties;
		}

		public function check(taskAssign:TaskAssign):Boolean
		{
			var activityCode:String = taskAssign.activity.code;
			var processCode:String = taskAssign.activity.process.code;
			if(properties[PROP_ACTIVITYCODE] == activityCode
				&& properties[PROP_PROCESSCODE] == processCode)
				return true;
			return false;
		}
		
	}
}