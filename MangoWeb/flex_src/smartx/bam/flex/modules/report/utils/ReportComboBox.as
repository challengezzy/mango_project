package smartx.bam.flex.modules.report.utils
{
	import flash.events.MouseEvent;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	import mx.controls.ComboBox;
	import mx.core.UIComponent;
	import mx.events.ListEvent;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.mxml.RemoteObject;
	
	import qs.utils.StringUtils;
	
	import smartx.bam.flex.utils.BAMUtil;
	import smartx.flex.components.event.UIComponentEvent;
	
	public class ReportComboBox extends ReportComponent
	{
		private var comboBox:ComboBox = new ComboBox();
		private var dataProvider:ArrayCollection =new ArrayCollection();
		private var bamService:RemoteObject;
		
		public function ReportComboBox(labelName:String,keyName:String,endpoint:String,destination:String,params:Object,isMandatory:Boolean,fetchSQL:String="",comBoxItemVOs:Array=null,showLabel:Boolean=true)
		{
			super(labelName,keyName,comboBox,params,isMandatory,fetchSQL,showLabel);
			
			this.bamService = new RemoteObject(destination);
			bamService.endpoint = endpoint;
			
			bamService.getReportComponentHashVO.addEventListener(ResultEvent.RESULT,getReportComponentHashVOHandler);
			bamService.getReportComponentHashVO.addEventListener(FaultEvent.FAULT,faultHandler);
			
			if( comBoxItemVOs != null ){
				for each(var obj:Object in comBoxItemVOs){
					var item:Object = new Object();
					item.data = obj["ID"];
					item.label = obj["NAME"];
					item.code = obj["CODE"];
					dataProvider.addItem(item);
				}
			}
			
			comboBox.dataProvider = dataProvider;
			
			if(comBoxItemVOs != null){
				dataLoadComplete();
			}
			
			comboBox.selectedIndex = -1;
			comboBox.editable = false;
			comboBox.labelField="label";
			comboBox.addEventListener(ListEvent.CHANGE,dataChange);
			
			if(!BAMUtil.isEmpty(fetchSQL)){
				bamService.getReportComponentHashVO(BAMUtil.expression(fetchSQL,params));
			}
		}
		
		private function getReportComponentHashVOHandler(event:ResultEvent):void{
			dataProvider.removeAll();
			var temArr:Array = event.result as Array;
			if(temArr != null ){
				for each(var obj:Object in temArr){
					var item:Object = new Object();
					item.data = obj["ID"];
					item.label = obj["NAME"];
					item.code = obj["CODE"];
					dataProvider.addItem(item);
				}
			}
			if(dataProvider.length>0){
				comboBox.selectedIndex = 0;
			}
			comboBox.dataProvider = dataProvider;
			comboBox.selectedIndex = -1;
			dataLoadComplete();
		}
		
		private function faultHandler(event:FaultEvent):void{
			Alert.show(event.fault.faultString, 'Error');
			dataLoadComplete();
		}
		
		public override function get stringValue():String{
			if(comboBox.selectedIndex >= 0)
				return comboBox.selectedItem.data as String;
			return null;
		}
		
		public override function clearContent(event:MouseEvent):void{
			comboBox.selectedIndex = -1;
			dataChange(null);
		}
		
		public override function setValue(value:Object,showValue:Object,isDefault:Boolean=false):void {
			var tempValue:String = String(value);
			var selectItem:Object = null;
			for each(var obj:Object in dataProvider){
				var itemValue:String = String(obj.data);
				if(!BAMUtil.isEmpty(itemValue)&&!BAMUtil.isEmpty(tempValue)&&itemValue == tempValue){
					selectItem = obj;
					break;
				}
			}
			if(selectItem != null){
				comboBox.selectedItem = selectItem;
			}else{
				if(dataProvider.length>0){
					comboBox.selectedIndex = 0;
				}
			}
			var item:Object = comboBox.selectedItem;
			var valueStr:String = "";
			if(item != null){
				valueStr = item["data"];
			}
			if(isDefault){
				this.params[this.keyName] = valueStr;
				dispatchEvent(new ReportEvent(ReportEvent.SET_DEFAULT_VALUE));
			}
			dataChange(null);
		}
		
		private function dataChange(event:ListEvent):void{
			dispatchEvent(new ReportEvent(ReportEvent.REAL_VALUE_CHANGE));
		}
		
		protected override function dataLoadComplete():void{
			dispatchEvent(new ReportEvent(ReportEvent.DATA_LOAD_COMPLETE));
		}
		
		public override function refresh(variable:String=null,value:String=null):void{
			if(!BAMUtil.isEmpty(fetchSQL)){
				bamService.getReportComponentHashVO(BAMUtil.expression(fetchSQL,params));
			}

		}
		
	}
}