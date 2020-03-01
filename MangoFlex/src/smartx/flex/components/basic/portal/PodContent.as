package smartx.flex.components.basic.portal
{
	import smartx.flex.components.basic.DeskTopFrame;
	
	import mx.containers.VBox;
	
	public class PodContent extends VBox
	{
		[Bindable]
		public var userId:String;
		[Bindable]
		public var deskTopFrame:DeskTopFrame;
		
		
	}
}