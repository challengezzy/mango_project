package smartx.flex.components.vo
{
	import mx.collections.ArrayCollection;

	[RemoteClass(alias="smartx.publics.form.vo.BillListPanelVO")]
	public class BillListPanelVO
	{
		public var tableDataRowCount:int=0;
		public var tableDataPageCount:int=0;
		public var rowCountPerPage:int=40;
		public var currentPage:int=1;
		public var templetVO:TempletVO;
		public var templetItemVOs:Array;
		public var realSQL:String;
		[Bindable]
		public var tableDataValues:ArrayCollection;
		public function BillListPanelVO()
		{
		}
	}
}