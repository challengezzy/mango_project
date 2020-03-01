package smartx.flex.modules.basic.system.jobmgmt
{
	import mx.controls.Alert;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.core.BillListPanel;
	import smartx.flex.components.styletemplate.ifc.ListButtonListener;
	import smartx.flex.components.vo.GlobalConst;
	import smartx.flex.components.vo.SimpleComboxItemVO;
	
	public class JobMgmtStartButtonListener implements ListButtonListener
	{
		public var destination:String = GlobalConst.SERVICE_JOB;
		private var endpoint:String  = ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT) as String;
		private var jobService:RemoteObject;
		private var listPanel:BillListPanel;
		
		public function JobMgmtStartButtonListener()
		{
			jobService = new RemoteObject(destination);
			if(endpoint!=null)
				jobService.endpoint = endpoint;
			jobService.jobStart.addEventListener(ResultEvent.RESULT,function(event:ResultEvent):void{
				Alert.show("任务启动成功");
				if(listPanel != null)
					listPanel.query();
			});
			jobService.jobStart.addEventListener(FaultEvent.FAULT,faultHandler);
		}
		
		private function faultHandler(event:FaultEvent):void{   
			Alert.show(event.fault.faultString, 'Error');
		}
		
		public function buttonClick(listPanel:BillListPanel):void
		{
			this.listPanel = listPanel;
			var obj:Object = listPanel.getSelectedRowValue();
			if(obj == null){
				Alert.show("请选择要启动的任务");
				return;
			}
			if(obj["STATUS"] is SimpleComboxItemVO
				&& SimpleComboxItemVO(obj["STATUS"]).id == "1"
			){
				Alert.show("该任务已启动");
				return;
			}
			jobService.jobStart(obj["ID"]);
		}
	}
}