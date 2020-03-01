package smartx.bam.flex.modules.entitymodel.utils
{
	import com.adobe.utils.StringUtil;
	
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.mxml.RemoteObject;
	
	import smartx.flex.components.util.SmartXMessage;
	import smartx.flex.components.util.TempletDataUtil;
	import smartx.flex.components.vo.GlobalConst;
	import smartx.flex.components.vo.TempletVO;

	public class EntityTempletUtil
	{
		private var flushTempletCacheService:RemoteObject;
		
		private var generateTempletService:RemoteObject;
		
		private static var instance:EntityTempletUtil;
		
		private var destination:String = GlobalConst.SERVICE_FORM;
		
		public function EntityTempletUtil(){
			if(instance != null)
				throw new Error("不能多次初始化");
		}
		
		public static function getInstance():EntityTempletUtil{
			if(instance == null)
				instance = new EntityTempletUtil();
			return instance;
		}
		
		/**
		 * 刷新领域实体模型中所有实体编辑方案元原模板
		 **/
		public function flushTempletCacheByEntityModelCode(code:String,endpoint:String):void{
			if(flushTempletCacheService == null){
				flushTempletCacheService = new RemoteObject(destination);
				if(endpoint != null)
					flushTempletCacheService.endpoint = endpoint;
				flushTempletCacheService.refreshTempletCacheByEntityModelCode.addEventListener(ResultEvent.RESULT,function(event:ResultEvent):void{
					trace("刷新领域实体["+code+"]编辑元原模板成功！");
				});
				
				flushTempletCacheService.refreshTempletCacheByEntityModelCode.addEventListener(FaultEvent.FAULT,function(event:FaultEvent):void{
					SmartXMessage.show("刷新领域实体["+code+"]编辑元原模板错误！",SmartXMessage.MESSAGE_ERROR,event.fault.faultString);
				});
			}
			flushTempletCacheService.refreshTempletCacheByEntityModelCode(code);
		}
		
		/**
		 * 刷新实体元原模板
		 */ 
		public function flushEntityTempletVO(entityModelcode:String):void{
			for each(var templetCode:String in TempletDataUtil.getTempletVOCache().getKeySet()){
				if(StringUtil.beginsWith(templetCode,"[ENTITY]")){
					var codes:Array = templetCode.split("//");
					if(String(codes[0]).indexOf(entityModelcode) >= 0){
						TempletDataUtil.flushTempletVO(templetCode);
						trace("刷新客户端实体元原模板["+entityModelcode+"]成功!");
						break;
					}
				}
			}
		}
	}
}