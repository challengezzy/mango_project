package smartx.bam.flex.modules.eventadapter
{
	import flash.utils.getDefinitionByName;
	
	import mx.controls.Alert;
	import mx.core.IFlexDisplayObject;
	import mx.managers.PopUpManager;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	import smartx.bam.flex.vo.BAMConst;
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.core.BillListPanel;
	import smartx.flex.components.styletemplate.ifc.ListButtonListener;
	import smartx.flex.components.util.LoadingWindow;
	
	/**
	 * sky zhangzz
	 **/
	public class StartListButtonListener implements ListButtonListener
	{
		private var remoteObj:RemoteObject;
		private var adaptorClass:String;
		private var billListPanel:BillListPanel;
		private var operatingWindow:IFlexDisplayObject;
		
		public function StartListButtonListener()
		{
			remoteObj = new RemoteObject(BAMConst.BAM_Service);
			remoteObj.endpoint = String(ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT));
			remoteObj.startEventSource.addEventListener(ResultEvent.RESULT,startHandler);
			remoteObj.startEventSource.addEventListener(FaultEvent.FAULT,startFaultHandler);
		}
		
		public function buttonClick(blp:BillListPanel):void
		{
			billListPanel = blp;
			var data:Object = blp.getSelectedRowValue();
			if(data != null){
				if(data["STATUS"].name == "已启动"){
					Alert.show("事件源已启动!","警告");
					return;
				}
				//适配器CLASS
				adaptorClass = data["ADAPTORCLASS"];
				if(adaptorClass == null){
					Alert.show("无适配器实现类！","error");
					return;
				}
				//配置文件
				var config:String = data["CONFIGURATION"];
				//启动事件源
				startOperat();
				remoteObj.startEventSource(adaptorClass,config);
			}else
				Alert.show("请选择一条记录！","warning");
		}
		
		private function startHandler(event:ResultEvent):void{
			endOperat();
			Alert.show("启动事件源["+adaptorClass+"]成功！");
			billListPanel.query();
		}
		
		private function startFaultHandler(event:FaultEvent):void{
			endOperat();
			Alert.show("启动事件源["+adaptorClass+"]失败！\n 错误："+event.fault.faultString, 'Error');
		}
		
		private function startOperat():void{
			
			if(operatingWindow == null){
				var temp:LoadingWindow = new LoadingWindow();
				temp.text = "正在执行……";
				operatingWindow = temp;
			}
			PopUpManager.addPopUp(operatingWindow,billListPanel,true);
			PopUpManager.centerPopUp(operatingWindow);
		}
		
		private function endOperat():void{
			if(operatingWindow!=null)
				PopUpManager.removePopUp(operatingWindow);
		}
	}
}