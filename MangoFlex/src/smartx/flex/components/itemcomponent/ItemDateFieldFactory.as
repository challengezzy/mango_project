package smartx.flex.components.itemcomponent
{
	import smartx.flex.components.vo.GlobalConst;

	public class ItemDateFieldFactory extends AbstractItemUIComponentFactory
	{
		public override function getComponent(mode:int):ItemUIComponent
		{
			switch(mode){
				case GlobalConst.ITEMCOMPONENTMODE_LIST:
					return new ItemDateField(templetItemVO,false);
				case GlobalConst.ITEMCOMPONENTMODE_CARD:
					return new ItemDateField(templetItemVO,true);
				case GlobalConst.ITEMCOMPONENTMODE_CONDITION:
					return new ItemDateField(templetItemVO,true);
				default:
					return new ItemDateField(templetItemVO,true);
			}
		}
	}
}