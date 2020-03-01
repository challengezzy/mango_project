package smartx.bam.flex.modules.dqc.listener
{
	import flash.net.URLRequest;
	import flash.net.navigateToURL;
	
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	import mx.utils.StringUtil;
	
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.core.BillListPanel;
	import smartx.flex.components.styletemplate.ifc.ListButtonListener;
	import smartx.flex.components.util.OperatingTipUtil;
	import smartx.flex.components.util.SmartXMessage;
	import smartx.flex.components.vo.GlobalConst;
	
	public class OnuPortExportListener implements ListButtonListener
	{
		//这个类可以废弃不用啦!!!,由ExcelExportBtnListener代替
		private var smartxFormService:RemoteObject;		
		public var endpoint:String = String(ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT));
		
		private var excelMTCode:String = "EXCEL_ONUPORT_CMPEXPORT";//excel导出格式定义
		
		public function OnuPortExportListener(){
			smartxFormService = new RemoteObject();
			smartxFormService.endpoint = endpoint;
			smartxFormService.destination = GlobalConst.SERVICE_FORM;;
			
			smartxFormService.excelExport.addEventListener(ResultEvent.RESULT,exportDataToExcelOkHandler);
			smartxFormService.excelExport.addEventListener(FaultEvent.FAULT,faultHandler);
		}
		
		public function buttonClick(listPanel:BillListPanel):void{
			var condition:String = listPanel.getQueryCondition();			
			var user_region:String = ClientEnviorment.getInstance().getVar("PW_LOGINUSER_REGIONNAME") as String;			
			
			if(condition == null || StringUtil.trim(condition) == "1=1"){
				if(user_region == null || StringUtil.trim(user_region) == ""){
					SmartXMessage.show("请先填入查询条件后再导出，以控制查询导出数据量！",SmartXMessage.MESSAGE_INFO);
					return;
				}
			}
			var queryCondition:String = " and 1=1 ";
			if(user_region != null)
				queryCondition = queryCondition + " and region='" + user_region +"'";
			
			if(condition != null)
				queryCondition = queryCondition + " and" +condition;
			
			smartxFormService.excelExport(excelMTCode, queryCondition, "ONU端口比对后数据.zip");
			OperatingTipUtil.startOperat("生成ONU端口导出文件");
		}
		
		public function exportDataToExcelOkHandler(event:ResultEvent):void{
			var fileUrl:String = event.result as String;
			//下载生成的excel文件
			OperatingTipUtil.endOperat();
			navigateToURL(new URLRequest(encodeURI(fileUrl)) );
		}
		
		private function faultHandler(event:FaultEvent):void{
			OperatingTipUtil.endOperat();
			SmartXMessage.show("生成ONU端口的EXCEL文件失败",SmartXMessage.MESSAGE_ERROR,event.fault.faultString);
		}
		
	}
}