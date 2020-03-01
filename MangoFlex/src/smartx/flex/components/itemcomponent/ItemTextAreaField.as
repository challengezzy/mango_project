package smartx.flex.components.itemcomponent
{
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import mx.controls.TextArea;
	import mx.events.FlexEvent;
	import mx.utils.ObjectUtil;
	
	import smartx.flex.components.event.UIComponentEvent;
	import smartx.flex.components.util.CompareUtil;
	import smartx.flex.components.vo.ItemVO;
	import smartx.flex.components.vo.TempletItemVO;

	public class ItemTextAreaField extends ItemUIComponent
	{
	
		private var textInput:TextArea = new TextArea();

		public function ItemTextAreaField(templetItemVO:TempletItemVO)
		{
			super(templetItemVO,textInput);
			textInput.setStyle("borderStyle","solid");
			textInput.height = 60;
			textInput.addEventListener(Event.CHANGE,dataChange);
			textInput.addEventListener(FlexEvent.ENTER,function(event:FlexEvent):void{
				dispatchEvent(event);
			});
		}
		
		public function setTextInputHeight(textInputHeight:Number):void{
			textInput.height = textInputHeight;
		}
		
		public override function getQueryConditon(isPreciseQuery:Boolean=false):String{
			if(textInput.text != null && textInput.text != ""){
				if(isPreciseQuery)
					return " and ("+templetItemVO.itemkey+" = '"+textInput.text+"') ";
				else
					return " and ("+templetItemVO.itemkey+" like '%"+textInput.text+"%') ";
			}
			return "";
		}
		
		public override function get realValue():Object{
			if(textInput.text != "")
				return textInput.text;
			return null;
		}
		
		public override function get stringValue():String{
			return textInput.text;
		}
		
		protected override function clearContent(event:MouseEvent):void{
			textInput.text = null;
			dataChange(null);
		}
		
		public override function set editable(editable:Boolean):void{
			this._editable = editable;
			//clearButton.setVisible(_editable);
			textInput.editable = _editable;
		}
		
		public override function set data(value:Object):void {
        	super.data = value;
        	if(value != null)
        		textInput.text = value[templetItemVO.itemkey];
        	else
        		textInput.text = null;
        }
        
        private function dataChange(event:Event):void{
			dispatchEvent(new UIComponentEvent(UIComponentEvent.REAL_VALUE_CHANGE));
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
						return ObjectUtil.stringCompare(CompareUtil.getFirstPinYin(String(obj1[fieldName])),CompareUtil.getFirstPinYin(String(obj2[fieldName])),true);
					}
				}
				return 0;
			}
		}
		
	}
}