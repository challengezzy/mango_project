package smartx.bam.flex.modules.entitymodel.listener
{
	import com.adobe.utils.StringUtil;
	
	import flash.net.URLRequest;
	import flash.net.navigateToURL;
	
	import mx.controls.Alert;
	import mx.events.CloseEvent;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.core.BillListPanel;
	import smartx.flex.components.styletemplate.ifc.ListButtonListener;
	import smartx.flex.components.util.ConfirmUtil;
	import smartx.flex.components.vo.GlobalConst;
	import smartx.flex.components.vo.SimpleHashVO;
	import smartx.flex.components.vo.XMLExportObject;
	
	public class DQEmRuleExportButton implements ListButtonListener
	{
		private var formService:RemoteObject;
		private var adaptorClass:String;
		private var billListPanel:BillListPanel;
		private var destination:String = GlobalConst.SERVICE_FORM;
		private var endpoint:String  = ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT) as String;
		
		private var xmlExportObject:XMLExportObject;
		
		private var mtExportObject:XMLExportObject;
		
		private var msg:String = "是否导出全部结果?";
		
		public function DQEmRuleExportButton(){
			formService = new RemoteObject(destination);
			if(endpoint != null)
				formService.endpoint = endpoint;
			formService.exportXmlToZipFile.addEventListener(ResultEvent.RESULT,exportXmlToZipFileHandler);
			formService.exportXmlToZipFile.addEventListener(FaultEvent.FAULT,faultHandler);
			
			formService.getSimpleHashVoArrayByDS.addEventListener(ResultEvent.RESULT,getSimpleHashVoArrayByDSHandler);
			formService.getSimpleHashVoArrayByDS.addEventListener(FaultEvent.FAULT,function(event:FaultEvent):void{
				Alert.show(event.fault.faultString, '导出失败');
			});
		}
		
		public function buttonClick(blp:BillListPanel):void{
			this.billListPanel = blp;
			
			xmlExportObject = new XMLExportObject();
			xmlExportObject.tableName="dq_entity_rule";
			xmlExportObject.pkName="id";
			xmlExportObject.visiblePkName="code";
			xmlExportObject.datasource=billListPanel.datasourceName;
			
			var sql:String = "select name,code,mtcode,entitycode,entitymodelcode,createtime,description from dq_entity_rule ";
			
			var condition:String = billListPanel.getQueryCondition();
			
			if(condition == null || StringUtil.trim(condition)=="" || condition.toLowerCase() == "n/a"){
				msg = "是否导出全部结果?";
			}else{
				sql = sql+ " where " +condition
				msg = "是否导出查询结果?";
			}
			
			xmlExportObject.fetchSql = sql;
			
			formService.getSimpleHashVoArrayByDS(billListPanel.datasourceName,sql);
//			mtExportObject = new XMLExportObject();
//			mtExportObject.tableName = "pub_metadata_templet";
//			mtExportObject.pkName = "id";
//			mtExportObject.visiblePkName = "code";
//			mtExportObject.datasource = "datasource_default";
//			mtExportObject.fetchSql = "select name,code,owner,scope,content,type from pub_metadata_templet t where type=107";
			
		}
		
		private function faultHandler(event:FaultEvent):void{   
			Alert.show(event.fault.faultString, '导出失败');
		}
		
		private function getSimpleHashVoArrayByDSHandler(event:ResultEvent):void{
			var array:Array = event.result as Array;
			var conditon:String = "";
			if(array != null){
				for each(var sp:SimpleHashVO in array){
					var obj:Object = sp.dataMap;
					if(conditon == ""){
						conditon ="'"+obj["mtcode"]+"'";
					}else{
						conditon = conditon+",'"+obj["mtcode"]+"'";
					}
				}
			}
			
			mtExportObject = new XMLExportObject();
			mtExportObject.tableName = "pub_metadata_templet";
			mtExportObject.pkName = "id";
			mtExportObject.visiblePkName = "code";
			mtExportObject.datasource = "datasource_default";
			mtExportObject.fetchSql = "select name,code,owner,scope,content,type from pub_metadata_templet t where type=107 and code in ("+conditon+")";
			
			ConfirmUtil.confirm(msg,billListPanel,confirmHandler);
			
		}
		
		private function exportXmlToZipFileHandler(event:ResultEvent):void{
			var url:String = event.result as String;
			navigateToURL(new URLRequest(url), "_blank");
			
		}
		private function confirmHandler(event:CloseEvent):void{
			if(event.detail == Alert.YES){
				formService.exportXmlToZipFile([xmlExportObject,mtExportObject],"rule_data.mar","rules");
			}
		}
	}
}