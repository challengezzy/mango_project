package smartx.bam.flex.modules.dataanalyze
{
	import smartx.flex.components.core.BillCardPanel;
	import smartx.flex.components.styletemplate.ifc.ClientInterceptorIFC;
	
	public class DboInsertClientInterceptor implements ClientInterceptorIFC
	{
		public function DboInsertClientInterceptor()
		{
		}
		
		public function handler(obj:Object):void
		{
			var billCard:BillCardPanel = obj as BillCardPanel;
			
			var obj:Object = billCard.getDataValue();
			
			var code:String = obj["CODE"];
			
			obj["MTCODE"] = "MT_DO_"+code;
			
			billCard.setDataValue(obj);
		}
	}
}