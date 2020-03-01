package smartx.flex.components.vo.workflow
{
	[RemoteClass(alias="smartx.publics.workflow2.vo.Transition")]
	public class Transition
	{
		public var id:Number;
		public var code:String;
		public var wfname:String;
		public var uiname:String;
		public var fromActivity:Activity;
		public var toActivity:Activity;
		public var condition:String;
		public var process:Process;
		public function Transition()
		{
		}

	}
}