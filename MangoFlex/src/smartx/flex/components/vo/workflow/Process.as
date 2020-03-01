package smartx.flex.components.vo.workflow
{
	import mx.collections.ArrayCollection;
	
	[RemoteClass(alias="smartx.publics.workflow2.vo.Process")]
	public class Process
	{
		public var id:Number;
		public var code:String;
		public var name:String;
		public var startActivity:Activity;
		public var endActivity:Activity;
		public var activityCollection:ArrayCollection;
		public var transitionCollection:ArrayCollection;
		public function Process()
		{
		}

	}
}