package smartx.flex.modules.basic.system.mtdesigner
{
	import mx.controls.Alert;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	import smartx.flex.components.core.BillListPanel;
	import smartx.flex.components.styletemplate.ifc.ListButtonListener;
	import smartx.flex.components.vo.GlobalConst;
	
	public class FlushServerCacheButtonListener implements ListButtonListener
	{
		private var destination:String = GlobalConst.SERVICE_METADATATEMPLET;
		private var globalMetadataTempletService:RemoteObject;
		
		public function FlushServerCacheButtonListener()
		{
			globalMetadataTempletService = new RemoteObject(destination);
			globalMetadataTempletService.resetCache.addEventListener(ResultEvent.RESULT,function(event:ResultEvent):void{
				Alert.show("服务端缓存刷新成功");
			});
			globalMetadataTempletService.resetCache.addEventListener(FaultEvent.FAULT,faultHandler);
		}
		
		public function buttonClick(listPanel:BillListPanel):void
		{
			globalMetadataTempletService.endpoint = listPanel.endpoint;
			globalMetadataTempletService.resetCache();
		}
		
		private function faultHandler(event:FaultEvent):void{   
			Alert.show(event.fault.faultString, 'Error');
		}
	}
}