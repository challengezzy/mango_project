package smartx.bam.flex.modules.dataanalyze
{
	import smartx.flex.components.core.BillCardPanel;
	import smartx.flex.components.styletemplate.StyleTemplate02;
	import smartx.flex.components.styletemplate.ifc.ClientInterceptorIFC;
	
	public class DataAnalyzeClientInterceptor implements ClientInterceptorIFC
	{
		public function DataAnalyzeClientInterceptor()
		{
		}
		
		public function handler(obj:Object):void
		{
			var billCard:BillCardPanel = obj as BillCardPanel;
			
			var obj:Object = billCard.getDataValue();
			
			var code:String = obj["CODE"];
			
			obj["DASHBOARDCODE"] = "DQ_"+code;
			
			billCard.setDataValue(obj);
			
		}
	}
}