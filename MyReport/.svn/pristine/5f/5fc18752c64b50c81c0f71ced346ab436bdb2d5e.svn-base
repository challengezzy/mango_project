/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


函数调用表达式

*/

package hlib
{
	import mx.collections.ArrayCollection;
 
	public class MethodExpression extends Expression
	{
		public static var STRING_METHOD:String = "str";
		public static var SupportedMethods:Object = GetSupportedMethods();
		private static function GetSupportedMethods():Object
		{
			var methods:Object = {};
			//【日期函数】
			AddMethod(methods, "Date", CreateDate);//创建日期：Date(year,month,day, hour,minute, second)
			AddMethod(methods, "Year", Year);//获取年份：Year(date)
			AddMethod(methods, "Month", Month);//获取月份：Month(date)
			AddMethod(methods, "Day", Day);//获取天数：Day(date)
			//【数学函数】
			AddMethod(methods, "Num", Num);//转成数字：Num(exp)
			AddMethod(methods, "Int", Int);//转成整数：Int(exp)
			AddMethod(methods, "Abs", Abs);//绝对值：Abs(exp)
			AddMethod(methods, "Opp", Opp);//相反数：Opp(exp)
			AddMethod(methods, "Rand", Rand);//随机数：Rand(min,max)
			AddMethod(methods, "RandInt", RandInt);//随机整数：RandInt(min,max)
			AddMethod(methods, "LenInt", LenInt);//获取整数部分位数：LenInt(exp)
			AddMethod(methods, "Ceil", Ceil);//上限值：Ceil(exp, digits)
			AddMethod(methods, "Floor", Floor);//下限值：Floor(exp, digits)
			AddMethod(methods, "Round", Round);//四舍五入：Round(exp, digits)
			//【文本处理函数】
			AddMethod(methods, "Str", Str);//输出字符串：Str(exp)
			AddMethod(methods, "CurrencyCN", CurrencyCN);//转成中文大写金额：CurrencyCN(exp)
			AddMethod(methods, "Fixed", Fixed);//数字定点输出：Fixed(num,digits)
			AddMethod(methods, "Prefixed", Prefixed);//数字前置补零：Prefixed(num,len)
			AddMethod(methods, "FormatNum", FormatNum);//格式化数字输出：FormatNum(exp,format)
			AddMethod(methods, "FormatDate", FormatDate);//格式化日期输出：FormatDate(exp,format)
			AddMethod(methods, "Concat", Concat);//连接字符串：Concat(text1,text2,…)
			AddMethod(methods, "Len", Len);//获取字符串长度：Len(exp)
			AddMethod(methods, "Find", Find);//查找字符串：Find(text,within,start)
			AddMethod(methods, "Replace", Replace);//替换字符串：Replace(text,within,replace)
			AddMethod(methods, "Substr", Substr);//截断字符串：Substr(text,start,len)
			AddMethod(methods, "Substr2", Substr2);//截断字符串2：Substr2(text,start,end)
			AddMethod(methods, "Split", Split);//分割字符串：Split(text, delim)	5
			AddMethod(methods, "SplitAtLen", SplitAtLen);//根据长度分割字符串：SplitAtLen(text, len)	5
			AddMethod(methods, "Join", Join);//合并成字符串：Join(tokens, split)	5
			AddMethod(methods, "BracketL", BracketL);//小括号(左)：BracketL()
			AddMethod(methods, "BracketR", BracketR);//小括号(右)：BracketR()
			//【流程控制函数】
			AddMethod(methods, "If", If);//单条件执行：If(cond,proc_true,proc_false)
			AddMethod(methods, "IIf", If);//兼容代码
			AddMethod(methods, "Choose", Choose);//分支执行：Choose(index,proc1,proc2,proc3,…)
			AddMethod(methods, "Switch", Switch);//多条件执行：Switch(cond1,proc1,cond2,proc2,cond3,proc3,…)
			//【杂项函数】
			AddMethod(methods, "Null", Null);//空值：Null()
			AddMethod(methods, "True", True);//布尔值真：True()
			AddMethod(methods, "False", False);//布尔值非：False()
			//【数组函数】
			AddMethod(methods, "Array", _Array);//创建数组：Array(item1, item2, …)	6
			AddMethod(methods, "ConcatArray", ConcatArray);//连接数组：ConcatArray(array1, array2, …)	6
			AddMethod(methods, "GetItemAt", GetItemAt);//获取数组项：GetItemAt(array, index)	7
			AddMethod(methods, "GetItemIndex", GetItemIndex);//获取数组项索引：GetItemIndex(array, item)	7
			AddMethod(methods, "AddItem", AddItem);//添加数组项：AddItem(array, item)	7
			AddMethod(methods, "SetItemAt", SetItemAt);//设置数组项：SetItemAt(array, item, index)	7
			AddMethod(methods, "RemoveItemAt", RemoveItemAt);//移除数组项：RemoveItemAt(array, index)	7
			AddMethod(methods, "RemoveItem", RemoveItem);//移除数组项：RemoveItem(array, item)	7
	 
			return methods;
		}
 
		protected static function AddMethod(value:Object, method:String, func:Function):void
		{
			var method:String = method.toLowerCase();
			if(value.hasOwnProperty(method))
				throw new Error("方法“" + method + "”已存在！");
			value[method] = func;
		}
		
		protected var _Method:String;
		protected var _Arguments:Array;
		protected override function Disposing():void
		{
			DisposeUtil.Dispose(_Arguments);
			_Arguments = null;
			super.Disposing();
		}
		/**
		 * arguments：函数参数，存储Expression对象
		 */ 
		public function MethodExpression(method:String, arguments:Array)
		{
			super();
			_Arguments = arguments;
			_Method = method.toLowerCase();//转成小写
		}
 
		public override function Execute(context:*):*
		{
			var func:Function = SupportedMethods[_Method];//通过函数表查询处理方法
			return func(context, _Arguments);
		}
		public function toString():String
		{
			return "Method{"+_Method +", ["+_Arguments+"]}";
		}
 
	 
		/*
		【日期函数】
		创建日期：Date(year,month,day, hour,minute, second)
		获取年份：Year(date)
		获取月份：Month(date)
		获取天数：Day(date)
		*/
		private static function CreateDate(context:Object, args:Array):Object
		{
			var now:Date = new Date();
			if(args.length == 0)
				return now;
			var year:* = now.fullYear;
			var month:*= now.month;
			var date:*= now.date;
			var hour:*=0;
			var minutes:*=0;
			var seconds:*=0;
			if(args.length>0)
				year = Number(Expression(args[0]).Execute(context));
			if(args.length>1)
				month = Number(Expression(args[1]).Execute(context)) - 1;
			if(args.length>2)
				date = Number(Expression(args[2]).Execute(context));
			if(args.length>3)
				hour = Number(Expression(args[3]).Execute(context));
			if(args.length>4)
				minutes = Number(Expression(args[4]).Execute(context));
			if(args.length>5)
				seconds = Number(Expression(args[5]).Execute(context));
			
			return new Date(year, month, date, hour, minutes, seconds);
		}
		private static function _GetDate(context:Object, args:Array):*
		{
			var date:Date;
			if(args.length > 0)
			{
				var val:Object = Expression(args[0]).Execute(context);
				date = Parser.ParseDate(val);
			}
			if(!date)
				date = new Date();
			
			return date;
		}
		private static function Year(context:Object, args:Array):Object
		{
			var date:Date = _GetDate(context, args);

			return date.fullYear;
		}
		private static function Month(context:Object, args:Array):Object
		{
			var date:Date = _GetDate(context, args);
			
			return date.month + 1;
		}
		private static function Day(context:Object, args:Array):Object
		{
			var date:Date = _GetDate(context, args);
			
			return date.date;
		}
		/*
		【数学函数】
		转成数字：Num(exp)
		装成整数：Int(exp)
		绝对值：Abs(exp)
		相反数：Opp(exp)
		随机数：Rand(min,max)
		随机整数：RandInt(min,max)
		获取整数部分位数：LenInt(exp)
		上限整数：Ceil(exp)
		下限整数：Floor(exp)
		四舍五入整数：Round(exp)
		*/
		private static function Num(context:Object, args:Array):Object
		{
			if(args.length == 0)
				return 0;
			var exp:Expression = args[0];
			return parseFloat(String(exp.Execute(context)));
		}
		private static function Int(context:Object, args:Array):Object
		{
			if(args.length == 0)
				return 0;
			var exp:Expression = args[0];
			return parseInt(String(exp.Execute(context)));
		}
		private static function Abs(context:Object, args:Array):Object
		{
			if(args.length == 0)
				throw new Error("Abs函数缺少参数，当前参数长度：" + args.length + "。");
			var exp:Expression = args[0];
			return Math.abs(Number(exp.Execute(context)));
		}
		private static function Opp(context:Object, args:Array):Object
		{
			if(args.length == 0)
				throw new Error("Opp函数缺少参数，当前参数长度：" + args.length + "。");
			var exp:Expression = args[0];
			return Number(exp.Execute(context)) * -1;
		}
		private static function Rand(context:Object, args:Array):Object
		{
			if(args.length == 0)
				return Math.random();
			var exp:Expression = args[0];
			var min:Number = Number(exp.Execute(context));
			if(args.length==1)
				return min;
			exp = args[1];
			var max:Number = Number(exp.Execute(context));
			return MathUtil.Rand(min, max);
		}
		private static function RandInt(context:Object, args:Array):Object
		{
			var num:Number = Number(Rand(context, args));
			return Math.floor(num);
		}
		private static function LenInt(context:Object, args:Array):Object
		{
			if(args.length == 0)
				throw new Error("LenInt函数缺少参数，当前参数长度：" + args.length + "。");
			var exp:Expression = args[0];
			var num:Number = Number(Rand(context, args));
			if(isNaN(num))
				return 0;
			var i:int = Math.floor(num);
			return i.toString().length;
		}
		private static function Ceil(context:Object, args:Array):Object
		{
			if(args.length == 0)
				throw new Error("Ceil函数缺少参数，当前参数长度：" + args.length + "。");
			var exp:Expression = args[0];
			var num:Number = Number(exp.Execute(context));
			var digits:Number = 0;
			if(args.length > 1)
			{
				digits = Number(Expression(args[1]).Execute(context));
				if(isNaN(digits))
					digits = 0;
			}
			return MathUtil.Ceil(num, digits);
		}
		private static function Floor(context:Object, args:Array):Object
		{
			if(args.length == 0)
				throw new Error("Floor函数缺少参数，当前参数长度：" + args.length + "。");
			var exp:Expression = args[0];
			var num:Number = Number(exp.Execute(context));
			var digits:Number = 0;
			if(args.length > 1)
			{
				digits = Number(Expression(args[1]).Execute(context));
				if(isNaN(digits))
					digits = 0;
			}
			return MathUtil.Floor(num, digits);
		}
		private static function Round(context:Object, args:Array):Object
		{
			if(args.length == 0)
				throw new Error("Round函数缺少参数，当前参数长度：" + args.length + "。");
			var exp:Expression = args[0];
			var num:Number = Number(exp.Execute(context));
			var digits:Number = 0;
			if(args.length > 1)
			{
				digits = Number(Expression(args[1]).Execute(context));
				if(isNaN(digits))
					digits = 0;
			}
			
			return MathUtil.Round(num, digits);
		}
		/*
		【文本处理函数】
		输出字符串：Str(exp)
		转成中文大写金额：CurrencyCN(exp)
		数字定点输出：Fixed(num,digits)
		数字前置补零：Prefixed(num,len)
		格式化数字输出：FormatNum(exp,format)
		格式化日期输出：FormatDate(exp,format)
		连接字符串：Concat(text1,text2,…)			
		获取字符串长度：Len(exp)
		查找字符串：Find(text,within,start)
		替换字符串：Replace(text,within,replace)
		截断字符串：Substr(text,start,len)
		截断字符串2：Substr2(text,start,end)
		分割字符串：Split(text, delim)	5
		根据长度分割字符串：SplitAtLen(text, len)	5
		合并成字符串：Join(tokens, split)	5
		小括号(左)：BracketL()
		小括号(右)：BracketR()
		*/
		private static function Str(context:Object, args:Array):Object
		{
			if(args.length == 0)
				return "";
			var exp:Expression = args[0];
			return String(exp.Execute(context));
		}
		private static function CurrencyCN(context:Object, args:Array):Object
		{
			if(args.length == 0)
				throw new Error("CurrencyCN函数缺少参数，当前参数长度：" + args.length + "。");
			var exp:Expression = args[0];
			return Parser.ToUpperChineseCurrency(Number(exp.Execute(context)));
		}
		private static function Fixed(context:Object, args:Array):Object
		{
			if(args.length == 0)
				throw new Error("Fixed函数缺少参数，当前参数长度：" + args.length + "。");
			var num:Number = Number(Expression(args[0]).Execute(context));
			var digits:Number = 0;
			if(args.length > 1)
			{
				digits = Number(Expression(args[1]).Execute(context));
				if(isNaN(digits))
					digits = 0;
			}
			
			return MathUtil.Fixed(num, digits);
		}
		private static function Prefixed(context:Object, args:Array):Object
		{
			if(args.length == 0)
				throw new Error("Prefixed函数缺少参数，当前参数长度：" + args.length + "。");
			var num:Number = Number(Expression(args[0]).Execute(context));
			var digits:Number = 0;
			if(args.length > 1)
			{
				digits = Number(Expression(args[1]).Execute(context));
				if(isNaN(digits))
					digits = 0;
			}
			
			return Parser.ToPrefixed(num, digits);
		}
		private static function FormatNum(context:Object, args:Array):Object
		{
			if(args.length < 2)
				throw new Error("Format函数缺少参数，当前参数长度：" + args.length + "。");
			
			var num:Number = Number(Expression(args[0]).Execute(context));
			var format:String = String(Expression(args[1]).Execute(context));
			
			return Parser.Format(num, format);
		}
		private static function FormatDate(context:Object, args:Array):Object
		{
			if(args.length < 2)
				throw new Error("Format函数缺少参数，当前参数长度：" + args.length + "。");
			var date:Object = Expression(args[0]).Execute(context);
			var format:String = String(Expression(args[1]).Execute(context));
			
			return Parser.Format(date, format);
		}
		protected static function Concat(context:Object, args:Array):Object
		{
			var result:String="";
 
			for(var i:int=0; i<args.length; i++)
			{
				var exp:Expression = args[i];
				result += String(exp.Execute(context));
			}
			return result;
		}
		private static function Len(context:Object, args:Array):Object
		{
			if(args.length == 0)
				throw new Error("Len函数缺少参数，当前参数长度：" + args.length + "。");
			var exp:Expression = args[0];
			var value:Object = exp.Execute(context);
			
			if(value is Array || value is ArrayCollection)
				return value.length;
			return String(value).length;
		}
		private static function Find(context:Object, args:Array):Object
		{
			if(args.length < 2)
				throw new Error("Find函数缺少参数，当前参数长度：" + args.length + "。");
			var text:String = String(Expression(args[0]).Execute(context));
			var within:String = String(Expression(args[1]).Execute(context));
			var start:Number = 0;
			if(args.length > 2)
			{
				start = Number(Expression(args[2]).Execute(context));
				if(isNaN(start))
					start = 0;
			}
			return text.indexOf(within, start);
		}
		private static function Replace(context:Object, args:Array):Object
		{
			if(args.length < 3)
				throw new Error("Replace函数缺少参数，当前参数长度：" + args.length + "。");
			var text:String = String(Expression(args[0]).Execute(context));
			var oldStr:String = String(Expression(args[1]).Execute(context));
			var newStr:String = String(Expression(args[2]).Execute(context));
			
			return StringUtil.Replace(text, oldStr, newStr);
		}
		private static function Substr(context:Object, args:Array):Object
		{
			if(args.length < 2)
				throw new Error("Substr函数缺少参数，当前参数长度：" + args.length + "。");
			var text:String = String(Expression(args[0]).Execute(context));
			var start:Number = Number(Expression(args[1]).Execute(context));
			var len:Number = int.MAX_VALUE;
			if(args.length > 2)
			{
				len = Number(Expression(args[2]).Execute(context));
				if(isNaN(len))
					len = int.MAX_VALUE;
			}
			return text.substr(start, len);
		}
		private static function Substr2(context:Object, args:Array):Object
		{
			if(args.length < 2)
				throw new Error("Substr2函数缺少参数，当前参数长度：" + args.length + "。");
			var text:String = String(Expression(args[0]).Execute(context));
			var start:Number = Number(Expression(args[1]).Execute(context));
			var end:Number = int.MAX_VALUE;
			if(args.length > 2)
			{
				end = Number(Expression(args[2]).Execute(context));
				if(isNaN(end))
					end = int.MAX_VALUE;
			}
			return text.substring(start, end);
		}
		private static function Split(context:Object, args:Array):Object
		{
			if(args.length < 2)
				throw new Error("Split函数缺少参数，当前参数长度：" + args.length + "。");
			var text:String = String(Expression(args[0]).Execute(context));
			var delim:String = String(Expression(args[1]).Execute(context));
			return text.split(delim);
 
		}
		private static function SplitAtLen(context:Object, args:Array):Object
		{
			if(args.length < 2)
				throw new Error("SplitAtLen函数缺少参数，当前参数长度：" + args.length + "。");
			var text:String = String(Expression(args[0]).Execute(context));
			var len:int = int(Expression(args[1]).Execute(context));
			return hlib.StringUtil.SplitAtLen(text, len);
 
		}
		private static function Join(context:Object, args:Array):Object
		{
			if(args.length < 1)
				throw new Error("Join函数缺少参数，当前参数长度：" + args.length + "。");
			var tokens:Object = Expression(args[0]).Execute(context);
			var sep:String = "";
			if(args.length > 1)
				sep = String(Expression(args[1]).Execute(context));
			if(tokens is Array)
			{
				return (tokens as Array).join(sep);
			}
			
			return String(tokens);
			
		}
		private static function BracketL(context:Object, args:Array):Object
		{
			return "(";
		}
		private static function BracketR(context:Object, args:Array):Object
		{
			return ")";
		}
		/*
		【流程控制函数】
		单条件执行：If(cond,proc_true,proc_false)
		分支执行：Choose(index,proc1,proc2,proc3,…)
		多条件执行：Switch(cond1,proc1,cond2,proc2,cond3,proc3,…)
		*/
		private static function If(context:Object, args:Array):Object
		{
			if(args.length < 3)
				throw new Error("If函数缺少参数，当前参数长度：" + args.length + "。");
			
			if(Parser.ParseBoolean(Expression(args[0]).Execute(context)))
			{
				return Expression(args[1]).Execute(context);
			}
			else
			{
				return Expression(args[2]).Execute(context);
			}
			
			return null;
		}
		
		private static function Choose(context:Object, args:Array):Object
		{
			if(args.length < 2)
				throw new Error("Choose函数缺少参数，当前参数长度：" + args.length + "。");
			
			var value:Number = int(Expression(args[0]).Execute(context));
			if(!isNaN(value))
			{
				var index:int = int(value) + 1;
				if(index < args.length && index > 0)
					return Expression(args[index]).Execute(context);
			}
			
			return null;
		}
		
		private static function Switch(context:Object, args:Array):Object
		{
			var conditions:Array = new Array();
			var processes:Array = new Array();
			var i:int;
			for(i=0; i<args.length; i++)
			{
				if(i%2==0)
					conditions.push(args[i]);
				else
					processes.push(args[i]);	
			}
			if(conditions.length > processes.length)
				conditions.pop();
			
			for(i=0; i<conditions.length; i++)
			{
				var cond:Expression = conditions[i];
				if(Parser.ParseBoolean(cond.Execute(context)))
				{
					if(i < processes.length)
					{
						var proc:Expression = processes[i];
						return proc.Execute(context);
					}
				}
			}
			return null;
		}
		/*
		【数组函数】
		
		*/
		//创建数组：Array(item1, item2, …)	6
		private static function _Array(context:Object, args:Array):Object
		{
			var result:Array = new Array();
			var item:Object;
			for(var i:int=0; i<args.length; i++)
			{
				var exp:Expression = args[i];
				item = exp.Execute(context);
				result.push(item);
			}
			return result;
		}
		//连接数组：ConcatArray(array1, array2, …)	6
		private static function ConcatArray(context:Object, args:Array):Object
		{
			if(args.length < 1)
				throw new Error("ConcatArray函数缺少参数，当前参数长度：" + args.length + "。");
			var result:Array = Expression(args[0]).Execute(context) as Array;
			var item:Object;
			for(var i:int=1; i<args.length; i++)
			{
				var exp:Expression = args[i];
				var list:Array = exp.Execute(context) as Array;
				if(list)
				{
					for each(item in list)
					{
						result.push(item);
					}
				}
			}
			return result;
		}
		//获取数组项：GetItemAt(array, index)	7
		private static function GetItemAt(context:Object, args:Array):Object
		{
			if(args.length < 1)
				throw new Error("GetItemAt函数缺少参数，当前参数长度：" + args.length + "。");
			var array:Array = Expression(args[0]).Execute(context) as Array;
			var index:int = 0;
			if(args.length > 1)
				index = int(Expression(args[1]).Execute(context));
			
			if(index>=0 && index<array.length)			
				return array[index];
			return null;
		}
		//获取数组项索引：GetItemIndex(array, item)	7
		private static function GetItemIndex(context:Object, args:Array):Object
		{
			if(args.length < 2)
				throw new Error("GetItemIndex函数缺少参数，当前参数长度：" + args.length + "。");
			var array:Array = Expression(args[0]).Execute(context) as Array;
			var item:Object = Expression(args[1]).Execute(context);
			return array.indexOf(item);
		}
		//添加数组项：AddItem(array, item)	7
		private static function AddItem(context:Object, args:Array):Object
		{
			if(args.length < 2)
				throw new Error("AddItem函数缺少参数，当前参数长度：" + args.length + "。");
			var array:Array = Expression(args[0]).Execute(context) as Array;
			var item:Object = Expression(args[1]).Execute(context);
			return array.push(item);
		}
		//设置数组项：SetItemAt(array, item, index)	7
		private static function SetItemAt(context:Object, args:Array):Object
		{
			if(args.length < 2)
				throw new Error("SetItemAt函数缺少参数，当前参数长度：" + args.length + "。");
			var array:Array = Expression(args[0]).Execute(context) as Array;
			var item:Object = Expression(args[1]).Execute(context);
			var index:int = 0;
			if(args.length > 2)
				index = int(Expression(args[2]).Execute(context));
			var old:Object = null;
			if(index>=0 && index<array.length)	
			{
				old = array[index];
				array[index] = item;
			}
			return old;
			 
		}
		//移除数组项：RemoveItemAt(array, index)	7
		private static function RemoveItemAt(context:Object, args:Array):Object
		{
			if(args.length < 1)
				throw new Error("RemoveItemAt函数缺少参数，当前参数长度：" + args.length + "。");
			var array:Array = Expression(args[0]).Execute(context) as Array;
			var index:int = 0;
			if(args.length > 1)
				index = int(Expression(args[1]).Execute(context));
			
			if(index>=0 && index<array.length)			
				return array.splice(index,1)[0];
			return null;
		}
		//移除数组项：RemoveItem(array, item)	7
		private static function RemoveItem(context:Object, args:Array):Object
		{
			if(args.length < 2)
				throw new Error("RemoveItem函数缺少参数，当前参数长度：" + args.length + "。");
			var array:Array = Expression(args[0]).Execute(context) as Array;
			var item:Object = Expression(args[1]).Execute(context);
			var index:int = array.indexOf(item);
			if(index>=0)
				return array.splice(index,1)[0];
			return item;
		}
		/*
		【杂项函数】
		空值：Null()
		布尔值真：True()
		布尔值非：False()
		*/
		private static function Null(context:Object, args:Array):Object
		{
			return null;
		}
		private static function True(context:Object, args:Array):Object
		{
			return true;
		}
		private static function False(context:Object, args:Array):Object
		{
			return false;
		}
	}
}