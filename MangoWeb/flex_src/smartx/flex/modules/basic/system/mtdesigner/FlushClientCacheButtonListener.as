package smartx.flex.modules.basic.system.mtdesigner
{
	import mx.controls.Alert;
	
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.core.BillListPanel;
	import smartx.flex.components.event.MetadataTempletUtilEvent;
	import smartx.flex.components.styletemplate.ifc.ListButtonListener;
	import smartx.flex.components.util.MetadataTempletUtil;
	
	public class FlushClientCacheButtonListener implements ListButtonListener
	{
		public function FlushClientCacheButtonListener()
		{
			
		}
		
		public function buttonClick(listPanel:BillListPanel):void
		{
			var util:MetadataTempletUtil = MetadataTempletUtil.getInstance();
			util.addEventListener(MetadataTempletUtilEvent.INIT_GLOBAL_COMPLETE,initGlobalMetadataTempletCacheComplete);
			var endpoint:String = ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT) as String;
			util.initGlobalMetadataTempletCache(endpoint);
		}
		
		private function initGlobalMetadataTempletCacheComplete(event:MetadataTempletUtilEvent):void{
			var util:MetadataTempletUtil = MetadataTempletUtil.getInstance();
			util.removeEventListener(MetadataTempletUtilEvent.INIT_GLOBAL_COMPLETE,initGlobalMetadataTempletCacheComplete);
			Alert.show("客户端缓存刷新成功");
		}
	}
}