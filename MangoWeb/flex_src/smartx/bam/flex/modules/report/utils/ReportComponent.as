package smartx.bam.flex.modules.report.utils
{
	import flash.events.FocusEvent;
	import flash.events.MouseEvent;
	
	import mx.containers.Canvas;
	import mx.containers.HBox;
	import mx.controls.Button;
	import mx.controls.Label;
	import mx.core.UIComponent;
	
	import smartx.flex.components.assets.AssetsFileLib;
	
	[Event(name='realValueChange',type='smartx.bam.flex.modules.report.utils.ReportEvent')]
	[Event(name='dataLoadComplete',type='smartx.bam.flex.modules.report.utils.ReportEvent')]
	[Event(name='setDefaultValue',type='smartx.bam.flex.modules.report.utils.ReportEvent')]
	[Event(name='initComplete',type='smartx.bam.flex.modules.report.utils.ReportEvent')]
	public class ReportComponent extends Canvas
	{
		private var clearIcon:Class = AssetsFileLib.clearIcon;
		
		protected var hbox:HBox = new HBox();
		protected var hbox2:HBox = new HBox();
		protected var nameLabel:Label = new Label();
		protected var input:UIComponent;
		protected var clearButton:Button = new Button();
		private var isClearButtonUnderMouse:Boolean = false;
		protected var fetchSQL:String;
		protected var keyName:String;
		protected var labelName:String;
		protected var params:Object;
		protected var isMandatory:Boolean = false;
		
		public function ReportComponent(labelName:String,keyName:String,inputControl:UIComponent,params:Object,isMandatory:Boolean,fetchSQL:String="", showLabel:Boolean=true)
		{
			super();
			this.input = inputControl;
			this.fetchSQL = fetchSQL;
			this.keyName = keyName;
			this.labelName = labelName;
			this.params = params;
			this.isMandatory = isMandatory;
			hbox.setStyle("horizontalAlign","right");
			this.addChild(hbox);
			
			nameLabel.text = labelName;
			nameLabel.width = 60;
			if(showLabel)
				hbox.addChild(nameLabel);
			input.width = 150;
			input.height = 22;
			input.addEventListener(FocusEvent.FOCUS_IN,inputFocusIn);
			input.addEventListener(FocusEvent.FOCUS_OUT,inputFocusOut);
			
			hbox.addChild(hbox2);
			
			hbox2.setStyle("horizontalGap",0);
			hbox2.setStyle("verticalAlign","middle");
			hbox2.addChild(input);
			
			//清除按钮
			clearButton.setStyle("icon",clearIcon);
			clearButton.addEventListener(MouseEvent.CLICK,clearContent);
			clearButton.addEventListener(MouseEvent.CLICK,clearButtonMouseClick);
			clearButton.toolTip = "清除输入的内容";
			clearButton.width = 20;
			clearButton.height = 20;
			clearButton.addEventListener(MouseEvent.MOUSE_OVER,clearButtonMouseOver);
			clearButton.addEventListener(MouseEvent.MOUSE_OUT,clearButtonMouseOut);
			clearButton.tabFocusEnabled = false;
			hbox2.addChild(clearButton);
			callLater(hideClearButton);
			
		}
		
		public function hideClearButton():void{
			clearButton.setVisible(false);
		}
		
		private function clearButtonMouseClick(event:MouseEvent):void{
			hideClearButton();
		}
		
		private function clearButtonMouseOver(event:MouseEvent):void{
			isClearButtonUnderMouse = true;
		}
		
		private function clearButtonMouseOut(event:MouseEvent):void{
			isClearButtonUnderMouse = false;
		}
		
		private function inputFocusIn(event:FocusEvent):void{
 			clearButton.setVisible(true);
		}
		
		private function inputFocusOut(event:FocusEvent):void{
			if(!isClearButtonUnderMouse){
				hideClearButton();
			}		
		}
		
		public function get stringValue():String{
			return null;
		}
		
		public function clearContent(event:MouseEvent):void{
			return;
		}
		
		public function setNameLabelColor(color:String):void{
			nameLabel.setStyle("color",color);
		}
		
		public function setInputWidth(inputWidth:int):void{
			input.width = inputWidth;
		}
		
		public function setNameLabelWitdh(number:int):void{
			nameLabel.width = number;
		}
		
		public function refresh(variable:String=null,value:String=null):void{
			return;
		}
		
		public function getKeyName():String{
			return keyName;
		}
		
		public function setValue(value:Object,showValue:Object,isDefault:Boolean=false):void {
			return;
		}
		
		public function setShowValue(value:Object,isDefault:Boolean=false):void {
			return;
		}
		
		public function getIsMandatory():Boolean{
			return isMandatory;
		}
		
		public function getLabelName():String{
			return labelName;
		}
		
		protected function dataLoadComplete():void{
			
		}
		
	}
}