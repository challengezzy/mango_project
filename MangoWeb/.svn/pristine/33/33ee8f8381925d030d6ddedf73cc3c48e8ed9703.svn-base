package smartx.bam.flex.modules.analyzeview
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
	import smartx.bam.flex.vo.BAMConst;
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.basic.DeskTopFrame;
	import smartx.flex.components.core.BillCardPanel;
	import smartx.flex.components.core.BillTreePanel;
	import smartx.flex.components.styletemplate.StyleTemplate03;
	import smartx.flex.components.styletemplate.ifc.TreeButtonListener;
	import smartx.flex.components.util.LoadingWindow;
	import smartx.flex.components.util.SmartXMessage;
	import smartx.flex.components.vo.GlobalConst;
	import smartx.flex.components.vo.SimpleHashVO;
	
	public class AnalyzeViewQueryButtonListener implements TreeButtonListener
	{
		private var bcp:BillCardPanel;
		
		private var columns:Array;
		
		private var smartxFormService:RemoteObject;
		
		private var dataProvider:ArrayCollection;
		
		private var operatingWindow:IFlexDisplayObject;
		
		public var endpoint:String = String(ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT));
		
		private var desktop:DeskTopFrame = ClientEnviorment.getInstance().getVar(ClientEnviorment.DESKTOP_FRAME) as DeskTopFrame;
		
		public function AnalyzeViewQueryButtonListener()
		{
			smartxFormService = new RemoteObject(GlobalConst.SERVICE_FORM);
			smartxFormService.endpoint = endpoint;
			smartxFormService.getSimpleHashVoArrayByDS.addEventListener(ResultEvent.RESULT,getSimpleHashVoArrayByDSHandler);
			smartxFormService.getSimpleHashVoArrayByDS.addEventListener(FaultEvent.FAULT,faultHandler);
		}
		
		public function buttonClick(treePanel:BillTreePanel):void
		{
			var container:StyleTemplate03 = treePanel.parent.parent.parent as StyleTemplate03;
			if(container.cardPanelBox.getChildren().length > 0){
				bcp = container.cardPanelBox.getChildAt(0) as BillCardPanel;
				var dataValue:Object = bcp.getDataValue();
				if( dataValue != null && bcp.templetCode == "T_V_BAM_ANALYZEVIEW" ){
					var code:String = dataValue["CODE"] as String;
					startOperat();
					smartxFormService.getSimpleHashVoArrayByDS(BAMConst.DATASOURCE_AVE,"select * from "+code);
				}else{
					SmartXMessage.show("无法为该选项查询结果集！");
				}
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
				SmartXMessage.show("该分析视图没有数据！");
		}
		
		private function faultHandler(event:FaultEvent):void{
			endOperat();
			SmartXMessage.show("分析视图数据错误",SmartXMessage.MESSAGE_ERROR,event.fault.faultString);
		}
		
		private function startOperat():void{
			
			if(operatingWindow == null){
				var temp:LoadingWindow = new LoadingWindow();
				temp.text = "正在查询……";
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