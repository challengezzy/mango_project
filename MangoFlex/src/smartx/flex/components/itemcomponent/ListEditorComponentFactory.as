package smartx.flex.components.itemcomponent
{
	import mx.core.IFactory;
	
	import smartx.flex.components.vo.GlobalConst;
	import smartx.flex.components.vo.TempletItemVO;
	
	public class ListEditorComponentFactory implements IFactory
	{
		private var templetItemVO:TempletItemVO;
		private var destination:String;
		private var endpoint:String;
		public function ListEditorComponentFactory(templetItemVO:TempletItemVO,destination:String=null,endpoint:String=null)
		{
			super();
			if(templetItemVO == null)
				throw new Error("参数TempletItemVO不能为空");
			this.templetItemVO = templetItemVO;
			this.destination = destination;
			this.endpoint = endpoint;
		}
		
		public function newInstance():*{
			return ItemUIComponentBuilder.getBuilder().getComponent(
				GlobalConst.ITEMCOMPONENTMODE_LIST,
				templetItemVO,
				destination,
				endpoint);
		}
		
	}
}