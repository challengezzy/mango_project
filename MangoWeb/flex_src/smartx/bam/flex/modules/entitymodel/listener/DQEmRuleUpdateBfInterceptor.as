package smartx.bam.flex.modules.entitymodel.listener
{
	import smartx.bam.flex.vo.BAMConst;
	import smartx.flex.components.core.BillCardPanel;
	import smartx.flex.components.styletemplate.ifc.ClientInterceptorIFC;
	
	public class DQEmRuleUpdateBfInterceptor implements ClientInterceptorIFC
	{
		public function DQEmRuleUpdateBfInterceptor()
		{
		}
		
		public function handler(obj:Object):void
		{
			var cardPanel:BillCardPanel = obj as BillCardPanel;
			
			var value:Object = cardPanel.getDataValue();
			
			var mtCode:String = value["MTCODE"];
			
			if(mtCode == null || mtCode==""){
				var code:String = value["CODE"];
				
				mtCode = BAMConst.ENTITY_MT_RULE_PREFEX.concat(code);
				
				value["MTCODE"] = mtCode;
			}
		}
	}
}