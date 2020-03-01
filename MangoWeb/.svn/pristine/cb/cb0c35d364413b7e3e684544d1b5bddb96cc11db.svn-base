package smartx.bam.flex.modules.dqc.listener
{
	import mx.collections.ArrayCollection;
	import mx.controls.dataGridClasses.DataGridColumn;
	import mx.managers.PopUpManager;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	import smartx.bam.flex.modules.businessview.BvResultWindow;
	import smartx.bam.flex.vo.DQCConst;
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.basic.DeskTopFrame;
	import smartx.flex.components.core.BillListPanel;
	import smartx.flex.components.styletemplate.ifc.ListButtonListener;
	import smartx.flex.components.util.BillListWindow;
	import smartx.flex.components.util.MetadataTempletUtil;
	import smartx.flex.components.util.SmartXMessage;
	import smartx.flex.components.vo.GlobalConst;
	import smartx.flex.components.vo.MetadataTemplet;
	
	public class ChkDetailListButtonListener implements ListButtonListener
	{
		private var bvResultWindow:BvResultWindow;
		
		private var columns:Array;
		
		private var queryColumns:Array;
		
		private var dataProvider:ArrayCollection;
		
		private var formService:RemoteObject;
		
		public var endpoint:String = String(ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT));
		
		private var deskTopFrame:DeskTopFrame = ClientEnviorment.getInstance().getVar(ClientEnviorment.DESKTOP_FRAME) as DeskTopFrame;
		
		private var billListWindow:BillListWindow;
		
		public function ChkDetailListButtonListener(){
			formService = new RemoteObject();
			formService.endpoint = endpoint;
		}
		
		public function buttonClick(listPanel:BillListPanel):void{
			var data:Object = listPanel.getSelectedRowValue();
			if(data == null){
				SmartXMessage.show("请选择一条记录！",SmartXMessage.MESSAGE_ERROR);
				return;
			}
			var checkItem:Object = data["CHECK_ITEM"];
			var regionId:Object = data["REGIONID"];
			var checkDayStr:Object = data["CHECK_DAY_STR"];
			var templetCode:String;
			var title:String;
			var dqchkItemMt:MetadataTemplet = MetadataTempletUtil.getInstance().findMetadataTemplet(GlobalConst.MTCODE_DQCCHECKITEM);
			var contentXml:XML = dqchkItemMt.contentXML;
			var dataFilter:String;
			var showExcelBtn:String;
			var excelMTCode:String;
			var fileName:String;//下载的文件名
			var excelBtnLabel:String;
			for each(var chkItem:XML in contentXml.checkItem){
				if(checkItem["id"] == String(chkItem.@value) ){
					templetCode = String(chkItem.@templetCode);
					title = String(chkItem.@name);
					dataFilter = String(chkItem.@dataFilter);
					showExcelBtn = String(chkItem.@showExcelBtn);
					excelMTCode = String(chkItem.@excelMTCode);
					fileName = String(chkItem.@fileName);
					excelBtnLabel = String(chkItem.@excelBtnLabel);
				}
			}
			billListWindow = new BillListWindow();
			billListWindow.title = title;
			billListWindow.templetCode = templetCode;
			billListWindow.reportName = fileName;
			billListWindow.excelBtnLabel = excelBtnLabel;
			
			if(dataFilter != null && dataFilter != "")
				billListWindow.initQueryCondition = "CHECK_DAY_STR = '"+String(checkDayStr)+"' and "+dataFilter;
			else
				billListWindow.initQueryCondition = "CHECK_DAY_STR = '"+String(checkDayStr)+"'";
			
			billListWindow.excelMTCode = excelMTCode;
			if("true" == showExcelBtn || "TRUE" == showExcelBtn)
				billListWindow.isShowExcelBtn = true;
			
			var user_region:String = ClientEnviorment.getInstance().getVar("PW_LOGINUSER_REGIONID") as String;
			var queryCondition:String = "";
			if(user_region != null)
				queryCondition = queryCondition + " and REGIONID='" + user_region +"'";
			
			billListWindow.excelInitQueryCondition = queryCondition;
			
			PopUpManager.addPopUp(billListWindow,deskTopFrame,true);
			PopUpManager.centerPopUp(billListWindow);
		}
		
	}
}