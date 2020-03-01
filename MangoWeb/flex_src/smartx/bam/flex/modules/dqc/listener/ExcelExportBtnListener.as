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
	import smartx.flex.components.util.MetadataTempletUtil;
	import smartx.flex.components.util.OperatingTipUtil;
	import smartx.flex.components.util.SmartXMessage;
	import smartx.flex.components.vo.GlobalConst;
	import smartx.flex.components.vo.MetadataTemplet;
	
	public class ExcelExportBtnListener implements ListButtonListener
	{
		private var smartxFormService:RemoteObject;		
		public var endpoint:String = String(ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT));
		
		private var exportMapMtCode:String = "DQC_EXCELEXPORT_MAP";//DQC导出EXCEL数据映射 定义
		private var fileName:String ="excel";//下载文件名
		private var excelmtcode:String;//导出EXCLE内容定义
		
		public function ExcelExportBtnListener(){
			smartxFormService = new RemoteObject();
			smartxFormService.endpoint = endpoint;
			smartxFormService.destination = GlobalConst.SERVICE_FORM;;
			
			smartxFormService.excelExport.addEventListener(ResultEvent.RESULT,exportDataToExcelOkHandler);
			smartxFormService.excelExport.addEventListener(FaultEvent.FAULT,faultHandler);
		}
		
		public function buttonClick(listPanel:BillListPanel):void{
			//默认所有EXCEL的导出数据，均是按区域进行导出
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
			
			var pubtempletCode:String = listPanel.templetCode;//元模板编码
			var metadata:MetadataTemplet =  MetadataTempletUtil.getInstance().findMetadataTemplet( exportMapMtCode );
			var contentXml:XML = new XML(metadata.content);
			for each(var mapxml:XML in contentXml.map){
				var temp:String = mapxml.@templetcode;
				if(pubtempletCode == temp){
					fileName = mapxml.@filename;
					excelmtcode = mapxml.@excelmtcode;
				}
			}
			
			smartxFormService.excelExport(excelmtcode, queryCondition, fileName + ".zip");
			OperatingTipUtil.startOperat("生成文件中...");
		}
		
		public function exportDataToExcelOkHandler(event:ResultEvent):void{
			var fileUrl:String = event.result as String;
			//下载生成的excel文件
			OperatingTipUtil.endOperat();
			navigateToURL(new URLRequest(encodeURI(fileUrl)) );
		}
		
		private function faultHandler(event:FaultEvent):void{
			OperatingTipUtil.endOperat();
			SmartXMessage.show("生成"+fileName+"文件失败",SmartXMessage.MESSAGE_ERROR,event.fault.faultString);
		}
		
	}
}