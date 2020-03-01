package smartx.flex.components.itemcomponent
{
	import smartx.flex.components.vo.GlobalConst;
	import smartx.flex.components.vo.TempletItemVO;

	public class ItemTextFieldFactory extends AbstractItemUIComponentFactory
	{

		public override function getComponent(mode:int):ItemUIComponent
		{
			switch(mode){
				case GlobalConst.ITEMCOMPONENTMODE_LIST:
					return new ItemTextField(templetItemVO,false);
				default:
					return new ItemTextField(templetItemVO);
			}
		}
	}
}