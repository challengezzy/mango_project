package smartx.bam.flex.modules.entitymodel.listener
{
	import smartx.flex.components.core.BillCardPanel;
	import smartx.flex.components.styletemplate.ifc.ClientInterceptorIFC;
	import smartx.flex.components.util.MetadataTempletUtil;
	
	public class DQEmRuleAfInterceptor implements ClientInterceptorIFC
	{
		public function DQEmRuleAfInterceptor()
		{
		}
		
		public function handler(obj:Object):void
		{
			var cardPanel:BillCardPanel = obj as BillCardPanel;
			
			var value:Object = cardPanel.getDataValue();
			
			var mtCode:String = value["MTCODE"] as String;
			
			if(mtCode != null && mtCode != ""){
				MetadataTempletUtil.getInstance().flushMetadataTempletByMtcode(mtCode,cardPanel.endpoint);
			}
		}
	}
}