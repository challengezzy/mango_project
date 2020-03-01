package smartx.bam.flex.modules.dashboardobject
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
	
	public class DBOExportButtonListener implements TreeButtonListener
	{
		private var treePanel:BillTreePanel;
		
		private var xmlFolder:XMLExportObject;
		
		private var operatingWindow:IFlexDisplayObject;
		
		private var endpoint:String = String(ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT));
		
		private var formService:RemoteObject;
		
		public function DBOExportButtonListener()
		{
			formService = new RemoteObject(GlobalConst.SERVICE_FORM);
			if(endpoint != null)
				formService.endpoint = endpoint;
			
			formService.exportXmlToMDFile.addEventListener(ResultEvent.RESULT,exportXmlToMDFileHandler);
			formService.exportXmlToMDFile.addEventListener(FaultEvent.FAULT,faultHandler);
		}
		
		private function faultHandler(event:FaultEvent):void{
			endOperat();
			Alert.show(event.fault.faultString, '仪表盘对象错误!');
		}
		
		private function exportXmlToMDFileHandler(event:ResultEvent):void{
			var url:String = event.result as String;
			navigateToURL(new URLRequest(url), "_blank");
			endOperat();
		}
		
		public function buttonClick(treePanel:BillTreePanel):void
		{
			this.treePanel = treePanel;
			
			if(!treePanel.tree.selectedItems.length > 0){
				Alert.show("请选择要导出的仪表盘对象！");
				return ;
			}
			
			ConfirmUtil.confirm("确定要导出选中的仪表盘和仪表盘目录下的对象吗?",treePanel,confirmHandler);
		}
		
		private function confirmHandler(event:CloseEvent):void{
			if(event.detail == Alert.YES){
				//计算要导出的仪表盘对象				
				var selectObjs:Array = treePanel.tree.selectedItems;
				var fetchFolderSql:String ;//查询FOLDER对象
				var fetchDashSql:String;//查询DASHBOARD对象
				var fetchFolderIdsSql:String;//查询所有关联的目录ID
				var dashboardIds:String = "-1";
				var folderIds:String = "-1";
				
				for(var i:int=0;i<selectObjs.length;i++){
					var tempObj:Object = selectObjs[i];
					if( "仪表盘对象" == tempObj["TYPECN"]){
						dashboardIds = dashboardIds + "," + tempObj["PID"];
					}else if("仪表盘对象目录" == tempObj["TYPECN"]){
						folderIds = folderIds + "," + tempObj["PID"];
					}
				}
				
				fetchFolderIdsSql = "select id from bam_folder f connect by prior f.parentid = f.id start with f.id in ( "
					+ " select o.folderid from bam_dashboardobject o where o.id in (" + dashboardIds + ") )"
					+ " union all"
					+ " select id from bam_folder f connect by prior f.parentid = f.id start with f.id in (" + folderIds + ")"
					+ " union all"
					+ " select id from bam_folder f connect by prior f.id = f.parentid start with f.id in (" + folderIds + ")";
				
				fetchFolderSql = "select NAME,CODE,DESCRIPTION,TYPE,SEQ from bam_folder where type=2 and id in (" + fetchFolderIdsSql + ")"
				
				fetchDashSql = "select NAME,CODE,DESCRIPTION,TYPE,MTCODE from bam_dashboardobject o where o.id in (" + dashboardIds + ")"
					+ " union "
					+ " select NAME,CODE,DESCRIPTION,TYPE,MTCODE from bam_dashboardobject o where o.folderid in (" + fetchFolderIdsSql + ")";
				
				var fetchDashMtCodesSql:String = "select MTCODE from bam_dashboardobject o where o.id in (" + dashboardIds + ")"
					+ " union "
					+ " select MTCODE from bam_dashboardobject o where o.folderid in (" + fetchFolderIdsSql + ")";
				
				var xmlExportObject:XMLExportObject = new XMLExportObject();
				xmlExportObject.tableName="BAM_DASHBOARDOBJECT";
				xmlExportObject.pkName="ID";
				xmlExportObject.fkName="FOLDERID";
				xmlExportObject.visiblePkName="code";
				xmlExportObject.datasource=treePanel.datasource;
				//xmlExportObject.fetchSql = "select NAME,CODE,DESCRIPTION,TYPE,MTCODE from BAM_DASHBOARDOBJECT ";
				xmlExportObject.fetchSql = fetchDashSql;
				
				xmlFolder = new XMLExportObject();
				xmlFolder.tableName="BAM_FOLDER";
				xmlFolder.pkName="ID";
				xmlFolder.fkName="PARENTID";
				xmlFolder.visiblePkName="code,type";
				xmlFolder.datasource=treePanel.datasource;
				
				//xmlFolder.fetchSql = "select NAME,CODE,DESCRIPTION,TYPE,SEQ from BAM_FOLDER where TYPE=2";
				xmlFolder.fetchSql = fetchFolderSql;
				xmlFolder.childObjects = [];
				xmlFolder.childObjects.push(xmlFolder);
				xmlFolder.childObjects.push(xmlExportObject);
				
				var xmlMTContent:XMLExportObject = new XMLExportObject();
				xmlMTContent.tableName="pub_metadata_templet";
				xmlMTContent.pkName="id";
				xmlMTContent.visiblePkName="code";
				xmlMTContent.datasource=treePanel.datasource;
				//xmlMTContent.fetchSql = "select name,code,owner,scope,content,type from pub_metadata_templet where code in (select MTCODE from BAM_DASHBOARDOBJECT )";
				xmlMTContent.fetchSql = "select name,code,owner,scope,content,type from pub_metadata_templet where code in (" + fetchDashMtCodesSql + " )";
				
				startOperat();
				formService.exportXmlToMDFile([xmlFolder,xmlMTContent],"dashboard_object_data.MD","dashboardobject");
			}
		}
		
		private function startOperat():void{
			if(operatingWindow == null){
				var temp:LoadingWindow = new LoadingWindow();
				temp.text = "正在导出……";
				operatingWindow = temp;
			}
			PopUpManager.addPopUp(operatingWindow,treePanel,true);
			PopUpManager.centerPopUp(operatingWindow);
		}
		
		private function endOperat():void{
			if(operatingWindow!=null)
				PopUpManager.removePopUp(operatingWindow);
		}
		
	}
}