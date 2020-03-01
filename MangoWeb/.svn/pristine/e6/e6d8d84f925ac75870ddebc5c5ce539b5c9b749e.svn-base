package smartx.flex.modules.datatask
{
	import mx.controls.Alert;
	import mx.core.IFlexDisplayObject;
	import mx.managers.PopUpManager;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.basic.DeskTopFrame;
	import smartx.flex.components.core.BillListPanel;
	import smartx.flex.components.styletemplate.ifc.ListButtonListener;
	import smartx.flex.components.util.OperatingTipUtil;
	import smartx.flex.components.util.SmartXMessage;
	import smartx.flex.components.util.TextAreaWindow;
	import smartx.flex.components.vo.GlobalConst;
	import smartx.flex.components.vo.SimpleHashVO;
	
	public class ViewTaskExecLogListener implements ListButtonListener
	{
		private var destination:String = GlobalConst.SERVICE_FORM;
		private var formService:RemoteObject;
		private var endpoint:String  = ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT) as String;
		private var listPanel:BillListPanel;
		private var desktop:DeskTopFrame = ClientEnviorment.getInstance().getVar(ClientEnviorment.DESKTOP_FRAME) as DeskTopFrame;
		
		private var querySql:String;
		private var execLog:String = "";
		
		public function ViewTaskExecLogListener()
		{
			formService = new RemoteObject(destination);
			if(endpoint != null)
				formService.endpoint = endpoint;
			
			formService.getSimpleHashVoArrayByDS.addEventListener(ResultEvent.RESULT,getSimpleHashVoArrayByDSHandler);
			formService.getSimpleHashVoArrayByDS.addEventListener(FaultEvent.FAULT,faultHandler);
			
		}
		
		public function buttonClick(listPanel:BillListPanel):void
		{
			this.listPanel = listPanel;
			if(listPanel.getSelectedRowValues().length != 1){
				SmartXMessage.show("请选择一个任务实例查看！");
				return;
			}
			var selectObj:Object = listPanel.getSelectedRowValue();
			var datataskId:String = selectObj["ID"] as String;
	
			querySql = "select id,execlog from pub_datatask t where t.id =  "+ datataskId;
			formService.getSimpleHashVoArrayByDS(null,querySql);
		}
		
		private function faultHandler(event:FaultEvent):void{
			OperatingTipUtil.endOperat();
			SmartXMessage.show("获取任务日志信息失败！");
		}
		
		private function getSimpleHashVoArrayByDSHandler(event:ResultEvent):void{
			OperatingTipUtil.endOperat();;
			
			var simpleVoArr:Array = event.result as Array;
			
			if( simpleVoArr.length > 0){
				var logObj:SimpleHashVO = simpleVoArr[0];
				execLog = logObj.dataMap["execlog"];
			}else{
				SmartXMessage.show("末找对对应的任务执行日志！");
				return;
			}
			
			var window:TextAreaWindow  = new TextAreaWindow();
			window.text = execLog;
			window.width = 700;
			window.title = "数据任务执行日志";
			
			PopUpManager.addPopUp(window,desktop,true);
			PopUpManager.centerPopUp(window);
		}
		
	}
}