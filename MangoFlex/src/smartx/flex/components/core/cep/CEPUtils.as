package smartx.flex.components.core.cep
{
	
	import flash.geom.Point;
	
	import mx.collections.ArrayCollection;
	
	import smartx.flex.components.util.Hashtable;
	
	import smartx.flex.components.util.flowui.FlowIcon;

	public class CEPUtils
	{
		/**
		 * 产生一个指定范围的随机数
		 */
		public static function random(min: int, max: int): int{
			return int((max - min) * Math.random() + 1 + min);
		}
		
		/**
		 * 	生成一个随机字符串,长度由参数指定
		 */
		public static function randomString(n: int): String{
			var i: int = 0;
			var str: String = "";
			while(i < n){
				str += random(0, 9);
				i ++;
			}
			return str;
		}
		
		public static function getNextStreamAppCode():String{
			var prefix:String = "SA";
			var now:Date = new Date();
			return prefix+now.fullYear+"-"+(now.month+1)+"-"+now.date+"_"+randomString(4);
		}
		
		//获取下一个stream name
		public static function getNextStreamName(xml:XML):String{
			var i:int=0;
			var prefix:String = "SI";
			while(i < Infinity){
				var newName:String = (prefix+i);
				var exists:Boolean = false;
				for each(var t:XML in xml.streams.stream){
					if(t.@name == newName){
						exists = true;
						break;
					}
				}
				if(exists){
					i++;
					continue;
				}	
				else
					return newName;
			}
			return prefix+randomString(4);//不可能走到这里
		}
		
		//获取下一个stream name
		public static function getNextStreamCode(xml:XML):String{
			var i:int=0;
			var prefix:String = "SI";
			while(i < Infinity){
				var newCode:String = (prefix+i);
				var exists:Boolean = false;
				for each(var t:XML in xml.streams.stream){
					if(t.@code == newCode){
						exists = true;
						break;
					}
				}
				if(exists){
					i++;
					continue;
				}	
				else
					return newCode;
			}
			return prefix+randomString(4);//不可能走到这里
		}
		
		public static function xmlDeleteNode(xmlToDelete:XML):Boolean
		{
			var cn:XMLList = XMLList(xmlToDelete.parent()).children();
			
			for ( var i:Number = 0 ; i < cn.length() ; i++ )
			{
				if ( cn[i] == xmlToDelete ) 
				{
					delete cn[i];      
					return true;
				}
			}    
			
			return false;
			
		}
		
		public static function getArrowLineAxis(fromIcon:FlowIcon, toIcon:FlowIcon):Array{
			var fromx:int;
			var fromy:int;
			var tox:int;
			var toy:int;
			if(fromIcon.x + fromIcon.iconWidth <= toIcon.x){
				fromx = fromIcon.x + fromIcon.iconWidth;
				tox = toIcon.x;
			}
			else if(fromIcon.x >= toIcon.x + toIcon.iconWidth){
				fromx = fromIcon.x;
				tox = toIcon.x + toIcon.iconWidth;
			}
			else{
				fromx = fromIcon.x + fromIcon.iconWidth/2;
				tox = toIcon.x + toIcon.iconWidth/2;
			}
			
			if(fromIcon.y + fromIcon.iconHeight <= toIcon.y){
				fromy = fromIcon.y + fromIcon.iconHeight;
				toy = toIcon.y;
			}
			else if(fromIcon.y >= toIcon.y + toIcon.iconHeight){
				fromy = fromIcon.y;
				toy = toIcon.y + toIcon.iconHeight;
			}
			else{
				fromy = fromIcon.y + fromIcon.iconHeight/2;
				toy = toIcon.y + toIcon.iconHeight/2;
			}
			var result:Array = new Array(2);
			result[0] = new Point(fromx,fromy);
			result[1] = new Point(tox,toy);
			return result;
		}
		
		/*
			对事件流进行拓扑排序
		*/
		public static function topoSortStreamInstance(streamInstanceMap:Object):ArrayCollection{
			var result:ArrayCollection = new ArrayCollection();
			var streamInstanceCount:int = getObjectLength(streamInstanceMap);
			while(result.length < streamInstanceCount){
				if(!handleNoInputStreamInstances(result,streamInstanceMap)){
					if(result.length < streamInstanceCount){
						//存在循环
						throw new Error("编译错误：存在循环流");
					}
				}	
			}
			return result;
		}
		/*
			将没有未处理输入流的事件流加入已处理行列，返回值为本轮是否找到了待处理的事件流
		*/
		private static function handleNoInputStreamInstances(handledList:ArrayCollection,streamInstanceMap:Object):Boolean{
			var flag:Boolean = false;
			for(var name:String in streamInstanceMap){
				var streamInstance:StreamInstance = streamInstanceMap[name];
				if(handledList.contains(streamInstance))
					continue;
				var canBeHandled:Boolean = true;
				for each(var inputStreamInstance:StreamInstance in streamInstance.inputStreamList){
					if(!handledList.contains(inputStreamInstance)){
						canBeHandled = false;
						break;
					}
				}
				if(canBeHandled){
					handledList.addItem(streamInstance);
					flag = true;
				}
			}
			return flag;
		}
		
		public static function getObjectLength(o:Object):int
		{
			var len:int = 0;
			for (var item:* in o)
				if (item != "mx_internal_uid")
					len++;
			return len;
		}
		/*
			将编译事件流对象编译成EPL
		*/
		public static function compileStreamInstanceMapToEPL(streamInstanceMap:Object):Hashtable{
			var result:Hashtable = new Hashtable();
			var list:ArrayCollection = topoSortStreamInstance(streamInstanceMap);
			for each(var streamInstance:StreamInstance in list){
				var siEpl:String = streamInstance.compile();
				result.add(streamInstance.code, siEpl);
			}
			return result;
		}
		
		public static function compileStreamAppXMLToStreamInstanceMap(currentStreamAppXML:XML):Object{
			var streamInstanceMap:Object = new Object();
			for each(var stream:XML in currentStreamAppXML.streams.stream){
				var streamCode:String = stream.@code;
				if(streamInstanceMap[streamCode]==null){
					createStreamInstance(stream,streamInstanceMap,currentStreamAppXML);
				}
			}
			return streamInstanceMap;
		}
		
		private static function createStreamInstance(streamXML:XML,streamInstanceMap:Object,currentStreamAppXML:XML):StreamInstance{
			var inputStreamList:ArrayCollection = new ArrayCollection();
			for each(var inputStreamXML:XML in streamXML.inputStreams.stream){
				var inputStreamCode:String = inputStreamXML.@code;
				if(streamInstanceMap[inputStreamCode] != null){
					inputStreamList.addItem(streamInstanceMap[inputStreamCode]);
				}
				else{
					for each(var a:XML in currentStreamAppXML.streams.stream){
						var streamCode:String = a.@code;
						if(streamCode == inputStreamCode){
							var inputStreamInstance:StreamInstance = createStreamInstance(a,streamInstanceMap,currentStreamAppXML);
							inputStreamList.addItem(inputStreamInstance);
							break;
						}
					}
				}
			}
			var streamInstance:StreamInstance = new StreamInstance(streamXML,inputStreamList);
			//进行编译"类型检查"
			var result:ValidateResult = streamInstance.validate();
			if(result.isSuccessful == false){
				var errorInfo:String = result.errorInfo;
				throw new Error("["+streamInstance.name+"]: "+errorInfo);
			}
			streamInstanceMap[streamInstance.code] = streamInstance;
			return streamInstance;
		}
		
		public static function getEPLForCreateNamedWindow(instance:StreamInstance):String{
			var epl:String = "create window "+instance.windowName+".win";
			if(instance.windowType == "time")
				epl += ":time("+instance.windowSize+")";
			else
				epl += ":length("+instance.windowSize+")";
			epl += " as select * from "+instance.name+";\n";
			epl += "insert into "+instance.windowName+" select * from "+instance.name+";\n";
			return epl;
		}
		
		public static function getStreamInstanceModuleName(instance:StreamInstance):String{
			return "module_"+instance.name;
		}
		
		public static function getModuleEPLs(instance:StreamInstance):String{
			var result:String = "module "+getStreamInstanceModuleName(instance)+";\n";
			for each(var inputInstance:StreamInstance in instance.inputStreamList){
				var moduleName:String = getStreamInstanceModuleName(inputInstance);
				result += "uses "+moduleName+";\n";
			}
			return result;
		}
	}
	
	
}