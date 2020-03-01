package smartx.flex.components.vo
{
	import mx.collections.ArrayCollection;
	
	[RemoteClass(alias="smartx.framework.metadata.vo.Pub_Templet_1_ItemGroupVO")]
	public class TempletItemGroupVO
	{
		public var id:String;
		public var name:String;
		public var isShow:Boolean;
		public var isExpand:Boolean;
		public var parentTempletVO:TempletVO;
		public var itemVOs:ArrayCollection;
		public var seq:Number;
	}
}