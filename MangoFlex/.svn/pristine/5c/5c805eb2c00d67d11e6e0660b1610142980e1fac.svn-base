package smartx.flex.components.util
{
	import com.adobe.crypto.MD5;
	import com.adobe.utils.StringUtil;
	
	import flash.utils.ByteArray;
	
	import mx.collections.ArrayCollection;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.mxml.RemoteObject;
	
	import smartx.flex.components.vo.GlobalConst;
	import smartx.flex.components.vo.ItemVO;
	import smartx.flex.components.vo.SimpleComboxItemVO;
	import smartx.flex.components.vo.SimpleHashVO;
	import smartx.flex.components.vo.SimpleRefItemVO;
	import smartx.flex.components.vo.TempletItemVO;
	import smartx.flex.components.vo.TempletVO;
	
	public class TempletDataUtil
	{
		private static var templetVOCache:Hashtable = new Hashtable();
		
		public static function findTempletVO(code:String):TempletVO{
			var result:TempletVO = templetVOCache.find(code);
			if(result != null)
				trace("从缓存从读取元原模板[code="+code+"]");
			else
				trace("缓存中没有发现编码为"+code+"的元原模板");
			return result;
		}
		
		public static function putTempletVO(vo:TempletVO):void{
			trace("添加元原模板[code="+vo.templetcode+"]到缓存");
			templetVOCache.add(vo.templetcode,vo);
		}
		
		public static function flushTempletVO(code:String):void{
			templetVOCache.remove(code);
		}
		
		public static function flushAllTempletVO():void{
			templetVOCache.clear();
		}
		/*
			转换simplehashvo为object的arraycollection，以便作为datagrid的dataprovier
		*/
		public static function convertSimpleHashVOArrayToDataProvider(simpleHashVOArray:Array):ArrayCollection{
			var result:ArrayCollection = new ArrayCollection();
			for each(var vo:SimpleHashVO in simpleHashVOArray){
				var temp:Object = vo.dataMap;
				result.addItem(temp);
			}
			return result;
		}
		/*
			替换sql字符串中的‘为''		
		*/
		public static function convertSQLValue(value:String):String{
			if(value == null)
				 return null;
			else 
				return StringUtil.replace(value,"'","''");
		}
		//将abc转换成'abc'，空或""转换成null
		public static function strToSQLValue(value:String):String{
			if(value == null || value == "")
				return "null";
			return "'"+convertSQLValue(value)+"'";
		}
		
		public static function getDeleteSQL(templetVO:TempletVO,dataValue:Object):String{
			var sql:String;
			var saveTableName:String = templetVO.savedtablename;
			sql = "delete from  "+saveTableName+"";
			var pk:String = templetVO.pkname;
			var pkValue:Object = dataValue[pk];
			if(pkValue == null)
				throw new Error("主键数据不能为null");
			sql += " where "+pk+"='"+TempletDataUtil.convertSQLValue(pkValue.toString())+"'";
			return sql;
		}
		
		public static function getUpdateSQL(templetVO:TempletVO,dataValue:Object):String{
			var sql:String;
			var saveTableName:String = templetVO.savedtablename;
			sql = "update "+saveTableName+" set ";
			var count:int = 0;
			for each(var templetItemVO:TempletItemVO in templetVO.itemVos){
				if(templetItemVO.issave){//需要参与保存
					if(templetItemVO.itemkey.toLowerCase() == "version"){
						//version字段直接加1
						sql += "version=NVL(version,0) + 1";
					}
					else{
						var itemValue:Object = dataValue[templetItemVO.itemkey];
						if(itemValue == null)
							sql += templetItemVO.itemkey + "=null";
						else{
							var strValue:String; 
							if(itemValue is ItemVO){
								//是参照框等
								var novaItemValue:ItemVO = itemValue as ItemVO;
								strValue = novaItemValue.toSqlString();
							}
							else
								strValue = TempletDataUtil.convertSQLValue(itemValue.toString());
							if(templetItemVO.savedcolumndatatype.toLowerCase() == "date"){
								if(templetItemVO.itemtype == "日历")
									sql += templetItemVO.itemkey + "=to_date('"+ strValue + "','YYYY-MM-DD')";
								else
									sql += templetItemVO.itemkey + "=to_date('"+ strValue + "','YYYY-MM-DD HH24:MI:SS')";
							}
							else{
								sql += templetItemVO.itemkey + "='"+strValue+"'";
							}
						}
					}
					sql += ",";
					count++;
				}
			}
			if(count == 0)//没有要保存的
				return null;
			//去除最后一个逗号
			sql = sql.substring(0,sql.length-1);
			//拼装where条件
			var pk:String = templetVO.pkname;
			var pkValue:Object = dataValue[pk];
			if(pkValue == null)
				throw new Error("主键数据不能为null");
			sql += " where "+pk+"='"+TempletDataUtil.convertSQLValue(pkValue.toString())+"'";
			return sql;
		}
		
		public static function getInsertSQL(templetVO:TempletVO,dataValue:Object):String{
			var sql:String;
			var saveTableName:String = templetVO.savedtablename;
			sql = "insert into "+saveTableName+" ( ";
			var count:int = 0;
			for each(var templetItemVO:TempletItemVO in templetVO.itemVos){
				if(templetItemVO.issave){//需要参与保存
					sql += templetItemVO.itemkey + ",";
					count++;
				}
			}
			if(count == 0)
				return null;
			//去除最后一个逗号
			sql = sql.substring(0,sql.length-1);
			sql += ") values (";
			for each(var templetItemVO2:TempletItemVO in templetVO.itemVos){
				if(templetItemVO2.issave){//需要参与保存
					if(templetItemVO2.itemkey.toLowerCase() == "version"){
						//version字段直接设1
						sql += "'1',";
					}
					//主键序列应该事先取好
//					else if(templetVO.pkname != null && templetItemVO2.itemkey.toLowerCase() == templetVO.pkname.toLowerCase()){
//						//是主键
//						if(templetVO.pksequencename == null)
//							throw new Error("没有提供用于生成主键的序列名");
//						sql += templetVO.pksequencename+".nextVal,";
//					}
					else{
						var itemValue:Object = dataValue[templetItemVO2.itemkey];
						if(itemValue == null)
							sql += "null,";
						else{
							var strValue:String; 
							if(itemValue is ItemVO){
								//是参照框等
								var novaItemValue:ItemVO = itemValue as ItemVO;
								strValue = novaItemValue.toSqlString();
							}
							else
								strValue = TempletDataUtil.convertSQLValue(itemValue.toString());
							if(templetItemVO2.savedcolumndatatype.toLowerCase() == "date"){
								if(templetItemVO2.itemtype == "日历")
									sql += "to_date('"+ strValue + "','YYYY-MM-DD'),";
								else
									sql +="to_date('"+ strValue + "','YYYY-MM-DD HH24:MI:SS'),";
							}
							else{
								sql += "'"+strValue+"',";
							}
						}
					}
				}
			}
			sql = sql.substring(0,sql.length-1);
			sql += ")";
			return sql;
		}
		
		public static function stringToDate(value:String):Date{
			var a:Array = value.split(" ");
			if(a.length != 2)
				throw new Error("输入格式不对，应为'YYYY-MM-DD HH-mm-SS'");
			var dateStr:String = a[0] as String;
			var timeStr:String = a[1] as String;
			
			var a2:Array = dateStr.split("-");
			if(a2.length!=3)
				throw new Error("输入格式不对，应为'YYYY-MM-DD HH:mm:SS'");
			var year:int = parseInt(a2[0]);
			var month:int = parseInt(a2[1]);
			if(month < 1 || month > 12)
				throw new Error("输入格式不对，应为'YYYY-MM-DD HH:mm:SS'");
			var day:int = parseInt(a2[2]);
			if(day < 1 || day > 31)
				throw new Error("输入格式不对，应为'YYYY-MM-DD HH:mm:SS'");
			
			var a3:Array = timeStr.split(":");
			if(a3.length!=3)
				throw new Error("输入格式不对，应为'YYYY-MM-DD HH:mm:SS'");
			var hour:int = parseInt(a3[0]);
			if(hour < 0 || hour > 23)
				throw new Error("输入格式不对，应为'YYYY-MM-DD HH:mm:SS'");
			var minute:int = parseInt(a3[1]);
			if(minute < 0 || minute > 59)
				throw new Error("输入格式不对，应为'YYYY-MM-DD HH:mm:SS'");
			var second:int = parseInt(a3[2]);
			if(second < 0 || second > 59)
				throw new Error("输入格式不对，应为'YYYY-MM-DD HH:mm:SS'");
			
			return new Date(year,month-1,day,hour,minute,second);
		}
		
		public static function dateStrToDate(dateStr:String):Date{
			var a2:Array = dateStr.split("-");
			if(a2.length!=3)
				throw new Error("输入格式不对，应为'YYYY-MM-DD HH:mm:SS'");
			var year:int = parseInt(a2[0]);
			var month:int = parseInt(a2[1]);
			if(month < 1 || month > 12)
				throw new Error("输入格式不对，应为'YYYY-MM-DD HH:mm:SS'");
			var day:int = parseInt(a2[2]);
			if(day < 1 || day > 31)
				throw new Error("输入格式不对，应为'YYYY-MM-DD HH:mm:SS'");
			
			return new Date(year,month-1,day);
		}
		
		/*
			nova的密码MD5加密方法
		*/
		public static function generatePassword(username:String, password:String):String{
			return MD5Str8(username+password+"WWF");
		}
		
		private static const WWFHexDigits:Array = [
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'h', 'i', 'j', 'k',
        'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'];
		private static const hexDigitsLen:int = WWFHexDigits.length;
		public static function MD5Str8(str:String):String{
			//先进行MD5编码
			MD5.hash(str);
			//获取128位结果
			var mdflex:ByteArray = MD5.digest;
			//这里需要先转换成java计算md5时采用的格式
			//as中b975c10c java中0cc175b9
			var md:Array = new Array(mdflex.length);
			for(var x:int =0; x < 4; x++){
				md[x*4] = mdflex[x*4+3];
				md[x*4+1] = mdflex[x*4+2];
				md[x*4+2] = mdflex[x*4+1];
				md[x*4+3] = mdflex[x*4];
			}
			//将128位结果转换为长度为8的字符串
			var md5_len:int = md.length;
			var result:String ="";
            for (var i:int = 0; i < md5_len; i++) {
                var int0:int = md[i++];
                if (int0 < 0) {
                    int0 += 256;
                }

                var int1:int = md[i];
                if (int1 < 0) {
                    int1 += 256;
                }

                var int2:int = int0 + (int1 << 8);

                result += WWFHexDigits[int2 % hexDigitsLen];
            }

            return result;
		}
		//设置模板数据设置某字段的值，主要为参照型以及下拉框的转换
		public static function setTempletValue(keyname:String,value:Object,dataValue:Object,
			templetVO:TempletVO,setrefvalueCallBack:Function=null,destination:String=null,endpoint:String=null):void{
			if(destination == null)
				destination = GlobalConst.SERVICE_FORM;
			if(value is String){//原来nova填的是公式，字符串用""括起来的，须过滤掉
				var strValue:String = value as String;
				if(strValue.charAt(0) == "\"" && strValue.charAt(strValue.length-1) == "\"")
					value = strValue.substr(1,strValue.length-2);
			}
			for each (var templetItemVO:TempletItemVO in templetVO.itemVos){
				if(templetItemVO.itemkey == keyname){
					if(templetItemVO.itemtype == "下拉框"){
						//下拉框转换成simplecomboxitemvo
						for each(var comboxVO:SimpleComboxItemVO in templetItemVO.comBoxItemVos){
							if(comboxVO.id == value){
								dataValue[keyname] = comboxVO;
								return;
							}
						}
					}
					else if(templetItemVO.itemtype == "参照"){
						var formService:RemoteObject = new RemoteObject(destination);
				        if(endpoint!=null)
				        	formService.endpoint = endpoint;
				        formService.getRefItemVOByValue.addEventListener(ResultEvent.RESULT,function(event:ResultEvent):void
				     	{
				        	var refItemVO:SimpleRefItemVO = event.result as SimpleRefItemVO;
				        	dataValue[keyname] = refItemVO;
				        	if(setrefvalueCallBack!=null)
				        		setrefvalueCallBack.call(this);
				        });
				        formService.getRefItemVOByValue(value,templetItemVO,dataValue);
				        return;
					}
					dataValue[keyname] = value;
					return;
				}
			}
			throw new Error("给出的元原模板中不存在该字段");
		}
		
		public static function getTempletVOCache():Hashtable{
			return templetVOCache;
		}
		
	}
}