/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


常量表达式

*/
package hlib
{
	internal class ConstExpression extends Expression
	{
		private var _Value:*;
		public function ConstExpression(value:*)
		{
			super();
			_Value = value;
		}

		public override function Execute(context:*):*
		{
			return _Value;
		}
		public function toString():String
		{
			return _Value;
		}
	}
}