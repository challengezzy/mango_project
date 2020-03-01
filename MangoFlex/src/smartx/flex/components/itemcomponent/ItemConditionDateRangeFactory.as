package smartx.flex.components.itemcomponent
{
	import smartx.flex.components.vo.GlobalConst;

	public class ItemConditionDateRangeFactory extends AbstractItemUIComponentFactory
	{
		public override function getComponent(mode:int):ItemUIComponent
		{
			switch(mode){
				case GlobalConst.ITEMCOMPONENTMODE_CONDITION:
					return new ItemConditionDateRange(templetItemVO,true);
				default:
					return new ItemConditionDateRange(templetItemVO,true);
			}
		}
	}
}