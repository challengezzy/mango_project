package smartx.flex.components.itemcomponent
{
	import mx.utils.ObjectUtil;
	
	import smartx.flex.components.vo.GlobalConst;
	import smartx.flex.components.vo.ItemVO;

	public class ItemComboBoxFactory extends AbstractItemUIComponentFactory
	{
		public override function getComponent(mode:int):ItemUIComponent
		{
			switch(mode){
				case GlobalConst.ITEMCOMPONENTMODE_LIST:
					return new ItemComboBox(templetItemVO,false,false);
				case GlobalConst.ITEMCOMPONENTMODE_CARD:
					return new ItemComboBox(templetItemVO,false,true);
				case GlobalConst.ITEMCOMPONENTMODE_CONDITION:
					return new ItemComboBox(templetItemVO,true,true);
				default:
					return new ItemComboBox(templetItemVO,false,true);
			}
		}
		
	}
}