/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


二元运算符表达式，支持调用：

【算术运算】
加：		exp1 + exp2
减：		exp1 - exp2
乘：		exp1 * exp2
除：		exp1 / exp2
取余：	exp1 % exp2
幂运算：	exp1 ^ exp2

【比较运算】
等于：		exp1 == exp2
不等于：		exp1 != exp2
大于：		exp1 > exp2
大于等于：	exp1 >= exp2
小于：		exp1 < exp2
小于等于：	exp1 <= exp2

【逻辑运算】
并且：		exp1 && exp2
或者：		exp1 || exp2

*/

package hlib
{
	import hlib.MathUtil;

	internal class BinaryExpression extends Expression
	{
		private var _Operator:String;
		private var _Left:Expression;
		private var _Right:Expression;
		protected override function Disposing():void
		{
			_Left.Dispose();
			_Right.Dispose();
			_Left = null;
			_Right = null;
			super.Disposing();
		}
		public function BinaryExpression(operator:String, left:Expression, right:Expression)
		{
			super();
			_Operator = operator;
			_Left = left;
			_Right = right;
		}
		
		private static function ToNum(value:Object):Number
		{
			return Number(Number(value).toFixed(10));
		}

		/**
		 * 
		 * 该表达式没有使用contex参数
		 * 
		 */
		public override function Execute(context:*):*
		{
			var left:Number, right:Number, value:Number;
			switch(_Operator)
			{
				case "+":
					return ToNum(Number(_Left.Execute(context)) + Number(_Right.Execute(context)));
				case "-":
					/*
					 35.84 - 13.86- 21.98显示科学计数 
					*/
					return ToNum(Number(_Left.Execute(context)) - Number(_Right.Execute(context)));
				case "*":
					/*
					乘法小数位异常：3 * 21.2 = 63.599999999999994
					*/					
					left = Number(_Left.Execute(context));
					right = Number(_Right.Execute(context));
					value = left * right;
					var d1:int = MathUtil.LenDigit(left) + MathUtil.LenDigit(right);
					if(d1!=MathUtil.LenDigit(value))
						value = MathUtil.Round(value, d1);
					return ToNum(value);
				case "/":
					return ToNum(Number(_Left.Execute(context)) / Number(_Right.Execute(context)));
				case "%":
					return ToNum(Number(_Left.Execute(context)) % Number(_Right.Execute(context)));
				case "^":				
					return ToNum(Math.pow(Number(_Left.Execute(context)), Number(_Right.Execute(context))));				
				
				case "==":
					return Number(_Left.Execute(context) == _Right.Execute(context));
				case "!=":
					return Number(_Left.Execute(context) != _Right.Execute(context));
					
				case ">":
					return Number(Number(_Left.Execute(context)) > Number(_Right.Execute(context)));					
				case ">=":
					return Number(Number(_Left.Execute(context)) >= Number(_Right.Execute(context)));
				case "<":
					return Number(Number(_Left.Execute(context)) < Number(_Right.Execute(context)));					
				case "<=":
					return Number(Number(_Left.Execute(context)) <= Number(_Right.Execute(context)));
					
				case "&&":
					return Number(Parser.ParseBoolean(_Left.Execute(context)) && Parser.ParseBoolean(_Right.Execute(context)));
				case "||":
					return Number(Parser.ParseBoolean(_Left.Execute(context)) || Parser.ParseBoolean(_Right.Execute(context)));

			}

			return 0;
		}

		
		public function toString():String
		{
			return "Binary{" +_Operator+", "+_Left+", "+_Right+"}";
		}
	}
}