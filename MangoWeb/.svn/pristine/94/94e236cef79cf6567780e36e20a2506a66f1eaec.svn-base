package smartx.bam.flex.modules.dataanalyze
{
	import com.flexmonster.utils.StringUtil;
	
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
	import smartx.flex.components.util.OperatingTipUtil;
	import smartx.flex.components.vo.GlobalConst;
	import smartx.flex.components.vo.SimpleHashVO;
	import smartx.flex.components.vo.SimpleRefItemVO;
	import smartx.flex.components.vo.XMLExportObject;
	
	public class DataAnalyzeExportListener implements ListButtonListener
	{
		private var formService:RemoteObject;
		private var adaptorClass:String;
		private var billListPanel:BillListPanel;
		private var destination:String = GlobalConst.SERVICE_FORM;
		private var endpoint:String  = ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT) as String;
		
		private var msg:String;
		
		private var xmlExportObjectArr:Array = new Array();
		
		public function DataAnalyzeExportListener()
		{
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
		
		public function buttonClick(blp:BillListPanel):void
		{
			this.billListPanel = blp;
			xmlExportObjectArr = new Array();
			
			var xmlExportObject:XMLExportObject = new XMLExportObject();
			xmlExportObject.tableName="dq_datavisual";
			xmlExportObject.pkName="id";
			xmlExportObject.visiblePkName="code";
			xmlExportObject.datasource=billListPanel.datasourceName;
			
			var sql:String = "select name,code,description,dashboardcode,icon from dq_datavisual ";
			
			var condition:String = billListPanel.getQueryCondition();
			
			if(condition == null || StringUtil.trim(condition)=="" || condition.toLowerCase() == "n/a"){
				msg = "是否导出全部结果?";
			}else{
				sql = sql+ " where " +condition
				msg = "是否导出查询结果?";
			}
			
			xmlExportObject.fetchSql = sql;
			
			xmlExportObjectArr.push(xmlExportObject);
			
			formService.getSimpleHashVoArrayByDS(billListPanel.datasourceName,sql);
		}
		
		private function faultHandler(event:FaultEvent):void{
			OperatingTipUtil.endOperat();
			Alert.show(event.fault.faultString, '导出失败');
		}
		
		private function getSimpleHashVoArrayByDSHandler(event:ResultEvent):void{
			var array:Array = event.result as Array;
			var conditon:String = "";
			if(array != null){
				for each(var sp:SimpleHashVO in array){
					var obj:Object = sp.dataMap;
					if(conditon == ""){
						conditon ="'"+obj["dashboardcode"]+"'";
					}else{
						conditon = conditon+",'"+obj["dashboardcode"]+"'";
					}
				}
			}
			
			if(conditon != null && conditon != "" ){
				
				var fetchFolderIdsSql:String = "select distinct id from bam_folder f connect by prior f.parentid = f.id start with f.id in ( "
					+ " select o.folderid from bam_dashboard o where o.code in (" + conditon + ") )";
				
				var fetchFolderSql:String = "select NAME,CODE,DESCRIPTION,TYPE,SEQ from bam_folder where type=1 and id in (" + fetchFolderIdsSql + ")"
				
				var fetchDashSql:String = "select * from bam_dashboard o where o.code in (" + conditon + ")"
					+ " union "
					+ " select * from bam_dashboard o where o.folderid in (" + fetchFolderIdsSql + ")";
				
				var fetchDashMtCodesSql:String = "select LAYOUT_MTCODE from bam_dashboard o where o.code in (" + conditon + ")"
					+ " union "
					+ " select LAYOUT_MTCODE from bam_dashboard o where o.folderid in (" + fetchFolderIdsSql + ")";
				
				var xmlExportObject:XMLExportObject = new XMLExportObject();
				xmlExportObject.tableName="BAM_DASHBOARD";
				xmlExportObject.pkName="ID";
				xmlExportObject.fkName="FOLDERID";
				xmlExportObject.visiblePkName="code";
				xmlExportObject.datasource= "datasource_default";
				xmlExportObject.fetchSql = "select NAME,CODE,DESCRIPTION,REFRESHINTERVAL,LAYOUT_MTCODE,SEQ from (" + fetchDashSql + ") ";
				
				var xmlFolder:XMLExportObject = new XMLExportObject();
				xmlFolder.tableName="BAM_FOLDER";
				xmlFolder.pkName="ID";
				xmlFolder.fkName="PARENTID";
				xmlFolder.visiblePkName="code,type";
				xmlFolder.datasource="datasource_default";
				
				xmlFolder.fetchSql = fetchFolderSql;
				xmlFolder.childObjects = [];
				xmlFolder.childObjects.push(xmlFolder);
				xmlFolder.childObjects.push(xmlExportObject);
				
				xmlExportObjectArr.push(xmlFolder);
				
				var xmlMTContent:XMLExportObject = new XMLExportObject();
				xmlMTContent.tableName="pub_metadata_templet";
				xmlMTContent.pkName="id";
				xmlMTContent.visiblePkName="code";
				xmlMTContent.datasource="datasource_default";
				xmlMTContent.fetchSql = "select name,code,owner,scope,content,type from pub_metadata_templet where code in (" + fetchDashMtCodesSql + ")";
				
				xmlExportObjectArr.push(xmlMTContent);
				
				var fetchFolderIdsSql2:String = "select distinct id from bam_folder f connect by prior f.parentid = f.id start with f.code='DQ_DATAVISUAL_DBO'";
				
				var fetchFolderSql2:String = "select NAME,CODE,DESCRIPTION,TYPE,SEQ from bam_folder where type=2 and id in (" + fetchFolderIdsSql2 + ")"
				
				var fetchDashSql2:String = " select NAME,CODE,DESCRIPTION,TYPE,MTCODE from bam_dashboardobject o where o.folderid in (" + fetchFolderIdsSql2 + ")";
				
				var fetchDashMtCodesSql2:String = " select MTCODE from bam_dashboardobject o where o.folderid in (" + fetchFolderIdsSql2 + ")";
				
				var xmlExportObject2:XMLExportObject = new XMLExportObject();
				xmlExportObject2.tableName="BAM_DASHBOARDOBJECT";
				xmlExportObject2.pkName="ID";
				xmlExportObject2.fkName="FOLDERID";
				xmlExportObject2.visiblePkName="code";
				xmlExportObject2.datasource="datasource_default";
				xmlExportObject2.fetchSql = fetchDashSql2;
				
				var xmlFolder2:XMLExportObject = new XMLExportObject();
				xmlFolder2.tableName="BAM_FOLDER";
				xmlFolder2.pkName="ID";
				xmlFolder2.fkName="PARENTID";
				xmlFolder2.visiblePkName="code,type";
				xmlFolder2.datasource="datasource_default";
				
				xmlFolder2.fetchSql = fetchFolderSql2;
				xmlFolder2.childObjects = [];
				xmlFolder2.childObjects.push(xmlFolder2);
				xmlFolder2.childObjects.push(xmlExportObject2);
				
				xmlExportObjectArr.push(xmlFolder2);
				
				var xmlMTContent2:XMLExportObject = new XMLExportObject();
				xmlMTContent2.tableName="pub_metadata_templet";
				xmlMTContent2.pkName="id";
				xmlMTContent2.visiblePkName="code";
				xmlMTContent2.datasource="datasource_default";
				xmlMTContent2.fetchSql = "select name,code,owner,scope,content,type from pub_metadata_templet where code in (" + fetchDashMtCodesSql2 + " )";
				
				xmlExportObjectArr.push(xmlMTContent2);
				
			}
			
			ConfirmUtil.confirm(msg,billListPanel,confirmHandler);
			
		}
		
		private function exportXmlToZipFileHandler(event:ResultEvent):void{
			OperatingTipUtil.endOperat();
			var url:String = event.result as String;
			navigateToURL(new URLRequest(url), "_blank");
		}
		private function confirmHandler(event:CloseEvent):void{
			if(event.detail == Alert.YES){
				OperatingTipUtil.startOperat("正在导出....");
				formService.exportXmlToZipFile(xmlExportObjectArr,"dataanalyze.mar","dataanalyze");
			}
		}
	}
}