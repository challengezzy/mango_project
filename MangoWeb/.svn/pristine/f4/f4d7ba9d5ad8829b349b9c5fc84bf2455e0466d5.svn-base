package smartx.bam.flex.modules.eventadapter
{
	import com.adobe.utils.StringUtil;
	
	import flash.net.URLRequest;
	import flash.net.navigateToURL;
	import flash.utils.getDefinitionByName;
	
	import mx.controls.Alert;
	import mx.core.IFlexDisplayObject;
	import mx.events.CloseEvent;
	import mx.managers.PopUpManager;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	import smartx.bam.flex.vo.BAMConst;
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.core.BillListPanel;
	import smartx.flex.components.styletemplate.ifc.ListButtonListener;
	import smartx.flex.components.util.ConfirmUtil;
	import smartx.flex.components.util.LoadingWindow;
	import smartx.flex.components.util.StringUtils;
	import smartx.flex.components.vo.GlobalConst;
	import smartx.flex.components.vo.XMLExportObject;
	
	/**
	 * sky zhangzz
	 **/
	public class ExporteventButtonListener implements ListButtonListener
	{
		private var formService:RemoteObject;
		private var adaptorClass:String;
		private var billListPanel:BillListPanel;
		private var destination:String = GlobalConst.SERVICE_FORM;
		private var endpoint:String  = ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT) as String;
		
		private var xmlExportObject:XMLExportObject;
		
		private var operatingWindow:IFlexDisplayObject;
		
		private var msg:String = "是否导出全部结果?";
		
		public function ExporteventButtonListener()
		{
			formService = new RemoteObject(destination);
			if(endpoint != null)
				formService.endpoint = endpoint;
			formService.exportXmlToMDFile.addEventListener(ResultEvent.RESULT,exportXmlToMDFileHandler);
			formService.exportXmlToMDFile.addEventListener(FaultEvent.FAULT,faultHandler);
		}
		
		public function buttonClick(blp:BillListPanel):void
		{
			this.billListPanel = blp;
			xmlExportObject = new XMLExportObject();
			xmlExportObject.tableName="bam_eventdatasource";
			xmlExportObject.pkName="id";
			xmlExportObject.visiblePkName="code";
			xmlExportObject.datasource=billListPanel.datasourceName;
			
			var sql:String = "select name,code,adaptorclass,configuration, 1 as status from bam_eventdatasource ";
			
			var condition:String = billListPanel.getQueryCondition();
			
			if(condition == null || StringUtil.trim(condition)=="" || condition.toLowerCase() == "n/a"){
				msg = "是否导出全部结果?";
			}else{
				sql = sql+ " where " +condition
				msg = "是否导出查询结果?";
			}
			
			xmlExportObject.fetchSql = sql;
			
			ConfirmUtil.confirm(msg,billListPanel,confirmHandler);
		}
		
		private function faultHandler(event:FaultEvent):void{
			endOperat();
			Alert.show(event.fault.faultString, '导出失败');
		}
		
		private function exportXmlToMDFileHandler(event:ResultEvent):void{
			var url:String = event.result as String;
			navigateToURL(new URLRequest(url), "_blank");
			endOperat();
			
		}
		
		private function confirmHandler(event:CloseEvent):void{
			if(event.detail == Alert.YES){
				startOperat();
				formService.exportXmlToMDFile([xmlExportObject],"event_data.MD","event");
			}
		}
		
		private function startOperat():void{
			
			if(operatingWindow == null){
				var temp:LoadingWindow = new LoadingWindow();
				temp.text = "正在导出……";
				operatingWindow = temp;
			}
			PopUpManager.addPopUp(operatingWindow,billListPanel,true);
			PopUpManager.centerPopUp(operatingWindow);
		}
		
		private function endOperat():void{
			if(operatingWindow!=null)
				PopUpManager.removePopUp(operatingWindow);
		}
	}
}