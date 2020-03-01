package smartx.bam.flex.modules.common.variableControl
{
	import mx.controls.TextInput;
	
	import smartx.bam.flex.modules.common.variableControl.vo.VariableVo;

	public class VariableControlTextInput extends VariableControlComponent
	{
		private var textInput:TextInput = new TextInput();
		
		public function VariableControlTextInput(variableVo:VariableVo){
			super(textInput,variableVo);
		}
		
		override public function get realValue():Object{
			if(isIgnoreValue())
				return VariableVo.IGNORE_VALUE;
			else
				return textInput.text;
		}
		
		override public function get stringValue():String{
			if(isIgnoreValue())
				return VariableVo.IGNORE_VALUE;
			else
				return textInput.text;
		}
		
		override public function set data(value:Object):void{
			super.data = value;
			if(value == null)
				return;
			//如果是忽略值，则TEXT框中不显示
			if(isIgnoreValue())
				textInput.text = "";
			else
				textInput.text = value.defaultValue;
		}
	}
}