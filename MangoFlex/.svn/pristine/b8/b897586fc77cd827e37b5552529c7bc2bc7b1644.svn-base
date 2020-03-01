package smartx.flex.components.itemcomponent
{
	import smartx.flex.components.vo.GlobalConst;

	public class ItemNumberFieldFactory extends AbstractItemUIComponentFactory
	{
		public override function getComponent(mode:int):ItemUIComponent
		{
			switch(mode){
				case GlobalConst.ITEMCOMPONENTMODE_LIST:
					//return null;//采用默认的list编辑控件
					return new ItemNumberField(templetItemVO,false);
				default:
					return new ItemNumberField(templetItemVO);
			}
		}
	}
}