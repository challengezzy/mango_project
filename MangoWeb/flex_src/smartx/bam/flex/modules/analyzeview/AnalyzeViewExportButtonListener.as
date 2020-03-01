package smartx.bam.flex.modules.analyzeview
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
	
	public class AnalyzeViewExportButtonListener implements TreeButtonListener
	{
		private var tree:BillTreePanel = null;
		
		private var xmlFolder:XMLExportObject;
		
		private var operatingWindow:IFlexDisplayObject;
		
		private var endpoint:String = String(ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT));
		
		private var formService:RemoteObject;
		
		public function AnalyzeViewExportButtonListener()
		{
			formService = new RemoteObject(GlobalConst.SERVICE_FORM);
			if(endpoint != null)
				formService.endpoint = endpoint;
			
			formService.exportXmlToMDFile.addEventListener(ResultEvent.RESULT,exportXmlToMDFileHandler);
			formService.exportXmlToMDFile.addEventListener(FaultEvent.FAULT,faultHandler);
		}
		
		private function faultHandler(event:FaultEvent):void{
			endOperat();
			Alert.show(event.fault.faultString, '查询视图!');
		}
		
		private function exportXmlToMDFileHandler(event:ResultEvent):void{
			var url:String = event.result as String;
			navigateToURL(new URLRequest(url), "_blank");
			endOperat();
		}
		
		public function buttonClick(treePanel:BillTreePanel):void
		{
			tree = treePanel;
			if(!treePanel.tree.selectedItems.length > 0){
				Alert.show("请选择要导出的分析视图！");
				return ;
			}
			
			ConfirmUtil.confirm("确定要导出选中的分析视图和分析视图目录下的对象吗?",treePanel,confirmHandler);
		}
		
		private function confirmHandler(event:CloseEvent):void{
			if(event.detail == Alert.YES){
				//计算要导出的仪表盘对象				
				var selectObjs:Array = tree.tree.selectedItems;
				var fetchFolderSql:String ;//查询FOLDER对象
				var fetchDashSql:String;//查询DASHBOARD对象
				var fetchFolderIdsSql:String;//查询所有关联的目录ID
				var dashboardIds:String = "-1";
				var folderIds:String = "-1";
				
				for(var i:int=0;i<selectObjs.length;i++){
					var tempObj:Object = selectObjs[i];
					if( "分析视图" == tempObj["TYPECN"]){
						dashboardIds = dashboardIds + "," + tempObj["PID"];
					}else if("分析视图目录" == tempObj["TYPECN"]){
						folderIds = folderIds + "," + tempObj["PID"];
					}
				}
				
				fetchFolderIdsSql = "select id from bam_folder f connect by prior f.parentid = f.id start with f.id in ( "
					+ " select o.folderid from BAM_ANALYZEVIEW o where o.id in (" + dashboardIds + ") )"
					+ " union all"
					+ " select id from bam_folder f connect by prior f.parentid = f.id start with f.id in (" + folderIds + ")"
					+ " union all"
					+ " select id from bam_folder f connect by prior f.id = f.parentid start with f.id in (" + folderIds + ")";
				
				fetchFolderSql = "select NAME,CODE,DESCRIPTION,TYPE,SEQ from bam_folder where type=5 and id in (" + fetchFolderIdsSql + ")"
				
				fetchDashSql = "select NAME,CODE,METADATA from BAM_ANALYZEVIEW o where o.id in (" + dashboardIds + ")"
					+ " union all "
					+ " select NAME,CODE,METADATA from BAM_ANALYZEVIEW o where o.folderid in (" + fetchFolderIdsSql + ")";
				
				var xmlExportObject:XMLExportObject = new XMLExportObject();
				xmlExportObject.tableName="BAM_ANALYZEVIEW";
				xmlExportObject.pkName="ID";
				xmlExportObject.fkName="FOLDERID";
				xmlExportObject.visiblePkName="code";
				xmlExportObject.datasource=tree.datasource;
				//xmlExportObject.fetchSql = "select NAME,CODE,DESCRIPTION,TYPE,MTCODE from BAM_DASHBOARDOBJECT ";
				xmlExportObject.fetchSql = fetchDashSql;
				
				xmlFolder = new XMLExportObject();
				xmlFolder.tableName="BAM_FOLDER";
				xmlFolder.pkName="ID";
				xmlFolder.fkName="PARENTID";
				xmlFolder.visiblePkName="code,type";
				xmlFolder.datasource=tree.datasource;
				
				//xmlFolder.fetchSql = "select NAME,CODE,DESCRIPTION,TYPE,SEQ from BAM_FOLDER where TYPE=2";
				xmlFolder.fetchSql = fetchFolderSql;
				xmlFolder.childObjects = [];
				xmlFolder.childObjects.push(xmlFolder);
				xmlFolder.childObjects.push(xmlExportObject);
				
				startOperat();
				formService.exportXmlToMDFile([xmlFolder],"analyzeview_data.MD","analyzeview");
			}
		}
		
		private function startOperat():void{
			
			if(operatingWindow == null){
				var temp:LoadingWindow = new LoadingWindow();
				temp.text = "正在导出……";
				operatingWindow = temp;
			}
			PopUpManager.addPopUp(operatingWindow,tree,true);
			PopUpManager.centerPopUp(operatingWindow);
		}
		
		private function endOperat():void{
			if(operatingWindow!=null)
				PopUpManager.removePopUp(operatingWindow);
		}
	}
}