package smartx.bam.flex.modules.common.variableControl
{
	import mx.collections.ArrayCollection;
	import mx.controls.ComboBox;
	import mx.core.UIComponent;
	
	import smartx.bam.flex.modules.common.variableControl.vo.VariableVo;
	
	public class VariableControlComboBox extends VariableControlComponent
	{
		private var comboBox:ComboBox = new ComboBox();
		
		public function VariableControlComboBox(dataProvider:ArrayCollection,variableVo:VariableVo)
		{
			super(comboBox,variableVo);
			
			comboBox.labelField = "label";
			comboBox.dataProvider = dataProvider;
		}
		
		override public function get realValue():Object{
			if(isIgnoreValue())
				return VariableVo.IGNORE_VALUE;
			else
				return comboBox.selectedItem?comboBox.selectedItem.data:null;
		}
		
		override public function get stringValue():String{
			return comboBox.selectedItem?comboBox.selectedItem.label:null;
		}
		
		override public function set data(value:Object):void{
			super.data = value;
			if(value == null)
				return;
			for each(var cboData:Object in comboBox.dataProvider){
				if(cboData.data == value.defaultValue){
					comboBox.selectedItem = cboData;
					break;
				}else
					comboBox.selectedIndex = -1;
			}
		}
	}
}