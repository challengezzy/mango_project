/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


一元运算符表达式，支持调用：

【一元运算】
正：		+ exp
负：		- exp
取非：	! exp
*/

package hlib
{
	internal class SingleExpression extends Expression
	{
		private var _Operator:String;
		private var _Exp:Expression;
 
		protected override function Disposing():void
		{
			_Exp.Dispose();
			_Exp = null;
			super.Disposing();
		}
		public function SingleExpression(operator:String, exp:Expression)
		{
			super();
			_Operator = operator;
			_Exp = exp;
		}
 
		/**
		 * 
		 * 该表达式没有使用contex参数
		 * 
		 */
		public override function Execute(context:*):*
		{
			var value:Object = _Exp.Execute(context);
			switch(_Operator)
			{
				case "+N":
					return Number(value);
				case "-N":
					return Number(value) * -1;
					case "!":
						return Number(!Parser.ParseBoolean(value));
			}
			return value;
		}

		public function toString():String
		{
			return "Single{" +_Operator+", "+_Exp+"}";
		}
	}
}