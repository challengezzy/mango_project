package smartx.bam.flex.modules.entitymodel.listener
{
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.mxml.RemoteObject;
	
	import smartx.bam.flex.modules.entitymodel.utils.EntityTempletUtil;
	import smartx.flex.components.basic.ClientEnviorment;
	import smartx.flex.components.core.BillCardPanel;
	import smartx.flex.components.styletemplate.ifc.ClientInterceptorIFC;
	import smartx.flex.components.util.MetadataTempletUtil;
	import smartx.flex.components.util.SmartXMessage;
	import smartx.flex.components.util.TempletDataUtil;
	import smartx.flex.components.vo.GlobalConst;
	
	public class DQEmUpdateAfInterceptor implements ClientInterceptorIFC
	{
		public function DQEmUpdateAfInterceptor(){			
		}
		
		public function handler(obj:Object):void{
			var blp:BillCardPanel = obj as BillCardPanel;
			var dataValue:Object = blp.getDataValue();
			if(dataValue.MTCODE != null && dataValue.MTCODE != "")
				MetadataTempletUtil.getInstance().flushMetadataTempletByMtcode(dataValue.MTCODE,blp.endpoint);
		}
	}
}