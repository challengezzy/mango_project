/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


数学函数工具类。


*/
package hlib
{
	public final class MathUtil
	{
		/** min < value < max */
		public static function Rand(min:Number, max:Number):Number
		{
			if(min < max)
			{
				return min + Math.random() * (max-min);
			}
			return min;
		}
		/** min <= value < max */
		public static function RandInt(min:int, max:int):int
		{
			var num:Number = Number(Rand(min, max));
			return Math.floor(num);
		}
		/**
		 * 获取小数点后位数
		 */ 
		public static function LenDigit(value:Number):int
		{
			var str:String = value.toString();
			var index:int = str.indexOf(".");
			if(index>=0)
				return str.length-index-1;
			return 0;
		}
		/**
		 * 小数点右边格式化输出
		 */ 
		private static function FixedRight(value:Number, digits:uint = 0):String
		{
			var abs:Number = Math.abs(value);
			var str:String = abs.toString();
			var index:int = str.indexOf(".");
			var strInt:String = str;
			var strDec:String = "";
			if(index>0)
			{
				strInt = str.substr(0, index);
				strDec = str.substr(index+1, digits);
			}
			while(strDec.length < digits)
			{
				strDec += "0";
			}
			var integer:Number = Math.floor(Number(strInt+strDec));
			
			if(index>0)
			{
				var nums:Array = new Array();
				var dec:String = str.substr(index+1+digits);//取舍小数部分
				for(var i:int=0;i<dec.length;i++)
				{
					nums.push(int(dec.charAt(i)));//拆分每个数字
				}
				var n1:int;
				var n2:int;
				while(nums.length>1)
				{
					n1 = nums.pop();
					if(n1>4)
					{
						n2 = nums[nums.length-1]+1;
						nums[nums.length-1] = n2;
					}
				}
				if(nums.length && nums[nums.length-1]>4)
					integer++;
			}
			
			str = integer.toString();
			index = str.indexOf(".");
			var minus:String = value<0 ? "-" : "";
			if(index>=0)
				str = str.substr(0, index);
			if(digits==0)
				return minus + str;
			while(str.length<strInt.length+strDec.length)
			{
				str = "0"+str;
			}
			return minus + str.substr(0, str.length-digits) + "."+ str.substr(str.length-digits);
			
		}
 
		public static function Fixed(value:Number, digits:int = 0):String
		{
			if(isNaN(digits))
				digits = 0;
			if(digits >= 0)
				return FixedRight(value, digits);
			else
			{
				var p:Number = Math.pow(10, Math.abs(digits));
				value = value / p;
				var num:Number = Math.floor(Number(FixedRight(value, 0)) * p);
				var str:String = num.toString();
				var index:int = str.indexOf(".");
				if(index>=0)
					str = str.substr(0, index);
				return str;
			}
		}
		/**
		 * 四舍五入
		 */ 
		public static function Round(value:Number, digits:int = 0):Number
		{
			return Number(Fixed(value, digits));
		}
		/**
		 * 向下取整
		 */ 
		public static function Floor(value:Number, digits:int = 0):Number
		{
			if(isNaN(digits))
				digits = 0;
			
			var p:Number = Math.pow(10, Math.abs(digits));
			if(digits >= 0)
			{
				value = Math.floor(value * p);
				return value / p;
			}
			else
			{
				value = Math.floor(value / p);
				return value * p;
			}
		}
		/**
		 * 向上取整
		 */ 
		public static function Ceil(value:Number, digits:int = 0):Number
		{
			if(isNaN(digits))
				digits = 0;
			
			var p:Number = Math.pow(10, Math.abs(digits));
			if(digits >= 0)
			{
				value = Math.ceil(value * p);
				return value / p;
			}
			else
			{
				value = Math.ceil(value / p);
				return value * p;
			}
		}
		
		public static function IsPercentage(value:String):Boolean
		{
			return value.indexOf("%")>=0;
		}
		public static function GetPercentScale(value:String):Number
		{
			var scale:Number;
			var index:int = value.indexOf("%");
			if(index>=0)
				scale = Number(value.substring(0, index));
			else
				scale = Number(value);
			return scale / 100;
		}
	}
}