package smartx.flex.components.util
{
	import flash.events.EventDispatcher;
	import flash.net.SharedObject;
	import flash.net.registerClassAlias;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	import mx.core.Application;
	import mx.core.FlexGlobals;
	import mx.managers.PopUpManager;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	import qs.utils.StringUtils;
	
	import smartx.flex.components.event.BasicEvent;
	import smartx.flex.components.event.MetadataTempletUtilEvent;
	import smartx.flex.components.util.Hashtable;
	import smartx.flex.components.vo.GlobalConst;
	import smartx.flex.components.vo.MetadataTemplet;

	public class MetadataTempletUtil extends EventDispatcher
	{
		public static const STATE_NOTINITED:int = 0;
		public static const STATE_INITING:int = 1;
		public static const STATE_INITED:int = 2;
		
		private var mtInfo:SharedObject;
		private var globalMetadataTempletCache:Hashtable = new Hashtable();
		
		public var globalInitState:int = 0;
		
		private var destination:String = GlobalConst.SERVICE_METADATATEMPLET;
		private var globalMetadataTempletService:RemoteObject;
		
		private var globalCodeList:ArrayCollection;
		private var userCodeList:ArrayCollection;
		
		private var loadingCount:int = 0;
		private var loadedCount:int = 0;
		
		private static var instance:MetadataTempletUtil;
		
		private var mtInfoMap:Hashtable = new Hashtable();//存储所有的元数据编码和version映射关系
		
		public function MetadataTempletUtil(){
			if(instance != null)
				throw new Error("不能多次初始化");
		}
		
		/*public static function getInstance():MetadataTempletUtil{
			if(instance == null){
				instance = new MetadataTempletUtil();
				registerClassAlias("smartx.flex.components.vo.MetadataTemplet",MetadataTemplet);
				instance.mtInfo = SharedObject.getLocal(GlobalConst.KEYNAME_SHAREDOBJECT_MTINFO,"/");
				if(instance.mtInfo.data.mtCache != null){
					for (var code:String in instance.mtInfo.data.mtCache){
						if(instance.mtInfo.data.mtCache[code] is MetadataTemplet){
							instance.globalMetadataTempletCache.add(code,instance.mtInfo.data.mtCache[code]);
						}
					}
					
				}
				else
					instance.globalMetadataTempletCache = instance.mtInfo.data.mtCache = new Hashtable();
			}
			return instance;
		}*/
		
		public static function getInstance():MetadataTempletUtil{
			if(instance == null){
				instance = new MetadataTempletUtil();
				registerClassAlias("smartx.flex.components.vo.MetadataTemplet",MetadataTemplet);
				
				//删除原有的MTINFO
				var formerMtInfo:SharedObject = SharedObject.getLocal(GlobalConst.KEYNAME_SHAREDOBJECT_MTINFO,"/");
				if(formerMtInfo.data.mtCache != null)
					formerMtInfo.clear();
				
				instance.mtInfo = SharedObject.getLocal(GlobalConst.KEYNAME_SHAREDOBJECT_MT_NAMES,"/");
//				if(instance.mtInfo.data[GlobalConst.KEYNAME_SHAREDOBJECT_MT_NAMES] != null){
//					for each(var mtcode:String in instance.mtInfo.data[GlobalConst.KEYNAME_SHAREDOBJECT_MT_NAMES]){
//						var stDate:Date = new Date();
//						trace("开始加载..."+stDate.time);
//						var mtSo:SharedObject = SharedObject.getLocal(StringUtils.base64Encode(mtcode),"/");
//						var endDate:Date = new Date();
//						trace("加载完成..."+endDate.time);
//						trace("时差..."+(endDate.time - stDate.time));
//						if(mtSo.data[mtcode] != null){
//							if(mtSo.data[mtcode] is MetadataTemplet)
//								instance.globalMetadataTempletCache.add(mtcode.replace(GlobalConst.KEYNAME_SHAREDOBJECT_MT_PREFIX,""),mtSo.data[mtcode]);
//						}
//					}
//				}else
//					instance.mtInfoArr = instance.mtInfo.data[GlobalConst.KEYNAME_SHAREDOBJECT_MT_NAMES] = [];
				if(instance.mtInfo.data[GlobalConst.KEYNAME_SHAREDOBJECT_MT_NAMES] == null)
					instance.mtInfoMap = instance.mtInfo.data[GlobalConst.KEYNAME_SHAREDOBJECT_MT_NAMES] = new Hashtable();
				else{
					for(var mtcode:String in instance.mtInfo.data[GlobalConst.KEYNAME_SHAREDOBJECT_MT_NAMES]){
						instance.mtInfoMap.add(mtcode,instance.mtInfo.data[GlobalConst.KEYNAME_SHAREDOBJECT_MT_NAMES][mtcode]);
					}
				}
			}
			return instance;
		}
		
		public function findMetadataTemplet(code:String):MetadataTemplet{
			//先查询缓存中是否有这个元数据，如果没有就去本地共享对象中查找
			if(globalMetadataTempletCache.containsKey(code))
				return globalMetadataTempletCache.find(code);
			else{
				var mtSo:SharedObject = SharedObject.getLocal(encodeMtCode(code),"/");
				if(mtSo.data[code] != null){
					if(mtSo.data[code] is MetadataTemplet){
						globalMetadataTempletCache.add(code,mtSo.data[code]);
						return mtSo.data[code];
					}
				}
			}
			return null;
		}
		
		//add by zhangzz 20110415 start
		private var metadataTempletService:RemoteObject;
		
		//根据元数据模板编码刷新缓存
		public function flushMetadataTempletByMtcode(mtcode:String,endpoint:String=null):void{
			if(mtcode == null || mtcode == "")
				throw new Error("mtcode不能为空!");
			if(metadataTempletService == null){
				metadataTempletService = new RemoteObject(destination);
				if(endpoint!=null)
					metadataTempletService.endpoint = endpoint;
				
				if(globalMetadataTempletCache == null)
					globalMetadataTempletCache = new Hashtable();

				metadataTempletService.findMetadataTempletNoCache.addEventListener(ResultEvent.RESULT,function(event:ResultEvent):void{
					var templet:MetadataTemplet = event.result as MetadataTemplet;
					trace("已读取模板["+templet.code+"]");
//					globalMetadataTempletCache.add(templet.code,templet);
					addMetadataInSharedObject(templet);
					flushCacheByMt(templet);
					trace("模板内容["+templet.contentXML.toXMLString()+"]");
					trace("刷新模板["+templet.code+"]完成");
					dispatchEvent(new MetadataTempletUtilEvent(MetadataTempletUtilEvent.FLUSH_MTCODE_COMPLETE,null,templet.code));
				});
				metadataTempletService.findMetadataTempletNoCache.addEventListener(FaultEvent.FAULT,function(event:FaultEvent):void{
					trace("刷新模板失败!\n 错误:"+event.fault.faultString);
					dispatchEvent(new MetadataTempletUtilEvent(MetadataTempletUtilEvent.FLUSH_MTCODE_FAILED,event.fault.faultString));
				});
			}
			metadataTempletService.findMetadataTempletNoCache(mtcode);
		}
		
		//add by zhangzz 20110415 end
		
		private var metadataTempletService2:RemoteObject;
		
		//add by caohenghui --start  根据MTCODE数据批量更新缓存
		public function flushMetadataTempletByMtCodeArray(codeArray:Array,endpoint:String=null):void{
			if(codeArray == null || codeArray.length<0){
				dispatchEvent(new MetadataTempletUtilEvent(MetadataTempletUtilEvent.FLUSH_MTCODE_COMPLETE));
				return;
			}
			if(metadataTempletService2 == null){
				metadataTempletService2 = new RemoteObject(destination);
				if(endpoint!=null)
					metadataTempletService2.endpoint = endpoint;
				
				if(globalMetadataTempletCache == null)
					globalMetadataTempletCache = new Hashtable();
				
				metadataTempletService2.findMetadataTempletByCodeArray.addEventListener(ResultEvent.RESULT,function(event:ResultEvent):void{
					
					var templetArr:Array = event.result as Array;
					if(templetArr != null){
						for each(var templet:MetadataTemplet in templetArr){
//							globalMetadataTempletCache.add(templet.code,templet);
							addMetadataInSharedObject(templet);
						}
						flushCacheByMt(templet);
					}
					dispatchEvent(new MetadataTempletUtilEvent(MetadataTempletUtilEvent.FLUSH_MTCODE_COMPLETE));
				});
				metadataTempletService2.findMetadataTempletByCodeArray.addEventListener(FaultEvent.FAULT,function(event:FaultEvent):void{
					dispatchEvent(new MetadataTempletUtilEvent(MetadataTempletUtilEvent.FLUSH_MTCODE_FAILED,event.fault.faultString));
				});
			}
			
			metadataTempletService2.findMetadataTempletByCodeArray(codeArray);
			
		}
		//add by caohenghui --end
		
		public function initUserMetadataTempletCache(username:String,endpoint:String=null):void{
			if(globalMetadataTempletCache == null){
				globalMetadataTempletCache = new Hashtable();
			}
			
			globalMetadataTempletService = new RemoteObject(destination);
			if(endpoint!=null){
				globalMetadataTempletService.endpoint = endpoint;
			}
			
			globalMetadataTempletService.getUserMetadataTempletCodeList.addEventListener(ResultEvent.RESULT,function(event:ResultEvent):void{
				userCodeList = event.result as ArrayCollection;
				for each(var code:String in userCodeList){
					trace("开始读取模板["+code+"]");
					globalMetadataTempletService.findMetadataTemplet(code);
				}
			});
			
			globalMetadataTempletService.getUserMetadataTempletCodeList.addEventListener(FaultEvent.FAULT,faultUserHandler);
			
			globalMetadataTempletService.findMetadataTemplet.addEventListener(ResultEvent.RESULT,function(event:ResultEvent):void{
				var templet:MetadataTemplet = event.result as MetadataTemplet;
				trace("已读取模板["+templet.code+"]");
//				globalMetadataTempletCache.add(templet.code,templet);
				addMetadataInSharedObject(templet);
				if(globalMetadataTempletCache.size >= userCodeList.length){
					trace("用户元数据模板初始化完毕");
					dispatchEvent(new MetadataTempletUtilEvent(MetadataTempletUtilEvent.INIT_USER_COMPLETE));
				}
				
			});
			
			globalMetadataTempletService.findMetadataTemplet.addEventListener(FaultEvent.FAULT,faultUserHandler);
			
			globalMetadataTempletService.getUserMetadataTempletCodeList(username);
		}
		
		private function faultUserHandler(event:FaultEvent):void{
			dispatchEvent(new MetadataTempletUtilEvent(MetadataTempletUtilEvent.INIT_USER_FAILED,event.fault.faultString));
		}
		
		public function initGlobalMetadataTempletCache(endpoint:String=null):void{
			if(globalInitState == STATE_INITING)
				throw new Error("正在初始化，不能另起一个初始化过程");
			globalInitState = STATE_INITING;
			//globalMetadataTempletCache = new Hashtable();
			globalMetadataTempletService = new RemoteObject(destination);
			if(endpoint!=null)
				globalMetadataTempletService.endpoint = endpoint;
			
			globalMetadataTempletService.getGlobalMetadataTempletCodeAndVersionCodeList.addEventListener(ResultEvent.RESULT,function(event:ResultEvent):void{
				globalCodeList = event.result as ArrayCollection;
				if(globalCodeList == null || globalCodeList.length ==0 ){
					var fup:FileUploadPanel = new FileUploadPanel();
					
					fup.addEventListener(BasicEvent.LOADDATA_SUCCESSFUL,uploadSuccessfulHandler);
					fup.datasource = null;
					fup.flagName ="meta";
					fup.title = "没有初始化元数据,需要先导入元数据!";
					
					PopUpManager.addPopUp(fup,FlexGlobals.topLevelApplication as Application);
					PopUpManager.centerPopUp(fup);
					
				}else{
					loadingCount = 0;
					loadedCount = 0;
					for each(var str:String in globalCodeList){
						var strArray:Array = str.split("@@");
						if(strArray.length != 2)
							continue;
						var code:String = strArray[0];
						var versionCode:String = strArray[1];
//						var templet:MetadataTemplet = findMetadataTemplet(code);
//						if(templet != null && templet.versionCode == versionCode)
//							continue;
						if(mtInfoMap.containsKey(code) && mtInfoMap.find(code) == versionCode)
							continue;
						trace("开始读取模板["+code+"]");
						globalMetadataTempletService.findMetadataTemplet(code);
						loadingCount++;
					}
					if(loadingCount == 0){
						trace("无需更新元数据模板");
						globalInitState = STATE_INITED;
						dispatchEvent(new MetadataTempletUtilEvent(MetadataTempletUtilEvent.INIT_GLOBAL_COMPLETE));
					}
				}

			});
			globalMetadataTempletService.getGlobalMetadataTempletCodeAndVersionCodeList.addEventListener(FaultEvent.FAULT,faultHandler);
			
			globalMetadataTempletService.findMetadataTemplet.addEventListener(ResultEvent.RESULT,function(event:ResultEvent):void{
				var templet:MetadataTemplet = event.result as MetadataTemplet;
				loadedCount++;
				trace("已读取模板["+templet.code+"]");
//				globalMetadataTempletCache.add(templet.code,templet);
				addMetadataInSharedObject(templet);
				if(loadedCount >= loadingCount){
					trace("全局元数据模板初始化完毕");
					globalInitState = STATE_INITED;
					flushCache();
					dispatchEvent(new MetadataTempletUtilEvent(MetadataTempletUtilEvent.INIT_GLOBAL_COMPLETE));
				}
				
			});
			globalMetadataTempletService.findMetadataTemplet.addEventListener(FaultEvent.FAULT,faultHandler);
			
			globalMetadataTempletService.getGlobalMetadataTempletCodeAndVersionCodeList();
		}
		
		/**
		 * 将元数据模板加入SHAREDOBJECT
		 */ 
		private function addMetadataInSharedObject(templet:MetadataTemplet):void{
			globalMetadataTempletCache.add(templet.code,templet);
			var mtSo:SharedObject = SharedObject.getLocal(encodeMtCode(templet.code),"/");
			mtSo.data[templet.code] = templet;
			mtInfoMap.add(templet.code,templet.versionCode);
		}
		
//		private function flushCache():void{
//			mtInfo.data.mtCache = new Object();
//			for each (var mt:MetadataTemplet in globalMetadataTempletCache.toArray()){
//				mtInfo.data.mtCache[mt.code] = mt;
//			}
//			mtInfo.flush();
//		}
		
		private function flushCache():void{
			mtInfo.data[GlobalConst.KEYNAME_SHAREDOBJECT_MT_NAMES] = {};
			for each (var key:String in mtInfoMap.getKeySet()){
				mtInfo.data[GlobalConst.KEYNAME_SHAREDOBJECT_MT_NAMES][key] = mtInfoMap.find(key);
			}
			mtInfo.flush();
		}
		
		private function encodeMtCode(code:String):String{
			return qs.utils.StringUtils.replaceAll(smartx.flex.components.util.StringUtils.base64Encode(code),"/","");
		}
		
		private function flushCacheByMt(mt:MetadataTemplet):void{
			var mtSo:SharedObject = SharedObject.getLocal(encodeMtCode(mt.code),"/");
			mtSo.flush();
		}
			
		private function uploadSuccessfulHandler(event:BasicEvent):void{
			globalInitState = STATE_NOTINITED;
			initGlobalMetadataTempletCache(globalMetadataTempletService.endpoint);
		}
		
		private function faultHandler(event:FaultEvent):void{   
			globalInitState = STATE_NOTINITED;
			dispatchEvent(new MetadataTempletUtilEvent(MetadataTempletUtilEvent.INIT_GLOBAL_FAILED,event.fault.faultString));
		}
	}
}