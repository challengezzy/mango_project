package smartx.flex.components.itemcomponent
{
	import smartx.flex.components.vo.TempletItemVO;
	
	public class AbstractItemUIComponentFactory implements ItemUIComponentFactory
	{
		protected var templetItemVO:TempletItemVO;
		protected var destination:String;
		protected var endpoint:String;
		protected var dataValue:Object;;
		
		public function init(templetItemVO:TempletItemVO,destination:String=null, endpoint:String=null, dataValue:Object=null):void{
			if(templetItemVO == null)
				throw new Error("参数TempletItemVO不能为空");
			this.templetItemVO = templetItemVO;
			this.destination = destination;
			this.endpoint = endpoint;
			this.dataValue = dataValue;
		}
		
		public function getComponent(mode:int):ItemUIComponent
		{
			return null;
		}
		
	}
}