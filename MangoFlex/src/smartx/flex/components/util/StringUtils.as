package smartx.flex.components.util
{
	import mx.formatters.DateFormatter;
	import mx.utils.Base64Decoder;
	import mx.utils.Base64Encoder;

	public class StringUtils
	{
		public static function parseUrlParameters(url:String):Object{
			// Remove everything before the question mark, including
			// the question mark.
			var myPattern:RegExp = /.*\?/;  
			var s:String = url;
			s = s.replace(myPattern, "");
			
			// Create an Array of name=value Strings.
			var params:Array = s.split("&");
			
			var result:* = new Object();
			
			// Set the values of the salutation.
			for (var i:int = 0; i < params.length; i++) {
				var temp:Array = params[i].split("=");                        
				result[temp[0]] = temp[1];
			}                    
			return result;
		}
		
		/** 
		 * StringReplaceAll 
		 * @param source:String 源数据 
		 * @param find:String 替换对象 
		 * @param replacement:Sring 替换内容 
		 * @return String 
		 * **/  
		public static function StringReplaceAll( source:String, find:String, replacement:String ):String{  
			return source.split( find ).join( replacement );  
		} 
		
		/**
		 * 把日期转换成字符串,formatterType表示字符串格式
		 * 1, "YYYY-MM-DD JJ:NN:SS"  2,"YYYY-MM-DD"  3,"YYYY-MM-DD JJ:NN:SS:QQQ" 4,"MM/DD/YYYY"
		 * */
		public static function convertDateToString(date:Date, formatterType:int):String {
			var dateStr:String;
			var df:DateFormatter = new DateFormatter();
			
			if(formatterType == 1){
				df.formatString = "YYYY-MM-DD JJ:NN:SS";
			}else if(formatterType == 2){
				df.formatString = "YYYY-MM-DD"
			}else if(formatterType == 3){
				df.formatString = "YYYY-MM-DD JJ:NN:SS:QQQ"
			}else if(formatterType == 4){
				df.formatString = "MM/DD/YYYY"
			}
			
			dateStr = df.format(date);
			return dateStr;
		}
		
		public static function hashCode(str:String):uint{
			var hash:uint = 0;
			for (var i:int = 0; i < str.length; i++) {
				hash = 31*hash + str.charCodeAt(i) ;
			}
			return hash;
		}
		
		/**
		 * Base64加密
		 */ 
		public static function base64Encode($orgin:String):String{                 
			var $base64:Base64Encoder = new Base64Encoder();                 
			$base64.insertNewLines = false;//该值等于true时，输出的结果会自动换行，默认为true，                
			$base64.encodeUTFBytes($orgin);//这里注意，如果想加密中文就不要使用$base64.encode();                 
			var $result:String = $base64.toString();//输出结果                 
			return $result;             
		}
		
		/**
		 * Base64解密
		 */ 
		public static function btnDecode_clickHandler($origi:String):String{                 
			var $base64:Base64Decoder = new Base64Decoder();                 
			$base64.decode($origi);                 
			var $result:String = $base64.toByteArray().toString();//输出结果，decode类只能输出ByteArray类型的数据，因此要转换成string                 
			return $result;             
		}
		/**
		 * 输入字符串为abcd{aa}，然后用dataValue提供的变量进行替换
		 */
		public static function parseVariable(input:String,dataValue:Object):String{
			var variableStr:String = input;
			var lastIndex:int = 0;
			while(variableStr.indexOf("{",lastIndex) > -1 && variableStr.indexOf("}",lastIndex) > -1){	
				var paramName:String = variableStr.substring(variableStr.indexOf("{",lastIndex)+1,variableStr.indexOf("}",lastIndex));
				if(dataValue[paramName] != null){
					variableStr = variableStr.replace("{".concat(paramName).concat("}"),dataValue[paramName]);
				}
				lastIndex = variableStr.indexOf("{",lastIndex);
			}
			return variableStr;
		}
	}
}