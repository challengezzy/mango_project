package smartx.bam.flex.modules.businessscenario
{
	import flash.net.URLRequest;
	import flash.net.navigateToURL;
	
	import mx.controls.Alert;
	import mx.core.IFlexDisplayObject;
	import mx.events.CloseEvent;
	import mx.managers.PopUpManager;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.core.BillTreePanel;
	import smartx.flex.components.styletemplate.ifc.TreeButtonListener;
	import smartx.flex.components.util.ConfirmUtil;
	import smartx.flex.components.util.LoadingWindow;
	import smartx.flex.components.vo.GlobalConst;
	import smartx.flex.components.vo.XMLExportObject;
	
	public class ScenarioExportButtonListener implements TreeButtonListener
	{
		private var tree:BillTreePanel = null;
		
		private var xmlFolder:XMLExportObject;
		
		private var operatingWindow:IFlexDisplayObject;
		
		private var endpoint:String = String(ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT));
		
		private var formService:RemoteObject;
		
		public function ScenarioExportButtonListener()
		{
			formService = new RemoteObject(GlobalConst.SERVICE_FORM);
			if(endpoint != null)
				formService.endpoint = endpoint;
			
			formService.exportXmlToMDFile.addEventListener(ResultEvent.RESULT,exportXmlToMDFileHandler);
			formService.exportXmlToMDFile.addEventListener(FaultEvent.FAULT,faultHandler);
		}
		
		public function buttonClick(treePanel:BillTreePanel):void
		{
			tree = treePanel;
			
			var xmlExportObject:XMLExportObject = new XMLExportObject();
			xmlExportObject.tableName="BAM_BUSINESSSCENARIO";
			xmlExportObject.pkName="ID";
			xmlExportObject.fkName="FOLDERID";
			xmlExportObject.visiblePkName="code";
			xmlExportObject.datasource=treePanel.datasource;
			//				xmlExportObject.childObject = xmlExportObject;
			xmlExportObject.fetchSql = "select NAME,CODE,DESCRIPTION,DATASOURCETYPE,DATASOURCECODE from BAM_BUSINESSSCENARIO ";
			
			xmlFolder = new XMLExportObject();
			xmlFolder.tableName="BAM_FOLDER";
			xmlFolder.pkName="ID";
			xmlFolder.fkName="PARENTID";
			xmlFolder.visiblePkName="code,type";
			xmlFolder.datasource=treePanel.datasource;
			
			xmlFolder.fetchSql = "select NAME,CODE,DESCRIPTION,TYPE from BAM_FOLDER where TYPE=0";
			xmlFolder.childObjects = [];
			xmlFolder.childObjects.push(xmlFolder);
			xmlFolder.childObjects.push(xmlExportObject);
			
			ConfirmUtil.confirm("确定要导出吗?",treePanel,confirmHandler);
		}
		
		private function confirmHandler(event:CloseEvent):void{
			if(event.detail == Alert.YES){
				startOperat();
				formService.exportXmlToMDFile([xmlFolder],"scenariodata.MD","scenario");
			}
		}
		
		private function faultHandler(event:FaultEvent):void{
			endOperat();
			Alert.show(event.fault.faultString, '导出业务场景失败');
		}
		
		private function exportXmlToMDFileHandler(event:ResultEvent):void{
			var url:String = event.result as String;
			navigateToURL(new URLRequest(url), "_blank");
			endOperat();
		}
		
		private function startOperat():void{
			
			if(operatingWindow == null){
				var temp:LoadingWindow = new LoadingWindow();
				temp.text = "正在导出……";
				operatingWindow = temp;
			}
			PopUpManager.addPopUp(operatingWindow,tree,true);
			PopUpManager.centerPopUp(operatingWindow);
		}
		
		private function endOperat():void{
			if(operatingWindow!=null)
				PopUpManager.removePopUp(operatingWindow);
		}
	}
}