package smartx.bam.flex.utils
{
	import com.adobe.utils.StringUtil;
	
	import mx.collections.ArrayCollection;
	import mx.utils.ObjectUtil;
	
	import qs.utils.StringUtils;
	
	import smartx.flex.components.util.MetadataTempletUtil;
	import smartx.flex.components.vo.GlobalConst;
	import smartx.flex.components.vo.MetadataTemplet;

	public class BAMUtil
	{
		public function BAMUtil()
		{
		}
		
		public static function isEmpty(str:String):Boolean{
			var flag:Boolean = true;
			if(str != null && StringUtil.trim(str) != ""){
				flag = false;
			}
			return flag;
		}
		
		public static function expression(str:String,params:Object):String{
			var tempStr:String = new String(str);
			var objInfo:Object = ObjectUtil.getClassInfo(params);
			var fieldArr:Array = objInfo["properties"] as Array;
			if(fieldArr != null ){
				for each(var fieldName:String in fieldArr){
					var paramValue:String = String(params[fieldName]);
					if(!BAMUtil.isEmpty(fieldName)&&paramValue!="undefined"){
						tempStr = StringUtils.replaceAll(tempStr,"{"+fieldName+"}",paramValue);
					}
				}
			}
			return tempStr;
		}
		
		public static function isNumber(str:String):Boolean{
			var flag:Boolean = true;
			try{
				var i:int = parseInt(str);
			}catch(error:Error){
				flag = false;
			}
			return false;
		}
		
		/**
		 * 获取新字段名称
		 **/ 
		public static function getNewFieldName(fieldName:String,schemaXML:XMLList):String{
			var i:int=1;
			while(true){
				var name:String = fieldName+i;
				if(isFieldNameExists(name,fieldName,schemaXML)){
					i++;
					continue;
				}
				return name;
			}
			return null;
		}
		
		private static function isFieldNameExists(name:String,fieldName:String,schemaXML:XMLList):Boolean{
			for each(var field:XML in schemaXML){
				if(field[fieldName] == name){
					return true;
				}
			}
			return false;
		}
		
		public static function getSynonymsToArrayCollection(dictionaryCode:String):ArrayCollection{
			var arrCol:ArrayCollection = new ArrayCollection();
			var metadataTemplet:MetadataTemplet= MetadataTempletUtil.getInstance().findMetadataTemplet(GlobalConst.MTCODE_SYNONYMS);
			if(metadataTemplet != null ){
				for each(var synonymsXml:XML in metadataTemplet.contentXML.synonyms){
					var tempCode:String =String(synonymsXml.@code);
					if(tempCode != null && tempCode != "" && tempCode == dictionaryCode){
						for each( var valuesXml:XML in synonymsXml.variable){
							var name:String = String(valuesXml.@name);
							var value:String = String(valuesXml.@value);
							var item:Object = new Object();
							item["name"] = name;
							item["value"] = value;
							arrCol.addItem(item);
						}
					}
				}
			}
			return arrCol;
		}
		
		
	}
}