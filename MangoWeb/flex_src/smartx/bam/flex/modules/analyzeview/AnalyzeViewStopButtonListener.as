package smartx.bam.flex.modules.analyzeview
{
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	import smartx.bam.flex.vo.BAMConst;
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.core.BillCardPanel;
	import smartx.flex.components.core.BillTreePanel;
	import smartx.flex.components.styletemplate.StyleTemplate03;
	import smartx.flex.components.styletemplate.ifc.TreeButtonListener;
	import smartx.flex.components.util.LoadingWindow;
	import smartx.flex.components.util.SmartXMessage;
	
	public class AnalyzeViewStopButtonListener implements TreeButtonListener
	{
		private var bcp:BillCardPanel;
		
		private var bamService:RemoteObject;
		
		private var loadingWindow:LoadingWindow = new LoadingWindow();
		
		public var endpoint:String = String(ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT));
		
		public function AnalyzeViewStopButtonListener()
		{
			bamService = new RemoteObject(BAMConst.BAM_Service);
			bamService.endpoint = endpoint;
			bamService.stopAnalyzerJobByName.addEventListener(ResultEvent.RESULT,stopAnalyzerJobByNameHandler);
			bamService.stopAnalyzerJobByName.addEventListener(FaultEvent.FAULT,faultHandler);
		}
		
		public function buttonClick(treePanel:BillTreePanel):void
		{
			var container:StyleTemplate03 = treePanel.parent.parent.parent as StyleTemplate03;
			if(container.cardPanelBox.getChildren().length > 0){
				bcp = container.cardPanelBox.getChildAt(0) as BillCardPanel;
				var dataValue:Object = bcp.getDataValue();
				if( dataValue != null && bcp.templetCode == "T_V_BAM_ANALYZEVIEW" ){
					var code:String = dataValue["CODE"] as String;
					var status:String = dataValue["STATUS"] as String;
					if(code == null || code == "" ){
						SmartXMessage.show("分析视图编码为空,无法停止后台分析！");
					}else if(status != null && status == "停止"){
						SmartXMessage.show("后台分析已停止,无需多次停止!");
					}else{
						loadingWindow.startOper("正在停止...",treePanel);
						bamService.stopAnalyzerJobByName(code);
					}
					
				}else{
					SmartXMessage.show("无法为该选项进行停止操作！");
				}
			}
		}
		
		public function stopAnalyzerJobByNameHandler(event:ResultEvent):void{
			var dataValue:Object = bcp.getDataValue();
			dataValue["STATUS"] = "停止";
			bcp.setDataValue(dataValue);
			loadingWindow.stopOper();
			SmartXMessage.show("后台分析已停止!");
		}
		
		private function faultHandler(event:FaultEvent):void{
			loadingWindow.stopOper();
			SmartXMessage.show("停止后台分析失败!",SmartXMessage.MESSAGE_ERROR,event.fault.faultString);
		}
		
	}
}