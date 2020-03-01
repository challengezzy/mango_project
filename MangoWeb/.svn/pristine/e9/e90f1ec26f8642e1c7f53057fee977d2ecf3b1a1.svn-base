package smartx.flex.modules.basic.system.mtdesigner
{
	import com.adobe.utils.StringUtil;
	import com.hurlant.eval.ast.Void;
	
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
	import smartx.flex.components.core.BillListPanel;
	import smartx.flex.components.styletemplate.ifc.ListButtonListener;
	import smartx.flex.components.util.ConfirmUtil;
	import smartx.flex.components.util.LoadingWindow;
	import smartx.flex.components.util.TextAreaWindow;
	import smartx.flex.components.vo.GlobalConst;
	import smartx.flex.components.vo.XMLExportObject;
	
	public class ExportMetadataButtonListener implements ListButtonListener
	{
		private var destination:String = GlobalConst.SERVICE_FORM;
		private var formService:RemoteObject;
		private var endpoint:String  = ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT) as String;
		private var listPanel:BillListPanel;
		private var operatingWindow:IFlexDisplayObject;
		
		private var xmlExportObject:XMLExportObject;
		
		private var msg:String = "是否导出全部结果?";
		
		public function ExportMetadataButtonListener()
		{
			formService = new RemoteObject(destination);
			if(endpoint != null)
				formService.endpoint = endpoint;
			formService.exportXmlToMDFile.addEventListener(ResultEvent.RESULT,exportXmlToMDFileHandler);
			formService.exportXmlToMDFile.addEventListener(FaultEvent.FAULT,faultHandler);
		}
		
		public function buttonClick(listPanel:BillListPanel):void
		{
			this.listPanel = listPanel;
			xmlExportObject = new XMLExportObject();
			xmlExportObject.tableName="pub_metadata_templet";
			xmlExportObject.pkName="id";
			xmlExportObject.visiblePkName="code";
			xmlExportObject.datasource=listPanel.datasourceName;
			
			var sql:String = "select name,code,owner,scope,content,type from pub_metadata_templet ";
			
			var condition:String = listPanel.getQueryCondition();
			
			if(condition == null || StringUtil.trim(condition)=="" || condition.toLowerCase() == "n/a"){
				msg = "是否导出全部结果?";
			}else{
				sql = sql+ " where " +condition
				msg = "是否导出查询结果?";
			}
			
			xmlExportObject.fetchSql = sql;
			
			ConfirmUtil.confirm(msg,listPanel,confirmHandler);
		
		}
		
		private function faultHandler(event:FaultEvent):void{
			endExport();
			Alert.show(event.fault.faultString, '导出失败');
		}
		
		private function exportXmlToMDFileHandler(event:ResultEvent):void{
			var url:String = event.result as String;
			navigateToURL(new URLRequest(url), "_blank");
			endExport();
		}
		
		private function confirmHandler(event:CloseEvent):void{
			if(event.detail == Alert.YES){
				startExport()
				formService.exportXmlToMDFile([xmlExportObject],"metadata.MD","meta");
			}
		}
		
		private function startExport():void{
			
			if(operatingWindow == null){
				var temp:LoadingWindow = new LoadingWindow();
				temp.text = "正在导出……";
				operatingWindow = temp;
			}
			PopUpManager.addPopUp(operatingWindow,listPanel,true);
			PopUpManager.centerPopUp(operatingWindow);
		}
		
		private function endExport():void{
			if(operatingWindow!=null)
				PopUpManager.removePopUp(operatingWindow);
		}
		
	}
}