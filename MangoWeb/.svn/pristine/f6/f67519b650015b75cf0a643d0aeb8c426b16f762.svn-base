package smartx.bam.flex.modules.common.variableControl
{
	import flash.events.Event;
	
	import mx.containers.Canvas;
	import mx.containers.HBox;
	import mx.controls.ButtonLabelPlacement;
	import mx.controls.CheckBox;
	import mx.core.UIComponent;
	
	import smartx.bam.flex.modules.common.variableControl.vo.VariableVo;
	
	public class VariableControlComponent extends Canvas
	{
		private var input:UIComponent;
		
		private var hbox:HBox = new HBox();
		
		private var ignoreChk:CheckBox = new CheckBox();
		
		protected var variableVo:VariableVo;
		
		public function VariableControlComponent(inputControl:UIComponent,variableVo:VariableVo){
			super();
			this.input = inputControl;
			this.variableVo = variableVo;
			
//			input.percentHeight = 100;
			input.percentWidth = 100;
			input.height = 22;
			hbox.setStyle("horizontalGap",5);
			hbox.setStyle("verticalAlign","middle");
			hbox.percentHeight=100;
			hbox.percentWidth = 100;
			hbox.addChild(input);
			if(variableVo.isIgnoreDefaultValue){
				ignoreChk.label = "忽略";
				ignoreChk.labelPlacement = ButtonLabelPlacement.RIGHT;
				if(variableVo.defaultValue == VariableVo.IGNORE_VALUE){
					ignoreChk.selected = true;
					input.enabled = false;
				}
				ignoreChk.addEventListener(Event.CHANGE,checkBoxChangeHandler);
				hbox.addChild(ignoreChk);
			}
			
			this.addChild(hbox);
		}
		
		
		private function checkBoxChangeHandler(event:Event):void{
			if(ignoreChk.selected)
				input.enabled = false;
			else
				input.enabled = true;
		}
		
		public function get realValue():Object{
			return null;
		}
		
		public function get stringValue():String{
			return null;
		}
		
		protected function isIgnoreValue():Boolean{
			if(variableVo.isIgnoreDefaultValue && ignoreChk.selected)
				return true;
			else
				return false;
		}
		
	}
}