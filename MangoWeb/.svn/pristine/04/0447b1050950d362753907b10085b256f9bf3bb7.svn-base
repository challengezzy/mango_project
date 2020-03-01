package smartx.bam.flex.modules.syssetting
{
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	import smartx.bam.flex.vo.BAMConst;
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.core.BillListPanel;
	import smartx.flex.components.styletemplate.ifc.ListButtonListener;
	import smartx.flex.components.util.SmartXMessage;
	
	/**
	 * sky zhangzz
	 **/
	public class RefreshCacheListButtonListener implements ListButtonListener
	{
		
		protected  var remoteObj:RemoteObject;
		
		public var endpoint:String = String(ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT));
		
		public function RefreshCacheListButtonListener()
		{
			remoteObj = new RemoteObject(BAMConst.BAM_Service);
			remoteObj.endpoint = endpoint;
			remoteObj.refreshSysSettingCache.addEventListener(ResultEvent.RESULT,function(event:ResultEvent):void{
				SmartXMessage.show("刷新系统参数成功!");
			});
			remoteObj.refreshSysSettingCache.addEventListener(FaultEvent.FAULT,function(event:FaultEvent):void{
				SmartXMessage.show("刷新系统参数错误!",SmartXMessage.MESSAGE_ERROR,"错误："+event.fault.faultString);
			});
		}
		
		public function buttonClick(listPanel:BillListPanel):void
		{
			remoteObj.refreshSysSettingCache();
		}
	}
}