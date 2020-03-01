package smartx.bam.flex.modules.queryview
{
	import mx.collections.ArrayCollection;
	import mx.controls.dataGridClasses.DataGridColumn;
	import mx.core.IFlexDisplayObject;
	import mx.managers.PopUpManager;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	import mx.utils.ObjectUtil;
	
	import smartx.bam.flex.modules.businessview.BvResultWindow;
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.basic.DeskTopFrame;
	import smartx.flex.components.core.BillCardPanel;
	import smartx.flex.components.styletemplate.ifc.CardButtonListener;
	import smartx.flex.components.util.LoadingWindow;
	import smartx.flex.components.util.SmartXMessage;
	import smartx.flex.components.vo.GlobalConst;
	import smartx.flex.components.vo.SimpleComboxItemVO;
	import smartx.flex.components.vo.SimpleHashVO;
	
	public class QueryButtonListener implements CardButtonListener
	{
		private var bcp:BillCardPanel;
		
		private var columns:Array;
		
		private var smartxFormService:RemoteObject;
		
		private var dataProvider:ArrayCollection;
		
		private var operatingWindow:IFlexDisplayObject;
		
		private var cepDatasource:String = GlobalConst.CEP_DATASOURCE_PREFIX.concat(GlobalConst.DEFAULTPROVIDERNAME_CEP);
		
		public var endpoint:String = String(ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT));
		
		private var desktop:DeskTopFrame = ClientEnviorment.getInstance().getVar(ClientEnviorment.DESKTOP_FRAME) as DeskTopFrame;
		
		public function QueryButtonListener()
		{
			smartxFormService = new RemoteObject(GlobalConst.SERVICE_FORM);
			smartxFormService.endpoint = endpoint;
			smartxFormService.getSimpleHashVoArrayByDS.addEventListener(ResultEvent.RESULT,getSimpleHashVoArrayByDSHandler);
			smartxFormService.getSimpleHashVoArrayByDS.addEventListener(FaultEvent.FAULT,faultHandler);
		}
		
		public function buttonClick(cardPanel:BillCardPanel):void
		{
			
			var dataValue:Object = cardPanel.getDataValue();
			if( dataValue != null ){
				var refItem:SimpleComboxItemVO = dataValue["DATASOURCENAME"] as SimpleComboxItemVO;
				var datasource:String = refItem.id;
				var sql:String = dataValue["SQL"];
				
				startOperat();
				smartxFormService.getSimpleHashVoArrayByDS(datasource,sql);
			}
			
		}
		
		private function getSimpleHashVoArrayByDSHandler(event:ResultEvent):void{
			endOperat();
			dataProvider = new ArrayCollection();
			var queryColumns:Array = new Array();
			columns = new Array();
			
			var simpleHashVos:Array = event.result as Array;
			if(simpleHashVos.length > 0){
				var simpleVo:SimpleHashVO = simpleHashVos[0];
				var keyArray:Array = ObjectUtil.getClassInfo(simpleVo.tableNameMap).properties as Array; 
				for each(var key:String in keyArray){
					var dataGridColumn:DataGridColumn = new DataGridColumn(key);
					columns.push(dataGridColumn);
					queryColumns.push(key);
				}					
			}
			
			for each(var simpleHashVo:SimpleHashVO in simpleHashVos){
				dataProvider.addItem(simpleHashVo.dataMap);
			}
			if(dataProvider.length > 0){
				var resultWindow:BvResultWindow = new BvResultWindow();
				resultWindow.columns = columns;
				resultWindow.queryColumns = queryColumns;
				PopUpManager.addPopUp(resultWindow,desktop,true);
				PopUpManager.centerPopUp(resultWindow);
				resultWindow.initDate(dataProvider);
			}else
				SmartXMessage.show("该视图没有数据！");
		}
		
		private function faultHandler(event:FaultEvent):void{
			endOperat();
			SmartXMessage.show("查询视图数据错误",SmartXMessage.MESSAGE_ERROR,event.fault.faultString);
		}
		
		private function startOperat():void{
			
			if(operatingWindow == null){
				var temp:LoadingWindow = new LoadingWindow();
				temp.text = "正在执行……";
				operatingWindow = temp;
			}
			PopUpManager.addPopUp(operatingWindow,desktop,true);
			PopUpManager.centerPopUp(operatingWindow);
		}
		
		private function endOperat():void{
			if(operatingWindow!=null)
				PopUpManager.removePopUp(operatingWindow);
		}
	}
}