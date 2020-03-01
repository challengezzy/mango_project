package smartx.flex.modules.datatask
{
	import com.adobe.utils.StringUtil;
	
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
	import smartx.flex.components.util.OperatingTipUtil;
	import smartx.flex.components.util.SmartXMessage;
	import smartx.flex.components.util.TextAreaWindow;
	import smartx.flex.components.vo.GlobalConst;
	import smartx.flex.components.vo.XMLExportObject;
	
	public class ExportDataTaskListener implements ListButtonListener
	{
		private var destination:String = GlobalConst.SERVICE_FORM;
		private var formService:RemoteObject;
		private var endpoint:String  = ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT) as String;
		private var listPanel:BillListPanel;
		
		public function ExportDataTaskListener()
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
	
			SmartXMessage.show("确认导出全部查询结果？",SmartXMessage.MESSAGE_CONFIRM,null,listPanel,confirmHandler);
		}
		
		private function faultHandler(event:FaultEvent):void{
			OperatingTipUtil.endOperat();
			SmartXMessage.show("导出失败",SmartXMessage.MESSAGE_ERROR,event.fault.faultString);
		}
		
		private function exportXmlToMDFileHandler(event:ResultEvent):void{
			var url:String = event.result as String;
			navigateToURL(new URLRequest(url), "_blank");
			OperatingTipUtil.endOperat();;
		}
		
		private function confirmHandler(event:CloseEvent):void{
			if(event.detail == Alert.YES){
				var taskExpObj:XMLExportObject = new XMLExportObject();
				taskExpObj.tableName="PUB_DATATASK_TEMPLET";
				taskExpObj.pkName="ID";
				taskExpObj.visiblePkName="CODE";
				taskExpObj.datasource= listPanel.datasourceName;
				
				var condition:String = listPanel.getQueryCondition();				
				var taskSql:String = "select name,code,type,cronexpression,description,mtcode,appmodule,taskcategory,foregroundtask,icon,isshowtopo from PUB_DATATASK_TEMPLET";				
				if(condition != null && StringUtil.trim(condition) !=""){
					taskSql = taskSql+ " where " +condition
				}
				taskExpObj.fetchSql = taskSql;
				
				var mtExpObj:XMLExportObject = new XMLExportObject();
				mtExpObj.tableName="pub_metadata_templet";
				mtExpObj.pkName="id";
				mtExpObj.visiblePkName="code";
				mtExpObj.datasource= listPanel.datasourceName;
				
				var mtSql:String = "select name,code,owner,scope,content,type from pub_metadata_templet where code in ( select mtcode from (" + taskSql + "))";
				mtExpObj.fetchSql = mtSql;				
				
				OperatingTipUtil.startOperat("正在导出数据任务模板数据......",listPanel);				
				formService.exportXmlToMDFile([taskExpObj,mtExpObj],"datatask_templet.md","datatask");
			}
		}
	}
}