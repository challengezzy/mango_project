package smartx.bam.flex.modules.dqc.listener.fttb
{
	import mx.managers.PopUpManager;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	import smartx.bam.flex.modules.businessview.BvResultWindow;
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.basic.DeskTopFrame;
	import smartx.flex.components.core.BillListPanel;
	import smartx.flex.components.styletemplate.ifc.ListButtonListener;
	import smartx.flex.components.util.BillListWindow;
	import smartx.flex.components.util.SmartXMessage;
	import smartx.flex.components.vo.GlobalConst;
	
	public class DetailFTTBMAC4ListBtnListener implements ListButtonListener
	{
		private var formService:RemoteObject;
		
		public var endpoint:String = String(ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT));
		
		private var deskTopFrame:DeskTopFrame = ClientEnviorment.getInstance().getVar(ClientEnviorment.DESKTOP_FRAME) as DeskTopFrame;
		
		private var billListWindow:BillListWindow;
		
		public function DetailFTTBMAC4ListBtnListener(){
			formService = new RemoteObject();
			formService.endpoint = endpoint;
		}
		
		public function buttonClick(listPanel:BillListPanel):void{
			var data:Object = listPanel.getSelectedRowValue();
			if(data == null){
				SmartXMessage.show("请选择一条记录！",SmartXMessage.MESSAGE_ERROR);
				return;
			}
			var checkDayStr:Object = data["COMP_DAY"] as String;
			var templetCode:String = "T_DQC_CMP_DETAIL_PONDEVICE_4";
			var title:String = "FTTB设备MAC比对网管中异常";
			var dataFilter:String = " COMP_DAY = '"+String(checkDayStr)+"' and type=4 and compareitem=7";
			
			billListWindow = new BillListWindow();
			billListWindow.title = title;
			billListWindow.templetCode = templetCode;
			
			billListWindow.initQueryCondition = dataFilter;
			
			PopUpManager.addPopUp(billListWindow,deskTopFrame,true);
			PopUpManager.centerPopUp(billListWindow);
		}
		
	}
}