package smartx.bam.flex.modules.common.variableControl
{
	import flash.events.Event;
	
	import mx.collections.ArrayCollection;
	import mx.containers.HBox;
	import mx.containers.Tile;
	import mx.containers.VBox;
	import mx.controls.ButtonLabelPlacement;
	import mx.controls.CheckBox;
	import mx.controls.Label;
	import mx.core.UIComponent;
	
	import smartx.bam.flex.modules.common.variableControl.vo.VariableVo;
	import smartx.flex.components.util.Hashtable;
	
	public class VariableControlContainer extends VBox
	{
		public var destination:String;
		
		public var endpoint:String;
		//变量数据
		public var variableDataProvider:ArrayCollection;
		
		public var variableControlMap:Hashtable = new Hashtable();//动态变量控件MAP
		
		private const DEFAULT_LABEL_WIDTH:Number = 100;
		
		private const DEFAULT_INPUT_WIDTH:Number = 300;
		
		private var nameLabel:Label;
		
		private var hbox:HBox;
		
		public var nullTip:String;
		
		public function VariableControlContainer()
		{
			super();
			this.setStyle("verticalAlign","top");
			this.setStyle("horizontalAlign","center");
			this.setStyle("borderStyle","none");
			this.setStyle("paddingTop",20);
			this.setStyle("paddingBottom",20);
			this.setStyle("paddingLeft",20);
			this.setStyle("paddingRight",20);
		}
		
		public function refreshControl():void{
			this.removeAllChildren();
			variableControlMap.clear();
			if(variableDataProvider && variableDataProvider.length > 0){
				for each(var variable:VariableVo in variableDataProvider){
					//如果控件已经存在则不添加
					if(variableControlMap.containsKey(variable.name))
						continue;
					this.addChild(createControl(variable));
				}
			}else{
				nameLabel = new Label();
				nameLabel.text = nullTip;
				this.addChild(nameLabel);
			}
		}
		
		private function createControl(variable:VariableVo):HBox{
			hbox = new HBox();
			hbox.setStyle("horizontalGap",0);
			hbox.setStyle("verticalAlign","middle");
			
			nameLabel = new Label();
			nameLabel.text = variable.label==""?variable.name:variable.label;//如果标签名为空则取变量名作为标签名
			nameLabel.width = DEFAULT_LABEL_WIDTH;
			hbox.addChild(nameLabel);
			
			var input:VariableControlComponent = VariableControlBuilder.getBuilder().getControl(variable,destination,endpoint);
			input.width = DEFAULT_INPUT_WIDTH;
			input.toolTip = variable.name;
			hbox.addChild(input);
			variableControlMap.add(variable.name,input);
			return hbox;
		}
		
	}
}