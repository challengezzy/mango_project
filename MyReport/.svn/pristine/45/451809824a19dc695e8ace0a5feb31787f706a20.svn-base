/*
Copyright (c) 2010 - 2011, Hunk.Cai
All rights reserved.

创建人：Hunk.Cai
修改人：


表达式解析上下文。


*/

package hlib
{
 
	internal final class ExpressionInterpreterContext implements IDispose
	{
		public var Expression:String;//当前表达式
		public var Start:int;//当前表达式的解析索引
 		private var _Stack:Array;
		// 左括号计数, (
		public var LeftBracketCount:int = 0;
		// 右括号计数, )
		public var RightBracketCount:int = 0;
		// 双引号计数, "
		public var DoubleQuotationCount:int = 0;
		//================IDispose====================
		private var _Disposed:Boolean = false;	
		public function Dispose():void
		{
			if(_Disposed) return;
			_Disposed = true;
			DisposeUtil.Dispose(_Stack);
			_Stack = null;
			Expression = null;

		}
		public function ExpressionInterpreterContext(expression:String)
		{
			Expression = expression;
 			Start = 0;
			_Stack = new Array();
		}
		public function get Length():uint
		{
			return _Stack.length;
		}
 		public function Push(value:Object):void
		{
			_Stack.push(value);
		}
		public function Pop():Object
		{
			return _Stack.pop();
		}
		public function GetItemAt(index:int):Object
		{
			return _Stack[index];
		}
		public function get CanInterpret():Boolean
		{
			return Start < Expression.length;
		}
		//裁剪表达式
		public function AcceptInterpret(start:int):void
		{
			if(start>0)
			{
				Expression = Expression.substr(start);
				Start = 0;
				//trace("[当前表达式]"+Expression);
			}
		}
		public function ResetCounter():void
		{
			LeftBracketCount = 0;
			RightBracketCount = 0;
			DoubleQuotationCount = 0;
			
		}
	}
}