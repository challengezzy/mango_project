/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：

表达式解析器，提供通用解析逻辑。


*/
package hlib
{
 
	import flash.utils.Dictionary;
 
	public class ExpressionInterpreter implements IDispose
	{
		//======================static===========================
		private static var _SupportedMethods:Array;
		private static var _SupportedStringMethods:Array;
		public static var Instance:ExpressionInterpreter = new ExpressionInterpreter();

		public static var Trace:Boolean = false;
 
		/**
		 * 表达式必须以“=”开头
		 */ 
		public static function IsExpression(value:String):Boolean
		{
			if(value)
			{
				var str:String = StringUtil.TrimStart(value);
				return str && str.charAt(0) == "=";
			}
			return false;
		}
		//======================instance===========================
		protected var _HandleError:Boolean = true;
		private var _Disposed:Boolean = false;	
		protected final function get Disposed():Boolean
		{
			return _Disposed;
		}
		protected function Disposing():void
		{
			
		}
		public final function Dispose():void
		{
			if(_Disposed) return;
			_Disposed = true;
			Disposing();
		}
		public function ExpressionInterpreter()
		{
			if(!_SupportedMethods)
			{
				_SupportedMethods = new Array();
				for(var name:String in MethodExpression.SupportedMethods)
				{
					_SupportedMethods.push(name);
				}
			}
			if(!_SupportedStringMethods)
			{
				_SupportedStringMethods =[MethodExpression.STRING_METHOD]; 
			}
		}

		private function CreateContext(expression:String):ExpressionInterpreterContext
		{
			var item:ExpressionInterpreterContext = new ExpressionInterpreterContext(expression);
			return item;
		}
		private function CreateSingleExpression(operator:String, exp:Expression):Expression
		{
			var exp:Expression = new SingleExpression(operator, exp);
			return exp;
		}
		
		private function CreateBinaryExpression(operator:String, left:Expression, right:Expression):Expression
		{
			var exp:Expression = new BinaryExpression(operator, left, right);
			return exp;
		}
		
		protected function CreateConstExpression(value:*):Expression
		{
			var exp:Expression = new ConstExpression(value);
			return exp;
		}
 
		protected function CreateMethodExpression(method:String, arguments:Array):Expression
		{
			var exp:Expression = new MethodExpression(method, arguments);
			return exp;
 
		}
 
		protected function CreateValueExpression(expression:String):Expression
		{
			var exp:Expression = new ConstExpression(expression);
			return exp;
		}
		//================================
		public final function Invoke(expression:String, context:*):Object
		{
			var result:Object = expression;
			if(IsExpression(expression))
			{
				var str:String = expression.substr(expression.indexOf("=")+1);
				result = str;
				try
				{
					var exp:Expression = Interpret(CreateContext(str));
					if(exp)
						result = exp.Execute(context);
				}
				catch(e:Error)
				{
					result = null;
					if(_HandleError)
					{
						trace(e.getStackTrace());
						result = e.getStackTrace();
					}
					else
						throw e;
				}
			}
			return result;
		}
		
		private function Interpret(context:ExpressionInterpreterContext):Expression
		{
			var stack:Array = new Array();//运算符堆栈
			//var exp:Expression;
			var hasLeft:Boolean = false;//是否有左参数，用于判断一元运算符
			var interpret:Boolean = false;//是否成功解析
			var opTop:String;
			while(context.CanInterpret)
			{
				interpret = InterpretStringExpression(context, context.Start);
				if(!interpret)
					interpret = InterpretMethodExpression(context, context.Start);
				hasLeft = hasLeft || interpret;
				if(!interpret)
				{
					var operator:String = InterpretOperator(context.Expression, context.Start);
					if(operator)
					{
						//遇到运算符，输出值表达式到上下文
						if(context.Start>0)
						{
							interpret = InterpretValueExpression(context, context.Start);
							hasLeft = hasLeft || interpret;
						}
						if(operator == "(")
						{
							stack.push(operator);
						}
						else if(operator == ")")
						{
							var opStart:String = "(";
							while(stack.length)
							{
								opTop = stack.pop();
								if(opTop == opStart)//找到开头括号跳出循环
									break;
								context.Push(opTop);
							}
						}
						else
						{
							//修正运算符：正负号，空格，大小写
							var opNew:String = OperatorUtil.AdjustOperator(operator, hasLeft);
							hasLeft = false;
							if(stack.length)
							{
								while(stack.length)
								{
									opTop = stack.pop();
									if(opTop == "(" || GetPriority(opNew) > GetPriority(opTop))
									{
										stack.push(opTop);
										stack.push(opNew);
										break;
									}
									else
									{
										context.Push(opTop);
										if(stack.length == 0)
										{
											stack.push(opNew);
											break;
										}
									}
								}
							}
							else
								stack.push(opNew);
						}
						context.AcceptInterpret(operator.length);
					}
					else
					{
						context.Start ++;
					}
				}
			}
			InterpretValueExpression(context, context.Expression.length);
			
			while(stack.length)
			{
				opTop = stack.pop();
				context.Push(opTop);
			}
			
			if(Trace)
			{
				trace("[输出堆栈]:");
				for(var i:int=0; i<context.Length; i++)
				{
					trace(i+":\t"+context.GetItemAt(i));
				}
			}
			//解析后缀表达式
			return Calculate(context);
		}
		
		private function Calculate(context:ExpressionInterpreterContext):Expression
		{
			var result:Expression;
			var stack:Array = new Array();//运算堆栈
			
			for(var i:int=0; i<context.Length; i++)
			{
				var item:Object = context.GetItemAt(i);
				if(item is Expression)
				{
					stack.push(item);
				}
				else//运算符
				{
					var op:String = item.toString();					
					var exp:Expression;
					if(OperatorUtil.IsSingleOperator(op))
					{
						exp = stack.pop() as Expression;
						if(!exp)
						{
							throw new Error(op + "运算符参数不正确。\n表达式：" + context.Expression);
						}
						exp = CreateSingleExpression(op, exp);
					}
					else
					{
						var right:Expression, left:Expression;
						right = stack.pop() as Expression;
						left = stack.pop() as Expression;
						if(!left)
						{
							throw new Error(op + "运算符左参数不正确。\n表达式：" + context.Expression);
						}
						if(!right)
						{
							throw new Error(op + "运算符右参数不正确。\n表达式：" + context.Expression);
						}
						exp = CreateBinaryExpression(op, left, right);
					}
					stack.push(exp);
				}
			}
			
			if(stack.length)
			{
				result = stack.pop() as Expression;
			}
			while(stack.length)
				stack.pop();
			
			return result;
			
		}
		private static function GetPriority(op:String):int
		{
			return OperatorUtil.OperatorsPriority[op];
		}
		private function InterpretOperator(exp:String, start:int):String
		{
			var op:String = OperatorUtil.InterpretOperator(exp, start);
			return ValidateOperator(op, exp, start) ? op : null;
		}
		private function InterpretStringOperator(exp:String, start:int):String
		{
			var op:String = OperatorUtil.InterpretStringOperator(exp, start);
			return ValidateOperator(op, exp, start) ? op : null;
		}
		/**
		 * 判断运算符合法性方法
		 */
		protected function ValidateOperator(operator:String, exp:String, start:int):Boolean
		{
			return true;
 
		}
 
		/**
		 * 所有支持的方法
		 */ 
		protected function get SupportedMethods():Array
		{
			return _SupportedMethods;
		}
		/**
		 * 字符串方法，用于解析字符串
		 */ 
		protected function get SupportedStringMethods():Array
		{
			return _SupportedStringMethods;
		}
		
		private function IsStringMethod(method:String):Boolean
		{
			if(!method)
				return false;
			for each(var name:String in SupportedStringMethods)
			{
				if(StringUtil.IgnoreCaseCompare(name, method))
					return true;
			}
			return false;
		}
		private static function InterpretMethodName(context:ExpressionInterpreterContext, start:int, methods:Array):String
		{
			var exp:String = context.Expression;
			if(!exp)
				return null;
			exp = context.Expression.substr(start);
			for each(var name:String in methods)
			{
				//判断例子：name(
				if(StringUtil.StartsWidth(exp, name+"("))
					return exp.substr(0, name.length);
			}
			return null;
		}
		private static function GetMethodEndIndex(context:ExpressionInterpreterContext, method:String):int
		{
			context.ResetCounter();
			
			//当双引号为复数，右括号数量等于左括号数量时判断方法结束
			
			var exp:String = context.Expression;
			var length:int = exp.length;
			for(var i:int=method.length; i<length; i++)
			{
				var char:String = exp.charAt(i);
				if(char == "\"")
					context.DoubleQuotationCount ++ ;
				else if(char == "(" && context.DoubleQuotationCount%2==0)
					context.LeftBracketCount++;
					
				else if(char == ")" && context.DoubleQuotationCount%2==0)
				{
					context.RightBracketCount++;
					if (context.LeftBracketCount == context.RightBracketCount)
						return i;
				}
			}
			throw new Error(method +"函数非正常结束。" + "\n表达式："+context.Expression);
			
//			var leftBracket:int = 0;//左括号数量
//			var rightBracket:int = 0;//右括号数量
//			//当右括号数量等于左括号数量时判断方法结束
			
//			var exp:String = context.Expression;
//			var length:int = exp.length;
//			for(var i:int=method.length; i<length; i++)
//			{
//				var char:String = exp.charAt(i);
//				if(char == "(")
//					leftBracket++;
//				else if(char == ")")
//				{
//					rightBracket++;
//					if(rightBracket == leftBracket) 
//						return i;
//				}
//			}
//			throw new Error(method +"函数非正常结束。" + "\n表达式："+context.Expression);
		}
		private function InterpretStringExpression(context:ExpressionInterpreterContext, start:int):Boolean
		{
			var exp:Expression;
			var end:int;
			var method:String = InterpretMethodName(context, start, SupportedStringMethods);
			/*
			* 判断是否字符串函数，字符串函数的内部不会进行参数解析。
			* 可用来生成特殊的字符，例如：' '，','，运算符等，内部的'('和')'必须成对出现。
			*/ 
			if(IsStringMethod(method))
			{
				context.AcceptInterpret(start);//方法前
				end = GetMethodEndIndex(context, method);//方法右括号前
				var argsExp:String = context.Expression.substring(method.length+1, end);//去掉方法括号的内容
				var args:Array;
				args = new Array();
				args.push(CreateConstExpression(argsExp));
 				exp = CreateMethodExpression(method, args);
				context.Push(exp);
				context.AcceptInterpret(end+1);//方法右括号后
				return true;
			}
			else
			{
				var operator:String = InterpretStringOperator(context.Expression, context.Start);
				if(operator)
				{
					end = context.Expression.indexOf(operator, context.Start + 1);
					if(end<0)
						throw new Error("缺少"+operator +"符号，字符串非正常结束。" + "\n表达式："+context.Expression);
					var value:String = context.Expression.substring(context.Start+operator.length, end);
					exp = CreateConstExpression(value);
					context.Push(exp);
					context.AcceptInterpret(end+operator.length);//字符串结束
					return true;
				}
			}
			
			return false;
		}
		
		private function InterpretMethodExpression(context:ExpressionInterpreterContext, start:int):Boolean
		{
			var method:String = InterpretMethodName(context, start, SupportedMethods);
			if(method)
			{
				context.AcceptInterpret(start);//方法前
				
				var end:int = GetMethodEndIndex(context, method);//方法右括号前
				var argsExp:String = context.Expression.substring(method.length+1, end);//去掉方法括号的内容
				var args:Array;
				/*
				* 判断是否字符串函数，字符串函数的内部不会进行参数解析。
				* 可用来生成特殊的字符，例如：' '，','，运算符等，内部的'('和')'必须成对出现。
				*/ 
				if(IsStringMethod(method))
				{
					args = new Array();
					args.push(CreateConstExpression(argsExp));
				}
				else
					args = InterpretMethodArguments(argsExp, context);
				
				var exp:Expression = CreateMethodExpression(method, args);
				context.Push(exp);
				context.AcceptInterpret(end+1);//方法右括号后
				return true;
			}
			return false;
		}
		
		private function InterpretMethodArguments(expression:String, context:ExpressionInterpreterContext):Array
		{
			var args:Array = new Array();
			var exp:Expression;
			var argExp:String;
			var argContext:ExpressionInterpreterContext;
			context.ResetCounter();
			
			var start:int = 0;
			var length:int = expression.length;
			for(var i:int=0; i<length; i++)
			{
				var char:String = expression.charAt(i);
				if(char == "\"")
					context.DoubleQuotationCount ++ ;
				else if(char == "(" && context.DoubleQuotationCount%2==0)
					context.LeftBracketCount++;
				else if(char == ")" && context.DoubleQuotationCount%2==0)
					context.RightBracketCount++;				
					
				else if(char == "," && context.LeftBracketCount == context.RightBracketCount &&
					context.DoubleQuotationCount % 2 == 0)
				{
					
					argExp = expression.substring(start, i);
					argContext = CreateContext(argExp);
					exp = Interpret(argContext);
					if(!exp)
						exp = CreateConstExpression("");//参数表达式为空时，创建空字符串常量表达式
					args.push(exp);
					
					start = i+1;
				}
			}

			if(start < length)
			{
				argExp = expression.substring(start);
				argContext = CreateContext(argExp);
				exp = Interpret(argContext);
				if(!exp)
					exp = CreateConstExpression("");//参数表达式为空时，创建空字符串常量表达式
				args.push(exp);
			}
			return args;
			
			//			var args:Array = new Array();
			//			var argExp:String;
			//			var argContext:ExpressionInterpreterContext;
			//			var leftBracket:int = 0;//左括号数量
			//			var rightBracket:int = 0;//右括号数量
			//			var start:int = 0;
			//			var length:int = expression.length;
			//			for(var i:int=0; i<length; i++)
			//			{
			//				var char:String = expression.charAt(i);
			//				if(char == "(")
			//					leftBracket++;
			//				else if(char == ")")
			//					rightBracket++;
			//				else if(char == ",")
			//				{
			//					if(leftBracket == rightBracket)
			//					{
			//						argExp = expression.substring(start, i);
			//						argContext = CreateContext(argExp);
			//						args.push(Interpret(argContext));
			//						
			//						start = i+1;
			//					}
			//				}
			//			}
//			if(start < length)
//			{
//				argExp = expression.substring(start);
//				argContext = CreateContext(argExp);
//				args.push(Interpret(argContext));
//			}
//			return args;
		}
		
		private function InterpretValueExpression(context:ExpressionInterpreterContext, end:int):Boolean
		{
			var result:Boolean = false;
			var expression:String = context.Expression.substring(0, end);
			expression = StringUtil.Trim(expression);
			if(expression)
			{
				var exp:Expression;
				var num:Number = Number(expression);
				if(!isNaN(num))
					exp = CreateConstExpression(num);
				else
					exp = CreateValueExpression(expression);
				context.Push(exp);
				result = true;
			}
			context.AcceptInterpret(end);
			return result;
		}


		
	}
}