package smartx.bam.flex.modules.entitymodel.listener
{
	import com.adobe.utils.StringUtil;
	
	import flash.net.URLRequest;
	import flash.net.navigateToURL;
	
	import mx.controls.Alert;
	import mx.events.CloseEvent;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.mxml.RemoteObject;
	
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.core.BillListPanel;
	import smartx.flex.components.styletemplate.ifc.ListButtonListener;
	import smartx.flex.components.util.OperatingTipUtil;
	import smartx.flex.components.util.SmartXMessage;
	import smartx.flex.components.vo.GlobalConst;
	import smartx.flex.components.vo.XMLExportObject;

	public class DQEmExportListButtonListener implements ListButtonListener
	{
		private var formService:RemoteObject;
		
		private var endpoint:String  = ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT) as String;
		
		private var xmlExportObject:XMLExportObject;
		
		private var msg:String;
		
		private var listPanel:BillListPanel;
		
		public function DQEmExportListButtonListener(){
			formService = new RemoteObject(GlobalConst.SERVICE_FORM);
			if(endpoint != null)
				formService.endpoint = endpoint;
			formService.exportXmlToMDFile.addEventListener(ResultEvent.RESULT,exportXmlToMDFileHandler);
			formService.exportXmlToMDFile.addEventListener(FaultEvent.FAULT,function (event:FaultEvent):void{
				OperatingTipUtil.endOperat();
				SmartXMessage.show("导出失败",SmartXMessage.MESSAGE_ERROR,event.fault.faultString);
			});
		}
		
		public function buttonClick(listPanel:BillListPanel):void{
			this.listPanel = listPanel;
			xmlExportObject = new XMLExportObject();
			xmlExportObject.tableName="bam_entitymodel";
			xmlExportObject.pkName="id";
			xmlExportObject.visiblePkName="code";
			xmlExportObject.datasource=listPanel.datasourceName;
			
			var sql:String = "select name,code,description,mtcode,datasource from bam_entitymodel";
			
			var condition:String = listPanel.getQueryCondition();
			
			if(condition == null || StringUtil.trim(condition)=="" || condition.toLowerCase() == "n/a"){
				msg = "是否导出全部结果?";
			}else{
				sql = sql+ " where " +condition
				msg = "是否导出查询结果?";
			}
			
			xmlExportObject.fetchSql = sql;
			
			SmartXMessage.show(msg,SmartXMessage.MESSAGE_CONFIRM,"",listPanel,confirmHandler);
		}
		
		private function confirmHandler(event:CloseEvent):void{
			if(event.detail == Alert.YES){
				OperatingTipUtil.startOperat("正在导出...",listPanel);
				formService.exportXmlToMDFile([xmlExportObject],"entitymodel.MD","entitymodel");
			}
		}
		
		private function exportXmlToMDFileHandler(event:ResultEvent):void{
			var url:String = event.result as String;
			navigateToURL(new URLRequest(url), "_blank");
			OperatingTipUtil.endOperat();
		}
		
	}
}