package smartx.flex.components.basic
{
	import flash.utils.getDefinitionByName;
	
	import mx.collections.ArrayCollection;
	
	import smartx.flex.components.basic.workflow.DefaultTaskHandler;
	import smartx.flex.components.core.workflow.task.DefaultTaskFilter;
	import smartx.flex.components.core.workflow.task.TaskFilter;
	import smartx.flex.components.core.workflow.task.TaskHandler;
	import smartx.flex.components.core.workflow.task.TaskRule;
	import smartx.flex.components.styletemplate.ifc.ClientInterceptorIFC;
	import smartx.flex.components.util.SmartXMessage;
	
	public class ClientEnviorment
	{
		private static var instance:ClientEnviorment = new ClientEnviorment();
		private var varMap:Object = new Object();
		
		public static const KEY_LOGIN_USER_ID:String = "LOGIN_USER_ID";
		public static const KEY_LOGIN_USER_LOGINNAME:String = "LOGIN_USER_LOGINNAME";
		public static const KEY_LOGIN_USER_CODE:String = "LOGIN_USER_CODE";
		public static const KEY_LOGIN_USER_NAME:String = "LOGIN_USER_NAME";
		public static const KEY_LOGIN_INFOVO:String = "LOGIN_INFOVO";
		public static const KEY_LOGIN_USER_MANAGEREGION:String = "LOGIN_USER_MANAGEREGION";
		public static const KEY_LOGIN_USER_MANAGEREGION_STR:String = "LOGIN_USER_MANAGEREGION_STR";
		public static const KEY_LOGIN_USER_DBREADWRITE:String = "LOGIN_USER_DBREADWRITE";
		
		public static const KEY_XML_NOVA2CONFIG:String = "XML_NOVA2CONFIG";
		//public static const KEY_XML_FLEXMENUCONFIG:String = "XML_FLEXMENUCONFIG";
		public static const KEY_XML_MENUCONFIG:String = "XML_MENUCONFIG";
		public static const KEY_XML_WORKFLOWCONFIG:String = "XML_WORKFLOWCONFIG";
		public static const KEY_GLOBAL_DEBUGMODE:String = "GLOBAL_DEBUGMODE";
		public static const KEY_SERVICE_ENDPOINT:String = "SERVICE_ENDPOINT";
		public static const KEY_SERVICE_ENDPOINT_POLLING:String = "SERVICE_ENDPOINT_POLLING";
		public static const KEY_SERVICE_ENDPOINT_STREAMING:String = "SERVICE_ENDPOINT_STEAMING";
		
		public static const KEY_SERVICE_WEBROOT:String = "SERVICE_WEBROOT";
		//add by zhangzz
		public static const DESKTOP_FRAME:String = "DESKTOP_FRAME";
		
		//数据源
		public static const DATASOURCE_DEFAULT:String = "datasource_default";
		public static const DATASOURCE_USERMGMT:String = "datasource_usermgmt";
		
		public  function ClientEnviorment()
		{
		}
		
		public static function getInstance():ClientEnviorment {
			return instance;
		}
		
		public function putVar(key:String,value:Object):void{
			varMap[key] = value;
		}
		
		public function getVar(key:String):Object{
			if(varMap.hasOwnProperty(key)){
				return varMap[key];
			}
			return null;
		}
		
		public function clearClientEnviorment():void{
			varMap = new Object();
		}
		
		public function getNova2ConfigParam(key:String):String{
			var configXML:XML = ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_XML_NOVA2CONFIG) as XML;
		    if(configXML != null){
		    	var value:String = configXML["init-param"].(@key == key).@value;
		    	return value;
		    }
		    return null;
		}
		
		public function geNova2ConfigParamArray():ArrayCollection{
			var configXML:XML = ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_XML_NOVA2CONFIG) as XML;
		    var result:ArrayCollection = new ArrayCollection();
		    if(configXML != null){
		    	for each(var param:XML in configXML["init-param"]){
		    		var item:Object = new Object();
		    		item["key"] = String(param.@key);
		    		item["value"] = String(param.@value);
		    		item["descr"] = String(param.@descr);
		    		result.addItem(item);
		    	}
		    }
		    return result;
		}
		
		public function getNova2ConfigDatasources():ArrayCollection{
			var configXML:XML = ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_XML_NOVA2CONFIG) as XML;
		    var result:ArrayCollection = new ArrayCollection();
		    if(configXML != null){
		    	for each(var datasource:XML in configXML.datasources.datasource){
		    		var novaDatasource:Datasource = new Datasource();
		    		novaDatasource.name = datasource.@name;
		    		novaDatasource.driver = datasource.driver;
		    		novaDatasource.url = datasource.url;
		    		novaDatasource.initsize = datasource.initsize;
		    		novaDatasource.poolsize = datasource.poolsize;
		    		result.addItem(novaDatasource);
		    	}
		    }
		    return result;
		}

		public function getDefaultUserInfo():Object{
			var configXML:XML = ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_XML_NOVA2CONFIG) as XML;
			var result:Object = new Object();
			if(configXML != null){
				var defaultUser:XML = configXML["default-user"][0];
				if(defaultUser != null){
					result["name"] = defaultUser.@name;
					result["pwd"] = defaultUser.@pwd;
					result["adminpwd"] = defaultUser.@adminpwd;
				}
			}
			return result;
		}
		

		public function getMenuConfigItemMap():Object{
			var configXML:XML = ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_XML_MENUCONFIG) as XML;
		    var result:Object = new Object();
		    if(configXML != null){
		    	for each(var param:XML in configXML.menuItemList.menuItem){
		    		var item:Object = new Object();
		    		item["code"] = String(param.@code);
		    		item["icon"] = String(param.@icon);
		    		item["url"] = String(param.@url);
		    		item["toolBox"] = String(param.@toolBox);
		    		item["toolBoxSeq"] = Number(param.@toolBoxSeq);
		    		if(isNaN(item["toolBoxSeq"]))
		    			item["toolBoxSeq"] = Infinity;
		    		result[item["code"]] = item;
		    	}
		    }
		    return result;
		}

		public function getVarMap():Object{
			return varMap;
		}
		
		public function getWFTaskRuleList():ArrayCollection{
			var configXML:XML = ClientEnviorment.getInstance().getVar(ClientEnviorment.KEY_XML_WORKFLOWCONFIG) as XML;
			var result:ArrayCollection = new ArrayCollection();
			if(configXML != null){
				for each(var ruleXML:XML in configXML.taskRuleList.taskRule){
					try{
						var filterList:ArrayCollection = new ArrayCollection();
						for each(var filterXML:XML in ruleXML.taskFilterList.taskFilter){
							try{
								var implClass:String = filterXML.@implClass;
								var properties:Object = new Object();
								for each(var propXML:XML in filterXML.property){
									var name:String = propXML.@name;
									var value:String = propXML.@value;
									properties[name] = value;
								}
								var filter:TaskFilter = null;
								if(implClass != null && implClass != ""){
									var classReference:Class = Class(getDefinitionByName(implClass));
									filter = new classReference(properties);
								}
								else{
									filter = new DefaultTaskFilter(properties);
								}
								filterList.addItem(filter);
							}
							catch(e:Error){
								continue;
							}
							
						}
						var handler:TaskHandler = null;
						var handlerImplClass:String = ruleXML.taskHandler.@implClass;
						if(handlerImplClass != null && handlerImplClass != ""){
							var handlerImplClassReference:Class = Class(getDefinitionByName(handlerImplClass));
							handler = new handlerImplClassReference();
						}
						else
							handler = new DefaultTaskHandler();
						var rule:TaskRule = new TaskRule(filterList,handler);
						result.addItem(rule);
					}
					catch(e:Error){
						continue;
					}
				}
			}
			return result;
		}
		
		public function execInterceptor(incName:String,obj:Object):void{
			try{
				
				if(incName!=null && incName != ""){
					var classReference:Class = getDefinitionByName(incName) as Class;
					var handerClass:ClientInterceptorIFC = new classReference() as ClientInterceptorIFC;
					handerClass.handler(obj);
				}
				
			}catch(e:Error){
				trace(e);
				SmartXMessage.show("执行客户端拦截器"+incName+"失败",SmartXMessage.MESSAGE_ERROR,e.toString());
				throw new Error(e,e);
			}
		}
		
		/**
		 * 解析客户端环境变量参数，原始字符创形式为[abcded{var}xxx....]
		 **/
		public function parseClientVariable(variableStr:String):String{
			
			var lastIndex:int = 0;
			while(variableStr.indexOf("{",lastIndex) > -1 && variableStr.indexOf("}",lastIndex) > -1 ){				
				var params:String = variableStr.substring(variableStr.indexOf("{",lastIndex)+1,variableStr.indexOf("}",lastIndex));
				var clientVal:Object = ClientEnviorment.getInstance().getVar(params);//客户端环境变量
				
				if( clientVal ){
					variableStr = variableStr.replace("{".concat(params).concat("}"),clientVal);
				}else{
					trace("变量{" + params + "}在客户端环境变量中未找到！");
				}
				
				lastIndex = variableStr.indexOf("{",lastIndex);
			}
			return variableStr;
		}
		
		
	}
}