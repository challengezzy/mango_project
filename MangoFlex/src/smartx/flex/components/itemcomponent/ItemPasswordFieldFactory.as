package smartx.flex.components.itemcomponent
{
	import mx.controls.TextInput;
	
	import smartx.flex.components.vo.GlobalConst;

	public class ItemPasswordFieldFactory extends AbstractItemUIComponentFactory
	{
		public override function getComponent(mode:int):ItemUIComponent
		{
			switch(mode){
				case GlobalConst.ITEMCOMPONENTMODE_LIST:
					return null;//采用默认的list编辑控件
				default:
					return new ItemPasswordField(templetItemVO);
			}
		}
	}
}