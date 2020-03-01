package smartx.bam.flex.modules.entitymodel.listener
{
	import smartx.flex.components.core.BillCardPanel;
	import smartx.flex.components.styletemplate.ifc.ClientInterceptorIFC;
	import smartx.flex.components.util.MetadataTempletUtil;
	
	public class EmInsertAfInterceptor implements ClientInterceptorIFC
	{
		public function EmInsertAfInterceptor(){
		}
		
		public function handler(obj:Object):void{
			var blp:BillCardPanel = obj as BillCardPanel;
			var dataValue:Object = blp.getDataValue();
			blp.initQueryCondition = "code = '"+dataValue["CODE"]+"'";
			blp.setDataValueByQuery();
			if(dataValue.MTCODE != null)
				MetadataTempletUtil.getInstance().flushMetadataTempletByMtcode(dataValue.MTCODE,blp.endpoint);
		}
	}
}