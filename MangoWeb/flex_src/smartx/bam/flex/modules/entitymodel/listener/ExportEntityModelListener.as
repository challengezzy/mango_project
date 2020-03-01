package smartx.bam.flex.modules.entitymodel.listener
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
	import smartx.flex.components.vo.SimpleHashVO;
	import smartx.flex.components.vo.XMLExportObject;
	
	public class ExportEntityModelListener implements ListButtonListener
	{
		private var destination:String = GlobalConst.SERVICE_FORM;
		private var formService:RemoteObject;
		private var endpoint:String  = ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT) as String;
		private var listPanel:BillListPanel;
		private var emExpObj:XMLExportObject ;
		private var entityMtObj:XMLExportObject;
		private var mtExpObj:XMLExportObject ;
		
		public function ExportEntityModelListener()
		{
			formService = new RemoteObject(destination);
			if(endpoint != null)
				formService.endpoint = endpoint;
			
			formService.exportXmlToZipFile.addEventListener(ResultEvent.RESULT,exportXmlToZipFileHandler);
			formService.exportXmlToZipFile.addEventListener(FaultEvent.FAULT,faultHandler);
			
			formService.getSimpleHashVoArrayByDS.addEventListener(ResultEvent.RESULT,getSimpleHashVoArrayByDSHandler);
			formService.getSimpleHashVoArrayByDS.addEventListener(FaultEvent.FAULT,faultHandler);
			
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
		
		private function exportXmlToZipFileHandler(event:ResultEvent):void{
			var url:String = event.result as String;
			navigateToURL(new URLRequest(url), "_blank");
			OperatingTipUtil.endOperat();;
		}
		
		private function confirmHandler(event:CloseEvent):void{
			if(event.detail == Alert.YES){
				emExpObj = new XMLExportObject();
				emExpObj.tableName="BAM_ENTITYMODEL";
				emExpObj.pkName="ID";
				emExpObj.visiblePkName="CODE";
				emExpObj.datasource= listPanel.datasourceName;
				
				var condition:String = listPanel.getQueryCondition();				
				var emSql:String = "SELECT CODE,NAME,DESCRIPTION,MTCODE,DATASOURCE,DWDS FROM BAM_ENTITYMODEL ";				
				if(condition != null && StringUtil.trim(condition) !=""){
					emSql = emSql+ " where " +condition
				}
				emExpObj.fetchSql = emSql;
				
				mtExpObj = new XMLExportObject();
				mtExpObj.tableName="pub_metadata_templet";
				mtExpObj.pkName="id";
				mtExpObj.visiblePkName="code";
				mtExpObj.datasource= listPanel.datasourceName;
				
				var mtSql:String = "select name,code,owner,scope,content,type from pub_metadata_templet where code in ( select mtcode from (" + emSql + "))";
				mtExpObj.fetchSql = mtSql;				
				
				formService.getSimpleHashVoArrayByDS(listPanel.datasourceName,mtSql);
				OperatingTipUtil.startOperat("正在导出");
			}
		}
		
		private function getSimpleHashVoArrayByDSHandler(event:ResultEvent):void{
			var array:Array = event.result as Array;
			var conditon:String = "''";
			if(array != null){
				for each(var sp:SimpleHashVO in array){
					var obj:Object = sp.dataMap;
					var emContent:XML = new XML(obj["content"] as String);
					for each( var entity:XML in emContent.entities.entity){
						//针对每个领域实体，导出领域实体中的实体元数据
						var mtcode:String = entity.@mtcode;
						
						conditon = conditon+",'"+mtcode+"'";
					}
				}
			}
			
			entityMtObj = new XMLExportObject();
			entityMtObj.tableName = "pub_metadata_templet";
			entityMtObj.pkName = "id";
			entityMtObj.visiblePkName = "code";
			entityMtObj.datasource = "datasource_default";
			entityMtObj.fetchSql = "select name,code,owner,scope,content,type from pub_metadata_templet t where type=106 and code in ("+conditon+")";
			formService.exportXmlToZipFile([emExpObj,mtExpObj,entityMtObj],"EntityModel.mar","entitymodel");
		}
	}
}