package smartx.flex.components.itemcomponent
{
	import smartx.flex.components.vo.GlobalConst;

	public class ItemConditionNumberRangeFactory extends AbstractItemUIComponentFactory
	{
		public override function getComponent(mode:int):ItemUIComponent
		{
			switch(mode){
				case GlobalConst.ITEMCOMPONENTMODE_LIST:
					//return null;//采用默认的list编辑控件
					return new ItemConditionNumberRange(templetItemVO,false);
				default:
					return new ItemConditionNumberRange(templetItemVO);
			}
		}
	}
}