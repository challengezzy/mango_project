package smartx.flex.components.itemcomponent.renderer
{
	import flash.display.Graphics;
	
	import mx.controls.Label;
	import mx.states.OverrideBase;
	
	import qs.utils.StringUtils;
	
	import smartx.flex.components.util.script.ScriptEvent;
	import smartx.flex.components.util.script.ScriptExecutor;
	import smartx.flex.components.util.script.ScriptExecutorFactory;
	import smartx.flex.components.vo.TempletItemVO;
	
	public class DataGridColumnColorRenderer extends Label
	{
		
		public var templetItemVO:TempletItemVO;
		
		private var _graphics:Graphics;
		
		private var itemKey:String;
		
		private var _data:Object;
		
		public function DataGridColumnColorRenderer()
		{
			super();
			this.selectable = true;
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void{
			super.updateDisplayList(unscaledWidth,unscaledHeight);
			_graphics = graphics;
			_data = data;
			if(templetItemVO != null){
//				_graphics.clear();
				this.setStyle("color",null);
				itemKey = templetItemVO.itemkey;
				var value:String = _data[itemKey];
				var colorStr:String = _data[itemKey+"_color"];
				if(colorStr != null){
					repaint(colorStr);
					return;
				}
				var colorFormula:String = templetItemVO.colorformula;
				if(colorFormula != null && colorFormula != "" ){
					
//					colorFormula = StringUtils.replaceAll(colorFormula,"{"+itemKey+"}",value);
					
					var executor:ScriptExecutor = ScriptExecutorFactory.createNewExecutor();
					executor.registorVarMap(_data);
					
					if(colorFormula.indexOf("sysOutput")<0){
						colorFormula = "sysOutput(\""+colorFormula+"\")";
					}
					executor.scriptText = colorFormula;
					try{
						executor.compile();
					}
					catch(error:Error){
//						executor.scriptText = "sysOutput(\""+colorFormula+"\")";
//						executor.compile();
					}
					
					executor.addEventListener(ScriptEvent.OUTPUT_VALUE,function(event:ScriptEvent):void{
						var colorHex:String = String(event.outputValue);
						if(isHex(colorHex)){
							
							_data[itemKey+"_color"] = colorHex;
							repaint(colorHex);
						}
					});
					executor.execute();
				}
			}
		}
		
		private function repaint(colorHex:String):void{
			if(isHex(colorHex)){
				this.setStyle("color",colorHex);
//				_graphics.beginFill(uint(colorHex));
//				_graphics.drawRect(0, 0, unscaledWidth, unscaledHeight);
//				_graphics.endFill();
			}
		}
		
		private function isHex(hexStr:String):Boolean{
			var flag:Boolean = false;
			if(hexStr != null && hexStr != ""){
				if(hexStr.match('0x[0-9A-Fa-f]{6}') != null){
					flag = true;
				}
			}
			return flag;
		}
	}
}