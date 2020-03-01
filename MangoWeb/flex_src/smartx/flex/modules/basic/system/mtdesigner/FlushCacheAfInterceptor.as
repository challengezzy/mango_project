package smartx.flex.modules.basic.system.mtdesigner
{
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.core.BillCardPanel;
	import smartx.flex.components.styletemplate.ifc.ClientInterceptorIFC;
	import smartx.flex.components.util.MetadataTempletUtil;
	
	public class FlushCacheAfInterceptor implements ClientInterceptorIFC
	{
		private var endpoint:String  = ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_SERVICE_ENDPOINT) as String;
		
		public function FlushCacheAfInterceptor()
		{
		}
		
		public function handler(obj:Object):void
		{
			var cardPanel:BillCardPanel = obj as BillCardPanel;
			
			var value:Object = cardPanel.getDataValue();
			
			var mtCode:String = value["CODE"] as String;
			
			if(mtCode != null && mtCode != ""){
				MetadataTempletUtil.getInstance().flushMetadataTempletByMtcode(mtCode,endpoint);
			}
		}
	}
}