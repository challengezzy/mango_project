package smartx.flex.components.itemcomponent
{
	import smartx.flex.components.vo.TempletItemVO;

	public interface ItemUIComponentFactory
	{
		function init(templetItemVO:TempletItemVO,destination:String=null, endpoint:String=null, dataValue:Object=null):void;
		function getComponent(mode:int):ItemUIComponent;
	}
}