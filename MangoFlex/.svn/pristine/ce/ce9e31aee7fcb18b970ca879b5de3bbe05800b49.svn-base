package smartx.flex.components.itemcomponent
{
	import smartx.flex.components.vo.GlobalConst;

	public class ItemFormulaFieldFactory extends AbstractItemUIComponentFactory
	{
		public override function getComponent(mode:int):ItemUIComponent
		{
			switch(mode){
				case GlobalConst.ITEMCOMPONENTMODE_LIST:
					return new ItemTextField(templetItemVO,false,true);
				case GlobalConst.ITEMCOMPONENTMODE_CARD:
					return new ItemTextField(templetItemVO,true,true);
				case GlobalConst.ITEMCOMPONENTMODE_CONDITION:
					return new ItemTextField(templetItemVO,true,true);
				default:
					return new ItemTextField(templetItemVO,true,true);
			}
		}
	}
}