package smartx.flex.modules.basic.system.mtdesigner
{
	import smartx.flex.components.core.BillCardPanel;
	import smartx.flex.components.styletemplate.ifc.ClientInterceptorIFC;

	public class MetadataUpdateBfInterceptor implements ClientInterceptorIFC
	{
		public function MetadataUpdateBfInterceptor()
		{
		}
		
		public function handler(obj:Object):void{
			var bcp:BillCardPanel = obj as BillCardPanel;
			var dataValue:Object = bcp.getDataValue();
			try{
				new XML(dataValue["CONTENT"]);
			}catch(err:Error){
				throw err;
			}
		}
	}
}