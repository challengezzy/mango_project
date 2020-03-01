package smartx.flex.modules.basic.system.menumgmt
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
	import smartx.flex.components.util.TextAreaWindow;
	import smartx.flex.components.vo.GlobalConst;
	import smartx.flex.components.vo.XMLExportObject;
	
	public class MenuExportButtonListener implements TreeButtonListener
	{
		private var destination:String = GlobalConst.SERVICE_FORM;
		private var endpoint:String  = ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT) as String;
		private var formService:RemoteObject;
		private var treePanel:BillTreePanel;
		private var operatingWindow:IFlexDisplayObject;
		
		private var xmlExportObject:XMLExportObject
		
		public function MenuExportButtonListener()
		{
			formService = new RemoteObject(destination);
			if(endpoint != null)
				formService.endpoint = endpoint;
			
			formService.exportXmlToMDFile.addEventListener(ResultEvent.RESULT,exportXmlToMDFileHandler);
			formService.exportXmlToMDFile.addEventListener(FaultEvent.FAULT,faultHandler);
		}
		
		public function buttonClick(treePanel:BillTreePanel):void
		{
			this.treePanel = treePanel;
			
			if(!treePanel.tree.selectedItems.length > 0){
				Alert.show("请选择要导出的菜单项！");
				return ;
			}
			
			ConfirmUtil.confirm("确定要导出吗?",treePanel,confirmHandler);
		}
		private function faultHandler(event:FaultEvent):void{
			endExport();
			Alert.show(event.fault.faultString, '菜单导出失败');
		}
		
		private function exportXmlToMDFileHandler(event:ResultEvent):void{
			var url:String = event.result as String;
			navigateToURL(new URLRequest(url), "_blank");
			endExport();
		}
		
		private function confirmHandler(event:CloseEvent):void{
			if(event.detail == Alert.YES){
				
				var selectObjs:Array = treePanel.tree.selectedItems;
				var menuIds:String = "-1";
				var fetchMenuSql:String;
				
				for(var i:int=0;i<selectObjs.length;i++){
					var tempObj:Object = selectObjs[i];
					menuIds = menuIds + "," + tempObj["ID"];
				}
				var tempMenuSql:String = "select * from pub_menu m connect by prior m.parentmenuid=m.id  start with m.id in (" + menuIds + ")"
					+ " union "
				    + "select * from pub_menu m  connect by prior m.id=m.parentmenuid start with m.id in (" + menuIds + ")";
				
				fetchMenuSql = "select name,localname,icon,seq,command,showintoolbar,toolbarseq,appmodule,comments,isflex from ("
					+ tempMenuSql +") where isFlex='Y'";
				
				xmlExportObject = new XMLExportObject();
				xmlExportObject.tableName="pub_menu";
				xmlExportObject.pkName="id";
				xmlExportObject.fkName="parentmenuid";
				xmlExportObject.visiblePkName="name";
				xmlExportObject.datasource=null;
				xmlExportObject.childObjects = [xmlExportObject];
				xmlExportObject.fetchSql = fetchMenuSql;
				
				startExport();
				formService.exportXmlToMDFile([xmlExportObject],"menudata.MD","menu");
			}
		}
		
		private function startExport():void{
			
			if(operatingWindow == null){
				var temp:LoadingWindow = new LoadingWindow();
				temp.text = "正在导出……";
				operatingWindow = temp;
			}
			PopUpManager.addPopUp(operatingWindow,treePanel,true);
			PopUpManager.centerPopUp(operatingWindow);
		}
		
		private function endExport():void{
			if(operatingWindow!=null)
				PopUpManager.removePopUp(operatingWindow);
		}
		
	}
}