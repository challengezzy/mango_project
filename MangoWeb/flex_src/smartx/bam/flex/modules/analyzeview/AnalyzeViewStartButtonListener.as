package smartx.bam.flex.modules.analyzeview
{
	import mx.collections.ArrayCollection;
	import mx.core.IFlexDisplayObject;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	import smartx.bam.flex.vo.BAMConst;
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.basic.DeskTopFrame;
	import smartx.flex.components.core.BillCardPanel;
	import smartx.flex.components.core.BillTreePanel;
	import smartx.flex.components.styletemplate.StyleTemplate03;
	import smartx.flex.components.styletemplate.ifc.TreeButtonListener;
	import smartx.flex.components.util.LoadingWindow;
	import smartx.flex.components.util.SmartXMessage;
	
	public class AnalyzeViewStartButtonListener implements TreeButtonListener
	{
		private var bcp:BillCardPanel;
		
		private var bamService:RemoteObject;
		
		private var loadingWindow:LoadingWindow = new LoadingWindow();
		
		public var endpoint:String = String(ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT));
		
		public function AnalyzeViewStartButtonListener()
		{
			bamService = new RemoteObject(BAMConst.BAM_Service);
			bamService.endpoint = endpoint;
			bamService.startAnalyzerJobByName.addEventListener(ResultEvent.RESULT,startAnalyzerJobByNameHandler);
			bamService.startAnalyzerJobByName.addEventListener(FaultEvent.FAULT,faultHandler);
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
					var xmlStr:String = dataValue["METADATA"] as String;
					if(code == null || code == "" ){
						SmartXMessage.show("分析视图编码为空,无法启动后台分析！");
					}else if(status != null && status=="启动"){
						SmartXMessage.show("后台分析已启动,无需多次启动!");
					}else{
						var exp:String = "";
						var xml:XML = new XML(xmlStr);
						if(xml.analyzer.length() > 0){
							exp = xml.analyzer.@analyzeJobExp;
						}
						loadingWindow.startOper("正在启动...",treePanel);
						bamService.startAnalyzerJobByName(code,exp);
					}

				}else{
					SmartXMessage.show("无法为该选项进行启动操作!");
				}
			}
		}
		
		public function startAnalyzerJobByNameHandler(event:ResultEvent):void{
			var dataValue:Object = bcp.getDataValue();
			dataValue["STATUS"] = "启动";
			bcp.setDataValue(dataValue);
			loadingWindow.stopOper();
			SmartXMessage.show("后台分析已启动!");
		}
		
		private function faultHandler(event:FaultEvent):void{
			loadingWindow.stopOper();
			SmartXMessage.show("启动后台分析失败!",SmartXMessage.MESSAGE_ERROR,event.fault.faultString);
		}
	}
}