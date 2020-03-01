/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


字符串处理工具类。


*/

package hlib
{
	import flash.utils.ByteArray;
	
	import mx.utils.Base64Decoder;
	import mx.utils.Base64Encoder;

	public final class StringUtil
	{
		private static const ALPHA_CHAR_CODES:Array = [48, 49, 50, 51, 52, 53, 54, 
			55, 56, 57, 65, 66, 67, 68, 69, 70];
		/**
		 * 生成GUID
		 */ 
		public static function NewGuid():String
		{
			return createUID();
		}
		/**
		 *  Generates a UID (unique identifier) based on ActionScript's
		 *  pseudo-random number generator and the current time.
		 *
		 *  <p>The UID has the form
		 *  <code>"XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX"</code>
		 *  where X is a hexadecimal digit (0-9, A-F).</p>
		 *
		 *  <p>This UID will not be truly globally unique; but it is the best
		 *  we can do without player support for UID generation.</p>
		 *
		 *  @return The newly-generated UID.
		 */
		private static function createUID():String
		{
			var uid:Array = new Array(36);
			var index:int = 0;
			
			var i:int;
			var j:int;
			
			for (i = 0; i < 8; i++)
			{
				uid[index++] = ALPHA_CHAR_CODES[Math.floor(Math.random() *  16)];
			}
			
			for (i = 0; i < 3; i++)
			{
				uid[index++] = 45; // charCode for "-"
				
				for (j = 0; j < 4; j++)
				{
					uid[index++] = ALPHA_CHAR_CODES[Math.floor(Math.random() *  16)];
				}
			}
			
			uid[index++] = 45; // charCode for "-"
			
			var time:Number = new Date().getTime();
			// Note: time is the number of milliseconds since 1970,
			// which is currently more than one trillion.
			// We use the low 8 hex digits of this number in the UID.
			// Just in case the system clock has been reset to
			// Jan 1-4, 1970 (in which case this number could have only
			// 1-7 hex digits), we pad on the left with 7 zeros
			// before taking the low digits.
			var timeString:String = ("0000000" + time.toString(16).toUpperCase()).substr(-8);
			
			for (i = 0; i < 8; i++)
			{
				uid[index++] = timeString.charCodeAt(i);
			}
			
			for (i = 0; i < 4; i++)
			{
				uid[index++] = ALPHA_CHAR_CODES[Math.floor(Math.random() *  16)];
			}
			return String.fromCharCode.apply(null, uid);
		}

		/**
		 * 生成日期ID, YYYY-MM-dd-HH-mm-ss
		 */ 
		public static function NewDateId():String
		{
			var time:Date = new Date();
			var year:int = int(time.getFullYear());
			var month:int = int((time.getMonth() + 1));
			var day:int = int(time.getDate());
			var hour:int = int(time.getHours());
			var minute:int = int(time.getMinutes());
			var second:int = int(time.getSeconds());
			
			var name:String = year + "-";
			if (month < 10)
				name += "0" + month;
			else
				name += month;
			name += "-";
			if (day < 10)
				name += "0" + day;
			else
				name += day;
			name += "-";
			if (hour < 10)
				name += "0" + hour;
			else
				name += hour;
			name += "-";
			if (minute < 10)
				name += "0" + minute;
			else
				name += minute;
			name += "-";
			if (second < 10)
				name += "0" + second;
			else
				name += second;
			return name;
		}
		public static function Contains(src:String, tokens:Array):Boolean
		{
			if(!src)
				return false;
			for(var i:int=0; i<src.length; i++)
			{
				var char:String = src.charAt(i);
				if(tokens.indexOf(char)>=0)
					return true;
			}
			return false;
		}
		
		/**
		 * 是否包含XML关键字符< ' " & >
		 */ 
		public static function HasXmlChar(value:String):Boolean
		{
			if(!value)
				return false;
			for(var i:int=0; i<value.length; i++)
			{
				var char:String = value.charAt(i);
				switch(char)
				{
					case "<":
					case ">":
					case "&":
					case "\"":
					case "\'":
						return true;
				}
			}
			return false;
		}
		
		
		
		/**
		 * 替换< ' " & >字符
		 */
		public static function EscapeXML(value:String, check:Boolean = true):String
		{
			if (check && !HasXmlChar(value))
			{
				return value;
			}
			return value.replace(/&/g,"&amp;").replace(/</g, "&lt;")
				.replace(/>/g, "&gt;").replace(/\'/g,"&apos;").replace(/\"/g,"&quot;");
		}
		/**
		 * 替换<  " & >字符
		 */
		public static function EscapeXMLIgnoreApos(value:String, check:Boolean = true):String
		{
			if (check && !HasXmlChar(value))
			{
				return value;
			}
			return value.replace(/&/g,"&amp;").replace(/</g, "&lt;")
				.replace(/>/g, "&gt;").replace(/\"/g,"&quot;");
		}
		
		public static function UnescapeXML(value:String):String
		{
			return value.replace(/&amp;/g,"&").replace(/&lt;/g, "<")
				.replace(/&gt;/g, ">").replace(/&apos;/g,"\'").replace(/&quot;/g,"\"");
		}
		/**
		 * 是否包含CSV关键字符",
		 */
		public static function HasCsvChar(value:String):Boolean
		{
			if(!value)
				return false;
			for(var i:int=0; i<value.length; i++)
			{
				var char:String = value.charAt(i);
				switch(char)
				{
					case "\"":
					case ",":
						return true;
				}
			}
			return false;
		}
		/**
		 * 替换"字符
		 */
		public static function EscapeCSV(value:String, check:Boolean = true):String
		{
			if (check && !HasCsvChar(value))
			{
				return value;
			}
			return value.replace(/\"/g,"\"\"");
		}
		/**
		 * 格式化字符串
		 */ 
		public static function Format(str:String, ... rest):String
		{
			if (!str) return "";
			
			// Replace all of the parameters in the msg string.
			var len:uint = rest.length;
			var args:Array;
			if (len == 1 && rest[0] is Array)
			{
				args = rest[0] as Array;
				len = args.length;
			}
			else
			{
				args = rest;
			}
			
			for (var i:int = 0; i < len; i++)
			{
				str = str.replace(new RegExp("\\{"+i+"\\}", "g"), args[i]);
			}
			return str;
		}
		/**
		 * 替换字符串
		 */ 
		public static function Replace(value:String, oldStr:String, newStr:String):String
		{
			var startIndex:int = 0;
			var result:String = value;
			var begin:int = result.indexOf(oldStr, startIndex);
			while (begin >= 0)
			{
				result = result.substring(0, begin) + newStr + result.substr(begin + oldStr.length);
				startIndex = begin + newStr.length;
				begin = result.indexOf(oldStr, startIndex);
			}
			return result;
		}
		/**
		 * 忽略大小写比较字符串
		 */ 
		public static function IgnoreCaseCompare(str1:String, str2:String):Boolean
		{
			if (str1 == str2)
				return true;
			if (str1 == null || str2 == null)
				return false;
			return str1.toLowerCase() == str2.toLowerCase();
		}
		/**
		 * 去除字符串前后空格
		 */ 
		public static function Trim(str:String):String
		{
			if (str == null) return '';
			
			var startIndex:int = 0;
			while (IsWhitespace(str.charAt(startIndex)))
				++startIndex;
			
			var endIndex:int = str.length - 1;
			while (IsWhitespace(str.charAt(endIndex)))
				--endIndex;
			
			if (endIndex >= startIndex)
				return str.slice(startIndex, endIndex + 1);
			else
				return "";
		}
		/**
		 * 去除字符串前空格
		 */ 
		public static function TrimStart(str:String):String
		{
			if (str == null) return '';
			
			var startIndex:int = 0;
			while (IsWhitespace(str.charAt(startIndex)))
				++startIndex;
			
			var endIndex:int = str.length - 1;
			if (endIndex >= startIndex)
				return str.slice(startIndex, endIndex + 1);
			else
				return "";
		}
		/**
		 * 去除字符串后空格
		 */ 
		public static function TrimEnd(str:String):String
		{
			if (str == null) return '';
			
			var startIndex:int = 0;

			var endIndex:int = str.length - 1;
			while (IsWhitespace(str.charAt(endIndex)))
				--endIndex;
			
			if (endIndex >= startIndex)
				return str.slice(startIndex, endIndex + 1);
			else
				return "";
		}

		public static function StartsWidth(src:String, value:String, ignoreCase:Boolean = true):Boolean
		{
			if(src && value && src.length >= value.length)
			{
				 if(ignoreCase)
				 {
					 return IgnoreCaseCompare(src.substr(0, value.length), value);
				 }
				 else
				 {
					 return src.indexOf(value) == 0;
				 }
			}
			else if(src && value == "")
			{
				return true;
			}
			return false;	
		}
		public static function EndsWidth(src:String, value:String, ignoreCase:Boolean = true):Boolean
		{
			if(src && value && src.length >= value.length)
			{
				if(ignoreCase)
				{
					return IgnoreCaseCompare(src.substr(src.length - value.length), value);
				}
				else
				{
					return src.lastIndexOf(value) == src.length - value.length;
				}
			}
			return false;	
		}
		
		public static function StartsAndEndsWidth(src:String, start:String, end:String, ignoreCase:Boolean = true):Boolean
		{
			return StartsWidth(src, start, ignoreCase) && EndsWidth(src, end, ignoreCase);
		}
		/**
		 * 去掉src头尾
		 */
		public static function SliceWdith(src:String, start:String, end:String, ignoreCase:Boolean = true):String
		{
			if(!src) return "";
			var _start:int = 0;
			var _end:int = int.MAX_VALUE;
			if(start && StartsWidth(src, start, ignoreCase))
				_start = start.length;
			if(end && EndsWidth(src, end, ignoreCase))
				_end = src.length - end.length;
			return src.slice(_start, _end);
		}
		
		public static function IndexOf(src:String, value:String, start:int=0, ignoreCase:Boolean = true):int
		{
			if(!ignoreCase)
				return src.indexOf(value, start);
			
			if (src.length == 0)
				return -1;
			
			for (var i:int = start; i < src.length; i++)
			{
				if (IgnoreCaseCompare(src.substr(i, value.length), value))
				{
					return i;
				}
			}
			
			return -1;
		}
		/**
		 * 以delim分割，并对每项调用Trim
		 */
		public static function SplitTrim(src:String, delim:String):Array
		{
			var tokens:Array = src.split(delim);
			for(var i:int=0;i<tokens.length;i++)
			{
				tokens[i] = Trim(tokens[i]);
			}
			return tokens;
		}
		/**
		 * 判断是否空格
		 */ 
		public static function IsWhitespace(character:String):Boolean
		{
			switch (character)
			{
				case " ":
				case "\t":
				case "\r":
				case "\n":
				case "\f":
					return true;
					
				default:
					return false;
			}
		}
		/**
		 * 获取base64编码字符串，字符串采用UTF8编码
		 */
		public static function GetBase64String(value:Object):String
		{
			var bytes:ByteArray;
			if(value is ByteArray)
				bytes = value as ByteArray;
			else
			{
				bytes = new ByteArray();
				bytes.writeUTFBytes(value.toString());
			}
			var encoder:Base64Encoder = new Base64Encoder();
			encoder.encodeBytes(bytes);

			return encoder.flush();
		}
		/**
		 * base64字符串解码
		 */
		public static function Base64StringToByteArray(value:String):ByteArray
		{
			var decoder:Base64Decoder = new Base64Decoder();
			decoder.decode(value);
			return decoder.flush();
		}
		/**
		 * 采用特定编码进行url encode。
		 * 默认是utf-8，常用有gb2312, gbk, big5
		 */ 
		public static function UrlEncode(url:String, charSet:String = "utf-8"):String
		{
			var result:String ="";
			var byte:ByteArray =new ByteArray();
			byte.writeMultiByte(url, charSet);
			for(var i:int;i<byte.length;i++){
				result += escape(String.fromCharCode(byte[i]));
			}
			return result;
		}
		
		/**
		 * 读取XML文件流的字节，能够自动处理0xEF，0xBB，0xBF字节
		 */ 
		public static function ReadXmlFileBytes(bytes:ByteArray):String
		{
			var text:String;
			
			if(bytes.length>=3)
			{
				if(bytes[0] == 0xEF && bytes[1] == 0xBB && bytes[2] == 0xBF)
				{
					bytes.position = 3;
					text = bytes.readMultiByte(bytes.length-3, "utf-8");
				}
				else
					text = bytes.readMultiByte(bytes.length, "utf-8");
			}
			else
				text = bytes.readMultiByte(bytes.length, "utf-8");
			
			return text;
		}
		
		public static function Repeat(value:String, count:int):String
		{
			var res:String = "";
			for(var i:int=0;i<count;i++)
			{
				res+=value;
			}
			return res;
		}
		
		public static function SplitAtLen(value:String, len:int):Array
		{
			var tokens:Array = new Array();
			if(!value || !len)
			{
				tokens.push(value);
				return tokens;
			}
			
			var index:int = 0;
			var start:int = 0;
			var token:String;
			while(index<value.length)
			{
				index++;
				if(index%len==0)
				{
					token = value.substring(start, index);
					tokens.push(token);
					start = index;
				}
				else if(index == value.length)
				{
					token = value.substring(start);
					tokens.push(token);
				}
			}
			return tokens;
		}
		
		
	}
}