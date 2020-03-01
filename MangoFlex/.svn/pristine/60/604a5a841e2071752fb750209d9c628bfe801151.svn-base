package smartx.flex.components.itemcomponent
{
	import flash.events.MouseEvent;
	
	import mx.containers.HBox;
	import mx.controls.Label;
	import mx.controls.TextInput;
	import mx.utils.ObjectUtil;
	
	import smartx.flex.components.itemcomponent.ext.AdvTextInput;
	import smartx.flex.components.util.CompareUtil;
	import smartx.flex.components.vo.ItemVO;
	import smartx.flex.components.vo.TempletItemVO;
	
	/**数字范围查询控件  */
	public class ItemConditionNumberRange extends ItemUIComponent
	{
		private var minTextInput:AdvTextInput = new AdvTextInput();
		private var maxTextInput:AdvTextInput = new AdvTextInput();
		
		public function ItemConditionNumberRange(templetItemVO:TempletItemVO,showLabel:Boolean=true)
		{
			var hyphenLable:Label = new Label();
			hyphenLable.text = "-";
			hyphenLable.setStyle("textAlign","center");
			
			var rangBox:HBox = new HBox();
			rangBox.setStyle("horizontalGap",0);
			rangBox.setStyle("verticalAlign","middle");
			rangBox.addChild(minTextInput);
			rangBox.addChild(hyphenLable);
			rangBox.addChild(maxTextInput);
			
			super(templetItemVO, rangBox, showLabel);
			
			minTextInput.realTextInput.restrict = "0-9\-\.";
			maxTextInput.realTextInput.restrict = "0-9\-\.";
			//不显示详情编辑按钮
			minTextInput.removeChild(minTextInput.editButton);
			maxTextInput.removeChild(maxTextInput.editButton);
		}
		
		public override function getQueryConditon(isPreciseQuery:Boolean=false):String{
			var condStr:String = "";
			var minText:String = minTextInput.text;
			var maxText:String = maxTextInput.text; 
			if( (minText != null && minText != "") && (maxText != null && maxText != "") ){
				condStr =  " and ( "+templetItemVO.itemkey+" >= "+minText+" and "+templetItemVO.itemkey+" <= "+maxText+" ) ";
				
			}else if( (minText == null || minText == "") && (maxText != null && maxText != "") ){
				condStr =  " and ( "+templetItemVO.itemkey+" <= "+maxText+" ) ";
				
			}else if( (minText != null && minText != "") && (maxText == null || maxText == "") ){
				condStr =  " and ( "+templetItemVO.itemkey+" >= "+minText+" ) ";
			}
			
			return condStr;
		}
		
		protected override function clearContent(event:MouseEvent):void{
			minTextInput.text = null;
			maxTextInput.text = null;
		}
		
		public override function set editable(editable:Boolean):void{
			this._editable = editable;
			minTextInput.editable =_editable;
			maxTextInput.editable =_editable;
		}
		
		public override function getSortFunction(fieldName:String):Function{
			return function(obj1:Object,obj2:Object):int{
				if(obj1[fieldName] != null && obj2[fieldName] == null)
					return -1;
				if(obj1[fieldName] == null && obj2[fieldName] != null)
					return 1;
				if(obj1[fieldName] && obj2[fieldName]){
					if(CompareUtil.hashTotalColumn(obj1)){
						if(column){
							return (column.sortDescending?-1:1)*1;
						}else{
							return 1;
						}
					}else if(CompareUtil.hashTotalColumn(obj2)){
						if(column){
							return (column.sortDescending?-1:1)*-1;
						}else{
							return -1;
						}
					}else{
						return ObjectUtil.numericCompare(Number(obj1[fieldName]),Number(obj2[fieldName])); //数字比较
					}
				}
				return 0;
			}
		}
		
	}
}