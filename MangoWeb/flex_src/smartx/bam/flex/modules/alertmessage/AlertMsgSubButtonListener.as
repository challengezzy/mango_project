package smartx.bam.flex.modules.alertmessage
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
	import smartx.flex.components.util.LoadingWindow;
	import smartx.flex.components.vo.GlobalConst;
	import smartx.flex.components.vo.SimpleComboxItemVO;
	import smartx.flex.components.vo.SimpleRefItemVO;
	
	public class AlertMsgSubButtonListener implements ListButtonListener
	{
		private var panel:BillListPanel = null;
		
		private var operatWindow:IFlexDisplayObject;
		
		private var formService:RemoteObject;
		
		private var endpoint:String = String(ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT));
		
		public function AlertMsgSubButtonListener()
		{
			formService = new RemoteObject(GlobalConst.SERVICE_FORM);
			formService.endpoint = endpoint;
			formService.executeBatchByDS.addEventListener(ResultEvent.RESULT,executeUpdateByDSSHandler);
			formService.executeBatchByDS.addEventListener(FaultEvent.FAULT,faultHandler);
		}
		
		public function buttonClick(listPanel:BillListPanel):void
		{
			panel = listPanel;
			
			var rows:Array = listPanel.getSelectedRowValues()
			
			if(rows == null){
				Alert.show("请先选择一条数据!","提示");
			}else{
				
				var sqls:Array = new Array();
				
				for each(var item:Object in rows){
					
					var affirmVO:SimpleComboxItemVO = item["AFFIRM"] as SimpleComboxItemVO;
					
					var messageId:String = item["ID"] as String;
					
					var update:String= "update bam_alertmessage set status = 1 where id = " +messageId;
					
					sqls.push(update);
					
					if(affirmVO != null && affirmVO.id == '1'){
						
						var alertIdVO:SimpleRefItemVO = item["ALERTID"] as SimpleRefItemVO;
						
						var alertId:String = alertIdVO.id;
						
						var sql:String ="update bam_alert t set t.status=2 where t.id="+alertId;
						
						sqls.push(sql);
						
					}
					
				}
				
				
				startOperat();
				formService.executeBatchByDS(null,sqls);
			}
		}
		
		private function faultHandler(event:FaultEvent):void{
			endOperat();
			Alert.show(event.fault.faultString, 'Error');
		}
		
		private function executeUpdateByDSSHandler(event:ResultEvent):void{
			endOperat();
			Alert.show("消息已确认!", '提示');
			panel.query(false,true,true);
		}
		
		private function startOperat():void{
				if(operatWindow == null){
					var temp:LoadingWindow = new LoadingWindow();
					temp.text = "正在确认……";
					operatWindow = temp;
				}
				PopUpManager.addPopUp(operatWindow,panel,true);
				PopUpManager.centerPopUp(operatWindow);
		}
		
		private function endOperat():void{
			if(operatWindow!=null)
				PopUpManager.removePopUp(operatWindow);
		}
	}
}