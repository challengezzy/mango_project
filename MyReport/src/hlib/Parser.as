/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


格式转换工具类。


*/

package hlib
{
	
	import mx.formatters.NumberFormatter;
	
	public final class Parser
	{
		
		//英寸（相当于 2。54 厘米，一英尺有 12 英寸）（ 
		public static function CMToInches(value:Number):Number
		{
			return value/2.54;
		}
		
		public static function MMToInches(value:Number):Number
		{
			return value/25.4;
		}
		
		//1 像素 = 1 磅 = 1/72 英寸 = 20 缇 (twip) 
		//1 英寸 = 72 像素 = 72 磅 = 1440 缇 
		//1 厘米 = 567缇 
		public static function GetPixel(value:Number, unit:String):Number
		{
			if (unit.toLowerCase() == "cm")
			{
				return Math.floor(value * 28.35);
			}
			else if(unit.toLowerCase() == "mm")
			{
				return Math.floor(value * 2.835); 
			}
			throw new Error("Not suport unit " + unit);
		}
		
		public static function GetUnit(pixel:Number, unit:String):Number
		{
			if (unit.toLowerCase() == "cm")
			{
				return pixel / 28.35;
			}
			else if(unit.toLowerCase() == "mm")
			{
				return pixel / 2.835;
			}
			throw new Error("Not suport unit " + unit);
		}
		
		public static function ParseBoolean(value:Object):Boolean
		{
			if(value is Boolean || value is Number)
				return Boolean(value);
			else if(value is IXmlSerializable)
				return true;
			else if(value == null)
				return false;
 
			var str:String = String(value);
			if(!str)
				return false;
			str = str.toLowerCase();
			if(str=="true" || str == "是"|| str == "对")
				return true;
			else if(str == "false" || str=="否" || str == "错")
				return false;
			var num:Number = parseFloat(str);
			if(!isNaN(num))
				return num!=0;
			return Boolean(str);
			
		}
		
		public static function ParseColor(value:String):uint
		{
			if(!value) return 0;
			var index:int = value.indexOf("#");
			if(index>=0)
				return parseInt(value.substr(index+1), 16);
			return uint(value);
		}
		
		public static function ToHexColor(color:int):String
		{
			var value:String = color.toString(16);
			return "#" + ToPrefixed(value, 6);
		}
		public static function ToPrefixed(value:Object, len:int):String
		{
			var result:String = String(value);
			while(result.length < len)
			{
				result = "0" + result;
			}
			return result;
		}
		
		/**
		 * 字符串转成日期，支持格式：
		 * yyyyMMdd 
		 * yyyyMMddHH 
		 * yyyyMMddHHmm 
		 * yyyyMMddHHmmss
		 * yyyy-MM-dd（“-”作判断关键字）
		 * yyyy-MM-dd HH（“-”作判断关键字）
		 * yyyy-MM-dd HH（“-”作判断关键字）
		 * yyyy-MM-dd HH:mm （“-”作判断关键字）
		 * yyyy-MM-dd HH:mm:ss（“-”作判断关键字）
		 * yyyy年MM月dd日（“年”作判断关键字）
		 * yyyy年MM月dd日HH时（“年”作判断关键字）
		 * yyyy年MM月dd日HH时mm分（“年”作判断关键字）
		 * yyyy年MM月dd日HH时mm分ss秒（“年”作判断关键字）
		 * MM/dd/yyyy（例如，“02/01/2005”）
		 * MM/dd/yyyy HH:mm:ss
		 * MM/yyyy dd（例如，“02/2005 23”）
		 * Day Month Date Hours:Minutes:Seconds GMT Year（例如，“Tue Feb 1 00:00:00 GMT-0800 2005”，这与 toString() 一致）
		 * Day Month Date Year Hours:Minutes:Seconds AM/PM（例如，“Tue Feb 1 2005 12:00:00 AM”，这与 toLocaleString() 一致）
		 * Day Month Date Year Hours:Minutes:Seconds（例如，“Tue Feb 1 2005 23:59:59”）
		 * Day Month Date Year（例如，“Tue Feb 1 2005”，这与 toDateString() 一致）
		 */
		public static function ParseDate(value:Object):Date
		{
			if (!value)
				return new Date();
			if(value is Date)
				return value as Date;
			var _value:String = String(value);
			var year:Number;
			var month:Number;
			var date:Number;
			var h:Number = 0;
			var m:Number = 0;
			var s:Number = 0;
			var ms:Number = 0;
			/*
			* yyyyMMdd 
			* yyyyMMddHH 
			* yyyyMMddHHmm 
			* yyyyMMddHHmmss
			*/
			if(!isNaN(Number(_value)) && _value.length >= 8)
			{
				year = Number(_value.substr(0, 4));
				month = Number(_value.substr(4, 2)) -1;
				date = Number(_value.substr(6, 2));
				if(_value.length>=10)
					h = Number(_value.substr(8, 2));
				if(_value.length>=12)
					m = Number(_value.substr(10, 2));
				if(_value.length>=14)
					s = Number(_value.substr(12, 2));
				
				return new Date(year, month, date,h,m,s,ms);
			}
			/*
			* yyyy-MM-dd（“-”作判断关键字）
			* yyyy-MM-dd HH（“-”作判断关键字）
			* yyyy-MM-dd HH（“-”作判断关键字）
			* yyyy-MM-dd HH:mm （“-”作判断关键字）
			* yyyy-MM-dd HH:mm:ss（“-”作判断关键字）
			* yyyy年MM月dd日（“年”作判断关键字）
			* yyyy年MM月dd日HH时（“年”作判断关键字）
			* yyyy年MM月dd日HH时mm分（“年”作判断关键字）
			* yyyy年MM月dd日HH时mm分ss秒（“年”作判断关键字）
			*/
			if(_value.indexOf("-")>0 || _value.indexOf("年")>0)
			{
				var match:Array = _value.match( /\d+/g );
				year = Number(match[0]);
				month = Number(match[1]) -1;
				date = Number(match[2]);
				if(match.length>3)
					h = Number(match[3]);
				if(match.length>4)
					m = Number(match[4]);
				if(match.length>5)
					s = Number(match[5]);
				
				return new Date(year, month, date,h,m,s,ms);
			}
			
			/*
			* MM/dd/yyyy（例如，“02/01/2005”）
			* MM/dd/yyyy HH:mm:ss
			* MM/yyyy dd（例如，“02/2005 23”）
			* Day Month Date Hours:Minutes:Seconds GMT Year（例如，“Tue Feb 1 00:00:00 GMT-0800 2005”，这与 toString() 一致）
			* Day Month Date Year Hours:Minutes:Seconds AM/PM（例如，“Tue Feb 1 2005 12:00:00 AM”，这与 toLocaleString() 一致）
			* Day Month Date Year Hours:Minutes:Seconds（例如，“Tue Feb 1 2005 23:59:59”）
			* Day Month Date Year（例如，“Tue Feb 1 2005”，这与 toDateString() 一致）
			*/
			return new Date(_value);
		}
		
		private static function FormatNumber(value:Number, format:String):String
		{
			if(!format)
				return String(value);
			
			format = StringUtil.Trim(format.toLowerCase());
			var index:int;
			var digits:uint;
			index = format.indexOf("c#,#")//货币符号、千分符
			if(index >= 0)
			{
				digits = uint(format.substr(index + 4));
				value = MathUtil.Round(value, digits);
				return "￥" + ThousandsSeparatorMoney(value, digits);
			}
			index = format.indexOf("#,#")//千分符数字
			if(index >= 0)
			{
				digits = uint(format.substr(index + 3));
				value = MathUtil.Round(value, digits);
				return ThousandsSeparatorMoney(value, digits);
			}
			index = format.indexOf("c");
			if(index >= 0)
			{
				digits = uint(format.substr(index + 1));
				return "￥" + MathUtil.Fixed(value, digits);
			}
			index = format.indexOf("f");
			if(index >= 0)
			{
				digits = uint(format.substr(index + 1));
				return MathUtil.Fixed(value, digits);
			}
			index = format.indexOf("p");
			if(index >= 0)
			{
				digits = uint(format.substr(index + 1));
				return MathUtil.Fixed(value * 100, digits)+"%";
			}
			index = format.indexOf("0.#");
			if(index >= 0)
			{
				digits = format.substr(index + 2).length;
				return String(Number(MathUtil.Fixed(value, digits)));
			}
			
			return String(value);
		}
		/**
		 * 格式化金额
		 * @param value
		 * @param digits
		 * @return 
		 */
		private static function ThousandsSeparatorMoney(value:Number,digits:uint=2):String
		{
			var numFormat:NumberFormatter = new NumberFormatter();
			numFormat.precision = digits;
			numFormat.useThousandsSeparator=true;
			return numFormat.format(value);
		}
		
		private static function FormatDate(value:Date, format:String):String
		{
			var date:Date = value;
			format = StringUtil.Trim(format.toLowerCase());
			if (format == "d")
				return date.fullYear + "年" + (date.month + 1) + "月" + date.date + "日";
			else if (format == "yyyy-mm-dd")
				return date.fullYear + "-" + ToPrefixed(date.month+1, 2) + "-" + ToPrefixed(date.date, 2);
			else if (format == "yyyy-mm-dd hh:mm:ss")
				return date.fullYear + "-" + ToPrefixed(date.month+1, 2) + "-" + ToPrefixed(date.date, 2) 
					+ " " + ToPrefixed(date.hours, 2) + ":" + ToPrefixed(date.minutes, 2) + ":" + ToPrefixed(date.seconds, 2);
			else if (format == "yyyymmdd")
				return date.fullYear + ToPrefixed(date.month+1, 2) + ToPrefixed(date.date, 2);
			else if (format == "yyyymmddhhmmss")
				return date.fullYear + ToPrefixed(date.month+1, 2) + ToPrefixed(date.date, 2) 
					+ ToPrefixed(date.hours, 2) + ToPrefixed(date.minutes, 2) + ToPrefixed(date.seconds, 2);
			return date.fullYear + "-" + ToPrefixed(date.month+1, 2) + "-" + ToPrefixed(date.date, 2);
		}
		
		private static function FormatBoolean(value:Object, format:String):String
		{
			var b:Boolean = ParseBoolean(value);
			switch(format)
			{
				case "t/f":
					return b?"True":"False";
				case "y/n":
					return b?"Yes":"No";
				case "对/错":
					return b?"对":"错";
				case "是/否":
					return b?"是":"否";
			}
			return b.toString();
		}
		
		public static function Format(value:Object, format:String):String
		{
			if(!format)
				return String(value);
			if (value is Number)
			{
				return FormatNumber(value as Number, format);
			}
			else if (value is Date)
			{
				return FormatDate(value as Date, format);
			}
			
			format = StringUtil.Trim(format.toLowerCase());
			//Date
			switch(format)
			{
				case "d":
				case "yyyy-mm-dd":
				case "yyyy-mm-dd hh:mm:ss":
				case "yyyymmdd":
				case "yyyymmddhhmmss":
					var date:Date = ParseDate(String(value));
					return FormatDate(date, format);
			}
			//Boolean
			switch(format)
			{
				case "t/f":
				case "y/n":
				case "对/错":
				case "是/否":
					return FormatBoolean(value, format);
			}
			//Number
			if(format.indexOf("f")>=0||format.indexOf("c")>=0||
				format.indexOf("p")>=0||format.indexOf("0.#")>=0||format.indexOf("#,#")>=0)
			{
				if(!isNaN(Number(value)))
					return FormatNumber(Number(value), format);
			}
			
			return String(value);
		}
		
		public static function ToUpperChineseCurrency(money:Number):String
		{
			
			var str1:String = "零壹贰叁肆伍陆柒捌玖"; //0-9所对应的汉字     
			var str2:String = "万仟佰拾亿仟佰拾万仟佰拾元角分"; //数字位所对应的汉字     
			var str3:String = ""; //从原num值中取出的值     
			var str4:String = ""; //数字的字符串形式     
			var str5:String = ""; //人民币大写金额形式     
			var i:int; //循环变量     
			var j:int; //num的值乘以100的字符串长度     
			var ch1:String = ""; //数字的汉语读法     
			var ch2:String = ""; //数字位的汉字读法     
			var nzero:int = 0; //用来计算连续的零值是几个     
			var temp:int; //从原num值中取出的值     
			
			money = Number(MathUtil.Fixed(Math.abs(money), 2)); //将num取绝对值并四舍五入取2位小数     
			str4 = Math.round(money * 100).toString(); //将num乘100并转换成字符串形式     
			j = str4.length; //找出最高位     
			if (j > 15)
			{
				return "溢出";
			}
			str2 = str2.substr(15 - j); //取出对应位数的str2的值。如：200.55,j为5所以str2=佰拾元角分     
			
			//循环取出每一位需要转换的值     
			for (i = 0; i < j; i++)
			{
				str3 = str4.substr(i, 1); //取出需转换的某一位的值     
				temp = int(str3); //转换为数字     
				if (i != (j - 3) && i != (j - 7) && i != (j - 11) && i != (j - 15))
				{
					//当所取位数不为元、万、亿、万亿上的数字时     
					if (str3 == "0")
					{
						ch1 = "";
						ch2 = "";
						nzero = nzero + 1;
					}
					else
					{
						if (str3 != "0" && nzero != 0)
						{
							ch1 = "零" + str1.substr(temp * 1, 1);
							ch2 = str2.substr(i, 1);
							nzero = 0;
						}
						else
						{
							ch1 = str1.substr(temp * 1, 1);
							ch2 = str2.substr(i, 1);
							nzero = 0;
						}
					}
				}
				else
				{
					//该位是万亿，亿，万，元位等关键位     
					if (str3 != "0" && nzero != 0)
					{
						ch1 = "零" + str1.substr(temp * 1, 1);
						ch2 = str2.substr(i, 1);
						nzero = 0;
					}
					else
					{
						if (str3 != "0" && nzero == 0)
						{
							ch1 = str1.substr(temp * 1, 1);
							ch2 = str2.substr(i, 1);
							nzero = 0;
						}
						else
						{
							if (str3 == "0" && nzero >= 3)
							{
								ch1 = "";
								ch2 = "";
								nzero = nzero + 1;
							}
							else
							{
								if (j >= 11)
								{
									ch1 = "";
									nzero = nzero + 1;
								}
								else
								{
									ch1 = "";
									ch2 = str2.substr(i, 1);
									nzero = nzero + 1;
								}
							}
						}
					}
				}
				if (i == (j - 11) || i == (j - 3))
				{
					//如果该位是亿位或元位，则必须写上     
					ch2 = str2.substr(i, 1);
				}
				str5 = str5 + ch1 + ch2;
				
				if (i == j - 1 && str3 == "0")
				{
					//最后一位（分）为0时，加上“整”     
					str5 = str5 + '整';
				}
			}
			if (money == 0)
			{
				str5 = "零元整";
			}
			return str5;
		}
		

	}
}