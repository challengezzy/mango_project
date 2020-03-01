package smartx.flex.components.itemcomponent
{
	import mx.utils.ObjectUtil;
	
	import smartx.flex.components.vo.GlobalConst;
	import smartx.flex.components.vo.ItemVO;

	public class ItemMultiRefPanelFactory extends AbstractItemUIComponentFactory
	{
		public override function getComponent(mode:int):ItemUIComponent
		{
			switch(mode){
				case GlobalConst.ITEMCOMPONENTMODE_LIST:
					return new ItemRefPanel(templetItemVO,destination,endpoint,false,false);
				case GlobalConst.ITEMCOMPONENTMODE_CARD:
					return new ItemRefPanel(templetItemVO,destination,endpoint,false,true,dataValue);
				case GlobalConst.ITEMCOMPONENTMODE_CONDITION:
					return new ItemRefPanel(templetItemVO,destination,endpoint,true,true);
				default:
					return new ItemRefPanel(templetItemVO,destination,endpoint,false,true,dataValue);
			}
		}
		
	}
}