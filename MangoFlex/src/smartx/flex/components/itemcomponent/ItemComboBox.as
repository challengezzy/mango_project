package smartx.flex.components.itemcomponent
{
	import flash.events.MouseEvent;
	
	import mx.collections.ArrayCollection;
	import mx.controls.ComboBox;
	import mx.events.ListEvent;
	import mx.utils.ObjectUtil;
	
	import smartx.flex.components.event.UIComponentEvent;
	import smartx.flex.components.util.CompareUtil;
	import smartx.flex.components.vo.ItemVO;
	import smartx.flex.components.vo.SimpleComboxItemVO;
	import smartx.flex.components.vo.TempletItemVO;
	
	public class ItemComboBox extends ItemUIComponent
	{
		private var comboBox:ComboBox = new ComboBox();
		private var dataProvider:ArrayCollection =new ArrayCollection();
		
		public function ItemComboBox(templetItemVO:TempletItemVO, isCondition:Boolean=false, showLabel:Boolean=true)
		{
			super(templetItemVO,comboBox,showLabel);
			var comBoxItemVOs:Array;
			if(isCondition)
				comBoxItemVOs = templetItemVO.conditionComBoxItemVos;
			if(comBoxItemVOs == null)
				comBoxItemVOs = templetItemVO.comBoxItemVos;
			if(comBoxItemVOs == null)
				return;
			
			for each(var comBoxItemVO:SimpleComboxItemVO in comBoxItemVOs){
				var item:Object = new Object();
				item.data = comBoxItemVO.id;
				item.label = comBoxItemVO.name;
				item.realData = comBoxItemVO;
				dataProvider.addItem(item);
			}
			comboBox.dataProvider = dataProvider;
			comboBox.selectedIndex = -1;
			comboBox.editable = false;
			comboBox.addEventListener(ListEvent.CHANGE,dataChange);
		}
		
		public override function getQueryConditon(isPreciseQuery:Boolean=false):String{
			if(comboBox.selectedIndex >= 0)
				return " and ("+templetItemVO.itemkey+" = '"+comboBox.selectedItem.data+"') ";
			return "";
		}
		

		public override function get realValue():Object{
			if(comboBox.selectedIndex >= 0)
				return comboBox.selectedItem.realData;
			return null;
		}
		
		public override function get stringValue():String{
			if(comboBox.selectedIndex >= 0)
				return comboBox.selectedItem.data as String;
			return null;
		}
		
		protected override function clearContent(event:MouseEvent):void{
			comboBox.selectedIndex = -1;
			dataChange(null);
		}
   		
   		 // Define the getter method.
    	public override function set data(value:Object):void {
        	super.data = value;
        	if(value == null)
        		return;
			if(value[templetItemVO.itemkey] is SimpleComboxItemVO){
				var valueItemVO:SimpleComboxItemVO = value[templetItemVO.itemkey] as SimpleComboxItemVO;
				for each (var comboxItem:Object in dataProvider){
					if(comboxItem.data == valueItemVO.id){
						comboBox.selectedItem = comboxItem;
						return;
					}
				}
			}
			comboBox.selectedIndex = -1;
   		}
		
		public override function set editable(editable:Boolean):void{
			this._editable = editable;
			//comboBox.editable = editable;
			comboBox.enabled = editable;
		}
		
		private function dataChange(event:ListEvent):void{
			dispatchEvent(new UIComponentEvent(UIComponentEvent.REAL_VALUE_CHANGE));
		}
		
		public override function getSortFunction(fieldName:String):Function{
			return function(obj1:Object,obj2:Object):int{
				if(obj1[fieldName] != null && obj2[fieldName] == null)
					return -1;
				if(obj1[fieldName] == null && obj2[fieldName] != null)
					return 1;
				if(obj1[fieldName] is ItemVO && obj2[fieldName] is ItemVO){
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
						return ObjectUtil.stringCompare(CompareUtil.getFirstPinYin(String(ItemVO(obj1[fieldName]))),CompareUtil.getFirstPinYin(String(ItemVO(obj2[fieldName]))),true);
					}
				}
				return 0;
			}
		}

	}
}