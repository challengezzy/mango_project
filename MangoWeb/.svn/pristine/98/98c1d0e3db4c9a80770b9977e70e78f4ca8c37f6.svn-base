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
	
	public class EmUpdateAfInterceptor implements ClientInterceptorIFC
	{
		public function EmUpdateAfInterceptor(){			
		}
		
		public function handler(obj:Object):void{
			var blp:BillCardPanel = obj as BillCardPanel;
			var dataValue:Object = blp.getDataValue();
			MetadataTempletUtil.getInstance().flushMetadataTempletByMtcode(dataValue.MTCODE,blp.endpoint);
			//刷新服务端元原模板缓存
			EntityTempletUtil.getInstance().flushTempletCacheByEntityModelCode(dataValue.CODE,blp.endpoint);
			//刷新客户端元原模板缓存
			EntityTempletUtil.getInstance().flushEntityTempletVO(dataValue.CODE);
		}
	}
}