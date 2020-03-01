package smartx.bam.flex.modules.entitymodel.listener
{
	import smartx.bam.flex.vo.BAMConst;
	import smartx.flex.components.core.BillCardPanel;
	import smartx.flex.components.styletemplate.ifc.ClientInterceptorIFC;
	
	public class DQEmRuleInsertBfInterceptor implements ClientInterceptorIFC
	{
		public function DQEmRuleInsertBfInterceptor()
		{
		}
		
		public function handler(obj:Object):void
		{
			var cardPanel:BillCardPanel = obj as BillCardPanel;
			
			var value:Object = cardPanel.getDataValue();
			
			var code:String = value["CODE"];
			
			var mtCode:String = BAMConst.ENTITY_MT_RULE_PREFEX.concat(code);
			
			value["MTCODE"] = mtCode;
		}
	}
}