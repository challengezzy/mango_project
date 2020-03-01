package smartx.bam.flex.modules.common.variableControl
{
	import flash.events.Event;
	
	import mx.core.UIComponent;
	
	import smartx.bam.flex.modules.common.variableControl.vo.VariableVo;
	import smartx.flex.components.itemcomponent.ItemRefPanel;
	import smartx.flex.components.vo.SimpleRefItemVO;
	
	public class VariableControlRefPanel extends VariableControlComponent
	{
		private var itemRefPanel:ItemRefPanel;
		
		public function VariableControlRefPanel(itemRefPanel:ItemRefPanel,variableVo:VariableVo){
			super(itemRefPanel,variableVo);
			this.itemRefPanel = itemRefPanel;
			itemRefPanel.addEventListener("initComplete",refPanelInitComplete);
		}
		
		override public function get realValue():Object{
			if(isIgnoreValue()){
				var realValue:SimpleRefItemVO = new SimpleRefItemVO();
				realValue.name = realValue.id = VariableVo.IGNORE_VALUE;
				return realValue;
			}else
				return itemRefPanel.realValue;
		}
		
		override public function get stringValue():String{
			return itemRefPanel.stringValue;
		}
		
		override public function set data(value:Object):void{
			super.data = value;
			if(value == null)
				return;
			var returnVO:SimpleRefItemVO = new SimpleRefItemVO();
			returnVO.id = returnVO.code = value.defaultValue;
			returnVO.name = value.defaultValueLabel;
			var dataValue:Object = {};
			dataValue[value.name] = returnVO;
			itemRefPanel.data = dataValue;
		}
		
		private function refPanelInitComplete(event:Event):void{
			itemRefPanel.setValueByItemId(variableVo.defaultValue);
		}
	}
}