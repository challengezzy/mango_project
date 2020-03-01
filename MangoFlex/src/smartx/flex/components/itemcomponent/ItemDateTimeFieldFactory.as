package smartx.flex.components.itemcomponent
{
	import smartx.flex.components.vo.GlobalConst;

	public class ItemDateTimeFieldFactory extends AbstractItemUIComponentFactory
	{
		public override function getComponent(mode:int):ItemUIComponent
		{
			switch(mode){
				case GlobalConst.ITEMCOMPONENTMODE_LIST:
					return new ItemDateTimeField(templetItemVO,false);
				case GlobalConst.ITEMCOMPONENTMODE_CARD:
					return new ItemDateTimeField(templetItemVO,true);
				case GlobalConst.ITEMCOMPONENTMODE_CONDITION:
					return new ItemDateTimeField(templetItemVO,true);
				default:
					return new ItemDateTimeField(templetItemVO,true);
			}
		}
	}
}